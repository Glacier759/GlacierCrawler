package com.glacier.crawler.pipeline;

import com.glacier.crawler.utils.XMLUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;

import java.io.File;

/**
 * Created by Glacier on 16/4/9.
 */
public class Pipeline {

    public void process(Document document) {
        if ( document != null ) {
            try {
                String fileName = System.currentTimeMillis() + ".xml";
                FileUtils.write(new File("/tmp/data/", fileName), XMLUtil.formatXML(document), "UTF-8");
                System.out.println(new File("/tmp/data/"+fileName).getAbsolutePath());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
