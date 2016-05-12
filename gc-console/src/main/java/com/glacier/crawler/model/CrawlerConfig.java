package com.glacier.crawler.model;

public class CrawlerConfig {
    private Long id;

    private String taskName;

    private String loginUrl;

    private Boolean loginUseCookie;

    private String taskType;

    private Integer taskThreadNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName == null ? null : taskName.trim();
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl == null ? null : loginUrl.trim();
    }

    public Boolean getLoginUseCookie() {
        return loginUseCookie;
    }

    public void setLoginUseCookie(Boolean loginUseCookie) {
        this.loginUseCookie = loginUseCookie;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType == null ? null : taskType.trim();
    }

    public Integer getTaskThreadNum() {
        return taskThreadNum;
    }

    public void setTaskThreadNum(Integer taskThreadNum) {
        this.taskThreadNum = taskThreadNum;
    }
}