package com.gala.video.app.epg.ui.setting.data;

import com.gala.video.lib.framework.core.proguard.Keep;

@Keep
public class CustomDataInfo {
    private String mItemOption = "";
    private String mKey = "";
    private String mValue = "";

    public CustomDataInfo(String key, String value, String itemOption) {
        this.mKey = key;
        this.mValue = value;
        this.mItemOption = itemOption;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getValue() {
        return this.mValue;
    }

    public String getItemOption() {
        return this.mItemOption;
    }

    public void setKey(String key) {
        this.mKey = key;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    public void setItemOption(String itemcontent) {
        this.mItemOption = itemcontent;
    }
}
