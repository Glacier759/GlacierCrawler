package com.glacier.crawler.service.impl;

import com.glacier.crawler.dao.CrawlerConfigMapper;
import com.glacier.crawler.dao.CrawlerTemplateMapper;
import com.glacier.crawler.model.CrawlerConfigWithBLOBs;
import com.glacier.crawler.model.CrawlerTemplate;
import com.glacier.crawler.service.CrawlerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created by Glacier on 16/5/10.
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Resource
    CrawlerConfigMapper configMapper;
    @Resource
    CrawlerTemplateMapper templateMapper;

    @Override
    public int deleteCrawlerConfig(String crawlerName) {
        return configMapper.deleteByCrawlerName(crawlerName);
    }

    @Override
    public int insertCrawlerConfig(CrawlerConfigWithBLOBs record) {
        return configMapper.insertSelective(record);
    }

    @Override
    public CrawlerConfigWithBLOBs selectByCrawlerName(String crawlerName) {
        return configMapper.selectByCrawlerName(crawlerName);
    }

    @Override
    public int updateCrawlerConfig(CrawlerConfigWithBLOBs record) {
        return configMapper.updateCrawlerConfigByName(record);
    }

    @Override
    public int updateCrawlerConfigByID(CrawlerConfigWithBLOBs record) {
        return configMapper.updateByPrimaryKeySelective(record);
    }

    @Override
    public int deleteTemplate(Long id) {
        return templateMapper.deleteByPrimaryKey(id);
    }

    @Override
    public int insertTemplate(CrawlerTemplate record) {
        return templateMapper.insertSelective(record);
    }

    @Override
    public CrawlerTemplate selectTemplate(Long id) {
        return templateMapper.selectByPrimaryKey(id);
    }

    @Override
    public int updateTemplate(CrawlerTemplate record) {
        return templateMapper.updateByPrimaryKeySelective(record);
    }
}
