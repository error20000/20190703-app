package com.jian.system.utils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataCacheUtils {

    static Map<String, Object> cache = new ConcurrentHashMap<>();

    public static <T> T getObject(String key, Class<T> clazz){
        return (T) cache.get(key);
    }

    public static <T> List<T> getList(String key, Class<T> clazz){
        return (List<T>) cache.get(key);
    }

    public static void set(String key, Object obj){
        cache.put(key, obj);
    }

    public static void clear(String key){
        cache.remove(key);
    }
}
