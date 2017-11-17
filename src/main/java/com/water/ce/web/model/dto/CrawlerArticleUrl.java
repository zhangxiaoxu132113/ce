package com.water.ce.web.model.dto;

import com.xpush.serialization.protobuf.ProtoEntity;
import com.xpush.serialization.protobuf.ProtoMember;

/**
 * Created by zhang miaojie on 2017/11/17.
 */
public class CrawlerArticleUrl extends ProtoEntity {
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
}
