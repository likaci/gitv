package com.gala.video.app.player.ui.overlay.panels;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout.LayoutParams;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C0165R;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.C2;
import com.gala.pingback.PingbackStore.IS1080P;
import com.gala.pingback.PingbackStore.IS4K;
import com.gala.pingback.PingbackStore.NOW_C1;
import com.gala.pingback.PingbackStore.NOW_C2;
import com.gala.pingback.PingbackStore.NOW_QPID;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.QPLD;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.pingback.PingbackStore.QY_PRV;
import com.gala.pingback.PingbackStore.RPAGE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideo.OnVideoDataChangedListener;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.data.ContentsCreatorParams;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.ui.overlay.contents.BitStreamContent;
import com.gala.video.app.player.ui.overlay.contents.ContentHolder;
import com.gala.video.app.player.ui.overlay.contents.ContentsCreatorFactory;
import com.gala.video.app.player.ui.overlay.contents.EpisodeListContent;
import com.gala.video.app.player.ui.overlay.contents.GalleryListContent;
import com.gala.video.app.player.ui.overlay.contents.IContent;
import com.gala.video.app.player.ui.overlay.contents.IContentsCreator;
import com.gala.video.app.player.ui.overlay.contents.MenuPanelContentsCreator;
import com.gala.video.app.player.ui.overlay.contents.ScreenRatioContent;
import com.gala.video.app.player.ui.overlay.contents.SkipHeadTailContent;
import com.gala.video.app.player.ui.widget.tabhost.ISimpleTabHost.OnTabChangedListener;
import com.gala.video.app.player.ui.widget.tabhost.ISimpleTabHost.OnTabFocusChangedListener;
import com.gala.video.app.player.ui.widget.tabhost.SimpleTabHost;
import com.gala.video.app.player.ui.widget.tabhost.SimpleTabHostAdapter;
import com.gala.video.app.player.utils.UiUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.DlnaKeyEvent;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.widget.MyRadioGroup;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.http.HTTP;

public class MenuPanel extends AbsMenuPanel {
    private static final List<Pair<Integer, Integer>> DISP_MODE_LIST = new ArrayList();
    private static final String TAG = "Player/Ui/MenuPanel";
    private boolean mFocus = false;
    private boolean mHasVipBitStreamPingback = false;
    private boolean mIsBitStreamContentHasShow = false;
    private boolean mIsMenuProgramShow = false;
    private boolean mIsRecommendDataFilled = false;
    private boolean mIsScreenRatioShow = false;
    private boolean mIsShown = false;
    private boolean mIsSkipHeaderShow = false;
    private ContentHolder mLastContentHolder;
    private boolean mNeedRefreshUI = false;
    private SimpleTabHostAdapter mTabAdapter = null;
    private OnTabChangedListener mTabChangedListener = new C15052();
    private OnTabFocusChangedListener mTabFocusChangedListener = new C15063();
    private OnVideoDataChangedListener mVideoDataChangedListener = new C15041();

    class C15041 implements OnVideoDataChangedListener {
        C15041() {
        }

