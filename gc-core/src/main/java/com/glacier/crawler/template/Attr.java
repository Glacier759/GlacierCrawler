package com.glacier.crawler.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glacier on 16/5/12.
 */
public class Attr implements Serializable{

    String id, operator, templateID;
    List<String> attrs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public List<String> getAttrs() {
        return attrs;
    }

    public void setAttrs(List<String> attrs) {
        this.attrs = attrs;
    }

    public void addAttr(String attr) {
        if (this.attrs == null) {
            this.attrs = new ArrayList<>();
        }
        this.attrs.add(attr);
    }

    public String getTemplateID() {
        return templateID;
    }

    public void setTemplateID(String templateID) {
        this.templateID = templateID;
    }

    @Override
    public String toString() {
        return "Attr{" +
                "id='" + id + '\'' +
                ", operator='" + operator + '\'' +
                ", templateID='" + templateID + '\'' +
                ", attrs=" + attrs +
                '}';
    }
}
