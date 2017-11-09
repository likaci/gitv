package com.gala.video.app.epg.ui.ucenter.record;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Spanned;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.sdk.player.IMediaPlayer;
import com.gala.video.albumlist4.utils.AnimationUtils;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemAnimatorFinishListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnScrollListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt.INetworkStateListener;
import com.gala.video.app.epg.ui.albumlist.constant.IFootConstant;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum.FootLeftRefreshPage;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.app.epg.ui.ucenter.record.adapter.DeleteModeAdapter;
import com.gala.video.app.epg.ui.ucenter.record.adapter.FavouriteAdapter;
import com.gala.video.app.epg.ui.ucenter.record.adapter.HistoryAdapter;
import com.gala.video.app.epg.ui.ucenter.record.adapter.SubscribeAdapter;
import com.gala.video.app.epg.ui.ucenter.record.contract.RecordFavouriteContentContract.Presenter;
import com.gala.video.app.epg.ui.ucenter.record.contract.RecordFavouriteContentContract.View;
import com.gala.video.app.epg.ui.ucenter.record.utils.ColorString;
import com.gala.video.app.epg.ui.ucenter.record.utils.ColorString.ColorStringItem;
import com.gala.video.app.epg.ui.ucenter.record.utils.RecordFavouriteUtil;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class RecordFavouriteContentFragment extends Fragment implements View, OnItemFocusChangedListener {
    private static final int ANIMATION_DURATION = 200;
    private static final float ANIMATION_SCALE = 1.093f;
    private static final int GRIDVIEW_TOP = ResourceUtil.getDimen(R.dimen.dimen_2dp);
    private static final int GRIDVIEW_VERTICALMARGIN = ResourceUtil.getPx(-7);
    private DeleteModeAdapter mAdapter;
    private boolean mContentNeedFocus = true;
    private GlobalQRFeedbackPanel mErrorPanel;
    private VerticalGridView mGridView;
    private int mGridViewNum = 5;
    private MyHandler mHandler;
    private ImageLoadTask mImageLoadTask;
    private boolean mIsFirstIn;
    private boolean mIsFromLogin = false;
    private Button mLoginButton;
    private TextView mLoginTip;
    private TextView mMenuDesView;
    private NetworkPrompt mNetworkStatePrompt;
    private ImageView mNoCommonResult;
    private BitmapDrawable mNoFavouriteDrawable;
    private TextView mNoHistoryResultView;
    private BitmapDrawable mNoSubscribleDrawable;
    private OnFocusChangeListener mOnFocusChangeListener;
    private OnItemAnimatorFinishListener mOnItemAnimatorFinishListener = new OnItemAnimatorFinishListener() {
        public void onItemAnimatorFinished() {
            RecordFavouriteContentFragment.this.updateView();
            RecordFavouriteContentFragment.this.loadMore();
        }
    };
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            int position = holder.getLayoutPosition();
            if (position >= 0 && position < ListUtils.getCount(RecordFavouriteContentFragment.this.mAdapter.getDataList())) {
                int selectedColumn = holder.getLayoutColumn();
                int selectedRow = holder.getLayoutRow();
                AlbumInfoModel infoModel = RecordFavouriteContentFragment.this.mPresenter.getInfoModel();
                infoModel.setFocusPosition(position);
                infoModel.setSelectColumn(selectedColumn);
                infoModel.setSelectRow(selectedRow);
                RecordFavouriteContentFragment.this.mPresenter.doOnItemClick(position, (IData) RecordFavouriteContentFragment.this.mAdapter.getData(position));
            }
        }
    };
    private OnItemRecycledListener mOnItemRecycledListener = new OnItemRecycledListener() {
        public void onItemRecycled(ViewGroup parent, ViewHolder holder) {
            RecordFavouriteContentFragment.this.mAdapter.recycleBitmap(holder.itemView);
            RecordFavouriteContentFragment.this.mAdapter.releaseData(holder.itemView);
        }
    };
    private OnScrollListener mOnScrollListener = new OnScrollListener() {
        public void onScrollStart() {
            RecordFavouriteContentFragment.this.mGridView.setExtraPadding(0);
            RecordFavouriteContentFragment.this.mHandler.removeCallbacks(RecordFavouriteContentFragment.this.mScrollStopRunnable);
            if (RecordFavouriteContentFragment.this.mAdapter != null) {
                RecordFavouriteContentFragment.this.mAdapter.onCancelAllTasks();
            }
        }

        public void onScroll(ViewParent parent, int firstVisibleItem, int lastVisibleItem, int totalItemCount) {
            RecordFavouriteContentFragment.this.loadMore();
        }

        public void onScrollStop() {
            RecordFavouriteContentFragment.this.onReloadTasks();
        }
    };
    private FootLeftRefreshPage mPage;
    private Presenter mPresenter;
    private final Runnable mScrollStopRunnable = new Runnable() {
        public void run() {
            if (RecordFavouriteContentFragment.this.mGridView != null && RecordFavouriteContentFragment.this.mAdapter != null) {
                int first = RecordFavouriteContentFragment.this.mGridView.getFirstAttachedPosition();
                int last = RecordFavouriteContentFragment.this.mGridView.getLastAttachedPosition();
                for (int i = first; i <= last; i++) {
                    RecordFavouriteContentFragment.this.mAdapter.onReloadTasks(RecordFavouriteContentFragment.this.mGridView.getViewByPosition(i));
                }
            }
        }
    };

    private class ImageLoadTask extends AsyncTask<Integer, Integer, Drawable> {
        private Drawable mDrawable;
        private ImageView mImageView;

        public ImageLoadTask(ImageView view, Drawable drawable) {
            this.mImageView = view;
            this.mDrawable = drawable;
        }

        protected Drawable doInBackground(Integer... resId) {
            if (isCancelled()) {
                return null;
            }
            return RecordFavouriteContentFragment.this.getResources().getDrawable(resId[0].intValue());
        }

        protected void onPostExecute(Drawable result) {
            if (!isCancelled()) {
                this.mDrawable = result;
                this.mImageView.setImageDrawable(this.mDrawable);
            }
        }
    }

    private static class MyHandler extends Handler {
        private WeakReference<RecordFavouriteContentFragment> mFragment;

        public MyHandler(RecordFavouriteContentFragment fragment) {
            this.mFragment = new WeakReference(fragment);
        }

        public void handleMessage(Message msg) {
            RecordFavouriteContentFragment fragment = (RecordFavouriteContentFragment) this.mFragment.get();
            if (fragment != null) {
                fragment.handleMessage(msg);
            }
        }
    }

    private class NetworkListener implements INetworkStateListener {
        private NetworkListener() {
        }

        public void onConnected(boolean isChanged) {
            if (isChanged) {
                RecordFavouriteContentFragment.this.mPresenter.loadData();
            }
        }
    }

    private void handleMessage(Message msg) {
        if (this.mAdapter.getCount() == 0) {
            if (this.mNoCommonResult.getVisibility() == 0) {
                this.mNoCommonResult.setVisibility(4);
            }
            if (this.mNoHistoryResultView.getVisibility() == 0) {
                this.mNoHistoryResultView.setVisibility(4);
            }
            this.mMenuDesView.setVisibility(4);
            this.mAdapter.showCacheView(true);
        }
    }

    @SuppressLint({"InflateParams"})
    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mIsFirstIn = true;
        android.view.View rootView = inflater.inflate(R.layout.epg_fragment_record_favourite_content, null);
        this.mMenuDesView = (TextView) rootView.findViewById(R.id.epg_q_album_menu_des);
        this.mGridView = (VerticalGridView) rootView.findViewById(R.id.epg_qalbum_gridview);
        initGridView();
        this.mNoHistoryResultView = (TextView) rootView.findViewById(R.id.epg_no_history_result);
        this.mNoCommonResult = (ImageView) rootView.findViewById(R.id.epg_no_common_result);
        this.mHandler = new MyHandler(this);
        return rootView;
    }

    private void initGridView() {
        int low = GRIDVIEW_TOP + (GridAdapter.HEIGHT / 2);
        int high = (GridAdapter.HEIGHT + low) + GRIDVIEW_VERTICALMARGIN;
        this.mGridView.setFocusPlace(low, low);
        this.mGridView.setNumRows(this.mGridViewNum);
        this.mGridView.setFocusable(true);
        this.mGridView.setFocusLoop(true);
        this.mGridView.setFocusMode(1);
        this.mGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mGridView.setExtraPadding(IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
        this.mGridView.setFocusLeaveForbidden(66);
        this.mGridView.setPadding(ResourceUtil.getDimen(R.dimen.dimen_24dp), ResourceUtil.getDimen(R.dimen.dimen_2dp), ResourceUtil.getDimen(R.dimen.dimen_10dp), ResourceUtil.getDimen(R.dimen.dimen_15dp));
        this.mGridView.setVerticalMargin(ResourceUtil.getPx(-7));
        this.mGridView.setHorizontalMargin(ResourceUtil.getDimen(R.dimen.dimen_4dp));
        this.mGridView.setOnItemFocusChangedListener(this);
        this.mGridView.setOnItemClickListener(this.mOnItemClickListener);
        this.mGridView.setOnScrollListener(this.mOnScrollListener);
        this.mGridView.setOnItemRecycledListener(this.mOnItemRecycledListener);
        this.mGridView.setOnItemAnimatorFinishListener(this.mOnItemAnimatorFinishListener);
        this.mGridView.setScrollBarDrawable(R.drawable.epg_thumb);
    }

    private void loadMore() {
        this.mPresenter.loadMore(this.mGridView.getRow(this.mGridView.getFocusPosition()), this.mAdapter.getCount() / this.mGridView.getNumRows());
    }

    private void onReloadTasks() {
        this.mHandler.removeCallbacks(this.mScrollStopRunnable);
        this.mHandler.post(this.mScrollStopRunnable);
    }

    public void onResume() {
        super.onResume();
        if (this.mNetworkStatePrompt == null) {
            this.mNetworkStatePrompt = new NetworkPrompt(getActivity());
        }
        this.mNetworkStatePrompt.registerNetworkListener(new NetworkListener());
        this.mGridView.setVerticalScrollBarEnabled(UserUtil.isLogin());
        if (this.mIsFirstIn) {
            this.mIsFirstIn = false;
            return;
        }
        switch (this.mPage) {
            case PLAY_HISTORY_ALL:
            case PLAY_HISTORY_LONG:
                if (!this.mPresenter.isLoginChanged() && this.mIsFromLogin) {
                    this.mIsFromLogin = false;
                    return;
                }
            case SUBSCRIBE:
            case FAVOURITE:
                if (!this.mPresenter.isLoginChanged()) {
                    return;
                }
                break;
        }
        if (this.mIsFromLogin) {
            this.mIsFromLogin = false;
            this.mContentNeedFocus = true;
        }
        this.mPresenter.reloadData();
        this.mGridView.setFocusPosition(0);
    }

    public void onPause() {
        super.onPause();
        if (this.mNetworkStatePrompt != null) {
            this.mNetworkStatePrompt.unregisterNetworkListener();
        }
    }

    public void setPresenter(Presenter presenter) {
        this.mPresenter = presenter;
    }

    public void updateMenuDesc() {
        if (FootLeftRefreshPage.SUBSCRIBE == this.mPage) {
            this.mMenuDesView.setVisibility(4);
            return;
        }
        this.mMenuDesView.setVisibility(0);
        this.mMenuDesView.setText(formatMenuDesc());
    }

    private Spanned formatMenuDesc() {
        String descTxt = null;
        switch (this.mPage) {
            case PLAY_HISTORY_ALL:
            case PLAY_HISTORY_LONG:
                descTxt = IFootConstant.STR_MYHISTORY_TOP_MENU;
                break;
            case FAVOURITE:
                descTxt = IFootConstant.STR_FAV_TOP_MENU;
                break;
        }
        int menuDescResID = R.string.menu_desc_format_noLogin;
        if (UserUtil.isLogin()) {
            menuDescResID = R.string.menu_desc_format;
        }
        ColorString msp = new ColorString(String.format(ResourceUtil.getStr(menuDescResID), new Object[]{descTxt}));
        msp.setColor(ResourceUtil.getColor(R.color.albumview_menu_color));
        msp.setColor(new ColorStringItem(ResourceUtil.getStr(R.string.alter_menukey_text), ResourceUtil.getColor(R.color.albumview_yellow_color)));
        return msp.build();
    }

    public void updateData(List<IData> list, boolean firstLoad) {
        this.mAdapter.showCacheView(false);
        this.mAdapter.updateData(list);
        if (firstLoad) {
            this.mAdapter.notifyDataSetChanged();
        }
        updateView();
    }

    public void showLoading(boolean showLoading) {
        this.mAdapter.showLoading(showLoading);
    }

    public void showLogin(boolean showLogin) {
        if (showLogin) {
            initLoginView();
            if (this.mLoginButton != null && this.mLoginTip != null) {
                this.mLoginButton.setVisibility(0);
                this.mLoginTip.setVisibility(0);
                this.mGridView.setNextFocusDownId(this.mLoginButton.getId());
                this.mLoginButton.setNextFocusUpId(this.mGridView.getId());
            }
        } else if (this.mLoginButton != null) {
            this.mLoginButton.setVisibility(8);
            this.mLoginTip.setVisibility(8);
            this.mLoginButton.setNextFocusUpId(-1);
            this.mGridView.setNextFocusDownId(-1);
        }
    }

    public void hideLoading() {
        this.mAdapter.hideLoading();
    }

    public void enterDeleteMode() {
        if (!isEmpty()) {
            this.mAdapter.setDeleteMode(true);
            this.mAdapter.notifyDataSetUpdate();
            QAPingback.recordLayerShowPingback(2, 0, this.mPresenter.getInfoModel());
        }
    }

    public boolean isEmpty() {
        return this.mAdapter == null || ListUtils.isEmpty(this.mAdapter.getDataList());
    }

    public void leaveDeleteMode() {
        this.mAdapter.setDeleteMode(false);
        this.mAdapter.notifyDataSetUpdate();
        QAPingback.recordDeleteLayerPingback(3);
    }

    public boolean isDeleteMode() {
        return this.mAdapter.isDeleteMode();
    }

    public void notifyItemRemoved(int position) {
        this.mAdapter.deleteData(position);
        this.mAdapter.notifyItemRemoved(position);
    }

    public void setPage(FootLeftRefreshPage page) {
        if (this.mPage == null || this.mPage != page) {
            resetErrorView();
            this.mPage = page;
            if (this.mAdapter != null) {
                this.mAdapter.getDataList().clear();
                this.mAdapter = null;
            }
            switch (this.mPage) {
                case PLAY_HISTORY_ALL:
                case PLAY_HISTORY_LONG:
                    this.mAdapter = new HistoryAdapter(getActivity(), AlbumViewType.VERTICAL);
                    break;
                case SUBSCRIBE:
                    this.mAdapter = new SubscribeAdapter(getActivity(), AlbumViewType.VERTICAL);
                    break;
                case FAVOURITE:
                    this.mAdapter = new FavouriteAdapter(getActivity(), AlbumViewType.VERTICAL);
                    break;
            }
            this.mGridView.setAdapter(this.mAdapter);
            this.mHandler.sendEmptyMessageDelayed(0, 250);
        }
    }

    private void resetErrorView() {
        if (this.mErrorPanel != null && this.mErrorPanel.isShown()) {
            this.mErrorPanel.setVisibility(8);
        }
        this.mNoCommonResult.setVisibility(8);
        this.mNoHistoryResultView.setVisibility(8);
        this.mGridView.setVisibility(0);
        this.mGridView.setFocusable(false);
        showLogin(false);
    }

    private void hideNoResultView() {
        updateMenuDesc();
        this.mNoCommonResult.setVisibility(8);
        this.mNoHistoryResultView.setVisibility(8);
    }

    private void showNoResultView() {
        this.mMenuDesView.setVisibility(4);
        switch (this.mPage) {
            case PLAY_HISTORY_ALL:
            case PLAY_HISTORY_LONG:
                this.mNoCommonResult.setVisibility(8);
                this.mNoHistoryResultView.setVisibility(0);
                int resId = this.mPage == FootLeftRefreshPage.PLAY_HISTORY_ALL ? this.mPresenter.isLogin() ? R.string.temporary_no_play_history_login : R.string.temporary_no_play_history : R.string.temporary_no_play_history_long;
                this.mNoHistoryResultView.setText(resId);
                return;
            case SUBSCRIBE:
                this.mNoHistoryResultView.setVisibility(8);
                this.mNoCommonResult.setVisibility(0);
                if (this.mNoSubscribleDrawable == null) {
                    setImageResource(this.mNoCommonResult, R.drawable.epg_no_subscrible_record_alter_image);
                    return;
                } else {
                    this.mNoCommonResult.setImageDrawable(this.mNoSubscribleDrawable);
                    return;
                }
            case FAVOURITE:
                this.mNoHistoryResultView.setVisibility(8);
                this.mNoCommonResult.setVisibility(0);
                if (this.mNoFavouriteDrawable == null) {
                    setImageResource(this.mNoCommonResult, R.drawable.epg_no_favorite_record_alter_image);
                    return;
                } else {
                    this.mNoCommonResult.setImageDrawable(this.mNoFavouriteDrawable);
                    return;
                }
            default:
                return;
        }
    }

    private void setImageResource(ImageView iamgeView, int resId) {
        if (this.mImageLoadTask != null) {
            this.mImageLoadTask.cancel(true);
            this.mImageLoadTask = null;
        }
        this.mImageLoadTask = new ImageLoadTask(this.mNoCommonResult, this.mNoFavouriteDrawable);
        this.mImageLoadTask.execute(new Integer[]{Integer.valueOf(resId)});
        iamgeView.setImageDrawable(null);
    }

    public void notifyDataSetChanged() {
        this.mAdapter.notifyDataSetChanged();
    }

    public void showClearAllDialog() {
        int msg = -1;
        switch (this.mPage) {
            case PLAY_HISTORY_ALL:
            case PLAY_HISTORY_LONG:
                msg = R.string.playhistory_clear_confirm;
                break;
            case SUBSCRIBE:
                msg = R.string.subscrible_clear_all;
                break;
            case FAVOURITE:
                msg = R.string.favourite_clear_confirm;
                break;
        }
        final GlobalDialog clearAlbumDialog = Project.getInstance().getControl().getGlobalDialog(getActivity());
        clearAlbumDialog.setParams(ResourceUtil.getStr(msg), ResourceUtil.getStr(R.string.delete_sure), new OnClickListener() {
            public void onClick(android.view.View v) {
                clearAlbumDialog.dismiss();
                if (!RecordFavouriteContentFragment.this.isEmpty()) {
                    RecordFavouriteContentFragment.this.mPresenter.clearAll();
                }
                QAPingback.recordClearDialogPingback(0);
            }
        }, ResourceUtil.getStr(R.string.exit_cancel_btn), new OnClickListener() {
            public void onClick(android.view.View v) {
                clearAlbumDialog.dismiss();
                QAPingback.recordClearDialogPingback(1);
            }
        });
        clearAlbumDialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                if (event.getAction() == 0) {
                    switch (keyCode) {
                        case 4:
                        case 111:
                            QAPingback.recordClearDialogPingback(3);
                            break;
                    }
                }
                return false;
            }
        });
        clearAlbumDialog.show();
        QAPingback.recordLayerShowPingback(1, 0, this.mPresenter.getInfoModel());
    }

    public void deleteAll() {
        this.mAdapter.showLoading(false);
        this.mAdapter.getDataList().clear();
        this.mAdapter.notifyDataSetChanged();
        updateView();
    }

    private void updateView() {
        this.mHandler.removeMessages(0);
        if (this.mErrorPanel != null && this.mErrorPanel.isShown()) {
            this.mErrorPanel.setVisibility(8);
        }
        if (this.mAdapter.getCount() == 0) {
            showNoResultView();
            this.mGridView.setFocusable(false);
        } else {
            hideNoResultView();
            this.mGridView.setVisibility(0);
            this.mGridView.setFocusable(true);
            if (this.mContentNeedFocus) {
                this.mGridView.requestFocus();
            }
        }
        this.mContentNeedFocus = false;
    }

    public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
        AnimationUtil.zoomAnimation(holder.itemView, hasFocus ? ANIMATION_SCALE : 1.0f, 200);
        if (this.mOnFocusChangeListener != null) {
            this.mOnFocusChangeListener.onFocusChange(parent, hasFocus);
        }
    }

    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        this.mOnFocusChangeListener = l;
    }

    public void showErrorView(ErrorKind kind, ApiException e) {
        this.mHandler.removeMessages(0);
        this.mMenuDesView.setVisibility(4);
        this.mNoCommonResult.setVisibility(4);
        this.mNoHistoryResultView.setVisibility(4);
        this.mGridView.setFocusable(false);
        this.mGridView.setVisibility(8);
        GetInterfaceTools.getUICreator().maketNoResultView(getActivity(), getErrorPanel(), kind, e);
    }

    private GlobalQRFeedbackPanel getErrorPanel() {
        if (this.mErrorPanel == null) {
            if (getView() == null) {
                return null;
            }
            android.view.View view = ((ViewStub) getView().findViewById(R.id.epg_content_status_layout)).inflate();
            view.setFocusable(true);
            view.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(android.view.View v, boolean hasFocus) {
                    if (RecordFavouriteContentFragment.this.mErrorPanel != null && RecordFavouriteContentFragment.this.mErrorPanel.isShown()) {
                        RecordFavouriteContentFragment.this.mErrorPanel.getButton().requestFocus();
                    }
                }
            });
            this.mErrorPanel = (GlobalQRFeedbackPanel) getView().findViewById(R.id.epg_q_album_right_data_no_result_panel);
            this.mErrorPanel.getButton().setOnFocusChangeListener(this.mOnFocusChangeListener);
        }
        return this.mErrorPanel;
    }

    public boolean isFocusable() {
        return isFeedbackShown() || this.mAdapter.getCount() > 0;
    }

    private boolean isFeedbackShown() {
        return this.mErrorPanel != null && this.mErrorPanel.isShown() && this.mErrorPanel.getButton() != null && this.mErrorPanel.getButton().getVisibility() == 0;
    }

    public void onDestroy() {
        super.onDestroy();
        releaseBitmapDrawable(this.mNoFavouriteDrawable);
        releaseBitmapDrawable(this.mNoSubscribleDrawable);
    }

    public void onDetach() {
        super.onDetach();
        if (this.mImageLoadTask != null) {
            this.mImageLoadTask.cancel(true);
            this.mImageLoadTask = null;
        }
    }

    private void releaseBitmapDrawable(BitmapDrawable drawable) {
        if (drawable != null) {
            drawable.setCallback(null);
            drawable.getBitmap().recycle();
        }
    }

    public boolean isActive() {
        return isAdded();
    }

    public List<IData> getList() {
        return this.mAdapter.getDataList();
    }

    public void requestFocus() {
        if (!ListUtils.isEmpty(getList())) {
            this.mGridView.requestFocus();
        }
    }

    public void setTotalSize(int totalSize) {
        this.mGridView.setTotalSize(totalSize);
    }

    private void initLoginView() {
        if (this.mLoginButton == null) {
            if (getView() != null) {
                android.view.View view = ((ViewStub) getView().findViewById(R.id.epg_login_layout)).inflate();
                view.setFocusable(false);
                this.mLoginButton = (Button) view.findViewById(R.id.epg_btn_my_login);
                this.mLoginButton.setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(android.view.View v, boolean hasFocus) {
                        AnimationUtil.zoomAnimation(v, hasFocus, 1.04f, 200);
                        RecordFavouriteContentFragment.this.mLoginButton.setTextColor(hasFocus ? ResourceUtil.getColor(R.color.albumview_focus_color) : ResourceUtil.getColor(R.color.albumview_normal_color));
                    }
                });
                this.mLoginButton.setOnKeyListener(new android.view.View.OnKeyListener() {
                    public boolean onKey(android.view.View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == 0) {
                            switch (keyCode) {
                                case 20:
                                    RecordFavouriteContentFragment.this.startShakeAnimation(Service.CISCO_FNA);
                                    break;
                                case 21:
                                    RecordFavouriteContentFragment.this.mGridView.setFocusPosition(0);
                                    break;
                                case 22:
                                    RecordFavouriteContentFragment.this.startShakeAnimation(66);
                                    break;
                            }
                        }
                        return false;
                    }
                });
                this.mLoginButton.setOnClickListener(new OnClickListener() {
                    public void onClick(android.view.View v) {
                        RecordFavouriteContentFragment.this.mIsFromLogin = true;
                        GetInterfaceTools.getLoginProvider().startLoginActivity(RecordFavouriteContentFragment.this.getActivity(), RecordFavouriteUtil.getS1FromForPingBack(RecordFavouriteContentFragment.this.mPage), 2);
                        QToast.makeTextAndShow(AppRuntimeEnv.get().getApplicationContext(), RecordFavouriteContentFragment.this.mPage == FootLeftRefreshPage.FAVOURITE ? ResourceUtil.getStr(R.string.favourite_login_toast) : ResourceUtil.getStr(R.string.record_login_toast), 5000);
                    }
                });
                this.mLoginTip = (TextView) view.findViewById(R.id.epg_txt_login_tip);
                this.mLoginTip.setTextColor(ResourceUtil.getColorStateList(R.color.albumview_normal_color));
            } else {
                return;
            }
        }
        refreshLoginTip();
    }

    private void startShakeAnimation(int direction) {
        AnimationUtils.shakeAnimation(getActivity(), this.mLoginButton, direction);
    }

    private void refreshLoginTip() {
        int loginTipResId = R.string.record_login_tip;
        if (this.mPage == FootLeftRefreshPage.FAVOURITE) {
            loginTipResId = R.string.favourite_login_tip;
        }
        if (this.mLoginTip != null) {
            this.mLoginTip.setText(ResourceUtil.getStr(loginTipResId));
        }
    }

    public void setIsFromLoginView(boolean isFromLoginView) {
        this.mIsFromLogin = isFromLoginView;
    }
}
