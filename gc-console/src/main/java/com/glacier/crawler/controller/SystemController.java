package com.glacier.crawler.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.glacier.crawler.crawler.Crawler;
import com.glacier.crawler.entity.CrawlerEntity;
import com.glacier.crawler.entity.Task;
import com.glacier.crawler.login.Login;
import com.glacier.crawler.model.CrawlerTask;
import com.glacier.crawler.model.CrawlerTemplate;
import com.glacier.crawler.service.CrawlerService;
import com.glacier.crawler.service.SystemService;
import com.glacier.crawler.template.Template;
import com.glacier.crawler.utils.CrawlerUtil;
import com.glacier.crawler.utils.ReturnResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * Created by Glacier on 16/5/10.
 */
@Controller
@RequestMapping(value = "/system")
public class SystemController {

    @Resource
    private SystemService systemService;
    @Resource
    private CrawlerService crawlerService;

    @ResponseBody
    @RequestMapping(value = "/status", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String status(HttpServletRequest request) {
        JSONObject result = new JSONObject();
        File file = new File("baidu");
        if (!file.exists()) {
            file.mkdirs();
        }
        List<CrawlerTask> crawlerTaskList = systemService.selectAllCrawlerTask();
        if ( crawlerTaskList.size() > 0 ) {
            result.put("status", "success");
            JSONArray jsonArray = new JSONArray();
            for (CrawlerTask crawlerTask : crawlerTaskList) {
                JSONObject task = new JSONObject();
                task.put("name", crawlerTask.getTaskName());
                task.put("status", crawlerTask.getStatus());
                jsonArray.add(task);
            }
            result.put("task", jsonArray);
        }
        else {
            result.put("status", "failed");
            result.put("message", "task list is empty");
        }

        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/status/{task}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String taskStatus(HttpServletRequest request, @PathVariable("task") String task) {
        JSONObject result = new JSONObject();

        CrawlerTask crawlerTask = systemService.selectCrawlerTask(task);
        if ( crawlerTask != null ) {
            result.put("status", "success");
            JSONObject object = new JSONObject();
            object.put("name", crawlerTask.getTaskName());
            object.put("status", crawlerTask.getStatus());
            result.put("task", object);
        }
        else {
            result.put("status", "failed");
            result.put("message", "task ["+task+"] doesn`t exist");
        }

        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/{task}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String taskStatusT(HttpServletRequest request, @PathVariable("task") String task) {
        return taskStatus(request, task);
    }

    @ResponseBody
    @RequestMapping(value = "/{task}/status", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String taskStatusP(HttpServletRequest request, @PathVariable("task") String task) {
        return taskStatus(request, task);
    }

    @ResponseBody
    @RequestMapping(value = "/{task}/start", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String taskStart(HttpServletRequest request, @PathVariable("task") String task) {
        JSONObject result = new JSONObject();
        CrawlerTask crawlerTask = systemService.selectCrawlerTask(task);
        if ( crawlerTask == null ) {
            result.put("status", "failed");
            result.put("message", "task [" + task + "] doesn`t exist");
            return ReturnResult.processOutputJSON(request, result);
        }
        else if ( "running".equals(crawlerTask.getStatus()) ) {
            result.put("status", "failed");
            result.put("message", "task is already running.");
            return ReturnResult.processOutputJSON(request, result);
        }

        CrawlerEntity crawlerEntity = CrawlerUtil.transferConfig(crawlerService.selectByCrawlerName(task));

        Crawler crawler = CrawlerUtil.getCrawler(crawlerEntity.getCrawler_name());
        crawler.scheduler().setKey(crawlerEntity.getCrawler_name());

        if ( crawlerEntity.canLogin() ) {
            Login login = new Login();
            login.setLoginURL(crawlerEntity.getLogin_url());
            for (String key : crawlerEntity.getLogin_headers().keySet()) {
                String value = crawlerEntity.getLogin_headers().get(key);
                login.addHeader(key, value);
            }
            if ( crawlerEntity.isLogin_useCookie() ) {
                login.useCookie();
                login.addCookie(crawlerEntity.getLogin_cookie());
            }
            else {
                for (String key : crawlerEntity.getLogin_params().keySet()) {
                    String value = crawlerEntity.getLogin_params().get(key);
                    login.addParam(key, value);
                }
            }
            try {
                crawler.login(login);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        for (String startUrl : crawlerEntity.getTask_start_url()) {
            Task t = new Task(startUrl);
            if (StringUtils.equalsIgnoreCase("TYPE_TASK_ALL", crawlerEntity.getTask_type())) {
                t.setCrawlerType(Task.TYPE_TASK_ALL);
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_QUEUE", crawlerEntity.getTask_type())) {
                t.setCrawlerType(Task.TYPE_TASK_QUEUE);
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_DOMAIN", crawlerEntity.getTask_type())) {
                t.setCrawlerType(Task.TYPE_TASK_DOMAIN);
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_PROCESSOR", crawlerEntity.getTask_type())) {
                t.setCrawlerType(Task.TYPE_TASK_PROCESSOR);
            }
            else if (StringUtils.equalsIgnoreCase("TYPE_TASK_PATTERN", crawlerEntity.getTask_type())) {
                t.setCrawlerType(Task.TYPE_TASK_PATTERN);
            }
            else {
                t.setCrawlerType(Task.TYPE_TASK_QUEUE);
            }
            if (crawlerEntity.getTemplateID() != null) {
                CrawlerTemplate crawlerTemplate = crawlerService.selectTemplate(Long.parseLong(crawlerEntity.getTemplateID()));
                if (crawlerTemplate == null) {
                    result.put("status", "failed");
                    result.put("message", "Unrecognized Profile.");
                    return ReturnResult.processOutputJSON(request, result);
                }
                Template template = CrawlerUtil.transferCrawlerTemplate(crawlerTemplate);
                t.setTemplate(template);
            }
            if (crawlerEntity.getTask_pattern() != null) {
                for ( String pattern : crawlerEntity.getTask_pattern().keySet() ) {
                    String templateID = crawlerEntity.getTask_pattern().get(pattern);
                    CrawlerTemplate crawlerTemplate = null;
                    Template template = null;
                    if (!StringUtils.equalsIgnoreCase("null", templateID)) {
                        if (StringUtils.isNotEmpty(templateID)) {
                            crawlerTemplate = crawlerService.selectTemplate(Long.parseLong(templateID));
                        }
                        if (crawlerTemplate == null) {
                            result.put("status", "failed");
                            result.put("message", "Unrecognized Profile.");
                            return ReturnResult.processOutputJSON(request, result);
                        }
                        template = CrawlerUtil.transferCrawlerTemplate(crawlerTemplate);
                    }
                    t.addPattern(pattern, template);
                }
            }
            crawler.scheduler().push(t);
        }
//        crawler.scheduler().clear();
        crawler.setThread(crawlerEntity.getTask_thread_num());
        crawler.start();

        crawlerTask.setStatus("running");
        systemService.updateCrawlerTask(crawlerTask);
        return taskStatus(request, task);
    }

    @ResponseBody
    @RequestMapping(value = "/{task}/stop", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String taskStop(HttpServletRequest request, @PathVariable("task") String task) {
        JSONObject result = new JSONObject();
        CrawlerTask crawlerTask = systemService.selectCrawlerTask(task);
        if ( crawlerTask == null ) {
            result.put("status", "failed");
            result.put("message", "task [" + task + "] doesn`t exist");
            return ReturnResult.processOutputJSON(request, result);
        }
        else if ( "stop".equals(crawlerTask.getStatus()) ) {
            result.put("status", "failed");
            result.put("message", "task is already stoped.");
            return ReturnResult.processOutputJSON(request, result);
        }

        Crawler crawler = CrawlerUtil.getCrawler(task);
        crawler.stop();

        crawlerTask.setStatus("stop");
        systemService.updateCrawlerTask(crawlerTask);
        return taskStatus(request, task);
    }

}
