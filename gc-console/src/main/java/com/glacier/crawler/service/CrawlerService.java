package com.glacier.crawler.service;

import com.glacier.crawler.model.CrawlerConfig;
import com.glacier.crawler.model.CrawlerConfigWithBLOBs;

/**
 * Created by Glacier on 16/5/10.
 */
public interface CrawlerService {

    int deleteCrawlerConfig(String crawlerName);

    int insertCrawlerConfig(CrawlerConfigWithBLOBs record);

    CrawlerConfigWithBLOBs selectByCrawlerName(String crawlerName);

    int updateCrawlerConfig(CrawlerConfigWithBLOBs record);

    int updateCrawlerConfigByID(CrawlerConfigWithBLOBs record);

}
