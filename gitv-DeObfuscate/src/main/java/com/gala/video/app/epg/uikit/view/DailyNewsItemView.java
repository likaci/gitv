package com.gala.video.app.epg.uikit.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.cloudui.utils.CloudUtilsGala;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.HomeDebug;
import com.gala.video.app.epg.home.data.HomeDataCenter;
import com.gala.video.app.epg.home.data.bus.IHomeDataObserver;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.provider.DailyNewsProvider;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.action.model.DailyActionModel;
import com.gala.video.lib.share.uikit.contract.DailyNewsItemContract.Presenter;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.utils.ImageLoader;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IGifLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.ImageCropModel;
import com.gala.video.lib.share.uikit.view.IViewLifecycle;
import com.gala.video.lib.share.uikit.view.widget.UIKitCloudItemView;
import com.gala.video.lib.share.utils.Precondition;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import pl.droidsonroids.gif.GifDrawable;

public class DailyNewsItemView extends UIKitCloudItemView implements IViewLifecycle<Presenter> {
    private static final int MESSAGE_NUM = 6;
    private static final int MSG_START = 100000;
    private static final long SWITCH_DELAY = 180000;
    private String TAG = "DailyNewsItemView";
    private boolean isFirstEntry = true;
    private Context mContext;
    private String mCurrentImageUrl = null;
    private List<DailyLabelModel> mDailyLabelModelList;
    private List<Album> mDailyNewsData = new CopyOnWriteArrayList();
    private int mDailyPos = -1;
    private Handler mDaily_Handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            LogUtils.m1568d(DailyNewsItemView.this.TAG, "mDaily_Handler---handleMessage");
            switch (msg.what) {
                case DailyNewsItemView.MSG_START /*100000*/:
                    if (ListUtils.isEmpty(DailyNewsItemView.this.mDailyNewsData)) {
                        LogUtils.m1577w(DailyNewsItemView.this.TAG, "mDaily_Handler---ListUtils.isEmpty(mDailyNewsData)");
                        return;
                    }
                    DailyNewsItemView.this.mDailyPos = DailyNewsItemView.this.mDailyPos + 1;
                    DailyNewsItemView.this.update();
                    return;
                default:
                    return;
            }
        }
    };
    private GifDrawable mGifDrawable = null;
    private IGifLoadCallback mGifLoadCallback = new C12324();
    private IImageProvider mIconLoader;
    private IImageLoadCallback mImageCallback = new C12313();
    private ImageCropModel mImageModel = new ImageCropModel();
    private ImageLoader mImageViewLoader;
    private ItemInfoModel mItemModel;
    private IImageCallback mLoadFocusIconCallback = new C12346();
    private IImageCallback mLoadNormalIconCallback = new C12335();
    private OnClickListener mOnClickListener = new C12302();
    private boolean mShouldSpliceUrl = false;
    IHomeDataObserver observer = new C12357();

    class C12291 implements Runnable {
        C12291() {
        }

        public void run() {
            DailyNewsItemView.this.registerDataUpdateObserver();
        }
    }

    class C12302 implements OnClickListener {
        C12302() {
        }

        public void onClick(View v) {
            if (DailyNewsItemView.this.mItemModel != null) {
                BaseActionModel model = DailyNewsItemView.this.mItemModel.getActionModel();
                if (model instanceof DailyActionModel) {
                    ((DailyActionModel) DailyNewsItemView.this.mItemModel.getActionModel()).setDailyLabelModelList(DailyNewsItemView.this.mDailyLabelModelList);
                    ((DailyActionModel) DailyNewsItemView.this.mItemModel.getActionModel()).setNewParamsPos(DailyNewsItemView.this.mDailyPos);
                    model.onItemClick(DailyNewsItemView.this.mContext);
                }
            }
        }
    }

    class C12313 implements IImageLoadCallback {
        C12313() {
        }

        public void onSuccess(Bitmap bitmap) {
            LogUtils.m1568d(DailyNewsItemView.this.TAG, "mImageCallback > onSuccess");
            if (DailyNewsItemView.this.mGifDrawable != null) {
                DailyNewsItemView.this.mGifDrawable.recycle();
                DailyNewsItemView.this.mGifDrawable = null;
            }
            DailyNewsItemView.this.onLoadImageSuccess(new BitmapDrawable(ResourceUtil.getResource(), bitmap));
        }

        public void onFailed(String url) {
            LogUtils.m1568d(DailyNewsItemView.this.TAG, "mImageCallback > onFailed,url=" + url);
            if (DailyNewsItemView.this.mGifDrawable != null) {
                DailyNewsItemView.this.mGifDrawable.recycle();
                DailyNewsItemView.this.mGifDrawable = null;
            }
            DailyNewsItemView.this.onLoadImageFailed(null);
        }
    }

    class C12324 implements IGifLoadCallback {
        C12324() {
        }

        public void onSuccess(GifDrawable drawable) {
            if (drawable == null) {
                LogUtils.m1571e(DailyNewsItemView.this.TAG, "IGifLoadCallback onSuccess drawable = null");
                DailyNewsItemView.this.loadStaticImage();
                return;
            }
            if (DailyNewsItemView.this.mGifDrawable != null) {
                DailyNewsItemView.this.mGifDrawable.recycle();
            }
            DailyNewsItemView.this.mGifDrawable = drawable;
            DailyNewsItemView.this.onLoadImageSuccess(drawable);
            if (!DailyNewsItemView.this.mGifDrawable.isRunning()) {
                DailyNewsItemView.this.mGifDrawable.start();
            }
        }
    }

    class C12335 implements IImageCallback {
        C12335() {
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            LogUtils.m1574i(DailyNewsItemView.this.TAG, "mLoadNormalIconCallback --- onSuccess");
            CuteImage cornerLBView = DailyNewsItemView.this.getCornerLBView();
            if (cornerLBView != null) {
                cornerLBView.setDrawable(new BitmapDrawable(ResourceUtil.getResource(), bitmap));
            }
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
        }
    }

    class C12346 implements IImageCallback {
        C12346() {
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            LogUtils.m1574i(DailyNewsItemView.this.TAG, "mLoadFocusIconCallback --- onSuccess");
            CuteImage cornerLBView = DailyNewsItemView.this.getCornerLBView();
            if (cornerLBView != null) {
                cornerLBView.setFocusDrawable(new BitmapDrawable(ResourceUtil.getResource(), bitmap));
            }
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
        }
    }

    class C12357 implements IHomeDataObserver {
        C12357() {
        }

        public void update(WidgetChangeStatus status, HomeModel data) {
            int maxMessageNum = 6;
            LogUtils.m1568d(DailyNewsItemView.this.TAG, "IHomeDataObserver--update.............. ");
            DailyNewsItemView.this.mDailyLabelModelList = DailyNewsProvider.getInstance().getDailyNewModelList();
            if (!Precondition.isEmpty(DailyNewsItemView.this.mDailyLabelModelList)) {
                DailyLabelModel firstLabelModel = (DailyLabelModel) DailyNewsItemView.this.mDailyLabelModelList.get(0);
                if (firstLabelModel != null) {
                    DailyNewsItemView.this.clearData();
                    List newModels = firstLabelModel.mDailyNewModelList;
                    if (!ListUtils.isEmpty(newModels)) {
                        if (newModels.size() <= 6) {
                            maxMessageNum = newModels.size();
                        }
                        for (int i = 0; i < maxMessageNum; i++) {
                            DailyNewsItemView.this.mDailyNewsData.add(newModels.get(i));
                        }
                    }
                }
            }
            LogUtils.m1568d(DailyNewsItemView.this.TAG, "mDailyNewsData.size() = " + DailyNewsItemView.this.mDailyNewsData.size());
            DailyNewsItemView.this.startDailySwitch();
        }
    }

    public DailyNewsItemView(Context context) {
        super(context);
        this.mContext = context;
        initImageLoader();
        setOnClickListener(this.mOnClickListener);
    }

    public void onBind(Presenter object) {
        loadUIStyle(object);
        this.mItemModel = object.getModel();
        updateUI(this.mItemModel);
        setDefaultValue();
        getDailyNewsData();
        fetchIcons();
    }

    public void onUnbind(Presenter object) {
        unregisterObserver();
        recycleAndStopSwitch();
        recycle();
    }

    public void onShow(Presenter object) {
        if (this.isFirstEntry) {
            this.isFirstEntry = false;
            startDailySwitch();
        }
    }

    public void onHide(Presenter object) {
        recycleAndStopSwitch();
    }

    private void setDefaultValue() {
        if (getCoreImageView() != null) {
            getCoreImageView().setDrawable(getCoreImageView().getDefaultDrawable());
        }
        CuteImage cornerLBView = getCornerLBView();
        if (cornerLBView != null) {
            Drawable defaultCoreImageViewDrawable = cornerLBView.getDefaultDrawable();
            Drawable defaultCoreImageViewFocusDrawable = CloudUtilsGala.getCurStateDrawable(defaultCoreImageViewDrawable, new int[]{16842908});
            cornerLBView.setDrawable(CloudUtilsGala.getCurStateDrawable(defaultCoreImageViewDrawable, new int[]{16842910}));
            cornerLBView.setFocusDrawable(defaultCoreImageViewFocusDrawable);
        }
        CuteText titleView = getTitleView();
        if (titleView != null) {
            titleView.setText(titleView.getDefaultText());
        }
        this.isFirstEntry = true;
    }

    private CuteImage getCoreImageView() {
        return getCuteImage("ID_IMAGE");
    }

    private CuteImage getCornerLBView() {
        return getCuteImage("ID_CORNER_L_B");
    }

    private CuteText getTitleView() {
        return getCuteText("ID_TITLE");
    }

    private void getDailyNewsData() {
        ThreadUtils.execute(new C12291());
    }

    protected void loadUIStyle(Presenter object) {
        setStyleByName(StringUtils.append("dailynews", object.getModel().getSkinEndsWith()));
        setTag(C0508R.id.focus_res_ends_with, object.getModel().getSkinEndsWith());
    }

    private void fetchIcons() {
        String iconUrls = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getDailyInfoCornerMark();
        String[] ulrs = null;
        if (!(iconUrls == null || iconUrls.isEmpty())) {
            ulrs = iconUrls.split(",");
        }
        if (ulrs != null && ulrs.length >= 2) {
            String normalIconUrl = ulrs[0];
            LogUtils.m1576i(this.TAG, "normalIconUrl = ", normalIconUrl, " focusIconUrl = ", ulrs[1]);
            if (!TextUtils.isEmpty(normalIconUrl) && !TextUtils.isEmpty(normalIconUrl)) {
                ImageRequest normalIconRequest = new ImageRequest(normalIconUrl);
                normalIconRequest.setTargetWidth(getCornerLBView().getWidth());
                normalIconRequest.setTargetHeight(getCornerLBView().getHeight());
                normalIconRequest.setScaleType(ScaleType.NO_CROP);
                this.mIconLoader.loadImage(normalIconRequest, this.mLoadNormalIconCallback);
                ImageRequest focusIconRequest = new ImageRequest(focusIconUrl);
                focusIconRequest.setTargetWidth(getCornerLBView().getWidth());
                focusIconRequest.setTargetHeight(getCornerLBView().getHeight());
                focusIconRequest.setScaleType(ScaleType.NO_CROP);
                this.mIconLoader.loadImage(focusIconRequest, this.mLoadFocusIconCallback);
            }
        }
    }

    void initImageLoader() {
        this.mImageViewLoader = new ImageLoader();
        this.mImageViewLoader.setImageLoadCallback(this.mImageCallback);
        this.mIconLoader = ImageProviderApi.getImageProvider();
    }

    private void setDefaultImage() {
        if (this.mGifDrawable != null) {
            this.mGifDrawable.recycle();
            this.mGifDrawable = null;
        }
        CuteImage coreImageView = getCoreImageView();
        if (coreImageView != null) {
            coreImageView.setDrawable(coreImageView.getDefaultDrawable());
        }
    }

    private void loadStaticImage() {
        String url = this.mCurrentImageUrl;
        LogUtils.m1570d(this.TAG, "loadImage url = ", url, " shouldSpliceUrl = ", Boolean.valueOf(this.mShouldSpliceUrl));
        if (this.mShouldSpliceUrl) {
            url = PicSizeUtils.getUrlWithSize(PhotoSize._480_270, url);
            LogUtils.m1570d(this.TAG, "loadImage---PhotoSize._480_270", "newUrl = ", url);
        }
        this.mImageViewLoader.loadImage(url, getImageCropModel());
    }

    private void onLoadImageSuccess(Drawable drawable) {
        CuteImage coreImageView = getCoreImageView();
        LogUtils.m1573e(this.TAG, "onLoadImageSuccess --- getCoreImageView() = ", coreImageView, "drawable = ", drawable);
        if (coreImageView != null) {
            coreImageView.setDrawable(drawable);
        } else {
            LogUtils.m1577w(this.TAG, "onLoadImageSuccess---  getCoreImageView() == null");
        }
    }

    private void onLoadImageFailed(String url) {
        LogUtils.m1573e(this.TAG, "onLoadImageFailed --- getCoreImageView() = ", getCoreImageView(), "url=", url);
        setDefaultImage();
    }

    private ImageCropModel getImageCropModel() {
        this.mImageModel.width = this.mItemModel.getWidth();
        this.mImageModel.height = this.mItemModel.getHeight();
        this.mImageModel.cropType = ScaleType.NO_CROP;
        this.mImageModel.radius = 0;
        return this.mImageModel;
    }

    private void registerDataUpdateObserver() {
        HomeDataCenter.registerObserver(HomeDataType.DAILY_INFO, this.observer);
    }

    private void unregisterObserver() {
        HomeDataCenter.unregisterObserver(HomeDataType.DAILY_INFO, this.observer);
    }

    private synchronized void clearData() {
        this.mDailyNewsData.clear();
    }

    private synchronized Album getDailyNewAlbum(int position) {
        Album album;
        album = null;
        if (ListUtils.isLegal(this.mDailyNewsData, position)) {
            album = (Album) this.mDailyNewsData.get(position);
        } else {
            LogUtils.m1573e(this.TAG, "mDailyNewsData.size() = ", Integer.valueOf(this.mDailyNewsData.size()), "position = ", Integer.valueOf(position));
        }
        return album;
    }

    private void update() {
        if (!ListUtils.isLegal(this.mDailyNewsData, this.mDailyPos)) {
            this.mDailyPos = 0;
        }
        tempForclick();
        Album currentAlbum = getDailyNewAlbum(this.mDailyPos);
        if (currentAlbum != null) {
            String name = currentAlbum.tvName;
            String gifUrl = currentAlbum.imageGif;
            this.mCurrentImageUrl = TextUtils.isEmpty(currentAlbum.tvPic) ? currentAlbum.pic : currentAlbum.tvPic;
            this.mShouldSpliceUrl = TextUtils.isEmpty(currentAlbum.tvPic);
            LogUtils.m1570d(this.TAG, "dayliy news title is ", name, " gif url = ", gifUrl, " image url = ", this.mCurrentImageUrl);
            CuteText titleView = getTitleView();
            if (titleView != null) {
                titleView.setText(name);
                setContentDescription(name);
            } else {
                LogUtils.m1577w(this.TAG, "update()--mTitleView==null.");
            }
            if (TextUtils.isEmpty(gifUrl)) {
                loadStaticImage();
            } else {
                this.mImageViewLoader.loadGif(gifUrl, this.mGifLoadCallback);
            }
        }
        if (this.mDailyPos != -1) {
            nextDailyNews(180000);
        }
    }

    public void nextDailyNews(long delay) {
        if (!ListUtils.isEmpty(this.mDailyNewsData)) {
            this.mDaily_Handler.removeMessages(MSG_START);
            this.mDaily_Handler.sendEmptyMessageDelayed(MSG_START, delay);
        }
    }

    private void tempForclick() {
        ((DailyActionModel) this.mItemModel.getActionModel()).setDailyLabelModelList(this.mDailyLabelModelList);
        ((DailyActionModel) this.mItemModel.getActionModel()).setNewParamsPos(this.mDailyPos);
    }

    public void startDailySwitch() {
        LogUtils.m1568d(this.TAG, "start daily switch current position: " + this.mDailyPos);
        this.mDailyPos = -1;
        Message msg = this.mDaily_Handler.obtainMessage();
        msg.what = MSG_START;
        this.mDaily_Handler.removeMessages(MSG_START);
        this.mDaily_Handler.sendMessage(msg);
    }

    public void stopDailySwitch() {
        LogUtils.m1568d(this.TAG, "stop daily switch");
        this.mDaily_Handler.removeMessages(MSG_START);
    }

    private void recycleAndStopSwitch() {
        stopDailySwitch();
        if (!this.mImageViewLoader.isRecycled()) {
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.m1568d(this.TAG, "recycleImage");
            }
            this.mImageViewLoader.recycle();
            setDefaultImage();
        }
        this.isFirstEntry = true;
    }
}
