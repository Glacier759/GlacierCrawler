package com.glacier.crawler.downloader;

/**
 * Created by Glacier on 16/4/9.
 */
public interface Downloader {

    HttpClientConnection connect(String url);

    void setThread(int threadNum);

}
