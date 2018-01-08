package com.water.ce.web.service;

/**
 * Created by mrwater on 2017/12/9.
 */
public interface InfoQService extends CrawlingArticleService {
    /**
     * 抓取头条
     */
    void fetchToutiao();
}
