package com.water.crawl.db.service.impl;

import com.water.crawl.db.dao.CourseMapper;
import com.water.crawl.db.service.CourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("courseService")
public class CourseServiceImpl implements CourseService {
    @Resource
    private CourseMapper courseMapper;
}