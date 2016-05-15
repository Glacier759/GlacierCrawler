package com.glacier.crawler.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glacier.crawler.model.CrawlerConfigWithBLOBs;
import com.glacier.crawler.model.CrawlerTask;
import com.glacier.crawler.service.CrawlerService;
import com.glacier.crawler.service.SystemService;
import com.glacier.crawler.utils.CrawlerUtil;
import com.glacier.crawler.utils.KeyUtil;
import com.glacier.crawler.utils.ReturnResult;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * Created by Glacier on 16/5/10.
 */
@Controller
@RequestMapping(value = "/task")
public class TaskController {

    private static final Logger logger = Logger.getLogger(TaskController.class);

    @Resource
    private SystemService systemService;
    @Resource
    private CrawlerService crawlerService;

    @ResponseBody
    @RequestMapping(value = "/{task}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String status(HttpServletRequest request, @PathVariable("task") String task) {
        JSONObject result = new JSONObject();
        if ( systemService.selectCrawlerTask(task) == null ) {
            result.put("status", "failed");
            result.put("message", "task [" + task + "] doesn`t exist.");
        }
        else {
            CrawlerConfigWithBLOBs crawlerBean = crawlerService.selectByCrawlerName(task);
            JSONObject tasko = JSON.parseObject(ReturnResult.toJSON(CrawlerUtil.transferConfig(crawlerBean)));
            result.put("status", "success");
            result.put("task", tasko);
        }
        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String create(HttpServletRequest request) {
        JSONObject result = new JSONObject();

        KeyUtil keyGen = new KeyUtil();

        String taskName = keyGen.generateKey();
        while(systemService.selectCrawlerTask(taskName) != null) {
            taskName = keyGen.generateKey();
        }
        CrawlerTask crawlerTask = new CrawlerTask();
        crawlerTask.setTaskName(taskName);
        systemService.insertCrawlerTask(crawlerTask);

        CrawlerConfigWithBLOBs crawlerBean = new CrawlerConfigWithBLOBs();
        crawlerBean.setTaskName(taskName);
        crawlerBean.setTaskType("TYPE_TASK_QUEUE");
        crawlerService.insertCrawlerConfig(crawlerBean);

        try {
            String body = IOUtils.toString(request.getInputStream(), "UTF-8");
            if (body.length() > 0) {
                return modify(request, taskName, body);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        result.put("status", "success");
        JSONObject tasko = JSON.parseObject(ReturnResult.toJSON(CrawlerUtil.transferConfig(crawlerBean)));
        result.put("task", tasko);

        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/{task}", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String createT(HttpServletRequest request, @PathVariable("task") String task) {
        JSONObject result = new JSONObject();

        if (systemService.selectCrawlerTask(task) != null) {
            result.put("status", "failed");
            result.put("message", "task [" + task + "] is already exist, please use the PUT method to modify.");
            return ReturnResult.processOutputJSON(request, result);
        }

        CrawlerTask crawlerTask = new CrawlerTask();
        crawlerTask.setTaskName(task);
        systemService.insertCrawlerTask(crawlerTask);

        CrawlerConfigWithBLOBs crawlerBean = new CrawlerConfigWithBLOBs();
        crawlerBean.setTaskName(task);
        crawlerBean.setTaskType("TYPE_TASK_QUEUE");
        crawlerService.insertCrawlerConfig(crawlerBean);

        try {
            String body = IOUtils.toString(request.getInputStream(), "UTF-8");
            if (body.length() > 0) {
                return modify(request, task, body);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        result.put("status", "success");
        JSONObject tasko = JSON.parseObject(ReturnResult.toJSON(CrawlerUtil.transferConfig(crawlerBean)));
        result.put("task", tasko);

        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/{task}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public String modify(HttpServletRequest request, @PathVariable("task") String task, @RequestBody String json) {
        JSONObject result = new JSONObject();

        if (systemService.selectCrawlerTask(task) == null) {
            result.put("status", "failed");
            result.put("message", "task [" + task + "] is doesn`t exist.");
            return ReturnResult.processOutputJSON(request, result);
        }

        CrawlerConfigWithBLOBs crawlerBean = crawlerService.selectByCrawlerName(task);

        JSONObject taskObj = JSON.parseObject(json);
        taskObj = taskObj.getJSONObject("task");
        if (taskObj != null) {
            String crawlerName = taskObj.getString("crawler_name");
            crawlerBean.setLoginCookie(taskObj.getString("login_cookie"));
            crawlerBean.setLoginUrl(taskObj.getString("login_url"));
            crawlerBean.setLoginUseCookie(taskObj.getBoolean("login_useCookie"));
            crawlerBean.setTaskThreadNum(taskObj.getInteger("task_thread_num"));
            String crawlerType = taskObj.getString("task_type");
            if (StringUtils.equalsIgnoreCase("TYPE_TASK_QUEUE", crawlerType)) {
                crawlerBean.setTaskType("TYPE_TASK_QUEUE");
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_ALL", crawlerType)) {
                crawlerBean.setTaskType("TYPE_TASK_ALL");
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_DOMAIN", crawlerType)) {
                crawlerBean.setTaskType("TYPE_TASK_DOMAIN");
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_PROCESSOR", crawlerType)) {
                crawlerBean.setTaskType("TYPE_TASK_PROCESSOR");
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_PATTERN", crawlerType)) {
                crawlerBean.setTaskType("TYPE_TASK_PATTERN");
            }
            else {
                crawlerBean.setTaskType("TYPE_TASK_QUEUE");
            }
            String templateID = taskObj.getString("templateID");
            JSONObject patternObj = taskObj.getJSONObject("task_pattern");
            StringBuilder patternBuff = new StringBuilder();
            if (templateID != null) {
                patternBuff.append("this");
                patternBuff.append(":.:");
                patternBuff.append(templateID);
                patternBuff.append(";");
            }
            if (patternObj != null) {
                for (String key : patternObj.keySet()) {
                    patternBuff.append(key);
                    patternBuff.append(":.:");
                    patternBuff.append(patternObj.get(key));
                    patternBuff.append(";");
                }
            }
            crawlerBean.setTaskPatterns(patternBuff.toString());

            JSONObject paramObj = taskObj.getJSONObject("login_params");
            if (paramObj != null) {
                StringBuilder paramBuff = new StringBuilder();
                for (String key : paramObj.keySet()) {
                    paramBuff.append(key);
                    paramBuff.append(":.:");
                    paramBuff.append(paramObj.get(key));
                    paramBuff.append(";");
                }
                crawlerBean.setLoginParams(paramBuff.toString());
            }
            JSONObject headerObj = taskObj.getJSONObject("login_headers");
            if (headerObj != null) {
                StringBuilder headerBuff = new StringBuilder();
                for (String key : headerObj.keySet()) {
                    headerBuff.append(key);
                    headerBuff.append(":.:");
                    headerBuff.append(headerObj.get(key));
                    headerBuff.append(";");
                }
                crawlerBean.setLoginHeaders(headerBuff.toString());
            }
            JSONArray startUrls = taskObj.getJSONArray("task_start_url");
            if ( startUrls != null ) {
                StringBuilder startUrlBuff = new StringBuilder();
                for (int index = 0; index < startUrls.size(); index++) {
                    startUrlBuff.append(startUrls.getString(index));
                    startUrlBuff.append(";");
                }
                crawlerBean.setTaskStartUrl(startUrlBuff.toString());
            }
            if ( !crawlerName.equals(crawlerBean.getTaskName()) ) {
                CrawlerTask crawlerTask = systemService.selectCrawlerTask(crawlerName);
                if (crawlerTask != null) {
                    result.put("status", "failed");
                    result.put("message", "task [" + crawlerName + "] is already exist.");
                    return ReturnResult.processOutputJSON(request, result);
                }
                crawlerTask = systemService.selectCrawlerTask(crawlerBean.getTaskName());
                crawlerTask.setTaskName(crawlerName);
                systemService.updateCrawlerTaskByID(crawlerTask);
                crawlerBean.setTaskName(crawlerName);
            }
            crawlerService.updateCrawlerConfigByID(crawlerBean);
            JSONObject tasko = JSON.parseObject(ReturnResult.toJSON(CrawlerUtil.transferConfig(crawlerBean)));
            result.put("status", "success");
            result.put("task", tasko);
            return ReturnResult.processOutputJSON(request, result);
        }

        result.put("status", "failed");
        result.put("message", "Unrecognized Profile.");

        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/{task}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public String delete(HttpServletRequest request, @PathVariable("task") String task) {
        JSONObject result = new JSONObject();
        if ( systemService.selectCrawlerTask(task) == null ) {
            result.put("status", "failed");
            result.put("message", "task [" + task + "] doesn`t exist.");
        }
        else {
            CrawlerConfigWithBLOBs crawlerBean = crawlerService.selectByCrawlerName(task);
            crawlerService.deleteCrawlerConfig(task);
            systemService.deleteCrawlerTask(task);
            JSONObject tasko = JSON.parseObject(ReturnResult.toJSON(CrawlerUtil.transferConfig(crawlerBean)));
            result.put("status", "success");
            result.put("message", "task [" + task + "] is deleted.");
            result.put("task", tasko);
        }
        return ReturnResult.processOutputJSON(request, result);
    }

}
