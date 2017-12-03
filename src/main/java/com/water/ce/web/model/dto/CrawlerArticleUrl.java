package com.water.ce.web.model.dto;

import com.xpush.serialization.protobuf.ProtoEntity;
import com.xpush.serialization.protobuf.ProtoMember;

/**
 * Created by zhang miaojie on 2017/11/17.
 */
public class CrawlerArticleUrl extends ProtoEntity {
    public CrawlerArticleUrl(){
    }

    public CrawlerArticleUrl(String url) {
        this.url = url;
    }

    public CrawlerArticleUrl(String url, Integer category, Integer module, String webSite, String webSiteModule, Integer origin) {
        this.url = url;
        this.category = category;
        this.module = module;
        this.webSite = webSite;
        this.webSiteModule = webSiteModule;
        this.origin = origin;
    }

    /**
     * url主键
     */
    @ProtoMember(1)
    private String urlId;

    /**
     * url链接
     */
    @ProtoMember(2)
    private String url;

    /**
     * 分类
     */
    @ProtoMember(3)
    private Integer category;

    /**
     * 所属模块
     */
    @ProtoMember(4)
    private Integer module;

    @ProtoMember(5)
    private String webSite;

    @ProtoMember(6)
    private String webSiteModule;

    @ProtoMember(7)
    private Integer origin;

    public String getUrlId() {
        return urlId;
    }

    public void setUrlId(String urlId) {
        this.urlId = urlId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getCategory() {
        return category;
    }

    public void setCategory(Integer category) {
        this.category = category;
    }

    public Integer getModule() {
        return module;
    }

    public void setModule(Integer module) {
        this.module = module;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getWebSiteModule() {
        return webSiteModule;
    }

    public void setWebSiteModule(String webSiteModule) {
        this.webSiteModule = webSiteModule;
    }

    public Integer getOrigin() {
        return origin;
    }

    public void setOrigin(Integer origin) {
        this.origin = origin;
    }
}
