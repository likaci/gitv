package com.gala.video.app.epg.ui.search.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Word;
import com.gala.tvapi.tv2.result.ApiResultHotWords;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.gala.video.albumlist4.widget.RecyclerView.OnFocusLostListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.search.adapter.SuggestGridAdapter;
import com.gala.video.app.epg.ui.search.db.SearchHistoryBean;
import com.gala.video.app.epg.ui.search.utils.SearchPingbackUtils;
import com.gala.video.app.epg.utils.QSizeUtils;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.FeedBackModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.List;

public class SearchSuggestFragment extends SearchBaseFragment {
    private static final String LOG_TAG = "EPG/search/SearchRightShowListFragment";
    private SuggestGridAdapter mAdapter;
    private ApiResultHotWords mApiResult;
    private ViewStub mFeedbackStub;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private String mKeyWords;
    private View mMainView;
    private GlobalQRFeedbackPanel mQRFeedbackPanel;
    private boolean mSubIsInflated;
    private VerticalGridView mSuggestGridView;
    private TextView mSuggestTitle;
    private List<Word> mSuggestWords;

    class C10081 implements OnItemFocusChangedListener {
        C10081() {
        }

        public void onItemFocusChanged(ViewGroup group, ViewHolder holder, boolean hasFocus) {
            TextView textView = holder.itemView;
            if (hasFocus) {
                SearchSuggestFragment.this.changeTabValid(SearchSuggestFragment.this.getPageLocationType());
                textView.setTextColor(SearchSuggestFragment.this.getColor(C0508R.color.gala_write));
                if (Project.getInstance().getBuild().isLitchi()) {
                    textView.setTextColor(SearchSuggestFragment.this.getColor(C0508R.color.gala_write));
                }
                AnimationUtil.scaleAnimation(holder.itemView, 1.0f, 1.03f, 500);
                return;
            }
            textView.setTextColor(SearchSuggestFragment.this.getColor(C0508R.color.search_right_text_color));
            AnimationUtil.scaleAnimation(holder.itemView, 1.03f, 1.0f, 500);
        }
    }

    class C10092 implements OnItemRecycledListener {
        C10092() {
        }

        public void onItemRecycled(ViewGroup group, ViewHolder holder) {
        }
    }

    class C10113 implements OnItemClickListener {
        C10113() {
        }

        public void onItemClick(ViewGroup group, ViewHolder holder) {
            if (SearchSuggestFragment.this.mContext != null && SearchEnterUtils.checkNetWork(SearchSuggestFragment.this.mContext)) {
                String name = ((TextView) holder.itemView).getText().toString().trim();
                String qpid = (String) holder.itemView.getTag(ISearchConstant.TAGKEY_SUGGEST_QPID);
                String type = (String) holder.itemView.getTag(ISearchConstant.TAGKEY_SUGGEST_TYPE);
                boolean isPerson = true;
                if (!StringUtils.equals(type, ISearchConstant.SUGGEST_TYPE_PERSON)) {
                    isPerson = false;
                    qpid = null;
                }
                SearchSuggestFragment.this.startSearch(1, name, qpid, 6, SearchSuggestFragment.this.START_REQUESTCODE);
                doInThread(holder, name, qpid, type, isPerson);
                sendClickPingback(holder.getLayoutPosition());
            }
        }

        private void sendClickPingback(int pos) {
            String seletedWord = ((Word) SearchSuggestFragment.this.mSuggestWords.get(pos)).name;
            PingBackParams params = new PingBackParams();
            params.add(Keys.f2035T, "20").add("rseat", (pos + 1) + "").add("rpage", "search").add("block", "suggest").add("rt", "i").add("r", seletedWord);
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }

