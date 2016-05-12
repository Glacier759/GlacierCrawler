package com.glacier.crawler.entity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Created by Glacier on 16/4/27.
 */
public class Page {

    private String baseURL;
    private Document document;
    private int statusCode;
    private String rawText;

    public Document getDocument() {
        document = Jsoup.parse(rawText);
        document.setBaseUri(baseURL);
        return document;
    }

    public String getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(String baseURL) {
        this.baseURL = baseURL;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getRawText() {
        return rawText;
    }

    public void setRawText(String rawText) {
        this.rawText = rawText;
    }
}
