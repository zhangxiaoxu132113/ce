package com.water.test;

import com.water.crawl.utils.ElasticSearchUtils;

/**
 *
 * Created by zhangmiaojie on 2017/2/28.
 */
public class TestElastcsearch {

    public static void main(String[] args) {
//        ElasticSearchUtils.searchDocumentByTerm("kafka");
//        ElasticSearchUtils.createIKMapping(ITArticle.class);
//        ElasticSearchUtils.createIndex(ITArticle.class);
        ElasticSearchUtils.matchQueryBuilder("blog", "article", "content", "依赖注入");
    }
}
