package com.glacier.crawler.crawler;

import com.glacier.crawler.downloader.HttpClientDownloader;
import com.glacier.crawler.entity.Page;
import com.glacier.crawler.login.Login;
import com.glacier.crawler.pipeline.Pipeline;
import com.glacier.crawler.processor.PageProcessor;
import com.glacier.crawler.scheduler.RedisScheduler;
import com.glacier.crawler.utils.ReturnResult;
import org.apache.http.HttpStatus;

/**
 * Created by Glacier on 16/4/26.
 */
public class BaseCrawler extends Crawler {

    public BaseCrawler() {
        super();
        downloader = new HttpClientDownloader();
        pageProcessor = new PageProcessor();
        pipeline = new Pipeline();
        scheduler = new RedisScheduler();
    }

    @Override
    public String login(Login login) throws Exception {
        if (login == null) {
            return ReturnResult.toString(ReturnResult.FAILED, "Login is not init");
        }
        downloader.setThread(threadNum);
        Page page = null;
        if (login.isUseCookie()) {
            page = downloader.connect(login.getLoginURL())
                    .addHeaders(login.getHeaders())
                    .get();
        }
        else {
            page = downloader.connect(login.getLoginURL())
                    .addParams(login.getParams())
                    .addHeaders(login.getHeaders())
                    .post();
        }
        if (page.getStatusCode() == HttpStatus.SC_OK) {
            return ReturnResult.toString(ReturnResult.SUCCESS, page.getRawText());
        }
        return ReturnResult.toString(ReturnResult.FAILED, page.getRawText());
    }



}
