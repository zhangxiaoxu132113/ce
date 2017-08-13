package com.water.crawl.db.service.article.impl;


import javax.annotation.Resource;

import com.water.uubook.dao.ITTagMapper;
import com.water.crawl.db.service.article.ITTagService;
import org.springframework.stereotype.Service;

@Service("iTTagService")
public class ITTagServiceImpl implements ITTagService {
    @Resource
    private ITTagMapper iTTagMapper;
}