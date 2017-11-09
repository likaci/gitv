package com.gala.video.lib.share.ifimpl.ads;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;

public class AdImageShowActivity extends QMultiScreenActivity {
    private static final int MSG_DOWNLOAD_IMAGE_SUCCESS = 100;
    private static final String TAG = "ads/view/AdImageShowDialog";
    private volatile Bitmap mAdBitmap;
    private ImageView mAdImgView;
    private MyHandler mHandler = new MyHandler(Looper.getMainLooper());
    private IImageCallback mImageCallback = new IImageCallback() {
        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            LogUtils.d(AdImageShowActivity.TAG, "down load image success");
            AdImageShowActivity.this.mAdBitmap = bitmap;
            AdImageShowActivity.this.mHandler.sendEmptyMessage(100);
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
            LogUtils.d(AdImageShowActivity.TAG, "down load image failed, url  = " + imageRequest.getUrl());
        }
    };
    private IImageProvider mImageProvider = ImageProviderApi.getImageProvider();
    private String mImageUrl;

    @SuppressLint({"HandlerLeak"})
    private class MyHandler extends Handler {
        private MyHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    AdImageShowActivity.this.mAdImgView.setScaleType(ScaleType.FIT_XY);
                    AdImageShowActivity.this.mAdImgView.setImageBitmap(AdImageShowActivity.this.mAdBitmap);
                    return;
                default:
                    return;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void onStart() {
        super.onStart();
        showImage();
    }

    private void initView() {
        setContentView(R.layout.share_layout_image_ad_loading);
        this.mAdImgView = (ImageView) findViewById(R.id.share_ad_imv_image);
    }

    private void showImage() {
        LogUtils.d(TAG, "showImage");
        this.mImageUrl = getIntent().getStringExtra("adimageUrl");
        if (!StringUtils.isEmpty(this.mImageUrl)) {
            this.mAdBitmap = null;
            this.mImageProvider.loadImage(new ImageRequest(this.mImageUrl), this.mImageCallback);
        }
    }
}
