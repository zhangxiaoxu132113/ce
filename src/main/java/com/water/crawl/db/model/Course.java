package com.water.crawl.db.model;

import java.io.Serializable;

public class Course implements Serializable {
    private String id;

    private String articleId;

    private String courseSubjectId;

    private String partentId;

    private String title;

    private Byte sort;

    private Long updateTime;

    private Long createOn;

    private static final long serialVersionUID = 1L;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId == null ? null : articleId.trim();
    }

    public String getCourseSubjectId() {
        return courseSubjectId;
    }

    public void setCourseSubjectId(String courseSubjectId) {
        this.courseSubjectId = courseSubjectId == null ? null : courseSubjectId.trim();
    }

    public String getPartentId() {
        return partentId;
    }

    public void setPartentId(String partentId) {
        this.partentId = partentId == null ? null : partentId.trim();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public Byte getSort() {
        return sort;
    }

    public void setSort(Byte sort) {
        this.sort = sort;
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