package com.water.crawl.core;

import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by mrwater on 2017/3/19.
 */
public abstract class CrawlAction<T> {
    private String id;
    private String module;
    private String url;
    private static ExecutorService executorService = Executors.newFixedThreadPool(20);

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
            executorService.execute(new CrawlCallBack(CrawlBox.getCrawlKey(id, module), url) {
                @Override
                public void calBack(JsonObject obj) {
                    action(obj);
                }
            });

        }
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public abstract void action(JsonObject obj);
}
