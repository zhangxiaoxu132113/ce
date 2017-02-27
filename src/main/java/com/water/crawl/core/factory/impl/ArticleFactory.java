package com.water.crawl.core.factory.impl;


import com.water.crawl.core.factory.IArticleFactory;

/**
 * Created by zhangmiaojie on 2017/2/27.
 */
public class ArticleFactory {

    public static IArticleFactory build(int module) {
        IArticleFactory articleFactory = null;
        switch (module) {
            case 0:
                articleFactory = new IBMArticleFactory();
                break;
            case 1:
                articleFactory = new CSDNArticleFactory();
                break;
            default:
                throw new RuntimeException("没有这个类型的工厂!");
        }
        return articleFactory;
    }

    public static class FactoryConfig {
        public static int IBM = 0;
        public static int CSDN = 1;
    }
}
