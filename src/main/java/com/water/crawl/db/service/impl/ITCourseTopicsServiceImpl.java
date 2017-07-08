package com.water.crawl.db.service.impl;

import com.water.crawl.db.dao.ITCourseTopicsMapper;
import com.water.crawl.db.service.ITCourseTopicsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("iTCourseTopicsService")
public class ITCourseTopicsServiceImpl implements ITCourseTopicsService {
    @Resource
    private ITCourseTopicsMapper iTCourseTopicsMapper;
}