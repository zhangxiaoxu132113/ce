package com.water.ce.web.service;

/**
 * Created by mrwater on 2017/11/25.
 */
public interface OSChinaService extends CrawlingArticleService {
    /**
     * 抓取最新资讯
     */
    void fetchSoftwareUpdateNews();
}
