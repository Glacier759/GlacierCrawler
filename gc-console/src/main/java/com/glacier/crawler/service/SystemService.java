package com.glacier.crawler.service;

import com.glacier.crawler.model.CrawlerTask;

import java.util.List;

/**
 * Created by Glacier on 16/5/10.
 */
public interface SystemService {

    int deleteCrawlerTask(String taskName);

    int insertCrawlerTask(CrawlerTask record);

    CrawlerTask selectCrawlerTask(String taskName);

    List<CrawlerTask> selectAllCrawlerTask();

    int updateCrawlerTask(CrawlerTask record);

    int updateCrawlerTaskByID(CrawlerTask record);
}
