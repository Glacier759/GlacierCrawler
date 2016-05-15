package com.glacier.crawler.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.glacier.crawler.model.CrawlerTemplate;
import com.glacier.crawler.service.CrawlerService;
import com.glacier.crawler.template.Attr;
import com.glacier.crawler.template.Tag;
import com.glacier.crawler.template.Template;
import com.glacier.crawler.utils.CrawlerUtil;
import com.glacier.crawler.utils.ReturnResult;
import com.glacier.crawler.utils.SerializeUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Created by Glacier on 16/5/12.
 */
@Controller
@RequestMapping(value = "/template")
public class TemplateController {

    @Resource
    CrawlerService crawlerService;

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    public String get(HttpServletRequest request, @PathVariable("id") Long id) {
        JSONObject result = new JSONObject();
        CrawlerTemplate crawlerTemplate = crawlerService.selectTemplate(id);
        if (crawlerTemplate == null) {
            result.put("status", "failed");
            result.put("templateID", id);
            result.put("message", "template - "+ id + " doesn`t exist");
            return ReturnResult.processOutputJSON(request, result);
        }
        Template template = CrawlerUtil.transferCrawlerTemplate(crawlerTemplate);
        result.put("status", "success");
        result.put("templateID", id);
        result.put("template", JSON.parseObject(ReturnResult.toJSON(template)));
        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    public String modify(HttpServletRequest request, @PathVariable("id") Long id, @RequestBody String json) {
        JSONObject result = new JSONObject();
        CrawlerTemplate crawlerTemplate = crawlerService.selectTemplate(id);
        if (crawlerTemplate == null) {
            result.put("status", "failed");
            result.put("templateID", id);
            result.put("message", "template - "+ id + " doesn`t exist");
            return ReturnResult.processOutputJSON(request, result);
        }
        JSONObject modifyJSON = JSON.parseObject(json);
        JSONObject templateJSON = modifyJSON.getJSONObject("template");
        if (templateJSON == null) {
            result.put("status", "failed");
            result.put("templateID", id);
            result.put("message", "Unrecognized Profile.");
            return ReturnResult.processOutputJSON(request, result);
        }
        Template template = new Template();
        JSONArray tagArray = templateJSON.getJSONArray("tags");
        for (int index = 0; index < tagArray.size(); index ++) {
            JSONObject tagObj = tagArray.getJSONObject(index);
            Tag tag = new Tag();
            tag.setName(tagObj.getString("name"));
            tag.setPattern(tagObj.getString("pattern"));
            tag.setOnlyPattern(tagObj.getBoolean("onlyPattern"));

            JSONObject attrObj = tagObj.getJSONObject("operators");
            Attr attr = new Attr();
            attr.setId(attrObj.getString("id"));
            attr.setTemplateID(attrObj.getString("templateID"));
            attr.setOperator(attrObj.getString("operator"));
            JSONArray attrArray = attrObj.getJSONArray("attrs");
            if (attrArray != null) {
                for (int i = 0; i < attrArray.size(); i ++) {
                    if (StringUtils.isNotEmpty(attrArray.getString(i))) {
                        attr.addAttr(attrArray.getString(i));
                    }
                }
            }
            tag.setOperators(attr);
            template.addTag(tag);
        }
        crawlerTemplate.setTemplate(CrawlerUtil.transferTemplate(template).getTemplate());
        crawlerService.updateTemplate(crawlerTemplate);
        result.put("status", "success");
        result.put("templateID", id);
        result.put("template", JSON.parseObject(ReturnResult.toJSON(template)));
        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    public String delete(HttpServletRequest request, @PathVariable("id") Long id) {
        JSONObject result = new JSONObject();
        CrawlerTemplate crawlerTemplate = crawlerService.selectTemplate(id);
        if (crawlerTemplate == null) {
            result.put("status", "failed");
            result.put("templateID", id);
            result.put("message", "template - "+ id + " doesn`t exist");
            return ReturnResult.processOutputJSON(request, result);
        }
        crawlerService.deleteTemplate(id);
        Template template = CrawlerUtil.transferCrawlerTemplate(crawlerTemplate);
        result.put("status", "success");
        result.put("templateID", id);
        result.put("template", JSON.parseObject(ReturnResult.toJSON(template)));
        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String create(HttpServletRequest request, @RequestBody String json) {
        JSONObject result = new JSONObject();
        CrawlerTemplate crawlerTemplate = new CrawlerTemplate();

        JSONObject modifyJSON = JSON.parseObject(json);
        JSONObject templateJSON = modifyJSON.getJSONObject("template");
        if (templateJSON == null) {
            result.put("status", "failed");
            result.put("message", "Unrecognized Profile.");
            return ReturnResult.processOutputJSON(request, result);
        }
        Template template = new Template();
        JSONArray tagArray = templateJSON.getJSONArray("tags");
        for (int index = 0; index < tagArray.size(); index ++) {
            JSONObject tagObj = tagArray.getJSONObject(index);
            Tag tag = new Tag();
            tag.setName(tagObj.getString("name"));
            tag.setPattern(tagObj.getString("pattern"));
            tag.setOnlyPattern(tagObj.getBoolean("onlyPattern"));

            JSONObject attrObj = tagObj.getJSONObject("operators");
            Attr attr = new Attr();
            attr.setId(attrObj.getString("id"));
            attr.setOperator(attrObj.getString("operator"));
            attr.setTemplateID(attrObj.getString("templateID"));
            JSONArray attrArray = attrObj.getJSONArray("attrs");
            if (attrArray != null) {
                for (int i = 0; i < attrArray.size(); i ++) {
                    if (StringUtils.isNotEmpty(attrArray.getString(i))) {
                        attr.addAttr(attrArray.getString(i));
                    }
                }
            }
            tag.setOperators(attr);
            template.addTag(tag);
        }

        crawlerTemplate = CrawlerUtil.transferTemplate(template);
        crawlerService.insertTemplate(crawlerTemplate);
        result.put("status", "success");
        result.put("templateID", crawlerTemplate.getId());
        result.put("template", JSON.parseObject(ReturnResult.toJSON(template)));
        return ReturnResult.processOutputJSON(request, result);
    }

    @ResponseBody
    @RequestMapping(value = "/serialize/{id}", method = RequestMethod.GET)
    public byte[] serialize(HttpServletRequest request, @PathVariable("id") Long id) {
        JSONObject result = new JSONObject();
        CrawlerTemplate crawlerTemplate = crawlerService.selectTemplate(id);
        Template template = CrawlerUtil.transferCrawlerTemplate(crawlerTemplate);
        return SerializeUtil.serialize(template);
    }

}
