package com.glacier.crawler.template;


import java.io.Serializable;

/**
 * Created by Glacier on 16/5/12.
 */
public class Tag implements Serializable{

    String name, pattern;
    Boolean onlyPattern;
    Attr operators;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Boolean getOnlyPattern() {
        return onlyPattern;
    }

    public void setOnlyPattern(Boolean onlyPattern) {
        this.onlyPattern = onlyPattern;
    }

    public Attr getOperators() {
        return operators;
    }

    public void setOperators(Attr operators) {
        this.operators = operators;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "name='" + name + '\'' +
                ", pattern='" + pattern + '\'' +
                ", onlyPattern=" + onlyPattern +
                ", operators=" + operators +
                '}';
    }
}
