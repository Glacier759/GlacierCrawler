package com.glacier.crawler.entity;

import com.glacier.crawler.processor.PageProcessor;
import com.glacier.crawler.utils.URLUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Glacier on 16/5/5.
 */
public class Task implements Serializable, Cloneable {

    private Task parent = null;
    private String pageProcessor = "com.glacier.crawler.crawler.BaseCrawler";
    private String url;

    public final static int TYPE_TASK_ALL = 0;
    public final static int TYPE_TASK_QUEUE = 1;
    public final static int TYPE_TASK_DOMAIN = 2;
    public final static int TYPE_TASK_PROCESSOR = 3;
    public final static int TYPE_TASK_PATTERN = 4;

    protected int crawlerType = TYPE_TASK_QUEUE;
    protected Map<String, String> patternsAndProcessors = new HashMap<String, String>();

    public Task(String url) {
        this.url = URLUtil.getURL(url);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getCrawlerType() {
        return crawlerType;
    }

    public void setCrawlerType(int crawlerType) {
        this.crawlerType = crawlerType;
    }

    public Map<String, String> getPatterns() {
        return patternsAndProcessors;
    }

    public void addPattern(String pattern) {
        patternsAndProcessors.put(pattern, pageProcessor);
    }

    public void addPattern(String pattern, PageProcessor pageProcessor) {
        patternsAndProcessors.put(pattern, pageProcessor.getClass().getName());
    }

    public void addPattern(String pattern, String processorClass) {
        patternsAndProcessors.put(pattern, processorClass);
    }

    public void setProcessorClass(String processorClass) {
        this.pageProcessor = processorClass;
    }

    public Task getParent() {
        return parent;
    }

    public void setParent(Task parent) {
        parent = null;
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "Task{" +
                "parent=" + parent +
                ", pageProcessor='" + pageProcessor + '\'' +
                ", url='" + url + '\'' +
                ", crawlerType=" + crawlerType +
                ", patternsAndProcessors=" + patternsAndProcessors +
                '}';
    }

    @Override
    public Task clone() throws CloneNotSupportedException {
        return (Task) super.clone();
    }
}
