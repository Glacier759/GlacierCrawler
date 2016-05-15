package com.glacier.crawler.utils;

import com.glacier.crawler.downloader.Downloader;
import com.glacier.crawler.downloader.HttpClientDownloader;
import com.glacier.crawler.entity.Page;

/**
 * Created by Glacier on 16/5/5.
 */
public class Tmp {

    public static void main(String[] args) {
        try {
            Downloader downloader = new HttpClientDownloader();
            Page page = downloader.connect("http://www.kooaoo.com/").get();
            System.out.println(page.getDocument());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}
