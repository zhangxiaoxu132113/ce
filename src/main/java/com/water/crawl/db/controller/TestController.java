package com.water.crawl.db.controller;

import com.alibaba.fastjson.JSONObject;
import com.water.crawl.core.CrawlBox;
import com.water.crawl.core.CrawlRule;
import com.water.crawl.core.CrawlRuleArray;
import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.db.service.ICSDNArticleService;
import com.water.crawl.utils.Constants;
import com.water.crawl.work.FetchArticleUrlCrawlTask;
import com.water.es.api.Service.IArticleService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@RestController
public class TestController {

    @Resource
    private ICSDNArticleService icsdnArticleService;

    @Resource(name = "esArticleService")
    private IArticleService esArticleService;

    @Resource
    private CacheManager cacheManager;

    @RequestMapping(value = "/test")
    public String test() {
//        new Thread(new FetchArticleUrlCrawlTask(cacheManager)).start();
        new Thread(new FetchArticleUrlCrawlTask(cacheManager)).start();
        return "test";
    }

    @RequestMapping(value = "/test1", method = RequestMethod.POST)
    public String test1(@RequestBody CrawlRule crawlRule) {
        String serverName = crawlRule.getId();
        String filePath = Constants.CRALWER_PATH + serverName + ".json";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String json = JSONObject.toJSONString(new CrawlRule[]{crawlRule});
        try {
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            os.write(json.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "success!";
    }

    @RequestMapping(value = "/getAllCrawler", method = RequestMethod.GET)
    public String getAllCrawler() {
        return JSONObject.toJSONString(CrawlBox.getCrawlRuleList());
    }

    public static void main(String[] args) {
        String serverName = "csdn.net";
        String filePath = Constants.CRALWER_PATH + serverName + ".json";
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        CrawlRule crawlRule = new CrawlRule();
        crawlRule.setId(serverName);
        crawlRule.setDescription("博客园");

        CrawlRuleArray crawlRuleArray = new CrawlRuleArray();
        crawlRuleArray.setMid("article");
        List<CrawlRule> crawlRules = new ArrayList<>();
        CrawlRule crawlRule1 = new CrawlRule();
        crawlRule1.setRule(new String[]{"#user-name"});
        crawlRule1.setId("usename");
        crawlRule1.setGet("text");
        crawlRules.add(crawlRule1);
        crawlRuleArray.setRuleList(crawlRules);
        crawlRule.setModule(new CrawlRuleArray[]{crawlRuleArray});

        String json = JSONObject.toJSONString(crawlRule);
        try {
            BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(file));
            os.write(json.getBytes());
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
