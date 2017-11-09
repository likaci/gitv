package com.gala.video.app.player.ui.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.sdk.player.BitStream;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.overlay.UiHelper;
import com.gala.video.app.player.ui.overlay.contents.BitStreamContent.BitStreamParams;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import org.xbill.DNS.WKSRecord.Service;

public class BitStreamGuideDialog extends AlertDialog {
    public static final int GUIDE_TYPE_4K = 2;
    public static final int GUIDE_TYPE_HDR = 1;
    public static final int GUIDE_TYPE_SDR = 3;
    private static final String TAG = BitStreamGuideDialog.class.getSimpleName();
    private TextView mBottomLeft;
    private TextView mBottomRight;
    private Context mContext;
    private OnFocusChangeListener mFocusChangedListener = new C15154();
    private OnHDRToggleListener mHDRToggleListener;
    private ImageView mImgBg;
    private String mImgUrl;
    private OnKeyListener mKeyEventListener = new C15165();
    private OnHDRUserPlayListener mOnHDRUserPlayListener;
    private BitStreamParams mParams;
    private int mType;
    private Handler mUIHandler = new Handler(Looper.getMainLooper());

    public interface OnHDRToggleListener {
        void onToggle(boolean z);
    }

    public interface OnHDRUserPlayListener {
        void onPlay(String str);
    }

    class C15121 implements IImageCallback {
        C15121() {
        }

        public void onSuccess(ImageRequest arg0, final Bitmap bitmap) {
            BitStreamGuideDialog.this.mUIHandler.post(new Runnable() {
                public void run() {
                    BitStreamGuideDialog.this.mImgBg.setImageBitmap(bitmap);
                }
            });
        }

        public void onFailure(ImageRequest arg0, Exception arg1) {
        }
    }

    class C15132 implements OnClickListener {
        C15132() {
        }

