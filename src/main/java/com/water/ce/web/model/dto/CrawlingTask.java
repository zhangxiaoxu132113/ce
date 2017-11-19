package com.water.ce.web.model.dto;

import com.xpush.serialization.protobuf.ProtoEntity;
import com.xpush.serialization.protobuf.ProtoMember;

import java.util.List;

/**
 * Created by zhangmiaojie on 2016/12/2.
 * 爬虫任务
 */
public class CrawlingTask extends ProtoEntity{
    @ProtoMember(1)
    private String taskId;

    @ProtoMember(2)
    private String webSite;

    @ProtoMember(3)
    private String module;

    private List<ProtoEntity> datas;

    private long taskLen;

    public String getTaskId() {
        return taskId;
    }

    public CrawlingTask setTaskId(String taskId) {
        this.taskId = taskId;
        return this;
    }

    public List<ProtoEntity> getDatas() {
        return datas;
    }

    public CrawlingTask setDatas(List<ProtoEntity> datas) {
        this.datas = datas;
        return this;
    }

    public long getTaskLen() {
        return taskLen;
    }

    public void setTaskLen(long taskLen) {
        this.taskLen = taskLen;
    }

    public String getWebSite() {
        return webSite;
    }

    public void setWebSite(String webSite) {
        this.webSite = webSite;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
