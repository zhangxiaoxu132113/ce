package com.water.test;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import com.water.crawl.core.WaterHtmlParse;
import com.water.crawl.utils.http.HttpRequestTool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zhangmiaojie on 2017/2/15.
 */
public class TestUid {
    private ThreadLocal<String> url = new ThreadLocal(){
        @Override
        protected String initialValue() {
            return "";
        }
    };

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }

    public static void main(String[] args) {
//        System.out.println(randomUid());
        fetchCourseTopics();
    }

    /**
     * 随机生成10位的用户ID
     *
     * @return
     */
    public static long randomUid() {
        long max = 9999999999L;
        long min = 1000000000L;
        Random random = new Random();
        long id = random.nextInt((int) max) % (max - min + 1) + min;
        return Math.abs(id);
    }

    public void fetchweightWebsite() throws IOException, DocumentException {
        SAXReader reader = new SAXReader();
        org.dom4j.Document xmlDoc = reader.read(new File("E:\\weightWebsite.xml"));
        org.dom4j.Element rootEle = xmlDoc.getRootElement();

        String url = "http://top.chinaz.com/hangye/index_yiliao_br%s.html";
        String requestUrl = "";
        String htmlPage = "";
        Document doc = null;
        List<String> serverList = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            if (i == 1) {
                requestUrl = String.format(url, "");
            } else {
                requestUrl = String.format(url, "_" + i);
            }
            System.out.println("开始抓取:" + requestUrl);
            htmlPage = (String) HttpRequestTool.getRequest(requestUrl, false);
            if (StringUtils.isNotBlank(htmlPage)) {
                doc = Jsoup.parse(htmlPage);
                Elements itemsEle = doc.select(".listCentent li");
                for (Element itemEle : itemsEle) {
                    String weightUrl = itemEle.select(".RtCData img").attr("src");
//                   System.out.println(weightUrl);
                    weightUrl = weightUrl.substring(weightUrl.lastIndexOf("/") + 1, weightUrl.lastIndexOf("."));
                    int weight = Integer.parseInt(weightUrl);
                    if (weight >= 5) {
                        org.dom4j.Element produceEle = DocumentHelper.createElement("produce");

                        org.dom4j.Element nameEle = DocumentHelper.createElement("name");
                        String produceName = itemEle.select(".rightTxtHead a").text();
                        nameEle.setText(produceName);
                        produceEle.add(nameEle);

                        org.dom4j.Element serverEle = DocumentHelper.createElement("server");
                        String serverName = itemEle.select(".col-gray").text();
                        serverEle.setText(serverName);
                        produceEle.add(serverEle);

                        org.dom4j.Element weightEle = DocumentHelper.createElement("weight");
                        weightEle.setText(weight + "");
                        produceEle.add(weightEle);

                        rootEle.add(produceEle);
                    }
                }
            } else {
                System.out.println("抓取不到数据 -> " + requestUrl);
            }
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream(new File("E:\\weightWebsite1.xml")), "utf-8"), format);
        writer.write(xmlDoc);
        writer.close();
    }

    public static void fetchCourseTopics() {
        String requestUrl = "http://www.yiibai.com/";
        String htmlPage = (String) HttpRequestTool.getRequest(requestUrl, false);
        if (htmlPage == null) return;
        Document doc = Jsoup.parse(htmlPage);
        Elements itemElements = doc.select(".item");
        for (Element item : itemElements) {
            String parentNode = item.select("h2").text();
            System.out.println("教程专题：" + parentNode);
            Elements sonElements = item.select(".blogroll a");
            for (Element sonEle : sonElements) {
                String sonNode = sonEle.text();
                String description = sonEle.attr("title");
                String url = sonEle.absUrl("href");
                if (StringUtils.isNotBlank(url)) {
                    System.out.println("开始抓取 -> " + sonNode + " - > " + url);
                    htmlPage = (String) HttpRequestTool.getRequest(url, false);
                    if (htmlPage == null) continue;
                    Document sonDoc = Jsoup.parse(htmlPage);
                    Elements menus = sonDoc.select(".pagemenu li a");
                    for (Element oneItem : menus) {
                        String title = oneItem.text();
                        String articleUrl = oneItem.absUrl("href");
                        System.out.println("标题:" + title + " | 链接=" + articleUrl);
                    }
                }
                System.out.println(sonNode + ":" + description);
            }
            System.out.println("---------------------------------------------------------------------------------");
        }
    }

