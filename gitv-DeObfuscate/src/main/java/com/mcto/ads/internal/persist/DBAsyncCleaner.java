package com.mcto.ads.internal.persist;

import android.os.AsyncTask;

public class DBAsyncCleaner extends AsyncTask<Void, Void, Void> {
    private StorageManager storageManager;

    public DBAsyncCleaner(StorageManager storageManager) {
        this.storageManager = storageManager;
    }

    protected Void doInBackground(Void... params) {
        if (this.storageManager != null) {
            this.storageManager.checkValidityOfNativeVideoItems();
        }
        return null;
    }
}
