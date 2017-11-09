package com.gala.video.app.epg.ui.star.widget;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.BitmapUtils;
import com.gala.video.lib.framework.core.utils.BitmapUtils.ScalingLogic;
import com.gala.video.lib.framework.core.utils.BlurUtils;
import com.gala.video.lib.share.common.widget.alignmentview.AlignmentTextView;
import com.gala.video.lib.share.utils.ResourceUtil;
import org.xbill.DNS.WKSRecord.Service;

public class StarsFullView {
    private View mFullView;
    private ImageView mImgDescBg;
    private boolean mIsDescriptionBgSet = false;
    private boolean mIsShown = false;
    private IImageCallback mLoadDescBgCallback = new IImageCallback() {
        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            final Bitmap mScaleBitmap = StarsFullView.boxBlurFilter(bitmap);
            StarsFullView.this.mMainHandler.post(new Runnable() {
                public void run() {
                    StarsFullView.this.mImgDescBg.setImageBitmap(mScaleBitmap);
                }
            });
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
        }
    };
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private OnFullDescClickedListener mOnFullDescClickedListener;
    private View mRootView;
    private Star mStar;
    private String mStarPicUrl;
    private AlignmentTextView mTxtDescDetail;
    private TextView mTxtDescTitle;
    private LinearLayout mllDesc;

    public interface OnFullDescClickedListener {
        void onFullDescClicked();
    }

    public StarsFullView(View root) {
        this.mRootView = root;
    }

    private void initViews(int realcount) {
        this.mFullView = ((ViewStub) this.mRootView.findViewById(R.id.epg_stars_full_description_id)).inflate();
        this.mFullView.setVisibility(0);
        this.mllDesc = (LinearLayout) this.mFullView.findViewById(R.id.ll_album_desc);
        this.mImgDescBg = (ImageView) this.mFullView.findViewById(R.id.img_album_desc_bg);
        this.mTxtDescTitle = (TextView) this.mFullView.findViewById(R.id.tv_album_desc_title);
        this.mTxtDescDetail = (AlignmentTextView) this.mFullView.findViewById(R.id.tv_album_desc_content);
        if (realcount > 0 && realcount < 14) {
            LayoutParams LP = (LayoutParams) this.mTxtDescDetail.getLayoutParams();
            LP.height = (realcount + 2) * ResourceUtil.getDimen(R.dimen.dimen_32dp);
            this.mTxtDescDetail.setLayoutParams(LP);
        }
    }

    public void show(int realcount) {
        if (!this.mIsShown) {
            if (this.mStar == null) {
                throw new RuntimeException("mStar should be set before show()");
            }
            if (this.mFullView == null) {
                initViews(realcount);
                setFullViews();
                setDataAndBackground();
            }
            this.mImgDescBg.setVisibility(0);
            this.mllDesc.setVisibility(0);
            this.mTxtDescDetail.requestFocus();
            this.mIsShown = true;
        }
    }

    public void hide() {
        if (this.mIsShown) {
            this.mllDesc.setVisibility(8);
            this.mImgDescBg.setVisibility(8);
            this.mIsShown = false;
        }
    }

    public boolean isShown() {
        return this.mIsShown;
    }

    private void setFullViews() {
        this.mTxtDescDetail.setLineSpace((float) ResourceUtil.getDimen(R.dimen.dimen_10dp));
        this.mTxtDescDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mTxtDescDetail.setScrollbarFadingEnabled(false);
        this.mTxtDescDetail.setNextFocusDownId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusForwardId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusLeftId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusRightId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusUpId(R.id.tv_album_desc_content);
    }

    private void setDataAndBackground() {
        if (this.mStar != null) {
            this.mStarPicUrl = this.mStar.cover;
            this.mTxtDescTitle.setText(this.mStar.name);
            this.mTxtDescDetail.setText(this.mStar.desc);
            if (!this.mIsDescriptionBgSet) {
                this.mImgDescBg.setImageDrawable(ResourceUtil.getDrawable(R.drawable.share_loadingview_bg).getConstantState().newDrawable());
                this.mIsDescriptionBgSet = true;
            }
            updateAndBlurImageView(this.mStarPicUrl);
        }
    }

    private void updateAndBlurImageView(String url) {
        ImageProviderApi.getImageProvider().loadImage(new ImageRequest(url), this.mLoadDescBgCallback);
    }

    private static Bitmap boxBlurFilter(Bitmap bitmap) {
        Bitmap bitmap2 = BlurUtils.boxBlurFilter(BitmapUtils.createScaledBitmap(bitmap, 200, Service.LOCUS_MAP, ScalingLogic.FIT));
        return Bitmap.createBitmap(bitmap2, 5, 5, bitmap2.getWidth() - 10, bitmap2.getHeight() - 10);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (!this.mIsShown || (23 != event.getKeyCode() && 66 != event.getKeyCode() && 4 != event.getKeyCode())) {
            return false;
        }
        OnFullDescClicked();
        return true;
    }

    private synchronized void OnFullDescClicked() {
        if (this.mOnFullDescClickedListener != null) {
            this.mOnFullDescClickedListener.onFullDescClicked();
        }
    }

    public void setStar(Star star) {
        this.mStar = star;
    }

    public void setOnFullDescClickedListener(OnFullDescClickedListener listener) {
        this.mOnFullDescClickedListener = listener;
    }

    public void onDestroy() {
        this.mMainHandler.removeCallbacksAndMessages(null);
    }
}
