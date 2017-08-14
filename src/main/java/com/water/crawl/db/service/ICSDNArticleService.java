package com.water.crawl.db.service;

import com.water.uubook.model.ITLib;

import java.util.List;

/**
 * Created by zhangmiaojie on 2017/3/1.
 */
public interface ICSDNArticleService extends ITArticleService {

    /**
     * 添加CSDN知识库下所有的分类
     *
     * @return Integer
     */
    Integer addAllLibCategory();

    /**
     * 获取CSDN知识库下所有的分类
     *
     * @return List<ITLib>
     */
    List<ITLib> getAllLibCategory();
}
