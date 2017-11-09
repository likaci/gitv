package com.gala.video.app.epg.ui.search;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.AlbumUtils;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridParams;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.WidgetStatusListener;
import com.gala.video.app.epg.ui.search.ad.BaseBannerAdTask;
import com.gala.video.app.epg.ui.search.ad.CommenBannerAdTask;
import com.gala.video.app.epg.ui.search.ad.IFetchBannerAdListener;
import com.gala.video.app.epg.ui.search.ad.SearchBannerAdUrlConfig;
import com.gala.video.app.epg.ui.search.adapter.KeyboardOperAdapter;
import com.gala.video.app.epg.ui.search.fragment.SearchBaseFragment;
import com.gala.video.app.epg.ui.search.fragment.SearchFullFragment;
import com.gala.video.app.epg.ui.search.fragment.SearchHotFragment;
import com.gala.video.app.epg.ui.search.fragment.SearchSmartFragment;
import com.gala.video.app.epg.ui.search.fragment.SearchT9Fragment;
import com.gala.video.app.epg.ui.search.keybord.manager.KeyboardManager;
import com.gala.video.app.epg.ui.search.widget.SearchCursorView;
import com.gala.video.app.epg.ui.search.widget.SearchCursorView.NextRightFocusListener;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.ProgressBarItem;
import com.gala.video.lib.share.ifimpl.ads.AdsClientUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.BannerImageAdModel;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.constants.AdEvent;
import java.util.HashMap;
import java.util.List;

public class QSearchActivity extends QMultiScreenActivity implements ISearchEvent, OnFocusChangeListener {
    private static final boolean IS_SURPPORT_PERSON = false;
    private static final long LOADING_DELAY_1500 = 1500;
    private static String TAG = "QSearchActivity";
    private final String LOG_TAG = "EPG/search/QSearchActivity";
    private final String SEARCH_TIP_HTMLTEXT = ("<html><head></head><body><p><font color='#" + ResourceUtil.getColorLength6(C0508R.color.keyboard_input_tip) + "'>输入片名/人名的</font>" + "<font color='#" + ResourceUtil.getColorLength6(C0508R.color.albumview_yellow_color) + "'>首字母</font><font color='#" + ResourceUtil.getColorLength6(C0508R.color.keyboard_input_tip) + "'>或</font><font color='#" + ResourceUtil.getColorLength6(C0508R.color.albumview_yellow_color) + "'>全拼</font></p></body></html>");
    private boolean isOpenApi;
    private int mAlbumChannelId;
    private String mAlbumChannelName;
    private Bundle mBundle;
    private int mCurKeyboardType;
    private SearchBaseFragment mCurLeftFragment;
    private SearchBaseFragment mCurRightFragment;
    private TextView mExpandKeyboardTab;
    private BaseBannerAdTask mFetchSearchBannerAdTask;
    private boolean mFlag;
    private TextView mFullKeyboardTab;
    private Handler mHandler = new Handler();
    private ImageView mImageAdContent;
    private ImageView mImageAdCorner;
    private IImageProvider mImageLoader;
    private int mInputNum;
    private boolean mIsNeedAdBadg;
    private boolean mIsSuggestDisplay;
    private boolean mIsTabFocusable;
    private View mLine;
    private boolean mLoadingFlag;
    private KeyboardOperAdapter mOperationAdapter;
    private PhotoGridView mOperationView;
    private WidgetStatusListener mOperationViewListener = new C09677();
    private long mPreClickTime;
    private ProgressBarItem mProgressBar;
    private SearchCursorView mSearchText;
    private TextView mT9KeyboardTab;
    private String mTvsrchsource;
    private Runnable f1947r = new C09699();

    class C09611 implements Runnable {
        C09611() {
        }

        public void run() {
            QSearchActivity.this.initContentView();
        }
    }

    class C09622 implements Runnable {
        C09622() {
        }

        public void run() {
            KeyboardManager.get().init();
            QSearchActivity.this.initIntent();
        }
    }

    class C09633 implements NextRightFocusListener {
        C09633() {
        }

        public void onNextRightFocusChanged() {
            QSearchActivity.this.mCurRightFragment.requestDefaultFocus();
        }
    }

    class C09644 implements IFetchBannerAdListener {
        C09644() {
        }

