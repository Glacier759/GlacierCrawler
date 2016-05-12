package com.glacier.crawler.entity;

import com.glacier.crawler.crawler.Crawler;
import com.glacier.crawler.utils.URLUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Glacier on 16/4/9.
 */
public class Site {

    private String domain, userAgent;
    private boolean useGzip;
    private int retryTimes;
    private Map<String,String> cookies = new HashMap<String, String>();

    private static Map<String, Site> sites = new HashMap<String, Site>();

    public static Site getSite(String url) {
        String domain = URLUtil.getDomain(url);
        if (domain == null) {
            return null;
        }
        Site site = sites.get(domain);
        if (site == null) {
            synchronized (Crawler.class) {
                site = sites.get(domain);
                if (site == null) {
                    site = new Site(domain);
                    sites.put(domain, site);
                }
            }
        }
        return site;
    }

    private Site(String domain){
        this.domain = domain;
    }



    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDomain() {
        return domain;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public boolean isUseGzip() {
        return useGzip;
    }

    public void setUseGzip(boolean useGzip) {
        this.useGzip = useGzip;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Map<String, String> getCookies() {
        return cookies;
    }

    public void setCookies(Map<String, String> cookies) {
        this.cookies = cookies;
    }

//    public Map<String,Map<String, String>> getAllCookies() {
//
//    }

}
