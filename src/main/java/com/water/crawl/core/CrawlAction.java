package com.water.crawl.core;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mrwater on 2017/3/19.
 */
public abstract class CrawlAction {
    private String id;
    private String module;
    private String url;
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);
    private Log log = LogFactory.getLog(CrawlAction.class);

    public CrawlAction(String id, String module, String url) {
        this.id = id;
        this.module = module;
        this.url = url;
    }

    public CrawlAction(String id, String module) {
        this.id = id;
        this.module = module;
    }

    public void work() {
        if (StringUtils.isNotBlank(this.url)) {
            log.info("爬取链接 -> " + this.url);
            executorService.execute(new CrawlCallBack(CrawlBox.getCrawlKey(id, module), this.getUrl()) {
                @Override
                public void calBack(JsonObject obj, String targetUrl) {
                    action(obj, targetUrl);
                }
            });

        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return this.url;
    }

    public abstract void action(JsonObject obj, String targetUrl);
}
