package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.graphics.Bitmap;
import com.gala.cloudui.block.CuteImage;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.uikit.action.model.ApplicationActionModel;
import com.gala.video.lib.share.uikit.contract.AppItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.ImageCropModel;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;
import java.lang.ref.WeakReference;

public class AppItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter> {
    private ImageCropModel mImageModel = new ImageCropModel();
    private ImageLoader mLoader = new ImageLoader();

    private static class ImageLoadCallback implements IImageLoadCallback {
        WeakReference<AppItemView> mOuter;

        public ImageLoadCallback(AppItemView outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(Bitmap bitmap) {
            AppItemView outer = (AppItemView) this.mOuter.get();
            if (outer != null) {
                CuteImage coreImageView = outer.getCoreImageView();
                if (coreImageView != null) {
                    coreImageView.setBitmap(bitmap);
                }
            }
        }

        public void onFailed(String url) {
            AppItemView outer = (AppItemView) this.mOuter.get();
            if (outer != null) {
                outer.recycleAndShowDefaultImage();
            }
        }
    }

    public AppItemView(Context context) {
        super(context);
    }

    public void onBind(Presenter object) {
        setStyleByName(object.getModel().getStyle());
        setTag(C1632R.id.focus_res_ends_with, object.getModel().getSkinEndsWith());
        ItemInfoModel itemInfoModel = object.getModel();
        setDefaultImage();
        updateUI(itemInfoModel);
        if (getAppType(itemInfoModel) == 1 && getCoreImageView() != null) {
            getCoreImageView().setDrawable(object.getIconDrawable());
        }
        setContentDescription(itemInfoModel.getCuteViewData("ID_TITLE", "text"));
    }

    private boolean isNeedLoadImage(int appType) {
        return appType == 2;
    }

    private int getAppType(ItemInfoModel itemInfoModel) {
        return ((ApplicationActionModel) itemInfoModel.getActionModel()).getData().getApplicationType();
    }

    private void recycleAndShowDefaultImage() {
        if (!(this.mLoader == null || this.mLoader.isRecycled())) {
            this.mLoader.recycle();
        }
        setDefaultImage();
    }

    private void setDefaultImage() {
        CuteImage coreImageView = getCoreImageView();
        if (coreImageView != null) {
            coreImageView.setDrawable(coreImageView.getDefaultDrawable());
        }
    }

    public void onUnbind(Presenter object) {
        recycleAndShowDefaultImage();
        recycle();
    }

    public void onShow(Presenter object) {
        ItemInfoModel mItemInfoModel = object.getModel();
        updateUI(mItemInfoModel);
        int appType = getAppType(mItemInfoModel);
        if (isNeedLoadImage(appType)) {
            loadImage(mItemInfoModel);
        } else if (appType == 1 && getCoreImageView() != null) {
            getCoreImageView().setDrawable(object.getIconDrawable());
        }
    }

    public void onHide(Presenter object) {
        recycleAndShowDefaultImage();
    }

    private void loadImage(ItemInfoModel itemInfoModel) {
        String mIconUrl = itemInfoModel.getCuteViewData("ID_IMAGE", "value");
        this.mLoader.setImageLoadCallback(new ImageLoadCallback(this));
        this.mLoader.loadImage(mIconUrl, getImageCropModel());
    }

    private ImageCropModel getImageCropModel() {
        CuteImage coreImageView = getCoreImageView();
        if (coreImageView == null) {
            return null;
        }
        this.mImageModel.width = coreImageView.getWidth();
        this.mImageModel.height = coreImageView.getHeight();
        this.mImageModel.cropType = ScaleType.NO_CROP;
        this.mImageModel.radius = 0;
        return this.mImageModel;
    }

    public CuteImage getCoreImageView() {
        return getCuteImage("ID_IMAGE");
    }
}
