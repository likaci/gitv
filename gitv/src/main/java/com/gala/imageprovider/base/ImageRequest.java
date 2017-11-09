package com.gala.imageprovider.base;

import android.graphics.Bitmap.Config;
import com.gala.imageprovider.private.b;

public class ImageRequest {
    private float a = 32.0f;
    private int f9a = 0;
    private Config f10a = Config.ARGB_8888;
    private ImageType f11a = ImageType.DEFAULT;
    private ScaleType f12a = ScaleType.DEFAULT;
    private Object f13a;
    private String f14a;
    private boolean f15a;
    private int b = 0;
    private String f16b;
    private boolean f17b;
    private boolean c = false;
    private boolean d = true;

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
        this.f14a = imageUrl;
    }

    public ImageRequest(String imageUrl, Object cookie) {
        this.f14a = imageUrl;
        this.f13a = cookie;
    }

    public void setLasting(boolean isLasting) {
        this.f15a = isLasting;
    }

    public String getUrl() {
        return this.f14a;
    }

    public Object getCookie() {
        return this.f13a;
    }

    public boolean isLasting() {
        return this.f15a;
    }

    public void setSavePath(String savePath) {
        this.f16b = savePath;
    }

    public String getSavePath() {
        return this.f16b;
    }

    public void setImageType(ImageType type) {
        this.f11a = type;
    }

    public ImageType getImageType() {
        return this.f11a;
    }

    public void setScaleType(ScaleType scaleType) {
        this.f12a = scaleType;
    }

    public ScaleType getScaleType() {
        return this.f12a;
    }

    public void setRadius(float radius) {
        this.a = radius;
    }

    public float getRadius() {
        return this.a;
    }

    public int getTargetWidth() {
        return this.f9a;
    }

    public void setTargetWidth(int width) {
        this.f9a = width;
    }

    public int getTargetHeight() {
        return this.b;
    }

    public void setTargetHeight(int height) {
        this.b = height;
    }

    public void setDecodeConfig(Config config) {
        this.f10a = config;
        this.f17b = true;
    }

    public Config getDecodeConfig() {
        return this.f10a;
    }

    public boolean isArbitraryDecodeConfig() {
        return this.f17b;
    }

    public boolean getShouldBeKilled() {
        return this.d;
    }

    public void setShouldBeKilled(boolean kill) {
        this.d = kill;
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
        boolean equals = url.equals(this.f14a);
        Object cookie = getCookie();
        if (cookie == null) {
            cookie = new Object();
        }
        int i3 = equals & (cookie == imageRequest.getCookie() ? 1 : 0);
        if (this.f10a == imageRequest.f10a) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.f9a == imageRequest.f9a) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.b == imageRequest.b) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.f16b == imageRequest.f16b || (this.f16b != null && this.f16b.equals(imageRequest.f16b))) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i3 &= i2;
        if (this.a == imageRequest.a) {
            i2 = 1;
        } else {
            i2 = 0;
        }
        i2 &= i3;
        if (this.f11a != imageRequest.f11a) {
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
        stringBuilder.append(this.f14a);
        stringBuilder.append(", isLasting=");
        stringBuilder.append(this.f15a);
        stringBuilder.append(", target w/h=");
        stringBuilder.append(this.f9a).append("/").append(this.b);
        stringBuilder.append(", radius=");
        stringBuilder.append(this.a);
        stringBuilder.append(", savePath=");
        stringBuilder.append(this.f16b);
        stringBuilder.append(", scaleType=").append(this.f12a);
        stringBuilder.append(", decodeConfig=").append(this.f10a);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }

    public static boolean checkRequestValid(ImageRequest request) {
        if (request == null) {
            return false;
        }
        if (b.a(request.getUrl())) {
            return false;
        }
        return true;
    }

    public boolean getStopFlag() {
        return this.c;
    }

    public void setStopFlag(boolean flag) {
        this.c = flag;
    }
}
