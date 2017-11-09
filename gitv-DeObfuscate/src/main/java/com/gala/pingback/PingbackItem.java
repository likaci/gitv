package com.gala.pingback;

public class PingbackItem {
    private String mKey;
    private String mValue;

    public PingbackItem(String key, String value) {
        this.mKey = key;
        this.mValue = value;
    }

    public String getKey() {
        return this.mKey;
    }

    public String getValue() {
        return this.mValue;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PingbackItem@").append(Integer.toHexString(hashCode())).append("{");
        builder.append("mKey=").append(this.mKey);
        builder.append(", mValue=").append(this.mValue);
        builder.append("}");
        return builder.toString();
    }
}
