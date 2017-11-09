package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.C2;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_C2;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.OnUserChannelChangeListener;
import com.gala.sdk.player.data.CarouselChannelDetail;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideoProvider.AllChannelCallback;
import com.gala.sdk.player.data.IVideoProvider.AllChannelDetailCallback;
import com.gala.sdk.player.data.IVideoProvider.ProgramListCallback;
import com.gala.sdk.player.ui.OnRequestChannelInfoListener;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.tvapi.vrs.model.ChannelCarousel;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.widget.views.MarqueeTextView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.List;

public class CarouselChannelListPanel {
    private static final int STATE_HIDEN = 4;
    private static final int STATE_LOADING = 1;
    private static final int STATE_NODATA = 3;
    private static final int STATE_PROGRAMME = 2;
    private static final String TAG_S = "Player/Ui/CarouselChannelListPanel";
    private String TAG = ("Player/Ui/CarouselChannelListPanel@" + Integer.toHexString(hashCode()));
    private long currentId = 0;
    private CarouselChannelAdapter mAdapter;
    private AllChannelCallback mAllChannelCallback;
    private AllChannelDetailCallback mAllChannelDetailCallback;
    private OnRequestChannelInfoListener mChannelInfoListener;
    private List<TVChannelCarousel> mChannelList;
    private PlayerListContent mContent;
    private Context mContext;
    private TVChannelCarousel mCurrentCarosel;
    private int mCurrentIndex = -1;
    private TVChannelCarouselTag mCurrentLabel;
    private TVChannelCarousel mFakeCarousel;
    private int mIndex = -1;
    private boolean mIsEnableShow = true;
    private boolean mIsFullScreen;
    private OnUserChannelChangeListener mListener;
    private onChannelChangeListener mOnChannelChangeListener;
    private PlayerListListener mOuterPlayerListListener;
    private IPingbackContext mPingbackContext;
    private PlayerListListener mPlayerListListener = new PlayerListListener() {
        public void onItemFocusChanged(ViewHolder holder, boolean isSelected, TVChannelCarouselTag label, int tag) {
            if (holder != null) {
                int position = holder.getLayoutPosition();
                CarouselDetailListViewItem itemView = holder.itemView;
                if (itemView != null) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(CarouselChannelListPanel.this.TAG, "onItemFocusChanged position = " + position + "/" + isSelected);
                    }
                    TextView channelName = itemView.getChannelNameView();
                    MarqueeTextView channelTvName = itemView.getChannelTvNameView();
                    TextView channelId = itemView.getChannelIdView();
                    int itemFocusedBgResId = R.drawable.player_carousel_btn_focus;
                    int itemNormalBgResId = R.drawable.player_carousel_btn_transparent;
                    int itemSpreadBgResId = R.drawable.player_carousel_list_spread;
                    int channelNormalColor = CarouselChannelListPanel.this.mContext.getResources().getColor(R.color.player_ui_text_color_default);
                    int tvNameColor = CarouselChannelListPanel.this.mContext.getResources().getColor(R.color.player_ui_carousel_item_tvname_normal);
                    int spreadColor = CarouselChannelListPanel.this.mContext.getResources().getColor(R.color.player_ui_carousel_item_channel_selected);
                    int focusColor = CarouselChannelListPanel.this.mContext.getResources().getColor(R.color.player_ui_text_color_focused);
                    if (isSelected) {
                        itemView.setBackgroundResource(itemFocusedBgResId);
                        channelTvName.setTextColor(focusColor);
                        channelName.setTextColor(focusColor);
                        channelId.setTextColor(focusColor);
                        channelTvName.start();
                    } else {
                        channelTvName.stop();
                        itemView.setBackgroundResource(itemNormalBgResId);
                        channelName.setTextColor(channelNormalColor);
                        channelId.setTextColor(channelNormalColor);
                        channelTvName.setTextColor(tvNameColor);
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(CarouselChannelListPanel.this.TAG, "onItemFocusChanged mSpreadPosition=" + CarouselChannelListPanel.this.mSpreadPosition);
                        }
                        if (CarouselChannelListPanel.this.mSpreadPosition > -1 && CarouselChannelListPanel.this.mSpreadPosition == position) {
                            itemView.setBackgroundResource(itemSpreadBgResId);
                            channelName.setTextColor(spreadColor);
                            channelId.setTextColor(spreadColor);
                            channelTvName.setTextColor(spreadColor);
                        }
                    }
                    AnimationUtil.zoomAnimation(itemView, isSelected, 1.05f, 200, false);
                }
            }
        }

        public void onItemClick(ViewHolder holder, int tag) {
            if (holder != null) {
                int position = holder.getLayoutPosition();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(CarouselChannelListPanel.this.TAG, "onItemClick position = " + position);
                }
                if (!ListUtils.isEmpty(CarouselChannelListPanel.this.mChannelList)) {
                    TVChannelCarousel channel = (TVChannelCarousel) CarouselChannelListPanel.this.mChannelList.get(position);
                    CarouselChannelListPanel.this.sendClickPingback(channel);
                    if (CarouselChannelListPanel.this.mOuterPlayerListListener != null) {
                        CarouselChannelListPanel.this.mOuterPlayerListListener.onItemClick(holder, 2);
                    }
                    CarouselChannelListPanel.this.mAdapter.setPlayingIndex(CarouselChannelListPanel.this.mCurrentIndex);
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(CarouselChannelListPanel.this.TAG, "onItemClick channel = " + channel);
                    }
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(CarouselChannelListPanel.this.TAG, "onItemClick mCurrentCarosel = " + CarouselChannelListPanel.this.mCurrentCarosel);
                    }
                    if (CarouselChannelListPanel.this.mCurrentCarosel == null || channel == null || CarouselChannelListPanel.this.mCurrentCarosel.id != channel.id) {
                        if (CarouselChannelListPanel.this.mListener != null) {
                            CarouselChannelListPanel.this.mListener.onChannelChange(channel);
                        }
                        CarouselChannelListPanel.this.mCurrentIndex = position;
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.d(CarouselChannelListPanel.this.TAG, "onItemClick same channel");
                    }
                    if (CarouselChannelListPanel.this.mOnChannelChangeListener != null) {
                        CarouselChannelListPanel.this.mOnChannelChangeListener.onChannelChange(channel, true);
                    }
                }
            }
        }

        public void onItemRecycled(ViewHolder holder) {
        }

        public void onListShow(TVChannelCarouselTag label, int tag, boolean isShow) {
        }
    };
    private ProgramListCallback mProgramListCallback;
    private View mRootView;
    private int mSpreadPosition = -1;
    private int mState;
    private IVideo mVideo;

    public CarouselChannelListPanel(View rootView) {
        this.mRootView = rootView;
        this.mContext = rootView.getContext();
        this.mPingbackContext = (IPingbackContext) this.mContext;
        CarouselPlayerDataProvider.init();
        initViews();
    }

    private void initViews() {
        this.mContent = (PlayerListContent) this.mRootView.findViewById(R.id.channel_content);
        this.mContent.initView();
        this.mContent.setListParams(this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_308dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_100dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_10dp), this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_700dp));
        this.mContent.setBackgroundColor(Color.parseColor("#e5121212"));
        this.mAdapter = new CarouselChannelAdapter(this.mContext);
        this.mContent.setListListener(this.mPlayerListListener);
    }

    public void show(TVChannelCarouselTag tvtag) {
        if (tvtag != null) {
            this.mCurrentLabel = tvtag;
            this.mChannelList = CarouselPlayerDataProvider.getInstance().getCarouselChannelList(tvtag);
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "show() tvtag=" + tvtag + ", mChannelList=" + (this.mChannelList == null ? -1 : this.mChannelList.size()));
            }
            if (ListUtils.isEmpty(this.mChannelList)) {
                this.mContent.showLoading();
                if (this.mChannelInfoListener != null) {
                    this.mChannelInfoListener.onRequestFullChannel(this.mCurrentLabel, this.mAllChannelCallback);
                    return;
                }
                return;
            }
            refreshPlayList(this.mChannelList, tvtag);
            this.mContent.showList(false);
            setPosition();
            sendShowPingback();
            requestChannelInfo();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "show() tag is null");
        }
    }

    private void refreshPlayList(List<TVChannelCarousel> allChannelList, TVChannelCarouselTag tvtag) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "refreshPlayList() size=" + allChannelList.size());
        }
        this.mAdapter.setAllChannelList(allChannelList);
        this.mContent.setAdapter(this.mAdapter);
        List detail = CarouselPlayerDataProvider.getInstance().getCarouselChannelProgramList(tvtag);
        if (!ListUtils.isEmpty(detail)) {
            updateProgramList(detail);
        }
    }

    private void setPosition() {
        if (this.mCurrentCarosel != null) {
            setCurrentPlayIndex(this.mCurrentCarosel.id, true);
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "show playingindex=" + getPlayIndex(this.mCurrentCarosel.id));
            }
        }
    }

    public void setFocus() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setFocusPosition()");
        }
        this.mContent.setFocusPosition(getPlayIndex(this.mCurrentCarosel.id));
        requestFocus();
    }

    private void requestChannelInfo() {
        if (this.mChannelInfoListener != null) {
            this.mChannelInfoListener.onRequestFullChannelDetail(this.mCurrentLabel, this.mAllChannelDetailCallback);
        }
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "hide()");
        }
        if (this.mContent != null) {
            this.mContent.hide();
        }
        this.mSpreadPosition = -1;
        this.mIndex = -1;
        this.mCurrentLabel = null;
    }

    public void setAllChannelList(List<TVChannelCarousel> allChannelList, TVChannelCarouselTag tag) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setAllChannelList() allChannelList=" + ListUtils.getCount((List) allChannelList) + ", tag=" + tag);
        }
        if (this.mCurrentLabel != tag) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "setAllChannelList() labels are not the same");
            }
        } else if (ListUtils.isEmpty((List) allChannelList)) {
            this.mContent.showError(this.mContext.getResources().getString(R.string.carousel_list_error));
        } else {
            if (ListUtils.isEmpty(CarouselPlayerDataProvider.getInstance().getCarouselChannelList(tag))) {
                this.mChannelList = allChannelList;
                refreshPlayList(allChannelList, tag);
                this.mContent.showList(false);
                setPosition();
                sendShowPingback();
                requestChannelInfo();
            }
            CarouselPlayerDataProvider.getInstance().updateCarouselChannel(allChannelList, tag);
        }
    }

    public void setCurrentPlayIndex(long tvId, boolean needFocus) {
        LogUtils.d(this.TAG, "setCurrentPlayIndex" + tvId + ", needFocus=" + needFocus);
        int index = -1;
        if (!ListUtils.isEmpty(this.mChannelList)) {
            int size = this.mChannelList.size();
            for (int i = 0; i < size; i++) {
                if (tvId == ((TVChannelCarousel) this.mChannelList.get(i)).id) {
                    index = i;
                    break;
                }
            }
        }
        LogUtils.d(this.TAG, "setCurrentPlayIndex index=" + index);
        this.mAdapter.setPlayingIndex(index);
    }

    private int getPlayIndex(long tvId) {
        int index = -1;
        if (!ListUtils.isEmpty(this.mChannelList)) {
            int size = this.mChannelList.size();
            for (int i = 0; i < size; i++) {
                if (tvId == ((TVChannelCarousel) this.mChannelList.get(i)).id) {
                    index = i;
                    break;
                }
            }
        }
        LogUtils.d(this.TAG, "getPlayIndex=" + tvId + ", index=" + index);
        return index;
    }

    public void setChannelProgramName(List<CarouselChannelDetail> list, TVChannelCarouselTag tag) {
        LogUtils.d(this.TAG, "setChannelProgramName list=" + list.size());
        if (this.mCurrentLabel == tag && !ListUtils.isEmpty((List) list)) {
            updateProgramList(list);
            CarouselPlayerDataProvider.getInstance().updateCarouselChannelProgram(list, tag);
        }
    }

    private void updateProgramList(List<CarouselChannelDetail> list) {
        LogUtils.d(this.TAG, "updateProgramList list=" + list.size());
        this.mAdapter.updateData(list);
        this.mAdapter.notifyDataSetUpdate();
    }

    public boolean hasFocus() {
        if (this.mContent != null) {
            return this.mContent.hasFocus();
        }
        return false;
    }

    public boolean isShow() {
        if (this.mContent != null) {
            return this.mContent.isShown();
        }
        return false;
    }

    public void setOnUserChannelChangeListener(OnUserChannelChangeListener listener) {
        this.mListener = listener;
    }

    public void setOnRequestChannelInfoListener(OnRequestChannelInfoListener listener) {
        this.mChannelInfoListener = listener;
    }

    public void dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "dispatchKeyEvent event=" + event);
        }
    }

    public boolean isEnableShow() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "isEnableShow()" + this.mIsEnableShow);
        }
        return this.mIsEnableShow;
    }

    public boolean isLoadingState() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "isLoadingState()" + this.mState);
        }
        if (this.mState == 1) {
            return true;
        }
        return false;
    }

    private void updateCurrentIndex(long currentId, int size) {
        for (int i = 0; i < size; i++) {
            if (currentId == ((TVChannelCarousel) this.mChannelList.get(i)).id) {
                this.mCurrentIndex = i;
                LogUtils.d(this.TAG, "notifyChannelInfoChange for currentIndex=" + this.mCurrentIndex);
                return;
            }
        }
    }

    public void setCurrentChannel(TVChannelCarousel channelCarousel) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setCurrentChannel channelCarousel=" + channelCarousel + ", mCurrentCarosel=" + this.mCurrentCarosel);
        }
        if (this.mCurrentCarosel != null && this.mCurrentCarosel == channelCarousel) {
            this.mIsEnableShow = true;
        }
        this.mCurrentCarosel = channelCarousel;
        this.mFakeCarousel = null;
    }

    public void setOnChannelChangeListener(onChannelChangeListener channelChangeListener) {
        this.mOnChannelChangeListener = channelChangeListener;
    }

    public void setCurrentChannelList(List<ChannelCarousel> list) {
    }

    public void setChannelChangeByIndex(int index) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setChannelChangeByIndex=" + index);
        }
        this.mIndex = index;
    }

    public TVChannelCarousel notifyRequestProgramme() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "notifyRequestProgramme()");
        }
        TVChannelCarousel channel = null;
        if (!(ListUtils.isEmpty(this.mChannelList) || this.mChannelInfoListener == null)) {
            this.mSpreadPosition = this.mContent.getFocusPosition();
            if (this.mSpreadPosition >= 0) {
                channel = (TVChannelCarousel) this.mChannelList.get(this.mSpreadPosition);
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "notifyRequestProgramme() channel=" + channel + ",mSpreadPosition=" + this.mSpreadPosition);
                }
                this.mChannelInfoListener.onRequestProgramList(channel, this.mProgramListCallback);
            }
        }
        return channel;
    }

    public void updateSpreadPosition(int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "updateSpreadPosition =" + position);
        }
        this.mSpreadPosition = -1;
    }

    public boolean requestFocus() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "requestFocus()");
        }
        if (this.mContent != null) {
            return this.mContent.requestFocus();
        }
        return false;
    }

    public void notifyUserClickProgramme() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "notifyUserClickProgramme()");
        }
        if (!ListUtils.isEmpty(this.mChannelList) && this.mSpreadPosition >= 0) {
            TVChannelCarousel channel = (TVChannelCarousel) this.mChannelList.get(this.mSpreadPosition);
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "onItemClick mCurrentCarosel.id = " + this.mCurrentCarosel.id + ",channel.id=" + channel.id);
            }
            if (this.mCurrentCarosel == null || channel == null || this.mCurrentCarosel.id != channel.id) {
                if (this.mListener != null) {
                    this.mListener.onChannelChange(channel);
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "notifyUserClickProgramme same channel");
            }
            if (this.mOnChannelChangeListener != null) {
                this.mOnChannelChangeListener.onChannelChange(channel, true);
            }
        }
    }

    public void switchScreen(boolean isFullScreen, float zoomRatio) {
        this.mIsFullScreen = isFullScreen;
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "setVideo()");
        }
        this.mVideo = video;
    }

    public void setPlayerListListener(PlayerListListener listener) {
        this.mOuterPlayerListListener = listener;
    }

    public void notifyChannelInfoChange(int chnChangeOffset, boolean needRequestAll, boolean realChannelChange) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "notifyChannelInfoChange chnChangeOffset=" + chnChangeOffset + ", needRequestAll=" + needRequestAll + ", realChannelChange=" + realChannelChange);
        }
        if (!ListUtils.isEmpty(this.mChannelList)) {
            if (needRequestAll) {
            }
            if (this.mFakeCarousel == null) {
                this.currentId = this.mCurrentCarosel.id;
            } else {
                this.currentId = this.mFakeCarousel.id;
            }
            int size = this.mChannelList.size();
            LogUtils.d(this.TAG, "notifyChannelInfoChange currentIndex=" + this.mCurrentIndex);
            switch (chnChangeOffset) {
                case -1:
                    this.mIsEnableShow = false;
                    updateCurrentIndex(this.currentId, size);
                    if (this.mCurrentIndex < size - 1) {
                        this.mCurrentIndex++;
                    } else {
                        this.mCurrentIndex = 0;
                    }
                    if (this.mCurrentIndex >= 0 && this.mOnChannelChangeListener != null) {
                        this.mOnChannelChangeListener.onChannelChange((TVChannelCarousel) this.mChannelList.get(this.mCurrentIndex), false);
                        break;
                    }
                case 0:
                    if (realChannelChange && this.mCurrentIndex >= 0) {
                        this.mCurrentCarosel = (TVChannelCarousel) this.mChannelList.get(this.mCurrentIndex);
                        if (this.mListener != null) {
                            this.mListener.onChannelChange((TVChannelCarousel) this.mChannelList.get(this.mCurrentIndex));
                        }
                        this.mAdapter.setPlayingIndex(this.mCurrentIndex);
                        this.mFakeCarousel = null;
                        break;
                    }
                case 1:
                    this.mIsEnableShow = false;
                    updateCurrentIndex(this.currentId, size);
                    if (this.mCurrentIndex > size - 1 || this.mCurrentIndex <= 0) {
                        this.mCurrentIndex = size - 1;
                    } else {
                        this.mCurrentIndex--;
                    }
                    if (this.mCurrentIndex >= 0 && this.mOnChannelChangeListener != null) {
                        this.mOnChannelChangeListener.onChannelChange((TVChannelCarousel) this.mChannelList.get(this.mCurrentIndex), false);
                        break;
                    }
                    break;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "notifyChannelInfoChange currentIndex=" + this.mCurrentIndex);
            }
            if (this.mCurrentIndex >= 0) {
                this.mFakeCarousel = (TVChannelCarousel) this.mChannelList.get(this.mCurrentIndex);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "notifyChannelInfoChange ChannelList is null");
        }
    }

    private void sendShowPingback() {
        String c1 = "101221";
        String c2 = "";
        String block = "";
        if (this.mCurrentLabel != null) {
            block = this.mCurrentLabel.name;
        }
        if (this.mCurrentCarosel != null) {
            c2 = String.valueOf(this.mCurrentCarosel.id);
        }
        if (this.mPingbackContext != null) {
            PingbackFactory.instance().createPingback(25).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(c1)).addItem(QTCURL.QTCURL_TYPE("chlist")).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE(block)).addItem(C2.C2_TYPE(c2)).addItem(NOW_C1.NOW_C1_TYPE(c1)).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendShowPingback mPingbackContext is null");
        }
    }

    private void sendClickPingback(TVChannelCarousel channel) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "sendClickPingback() channel=" + channel);
        }
        if (channel != null && this.mVideo != null) {
            String block = "";
            String c2 = "";
            if (this.mCurrentCarosel != null) {
                c2 = String.valueOf(this.mCurrentCarosel.id);
            }
            if (this.mCurrentLabel != null) {
                block = this.mCurrentLabel.name;
            }
            String c1 = "101221";
            PingbackFactory.instance().createPingback(23).addItem(BLOCK.BLOCK_TYPE(block)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(channel.name)).addItem(RPAGE.RPAGE_ID("chlist")).addItem(C1.C1_TYPE(c1)).addItem(C2.C2_TYPE(String.valueOf(channel.id))).addItem(NOW_C1.NOW_C1_TYPE(c1)).addItem(NOW_C2.NOW_C2_TYPE(c2)).post();
        }
    }

    public void setAllChannnelCallBack(AllChannelCallback callback) {
        this.mAllChannelCallback = callback;
    }

    public void setAllChannelDetailCallback(AllChannelDetailCallback callback) {
        this.mAllChannelDetailCallback = callback;
    }

    public void setProgramListCallback(ProgramListCallback callback) {
        this.mProgramListCallback = callback;
    }
}
