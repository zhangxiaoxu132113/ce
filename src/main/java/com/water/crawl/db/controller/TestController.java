package com.water.crawl.db.controller;

import com.google.protobuf.InvalidProtocolBufferException;
import com.water.crawl.core.cache.CacheManager;
import com.water.crawl.db.model.dto.CrawlKeyword;
import com.xpush.serialization.protobuf.ProtoEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.ShardedJedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangmiaojie on 2017/2/4.
 */
@RestController
public class TestController {

    @Resource
    private CacheManager cacheManager;

    @RequestMapping(value = "/test")
    public String test() throws InvalidProtocolBufferException, IllegalAccessException, InstantiationException {
        ShardedJedis redis = cacheManager.getShardedJedis();
        List<ProtoEntity> keywordList = new ArrayList<ProtoEntity>();
        CrawlKeyword keyword = new CrawlKeyword();
        keyword.setId("132113");
        keyword.setKeyword("mrwater");
        keyword.setPv("12");
        keywordList.add(keyword);
        redis.lpush("baidu_queue".getBytes(),keyword.toByteArray());


        List<byte[]> bytes = redis.lrange("baidu_queue".getBytes(), 0, -1);
        for (byte[] data : bytes) {
            keyword = (CrawlKeyword)ProtoEntity.parseFrom(keyword,data);
            System.out.println(keyword.getKeyword());
        }
        return "HelleWorld";
    }
}
