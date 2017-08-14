package com.water.crawl.db.service.impl;

import com.water.uubook.dao.ITCourseMapper;
import com.water.crawl.db.service.ITCourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("iTCourseService")
public class ITCourseServiceImpl implements ITCourseService {
    @Resource
    private ITCourseMapper iTCourseMapper;
}