package com.glacier.crawler.dao;

import com.glacier.crawler.model.CrawlerConfig;
import com.glacier.crawler.model.CrawlerConfigWithBLOBs;

public interface CrawlerConfigMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CrawlerConfigWithBLOBs record);

    int insertSelective(CrawlerConfigWithBLOBs record);

    CrawlerConfigWithBLOBs selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CrawlerConfigWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(CrawlerConfigWithBLOBs record);

    int updateByPrimaryKey(CrawlerConfig record);

    int deleteByCrawlerName(String taskName);

    CrawlerConfigWithBLOBs selectByCrawlerName(String taskName);

    int updateCrawlerConfigByName(CrawlerConfig record);
}