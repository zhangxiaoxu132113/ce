package com.water.crawl.db.model.dto;

import com.xpush.serialization.protobuf.ProtoEntity;
import com.xpush.serialization.protobuf.ProtoMember;

/**
 * Created by zhangmiaojie on 2017/2/10.
 */
public class CrawlKeyword extends ProtoEntity {
    @ProtoMember(1)
    private String id;

    @ProtoMember(2)
    private String keyword;

    @ProtoMember(3)
    private String pv;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }
}
