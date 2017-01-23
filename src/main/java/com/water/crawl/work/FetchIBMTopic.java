package com.water.crawl.work;

import com.squareup.okhttp.Response;
import com.water.crawl.core.HttpCrawlClient;
import com.water.crawl.model.Article;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mrwater on 2017/1/10.
 */
public class FetchIBMTopic {

    public static void main(String[] args) {
        FetchIBMTopic fetchIBMTopic = new FetchIBMTopic();
        fetchIBMTopic.fetchTopics();
    }

    public void fetchTopics() {
        long startTime = System.currentTimeMillis();
        HttpCrawlClient client = null;
        try {
            client = HttpCrawlClient.newInstance();
            Map<String, String> params = new HashMap<String, String>();
            params.put("", "");
            String url = "http://www.ibm.com/developerworks/cn/views/web/libraryview.jsp";
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
                        //抓取每一篇文章的内容
                        Article article = fetchTopic(articlelink);
                        articleList.add(article);
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
        Element titleEle = doc.select(".dw-article-ps-title").get(0); //获取文章的标题
        Element createAtEle = doc.select("#dw-article-ps-date").get(0); //文章的发表的日期
        Element contentEle = doc.select(".ibm-columns").get(0);         //文章的内容
//        contentEle.te
        article.setId(12L);
        article.setTitle(titleEle.html());
        article.setCreateAt(createAtEle.html());
        article.setContent(contentEle.html());
        return article;
    }
}
