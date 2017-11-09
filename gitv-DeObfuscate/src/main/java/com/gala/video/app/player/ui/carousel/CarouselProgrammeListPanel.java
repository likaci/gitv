package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C0165R;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.C2;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_C2;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.OnUserVideoChangeListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.type.AlbumType;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.ListView;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.widget.views.MarqueeTextView;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.ProgressBarNewItem;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.List;

public class CarouselProgrammeListPanel implements OnItemRecycledListener, OnItemClickListener, OnItemFocusChangedListener {
    private static final String BLOCK_CAROUSELLIST = "carousellist";
    private static final int IS_LIVE = 1;
    private static final String TAG_S = "Player/Ui/CarouselProgrammeListPanel";
    private String TAG = ("Player/Ui/CarouselProgrammeListPanel@" + Integer.toHexString(hashCode()));
    private Context mContext;
    private IVideo mCurrentVideo;
    private List<IVideo> mList;
    private PlayerListListener mListListener;
    private ListView mListViewCarousel;
    private IPingbackContext mPingbackContext;
    private ProgressBarNewItem mProgessbar;
    private FrameLayout mProgrammePanel;
    private View mRootView;
    private TVChannelCarousel mSpreadChannel;
    private TextView mTxt;
    private OnUserVideoChangeListener mVideoChangeListener;

    protected enum ProgrammeViewMode {
        MODE_NONE,
        MODE_PROGRAMME,
        MODE_PROGRAMME_NODATA,
        MODE_LOADING
    }

    public CarouselProgrammeListPanel(View rootView) {
        this.mRootView = rootView;
        this.mContext = rootView.getContext();
        this.mPingbackContext = (IPingbackContext) this.mContext;
        initViews();
    }

    private void initViews() {
        this.mListViewCarousel = (ListView) this.mRootView.findViewById(C1291R.id.programm_list);
        this.mTxt = (TextView) this.mRootView.findViewById(C1291R.id.carousel_txt);
        this.mProgessbar = (ProgressBarNewItem) this.mRootView.findViewById(C1291R.id.carousel_prograssbar);
        this.mProgrammePanel = (FrameLayout) this.mRootView.findViewById(C1291R.id.carousel_programme_panel);
        initListView();
        updateFocusPath();
    }

