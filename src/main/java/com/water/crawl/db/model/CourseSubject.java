package com.water.crawl.db.model;

import java.io.Serializable;

public class CourseSubject implements Serializable {
    private String id;

    private String name;

    private String description;

    private String partentId;

    private Long updateTime;

    private Long createOn;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getPartentId() {
        return partentId;
    }

    public void setPartentId(String partentId) {
        this.partentId = partentId == null ? null : partentId.trim();
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCreateOn() {
        return createOn;
    }

    public void setCreateOn(Long createOn) {
        this.createOn = createOn;
    }
}