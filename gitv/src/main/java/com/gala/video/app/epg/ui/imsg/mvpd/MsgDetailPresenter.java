package com.gala.video.app.epg.ui.imsg.mvpd;

import android.graphics.Bitmap;
import com.gala.video.app.epg.ui.imsg.mvpd.MsgDetailContract.Presenter;
import com.gala.video.app.epg.ui.imsg.mvpd.MsgDetailContract.View;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;

public class MsgDetailPresenter implements Presenter {
    private static String TAG = "imsg/MsgDetailPresenter";
    private IImageLoadCallback mImageCallback = new IImageLoadCallback() {
        public void onSuccess(Bitmap bitmap) {
            LogUtils.d(MsgDetailPresenter.TAG, "mImageCallback > onSuccess");
            MsgDetailPresenter.this.mView.showImage(bitmap);
        }

        public void onFailed(String url) {
            LogUtils.e(MsgDetailPresenter.TAG, "mImageCallback > onFailed");
        }
    };
    private ImageLoader mImageViewLoader;
    View mView;

    public MsgDetailPresenter(View view) {
        this.mView = (View) ActivityUtils.checkNotNull(view, "View cannot be null!");
        this.mView.setPresenter(this);
    }

    public void start() {
    }

    public void onDestroy() {
        if (!this.mImageViewLoader.isRecycled()) {
            LogUtils.d(TAG, "recycleImage");
            this.mImageViewLoader.recycle();
        }
    }

    public void onLoadImage(String imageurl) {
        this.mImageViewLoader = new ImageLoader();
        this.mImageViewLoader.setImageLoadCallback(this.mImageCallback);
        this.mImageViewLoader.loadImage(imageurl, null);
    }
}
