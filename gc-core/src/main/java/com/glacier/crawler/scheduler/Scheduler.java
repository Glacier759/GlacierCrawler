package com.glacier.crawler.scheduler;

import com.glacier.crawler.entity.Task;

/**
 * Created by Glacier on 16/4/9.
 */
public abstract class Scheduler {

    protected String key = "crawler";

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return this.key;
    }

    public abstract int count();

    public abstract void clear();

    public abstract void push(Task task);

    public abstract Task poll();

    public String toString() {
        return "abstract";
    }

}
