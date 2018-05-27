package com.water.ce.web;

import com.water.ce.cache.CacheManager;
import com.water.ce.web.service.*;
import com.water.ce.work.fetchTag.FetchTagTask;
import com.water.uubook.dao.TbCeFetchUrlMapper;
import com.water.uubook.dao.TbUbArticleMapper;
import com.water.uubook.model.TbCeFetchUrl;
import com.water.uubook.model.TbCeFetchUrlCriteria;
import com.water.uubook.model.TbUbArticle;
import com.water.uubook.model.TbUbArticleCriteria;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 *
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
    @Resource
    private InfoQService infoQService;
    @Resource
    private FanyiService fanyiService;
    @Resource
    private ChengxuyuanArticleService chengxuyuanArticleService;

    @RequestMapping(value = "/test")
    public String test() {
//        osChinaService.fetchSoftwareUpdateNews();
//        csdnCrawlingArticleService.handle();
//        osChinaService.handle();
//        csdnCrawlingArticleService.handle();

//        infoQService.fetchToutiao();
        csdnCrawlingArticleService.fetchAllUrl("www.voidcn.com", "tag", 22, "抓取程序园文章");

//        chengxuyuanArticleService.handle();
        return "dd";
    }
}
