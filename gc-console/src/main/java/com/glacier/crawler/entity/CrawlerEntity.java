package com.glacier.crawler.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Glacier on 16/5/10.
 */
public class CrawlerEntity {

    private String crawler_name;
    private String login_url;
    private Map<String, String> login_headers = new HashMap<>();
    private String login_cookie;
    private Boolean login_useCookie;
    private Map<String, String> login_params = new HashMap<>();

    private Map<String, String> task_pattern = new HashMap<>();
    private String task_type;
    private Integer task_thread_num;
    private List<String> task_start_url = new ArrayList<>();

    private boolean canLogin = false;

    public void canLogin(boolean canLogin) {
        this.canLogin = canLogin;
    }

    public boolean canLogin() {
        return this.canLogin;
    }

    public String getCrawler_name() {
        return crawler_name;
    }

    public void setCrawler_name(String crawler_name) {
        this.crawler_name = crawler_name;
    }

    public String getLogin_url() {
        return login_url;
    }

    public void setLogin_url(String login_url) {
        this.login_url = login_url;
    }

    public Map<String, String> getLogin_headers() {
        return login_headers;
    }

    public void setLogin_headers(Map<String, String> login_headers) {
        this.login_headers = login_headers;
    }

    public void addHeaders(String key, String value) {
        this.login_headers.put(key, value);
    }

    public String getLogin_cookie() {
        return login_cookie;
    }

    public void setLogin_cookie(String login_cookie) {
        this.login_cookie = login_cookie;
    }

    public boolean isLogin_useCookie() {
        return login_useCookie;
    }

    public void setLogin_useCookie(Boolean login_useCookie) {
        if (login_useCookie == null) {
            this.login_useCookie = false;
        }
        else {
            this.login_useCookie = login_useCookie;
        }
    }

    public Map<String, String> getLogin_params() {
        return login_params;
    }

    public void setLogin_params(Map<String, String> login_params) {
        this.login_params = login_params;
    }

    public void addParams(String key, String value) {
        this.login_params.put(key, value);
    }

    public Map<String, String> getTask_pattern() {
        return task_pattern;
    }

    public void setTask_pattern(Map<String, String> task_pattern) {
        this.task_pattern = task_pattern;
    }

    public void addTaskPattern(String key, String value) {
        this.task_pattern.put(key, value);
    }

    public String getTask_type() {
        return task_type;
    }

    public void setTask_type(String task_type) {
        this.task_type = task_type;
    }

    public Integer getTask_thread_num() {
        return task_thread_num;
    }

    public void setTask_thread_num(Integer task_thread_num) {
        this.task_thread_num = task_thread_num;
    }

    public List<String> getTask_start_url() {
        return task_start_url;
    }

    public void setTask_start_url(List<String> task_start_url) {
        this.task_start_url = task_start_url;
    }

    public void addTaskStartUrl(String startUrl) {
        this.task_start_url.add(startUrl);
    }
}
