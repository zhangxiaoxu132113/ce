package com.water.crawl.db.service.article;

import java.util.List;
import java.util.Set;

/**
 * Created by zhangmiaojie on 2017/2/28.
 */
public interface IBMArticleService extends ITArticleService{

    /**
     * 获取当前页面的所有文章链接
     * @param html 内容
     * @return List<String>
     */
    List<String> getAllArticleLink(String html);

    /**
     * 获取IBM开发者社区所有模块的分类
     * @return Set<String>
     */
    Set<String> getIBMArticleCategoryUrl();
}
