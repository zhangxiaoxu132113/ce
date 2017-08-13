package com.water.crawl.db.service.impl;


import com.water.uubook.dao.ArticleMapper;
import com.water.uubook.dao.ITArticleMapper;
import com.water.uubook.dao.ITLibMapper;
import com.water.crawl.db.service.ITArticleService;
import com.water.crawl.utils.lang.StringUtil;
import com.water.uubook.model.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class ITArticleServiceImpl implements ITArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ITLibMapper itLibMapper;

    @Override
    public Integer addArticle(Article article) {
        if (article != null) {
//            Map<String, Object> queryParams = new HashMap<>();
//            queryParams.put("title", article.getTitle());
//            queryParams.put("category", article.getOrigin()); //根据同一个网站不能出现同一个标题的原则来避免添加重复的文章
//            List<Article> articleList = this.queryArticleByCondition(queryParams);
//            if (articleList != null && articleList.size() == 0) {
//                return iTArticleMapper.insert(article);
//            }
            articleMapper.insertReturnPrimaryKey(article);
            return article.getId();
        }
        return -1;
    }

    /**
     * 生成文章主键
     * @return
     */
//    private String generateArticleId() {
//        String uid;
//        int retryCount = 0;
//        Article article;
//        while (true) {
//            retryCount++;
//            uid = StringUtil.uuid();
//            article = articleMapper.selectByPrimaryKey(uid);
//            if (article == null) break;
//            if (retryCount == 3) { //如果id重复，则重新生成，直到第三次退出循环
//                uid = "";
//                break;
//            }
//        }
//        return uid;
//    }

    public void consummateArticle(Article article, int origin, int category, String descryptUrl) {
        article.setOrigin(origin);
        article.setCategory(category);
        article.setDescryptUrl(descryptUrl);
        article.setCreateOn(System.currentTimeMillis());
        if (StringUtils.isBlank(article.getDescription())) {
            String content = article.getContent();
            if (content.length() >= 255) {
                article.setDescription(article.getContent().substring(0,255));
            } else {
                article.setDescription(content);
            }
        }
    }

    /**
     * 根据条件查询文章
     * @param queryParams 查询参数
     * @return            List<Article>
     */
    protected List<Article> queryArticleByCondition(Map<String, Object> queryParams) {
        if (queryParams == null) {
            throw new RuntimeException("参数不能为null!");
        }
        ArticleCriteria articleCriteria = new ArticleCriteria();
        ArticleCriteria.Criteria criteria = articleCriteria.createCriteria();
        if (queryParams.containsKey("title")) {
            criteria.andTitleEqualTo((String) queryParams.get("title"));
        } else if (queryParams.containsKey("author")) {
            criteria.andAuthorEqualTo((String) queryParams.get("author"));
        } else if (queryParams.containsKey("category")) {
            criteria.andCategoryEqualTo((Integer) queryParams.get("category"));
        }
        return articleMapper.selectByExample(articleCriteria);
    }

    /**
     * 根据条件查询知识库
     * @param queryParams 查询参数
     * @return            List<ITLib>
     */
    protected List<ITLib> queryLibByCondition(Map<String, Object> queryParams) {
        if (queryParams == null) {
            throw new RuntimeException("参数不能为null!");
        }
        ITLibCriteria itLibCriteria = new ITLibCriteria();
        ITLibCriteria.Criteria criteria = itLibCriteria.createCriteria();
        if (queryParams.containsKey("name")) {
            criteria.andNameEqualTo((String) queryParams.get("name"));
        } else if (queryParams.containsKey("category")) {
            criteria.andCategoryEqualTo((Integer) queryParams.get("category"));
        }
        return itLibMapper.selectByExample(itLibCriteria);
    }


}