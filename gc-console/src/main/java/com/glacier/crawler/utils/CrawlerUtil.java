package com.glacier.crawler.utils;

import com.glacier.crawler.crawler.Crawler;
import com.glacier.crawler.entity.CrawlerEntity;
import com.glacier.crawler.model.CrawlerConfigWithBLOBs;
import com.glacier.crawler.model.CrawlerTemplate;
import com.glacier.crawler.template.Attr;
import com.glacier.crawler.template.Tag;
import com.glacier.crawler.template.Template;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Glacier on 16/5/10.
 */
public class CrawlerUtil {

    public static Map<String, Crawler> crawlerMap = new HashMap<String, Crawler>();

    public static Crawler getCrawler(String taskName) {
        Crawler crawler = null;
        if (crawlerMap.containsKey(taskName)) {
            crawler = crawlerMap.get(taskName);
        }
        else {
            crawler = Crawler.getInstance(Crawler.BASE);
            crawler.setCrawlerName(taskName);
            crawlerMap.put(taskName, crawler);
        }
        return crawler;
    }

    public static CrawlerEntity transferConfig(CrawlerConfigWithBLOBs crawlerBean) {
        CrawlerEntity crawlerEntity = new CrawlerEntity();
        crawlerEntity.setCrawler_name(crawlerBean.getTaskName());
        crawlerEntity.setLogin_useCookie(crawlerBean.getLoginUseCookie());
        crawlerEntity.setTask_type(crawlerBean.getTaskType());
        crawlerEntity.setLogin_cookie(crawlerBean.getLoginCookie());
        crawlerEntity.setLogin_url(crawlerBean.getLoginUrl());
        crawlerEntity.setTask_thread_num(crawlerBean.getTaskThreadNum());

        if (!StringUtils.isEmpty(crawlerBean.getLoginHeaders())) {
            for (String header : crawlerBean.getLoginHeaders().split(";")) {
                String[] header_array = header.split(":.:");
                crawlerEntity.addHeaders(header_array[0], header_array[1]);
            }
        }
        if (!StringUtils.isEmpty(crawlerBean.getLoginParams())) {
            for (String param : crawlerBean.getLoginParams().split(";")) {
                String[] param_array = param.split(":.:");
                crawlerEntity.addParams(param_array[0], param_array[1]);
            }
        }
        if (!StringUtils.isEmpty(crawlerBean.getTaskPatterns())) {
            for (String pattern : crawlerBean.getTaskPatterns().split(";")) {
                String[] pattern_array = pattern.split(":.:");
                if (StringUtils.equalsIgnoreCase("this", pattern_array[0])) {
                    crawlerEntity.setTemplateID(pattern_array[1]);
                }
                else {
                    crawlerEntity.addTaskPattern(pattern_array[0], pattern_array[1]);
                }
            }
        }
        if (!StringUtils.isEmpty(crawlerBean.getTaskStartUrl())) {
            for (String startUrl : crawlerBean.getTaskStartUrl().split(";")) {
                crawlerEntity.addTaskStartUrl(startUrl);
            }
        }

        if (!StringUtils.isEmpty(crawlerEntity.getLogin_url())) {
            if ( crawlerEntity.isLogin_useCookie() && crawlerEntity.getLogin_headers().containsKey("Cookie") ){
                crawlerEntity.canLogin(true);
            }
            else if( !crawlerEntity.isLogin_useCookie() && crawlerEntity.getLogin_params().size() > 0 ){
                crawlerEntity.canLogin(true);
            }
        }

        return crawlerEntity;
    }

