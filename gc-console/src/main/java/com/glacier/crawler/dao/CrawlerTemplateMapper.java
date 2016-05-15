package com.glacier.crawler.dao;

import com.glacier.crawler.model.CrawlerTemplate;

public interface CrawlerTemplateMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CrawlerTemplate record);

    int insertSelective(CrawlerTemplate record);

    CrawlerTemplate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CrawlerTemplate record);

    int updateByPrimaryKeyWithBLOBs(CrawlerTemplate record);
}