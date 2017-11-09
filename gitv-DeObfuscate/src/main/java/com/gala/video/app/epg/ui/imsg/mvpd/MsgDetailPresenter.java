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
    private IImageLoadCallback mImageCallback = new C09011();
    private ImageLoader mImageViewLoader;
    View mView;

    class C09011 implements IImageLoadCallback {
        C09011() {
        }

        public void onSuccess(Bitmap bitmap) {
            LogUtils.m1568d(MsgDetailPresenter.TAG, "mImageCallback > onSuccess");
            MsgDetailPresenter.this.mView.showImage(bitmap);
        }

        public void onFailed(String url) {
            LogUtils.m1571e(MsgDetailPresenter.TAG, "mImageCallback > onFailed");
        }
    }

    public MsgDetailPresenter(View view) {
        this.mView = (View) ActivityUtils.checkNotNull(view, "View cannot be null!");
        this.mView.setPresenter(this);
    }

    public void start() {
    }

    public void onDestroy() {
        if (!this.mImageViewLoader.isRecycled()) {
            LogUtils.m1568d(TAG, "recycleImage");
            this.mImageViewLoader.recycle();
        }
    }

    public void onLoadImage(String imageurl) {
        this.mImageViewLoader = new ImageLoader();
        this.mImageViewLoader.setImageLoadCallback(this.mImageCallback);
        this.mImageViewLoader.loadImage(imageurl, null);
    }
}
