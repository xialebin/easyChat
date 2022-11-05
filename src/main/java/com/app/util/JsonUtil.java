package com.app.util;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

public class JsonUtil {

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

}
