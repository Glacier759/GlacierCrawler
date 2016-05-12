package com.glacier.crawler.dao;

import com.glacier.crawler.model.CrawlerTask;

import java.util.List;

public interface CrawlerTaskMapper {
    int deleteByPrimaryKey(String id);

    int insert(CrawlerTask record);

    int insertSelective(CrawlerTask record);

    CrawlerTask selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CrawlerTask record);

    int updateByPrimaryKey(CrawlerTask record);

    int deleteByTaskName(String taskName);

    CrawlerTask selectByTaskName(String taskName);

    List<CrawlerTask> selectAllCrawlerTask();

    int updateByTaskNameSelective(CrawlerTask record);

}