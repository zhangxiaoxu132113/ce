package com.water.crawl.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.google.protobuf.InvalidProtocolBufferException;
import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.core.factory.impl.ArticleFactory;
import com.water.crawl.core.repositories.ArticleRepositories;
import com.water.crawl.db.dao.ITArticleMapper;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.db.model.dto.CrawlKeyword;
import com.water.crawl.utils.ElasticSearchUtils;
import com.water.crawl.utils.ElasticsearchExtTemplate;
import com.water.crawl.utils.HttpRequestTool;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@RestController
public class TestController {


    @Resource
    private ArticleRepositories articleRepositories;

    @Resource
    private CacheManager cacheManager;

    @Resource
    private ITArticleMapper articleMapper;

    @RequestMapping(value = "/test")
    public String test() throws InvalidProtocolBufferException, IllegalAccessException, InstantiationException {
        List<ITArticle> articles = new ArrayList<ITArticle>();
        List<String> fetchFailurelinks = new ArrayList<String>();
        Set<String> articleCategoryUrls = new HashSet<String>();
        articleCategoryUrls.add("http://www.ibm.com/developerworks/cn/views/java/libraryview.jsp");
        articleCategoryUrls.add("http://www.ibm.com/developerworks/cn/views/web/libraryview.jsp");
        System.out.println("开始抓取IBM开发者社区各个模块的文章，" + articleCategoryUrls.size());
        IArticleFactory articleFactory = ArticleFactory.build(ArticleFactory.FactoryConfig.IBM);
        for (String url : articleCategoryUrls) {
            System.out.println("开始抓取 ： " + url);
            Map<String, String> paramMap = new HashMap<String, String>();
            for (int i = 1; true; i++) {//循环获取所有模块下每个页面的文章
                paramMap.put("start", String.valueOf(i + (i - 1) * 100));
                paramMap.put("end", String.valueOf(i * 100));
                String result = (String) HttpRequestTool.postRequest(url, paramMap, false);
                if (StringUtils.isBlank(result)) break;
                List<String> linkList = getAllArticleLink(result);
                for (String link : linkList) {
                    System.out.println("开始抓取文章--" + link);
                    String html = (String) HttpRequestTool.getRequest(link);
                    if (StringUtils.isNotBlank(html)) {
                        Document doc = Jsoup.parse(html);
                        ITArticle article = articleFactory.createArticle(doc, link);
                        if (article != null) {
                            articleMapper.insert(article);
                            ElasticSearchUtils.addDocument(JSONObject.toJSONString(article), "blog", "article", article.getId());
                            articles.add(article);
                        } else {
                            fetchFailurelinks.add(link);
                        }
                    }
                }
                articleFactory.printExecuteInfo();

            }
        }
        return "HelleWorld";
    }

    public String testRedis() throws InvalidProtocolBufferException, IllegalAccessException, InstantiationException {
        ShardedJedis redis = cacheManager.getShardedJedis();
        List<ProtoEntity> keywordList = new ArrayList<ProtoEntity>();
        CrawlKeyword keyword = new CrawlKeyword();
        keyword.setId("132113");
        keyword.setKeyword("mrwater");
        keyword.setPv("12");
        keywordList.add(keyword);
        redis.lpush("baidu_queue".getBytes(),keyword.toByteArray());


        List<byte[]> bytes = redis.lrange("baidu_queue".getBytes(), 0, -1);
        for (byte[] data : bytes) {
            keyword = (CrawlKeyword)ProtoEntity.parseFrom(keyword,data);
            System.out.println(keyword.getKeyword());
        }
        return "HelleWorld";
    }

    /**
     * 获取当前页面的所有文章链接
     *
     * @param html 内容
     * @return List<String>
     */
    public static List<String> getAllArticleLink(String html) {
        List<String> links = new ArrayList<String>();
        Document doc = Jsoup.parse(html);
        Element articleListEle = doc.select("table.ibm-data-table tbody").get(0);
        if (articleListEle != null) {
            Elements linkEles = articleListEle.getElementsByTag("a");
            for (Element linkEle : linkEles) {
                links.add(linkEle.attr("abs:href"));
            }
        }
        return links;
    }

    /**
     * 获取IBM开发者社区所有模块的分类
     *
     * @return Set<String>
     */
    public static Set<String> getIBMArticleCategoryUrl() {
        Set<String> linkList = null;
        String result = (String) HttpRequestTool.getRequest("https://www.ibm.com/developerworks/cn/topics/");
        if (StringUtils.isNotBlank(result)) {
            Document doc = Jsoup.parse(result);
            Elements links = doc.select("#ibm-content-main .ibm-columns").get(0).select("a[href]");
            if (links != null && links.size() > 0) {
                linkList = new HashSet<String>();
                List<String> tmpListLink = new ArrayList<String>();
                for (Element element : links) {
                    tmpListLink.add(element.attr("href"));
                    System.out.println(element.attr("href"));
                }

                for (String url : tmpListLink) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("开始请求 ===========================" + url);
                    result = (String) HttpRequestTool.getRequest(url);
                    if (StringUtils.isNotBlank(result)) doc = Jsoup.parse(result);
                    if (doc.select(".ibm-ind-link a").size() > 0) {
                        Element element = doc.select(".ibm-ind-link a").get(0);
                        if (element.text() != null) {
                            String requestUrl = element.attr("abs:href");
                            if (requestUrl == null) continue;
                            if (requestUrl.startsWith("http://www.ibm.com/")) {
                                if (requestUrl.contains(".jsp")) {
                                    requestUrl = requestUrl.substring(0, requestUrl.indexOf(".jsp") + ".jsp".length());
                                    linkList.add(requestUrl);
                                    System.out.println(requestUrl);
                                }
                            }

                        }
                    }
                }

            }
        }
        return linkList;
    }

    @RequestMapping(value = "/test1")
    public void addDocument() {
        ITArticle article = new ITArticle();
        article.setContent("zhangmiaojie");
        articleRepositories.save(article);
    }
}
