package com.gala.video.app.epg.home.component.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.video.app.epg.HomeDebug;
import com.gala.video.app.epg.home.component.Item;
import com.gala.video.app.epg.home.component.item.widget.ComplexItemCloudView;
import com.gala.video.app.epg.home.component.item.widget.ItemCloudViewType;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.home.data.pingback.HomePingbackDataModel.Builder;
import com.gala.video.app.epg.home.utils.HomeDebugUtils;
import com.gala.video.app.epg.home.utils.HomeItemUtils;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IGifLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.ImageCropModel;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import pl.droidsonroids.gif.GifDrawable;

public abstract class AndroidItem extends Item {
    protected boolean gifAvailable;
    private boolean gifStart;
    private ImageCallback mCallback;
    protected Context mContext;
    protected CuteImageView mCoreImageView;
    private GifDrawable mGifDrawable;
    private ImageCropModel mImageModel;
    protected String mImageUrl;
    private final boolean mIsIcon;
    protected ItemData mItemData;
    private ImageLoader mLoader;
    protected ComplexItemCloudView mView;
    private ItemCloudViewType mViewType;

    private class ImageCallback implements IImageLoadCallback, IGifLoadCallback {
        private ImageCallback() {
        }

        public void onSuccess(GifDrawable drawable) {
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.d(AndroidItem.this.TAG, "loadgif onSuccess ,gifdrawable = " + drawable);
            }
            if (drawable == null) {
                AndroidItem.this.gifAvailable = false;
                AndroidItem.this.loadImage();
                return;
            }
            if (AndroidItem.this.mGifDrawable != drawable) {
                AndroidItem.this.mGifDrawable = drawable;
                drawable.stop();
            }
            AndroidItem.this.onLoadImageSuccess((Drawable) drawable);
            if (AndroidItem.this.gifStart && !AndroidItem.this.mGifDrawable.isRunning()) {
                AndroidItem.this.mGifDrawable.start();
            }
        }

