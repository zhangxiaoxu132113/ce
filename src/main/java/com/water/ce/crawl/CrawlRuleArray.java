package com.water.ce.crawl;

import java.util.List;

/**
 * Created by mrwater on 2017/3/18.
 */
public class CrawlRuleArray {
    private String mid;
//    private
//    private String returnType;
    private List<CrawlRule> ruleList;

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

//    public String getReturnType() {
//        return returnType;
//    }
//
//    public void setReturnType(String returnType) {
//        this.returnType = returnType;
//    }

    public List<CrawlRule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<CrawlRule> ruleList) {
        this.ruleList = ruleList;
    }
}
