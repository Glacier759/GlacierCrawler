package com.glacier.crawler.model;

public class CrawlerConfigWithBLOBs extends CrawlerConfig {
    private String loginHeaders;

    private String loginCookie;

    private String loginParams;

    private String taskPatterns;

    private String taskStartUrl;

    public CrawlerConfigWithBLOBs() {
        this.setTaskThreadNum(4);
    }

    public String getLoginHeaders() {
        return loginHeaders;
    }

    public void setLoginHeaders(String loginHeaders) {
        this.loginHeaders = loginHeaders == null ? null : loginHeaders.trim();
    }

    public String getLoginCookie() {
        return loginCookie;
    }

    public void setLoginCookie(String loginCookie) {
        this.loginCookie = loginCookie == null ? null : loginCookie.trim();
    }

    public String getLoginParams() {
        return loginParams;
    }

    public void setLoginParams(String loginParams) {
        this.loginParams = loginParams == null ? null : loginParams.trim();
    }

    public String getTaskPatterns() {
        return taskPatterns;
    }

    public void setTaskPatterns(String taskPatterns) {
        this.taskPatterns = taskPatterns == null ? null : taskPatterns.trim();
    }

    public String getTaskStartUrl() {
        return taskStartUrl;
    }

    public void setTaskStartUrl(String taskStartUrl) {
        this.taskStartUrl = taskStartUrl == null ? null : taskStartUrl.trim();
    }
}