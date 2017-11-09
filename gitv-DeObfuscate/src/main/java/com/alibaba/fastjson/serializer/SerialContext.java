package com.alibaba.fastjson.serializer;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class SerialContext {
    public final int features;
    public final Object fieldName;
    public final Object object;
    public final SerialContext parent;

    public SerialContext(SerialContext parent, Object object, Object fieldName, int features, int fieldFeatures) {
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
        this.features = features;
    }

    public String toString() {
        if (this.parent == null) {
            return "$";
        }
        if (this.fieldName instanceof Integer) {
            return this.parent.toString() + "[" + this.fieldName + AlbumEnterFactory.SIGN_STR;
        }
        return this.parent.toString() + "." + this.fieldName;
    }
}