        public void onSuccess(List<BannerImageAdModel> bannerImageAdModelList) {
            BannerImageAdModel bannerImageAdModel = (BannerImageAdModel) bannerImageAdModelList.get(0);
            ImageRequest bannerAdImageRequest = new ImageRequest(bannerImageAdModel.getImageUrl());
            QSearchActivity.this.mIsNeedAdBadg = bannerImageAdModel.getNeedAdBadge();
            QSearchActivity.this.mImageLoader.loadImage(bannerAdImageRequest, new MyBannerAdImageCallBack(bannerImageAdModel.getAdId()));
        }

        public void onFailed(ApiException e) {
            LogRecordUtils.loge(QSearchActivity.TAG, ">> fetchBannerAd TaskListener onFailed, e=" + e);
            QSearchActivity.this.mImageAdContent.setVisibility(8);
            QSearchActivity.this.mImageAdCorner.setVisibility(8);
        }

        public void onSendPingback(PingBackParams pingBackParams) {
            pingBackParams.add(Keys.RI, "ad_banner_search");
            PingBack.getInstance().postPingBackToLongYuan(pingBackParams.build());
        }
    }

    class C09655 implements Runnable {
        C09655() {
        }

        public void run() {
        }
    }

    class C09666 implements Runnable {
        C09666() {
        }

        public void run() {
            QSearchActivity.this.mOperationView.setAdapter(QSearchActivity.this.mOperationAdapter);
            QSearchActivity.this.initOperationViewFocus();
        }
    }

    class C09677 implements WidgetStatusListener {
        C09677() {
        }

        public void onLoseFocus(ViewGroup parent, View view, int position) {
        }

        public void onItemTouch(View view, MotionEvent arg1, int position) {
        }

        public void onItemSelectChange(View view, int position, boolean focus) {
            TextView textView = (TextView) view.findViewById(C0508R.id.epg_operate_text);
            if (view.getTag(ISearchConstant.KEYBOARD_OPER_TAG_KEY) != null) {
                int tagValue = ((Integer) view.getTag(ISearchConstant.KEYBOARD_OPER_TAG_KEY)).intValue();
                if (focus) {
                    textView.setTextColor(QSearchActivity.this.getResources().getColor(C0508R.color.detail_title_text_color_new));
                    AnimationUtil.scaleAnimation(view, 1.0f, 1.1f, 200);
                    return;
                }
                textView.setTextColor(QSearchActivity.this.getResources().getColor(C0508R.color.searchHistorytitle_normal_color));
                AnimationUtil.scaleAnimation(view, 1.1f, 1.0f, 200);
            }
        }

        public void onItemClick(ViewGroup viewGroup, View view, int position) {
            if (SearchEnterUtils.checkNetWork(AppRuntimeEnv.get().getApplicationContext())) {
                CharSequence searchContent = QSearchActivity.this.mSearchText.getText().toString();
                if (StringUtils.isEmpty(searchContent)) {
                    LogUtils.m1571e("EPG/search/QSearchActivity", "CursorTextView content is empty ---- invalide opearation");
                    return;
                }
                AnimationUtil.clickScaleAnimation(view);
                if (view.getTag(ISearchConstant.KEYBOARD_OPER_TAG_KEY) != null) {
                    int tagValue = ((Integer) view.getTag(ISearchConstant.KEYBOARD_OPER_TAG_KEY)).intValue();
                    if (tagValue == 0) {
                        QSearchActivity.this.mSearchText.clear();
                        QSearchActivity.this.onSwitchFragment(new SearchHotFragment());
                    } else if (1 == tagValue && QSearchActivity.this.mSearchText.isDeletable()) {
                        QSearchActivity.this.mSearchText.delete();
                        if (searchContent.length() == 1) {
                            QSearchActivity.this.onSwitchFragment(new SearchHotFragment());
                        } else {
                            QSearchActivity.this.onKeyBoardTextChanged();
                        }
                    }
                }
            }
        }
    }

    class C09688 implements Runnable {
        C09688() {
        }

        public void run() {
            QSearchActivity.this.setSaveKeyType(QSearchActivity.this.mCurKeyboardType);
        }
    }

    class C09699 implements Runnable {
        C09699() {
        }

