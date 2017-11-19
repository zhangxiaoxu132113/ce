package com.water.ce.work;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.water.ce.crawl.CrawlAction;
import com.water.uubook.dao.*;
import com.water.uubook.model.*;
import com.water.ce.utils.*;
import com.water.ce.utils.http.HttpRequestTool;
import com.water.ce.utils.lang.StringUtil;
import com.water.es.api.Service.IArticleService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 抓取文章的定时器类
 * <p>
 * <p>
 * Created by zhangmiaojie on 2017/2/28.
 */
@Service("fetchArticleTask")
public class FetchArticleTask {
    private static Log log = LogFactory.getLog(FetchArticleTask.class);

    @Resource
    private ArticleMapper articleMapper;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private CourseSubjectMapper courseSubjectMapper;

    @Resource
    private CourseMapper courseMapper;

    @Resource
    private TagRelationshipMapper tagRelationshipMapper;

    private Gson gson = new Gson();

    /**
     * 抓取IBM开发者社区的文章
     */
    public void fetchIBMArticles() {
        System.out.println("抓取IBM开发者社区的文章--------------------------------------------------");
        List<Article> articles = new ArrayList<>();
        List<String> fetchFailurelinks = new ArrayList<String>();
        Set<String> articleCategoryUrls = new HashSet<String>();

        articleCategoryUrls = ibmArticleService.getIBMArticleCategoryUrl();
//        articleCategoryUrls.add("http://www.ibm.com/developerworks/cn/views/data/libraryview.jsp");
//        articleCategoryUrls.add("http://www.ibm.com/developerworks/cn/views/java/libraryview.jsp");
        System.out.println("开始抓取IBM开发者社区各个模块的文章，" + articleCategoryUrls.size());

        boolean isBreak = false;
        for (String url : articleCategoryUrls) {
            System.out.println("开始抓取 ： " + url);

            Map<String, String> paramMap = new HashMap<String, String>();
            for (int i = 1; true; i++) {//循环获取所有模块下每个页面的文章
                paramMap.put("start", String.valueOf(i + (i - 1) * 100));
                paramMap.put("end", String.valueOf(i * 100));
                String result = (String) HttpRequestTool.postRequest(url, paramMap, true);
                if (StringUtils.isBlank(result)) break;
                List<String> linkList = ibmArticleService.getAllArticleLink(result);
                if (linkList == null || linkList.size() <= 0) {
                    isBreak = true;
                    break;
                }
                for (String link : linkList) {
                    log.info("开始抓取文章--" + link);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CrawlAction crawlAction = new CrawlAction("IBM", "Article", link) {
                        @Override
                        public void action(JsonObject obj, Object data) {
                            String json = obj.toString();
                            Type type = new TypeToken<Article>() {
                            }.getType();
                            Article article = gson.fromJson(json, type);
                            if (article != null) {
                                article.setCategory(0);
                                article.setViewHits(0);
                                article.setContent(handleContent(article.getContent(), this.getUrl()));
                                article.setModule(Constant.ARTICLE_MODULE.BLOG.getIndex());
                                if (article.getCreateOn() == null) {
                                    article.setCreateOn(System.currentTimeMillis());
                                }
                                if (ibmArticleService.addArticle(article) > 0) {
//                                    com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
//                                    BeanUtils.copyProperties(article, esArticle);
//                                    esArticleService.addArticle(esArticle);
                                }
                            }
                        }


                    };
                    crawlAction.work();
                }
            }
            if (isBreak) break;
        }
    }

    private String handleContent(String content, String url) {
        Document doc = Jsoup.parse(content);
        Elements eles = doc.select("img");
        for (Element ele : eles) {
            String imgSrc = ele.attr("src");
            String newImgSrc = url.substring(0, url.lastIndexOf("/")+1) + imgSrc;
            ele.attr("src", newImgSrc);
        }

        return doc.toString();
    }

