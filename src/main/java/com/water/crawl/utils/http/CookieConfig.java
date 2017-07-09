package com.water.crawl.utils.http;

import java.util.Date;
import java.util.Map;

/**
 * Created by zhangmiaojie on 2017/2/22.
 */
public class CookieConfig {
    private Map<String, String> cookirMap;
    private String domain;
    private Date expiryDate;

    public Map<String, String> getCookirMap() {
        return cookirMap;
    }

    public void setCookirMap(Map<String, String> cookirMap) {
        this.cookirMap = cookirMap;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