    public static CrawlerConfigWithBLOBs transferEntity(CrawlerEntity crawlerEntity) {
        CrawlerConfigWithBLOBs crawlerBean = new CrawlerConfigWithBLOBs();
        crawlerBean.setTaskName(crawlerEntity.getCrawler_name());
        crawlerBean.setLoginUseCookie(crawlerEntity.isLogin_useCookie());
        crawlerBean.setTaskType(crawlerEntity.getTask_type());
        crawlerBean.setLoginCookie(crawlerEntity.getLogin_cookie());
        crawlerBean.setLoginUrl(crawlerEntity.getLogin_url());
        crawlerBean.setTaskThreadNum(crawlerEntity.getTask_thread_num());
        StringBuffer buffer = new StringBuffer();
        for ( String key : crawlerEntity.getLogin_headers().keySet() ) {
            String value = crawlerEntity.getLogin_headers().get(key);
            buffer.append(key+":.:"+value);
            buffer.append(";");
        }
        crawlerBean.setLoginHeaders(buffer.toString());
        buffer.setLength(0);
        for ( String key : crawlerEntity.getLogin_params().keySet() ) {
            String value = crawlerEntity.getLogin_params().get(key);
            buffer.append(key+":.:"+value);
            buffer.append(";");
        }
        crawlerBean.setLoginParams(buffer.toString());
        buffer.setLength(0);
        for ( String key : crawlerEntity.getTask_pattern().keySet() ) {
            String value = crawlerEntity.getTask_pattern().get(key);
            buffer.append(key+":.:"+value);
            buffer.append(";");
        }
        crawlerBean.setTaskPatterns(buffer.toString());
        buffer.setLength(0);
        for ( String key : crawlerEntity.getTask_start_url() ) {
            buffer.append(key);
            buffer.append(";");
        }
        crawlerBean.setTaskStartUrl(buffer.toString());
        buffer.setLength(0);
        return crawlerBean;
    }

    public static Template transferCrawlerTemplate(CrawlerTemplate crawlerTemplate) {
        Template template = new Template();

        try {
            Document doc = DocumentHelper.parseText(crawlerTemplate.getTemplate());
            Element root = doc.getRootElement();
            List<?> tags = root.elements("tag");
            for (Iterator<?> iterator = tags.iterator(); iterator.hasNext(); ) {
                Element tagEle = (Element) iterator.next();
                Tag tag = new Tag();
                tag.setName(tagEle.attributeValue("name"));
                tag.setPattern(tagEle.element("pattern").getText());
                tag.setOnlyPattern(Boolean.parseBoolean(tagEle.element("pattern").attributeValue("onlyPattern")));
                Element operator = tagEle.element("operator");
                Attr oper = new Attr();
                oper.setOperator(operator.attributeValue("op"));
                Element idEle = operator.element("id");
                if (idEle != null) {
                    oper.setId(idEle.getText());
                }
                Element templateEle = operator.element("templateID");
                if (templateEle != null) {
                    oper.setTemplateID(templateEle.getText());
                }
                List<?> attrEles = operator.elements("attr");
                if (attrEles != null) {
                    for (Iterator<?> iter = attrEles.iterator(); iter.hasNext(); ) {
                        Element attrEle = (Element) iter.next();
                        oper.addAttr(attrEle.getText());
                    }
                }
                tag.setOperators(oper);
                template.addTag(tag);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return template;
    }

    public static CrawlerTemplate transferTemplate(Template template) {
        CrawlerTemplate crawlerTemplate = new CrawlerTemplate();

        try {
            Document doc = DocumentHelper.createDocument();
            Element root = doc.addElement("template");
            for ( Tag tag : template.getTags() ) {
                Element tagEle = root.addElement("tag");
                tagEle.addAttribute("name", tag.getName());
                tagEle.addElement("pattern").addAttribute("onlyPattern", tag.getOnlyPattern().toString()).addText(tag.getPattern());
                Element operEle = tagEle.addElement("operator");
                operEle.addAttribute("op", tag.getOperators().getOperator());
                if (tag.getOperators().getId() != null) {
                    operEle.addElement("id").addText(tag.getOperators().getId());
                }
                if (tag.getOperators().getAttrs() != null) {
                    for (String attr : tag.getOperators().getAttrs()) {
                        operEle.addElement("attr").addText(attr);
                    }
                }
                if (tag.getOperators().getTemplateID() != null) {
                    operEle.addElement("templateID").addText(tag.getOperators().getTemplateID());
                }
            }
            crawlerTemplate.setTemplate(doc.asXML());
        }catch (Exception e){
            e.printStackTrace();
        }

        return crawlerTemplate;
    }

}