        public void onVideoDataChanged(int dataFlag) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(MenuPanel.TAG, ">> mVideoDataChangedListener.onVideoDataChanged, dataFlag=" + Integer.toHexString(dataFlag));
            }
            if (MenuPanel.this.mAssociativeContentHolder != null) {
                int dataType = dataFlag & 4095;
                String tag = MenuPanel.this.mAssociativeContentHolder.getTag();
                IContent<?, ?> content = MenuPanel.this.mAssociativeContentHolder.getWrappedContent();
                if (DetailConstants.CONTENT_TAG_EPISODE.equals(tag) && 1 == dataType) {
                    MenuPanel.this.fillEpisodeData(content);
                } else if (DetailConstants.CONTENT_TAG_PROGRAM.equals(tag) && 1 == dataType) {
                    MenuPanel.this.fillProgramData(content);
                } else if (DetailConstants.CONTENT_TAG_BODAN.equals(tag) && 2 == dataType) {
                    MenuPanel.this.fillBodanData(content);
                } else if (DetailConstants.CONTENT_TAG_RECOMMEND.equals(tag) && 4 == dataType) {
                    MenuPanel.this.fillRecommendData(content);
                    if (MenuPanel.this.mLastContentHolder == null || !DetailConstants.CONTENT_TAG_RECOMMEND.equals(MenuPanel.this.mLastContentHolder.getTag()) || !MenuPanel.this.mIsShown) {
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(MenuPanel.TAG, "mVideoDataChangedListener.onVideoDataChanged, unhandled callback, tag=" + tag + ", dataType=" + dataType);
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(MenuPanel.TAG, "mAssociativeContentHolder is null!!!");
            }
        }

        public void onException(int dataFlag) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(MenuPanel.TAG, ">> mVideoDataChangedListener.onException, dataFlag=" + Integer.toHexString(dataFlag));
            }
        }
    }

    class C15052 implements OnTabChangedListener {
        C15052() {
        }

        public void onTabChanged(int position) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(MenuPanel.TAG, ">> onTabChanged position:" + position);
            }
            ContentHolder holder = (ContentHolder) MenuPanel.this.mContentHolderList.get(position);
            holder.getWrappedContent().show();
            holder.getWrappedContent().getFocusableView().setNextFocusUpId(16908307);
            if (DetailConstants.CONTENT_TAG_RECOMMEND.equals(holder.getTag()) && MenuPanel.this.mIsRecommendDataFilled) {
                List list = MenuPanel.this.getVisibleList();
                if (!ListUtils.isEmpty(list)) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(MenuPanel.TAG, ">> onTabChanged list:" + list.toString());
                    }
                    MenuPanel.this.notifyRecommendShow(((Integer) list.get(0)).intValue(), ((Integer) list.get(list.size() - 1)).intValue());
                }
            }
            if (MenuPanel.this.mLastContentHolder != null) {
                MenuPanel.this.mLastContentHolder.getWrappedContent().hide();
            }
            MenuPanel.this.mLastContentHolder = holder;
            if (holder != null) {
                String tag = holder.getTag();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(MenuPanel.TAG, ">> onTabChanged tag=" + tag);
                }
                if (DetailConstants.CONTENT_TAG_BITSTREAM.equals(tag)) {
                    BitStreamContent content = (BitStreamContent) holder.getWrappedContent();
                    if (!MenuPanel.this.mIsBitStreamContentHasShow) {
                        if (content.isHasHDRandToggleOpen()) {
                            MenuPanel.this.sendBitStreamContentShowPingback("quality_hdr_open");
                        } else if (content.isHasHDRandToggleClose()) {
                            MenuPanel.this.sendBitStreamContentShowPingback("quality_hdr_close");
                        } else {
                            MenuPanel.this.sendBitStreamContentShowPingback("quality");
                        }
                        MenuPanel.this.mIsBitStreamContentHasShow = true;
                    }
                } else if (DetailConstants.CONTENT_TAG_SKIPHEADER.equals(tag)) {
                    if (!MenuPanel.this.mIsSkipHeaderShow) {
                        MenuPanel.this.sendSkipHeaderShowPingback();
                        MenuPanel.this.mIsSkipHeaderShow = true;
                    }
                } else if (DetailConstants.CONTENT_TAG_SCREENRATIO.equals(tag)) {
                    if (!MenuPanel.this.mIsScreenRatioShow) {
                        MenuPanel.this.sendScreenRatioShowPingback();
                        MenuPanel.this.mIsScreenRatioShow = true;
                    }
                } else if ((DetailConstants.CONTENT_TAG_EPISODE.equals(tag) || DetailConstants.CONTENT_TAG_RECOMMEND.equals(tag) || DetailConstants.CONTENT_TAG_BODAN.equals(tag) || DetailConstants.CONTENT_TAG_PROGRAM.equals(tag)) && !MenuPanel.this.mIsMenuProgramShow) {
                    MenuPanel.this.sendMenuProgramShowPingback();
                    MenuPanel.this.mIsMenuProgramShow = true;
                }
            }
        }
    }

    class C15063 implements OnTabFocusChangedListener {
        C15063() {
        }

        public void onTabFocusChanged(View view, boolean hasFocus) {
            view.setNextFocusUpId(view.getId());
        }
    }

    static {
        DISP_MODE_LIST.add(new Pair(Integer.valueOf(1), Integer.valueOf(C1291R.string.screen_original)));
        DISP_MODE_LIST.add(new Pair(Integer.valueOf(4), Integer.valueOf(C1291R.string.screen_fullscreen)));
    }

    public MenuPanel(Context context) {
        super(context);
    }

    public MenuPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MenuPanel(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setVideo(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> setVideo, video=" + video);
        }
        if (video != null) {
            IVideo oldVideo = this.mCurrentVideo;
            if (this.mCurrentVideo != null) {
                this.mCurrentVideo.removeListener(this.mVideoDataChangedListener);
            }
            video.addListener(this.mVideoDataChangedListener);
            super.setVideo(video);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "setVideo: oldVideo=" + oldVideo + ", new video=" + this.mCurrentVideo);
            }
            if (needRefreshContents(oldVideo)) {
                this.mNeedRefreshUI = true;
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setVideo, video is null, return");
        }
    }

    private boolean needRefreshContents(IVideo video) {
        if (video == null) {
            return false;
        }
        if (!video.getTvId().equals(this.mCurrentVideo.getTvId()) || isLiveChannelChanged(video)) {
            return true;
        }
        return false;
    }

    private void refreshContents() {
        List<ContentHolder> list = new ArrayList();
        list.add(findHolderByTag(DetailConstants.CONTENT_TAG_BITSTREAM));
        IContentsCreator creator = ContentsCreatorFactory.instance().getMenuPanelContentCreator();
        ContentsCreatorParams params = new ContentsCreatorParams();
        params.setVideo(this.mCurrentVideo).setEpisodeUIStyle(this.mEpisodeStyle).setGalleryUIStyle(this.mLandGalleryUIStyle).setMenuPanelUIStyle(this.mMenuPanelUIStyle);
        this.mAssociativeContentHolder = ((MenuPanelContentsCreator) creator).createAssociativeContent(this.mContext, params);
        if (this.mAssociativeContentHolder != null) {
            list.add(this.mAssociativeContentHolder);
        }
        creator.createRestContents(this.mContext, params, list);
        this.mContentHolderList.clear();
        this.mContentHolderList.addAll(list);
        this.mTabAdapter.updateData(extractContentList());
        setupContents();
        fillDataOnInit();
    }

    private boolean isLiveChannelChanged(IVideo lastVideo) {
        boolean ret = false;
        SourceType sourceType = this.mCurrentVideo.getSourceType();
        String curLiveChannelID = this.mCurrentVideo.getLiveChannelId();
        String lastLiveChannelID = lastVideo.getLiveChannelId();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> isLiveChannelChanged, sourceType=" + sourceType + ", curLiveChannelID=" + curLiveChannelID + ", lastLiveChannelID=" + lastLiveChannelID);
        }
        if ((SourceType.CAROUSEL == sourceType || SourceType.LIVE == sourceType) && !curLiveChannelID.equals(lastLiveChannelID)) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< isLiveChannelChanged, ret=" + ret);
        }
        return ret;
    }

    protected void initViews(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> initViews.");
        }
        LayoutInflater.from(context).inflate(this.mMenuPanelUIStyle.getMenuPanelLayoutResId(), this, true);
        initTabHost();
        initTabAdapter();
        fillDataOnInit();
    }

    private boolean findVipIndex(List<BitStream> list, List<BitStream> viplist) {
        if (ListUtils.isEmpty((List) list) || ListUtils.isEmpty((List) viplist)) {
            return false;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (viplist.contains(list.get(i))) {
                return true;
            }
        }
        return false;
    }

    private void fillBitStreamData(IContent<?, ?> content) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> fillBitStreamData");
        }
        if (this.mCurrentVideo != null && this.mCurrentVideo.getAllBitStreams() != null) {
            List listAll;
            if (this.mCurrentVideo.getSourceType() == SourceType.PUSH) {
                listAll = this.mCurrentVideo.getPlayBitStreams();
            } else {
                listAll = this.mCurrentVideo.getAllBitStreams();
            }
            List<BitStream> listVIP = this.mCurrentVideo.getVipBitStreams();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "fillBitStreamData, SourceType=" + this.mCurrentVideo.getSourceType() + "listAll=" + listAll + ", listVIP=" + listVIP);
            }
            ((BitStreamContent) content).setData(listAll);
            ((BitStreamContent) content).setVipData(listVIP);
            this.mHasVipBitStreamPingback = findVipIndex(listAll, listVIP);
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "fillBitStreamData, invalid video or VideoBitStream.");
        }
    }

    private void fillDataOnInit() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> fillDataOnInit");
        }
        if (this.mCurrentVideo != null) {
            IVideo video = this.mCurrentVideo;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "fillDataOnInit, mContentHolderList=" + this.mContentHolderList);
            }
            for (ContentHolder each : this.mContentHolderList) {
                IContent<?, ?> content = each.getWrappedContent();
                String tag = each.getTag();
                if (DetailConstants.CONTENT_TAG_BITSTREAM.equals(tag)) {
                    ((BitStreamContent) content).setSelection(video.getCurrentBitStream());
                    fillBitStreamData(content);
                } else if (DetailConstants.CONTENT_TAG_EPISODE.equals(tag)) {
                    ((EpisodeListContent) content).setSelection(video);
                    if (video.isEpisodeFilled()) {
                        fillEpisodeData(content);
                    }
                } else if (DetailConstants.CONTENT_TAG_PROGRAM.equals(tag)) {
                    ((GalleryListContent) content).setSelection(video);
                    if (video.isEpisodeFilled()) {
                        fillProgramData(content);
                    }
                } else if (DetailConstants.CONTENT_TAG_BODAN.equals(tag)) {
                    ((GalleryListContent) content).setSelection(video);
                    if (video.getProvider().isPlaylistReady()) {
                        fillBodanData(content);
                    }
                } else if (DetailConstants.CONTENT_TAG_RECOMMEND.equals(tag)) {
                    ((GalleryListContent) content).setSelection(video);
                    if (video.getProvider().isPlaylistReady()) {
                        fillRecommendData(content);
                    }
                } else if (DetailConstants.CONTENT_TAG_SCREENRATIO.equals(tag)) {
                    ((ScreenRatioContent) content).setData(DISP_MODE_LIST);
                    ((ScreenRatioContent) content).setSelection((Integer) ((Pair) DISP_MODE_LIST.get(PlayerAppConfig.getStretchPlaybackToFullScreen() ? 1 : 0)).first);
                } else if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "unhandled content tag=" + tag);
                }
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "fillDataOnInit, mCurrentVideo is null;");
        }
    }

    private void fillBodanData(IContent<?, ?> content) {
        ((GalleryListContent) content).setData(this.mCurrentVideo.getBodanVideos());
    }

    private void fillEpisodeData(IContent<?, ?> content) {
        ((EpisodeListContent) content).setData(this.mCurrentVideo.getEpisodeVideos());
    }

    private void fillProgramData(IContent<?, ?> content) {
        ((GalleryListContent) content).setData(this.mCurrentVideo.getEpisodeVideos());
    }

    private void fillRecommendData(IContent<?, ?> content) {
        ((GalleryListContent) content).setData(this.mCurrentVideo.getBodanVideos());
        this.mIsRecommendDataFilled = true;
    }

    private void initTabHost() {
        this.mTabHost = (SimpleTabHost) findViewById(C1291R.id.menu_tabhost);
        this.mTabHost.setOnTabChangedListener(this.mTabChangedListener);
        this.mTabHost.setOnTabFocusChangedListener(this.mTabFocusChangedListener);
    }

    private void initTabAdapter() {
        initContents();
        this.mTabAdapter = new SimpleTabHostAdapter(extractContentList());
    }

    private List<IContent<?, ?>> extractContentList() {
        List<IContent<?, ?>> list = new ArrayList();
        if (!ListUtils.isEmpty(this.mContentHolderList)) {
            for (ContentHolder each : this.mContentHolderList) {
                list.add(each.getWrappedContent());
            }
        }
        return list;
    }

    private void initContents() {
        if (this.mCurrentVideo != null) {
            IContentsCreator contentsCreator = ContentsCreatorFactory.instance().getMenuPanelContentCreator();
            ContentsCreatorParams params = new ContentsCreatorParams();
            params.setVideo(this.mCurrentVideo).setEpisodeUIStyle(this.mEpisodeStyle).setGalleryUIStyle(this.mLandGalleryUIStyle).setMenuPanelUIStyle(this.mMenuPanelUIStyle);
            contentsCreator.createMajorContents(this.mContext, params, this.mContentHolderList);
            if (!ListUtils.isEmpty(this.mContentHolderList) && this.mContentHolderList.size() > 1) {
                this.mAssociativeContentHolder = (ContentHolder) this.mContentHolderList.get(1);
            }
            contentsCreator.createRestContents(this.mContext, params, this.mContentHolderList);
            setupContents();
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "mCurrentVideo is null!!");
        }
    }

    private void setupContents() {
        for (ContentHolder holder : this.mContentHolderList) {
            setContentListener(holder);
        }
    }

    public void notifyHDRToggle(boolean isOpen) {
        if (!ListUtils.isEmpty(this.mContentHolderList)) {
            ContentHolder holder = (ContentHolder) this.mContentHolderList.get(0);
            if (holder != null && DetailConstants.CONTENT_TAG_BITSTREAM.equals(holder.getTag())) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, ">> notifyHDRToggle bitStramContent!!! isOpen " + isOpen);
                }
                if (isOpen) {
                    sendBitStreamTogglePingback("quality_hdr_close", isOpen);
                } else {
                    sendBitStreamTogglePingback("quality_hdr_open", isOpen);
                }
            }
        }
    }

    protected void doShow() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> doShow " + this.mIsShown + "mNeedRefreshUI=" + this.mNeedRefreshUI);
        }
        if (!this.mIsShown) {
            setAdData();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, ">> doShow preAdjustHeight");
            }
            this.mIsShown = true;
            if (this.mNeedRefreshUI) {
                this.mNeedRefreshUI = false;
                refreshContents();
            }
            preAdjustHeight();
            if (this.mTabAdapter != null) {
                this.mTabHost.setAdapter(this.mTabAdapter);
                setDefaultFocusOnShow();
            }
            if (!PlayerAppConfig.isSelectionPanelShown() && this.mKey == 20) {
                String countstr = PlayerAppConfig.getSelectionPanelShownCount();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1571e(TAG, "getSelectionPanelShownCount：" + countstr);
                }
                int count = getCount(countstr);
                if (count < 3) {
                    PlayerAppConfig.setSelectionPanelShownCount(Project.getInstance().getBuild().getVersionString() + "," + (count + 1));
                    return;
                }
                PlayerAppConfig.setSelectionPanelShown(true);
            }
        }
    }

    private int getCount(String countstr) {
        if (StringUtils.isEmpty((CharSequence) countstr)) {
            return 0;
        }
        CharSequence currentversion = Project.getInstance().getBuild().getVersionString();
        String[] array = splitContent(countstr);
        String version = array[0];
        String count = array[1];
        if (StringUtils.isEmpty(currentversion) || !currentversion.contentEquals(version)) {
            return 0;
        }
        return parseString(count);
    }

    private String[] splitContent(String content) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "splitContent:" + content.toString());
        }
        try {
            if (content.contains(",")) {
                return content.split(",");
            }
        } catch (Exception e) {
        }
        return null;
    }

    private int parseString(String str) {
        if (!StringUtils.isEmpty((CharSequence) str)) {
            try {
                return Integer.valueOf(str).intValue();
            } catch (Exception e) {
            }
        }
        return 0;
    }

    private void sendSkipHeaderShowPingback() {
        if (this.mCurrentVideo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(TAG, "sendSkipHeaderShowPingback mCurrentVideo is null");
            }
        } else if (this.mPingbackContext != null) {
            Album album = this.mCurrentVideo.getAlbum();
            if (album != null) {
                PingbackFactory.instance().createPingback(50).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(getC1())).addItem(QTCURL.QTCURL_TYPE(getQtcurl())).addItem(QPLD.QPLD_TYPE(album.tvQid)).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE("skip")).addItem(C2.C2_TYPE(getC2())).addItem(QY_PRV.ITEM(getQyPrv())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).post();
            }
        }
    }

    private void sendScreenRatioShowPingback() {
        if (this.mCurrentVideo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(TAG, "sendScreenRatioShowPingback mCurrentVideo is null");
            }
        } else if (this.mPingbackContext != null) {
            Album album = this.mCurrentVideo.getAlbum();
            if (album != null) {
                PingbackFactory.instance().createPingback(51).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(getC1())).addItem(QTCURL.QTCURL_TYPE(getQtcurl())).addItem(QPLD.QPLD_TYPE(album.tvQid)).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE("ratio")).addItem(C2.C2_TYPE(getC2())).addItem(QY_PRV.ITEM(getQyPrv())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).post();
            }
        }
    }

    private void sendMenuProgramShowPingback() {
        if (this.mCurrentVideo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1571e(TAG, "sendMenuProgramShowPingback mCurrentVideo is null");
            }
        } else if (this.mPingbackContext != null) {
            Album album = this.mCurrentVideo.getAlbum();
            if (album != null) {
                PingbackFactory.instance().createPingback(52).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(getC1())).addItem(QTCURL.QTCURL_TYPE(getQtcurl())).addItem(QPLD.QPLD_TYPE(album.tvQid)).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE(getProgramBlock())).addItem(C2.C2_TYPE(getC2())).addItem(QY_PRV.ITEM(getQyPrv())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).post();
            }
        }
    }

    private List<BitStream> getCurrentBitStreamList() {
        if (ListUtils.isEmpty(this.mContentHolderList)) {
            return null;
        }
        ContentHolder holder = (ContentHolder) this.mContentHolderList.get(0);
        if (holder == null || !DetailConstants.CONTENT_TAG_BITSTREAM.equals(holder.getTag())) {
            return null;
        }
        return ((BitStreamContent) holder.getWrappedContent()).getContentData();
    }

    private void sendBitStreamContentShowPingback(String block) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, ">> sendBitStreamContentShowPingback.");
        }
        this.mHDRBlock = block;
        String is4K = "NA";
        String is1080P = "NA";
        List allList;
        if (this.mCurrentVideo.getSourceType() == SourceType.PUSH) {
            allList = this.mCurrentVideo.getPlayBitStreams();
        } else {
            allList = getCurrentBitStreamList();
        }
        List vipList = this.mCurrentVideo.getVipBitStreams();
        if (LogUtils.mIsDebug) {
            LogUtils.m1571e(TAG, "sendBitStreamContentShowPingback allList=" + allList + ", vipList=" + vipList);
        }
        if (!ListUtils.isEmpty((List) allList)) {
            for (BitStream bitstream : allList) {
                if (bitstream.getDefinition() == 10) {
                    if (ListUtils.isEmpty(vipList) || !vipList.contains(bitstream)) {
                        is4K = "0";
                    } else {
                        is4K = "1";
                    }
                }
                if (bitstream.getDefinition() == 5) {
                    if (ListUtils.isEmpty(vipList) || !vipList.contains(bitstream)) {
                        is1080P = "0";
                    } else {
                        is1080P = "1";
                    }
                }
            }
        }
        PingbackFactory.instance().createPingback(45).addItem(BTSPTYPE.BSTP_1).addItem(C1.C1_TYPE(getC1())).addItem(QTCURL.QTCURL_TYPE(getQtcurl())).addItem(QPLD.QPLD_TYPE(this.mCurrentVideo.getAlbum().tvQid)).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE(block)).addItem(QY_PRV.ITEM(getQyPrv())).addItem(IS4K.ITEM(is4K)).addItem(IS1080P.ITEM(is1080P)).addItem(NOW_C1.NOW_C1_TYPE(getC1())).addItem(C2.C2_TYPE(getC2())).post();
    }

    private void sendBitStreamTogglePingback(String blockType, boolean isOpen) {
        String rseatType = "";
        if (isOpen) {
            rseatType = "open";
        } else {
            rseatType = HTTP.CLOSE;
        }
        if (this.mCurrentVideo != null) {
            PingbackFactory.instance().createPingback(47).addItem(C0165R.R_TYPE(getTvqID())).addItem(BLOCK.BLOCK_TYPE(blockType)).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(rseatType)).addItem(RPAGE.RPAGE_ID(getRpage())).addItem(C1.C1_TYPE(getC1())).addItem(NOW_C1.NOW_C1_TYPE(getC1())).addItem(NOW_QPID.NOW_QPID_TYPE(getNowQpid())).addItem(NOW_C2.NOW_C2_TYPE(getC2())).addItem(C2.C2_TYPE(getC2())).post();
        }
    }

    protected String getC1() {
        SourceType type = this.mCurrentVideo.getSourceType();
        if (type == SourceType.CAROUSEL) {
            return "101221";
        }
        if (type != SourceType.LIVE) {
            return String.valueOf(this.mCurrentVideo.getChannelId());
        }
        if (this.mCurrentVideo.isTrailer()) {
            return String.valueOf(this.mCurrentVideo.getChannelId());
        }
        return "101221";
    }

    private String getQtcurl() {
        String qtcurl = "";
        if (this.mKey == 20) {
            return "downpanel";
        }
        if (this.mKey == 82) {
            return "menupanel";
        }
        return qtcurl;
    }

    private void setDefaultFocusOnShow() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> setDefaultFocusOnShow");
        }
        if (this.mKey == 82) {
            showDefinitionTab();
        } else if (this.mKey != 20) {
        } else {
            if (this.mAssociativeContentHolder == null) {
                showDefinitionTab();
                return;
            }
            IContent<?, ?> content = null;
            int contentType = this.mAssociativeContentHolder.getType();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "setDefaultFocusOnShow contentType=" + contentType);
            }
            switch (contentType) {
                case 1:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_EPISODE);
                    break;
                case 2:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_PROGRAM);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "setDefaultFocusOnShow content=" + content);
                        break;
                    }
                    break;
                case 3:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_RECOMMEND);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "setDefaultFocusOnShow content=" + content);
                        break;
                    }
                    break;
                case 7:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_BODAN);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "setDefaultFocusOnShow content=" + content);
                        break;
                    }
                    break;
            }
            this.mTabHost.setCurrentTab(1);
            if (content == null) {
                return;
            }
            if (content instanceof EpisodeListContent) {
                if (!this.mFocus) {
                    if (content.getFocusableView().isShown()) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1568d(TAG, "setDefaultFocusOnShow focus=" + this.mFocus);
                        }
                        content.getFocusableView().requestFocus();
                        if (!this.mFocus) {
                            this.mFocus = true;
                        }
                    } else {
                        this.mTabHost.getIndicatorView().requestFocus();
                    }
                    ((EpisodeListContent) content).setSelection(this.mCurrentVideo);
                }
            } else if (content instanceof GalleryListContent) {
                boolean empty = ListUtils.isEmpty((List) content.getContentData());
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "setDefaultFocusOnShow empty=" + empty);
                }
                if (empty) {
                    this.mTabHost.getIndicatorView().requestFocus();
                } else {
                    content.getFocusableView().requestFocus();
                }
                ((GalleryListContent) content).setSelection(this.mCurrentVideo);
            }
        }
    }

    protected void handleContentRequestFocus() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "handleContentRequestFocus()");
        }
        if (this.mKey == 20 && this.mAssociativeContentHolder != null) {
            IContent<?, ?> content = null;
            int contentType = this.mAssociativeContentHolder.getType();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "handleContentRequestFocus contentType=" + contentType);
            }
            switch (contentType) {
                case 1:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_EPISODE);
                    break;
                case 2:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_PROGRAM);
                    break;
                case 3:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_RECOMMEND);
                    break;
                case 7:
                    content = findContentByTag(DetailConstants.CONTENT_TAG_BODAN);
                    break;
            }
            if (content == null) {
                return;
            }
            if (content instanceof EpisodeListContent) {
                if (content.getFocusableView().isShown() && !this.mFocus) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "setDefaultFocusOnShow hasfocus=" + this.mFocus);
                    }
                    content.getFocusableView().requestFocus();
                    if (!this.mFocus) {
                        this.mFocus = true;
                    }
                }
            } else if (content instanceof GalleryListContent) {
                boolean empty = ListUtils.isEmpty((List) content.getContentData());
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "handleContentRequestFocus empty=" + empty);
                }
                content.getFocusableView().requestFocus();
            }
        }
    }

    private void showDefinitionTab() {
        this.mTabHost.setCurrentTab(0);
        IContent<?, ?> content = findContentByTag(DetailConstants.CONTENT_TAG_BITSTREAM);
        if (content != null) {
            MyRadioGroup rg = (MyRadioGroup) content.getView().findViewById(C1291R.id.rg_definition);
            int selection = findCurBitstreamIndex();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "setDefaultFocusOnShow, list= " + this.mCurrentVideo.getAllBitStreams() + ", current bitstream=" + this.mCurrentVideo.getCurrentBitStream() + ", selection=" + selection);
            }
            if (selection >= 0) {
                rg.requestFocusOnChild(selection);
                rg.setSelection(selection);
            }
        }
    }

    private IContent<?, ?> findContentByTag(String tag) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> findContentByTitle, tag=" + tag);
        }
        for (ContentHolder each : this.mContentHolderList) {
            if (each.getTag().equals(tag)) {
                return each.getWrappedContent();
            }
        }
        return null;
    }

    private ContentHolder findHolderByTag(String tag) {
        for (ContentHolder each : this.mContentHolderList) {
            if (each.getTag().equals(tag)) {
                return each;
            }
        }
        return null;
    }

    private int findCurBitstreamIndex() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> findCurBitstreamIndex");
        }
        int ret = -1;
        List list = null;
        if (!ListUtils.isEmpty(this.mContentHolderList)) {
            ContentHolder holder = (ContentHolder) this.mContentHolderList.get(0);
            if (holder != null && DetailConstants.CONTENT_TAG_BITSTREAM.equals(holder.getTag())) {
                list = ((BitStreamContent) holder.getWrappedContent()).getContentData();
            }
        }
        BitStream cur = this.mCurrentVideo.getCurrentBitStream();
        if (ListUtils.isEmpty(list)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "findCurBitstreamIndex, invalid bitstream list=" + list);
            }
            return -1;
        } else if (cur == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "findCurBitstreamIndex, current bitstream is null!!!");
            }
            return -1;
        } else {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "findCurBitstreamIndex, list=" + list + "current bitstream=" + cur);
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                if (cur.equal((BitStream) list.get(i))) {
                    ret = i;
                    break;
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "<< findCurBitstreamIndex, ret=" + ret);
            }
            return ret;
        }
    }

    protected void doHide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> doHide");
        }
        if (this.mIsShown) {
            this.mIsShown = false;
            this.mLastContentHolder.getWrappedContent().hide();
            this.mLastContentHolder = null;
            this.mFocus = false;
            this.mIsBitStreamContentHasShow = false;
            this.mIsScreenRatioShow = false;
            this.mIsSkipHeaderShow = false;
            this.mIsMenuProgramShow = false;
        }
    }

    protected void clearAd() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clearAd()");
        }
        if (!ListUtils.isEmpty(this.mContentHolderList)) {
            ContentHolder holder = (ContentHolder) this.mContentHolderList.get(0);
            if (holder != null && DetailConstants.CONTENT_TAG_BITSTREAM.equals(holder.getTag())) {
                ((BitStreamContent) holder.getWrappedContent()).clearAd();
            }
        }
        if (this.mAssociativeContentHolder != null) {
            IContent<?, ?> content = this.mAssociativeContentHolder.getWrappedContent();
            if (content != null) {
                if (content instanceof GalleryListContent) {
                    ((GalleryListContent) content).clearAd();
                } else if (content instanceof EpisodeListContent) {
                    ((EpisodeListContent) content).clearAd();
                }
            } else {
                return;
            }
        }
        this.mAdData = null;
    }

    private void setAdData() {
        if (!ListUtils.isEmpty(this.mContentHolderList)) {
            ContentHolder holder = (ContentHolder) this.mContentHolderList.get(0);
            if (holder != null && DetailConstants.CONTENT_TAG_BITSTREAM.equals(holder.getTag())) {
                ((BitStreamContent) holder.getWrappedContent()).setAdData(this.mAdData);
            }
        }
    }

    private void preAdjustHeight() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> preAdjustHeight() ");
        }
        int panelHeight = UiUtils.getDimensionInPx(this.mContext, C1291R.dimen.dimen_248dp);
        LayoutParams params = (LayoutParams) getLayoutParams();
        if (this.mAssociativeContentHolder != null && (this.mAssociativeContentHolder.getWrappedContent() instanceof GalleryListContent)) {
            panelHeight = UiUtils.getDimensionInPx(this.mContext, C1291R.dimen.dimen_315dp);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> preAdjustHeight() " + hasBitStreamHDRType());
        }
        if (hasBitStreamHDRType()) {
            panelHeight = UiUtils.getDimensionInPx(this.mContext, C1291R.dimen.dimen_315dp);
        }
        params.height = panelHeight;
        setLayoutParams(params);
        notifyEpisodelistContentHDR();
    }

    private void notifyEpisodelistContentHDR() {
        if (this.mAssociativeContentHolder != null && (this.mAssociativeContentHolder.getWrappedContent() instanceof EpisodeListContent)) {
            IContent<?, ?> content = this.mAssociativeContentHolder.getWrappedContent();
            if (content != null) {
                if (hasBitStreamHDRType()) {
                    ((EpisodeListContent) content).notifyEpisodelistContentHDR(true);
                } else {
                    ((EpisodeListContent) content).notifyEpisodelistContentHDR(false);
                }
            }
        }
    }

    private boolean hasBitStreamHDRType() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> hasBitStreamHDRType");
        }
        if (this.mCurrentVideo == null || this.mCurrentVideo.getAllBitStreams() == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "hasBitStreamHDRType, invalid video or VideoBitStream.");
            }
            return false;
        }
        List<BitStream> listAll;
        if (this.mCurrentVideo.getSourceType() == SourceType.PUSH) {
            listAll = this.mCurrentVideo.getPlayBitStreams();
        } else {
            listAll = this.mCurrentVideo.getAllBitStreams();
        }
        int i = 0;
        while (i < listAll.size()) {
            LogRecordUtils.logd(TAG, " hasBitStreamHDRType , i " + i + ", type= " + ((BitStream) listAll.get(i)).getDynamicRangeType());
            if (((BitStream) listAll.get(i)).getDynamicRangeType() == 1 || ((BitStream) listAll.get(i)).getDynamicRangeType() == 2) {
                return true;
            }
            i++;
        }
        if (!PlayerDebugUtils.testHDRBitStreamData()) {
            return false;
        }
        LogRecordUtils.logd(TAG, " testHDRBitStreamData ");
        return true;
    }

    public void updateSkipHeadAndTail(boolean isSkipTail) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "updateSkipHeadAndTail = " + isSkipTail);
        }
        IContent<?, ?> content = findContentByTag(DetailConstants.CONTENT_TAG_SKIPHEADER);
        if (content != null) {
            ((SkipHeadTailContent) content).setSelection(Boolean.valueOf(isSkipTail));
        }
    }

    public void updateBitStream(List<BitStream> list, BitStream bitStream) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(TAG, "updateBitStream( " + bitStream + ",list " + list + ")");
        }
        IContent<?, ?> content = findContentByTag(DetailConstants.CONTENT_TAG_BITSTREAM);
        if (content != null) {
            ((BitStreamContent) content).setSelection(bitStream);
            fillBitStreamData(content);
            preAdjustHeight();
        }
    }

    public void onActivityDestroyed() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> onActivityDestroyed.");
        }
        super.onActivityDestroyed();
        if (this.mCurrentVideo != null) {
            this.mCurrentVideo.removeListener(this.mVideoDataChangedListener);
            this.mCurrentVideo = null;
        }
    }

    public boolean onDlnaKeyEvent(DlnaKeyEvent event, KeyKind key) {
        LogUtils.m1574i(TAG, "onDlnaKeyEvent = " + event + ", key " + key);
        if (isShown() && event == DlnaKeyEvent.FLING) {
            switch (key) {
                case LEFT:
                    DeviceUtils.sendKeyCode(92);
                    break;
                case RIGHT:
                    DeviceUtils.sendKeyCode(93);
                    break;
                default:
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1577w(TAG, "invalid key for fling event: " + key);
                        break;
                    }
                    break;
            }
        }
        return false;
    }
}
