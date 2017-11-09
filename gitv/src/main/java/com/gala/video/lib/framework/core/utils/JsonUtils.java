package com.gala.video.lib.framework.core.utils;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;

public class JsonUtils {
    private static Gson mGson = new Gson();

    public static String toJSONString(JSONObject jsonParam) {
        String str = null;
        if (jsonParam != null) {
            try {
                str = jsonParam.toJSONString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return str;
    }

    public static <T> T deserialize(String json, Type type) throws JsonSyntaxException {
        return mGson.fromJson(json, type);
    }

    public static <T> String toJson(T t) {
        if (t == null) {
            return "";
        }
        try {
            return mGson.toJson((Object) t);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
