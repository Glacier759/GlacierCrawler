package com.glacier.crawler.crawler;

import com.glacier.crawler.concurrent.CountableThreadPool;
import com.glacier.crawler.downloader.Downloader;
import com.glacier.crawler.downloader.HttpClientDownloader;
import com.glacier.crawler.entity.Page;
import com.glacier.crawler.entity.Task;
import com.glacier.crawler.login.Login;
import com.glacier.crawler.pipeline.Pipeline;
import com.glacier.crawler.processor.PageProcessor;
import com.glacier.crawler.scheduler.Scheduler;
import com.glacier.crawler.utils.URLUtil;
import org.apache.commons.io.FileUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

/**
 * Created by Glacier on 16/4/9.
 */
public abstract class Crawler implements Runnable {

    public final static int BASE = 0;
    protected final static int STAT_INIT = 10;
    protected final static int STAT_RUNNING = 11;
    protected final static int STAT_STOPPED = 12;

    protected Downloader downloader;
    protected PageProcessor pageProcessor;
    protected Pipeline pipeline;
    protected Scheduler scheduler;
    protected CountableThreadPool threadPool;
    protected int threadNum = 1;
    protected AtomicInteger status = new AtomicInteger(STAT_INIT);
    protected ExecutorService executorService;

    private String crawlerName;

    private ReentrantLock newUrlLock = new ReentrantLock();
    private Condition newUrlCondition = newUrlLock.newCondition();

    public void run() {
        checkRunningStat();
        initComponent();
        while(status.get() == STAT_RUNNING) {
            if (scheduler.count() == 0) {
                try {
                    System.out.println("["+this.crawlerName+"] - task queue is empty");
                    Thread.sleep(1000);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                Task task = scheduler.poll();
                final Task taskFinal = task;
                threadPool.execute(() -> {
                    try {
                        processTask(taskFinal);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        }
        shutdown();
    }
    public void start() {
        Thread thread = new Thread(this);
        thread.setDaemon(false);
        thread.start();
    }

    public void stop() {
        status.set(STAT_STOPPED);
        shutdown();
    }

    public void shutdown() {
        if ( threadPool != null ) {
            threadPool.shutdown();
        }
    }

    public void processTask(Task task) {
        try {
//            System.out.println("before downloader - " + task.getUrl());
            Page page = downloader.connect(task.getUrl())
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_11_4) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/50.0.2661.86 Safari/537.36")
                    .get();
            System.out.println("after downloader - " + page.getBaseURL());
            if (page.getStatusCode() == 200) {
                org.dom4j.Document document = pageProcessor.process(page, task, scheduler);
                pipeline.process(document);
            }


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void checkRunningStat() {
        while(true) {
            int statusNow = status.get();
            if (statusNow == STAT_RUNNING) {
                throw new IllegalStateException("Crawler is already running!");
            }
            if (status.compareAndSet(statusNow, STAT_RUNNING)) {
                break;
            }
        }
    }

    protected void initComponent() {
        if (downloader == null) {
            this.downloader = new HttpClientDownloader();
        }
        downloader.setThread(threadNum);
        if (threadPool == null || threadPool.isShutdown()) {
            if (executorService != null && !executorService.isShutdown()) {
                threadPool = new CountableThreadPool(threadNum, executorService);
            } else {
                threadPool = new CountableThreadPool(threadNum);
            }
        }
    }

    public abstract String login(Login login) throws Exception;

    public void downloader(Downloader downloader) {
        this.downloader = downloader;
    }

    public Downloader downloader() {
        return this.downloader;
    }

    public void pageProcessor(PageProcessor pageProcessor) {
        this.pageProcessor = pageProcessor;
    }

    public PageProcessor pageProcessor() {
        return this.pageProcessor;
    }

    public void pipline(Pipeline pipeline) {
        this.pipeline = pipeline;
    }

    public Pipeline pipeline() {
        return pipeline;
    }

    public void scheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public Scheduler scheduler() {
        return this.scheduler;
    }

    public static Crawler getInstance(int flag) {
        switch (flag) {
            case Crawler.BASE:
                return new BaseCrawler();
            default:
                return null;
        }
    }

    public void setThread(int threadNum) {
        this.threadNum = threadNum;
    }

    private void signalNewUrl() {
        try {
            newUrlLock.lock();
            newUrlCondition.signalAll();
        } finally {
            newUrlLock.unlock();
        }
    }

    public String getCrawlerName() {
        if (crawlerName == null) {
            return "";
        }
        return crawlerName;
    }

    public void setCrawlerName(String crawlerName) {
        if (crawlerName == null) {
            this.crawlerName = "";
        }
        this.crawlerName = crawlerName;
    }
}
