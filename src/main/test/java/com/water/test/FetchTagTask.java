package com.water.test;

import com.water.crawl.utils.http.HttpRequestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 抓取页面的标签
 * Created by zhangmiaojie on 2017/8/14.
 */
public class FetchTagTask implements Runnable {
    private String url;
    private Log logger = LogFactory.getLog(FetchTagTask.class);

    public FetchTagTask(){
    }

    public FetchTagTask(String url1){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public void run() {
        System.out.println(getUrl());
        System.out.println(Thread.currentThread().getName());
        if (StringUtils.isBlank(getUrl())) {
            throw new RuntimeException("传入的根连接不能为空！");
        }
        if (getUrl().contains("/article/details")) {
            String content = (String) HttpRequestTool.getRequest(getUrl());
            Document doc = Jsoup.parse(content);
            Elements tagEles = doc.select(".link_categories > a");
            Elements categoeyEles = doc.select(".category_r > label > span");
            if (tagEles != null && tagEles.size() > 0) {
                for (Element tagEle : tagEles) {
                    String tag = tagEle.ownText();
                    logger.info(tag);
                }
            }

            if (categoeyEles != null && categoeyEles.size() == 1) {
                String category = categoeyEles.get(0).ownText();
                logger.info(category);
            }
        }
    }

    public static void main(String[] args) {
        FetchTagTask fetchTagTask = new FetchTagTask();
        fetchTagTask.setUrl("http://blog.csdn.net/cen_cs/article/details/46010523");
        Thread t1 = new Thread(fetchTagTask);
        t1.start();
    }
    
}
