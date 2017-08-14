package com.water.crawl.db.service;

import com.water.uubook.model.Article;
import com.water.uubook.model.ITArticle;

public interface ITArticleService {

    /**
     * 添加文章
     * @param  article 文章对象
     * @return Integer
     */
    Integer addArticle(Article article);


    void consummateArticle(Article article, int origin, int category, String descryptUrl);
}