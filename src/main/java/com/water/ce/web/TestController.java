package com.water.ce.web;

import com.water.ce.cache.CacheManager;
import com.water.ce.web.service.CSDNCrawlingArticleService;
import com.water.ce.web.service.IBMCrawlingArticleService;
import com.water.ce.web.service.OSChinaService;
import com.water.ce.web.service.Open2OpenCrawlingArticleService;
import com.water.ce.work.fetchTag.FetchTagTask;
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
    @Resource
    private CSDNCrawlingArticleService csdnCrawlingArticleService;
    @Resource
    private OSChinaService osChinaService;
    @Resource
    private FetchTagTask fetchTagTask;
    @RequestMapping(value = "/test")
    public String test() {
        ibmCrawlingArticleService.fetchAllUrl("open-open.com", "article", "深度开源");
//        open2OpenCrawlingArticleService.handle();
//        osChinaService.handle();
//        csdnCrawlingArticleService.handle();
        return "dd";
    }
}
