package com.water.crawl.utils;

/**
 * Created by zhangmiaojie on 2016/12/2.
 */
public class Constant {

    /**
     * 文章按网站分类
     */
    public static enum ArticleCategory {
        NONE(0,"未分类"),
        IBM(1,"IBM"),           //IBM开发者社区
        CSDN(2,"CSDN"),         //CSDN
        OS_CHINA(3,"oschina");  //开源中国
        public static String getName(int index) {
            for (ArticleCategory item : ArticleCategory.values()) {
                if (item.getIndex() == index) {
                    return item.name;
                }
            }
            return null;
        }
        private ArticleCategory(int index,String name) {
            this.index=index;
            this.name=name;
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
