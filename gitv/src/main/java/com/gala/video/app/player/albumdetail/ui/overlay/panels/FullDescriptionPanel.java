package com.gala.video.app.player.albumdetail.ui.overlay.panels;

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
import com.gala.video.app.player.R;
import com.gala.video.app.player.utils.ImageViewUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.alignmentview.AlignmentTextView;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class FullDescriptionPanel {
    private static final float LINE_COUNT = 34.0f;
    private static final String TAG = "Detail/UI/FullDescriptionPanel";
    private OnFullDescClickedListener mFullDescClickedListener;
    private View mFullDescriptionRootView;
    private ImageView mImgDescBg;
    private boolean mIsDescriptionBgSet = false;
    private boolean mIsPanelShown = false;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private View mRootView;
    private AlignmentTextView mTxtDescDetail;
    private TextView mTxtDescTitle;
    private LinearLayout mllDesc;

    public interface OnFullDescClickedListener {
        void onFullDescClicked();
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (!this.mIsPanelShown || event.getAction() != 0 || event.getRepeatCount() != 0 || (23 != event.getKeyCode() && 66 != event.getKeyCode() && 4 != event.getKeyCode())) {
            return false;
        }
        notifyFullDescClicked();
        LogRecordUtils.logd(TAG, "handleKeyEvent, handled.");
        return true;
    }

    public FullDescriptionPanel(View root) {
        this.mRootView = root;
    }

    public void show(String title, String desc, String imageURL, int realcount) {
        LogRecordUtils.logd(TAG, ">> show(), mIsPanelShown=" + this.mIsPanelShown);
        if (!this.mIsPanelShown) {
            if (this.mFullDescriptionRootView == null) {
                initFullDescriptionViews();
                setupFullDescriptionViews();
                setupBackground(imageURL);
            }
            setupDescText(title, desc);
            this.mImgDescBg.setVisibility(0);
            this.mllDesc.setVisibility(0);
            this.mTxtDescDetail.requestFocus();
            this.mIsPanelShown = true;
        }
    }

    private void initFullDescriptionViews() {
        this.mFullDescriptionRootView = ((ViewStub) this.mRootView.findViewById(R.id.stub_full_description)).inflate();
        this.mFullDescriptionRootView.setVisibility(0);
        this.mllDesc = (LinearLayout) this.mFullDescriptionRootView.findViewById(R.id.ll_album_desc);
        this.mImgDescBg = (ImageView) this.mFullDescriptionRootView.findViewById(R.id.img_album_desc_bg);
        this.mTxtDescTitle = (TextView) this.mFullDescriptionRootView.findViewById(R.id.tv_album_desc_title);
        this.mTxtDescDetail = (AlignmentTextView) this.mFullDescriptionRootView.findViewById(R.id.tv_album_desc_content);
    }

    private void setupFullDescriptionViews() {
        LogRecordUtils.logd(TAG, ">> setupFullDescriptionViews.");
        this.mTxtDescDetail.setLineSpace((float) ResourceUtil.getDimen(R.dimen.dimen_10dp));
        this.mTxtDescDetail.setMovementMethod(ScrollingMovementMethod.getInstance());
        this.mTxtDescDetail.setScrollbarFadingEnabled(false);
        this.mTxtDescDetail.setNextFocusDownId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusForwardId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusLeftId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusRightId(R.id.tv_album_desc_content);
        this.mTxtDescDetail.setNextFocusUpId(R.id.tv_album_desc_content);
    }

    private void notifyFullDescClicked() {
        LogRecordUtils.logd(TAG, ">> notifyFullDescClicked");
        if (this.mFullDescClickedListener != null) {
            this.mFullDescClickedListener.onFullDescClicked();
        }
    }

    private void setupBackground(String imageURL) {
        LogRecordUtils.logd(TAG, ">> setupBackground");
        if (!this.mIsDescriptionBgSet) {
            this.mImgDescBg.setImageDrawable(ResourceUtil.getDrawable(R.drawable.share_loadingview_bg).getConstantState().newDrawable());
            this.mIsDescriptionBgSet = true;
        }
        String realUrl = PicSizeUtils.getUrlWithSize(PhotoSize._480_270, imageURL);
        LogRecordUtils.logd(TAG, "loadDetailImage: tvPic=" + imageURL + ", url=" + realUrl);
        ImageViewUtils.updateAndBlurImageView(this.mImgDescBg, realUrl, this.mMainHandler);
    }

    private void setupDescText(String title, String desc) {
        LogRecordUtils.logd(TAG, ">> setupDescText");
        if (!StringUtils.isEmpty((CharSequence) title)) {
            title = title.trim();
            if (!StringUtils.equals(title, this.mTxtDescTitle.getText())) {
                this.mTxtDescTitle.setText(title);
            }
        }
        if (!StringUtils.isEmpty((CharSequence) desc)) {
            desc = desc.trim();
            if (!StringUtils.equals(desc, this.mTxtDescDetail.getText())) {
                setDescHeight(desc);
                this.mTxtDescDetail.setText(desc);
            }
        }
    }

    private void setDescHeight(String desc) {
        int len = StringUtils.getLength(desc);
        LogRecordUtils.logd(TAG, "setDescHeight(): len -> " + len);
        if (((float) len) >= 476.0f) {
            LayoutParams lp = (LayoutParams) this.mTxtDescDetail.getLayoutParams();
            lp.height = ResourceUtil.getDimen(R.dimen.dimen_492dp);
            this.mTxtDescDetail.setLayoutParams(lp);
            return;
        }
        int lineCount = (int) Math.ceil((double) (((float) len) / LINE_COUNT));
        LogRecordUtils.logd(TAG, "setDescHeight(): lineCount -> " + lineCount);
        lp = (LayoutParams) this.mTxtDescDetail.getLayoutParams();
        lp.height = (lineCount + 2) * ResourceUtil.getDimen(R.dimen.dimen_35dp);
        this.mTxtDescDetail.setLayoutParams(lp);
    }

    public void hide() {
        LogRecordUtils.logd(TAG, ">> hide(), mIsPanelShown=" + this.mIsPanelShown);
        if (this.mIsPanelShown) {
            this.mllDesc.setVisibility(8);
            this.mImgDescBg.setVisibility(8);
            this.mIsPanelShown = false;
        }
    }

    public boolean isShown() {
        return this.mIsPanelShown;
    }

    public void setOnFullDescClickedListener(OnFullDescClickedListener listener) {
        this.mFullDescClickedListener = listener;
    }
}
