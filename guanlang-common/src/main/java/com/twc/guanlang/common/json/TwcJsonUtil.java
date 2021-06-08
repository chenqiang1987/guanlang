package com.twc.guanlang.common.json;

import net.sf.json.JSONObject;

import java.util.Map;

public class TwcJsonUtil {

    public static JSONObject reverseObj2JsonObj(Object o) {

        return JSONObject.fromObject(o);
    }

    public static JSONObject bean2JsonByParams(Map<String, String> param) {

        JSONObject jsonObject = new JSONObject();

        param.keySet().forEach((key) -> {
            jsonObject.accumulate((String) key, param.get(key).trim());
        });

        return jsonObject;
    }
}
