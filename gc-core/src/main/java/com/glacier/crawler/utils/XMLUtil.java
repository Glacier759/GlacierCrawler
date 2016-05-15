package com.glacier.crawler.utils;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import java.io.StringWriter;

/**
 * Created by Glacier on 16/5/12.
 */
public class XMLUtil {

    public static String formatXML(Document document) {
        String formatXMLStr = null;
        try {
            OutputFormat format = OutputFormat.createPrettyPrint();
            format.setEncoding("UTF-8");
            StringWriter writer = new StringWriter();
            XMLWriter xmlWriter = new XMLWriter(writer, format);
            xmlWriter.write(document);
            formatXMLStr = writer.toString();
        }catch (Exception e) {
            e.printStackTrace();
        }
        return formatXMLStr.replaceAll("&", "");
    }

}
