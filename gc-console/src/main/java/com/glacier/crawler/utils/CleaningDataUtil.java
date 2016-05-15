package com.glacier.crawler.utils;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

/**
 * Created by Glacier on 16/5/14.
 */
public class CleaningDataUtil {

    private static Connection connection = null;

    public static void process() throws Exception {
        connection = DriverManager.getConnection("jdbc:mysql://localhost/earthquake?useUnicode=true&characterEncoding=UTF-8", "crawler", "crawler");

        File dataDir = new File("/tmp/data/");
        String[] keywords = { "广东", "地震", "将", "发生"};
        for ( File dataFile : dataDir.listFiles() ) {
            try {
                System.out.println(dataFile.getCanonicalPath());
                String dataContent = FileUtils.readFileToString(dataFile);
                Document dataDoc = DocumentHelper.parseText(dataContent.replaceAll("&", ""));
                Element page = dataDoc.getRootElement();
                String source = page.elementText("source");
                String crawl_date = page.elementText("crawl_date");
                String page_date = page.elementText("page_date");
                if (page_date == null || page_date.equals("null")) {
                    page_date = crawl_date;
                }
                String content = page.elementText("default_content");
                String title = page.elementText("title");

                String sql = "INSERT INTO quake_info(title,description,type,manager,status,jump_to,create_time,publish_time) VALUES(?,?,?,?,?,?,?,?)";
                if (content != null) {
                    String desc = StringUtil.examinePageKeywords(content, keywords);
                    System.out.println(source + " - " + desc);
                    if ( desc != null ) {
                        String sql_title = desc;
                        String sql_keywords = "";
                        for (String keyword : keywords) {
                            sql_keywords += ";" + keyword;
                        }
                        sql_keywords = sql_keywords.substring(1);
                        String sql_type = "必应";
                        String sql_manager = "1";
                        String sql_status = "2";
                        String sql_jumpto = source;
                        String sql_create_date = crawl_date;
                        String sql_publish_date = page_date;

                        PreparedStatement ps = connection.prepareStatement(sql);
                        ps.setString(1, sql_title);
                        ps.setString(2, sql_keywords);
                        ps.setString(3, sql_type);
                        ps.setInt(4, Integer.parseInt(sql_manager));
                        ps.setInt(5, Integer.parseInt(sql_status));
                        ps.setString(6, sql_jumpto);
                        ps.setString(7, sql_create_date);
                        ps.setString(8, sql_publish_date);

                        int result = ps.executeUpdate();
                        System.out.println(result);
                    }
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception{
        process();
    }

}
