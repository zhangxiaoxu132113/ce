package com.water.crawl.db.controller;

import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.db.service.article.ICSDNArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 *
 * Created by zhangmiaojie on 2017/2/4.
 */
@RestController
public class TestController {

    @Resource
    private ICSDNArticleService icsdnArticleService;

    @RequestMapping(value = "/test")
    public String test() {
        icsdnArticleService.addAllLibCategory();
        return "test";
    }
}
