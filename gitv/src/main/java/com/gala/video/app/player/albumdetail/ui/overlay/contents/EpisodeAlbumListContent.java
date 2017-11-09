package com.gala.video.app.player.albumdetail.ui.overlay.contents;

import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.ui.config.style.IEpisodeListUIStyle;
import com.gala.video.app.player.ui.overlay.contents.IContent;
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

public class EpisodeAlbumListContent implements IContent<List<AlbumInfo>, AlbumInfo> {
    private static final int ARROW_LEFT = 1;
    private static final int ARROW_RIGHT = 2;
    private static final boolean IS_ZOOM_ENABLED = Project.getInstance().getControl().isOpenAnimation();
    private static final int MSG_DATA_REFRESHED = 1;
    private static final int MSG_SELECTION_REFRESHED = 2;
    private final String TAG;
    private boolean isDataInit = false;
    private AlbumInfo mAlbumInfo;
    private ImageView mArrowLeft;
    private ImageView mArrowRight;
    private View mContentView;
    private Context mContext;
    private OnEpisodeClickListener mEpisodeClickListener = new OnEpisodeClickListener() {
        public void onEpisodeClick(View v, int index) {
            int playOrder = index + 1;
            if (VideoChecker.isExistInEpisodeListAlbum(EpisodeAlbumListContent.this.mEpisodes, playOrder)) {
                AlbumInfo clickedItem = DataHelper.findVideoByOrderAlbum(EpisodeAlbumListContent.this.mEpisodes, playOrder);
                if (EpisodeAlbumListContent.this.mItemListener != null) {
                    EpisodeAlbumListContent.this.mItemListener.onItemClicked(clickedItem, index);
                }
            } else if (EpisodeAlbumListContent.this.mAlbumInfo.getEpisodesTotalCount() > EpisodeAlbumListContent.this.mEpisodes.size()) {
                PlayerToastHelper.showToast(EpisodeAlbumListContent.this.mContext, R.string.prepare_album_episode, 2000);
            } else {
                PlayerToastHelper.showToast(EpisodeAlbumListContent.this.mContext, R.string.no_album_episode, 2000);
            }
        }
    };
    private OnEpisodeFocusChangeListener mEpisodeFocusChangedListener = new OnEpisodeFocusChangeListener() {
        public void onEpisodeFocus(int i) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(EpisodeAlbumListContent.this.TAG, ">> mEpisodeFocusChangedListener.onEpisodeFocus, position=" + i);
            }
        }
    };
    private EpisodeListView mEpisodeListView;
    private List<AlbumInfo> mEpisodes;
    private boolean mIsAutoFocus = false;
    private boolean mIsShown = false;
    private IItemListener<AlbumInfo> mItemListener;
    private Handler mMainHandler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    EpisodeAlbumListContent.this.handleDataRefreshed(msg.obj);
                    return;
                case 2:
                    EpisodeAlbumListContent.this.handleSelectionRefreshed(msg.obj);
                    return;
                default:
                    super.handleMessage(msg);
                    return;
            }
        }
    };
    private String mTitle;
    private TextView mTxtLoading;
    private IEpisodeListUIStyle mUiStyle;

    private void handleSelectionRefreshed(AlbumInfo albumInfo) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> handleSelectionRefreshed, new albumInfo=" + albumInfo);
        }
        if (albumInfo != null) {
            this.mAlbumInfo = albumInfo;
            refreshEpisodeSelection();
            setSelectedTextColor(true);
        }
    }

    private void handleDataRefreshed(List<AlbumInfo> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> handleDataRefreshed, new data size=" + list.size());
        }
        this.mEpisodes = list;
        if (this.mIsShown) {
            adjustEpisodelistView();
        }
    }

    public EpisodeAlbumListContent(Context context, IEpisodeListUIStyle uiStyle, String title, boolean autoFocus) {
        this.mContext = context;
        this.mUiStyle = uiStyle;
        if (title != null) {
            this.mTitle = title;
        } else {
            this.mTitle = "";
        }
        this.mIsAutoFocus = autoFocus;
        this.TAG = "/Player/ui/layout/EpisodeAlbumListContent@" + Integer.toHexString(hashCode()) + "@" + this.mTitle;
    }

    private void initViews() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> initViews()");
        }
        initContentView(this.mContext);
        initEpisodeListView();
        showDataLoading();
    }

    private void initContentView(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "initContentView => inflate");
        }
        this.mContentView = LayoutInflater.from(context).inflate(R.layout.player_episode_content_common, null);
        this.mContentView.setLayoutParams(getContentViewFLP());
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "initContentView <= inflate: result=" + this.mContentView);
        }
        this.mTxtLoading = (TextView) this.mContentView.findViewById(R.id.txt_loading);
        this.mArrowLeft = (ImageView) this.mContentView.findViewById(R.id.detail_arrow_left);
        this.mArrowRight = (ImageView) this.mContentView.findViewById(R.id.detail_arrow_right);
        this.mEpisodeListView = (EpisodeListView) this.mContentView.findViewById(R.id.view_episodelistview);
    }

    public void setSelectedTextColor(boolean isDefaultColor) {
        IEpisodeListUIStyle episodeListUIStyle = this.mUiStyle;
        ItemStyleParamBuilder styleBuilder = new ItemStyleParamBuilder();
        styleBuilder.setTextNormalColor(episodeListUIStyle.getItemTextColorNormal()).setTextFocusedColor(episodeListUIStyle.getItemTextColorFocused()).setParentTextNormalColor(episodeListUIStyle.getParentItemTextColorNormal());
        if (isDefaultColor) {
            styleBuilder.setTextSelectedColor(episodeListUIStyle.getItemTextColorSelected());
        } else {
            styleBuilder.setTextSelectedColor(episodeListUIStyle.getItemTextColorNormal());
        }
        this.mEpisodeListView.setItemTextStyle(styleBuilder);
        this.mEpisodeListView.setSelectedChild(this.mEpisodeListView.getSelectedChild());
    }

    private LayoutParams getContentViewFLP() {
        LayoutParams lp = (LayoutParams) this.mContentView.getLayoutParams();
        if (lp == null) {
            lp = new LayoutParams(-1, ResourceUtil.getDimen(R.dimen.dimen_165dp));
        }
        if (!this.mUiStyle.isDetail()) {
            lp.gravity = 16;
        }
        return lp;
    }

    private void initEpisodeListView() {
        if (LogUtils.mIsDebug) {
            LogUtils.w(this.TAG, ">> initEpisodeListView()");
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
        this.mEpisodeListView.setVipIconResId(R.drawable.share_corner_vip, R.drawable.share_corner_yongquan, R.drawable.share_corner_fufeidianbo);
        int margin = ResourceUtil.getDimensionPixelSize(R.dimen.dimen_0dp);
        this.mEpisodeListView.setVipImgMargins(margin, margin, 0, 0);
        this.mEpisodeListView.setTipsBgResId(episodeListUIStyle.getTipsBgResId());
        this.mEpisodeListView.setTipsTextSizeResId(episodeListUIStyle.getTipsTextSizeResId());
        this.mEpisodeListView.setZoomEnabled(IS_ZOOM_ENABLED);
        this.mEpisodeListView.setAutoFocusSelection(true);
        this.mEpisodeListView.setEnableRequestFocusByParent(true);
        if (!IS_ZOOM_ENABLED) {
            Rect contentPadding = this.mEpisodeListView.getContentPadding();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "initEpisodeView: content padding=" + contentPadding);
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
        adjustArrowParams(this.mArrowLeft, this.mUiStyle.getArrowMarginLeftTop(), this.mUiStyle.getArrowMarginLeft(), 0, 1);
        adjustArrowParams(this.mArrowRight, this.mUiStyle.getArrowMarginRightTop(), 0, this.mUiStyle.getArrowMarginRight(), 2);
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "<< initEpisodeListView()");
        }
    }

    private void setMargins(int left, int top, int right, int bottom) {
        if (LogUtils.mIsDebug) {
            LogUtils.w(this.TAG, ">>setMargins()");
        }
        if (this.mEpisodeListView != null) {
            LayoutParams params = (LayoutParams) this.mEpisodeListView.getLayoutParams();
            params.leftMargin = left;
            params.topMargin = top;
            params.rightMargin = right;
            params.bottomMargin = bottom;
            this.mEpisodeListView.setLayoutParams(params);
            if (LogUtils.mIsDebug) {
                LogUtils.w(this.TAG, "<<setMargins()");
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
            LogUtils.d(this.TAG, ">> adjustEpisodelistView");
        }
        if (this.mAlbumInfo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "adjustEpisodelistView, mAlbumInfo does not set!!!");
            }
        } else if (this.mEpisodes != null) {
            if (this.mIsShown && this.mEpisodeListView.getVisibility() != 0) {
                this.mEpisodeListView.setVisibility(0);
            }
            List<AlbumInfo> list = this.mEpisodes;
            int episodeMaxOrder = this.mAlbumInfo.getEpisodeMaxOrder();
            int playOrder = this.mAlbumInfo.getPlayOrder();
            if (LogUtils.mIsDebug) {
                LogUtils.i(this.TAG, "adjustEpisodelistView: playOrder/ep count=" + playOrder + "/" + episodeMaxOrder);
            }
            if (playOrder > 0) {
                if (episodeMaxOrder == 0) {
                    episodeMaxOrder = this.mAlbumInfo.getTvCount();
                }
                if (episodeMaxOrder > 0) {
                    hideDataLoading();
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(this.TAG, "adjustEpisodelistView (" + episodeMaxOrder + ", playOrder " + playOrder + ")");
                    }
                    this.mEpisodeListView.setCornerIconPositionList(DataExtractor.getTrailerIndicesAlbumList(list));
                    this.mEpisodeListView.setTipsContent(DataExtractor.getOneWordListAlbum(list));
                    this.mEpisodeListView.setVipCornerList(DataExtractor.getVipIndicesListAlbum(list));
                    this.mEpisodeListView.setDisableOrderList(DataExtractor.getEpisodeNoPlayListAlbum(list, episodeMaxOrder));
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
                        LogUtils.d(this.TAG, "<< adjustEpisodelistView");
                        return;
                    }
                    return;
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.d(this.TAG, "Info error, episodeCount(" + episodeMaxOrder + "), playOrder(" + playOrder + ")");
                }
                showDataFailedLoading();
            } else if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "adjustEpisodelistView, invalid play order, albumInfo=" + this.mAlbumInfo);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "adjustEpisodelistView episode list is null!!!");
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
            LogUtils.d(this.TAG, ">> show()");
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
                LogUtils.d(this.TAG, "<< show()");
            }
        }
    }

    private void showDataLoading() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> showDataLoading()");
        }
        this.mTxtLoading.setVisibility(0);
        this.mTxtLoading.setText(R.string.album_detail_data_loading);
        this.mEpisodeListView.setVisibility(8);
    }

    private void hideDataLoading() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> hideDataLoading()");
        }
        this.mTxtLoading.setVisibility(8);
        this.mEpisodeListView.setVisibility(0);
    }

    private void showDataFailedLoading() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> showDataFailedLoading...");
        }
        this.mTxtLoading.setText(R.string.video_play_episode_list_failed);
        this.mTxtLoading.setVisibility(0);
        this.mEpisodeListView.setVisibility(8);
    }

    public void hide() {
        if (LogUtils.mIsDebug) {
            LogUtils.i(this.TAG, ">> onHide()");
        }
        if (this.mIsShown) {
            if (!this.mUiStyle.isDetail()) {
                refreshEpisodeSelection();
            }
            this.mContentView.setVisibility(8);
            this.mIsShown = false;
        }
    }

    public void setItemListener(IItemListener<AlbumInfo> listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setItemListener(" + listener + ")");
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

    public List<AlbumInfo> getContentData() {
        return this.mEpisodes;
    }

    public void setData(List<AlbumInfo> data) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setData, data size=" + data.size());
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            handleDataRefreshed(data);
        } else {
            this.mMainHandler.sendMessage(this.mMainHandler.obtainMessage(1, data));
        }
    }

    public void setSelection(AlbumInfo item) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setSelection, oldAlbuminfo=" + this.mAlbumInfo);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> setSelection, newAlbuminfo=" + item);
        }
        if (Looper.getMainLooper() == Looper.myLooper()) {
            handleSelectionRefreshed(item);
        } else {
            this.mMainHandler.sendMessage(this.mMainHandler.obtainMessage(2, item));
        }
    }

    private void refreshEpisodeSelection() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, ">> refreshEpisodeSelection");
        }
        if (this.mAlbumInfo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "refreshEpisodeSelection, mAlbumInfo is null!");
            }
        } else if (this.mEpisodeListView != null) {
            int playOrder = this.mAlbumInfo.getPlayOrder();
            if (LogUtils.mIsDebug) {
                LogUtils.d(this.TAG, "refreshEpisodeSelection, playOrder=" + playOrder + ", isDataInit=" + this.isDataInit);
            }
            if (this.isDataInit && playOrder > 0) {
                this.mEpisodeListView.setSelectedChild(playOrder - 1);
                this.mEpisodeListView.resetDefaultFocus(playOrder - 1);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "refreshEpisodeSelection, mEpisodeListView is null!");
        }
    }
}
