package com.water.crawl.core.factory;

import com.water.uubook.model.Article;
import com.water.uubook.model.ITArticle;
import org.jsoup.nodes.Document;

/**
 * Created by zhangmiaojie on 2017/2/24.
 */
public interface IArticleFactory {

    Article createArticle(Document doc, String decryptUrl);

    void printExecuteInfo();
}
