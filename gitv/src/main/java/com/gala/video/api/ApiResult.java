package com.gala.video.api;

import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import java.util.ArrayList;
import java.util.List;

public class ApiResult {
    private int a = 0;
    private List<Integer> f69a;
    public String code;
    public String json;
    public String msg;

    public boolean isFailed() {
        return this.code == null || this.code.startsWith("E") || !this.code.startsWith("N");
    }

    public boolean isSuccessfull() {
        return this.code != null && (this.code.startsWith("N") || this.code.equals(IAlbumConfig.NET_ERROE_CODE));
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setMsg(String message) {
        this.msg = message;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setRequestTimes(List<Integer> times) {
        if (times != null && times.size() > 0) {
            this.f69a = new ArrayList(times.size());
            for (Integer intValue : times) {
                this.f69a.add(Integer.valueOf(intValue.intValue()));
            }
        }
    }

    public List<Integer> getRequestTimes() {
        return this.f69a;
    }

    public void setParseTime(int time) {
        this.a = time;
    }

    public int getParseTime() {
        return this.a;
    }
}