    /**
     * 抓取CSDN知识库下所有的文章
     */
    public void fetchCSDNArticleLib() {
//        Gson gson = new Gson();
//        CrawlAction crawlAction = new CrawlAction("IBM", "Article", "https://www.ibm.com/developerworks/cn/web/wa-implement-a-single-page-application-with-angular2/index.html") {
//            @Override
//            public void action(JsonObject obj, Object data) {
//                System.out.println();
//            }
//        };
//        crawlAction.work();
//
//        int count = 1;
//        Map<String, String> queryMap = new HashMap<String, String>();
//        List<ITLib> itLibList = icsdnArticleService.getAllLibCategory();
//        Set<String> linkSet = new HashSet<String>();
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
//                                Article article = articleFactory.createArticle(doc, articleLink);
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
//        String base_url = "https://www.oschina.net/blog";                                   //第一次进来不是ajax请求
//        String ajax_url = "https://www.oschina.net/action/ajax/get_more_recommend_blog";    //第二次进来是ajax请求
//        Set<String> linkSet = ioschinaArticleService.getAllCategory();
//        for (String link : linkSet) {
//            Map<String, String> queryParams = StringUtil.getParamWithUrl(link);
//            String html = "";
//            for (int i = 1; ; i++) {
//                if (i == 1) {
//                    html = (String) HttpRequestTool.postRequest(base_url, queryParams, false);
//                } else {
//                    queryParams.put("q", String.valueOf(i));
//                    html = (String) HttpRequestTool.postRequest(ajax_url, queryParams, false);
//                }
//
//                Document doc = Jsoup.parse(html);
//                Elements articleLinks = doc.select(".blog-title-link");
//                for (Element articleLinkEle : articleLinks) {
//                    String articleLink = articleLinkEle.attr("href");
//                    System.out.println(articleLink);
//                    html = (String) HttpRequestTool.getRequest(articleLink, false);
//                    doc = Jsoup.parse(html);
//                    //创建文章
//                }
//            }
//        }
    }

    public void fetchCourse() {
        String root_url = "http://ifeve.com/java-7-concurrency-cookbook/";
        String pageHtml = (String) HttpRequestTool.getRequest(root_url, false);
        Document doc = Jsoup.parse(pageHtml);

        Elements baseEle = doc.select(".post_content ");
        Elements titleEles = baseEle.select("h3");
        Elements secondTitleEles = baseEle.select("ol");
        ITCourse course = new ITCourse();
        int total = 8;
        for (int i = 0; i < total; i++) {
            Element titleEle = titleEles.get(i + 1);
            Element secondTitleEle = secondTitleEles.get(i);
            String title = titleEle.text();
            String partentId = UUID.randomUUID().toString();
            course.setId(partentId);
            course.setName(title);
            course.setParentId(null);
            course.setCreateOn(System.currentTimeMillis());
//            courseMapper.insert(course);
            Elements linkEles = secondTitleEle.select("li > a");
            for (Element linkEle : linkEles) {
                System.out.println(linkEle.text() + "\t\t" + linkEle.absUrl("href"));
            }
        }
    }

