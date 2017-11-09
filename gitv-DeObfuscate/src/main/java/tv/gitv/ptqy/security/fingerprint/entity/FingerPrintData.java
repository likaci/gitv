package tv.gitv.ptqy.security.fingerprint.entity;

public class FingerPrintData {
    private long expireTime;
    private String fingerPrint;
    private long storeTime;

    public FingerPrintData(String fingerPrint, long storeTime, long expireTime) {
        this.fingerPrint = fingerPrint;
        this.storeTime = storeTime;
        this.expireTime = expireTime;
    }

    public long getStoreTime() {
        return this.storeTime;
    }

    public void setStoreTime(long storeTime) {
        this.storeTime = storeTime;
    }

    public long getExpireTime() {
        return this.expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    public String getFingerPrint() {
        return this.fingerPrint;
    }

    public void setFingerPrint(String fingerPrint) {
        this.fingerPrint = fingerPrint;
    }
}
