package com.gala.video.lib.share.ifimpl.netdiagnose.collection.check;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class AlbumListCheck extends CheckTask {
    private static final String PAGE_NO = "1";
    private static final String PAGE_SIZE = "60";
    private static final String TAG = "AlbumListCheck";
    private static final String mChannelID = "1";
    private static final String tagID = "293208302";

    public AlbumListCheck(CheckEntity checkEntity) {
        super(checkEntity);
    }

    public boolean runCheck() {
        final long startTime = System.currentTimeMillis();
        TVApi.albumList.callSync(new IApiCallback<ApiResultAlbumList>() {
            public void onSuccess(ApiResultAlbumList apiResult) {
                String dataList;
                Album album = (Album) apiResult.getAlbumList().get(0);
                AlbumListCheck.this.mCheckEntity.setAlbum(album);
                LogUtils.d(AlbumListCheck.TAG, " AlbumListCheck apiResult.getAlbumList().get(0)= " + album.name);
                if (apiResult.data.size() >= 5) {
                    dataList = apiResult.data.subList(0, 5).toString();
                } else {
                    dataList = apiResult.data.toString();
                }
                AlbumListCheck.this.mCheckEntity.add("AlbumListCheck apiResult success , use time:" + (System.currentTimeMillis() - startTime));
                AlbumListCheck.this.mCheckEntity.add(", result = ");
                AlbumListCheck.this.mCheckEntity.add(dataList);
            }

            public void onException(ApiException apiException) {
                LogUtils.e(AlbumListCheck.TAG, " AlbumListCheck ApiException code= " + apiException.getCode());
                LogUtils.e(AlbumListCheck.TAG, " AlbumListCheck ApException cause= " + apiException.getCause());
                AlbumListCheck.this.mCheckEntity.add("AlbumListCheck onException: code=" + apiException.getCode() + ", msg=" + apiException.getMessage() + ", url=" + apiException.getUrl());
            }
        }, "1", null, "", "1", PAGE_SIZE, tagID);
        return true;
    }
}
