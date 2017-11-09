package com.gala.download.base;

import com.gala.imageprovider.p000private.C0126b;
import com.gala.imageprovider.p000private.C0151w;

public class FileRequest {
    private int f494a;
    private C0151w f495a = new C0151w();
    private Object f496a;
    private String f497a;
    private boolean f498a;
    private String f499b;
    private boolean f500b = false;
    private boolean f501c = true;

    public FileRequest(String imageUrl) {
        this.f497a = imageUrl;
    }

    public FileRequest(String imageUrl, Object cookie) {
        this.f497a = imageUrl;
        this.f496a = cookie;
    }

    public void setLasting(boolean isLasting) {
        this.f498a = isLasting;
    }

    public String getUrl() {
        return this.f497a;
    }

    public Object getCookie() {
        return this.f496a;
    }

    public boolean isLasting() {
        return this.f498a;
    }

    public void setSavePath(String savePath) {
        this.f499b = savePath;
    }

    public String getSavePath() {
        return this.f499b;
    }

    public C0151w getSameTaskQueue() {
        return this.f495a;
    }

    public boolean getShouldBeKilled() {
        return this.f501c;
    }

    public void setShouldBeKilled(boolean kill) {
        this.f501c = kill;
    }

    public boolean equals(Object o) {
        int i = 0;
        if (o == null || !(o instanceof FileRequest)) {
            return false;
        }
        int i2;
        FileRequest fileRequest = (FileRequest) o;
        String url = fileRequest.getUrl();
        if (url == null) {
            url = "";
        }
        boolean equals = url.equals(this.f497a);
        Object cookie = getCookie();
        if (cookie == null) {
            cookie = new Object();
        }
        if (cookie == fileRequest.getCookie()) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i2 &= equals;
        if (this.f499b == fileRequest.f499b || (this.f499b != null && this.f499b.equals(fileRequest.f499b))) {
            i = 1;
        }
        return i & i2;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImageRequest@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        stringBuilder.append("{");
        stringBuilder.append("url=");
        stringBuilder.append(this.f497a);
        stringBuilder.append(", isLasting=");
        stringBuilder.append(this.f498a);
        stringBuilder.append(", savePath=");
        stringBuilder.append(this.f499b);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static boolean checkRequestValid(FileRequest request) {
        if (request == null) {
            return false;
        }
        if (C0126b.m307a(request.getUrl())) {
            return false;
        }
        return true;
    }

    public boolean getStopFlag() {
        return this.f500b;
    }

    public void setStopFlag(boolean flag) {
        this.f500b = flag;
    }

    public int getLimitSize() {
        return this.f494a;
    }

    public void setLimitSize(int limitSize) {
        this.f494a = limitSize;
    }
}
