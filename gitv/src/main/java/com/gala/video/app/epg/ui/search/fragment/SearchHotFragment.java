package com.gala.video.app.epg.ui.search.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultHotWords;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridParams;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.WidgetStatusListener;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.search.adapter.SearchHistoryAdapter;
import com.gala.video.app.epg.ui.search.adapter.SearchHotAdapter;
import com.gala.video.app.epg.ui.search.adapter.SearchVipAdapter;
import com.gala.video.app.epg.ui.search.db.SearchHistoryBean;
import com.gala.video.app.epg.ui.search.utils.SearchPingbackUtils;
import com.gala.video.app.epg.ui.solotab.SoloTabEnterUtils;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.data.provider.VipProvider;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class SearchHotFragment extends SearchBaseFragment {
    private static final String LOG_TAG = "EPG/search/SearchRightDefaultFragment";
    private static int mHotClickPos;
    private static int mVipClickPos;
    private boolean isSupportVipMonth;
    private List<ChannelLabel> mAlbumDataList;
    private List<ChannelLabel> mAlbumDataSrcList;
    private WidgetStatusListener mGridViewListener = new WidgetStatusListener() {
        public void onLoseFocus(ViewGroup parent, View view, int position) {
        }

        public void onItemTouch(View arg0, MotionEvent arg1, int arg2) {
        }

        public void onItemSelectChange(View view, int position, boolean hasFocus) {
            TextView textView = (TextView) view;
            if (hasFocus) {
                textView.bringToFront();
                SearchHotFragment.this.changeTabValid(SearchHotFragment.this.getPageLocationType());
                textView.setTextColor(SearchHotFragment.this.getColor(R.color.gala_write));
                if (Project.getInstance().getBuild().isLitchi()) {
                    textView.setTextColor(SearchHotFragment.this.getColor(R.color.gala_write));
                }
                AnimationUtil.scaleAnimation(view, 1.0f, 1.07f, 200);
                textView.setMarqueeRepeatLimit(-1);
                textView.setEllipsize(TruncateAt.MARQUEE);
                return;
            }
            textView.setEllipsize(TruncateAt.END);
            textView.setTextColor(SearchHotFragment.this.getColor(R.color.search_right_text_color));
            AnimationUtil.scaleAnimation(view, 1.07f, 1.0f, 200);
        }

        public void onItemClick(ViewGroup viewGroup, View view, int position) {
            if (SearchHotFragment.this.mContext != null && SearchEnterUtils.checkNetWork(SearchHotFragment.this.mContext)) {
                int clickType = 0;
                String key = ((TextView) view).getText().toString().trim();
                String qpid = (String) view.getTag(ISearchConstant.TAGKEY_SUGGEST_QPID);
                String type = (String) view.getTag(ISearchConstant.TAGKEY_SUGGEST_TYPE);
                Object tag = view.getTag(ISearchConstant.SEARCH_TYPE_TAG_KEY);
                if (tag != null) {
                    int viewTag = ((Integer) tag).intValue();
                    if (1 == viewTag) {
                        clickType = 2;
                        SearchHotFragment.this.START_REQUESTCODE = 2;
                        if (!StringUtils.equals(type, ISearchConstant.SUGGEST_TYPE_PERSON)) {
                            qpid = null;
                        }
                        if (LogUtils.mIsDebug) {
                            LogUtils.d(SearchHotFragment.LOG_TAG, ">>>>> name: ", key, "  type: ", type, "  qpid: ", qpid);
                        }
                    } else if (viewTag == 0) {
                        clickType = 0;
                        SearchHotFragment.mHotClickPos = position;
                    }
                    sendPingback(position, viewTag);
                }
                SearchHotFragment.this.startSearch(clickType, key, qpid, 6, SearchHotFragment.this.START_REQUESTCODE);
                insertDB(key, qpid, type);
            }
        }

        private void sendPingback(final int position, final int viewTag) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    SearchHotFragment.this.sendClickPingback(viewTag, position);
                }
            });
        }

        private void insertDB(final String key, final String qpid, final String type) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    LogUtils.i("SQL_TEST", "SearchRightDefaultFragment --- click ---key=", key);
                    SearchHotFragment.this.mHistoryDao.insertHistory(new SearchHistoryBean(key, qpid, type), 6, true);
                }
            });
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mHasHistory = false;
    private SearchHistoryAdapter mHistoryAdapter;
    private List<SearchHistoryBean> mHistoryBeanList;
    private boolean mHistoryRequestFocus;
    private View mHistoryTitleView;
    private SearchHotAdapter mHotAdapter;
    private List<String> mHotHordsList;
    private View mHotTitleView;
    private View mMainView;
    private WidgetStatusListener mPhotoViewListener = new WidgetStatusListener() {
        public void onLoseFocus(ViewGroup parent, View view, int position) {
        }

        public void onItemTouch(View arg0, MotionEvent arg1, int arg2) {
        }

        public void onItemSelectChange(View v, int position, boolean isSelected) {
            View title = v.findViewById(R.id.epg_searchVip_titleText);
            if (position == 3) {
                if (isSelected) {
                    AppClientUtils.setBackgroundDrawable(title, ResourceUtil.getDrawable(R.drawable.share_bg_focus));
                } else {
                    AppClientUtils.setBackgroundDrawable(title, ResourceUtil.getDrawable(R.drawable.share_bg_unfocus));
                }
            } else if (isSelected) {
                AppClientUtils.setBackgroundDrawable(title, ResourceUtil.getDrawable(R.drawable.share_item_title_focus_bg));
            } else {
                AppClientUtils.setBackgroundDrawable(title, ResourceUtil.getDrawable(R.drawable.share_album_desc_bg));
            }
            AnimationUtil.zoomAnimation(v, isSelected, 1.1f, 200, true);
        }

        public void onItemClick(ViewGroup viewGroup, View view, int position) {
            if (SearchEnterUtils.checkNetWork(SearchHotFragment.this.mContext) && SearchHotFragment.this.mAlbumDataList != null && position < ListUtils.getCount(SearchHotFragment.this.mAlbumDataList)) {
                SearchHotFragment.mVipClickPos = position;
                ChannelLabel item = (ChannelLabel) SearchHotFragment.this.mAlbumDataList.get(position);
                String title = item.desc;
                String from = "3";
                String buySource = "topic";
                if (position == 3) {
                    boolean hasVipTab = TabProvider.getInstance().hasVipTab();
                    TabModel vipTabModel = TabProvider.getInstance().getTabModel(1000002);
                    if (!hasVipTab || vipTabModel == null) {
                        LogUtils.w(SearchHotFragment.LOG_TAG, "onItemClick, VIP_VIDEO, vip tabModel:" + vipTabModel + ", hasVipTab : " + hasVipTab);
                        AlbumUtils.startChannelPage(SearchHotFragment.this.mContext, 1000002, from, buySource);
                    } else {
                        SoloTabEnterUtils.start(SearchHotFragment.this.mContext, vipTabModel, "tab_" + HomePingbackSender.getInstance().getTabName(), from);
                    }
                } else {
                    ItemUtils.openDetailOrPlay(SearchHotFragment.this.mContext, item, title, from, buySource, null);
                }
                SearchHotFragment.this.sendClickPingback(3, position);
            }
        }
    };
    private GlobalQRFeedbackPanel mQrPanel;
    private PhotoGridView mSearchHistoryGridView;
    private RelativeLayout mSearchHistoryLayout;
    private PhotoGridView mSearchHotGridView;
    private PhotoGridView mSearchVipGridView;
    private TextView mTextView;
    private SearchVipAdapter mVipAdapter;

    private static class IApiCallbackImpl implements IApiCallback<ApiResultHotWords> {
        private WeakReference<SearchHotFragment> mOuter;

        public IApiCallbackImpl(SearchHotFragment outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(final ApiResultHotWords result) {
            final SearchHotFragment outer = (SearchHotFragment) this.mOuter.get();
            if (outer != null) {
                if (result == null || result.data == null) {
                    LogUtils.e(SearchHotFragment.LOG_TAG, "requestData --- onSuccess result is null");
                } else {
                    outer.runOnUiThread(new Runnable() {
                        public void run() {
                            outer.doAfterRequestSucc(result.data.hotwords);
                        }
                    });
                }
            }
        }

        public void onException(final ApiException exception) {
            final SearchHotFragment outer = (SearchHotFragment) this.mOuter.get();
            if (outer != null) {
                LogUtils.e(SearchHotFragment.LOG_TAG, ">>>>>>>>>>>> Api.hotword.call Exception: ", exception.getMessage());
                SearchPingbackUtils.error(exception, "TVApi.hotWords");
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int arg0) {
                        outer.runOnUiThread(new Runnable() {
                            public void run() {
                                outer.doAfterRequestException(exception);
                                outer.mSearchVipGridView.setVisibility(8);
                            }
                        });
                    }
                });
            }
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(R.layout.epg_fragment_search_right, null);
        init(this.mMainView);
        showLoading();
        initInThread();
        return this.mMainView;
    }

    public void initInThread() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                SearchHotFragment.this.requestData();
            }
        });
    }

    private void init(View view) {
        initLayout();
        initSearchVipGridView();
        initSearchHotGridView();
        initSearchHistoryGridView();
        this.mSearchHotGridView.setListener(this.mGridViewListener);
        if (this.mSearchHistoryGridView != null) {
            this.mSearchHistoryGridView.setListener(this.mGridViewListener);
        }
    }

    private void requestData() {
        TVApi.hotWords.call(new IApiCallbackImpl(this), new String[0]);
    }

    private void doAfterRequestSucc(List<String> hotwords) {
        prepareVipData(hotwords);
        hideLoading();
        setHotViewAdapter(this.mHotHordsList);
        showVipView();
        setHistoryViewAdapter();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                SearchHotFragment.this.setClearViewFocus();
            }
        }, 500);
    }

    private void prepareVipData(List<String> hotwords) {
        this.mAlbumDataSrcList = VipProvider.getInstance().getList();
        if (this.mAlbumDataList == null) {
            this.mAlbumDataList = new ArrayList();
        }
        if (!ListUtils.isEmpty(this.mAlbumDataSrcList)) {
            for (int i = 0; i < ListUtils.getCount(this.mAlbumDataSrcList); i++) {
                if (!ResourceType.DIY.equals(((ChannelLabel) this.mAlbumDataSrcList.get(i)).getType())) {
                    this.mAlbumDataList.add(this.mAlbumDataSrcList.get(i));
                }
            }
        }
        if (!(this.mContext == null || ListUtils.isEmpty(this.mAlbumDataList) || this.mSearchVipGridView == null)) {
            this.mVipAdapter = new SearchVipAdapter(this.mContext, this.mAlbumDataList);
        }
        this.mHotHordsList = filterList(hotwords, 10);
        mHotHordsCacheList = this.mHotHordsList;
    }

    private void doAfterRequestException(ApiException exception) {
        this.mHotTitleView.setVisibility(0);
        this.mQrPanel = (GlobalQRFeedbackPanel) this.mMainView.findViewById(R.id.epg_search_default_QR_panel);
        FeedBackModel model = CreateInterfaceTools.createFeedbackFactory().createFeedBack(exception);
        String content = model.getErrorMsg();
        if (model.isShowQR()) {
            this.mQrPanel.setQRText(content);
            this.mQrPanel.setQRExcetion(exception);
            this.mQrPanel.setVisibility(0);
        } else {
            this.mTextView = (TextView) this.mMainView.findViewById(R.id.epg_search_hot_exception_textview);
            this.mTextView.setTextColor(getColor(R.color.albumview_yellow_color));
            this.mTextView.setText(content.replaceAll("\\n", ""));
            this.mTextView.setVisibility(0);
        }
        setHistoryViewAdapter();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                SearchHotFragment.this.setClearViewFocus();
            }
        }, 500);
        hideLoading();
    }

    private void initSearchHotGridView() {
        if (this.mSearchHotGridView != null) {
            this.mSearchHotGridView.setNextRightFocusLeaveAvail(false);
            this.mSearchHotGridView.setNextUpFocusLeaveAvail(true);
            this.mSearchHotGridView.setNextDownFocusLeaveAvail(true);
            this.mSearchHotGridView.setParams(getHotWordsGrideViewParams());
            return;
        }
        LogUtils.e(LOG_TAG, ">>>>>> hot gridview is null");
    }

    private PhotoGridParams getHotWordsGrideViewParams() {
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = 2;
        p.verticalSpace = getDimen(R.dimen.dimen_8dp);
        p.horizontalSpace = getDimen(R.dimen.dimen_6dp);
        p.contentHeight = getDimen(R.dimen.dimen_46dp);
        p.contentWidth = getDimen(R.dimen.dimen_262dp);
        p.scaleRate = 1.1f;
        return p;
    }

    private void initSearchHistoryGridView() {
        if (this.mSearchHistoryGridView != null) {
            LogUtils.d(LOG_TAG, ">>>>>> init history gridview");
            this.mSearchHistoryGridView.setNextRightFocusLeaveAvail(false);
            this.mSearchHistoryGridView.setNextUpFocusLeaveAvail(true);
            this.mSearchHistoryGridView.setNextDownFocusLeaveAvail(false);
            return;
        }
        Log.e(LOG_TAG, ">>>>>> history gridview is null");
    }

    private void initSearchVipGridView() {
        this.mSearchVipGridView.setNextRightFocusLeaveAvail(false);
        this.mSearchVipGridView.setNextUpFocusLeaveAvail(false);
        this.mSearchVipGridView.setParams(getPhotoViewParams());
        this.mSearchVipGridView.setListener(this.mPhotoViewListener);
    }

    private PhotoGridParams getPhotoViewParams() {
        int itemWidth = getDimen(R.dimen.dimen_116dp);
        int itemHeight = getDimen(R.dimen.dimen_160dp);
        int itemPadding = getDimen(R.dimen.dimen_20dp);
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = 4;
        p.horizontalSpace = itemPadding;
        p.contentHeight = itemHeight;
        p.contentWidth = itemWidth;
        p.scaleRate = 1.1f;
        return p;
    }

    private void showVipView() {
        if (GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel() != null) {
            this.isSupportVipMonth = true;
        }
        if (this.isSupportVipMonth && !ListUtils.isEmpty(this.mAlbumDataList) && this.mSearchVipGridView != null && this.mVipAdapter != null) {
            this.mSearchVipGridView.setVisibility(0);
            this.mSearchVipGridView.setAdapter(this.mVipAdapter);
            this.mVipAdapter.notifyDataSetChanged(this.mAlbumDataList);
        }
    }

    private void setSearchHotLayoutMarginTop(int marginTopDimen) {
    }

    private void initLayout() {
        this.mSearchHotGridView = (PhotoGridView) this.mMainView.findViewById(R.id.epg_search_hot_gridview);
        this.mSearchHistoryGridView = (PhotoGridView) this.mMainView.findViewById(R.id.epg_search_history_gridview);
        this.mSearchHistoryLayout = (RelativeLayout) this.mMainView.findViewById(R.id.epg_search_history_layout);
        this.mHotTitleView = this.mMainView.findViewById(R.id.epg_search_hot_title_layout);
        this.mHistoryTitleView = this.mMainView.findViewById(R.id.epg_search_history_title_layout);
        this.mSearchVipGridView = (PhotoGridView) this.mMainView.findViewById(R.id.epg_search_vipList);
    }

    private boolean checkIsHasSearchHistory() {
        this.mHistoryBeanList = this.mHistoryDao.queryHistory(6);
        if (ListUtils.isEmpty(this.mHistoryBeanList)) {
            this.mHasHistory = false;
        } else {
            this.mHasHistory = true;
        }
        return this.mHasHistory;
    }

    private void setHotViewAdapter(List<String> hotDataSource) {
        if (this.mHotTitleView != null) {
            this.mHotTitleView.setVisibility(0);
        }
        if (!ListUtils.isEmpty((List) hotDataSource) && this.mSearchHotGridView != null && this.mContext != null) {
            this.mHotAdapter = new SearchHotAdapter(this.mContext, hotDataSource);
            this.mSearchHotGridView.setAdapter(this.mHotAdapter);
        }
    }

    private void setHistoryViewAdapter() {
        if (this.mContext != null && this.mHistoryTitleView != null) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    SearchHotFragment.this.mHasHistory = SearchHotFragment.this.checkIsHasSearchHistory();
                    if (SearchHotFragment.this.mHasHistory) {
                        SearchHotFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                SearchHotFragment.this.mSearchHistoryLayout.setVisibility(0);
                                SearchHotFragment.this.mSearchHistoryGridView.setParams(SearchHotFragment.this.getHistoryGrideViewParams(SearchHotFragment.this.mHistoryBeanList));
                                SearchHotFragment.this.setSearchHotLayoutMarginTop(R.dimen.dimen_48dp);
                                SearchHotFragment.this.mHistoryTitleView.setVisibility(0);
                                SearchHotFragment.this.mHistoryAdapter = new SearchHistoryAdapter(SearchHotFragment.this.mContext, SearchHotFragment.this.mHistoryBeanList);
                                SearchHotFragment.this.mSearchHistoryGridView.setAdapter(SearchHotFragment.this.mHistoryAdapter);
                                if (SearchHotFragment.this.mHistoryRequestFocus) {
                                    SearchHotFragment.this.mHistoryRequestFocus = false;
                                    View viewByPos = SearchHotFragment.this.mSearchHistoryGridView.getViewByPos(0);
                                    if (viewByPos != null) {
                                        viewByPos.requestFocus();
                                    }
                                }
                            }
                        });
                    } else {
                        SearchHotFragment.this.mHandler.post(new Runnable() {
                            public void run() {
                                SearchHotFragment.this.mSearchHistoryLayout.setVisibility(8);
                                SearchHotFragment.this.setSearchHotLayoutMarginTop(R.dimen.dimen_162dp);
                                SearchHotFragment.this.mSearchHotGridView.setNextDownFocusLeaveAvail(false);
                            }
                        });
                    }
                }
            });
        }
    }

    private PhotoGridParams getHistoryGrideViewParams(List<SearchHistoryBean> historyBeanList) {
        int i = 1;
        PhotoGridParams p = new PhotoGridParams();
        if (historyBeanList.size() != 1) {
            i = 2;
        }
        p.columnNum = i;
        p.verticalSpace = getDimen(R.dimen.dimen_8dp);
        p.horizontalSpace = getDimen(R.dimen.dimen_6dp);
        p.contentHeight = getDimen(R.dimen.dimen_46dp);
        p.contentWidth = getDimen(R.dimen.dimen_262dp);
        p.scaleRate = 1.1f;
        return p;
    }

    private void setClearViewFocus() {
        View focusView = null;
        int focusPos;
        if (this.mSearchHistoryLayout.getVisibility() == 8) {
            if (this.mHotAdapter != null) {
                focusPos = this.mHotAdapter.getCount() - 1;
                if (focusPos % 2 != 0) {
                    focusPos--;
                }
                if (this.mSearchHotGridView != null) {
                    focusView = this.mSearchHotGridView.getViewByPos(focusPos);
                }
            } else {
                return;
            }
        } else if (this.mHistoryAdapter != null) {
            focusPos = this.mHistoryAdapter.getCount() - 1;
            if (focusPos % 2 != 0) {
                focusPos--;
            }
            if (this.mSearchHistoryGridView != null) {
                focusView = this.mSearchHistoryGridView.getViewByPos(focusPos);
            }
        } else {
            return;
        }
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onChangeClearViewFocus(focusView);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mQrPanel != null && this.mQrPanel.getVisibility() == 0) {
            this.mQrPanel.setVisibility(4);
        }
        if (!(this.mTextView == null || this.mTextView.getVisibility() != 0 || ListUtils.isEmpty(mHotHordsCacheList))) {
            this.mTextView.setVisibility(4);
            prepareVipData(mHotHordsCacheList);
            showVipView();
            setHotViewAdapter(mHotHordsCacheList);
        }
        reloadVipAbout(requestCode);
        reloadHotAbout(requestCode);
        reloadHistoryAbout(requestCode);
    }

    private void reloadVipAbout(int requestCode) {
        if (this.mSearchVipGridView != null) {
            View viewByPos = this.mSearchVipGridView.getViewByPos(mVipClickPos);
            if (!(viewByPos == null || requestCode == 1 || requestCode == 2)) {
                viewByPos.requestFocus();
            }
            this.START_REQUESTCODE = 1;
            return;
        }
        Log.e(LOG_TAG, ">>>>>> vip gridview is null");
    }

    private void reloadHotAbout(int requestCode) {
        if (this.mSearchHotGridView != null) {
            initSearchHotGridView();
            setHotViewAdapter(mHotHordsCacheList);
            View viewByPos = this.mSearchHotGridView.getViewByPos(mHotClickPos);
            if (viewByPos != null && requestCode == 1) {
                viewByPos.requestFocus();
                return;
            }
            return;
        }
        Log.e(LOG_TAG, ">>>>>> hot gridview is null");
    }

    private void reloadHistoryAbout(int requestCode) {
        if (this.mSearchHistoryLayout == null || this.mSearchHistoryGridView == null) {
            Log.e(LOG_TAG, ">>>>>> mSearchHistoryGridView is null or mSearchHistoryLayout is null");
            return;
        }
        setHistoryViewAdapter();
        if (this.mHasHistory && requestCode == 2) {
            this.mHistoryRequestFocus = true;
            this.START_REQUESTCODE = 1;
        }
    }

    private List<String> filterList(List<String> list, int maxNum) {
        List<String> filterList = new ArrayList();
        if (list == null) {
            return filterList;
        }
        List vipList = null;
        if (this.mVipAdapter != null) {
            vipList = this.mVipAdapter.getVipHotWord();
        }
        if (ListUtils.isEmpty(vipList)) {
            return list.size() > 6 ? list.subList(0, 6) : list;
        } else {
            int preNum = list.size();
            int vipNum = vipList.size();
            for (int i = 0; i < preNum; i++) {
                String preValue = (String) list.get(i);
                int j = 0;
                while (j < vipNum) {
                    if (preValue.equals((String) vipList.get(j))) {
                        LogUtils.e(LOG_TAG, ">>>>> filterList --- ", preValue);
                        break;
                    }
                    j++;
                }
                if (j == vipNum) {
                    filterList.add(preValue);
                }
                if (filterList.size() >= 6) {
                    return filterList;
                }
            }
            return filterList;
        }
    }

    private void sendClickPingback(int tag, int pos) {
        int lineIndex = (pos / 2) + 1;
        int rowIndex = (pos % 2) + 1;
        PingBackParams params;
        if (tag == 0) {
            if (ListUtils.isLegal(this.mHotHordsList, pos)) {
                String selectedHotWord = (String) this.mHotHordsList.get(pos);
                params = new PingBackParams();
                params.add(Keys.T, "20").add("rseat", lineIndex + "_" + rowIndex).add("rpage", "search").add("block", "hotword").add("rt", "i").add("r", selectedHotWord);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            }
        } else if (tag == 1) {
            if (ListUtils.isLegal(this.mHistoryBeanList, pos)) {
                String selectedHistoryWord = ((SearchHistoryBean) this.mHistoryBeanList.get(pos)).getKeyword();
                params = new PingBackParams();
                params.add(Keys.T, "20").add("rseat", lineIndex + "_" + rowIndex).add("rpage", "search").add("block", "searchhistory").add("rt", "i").add("r", selectedHistoryWord);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            }
        } else if (tag == 3 && ListUtils.isLegal(this.mAlbumDataList, pos)) {
            String qpid;
            ChannelLabel mChannelLabel = (ChannelLabel) this.mAlbumDataList.get(pos);
            String plid = "";
            if (ResourceType.COLLECTION.equals(mChannelLabel.getType())) {
                plid = mChannelLabel.id;
            } else {
                plid = "";
            }
            if (pos == 3) {
                qpid = "vip";
            } else {
                qpid = mChannelLabel.albumQipuId;
            }
            params = new PingBackParams();
            params.add(Keys.T, "20").add("rseat", "1_" + (pos + 1)).add("rpage", "search").add("block", "viphot").add("rt", "i").add("plid", plid).add("r", qpid);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    public void requestDefaultFocus() {
        if (LogUtils.mIsDebug) {
            LogUtils.d("FOCUS_TEST", ">>>>> mSearchEvent.onRequestRightDefaultFocus() --- SearchRightDefault");
        }
        if (this.mQrPanel != null && (this.mQrPanel.getVisibility() == 0 || this.mSearchHotGridView == null)) {
            return;
        }
        View view;
        if (ListUtils.isEmpty(this.mAlbumDataList) || this.mSearchVipGridView == null) {
            view = this.mSearchHotGridView.getViewByPos(0);
            if (view != null) {
                view.requestFocus();
                return;
            }
            return;
        }
        view = this.mSearchVipGridView.getViewByPos(0);
        if (view != null) {
            view.requestFocus();
        }
    }
}
