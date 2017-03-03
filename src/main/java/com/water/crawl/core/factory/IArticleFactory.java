package com.water.crawl.core.factory;

import com.water.crawl.db.model.ITArticle;
import org.jsoup.nodes.Document;

/**
 * Created by zhangmiaojie on 2017/2/24.
 */
public interface IArticleFactory {

    ITArticle createArticle(Document doc, String decryptUrl);

    void printExecuteInfo();
}
