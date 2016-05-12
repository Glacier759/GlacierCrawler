package com.glacier.crawler.utils;

import java.util.regex.Pattern;

/**
 * Created by Glacier on 16/5/5.
 */
public class Tmp {

    public static void main(String[] args) {
        String url = "http://blog.csdn.net/blogdevteam/article/details/22283977";
        String pattern = "http://blog.csdn.net/([^\\s]*)/([^\\s]*)/([^\\s]*)/([0-9]{8})";
        System.out.println(Pattern.matches(pattern, url));
    }

}
