package com.glacier.crawler.login;

import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Glacier on 16/4/26.
 */
public class Login {

    protected String loginURL;

    protected List<NameValuePair> params = new ArrayList<NameValuePair>();
    protected List<Header> headers = new ArrayList<Header>();
    protected boolean useCookie = false;


    public void addHeader(String key, String value) {
        headers.add(new BasicHeader(key, value));
    }

    public List<Header> getHeaders() {
        return this.headers;
    }

    public void useCookie() {
        this.useCookie = true;
    }

    public void withoutCookie() {
        this.useCookie = false;
    }

    public boolean isUseCookie() {
        return this.useCookie;
    }

    public void addCookie(String cookie) {
        this.addHeader("Cookie", cookie);
    }

    public void setParams(Map<String, String> params) {
        this.params.clear();
        for (String key : params.keySet()) {
            this.params.add(new BasicNameValuePair(key, params.get(key)));
        }
    }

    public void addParam(String key, String value) {
        params.add(new BasicNameValuePair(key, value));
    }

    public void addParams(Map<String, String> params) {
        for (String key : params.keySet()) {
            this.params.add(new BasicNameValuePair(key, params.get(key)));
        }
    }

    public String getLoginURL() {
        return loginURL;
    }

    public void setLoginURL(String loginURL) {
        this.loginURL = loginURL;
    }

    public List<NameValuePair> getParams() {
        return this.params;
    }
}