        public void run() {
            if (QSearchActivity.this.mLoadingFlag) {
                QSearchActivity.this.showProgressBar();
            }
            QSearchActivity.this.mLoadingFlag = true;
        }
    }

    class MyBannerAdImageCallBack implements IImageCallback {
        int mBanneAdId;

        class C09712 implements Runnable {
            C09712() {
            }

            public void run() {
                QSearchActivity.this.mImageAdCorner.setVisibility(8);
                QSearchActivity.this.mImageAdContent.setVisibility(8);
            }
        }

        public MyBannerAdImageCallBack(int adId) {
            this.mBanneAdId = adId;
        }

        public void onSuccess(ImageRequest imageRequest, final Bitmap bitmap) {
            LogUtils.m1574i(QSearchActivity.TAG, "mLoadBannerAdImageCallback --- onSuccess");
            QSearchActivity.this.mHandler.post(new Runnable() {
                public void run() {
                    QSearchActivity.this.mImageAdContent.setImageBitmap(bitmap);
                    QSearchActivity.this.mImageAdContent.setVisibility(0);
                    if (QSearchActivity.this.mIsNeedAdBadg) {
                        QSearchActivity.this.mImageAdCorner.setVisibility(0);
                    }
                }
            });
            QAPingback.searchBannerAdShowPingback();
            AdsClientUtils.getInstance().onAdStarted(this.mBanneAdId);
            AdsClientUtils.getInstance().onAdEvent(this.mBanneAdId, AdEvent.AD_EVENT_IMPRESSION, new HashMap());
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
            QSearchActivity.this.mHandler.post(new C09712());
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImageProviderApi.getImageProvider().stopAllTasks();
        setContentView(C0508R.layout.epg_activity_search);
        this.mCurKeyboardType = SearchPreference.getKeyTab(this);
        if (SearchPreference.isSearchNew(this)) {
            SearchPreference.setSearchNew(this, false);
            CharSequence type = Project.getInstance().getBuild().getKeyboardType();
            if (!StringUtils.isEmpty(type)) {
                this.mCurKeyboardType = Integer.parseInt(type);
            }
        }
        SearchBaseFragment.mIsRequireFocus = true;
        this.mHandler.post(new C09611());
        doInThread();
        getWindow().setFlags(16777216, 16777216);
        QAPingback.searchShowPingback();
    }

    private void doInThread() {
        ThreadUtils.execute(new C09622());
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            this.mIsTabFocusable = true;
        }
    }

