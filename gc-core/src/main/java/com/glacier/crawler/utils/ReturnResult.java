package com.glacier.crawler.utils;

import com.alibaba.fastjson.JSONObject;
import java.util.List;
import java.util.Map;

/**
 * Created by Glacier on 16/4/26.
 */
public class ReturnResult {

    public static final String SUCCESS = "success";
    public static final String FAILED = "failed";

    public static String toString(String status, String message) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        return result.toJSONString();
    }

    public static String toString(String status, Map<String, String> results) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        JSONObject msg = new JSONObject();
        for (String key : results.keySet()) {
            msg.put(key, msg);
        }
        result.put("message", msg);
        return result.toJSONString();
    }

    public static String toString(String status, List<String> results) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", results);
        return result.toJSONString();
    }

    public static String toString(String status) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        return result.toString();
    }

    public static JSONObject toJSONObject(String status, String message) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", message);
        return result;
    }

    public static JSONObject toJSONObject(String status, Map<String, String> results) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        JSONObject msg = new JSONObject();
        for (String key : results.keySet()) {
            msg.put(key, msg);
        }
        result.put("message", msg);
        return result;
    }

    public static JSONObject toJSONObject(String status, List<String> results) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        result.put("message", results);
        return result;
    }

    public static JSONObject toJSONObject(String status) {
        JSONObject result = new JSONObject();
        result.put("status", status);
        return result;
    }

}
