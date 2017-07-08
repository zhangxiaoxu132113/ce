package com.water.crawl.db.service.impl;

import com.water.crawl.db.dao.CourseSubjectMapper;
import com.water.crawl.db.service.CourseSubjectService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("courseSubjectService")
public class CourseSubjectServiceImpl implements CourseSubjectService {
    @Resource
    private CourseSubjectMapper courseSubjectMapper;
}