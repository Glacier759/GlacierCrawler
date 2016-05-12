package com.glacier.crawler.service.impl;

import com.glacier.crawler.dao.CrawlerTaskMapper;
import com.glacier.crawler.model.CrawlerTask;
import com.glacier.crawler.service.SystemService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by Glacier on 16/5/10.
 */

@Service
public class SystemServiceImpl implements SystemService  {

    @Resource
    private CrawlerTaskMapper crawlerTaskMapper;

    @Override
    public int deleteCrawlerTask(String taskName) {
        return crawlerTaskMapper.deleteByTaskName(taskName);
    }

    @Override
    public int insertCrawlerTask(CrawlerTask record) {
        return crawlerTaskMapper.insertSelective(record);
    }

    @Override
    public CrawlerTask selectCrawlerTask(String taskName) {
        return crawlerTaskMapper.selectByTaskName(taskName);
    }

    @Override
    public List<CrawlerTask> selectAllCrawlerTask() {
        return crawlerTaskMapper.selectAllCrawlerTask();
    }

    @Override
    public int updateCrawlerTask(CrawlerTask record) {
        return crawlerTaskMapper.updateByTaskNameSelective(record);
    }
    @Override
    public int updateCrawlerTaskByID(CrawlerTask record) {
        return crawlerTaskMapper.updateByPrimaryKeySelective(record);
    }
}
