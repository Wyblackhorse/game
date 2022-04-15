package com.oxo.ball.utils;

import com.google.common.collect.Maps;
import com.oxo.ball.bean.dao.BaseDAO;

import java.util.HashMap;
import java.util.Map;

public class MapUtil {
    public static Map<?, ?> objectToMap(Object obj) {
        if (obj == null) {
            return null;
        }
        return new org.apache.commons.beanutils.BeanMap(obj);
    }

    public static Map<String, Object> newMap(String key, Object value) {
        HashMap<String, Object> map = Maps.newHashMap();
        map.put(key,value);
        return map;
    }

    public static void setCreateTime(BaseDAO base){
        base.setCreatedAt(System.currentTimeMillis());
        base.setUpdatedAt(0L);
    }
}
