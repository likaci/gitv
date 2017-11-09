package com.gala.video.app.epg.ui.ucenter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.result.ApiResultGroupDetail;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.ucenter.account.presenter.UCenterPresenter;
import com.gala.video.app.epg.ui.ucenter.account.ui.IUCenterView;
import com.gala.video.app.epg.ui.ucenter.account.ui.widget.UCenterFlashView;
import com.gala.video.app.epg.uikit.card.UCenterCard;
import com.gala.video.app.epg.uikit.item.DailyNewsItem;
import com.gala.video.app.epg.uikit.item.UCenterItem;
import com.gala.video.app.epg.uikit.view.DailyNewsItemView;
import com.gala.video.app.epg.uikit.view.UCenterItemView;
import com.gala.video.app.epg.widget.dialog.BitmapAlbum;
import com.gala.video.app.epg.widget.dialog.GlobalVipCloudView.GlobalVipCloudViewCallBack;
import com.gala.video.app.epg.widget.dialog.GlobalVipDialog;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.ProgressBarItem;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifimpl.ucenter.history.impl.HistoryInfoHelper;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.pingback.UcenterPingbackUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.uikit.UIKitEngine;
import com.gala.video.lib.share.uikit.action.ActionModelFactory;
import com.gala.video.lib.share.uikit.action.data.UcenterRecordAllData;
import com.gala.video.lib.share.uikit.action.model.AlbumVideoLiveActionModel;
import com.gala.video.lib.share.uikit.action.model.UcenterRecordAllActionModel;
import com.gala.video.lib.share.uikit.action.model.VipVideoActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.PageInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.CardInfoBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.item.SettingItem;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class UcenterActivity extends QMultiScreenActivity implements IUCenterView {
    private static final int LIMITED_RECORD_VIDEO_COUNT = 6;
    private static final String LOG_TAG = "EPG/login/UcenterActivity";
    private UCenterActionPolicy mActionPolicy;
    private CardFocusHelper mCardFocusHelper;
    List<CardInfoModel> mCardInfoModels;
    private View mChangePassFocusView;
    private View mChangePassLoadingBg;
    private ImageView mChangePassQRImg;
    private View mChangePassView;
    private int mClickType = 0;
    private GlobalVipDialog mConfirmDialog;
    private Context mContext;
    private String mE;
    private UCenterFlashView mFlashView;
    private GlobalVipCloudViewCallBack mGlobalVipCloudViewCallBack = new C11265();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private CardInfoModel mHeadModel;
    private MyObserver mHistoryChangedObserver = new C11308();
    private int mHistoryIndex;
    private CardInfoModel mHistoryModel;
    private boolean mIsLoadMore = true;
    private boolean mIsLogin;
    private boolean mIsVip;
    private boolean mLoadFinished = false;
    private View mMyCenenterLayerView;
    private int mPageIndex;
    private PageInfoModel mPageInfoModel;
    private UcenterPingbackActionPolicy mPingbackActionPolicy;
    private UCenterPresenter mPresenter;
    private ProgressBarItem mProgressBar;
    private UcenterRecordAllData mRecordData;
    private UcenterRecordAllActionModel mRecordModel;
    private BlocksView mRootView;
    private String mS1;
    private String mSourceID = "219161312";
    private TextView mTxtError;
    private UIKitEngine mUIKitEngine;
    private View mViewFailure;
    private View mViewLoading;

    class C11221 implements OnClickListener {
        C11221() {
        }

        public void onClick(View view) {
            UcenterActivity.this.mPresenter.startSecurityCenter();
        }
    }

    class C11232 implements OnClickListener {
        C11232() {
        }

        public void onClick(View view) {
            boolean result = UcenterActivity.this.mPresenter.confirmLogout();
            UcenterActivity.this.mConfirmDialog.dismiss();
            if (result) {
                UcenterActivity.this.resetPingbackE();
                UcenterActivity.this.mUIKitEngine.getPage().backToTop();
                UcenterActivity.this.initUserInfoCard();
                UcenterActivity.this.initFirstPageCard();
                UcenterActivity.this.mIsLogin = false;
                UcenterActivity.this.mIsVip = false;
            }
        }
    }

    class C11243 implements OnClickListener {
        C11243() {
        }

        public void onClick(View view) {
            UcenterActivity.this.mConfirmDialog.dismiss();
            UcenterActivity.this.mPresenter.cancelLogout();
        }
    }

    class C11254 implements OnDismissListener {
        C11254() {
        }

        public void onDismiss(DialogInterface dialog) {
            UcenterActivity.this.mPresenter.logoutDialogDismiss();
        }
    }

    class C11265 implements GlobalVipCloudViewCallBack {
        C11265() {
        }

        public void clickLitener() {
            UcenterActivity.this.mClickType = 4;
        }
    }

    class C11287 implements OnDismissListener {
        C11287() {
        }

        public void onDismiss(DialogInterface dialog) {
            LogUtils.m1568d(UcenterActivity.LOG_TAG, ">>>>>showAccountExceptionDialog --- OnDismissListener()");
            UcenterActivity.this.mPresenter.clearUserInfo();
            UcenterActivity.this.resetPingbackE();
            UcenterActivity.this.mPageIndex = 1;
            UcenterActivity.this.initUserInfoCard();
            UcenterActivity.this.initFirstPageCard();
            UcenterActivity.this.mIsLogin = false;
            UcenterActivity.this.mIsVip = false;
            GetInterfaceTools.getLoginProvider().startLoginActivity(UcenterActivity.this, UcenterActivity.this.mS1, 2);
        }
    }

    class C11308 implements MyObserver {

        class C11291 implements Runnable {
            C11291() {
            }

            public void run() {
                LogUtils.m1568d(UcenterActivity.LOG_TAG, ">>>>> on receive history change event");
                UcenterActivity.this.updateHistoryCard(false);
            }
        }

        C11308() {
        }

        public void update(String event) {
            UcenterActivity.this.mHandler.post(new C11291());
        }
    }

    static class UCenterActionPolicy extends ActionPolicy {
        WeakReference<UcenterActivity> mOuter;

        public UCenterActionPolicy(UcenterActivity outer) {
            this.mOuter = new WeakReference(outer);
        }

        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            super.onItemClick(parent, holder);
            LoginPingbackUtils.getInstance().setS2(LoginConstant.S2_UCENTER);
            if (holder != null) {
                UcenterActivity ucenterActivity = (UcenterActivity) this.mOuter.get();
                if (ucenterActivity != null) {
                    Item item = ucenterActivity.mUIKitEngine.getPage().getItem(holder.getLayoutPosition());
                    ItemInfoModel model = item.getModel();
                    if (model != null && (model.getActionModel() instanceof AlbumVideoLiveActionModel)) {
                        if ("history".equals(model.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SOURCE))) {
                            LoginPingbackUtils.getInstance().setS2("8");
                            ucenterActivity.mClickType = 3;
                        }
                    }
                    if (model.getItemType() == UIKitConfig.ITEM_TYPE_HISTORY_ALL_ENTRY) {
                        ucenterActivity.mClickType = 2;
                        if (ucenterActivity.mRecordData == null) {
                            ucenterActivity.mRecordData = new UcenterRecordAllData("全部记录", false);
                        }
                        List<HistoryInfo> historyInfosList = GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10);
                        List albums = new ArrayList();
                        if (ListUtils.isEmpty((List) historyInfosList)) {
                            LogUtils.m1571e(UcenterActivity.LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), historyInfosList is empty");
                        } else {
                            LogUtils.m1570d(UcenterActivity.LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), size = ", Integer.valueOf(historyInfosList.size()));
                            for (HistoryInfo historyInfo : historyInfosList) {
                                albums.add(historyInfo.getAlbum());
                            }
                        }
                        if (ListUtils.isEmpty(albums) || albums.size() <= 6 || ucenterActivity.mIsLogin) {
                            ucenterActivity.mRecordData.setNeedToLogin(false);
                        } else {
                            ucenterActivity.mRecordData.setNeedToLogin(true);
                        }
                        if (ucenterActivity.mRecordModel == null) {
                            ucenterActivity.mRecordModel = (UcenterRecordAllActionModel) ActionModelFactory.createUcenterRecordAllModel(ucenterActivity.mRecordData);
                        }
                        model.setActionModel(ucenterActivity.mRecordModel);
                    }
                    if (item != null && (item instanceof SettingItem)) {
                        SettingItem settingItem = (SettingItem) item;
                        if (settingItem.getSettingItemType() == 9) {
                            ucenterActivity.mPresenter.startSecurityCenter();
                        } else if (settingItem.getSettingItemType() == 10) {
                            ucenterActivity.mPresenter.clickLogout();
                        }
                    }
                    if (model != null && (model.getActionModel() instanceof VipVideoActionModel)) {
                        ucenterActivity.mClickType = 5;
                        boolean isOpenapi = false;
                        int flag = -1;
                        Intent intent = ucenterActivity.getIntent();
                        if (intent != null) {
                            isOpenapi = intent.getBooleanExtra("from_openapi", false);
                            flag = intent.getFlags();
                        }
                        VipVideoActionModel model2 = (VipVideoActionModel) model.getActionModel();
                        model2.setFlag(flag);
                        model2.setOpenapi(isOpenapi);
                    }
                }
            }
        }

        public void onFirstLayout(ViewGroup parent) {
            UcenterActivity ucenterActivity = (UcenterActivity) this.mOuter.get();
            if (ucenterActivity != null && !ucenterActivity.mRootView.hasFocus()) {
                ucenterActivity.mRootView.requestFocus();
            }
        }

        public void onScrollBefore(ViewGroup parent, ViewHolder holder) {
            UcenterActivity ucenterActivity = (UcenterActivity) this.mOuter.get();
            if (ucenterActivity != null) {
                Item item = ucenterActivity.mUIKitEngine.getPage().getItem(holder.getLayoutPosition());
                if (item == null) {
                    return;
                }
                if (item.getParent().getLine() == 1) {
                    ucenterActivity.mUIKitEngine.getPage().setTopBarHeight(ucenterActivity.mUIKitEngine.getPage().getCard(0).getModel().getBodyHeight());
                    return;
                }
                ucenterActivity.mUIKitEngine.getPage().setTopBarHeight(ResourceUtil.getDimen(C0508R.dimen.dimen_30dp));
            }
        }

        public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
            UcenterActivity ucenterActivity = (UcenterActivity) this.mOuter.get();
            if (ucenterActivity != null) {
                Page page = ucenterActivity.mUIKitEngine.getPage();
                Card card = page.getItem(cast(parent).getFocusPosition()).getParent();
                if (card != null && page.shouldLoadMore()) {
                    synchronized (page) {
                        List<Card> cardList = page.getCards();
                        Card lastCard = (Card) cardList.get(cardList.size() - 1);
                        int size = cardList.size();
                        int curPos = cardList.indexOf(card);
                        if (lastCard != null && size - curPos <= 4 && ucenterActivity.mIsLoadMore) {
                            ucenterActivity.mIsLoadMore = false;
                            VrsHelper.groupDetailPage.call(new VrsCallback(ucenterActivity, 2), ucenterActivity.mSourceID, "0", String.valueOf(ucenterActivity.mPageIndex));
                        }
                    }
                }
            }
        }
    }

    static class VrsCallback<ApiResultGroupDetail> implements IVrsCallback<ApiResultGroupDetail> {
        static final int FIRST_REQUEST = 1;
        static final int PAGE_REQUEST = 2;
        WeakReference<UcenterActivity> mOuter;
        private int mRequestType;

        public VrsCallback(UcenterActivity outer, int requstType) {
            this.mOuter = new WeakReference(outer);
            this.mRequestType = requstType;
        }

        public void onSuccess(ApiResultGroupDetail apiResultGroupDetail) {
            UcenterActivity ucenterActivity = (UcenterActivity) this.mOuter.get();
            if (ucenterActivity != null) {
                switch (this.mRequestType) {
                    case 1:
                        dealFirstRequest(apiResultGroupDetail, ucenterActivity);
                        return;
                    case 2:
                        dealPageRequest(apiResultGroupDetail, ucenterActivity);
                        return;
                    default:
                        return;
                }
            }
        }

        private void dealPageRequest(ApiResultGroupDetail apiResultGroupDetail, final UcenterActivity ucenterActivity) {
            LogUtils.m1570d(UcenterActivity.LOG_TAG, ">>>>> request new page data:", Integer.valueOf(ucenterActivity.mPageIndex));
            List<CardInfoModel> moreModels = CardInfoBuildTool.buildCardList(apiResultGroupDetail, ucenterActivity.mSourceID, ucenterActivity.mPageIndex, ucenterActivity.mUIKitEngine.getId(), false);
            List<CardInfoModel> filterModels = new ArrayList();
            if (!ListUtils.isEmpty((List) moreModels)) {
                for (CardInfoModel newModel : moreModels) {
                    boolean filterFlag = false;
                    CharSequence newType = newModel.mCardId;
                    for (CardInfoModel oldModel : ucenterActivity.mCardInfoModels) {
                        CharSequence oldType = oldModel.mCardId;
                        if (!StringUtils.isEmpty(newType) && !StringUtils.isEmpty(oldType) && newType.equals(oldType)) {
                            LogUtils.m1570d(UcenterActivity.LOG_TAG, ">>>>>filter same cardType=", newType);
                            filterFlag = true;
                            break;
                        }
                    }
                    if (!filterFlag) {
                        filterModels.add(newModel);
                    }
                }
            }
            if (ListUtils.isEmpty((List) filterModels)) {
                ucenterActivity.mHandler.post(new Runnable() {
                    public void run() {
                        ucenterActivity.mUIKitEngine.appendData(null);
                    }
                });
                return;
            }
            for (CardInfoModel model : filterModels) {
                ucenterActivity.mCardInfoModels.add(model);
            }
            ucenterActivity.mPageIndex = ucenterActivity.mPageIndex + 1;
            ucenterActivity.mIsLoadMore = true;
            final List<CardInfoModel> result = filterModels;
            ucenterActivity.mHandler.post(new Runnable() {
                public void run() {
                    ucenterActivity.mUIKitEngine.appendData(result);
                }
            });
        }

        private void dealFirstRequest(ApiResultGroupDetail apiResultGroupDetail, final UcenterActivity ucenterActivity) {
            ucenterActivity.mCardInfoModels = CardInfoBuildTool.buildCardList(apiResultGroupDetail, ucenterActivity.mSourceID, 1, ucenterActivity.mUIKitEngine.getId(), false);
            ucenterActivity.mCardInfoModels.add(0, ucenterActivity.mHeadModel);
            for (int i = 0; i < ucenterActivity.mCardInfoModels.size(); i++) {
                CardInfoModel cardInfoModel = (CardInfoModel) ucenterActivity.mCardInfoModels.get(i);
                if ("history".equals(cardInfoModel.mSource)) {
                    ucenterActivity.mHistoryModel = cardInfoModel;
                    ucenterActivity.mHistoryIndex = i;
                    break;
                }
            }
            for (CardInfoModel cardInfoModel2 : ucenterActivity.mCardInfoModels) {
                List<HistoryInfo> historyInfosList;
                List<Album> albums;
                if (cardInfoModel2.getCardType() == UIKitConfig.CARD_TYPE_VIP) {
                    IDynamicResult dynamicQDataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
                    String value = "";
                    if (dynamicQDataModel != null) {
                        value = dynamicQDataModel.getHomeHeaderVipText();
                    }
                    cardInfoModel2.setTitleTips(value);
                    if (ucenterActivity.mHistoryModel != null) {
                        historyInfosList = GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10);
                        albums = new ArrayList();
                        if (ListUtils.isEmpty((List) historyInfosList)) {
                            LogUtils.m1570d(UcenterActivity.LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), size = ", Integer.valueOf(historyInfosList.size()));
                            for (HistoryInfo historyInfo : historyInfosList) {
                                albums.add(historyInfo.getAlbum());
                            }
                        } else {
                            LogUtils.m1571e(UcenterActivity.LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), historyInfosList is empty");
                        }
                        ucenterActivity.mHistoryModel = CardInfoBuildTool.buildHistoryCard(ucenterActivity.mHistoryModel, albums);
                        ucenterActivity.mCardInfoModels.set(ucenterActivity.mHistoryIndex, ucenterActivity.mHistoryModel);
                    }
                    ucenterActivity.mPageInfoModel.setCardInfoModels(ucenterActivity.mCardInfoModels);
                    LogUtils.m1570d(UcenterActivity.LOG_TAG, ">>>>> request first data success - ", Integer.valueOf(ucenterActivity.mPageIndex), " ----size is", Integer.valueOf(ucenterActivity.mCardInfoModels.size()));
                    ucenterActivity.mPageIndex = ucenterActivity.mPageIndex + 1;
                    ucenterActivity.mIsLoadMore = true;
                    ucenterActivity.mLoadFinished = false;
                    ucenterActivity.mHandler.post(new Runnable() {
                        public void run() {
                            ucenterActivity.mUIKitEngine.setData(ucenterActivity.mPageInfoModel.getCardInfoModels());
                            ucenterActivity.mUIKitEngine.getPage().showLoading();
                            ucenterActivity.mPresenter.stopLoading();
                        }
                    });
                }
            }
            if (ucenterActivity.mHistoryModel != null) {
                historyInfosList = GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10);
                albums = new ArrayList();
                if (ListUtils.isEmpty((List) historyInfosList)) {
                    LogUtils.m1571e(UcenterActivity.LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), historyInfosList is empty");
                } else {
                    LogUtils.m1570d(UcenterActivity.LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), size = ", Integer.valueOf(historyInfosList.size()));
                    while (r9.hasNext()) {
                        albums.add(historyInfo.getAlbum());
                    }
                }
                ucenterActivity.mHistoryModel = CardInfoBuildTool.buildHistoryCard(ucenterActivity.mHistoryModel, albums);
                ucenterActivity.mCardInfoModels.set(ucenterActivity.mHistoryIndex, ucenterActivity.mHistoryModel);
            }
            ucenterActivity.mPageInfoModel.setCardInfoModels(ucenterActivity.mCardInfoModels);
            LogUtils.m1570d(UcenterActivity.LOG_TAG, ">>>>> request first data success - ", Integer.valueOf(ucenterActivity.mPageIndex), " ----size is", Integer.valueOf(ucenterActivity.mCardInfoModels.size()));
            ucenterActivity.mPageIndex = ucenterActivity.mPageIndex + 1;
            ucenterActivity.mIsLoadMore = true;
            ucenterActivity.mLoadFinished = false;
            ucenterActivity.mHandler.post(/* anonymous class already generated */);
        }

        public void onException(ApiException e) {
            UcenterActivity ucenterActivity = (UcenterActivity) this.mOuter.get();
            if (ucenterActivity != null) {
                switch (this.mRequestType) {
                    case 1:
                        firstRequesError(ucenterActivity);
                        return;
                    case 2:
                        pageRequestError(ucenterActivity);
                        return;
                    default:
                        return;
                }
            }
        }

        private void pageRequestError(UcenterActivity ucenterActivity) {
            LogUtils.m1571e("test", ">>>>>onException");
            ucenterActivity.mLoadFinished = true;
        }

        private void firstRequesError(final UcenterActivity ucenterActivity) {
            LogUtils.m1571e(UcenterActivity.LOG_TAG, ">>>>>exception!---card data call faild");
            ucenterActivity.mHandler.post(new Runnable() {
                public void run() {
                    ucenterActivity.mPresenter.stopLoading();
                    ucenterActivity.showErrorUI();
                }
            });
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_fragment_my);
        initParams();
        initViewAndUIKit();
        initUserInfoCard();
        this.mPresenter.showLoading();
        initFirstPageCard();
        setPingbackActionPolicy();
    }

    private void initViewAndUIKit() {
        this.mIsLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext);
        this.mIsVip = GetInterfaceTools.getIGalaAccountManager().isVip();
        this.mCardFocusHelper = new CardFocusHelper(findViewById(C0508R.id.card_focus));
        this.mRootView = (BlocksView) findViewById(C0508R.id.epg_ucenter_page);
        this.mRootView.setShakeForbidden(33);
        this.mRootView.setPadding(0, 0, 0, ResourceUtil.getPx(35));
        this.mUIKitEngine = UIKitEngine.newInstance(this);
        this.mUIKitEngine.getUIKitBuilder().registerSpecialCard(HistoryInfoHelper.MSG_MERGE, UCenterCard.class);
        this.mUIKitEngine.getUIKitBuilder().registerSpecialItem(220, UCenterItem.class, UCenterItemView.class);
        this.mUIKitEngine.getUIKitBuilder().registerSpecialItem(216, DailyNewsItem.class, DailyNewsItemView.class);
        this.mUIKitEngine.bindView(this.mRootView);
        this.mPageInfoModel = new PageInfoModel();
        this.mActionPolicy = new UCenterActionPolicy(this);
        this.mUIKitEngine.getPage().registerActionPolicy(this.mActionPolicy);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_CLOUD_SYNC_COMPLETED, this.mHistoryChangedObserver);
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.HISTORY_CLOUD_SYNC_COMPLETED, this.mHistoryChangedObserver);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_DB_RELOAD_COMPLETED, this.mHistoryChangedObserver);
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.HISTORY_DB_RELOAD_COMPLETED, this.mHistoryChangedObserver);
    }

    private void initParams() {
        this.mContext = this;
        this.mPresenter = new UCenterPresenter(this);
        setPingbackPage(PingbackPage.Ucenter);
        Intent intent = getIntent();
        if (intent != null) {
            this.mS1 = intent.getStringExtra(LoginConstant.S1_TAB);
            this.mPresenter.setPingbackS1(this.mS1);
        }
        this.mE = PingBackUtils.createEventId();
        intent.putExtra(LoginConstant.E_TAB, this.mE);
        UcenterPingbackUtils.getInstance().setE(this.mE);
        this.mPresenter.setPingbackE(this.mE);
        try {
            if (Boolean.parseBoolean(new BufferedReader(new InputStreamReader(Runtime.getRuntime().exec("getprop test-sourceid").getInputStream())).readLine())) {
                this.mSourceID = "482650912";
            }
        } catch (IOException e) {
            LogUtils.m1571e(LOG_TAG, ">>>>>get prop error");
        }
    }

    private CardInfoModel createHeadModel() {
        CardInfoModel model = new CardInfoModel();
        model.setCardType(UIKitConfig.CARD_TYPE_UCENTER);
        model.setBodyHeight((short) ResourceUtil.getDimen(C0508R.dimen.dimen_330dp));
        return model;
    }

    private void initUserInfoCard() {
        this.mHeadModel = createHeadModel();
        if (ListUtils.isEmpty(this.mCardInfoModels)) {
            LogUtils.m1568d(LOG_TAG, ">>>>> init userinfo ui ---- mCardInfoModels is null");
            this.mCardInfoModels = new ArrayList();
            this.mCardInfoModels.add(this.mHeadModel);
        } else {
            LogUtils.m1568d(LOG_TAG, ">>>>> init userinfo ui ---- mCardInfoModels is not null");
            this.mCardInfoModels.set(0, this.mHeadModel);
            this.mIsLoadMore = true;
        }
        this.mUIKitEngine.setData(this.mCardInfoModels);
    }

    private void initFirstPageCard() {
        this.mPageIndex = 1;
        VrsHelper.groupDetailPage.call(new VrsCallback(this, 1), this.mSourceID, "0", "1");
    }

    protected void onResume() {
        super.onResume();
        LoginPingbackUtils.getInstance().setS2(LoginConstant.S2_UCENTER);
        this.mUIKitEngine.start();
        resetViews();
        boolean isLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext);
        boolean isVip = GetInterfaceTools.getIGalaAccountManager().isVip();
        if (!(this.mIsLogin == isLogin && this.mIsVip == isVip)) {
            LogUtils.m1568d(LOG_TAG, ">>>>> refresh u center ui.");
            CardFocusHelper.forceVisible(this, false);
            resetPingbackE();
            hideErrorUI();
            this.mPageIndex = 1;
            this.mClickType = 0;
            this.mUIKitEngine.getPage().backToTop();
            initUserInfoCard();
            initFirstPageCard();
            if (isLogin) {
                this.mRootView.setFocusPosition(1);
                this.mRootView.requestFocus();
            }
            this.mIsLogin = isLogin;
            this.mIsVip = isVip;
        }
        if (this.mClickType == 3 || this.mClickType == 2) {
            updateHistoryCard(true);
        } else {
            updateHistoryCard(false);
        }
        this.mClickType = 0;
        this.mPresenter.checkUserInfo(true);
        this.mPresenter.loadAlbumPicture();
        this.mPingbackActionPolicy.initTimestamp(this.mRootView);
    }

    private void updateHistoryCard(boolean updateFocus) {
        if (this.mHistoryModel != null) {
            List<HistoryInfo> historyInfosList = GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10);
            List<Album> albums = new ArrayList();
            if (ListUtils.isEmpty((List) historyInfosList)) {
                LogUtils.m1571e(LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), historyInfosList is empty");
            } else {
                LogUtils.m1570d(LOG_TAG, ">>>>> GetInterfaceTools.getIHistoryCacheManager().getLatestVideoHistory(10), size = ", Integer.valueOf(historyInfosList.size()));
                for (HistoryInfo historyInfo : historyInfosList) {
                    albums.add(historyInfo.getAlbum());
                }
            }
            this.mHistoryModel = CardInfoBuildTool.buildHistoryCard(this.mHistoryModel, albums);
            this.mCardInfoModels.set(this.mHistoryIndex, this.mHistoryModel);
            if (updateFocus) {
                int focusPos;
                if (this.mClickType == 3) {
                    focusPos = this.mUIKitEngine.getPage().getCard(this.mHistoryIndex).getBlockLayout().getFirstPosition();
                } else {
                    focusPos = this.mUIKitEngine.getPage().getCard(this.mHistoryIndex).getBlockLayout().getLastPosition();
                }
                if (focusPos != -1) {
                    this.mRootView.setFocusPosition(focusPos);
                }
            }
            this.mUIKitEngine.changeCardModel(this.mHistoryModel);
        }
    }

    private void resetViews() {
        if (this.mConfirmDialog != null && this.mConfirmDialog.isShowing()) {
            this.mConfirmDialog.dismiss();
        }
    }

    public void showSecurityUI() {
        if (this.mMyCenenterLayerView == null) {
            this.mMyCenenterLayerView = findViewById(C0508R.id.epg_my_quick_login);
            this.mChangePassView = this.mMyCenenterLayerView.findViewById(C0508R.id.epg_change_password_qrlayout);
            this.mChangePassFocusView = this.mMyCenenterLayerView.findViewById(C0508R.id.epg_change_password_qrfocus);
            this.mChangePassLoadingBg = this.mMyCenenterLayerView.findViewById(C0508R.id.epg_changepass_loading_bg);
            this.mViewLoading = this.mMyCenenterLayerView.findViewById(C0508R.id.epg_view_loading);
            this.mViewFailure = this.mMyCenenterLayerView.findViewById(C0508R.id.epg_view_failure);
            this.mChangePassQRImg = (ImageView) this.mMyCenenterLayerView.findViewById(C0508R.id.epg_change_password_img);
        }
        this.mChangePassView.setClickable(false);
        this.mMyCenenterLayerView.setVisibility(0);
        this.mViewFailure.setVisibility(4);
        this.mChangePassQRImg.setVisibility(4);
        this.mChangePassLoadingBg.setVisibility(0);
        this.mViewLoading.setVisibility(0);
        this.mChangePassView.setBackgroundColor(ResourceUtil.getColor(C0508R.color.transparent));
        this.mChangePassView.setVisibility(0);
        this.mChangePassFocusView.setVisibility(0);
        this.mMyCenenterLayerView.setBackgroundDrawable(Project.getInstance().getControl().getBackgroundDrawable());
        this.mChangePassView.requestFocus();
        this.mChangePassView.setOnClickListener(new C11221());
    }

    public void hideSecurityUI() {
        this.mMyCenenterLayerView.setVisibility(4);
    }

    public void showLogoutUI() {
        this.mConfirmDialog = new GlobalVipDialog(this);
        this.mConfirmDialog.setGlobalVipCloudViewCallBack(this.mGlobalVipCloudViewCallBack);
        OnClickListener okListener = new C11232();
        OnClickListener cancelListener = new C11243();
        this.mConfirmDialog.setOnDismissListener(new C11254());
        List bitmapAlbums = this.mPresenter.getAlbumPics();
        if (ListUtils.isEmpty(bitmapAlbums)) {
            this.mConfirmDialog.setParams(ResourceUtil.getStr(C0508R.string.confirm_logout_vip), ResourceUtil.getStr(C0508R.string.logout_ok), okListener, ResourceUtil.getStr(C0508R.string.logout_cancel), cancelListener, true, null, null, null);
        } else if (bitmapAlbums.size() < 3) {
            this.mConfirmDialog.setParams(ResourceUtil.getStr(C0508R.string.confirm_logout_vip), ResourceUtil.getStr(C0508R.string.logout_ok), okListener, ResourceUtil.getStr(C0508R.string.logout_cancel), cancelListener, true, null, null, null);
        } else {
            this.mConfirmDialog.setParams(ResourceUtil.getStr(C0508R.string.confirm_logout_vip), ResourceUtil.getStr(C0508R.string.logout_ok), okListener, ResourceUtil.getStr(C0508R.string.logout_cancel), cancelListener, true, (BitmapAlbum) bitmapAlbums.get(0), (BitmapAlbum) bitmapAlbums.get(1), (BitmapAlbum) bitmapAlbums.get(2));
        }
        boolean isVipStatus = GetInterfaceTools.getIGalaAccountManager().isVip();
        LogUtils.m1568d(LOG_TAG, "isVipStatus = " + isVipStatus);
        this.mConfirmDialog.setVipStyle(isVipStatus);
        this.mConfirmDialog.show();
    }

    public void showExceptionUI(String tipValue) {
        final GlobalDialog globalDialog = new GlobalDialog(this);
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View view) {
                LogUtils.m1568d(UcenterActivity.LOG_TAG, ">>>>>showAccountExceptionDialog --- OnClickListener -- ok");
                globalDialog.dismiss();
            }
        };
        globalDialog.setOnDismissListener(new C11287());
        globalDialog.setParams(tipValue, ResourceUtil.getStr(C0508R.string.arefresh_login_ok), okListener);
        globalDialog.show();
    }

    public void showQRImageUI(Bitmap bitmap) {
        if (bitmap != null) {
            LogUtils.m1571e(LOG_TAG, "Success --- QRImage Load");
            this.mChangePassView.setClickable(false);
            this.mChangePassView.setBackgroundColor(ResourceUtil.getColor(C0508R.color.gala_write));
            this.mChangePassLoadingBg.setVisibility(4);
            this.mViewLoading.setVisibility(4);
            this.mViewFailure.setVisibility(4);
            this.mChangePassQRImg.setVisibility(0);
            this.mChangePassQRImg.setImageBitmap(bitmap);
            return;
        }
        LogUtils.m1571e(LOG_TAG, "Exception --- QRImage Load");
        this.mChangePassQRImg.setVisibility(4);
        this.mViewLoading.setVisibility(4);
        this.mViewFailure.setVisibility(0);
        this.mChangePassView.setClickable(true);
    }

    public void showQRFailUI() {
        this.mChangePassQRImg.setVisibility(4);
        this.mViewLoading.setVisibility(4);
        this.mViewFailure.setVisibility(0);
        this.mChangePassView.setClickable(true);
    }

    public void onBackPressed() {
        if (this.mMyCenenterLayerView == null || this.mMyCenenterLayerView.getVisibility() != 0) {
            super.onBackPressed();
        } else {
            this.mPresenter.hideSecurity();
        }
    }

    protected void onStop() {
        super.onStop();
        this.mUIKitEngine.stop();
    }

    protected View getBackgroundContainer() {
        return findViewById(C0508R.id.epg_my_layout);
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mHistoryChangedObserver != null) {
            GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_CLOUD_SYNC_COMPLETED, this.mHistoryChangedObserver);
            GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.HISTORY_DB_RELOAD_COMPLETED, this.mHistoryChangedObserver);
        }
        this.mPresenter.onDestory();
        this.mUIKitEngine.destroy();
        this.mCardFocusHelper.destroy();
        if (this.mUIKitEngine.getPage() != null && this.mActionPolicy != null) {
            this.mUIKitEngine.getPage().unregisterActionPolicy(this.mActionPolicy);
        }
    }

    public void showLoadingUI() {
        if (this.mProgressBar == null) {
            this.mProgressBar = (ProgressBarItem) findViewById(C0508R.id.epg_my_card_progress);
            this.mProgressBar.setText(ResourceUtil.getStr(C0508R.string.loading_txt));
        }
        this.mProgressBar.setVisibility(0);
    }

    public void hideLoadingUI() {
        if (this.mProgressBar != null && this.mProgressBar.getVisibility() == 0) {
            this.mProgressBar.setVisibility(4);
        }
    }

    public void showErrorUI() {
        if (this.mTxtError == null) {
            this.mTxtError = (TextView) findViewById(C0508R.id.epg_txt_my_data_error);
            this.mTxtError.setTextColor(ResourceUtil.getColor(C0508R.color.albumview_yellow_color));
            this.mTxtError.setText(ResourceUtil.getStr(C0508R.string.my_error_tip));
        }
        this.mTxtError.setVisibility(0);
    }

    public void hideErrorUI() {
        if (this.mTxtError != null && this.mTxtError.getVisibility() == 0) {
            this.mTxtError.setVisibility(4);
        }
    }

    public void refreshUserInfoUI() {
        LogUtils.m1568d(LOG_TAG, ">>>>> checkUserInfo - refresh userinfo ui - after check userinfo");
        initUserInfoCard();
    }

    protected void onPause() {
        super.onPause();
        this.mPingbackActionPolicy.onSendUcenterCardShowPingback(this.mRootView, this.mUIKitEngine.getPage(), false);
    }

    private void setPingbackActionPolicy() {
        UcenterPingbackActionPolicy actionPolicy = new UcenterPingbackActionPolicy(this.mContext, this.mUIKitEngine.getPage(), this.mE);
        if (this.mPingbackActionPolicy != null) {
            this.mUIKitEngine.getPage().unregisterActionPolicy(this.mPingbackActionPolicy);
        }
        this.mPingbackActionPolicy = actionPolicy;
        if (this.mUIKitEngine != null) {
            this.mUIKitEngine.getPage().registerActionPolicy(this.mPingbackActionPolicy);
        }
    }

    public void startFlashAnimation() {
        if (this.mFlashView == null) {
            this.mFlashView = (UCenterFlashView) findViewById(C0508R.id.epg_ucenter_flash_btn);
        }
        this.mFlashView.startAnimation();
    }

    public void stopFlashAnimation() {
        if (this.mFlashView != null) {
            this.mFlashView.stopAnimation();
        }
    }

    public void unBindAnimation() {
        if (this.mFlashView != null) {
            this.mFlashView.unBindAnimation();
        }
    }

    private void resetPingbackE() {
        this.mE = PingBackUtils.createEventId();
        UcenterPingbackUtils.getInstance().setE(this.mE);
        if (this.mPresenter != null) {
            this.mPresenter.setPingbackE(this.mE);
        }
        if (this.mPingbackActionPolicy != null) {
            this.mPingbackActionPolicy.setPingbackE(this.mE);
        }
    }
}
