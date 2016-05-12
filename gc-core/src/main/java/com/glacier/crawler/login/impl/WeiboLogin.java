package com.glacier.crawler.login.impl;

import com.glacier.crawler.login.Login;
import org.apache.http.message.BasicNameValuePair;

/**
 * Created by Glacier on 16/4/26.
 */
public class WeiboLogin extends Login {

    public WeiboLogin() {
        this.params.clear();
        params.add(new BasicNameValuePair("", ""));
    }

    public void setAccount(String username, String password) {
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
    }

}
