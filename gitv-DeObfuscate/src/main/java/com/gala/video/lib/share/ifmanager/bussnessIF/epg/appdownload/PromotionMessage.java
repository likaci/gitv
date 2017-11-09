package com.gala.video.lib.share.ifmanager.bussnessIF.epg.appdownload;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

public class PromotionMessage implements Serializable {
    private static final long serialVersionUID = -4747761190124312132L;
    @JSONField(name = "code")
    private String code;
    @JSONField(name = "data")
    private PromotionAppInfoWithDocument document;
    @JSONField(serialize = false)
    private String id;
    @JSONField(name = "msg")
    private String msg;
    private int type = -1;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public PromotionAppInfoWithDocument getDocument() {
        return this.document;
    }

    public void setDocument(PromotionAppInfoWithDocument document) {
        this.document = document;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String toString() {
        return "PromotionMessage{id='" + this.id + '\'' + ", msg='" + this.msg + '\'' + ", code='" + this.code + '\'' + ", document=" + this.document + '}';
    }
}
