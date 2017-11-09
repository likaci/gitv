package com.gala.video.app.player.albumdetail.data.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.result.ApiResultAlbum;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;

public class DetailInfoCompleteTask {
    private static final String TAG = "DetailInfoCompleteTask";
    private Album mAlbum;
    private IApiCallback<ApiResultAlbum> mFirstCompleteCallback = new C13142();
    private IDetailInfoCompleteTaskListener mListener;
    private IApiCallback<ApiResultAlbum> mSecondCompleteCallback = new C13153();

    class C13131 implements Runnable {
        C13131() {
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(DetailInfoCompleteTask.TAG, "execute, mAlbum=" + DetailInfoCompleteTask.this.mAlbum);
            }
            if (DetailInfoCompleteTask.this.mAlbum == null || StringUtils.isEmpty(DetailInfoCompleteTask.this.mAlbum.tvQid)) {
                DetailInfoCompleteTask.this.mListener.onFailed(new ApiException("execute, invalid mAlbum=" + DetailInfoCompleteTask.this.mAlbum));
                return;
            }
            TVApi.albumInfo.call(DetailInfoCompleteTask.this.mFirstCompleteCallback, DetailInfoCompleteTask.this.mAlbum.tvQid);
        }
    }

    class C13142 implements IApiCallback<ApiResultAlbum> {
        C13142() {
        }

        public void onSuccess(ApiResultAlbum apiResultAlbum) {
            if (DetailInfoCompleteTask.this.mListener != null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(DetailInfoCompleteTask.TAG, "mFirstCompleteCallback, onSuccess, apiResultAlbum.data=" + apiResultAlbum.data);
                }
                DetailInfoCompleteTask.this.notifyInfoCompleteSuccess(apiResultAlbum.data);
            }
        }

        public void onException(ApiException e) {
            TVApi.albumInfo.call(DetailInfoCompleteTask.this.mSecondCompleteCallback, DetailInfoCompleteTask.this.mAlbum.tvQid);
        }
    }

    class C13153 implements IApiCallback<ApiResultAlbum> {
        C13153() {
        }

        public void onSuccess(ApiResultAlbum apiResultAlbum) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(DetailInfoCompleteTask.TAG, "mSecondCompleteCallback, onSuccess, apiResultAlbum.data=" + apiResultAlbum.data);
            }
            DetailInfoCompleteTask.this.notifyInfoCompleteSuccess(apiResultAlbum.data);
        }

        public void onException(ApiException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(DetailInfoCompleteTask.TAG, "mSecondCompleteCallback, onException, e=" + e);
            }
            DetailInfoCompleteTask.this.mListener.onFailed(e);
        }
    }

    public interface IDetailInfoCompleteTaskListener {
        void onFailed(ApiException apiException);

        void onSuccess(Album album);
    }

    public DetailInfoCompleteTask(Album album) {
        this.mAlbum = album;
    }

    public void setTaskListener(IDetailInfoCompleteTaskListener listener) {
        this.mListener = listener;
    }

    public void execute() {
        ThreadUtils.execute(new C13131());
    }

    private void notifyInfoCompleteSuccess(Album album) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> notifyInfoCompleteSuccess, mAlbum=" + this.mAlbum + ", album=" + album);
        }
        if (album != null) {
            this.mListener.onSuccess(album);
        } else {
            this.mListener.onFailed(new ApiException("album from tvApi(albumInfo) is null."));
        }
    }
}
