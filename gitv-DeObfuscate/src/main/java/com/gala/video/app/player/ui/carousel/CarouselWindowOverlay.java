package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.video.app.player.C1291R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class CarouselWindowOverlay {
    private static final String TAG = "Player/Ui/CarouselWindowOverlay";
    private RelativeLayout mBottom;
    private TVChannelCarousel mChannelCarousel;
    private Context mContext;
    private TextView mId;
    private TextView mName;
    private ImageView mTipImg;

    public CarouselWindowOverlay(View root) {
        this.mContext = root.getContext();
        initView(root);
    }

    private void initView(View root) {
        this.mBottom = (RelativeLayout) root.findViewById(C1291R.id.win_bottom);
        this.mId = (TextView) root.findViewById(C1291R.id.window_id);
        this.mName = (TextView) root.findViewById(C1291R.id.window_name);
        this.mTipImg = (ImageView) root.findViewById(C1291R.id.carousel_window_tip);
    }

    private void updateInfo(TVChannelCarousel channelCarousel) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateInfo()");
        }
        if (channelCarousel != null) {
            String name = channelCarousel.name;
            CharSequence id = String.valueOf(channelCarousel.sid);
            String channelId = "";
            if (!StringUtils.isEmpty(id)) {
                if (id.length() == 1) {
                    channelId = "0" + id;
                } else if (id.length() >= 3) {
                    channelId = id.substring(0, 3);
                } else {
                    CharSequence channelId2 = id;
                }
            }
            this.mName.setText(name);
            this.mId.setText(channelId);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "updateInfo() name=" + name + ", id=" + id);
            }
        }
    }

    private void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "show()");
        }
        if (this.mChannelCarousel != null) {
            updateInfo(this.mChannelCarousel);
        }
        this.mBottom.setVisibility(0);
    }

    private void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "hide()");
        }
        this.mBottom.setVisibility(8);
        this.mId.setText(null);
        this.mName.setText(null);
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        if (isFullScreen) {
            hide();
        } else {
            show();
        }
    }

    public void setCurrentChannel(TVChannelCarousel channelCarousel) {
        this.mChannelCarousel = channelCarousel;
        updateInfo(channelCarousel);
    }
}
