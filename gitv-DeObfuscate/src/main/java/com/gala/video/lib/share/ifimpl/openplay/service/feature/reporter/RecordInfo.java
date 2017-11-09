package com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter;

import com.gala.tvapi.tv2.model.Album;

public class RecordInfo {
    private int mOperation;
    private int mRecordType;
    private boolean mResult;
    private Album mTvapiAlbum;
    private String mUserToken;

    public RecordInfo(Album album) {
        this.mTvapiAlbum = album;
    }

    public Album getAlbum() {
        return this.mTvapiAlbum;
    }

    public void setAlbum(Album album) {
        this.mTvapiAlbum = album;
    }

    public String getUserToken() {
        return this.mUserToken;
    }

    public void setUserToken(String userToken) {
        this.mUserToken = userToken;
    }

    public boolean isResult() {
        return this.mResult;
    }

    public void setmResult(boolean result) {
        this.mResult = result;
    }

    public int getRecordType() {
        return this.mRecordType;
    }

    public void setRecordType(int recordType) {
        this.mRecordType = recordType;
    }

    public int getOperation() {
        return this.mOperation;
    }

    public void setOperation(int operation) {
        this.mOperation = operation;
    }
}
