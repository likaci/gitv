package com.gala.sdk.plugin.server.storage;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class StorageException implements Parcelable {
    public static Creator<StorageException> CREATOR = new Creator<StorageException>() {
        public StorageException[] newArray(int size) {
            return new StorageException[size];
        }

        public StorageException createFromParcel(Parcel source) {
            StorageException exception = new StorageException();
            exception.readFromParcel(source);
            return exception;
        }
    };
    private static final String KEY_STORAGE_EXCEPTION = "storage_exception";
    private Throwable mThrowable;

    public void setThrowable(Throwable throwable) {
        this.mThrowable = throwable;
        if (this.mThrowable != null) {
            this.mThrowable.printStackTrace();
        }
    }

    public Throwable getThrowable() {
        return this.mThrowable;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        new Bundle().putSerializable(KEY_STORAGE_EXCEPTION, this.mThrowable);
    }

    public void readFromParcel(Parcel reply) {
        setThrowable((Throwable) reply.readBundle(StorageException.class.getClassLoader()).getSerializable(KEY_STORAGE_EXCEPTION));
    }
}
