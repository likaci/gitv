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
import com.gala.video.app.epg.R;
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

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSearchEvent != null) {
            this.mSearchEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(R.layout.epg_fragment_search_right_show_list, null);
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
        this.mSuggestGridView.setOnItemFocusChangedListener(new OnItemFocusChangedListener() {
            public void onItemFocusChanged(ViewGroup group, ViewHolder holder, boolean hasFocus) {
                TextView textView = holder.itemView;
                if (hasFocus) {
                    SearchSuggestFragment.this.changeTabValid(SearchSuggestFragment.this.getPageLocationType());
                    textView.setTextColor(SearchSuggestFragment.this.getColor(R.color.gala_write));
                    if (Project.getInstance().getBuild().isLitchi()) {
                        textView.setTextColor(SearchSuggestFragment.this.getColor(R.color.gala_write));
                    }
                    AnimationUtil.scaleAnimation(holder.itemView, 1.0f, 1.03f, 500);
                    return;
                }
                textView.setTextColor(SearchSuggestFragment.this.getColor(R.color.search_right_text_color));
                AnimationUtil.scaleAnimation(holder.itemView, 1.03f, 1.0f, 500);
            }
        });
        this.mSuggestGridView.setOnItemRecycledListener(new OnItemRecycledListener() {
            public void onItemRecycled(ViewGroup group, ViewHolder holder) {
            }
        });
        this.mSuggestGridView.setOnItemClickListener(new OnItemClickListener() {
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
                params.add(Keys.T, "20").add("rseat", (pos + 1) + "").add("rpage", "search").add("block", "suggest").add("rt", "i").add("r", seletedWord);
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
                            LogUtils.d(SearchSuggestFragment.LOG_TAG, ">>>>> name: ", str, "  type: ", str3, "  qpid: ", str2);
                        }
                        if (SearchSuggestFragment.this.mSearchEvent != null) {
                            SearchSuggestFragment.this.mSearchEvent.onSuggestClickPingback(SearchSuggestFragment.this.mApiResult.eventId, SearchSuggestFragment.this.mApiResult.data.site, viewHolder.getLayoutPosition() + 1, str, SearchSuggestFragment.this.mKeyWords, z);
                        }
                    }
                });
            }
        });
        this.mSuggestGridView.setOnFocusLostListener(new OnFocusLostListener() {
            public void onFocusLost(ViewGroup group, ViewHolder holder) {
                SearchSuggestFragment.this.setClearViewFocus();
            }
        });
    }

    private void initLayout() {
        this.mFeedbackStub = (ViewStub) this.mMainView.findViewById(R.id.epg_stub_feedback);
        this.mSuggestTitle = (TextView) this.mMainView.findViewById(R.id.epg_search_showlist_title_text);
        this.mSuggestGridView = (VerticalGridView) this.mMainView.findViewById(R.id.epg_suggest_gridview_v);
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
        this.mSuggestGridView.setContentWidth(getDimen(R.dimen.dimen_518dp));
        this.mSuggestGridView.setContentHeight(getDimen(R.dimen.dimen_80dp));
        this.mSuggestGridView.setVerticalMargin(getDimen(R.dimen.dimen_025dp));
        this.mSuggestGridView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        this.mSuggestGridView.setFocusPosition(0);
        this.mSuggestGridView.setFocusMode(1);
        this.mSuggestGridView.setFocusLeaveForbidden(Opcodes.IF_ICMPGT);
        this.mSuggestGridView.setShakeForbidden(66);
        this.mSuggestGridView.setScrollRoteScale(0.8f, 1.0f, 2.0f);
        this.mSuggestGridView.setScrollBarDrawable(R.drawable.epg_thumb);
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
        TVApi.suggestWords.call(new IApiCallback<ApiResultHotWords>() {
            public void onSuccess(ApiResultHotWords result) {
                SearchSuggestFragment.this.mApiResult = result;
                SearchSuggestFragment.this.mSuggestWords = result.data.getWords();
                SearchSuggestFragment.this.mHandler.post(new Runnable() {
                    public void run() {
                        SearchSuggestFragment.this.doAfterRequestSucc();
                    }
                });
            }

            public void onException(final ApiException exception) {
                LogUtils.e(SearchSuggestFragment.LOG_TAG, ">>>>>>>>>>>> Api.Suggest.call Exception: ", exception.getMessage());
                SearchPingbackUtils.error(exception, "TVApi.suggestWords");
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int arg0) {
                        SearchSuggestFragment.this.runOnUiThread(new Runnable() {
                            public void run() {
                                SearchSuggestFragment.this.doAfterRequestException(exception);
                            }
                        });
                    }
                });
            }
        }, this.mKeyWords);
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
                this.mQRFeedbackPanel = (GlobalQRFeedbackPanel) this.mMainView.findViewById(R.id.epg_search_qr_panel);
            }
            hideLoading();
            FeedBackModel model = CreateInterfaceTools.createFeedbackFactory().createFeedBack(exception);
            String content = model.getErrorMsg();
            if (model.isShowQR()) {
                this.mQRFeedbackPanel.setQRText(content);
                this.mQRFeedbackPanel.setQRExcetion(exception);
            } else {
                TextView textView = new TextView(this.mContext);
                QSizeUtils.setTextSize(textView, R.dimen.dimen_27dp);
                textView.setTextColor(getColor(R.color.albumview_yellow_color));
                textView.setText(content.replaceAll("\\n", ""));
                Drawable img = getDrawable(R.drawable.epg_no_album_text_warn);
                img.setBounds(0, 0, getDimen(R.dimen.dimen_32dp), getDimen(R.dimen.dimen_32dp));
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
            LogUtils.d("FOCUS_TEST", ">>>>> mSearchEvent.onRequestRightDefaultFocus() --- SearchRightShowListFragment");
        }
        if ((this.mQRFeedbackPanel == null || this.mQRFeedbackPanel.getVisibility() != 0) && this.mSuggestGridView != null) {
            this.mSuggestGridView.requestFocus();
            changeTabValid(getPageLocationType());
        }
    }
}
