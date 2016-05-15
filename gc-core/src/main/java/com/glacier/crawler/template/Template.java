package com.glacier.crawler.template;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Glacier on 16/4/26.
 */
public class Template implements Serializable {

    List<Tag> tags = new ArrayList<>();

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    @Override
    public String toString() {
        return "Template{" +
                "tags=" + tags +
                '}';
    }

}
