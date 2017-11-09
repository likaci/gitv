package com.gala.video.app.epg.ui.ucenter.record;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher.IMessageNotification;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.constant.IFootConstant;
import com.gala.video.app.epg.ui.albumlist.enums.IFootEnum.FootLeftRefreshPage;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel;
import com.gala.video.app.epg.ui.albumlist.presenter.AlbumTopPresenter;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.albumlist.utils.QAPingback;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.app.epg.ui.albumlist.widget.DeleteClearMenu;
import com.gala.video.app.epg.ui.albumlist.widget.DeleteClearMenu.onClickCallback;
import com.gala.video.app.epg.ui.ucenter.record.NavigationBarFragment.LabelChangedListener;
import com.gala.video.app.epg.ui.ucenter.record.RecordFavouriteContentPresenter.OnStatusListener;
import com.gala.video.app.epg.ui.ucenter.record.contract.NavigationBarContract;
import com.gala.video.app.epg.ui.ucenter.record.contract.RecordFavouriteContentContract.Presenter;
import com.gala.video.app.epg.ui.ucenter.record.contract.RecordFavouriteContentContract.View;
import com.gala.video.app.epg.ui.ucenter.record.utils.RecordFavouriteUtil;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.lib.share.utils.ResourceUtil;
import org.xbill.DNS.WKSRecord.Service;

public class RecordFavouriteActivity extends QMultiScreenActivity implements onClickCallback, OnStatusListener, OnFocusChangeListener, OnItemClickListener {
    private AlbumTopPresenter mActionBarPresenter;
    private Presenter mContentPresenter;
    private View mContentView;
    private FootLeftRefreshPage mDefaultPage;
    private DeleteClearMenu mDeleteClearMenu;
    private boolean mFirstDataLoad = true;
    private android.view.View mFocusView;
    private AlbumInfoModel mInfoModel;
    private LabelChangedListener mLabelChangedListener = new C12051();
    private IMessageNotification mMessageNotification = new C12062();
    private NavigationBarContract.View mNavigationBarView;
    protected boolean mRefreshImmediately;
    private TextView mTagDesTxt;
    private TextView mTextTipLogin;
    private TextView mTextTipNoLogin;

    class C12051 implements LabelChangedListener {
        C12051() {
        }

        public void onLabelChanged(FootLeftRefreshPage page) {
            if (page != RecordFavouriteActivity.this.mContentPresenter.getPage()) {
                RecordFavouriteActivity.this.mNavigationBarView.setFocusLeaveForbidden(194);
                RecordFavouriteActivity.this.mContentPresenter.start(page);
                RecordFavouriteActivity.this.setTopTagTxt(page, 0);
            }
        }
    }

    class C12062 implements IMessageNotification {
        C12062() {
        }

        public void onMessageReceive(IMsgContent content) {
            RecordFavouriteActivity.this.mActionBarPresenter.onMessageReceive(content);
        }
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFormat(-2);
        ImageProviderApi.getImageProvider().stopAllTasks();
        setContentView(C0508R.layout.epg_activity_record_favourite);
        init();
    }

    private void init() {
        initIntentModel();
        initDefaultPage();
        setBackground();
        initViews();
    }

    private void initDefaultPage() {
        String pageType = this.mInfoModel.getPageType();
        if (IAlbumConfig.UNIQUE_FOOT_FAVOURITE.equals(pageType)) {
            this.mDefaultPage = FootLeftRefreshPage.FAVOURITE;
        } else if (IAlbumConfig.UNIQUE_FOOT_SUBSCRIBLE.equals(pageType)) {
            this.mDefaultPage = FootLeftRefreshPage.SUBSCRIBE;
        } else {
            this.mDefaultPage = FootLeftRefreshPage.PLAY_HISTORY_LONG;
        }
    }

    private void initViews() {
        this.mDeleteClearMenu = (DeleteClearMenu) findViewById(C0508R.id.epg_delete_clear_menu);
        this.mTextTipNoLogin = (TextView) findViewById(C0508R.id.epg_record_menu_text_nologin);
        this.mTextTipLogin = (TextView) findViewById(C0508R.id.epg_record_menu_text_login);
        this.mDeleteClearMenu.setOnClickCallback(this);
        this.mActionBarPresenter = new AlbumTopPresenter(this, getTopLayout(), this.mInfoModel);
        this.mNavigationBarView = (NavigationBarFragment) getSupportFragmentManager().findFragmentById(C0508R.id.epg_navigation_bar);
        NavigationBarPresenter navigationBarPresenter = new NavigationBarPresenter(this.mNavigationBarView, this.mInfoModel);
        this.mNavigationBarView.setLabelChangedListener(this.mLabelChangedListener);
        this.mNavigationBarView.setOnFocusChangeListener(this);
        this.mNavigationBarView.setOnItemClickListener(this);
        this.mNavigationBarView.setDefaultPage(this.mDefaultPage);
        RecordFavouriteContentFragment contentView = (RecordFavouriteContentFragment) getSupportFragmentManager().findFragmentById(C0508R.id.epg_content);
        this.mContentView = contentView;
        this.mContentView.setOnFocusChangeListener(this);
        this.mContentPresenter = new RecordFavouriteContentPresenter(this, contentView, this.mInfoModel);
        this.mContentPresenter.setOnStatusListener(this);
        this.mContentPresenter.loadDefaultPage(this.mDefaultPage);
        findViewById(C0508R.id.epg_q_album_tag_cutting_line).setVisibility(0);
        TextView channelTv = (TextView) findViewById(C0508R.id.epg_q_album_channel_name_txt);
        channelTv.setVisibility(0);
        channelTv.setText(IFootConstant.STR_FILM_FOOT);
    }

