package com.water.crawl.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Map;

/**
 * elasticsearch帮助类
 * Created by zhangmiaojie on 2017/2/17.
 */
public class ElasticSearchUtils {
    private static Log logger = LogFactory.getLog(ElasticSearchUtils.class);
    private static TransportClient client;

    static {
        try {
            Settings settings = org.elasticsearch.common.settings.Settings.builder().put("cluster.name", "my-application").build();
            //创建client
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("127.0.0.1"), 9300));
        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * 添加文档
     *
     * @param objStr 对象
     * @param index  索引
     * @param type   类型
     * @param id     主键
     * @return       String
     */
    public static String addDocument(String objStr, String index, String type, String id) {
        IndexResponse indexResponse = client.prepareIndex(index, type, id).setSource(objStr.getBytes()).get();
        return indexResponse.toString();
    }

    /**
     * 获取文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    主键
     * @return      Map
     */
    public static Map<String, Object> getDocument(String index, String type, String id) {
        GetResponse getResponse = client.prepareGet(index, type, id).get();
        return getResponse.getSource();
    }

    /**
     * 删除文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    主键
     * @return      String
     */
    public static String delDocument(String index, String type, String id) {
        DeleteResponse deleteResponse = client.prepareDelete(index, type, id).get();
        return deleteResponse.toString();
    }

    /**
     * 更新文档
     *
     * @param index 索引
     * @param type  类型
     * @param id    主键
     * @param key   key
     * @param value value
     * @return      String
     */
    public static String updateDocument(String index, String type, String id, String key, String value) {
        try {
            UpdateRequest updateRequest = new UpdateRequest(index, type, id);
            updateRequest.doc(XContentFactory.jsonBuilder().startObject().field(key, value).endObject());
            UpdateResponse updateResponse = client.update(updateRequest).get();
            return updateResponse.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 搜索文档
     *
     * @param queryValue
     */
    public static void searchDocumentByTerm(String queryValue) {
        QueryBuilder queryBuilder = new TermQueryBuilder("content",queryValue);
        SearchResponse searchResponse = client.prepareSearch("blog")
                .setQuery(queryBuilder)
                .setFrom(0)
                .setSize(60)
                .setExplain(true)
                .execute()
                .actionGet();
        SearchHits searchHit = searchResponse.getHits();
        for (int i=0; i<searchHit.totalHits(); i++){
            System.out.println(searchHit.getAt(i).getSource().get("title") + " : " + searchHit.getAt(i).getScore());
//            System.out.println(searchHit.getAt(i).getSource().get("content"));
        }
    }

    public static XContentBuilder createIKMapping(Class cls) {
        XContentBuilder xContentBuilder = null;
        try {
            xContentBuilder = XContentFactory.jsonBuilder();
            xContentBuilder.startObject().startObject("article").startObject("_all").field("analyzer","ik_smart").field("search_analyzer","ik_smart").endObject();
            xContentBuilder.startObject("properties");
            for (Field field : cls.getDeclaredFields()) {
                if (field.getType().getSimpleName().toLowerCase().contains("long")) continue;
                xContentBuilder.startObject(field.getName()).field("type", field.getType().getSimpleName().toLowerCase()).field("analyzer","ik_smart").field("search_analyzer","ik_smart").endObject();
            }
            xContentBuilder.endObject().endObject().endObject();
            System.out.println(xContentBuilder.string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xContentBuilder;
    }

    public static boolean createIndex(Class cls) {
        XContentBuilder xContentBuilder = createIKMapping(cls);
        client.admin().indices()
                .preparePutMapping("blog")
                .setType("article")
                .setSource(xContentBuilder)
                .execute()
                .actionGet();

        return false;
    }

    public static void matchQueryBuilder(String index, String type, String key, String value) {
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery(key, value);
        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setQuery(queryBuilder)
                .execute()
                .actionGet();

        SearchHits hits = searchResponse.getHits();
        for (SearchHit hit : hits) {
            String title = (String) hit.getSource().get("title");
            System.out.println(title);
        }
    }
}
