package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.sdk.player.data.CarouselChannelDetail;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.views.MarqueeTextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import pl.droidsonroids.gif.GifImageView;

public class CarouselDetailListViewItem extends RelativeLayout {
    private String TAG;
    private TextView mChannelId;
    private TextView mChannelName;
    private MarqueeTextView mChannelTvName;
    private Context mContext;
    private boolean mIsSpread;
    private int mItemNormalBgResId;
    private int mPlaying;
    private GifImageView mPlayingGifView;
    private boolean mWasFocused;

    public CarouselDetailListViewItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mContext = context;
        initViews();
    }

    public CarouselDetailListViewItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initViews();
    }

    public CarouselDetailListViewItem(Context context) {
        super(context);
        this.mContext = context;
        initViews();
    }

    private void initUI() {
        this.mItemNormalBgResId = R.drawable.player_carousel_btn_transparent;
        this.mPlaying = R.drawable.share_detail_item_playing;
    }

    private void initViews() {
        this.TAG = "CarouselDetaiListViewItem@" + Integer.toHexString(hashCode());
        initUI();
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.player_carousel_listview, this);
        this.mChannelId = (TextView) view.findViewById(R.id.channel_item_id);
        this.mChannelName = (TextView) view.findViewById(R.id.channel_item_name);
        this.mChannelTvName = (MarqueeTextView) view.findViewById(R.id.channel_item_tvname);
        this.mPlayingGifView = new GifImageView(this.mContext);
        LayoutParams params = new LayoutParams(this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_24dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_24dp));
        params.topMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_13dp);
        addView(this.mPlayingGifView, params);
        this.mPlayingGifView.setFocusable(false);
        this.mPlayingGifView.setScaleType(ScaleType.FIT_XY);
        this.mPlayingGifView.setVisibility(8);
        setBackgroundResource(this.mItemNormalBgResId);
    }

    public TextView getChannelIdView() {
        return this.mChannelId;
    }

    public TextView getChannelNameView() {
        return this.mChannelName;
    }

    public MarqueeTextView getChannelTvNameView() {
        return this.mChannelTvName;
    }

    public void setChannelInfo(CarouselChannelDetail info) {
        this.mChannelTvName.setViewBound(this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_220dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_22dp));
        this.mChannelTvName.setText(info.getCurrentProgramName());
    }

    public void setChannelList(TVChannelCarousel channelCarousel) {
        CharSequence id = String.valueOf(channelCarousel.sid);
        String channelId = null;
        if (!StringUtils.isEmpty(id)) {
            if (id.length() == 1) {
                channelId = "0" + id;
            } else if (id.length() >= 3) {
                channelId = id.substring(0, 3);
            } else {
                CharSequence channelId2 = id;
            }
        }
        this.mChannelId.setText(channelId);
        this.mChannelName.setText(channelCarousel.name);
        adjustGifViewPosition();
    }

    public void clearItem() {
        Log.d(this.TAG, "clearItem()");
    }

    public void setPlaying(boolean isPlaying) {
        LogUtils.d(this.TAG, "setPlaying =" + isPlaying);
        if (isPlaying) {
            adjustGifViewPosition();
            this.mPlayingGifView.setImageResource(this.mPlaying);
            this.mPlayingGifView.setVisibility(0);
            return;
        }
        this.mPlayingGifView.setImageResource(0);
        this.mPlayingGifView.setVisibility(8);
    }

    private void adjustGifViewPosition() {
        LayoutParams params = (LayoutParams) this.mPlayingGifView.getLayoutParams();
        params.addRule(1, this.mChannelName.getId());
        params.leftMargin = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_5dp);
        this.mPlayingGifView.setLayoutParams(params);
    }
}
