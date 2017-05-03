package com.water.crawl.db.service.article.impl;

import com.water.crawl.db.dao.ITCourseMapper;
import com.water.crawl.db.service.article.ITCourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("iTCourseService")
public class ITCourseServiceImpl implements ITCourseService {
    @Resource
    private ITCourseMapper iTCourseMapper;
}