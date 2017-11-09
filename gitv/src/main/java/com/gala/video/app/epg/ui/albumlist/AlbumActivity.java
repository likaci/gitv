package com.gala.video.app.epg.ui.albumlist;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewStub;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.Tag;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.result.ApiResultChannelList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.hdata.task.ChannelRequestTask;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarItemView;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher.IMessageNotification;
import com.gala.video.app.epg.ui.albumlist.adapter.AlbumTopAdapter.onFocusChangedListener;
import com.gala.video.app.epg.ui.albumlist.common.AlbumProviderHelper;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.data.api.ChannelApi;
import com.gala.video.app.epg.ui.albumlist.data.api.SearchResultApi;
import com.gala.video.app.epg.ui.albumlist.enums.IAlbumEnum.AlbumFragmentLocation;
import com.gala.video.app.epg.ui.albumlist.event.IAlbumBaseEvent;
import com.gala.video.app.epg.ui.albumlist.factory.AlbumFragmentFactory;
import com.gala.video.app.epg.ui.albumlist.fragment.AlbumBaseFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.left.AlbumBaseLeftFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.AlbumBaseRightFragment;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel;
import com.gala.video.app.epg.ui.albumlist.multimenu.MultiMenuPanel;
import com.gala.video.app.epg.ui.albumlist.multimenu.MultiMenuPanel.MultiMenuPanelListenter;
import com.gala.video.app.epg.ui.albumlist.presenter.AlbumTopPresenter;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.DebugUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.ProgressBarItem;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class AlbumActivity extends QMultiScreenActivity implements IAlbumBaseEvent {
    private static final String LOG_TAG = "EPG/album4/AlbumActivity";
    private static boolean NOLOG = (!DebugUtils.ALBUM4_NEEDLOG);
    private boolean firstShowResultPanel = true;
    private boolean isOpenedH5;
    private AlbumTopPresenter mActionBarPresenter;
    onFocusChangedListener mAlbumTopChangedListener = new onFocusChangedListener() {
        public void onFocusChanged(View view, boolean hasFocus) {
            if (hasFocus) {
                AlbumActivity.this.setGlobalLastFocusView(view);
            }
            if (AlbumInfoFactory.isFootPage(AlbumActivity.this.mInfoModel.getPageType()) && AlbumActivity.this.getMenuDesTxt() != null) {
                if (hasFocus && AlbumActivity.this.getMenuDesTxt().getVisibility() != 4) {
                    AlbumActivity.this.setTopMenuLayoutVisible(4);
                } else if (AlbumActivity.this.mInfoModel != null && AlbumActivity.this.mInfoModel.isRightFragmentHasData() && AlbumActivity.this.getMenuDesTxt().getVisibility() != 0) {
                    AlbumActivity.this.setTopMenuLayoutVisible(0);
                }
            }
        }
    };
    private final Handler mBaseHandler = new Handler(Looper.getMainLooper());
    private TextView mChannelNameTxt;
    private AlbumBaseLeftFragment mCurLeftFragment;
    private AlbumBaseRightFragment mCurRightFragment;
    private BaseDataApi mDataApi;
    private View mGlobalLastFocusView;
    private AlbumInfoModel mInfoModel;
    private List<AlbumBaseFragment> mLeftFragmentCacheList;
    private FrameLayout mLeftLayout;
    private RelativeLayout mMainLayout;
    private TextView mMenuDesTxt;
    private View mMenuView;
    private IMessageNotification mMessageNotification = new IMessageNotification() {
        public void onMessageReceive(IMsgContent content) {
            AlbumActivity.this.mActionBarPresenter.onMessageReceive(content);
        }
    };
    private GlobalQRFeedbackPanel mNoResultPanel;
    private String mPageType;
    private ProgressBarItem mProgressBar;
    private Runnable mProgressBarRunable = new Runnable() {
        public void run() {
            AlbumActivity.this.getStatusLayout().setVisibility(0);
            AlbumActivity.this.getNoResultPanel().setVisibility(8);
            AlbumActivity.this.getProgressBar().setVisibility(0);
            AlbumActivity.this.getRightLayout().setVisibility(8);
            AlbumActivity.this.firstShowResultPanel = false;
        }
    };
    private List<AlbumBaseFragment> mRightFragmentCacheList;
    private FrameLayout mRightLayout;
    private String mSaveMenuDesTxt;
    private RelativeLayout mStatusLayout;
    private ImageView mTagDesLine;
    private TextView mTagDesTxt;
    private RelativeLayout mTopLayout;
    private String mTopMenuDescExpandString = ("<font color= '#" + ResourceUtil.getColorLength6(R.color.albumview_menu_color) + "'>按</font><font color='#" + ResourceUtil.getColorLength6(R.color.albumview_yellow_color) + "'>" + ResourceUtil.getStr(R.string.alter_menukey_text) + "</font><font color= '#" + ResourceUtil.getColorLength6(R.color.albumview_menu_color) + "'>");

    private static class FetchApiImpl implements Runnable {
        private WeakReference<AlbumActivity> mOuter;

        public FetchApiImpl(AlbumActivity outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void run() {
            AlbumActivity outer = (AlbumActivity) this.mOuter.get();
            if (outer != null) {
                outer.fetchChannelListApi();
            }
        }
    }

    private static class IApiCallbackImpl implements IApiCallback<ApiResultChannelList> {
        private WeakReference<AlbumActivity> mOuter;

        public IApiCallbackImpl(AlbumActivity outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onSuccess(ApiResultChannelList result) {
            String str = null;
            final AlbumActivity outer = (AlbumActivity) this.mOuter.get();
            if (outer != null) {
                if (result == null || result.data == null) {
                    if (!AlbumActivity.NOLOG) {
                        str = "onCreate---tvapi.getchannellist, success but return.result=" + result;
                    }
                    outer.log(str);
                    return;
                }
                outer.log("onCreate---tvapi.getchannellist, success");
                new ChannelRequestTask(null).saveChannels(result.data);
                outer.runOnUiThread(new Runnable() {
                    public void run() {
                        outer.showPage();
                    }
                });
            }
        }

        public void onException(ApiException exception) {
            final AlbumActivity outer = (AlbumActivity) this.mOuter.get();
            if (outer != null) {
                outer.log(AlbumActivity.NOLOG ? null : "onCreate---tvapi.getchannellist, fail, before net check,exception=" + exception);
                final long time1 = System.currentTimeMillis();
                NetWorkManager.getInstance().checkNetWork(new StateCallback() {
                    public void getStateResult(int state) {
                        outer.log(AlbumActivity.NOLOG ? null : "onCreate--- tvapi.getchannellist, fail, net work check,consume=" + (System.currentTimeMillis() - time1));
                    }
                });
                QAPingback.error(AlbumActivity.LOG_TAG, String.valueOf(outer.mInfoModel.getChannelId()), outer.mInfoModel.getDataTagName(), exception);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(-2);
        ImageProviderApi.getImageProvider().stopAllTasks();
        setContentView(R.layout.epg_q_album_activity);
        initIntentModel();
        initTopView();
        initData();
    }

    private void initData() {
        String str = null;
        boolean isSearchResultPage = AlbumInfoFactory.isSearchResultPage(this.mInfoModel.getPageType());
        log(NOLOG ? null : "onCreate---isSearchResultPage=" + isSearchResultPage);
        if (AlbumProviderHelper.isHasSetChannelList() || isSearchResultPage) {
            if (!NOLOG) {
                str = "onCreate---channelList != null, start show page";
            }
            log(str);
            showPage();
            return;
        }
        if (!NOLOG) {
            str = "onCreate---channelList == null, start : tvapi.getchannellist";
        }
        log(str);
        ThreadUtils.execute(new FetchApiImpl(this));
    }

    private void showPage() {
        initDataInfo();
        initFragment();
    }

    private void fetchChannelListApi() {
        TVApi.channelList.call(new IApiCallbackImpl(this), Project.getInstance().getBuild().getVersionString(), "1", "60");
    }

    protected void onStart() {
        super.onStart();
        MessagePromptDispatcher.get().register(this.mMessageNotification);
        this.mActionBarPresenter.onStart();
    }

    protected void onStop() {
        this.mActionBarPresenter.onStop();
        super.onStop();
        MessagePromptDispatcher.get().unregister(this.mMessageNotification);
        trimActivityMemory();
    }

    private void trimActivityMemory() {
        String model = Build.MODEL;
        LogUtils.d(LOG_TAG, "model：" + model);
        LogUtils.d(LOG_TAG, "isLoaderWEBActivity：" + isLoaderWEBActivity);
        if (model != null && model.toLowerCase().equals("mibox3s") && !isLoaderWEBActivity) {
            try {
                cleanMemory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initTopView() {
        this.mActionBarPresenter = new AlbumTopPresenter(this, getTopLayout(), this.mInfoModel);
        this.mActionBarPresenter.setFocusChangedListener(this.mAlbumTopChangedListener);
    }

    private void initIntentModel() {
        String str = null;
        AlbumIntentModel intentModel = null;
        Intent intent = getIntent();
        if (intent != null) {
            try {
                intentModel = (AlbumIntentModel) intent.getSerializableExtra("intent_model");
            } catch (Exception e) {
                e.printStackTrace();
                finish();
            }
            if (intentModel == null) {
                int channelId = intent.getIntExtra("intent_channel_id", -1);
                String channelName = AlbumInfoFactory.getChannelNameByChannelId(channelId);
                intentModel = new AlbumIntentModel();
                intentModel.setFrom(AlbumEnterFactory.CHANNEL_STR + channelId + AlbumEnterFactory.SIGN_STR);
                intentModel.setChannelId(channelId);
                intentModel.setChannelName(channelName);
                log(NOLOG ? null : "initIntent---by setChannelId");
            }
        }
        this.mInfoModel = new AlbumInfoModel(intentModel);
        log(NOLOG ? null : this.mInfoModel + DebugUtils.print());
        if (!NOLOG) {
            str = this.mInfoModel + DebugUtils.print();
        }
        logRecord(str);
    }

    private void initDataInfo() {
        this.mPageType = this.mInfoModel.getPageType();
        if (IAlbumConfig.CHANNEL_PAGE.equals(this.mPageType)) {
            this.mDataApi = new ChannelApi(this.mInfoModel);
        } else if (AlbumInfoFactory.isSearchResultPage(this.mInfoModel.getPageType())) {
            this.mDataApi = new SearchResultApi(this.mInfoModel);
        } else if (IAlbumConfig.CHANNEL_API_PAGE.equals(this.mPageType)) {
            String type;
            Tag tag;
            String dataTagType = this.mInfoModel.getDataTagType();
            if (StringUtils.equals(dataTagType, IAlbumConfig.LABEL_LABEL)) {
                type = SourceTool.LABEL_CHANNEL_TAG;
            } else if (StringUtils.equals(dataTagType, IAlbumConfig.LABEL_MENU)) {
                type = "-100";
            } else if (StringUtils.equals(dataTagType, IAlbumConfig.LABEL_CHANNEL)) {
                type = SourceTool.VIRTUAL_CHANNEL_TAG;
            } else {
                type = "-100";
            }
            String dataTagId;
            String str;
            if (this.mInfoModel.getLayoutKind() == null) {
                dataTagId = this.mInfoModel.getDataTagId();
                if (this.mInfoModel.getDataTagName() == null) {
                    str = "";
                } else {
                    str = this.mInfoModel.getDataTagName();
                }
                tag = new Tag(dataTagId, str, type);
            } else {
                dataTagId = this.mInfoModel.getDataTagId();
                if (this.mInfoModel.getDataTagName() == null) {
                    str = "";
                } else {
                    str = this.mInfoModel.getDataTagName();
                }
                tag = new Tag(dataTagId, str, type, this.mInfoModel.getLayoutKind());
            }
            this.mDataApi = new ChannelApi(this.mInfoModel);
            this.mDataApi.resetApi(tag);
        } else {
            this.mDataApi = new ChannelApi(this.mInfoModel);
        }
    }

    private void initFragment() {
        String str = null;
        this.mLeftFragmentCacheList = new ArrayList(2);
        this.mRightFragmentCacheList = new ArrayList(2);
        AlbumBaseFragment[] fragments = AlbumFragmentFactory.create(this.mPageType);
        AlbumBaseFragment lf = fragments[0];
        initViewByCondition(lf);
        AlbumBaseFragment rf = fragments[1];
        replaceFragment(rf);
        log(NOLOG ? null : "initFragment---left=" + lf + "---right=" + rf);
        if (!NOLOG) {
            str = "initFragment---left=" + lf + "---right=" + rf;
        }
        logRecord(str);
    }

    private void initViewByCondition(AlbumBaseFragment lf) {
        boolean noLeftLayout;
        getTopLayout().setVisibility(0);
        setTopChannelNameTxt(this.mInfoModel.getChannelName());
        if (lf == null || this.mInfoModel.isNoLeftFragment()) {
            noLeftLayout = true;
        } else {
            noLeftLayout = false;
        }
        if (noLeftLayout) {
            ((LayoutParams) getStatusLayout().getLayoutParams()).addRule(11, 0);
            return;
        }
        getLeftLayout().setVisibility(0);
        replaceFragment(lf);
    }

    public void replaceFragment(AlbumBaseFragment fragment) {
        String str = null;
        if (fragment != null && !isFinishing()) {
            log(NOLOG ? null : "replaceFragment---" + fragment);
            if (!NOLOG) {
                str = "replaceFragment---" + fragment;
            }
            logRecord(str);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            AlbumFragmentLocation locationType = fragment.getLocationType();
            try {
                for (AlbumBaseFragment f : this.mLeftFragmentCacheList) {
                    removeFragment(f);
                }
                for (AlbumBaseFragment f2 : this.mRightFragmentCacheList) {
                    removeFragment(f2);
                }
            } catch (Exception e) {
            }
            if (locationType == AlbumFragmentLocation.LEFT) {
                this.mLeftFragmentCacheList.clear();
                getLeftLayout().removeAllViewsInLayout();
                this.mCurLeftFragment = (AlbumBaseLeftFragment) fragment;
                this.mLeftFragmentCacheList.add(this.mCurRightFragment);
                transaction.replace(R.id.epg_q_album_left_panel, this.mCurLeftFragment);
            } else if (locationType == AlbumFragmentLocation.RIGHT) {
                this.mRightFragmentCacheList.clear();
                getRightLayout().removeAllViewsInLayout();
                this.mCurRightFragment = (AlbumBaseRightFragment) fragment;
                this.mRightFragmentCacheList.add(this.mCurRightFragment);
                transaction.replace(R.id.epg_q_album_right_panel, this.mCurRightFragment);
            }
            transaction.commitAllowingStateLoss();
        } else if (fragment != null) {
            if (!NOLOG) {
                str = "replaceFragment error f=" + fragment;
            }
            log(str);
        }
    }

    public void addFragment(AlbumBaseFragment f) {
        String str = null;
        if (f != null && !isFinishing()) {
            if (!NOLOG) {
                str = "addFragment---" + f;
            }
            log(str);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            AlbumFragmentLocation locationType = f.getLocationType();
            if (locationType == AlbumFragmentLocation.LEFT) {
                if (this.mCurLeftFragment != null) {
                    transaction.hide(this.mCurLeftFragment);
                }
                this.mCurLeftFragment = (AlbumBaseLeftFragment) f;
                this.mLeftFragmentCacheList.add(this.mCurLeftFragment);
                transaction.add(R.id.epg_q_album_left_panel, this.mCurLeftFragment);
            } else if (locationType == AlbumFragmentLocation.RIGHT) {
                if (this.mCurRightFragment != null) {
                    transaction.hide(this.mCurRightFragment);
                }
                this.mCurRightFragment = (AlbumBaseRightFragment) f;
                this.mRightFragmentCacheList.add(this.mCurRightFragment);
                transaction.add(R.id.epg_q_album_right_panel, this.mCurRightFragment);
            }
            transaction.commitAllowingStateLoss();
        } else if (f != null) {
            if (!NOLOG) {
                str = "addFragment---error--f=" + f;
            }
            log(str);
        }
    }

    public AlbumBaseFragment getFragmentAddingRight(int pos) {
        if (ListUtils.isLegal(this.mRightFragmentCacheList, pos)) {
            return (AlbumBaseFragment) this.mRightFragmentCacheList.get(pos);
        }
        return null;
    }

    public AlbumBaseFragment getFragmentAddingLeft(int pos) {
        if (ListUtils.isLegal(this.mLeftFragmentCacheList, pos)) {
            return (AlbumBaseFragment) this.mLeftFragmentCacheList.get(pos);
        }
        return null;
    }

    public void removeFragment(AlbumBaseFragment f) {
        String str = null;
        if (f != null && !isFinishing()) {
            if (!NOLOG) {
                str = "removeFragment---" + f;
            }
            log(str);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            AlbumFragmentLocation locationType = f.getLocationType();
            AlbumBaseFragment lastFragment = null;
            int count;
            if (locationType == AlbumFragmentLocation.LEFT) {
                if (getFragmentManager().findFragmentById(f.getId()) != null) {
                    transaction.remove(f);
                }
                if (this.mLeftFragmentCacheList.contains(f)) {
                    this.mLeftFragmentCacheList.remove(f);
                }
                count = ListUtils.getCount(this.mLeftFragmentCacheList);
                if (count > 0) {
                    lastFragment = (AlbumBaseFragment) this.mLeftFragmentCacheList.get(count - 1);
                    transaction.show(lastFragment);
                    this.mCurLeftFragment = (AlbumBaseLeftFragment) lastFragment;
                }
            } else if (locationType == AlbumFragmentLocation.RIGHT) {
                if (getFragmentManager().findFragmentById(f.getId()) != null) {
                    transaction.remove(f);
                }
                if (this.mRightFragmentCacheList.contains(f)) {
                    this.mRightFragmentCacheList.remove(f);
                }
                count = ListUtils.getCount(this.mRightFragmentCacheList);
                if (count > 0) {
                    lastFragment = (AlbumBaseFragment) this.mRightFragmentCacheList.get(count - 1);
                    transaction.show(lastFragment);
                    this.mCurRightFragment = (AlbumBaseRightFragment) lastFragment;
                }
            }
            transaction.commitAllowingStateLoss();
            if (lastFragment != null && lastFragment.getView() != null) {
                lastFragment.getView().requestFocus();
            }
        } else if (f != null) {
            if (!NOLOG) {
                str = "removeFragment---error--f=" + f;
            }
            log(str);
        }
    }

    public void showProgress() {
        this.mBaseHandler.removeCallbacks(this.mProgressBarRunable);
        this.mBaseHandler.postDelayed(this.mProgressBarRunable, (long) AlbumInfoFactory.getLoadingViewDelayedMillis(this.mInfoModel.getPageType()));
    }

    public void showProgressWithoutDelay() {
        this.firstShowResultPanel = false;
        showProgress();
    }

    public void showHasResultPanel() {
        this.mBaseHandler.removeCallbacks(this.mProgressBarRunable);
        if (!this.firstShowResultPanel) {
            getStatusLayout().setFocusable(false);
            getStatusLayout().setVisibility(8);
            getNoResultPanel().setVisibility(8);
            getProgressBar().setVisibility(8);
            getRightLayout().setVisibility(0);
        }
        this.firstShowResultPanel = false;
    }

    public Bitmap showNoResultPanel(ErrorKind kind, ApiException e) {
        this.mBaseHandler.removeCallbacks(this.mProgressBarRunable);
        getStatusLayout().setFocusable(true);
        getStatusLayout().setVisibility(0);
        getNoResultPanel().setVisibility(0);
        getProgressBar().setVisibility(8);
        getRightLayout().setVisibility(8);
        this.firstShowResultPanel = false;
        return GetInterfaceTools.getUICreator().maketNoResultView(this, getNoResultPanel(), kind, e);
    }

    private TextView getChannelNameTxt() {
        if (this.mChannelNameTxt == null) {
            this.mChannelNameTxt = (TextView) getTopLayout().findViewById(R.id.epg_q_album_channel_name_txt);
        }
        return this.mChannelNameTxt;
    }

    private FrameLayout getRightLayout() {
        if (this.mRightLayout == null) {
            this.mRightLayout = (FrameLayout) findViewById(R.id.epg_q_album_right_panel);
        }
        return this.mRightLayout;
    }

    private FrameLayout getLeftLayout() {
        if (this.mLeftLayout == null && !this.mInfoModel.isNoLeftFragment()) {
            this.mLeftLayout = (FrameLayout) findViewById(R.id.epg_q_album_left_panel);
        }
        return this.mLeftLayout;
    }

    private RelativeLayout getMainLayout() {
        if (this.mMainLayout == null) {
            this.mMainLayout = (RelativeLayout) findViewById(R.id.epg_q_album_main_panel);
        }
        return this.mMainLayout;
    }

    private RelativeLayout getStatusLayout() {
        if (this.mStatusLayout == null) {
            this.mStatusLayout = (RelativeLayout) ((ViewStub) findViewById(R.id.epg_q_album_right_status_layout)).inflate();
            this.mStatusLayout.setOnFocusChangeListener(new OnFocusChangeListener() {
                public void onFocusChange(View v, boolean hasFocus) {
                    if (AlbumActivity.this.getNoResultPanel() != null && AlbumActivity.this.getNoResultPanel().getVisibility() == 0 && AlbumActivity.this.getNoResultPanel().getButton() != null) {
                        AlbumActivity.this.getNoResultPanel().getButton().requestFocus();
                    }
                }
            });
        }
        return this.mStatusLayout;
    }

    private ProgressBarItem getProgressBar() {
        if (this.mProgressBar == null) {
            this.mProgressBar = (ProgressBarItem) findViewById(R.id.epg_q_album_right_data_progress);
            this.mProgressBar.setText(getString(R.string.album_list_loading));
        }
        return this.mProgressBar;
    }

    public GlobalQRFeedbackPanel getNoResultPanel() {
        if (this.mNoResultPanel == null) {
            getStatusLayout();
            this.mNoResultPanel = (GlobalQRFeedbackPanel) findViewById(R.id.epg_q_album_right_data_no_result_panel);
            if (this.mNoResultPanel.getButton() != null) {
                final OnFocusChangeListener listener = this.mNoResultPanel.getButton().getOnFocusChangeListener();
                this.mNoResultPanel.getButton().setOnFocusChangeListener(new OnFocusChangeListener() {
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (listener != null) {
                            listener.onFocusChange(v, hasFocus);
                        }
                        if (hasFocus) {
                            AlbumActivity.this.setNextFocusUpId(v);
                        }
                    }
                });
            }
        }
        return this.mNoResultPanel;
    }

    private RelativeLayout getTopLayout() {
        if (this.mTopLayout == null) {
            this.mTopLayout = (RelativeLayout) findViewById(R.id.epg_q_album_top_panel);
        }
        return this.mTopLayout;
    }

    public void setTopChannelNameTxtVisible(int visible) {
        if (getChannelNameTxt() != null) {
            getChannelNameTxt().setVisibility(visible);
        }
    }

    public void setTopChannelNameTxt(String name) {
        if (getChannelNameTxt() != null) {
            TextView channelNameTxt = getChannelNameTxt();
            if (AlbumInfoFactory.isSearchResultPage(this.mInfoModel.getPageType())) {
                name = IAlbumConfig.STR_SEARCH;
            }
            channelNameTxt.setText(name);
            setTopChannelNameTxtVisible(0);
        }
    }

    public void setTopTagLayoutVisible(int visible) {
        if (getTagDesTxt() != null) {
            getTagDesTxt().setVisibility(visible);
        }
        if (getTagDesLine() != null && getTagDesLine().getVisibility() != visible) {
            getTagDesLine().setVisibility(visible);
        }
    }

    public void setTopMenuLayoutVisible(int visible) {
        if (getMenuDesTxt() != null) {
            getMenuDesTxt().setVisibility(visible);
        }
    }

    private TextView getTagDesTxt() {
        if (this.mTagDesTxt == null) {
            this.mTagDesTxt = (TextView) getTopLayout().findViewById(R.id.epg_q_album_tag_des);
        }
        return this.mTagDesTxt;
    }

    private ImageView getTagDesLine() {
        if (this.mTagDesLine == null) {
            this.mTagDesLine = (ImageView) getTopLayout().findViewById(R.id.epg_q_album_tag_cutting_line);
        }
        return this.mTagDesLine;
    }

    public void setTopTagTxt(String tagDesTxt, String tagNameTxt, String tagCountTxt) {
        if (IAlbumConfig.UNIQUE_CHANNEL_RECOMMEND1.equals(this.mInfoModel.getIdentification()) || IAlbumConfig.UNIQUE_CHANNEL_RECOMMEND2.equals(this.mInfoModel.getIdentification())) {
            setTopTagLayoutVisible(8);
            return;
        }
        if (AlbumInfoFactory.isSearchResultPage(this.mInfoModel.getPageType())) {
            CharSequence tagCountTxt2 = null;
        }
        if (!IAlbumConfig.CINEMA_PAGE.equals(this.mPageType)) {
            if (AlbumInfoFactory.isHotChannel(this.mInfoModel.getChannelId(), this.mInfoModel.getPageType())) {
                setTopTagLayoutVisible(8);
            } else if (getTagDesTxt() != null) {
                boolean desEmpty = StringUtils.isEmpty((CharSequence) tagDesTxt);
                boolean nameEmpty = StringUtils.isEmpty((CharSequence) tagNameTxt);
                boolean countEmpty = StringUtils.isEmpty(tagCountTxt2);
                if (countEmpty && desEmpty && nameEmpty) {
                    setTopTagLayoutVisible(8);
                    return;
                }
                String blankStr = " ";
                String str = blankStr;
                if (!desEmpty) {
                    str = str + tagDesTxt + blankStr;
                }
                if (!nameEmpty) {
                    str = str + tagNameTxt + blankStr;
                }
                if (!countEmpty) {
                    str = str + tagCountTxt2 + blankStr;
                }
                setTopTagLayoutVisible(0);
                if (!StringUtils.isEmpty((CharSequence) str)) {
                    str = str.trim();
                }
                getTagDesTxt().setText(str);
            }
        }
    }

    public void setTopMenuDesTxt(String menuDesTxt) {
        if (getMenuDesTxt() != null) {
            if (StringUtils.isEmpty((CharSequence) menuDesTxt)) {
                setTopMenuLayoutVisible(4);
            } else if (!menuDesTxt.equals(this.mSaveMenuDesTxt)) {
                this.mSaveMenuDesTxt = menuDesTxt;
                getMenuDesTxt().setText(Html.fromHtml(this.mTopMenuDescExpandString + this.mSaveMenuDesTxt + "</font>"));
                setTopMenuLayoutVisible(0);
            }
        }
    }

    private TextView getMenuDesTxt() {
        if (this.mMenuDesTxt == null) {
            this.mMenuDesTxt = (TextView) findViewById(R.id.epg_q_album_menu_des);
            this.mMenuDesTxt.setOnClickListener(new OnClickListener() {
                public void onClick(View arg0) {
                    if (AlbumActivity.this.mCurRightFragment != null) {
                        AlbumActivity.this.mCurRightFragment.setMenu2Activity();
                        AlbumActivity.this.showMenu();
                    }
                }
            });
        }
        return this.mMenuDesTxt;
    }

    public View getMenuView() {
        return this.mMenuView;
    }

    public void setMenuView(View view) {
        if (!(this.mMenuView == null || getMainLayout() == null)) {
            getMainLayout().removeView(this.mMenuView);
        }
        this.mMenuView = view;
        if (this.mMenuView != null && getMainLayout() != null) {
            if (!this.mInfoModel.isNoLeftFragment() || !(this.mMenuView instanceof MultiMenuPanel)) {
                this.mMenuView.setVisibility(8);
                LayoutParams params = new LayoutParams(-1, AlbumInfoFactory.isFootPage(this.mInfoModel.getPageType()) ? -1 : -2);
                params.addRule(12);
                getMainLayout().addView(this.mMenuView, params);
                if (this.mMenuView instanceof MultiMenuPanel) {
                    MultiMenuPanel panel = this.mMenuView;
                    panel.setMeltiMenuPanelListener(new MultiMenuPanelListenter() {
                        public void onAlbumDataChanged() {
                            AlbumActivity.this.refreshMenuData();
                        }

                        public String onCallAlbumTagId() {
                            return AlbumActivity.this.mInfoModel.getDataTagId();
                        }

                        public void onMenuPanelDismiss() {
                            AlbumActivity.this.hideMenu();
                        }
                    });
                    panel.setDefaultSelectPos(0);
                    panel.fillData(this.mDataApi.getMultiTags(), this.mInfoModel.getChannelId());
                }
            }
        }
    }

    public void showMenu() {
        if (this.mMenuView != null) {
            if (this.mMenuView instanceof MultiMenuPanel) {
                ((MultiMenuPanel) this.mMenuView).requestDefaultFocus();
                AnimationUtil.translateAnimationY(this.mMenuView, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 0.0f, 200, new AccelerateDecelerateInterpolator());
            } else {
                this.mMenuView.requestFocus();
            }
            this.mMenuView.setVisibility(0);
        }
    }

    public void hideMenu() {
        if (this.mMenuView != null) {
            if (GetInterfaceTools.getUICreator().isViewVisible(this.mGlobalLastFocusView)) {
                this.mGlobalLastFocusView.requestFocus();
            }
            if (this.mMenuView instanceof MultiMenuPanel) {
                AnimationUtil.translateAnimationY(this.mMenuView, 0.0f, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 150, new AccelerateInterpolator());
            }
            this.mMenuView.setVisibility(8);
        }
    }

    private void refreshMenuData() {
        Tag tag = ((MultiMenuPanel) this.mMenuView).getCheckedTag();
        Message msg = this.mBaseHandler.obtainMessage();
        msg.what = 50;
        msg.obj = tag;
        handlerMessage2Left(msg);
    }

    public void handlerMessage2Left(Message msg) {
        if (this.mCurLeftFragment != null && msg != null) {
            this.mCurLeftFragment.handlerMessage2Left(msg);
        }
    }

    public void handlerMessage2Right(Message msg) {
        if (this.mCurRightFragment != null && msg != null) {
            this.mCurRightFragment.handlerMessage2Right(msg);
        }
    }

    protected View getBackgroundContainer() {
        return getMainLayout();
    }

    public void setGlobalLastFocusView(View focusView) {
        this.mGlobalLastFocusView = focusView;
    }

    public void setNextFocusUpId(View v) {
        if (v != null && this.mActionBarPresenter != null) {
            v.setNextFocusUpId(this.mActionBarPresenter.getPreFocusUpId());
            this.mActionBarPresenter.setNextFocusDownId(v.getId());
        }
    }

    public void setFeedbackPanelFocus(View v) {
        if (getNoResultPanel() != null && getNoResultPanel().getButton() != null && getNoResultPanel().getButton().getVisibility() == 0) {
            getNoResultPanel().getButton().setNextFocusLeftId(v.getId());
        }
    }

    public View getGlobalLastFocusView() {
        return this.mGlobalLastFocusView;
    }

    public void setDataApi(BaseDataApi baseAlbumListApi) {
        this.mDataApi = baseAlbumListApi;
    }

    public BaseDataApi getDataApi() {
        return this.mDataApi;
    }

    public void resetDataApi(Tag tag) {
        this.mDataApi.resetApi(tag);
    }

    public AlbumInfoModel getInfoModel() {
        return this.mInfoModel;
    }

    public void setInfoModel(AlbumInfoModel infoModel) {
        this.mInfoModel = infoModel;
    }

    private void log(String str) {
        if (str != null) {
            if (this.mInfoModel != null) {
                Log.e(LOG_TAG, this.mInfoModel.getChannelName() + "/qactivity/" + this.mInfoModel.getDataTagName() + "//---" + str);
            } else {
                Log.e(LOG_TAG, "qactivity//---" + str);
            }
        }
    }

    private void logRecord(String str) {
        if (str != null) {
        }
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        if (getGlobalLastFocusView() != null && (getGlobalLastFocusView() instanceof ActionBarItemView) && event.getKeyCode() == 22) {
            return super.handleKeyEvent(event);
        }
        if (getGlobalLastFocusView() != null && (getGlobalLastFocusView() instanceof ActionBarItemView) && event.getKeyCode() == 20) {
            if (this.mInfoModel == null || this.mInfoModel.isLeftFragmentHasData()) {
                return super.handleKeyEvent(event);
            }
            if (getNoResultPanel() == null || getNoResultPanel().getButton() == null || getNoResultPanel().getButton().getVisibility() != 0) {
                return true;
            }
            return super.handleKeyEvent(event);
        } else if (onDispatchKey(event)) {
            return true;
        } else {
            switch (event.getKeyCode()) {
                case 4:
                case 111:
                    if (GetInterfaceTools.getUICreator().isViewVisible(this.mMenuView)) {
                        hideMenu();
                        return true;
                    } else if (!onPressBack()) {
                        finish();
                        break;
                    } else {
                        return true;
                    }
                case 82:
                    if (GetInterfaceTools.getUICreator().isViewVisible(this.mMenuView)) {
                        hideMenu();
                        return true;
                    } else if (!this.mInfoModel.isRightFragmentHasData() && (this.mMenuView == null || !(this.mMenuView instanceof MultiMenuPanel))) {
                        return true;
                    } else {
                        showMenu();
                        return true;
                    }
            }
            return super.handleKeyEvent(event);
        }
    }

    private boolean onDispatchKey(KeyEvent event) {
        boolean handleRightResult = false;
        boolean handleLeftResult = false;
        if (this.mCurRightFragment != null) {
            handleRightResult = this.mCurRightFragment.dispatchKeyEvent(event);
        }
        if (this.mCurLeftFragment != null) {
            handleLeftResult = this.mCurLeftFragment.dispatchKeyEvent(event);
        }
        return handleRightResult || handleLeftResult;
    }

    private boolean onPressBack() {
        boolean handleRightResult = false;
        boolean handleLeftResult = false;
        if (this.mCurRightFragment != null) {
            handleRightResult = this.mCurRightFragment.onPressBack();
        }
        if (this.mCurLeftFragment != null) {
            handleLeftResult = this.mCurLeftFragment.onPressBack();
        }
        return handleRightResult || handleLeftResult;
    }

    protected void onResume() {
        log(NOLOG ? null : "onResume");
        super.onResume();
        if (AlbumInfoFactory.isNewVipChannel(this.mInfoModel.getChannelId()) && !this.isOpenedH5) {
            CreateInterfaceTools.createWebRoleFactory().showRoleInVip(this);
            this.isOpenedH5 = true;
        }
        this.mActionBarPresenter.startVipAnimation(false);
    }

    protected void onPause() {
        super.onPause();
        this.mActionBarPresenter.stopVipAnimation();
    }

    protected void onDestroy() {
        log(NOLOG ? null : "onDestroy");
        super.onDestroy();
        this.mGlobalLastFocusView = null;
        this.mLeftFragmentCacheList = null;
        this.mRightFragmentCacheList = null;
        this.mCurLeftFragment = null;
        this.mCurRightFragment = null;
        this.mMenuView = null;
        this.mBaseHandler.removeCallbacksAndMessages(null);
    }

    public void requestLeftPanelFocus() {
        getLeftLayout();
        if (this.mLeftLayout != null) {
            this.mLeftLayout.requestFocus();
        }
    }
}