    public void fetchopen2open() {
//        int fetchTotal = 0;
//        String domain = "http://www.open-open.com";
//        String url = "http://www.open-open.com/lib/list/401?pn=1";
//        Document doc;
//        String htmlPage;
//        log.info("开始抓取www.open-open.com... ...");
//        htmlPage = (String) HttpRequestTool.getRequest(url, false);
//        doc = Jsoup.parse(htmlPage);
//        Element allList = doc.select(".all-sort-list").get(0);
//        Elements categoryList = allList.select(".item");
//        ITCategory category;
//        String partnetId;
//        for (Element cateogry : categoryList) {// 获取总的分类
//            category = new ITCategory();
//            String categoryStr = cateogry.select("h3 > a").text();
//            category.setId(UUID.randomUUID().toString());
//            category.setPartentId("");
//            category.setLevel(1);
//            category.setName(categoryStr);
//            category.setCreateOn(System.currentTimeMillis());
//            categoryMapper.insert(category);
//            partnetId = category.getId();
//            System.out.println(categoryStr);
//            Elements sonCategories = cateogry.select(".item-list > .subitem > a");// 获取总的分类下的子分类
//            for (Element sonCategory : sonCategories) {
//                String sonCategoryStr = sonCategory.text();
//                System.out.println("\t\t\t" + sonCategoryStr);
//                category = new ITCategory();
//                category.setId(UUID.randomUUID().toString());
//                category.setPartentId(partnetId);
//                category.setLevel(2);
//                category.setName(sonCategoryStr);
//                category.setCreateOn(System.currentTimeMillis());
//                categoryMapper.insert(category);
//
//
//                System.out.println("\t\t\t" + sonCategoryStr);
//                String fetchUrl = domain + sonCategory.attr("href");
//                String tmpFetchUrl = fetchUrl + "?pn=%s";
//                String realRequestUrl = "";
//                for (int i = 0; ; i++) {
//                    if (i != 0) {
//                        realRequestUrl = String.format(tmpFetchUrl, i);
//                    } else {
//                        realRequestUrl = fetchUrl;
//                    }
//                    htmlPage = (String) HttpRequestTool.getRequest(realRequestUrl);
//                    doc = Jsoup.parse(htmlPage);
//                    Elements topicList = doc.select(".container > .row > .col-md-8 > .list > li");
//                    if (topicList == null || topicList.size() <= 0) {
//                        log.info(sonCategoryStr + "模块解析完成！");
//                        break;
//                    }
//                    for (Element topicEle : topicList) {
//                        String articleLink = domain + topicEle.select(".cont > a").attr("href");
//                        log.info("抓取文章链接 = " + articleLink);
//                        //TODO 交给爬虫容器
//                    }
//                }
//
//            }
//        }
    }

    public void fetchCourse2() {
//        CrawlAction crawlAction = new CrawlAction("YIBAI", "Article") {
//            @Override
//            public void action(JsonObject obj, Object data) {
//                Course course = (Course) data;
//                Type type = new TypeToken<Article>() {
//                }.getType();
//                Article article = gson.fromJson(obj.toString(), type);
//                if (article != null) {
//                    article.setModule(Constant.ARTICLE_MODULE.JIAO_CHENG.getIndex());
//                    ibmArticleService.addArticle(article);
//                    Integer articleId = article.getId();
//                    if (articleId > 0) {
//                        course.setArticleId(articleId);
//                        courseMapper.insert(course);
//
//                    }
//                }
//            }
//        };
//        String rootUrl = "http://www.yiibai.com/";
//        String page;
//        Document doc;
//        page = (String) HttpRequestTool.getRequest(rootUrl);
//        doc = Jsoup.parse(page);
//        Element items = doc.select(".article-content > .items").get(0);
//        Elements itemList = items.select(".item");
//        for (int i = 1; i < itemList.size(); i++) {
//            CourseSubject courseSubject = new CourseSubject();
//            Element item = itemList.get(i);
//            String category = item.select("h2").text();
//            System.out.println(category);
//            courseSubject.setName(category);
//            courseSubject.setCreateOn(System.currentTimeMillis());
//            courseSubject.setUpdateTime(System.currentTimeMillis());
//            courseSubjectMapper.insertReturnPrimaryKey(courseSubject);
//            Integer coursePartentId = courseSubject.getId();
//            Elements liEles = item.select(".blogroll").get(0).select("li > a");
//            for (Element liEle : liEles) {
//                CourseSubject courseSubject1 = new CourseSubject();
//                String link = liEle.absUrl("href");
//                String name = liEle.text();
//                String desc = liEle.attr("title");
//                System.out.println(name + ":" + link + desc);
//
//                courseSubject1.setName(name);
//                courseSubject1.setDescription(desc);
//                courseSubject1.setPartentId(coursePartentId);
//                courseSubject1.setCreateOn(System.currentTimeMillis());
//                courseSubject1.setUpdateTime(System.currentTimeMillis());
//                courseSubjectMapper.insertReturnPrimaryKey(courseSubject1);
//                Integer partentId = courseSubject1.getId();
//                log.info("开始抓取每一个专题的文章");
//                int retry = 1;
//                page = (String) HttpRequestTool.getRequest(link);
//                while (true) {
//                    if (StringUtils.isBlank(page) || retry++ != 3) {
//                        page = (String) HttpRequestTool.getRequest(link);
//                    } else {
//                        break;
//                    }
//                }
//                if (StringUtils.isBlank(page)) continue;
//                doc = Jsoup.parse(page);
//                Elements titleElements = doc.select(".pagemenu > li > a");
//                byte sort = 1;
//                for (Element titleEle : titleElements) {
//                    String title = titleEle.text();
//                    String titleLink = titleEle.absUrl("href");
//                    Integer courseParentId = 0;
//                    Course course = new Course();
//                    course.setSort(sort);
//                    sort++;
//                    course.setTitle(title);
//                    course.setCourseSubjectId(partentId);
//                    course.setCreateOn(System.currentTimeMillis());
//                    course.setUpdateTime(System.currentTimeMillis());
//                    if (StringUtils.isBlank(titleLink)) {
//                        courseMapper.insertReturnPrimarykey(course);
//                        courseParentId = course.getId();
//                        continue;
//                    }
//                    System.out.println(title);
//                    course.setPartentId(courseParentId);
//                    crawlAction.setUrl(titleLink);
//                    crawlAction.setData(course);
//                    crawlAction.work();
//
//                }
//
//            }
//        }
//        System.out.println("运行结束！");
    }

