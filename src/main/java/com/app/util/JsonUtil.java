package com.app.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

public class JsonUtil {

    private static ObjectMapper mapper;

    static {
        mapper = new ObjectMapper();
        // Include.NON_NULL 属性为NULL 不序列化
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        SimpleDateFormat smt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // 时间类型格式化
        mapper.setDateFormat(smt);
        // 处理json中缺少映射字段异常
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * 实体类转化成json字符串
     * @param object
     * @return
     */
    public static String objToJson(Object object){
        return JSONArray.fromObject(object).toString();
    }

    /**
     * XML转JSON
     * @param xml
     * @return
     */
    public static JSONObject xmlToJson(String xml) {
        XMLSerializer xmlSerializer = new XMLSerializer();
        String resultStr = xmlSerializer.read(xml).toString();
        return JSONObject.fromObject(resultStr);
    }


    /**
     * 对象转JSON字符串
     *
     * @param obj           被转换对象
     * @return json字符串
     */
    public static <T> String beanToJson(T obj) {
        if (null == obj) {
            return null;
        }
        try {
            return mapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * JSON字符串转换为对象
     *
     * @param json          json字符串
     * @param claxx         转换类型
     * @param <T>           泛型类型
     * @return 转换后对象
     */
    public static <T> T jsonToBean(String json, Class<T> claxx) {
        T t = null;
        try {
            t = mapper.readValue(json, claxx);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 对象转换map
     *
     * @param obj           对象
     * @param <T>           对象类型
     * @return map
     */
    public static <T> Map beanToMap(T obj) {
        Map map = null;
        try {
            String json = beanToJson(obj);
            map = mapper.readValue(json, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


}
