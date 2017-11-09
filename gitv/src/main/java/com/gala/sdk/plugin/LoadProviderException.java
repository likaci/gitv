package com.gala.sdk.plugin;

public class LoadProviderException extends Throwable {
    private static final long serialVersionUID = 4326863282812033176L;
    private Throwable mThrowable;
    public String mType;

    public LoadProviderException(String type, Throwable e) {
        this.mType = type;
        this.mThrowable = e;
    }

    public String getType() {
        return this.mType;
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("LoadProviderException(").append("type=").append(this.mType).append(", throwable=").append(this.mThrowable != null ? this.mThrowable.toString() : "").append(")");
        if (this.mThrowable != null) {
            this.mThrowable.printStackTrace();
        }
        return builder.toString();
    }
}
