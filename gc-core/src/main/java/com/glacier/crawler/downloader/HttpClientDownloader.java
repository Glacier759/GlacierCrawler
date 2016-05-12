package com.glacier.crawler.downloader;

import com.glacier.crawler.utils.URLUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Glacier on 16/4/9.
 */
public class HttpClientDownloader implements Downloader {

    private Map<String, HttpClientConnection> connections = new HashMap<String, HttpClientConnection>();
    private HttpClientConnection connection = null;
    private int threadNum;

    private HttpClientConnection getConnection(String url) {
        String domain = URLUtil.getDomain(url);
        if (domain == null) {
            return null;
        }
        HttpClientConnection connection = connections.get(domain);
        if (connection == null) {
            synchronized (this) {
                connection = connections.get(domain);
                if (connection == null) {
                    connection = new HttpClientConnection();
                    connections.put(domain, connection);
                }
            }
        }
        return connection;
    }

    public HttpClientConnection connect(String url) {
        HttpClientConnection connection = getConnection(url);
        connection.setThread(threadNum);
        connection.setTask(url);
        this.connection = connection;
        return connection;
    }

    public void setThread(int threadNum) {
        this.threadNum = threadNum;
        if (connection != null) {
            connection.setThread(threadNum);
        }
        else {
            for ( String key : connections.keySet() ) {
                connections.get(key).setThread(threadNum);
            }
        }
    }

}
