package com.water.crawl.db.service.impl;

import com.water.uubook.dao.TagRelationshipMapper;
import com.water.crawl.db.service.TagRelationshipService;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("tagRelationshipService")
public class TagRelationshipServiceImpl implements TagRelationshipService {
    @Resource
    private TagRelationshipMapper tagRelationshipMapper;
}