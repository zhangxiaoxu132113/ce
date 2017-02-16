package com.water.test;

import redis.clients.jedis.Jedis;

/**
 * Created by zhangmiaojie on 2017/2/13.
 */
public class TestJedis01 {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.set("username","mrwater");
    }
}
