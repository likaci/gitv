package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.C2;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.QTCURLTYPE;
import com.gala.sdk.player.data.CarouselChannelDetail;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideoProvider.ChannelDetailCallback;
import com.gala.sdk.player.ui.OnRequestChannelInfoListener;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.views.AlwaysMarqueeTextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarouselChannelInfoOverlay {
    private static final String BLOCK_CAROUSEL_INFO = "infopanel";
    private static final String TAG = "Player/Ui/CarouselChannelInfoOverlay";
    private ChannelDetailCallback mCallBack = new ChannelDetailCallback() {
        public void onCacheReady(CarouselChannelDetail detail) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(CarouselChannelInfoOverlay.TAG, "ChannelDetailCallback onCacheReady =" + detail);
            }
            CarouselChannelInfoOverlay.this.setCurrentChannelDetail(detail);
        }

        public void onDataReady(CarouselChannelDetail detail) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(CarouselChannelInfoOverlay.TAG, "ChannelDetailCallback onDataReady =" + detail);
            }
            CarouselChannelInfoOverlay.this.setCurrentChannelDetail(detail);
        }

        public void onException(IVideo arg0, String arg1, String arg2) {
        }
    };
    private TVChannelCarousel mChannelCarousel;
    private TextView mChannelId;
    private LinearLayout mChannelInfoContainer;
    private TextView mChannelName;
    private Context mContext;
    private TextView mHelpDown;
    private TextView mHelpUp;
    private ImageView mImgHelpdown;
    private ImageView mImgHelpup;
    private OnRequestChannelInfoListener mListener;
    private IPingbackContext mPingbackContext;
    private AlwaysMarqueeTextView mPlayingName;
    private TextView mPlayingTime;
    private View mRoot;
    private IVideo mVideo;

    public CarouselChannelInfoOverlay(View root) {
        this.mContext = root.getContext();
        this.mPingbackContext = (IPingbackContext) this.mContext;
        this.mRoot = root;
        initView();
    }

    public void initView() {
        this.mChannelInfoContainer = (LinearLayout) this.mRoot.findViewById(R.id.channel_info_container);
        this.mChannelId = (TextView) this.mRoot.findViewById(R.id.channel_id);
        this.mChannelName = (TextView) this.mRoot.findViewById(R.id.channel_name);
        this.mPlayingName = (AlwaysMarqueeTextView) this.mRoot.findViewById(R.id.playing_name);
        this.mPlayingTime = (TextView) this.mRoot.findViewById(R.id.playing_time);
        this.mHelpUp = (TextView) this.mRoot.findViewById(R.id.channel_info_help_up);
        this.mHelpDown = (TextView) this.mRoot.findViewById(R.id.channel_info_help_down);
        this.mImgHelpup = (ImageView) this.mRoot.findViewById(R.id.img_help_up);
        this.mImgHelpdown = (ImageView) this.mRoot.findViewById(R.id.img_help_down);
        showHelpTip();
    }

    private void showHelpTip() {
        int txtUpId = R.string.channel_list;
        int drawableUpId = R.drawable.player_carousel_ok;
        int txtDownId = R.string.change_channel;
        int drawableDownId = R.drawable.player_carousel_updown;
        String txtUp = this.mContext.getResources().getString(txtUpId);
        String txtDown = this.mContext.getResources().getString(txtDownId);
        this.mHelpUp.setText(txtUp);
        this.mHelpDown.setText(txtDown);
        this.mImgHelpup.setImageResource(drawableUpId);
        this.mImgHelpdown.setImageResource(drawableDownId);
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "CarouselChannelInfoOverlay hide");
        }
        this.mPlayingName.setText(null);
        this.mPlayingTime.setText(null);
        this.mChannelInfoContainer.setVisibility(8);
    }

    public boolean isShow() {
        return this.mChannelInfoContainer.isShown();
    }

    public void setOnRequestChannelInfoListener(OnRequestChannelInfoListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setOnRequestChannelInfoListener listener=" + listener);
        }
        this.mListener = listener;
    }

    public void setCurrentChannelDetail(CarouselChannelDetail currentInfo) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setCurrentChannelDetail channelCarousel=" + currentInfo);
        }
        if (currentInfo != null) {
            CharSequence currentName = currentInfo.getCurrentProgramName();
            String nextName = currentInfo.getNextProgramName();
            StringBuilder currentTime = formatTime(currentInfo.getCurrentProgramStartTime(), currentInfo.getCurrentProgramEndTime());
            if (!(currentTime == null || StringUtils.isEmpty(currentName))) {
                this.mPlayingTime.setText(currentTime);
                this.mPlayingName.setText(currentName);
                this.mPlayingName.setVisibility(0);
                this.mPlayingTime.setVisibility(0);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "setCurrentChannelDetail currentName=" + currentName + ", nextName=" + nextName + ", currentTime=" + currentTime);
            }
        }
    }

    private StringBuilder formatTime(String startTime, String endTime) {
        String sTime = formatTime(startTime);
        String eTime = formatTime(endTime);
        if (sTime == null || eTime == null) {
            return null;
        }
        return new StringBuilder().append(sTime).append(" - ").append(eTime);
    }

    private String formatTime(String time) {
        if (StringUtils.isEmpty((CharSequence) time)) {
            return null;
        }
        return new SimpleDateFormat("HH:mm").format(new Date(Long.parseLong(time)));
    }

    public void setCurrentChannel(TVChannelCarousel channelCarousel) {
        this.mChannelCarousel = channelCarousel;
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setCurrentChannel channelCarousel=" + channelCarousel);
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
            this.mChannelName.setText(name);
            this.mChannelId.setText(channelId);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "setCurrentChannel name=" + name + ", id=" + id);
            }
        }
    }

    public void updateChannel(TVChannelCarousel channelCarousel, boolean needRequest, CarouselChannelDetail currentCarouselDetail) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "show() updateChannel=" + channelCarousel);
        }
        if (channelCarousel != null) {
            String name = channelCarousel.name;
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
            this.mChannelName.setText(name);
            this.mPlayingName.setText(null);
            this.mPlayingTime.setText(null);
            show(channelCarousel, needRequest, currentCarouselDetail);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "updateChannel name=" + name + ", id=" + id);
            }
        }
    }

    private void show(TVChannelCarousel channelCarousel, boolean needRequest, CarouselChannelDetail currentCarouselDetail) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "showUnchangeTip() mChannelCarousel=" + channelCarousel + ", currentCarouselDetail=" + currentCarouselDetail);
        }
        if (this.mChannelInfoContainer.getVisibility() != 0) {
            sendShowPingback();
        }
        this.mChannelInfoContainer.setVisibility(0);
        if (currentCarouselDetail != null) {
            setCurrentChannelDetail(currentCarouselDetail);
        }
        if (needRequest && this.mListener != null) {
            this.mListener.onRequestChannelDetail(channelCarousel, this.mCallBack);
        }
    }

    private void sendShowPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "sendShowPingback()");
        }
        String C1 = "101221";
        String C2 = "";
        if (this.mChannelCarousel != null) {
            C2 = String.valueOf(this.mChannelCarousel.id);
        }
        if (this.mPingbackContext != null) {
            PingbackFactory.instance().createPingback(26).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(C1)).addItem(QTCURLTYPE.PLAYER).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE(BLOCK_CAROUSEL_INFO)).addItem(C2.C2_TYPE(C2)).addItem(NOW_C1.NOW_C1_TYPE(C1)).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "sendShowPingback() mPingbackContext is null");
        }
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "setVideo() c2=" + video.getAlbum().live_channelId);
        }
        this.mVideo = video;
    }
}
