package com.glacier.crawler.processor;

import com.glacier.crawler.template.Template;

/**
 * Created by Glacier on 16/4/9.
 */
public class PageProcessor {

    private Template template;

    public void setParseTemplate(Template template) {
        this.template = template;
    }

    public void setParseTemplate(String template) {
        this.template = parseTemplate(template);
    }

    private Template parseTemplate(String templateStr) {
        return null;
    }

}
