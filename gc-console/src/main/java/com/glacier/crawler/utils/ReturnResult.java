package com.glacier.crawler.utils;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Glacier on 16/5/10.
 */
public class ReturnResult {

    public static String processOutputJSON(HttpServletRequest request, JSONObject result) {
        String pretty = request.getParameter("pretty");
        if (!StringUtils.isEmpty(pretty) && StringUtils.equalsIgnoreCase("y", pretty)) {
            ObjectMapper format = new ObjectMapper();
            try {
                return format.writerWithDefaultPrettyPrinter().writeValueAsString(result) + "\n";
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result.toJSONString();
    }

    public static String toJSON(Object object) {
        ObjectMapper format = new ObjectMapper();
        try {
            return format.writeValueAsString(object);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
