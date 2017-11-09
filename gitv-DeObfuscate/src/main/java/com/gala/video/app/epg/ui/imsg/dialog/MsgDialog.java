package com.gala.video.app.epg.ui.imsg.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RemoteViews.RemoteView;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.DisplayUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;

@RemoteView
public class MsgDialog extends Dialog {
    protected static final int DIALOG_CANCEL_DELAYMILLIS = 15000;
    protected static final int DIALOG_HEIGHT = 419;
    protected static final int DIALOG_WIDTH = 595;
    protected static final int LINE_LENGTH = 14;
    protected static final int MARGIN_EDGE = 0;
    protected static final int MAX_LENGTH = 20;
    public static final int POSITION_CENTER_BOTTOM = 2;
    public static final int POSITION_LEFT_BOTTOM = 1;
    public static final int POSITION_NONE = 0;
    public static final int POSITION_RIGHT_BOTTOM = 3;
    public static final int POSITION_RIGHT_TOP = 4;
    protected static final int POSTER_WIDTH = 238;
    private final String TAG;
    private Runnable mCancelRunnable;
    protected Context mContext;
    private MsgDialog mDialog;
    private Handler mHandler;
    protected int mHeight;
    protected boolean mIsSystem;
    protected ImageView mIvPoster;
    private long mLastAnimationX;
    private long mLastAnimationY;
    private MsgDialogStatusListener mMsgDialogStatusListener;
    private OnClickListener mOnClickListener;
    protected int mPosition;
    protected int mPositionX;
    protected int mPositionY;
    private float mScale;
    protected TextView mTvHint;
    protected TextView mTvHint2;
    protected View mView;
    protected int mWidth;

    class C08861 implements IImageCallback {
        C08861() {
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            try {
                if (VERSION.SDK_INT >= 16) {
                    RelativeLayout lt = (RelativeLayout) imageRequest.getCookie();
                    lt.setBackground(new BitmapDrawable(MsgDialog.this.mContext.getResources(), bitmap));
                    lt.invalidate();
                    MsgDialog.this.mTvHint.setVisibility(4);
                    MsgDialog.this.mTvHint2.setVisibility(4);
                    MsgDialog.this.mDialog.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                MsgDialog.this.mDialog.cancel();
            }
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
            Log.e("MsgDialog", "imageRequest = " + imageRequest, e);
            MsgDialog.this.mDialog.show();
        }
    }

    class C08872 implements IImageCallback {
        C08872() {
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            ImageView imgView = (ImageView) imageRequest.getCookie();
            MsgDialog.this.mWidth += MsgDialog.this.getRawPixel(238.0f);
            MsgDialog.this.mIvPoster.setVisibility(0);
            imgView.setImageBitmap(bitmap);
            imgView.invalidate();
            MsgDialog.this.refresh();
            MsgDialog.this.mDialog.show();
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
            Log.e("MsgDialog", "imageRequest = " + imageRequest, e);
            MsgDialog.this.mDialog.show();
        }
    }

    class C08883 implements Runnable {
        C08883() {
        }

        public void run() {
            if (MsgDialog.this.mMsgDialogStatusListener != null) {
                MsgDialog.this.mMsgDialogStatusListener.onCancel(MsgDialog.this.mDialog);
            }
            MsgDialog.this.dismiss();
        }
    }

    public interface MsgDialogStatusListener {
        void onCancel(Dialog dialog);

        void onShow(Dialog dialog);
    }

    public interface OnClickListener {
        void onClick(Dialog dialog, KeyEvent keyEvent);
    }

    public MsgDialog(Context context) {
        this(context, false);
    }

    public MsgDialog(Context context, boolean isSystem) {
        this(context, C0508R.style.Theme_Dialog_Loading_Notitle, 0, isSystem);
    }

    public MsgDialog(Context context, int position) {
        this(context, position, false);
    }

    public MsgDialog(Context context, int position, boolean isSystem) {
        this(context, C0508R.style.Theme_Dialog_Loading_Notitle, position, isSystem);
    }

    public MsgDialog(Context context, int theme, int position, boolean isSystem) {
        super(context, theme);
        this.TAG = "MsgDialog";
        this.mPosition = 0;
        this.mPositionX = 0;
        this.mPositionY = 0;
        this.mLastAnimationX = 0;
        this.mLastAnimationY = 0;
        this.mWidth = 0;
        this.mHeight = 0;
        this.mScale = -1.0f;
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mCancelRunnable = new C08883();
        this.mContext = context;
        this.mPosition = position;
        this.mIsSystem = isSystem;
        this.mDialog = this;
        init();
    }

    private void init() {
        this.mScale = ((float) this.mContext.getResources().getDisplayMetrics().widthPixels) / 1920.0f;
        this.mView = View.inflate(this.mContext, C0508R.layout.msg_dialog, null);
        setContentView(this.mView);
        initViews();
        initData();
        if (this.mIsSystem) {
            ImageProviderApi.getImageProvider().initialize(this.mContext);
        }
    }

    private void initData() {
        this.mWidth = getRawPixel(595.0f);
        this.mHeight = getRawPixel(419.0f);
        this.mPositionX = getPositionX();
        this.mPositionY = getPositionY();
    }

