package com.water.crawl.db.model;

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
}