        private void doInThread(ViewHolder holder, String name, String qpid, String type, boolean isPerson) {
            final String str = name;
            final String str2 = qpid;
            final String str3 = type;
            final ViewHolder viewHolder = holder;
            final boolean z = isPerson;
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    SearchSuggestFragment.this.mHistoryDao.insertHistory(new SearchHistoryBean(str, str2, str3), 6, true);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1570d(SearchSuggestFragment.LOG_TAG, ">>>>> name: ", str, "  type: ", str3, "  qpid: ", str2);
                    }
                    if (SearchSuggestFragment.this.mSearchEvent != null) {
                        SearchSuggestFragment.this.mSearchEvent.onSuggestClickPingback(SearchSuggestFragment.this.mApiResult.eventId, SearchSuggestFragment.this.mApiResult.data.site, viewHolder.getLayoutPosition() + 1, str, SearchSuggestFragment.this.mKeyWords, z);
                    }
                }
            });
        }
    }

    class C10124 implements OnFocusLostListener {
        C10124() {
        }

        public void onFocusLost(ViewGroup group, ViewHolder holder) {
            SearchSuggestFragment.this.setClearViewFocus();
        }
    }

    class C10165 implements IApiCallback<ApiResultHotWords> {

        class C10131 implements Runnable {
            C10131() {
            }

            public void run() {
                SearchSuggestFragment.this.doAfterRequestSucc();
            }
        }

        C10165() {
        }

        public void onSuccess(ApiResultHotWords result) {
            SearchSuggestFragment.this.mApiResult = result;
            SearchSuggestFragment.this.mSuggestWords = result.data.getWords();
            SearchSuggestFragment.this.mHandler.post(new C10131());
        }

        public void onException(final ApiException exception) {
            LogUtils.m1573e(SearchSuggestFragment.LOG_TAG, ">>>>>>>>>>>> Api.Suggest.call Exception: ", exception.getMessage());
            SearchPingbackUtils.error(exception, "TVApi.suggestWords");
            NetWorkManager.getInstance().checkNetWork(new StateCallback() {

                class C10141 implements Runnable {
                    C10141() {
                    }

                    public void run() {
                        SearchSuggestFragment.this.doAfterRequestException(exception);
                    }
                }

                public void getStateResult(int arg0) {
                    SearchSuggestFragment.this.runOnUiThread(new C10141());
                }
            });
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(C0508R.layout.epg_fragment_search_right_show_list, null);
        init();
        requestData();
        return this.mMainView;
    }

    public void onKeyBoardTextChanged(String keyWords) {
        this.mKeyWords = keyWords;
        requestData();
    }

    private void init() {
        this.mKeyWords = getArguments().getString("KEY_WORDS");
        initLayout();
        initGridView();
        this.mSuggestGridView.setOnItemFocusChangedListener(new C10081());
        this.mSuggestGridView.setOnItemRecycledListener(new C10092());
        this.mSuggestGridView.setOnItemClickListener(new C10113());
        this.mSuggestGridView.setOnFocusLostListener(new C10124());
    }

    private void initLayout() {
        this.mFeedbackStub = (ViewStub) this.mMainView.findViewById(C0508R.id.epg_stub_feedback);
        this.mSuggestTitle = (TextView) this.mMainView.findViewById(C0508R.id.epg_search_showlist_title_text);
        this.mSuggestGridView = (VerticalGridView) this.mMainView.findViewById(C0508R.id.epg_suggest_gridview_v);
    }

    private void initGridView() {
        if (this.mSuggestGridView != null) {
            setGridViewParams();
        } else {
            Log.e(LOG_TAG, ">>>>>> GridView is null");
        }
    }

    private void setGridViewParams() {
        this.mSuggestGridView.setOrientation(Orientation.VERTICAL);
        this.mSuggestGridView.setNumRows(1);
        this.mSuggestGridView.setContentWidth(getDimen(C0508R.dimen.dimen_518dp));
        this.mSuggestGridView.setContentHeight(getDimen(C0508R.dimen.dimen_80dp));
        this.mSuggestGridView.setVerticalMargin(getDimen(C0508R.dimen.dimen_025dp));
        this.mSuggestGridView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mSuggestGridView.setFocusPosition(0);
        this.mSuggestGridView.setFocusMode(1);
        this.mSuggestGridView.setFocusLeaveForbidden(Opcodes.IF_ICMPGT);
        this.mSuggestGridView.setShakeForbidden(66);
        this.mSuggestGridView.setScrollRoteScale(0.8f, 1.0f, 2.0f);
        this.mSuggestGridView.setScrollBarDrawable(C0508R.drawable.epg_thumb);
    }

    public void requestData() {
        this.mSuggestTitle.setVisibility(4);
        this.mSuggestGridView.setVisibility(4);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onChangeSuggestDisplay(false);
        }
        if (this.mQRFeedbackPanel != null) {
            this.mQRFeedbackPanel.setVisibility(4);
        }
        showLoading();
        TVApiBase.setOverseaFlag(this.mKeyWords);
        TVApi.suggestWords.call(new C10165(), this.mKeyWords);
    }

    private void doAfterRequestSucc() {
        if (this.mContext != null) {
            hideLoading();
            if (!ListUtils.isEmpty(this.mSuggestWords) || this.mSearchEvent == null) {
                this.mSuggestTitle.setVisibility(0);
                this.mSuggestGridView.setVisibility(0);
                setGridViewAdapter(this.mContext);
                return;
            }
            this.mSearchEvent.onSwitchFragment(new SearchDefaultFragment());
        }
    }

    private void doAfterRequestException(ApiException exception) {
        if (this.mContext != null) {
            if (!this.mSubIsInflated) {
                this.mFeedbackStub.inflate();
            }
            this.mSubIsInflated = true;
            if (this.mQRFeedbackPanel == null) {
                this.mQRFeedbackPanel = (GlobalQRFeedbackPanel) this.mMainView.findViewById(C0508R.id.epg_search_qr_panel);
            }
            hideLoading();
            FeedBackModel model = CreateInterfaceTools.createFeedbackFactory().createFeedBack(exception);
            String content = model.getErrorMsg();
            if (model.isShowQR()) {
                this.mQRFeedbackPanel.setQRText(content);
                this.mQRFeedbackPanel.setQRExcetion(exception);
            } else {
                TextView textView = new TextView(this.mContext);
                QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_27dp);
                textView.setTextColor(getColor(C0508R.color.albumview_yellow_color));
                textView.setText(content.replaceAll("\\n", ""));
                Drawable img = getDrawable(C0508R.drawable.epg_no_album_text_warn);
                img.setBounds(0, 0, getDimen(C0508R.dimen.dimen_32dp), getDimen(C0508R.dimen.dimen_32dp));
                textView.setCompoundDrawables(img, null, null, null);
                this.mQRFeedbackPanel.addMessageView(textView);
            }
            this.mQRFeedbackPanel.setVisibility(0);
            this.mSuggestGridView.setVisibility(4);
        }
    }

    private void setGridViewAdapter(Context context) {
        if (this.mAdapter == null) {
            this.mAdapter = new SuggestGridAdapter(context, this.mSuggestWords);
        } else {
            this.mAdapter.setSuggests(this.mSuggestWords);
        }
        this.mSuggestGridView.setAdapter(this.mAdapter);
        View view0 = this.mSuggestGridView.getViewByPosition(0);
        if (!(view0 == null || this.mSearchEvent == null)) {
            view0.setNextFocusUpId(view0.getId());
            this.mSearchEvent.onChangeSuggestDisplay(true);
        }
        View viewlast = this.mSuggestGridView.getViewByPosition(this.mSuggestWords.size() - 1);
        if (viewlast != null) {
            viewlast.setNextFocusDownId(viewlast.getId());
        }
        setClearViewFocus();
    }

    private void setClearViewFocus() {
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onChangeClearViewFocus(this.mSuggestGridView);
        }
    }

    public void requestDefaultFocus() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("FOCUS_TEST", ">>>>> mSearchEvent.onRequestRightDefaultFocus() --- SearchRightShowListFragment");
        }
        if ((this.mQRFeedbackPanel == null || this.mQRFeedbackPanel.getVisibility() != 0) && this.mSuggestGridView != null) {
            this.mSuggestGridView.requestFocus();
            changeTabValid(getPageLocationType());
        }
    }
}
