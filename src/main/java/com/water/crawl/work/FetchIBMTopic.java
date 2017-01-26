package com.water.crawl.work;

import com.squareup.okhttp.Response;
import com.water.crawl.core.HttpCrawlClient;
import com.water.crawl.model.Article;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mrwater on 2017/1/10.
 */
public class FetchIBMTopic {

    public static void main(String[] args) {
        FetchIBMTopic fetchIBMTopic = new FetchIBMTopic();
//        fetchIBMTopic.fetchTopics();

        for (String url : fetchIBMTopic.getAllCategoriesLinks()) {
            fetchIBMTopic.fetchTopics(url);
        }
    }

    /**
     * 获取IBM开发者社区下面的所有分类的连接
     * @return
     */
    public List<String> getAllCategoriesLinks() {
        List<String> allCategoriesLinks = new ArrayList<String>();
        List<String> categories = getCategoriesWithTopic();
        String categoryUrl = "http://www.ibm.com/developerworks/cn/views/%s/libraryview.jsp";
        if (categories != null && categories.size() > 0) {
            for (String category : categories) {
                allCategoriesLinks.add(String.format(categoryUrl, category));
            }
        }
        for (String str : allCategoriesLinks) {
            System.out.println(str);
        }
        return allCategoriesLinks;
    }

    /**
     * 获取IBM开发者社区下面的所有分类
     * @return
     */
    public List<String> getCategoriesWithTopic() {
        String url = "http://www.ibm.com/developerworks/cn/";
        List<String> topicCategories = new ArrayList<String>();
        HttpCrawlClient client = null;
        try {
            client = HttpCrawlClient.newInstance();
            Response response = client.executePostRequest(url + "topics/");
            if (response.isSuccessful()) {
                String conent = response.body().string();
                Document doc = Jsoup.parse(conent);
                Elements elements = doc.select("a");
                for (Element ele : elements) {
                    String link = ele.attr("abs:href");
                    if (StringUtils.isNotBlank(link) && link.startsWith(url) && link.length() > url.length()) {
                        link = link.substring(url.length(), link.endsWith("/") ? link.lastIndexOf("/") : link.length());
                        topicCategories.add(link);
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return topicCategories;
    }


    public void fetchTopics(String url) {
        long startTime = System.currentTimeMillis();
        HttpCrawlClient client = null;
        try {
            client = HttpCrawlClient.newInstance();
            Map<String, String> params = new HashMap<String, String>();
            params.put("", "");
            Response response = client.executePostRequest(url);
            if (response.isSuccessful()) {
                String content = response.body().string();
//                System.out.println("内容： \n" + content);

                Document doc = Jsoup.parse(content);
                //解析获取当前页面的所有的文章的链接
                List<String> articleLinks = getArticleLinks(doc);
                List<Article> articleList = new ArrayList<Article>();
                if (articleLinks != null && articleLinks.size() > 0) {
                    for (String articlelink : articleLinks) {
//                        Article article = fetchTopic(articlelink);//抓取每一篇文章的内容
//                        articleList.add(article); todo
                    }
                }
                System.out.println(articleList.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        long endTime = System.currentTimeMillis();
        System.out.println("任务执行完毕！ 耗时：" + (endTime - startTime) + "ms");
    }

    private List<String> getArticleLinks(Document doc) {
        List<String> articleLinks = new ArrayList<String>();
        Elements elements = doc.select(".ibm-container table tbody tr td a");
        for (Element element : elements) {
            String articleLink = element.attr("abs:href");
            articleLinks.add(articleLink);
            System.out.println(element.attr("abs:href"));
        }
        return articleLinks;
    }

    private Article fetchTopic(String url) {
        HttpCrawlClient client = null;
        try {
            client = HttpCrawlClient.newInstance();
            Response reponse = client.executePostRequest(url);
            if (reponse.isSuccessful()) {
                String body = reponse.body().string();
                Document doc = Jsoup.parse(body);
                return this.getArticle(doc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Article getArticle(Document doc) {
        Article article = new Article();
        Element titleEle = doc.select("#ibm-pagetitle-h1").get(0); //获取文章的标题
        Element createAtEle = doc.select("#dw-article-ps-date").get(0); //文章的发表的日期
        Element contentEle = doc.select(".ibm-columns").get(0);         //文章的内容
//        contentEle.te
        article.setId(12L);
        article.setTitle(titleEle.html());
        article.setCreateAt(createAtEle.html());
        article.setContent(contentEle.html());
        return article;
    }

//    public Element get(int i) {
//
//    }

}
