package com.water.crawl.work;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.water.crawl.core.CrawlAction;
import com.water.crawl.core.WaterHtmlParse;
import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.core.factory.impl.ArticleFactory;
import com.water.crawl.db.dao.ITArticleMapper;
import com.water.crawl.db.dao.ITTagMapper;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.db.model.ITLib;
import com.water.crawl.db.model.ITTag;
import com.water.crawl.db.service.article.IBMArticleService;
import com.water.crawl.db.service.article.ICSDNArticleService;
import com.water.crawl.db.service.article.IOSCHINAArticleService;
import com.water.crawl.utils.Constant;
import com.water.crawl.utils.HTMLUtil;
import com.water.crawl.utils.http.HttpRequestTool;
import com.water.crawl.utils.lang.StringUtil;
import com.water.es.api.Service.IArticleService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private Log log = LogFactory.getLog(FetchArticleTask.class);

    @Resource
    private IBMArticleService ibmArticleService;

    @Resource
    private ICSDNArticleService icsdnArticleService;

    @Resource
    private IOSCHINAArticleService ioschinaArticleService;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    @Resource
    private ITTagMapper tagMapper;

    @Resource
    private ITArticleMapper articleMapper;

    @Resource
    private CacheManager cacheManager;

    private Gson gson = new Gson();

    /**
     * 抓取IBM开发者社区的文章
     */
    public void fetchIBMArticles() {
        log.info("抓取IBM开发者社区的文章--------------------------------------------------");
        int webkey = Constant.Article_ORIGIN.IBM.getIndex();
        int category = Constant.ARTICLE_CATEOGRY.BLOG.getIndex();
        String webDomain = Constant.Article_ORIGIN.IBM.getName();
        CrawlAction crawlAction = new CrawlAction(webDomain, "article") {
            @Override
            public void action(JsonObject obj, String url) {
                String json = obj.toString();
                Type type = new TypeToken<ITArticle>() {
                }.getType();
                ITArticle article = gson.fromJson(json, type);
                if (article != null) {
                    ibmArticleService.consummateArticle(article, webkey, category, url);
//                    com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
//                    BeanUtils.copyProperties(article, esArticle);
                    if (ibmArticleService.addArticle(article) > 0) { //只有文章添加成功的时候，才保存到es中
//                        esArticleService.addArticle(esArticle);
                    }
                }
            }
        };
        Set<String> articleCategoryUrls = new HashSet<>();
        articleCategoryUrls.add("http://www.ibm.com/developerworks/cn/views/web/libraryview.jsp");
        articleCategoryUrls.add("http://www.ibm.com/developerworks/cn/views/global/libraryview.jsp");
        articleCategoryUrls.add("http://www.ibm.com/developerworks/cn/views/rational/libraryview.jsp");
//        Set<String> articleCategoryUrls = ibmArticleService.getIBMArticleCategoryUrl();
        log.info("开始抓取IBM开发者社区各个模块的文章，" + articleCategoryUrls.size());
        for (String url : articleCategoryUrls) {
            log.info("开始抓取 ： " + url);
            String tmplink = url + "?start=%s&end=%s";
            for (int i = 1; ; i++) {//循环获取所有模块下每个页面的文章
                String link = String.format(tmplink, (i + (i - 1) * 100), i * 100);
                String result = (String) HttpRequestTool.getRequest(link, false);
                if (StringUtils.isBlank(result) || result.contains("未找到结果")) break;
                List<String> linkList = ibmArticleService.getAllArticleLink(result);
                for (String link1 : linkList) {
                    crawlAction.setUrl(link1);
                    crawlAction.work();
                }
                try {
                    Thread.sleep(1000 * 20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 抓取CSDN知识库下所有的文章
     */
    public void fetchCSDNArticleLib() {
        String url = "https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=%s";
//        String url = "https://www.oschina.net/action/ajax/get_more_news_list?newsType=project&p=%s";
        String breakFalg = "没有更多内容";
        String requestUrl = "";
        String htmlPage = "";
        Document doc;
        for (int i = 1; ; i++) {
            requestUrl = String.format(url, i);
            htmlPage = (String) HttpRequestTool.getRequest(requestUrl, false);
            if (StringUtils.isBlank(htmlPage)) continue;
            if (htmlPage.contains(breakFalg)) break;
            doc = Jsoup.parse(htmlPage);
            Elements linkElements = doc.select(".title");
            for (Element linkEle : linkElements) {
                String link = linkEle.attr("href");
                if (StringUtils.isNotBlank(link)) {
                    if (!link.startsWith("https")) {
                        link = "https://www.oschina.net" + link;
                    }
                    cacheManager.lpush("zixun_url", link);
                }
            }

        }
        log.info("任务执行完毕");
        CrawlAction crawlAction = new CrawlAction("CSDN", "knowledge_base") {// 创建爬虫类
            @Override
            public void action(JsonObject obj, String targetUrl) {
                String json = obj.toString();
                Type type = new TypeToken<ITArticle>() {
                }.getType();
                ITArticle article = gson.fromJson(json, type);
                if (article != null) {
                    article.setCreateOn(System.currentTimeMillis());
                    article.setDescryptUrl(this.getUrl());
                    com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
                    BeanUtils.copyProperties(article, esArticle);
                    if (ibmArticleService.addArticle(article) > 0) { //只有文章添加成功的时候，才保存到es中
                        esArticleService.addArticle(esArticle);
                    }
                } else {
                    log.warn("解析" + this.getUrl() + "出现问题！");
                }
            }
        };


        int count = 1;
        Map<String, String> queryMap = new HashMap<>();
        List<ITLib> itLibList = icsdnArticleService.getAllLibCategory();
        Set<String> linkSet = new HashSet<>();
        IArticleFactory articleFactory = ArticleFactory.build(ArticleFactory.FactoryConfig.CSDN);
        for (ITLib itLib : itLibList) { //遍历每一个知识点
            String html = (String) HttpRequestTool.getRequest(itLib.getUrl(), false);
            doc = Jsoup.parse(html);
            Elements elements = doc.getElementsByTag("a");
            for (Element element : elements) { //获取每一个知识点下面的子知识点
                if (element.attr("href").contains("node/") || element.attr("href").contains("knowledge/")) {
                    String link = element.attr("href");
                    linkSet.add(link);
                }
            }

            for (String link : linkSet) {
                String tmpLink = link;
                for (int i = 1; i <= count; i++) {
                    tmpLink += "?page=" + i;
                    queryMap.put("page", String.valueOf(i));
                    html = (String) HttpRequestTool.getRequest(tmpLink, false);
                    if (StringUtils.isNotBlank(html)) {
                        doc = Jsoup.parse(html);
                        Elements elements1 = doc.select(".dynamicollect .csdn-tracking-statistics a");
                        if (elements1 == null || elements1.size() == 0) break;
                        if (StringUtils.isNotBlank(doc.select("#totalPage").attr("value2"))) {
                            count = Integer.valueOf(doc.select("#totalPage").attr("value2"));
                        }

                        for (Element ele : elements1) {
                            String articleLink = ele.attr("href");
                            crawlAction.setUrl(articleLink);
                            crawlAction.work();
                        }
                    }
                    try {
                        tmpLink = link;
                        Thread.sleep(1000 * 20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                count = 1; //重置为第一页
            }
        }
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

    public void fetchArticleTag() {
        String pageContent = (String) HttpRequestTool.getRequest("https://segmentfault.com/tags", true);
        Document doc = Jsoup.parse(pageContent);
        Element element1 = doc.select(".mt20").get(0);
        Elements elements = element1.select(".tag-list__itemWraper");
        ITTag tag = null;
        for (Element element : elements) {
            tag = new ITTag();
            String parentName = element.select(".tag-list__itemheader").get(0).text();
            String parentId = StringUtil.uuid();
            tag.setId(parentId);
            tag.setName(parentName);
            tag.setParent("");
            tag.setCreateOn(System.currentTimeMillis());
            tagMapper.insert(tag);
            System.out.println(parentName);

            Elements sonElements = element.select(".tag-list__itembody li");
            for (Element sonEle : sonElements) {
                String sonName = sonEle.text();
                tag.setId(StringUtil.uuid());
                tag.setName(sonName);
                tag.setParent(parentId);
                tag.setCreateOn(System.currentTimeMillis());
                tagMapper.insert(tag);
                System.out.println(sonName);
            }
            System.out.println("----------------------------------------------------------------------");
        }
    }

    public void fetchGreeArticle() {
        String queue_key = "zixun_url";
        String url = "";
        CrawlAction crawlAction = new CrawlAction("OpenChina", "Article") {
            @Override
            public void action(JsonObject obj, String targetUrl) {
                String json = obj.toString();
                Type type = new TypeToken<ITArticle>() {
                }.getType();
                ITArticle article = gson.fromJson(json, type);
                if (article != null) {
                    article.setId(UUID.randomUUID().toString());
                    article.setAuthor("OpenChina");
//                    article.setCategory(Constant.ArticleCategory.OS_CHINA.getName());
                    article.setCreateOn(System.currentTimeMillis());
                    article.setDescryptUrl(this.getUrl());
                    article.setModule(1);
                    com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
                    BeanUtils.copyProperties(article, esArticle);
                    if (ibmArticleService.addArticle(article) > 0) { //只有文章添加成功的时候，才保存到es中
                        esArticleService.addArticle(esArticle);
                    }
                }
            }
        };
        while (cacheManager.llen(queue_key) > 0) {
            url = cacheManager.lpop(queue_key);
            if (url.contains("www.oschina.net")) {
                cacheManager.lpush("tmp", url);
                crawlAction.setUrl(url);
                crawlAction.work();
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
        log.info("任务执行完毕");
    }

    public void fetchOpen2Open() {
        int fetchTotal = 0;
        String domain = "http://www.open-open.com";
        String url = "http://www.open-open.com/lib/list/401?pn=1";
        Document doc;
        String htmlPage;
        log.info("开始抓取www.open-open.com... ...");
        htmlPage = (String) HttpRequestTool.getRequest(url, false);
        doc = Jsoup.parse(htmlPage);
        Element allList = doc.select(".all-sort-list").get(0);

        CrawlAction crawlAction = new CrawlAction("open-open", "Article") {
            @Override
            public void action(JsonObject obj, String targetUrl) {
                String json = obj.toString();
                Type type = new TypeToken<ITArticle>() {
                }.getType();
                ITArticle article = gson.fromJson(json, type);
                if (article != null) {
                    article.setId(UUID.randomUUID().toString());
//                    article.setCategory(Constant.ArticleCategory.IBM.getName());
                    article.setCreateOn(System.currentTimeMillis());
                    article.setDescryptUrl(this.getUrl());
                    com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
                    BeanUtils.copyProperties(article, esArticle);
                    if (ibmArticleService.addArticle(article) > 0) { //只有文章添加成功的时候，才保存到es中
                        esArticleService.addArticle(esArticle);
                    }
                }
            }
        };

        Elements categoryList = allList.select(".item");
        for (Element cateogry : categoryList) {// 获取总的分类
            String categoryStr = cateogry.select("h3 > a").text();
            System.out.println(categoryStr);
            Elements sonCategories = cateogry.select(".item-list > .subitem > a");// 获取总的分类下的子分类
            for (Element sonCategory : sonCategories) {
                String sonCategoryStr = sonCategory.text();
                System.out.println("\t\t\t" + sonCategoryStr);
                String fetchUrl = domain + sonCategory.attr("href");
                String tmpFetchUrl = fetchUrl + "?pn=%s";
                String realRequestUrl = "";
                for (int i = 0; ; i++) {
                    if (i != 0) {
                        realRequestUrl = String.format(tmpFetchUrl, i);
                    } else {
                        realRequestUrl = fetchUrl;
                    }
                    htmlPage = (String) HttpRequestTool.getRequest(realRequestUrl);
                    doc = Jsoup.parse(htmlPage);
                    Elements topicList = doc.select(".container > .row > .col-md-8 > .list > li");
                    if (topicList == null || topicList.size() <= 0) {
                        log.info(sonCategoryStr + "模块解析完成！");
                        break;
                    }
                    for (Element topicEle : topicList) {
                        String articleLink = domain + topicEle.select(".cont > a").attr("href");
                        log.info("抓取文章链接 = " + articleLink);
                        crawlAction.setUrl(articleLink);
                        crawlAction.work();
                    }
                }

            }
        }
        log.info("抓取完成！");
    }

    public static void main(String[] args) {

    }

    public void import2es() {
        List<ITArticle> articleList = articleMapper.getAllArticles();
        for (ITArticle article : articleList) {
            String content = HTMLUtil.Html2Text(article.getContent());
            if (StringUtils.isBlank(article.getDescription())) {
                String description = "";
                if (content.length() > 255) {
                    description = content.substring(0, 255);
                } else {
                    description = content;
                }
                article.setDescription(description);
                articleMapper.updateByPrimaryKeySelective(article);
            }
            article.setContent(content);
            com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
            BeanUtils.copyProperties(article, esArticle);
            esArticleService.addArticle(esArticle);
        }
    }
}
