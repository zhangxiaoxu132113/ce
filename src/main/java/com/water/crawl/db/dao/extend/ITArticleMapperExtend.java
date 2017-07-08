package com.water.crawl.db.dao.extend;

import com.water.crawl.db.model.ITArticle;

import java.util.List;
import java.util.Map;

public interface ITArticleMapperExtend {
    List<ITArticle> getAllArticle(Map<String, Object> queryMap);
}