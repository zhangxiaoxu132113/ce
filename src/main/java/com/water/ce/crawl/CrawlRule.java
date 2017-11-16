package com.water.ce.crawl;

/**
 * Created by mrwater on 2017/3/18.
 */
public class CrawlRule {
    private String id;
    private String description;
    private CrawlRuleArray[] module;
    private String[] rule;
    private String get;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CrawlRuleArray[] getModule() {
        return module;
    }

    public void setModule(CrawlRuleArray[] module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getRule() {
        return rule;
    }

    public void setRule(String[] rule) {
        this.rule = rule;
    }

    public String getGet() {
        return get;
    }

    public void setGet(String get) {
        this.get = get;
    }
}
