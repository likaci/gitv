package com.gala.video.widget;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.gala.video.widget.util.LogUtils;

public class ItemPopupWindow {
    private static /* synthetic */ int[] $SWITCH_TABLE$com$gala$video$widget$ItemPopupWindow$HorizontalPosition;
    private final int MAX_NUM = 9;
    private float MAX_WIDTH;
    private final String TAG = "ItemPopupWindow";
    private Context mActivity;
    private int mMaxTextNum = 9;
    private Paint mPaint;
    private PopupWindow mPopupWindow;
    private int mTipsBgResId = Integer.MIN_VALUE;
    private int mTipsTextColor = -16777216;
    private int mTipsTextSize;
    private TextView mTxtTips;
    private int needHeight;
    private int needWidth;

    public static class HintWindowStyle {
        private int mTextBgResId;
        private int mTextSizeResId;

        public HintWindowStyle(int textSizeResId, int textBgResId) {
            this.mTextSizeResId = textSizeResId;
            this.mTextBgResId = textBgResId;
        }

        public int getTextSizeResId() {
            return this.mTextSizeResId;
        }

        public int getTextBgResId() {
            return this.mTextBgResId;
        }
    }

    public enum HorizontalPosition {
        LEFT,
        RIGHT,
        CENTER
    }

    public static class ItemHint {
        private String mHintContent;

        public ItemHint(String itemHint) {
            this.mHintContent = itemHint;
        }

        public String getHintContent() {
            return this.mHintContent;
        }
    }

    public enum VerticalPosition {
        DROPDOWN,
        DROPUP
    }

