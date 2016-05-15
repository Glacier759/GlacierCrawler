package com.glacier.crawler.service;

import com.glacier.crawler.model.CrawlerConfigWithBLOBs;
import com.glacier.crawler.model.CrawlerTemplate;

/**
 * Created by Glacier on 16/5/10.
 */
public interface CrawlerService {

    int deleteCrawlerConfig(String crawlerName);

    int insertCrawlerConfig(CrawlerConfigWithBLOBs record);

    CrawlerConfigWithBLOBs selectByCrawlerName(String crawlerName);

    int updateCrawlerConfig(CrawlerConfigWithBLOBs record);

    int updateCrawlerConfigByID(CrawlerConfigWithBLOBs record);

    int deleteTemplate(Long id);

    int insertTemplate(CrawlerTemplate record);

    CrawlerTemplate selectTemplate(Long id);

    int updateTemplate(CrawlerTemplate record);

}