    private void initIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            this.isOpenApi = intent.getBooleanExtra("from_openapi", false);
            this.mAlbumChannelId = intent.getIntExtra("channel_id", -1);
            this.mAlbumChannelName = intent.getStringExtra(ISearchConstant.CHANNEL_NAME);
            this.mTvsrchsource = intent.getStringExtra("tvsrchsource");
        }
    }

    private void initViewStatus() {
        this.mProgressBar.setText(getResources().getString(C0508R.string.search_loading));
        initTabViewParams(this.mCurKeyboardType);
        this.mFullKeyboardTab.setOnFocusChangeListener(this);
        this.mT9KeyboardTab.setOnFocusChangeListener(this);
        this.mExpandKeyboardTab.setOnFocusChangeListener(this);
    }

    private void initContentView() {
        this.mFullKeyboardTab = (TextView) findViewById(C0508R.id.epg_tab_keyboard_full);
        this.mT9KeyboardTab = (TextView) findViewById(C0508R.id.epg_tab_keyboard_t9);
        this.mExpandKeyboardTab = (TextView) findViewById(C0508R.id.epg_tab_keyboard_expand);
        this.mProgressBar = (ProgressBarItem) findViewById(C0508R.id.epg_search_progress);
        this.mSearchText = (SearchCursorView) findViewById(C0508R.id.epg_search_content_edit);
        this.mLine = findViewById(C0508R.id.epg_search_line);
        this.mOperationView = (PhotoGridView) findViewById(C0508R.id.epg_keyboard_operate_gridview);
        initCursorTextView();
        initOperationView();
        initViewStatus();
        initSearchFragment(this.mCurKeyboardType);
        initAdView();
        this.mSearchText.setOnFocusChangeListener(this);
        this.mSearchText.setListener(new C09633());
        this.mOperationView.setListener(this.mOperationViewListener);
    }

    private void initAdView() {
        View view = ((ViewStub) findViewById(C0508R.id.epg_ad_layout)).inflate();
        this.mImageAdContent = (ImageView) view.findViewById(C0508R.id.epg_imge_ad_content);
        this.mImageAdCorner = (ImageView) view.findViewById(C0508R.id.epg_imge_ad_corner);
        this.mImageLoader = ImageProviderApi.getImageProvider();
        this.mFetchSearchBannerAdTask = new CommenBannerAdTask(new SearchBannerAdUrlConfig(), new C09644());
        this.mFetchSearchBannerAdTask.execute();
    }

    private void initCursorTextView() {
        if (this.mSearchText != null) {
            this.mSearchText.startCursor(650);
            this.mSearchText.post(new C09655());
        }
    }

    private void initOperationView() {
        this.mOperationView.setNextRightFocusLeaveAvail(false);
        this.mOperationView.setNextUpFocusLeaveAvail(true);
        this.mOperationView.setNextDownFocusLeaveAvail(true);
        setOperViewParams();
    }

    private void setOperViewParams() {
        this.mOperationView.setParams(getKeyboardOperViewParams());
        this.mOperationAdapter = new KeyboardOperAdapter(this);
        this.mOperationView.post(new C09666());
    }

    private PhotoGridParams getKeyboardOperViewParams() {
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = 2;
        p.verticalSpace = ResourceUtil.getDimen(C0508R.dimen.dimen_0dp);
        p.horizontalSpace = ResourceUtil.getDimen(C0508R.dimen.dimen_18dp);
        p.contentHeight = ResourceUtil.getDimen(C0508R.dimen.dimen_53dp);
        p.contentWidth = ResourceUtil.getDimen(C0508R.dimen.dimen_175dp);
        p.scaleRate = 1.1f;
        return p;
    }

    private void initOperationViewFocus() {
        this.mOperationView.getViewByPos(0).setNextFocusLeftId(this.mOperationView.getViewByPos(0).getId());
        this.mOperationView.getViewByPos(0).setNextFocusDownId(this.mOperationView.getViewByPos(0).getId());
        this.mOperationView.getViewByPos(1).setNextFocusDownId(this.mOperationView.getViewByPos(1).getId());
    }

    private void initSearchFragment(int keyTab) {
        switch (keyTab) {
            case 0:
                onSwitchFragment(new SearchFullFragment());
                this.mSearchText.setNextFocusUpId(this.mFullKeyboardTab.getId());
                break;
            case 1:
                onSwitchFragment(new SearchT9Fragment());
                this.mSearchText.setNextFocusUpId(this.mT9KeyboardTab.getId());
                break;
            default:
                onSwitchFragment(new SearchSmartFragment());
                this.mSearchText.setNextFocusUpId(this.mExpandKeyboardTab.getId());
                break;
        }
        onSwitchFragment(new SearchHotFragment());
    }

    private void initTabViewParams(int keyTab) {
        this.mFullKeyboardTab.setNextFocusUpId(this.mFullKeyboardTab.getId());
        this.mT9KeyboardTab.setNextFocusUpId(this.mT9KeyboardTab.getId());
        this.mExpandKeyboardTab.setNextFocusUpId(this.mExpandKeyboardTab.getId());
        this.mFullKeyboardTab.setNextFocusLeftId(this.mFullKeyboardTab.getId());
        this.mFullKeyboardTab.setNextFocusRightId(this.mT9KeyboardTab.getId());
        this.mT9KeyboardTab.setNextFocusLeftId(this.mFullKeyboardTab.getId());
        this.mT9KeyboardTab.setNextFocusRightId(this.mExpandKeyboardTab.getId());
        this.mExpandKeyboardTab.setNextFocusLeftId(this.mT9KeyboardTab.getId());
        int color = C0508R.color.global_green;
        int background = C0508R.drawable.epg_keyboard_tab_bg;
        this.mFullKeyboardTab.setTag(ISearchConstant.KEYBOARD_TYPE_TAG_KEY, Integer.valueOf(0));
        this.mT9KeyboardTab.setTag(ISearchConstant.KEYBOARD_TYPE_TAG_KEY, Integer.valueOf(1));
        this.mExpandKeyboardTab.setTag(ISearchConstant.KEYBOARD_TYPE_TAG_KEY, Integer.valueOf(2));
        this.mFullKeyboardTab.setBackgroundResource(background);
        this.mT9KeyboardTab.setBackgroundResource(background);
        this.mExpandKeyboardTab.setBackgroundResource(background);
        if (this.mCurKeyboardType == 0) {
            this.mFullKeyboardTab.setBackgroundResource(background);
            this.mFullKeyboardTab.setTextColor(getColors(C0508R.color.gala_write));
            this.mT9KeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            this.mExpandKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            setTabItemDrawableBottom(true, false);
            this.mFullKeyboardTab.requestFocus();
        } else if (this.mCurKeyboardType == 1) {
            this.mT9KeyboardTab.setBackgroundResource(background);
            this.mT9KeyboardTab.setTextColor(getColors(color));
            this.mFullKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            this.mExpandKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            setTabItemDrawableBottom(false, true);
            this.mT9KeyboardTab.requestFocus();
        } else if (this.mCurKeyboardType == 2) {
            this.mExpandKeyboardTab.setBackgroundResource(background);
            this.mExpandKeyboardTab.setTextColor(getColors(C0508R.color.gala_write));
            this.mFullKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            this.mT9KeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            setTabItemDrawableBottom(false, true);
            this.mExpandKeyboardTab.requestFocus();
        }
    }

    private void setSaveKeyType(int keyTab) {
        switch (keyTab) {
            case 0:
                SearchPreference.setFullTab(this);
                return;
            case 1:
                SearchPreference.setT9Tab(this);
                return;
            default:
                SearchPreference.setExpandTab(this);
                return;
        }
    }

    private void setTabItemDrawableBottom(boolean setFullKeyboardTabFlag, boolean setT9KeyboardTabFlag) {
    }

    public void onSwitchFragment(SearchBaseFragment f) {
        if (f != null && !isFinishing()) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            initBundle(f);
            f.setArguments(this.mBundle);
            int pageLocationType = f.getPageLocationType();
            if (pageLocationType == 0) {
                fragmentTransaction.replace(C0508R.id.epg_qsearch_frame_left, f);
                sendPageShowPingback(this.mCurKeyboardType);
            } else if (pageLocationType == 1) {
                fragmentTransaction.replace(C0508R.id.epg_qsearch_frame_right, f);
            }
            fragmentTransaction.commitAllowingStateLoss();
        }
    }

    private void changeKeyboardFragment(View v) {
        int i = v.getId();
        if (i == C0508R.id.epg_tab_keyboard_full) {
            this.mCurKeyboardType = 0;
            onSwitchFragment(new SearchFullFragment());
            this.mSearchText.setNextFocusUpId(this.mFullKeyboardTab.getId());
        } else if (i == C0508R.id.epg_tab_keyboard_t9) {
            this.mCurKeyboardType = 1;
            onSwitchFragment(new SearchT9Fragment());
            this.mSearchText.setNextFocusUpId(this.mT9KeyboardTab.getId());
        } else if (i == C0508R.id.epg_tab_keyboard_expand) {
            this.mCurKeyboardType = 2;
            onSwitchFragment(new SearchSmartFragment());
            this.mSearchText.setNextFocusUpId(this.mExpandKeyboardTab.getId());
        }
        if (!StringUtils.isEmpty(this.mSearchText.getText().toString())) {
            this.mFlag = false;
        }
    }

    protected void resetTabBackground(View v) {
        ((TextView) v).setTextColor(getColors(C0508R.color.gala_write));
        int bgId = C0508R.drawable.epg_keyboard_tab_bg;
        this.mFullKeyboardTab.setBackgroundResource(bgId);
        this.mT9KeyboardTab.setBackgroundResource(bgId);
        this.mExpandKeyboardTab.setBackgroundResource(bgId);
        int tabItemColor = getColors(C0508R.color.keyboard_num);
        int i = v.getId();
        if (i == C0508R.id.epg_tab_keyboard_full) {
            this.mT9KeyboardTab.setTextColor(tabItemColor);
            this.mExpandKeyboardTab.setTextColor(tabItemColor);
            setTabItemDrawableBottom(true, false);
        } else if (i == C0508R.id.epg_tab_keyboard_t9) {
            this.mFullKeyboardTab.setTextColor(tabItemColor);
            this.mExpandKeyboardTab.setTextColor(tabItemColor);
            setTabItemDrawableBottom(false, true);
        } else if (i == C0508R.id.epg_tab_keyboard_expand) {
            this.mFullKeyboardTab.setTextColor(tabItemColor);
            this.mT9KeyboardTab.setTextColor(tabItemColor);
            setTabItemDrawableBottom(false, true);
        }
    }

    private void changeTabBackground(View v) {
        ((TextView) v).setTextColor(getColors(C0508R.color.gala_write));
        int selectedColor = C0508R.color.global_green;
        int i = v.getId();
        if (i == C0508R.id.epg_tab_keyboard_full) {
            if (!this.mT9KeyboardTab.hasFocus() && !this.mExpandKeyboardTab.hasFocus()) {
                this.mFullKeyboardTab.setBackgroundResource(C0508R.drawable.epg_keyboard_tab_bg);
                this.mFullKeyboardTab.setTextColor(getColors(selectedColor));
                this.mT9KeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
                this.mExpandKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
                setTabItemDrawableBottom(true, false);
            }
        } else if (i == C0508R.id.epg_tab_keyboard_t9) {
            if (!this.mFullKeyboardTab.hasFocus() && !this.mExpandKeyboardTab.hasFocus()) {
                this.mT9KeyboardTab.setBackgroundResource(C0508R.drawable.epg_keyboard_tab_bg);
                this.mT9KeyboardTab.setTextColor(getColors(selectedColor));
                this.mFullKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
                this.mExpandKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
                setTabItemDrawableBottom(false, true);
            }
        } else if (i == C0508R.id.epg_tab_keyboard_expand && !this.mFullKeyboardTab.hasFocus() && !this.mT9KeyboardTab.hasFocus()) {
            this.mExpandKeyboardTab.setBackgroundResource(C0508R.drawable.epg_keyboard_tab_bg);
            this.mExpandKeyboardTab.setTextColor(getColors(selectedColor));
            this.mFullKeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            this.mT9KeyboardTab.setTextColor(getColors(C0508R.color.keyboard_num));
            setTabItemDrawableBottom(false, true);
        }
    }

    private void initBundle(SearchBaseFragment f) {
        if (f.getArguments() == null) {
            this.mBundle = new Bundle();
        } else {
            this.mBundle = f.getArguments();
        }
    }

    public void showProgressBar() {
        if (this.mProgressBar != null) {
            this.mProgressBar.setVisibility(0);
        }
    }

    public void closeProgressBar() {
        if (this.mProgressBar != null && this.mProgressBar.getVisibility() == 0) {
            this.mProgressBar.setVisibility(4);
        }
    }

    protected View getBackgroundContainer() {
        return findViewById(C0508R.id.epg_qsearch_main_container);
    }

    protected boolean getVideoCheck() {
        return false;
    }

    protected void onDestroy() {
        super.onDestroy();
        ThreadUtils.execute(new C09688());
        if (!(this.mFullKeyboardTab == null || this.f1947r == null)) {
            this.mFullKeyboardTab.removeCallbacks(this.f1947r);
            this.f1947r = null;
        }
        this.mCurLeftFragment = null;
        this.mCurRightFragment = null;
        this.mFullKeyboardTab.setFocusable(false);
        this.mT9KeyboardTab.setFocusable(false);
        this.mExpandKeyboardTab.setFocusable(false);
        KeyboardManager.get().onDestory();
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        if (this.mSearchText != null && this.mSearchText.hasFocus() && this.mSearchText.dispatchKeyEvent(event)) {
            return true;
        }
        if (this.mCurLeftFragment == null || !this.mCurLeftFragment.dispatchKeyEvent(event)) {
            return super.handleKeyEvent(event);
        }
        return true;
    }

    public void onKeyBoardTextChanged() {
        CharSequence searchContent = this.mSearchText.getText().toString();
        if (this.mCurRightFragment != null && !StringUtils.isEmpty(searchContent) && searchContent.length() < 20) {
            this.mCurRightFragment.onKeyBoardTextChanged(searchContent);
        }
    }

    public void showLoading() {
        this.mLoadingFlag = true;
        if (this.mFullKeyboardTab != null && this.f1947r != null) {
            this.mFullKeyboardTab.removeCallbacks(this.f1947r);
            this.mFullKeyboardTab.postDelayed(this.f1947r, LOADING_DELAY_1500);
        }
    }

    public void hideLoading() {
        LogUtils.m1568d("EPG/search/QSearchActivity", ">>>>> hideloading()");
        this.mLoadingFlag = false;
        if (!(this.mFullKeyboardTab == null || this.f1947r == null)) {
            LogUtils.m1568d("EPG/search/QSearchActivity", ">>>>> hideloading() - removeCallbacks()");
            this.mFullKeyboardTab.removeCallbacks(this.f1947r);
        }
        closeProgressBar();
    }

    public void onStartSearch(int clickType, String key, String qpid, int mode, int requestCode) {
        if (this.isOpenApi) {
            AlbumUtils.startSearchResultPageForResultOpenApi(this, this.mAlbumChannelId, key, clickType, null, requestCode, this.mAlbumChannelName);
            return;
        }
        AlbumUtils.startSearchResultPageForResult(this, this.mAlbumChannelId, key, clickType, null, requestCode, this.mAlbumChannelName);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (this.mFullKeyboardTab != null) {
            this.mFullKeyboardTab.setFocusable(false);
        }
        if (this.mT9KeyboardTab != null) {
            this.mT9KeyboardTab.setFocusable(false);
        }
        if (this.mExpandKeyboardTab != null) {
            this.mExpandKeyboardTab.setFocusable(false);
        }
        if (this.mCurRightFragment != null) {
            this.mCurRightFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void onChangeTabFocusable(boolean isFocusble) {
        if (this.mFullKeyboardTab != null && this.mT9KeyboardTab != null && this.mExpandKeyboardTab != null) {
            if (isFocusble) {
                if (!this.mFullKeyboardTab.isFocusable()) {
                    this.mFullKeyboardTab.setFocusable(true);
                }
                if (!this.mT9KeyboardTab.isFocusable()) {
                    this.mT9KeyboardTab.setFocusable(true);
                }
                if (!this.mExpandKeyboardTab.isFocusable()) {
                    this.mExpandKeyboardTab.setFocusable(true);
                    return;
                }
                return;
            }
            if (this.mFullKeyboardTab.isFocusable()) {
                this.mFullKeyboardTab.setFocusable(false);
            }
            if (this.mT9KeyboardTab.isFocusable()) {
                this.mT9KeyboardTab.setFocusable(false);
            }
            if (this.mExpandKeyboardTab.isFocusable()) {
                this.mExpandKeyboardTab.setFocusable(false);
            }
        }
    }

    private void sendPageShowPingback(int keyType) {
        String block = "";
        switch (keyType) {
            case 0:
                block = "fullkeyboard";
                break;
            case 1:
                block = "t9keyboard";
                break;
            case 2:
                block = "smartkeyboard";
                break;
        }
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "21").add("bstp", "1").add("qtcurl", "srch_keyboard").add("block", block);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private int getColors(int id) {
        return ResourceUtil.getColor(id);
    }

    public void onAttachActivity(SearchBaseFragment fragment) {
        if (fragment.getPageLocationType() == 0) {
            this.mCurLeftFragment = fragment;
            if (this.mSearchText != null) {
                LogUtils.m1570d("EPG/search/QSearchActivity", "switchFragment -----  mSearchText = ", this.mSearchText);
            }
        } else if (fragment.getPageLocationType() == 1) {
            this.mCurRightFragment = fragment;
        }
    }

    public boolean onInputText(String text) {
        LogUtils.m1568d("EPG/search/QSearchActivity", "onInputText");
        if (this.mSearchText == null) {
            return false;
        }
        if (StringUtils.isEmpty(this.mSearchText.getText().toString())) {
            this.mFlag = true;
            this.mInputNum = 0;
            this.mPreClickTime = SystemClock.uptimeMillis();
        }
        return this.mSearchText.appendText(text);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (!isFinishing()) {
            int vid = v.getId();
            if (vid == C0508R.id.epg_tab_keyboard_full || vid == C0508R.id.epg_tab_keyboard_t9 || vid == C0508R.id.epg_tab_keyboard_expand) {
                int mFocusTag = ((Integer) v.getTag(ISearchConstant.KEYBOARD_TYPE_TAG_KEY)).intValue();
                if (this.mIsTabFocusable) {
                    if (hasFocus) {
                        this.mLine.setVisibility(0);
                        SearchBaseFragment.mIsRequireFocus = false;
                        resetTabBackground(v);
                        if (mFocusTag != this.mCurKeyboardType) {
                            changeKeyboardFragment(v);
                        }
                    } else {
                        changeTabBackground(v);
                    }
                    AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200, false);
                }
            } else if (vid != C0508R.id.epg_search_content_edit) {
            } else {
                if (hasFocus) {
                    onChangeTabFocusable(true);
                    AnimationUtil.zoomAnimation(v, 1.0f, 1.02f, 1.0f, 1.02f, 200);
                    this.mLine.setVisibility(4);
                    this.mSearchText.setTextColor(ResourceUtil.getColor(C0508R.color.keyboard_clear_remove_btn_focus));
                    this.mSearchText.setCursorColor(ResourceUtil.getColor(C0508R.color.keyboard_clear_remove_btn_focus));
                    return;
                }
                AnimationUtil.zoomAnimation(v, 1.0f, 1.0f, 1.0f, 1.02f, 200);
                this.mLine.setVisibility(0);
                this.mSearchText.setTextColor(ResourceUtil.getColor(C0508R.color.keyboard_letter));
                this.mSearchText.setCursorColor(ResourceUtil.getColor(C0508R.color.keyboard_letter));
            }
        }
    }

    public void onChangeClearViewFocus(View view) {
    }

    public void onRequestRightDefaultFocus() {
        if (this.mCurRightFragment != null) {
            this.mCurRightFragment.requestDefaultFocus();
        }
    }

    public String onGetSearchText() {
        if (this.mSearchText != null) {
            return this.mSearchText.getText().toString();
        }
        return null;
    }

    public void onSuggestClickPingback(String eventId, String site, int pos, String content, String keyword, boolean isPerson) {
        String keyboard = "";
        String input = "";
        String time = "";
        if (this.mCurKeyboardType == 0) {
            keyboard = "full";
        } else if (this.mCurKeyboardType == 1) {
            keyboard = "T9";
        } else if (this.mCurKeyboardType == 2) {
            keyboard = "smart";
        }
        if (this.mFlag && this.mInputNum != 0) {
            input = String.valueOf(this.mInputNum);
            if (SystemClock.uptimeMillis() - this.mPreClickTime <= 0) {
                input = "";
            } else {
                time = String.valueOf(SystemClock.uptimeMillis() - this.mPreClickTime);
            }
        }
        LogUtils.m1576i("EPG/search/QSearchActivity", ">>>>> onSuggestClickPingback() ---mFlag_", Boolean.valueOf(this.mFlag), " , input_", input, " , time_", time, "tvsrchsource_", this.mTvsrchsource);
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "5").add("c1", "-1").add("s1", "1").add("s2", "6").add("r", "6").add("a", "0").add(Keys.ALBUM_ID, "0").add(Keys.PTYPE, "1-2").add("e", eventId).add("site", site).add(Keys.POS, String.valueOf(pos)).add(Keys.TARGET, content).add("keyword", keyword).add(Keys.INPUT, input).add(Keys.TM1, time).add(Keys.KEYBOARD, keyboard).add("tvsrchsource", this.mTvsrchsource).add(Keys.IS_PERSON, "");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
        this.mFlag = false;
    }

    protected void onStop() {
        super.onStop();
        this.mFlag = false;
    }

    public void onInputAdd() {
        if (this.mSearchText != null && !this.mSearchText.hasFocus() && this.mFlag) {
            this.mInputNum++;
            if (LogUtils.mIsDebug) {
                LogUtils.m1576i("EPG/search/QSearchActivity", ">>>>> input:", Integer.valueOf(this.mInputNum));
            }
        }
    }

    public boolean isSuggestDisplay() {
        return this.mIsSuggestDisplay;
    }

    public void onChangeSuggestDisplay(boolean isDisplay) {
        this.mIsSuggestDisplay = isDisplay;
    }
}
