package com.gala.video.app.player.albumdetail.ui.overlay.panels;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackItem;
import com.gala.pingback.PingbackStore;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RPAGETYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RSEATTYPE;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.ScreenMode;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.AlbumInfo.VideoKind;
import com.gala.video.app.player.pingback.detail.DetailPingBackUtils;
import com.gala.video.app.player.ui.config.IAlbumDetailUiConfig;
import com.gala.video.app.player.ui.widget.views.EquityView;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class CtrlButtonPanel implements OnClickListener, OnFocusChangeListener, OnKeyListener {
    private static final String TAG = "AlbumDetail/UI/CtrlButtonPanel";
    private AlbumInfo mAlbumInfo;
    private Button mBtnBuy;
    private Button mBtnDesc;
    private Button mBtnFav;
    private Button mBtnFull;
    private IAlbumDetailUiConfig mConfig;
    private Context mContext;
    private OnCtrlButtonClickedListener mCtrlButtonClickedListener;
    private OnCtrlFocusChangeListener mCtrlFocusChangeListener;
    private ScreenMode mCurScreenMode = ScreenMode.WINDOWED;
    private View mCurrFocus = null;
    private boolean mEnableWindowPlay = true;
    private EquityView mEvEquity;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsClipSet = false;
    private boolean mIsCoupon = false;
    private boolean mIsEquityFocus = false;
    private boolean mIsFavored;
    private boolean mIsUserActed = false;
    private int mOldPlayOrder;
    private View mPanelView;
    private IPingbackContext mPingbackContext;
    private View mRootView;
    private boolean mShowBuy = false;
    private String mVipString;

    public interface OnCtrlFocusChangeListener {
        void onFoucusChanged(DetailButtonKeyFront detailButtonKeyFront, int i);
    }

    public interface OnCtrlButtonClickedListener {
        void onDescButtonClicked();

        void onEquityImageClicked();

        void onFavButtonClicked();

        void onFullButtonClicked();

        void onVIPButtonClicked();
    }

    public enum DetailButtonKeyFront {
        UP,
        LEFT
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (!this.mIsUserActed) {
            this.mIsUserActed = true;
        }
        return false;
    }

    public void updateNextFocusDown(int id) {
        if (id > 0) {
            this.mBtnFav.setNextFocusDownId(id);
            this.mBtnFull.setNextFocusDownId(id);
            this.mBtnBuy.setNextFocusDownId(id);
        }
    }

    public CtrlButtonPanel(Context context, View rootView, IAlbumDetailUiConfig config) {
        this.mRootView = rootView;
        this.mContext = context;
        this.mPingbackContext = (IPingbackContext) this.mContext;
        this.mConfig = config;
        this.mEnableWindowPlay = this.mConfig.isEnableWindowPlay();
        initViews();
    }

    private void initViews() {
        this.mPanelView = this.mRootView.findViewById(R.id.share_detail_ll_btn_panel);
        this.mBtnFav = (Button) this.mRootView.findViewById(R.id.share_detail_btn_album_fav);
        this.mBtnFull = (Button) this.mRootView.findViewById(R.id.share_detail_btn_album_full);
        this.mBtnBuy = (Button) this.mRootView.findViewById(R.id.share_detail_btn_album_vip);
        this.mBtnDesc = (Button) this.mRootView.findViewById(R.id.share_detail_btn_album_desc);
        this.mEvEquity = (EquityView) this.mRootView.findViewById(R.id.share_detail_ev_equity);
        setupFavButton();
        setupFullButton();
        setupBuyButton();
        setupDescButton();
        setupEquityImg();
    }

    private void setupFavButton() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> setupFavButton.");
        }
        this.mBtnFav.setOnFocusChangeListener(this);
        this.mBtnFav.setOnClickListener(this);
        this.mBtnFav.setOnKeyListener(this);
        this.mBtnFav.setNextFocusRightId(this.mBtnFav.getId());
        this.mBtnFav.setNextFocusUpId(this.mBtnDesc.getId());
        updateFavorButton();
    }

    private void setupFullButton() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> setupFullButton.");
        }
        this.mBtnFull.setOnFocusChangeListener(this);
        this.mBtnFull.setNextFocusUpId(this.mBtnDesc.getId());
        this.mBtnFull.setOnKeyListener(this);
        this.mBtnFull.setOnClickListener(this);
        this.mBtnFull.setText(ResourceUtil.getStr(this.mEnableWindowPlay ? R.string.full_screen : R.string.start_play));
        updateFullButton();
    }

    private void setupBuyButton() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> setupBuyButton.");
        }
        this.mBtnBuy.setOnFocusChangeListener(this);
        this.mBtnBuy.setNextFocusUpId(this.mBtnDesc.getId());
        this.mBtnBuy.setOnKeyListener(this);
        this.mBtnBuy.setOnClickListener(this);
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        this.mVipString = model != null ? model.getPurchaseButtonTxt() : "";
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "vip tip string is [" + this.mVipString + AlbumEnterFactory.SIGN_STR);
        }
        this.mBtnBuy.setVisibility(8);
    }

    private void setupDescButton() {
        this.mBtnDesc.setOnKeyListener(this);
        this.mBtnDesc.setOnFocusChangeListener(this);
        this.mBtnDesc.setNextFocusRightId(this.mBtnDesc.getId());
        this.mBtnDesc.setNextFocusUpId(this.mBtnDesc.getId());
        this.mBtnDesc.setOnClickListener(this);
        if (this.mEnableWindowPlay) {
            this.mBtnDesc.setNextFocusLeftId(R.id.share_detail_playwindow);
        } else {
            this.mBtnDesc.setNextFocusLeftId(this.mBtnDesc.getId());
        }
    }

    private void setupEquityImg() {
        this.mEvEquity.setOnKeyListener(this);
        this.mEvEquity.setOnClickListener(this);
        this.mEvEquity.setOnFocusChangeListener(this);
        this.mEvEquity.setNextFocusUpId(this.mBtnDesc.getId());
        this.mEvEquity.setNextFocusRightId(this.mEvEquity.getId());
        if (this.mEnableWindowPlay) {
            this.mEvEquity.setNextFocusLeftId(R.id.share_detail_playwindow);
        } else {
            this.mEvEquity.setNextFocusLeftId(this.mEvEquity.getId());
        }
    }

    public void onClick(View v) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onClick, v.id=" + v.getId());
        }
        int i = v.getId();
        if (i == R.id.share_detail_btn_album_fav) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onClick(fav)");
            }
            notifyFavButtonClicked();
            sendFavButtonClickedPingback();
        } else if (i == R.id.share_detail_btn_album_full) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onClick(full) mShowBuy=" + this.mShowBuy);
            }
            if (this.mShowBuy) {
                CreateInterfaceTools.createWebRoleFactory().showRoleInVip(this.mContext);
            }
            notifyFullButtonClicked();
        } else if (i == R.id.share_detail_btn_album_vip) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onClick(VIP)");
            }
            notifyVIPButtonClicked();
            sendBuyButtonClickedPingback();
        } else if (i == R.id.share_detail_btn_album_desc) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "onClick(desc)");
            }
            notifyDescButtonClicked();
            sendSummaryClickPingback();
        } else if (i == this.mEvEquity.getId()) {
            notifyEquityImageClicked();
            DetailPingBackUtils.sendEquityClickPingBack(this.mAlbumInfo, this.mPingbackContext);
        }
        this.mCurrFocus = v;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        int i = v.getId();
        if (i == R.id.share_detail_btn_album_fav) {
            setFoucsTextColor(this.mBtnFav, hasFocus);
            updateFavorButton();
            updateButtonBg(this.mBtnFav, hasFocus);
        } else if (i == R.id.share_detail_btn_album_full) {
            setFoucsTextColor(this.mBtnFull, hasFocus);
            updateFullButton();
            updateButtonBg(this.mBtnFull, hasFocus);
            if (LogUtils.mIsDebug && !hasFocus) {
                printCurrentFocusInfo();
            }
        } else if (i == R.id.share_detail_btn_album_vip) {
            if (!(this.mBtnBuy == null || this.mBtnBuy.getVisibility() == 8)) {
                setFoucsTextColor(this.mBtnBuy, hasFocus);
                updateBuyButton();
                updateButtonBg(this.mBtnBuy, hasFocus);
            }
        } else if (i == R.id.share_detail_btn_album_desc) {
            setFoucsTextColor(this.mBtnDesc, hasFocus);
        }
        if (i == this.mEvEquity.getId()) {
            this.mIsEquityFocus = hasFocus;
        }
        setZoomAnim(v);
        LogUtils.d(TAG, "onFocusChange(): mIsEquityFocus -> " + this.mIsEquityFocus);
    }

    private void printCurrentFocusInfo() {
        View curFocus = null;
        if (this.mContext instanceof Activity) {
            View root = ((Activity) this.mContext).getWindow().getDecorView();
            if (root instanceof ViewGroup) {
                curFocus = root.findFocus();
            }
        }
        LogRecordUtils.logd(TAG, "printCurrentFocusInfo, full button lose focus, focusInfo=" + (curFocus == null ? "null" : curFocus.toString()));
    }

    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> onKey, v.id=" + v.getId() + "keyCode=" + keyCode + ", event=" + event);
        }
        if (this.mCurScreenMode == ScreenMode.FULLSCREEN && (19 == event.getKeyCode() || 20 == event.getKeyCode() || 21 == event.getKeyCode() || 22 == event.getKeyCode())) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, ">> onKey, now is fullscreen mode, but playPanel not consume key, so we consume this key");
            }
            return true;
        }
        int i = v.getId();
        if (i == R.id.share_detail_btn_album_fav) {
            if (19 == event.getKeyCode() && event.getAction() == 0) {
                notifyFocusChange(DetailButtonKeyFront.UP, R.id.share_detail_btn_album_fav);
            }
        } else if (i == R.id.share_detail_btn_album_full) {
            if (event.getAction() == 0) {
                if (19 == event.getKeyCode()) {
                    notifyFocusChange(DetailButtonKeyFront.UP, R.id.share_detail_btn_album_full);
                } else if (21 == event.getKeyCode()) {
                    notifyFocusChange(DetailButtonKeyFront.LEFT, R.id.share_detail_btn_album_full);
                }
            }
        } else if (i == R.id.share_detail_btn_album_vip) {
            if (19 == event.getKeyCode() && event.getAction() == 0) {
                notifyFocusChange(DetailButtonKeyFront.UP, R.id.share_detail_btn_album_vip);
            }
        } else if (i == R.id.share_detail_btn_album_desc) {
            if (21 == event.getKeyCode()) {
                notifyFocusChange(DetailButtonKeyFront.LEFT, R.id.share_detail_btn_album_desc);
            }
        } else if (i == this.mEvEquity.getId() && 21 == event.getKeyCode()) {
            notifyFocusChange(DetailButtonKeyFront.LEFT, this.mEvEquity.getId());
        }
        return false;
    }

    public void setDescNextDownId(int rid) {
        if (this.mEvEquity.getVisibility() == 0) {
            this.mEvEquity.setNextFocusDownId(rid);
            this.mBtnDesc.setNextFocusDownId(this.mEvEquity.getId());
            return;
        }
        this.mBtnDesc.setNextFocusDownId(rid);
    }

    private synchronized void setFoucsTextColor(Button btn, boolean hasFocus) {
        int btnTextColorNormal = ResourceUtil.getColor(R.color.detail_text_color_default);
        int btnTextColorFocused = ResourceUtil.getColor(R.color.detail_text_color_focused);
        if (!hasFocus) {
            btnTextColorFocused = btnTextColorNormal;
        }
        btn.setTextColor(btnTextColorFocused);
    }

    private void updateButtonBg(Button btn, boolean hasFocus) {
        Drawable drawable;
        if (hasFocus) {
            drawable = ResourceUtil.getDrawable(R.drawable.share_skin_button_bg_focused);
        } else {
            drawable = ResourceUtil.getDrawable(R.drawable.share_skin_button_bg_unfocused);
        }
        AppClientUtils.setBackgroundDrawable(btn, drawable);
    }

    private void updateDescButton(Button btnDesc, boolean hasFocus) {
        Drawable drawable;
        if (hasFocus) {
            drawable = ResourceUtil.getDrawable(R.drawable.player_detail_skin_button_bg_desc_focused);
        } else {
            drawable = ResourceUtil.getDrawable(R.drawable.player_detail_skin_button_bg_desc_unfocused);
        }
        AppClientUtils.setBackgroundDrawable(btnDesc, drawable);
    }

    private void setButtonIcon(Button button, Drawable draw) {
        draw.setBounds(0, ResourceUtil.getDimen(R.dimen.dimen_01dp), ResourceUtil.getDimen(R.dimen.dimen_22dp), ResourceUtil.getDimen(R.dimen.dimen_20dp));
        button.setCompoundDrawables(draw, null, null, null);
        button.setCompoundDrawablePadding(ResourceUtil.getDimen(R.dimen.dimen_6dp));
    }

    private void loadEquityImage(String url) {
        if (StringUtils.isEmpty((CharSequence) url)) {
            LogUtils.d(TAG, "loadEquityImage(): url is null.");
            hideEquityImage();
            updateFocusPath();
            return;
        }
        LogUtils.d(TAG, "loadEquityImage(): url -> " + url);
        ImageProviderApi.getImageProvider().loadImage(new ImageRequest(url, this.mEvEquity), new IImageCallback() {
            public void onSuccess(ImageRequest imageRequest, final Bitmap bmp) {
                if (bmp != null) {
                    CtrlButtonPanel.this.mHandler.post(new Runnable() {
                        public void run() {
                            CtrlButtonPanel.this.mEvEquity.setType(100);
                            CtrlButtonPanel.this.mEvEquity.setBackground(bmp);
                            CtrlButtonPanel.this.showEquityImage();
                            CtrlButtonPanel.this.updateFocusPath();
                        }
                    });
                }
            }

            public void onFailure(ImageRequest arg0, Exception e) {
                LogUtils.d(CtrlButtonPanel.TAG, "showEquityImage(): load image failure.");
                CtrlButtonPanel.this.hideEquityImage();
                CtrlButtonPanel.this.updateFocusPath();
            }
        });
    }

    private void showEquityImage() {
        this.mEvEquity.setVisibility(0);
        LogUtils.d(TAG, "showEquityImage(): mIsEquityFocus -> " + this.mIsEquityFocus);
        if (this.mIsEquityFocus) {
            this.mEvEquity.requestFocus();
        }
        if (this.mCurScreenMode != ScreenMode.WINDOWED) {
        }
    }

    private void hideEquityImage() {
        this.mEvEquity.setVisibility(8);
    }

    private void setZoomAnim(View v) {
        if (this.mConfig.isZoomEnabled() && this.mIsUserActed) {
            setClipChild(v);
            if (v.getId() == this.mEvEquity.getId()) {
                if (v.hasFocus()) {
                    AnimationUtil.zoomInAnimation(v);
                } else {
                    AnimationUtil.zoomOutAnimation(v);
                }
            } else if (v.hasFocus()) {
                AnimationUtil.zoomInAnimation(v, 1.1f);
            } else {
                AnimationUtil.zoomInAnimation(v, 1.0f);
            }
        }
    }

    private void setClipChild(View v) {
        if (!this.mIsClipSet) {
            for (ViewParent parent = v.getParent(); parent instanceof ViewGroup; parent = parent.getParent()) {
                ((ViewGroup) parent).setClipChildren(false);
                ((ViewGroup) parent).setClipToPadding(false);
            }
            this.mIsClipSet = true;
        }
    }

    public void setFavored() {
        this.mIsFavored = this.mAlbumInfo.isFavored();
        updateFavorButton();
    }

    private void updateFavorButton() {
        LogUtils.d(TAG, ">>updateFavorButton()" + this.mIsFavored);
        this.mBtnFav.setText(ResourceUtil.getStr(this.mIsFavored ? R.string.added_to_favorite : R.string.add_to_favorite));
        Drawable icon = ResourceUtil.getDrawable(getAddToFavIconId(this.mIsFavored, this.mBtnFav.hasFocus()));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> updateFavorButton, mBtnFav " + this.mBtnFav.hasFocus());
        }
        setButtonIcon(this.mBtnFav, icon);
    }

    private int getAddToFavIconId(boolean isFavorite, boolean hasFocus) {
        return isFavorite ? hasFocus ? R.drawable.player_detail_button_img_faved_focused : R.drawable.player_detail_button_img_faved_normal : hasFocus ? R.drawable.player_detail_button_img_fav_focused : R.drawable.player_detail_button_img_fav_normal;
    }

    private void updateFullButton() {
        LogUtils.d(TAG, ">>updateFullButton(), mEnableWindowPlay=" + this.mEnableWindowPlay);
        Drawable icon = ResourceUtil.getDrawable(getPlayIconId(this.mEnableWindowPlay, this.mBtnFull.hasFocus()));
        this.mBtnFull.setNextFocusLeftId(this.mEnableWindowPlay ? R.id.share_detail_playwindow : R.id.share_detail_btn_album_full);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> updateFullButton, mBtnFull " + this.mBtnFull.hasFocus());
        }
        setButtonIcon(this.mBtnFull, icon);
    }

    private void updateBuyButton() {
        Drawable icon = ResourceUtil.getDrawable(getBuyIconId(this.mIsCoupon, this.mBtnBuy.hasFocus()));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> updateBuyButton, mBtnBuy " + this.mBtnBuy.hasFocus() + ", mIsCoupon=" + this.mIsCoupon);
        }
        setButtonIcon(this.mBtnBuy, icon);
    }

    private int getBuyIconId(boolean isCoupon, boolean hasFocus) {
        return isCoupon ? hasFocus ? R.drawable.player_detail_button_img_coupon_focused : R.drawable.player_detail_button_img_coupon_normal : hasFocus ? R.drawable.player_detail_button_img_vip_focused : R.drawable.player_detail_button_img_vip_normal;
    }

    private int getPlayIconId(boolean isEnableWindowPlay, boolean hasFocus) {
        return isEnableWindowPlay ? hasFocus ? R.drawable.player_detail_button_img_full_focused : R.drawable.player_detail_button_img_full_normal : hasFocus ? R.drawable.player_detail_button_img_play_focused : R.drawable.player_detail_button_img_play_normal;
    }

    private void showBuyBtn() {
        if (this.mBtnBuy != null) {
            int textResId;
            this.mBtnBuy.setVisibility(0);
            boolean isUserVip = GetInterfaceTools.getIGalaAccountManager().isVip();
            if (this.mAlbumInfo.isAlbumCoupon()) {
                textResId = R.string.btn_coupon;
            } else if (this.mAlbumInfo.isAlbumSinglePay()) {
                textResId = R.string.btn_buy_album;
            } else if (isUserVip) {
                textResId = R.string.share_detail_btn_renewal_vip;
            } else {
                textResId = R.string.share_detail_btn_join_vip;
            }
            this.mBtnBuy.setText(ResourceUtil.getStr(textResId));
            updateBuyButton();
            LogUtils.d(TAG, "showBuyBtn(): mIsUserActed -> " + this.mIsUserActed + ", mIsEquityFocus -> " + this.mIsEquityFocus);
            if (!this.mIsUserActed && !this.mIsEquityFocus) {
                if (!isUserVip) {
                    this.mBtnBuy.requestFocus();
                } else if (!this.mAlbumInfo.isVipAuthorized()) {
                    this.mBtnBuy.requestFocus();
                }
                if (this.mBtnBuy.getNextFocusDownId() == -1) {
                    this.mBtnBuy.setNextFocusDownId(-1);
                }
            }
        }
    }

    private void hideBuyBtn() {
        if (this.mBtnBuy != null) {
            if (this.mBtnBuy.isFocused()) {
                this.mBtnFull.requestFocus();
            }
            this.mBtnBuy.setVisibility(8);
        }
    }

    private void updateButtonFocusPath() {
        LogUtils.d(TAG, "updateFocusPath");
        ViewGroup btnPanel = (ViewGroup) this.mRootView.findViewById(R.id.share_detail_ll_btn_panel);
        int childCount = btnPanel.getChildCount();
        if (childCount > 0) {
            View firstChild = null;
            View lastChild = null;
            for (int i = 0; i <= childCount - 1; i++) {
                View curChild = btnPanel.getChildAt(i);
                if (curChild.getVisibility() == 0) {
                    if (firstChild == null) {
                        firstChild = curChild;
                    }
                    lastChild = curChild;
                    curChild.setNextFocusLeftId(0);
                    curChild.setNextFocusRightId(0);
                }
            }
            if (!(firstChild == null || this.mEnableWindowPlay)) {
                firstChild.setNextFocusLeftId(firstChild.getId());
            }
            if (lastChild != null) {
                lastChild.setNextFocusRightId(lastChild.getId());
            }
        }
    }

    private void updateFocusPath() {
        if (this.mEvEquity.getVisibility() == 0) {
            this.mBtnFav.setNextFocusUpId(this.mEvEquity.getId());
            this.mBtnFull.setNextFocusUpId(this.mEvEquity.getId());
            this.mBtnBuy.setNextFocusUpId(this.mEvEquity.getId());
            if (this.mEvEquity.getNextFocusDownId() == -1 && this.mBtnDesc.getNextFocusDownId() != this.mEvEquity.getId()) {
                this.mEvEquity.setNextFocusDownId(this.mBtnDesc.getNextFocusDownId());
            } else if (this.mEvEquity.getNextFocusDownId() == this.mBtnBuy.getId() && this.mBtnBuy.getVisibility() != 0) {
                this.mEvEquity.setNextFocusDownId(this.mBtnFull.getId());
            }
            this.mBtnDesc.setNextFocusDownId(this.mEvEquity.getId());
            return;
        }
        this.mBtnFav.setNextFocusUpId(this.mBtnDesc.getId());
        this.mBtnFull.setNextFocusUpId(this.mBtnDesc.getId());
        this.mBtnBuy.setNextFocusUpId(this.mBtnDesc.getId());
    }

    public void notifyVIPInfoReady() {
        LogUtils.d(TAG, ">> notifyVIPInfoReady, mAlbumInfo=" + this.mAlbumInfo.toStringBrief());
        if (this.mAlbumInfo.isVipAuthorized() && (this.mAlbumInfo.isAlbumSinglePay() || this.mAlbumInfo.isAlbumCoupon())) {
            this.mShowBuy = false;
        } else {
            this.mShowBuy = true;
        }
        LogRecordUtils.logd(TAG, "notifyVIPInfoReady isAlbumVip ->" + this.mAlbumInfo.isAlbumVip() + ", isAlbumCoupon -> " + this.mAlbumInfo.isAlbumCoupon() + ", isAlbumSinglePay -> " + this.mAlbumInfo.isAlbumSinglePay());
        LogRecordUtils.logd(TAG, "notifyVIPInfoReady isVipAuthorized -> " + this.mAlbumInfo.isVipAuthorized() + ", showBuy -> " + this.mShowBuy);
        if (this.mShowBuy) {
            showBuyBtn();
            updateButtonFocusPath();
        } else {
            hideBuyBtn();
        }
        Album album = this.mAlbumInfo.getAlbum();
        if (album != null) {
            LogRecordUtils.logd(TAG, "notifyVIPInfoReady(): isVip -> " + album.isVipForAccount() + ", isCoupon -> " + album.isCoupon() + ", isSingPay -> " + album.isSinglePay());
        }
        String url = "";
        IDynamicResult result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (this.mAlbumInfo.isAlbumVip()) {
            LogUtils.d(TAG, "notifyVIPInfoReady(): albumVip.");
            loadEquityImage(result.getVipMonthOperateImageUrls());
        } else if (!this.mAlbumInfo.isAlbumSinglePay() && !this.mAlbumInfo.isAlbumCoupon()) {
            LogUtils.d(TAG, "notifyVIPInfoReady(): free.");
            loadEquityImage(result.getDetailFreeVideoOperateImageUrls());
        }
    }

    public void notifyCouponReady() {
        LogUtils.d(TAG, "notifyCouponReady()");
        if (this.mAlbumInfo.isAlbumCoupon()) {
            boolean isUserVip = GetInterfaceTools.getIGalaAccountManager().isVip();
            LogUtils.d(TAG, "notifyCouponReady(): coupon, isUserVip -> " + isUserVip);
            if (this.mAlbumInfo.isVipAuthorized()) {
                loadEquityImage(GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getVipMonthOperateImageUrls());
                return;
            }
            if (isUserVip) {
                this.mEvEquity.setType(4);
                if (this.mAlbumInfo == null || StringUtils.isEmpty(this.mAlbumInfo.getCouponCount())) {
                    hideEquityImage();
                } else {
                    this.mEvEquity.setCouponCount(this.mAlbumInfo.getCouponCount());
                    showEquityImage();
                }
            } else {
                this.mEvEquity.setType(8);
                showEquityImage();
            }
            updateFocusPath();
        }
    }

    public void notifyTvodReady() {
        LogUtils.d(TAG, "notifyTvodReady()");
        if (this.mAlbumInfo.isAlbumSinglePay()) {
            boolean isUserVip = GetInterfaceTools.getIGalaAccountManager().isVip();
            LogUtils.d(TAG, "notifyTvodReady(): pay, isUserVip -> " + isUserVip);
            String url = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getVipMonthOperateImageUrls();
            if (this.mAlbumInfo.isVipAuthorized()) {
                loadEquityImage(url);
            } else if (!isUserVip) {
                loadEquityImage(url);
            } else if (this.mAlbumInfo == null || StringUtils.isEmpty(this.mAlbumInfo.getAlbumPrice()) || StringUtils.isEmpty(this.mAlbumInfo.getAlbumOriginPrice())) {
                hideEquityImage();
                updateFocusPath();
            } else if (StringUtils.equals(this.mAlbumInfo.getAlbumPrice(), this.mAlbumInfo.getAlbumOriginPrice())) {
                this.mEvEquity.setType(296);
                this.mEvEquity.setPrice(this.mAlbumInfo.getAlbumPrice());
                showEquityImage();
                updateFocusPath();
            } else {
                this.mEvEquity.setType(1);
                this.mEvEquity.setPrice(this.mAlbumInfo.getAlbumPrice(), this.mAlbumInfo.getAlbumOriginPrice());
                showEquityImage();
                updateFocusPath();
            }
        }
    }

    public void notifyScreenModeSwitched(ScreenMode mode, boolean isError) {
        LogUtils.d(TAG, "notifyScreenModeSwitched(): >> mode -> " + mode);
        this.mCurScreenMode = mode;
    }

    public void updateFullBtnText() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> updateFullBtnText, mAlbumInfo=" + this.mAlbumInfo);
        }
        AlbumInfo albumInfo = this.mAlbumInfo;
        if (albumInfo.isSourceType()) {
            CharSequence time = AlbumTextHelper.getVarietyDataForButton(albumInfo, this.mContext);
            if (!StringUtils.isEmpty(time)) {
                this.mBtnFull.setText(time);
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "Invalid Variety issue time, info=" + albumInfo);
            }
        } else if (albumInfo.isTvSeries()) {
            int playOrder = albumInfo.getPlayOrder();
            if (playOrder < 1) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "Invalid play order, info=" + albumInfo, new Throwable());
                }
            } else if (this.mOldPlayOrder != playOrder) {
                this.mOldPlayOrder = playOrder;
                this.mBtnFull.setText(ResourceUtil.getStr(R.string.play_order, Integer.valueOf(playOrder)));
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "the same play order, do not need refresh, playOrder=" + playOrder);
            }
        } else if (albumInfo.getKind() == VideoKind.VIDEO_SINGLE) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "updateFullBtnText");
            }
            this.mBtnFull.setText(ResourceUtil.getStr(this.mEnableWindowPlay ? R.string.full_screen : R.string.start_play));
        }
    }

    public boolean setDefaultFocus() {
        if (this.mAlbumInfo == null) {
            LogRecordUtils.logd(TAG, "setDefaultFocus: mAlbumInfo is null.");
            return false;
        } else if (this.mAlbumInfo.isVipAuthorized()) {
            if (this.mCurrFocus == null) {
                return this.mBtnFull.requestFocus();
            }
            if (this.mCurrFocus.getVisibility() != 0) {
                return this.mBtnFull.requestFocus();
            }
            if (this.mCurrFocus.getId() == this.mBtnBuy.getId()) {
                setZoomAnim(this.mCurrFocus);
            }
            return this.mCurrFocus.requestFocus();
        } else if (this.mBtnBuy.getVisibility() != 0) {
            return this.mBtnFull.requestFocus();
        } else {
            setZoomAnim(this.mBtnBuy);
            return this.mBtnBuy.requestFocus();
        }
    }

    public void setCurrentFocusView(View view) {
        this.mCurrFocus = view;
    }

    public void requestFullBtnFocus() {
        this.mBtnFull.requestFocus();
    }

    public View getDefaultFocusView() {
        if (this.mBtnBuy == null || this.mBtnBuy.getVisibility() != 0) {
            return this.mBtnFull;
        }
        return this.mBtnBuy;
    }

    public void setAlbumInfo(AlbumInfo albumInfo) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> setAlbumInfo, albumInfo=" + albumInfo);
        }
        if (albumInfo != null) {
            setItem("block", DetailPingBackUtils.getBlock(albumInfo));
            this.mAlbumInfo = albumInfo;
            updateFullBtnText();
        }
    }

    public void onActivityFinishing() {
        this.mAlbumInfo = null;
    }

    public void setOnCtrlFocusChangeListener(OnCtrlFocusChangeListener listener) {
        this.mCtrlFocusChangeListener = listener;
    }

    public void requestSummaryFocus() {
        this.mBtnDesc.requestFocus();
    }

    public void setDescVisibility(int visibility) {
        this.mBtnDesc.setVisibility(visibility);
    }

    private void notifyFocusChange(DetailButtonKeyFront k, int rid) {
        if (this.mCtrlFocusChangeListener != null) {
            this.mCtrlFocusChangeListener.onFoucusChanged(k, rid);
        }
    }

    public void setOnCtrlButtonClickedListener(OnCtrlButtonClickedListener listener) {
        this.mCtrlButtonClickedListener = listener;
    }

    public boolean isEquityShow() {
        if (this.mEvEquity == null || this.mEvEquity.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    private void notifyFullButtonClicked() {
        if (this.mCtrlButtonClickedListener != null) {
            this.mCtrlButtonClickedListener.onFullButtonClicked();
        }
    }

    private void notifyFavButtonClicked() {
        if (this.mCtrlButtonClickedListener != null) {
            this.mCtrlButtonClickedListener.onFavButtonClicked();
        }
    }

    private void notifyVIPButtonClicked() {
        if (this.mCtrlButtonClickedListener != null) {
            this.mCtrlButtonClickedListener.onVIPButtonClicked();
        }
    }

    private synchronized void notifyDescButtonClicked() {
        if (this.mCtrlButtonClickedListener != null) {
            this.mCtrlButtonClickedListener.onDescButtonClicked();
        }
    }

    private void notifyEquityImageClicked() {
        if (this.mCtrlButtonClickedListener != null) {
            this.mCtrlButtonClickedListener.onEquityImageClicked();
        }
    }

    private void setItem(String key, String value) {
        this.mPingbackContext.setItem(key, new PingbackItem(key, value));
    }

    private void sendFavButtonClickedPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> sendFavButtonClickedPingback");
        }
        if (this.mAlbumInfo != null && this.mAlbumInfo.getAlbum() != null && this.mPingbackContext != null) {
            String rseat = "";
            if (this.mBtnFav != null) {
                rseat = this.mBtnFav.getText().toString().trim();
            }
            PingbackFactory.instance().createPingback(5).addItem(PingbackStore.R.R_TYPE(this.mAlbumInfo.getAlbum().qpId)).addItem(this.mPingbackContext.getItem("block")).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(rseat)).addItem(RPAGETYPE.DETAIL).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getAlbum().chnId))).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("rfr")).addItem(NOW_QPID.NOW_QPID_TYPE(this.mAlbumInfo.getAlbumId())).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).post();
        }
    }

    private void sendBuyButtonClickedPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> sendFavButtonClickedPingback");
        }
        if (this.mAlbumInfo != null && this.mAlbumInfo.getAlbum() != null && this.mPingbackContext != null) {
            String rseat = "";
            if (this.mBtnBuy != null) {
                rseat = this.mBtnBuy.getText().toString().trim();
            }
            PingbackFactory.instance().createPingback(4).addItem(PingbackStore.R.R_TYPE(this.mAlbumInfo.getAlbum().qpId)).addItem(this.mPingbackContext.getItem("block")).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(rseat)).addItem(RPAGETYPE.DETAIL).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getAlbum().chnId))).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("rfr")).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).addItem(NOW_QPID.NOW_QPID_TYPE(this.mAlbumInfo.getAlbumId())).addItem(this.mPingbackContext.getItem("s2")).post();
        }
    }

    private void sendSummaryClickPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> sendSummaryClickPingback");
        }
        if (this.mAlbumInfo == null || this.mAlbumInfo.getAlbum() == null) {
            LogUtils.d(TAG, "video is null");
        } else if (this.mPingbackContext != null) {
            PingbackFactory.instance().createPingback(10).addItem(PingbackStore.R.R_TYPE(this.mAlbumInfo.getAlbumId())).addItem(this.mPingbackContext.getItem("block")).addItem(RTTYPE.RT_I).addItem(RSEATTYPE.INTRODUCTION).addItem(RPAGETYPE.DETAIL).addItem(C1.C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).addItem(this.mPingbackContext.getItem("e")).addItem(this.mPingbackContext.getItem("rfr")).addItem(NOW_QPID.NOW_QPID_TYPE(this.mAlbumInfo.getAlbumId())).addItem(NOW_C1.NOW_C1_TYPE(String.valueOf(this.mAlbumInfo.getChannelId()))).post();
        }
    }
}
