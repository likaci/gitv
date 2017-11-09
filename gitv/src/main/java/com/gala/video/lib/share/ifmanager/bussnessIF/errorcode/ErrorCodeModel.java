package com.gala.video.lib.share.ifmanager.bussnessIF.errorcode;

import java.io.Serializable;
import java.util.List;

public class ErrorCodeModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String code;
    private String content;
    private String desc;
    private String type;

    public static class ErrorCodeJSON {
        private List<ErrorCodeModel> data;

        public List<ErrorCodeModel> getData() {
            return this.data;
        }

        public void setData(List<ErrorCodeModel> data) {
            this.data = data;
        }
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
