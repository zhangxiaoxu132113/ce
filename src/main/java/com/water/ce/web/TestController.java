package com.water.ce.web;

import com.water.ce.cache.CacheManager;
import com.water.ce.web.service.IBMCrawlingArticleService;
import com.water.ce.web.service.Open2OpenCrawlingArticleService;
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
    @Resource
    private Open2OpenCrawlingArticleService open2OpenCrawlingArticleService;

    @RequestMapping(value = "/test")
    public String test() {
        open2OpenCrawlingArticleService.handle();
        return "dd";
    }
}
