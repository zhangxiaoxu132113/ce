package com.water.crawl.db.service.article.impl;


import com.water.crawl.core.factory.impl.ArticleFactory;
import com.water.crawl.db.dao.ITArticleMapper;
import com.water.crawl.db.model.ITArticle;
import com.water.crawl.db.model.ITArticleCriteria;
import com.water.crawl.db.service.article.ITArticleService;
import com.water.crawl.utils.Constant;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("iTArticleService")
public class ITArticleServiceImpl implements ITArticleService {
    @Resource
    private ITArticleMapper iTArticleMapper;

    @Override
    public Integer addArticle(ITArticle article) {
        if (article != null && StringUtils.isNotBlank(article.getTitle())) {
            Map<String, Object> queryParams = new HashMap<>();
            queryParams.put("title", article.getTitle());
            queryParams.put("category", Constant.ArticleCategory.IBM); //根据同一个网站不能出现同一个标题的原则来避免添加重复的文章
            List<ITArticle> articleList = this.queryArticleByCondition(queryParams);
            if (articleList != null && articleList.size() == 0) {
                return iTArticleMapper.insert(article);
            }
        }
        return -1;
    }

    private List<ITArticle> queryArticleByCondition(Map<String, Object> queryParams) {
        if (queryParams == null) {
            throw new RuntimeException("参数不能为null!");
        }
        ITArticleCriteria articleCriteria = new ITArticleCriteria();
        ITArticleCriteria.Criteria criteria = articleCriteria.createCriteria();
        if (queryParams.containsKey("title")) {
            criteria.andTitleEqualTo((String) queryParams.get("title"));
        } else if (queryParams.containsKey("author")) {
            criteria.andAuthorEqualTo((String) queryParams.get("author"));
        } else if (queryParams.containsKey("category")) {
            criteria.andCategoryEqualTo((String) queryParams.get("category"));
        }
        return iTArticleMapper.selectByExample(articleCriteria);
    }
}