package com.glacier.crawler.downloader;

import com.glacier.crawler.entity.Page;
import com.glacier.crawler.entity.Site;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Glacier on 16/4/9.
 */
public class HttpClientConnection {

    private String taskURL;
    private Map<String, CloseableHttpClient> httpClients = new HashMap<String, CloseableHttpClient>();

    private List<NameValuePair> params = new ArrayList<NameValuePair>();

    private HttpClientGenerator httpClientGenerator = new HttpClientGenerator();
//    private HttpGet httpGet = new HttpGet();

    private List<Header> headers = new ArrayList<Header>();
    private String userAgent;
    private Map<String, String> cookies = new HashMap<String, String>();

    private CloseableHttpClient getHttpClient(Site site) {
        String domain = site.getDomain();
        if (domain == null) {
            return httpClientGenerator.getClient(null);
        }
        CloseableHttpClient httpClient = httpClients.get(domain);
        if (httpClient == null) {
            synchronized (this) {
                httpClient = httpClients.get(domain);
                if (httpClient == null) {
                    httpClient = httpClientGenerator.getClient(site);
                    httpClients.put(domain, httpClient);
                }
            }
        }
        return httpClient;
    }

    public void setTask(String url) {
        this.taskURL = url;
    }

    public void setThread(int threadNum) {
        httpClientGenerator.setPoolSize(threadNum);
    }

    public HttpClientConnection setHeader(Header header) {
        this.headers.clear();
        this.headers.add(header);
        return this;
    }

    public HttpClientConnection setHeader(String key, String value) {
        this.headers.clear();
        this.headers.add(new BasicHeader(key, value));
        return this;
    }

    public HttpClientConnection setHeaders(Map<String, String> headers) {
        this.headers.clear();
        for (String key : headers.keySet()) {
            this.headers.add(new BasicHeader(key, headers.get(key)));
        }
        return this;
    }

    public HttpClientConnection setHeaders(List<Header> headers) {
        this.headers.clear();
        for ( Header header : headers ) {
            this.headers.add(header);
        }
        return this;
    }

    public HttpClientConnection addHeader(Header header) {
        this.headers.add(header);
        return this;
    }

    public HttpClientConnection addHeader(String key, String value) {
        this.headers.add(new BasicHeader(key, value));
        return this;
    }

    public HttpClientConnection addHeaders(Map<String, String> headers) {
        for ( String key : headers.keySet() ) {
            this.headers.add(new BasicHeader(key, headers.get(key)));
        }
        return this;
    }

    public HttpClientConnection addHeaders(List<Header> headers) {
        for ( Header header : headers ) {
            this.headers.add(header);
        }
        return this;
    }

    public HttpClientConnection userAgent(String userAgent) {
        this.userAgent = userAgent;
        return this;
    }

    public HttpClientConnection setParams(List<NameValuePair> params) {
        this.params.clear();
        this.params = params;
        return this;
    }

    public HttpClientConnection addParams(List<NameValuePair> params) {
        for (NameValuePair param : params) {
            this.params.add(param);
        }
        return this;
    }

    public HttpClientConnection addCookie(String cookies) {
        cookies = cookies.replaceAll(" ", "");
        for ( String cookie : cookies.split(";") ) {
            String[] cookieArray = cookie.split("=");
            this.cookies.put(cookieArray[0], cookieArray[1]);
        }
        return this;
    }

    public HttpClientConnection addParam(NameValuePair param) {
        this.params.add(param);
        return this;
    }


    public Page get() throws Exception{
        Page page = new Page();
        Site site = Site.getSite(taskURL);
        site.setCookies(this.cookies);
        HttpGet httpGet = new HttpGet(taskURL);
        for (Header header : headers) {
            httpGet.addHeader(header);
        }
        CloseableHttpClient httpClient = getHttpClient(site);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setRawText(EntityUtils.toString(response.getEntity()));
        page.setBaseURL(taskURL);
        return page;
    }

    public Page post() throws Exception {
        Page page = new Page();
        Site site = Site.getSite(taskURL);
        HttpPost httpPost = new HttpPost(taskURL);
        httpPost.setEntity(new UrlEncodedFormEntity(this.params, "UTF-8"));
        CloseableHttpClient httpClient = getHttpClient(site);
        CloseableHttpResponse response = httpClient.execute(httpPost);
        page.setStatusCode(response.getStatusLine().getStatusCode());
        page.setRawText(EntityUtils.toString(response.getEntity()));
        page.setBaseURL(taskURL);
        return page;
    }

    public Page put() throws Exception {
        Page page = new Page();
        return page;
    }

    public Page delete() throws Exception {
        Page page = new Page();
        return page;
    }

}
