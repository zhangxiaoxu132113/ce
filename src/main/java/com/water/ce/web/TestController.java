package com.water.ce.web;

import com.water.ce.cache.CacheManager;
import com.water.ce.web.service.IBMCrawlingArticleService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by admin on 2017/11/17.
 */
@RestController
public class TestController {
    @Resource
    private CacheManager cacheManager;

    @Resource
    private IBMCrawlingArticleService ibmCrawlingArticleService;

    @RequestMapping(value = "/test")
    public String test() {
        ibmCrawlingArticleService.handle();
        return "dd";
    }
}
