package com.water.crawl.utils;

/**
 * Created by zhangmiaojie on 2016/12/2.
 */
public class Constant {

    /**
     * 文章来源的网站
     */
    public static enum Article_ORIGIN {
        IBM(1, "www.ibm.com"),
        CSDN(2, "www.csdn.net"),
        OS_CHINA(3, "www.oschina.net"),
        OPEN_OPEN(4,"www.open-open.com"),
        OTHER(10001,"other"),
        MY_WEBSITE(0, "www.uubook.net");

        public static String getName(int index) {
            for (Article_ORIGIN item : Article_ORIGIN.values()) {
                if (item.getIndex() == index) {
                    return item.name;
                }
            }
            return null;
        }

        private Article_ORIGIN(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private int index;
        private String name;

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * 文章分类
     */
    public static enum ARTICLE_CATEOGRY {
        BLOG(0,"技术博文"),
        ZI_XUN(1,"资讯"),
        TOU_TIAO(2,"头条"),
        JIAO_CHENG(3,"教程"),
        ZHI_SHI_KU(4,"知识库");

        public static String getName(int index) {
            for (ARTICLE_CATEOGRY item : ARTICLE_CATEOGRY.values()) {
                if (item.getIndex() == index) {
                    return item.name;
                }
            }
            return null;
        }

        private ARTICLE_CATEOGRY(int index, String name) {
            this.index = index;
            this.name = name;
        }

        private int index;
        private String name;

        public int getIndex() {
            return index;
        }

        public String getName() {
            return name;
        }
    }
}
