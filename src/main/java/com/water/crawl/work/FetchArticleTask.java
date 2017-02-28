package com.water.crawl.work;

import com.alibaba.fastjson.JSONObject;
import com.water.crawl.core.factory.IArticleFactory;
import com.water.crawl.core.factory.impl.ArticleFactory;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.db.service.article.IBMArticleService;
import com.water.crawl.utils.ElasticSearchUtils;
import com.water.crawl.utils.HttpRequestTool;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by zhangmiaojie on 2017/2/28.
 */
public class FetchArticleTask {

    @Resource
    private IBMArticleService ibmArticleService;

    public void fetchIBMArticles() {
        System.out.println("抓取IBM的文章-----");
        List<ITArticle> articles = new ArrayList<ITArticle>();
        List<String> fetchFailurelinks = new ArrayList<String>();
        Set<String> articleCategoryUrls = new HashSet<String>();
        articleCategoryUrls = ibmArticleService.getIBMArticleCategoryUrl();
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
                List<String> linkList = ibmArticleService.getAllArticleLink(result);
                for (String link : linkList) {
                    System.out.println("开始抓取文章--" + link);
                    String html = (String) HttpRequestTool.getRequest(link);
                    if (StringUtils.isNotBlank(html)) {
                        Document doc = Jsoup.parse(html);
                        ITArticle article = articleFactory.createArticle(doc, link);
                        if (article != null) {
                            ibmArticleService.addArticle(article);
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
    }
}
