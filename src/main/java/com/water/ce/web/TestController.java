package com.water.ce.web;

import com.water.ce.cache.CacheManager;
import com.water.ce.web.service.CSDNBaseCrawlingArticleService;
import com.water.ce.web.service.IBMBaseCrawlingArticleService;
import com.water.ce.web.service.Open2OpenBaseCrawlingArticleService;
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
    private IBMBaseCrawlingArticleService ibmCrawlingArticleService;
    @Resource
    private Open2OpenBaseCrawlingArticleService open2OpenCrawlingArticleService;
    @Resource
    private CSDNBaseCrawlingArticleService csdnCrawlingArticleService;

    @RequestMapping(value = "/test")
    public String test() {
        csdnCrawlingArticleService.handle();
        return "dd";
    }
}
