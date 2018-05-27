package com.water.ce.work;

import com.water.ce.http.HttpRequestTool;
import com.water.ce.utils.Markdown2Html;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

/**
 * Created by mrwater on 2018/5/26.
 */
@Component("fetchBookTask")
public class FetchBook {

    private static final Logger logger = Logger.getLogger(FetchBook.class);
    private static String[] categories = new String[]{"android", "frontend", "ios", "backend", "ai", "cloud", "test", "database"};

    private final static String urlTemplate = "https://love2.io/get/category/posts/%s?p=%s&filter=all&sort=hot";
    private final static String imageTemplateurl = "https://oss.love2.io/";
    private final static String innerPageTemplateUrl = "https://love2.io/@webeta/doc/%s"; //内页的url模板
    private final static String summaryTemplateUrl = "https://raw.love2.io/%s/%s/%s/SUMMARY.md"; //内页的url模板

    public void fetchBookTask() throws UnsupportedEncodingException {
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

                for (ResultItem resultItem : result.getResult()) {
                    resultItem.setCover(imageTemplateurl + resultItem.getCover());
                    requestUrl = String.format(summaryTemplateUrl, resultItem.getAuthorUsername(), resultItem.getName(), resultItem.getSha());
                    resultStr = (String) HttpRequestTool.getRequest(requestUrl, false);
                    //将markdown格式的内容转换为html格式
                    resultStr = Markdown2Html.markdown2html(resultStr);
                    document = Jsoup.parse(resultStr);
                    //递归处理书目的章节
                    Element selectEle = document.select("ul").get(0);
                    this.recursionSelect(selectEle);
                }

            }
        }

    }

    private void recursionSelect(Element ele) {
        Elements aEles = ele.select("li > a");
        if (aEles == null || aEles.size() <= 0) {
            return;
        }
        for (Element aEle : aEles) {
            String href = aEle.attr("href");
            String title = aEle.text();
            logger.info("---> " + title + "| href --->" + href);
            recursionSelect(aEle);
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        FetchBook fetchBook = new FetchBook();
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

