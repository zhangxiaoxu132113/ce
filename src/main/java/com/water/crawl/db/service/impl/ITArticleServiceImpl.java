package com.water.crawl.db.service.impl;



import com.water.crawl.db.dao.ITArticleMapper;
import com.water.crawl.db.service.interfaces.ITArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("iTArticleService")
public class ITArticleServiceImpl implements ITArticleService {
    @Resource
    private ITArticleMapper iTArticleMapper;
}