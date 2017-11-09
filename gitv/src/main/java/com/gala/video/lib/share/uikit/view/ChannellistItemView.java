package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.graphics.Bitmap;
import com.gala.cloudui.block.CuteImage;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.uikit.contract.ItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.ImageCropModel;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;

public class ChannellistItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter> {
    private static final String TAG = "ChannellistItemView";
    private IImageLoadCallback mImageCallback = new IImageLoadCallback() {
        public void onSuccess(Bitmap bitmap) {
            CuteImage coreImageView = ChannellistItemView.this.getCoreImageView();
            if (coreImageView != null) {
                coreImageView.setBitmap(bitmap);
            }
        }

        public void onFailed(String url) {
            LogUtils.e(ChannellistItemView.TAG, "ChannellistItemView>mImageCallback > onFailed --- getCoreImageView() = " + ChannellistItemView.this.getCoreImageView() + " ;url = " + url);
            ChannellistItemView.this.setDefaultImage();
        }
    };
    private ImageCropModel mImageModel = new ImageCropModel();
    private String mImageUrl;
    private ItemInfoModel mItemModel;
    private ImageLoader mLoader = new ImageLoader();

    public ChannellistItemView(Context context) {
        super(context);
        this.mLoader.setImageLoadCallback(this.mImageCallback);
    }

    public void onBind(Presenter object) {
        loadUIStyle(object);
        this.mItemModel = object.getModel();
        updateUI(this.mItemModel);
        setDefaultImage();
        this.mImageUrl = this.mItemModel.getCuteViewData("ID_IMAGE", "value");
        setContentDescription(this.mItemModel.getCuteViewData("ID_TITLE", "text"));
    }

    public void onUnbind(Presenter object) {
        recycleAndShowDefaultImage();
        recycle();
    }

    public void onShow(Presenter object) {
        loadImage();
    }

    public void onHide(Presenter object) {
        recycleAndShowDefaultImage();
    }

    private void setDefaultImage() {
        CuteImage coreImageView = getCoreImageView();
        if (coreImageView != null) {
            coreImageView.setDrawable(coreImageView.getDefaultDrawable());
        }
    }

    private CuteImage getCoreImageView() {
        return getCuteImage("ID_IMAGE");
    }

    protected void loadUIStyle(Presenter object) {
        setStyleByName(object.getModel().getStyle());
    }

    private void loadImage() {
        this.mLoader.loadImage(this.mImageUrl, getImageCropModel());
    }

    private void recycleAndShowDefaultImage() {
        if (!this.mLoader.isRecycled()) {
            this.mLoader.recycle();
            setDefaultImage();
        }
    }

    private ImageCropModel getImageCropModel() {
        this.mImageModel.width = getCoreImageView().getWidth();
        this.mImageModel.height = getCoreImageView().getHeight();
        this.mImageModel.cropType = ScaleType.NO_CROP;
        this.mImageModel.radius = 0;
        return this.mImageModel;
    }
}
