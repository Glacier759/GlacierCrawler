package com.glacier.crawler.utils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Glacier on 16/5/13.
 */
public class EncodingUtil {

    public static String getString(InputStream inputStream, String contentType) {
        Document document = null;
        boolean match = false;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            IOUtils.copy(inputStream, outputStream);
            if (contentType != null && StringUtils.containsIgnoreCase(contentType, "charset")) {
                String[] contentArray = contentType.split(";");
                for (String contentStr : contentArray) {
                    if (StringUtils.containsIgnoreCase(contentStr, "charset")) {
                        String charset = contentStr.replaceAll(" ", "").substring(8);
                        return new String(IOUtils.toString(outputStream.toByteArray(), charset).getBytes(), "UTF-8");
                    }
                }
            }
            document = Jsoup.parse(IOUtils.toString(new ByteArrayInputStream(outputStream.toByteArray()), "UTF-8"));
            Elements metas = document.select("meta[content]");
            for (Element meta : metas) {
                String content = meta.attr("content");
                if (StringUtils.containsIgnoreCase(content, "charset")) {
                    String[] contentArray = content.split(";");
                    for (String contentStr : contentArray) {
                        if (StringUtils.containsIgnoreCase(contentStr, "charset")) {
                            String charset = contentStr.replaceAll(" ", "").substring(8);
                            return new String(IOUtils.toString(outputStream.toByteArray(), charset).getBytes(), "UTF-8");
                        }
                    }
                }
            }
            metas = document.select("meta[charset]");
            for ( Element meta : metas ) {
                String charset = meta.attr("charset");
                return new String(IOUtils.toString(outputStream.toByteArray(), charset).getBytes(), "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (document != null) {
            return document.toString();
        }
        return null;
    }

}
