package com.water.crawl.db.controller;

import com.water.crawl.db.model.ITArticle;
import com.water.crawl.db.service.article.ICSDNArticleService;
import com.water.es.api.Service.IArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.UUID;

/**
 *
 * Created by zhangmiaojie on 2017/2/4.
 */
@RestController
public class TestController {

    @Resource
    private ICSDNArticleService icsdnArticleService;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    @RequestMapping(value = "/test")
    public String test() {
        ITArticle article = new ITArticle();
        article.setId(UUID.randomUUID().toString());
        article.setAuthor("mrwater");
        com.water.es.entry.ITArticle esArticle = new com.water.es.entry.ITArticle();
        BeanUtils.copyProperties(article, esArticle);
        esArticleService.addArticle(esArticle);
        return "test";
    }
}
