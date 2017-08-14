package com.water.crawl.core.factory.impl;


import com.water.crawl.core.factory.IArticleFactory;
import com.water.uubook.model.Article;
import com.water.uubook.model.ITArticle;
import org.jsoup.nodes.Document;

import java.util.UUID;

/**
 *
 * Created by zhangmiaojie on 2017/2/23.
 */
public class IBMArticleFactory implements IArticleFactory {

    private static int methodOneExecuteFailureTimes;
    private static int methodTwoExecuteFailureTimes;
    private static int methodThreeExecuteFailureTimes;
    private static int methodFourExecuteFailureTimes;

    @Override
    public Article createArticle(Document doc, String decryptUrl) {
        Article article = null;
        article = createArticleMethodOne(doc, decryptUrl);
        if (article == null) {
            article = createArticleMethodTwo(doc, decryptUrl);
            if (article == null) {
                article = createArticleMethodThree(doc, decryptUrl);
                if (article == null) {
                    System.out.println("解析URL链接失败 " + decryptUrl);
                }
            }
        }

        return article;
    }

    public Article createArticleMethodOne(Document doc, String decryptUrl) {
        Article article = new Article();
        try {
            String title = doc.getElementsByTag("title").get(0).text();
            String description = doc.select("meta[name=description]").get(0).attr("content");
            String content = doc.select(".ibm-col-1-1").get(0).html();
            String author = doc.select("a.ibm-popup-link").get(0).ownText();
            String date = doc.select(".dw-summary-date").get(0).html();
            String reference = "";
            if (doc.select(".ibm-bullet-list") != null && doc.select(".ibm-bullet-list").size() > 0) {
                reference = doc.select(".ibm-bullet-list").get(0).html();
            }

            article.setAuthor(author);
            article.setTitle(title);
//            article.setCategory("IBM");
            article.setContent(content);
            article.setDescription(description);
            article.setReference(reference);
            article.setReleaseTime(date);
            article.setCreateOn(System.currentTimeMillis());
            article.setDescryptUrl(decryptUrl);
        } catch (Exception e) {
            article = null;
            methodOneExecuteFailureTimes++;
//            e.printStackTrace();
        }
        return article;
    }

    public Article createArticleMethodTwo(Document doc, String decryptUrl) {
        Article article = new Article();
        try {
            String title = doc.getElementsByTag("title").get(0).html();
            String description = doc.select("meta[name=description]").get(0).attr("content");
            String reference = "";
            if (doc.select(".ibm-bullet-list") != null && doc.select(".ibm-bullet-list").size() > 0) {
                reference = doc.select(".ibm-bullet-list").get(0).html();
            }
            String author = doc.select(".dw-article-ps-author").get(0).ownText();
            String content = doc.select(".ibm-col-6-4").get(1).html();
            String createOn = doc.select("#dw-article-ps-date").get(0).html();

            article.setAuthor(author);
            article.setTitle(title);
//            article.setCategory("IBM");
            article.setContent(content);
            article.setDescription(description);
            article.setReference(reference);
            article.setReleaseTime(createOn);
            article.setDescryptUrl(decryptUrl);
            article.setCreateOn(System.currentTimeMillis());
        } catch (Exception e) {
            article = null;
            methodTwoExecuteFailureTimes++;
//            e.printStackTrace();
        }
        return article;
    }

    public Article createArticleMethodThree(Document doc, String decryptUrl) {
        Article article = new Article();
        try {
            String title = doc.getElementsByTag("title").get(0).html();
            String description = doc.select("meta[name=description]").get(0).attr("content");
            String reference = "";
            if (doc.select(".ibm-bullet-list") != null && doc.select(".ibm-bullet-list").size() > 0) {
                reference = doc.select(".ibm-bullet-list").get(0).html();
            }
            String author = doc.select(".dw-article-authordate").get(0).ownText();
            // ownText() and text() 两者的区别在于ownText只包含了该节点的文本，不包含其子节点的文本。
            String content = doc.select(".ibm-col-6-4").get(0).html();
            String createOn = doc.select(".dw-article-pubdate").get(0).html().replace("发布", "");

            article.setAuthor(author);
            article.setTitle(title);
//            article.setCategory("IBM");
            article.setReleaseTime(createOn);
            article.setContent(content);
            article.setDescription(description);
            article.setReference(reference);
            article.setDescryptUrl(decryptUrl);
            article.setCreateOn(System.currentTimeMillis());
        } catch (Exception e) {
            article = null;
            methodThreeExecuteFailureTimes++;
//            e.printStackTrace();
        }
        return article;
    }

    public Article createArticleMethodFour(Document doc, String decryptUrl) {
        Article article = new Article();
        try {
            String title = doc.getElementsByTag("title").get(0).html();
            String description = doc.select("meta[name=description]").get(0).attr("content");
            String reference = "";
            if (doc.select(".ibm-bullet-list") != null && doc.select(".ibm-bullet-list").size() > 0) {
                reference = doc.select(".ibm-bullet-list").get(0).html();
            }
            String author = doc.select(".dw-summary-author").get(0).ownText();
            // ownText() and text() 两者的区别在于ownText只包含了该节点的文本，不包含其子节点的文本。
            String content = doc.select(".ibm-col-6-4").get(0).html();
            String date = doc.select(".dw-summary-date").get(0).html();

            article.setAuthor(author);
            article.setTitle(title);
//            article.setCategory("IBM");
            article.setContent(content);
            article.setDescription(description);
            article.setReference(reference);
            article.setReleaseTime(date);
            article.setDescryptUrl(decryptUrl);
            article.setCreateOn(System.currentTimeMillis());
        } catch (Exception e) {
            article = null;
            methodFourExecuteFailureTimes++;
//            e.printStackTrace();
        }
        return article;
    }

    @Override
    public void printExecuteInfo() {
        System.out.println(methodOneExecuteFailureTimes);
        System.out.println(methodTwoExecuteFailureTimes);
        System.out.println(methodThreeExecuteFailureTimes);
        System.out.println(methodFourExecuteFailureTimes);
    }

    /**
     * http://www.ibm.com/developerworks/cn/java/j-10_things_to_know_strongloop/index.html
     * http://www.ibm.com/developerworks/cn/java/j-bluemix-jvm-debugging01/index.html
     * http://www.ibm.com/developerworks/cn/java/j-workload-scheduler-service/index.html
     */
//    public static void main(String[] args) {
//        IBMArticleFactory factory = new IBMArticleFactory();
//        String url = "http://www.ibm.com/developerworks/cn/java/j-workload-scheduler-service/index.html";
//        String html = (String) TestHttpClient.getRequest(url, true);
//        Document doc = Jsoup.parse(html);
//        Article article = factory.createArticleMethodFour(doc, url);
//    }
}
