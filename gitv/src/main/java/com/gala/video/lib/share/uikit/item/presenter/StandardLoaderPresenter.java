package com.gala.video.lib.share.uikit.item.presenter;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.video.lib.share.uikit.contract.StandardItemContract.StandardItemLoaderImp;
import com.gala.video.lib.share.uikit.contract.StandardItemContract.View;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IGifLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.ImageCropModel;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import pl.droidsonroids.gif.GifDrawable;

public class StandardLoaderPresenter implements StandardItemLoaderImp {
    private static final String TAG = "StandardLoaderPresenter";
    private ImageCallback mImageCallback = new ImageCallback();
    private ImageCropModel mImageModel = new ImageCropModel();
    private ItemInfoModel mItemInfoModel;
    private ImageLoader mLoader = new ImageLoader();
    private View mView;

    private class ImageCallback implements IImageLoadCallback, IGifLoadCallback {
        private ImageCallback() {
        }

        public void onSuccess(GifDrawable drawable) {
            if (drawable == null) {
                LogUtils.e(StandardLoaderPresenter.TAG, "loadImage onSuccess ,gifDrawable = null,so return");
                return;
            }
            if (StandardLoaderPresenter.this.mView != null) {
                StandardLoaderPresenter.this.mView.onLoadImageSuccess((Drawable) drawable);
            }
            if (drawable != null) {
                drawable.start();
            }
        }

        public void onSuccess(Bitmap bitmap) {
            if (StandardLoaderPresenter.this.mView != null) {
                StandardLoaderPresenter.this.mView.onLoadImageSuccess(bitmap);
            }
        }

        public void onFailed(String url) {
            LogUtils.e(StandardLoaderPresenter.TAG, "loadImage onfailure, url=" + url);
            if (StandardLoaderPresenter.this.mView != null) {
                StandardLoaderPresenter.this.mView.onLoadImageFail();
            }
        }
    }

    private void initLoader(View view, ItemInfoModel itemInfoModel) {
        this.mItemInfoModel = itemInfoModel;
        this.mView = view;
        this.mLoader.resetLoader();
        this.mLoader.setImageLoadCallback(this.mImageCallback);
    }

    public void loadImage(View view, ItemInfoModel itemInfoModel) {
        initLoader(view, itemInfoModel);
        String gifUrl = itemInfoModel.getCuteViewData("ID_IMAGE", UIKitConfig.KEY_GIF);
        if (TextUtils.isEmpty(gifUrl)) {
            loadJpg(itemInfoModel.getCuteViewData("ID_IMAGE", "value"));
        } else if (gifUrl.endsWith(".gif")) {
            loadGif(gifUrl);
        } else {
            LogUtils.e(TAG, "loadImage loadJpg, gifUrl=" + gifUrl);
            loadJpg(gifUrl);
        }
    }

    private void loadGif(String gifUrl) {
        this.mLoader.loadGif(gifUrl, this.mImageCallback);
    }

    private void loadJpg(String url) {
        if (TextUtils.isEmpty(url)) {
            LogUtils.e(TAG, "loadImage onfailure,because url= empty ");
            this.mView.onLoadImageFail();
            return;
        }
        this.mLoader.loadImage(url, getImageCropModel());
    }

    public void recycleAndShowDefaultImage() {
        if (!this.mLoader.isRecycled()) {
            this.mLoader.recycle();
            this.mView.setDefaultImage();
        }
    }

    private ImageCropModel getImageCropModel() {
        if (this.mView.isCircleNoTitleType() || this.mView.isCircleTitleType()) {
            this.mImageModel.width = this.mItemInfoModel.getWidth();
            this.mImageModel.height = this.mItemInfoModel.getWidth();
            this.mImageModel.cropType = ScaleType.CENTER_CROP;
            this.mImageModel.radius = this.mItemInfoModel.getWidth() >> 1;
        } else {
            this.mImageModel.width = this.mItemInfoModel.getWidth();
            this.mImageModel.height = this.mView.isTitleoutType() ? this.mItemInfoModel.getHeight() - ResourceUtil.getPx(54) : this.mItemInfoModel.getHeight();
            this.mImageModel.cropType = ScaleType.NO_CROP;
            this.mImageModel.radius = 0;
        }
        return this.mImageModel;
    }
}
