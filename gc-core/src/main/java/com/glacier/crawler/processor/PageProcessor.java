package com.glacier.crawler.processor;

import com.glacier.crawler.entity.Page;
import com.glacier.crawler.entity.Task;
import com.glacier.crawler.scheduler.Scheduler;
import com.glacier.crawler.template.Tag;
import com.glacier.crawler.template.Template;
import com.glacier.crawler.utils.SerializeUtil;
import com.glacier.crawler.utils.StringUtil;
import com.glacier.crawler.utils.URLUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentHelper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * Created by Glacier on 16/4/9.
 */
public class PageProcessor {

    public org.dom4j.Document process(Page page, Task task, Scheduler scheduler) {
        Template template = task.getTemplate();
        if (template != null && template.getTags().size() == 0) {
            return null;
        }
        else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Document document = page.getDocument();
            org.dom4j.Document doc = DocumentHelper.createDocument();
            org.dom4j.Element root = doc.addElement("page");
            root.addElement("source").addText(page.getBaseURL());
            root.addElement("crawl_date").addText(dateFormat.format(new Date()));
            root.addElement("page_date").addText(StringUtil.getPageTime(page.getBaseURL(), page.getDocument().toString()));

            //往队列添加任务
            if (task.getCrawlerType() != Task.TYPE_TASK_QUEUE && task.getCrawlerType() != Task.TYPE_TASK_PROCESSOR) {
                Elements elements = document.select("a[href]");
                for (Element element : elements) {
                    String href = element.attr("abs:href");
                    if (StringUtils.isEmpty(href) || href.length() <= 5 ) {
                        continue;
                    }
                    try {
                        if (task.getCrawlerType() == Task.TYPE_TASK_ALL) {
                            Task t = task.clone();
                            t.setUrl(href);
                            t.setTemplate(null);
                            t.setCrawlerType(task.getCrawlerType());
                            scheduler.push(t);
                        }
                        else if (task.getCrawlerType() == Task.TYPE_TASK_DOMAIN) {
                            if (StringUtils.equalsIgnoreCase(URLUtil.getDomain(task.getUrl()), URLUtil.getDomain(href))) {
                                Task t = task.clone();
                                t.setUrl(href);
                                t.setTemplate(null);
                                t.setCrawlerType(task.getCrawlerType());
                                scheduler.push(t);
                            }
                        }
                        else if (task.getCrawlerType() == Task.TYPE_TASK_PATTERN) {
                            for (String pattern : task.getPatterns().keySet()) {
                                if (Pattern.matches(pattern, href)) {
                                    Task t = task.clone();
                                    t.setUrl(href);
                                    t.setTemplate(null);
                                    t.setCrawlerType(task.getCrawlerType());
                                    t.setTemplate(task.getPatterns().get(pattern));
                                    scheduler.push(t);
                                    break;
                                }
                            }
                        }
                    }catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            //模板为null时抽取网页全部文字
            if (template == null) {
                root.addElement("title").addText(document.title());
                root.addElement("default_content").addText(document.text().replaceAll("<","").replaceAll(">", ""));
                return doc;
            }
            //按照模板抽取数据, 提取有效信息
            for ( Tag tag : template.getTags() ) {
                if (tag.getOnlyPattern()) {
//                    String pattern = tag.getPattern();
                }
                else {
                    //pattern
                    org.dom4j.Element element = root.addElement(tag.getName());
                    Elements eles = document.children();
                    if ( tag.getOperators().getId() != null ) {
                        eles = document.getElementById(tag.getOperators().getId()).children();
                    }
                    if ( tag.getOperators().getAttrs() != null ) {
                        for (String attr : tag.getOperators().getAttrs()) {
                            eles = eles.select(attr);
                        }
                    }
                    if (StringUtils.equalsIgnoreCase("text", tag.getOperators().getOperator())) {
                        element.addText(eles.text());
                    }
                    else if (StringUtils.equalsIgnoreCase("html", tag.getOperators().getOperator())) {
                        element.addText(eles.html());
                    }
                    else {
                        String operator = tag.getOperators().getOperator();
                        if ( StringUtils.equalsIgnoreCase("attr", operator.substring(0, 4)) ) {
                            for ( Element ele : eles ) {
                                element.addElement("i").addText(ele.attr(operator.substring(5)));
                            }
                        }
                        else if (StringUtils.equalsIgnoreCase("push", operator.substring(0, 4))) {
                            Task t = new Task();
                            try {
                                t.setCrawlerType(task.getCrawlerType());
                                Template temp = null;
                                if (tag.getOperators().getTemplateID() != null && !StringUtils.equalsIgnoreCase("null", tag.getOperators().getTemplateID())) {
                                    InputStream ins = new URL("http://localhost:8080/template/serialize/"+tag.getOperators().getTemplateID()).openStream();
                                    temp = (Template) SerializeUtil.unserialize(IOUtils.toByteArray(ins));
                                }
                                t.setTemplate(temp);
                            }catch (Exception e) {
                                e.printStackTrace();
                            }
                            //稍等
                            for ( Element ele : eles ) {
                                String href = ele.attr(operator.substring(5));
                                if (StringUtils.isEmpty(href) || href.length() <= 5 ) {
                                    continue;
                                }
                                t.setUrl(href);
                                System.out.println("push: - " + href);
                                scheduler.push(t);
                            }
                        }
                    }
                }
            }
            return doc;
        }
    }
}
