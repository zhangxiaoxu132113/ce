package com.water.crawl.db.service.article;

import com.water.crawl.db.model.ITArticle;

public interface ITArticleService {

    /**
     * 添加文章
     * @param  article 文章对象
     * @return Integer
     */
    Integer addArticle(ITArticle article);
}