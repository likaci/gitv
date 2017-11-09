package com.alibaba.fastjson.parser;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.lang.reflect.Type;

public class ParseContext {
    public final Object fieldName;
    public Object object;
    public final ParseContext parent;
    public Type type;

    public ParseContext(ParseContext parent, Object object, Object fieldName) {
        this.parent = parent;
        this.object = object;
        this.fieldName = fieldName;
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
