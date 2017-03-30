package com.water.crawl.work;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.water.crawl.core.CrawlAction;
import com.water.crawl.core.CrawlRule;
import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.core.factory.impl.ArticleFactory;
import com.water.crawl.db.model.Article;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.db.model.ITLib;
import com.water.crawl.db.service.article.IBMArticleService;
import com.water.crawl.db.service.article.ICSDNArticleService;
import com.water.crawl.db.service.article.IOSCHINAArticleService;
import com.water.crawl.utils.HttpRequestTool;
import com.water.crawl.utils.StringUtil;
import com.water.es.api.Service.IArticleService;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 抓取文章的定时器类
 * Created by zhangmiaojie on 2017/2/28.
 */
public class FetchArticleTask {

    @Resource
    private IBMArticleService ibmArticleService;

    @Resource
    private ICSDNArticleService icsdnArticleService;

    @Resource
    private IOSCHINAArticleService ioschinaArticleService;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    private Gson gson = new Gson();

    /**
     * 抓取IBM开发者社区的文章
     */
    public void fetchIBMArticles() {
        System.out.println("抓取IBM开发者社区的文章--------------------------------------------------");
        List<ITArticle> articles = new ArrayList<ITArticle>();
        List<String> fetchFailurelinks = new ArrayList<String>();
        Set<String> articleCategoryUrls = new HashSet<String>();

        articleCategoryUrls = ibmArticleService.getIBMArticleCategoryUrl();
        System.out.println("开始抓取IBM开发者社区各个模块的文章，" + articleCategoryUrls.size());
        IArticleFactory articleFactory = ArticleFactory.build(ArticleFactory.FactoryConfig.IBM);
        for (String url : articleCategoryUrls) {
            System.out.println("开始抓取 ： " + url);

            Map<String, String> paramMap = new HashMap<>();
            for (int i = 1; true; i++) {//循环获取所有模块下每个页面的文章
                paramMap.put("start", String.valueOf(i + (i - 1) * 100));
                paramMap.put("end", String.valueOf(i * 100));
                String result = (String) HttpRequestTool.postRequest(url, paramMap, false);
                if (StringUtils.isBlank(result)) break;
                List<String> linkList = ibmArticleService.getAllArticleLink(result);
                for (String link : linkList) {
                    System.out.println("开始抓取文章--" + link);
                    CrawlAction crawlAction = new CrawlAction("IBM","Article",link) {
                        @Override
                        public void action(JsonObject obj) {
                            String json = obj.toString();
                            Type type = new TypeToken<ITArticle>() {}.getType();
                            ITArticle article = gson.fromJson(json, type);
                            if (article != null) {
                                article.setId(UUID.randomUUID().toString());
                                ibmArticleService.addArticle(article);
                                com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
                                BeanUtils.copyProperties(article, esArticle);
                                esArticleService.addArticle(esArticle);
                                articles.add(article);
                            } else {
                                fetchFailurelinks.add(link);
                            }
                        }
                    };
                    crawlAction.work();
                }
                articleFactory.printExecuteInfo();
            }
        }
    }

    /**
     * 抓取CSDN知识库下所有的文章
     */
    public void fetchCSDNArticleLib() {
        Gson gson = new Gson();
        CrawlAction crawlAction = new CrawlAction("IBM","Article","https://www.ibm.com/developerworks/cn/web/wa-implement-a-single-page-application-with-angular2/index.html") {
            @Override
            public void action(JsonObject obj) {
                System.out.println();
//                Type type = new TypeToken<Article>>() {}.getType();
//                crawlRuleList = gson.fromJson(json, listType);
            }
        };
        crawlAction.work();


//        int count = 1;
//        Map<String, String> queryMap = new HashMap<>();
//        List<ITLib> itLibList = icsdnArticleService.getAllLibCategory();
//        Set<String> linkSet = new HashSet<>();
//        IArticleFactory articleFactory = ArticleFactory.build(ArticleFactory.FactoryConfig.CSDN);
//        for (ITLib itLib : itLibList) { //遍历每一个知识点
//            String html = (String) HttpRequestTool.getRequest(itLib.getUrl(), false);
//            Document doc = Jsoup.parse(html);
//            Elements elements = doc.getElementsByTag("a");
//            for (Element element : elements) { //获取每一个知识点下面的子知识点
//                if (element.attr("href").contains("node/") || element.attr("href").contains("knowledge/")) {
//                    String link = element.attr("href");
//                    linkSet.add(link);
//                }
//            }
//
//            for (String link : linkSet) {
//                String tmpLink = link;
//                for (int i = 1; i <= count; i++) {
//                    tmpLink += "?page=" + i;
//                    queryMap.put("page", String.valueOf(i));
//                    html = (String) HttpRequestTool.getRequest(tmpLink, false);
//                    if (StringUtils.isNotBlank(html)) {
//                        doc = Jsoup.parse(html);
//                        Elements elements1 = doc.select(".dynamicollect .csdn-tracking-statistics a");
//                        if (elements1 == null || elements1.size() == 0) break;
//                        if (StringUtils.isNotBlank(doc.select("#totalPage").attr("value2"))) {
//                            count = Integer.valueOf(doc.select("#totalPage").attr("value2"));
//                        }
//
//                        for (Element ele : elements1) {
//                            String articleLink = ele.attr("href");
//                            html = (String) HttpRequestTool.getRequest(articleLink);
//                            if (StringUtils.isNotBlank(html)) {
//                                doc = Jsoup.parse(html);
//                                ITArticle article = articleFactory.createArticle(doc, articleLink);
//                                if (article != null) {
//                                    icsdnArticleService.addArticle(article);
//                                    com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
//                                    BeanUtils.copyProperties(article, esArticle);
//                                    esArticleService.addArticle(esArticle);
//                                } else {
//                                    System.out.println(articleLink + "连接解析有问题！！！！-------------------------------");
//                                }
//                            }
//                        }
//                    }
//                }
//                count = 1; //重置为第一页
//            }
//        }
    }

    /**
     * 抓取开源中国社区下的文章
     */
    public void fetchOSCHINAArticle() {
        String base_url = "https://www.oschina.net/blog";                                   //第一次进来不是ajax请求
        String ajax_url = "https://www.oschina.net/action/ajax/get_more_recommend_blog";    //第二次进来是ajax请求
        Set<String> linkSet = ioschinaArticleService.getAllCategory();
        for (String link : linkSet) {
            Map<String, String> queryParams = StringUtil.getParamWithUrl(link);
            String html = "";
            for (int i = 1; ; i++) {
                if (i == 1) {
                    html = (String) HttpRequestTool.postRequest(base_url, queryParams, false);
                } else {
                    queryParams.put("q", String.valueOf(i));
                    html = (String) HttpRequestTool.postRequest(ajax_url, queryParams, false);
                }

                Document doc = Jsoup.parse(html);
                Elements articleLinks = doc.select(".blog-title-link");
                for (Element articleLinkEle : articleLinks) {
                    String articleLink = articleLinkEle.attr("href");
                    System.out.println(articleLink);
                    html = (String) HttpRequestTool.getRequest(articleLink, false);
                    doc = Jsoup.parse(html);
                    //创建文章
                }
            }
        }
    }

}
