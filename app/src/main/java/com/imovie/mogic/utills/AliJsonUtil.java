package com.imovie.mogic.utills;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Lizhongren on 2015/12/24.
 */
public class AliJsonUtil {
    public static final SerializeConfig mapping = new SerializeConfig();
    public static final String dateFormat;
    static {
        dateFormat = "yyyy-MM-dd HH:mm:ss";
        mapping.put(Date.class, new SimpleDateFormatSerializer(dateFormat));
    }
    public static <T, K> Map<T, K> parseMapKey(String jsonString) {
        return JSON.parseObject(jsonString, new TypeReference<Map<T, K>>() {
        });
    }

    public static <T, K> List<Map<T, K>> listKeyMaps(String jsonString) {
        List<Map<T, K>> list = new ArrayList<Map<T, K>>();
        try {
            list = JSON.parseObject(jsonString, new TypeReference<List<Map<T, K>>>() {
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static <T> T parseObject(String jsonString, TypeReference<T> type) {
        return JSON.parseObject(jsonString, type);
    }

    public static <T> T parseObject(String jsonString, Class<T> cls) {
        return JSON.parseObject(jsonString, cls);
    }

    public static <T> List<T> parseList(String jsonStriung, Class<T> cls) {
        return JSON.parseArray(jsonStriung, cls);
    }

    public static String toJSONString(Object obj) {
        return JSON.toJSONString(obj,mapping);
    }
//    public static String toJSONString(Object obj,ComplexPropertyPreFilter filter) {
//        if(filter!=null && obj instanceof ResultModel){
//            filter.getIncludes().put(ResultModel.class, new String[] {"*"});
//        }
//        return JSON.toJSONString(obj,mapping,filter);
//    }

    public static <T> T parseObject(String jsonString, String key, Class<T> cls) {
        return JSON.parseObject(jsonString).getObject(key,cls);
    }

}
