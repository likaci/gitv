package com.gala.video.app.epg.home.ads.presenter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.ResourceOperatePingbackModel;
import com.gala.video.app.epg.home.data.model.ExitOperateImageModel;
import com.gala.video.app.epg.home.data.provider.ExitOperateImageProvider;
import com.gala.video.app.epg.home.data.tool.DataBuildTool;
import com.gala.video.app.epg.home.utils.ResourceOperateImageUtils;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult.OperationImageType;
import com.gala.video.lib.share.utils.AnimationUtil;

public class ExitOperateImagePresenter {
    private static final String TAG = "ExitOperateImagePresenter";
    private View mAdImageQrLayout = null;
    private View mContentView;
    private Context mContext = null;
    private ExitOperateImageModel mExitOperateImageModel = null;
    @SuppressLint({"HandlerLeak"})
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ExitOperateImagePresenter.this.mContentView.setBackgroundResource(R.color.transparent);
            ExitOperateImagePresenter.this.mOperateImageView.setImageBitmap(ExitOperateImagePresenter.this.mOperateBitmap);
            ExitOperateImagePresenter.this.mContentView.setBackgroundResource(R.drawable.share_item_rect_selector);
            ExitOperateImagePresenter.this.mContentView.setFocusable(true);
            ExitOperateImagePresenter.this.mContentView.setClickable(true);
            ExitOperateImagePresenter.this.mOperateImageView.setScaleType(ScaleType.FIT_XY);
            ExitOperateImagePresenter.this.mContentView.setOnClickListener(ExitOperateImagePresenter.this.mImageClickListener);
            ExitOperateImagePresenter.this.mContentView.setOnFocusChangeListener(ExitOperateImagePresenter.this.mImageFocusChangeListener);
            LogUtils.d(ExitOperateImagePresenter.TAG, "show, exit apk operate image show");
        }
    };
    private OnClickListener mImageClickListener = new OnClickListener() {
        public void onClick(View v) {
            ExitOperateImagePresenter.this.onClickImage();
            if (ExitOperateImagePresenter.this.mOnOperateImageClickListener != null) {
                ExitOperateImagePresenter.this.mOnOperateImageClickListener.onClick(ExitOperateImagePresenter.this.mExitOperateImageModel);
            }
        }
    };
    private OnFocusChangeListener mImageFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200);
        }
    };
    private int mNextFocusDownViewId;
    private OnOperateImageClickListener mOnOperateImageClickListener = null;
    private volatile Bitmap mOperateBitmap = null;
    private ImageView mOperateImageView = null;

    public interface OnOperateImageClickListener {
        void onClick(ExitOperateImageModel exitOperateImageModel);
    }

    public ExitOperateImagePresenter(Context mContext) {
        this.mContext = mContext;
    }

    public void setWidgets(View contentView, ImageView imageView, View adImageQrLayout) {
        this.mOperateImageView = imageView;
        this.mAdImageQrLayout = adImageQrLayout;
        this.mContentView = contentView;
    }

    private void setData(ExitOperateImageModel model) {
        this.mExitOperateImageModel = model;
    }

    public boolean hasLocalImage() {
        return ExitOperateImageProvider.getInstance().getLocalDataSize() > 0;
    }

    private ExitOperateImageModel fetchData() {
        ExitOperateImageModel model = ExitOperateImageProvider.getInstance().getExitOperateImageModel();
        setData(model);
        return model;
    }

    private void onClickImage() {
        if (this.mExitOperateImageModel == null) {
            LogUtils.d(TAG, "onClick, exit operate image data is empty");
        } else if (!ResourceOperateImageUtils.isSupportJump(this.mExitOperateImageModel.getChannelLabel())) {
            ResourceOperateImageUtils.onClickForNotSupportJump(this.mContext);
            LogUtils.d(TAG, "on click exit operate image, not support jump");
        } else if (ResourceOperateImageUtils.isSupportResType(this.mExitOperateImageModel.getChannelLabel())) {
            ResourceOperatePingbackModel pingbackModel = ResourceOperateImageUtils.getPingbackModel(this.mExitOperateImageModel.getChannelLabel(), OperationImageType.EXIT);
            pingbackModel.setIncomesrc("others");
            pingbackModel.setS2(IntentConfig2.FROM_EXIT_APP);
            PingBackUtils.setTabSrc("其他");
            pingbackModel.setEnterType(13);
            ResourceOperateImageUtils.onClick(this.mContext, this.mExitOperateImageModel.getChannelLabel(), pingbackModel);
        } else {
            LogUtils.w(TAG, "on click exit operate image, not support Resource type :" + DataBuildTool.getItemType(this.mExitOperateImageModel.getChannelLabel()));
            ResourceOperateImageUtils.onClickForNotSupportJump(this.mContext);
        }
    }

    public boolean isEnableJump() {
        if (this.mExitOperateImageModel != null && ResourceOperateImageUtils.isSupportJump(this.mExitOperateImageModel.getChannelLabel()) && ResourceOperateImageUtils.isSupportResType(this.mExitOperateImageModel.getChannelLabel())) {
            return true;
        }
        return false;
    }

    public void setNextFocusDownId(int id) {
        if (this.mOperateImageView != null) {
            this.mOperateImageView.setNextFocusDownId(id);
        }
    }

    public void show() {
        final ExitOperateImageModel model = fetchData();
        if (model == null) {
            LogUtils.w(TAG, "show, get exit operate image model is null");
        } else {
            new Thread8K(new Runnable() {
                public void run() {
                    String path = model.getImagePath();
                    long startDecodeTime = SystemClock.elapsedRealtime();
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (bitmap == null) {
                        LogUtils.d(ExitOperateImagePresenter.TAG, "show, decodeFile, operate image bitmap is null, path = " + path);
                        return;
                    }
                    ExitOperateImagePresenter.this.mOperateBitmap = bitmap;
                    LogUtils.d(ExitOperateImagePresenter.TAG, "show, decode bitmap, cost time :" + (SystemClock.elapsedRealtime() - startDecodeTime));
                    ExitOperateImagePresenter.this.mHandler.sendEmptyMessage(1);
                }
            }, TAG).start();
        }
    }

    public void setOnOperateImageClickListener(OnOperateImageClickListener listener) {
        this.mOnOperateImageClickListener = listener;
    }
}