    private void initListView() {
        this.mListViewCarousel.setVerticalScrollBarEnabled(false);
        this.mListViewCarousel.setOverScrollMode(2);
        this.mListViewCarousel.setVisibility(8);
        this.mListViewCarousel.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mListViewCarousel.setFocusMode(1);
        this.mListViewCarousel.setScrollRoteScale(1.0f, 1.0f, 2.0f);
        this.mListViewCarousel.setContentHeight(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_100dp));
        this.mListViewCarousel.setContentWidth(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_308dp));
        this.mListViewCarousel.setOnItemFocusChangedListener(this);
        this.mListViewCarousel.setOnItemRecycledListener(this);
        this.mListViewCarousel.setOnItemClickListener(this);
        this.mListViewCarousel.setFocusLeaveForbidden(Opcodes.IF_ICMPGT);
    }

    public void showProgrameList(TVChannelCarousel channel) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "showProgrameList()");
        }
        this.mSpreadChannel = channel;
        changeMode(ProgrammeViewMode.MODE_LOADING);
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "hide()");
        }
        if (this.mProgrammePanel.isShown()) {
            if (this.mListListener != null) {
                this.mListListener.onListShow(null, 3, false);
            }
            this.mListViewCarousel.setVisibility(8);
            this.mProgessbar.setVisibility(8);
            this.mTxt.setVisibility(8);
            this.mProgrammePanel.setVisibility(8);
            this.mSpreadChannel = null;
        }
    }

    private void setPlayList(List<IVideo> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setPlayList size=" + list.size());
        }
        this.mListViewCarousel.setAdapter(new CarouselProgrammAdapter(list, this.mContext));
    }

    private void notifyChannelChange(Album album) {
        CharSequence startTime = album.sliveTime;
        CharSequence endTime = album.eliveTime;
        long currentTime = DeviceUtils.getServerTimeMillis();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyChannelChange() live=" + album.isLive + ", startTime=" + startTime + ", endTime=" + endTime + ", currentTime=" + currentTime);
        }
        if (!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && currentTime >= Long.parseLong(startTime) && currentTime <= Long.parseLong(endTime) && this.mListListener != null) {
            this.mListListener.onItemClick(null, 3);
        }
    }

    private void notifyVideoChange(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyVwwideoChange(" + video + ") videoChangeListener: " + this.mVideoChangeListener);
        }
        if (this.mVideoChangeListener != null) {
            this.mVideoChangeListener.onVideoChange(this.mRootView, video);
        }
    }

    private void updateFocusPath() {
        LogUtils.m1568d(this.TAG, "updateTabFocusPath()");
        this.mListViewCarousel.setNextFocusRightId(this.mListViewCarousel.getId());
    }

    public void clearItem() {
        LogUtils.m1568d(this.TAG, "clearItem()");
    }

    public void onKeyDown() {
    }

    public Album getFocusedAlbum() {
        return null;
    }

    public void dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "dispatchKeyEvent event=" + event);
        }
        this.mListViewCarousel.dispatchKeyEvent(event);
    }

    public void notifyDataFilled() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyDataFilled mCurrentVideo=" + this.mCurrentVideo);
        }
        if (this.mCurrentVideo != null) {
            List list = this.mCurrentVideo.getCarouseProgramList();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "notifyDataFilled list=" + this.mList);
            }
            if (ListUtils.isEmpty(list)) {
                changeMode(ProgrammeViewMode.MODE_PROGRAMME_NODATA);
                return;
            }
            this.mList = list;
            updateProgramme(list);
        }
    }

    private void updateProgramme(List<IVideo> list) {
        setPlayList(list);
        changeMode(ProgrammeViewMode.MODE_PROGRAMME);
    }

    private void changeMode(ProgrammeViewMode mode) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "changeMode mode=" + mode);
        }
        switch (mode) {
            case MODE_PROGRAMME_NODATA:
                this.mListViewCarousel.setVisibility(8);
                this.mProgessbar.setVisibility(8);
                if (this.mProgrammePanel.isShown()) {
                    this.mTxt.setText(this.mContext.getResources().getString(C1291R.string.carousel_list_error));
                    this.mTxt.setVisibility(0);
                    return;
                }
                return;
            case MODE_PROGRAMME:
                this.mTxt.setVisibility(8);
                if (this.mProgrammePanel.isShown()) {
                    this.mProgessbar.setVisibility(8);
                    this.mProgrammePanel.setVisibility(0);
                    this.mListViewCarousel.setVisibility(0);
                    this.mListViewCarousel.requestFocus();
                    this.mListViewCarousel.setFocusPosition(getPlayIndex());
                    sendShowPingback();
                    return;
                }
                return;
            case MODE_LOADING:
                this.mProgessbar.setVisibility(0);
                this.mTxt.setVisibility(8);
                this.mListViewCarousel.setVisibility(8);
                this.mProgrammePanel.setVisibility(0);
                return;
            default:
                return;
        }
    }

    private void sendShowPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "sendShowPingback()");
        }
        String C1 = "101221";
        if (this.mPingbackContext != null) {
            String C2 = "";
            if (this.mSpreadChannel != null) {
                C2 = String.valueOf(this.mSpreadChannel.id);
            }
            PingbackFactory.instance().createPingback(24).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(C1)).addItem(QTCURL.QTCURL_TYPE(BLOCK_CAROUSELLIST)).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE(BLOCK_CAROUSELLIST)).addItem(C2.C2_TYPE(C2)).addItem(NOW_C1.NOW_C1_TYPE(C1)).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "mPingbackContext is null");
        }
    }

    private int getPlayIndex() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "==>> getPlayIndex()");
        }
        int index = -1;
        CharSequence currentId = null;
        if (!(this.mCurrentVideo == null || this.mCurrentVideo.getCurrentCarouselProgram() == null)) {
            currentId = this.mCurrentVideo.getCurrentCarouselProgram().program_id;
        }
        if (!ListUtils.isEmpty(this.mList)) {
            int size = this.mList.size();
            for (int i = 0; i < size; i++) {
                CharSequence id = ((IVideo) this.mList.get(i)).getAlbum().program_id;
                if (!StringUtils.isEmpty(currentId) && !StringUtils.isEmpty(id) && currentId.equals(id)) {
                    index = i;
                    break;
                }
            }
        }
        LogUtils.m1568d(this.TAG, "<<== getPlayIndex=" + currentId + ", index=" + index);
        return index;
    }

    public boolean isShow() {
        return this.mProgrammePanel.isShown();
    }

    public boolean isListViewShow() {
        return this.mListViewCarousel.isShown();
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "setVideo()=" + video);
        }
        this.mCurrentVideo = video;
    }

    public void OnUserVideoChangeListener(OnUserVideoChangeListener videoChangeListener) {
        this.mVideoChangeListener = videoChangeListener;
    }

    public void setPlayerListListener(PlayerListListener listener) {
        this.mListListener = listener;
    }

    private void sendClickPingback(List<IVideo> list, int position) {
        if (this.mCurrentVideo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(this.TAG, "sendClickPingback: null == curVideo");
            }
        } else if (!ListUtils.isEmpty((List) list)) {
            int size = list.size();
            int curPlayIndex = 0;
            String now_c1 = "101221";
            Album clickedAlbum = null;
            if (position >= 0 && position < size) {
                clickedAlbum = ((IVideo) list.get(position)).getAlbum();
            }
            if (clickedAlbum != null) {
                String rId;
                if (clickedAlbum.getType() == AlbumType.ALBUM) {
                    rId = clickedAlbum.qpId;
                } else {
                    rId = clickedAlbum.tvQid;
                }
                for (int i = 0; i < size; i++) {
                    IVideo item = (IVideo) list.get(i);
                    if (this.mCurrentVideo.getCurrentCarouselProgram() != null && StringUtils.equals(item.getAlbum().program_id, this.mCurrentVideo.getCurrentCarouselProgram().program_id)) {
                        curPlayIndex = i;
                        break;
                    }
                }
                String now_c2 = "";
                String c2 = "";
                if (this.mSpreadChannel != null) {
                    c2 = String.valueOf(this.mSpreadChannel.id);
                }
                if (this.mCurrentVideo.getSourceType() == SourceType.CAROUSEL) {
                    now_c2 = this.mCurrentVideo.getAlbum().live_channelId;
                }
                PingbackFactory.instance().createPingback(22).addItem(C0165R.R_TYPE("")).addItem(BLOCK.BLOCK_TYPE(BLOCK_CAROUSELLIST)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(position - curPlayIndex))).addItem(RPAGE.RPAGE_ID(BLOCK_CAROUSELLIST)).addItem(C1.C1_TYPE(String.valueOf(clickedAlbum.chnId))).addItem(C2.C2_TYPE(c2)).addItem(NOW_C1.NOW_C1_TYPE(now_c1)).addItem(NOW_C2.NOW_C2_TYPE(now_c2)).post();
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(this.TAG, "sendClickPingback: associatives is empty");
        }
    }

    public void onItemFocusChanged(ViewGroup arg0, ViewHolder holder, boolean isSelected) {
        if (holder != null) {
            int position = holder.getLayoutPosition();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "onItemFocusChanged position = " + position + "/" + isSelected);
            }
            CarouseProgrammeListViewItem itemView = holder.itemView;
            if (itemView != null) {
                MarqueeTextView programmeName = itemView.getProgramName();
                TextView programmeTime = itemView.getProgramTime();
                int itemFocusedBgResId = C1291R.drawable.player_carousel_btn_focus;
                int itemNormalBgResId = C1291R.drawable.player_carousel_btn_transparent;
                if (isSelected) {
                    itemView.setBackgroundResource(itemFocusedBgResId);
                    programmeName.setTextColor(Color.parseColor("#FFF1F1F1"));
                    programmeTime.setTextColor(Color.parseColor("#FFF1F1F1"));
                    programmeName.start();
                } else {
                    itemView.setBackgroundResource(itemNormalBgResId);
                    programmeName.setTextColor(Color.parseColor("#b2b2b2"));
                    programmeTime.setTextColor(Color.parseColor("#777777"));
                    programmeName.stop();
                }
                if (!ListUtils.isEmpty(this.mList) && position < this.mList.size()) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(this.TAG, "setGrayColor1()");
                    }
                    IVideo video = (IVideo) this.mList.get(position);
                    if (video != null) {
                        Album album = video.getAlbum();
                        if (album.isLive == 1) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d(this.TAG, "setGrayColor2()");
                            }
                            long currentTime = DeviceUtils.getServerTimeMillis();
                            if (!StringUtils.isEmpty(album.eliveTime)) {
                                long endtime = Long.parseLong(album.eliveTime);
                                if (currentTime >= endtime && endtime > 0) {
                                    programmeName.setTextColor(Color.parseColor("#80f1f1f1"));
                                    programmeTime.setTextColor(Color.parseColor("#80f1f1f1"));
                                }
                            }
                        }
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.m1571e(this.TAG, "onItemFocusChanged video is null");
                        return;
                    } else {
                        return;
                    }
                }
                AnimationUtil.zoomAnimation(itemView, isSelected, 1.05f, 200, false);
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1571e(this.TAG, "onItemFocusChanged itemView is null");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(this.TAG, "onItemFocusChanged holder is null");
        }
    }

    public void onItemClick(ViewGroup parent, ViewHolder holder) {
        if (holder != null) {
            int position = holder.getLayoutPosition();
            if (!ListUtils.isEmpty(this.mList)) {
                IVideo clickedVideo = (IVideo) this.mList.get(position);
                if (clickedVideo != null) {
                    sendClickPingback(this.mList, position);
                    Album album = clickedVideo.getAlbum();
                    if (album.isLive == 1) {
                        notifyChannelChange(album);
                    } else if (this.mCurrentVideo != null) {
                        notifyVideoChange(clickedVideo);
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.TAG, "onItemClick(port): pos=" + position);
                }
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1571e(this.TAG, "onItemSelected holder is null");
        }
    }

    public void onItemRecycled(ViewGroup arg0, ViewHolder arg1) {
    }
}