//    public void test00() {
//        public List<newsinfo> searcher2(String key, String indexId, String type) {
//            List<newsinfo> newsInfos= new ArrayList<newsinfo>();
//            try {
//                // 创建查询索引,参数productindex表示要查询的索引库为productindex
//                SearchRequestBuilder searchRequestBuilder = client
//                        .prepareSearch(indexId);
//
//                // 设置查询索引类型,setTypes("productType1", "productType2","productType3");
//                // 用来设定在多个类型中搜索
//                searchRequestBuilder.setTypes(type);
//                // 设置查询类型 1.SearchType.DFS_QUERY_THEN_FETCH = 精确查询 2.SearchType.SCAN
//                // = 扫描查询,无序
//                searchRequestBuilder.setSearchType(SearchType.DFS_QUERY_THEN_FETCH);
//                // 设置查询关键词
////          searchRequestBuilder
////                  .setQuery(QueryBuilders.boolQuery().should(QueryBuilders.termQuery("title", key))
////                          .should(QueryBuilders.termQuery("content", key)));
//                QueryStringQueryBuilder queryBuilder = new QueryStringQueryBuilder(key);
//                queryBuilder.analyzer("ik_smart");
//                queryBuilder.field("title").field("content");
//                searchRequestBuilder.setQuery(queryBuilder);
//                // 分页应用
//                searchRequestBuilder.setFrom(1).setSize(3000);
//
//                // 设置是否按查询匹配度排序
//                searchRequestBuilder.setExplain(true);
//                // 按照字段排序
//                searchRequestBuilder.addSort("publish_time", SortOrder.DESC);
//                // 设置高亮显示
//                searchRequestBuilder.addHighlightedField("title");
//                searchRequestBuilder.addHighlightedField("content");
//                searchRequestBuilder
//                        .setHighlighterPreTags("<span color:="">");
//                searchRequestBuilder.setHighlighterPostTags("");
////          searchRequestBuilder.setHighlighterPreTags("<em>");
////          searchRequestBuilder.setHighlighterPostTags("<em>");
//                // 执行搜索,返回搜索响应信息
//                SearchResponse response = searchRequestBuilder.execute()
//                        .actionGet();
//
//                // 获取搜索的文档结果
//                SearchHits searchHits = response.getHits();
//                SearchHit[] hits = searchHits.getHits();
//                // ObjectMapper mapper = new ObjectMapper();
//                for (int i = 0; i < hits.length; i++) {
//                    SearchHit hit = hits[i];
//                    // 将文档中的每一个对象转换json串值
//                    String json = hit.getSourceAsString();
//                    // 将json串值转换成对应的实体对象
//                    NewsInfo newsInfo = JsonUtils
//                            .readToObject(json, NewsInfo.class);
//                    // 获取对应的高亮域
//                    Map<string, highlightfield=""> result = hit.highlightFields();
//                    // 从设定的高亮域中取得指定域
//                    HighlightField titleField = result.get("title");
//                    if (titleField !=null) {
//                        // 取得定义的高亮标签
//                        Text[] titleTexts = titleField.fragments();
//                        // 为title串值增加自定义的高亮标签
//                        String title = "";
//                        for (Text text : titleTexts) {
//                            title += text;
//                        }
//                        newsInfo.setTitle(title);
//                    }
//                    // 从设定的高亮域中取得指定域
//                    HighlightField contentField = result.get("content");
//                    if (contentField !=null) {
//                        // 取得定义的高亮标签
//                        Text[] contentTexts = contentField.fragments();
//                        // 为title串值增加自定义的高亮标签
//                        String content = "";
//                        for (Text text : contentTexts) {
//                            content += text;
//                        }
//                        // 将追加了高亮标签的串值重新填充到对应的对象
//                        newsInfo.setContent(content);
//                    }
//                    newsInfos.add(newsInfo);
////              System.out.println(newsInfo.toString());
//                    // 打印高亮标签追加完成后的实体对象
//                }
//                // 防止出现：远程主机强迫关闭了一个现有的连接
////          Thread.sleep(10000);
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//            return newsInfos;
//        }
//    }
}
