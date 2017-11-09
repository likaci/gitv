package com.gala.multiscreen.dmr.model.msg;

import com.alibaba.fastjson.JSON;

public class MultiScreenMessage {
    public DlnaMessage cValue;
    public String control = "unknow";
    public String type = "result";
    public String value = "";
    public String version = "";

    public <T> T pressValue(Class<T> clazz) {
        if (this.value.equals("")) {
            return null;
        }
        return JSON.parseObject(this.value, (Class) clazz);
    }
}
