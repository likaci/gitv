package com.gala.imageprovider.base;

import android.graphics.Bitmap.Config;
import com.gala.imageprovider.p000private.C0126b;

public class ImageRequest {
    private float f505a = 32.0f;
    private int f506a = 0;
    private Config f507a = Config.ARGB_8888;
    private ImageType f508a = ImageType.DEFAULT;
    private ScaleType f509a = ScaleType.DEFAULT;
    private Object f510a;
    private String f511a;
    private boolean f512a;
    private int f513b = 0;
    private String f514b;
    private boolean f515b;
    private boolean f516c = false;
    private boolean f517d = true;

    public enum ImageType {
        DEFAULT,
        RECT,
        ROUND
    }

    public enum ScaleType {
        DEFAULT,
        CENTER_INSIDE,
        CENTER_CROP,
        NO_CROP
    }

    public ImageRequest(String imageUrl) {
        this.f511a = imageUrl;
    }

    public ImageRequest(String imageUrl, Object cookie) {
        this.f511a = imageUrl;
        this.f510a = cookie;
    }

    public void setLasting(boolean isLasting) {
        this.f512a = isLasting;
    }

    public String getUrl() {
        return this.f511a;
    }

    public Object getCookie() {
        return this.f510a;
    }

    public boolean isLasting() {
        return this.f512a;
    }

    public void setSavePath(String savePath) {
        this.f514b = savePath;
    }

    public String getSavePath() {
        return this.f514b;
    }

    public void setImageType(ImageType type) {
        this.f508a = type;
    }

    public ImageType getImageType() {
        return this.f508a;
    }

    public void setScaleType(ScaleType scaleType) {
        this.f509a = scaleType;
    }

    public ScaleType getScaleType() {
        return this.f509a;
    }

    public void setRadius(float radius) {
        this.f505a = radius;
    }

    public float getRadius() {
        return this.f505a;
    }

    public int getTargetWidth() {
        return this.f506a;
    }

    public void setTargetWidth(int width) {
        this.f506a = width;
    }

    public int getTargetHeight() {
        return this.f513b;
    }

    public void setTargetHeight(int height) {
        this.f513b = height;
    }

    public void setDecodeConfig(Config config) {
        this.f507a = config;
        this.f515b = true;
    }

    public Config getDecodeConfig() {
        return this.f507a;
    }

    public boolean isArbitraryDecodeConfig() {
        return this.f515b;
    }

    public boolean getShouldBeKilled() {
        return this.f517d;
    }

    public void setShouldBeKilled(boolean kill) {
        this.f517d = kill;
    }

    public boolean equals(Object o) {
        int i = 1;
        if (o == null || !(o instanceof ImageRequest)) {
            return false;
        }
        int i2;
        ImageRequest imageRequest = (ImageRequest) o;
        String url = imageRequest.getUrl();
        if (url == null) {
            url = "";
        }
        boolean equals = url.equals(this.f511a);
        Object cookie = getCookie();
        if (cookie == null) {
            cookie = new Object();
        }
        int i3 = equals & (cookie == imageRequest.getCookie() ? 1 : 0);
        if (this.f507a == imageRequest.f507a) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.f506a == imageRequest.f506a) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.f513b == imageRequest.f513b) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.f514b == imageRequest.f514b || (this.f514b != null && this.f514b.equals(imageRequest.f514b))) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.f505a == imageRequest.f505a) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i2 &= i3;
        if (this.f508a != imageRequest.f508a) {
            i = 0;
        }
        return i2 & i;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("ImageRequest@");
        stringBuilder.append(Integer.toHexString(hashCode()));
        stringBuilder.append("{");
        stringBuilder.append("url=");
        stringBuilder.append(this.f511a);
        stringBuilder.append(", isLasting=");
        stringBuilder.append(this.f512a);
        stringBuilder.append(", target w/h=");
        stringBuilder.append(this.f506a).append("/").append(this.f513b);
        stringBuilder.append(", radius=");
        stringBuilder.append(this.f505a);
        stringBuilder.append(", savePath=");
        stringBuilder.append(this.f514b);
        stringBuilder.append(", scaleType=").append(this.f509a);
        stringBuilder.append(", decodeConfig=").append(this.f507a);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static boolean checkRequestValid(ImageRequest request) {
        if (request == null) {
            return false;
        }
        if (C0126b.m307a(request.getUrl())) {
            return false;
        }
        return true;
    }

    public boolean getStopFlag() {
        return this.f516c;
    }

    public void setStopFlag(boolean flag) {
        this.f516c = flag;
    }
}