        public void onSuccess(Bitmap bitmap) {
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.d(AndroidItem.this.TAG, "load image onSuccess bitmap = " + bitmap);
            }
            AndroidItem.this.onLoadImageSuccess(bitmap);
        }

        public void onFailed(String url) {
            Log.e(AndroidItem.this.TAG, "download image onfailure, url=" + AndroidItem.this.mImageUrl + ", title = " + AndroidItem.this.mItemData.getTitle() + ", itemData = " + AndroidItem.this.mItemData);
            AndroidItem.this.onLoadImageFailed(null);
        }
    }

    abstract String getLogTag();

    protected abstract void initOnFocusChangeListener();

    abstract void initViewComponent();

    abstract void setData();

    public AndroidItem(int itemtype, ItemCloudViewType viewType) {
        boolean z = true;
        super(itemtype);
        this.mImageUrl = "";
        this.gifAvailable = true;
        this.mCallback = new ImageCallback();
        this.TAG = "home/item/" + getLogTag() + "@" + Integer.toHexString(hashCode());
        if (itemtype != 259) {
            z = false;
        }
        this.mIsIcon = z;
        this.mViewType = viewType;
        this.mLoader = new ImageLoader();
        this.mLoader.setImageLoadCallback(this.mCallback);
    }

    public View onCreateViewHolder(Context context) {
        this.mContext = context;
        this.mView = new ComplexItemCloudView(context, this.mViewType);
        initViewComponent();
        setDefaultImage();
        initOnFocusChangeListener();
        return this.mView;
    }

    public void onBindViewHolder(DataSource itemData) {
        setDataSource(itemData);
        this.mItemData = (ItemData) itemData;
        this.mImageUrl = this.mIsIcon ? this.mItemData.getIconUrl() : this.mItemData.getImage();
        setData();
    }

    public Object buildUI(Context context) {
        super.buildUI(context);
        if (context == null) {
            Log.e(this.TAG, this.TAG + "return buildUI, context == null");
            return this.mView;
        }
        this.mContext = context;
        this.mItemData = getDataSource();
        if (this.mItemData != null) {
            this.mImageUrl = this.mIsIcon ? this.mItemData.getIconUrl() : this.mItemData.getImage();
            this.mView = new ComplexItemCloudView(context, this.mViewType);
            initViewComponent();
            this.mLoader.recycle();
            setDefaultImage();
            setData();
            initOnClickListener();
            initOnFocusChangeListener();
            return this.mView;
        }
        Log.e(this.TAG, this.TAG + "return buildUI, itemData=null");
        return this.mView;
    }

    protected void initOnClickListener() {
        this.mView.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                HomeDebugUtils.printItemData(AndroidItem.this.mItemData, getClass().getSimpleName());
                HomeItemUtils.onItemClick(AndroidItem.this.mContext, AndroidItem.this.getPingbackDataSource(), AndroidItem.this.mParent.getPingbackDataSource(), AndroidItem.this.mParent.getParent().getPingbackDataSource(), new Builder().cardLine(String.valueOf(AndroidItem.this.getCardLine())).build());
            }
        });
    }

    public Object updateUI() {
        super.updateUI();
        this.gifAvailable = true;
        if (this.mContext == null) {
            Log.e(this.TAG, this.TAG + "return updateUI, mContext == null");
            return this.mView;
        }
        this.mItemData = getDataSource();
        if (this.mItemData != null) {
            this.mImageUrl = this.mIsIcon ? this.mItemData.getIconUrl() : this.mItemData.getImage();
            if (this.mGifDrawable != null) {
                this.mLoader.recycle();
                this.mGifDrawable.recycle();
                this.mGifDrawable = null;
            }
            if (this.mView != null || this.mItemData == null) {
                setData();
            }
            if (HomeDebug.DEBUG_LOG) {
                Log.d(this.TAG, this.TAG + "updateUI," + this.mItemData.getTitle() + ",mImageUrl=" + this.mImageUrl);
            }
            updateImage();
            return this.mView;
        }
        Log.e(this.TAG, this.TAG + "return updateUI, itemData=null");
        return this.mView;
    }

    protected void updateImage() {
        if (!isVisibleToUser() || TextUtils.isEmpty(this.mImageUrl)) {
            this.mLoader.recycle();
            setDefaultImage();
            return;
        }
        if (HomeDebug.DEBUG_LOG) {
            Log.e(this.TAG, this.TAG + "updateUI, 图片改变，下载新图---" + this.mItemData.getTitle() + ",url=" + this.mImageUrl);
        }
        if (this.mLoader.isRecycled()) {
            setDefaultImage();
        }
        loadImage();
    }

    public void onEvent(int event, Object result) {
        super.onEvent(event, result);
        postEvent(event, result);
    }

    private void postEvent(int event, Object result) {
        switch (event) {
            case 3:
                boolean isPageInFront = ((Boolean) result).booleanValue();
                if (!isVisibleToUser()) {
                    return;
                }
                if (isPageInFront) {
                    if (HomeDebug.DEBUG_LOG) {
                        LogUtils.d(this.TAG, "page in front,load image : " + this.mImageUrl);
                    }
                    this.gifStart = true;
                    loadImage();
                    return;
                }
                recycleAndShowDefaultImage();
                return;
            case 261:
                if (!((Boolean) result).booleanValue()) {
                    if (HomeDebug.DEBUG_LOG) {
                        LogUtils.d(this.TAG, "page is inVisible");
                    }
                    recycleAndShowDefaultImage();
                    return;
                } else if (isVisibleToUser()) {
                    this.gifStart = true;
                    loadImage();
                    if (HomeDebug.DEBUG_LOG) {
                        LogUtils.d(this.TAG, "page is visible to user");
                        return;
                    }
                    return;
                } else if (HomeDebug.DEBUG_LOG) {
                    LogUtils.e(this.TAG, "page is visible but is not visible to user");
                    return;
                } else {
                    return;
                }
            case 517:
                boolean cardVisible = ((Boolean) result).booleanValue();
                if (HomeDebug.DEBUG_LOG) {
                    LogUtils.d(this.TAG, "card visibility changed = " + cardVisible + " isVisible = " + isVisibleToUser());
                }
                if (cardVisible && isVisibleToUser()) {
                    this.gifStart = true;
                    loadImage();
                    return;
                } else if (!cardVisible) {
                    recycleAndShowDefaultImage();
                    return;
                } else {
                    return;
                }
            case 773:
                boolean isVisible = ((Boolean) result).booleanValue();
                if (isVisibleToUser()) {
                    this.gifStart = true;
                    loadImage();
                } else {
                    recycleAndShowDefaultImage();
                }
                if (HomeDebug.DEBUG_LOG) {
                    LogUtils.d(this.TAG, "item visibility changed = " + isVisible);
                    return;
                }
                return;
            case 774:
                loadImage();
                return;
            default:
                return;
        }
    }

    public void recycleAndShowDefaultImage() {
        if (!this.mLoader.isRecycled()) {
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.d(this.TAG, "recycleImage");
            }
            this.mLoader.recycle();
            if (this.mGifDrawable != null) {
                this.gifStart = false;
                this.mGifDrawable.stop();
            }
            setDefaultImage();
            this.mGifDrawable = null;
        }
    }

    protected void setDefaultImage() {
        if (this.mCoreImageView != null) {
            this.mCoreImageView.setDrawable(ImageCacheUtil.DEFAULT_DRAWABLE);
        }
    }

    public void loadImage() {
        String url = this.mImageUrl;
        checkGifAvailable();
        if (HomeDebug.DEBUG_LOG && this.mItemData != null) {
            LogUtils.d(this.TAG, "load image url : = " + url + ", gif = " + this.mItemData.mGif + ",name = " + this.mItemData.getTitle());
            LogUtils.d(this.TAG, "load image  gifAvailable = " + this.gifAvailable + " isUseGif() = " + isUseGif());
        }
        if (this.gifAvailable && isUseGif()) {
            if (this.mGifDrawable != null) {
                this.mCallback.onSuccess(this.mGifDrawable);
            } else {
                this.mLoader.loadGif(this.mItemData.mGif, this.mCallback);
            }
        } else if (this.mImageModel != null) {
            this.mLoader.loadImage(url, this.mImageModel);
        } else {
            this.mLoader.loadImage(url, createImageModel());
        }
    }

    private ImageCropModel createImageModel() {
        if (this.mItemData == null) {
            return null;
        }
        int actualItemWidth = getActualItemWidth();
        int actualItemHeight = getActualItemHeight();
        int actualImageWidth = this.mItemData.getWidth();
        int actualImageHeight = this.mItemData.getHeight();
        int factor = 1;
        if (actualItemWidth > 0 && actualImageWidth > 0) {
            while (actualImageWidth / factor >= (actualItemWidth << 1)) {
                factor <<= 1;
            }
        }
        if (factor == 1) {
            return null;
        }
        LogUtils.d(this.TAG, "image loader resize factor = " + factor + " ,raw size(" + actualImageWidth + " , " + actualImageHeight + "), item size(" + actualItemWidth + " , " + actualItemHeight + ")");
        ImageCropModel imageModel = new ImageCropModel();
        imageModel.width = actualImageWidth / factor;
        imageModel.height = actualImageHeight / factor;
        return imageModel;
    }

    protected void onLoadImageSuccess(Bitmap bitmap) {
        if (this.mCoreImageView != null) {
            this.mCoreImageView.setBitmap(bitmap);
        }
    }

    private void onLoadImageSuccess(Drawable drawable) {
        if (this.mCoreImageView != null) {
            this.mCoreImageView.setDrawable(drawable);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mGifDrawable != null) {
            this.mGifDrawable.recycle();
        }
    }

    protected void onLoadImageFailed(String url) {
        setDefaultImage();
    }
}