    public void onResume() {
        super.onResume();
        this.mActionBarPresenter.startVipAnimation(false);
        if (!UserUtil.isLogin() && this.mContentView.isDeleteMode()) {
            this.mContentView.leaveDeleteMode();
        }
    }

    protected void onPause() {
        super.onPause();
        this.mActionBarPresenter.stopVipAnimation();
    }

    protected void onStart() {
        super.onStart();
        this.mActionBarPresenter.onStart();
        MessagePromptDispatcher.get().register(this.mMessageNotification);
    }

    protected void onStop() {
        super.onStop();
        this.mActionBarPresenter.onStop();
        MessagePromptDispatcher.get().unregister(this.mMessageNotification);
        trimActivityMemory();
    }

    private void trimActivityMemory() {
        String model = Build.MODEL;
        LogUtils.m1568d("RecordFavouriteActivity", "model：" + model);
        LogUtils.m1568d("RecordFavouriteActivity", "isLoaderWEBActivity：" + isLoaderWEBActivity);
        if (model != null && model.toLowerCase().equals("mibox3s") && !isLoaderWEBActivity) {
            try {
                cleanMemory();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private android.view.View getTopLayout() {
        return findViewById(C0508R.id.epg_q_album_top_panel);
    }

    public void setBackground() {
        Drawable drawable;
        if (MemoryLevelInfo.isLowMemoryDevice()) {
            drawable = ResourceUtil.getDrawable(C0508R.drawable.epg_app_background);
        } else {
            drawable = Project.getInstance().getControl().getBackgroundDrawable();
        }
        getWindow().setBackgroundDrawable(drawable);
    }

    private void initIntentModel() {
        AlbumIntentModel intentModel = null;
        Intent intent = getIntent();
        if (intent != null) {
            intentModel = (AlbumIntentModel) intent.getSerializableExtra("intent_model");
            if (intentModel == null) {
                int channelId = intent.getIntExtra("intent_channel_id", -1);
                String channelName = AlbumInfoFactory.getChannelNameByChannelId(channelId);
                intentModel = new AlbumIntentModel();
                intentModel.setFrom(AlbumEnterFactory.CHANNEL_STR + channelId + AlbumEnterFactory.SIGN_STR);
                intentModel.setChannelId(channelId);
                intentModel.setChannelName(channelName);
            }
        }
        this.mInfoModel = new AlbumInfoModel(intentModel);
        this.mInfoModel.setIdentification(IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        boolean isDown;
        if (event.getAction() == 0) {
            isDown = true;
        } else {
            isDown = false;
        }
        int keyCode = event.getKeyCode();
        if (isDown && event.getRepeatCount() == 0) {
            if (keyCode == 82) {
                if (FootLeftRefreshPage.SUBSCRIBE == this.mNavigationBarView.getPage()) {
                    return true;
                }
                if (isMenuShown()) {
                    hideMenuViewWithPingback(false);
                    return true;
                } else if (this.mContentView.isEmpty()) {
                    return true;
                } else {
                    showMenuView();
                    return true;
                }
            } else if (keyCode == 4) {
                if (isMenuShown()) {
                    hideMenuViewWithPingback(true);
                    return true;
                } else if (this.mContentView.isDeleteMode()) {
                    this.mContentView.leaveDeleteMode();
                    return true;
                }
            }
        }
        return super.handleKeyEvent(event);
    }

    private void showMenuView() {
        this.mFocusView = getWindow().getDecorView().findFocus();
        AnimationUtil.translateAnimationY(this.mDeleteClearMenu, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 0.0f, 200, new AccelerateDecelerateInterpolator());
        this.mDeleteClearMenu.setVisibility(0);
        this.mDeleteClearMenu.requestFocusByIndex(0);
        if (UserUtil.isLogin()) {
            this.mTextTipNoLogin.setVisibility(8);
            this.mTextTipLogin.setVisibility(0);
        } else {
            this.mTextTipLogin.setVisibility(8);
            this.mTextTipNoLogin.setVisibility(0);
        }
        QAPingback.recordLayerShowPingback(0, 0, this.mInfoModel);
        if (this.mContentView.isDeleteMode()) {
            QAPingback.recordDeleteClearPingback(4);
        }
    }

    private void hideMenuViewWithPingback(boolean isBack) {
        hideMenuView();
        QAPingback.recordDeleteClearPingback(isBack ? 3 : 4);
    }

    private void hideMenuView() {
        if (this.mFocusView != null) {
            this.mFocusView.requestFocus();
        }
        AnimationUtil.translateAnimationY(this.mDeleteClearMenu, 0.0f, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 150, new AccelerateInterpolator());
        this.mDeleteClearMenu.setVisibility(8);
    }

    private boolean isMenuShown() {
        return this.mDeleteClearMenu.isShown();
    }

    public void OnClickCallback(int index) {
        hideMenuView();
        if (UserUtil.isLogin()) {
            if (index == 0) {
                this.mContentView.enterDeleteMode();
            } else {
                this.mContentView.showClearAllDialog();
            }
            QAPingback.recordDeleteClearPingback(index);
            return;
        }
        FootLeftRefreshPage page = this.mContentPresenter.getPage();
        GetInterfaceTools.getLoginProvider().startLoginActivity(this, RecordFavouriteUtil.getS1FromForPingBack(page), 2);
        QToast.makeTextAndShow(AppRuntimeEnv.get().getApplicationContext(), page == FootLeftRefreshPage.FAVOURITE ? ResourceUtil.getStr(C0508R.string.favourite_login_toast) : ResourceUtil.getStr(C0508R.string.record_login_toast), 5000);
        this.mContentView.setIsFromLoginView(true);
    }

    public void onDataChanged(FootLeftRefreshPage page, int itemCount, int totalCount) {
        if (itemCount == 0 || totalCount == 0) {
            if (this.mFirstDataLoad && this.mNavigationBarView.getPage() == FootLeftRefreshPage.PLAY_HISTORY_LONG) {
                this.mNavigationBarView.setFocusPosition(0);
            }
            if (!this.mActionBarPresenter.hasFocus()) {
                this.mNavigationBarView.requestFocus();
            }
            if (this.mContentView.isDeleteMode()) {
                this.mContentView.leaveDeleteMode();
            }
        }
        if (this.mFirstDataLoad) {
            this.mFirstDataLoad = false;
        }
        if (this.mContentView.isFocusable()) {
            this.mNavigationBarView.setFocusLeaveForbidden(Service.CISCO_FNA);
        } else {
            this.mNavigationBarView.setFocusLeaveForbidden(194);
        }
        setTopTagTxt(page, totalCount);
    }

    public void onError(FootLeftRefreshPage page) {
        this.mNavigationBarView.requestFocus();
        if (this.mContentView.isFocusable()) {
            this.mNavigationBarView.setFocusLeaveForbidden(Service.CISCO_FNA);
        } else {
            this.mNavigationBarView.setFocusLeaveForbidden(194);
        }
        setTopTagTxt(page, 0);
    }

    public void setTopTagTxt(FootLeftRefreshPage page, int count) {
        String tagNameTxt;
        String tagDesTxt;
        if (page == FootLeftRefreshPage.FAVOURITE) {
            tagNameTxt = "";
            tagDesTxt = IFootConstant.STR_FAV;
        } else if (page == FootLeftRefreshPage.SUBSCRIBE) {
            tagNameTxt = "";
            tagDesTxt = IFootConstant.STR_SUBSCRIBLE;
        } else {
            tagNameTxt = IFootConstant.STR_PLAYHISTORY;
            tagDesTxt = page == FootLeftRefreshPage.PLAY_HISTORY_ALL ? IFootConstant.STR_PLAYHISTORY_ALL : IFootConstant.STR_PLAYHISTORY_LONG;
        }
        if (count != 0) {
            getTagDesTxt().setText(String.format(getResources().getString(C0508R.string.top_tag_format), new Object[]{tagNameTxt, tagDesTxt, Integer.valueOf(count)}));
            return;
        }
        getTagDesTxt().setText(tagNameTxt + " " + tagDesTxt);
    }

    private TextView getTagDesTxt() {
        if (this.mTagDesTxt == null) {
            this.mTagDesTxt = (TextView) getTopLayout().findViewById(C0508R.id.epg_q_album_tag_des);
            this.mTagDesTxt.setVisibility(0);
        }
        return this.mTagDesTxt;
    }

    public void onFocusChange(android.view.View v, boolean hasFocus) {
        if (hasFocus) {
            v.setNextFocusUpId(this.mActionBarPresenter.getPreFocusUpId());
            this.mActionBarPresenter.setNextFocusDownId(v.getId());
        }
    }

    public void onItemClick(ViewGroup parent, ViewHolder holder) {
        this.mContentView.requestFocus();
    }
}
