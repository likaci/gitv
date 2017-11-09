package com.gala.download.base;

import com.gala.imageprovider.private.b;
import com.gala.imageprovider.private.w;

public class FileRequest {
    private int a;
    private w f286a = new w();
    private Object f287a;
    private String f288a;
    private boolean f289a;
    private String b;
    private boolean f290b = false;
    private boolean c = true;

    public FileRequest(String imageUrl) {
        this.f288a = imageUrl;
    }

    public FileRequest(String imageUrl, Object cookie) {
        this.f288a = imageUrl;
        this.f287a = cookie;
    }

    public void setLasting(boolean isLasting) {
        this.f289a = isLasting;
    }

    public String getUrl() {
        return this.f288a;
    }

    public Object getCookie() {
        return this.f287a;
    }

    public boolean isLasting() {
        return this.f289a;
    }

    public void setSavePath(String savePath) {
        this.b = savePath;
    }

    public String getSavePath() {
        return this.b;
    }

    public w getSameTaskQueue() {
        return this.f286a;
    }

    public boolean getShouldBeKilled() {
        return this.c;
    }

    public void setShouldBeKilled(boolean kill) {
        this.c = kill;
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
        boolean equals = url.equals(this.f288a);
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
        if (this.b == fileRequest.b || (this.b != null && this.b.equals(fileRequest.b))) {
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
        stringBuilder.append(this.f288a);
        stringBuilder.append(", isLasting=");
        stringBuilder.append(this.f289a);
        stringBuilder.append(", savePath=");
        stringBuilder.append(this.b);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static boolean checkRequestValid(FileRequest request) {
        if (request == null) {
            return false;
        }
        if (b.a(request.getUrl())) {
            return false;
        }
        return true;
    }

    public boolean getStopFlag() {
        return this.f290b;
    }

    public void setStopFlag(boolean flag) {
        this.f290b = flag;
    }

    public int getLimitSize() {
        return this.a;
    }

    public void setLimitSize(int limitSize) {
        this.a = limitSize;
    }
}
