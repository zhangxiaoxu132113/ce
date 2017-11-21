package com.water.ce.work;

import com.water.ce.utils.http.HttpRequestTool;
import com.water.ce.web.service.CSDNCrawlingArticleService;
import com.water.ce.web.service.IBMCrawlingArticleService;
import com.water.ce.web.service.Open2OpenCrawlingArticleService;
import com.water.uubook.model.ITCourse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * 抓取文章的定时器类
 * Created by zhangmiaojie on 2017/2/28.
 */
@Service("fetchArticleTask")
public class FetchArticleTask {
    private static Log log = LogFactory.getLog(FetchArticleTask.class);

    @Resource(name = "ibmCrawlingArticleService")
    private IBMCrawlingArticleService ibmCrawlingArticleService;

    @Resource(name = "open2OpenCrawlingArticleService")
    private Open2OpenCrawlingArticleService open2OpenCrawlingArticleService;

    @Resource(name = "csdnCrawlingArticleService")
    private CSDNCrawlingArticleService csdnCrawlingArticleService;
    /**
     * 抓取IBM开发者社区的文章
     */
    public void fetchIBMArticles() {
        ibmCrawlingArticleService.handle();
    }

    /**
     * 抓取深度开源的文章
     */
    public void fetchopen2open() {
        open2OpenCrawlingArticleService.handle();
    }

    private String handleContent(String content, String url) {
        Document doc = Jsoup.parse(content);
        Elements eles = doc.select("img");
        for (Element ele : eles) {
            String imgSrc = ele.attr("src");
            String newImgSrc = url.substring(0, url.lastIndexOf("/") + 1) + imgSrc;
            ele.attr("src", newImgSrc);
        }

        return doc.toString();
    }

    /**
     * 抓取CSDN知识库下所有的文章
     */
    public void fetchCSDNArticleLib() {
        csdnCrawlingArticleService.handle();
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
    }
}
