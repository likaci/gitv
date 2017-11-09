package com.tvos.appmanager.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class StorageStatus implements Parcelable {
    public static final Creator<StorageStatus> CREATOR = new C20661();
    private long RomAvailableSize = 0;
    private long RomTotalSize = 0;
    private long SDAvailableSize = 0;
    private long SDTotalSize = 0;

    class C20661 implements Creator<StorageStatus> {
        C20661() {
        }

        public StorageStatus[] newArray(int size) {
            return new StorageStatus[size];
        }

        public StorageStatus createFromParcel(Parcel source) {
            return new StorageStatus(source);
        }
    }

    public StorageStatus(Parcel p) {
        this.SDTotalSize = p.readLong();
        this.SDAvailableSize = p.readLong();
        this.RomTotalSize = p.readLong();
        this.RomAvailableSize = p.readLong();
    }

    public long getSDTotalSize() {
        return this.SDTotalSize;
    }

    public void setSDTotalSize(long sDTotalSize) {
        this.SDTotalSize = sDTotalSize;
    }

    public long getSDAvailableSize() {
        return this.SDAvailableSize;
    }

    public void setSDAvailableSize(long sDAvailableSize) {
        this.SDAvailableSize = sDAvailableSize;
    }

    public long getRomTotalSize() {
        return this.RomTotalSize;
    }

    public void setRomTotalSize(long romTotalSize) {
        this.RomTotalSize = romTotalSize;
    }

    public long getRomAvailableSize() {
        return this.RomAvailableSize;
    }

    public void setRomAvailableSize(long romAvailableSize) {
        this.RomAvailableSize = romAvailableSize;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.SDTotalSize);
        dest.writeLong(this.SDAvailableSize);
        dest.writeLong(this.RomTotalSize);
        dest.writeLong(this.RomAvailableSize);
    }
}
