package com.glacier.crawler.utils;

import com.glacier.crawler.downloader.HttpClientDownloader;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.net.URL;
import java.util.regex.Pattern;

/**
 * Created by Glacier on 16/4/9.
 */
public class URLUtil {

    private static Pattern patternForProtocal = Pattern.compile("[\\w]+://");
    private static CloseableHttpClient httpClient = HttpClients.createDefault();
    private static RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(2000).setConnectTimeout(2000).build();

    public static String removeProtocol(String url) {
        return patternForProtocal.matcher(url).replaceAll("");
    }

    public static String getDomain(String url) {
        String domain = removeProtocol(url);
        int i = StringUtils.indexOf(domain, "/", 1);
        if (i > 0) {
            domain = StringUtils.substring(domain, 0, i);
        }
        return domain;
    }

    public static String getURL(String str) {
        try {
            if (str.indexOf("http://") < 0 && str.indexOf("https://") < 0) {
                str = "http://" + str;
            }
            URL url = new URL(str);
            return url.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        URLUtil.getURL("www.baidu.com");
    }

    public static String getRealURL(String url) {
        HttpClientDownloader downloader = new HttpClientDownloader();
        try {
            return downloader.connect(url).getRealURL();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return url;
    }

}
