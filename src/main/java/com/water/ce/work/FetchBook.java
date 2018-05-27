package com.water.ce.work;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.Markdown2Html;
import com.water.uubook.dao.TbUbBookMapper;
import com.water.uubook.dao.extend.TbUbBookMapperExtend;
import com.water.uubook.model.TbUbBook;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by mrwater on 2018/5/26.
 */
@Component("fetchBookTask")
public class FetchBook {

    @Resource
    private TbUbBookMapper bookMapper;

    private static final Logger logger = Logger.getLogger(FetchBook.class);
    private static String[] categories = new String[]{"android", "frontend", "ios", "backend", "ai", "cloud", "test", "database"};

    private final static String urlTemplate = "https://love2.io/get/category/posts/%s?p=%s&filter=all&sort=hot";
    private final static String imageTemplateurl = "https://oss.love2.io/";
    private final static String innerPageTemplateUrl = "https://love2.io/@webeta/doc/%s"; //内页的url模板
    private final static String summaryTemplateUrl = "https://raw.love2.io/%s/%s/%s/SUMMARY.md"; //内页的url模板
    private final static String articleTemplateurl = "https://raw.love2.io/%s/%s/%s/%s";


    /**
     * 抓取书目任务
     * @throws UnsupportedEncodingException
     */
    public void fetchBookTask() throws UnsupportedEncodingException {
        logger.info("开始爬取内容-->");
        long startTime = System.currentTimeMillis();
        Result result;
        String resultStr;
        String requestUrl;
        Document document;
        //开始请求，依次请求各个模块
        for (String category : categories) {
            logger.info("fetch category -> " + category);
            for (int i = 1; ; i++) {
                requestUrl = String.format(urlTemplate, category, i);
                result = (Result) HttpRequestTool.getRequest(requestUrl, Result.class, false);
                if (result == null) {
                    logger.error("请求格式出现异常！");
                    continue;
                }
                if (result.getCode() == 1) {
                    logger.info(result.getDesc());
                    break;
                }
                //处理获取到的每一本书
                for (ResultItem resultItem : result.getResult()) {
                    resultItem.setCover(imageTemplateurl + resultItem.getCover());
                    requestUrl = String.format(summaryTemplateUrl, resultItem.getAuthorUsername(), resultItem.getName(), resultItem.getSha());
                    this.insertBook(resultItem);
                    //请求书目的目录
                    resultStr = (String) HttpRequestTool.getRequest(requestUrl, false);
                    //将markdown格式的内容转换为html格式
                    resultStr = Markdown2Html.markdown2html(resultStr);
                    document = Jsoup.parse(resultStr);
                    //递归处理书目的章节
                    Element selectEle = document.select("ul").get(0);
                    this.recursionSelect(selectEle, resultItem);
                }

            }
        }

        long endTime = System.currentTimeMillis();
        logger.info("任务执行结束,一共耗时---->"+(endTime-startTime)/1000+"秒");
    }

    private void insertBook(ResultItem resultItem) {
        TbUbBook tbUbBook = new TbUbBook();
        tbUbBook.setId(UUID.randomUUID().toString());
        tbUbBook.setName(resultItem.getTitle());
        tbUbBook.setFollow(0);
        tbUbBook.setChapterCount(0);
        tbUbBook.setCover(resultItem.getCover());
        tbUbBook.setAuthor("");
        tbUbBook.setCreateBy("admin");
        tbUbBook.setCreateTime(new Date());
        bookMapper.insert(tbUbBook);
    }

    /**
     * 递归处理书目的章节
     * @param ele
     * @param resultItem
     */
    private void recursionSelect(Element ele, ResultItem resultItem) {
        Elements aEles = ele.select("li > a");
        if (aEles == null || aEles.size() <= 0) {
            return;
        }
        for (Element aEle : aEles) {
            String href = aEle.attr("href");
            String title = aEle.text();
            logger.info("---> " + title + "| href --->" + href);
            String reuqestUrl = String.format(articleTemplateurl, resultItem.getAuthorUsername(),
                    resultItem.getName(), resultItem.getSha(), href);
            logger.info("fetch article content -- > " + reuqestUrl);
            String content = (String) HttpRequestTool.getRequest(reuqestUrl);
            content = Markdown2Html.markdown2html(content);
            System.out.println(content);
            recursionSelect(aEle, resultItem);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
//        FetchBook fetchBook = new FetchBook();
//        fetchBook.fetchBookTask();

        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        FetchBook fetchBook = (FetchBook) context.getBean("fetchBookTask");
        fetchBook.fetchBookTask();
    }
}

class Result {
    private Integer code;
    private String desc;
    private ResultItem[] result;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public ResultItem[] getResult() {
        return result;
    }

    public void setResult(ResultItem[] result) {
        this.result = result;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}

class ResultItem {
    private Integer id;
    private String sha;
    private String name;
    private String authorUsername;
    private String title;
    private String language;
    private String description;
    private String cover;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSha() {
        return sha;
    }

    public void setSha(String sha) {
        this.sha = sha;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public void setAuthorUsername(String authorUsername) {
        this.authorUsername = authorUsername;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }
}

