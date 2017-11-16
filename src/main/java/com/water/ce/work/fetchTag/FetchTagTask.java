package com.water.ce.work.fetchTag;

import com.water.ce.cache.CacheManager;
import com.water.ce.utils.http.HttpRequestTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.annotation.Resource;

/**
 * Created by zhangmiaojie on 2017/8/15.
 */
public class FetchTagTask implements Runnable {
    private String url;
    private final static String QUEUE_TAG = "queue_tag_%s";

    @Resource
    private CacheManager cacheManager;

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
            String category = "";
            Elements categoeyEles = doc.select(".category_r > label > span");
            if (categoeyEles != null && categoeyEles.size() == 1) {
                category = categoeyEles.get(0).ownText();
                logger.info(category);
            }
            if (StringUtils.isNotBlank(category)) {
                String queueName = String.format(QUEUE_TAG, category);
                Elements tagEles = doc.select(".link_categories > a");
                if (tagEles != null && tagEles.size() > 0) {
                    for (Element tagEle : tagEles) {
                        String tag = tagEle.ownText();
                        cacheManager.sadd(queueName, tag);
                        logger.info(tag);
                    }
                }
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
