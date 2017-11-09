package com.gala.video.app.epg.ui.search.fragment;

import android.app.Activity;
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
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridParams;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.WidgetStatusListener;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.search.adapter.SearchHotAdapter;
import com.gala.video.app.epg.ui.search.adapter.SearchVipAdapter;
import com.gala.video.app.epg.ui.search.db.SearchHistoryBean;
import com.gala.video.app.epg.ui.search.utils.SearchPingbackUtils;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.uikit.data.provider.VipProvider;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class SearchDefaultFragment extends SearchBaseFragment {
    private static final String LOG_TAG = "EPG/search/SearchRightNoResultFragment";
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
                SearchDefaultFragment.this.changeTabValid(SearchDefaultFragment.this.getPageLocationType());
                textView.setEllipsize(TruncateAt.MARQUEE);
                textView.setTextColor(SearchDefaultFragment.this.getColor(R.color.gala_write));
                AnimationUtil.scaleAnimation(view, 1.0f, 1.07f, 200);
                return;
            }
            textView.setEllipsize(TruncateAt.END);
            textView.setTextColor(SearchDefaultFragment.this.getColor(R.color.search_right_text_color));
            AnimationUtil.scaleAnimation(view, 1.07f, 1.0f, 200);
        }

        public void onItemClick(ViewGroup viewGroup, View view, int position) {
            if (SearchDefaultFragment.this.mContext != null && SearchEnterUtils.checkNetWork(SearchDefaultFragment.this.mContext)) {
                String key = ((TextView) view).getText().toString().trim();
                SearchDefaultFragment.this.startSearch(2, key, null, 6, SearchDefaultFragment.this.START_REQUESTCODE);
                doInThread(key);
            }
        }

        private void doInThread(final String key) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    SearchDefaultFragment.this.mHistoryDao.insertHistory(new SearchHistoryBean(key), 6, true);
                }
            });
        }
    };
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private SearchHotAdapter mHotAdapter;
    private List<String> mHotHordsList;
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
            if (SearchDefaultFragment.this.mAlbumDataList != null && position < ListUtils.getCount(SearchDefaultFragment.this.mAlbumDataList)) {
                ChannelLabel item = (ChannelLabel) SearchDefaultFragment.this.mAlbumDataList.get(position);
                String title = item.desc;
                String from = "3";
                String buySource = "topic";
                if (position == 3) {
                    AlbumUtils.startChannelPage(SearchDefaultFragment.this.mContext, 1000002, from, buySource);
                } else {
                    ItemUtils.openDetailOrPlay(SearchDefaultFragment.this.mContext, item, title, from, buySource, null);
                }
                SearchDefaultFragment.this.sendClickPingback(3, position);
            }
        }
    };
    private RelativeLayout mSearchNoResultGridTitle;
    private PhotoGridView mSearchNoResultGridView;
    private PhotoGridView mSearchVipGridView;
    private SearchVipAdapter mVipAdapter;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onAttachActivity(this);
        }
    }

    public void onDetach() {
        super.onDetach();
        if (this.mVipAdapter != null) {
            this.mVipAdapter.onDetachedFromWindows();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(R.layout.epg_fragment_search_right_no_result, null);
        init(this.mMainView);
        showLoading();
        initInThread();
        return this.mMainView;
    }

    public void initInThread() {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                SearchDefaultFragment.this.requestData();
            }
        });
    }

    private void init(View view) {
        this.mSearchNoResultGridTitle = (RelativeLayout) this.mMainView.findViewById(R.id.epg_search_hot_title_layout);
        this.mSearchNoResultGridView = (PhotoGridView) this.mMainView.findViewById(R.id.epg_search_no_result_gridview);
        this.mSearchVipGridView = (PhotoGridView) this.mMainView.findViewById(R.id.epg_search_no_result_vipList);
        initSearchVipGridView();
        initSearchHotGridView();
        this.mSearchNoResultGridView.setListener(this.mGridViewListener);
    }

    private void requestData() {
        TVApi.hotWords.call(new IApiCallback<ApiResultHotWords>() {
            public void onSuccess(final ApiResultHotWords result) {
                SearchDefaultFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        if (result == null || result.data == null) {
                            LogUtils.e(SearchDefaultFragment.LOG_TAG, "requestData --- onSuccess result is null");
                        } else {
                            SearchDefaultFragment.this.doAfterRequestSucc(result.data.hotwords);
                        }
                    }
                });
            }

            public void onException(final ApiException exception) {
                LogUtils.e(SearchDefaultFragment.LOG_TAG, ">>>>>>>>>>>> Api.hotword.call Exception: ", exception.getMessage());
                SearchPingbackUtils.error(exception, "TVApi.hotWords");
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int arg0) {
                        SearchDefaultFragment.this.runOnUiThread(new Runnable() {
                            public void run() {
                                SearchDefaultFragment.this.doAfterRequestException(exception);
                                SearchDefaultFragment.this.mSearchVipGridView.setVisibility(8);
                            }
                        });
                    }
                });
            }
        }, "");
    }

    private void doAfterRequestSucc(List<String> hotwords) {
        if (this.mContext == null || isRemoving()) {
            LogUtils.e(LOG_TAG, "doAfterRequestSucc --- 页面已经退出");
            return;
        }
        this.mSearchNoResultGridView.setVisibility(0);
        this.mSearchNoResultGridTitle.setVisibility(0);
        prepareVipData(hotwords);
        hideLoading();
        showVipView();
        setHotAdapter();
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                SearchDefaultFragment.this.setClearViewFocus();
            }
        }, 500);
    }

    private void doAfterRequestException(ApiException exception) {
        GlobalQRFeedbackPanel qrPanel = (GlobalQRFeedbackPanel) this.mMainView.findViewById(R.id.epg_search_noresult_QR_panel);
        qrPanel.setQRText(CreateInterfaceTools.createFeedbackFactory().createFeedBack(exception).getErrorMsg());
        qrPanel.setQRExcetion(exception);
        qrPanel.setVisibility(0);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                SearchDefaultFragment.this.setClearViewFocus();
            }
        }, 500);
        hideLoading();
    }

    private void setHotAdapter() {
        if (this.mContext != null) {
            this.mHotAdapter = new SearchHotAdapter(this.mContext, mHotHordsCacheList);
            this.mSearchNoResultGridView.setAdapter(this.mHotAdapter);
            setClearViewFocus();
        }
    }

    private void setClearViewFocus() {
        int focusPos = 0;
        if (this.mHotAdapter != null) {
            focusPos = this.mHotAdapter.getCount() - 1;
        }
        if (focusPos % 2 != 0) {
            focusPos--;
        }
        if (this.mSearchNoResultGridView != null) {
            View focusView = this.mSearchNoResultGridView.getViewByPos(focusPos);
            if (this.mSearchEvent != null) {
                this.mSearchEvent.onChangeClearViewFocus(focusView);
            }
        }
    }

    private void initSearchHotGridView() {
        if (this.mSearchNoResultGridView != null) {
            Log.v(LOG_TAG, ">>>>>> init hot gridview");
            this.mSearchNoResultGridView.setNextRightFocusLeaveAvail(false);
            this.mSearchNoResultGridView.setNextUpFocusLeaveAvail(true);
            this.mSearchNoResultGridView.setNextDownFocusLeaveAvail(false);
            this.mSearchNoResultGridView.setParams(getNoResultGridViewParams());
            return;
        }
        Log.e(LOG_TAG, ">>>>>> hot gridview is null");
    }

    private PhotoGridParams getNoResultGridViewParams() {
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = 2;
        p.verticalSpace = getDimen(R.dimen.dimen_8dp);
        p.horizontalSpace = getDimen(R.dimen.dimen_6dp);
        p.contentHeight = getDimen(R.dimen.dimen_46dp);
        p.contentWidth = getDimen(R.dimen.dimen_262dp);
        p.scaleRate = 1.1f;
        return p;
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
        if (tag == 3) {
            ChannelLabel mChannelLabel = (ChannelLabel) this.mAlbumDataList.get(pos);
            String plid = "";
            if (ResourceType.COLLECTION.equals(mChannelLabel.getType())) {
                plid = mChannelLabel.id;
            } else {
                plid = "";
            }
            PingBackParams params = new PingBackParams();
            params.add(Keys.T, "20").add("rseat", "1_" + (pos + 1)).add("rpage", "search").add("block", "viphot").add("rt", "i").add("plid", plid);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    public void requestDefaultFocus() {
        if (this.mSearchNoResultGridView == null) {
            return;
        }
        View view;
        if (ListUtils.isEmpty(this.mAlbumDataList) || this.mSearchVipGridView == null) {
            view = this.mSearchNoResultGridView.getViewByPos(0);
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
