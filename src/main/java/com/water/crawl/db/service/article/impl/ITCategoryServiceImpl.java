package com.water.crawl.db.service.article.impl;

import com.water.crawl.db.dao.ITCategoryMapper;
import com.water.crawl.db.service.article.ITCategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("iTCategoryService")
public class ITCategoryServiceImpl implements ITCategoryService {
    @Resource
    private ITCategoryMapper iTCategoryMapper;
}