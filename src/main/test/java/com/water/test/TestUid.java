package com.water.test;

import java.util.Random;

/**
 * Created by zhangmiaojie on 2017/2/15.
 */
public class TestUid {
    public static void main(String[] args) {

        System.out.println(randomUid());
    }

    /**
     * 随机生成10位的用户ID
     *
     * @return
     */
    public static long randomUid() {
        long max = 9999999999L;
        long min = 1000000000L;
        Random random = new Random();
        long id = random.nextInt((int) max) % (max - min + 1) + min;
        return Math.abs(id);
    }
}
