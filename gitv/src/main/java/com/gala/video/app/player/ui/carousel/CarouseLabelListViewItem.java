package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.app.player.R;

public class CarouseLabelListViewItem extends RelativeLayout {
    protected String TAG;
    private Context mContext;
    private int mItemNormalBgResId;
    private TextView mLabel;

    public CarouseLabelListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initViews();
    }

    public CarouseLabelListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }

    public CarouseLabelListViewItem(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    private void initUI() {
        this.mItemNormalBgResId = R.drawable.player_carousel_btn_transparent;
    }

    private void initViews() {
        this.TAG = "CarouseLabelListViewItem@" + Integer.toHexString(hashCode());
        initUI();
        this.mLabel = new TextView(this.mContext);
        this.mLabel.setTextSize(0, (float) this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_30dp));
        LayoutParams params = new LayoutParams(-2, -2);
        params.leftMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_27dp);
        params.addRule(15);
        addView(this.mLabel, params);
        this.mLabel.setMaxWidth(this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_120dp));
        this.mLabel.setEllipsize(TruncateAt.END);
        this.mLabel.setSingleLine(true);
        this.mLabel.setIncludeFontPadding(false);
        this.mLabel.setTextColor(this.mContext.getResources().getColor(R.color.player_ui_text_color_default));
        setBackgroundResource(this.mItemNormalBgResId);
    }

    public void setLabelInfo(TVChannelCarouselTag label) {
        if (this.mLabel != null) {
            this.mLabel.setText(label.name);
        }
    }

    public TextView getLabelView() {
        return this.mLabel;
    }

    public void setSelectedColor() {
        if (this.mLabel != null) {
            this.mLabel.setTextColor(this.mContext.getResources().getColor(R.color.player_ui_carousel_item_channel_selected));
            setBackgroundResource(R.drawable.player_carousel_label_spread);
        }
    }
}
