package com.gala.video.lib.share.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import java.io.InputStream;
import org.apache.http.util.EncodingUtils;

public class AssetsUtils {
    public static JSONObject getJSONObject(String jsonStream) {
        JSONObject obj = null;
        try {
            obj = JSON.parseObject(jsonStream, Feature.OrderedField);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static String getStreamFromAssets(String jsonPath) {
        String data = "";
        try {
            InputStream stream = AppRuntimeEnv.get().getApplicationContext().getAssets().open(jsonPath);
            byte[] b = new byte[stream.available()];
            stream.read(b);
            data = EncodingUtils.getString(b, "UTF-8");
        } catch (Exception e) {
        }
        return data;
    }
}
