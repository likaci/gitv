package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.QTCURL;
import com.gala.sdk.player.BaseAdData;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;
import com.gala.video.app.player.ui.overlay.OnAdStateListener;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.utils.DataExtractor;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.app.player.utils.PlayerToastHelper;
import com.gala.video.app.player.utils.VideoChecker;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.widget.episode.DimensParamBuilder;
import com.gala.video.widget.episode.EpisodeListView;
import com.gala.video.widget.episode.EpisodeListView.OnEpisodeClickListener;
import com.gala.video.widget.episode.EpisodeListView.OnEpisodeFocusChangeListener;
import com.gala.video.widget.episode.ItemStyleParamBuilder;
import java.util.List;

public class EpisodeListContent implements IContent<List<IVideo>, IVideo> {
    private static final int ARROW_LEFT = 1;
    private static final int ARROW_RIGHT = 2;
    private static final boolean IS_ZOOM_ENABLED = Project.getInstance().getControl().isOpenAnimation();
    private static final int MSG_DATA_REFRESHED = 1;
    private static final int MSG_SELECTION_REFRESHED = 2;
    private final String TAG;
    private boolean isDataInit = false;
    private BaseAdData mAdData;
    private OnAdStateListener mAdStateListener;
    private RelativeLayout mAdView;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    private View mContentView;
    private Context mContext;
    private IVideo mCurVideo;
    private OnEpisodeClickListener mEpisodeClickListener = new C14892();
    private OnEpisodeFocusChangeListener mEpisodeFocusChangedListener = new C14903();
    private EpisodeListView mEpisodeListView;
    private List<IVideo> mEpisodes;
    private boolean mIsAutoFocus = false;
    private boolean mIsHDR;
    private boolean mIsShown = false;
    private IItemListener<IVideo> mItemListener;
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EpisodeListContent.this.handleDataRefreshed(msg.obj);
                    return;
                case 2:
                    EpisodeListContent.this.handleSelectionRefreshed(msg.obj);
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    };
    private IPingbackContext mPingbackContext;
    private String mTitle;
    private TextView mTxtLoading;
    private IEpisodeListUIStyle mUiStyle;

    class C14892 implements OnEpisodeClickListener {
        C14892() {
        }

        public void onEpisodeClick(View v, int index) {
            int playOrder = index + 1;
            if (VideoChecker.isExistInEpisodeList(EpisodeListContent.this.mEpisodes, playOrder)) {
                IVideo clickedItem = DataHelper.findVideoByOrder(EpisodeListContent.this.mEpisodes, playOrder);
                if (EpisodeListContent.this.mItemListener != null) {
                    EpisodeListContent.this.mItemListener.onItemClicked(clickedItem, index);
                }
            } else if (EpisodeListContent.this.mCurVideo.getEpisodesTotalCount() > EpisodeListContent.this.mEpisodes.size()) {
                PlayerToastHelper.showToast(EpisodeListContent.this.mContext, C1291R.string.prepare_album_episode, 2000);
            } else {
                PlayerToastHelper.showToast(EpisodeListContent.this.mContext, C1291R.string.no_album_episode, 2000);
            }
        }
    }

    class C14903 implements OnEpisodeFocusChangeListener {
        C14903() {
        }

        public void onEpisodeFocus(int i) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(EpisodeListContent.this.TAG, ">> mEpisodeFocusChangedListener.onEpisodeFocus, position=" + i);
            }
        }
    }

    private void handleSelectionRefreshed(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> handleSelectionRefreshed, new video=" + video);
        }
        if (video != null) {
            this.mCurVideo = video;
            refreshEpisodeSelection();
        }
    }

    private void handleDataRefreshed(List<IVideo> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> handleDataRefreshed, new data size=" + list.size());
        }
        this.mEpisodes = list;
        if (this.mIsShown) {
            adjustEpisodelistView();
        }
    }

    public EpisodeListContent(Context context, IEpisodeListUIStyle uiStyle, String title, boolean autoFocus) {
        this.mContext = context;
        this.mUiStyle = uiStyle;
        this.mPingbackContext = (IPingbackContext) this.mContext;
        if (title != null) {
            this.mTitle = title;
        } else {
            this.mTitle = "";
        }
        this.mIsAutoFocus = autoFocus;
        this.TAG = "/Player/ui/layout/EpisodeListContent@" + Integer.toHexString(hashCode()) + "@" + this.mTitle;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> initViews()");
        }
        initContentView(this.mContext);
        initEpisodeListView();
        showDataLoading();
    }

    private void initContentView(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView => inflate");
        }
        this.mContentView = LayoutInflater.from(context).inflate(C1291R.layout.player_episode_content_common, null);
        this.mContentView.setLayoutParams(getContentViewFLP());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "initContentView <= inflate: result=" + this.mContentView);
        }
        this.mTxtLoading = (TextView) this.mContentView.findViewById(C1291R.id.txt_loading);
        this.mArrowLeft = (ImageView) this.mContentView.findViewById(C1291R.id.detail_arrow_left);
        this.mArrowRight = (ImageView) this.mContentView.findViewById(C1291R.id.detail_arrow_right);
        this.mEpisodeListView = (EpisodeListView) this.mContentView.findViewById(C1291R.id.view_episodelistview);
    }

    private LayoutParams getContentViewFLP() {
        LayoutParams lp = (LayoutParams) this.mContentView.getLayoutParams();
        if (this.mUiStyle.isDetail()) {
            return new LayoutParams(-1, ResourceUtil.getDimen(C1291R.dimen.dimen_165dp));
        }
        if (this.mIsHDR) {
            lp = new LayoutParams(-1, ResourceUtil.getDimen(C1291R.dimen.dimen_215dp));
        } else {
            lp = new LayoutParams(-1, ResourceUtil.getDimen(C1291R.dimen.dimen_195dp));
        }
        lp.gravity = 16;
        return lp;
    }

    private void initEpisodeListView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1577w(this.TAG, ">> initEpisodeListView()");
        }
        IEpisodeListUIStyle episodeListUIStyle = this.mUiStyle;
        this.mEpisodeListView.setItemBackgroundResource(episodeListUIStyle.getItemBgResId());
        DimensParamBuilder dimensBuilder = new DimensParamBuilder();
        dimensBuilder.setChildTextSizeResId(episodeListUIStyle.getItemTextSizeId()).setChildWidth(episodeListUIStyle.getItemWidthPx()).setChildHeight(episodeListUIStyle.getItemHeigthPx()).setItemSpacing(episodeListUIStyle.getItemSpacingPx()).setParentHeight(episodeListUIStyle.getParentItemHeightPx()).setParentLayoutMode(episodeListUIStyle.getParentLayoutMode()).setParentTextSizeResId(episodeListUIStyle.getParentItemTextSizeId());
        this.mEpisodeListView.setDimens(dimensBuilder);
        ItemStyleParamBuilder styleBuilder = new ItemStyleParamBuilder();
        styleBuilder.setTextNormalColor(episodeListUIStyle.getItemTextColorNormal()).setTextFocusedColor(episodeListUIStyle.getItemTextColorFocused()).setTextSelectedColor(episodeListUIStyle.getItemTextColorSelected()).setParentTextNormalColor(episodeListUIStyle.getParentItemTextColorNormal());
        this.mEpisodeListView.setItemTextStyle(styleBuilder);
        if (episodeListUIStyle.getCornerImgMargins() != null) {
            this.mEpisodeListView.setCornerImgMargins(episodeListUIStyle.getCornerImgMargins());
        }
        this.mEpisodeListView.setTipsShowLocation(episodeListUIStyle.getTipsShowLocation());
        this.mEpisodeListView.setItemDisableTextStyle(episodeListUIStyle.getItemTextDisableNormal(), episodeListUIStyle.getItemTextDisableFocused());
        this.mEpisodeListView.setTipsTextColor(episodeListUIStyle.getTipsTextColor());
        this.mEpisodeListView.setCornerIconResId(episodeListUIStyle.getCornerIconResId());
        this.mEpisodeListView.setVipIconResId(C1291R.drawable.share_corner_vip, C1291R.drawable.share_corner_yongquan, C1291R.drawable.share_corner_fufeidianbo);
        int margin = ResourceUtil.getDimensionPixelSize(C1291R.dimen.dimen_0dp);
        this.mEpisodeListView.setVipImgMargins(margin, margin, 0, 0);
        this.mEpisodeListView.setTipsBgResId(episodeListUIStyle.getTipsBgResId());
        this.mEpisodeListView.setTipsTextSizeResId(episodeListUIStyle.getTipsTextSizeResId());
        this.mEpisodeListView.setZoomEnabled(IS_ZOOM_ENABLED);
        this.mEpisodeListView.setAutoFocusSelection(true);
        if (!IS_ZOOM_ENABLED) {
            Rect contentPadding = this.mEpisodeListView.getContentPadding();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "initEpisodeView: content padding=" + contentPadding);
            }
            MarginLayoutParams params = (MarginLayoutParams) this.mEpisodeListView.getLayoutParams();
            if (params != null) {
                params.leftMargin -= contentPadding.left;
                this.mEpisodeListView.setLayoutParams(params);
            }
        }
        this.mEpisodeListView.setOnEpisodeClickListener(this.mEpisodeClickListener);
        this.mEpisodeListView.setOnEpisodeFocusChangeListener(this.mEpisodeFocusChangedListener);
        setMargins(this.mUiStyle.getEpisodeMarginLeft(), this.mUiStyle.getEpisodeMarginTop(), this.mUiStyle.getEpisodeMarginRight(), 0);
        setArrow();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< initEpisodeListView()");
        }
    }

    private void setArrow() {
        int lefttop;
        int righttop;
        if (this.mUiStyle.isDetail()) {
            lefttop = this.mUiStyle.getArrowMarginLeftTop();
            righttop = this.mUiStyle.getArrowMarginRightTop();
        } else if (this.mIsHDR) {
            lefttop = ResourceUtil.getDimen(C1291R.dimen.dimen_103dp);
            righttop = ResourceUtil.getDimen(C1291R.dimen.dimen_103dp);
        } else {
            lefttop = this.mUiStyle.getArrowMarginLeftTop();
            righttop = this.mUiStyle.getArrowMarginRightTop();
        }
        adjustArrowParams(this.mArrowLeft, lefttop, this.mUiStyle.getArrowMarginLeft(), 0, 1);
        adjustArrowParams(this.mArrowRight, righttop, 0, this.mUiStyle.getArrowMarginRight(), 2);
    }

    private void setMargins(int left, int top, int right, int bottom) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1577w(this.TAG, ">>setMargins()");
        }
        if (this.mEpisodeListView != null) {
            LayoutParams params = (LayoutParams) this.mEpisodeListView.getLayoutParams();
            params.leftMargin = left;
            params.topMargin = top;
            params.rightMargin = right;
            params.bottomMargin = bottom;
            this.mEpisodeListView.setLayoutParams(params);
            if (LogUtils.mIsDebug) {
                LogUtils.m1577w(this.TAG, "<<setMargins()");
            }
        }
        if (!this.mUiStyle.isDetail()) {
            LayoutParams lp = (LayoutParams) this.mEpisodeListView.getLayoutParams();
            lp.gravity = 16;
            this.mEpisodeListView.setGravity(16);
            this.mEpisodeListView.setLayoutParams(lp);
        }
    }

    private void adjustArrowParams(ImageView arrowView, int marginTop, int marginLeft, int marginRight, int position) {
        LayoutParams margin = new LayoutParams(-2, -2);
        if (1 == position) {
            margin.gravity = 3;
        } else if (2 == position) {
            margin.gravity = 5;
        }
        margin.setMargins(marginLeft, marginTop, marginRight, 0);
        arrowView.setLayoutParams(margin);
    }

    private void adjustEpisodelistView() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> adjustEpisodelistView");
        }
        if (this.mCurVideo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "adjustEpisodelistView, video does not set!!!");
            }
        } else if (this.mEpisodes != null) {
            if (this.mIsShown && this.mEpisodeListView.getVisibility() != 0) {
                this.mEpisodeListView.setVisibility(0);
            }
            IVideo video = this.mCurVideo;
            List<IVideo> list = this.mEpisodes;
            int episodeMaxOrder = video.getEpisodeMaxOrder();
            int playOrder = video.getPlayOrder();
            if (LogUtils.mIsDebug) {
                LogUtils.m1574i(this.TAG, "adjustEpisodelistView: playOrder/ep count=" + playOrder + "/" + episodeMaxOrder);
            }
            if (playOrder > 0) {
                if (episodeMaxOrder == 0) {
                    episodeMaxOrder = video.getTvCount();
                }
                if (episodeMaxOrder > 0) {
                    hideDataLoading();
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(this.TAG, "adjustEpisodelistView (" + episodeMaxOrder + ", playOrder " + playOrder + ")");
                    }
                    this.mEpisodeListView.setCornerIconPositionList(DataExtractor.getTrailerIndicesList(list));
                    this.mEpisodeListView.setTipsContent(DataExtractor.getOneWordList(list));
                    this.mEpisodeListView.setVipCornerList(DataExtractor.getVipIndicesList(list));
                    this.mEpisodeListView.setDisableOrderList(DataExtractor.getEpisodeNoPlayList(list, episodeMaxOrder));
                    if (this.isDataInit) {
                        this.mEpisodeListView.updateDataSource(episodeMaxOrder);
                    } else {
                        this.mEpisodeListView.setDataSource(episodeMaxOrder, playOrder - 1);
                        this.mEpisodeListView.resetDefaultFocus(playOrder - 1);
                        this.isDataInit = true;
                        if (this.mItemListener != null) {
                            this.mItemListener.onItemFilled();
                        }
                    }
                    if (this.mIsAutoFocus) {
                        this.mEpisodeListView.resetNextFocus();
                    }
                    showEpisodeArrow(episodeMaxOrder);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(this.TAG, "<< adjustEpisodelistView");
                        return;
                    }
                    return;
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.TAG, "Info error, episodeCount(" + episodeMaxOrder + "), playOrder(" + playOrder + ")");
                }
                showDataFailedLoading();
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "adjustEpisodelistView, invalid play order, video=" + video);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "adjustEpisodelistView episode list is null!!!");
        }
    }

    private void showEpisodeArrow(int count) {
        if (count >= 10) {
            this.mArrowLeft.setVisibility(0);
            this.mArrowRight.setVisibility(0);
            return;
        }
        this.mArrowLeft.setVisibility(8);
        this.mArrowRight.setVisibility(8);
    }

    public void show() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> show()");
        }
        if (!this.mIsShown) {
            if (this.mContentView == null) {
                initViews();
            }
            if (this.mContentView.getVisibility() != 0) {
                this.mContentView.setVisibility(0);
            }
            adjustEpisodelistView();
            this.mIsShown = true;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "<< show()");
            }
            if (this.mAdView != null) {
                this.mAdView.setVisibility(0);
                sendAdPingback();
                notifyAdCallback();
            } else if (this.mAdStateListener != null) {
                this.mAdStateListener.onRequest(3);
            }
        }
    }

    private void showDataLoading() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> showDataLoading()");
        }
        this.mTxtLoading.setVisibility(0);
        this.mTxtLoading.setText(C1291R.string.album_detail_data_loading);
        this.mEpisodeListView.setVisibility(8);
    }

    private void hideDataLoading() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> hideDataLoading()");
        }
        this.mTxtLoading.setVisibility(8);
        this.mEpisodeListView.setVisibility(0);
    }

    private void showDataFailedLoading() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> showDataFailedLoading...");
        }
        this.mTxtLoading.setText(C1291R.string.video_play_episode_list_failed);
        this.mTxtLoading.setVisibility(0);
        this.mEpisodeListView.setVisibility(8);
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1574i(this.TAG, ">> onHide()");
        }
        if (this.mIsShown) {
            if (!this.mUiStyle.isDetail()) {
                refreshEpisodeSelection();
            }
            this.mContentView.setVisibility(8);
            this.mIsShown = false;
        }
    }

    public void setItemListener(IItemListener<IVideo> listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setItemListener(" + listener + ")");
        }
        this.mItemListener = listener;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public View getView() {
        if (this.mContentView == null) {
            initViews();
        }
        return this.mContentView;
    }

    public View getFocusableView() {
        return this.mEpisodeListView;
    }

    public List<IVideo> getContentData() {
        return this.mEpisodes;
    }

    public void setData(List<IVideo> data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setData, data size=" + data.size());
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            handleDataRefreshed(data);
        } else {
            this.mMainHandler.sendMessage(this.mMainHandler.obtainMessage(1, data));
        }
    }

    public void setSelection(IVideo item) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setSelection, oldVideo=" + this.mCurVideo);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> setSelection, newVideo=" + item);
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            handleSelectionRefreshed(item);
        } else {
            this.mMainHandler.sendMessage(this.mMainHandler.obtainMessage(2, item));
        }
    }

    private void refreshEpisodeSelection() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, ">> refreshEpisodeSelection");
        }
        if (this.mCurVideo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "refreshEpisodeSelection, mCurVideo is null!");
            }
        } else if (this.mEpisodeListView != null) {
            int playOrder = this.mCurVideo.getPlayOrder();
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(this.TAG, "refreshEpisodeSelection, playOrder=" + playOrder + ", isDataInit=" + this.isDataInit);
            }
            if (this.isDataInit && playOrder > 0) {
                this.mEpisodeListView.setSelectedChild(playOrder - 1);
                this.mEpisodeListView.resetDefaultFocus(playOrder - 1);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "refreshEpisodeSelection, mEpisodeListView is null!");
        }
    }

    public void setOnAdStateListener(OnAdStateListener listener) {
        this.mAdStateListener = listener;
    }

    public void setPercisionAdData(BaseAdData data) {
        addAd(data);
    }

    private void addAd(BaseAdData data) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "addAd()");
        }
        if (data != null) {
            this.mAdData = data;
            if (this.mContentView != null && this.mAdView == null) {
                this.mAdView = data.getAdView();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.TAG, "addAd() mAdView=" + this.mAdView);
                }
                if (this.mAdView != null) {
                    LayoutParams params = new LayoutParams(this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_256dp), this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_36dp));
                    params.topMargin = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_8dp);
                    params.rightMargin = this.mContext.getResources().getDimensionPixelSize(C1291R.dimen.dimen_34dp);
                    params.gravity = 5;
                    ((ViewGroup) this.mContentView).addView(this.mAdView, params);
                    if (this.mIsShown) {
                        this.mAdView.setVisibility(0);
                        notifyAdCallback();
                        sendAdPingback();
                        return;
                    }
                    this.mAdView.setVisibility(8);
                }
            }
        }
    }

    private void notifyAdCallback() {
        if (this.mAdStateListener != null && this.mAdData != null) {
            this.mAdStateListener.onShow(102, Integer.valueOf(this.mAdData.getID()));
        }
    }

    public void clearAd() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "clearAd()");
        }
        if (this.mAdView != null) {
            if (this.mContentView != null) {
                ((ViewGroup) this.mContentView).removeView(this.mAdView);
            }
            this.mAdView = null;
            this.mAdData = null;
        }
    }

    private void sendAdPingback() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< sendAdPingback()");
        }
        if (this.mPingbackContext != null) {
            PingbackFactory.instance().createPingback(53).addItem(BTSPTYPE.BSTP_1).addItem(QTCURL.QTCURL_TYPE("ad_chgep")).addItem(this.mPingbackContext.getItem("e")).addItem(BLOCK.BLOCK_TYPE("ad_chgep")).post();
        }
    }

    public void notifyEpisodelistContentHDR(boolean isHDR) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyEpisodelistContentHDR:" + isHDR);
        }
        this.mIsHDR = isHDR;
        if (this.mContentView != null) {
            LayoutParams lp = (LayoutParams) this.mContentView.getLayoutParams();
            if (isHDR) {
                lp.height = ResourceUtil.getDimen(C1291R.dimen.dimen_215dp);
            } else {
                lp.height = ResourceUtil.getDimen(C1291R.dimen.dimen_195dp);
            }
            this.mContentView.setLayoutParams(lp);
            setArrow();
        }
    }
}
