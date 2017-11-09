package com.gala.video.app.epg.widget.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.utils.EpgImageCache;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.ViewConstant;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ImageCacheUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class GlobalVipCloudView extends CloudView implements OnFocusChangeListener, OnClickListener {
    private static final int DEFAULT_DURATION = 200;
    private static final float DEFAULT_SCALE = 1.1f;
    private String TAG = "GlobalVipCloudView";
    private BitmapAlbum mBitmapAlbum;
    private Context mContext;
    private CuteImageView mCuteBottombgView;
    private CuteImageView mCuteImageView;
    private CuteTextView mCuteTitleView;
    private GlobalVipCloudViewCallBack mGlobalVipCloudViewCallBack;
    private boolean mIsVipStatus = false;
    private String mPosition = "";

    public interface GlobalVipCloudViewCallBack {
        void clickLitener();
    }

    public GlobalVipCloudView(Context context) {
        super(context);
        init(context);
    }

    public GlobalVipCloudView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GlobalVipCloudView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        setStyle("home/loginoutitem.json");
        this.mCuteImageView = getImageView("ID_IMAGE");
        this.mCuteTitleView = getTextView("ID_TITLE");
        this.mCuteBottombgView = getImageView(ViewConstant.ID_BOTTOM_BG);
        setDefaultImage();
        setOnFocusChangeListener(this);
        setOnClickListener(this);
    }

    private void setDefaultImage() {
        if (this.mCuteImageView != null) {
            this.mCuteImageView.setDrawable(ImageCacheUtil.DEFAULT_DRAWABLE);
        }
    }

    public void setPosition(int position) {
        this.mPosition = position + "";
    }

    public void setGlobalVipCloudViewCallBack(GlobalVipCloudViewCallBack globalVipCloudViewCallBack) {
        this.mGlobalVipCloudViewCallBack = globalVipCloudViewCallBack;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (v == null) {
            Log.v(this.TAG, "GlobalVipCloudView---view== null.return.");
            return;
        }
        float f;
        if (!hasFocus) {
            this.mCuteBottombgView.setDrawable(EpgImageCache.COVER_COLOR_UNFOCUS_DRAWABLE_FOR_RECOMMENDVIEW);
        } else if (this.mIsVipStatus) {
            this.mCuteBottombgView.setDrawable(ResourceUtil.getDrawable(R.drawable.epg_global_dialog_gradient_vip_bg));
        } else {
            this.mCuteBottombgView.setDrawable(ImageCacheUtil.TITLE_FOCUS_DRAWABLE);
        }
        if (hasFocus) {
            f = 1.1f;
        } else {
            f = 1.0f;
        }
        AnimationUtil.zoomAnimation(v, f, 200);
    }

    public void setData(BitmapAlbum bitmapAlbum, Resources resources, Drawable defaultDrawable) {
        if (bitmapAlbum != null) {
            this.mBitmapAlbum = bitmapAlbum;
            if (this.mCuteImageView != null) {
                CuteImageView cuteImageView = this.mCuteImageView;
                if (bitmapAlbum.getBitmap() != null) {
                    defaultDrawable = new BitmapDrawable(resources, bitmapAlbum.getBitmap());
                }
                cuteImageView.setDrawable(defaultDrawable);
            } else {
                Log.v(this.TAG, "mCuteImageView is null");
            }
            if (this.mCuteTitleView == null) {
                Log.v(this.TAG, "mCuteTitleView is null");
            } else if (bitmapAlbum.getAlbum() != null) {
                this.mCuteTitleView.setText(bitmapAlbum.getAlbum().name);
            } else {
                Log.v(this.TAG, "bitmapAlbum.getAlbum() is null");
            }
            if (this.mCuteBottombgView != null) {
                this.mCuteBottombgView.setDrawable(EpgImageCache.COVER_COLOR_UNFOCUS_DRAWABLE_FOR_RECOMMENDVIEW);
                return;
            } else {
                Log.v(this.TAG, "mCuteBottombgView is null");
                return;
            }
        }
        setFocusable(false);
        setEnabled(false);
        Log.v(this.TAG, "bitmapAlbum is null");
    }

    public void setVipStyle(boolean isVipStyle) {
        this.mIsVipStatus = isVipStyle;
        if (this.mIsVipStatus) {
            setBackgroundDrawable(ImageCacheUtil.RECT_BG_VIP_DRAWABLE);
        } else {
            setBackgroundDrawable(ImageCacheUtil.RECT_BG_DRAWABLE);
        }
    }

    public void onClick(View v) {
        if (this.mBitmapAlbum != null) {
            Album album = this.mBitmapAlbum.getAlbum();
            if (album != null) {
                sendClickPingBack(this.mPosition, String.valueOf(album.chnId), album.tvQid);
                PingBackUtils.setTabSrc("其他");
                ItemUtils.openDetailOrPlay(this.mContext, this.mBitmapAlbum.getAlbum(), "logout", null, null);
            } else {
                Log.v(this.TAG, "onClick album is null");
            }
            LogUtils.e(this.TAG, "onClick mGlobalVipCloudViewCallBack: " + this.mGlobalVipCloudViewCallBack);
            if (this.mGlobalVipCloudViewCallBack != null) {
                this.mGlobalVipCloudViewCallBack.clickLitener();
                return;
            }
            return;
        }
        Log.v(this.TAG, "onClick BitmapAlbum is null");
    }

    private void sendClickPingBack(String rseat, String c1, String r) {
        Log.v(this.TAG, "sendClickPingBack rseat = " + rseat + " ,c1 = " + c1 + " ,r = " + r);
        PingBackParams params = new PingBackParams();
        params.add("rpage", "logout").add("block", "rec").add("rseat", rseat).add("c1", c1).add("r", r).add("rt", "i").add(Keys.T, "20");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