    private void initViews() {
        this.mTvHint = (TextView) findViewById(C0508R.id.epg_tv_msg_dialog_hint);
        this.mTvHint2 = (TextView) findViewById(C0508R.id.epg_tv_msg_dialog_hint2);
        this.mIvPoster = (ImageView) findViewById(C0508R.id.epg_img_msg_dialog_poster);
    }

    public void setPosition(int x, int y) {
        this.mPositionX = x;
        this.mPositionY = y;
    }

    public void setSize(int width, int height) {
        this.mWidth = width;
        this.mHeight = height;
    }

    public void setPosition(int position) {
        this.mPosition = position;
        initData();
    }

    public void setData(String imgUrl, String msg, int style) {
        setHint(msg);
        if (style == 1) {
            setHImg(imgUrl);
        } else if (style == 2) {
            setPosters(imgUrl);
        } else {
            this.mDialog.show();
        }
    }

    public void setIsSystem(boolean isSystem) {
        this.mIsSystem = isSystem;
    }

    @SuppressLint({"NewApi"})
    public void setHImg(String imgUrl) {
        try {
            if (TextUtils.isEmpty(imgUrl)) {
                this.mDialog.show();
                return;
            }
            ImageRequest request = new ImageRequest(imgUrl, findViewById(C0508R.id.epg_msg_dialog_text));
            request.setLasting(true);
            ImageProviderApi.getImageProvider().loadImage(request, new C08861());
        } catch (Exception e) {
            e.printStackTrace();
            this.mDialog.show();
        }
    }

    public void setPosters(String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) {
            this.mDialog.show();
            return;
        }
        try {
            ImageRequest request = new ImageRequest(imgUrl, this.mIvPoster);
            request.setLasting(true);
            ImageProviderApi.getImageProvider().loadImage(request, new C08872());
        } catch (Exception e) {
            e.printStackTrace();
            this.mDialog.show();
        }
    }

    public void setHint(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.mTvHint.setText(formatText(this.mTvHint.getPaint(), text));
        }
    }

    private CharSequence formatText(TextPaint paint, String text) {
        float fontWidth = paint.measureText("一");
        this.mTvHint.setWidth((int) (14.0f * fontWidth));
        return TextUtils.ellipsize(text, paint, 20.0f * fontWidth, TruncateAt.END);
    }

    private void setParams() {
        this.mIvPoster.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        LogUtils.m1574i("MsgDialog", "position: poster-width: " + this.mIvPoster.getMeasuredWidth() + ",poster-height: " + this.mIvPoster.getMeasuredHeight());
        Window dialogWindow = getWindow();
        if (this.mIsSystem) {
            dialogWindow.setType(2003);
        }
        LayoutParams layoutParams = dialogWindow.getAttributes();
        dialogWindow.setGravity(51);
        layoutParams.x = this.mPositionX;
        layoutParams.y = this.mPositionY;
        layoutParams.width = this.mWidth;
        layoutParams.height = this.mHeight;
        layoutParams.alpha = 1.0f;
        layoutParams.flags |= 32;
        dialogWindow.setAttributes(layoutParams);
    }

    protected void refresh() {
        setParams();
    }

    protected int getPositionY() {
        switch (this.mPosition) {
            case 1:
            case 2:
            case 3:
                return (DisplayUtils.getScreenHeight() - getRawPixel(0.0f)) - this.mHeight;
            case 4:
                return getRawPixel(57.0f);
            default:
                return this.mPositionY;
        }
    }

    protected int getPositionX() {
        switch (this.mPosition) {
            case 1:
                return DisplayUtils.dip2px(0);
            case 2:
                return (DisplayUtils.getScreenWidth() - this.mWidth) / 2;
            case 3:
            case 4:
                return (DisplayUtils.getScreenWidth() - getRawPixel(0.0f)) - this.mWidth;
            default:
                return this.mPositionX;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            int code = event.getKeyCode();
            onClick(event);
            if (code == 22 || code == 21) {
                lastXAnimation();
            } else if (code == 20 || code == 19) {
                lastYAnimation();
            }
        }
        return super.dispatchKeyEvent(event);
    }

    private void onClick(KeyEvent event) {
        if (this.mOnClickListener != null) {
            this.mOnClickListener.onClick(this, event);
        }
    }

    public void show() {
        setParams();
        super.show();
        this.mHandler.postDelayed(this.mCancelRunnable, 15000);
        if (this.mMsgDialogStatusListener != null) {
            this.mMsgDialogStatusListener.onShow(this);
        }
    }

    public void cancel() {
        this.mHandler.removeCallbacks(this.mCancelRunnable);
        super.cancel();
    }

    public void dismiss() {
        this.mHandler.removeCallbacks(this.mCancelRunnable);
        super.dismiss();
    }

    public void lastXAnimation() {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationX > 500) {
            this.mView.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake));
            this.mLastAnimationX = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public void lastYAnimation() {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationY > 500) {
            this.mView.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake_y));
            this.mLastAnimationY = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public int getRawPixel(float pixel) {
        return Math.round(this.mScale * pixel);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void setMsgDialogStatusListener(MsgDialogStatusListener msgDialogStatusListener) {
        this.mMsgDialogStatusListener = msgDialogStatusListener;
    }
}