        public void onClick(View v) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamGuideDialog.TAG, ">> handlemBottomLeftClicked ");
            }
            BitStreamGuideDialog.this.mHDRToggleListener.onToggle(true);
            BitStreamGuideDialog.this.dismiss();
        }
    }

    class C15143 implements OnClickListener {
        C15143() {
        }

        public void onClick(View v) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamGuideDialog.TAG, ">> handlemBottomRightClicked ");
            }
            BitStreamGuideDialog.this.mOnHDRUserPlayListener.onPlay("next");
            BitStreamGuideDialog.this.dismiss();
        }
    }

    class C15154 implements OnFocusChangeListener {
        C15154() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200, true);
        }
    }

    class C15165 implements OnKeyListener {
        C15165() {
        }

        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(BitStreamGuideDialog.TAG, ">> mKeyEventListener.onKey, v=" + v + ", event=" + event);
            }
            if (19 != keyCode || event.getAction() == 0) {
            }
            return false;
        }
    }

    public BitStreamGuideDialog(Context context, BitStreamParams params, int type) {
        super(context, C1291R.style.Theme_Dialog_Exit_App_Translucent_NoTitle);
        this.mParams = params;
        this.mContext = context;
        this.mType = type;
    }

    public BitStreamGuideDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public BitStreamGuideDialog(Context context, int theme) {
        super(context, theme);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C1291R.layout.player_hdr_guide);
        initWindow();
        initViews();
        setupViews();
        initFocus();
    }

    private void initWindow() {
        getWindow().setLayout(-1, -1);
        LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawableResource(C1291R.color.exit_app_dialog_background_color);
    }

    private void initViews() {
        Rect bgPadding = UiHelper.getBgDrawablePaddings(ResourceUtil.getDrawable(C1291R.drawable.share_exit_app_ad_dialog_btn_selector));
        this.mBottomLeft = (TextView) findViewById(C1291R.id.bottom_left_button);
        this.mBottomRight = (TextView) findViewById(C1291R.id.bottom_right_button);
        this.mBottomRight.setText(getContext().getResources().getString(C1291R.string.dhr_guide_next_start));
        this.mImgBg = (ImageView) findViewById(C1291R.id.iv_bg_hdr);
        this.mBottomLeft.setText(getLeftButtonTxt());
        switch (this.mType) {
            case 1:
                this.mImgUrl = getHDRGuideBgImgUrls();
                break;
            case 2:
                this.mImgUrl = get4kGuideBgImgUrls();
                break;
            case 3:
                this.mImgUrl = get1080GuideBgImgUrls();
                break;
        }
        ImageProviderApi.getImageProvider().loadImage(new ImageRequest(this.mImgUrl), new C15121());
        LinearLayout.LayoutParams bottomLP = new LinearLayout.LayoutParams((ResourceUtil.getDimen(C1291R.dimen.dimen_249dp) + bgPadding.left) + bgPadding.right, (ResourceUtil.getDimen(C1291R.dimen.dimen_60dp) + bgPadding.top) + bgPadding.bottom);
        this.mBottomLeft.setLayoutParams(bottomLP);
        this.mBottomRight.setLayoutParams(bottomLP);
    }

    private String getLeftButtonTxt() {
        if (this.mParams != null) {
            BitStream bitstream = this.mParams.getHdrSdrBitStream();
            if (bitstream != null) {
                int type = bitstream.getBenefitType();
                if (type == 2) {
                    return this.mContext.getResources().getString(C1291R.string.employing_six_minutes);
                }
                if (type == 0) {
                    return this.mContext.getResources().getString(C1291R.string.employing_instantly);
                }
            }
        }
        return this.mContext.getResources().getString(C1291R.string.employing_instantly);
    }

    private String get4kGuideBgImgUrls() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        String hdr4kGuideBgImgUrl = model != null ? model.get4kGuideBgImgUrls() : "";
        LogUtils.m1568d(TAG, "get4kGuideBgImgUrls=" + hdr4kGuideBgImgUrl);
        return hdr4kGuideBgImgUrl;
    }

    private String get1080GuideBgImgUrls() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        String hdr1080GuideBgImgUrl = model != null ? model.get1080pGuideBgImgUrls() : "";
        LogUtils.m1568d(TAG, "hdr1080GuideBgImgUrl=" + hdr1080GuideBgImgUrl);
        return hdr1080GuideBgImgUrl;
    }

    private String getHDRGuideBgImgUrls() {
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        String hdrGuideBgImgUrl = model != null ? model.getHdrGuideBgImgUrls() : "";
        LogUtils.m1568d(TAG, "hdrGuideBgImgUrl=" + hdrGuideBgImgUrl);
        return hdrGuideBgImgUrl;
    }

    public void setOnHDRToggleListener(OnHDRToggleListener listener) {
        this.mHDRToggleListener = listener;
    }

    public void setOnHDRUserPlayListener(OnHDRUserPlayListener listener) {
        this.mOnHDRUserPlayListener = listener;
    }

    private void setupViews() {
        this.mBottomLeft.setOnFocusChangeListener(this.mFocusChangedListener);
        this.mBottomRight.setOnFocusChangeListener(this.mFocusChangedListener);
        this.mBottomLeft.setOnKeyListener(this.mKeyEventListener);
        this.mBottomRight.setOnKeyListener(this.mKeyEventListener);
        this.mBottomLeft.setOnClickListener(new C15132());
        this.mBottomRight.setOnClickListener(new C15143());
    }

    private void initFocus() {
        this.mBottomLeft.setFocusable(false);
        this.mBottomRight.setFocusable(false);
        this.mBottomLeft.setFocusable(true);
        this.mBottomRight.setFocusable(true);
    }

    public void dismiss() {
        super.dismiss();
    }

    public void show() {
        super.show();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> focus debug, dispatchKeyEvent, event=" + event);
        }
        boolean requestResult = false;
        if (20 == event.getKeyCode() && event.getAction() == 0) {
            View focused = getWindow().getDecorView().findFocus();
            View next = FocusFinder.getInstance().findNextFocus((ViewGroup) getWindow().getDecorView(), focused, Service.CISCO_FNA);
            if (next == null || !next.requestFocus(Service.CISCO_FNA)) {
                requestResult = false;
            } else {
                requestResult = true;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "focus debug, focused=" + focused + ", next=" + next + ", requestResult=" + requestResult);
            }
        }
        if (4 == event.getKeyCode() && 1 == event.getAction()) {
            dismiss();
            if (this.mOnHDRUserPlayListener != null) {
                this.mOnHDRUserPlayListener.onPlay("back");
            }
            requestResult = true;
        }
        if (requestResult || super.dispatchKeyEvent(event)) {
            return true;
        }
        return false;
    }
}
