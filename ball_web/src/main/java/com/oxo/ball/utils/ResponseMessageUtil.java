package com.oxo.ball.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResponseMessageUtil {
    public static List<Map<String,Object>> responseMessage(String column,String msgKey){
        List<Map<String, Object>> errorList = new ArrayList<>();
        Map<String, Object> data = new HashMap<>();
        data.put("name", column);
        data.put("msgKey", msgKey);
        errorList.add(data);
        return errorList;
    }
    public static void responseMessage(List<Map<String,Object>> list,String column,String msgKey){
        Map<String, Object> data = new HashMap<>();
        data.put("name", column);
        data.put("msgKey", msgKey);
        list.add(data);
    }
    public static void responseMessageValue(List<Map<String,Object>> list,String column,String msgKey){
        Map<String, Object> data = new HashMap<>();
        data.put("name", column);
        data.put("value", msgKey);
        list.add(data);
    }
}