    static /* synthetic */ int[] $SWITCH_TABLE$com$gala$video$widget$ItemPopupWindow$HorizontalPosition() {
        int[] iArr = $SWITCH_TABLE$com$gala$video$widget$ItemPopupWindow$HorizontalPosition;
        if (iArr == null) {
            iArr = new int[HorizontalPosition.values().length];
            try {
                iArr[HorizontalPosition.CENTER.ordinal()] = 3;
            } catch (NoSuchFieldError e) {
            }
            try {
                iArr[HorizontalPosition.LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                iArr[HorizontalPosition.RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            $SWITCH_TABLE$com$gala$video$widget$ItemPopupWindow$HorizontalPosition = iArr;
        }
        return iArr;
    }

    public ItemPopupWindow(Context mActivity, float textSize, int bgResId) {
        this.mActivity = mActivity;
        this.mTipsBgResId = bgResId;
        this.mTipsTextSize = (int) textSize;
        initView();
    }

    public ItemPopupWindow(Context mActivity, float textSize, int bgResId, int textColor, int maxTextNum) {
        this.mActivity = mActivity;
        this.mTipsBgResId = bgResId;
        this.mTipsTextSize = (int) textSize;
        this.mTipsTextColor = textColor;
        this.mMaxTextNum = maxTextNum;
        initView();
    }

    private void initView() {
        this.mTxtTips = new TextView(this.mActivity);
        this.mTxtTips.setSelected(true);
        this.mTxtTips.setTextSize(0, (float) this.mTipsTextSize);
        this.mTxtTips.setTextColor(this.mTipsTextColor);
        this.mTxtTips.setIncludeFontPadding(false);
        this.mTxtTips.setSingleLine();
        this.mTxtTips.setMarqueeRepeatLimit(-1);
        this.mTxtTips.setEllipsize(TruncateAt.MARQUEE);
        this.mTxtTips.setBackgroundResource(this.mTipsBgResId);
        this.mTxtTips.setGravity(3);
        this.needHeight = (this.mTipsTextSize + this.mTxtTips.getCompoundPaddingTop()) + this.mTxtTips.getCompoundPaddingBottom();
        this.MAX_WIDTH = (float) (this.mTipsTextSize * this.mMaxTextNum);
        this.mPaint = this.mTxtTips.getPaint();
        this.mTxtTips.setMaxWidth((int) ((this.MAX_WIDTH + ((float) this.mTxtTips.getPaddingLeft())) + ((float) this.mTxtTips.getPaddingRight())));
        this.mPopupWindow = new PopupWindow(this.mTxtTips, -2, -2);
        this.mPopupWindow.setAnimationStyle(-1);
    }

    public void dismiss() {
        if (this.mPopupWindow.isShowing()) {
            this.mPopupWindow.dismiss();
        }
    }

    public void show(View anchorView, String content, VerticalPosition verticalPos) {
        if (TextUtils.isEmpty(content)) {
            LogUtils.e("ItemPopupWindow", "content is empty");
            dismiss();
            return;
        }
        adjustPopupWindowParams(content);
        if (verticalPos == VerticalPosition.DROPUP) {
            showDropUpPopWindow(anchorView, HorizontalPosition.CENTER);
        } else {
            showDropDownPopWindow(anchorView, HorizontalPosition.CENTER);
        }
        LogUtils.d("ItemPopupWindow", "  view.width=" + anchorView.getWidth() + "  view.getHeight()=" + anchorView.getHeight() + "  needWidth=" + this.needWidth + "  needHeight=" + this.needHeight);
    }

    public void show(View anchorView, String content, int offsetY) {
        if (TextUtils.isEmpty(content)) {
            LogUtils.e("ItemPopupWindow", "content is empty");
            dismiss();
            return;
        }
        adjustPopupWindowParams(content);
        int offsetX = getPopupWindowOffsetX(anchorView, HorizontalPosition.CENTER);
        if (this.mPopupWindow.isShowing()) {
            this.mPopupWindow.update(anchorView, offsetX, offsetY - anchorView.getHeight(), this.needWidth, -1);
        } else {
            this.mPopupWindow.showAsDropDown(anchorView, offsetX, offsetY - anchorView.getHeight());
            this.mPopupWindow.update(anchorView, offsetX, offsetY - anchorView.getHeight(), this.needWidth, -1);
        }
        LogUtils.d("ItemPopupWindow", "  view.width=" + anchorView.getWidth() + "  view.getHeight()=" + anchorView.getHeight() + "  needWidth=" + this.needWidth + "  needHeight=" + this.needHeight);
    }

    public void show(View anchorView, String content, VerticalPosition verticalPos, HorizontalPosition horizontalPos) {
        if (TextUtils.isEmpty(content)) {
            LogUtils.e("ItemPopupWindow", "content is empty");
            dismiss();
            return;
        }
        adjustPopupWindowParams(content);
        if (verticalPos == VerticalPosition.DROPUP) {
            showDropUpPopWindow(anchorView, horizontalPos);
        } else {
            showDropDownPopWindow(anchorView, horizontalPos);
        }
        LogUtils.d("ItemPopupWindow", "  view.width=" + anchorView.getWidth() + "  view.getHeight()=" + anchorView.getHeight() + "  needWidth=" + this.needWidth + "  needHeight=" + this.needHeight);
    }

    public void show(View anchorView, String content, int offsetX, int offsetY) {
        if (TextUtils.isEmpty(content)) {
            LogUtils.e("ItemPopupWindow", "content is empty");
            dismiss();
            return;
        }
        adjustPopupWindowParams(content);
        if (this.mPopupWindow.isShowing()) {
            this.mPopupWindow.update(anchorView, offsetX, offsetY - anchorView.getHeight(), this.needWidth, -1);
        } else {
            this.mPopupWindow.showAsDropDown(anchorView, offsetX, offsetY - anchorView.getHeight());
            this.mPopupWindow.update(anchorView, offsetX, offsetY - anchorView.getHeight(), this.needWidth, -1);
        }
        LogUtils.d("ItemPopupWindow", "  view.width=" + anchorView.getWidth() + "  view.getHeight()=" + anchorView.getHeight() + "  needWidth=" + this.needWidth + "  needHeight=" + this.needHeight + "  offsetX=" + offsetX + "  offsetY=" + offsetY);
    }

    private void showDropUpPopWindow(View view, HorizontalPosition horizontalPos) {
        int offsetX = getPopupWindowOffsetX(view, horizontalPos);
        if (this.mPopupWindow.isShowing()) {
            this.mPopupWindow.update(view, offsetX, -(view.getHeight() + this.needHeight), -1, -1);
            return;
        }
        this.mPopupWindow.showAsDropDown(view, offsetX, -(view.getHeight() + this.needHeight));
    }

    private void showDropDownPopWindow(View view, HorizontalPosition horizontalPos) {
        int offsetX = getPopupWindowOffsetX(view, horizontalPos);
        if (this.mPopupWindow.isShowing()) {
            this.mPopupWindow.update(view, offsetX, 0, this.needWidth, -1);
            return;
        }
        this.mPopupWindow.showAsDropDown(view, offsetX, 0);
    }

    private void adjustPopupWindowParams(String content) {
        this.needWidth = (int) this.mPaint.measureText(content);
        LogUtils.d("ItemPopupWindow", "measureText" + content + ";width=" + this.needWidth + ";TextSize" + this.mTipsTextSize + ";mTipsTextSize" + this.mTipsTextSize);
        if (((float) this.needWidth) > this.MAX_WIDTH) {
            this.needWidth = (int) this.MAX_WIDTH;
        }
        this.mTxtTips.setText(content);
        this.needWidth += this.mTxtTips.getPaddingLeft() + this.mTxtTips.getPaddingRight();
    }

    private int getPopupWindowOffsetX(View view, HorizontalPosition horizontalPos) {
        switch ($SWITCH_TABLE$com$gala$video$widget$ItemPopupWindow$HorizontalPosition()[horizontalPos.ordinal()]) {
            case 1:
                return 0;
            case 2:
                return view.getWidth() - this.needWidth;
            case 3:
                return (view.getWidth() / 2) - (this.needWidth / 2);
            default:
                return -1;
        }
    }
}
