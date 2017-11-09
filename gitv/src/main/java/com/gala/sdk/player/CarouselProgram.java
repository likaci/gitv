package com.gala.sdk.player;

public class CarouselProgram {
    private long mBeAid;
    private String mBeAname;
    private long mBeCid;
    private long mBeOrder;
    private long mBeQid;
    private long mBeSid;
    private long mBea;
    private long mBt;
    private long mContentType;
    private long mEt;
    private long mId;
    private String mName;

    public void setId(long id) {
        this.mId = id;
    }

    public void setBt(long bt) {
        this.mBt = bt;
    }

    public void setEt(long et) {
        this.mEt = et;
    }

    public void setBeQid(long beQid) {
        this.mBeQid = beQid;
    }

    public void setBea(long bea) {
        this.mBea = bea;
    }

    public void setBeAid(long beAid) {
        this.mBeAid = beAid;
    }

    public void setBeSid(long beSid) {
        this.mBeSid = beSid;
    }

    public void setBeCid(long beCid) {
        this.mBeCid = beCid;
    }

    public void setBeOrder(long beOrder) {
        this.mBeOrder = beOrder;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setBeAname(String beAname) {
        this.mBeAname = beAname;
    }

    public void setContentType(long contentType) {
        this.mContentType = contentType;
    }

    public long getId() {
        return this.mId;
    }

    public long getBt() {
        return this.mBt;
    }

    public long getEt() {
        return this.mEt;
    }

    public long getBeQid() {
        return this.mBeQid;
    }

    public long getBea() {
        return this.mBea;
    }

    public long getBeAid() {
        return this.mBeAid;
    }

    public long getBeSid() {
        return this.mBeSid;
    }

    public long getBeCid() {
        return this.mBeCid;
    }

    public long getBeOrder() {
        return this.mBeOrder;
    }

    public String getName() {
        return this.mName;
    }

    public String getBeAname() {
        return this.mBeAname;
    }

    public long getContenType() {
        return this.mContentType;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CarouselProgram@").append(Integer.toHexString(hashCode())).append("{");
        stringBuilder.append("id=").append(this.mId);
        stringBuilder.append(", bt=").append(this.mBt);
        stringBuilder.append(", et=").append(this.mEt);
        stringBuilder.append(", beQid=").append(this.mBeQid);
        stringBuilder.append(", bea=").append(this.mBea);
        stringBuilder.append(", beAid=").append(this.mBeAid);
        stringBuilder.append(", beSid=").append(this.mBeSid);
        stringBuilder.append(", beCid=").append(this.mBeCid);
        stringBuilder.append(", beOrder=").append(this.mBeOrder);
        stringBuilder.append(", name=").append(this.mName);
        stringBuilder.append(", beAname=").append(this.mBeAname);
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