    public static void main(String[] args) {

//        String rootUrl = "http://www.yiibai.com/";
//        String page = null;
//        Document doc = null;
//        page = (String) HttpRequestTool.getRequest(rootUrl);
//        doc = Jsoup.parse(page);
//        Element items = doc.select(".article-content > .items").get(0);
//        Elements itemList = items.select(".item");
//        for (int i = 1; i < itemList.size(); i++) {
//            CourseSubject courseSubject = new CourseSubject();
//            Element item = itemList.get(i);
//            String category = item.select("h2").text();
//            if (StringUtils.isNotBlank(category) && category.equals("推荐教程")) continue;
//            System.out.println(category);
//            courseSubject.setId(StringUtil.uuid());
//            courseSubject.setName(category);
//            courseSubject.setCreateOn(System.currentTimeMillis());
//            courseSubject.setUpdateTime(System.currentTimeMillis());
//            String partentId = courseSubject.getId();
//            Elements liEles = item.select(".blogroll").get(0).select("li > a");
//            for (Element liEle : liEles) {
//                courseSubject = new CourseSubject();
//                String link = liEle.absUrl("href");
//                String name = liEle.text();
//                String desc = liEle.attr("title");
//                System.out.println(name + ":" + link + desc);
//
//                courseSubject.setId(StringUtil.uuid());
//                courseSubject.setName(name);
//                courseSubject.setDescription(desc);
//                courseSubject.setPartentId(partentId);
//                courseSubject.setCreateOn(System.currentTimeMillis());
//                courseSubject.setUpdateTime(System.currentTimeMillis());
//
//                log.info("开始抓取每一个专题的文章");
//                page = (String) HttpRequestTool.getRequest(link);
//                doc = Jsoup.parse(page);
//                Elements titleElements = doc.select(".pagemenu > li > a");
//                for (Element titleEle : titleElements) {
//                    String title = titleEle.text();
//                    String titleLink = titleEle.absUrl("href");
//
//
//                }
//
//            }
//        }
//        System.out.println("运行结束！");


        String url = "https://www.ibm.com/developerworks/cn/java/j-local-Cucumber-high-level/image001.png";

        String newUrl = url.substring(0, url.lastIndexOf("/")+1);
        System.out.println(newUrl);
    }
}
