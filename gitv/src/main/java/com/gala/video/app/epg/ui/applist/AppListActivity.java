package com.gala.video.app.epg.ui.applist;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.appmanager.GalaAppManager;
import com.gala.appmanager.GalaAppManager.LoadAppCallback;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.appmanager.appinfo.AppOperation;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.KeyWordType;
import com.gala.video.albumlist4.utils.AnimationUtils;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.applist.activity.AppLauncherActivity;
import com.gala.video.app.epg.ui.applist.adapter.AppListAdapter;
import com.gala.video.app.epg.ui.applist.widget.AppView;
import com.gala.video.app.player.ui.widget.ThreeDimensionalParams;
import com.gala.video.cloudui.CloudUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class AppListActivity extends QMultiScreenActivity implements Observer, LoadAppCallback, OnClickListener, OnFocusChangeListener {
    private static final String LOG_TAG = "EPG/applist/AppListActivity";
    private static final int VIEW_COUNT_PER_ROW = 6;
    private final float SCALE = 1.1f;
    protected AppListAdapter mAdapter;
    protected TextView mAllNumView;
    protected TextView mAllTitleName;
    private GalaAppManager mAppManager;
    private RelativeLayout mAppMenu;
    private String mAppName = "";
    private TextView mAppNotice;
    private TextView mBringInstallItem;
    private TextView mBringToTopItem;
    private TextView mBringUninstallItem;
    protected View mFocusedView;
    protected VerticalGridView mGridView;
    protected List mInfoList;
    private boolean mIsSystemApp = true;
    private boolean mIsUpdateSystem = false;
    protected View mMenuDesView;
    private TextView mMenuTitle;
    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {
        public void onItemClick(ViewGroup arg0, ViewHolder viewHolder) {
            AppListActivity.this.onItemClicked(viewHolder.getLayoutPosition());
        }
    };
    private OnItemFocusChangedListener mOnItemFocusChangedListener = new OnItemFocusChangedListener() {
        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            if (holder != null) {
                AnimationUtils.zoomAnimation(holder.itemView, hasFocus, 1.1f, 200, true);
                if (hasFocus) {
                    AppListActivity.this.mSelectedPosition = holder.getLayoutPosition();
                    AppListActivity.this.mFocusedView = holder.itemView;
                }
            }
        }
    };
    protected int mSelectedPosition = 0;
    private int mTopCount = 0;

    class AppRunnable implements Runnable {
        private String mPackageName;

        AppRunnable(String packageName) {
            this.mPackageName = packageName;
        }

        public void run() {
            AppListActivity.this.mAppManager.startApp(this.mPackageName);
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        prepareView();
        prepareListData();
        setCountView();
        initAdapter();
        initGridView();
    }

    protected void onStart() {
        super.onStart();
        setAdapterData();
    }

    protected void setAdapterData() {
        int count = ListUtils.getCount(this.mInfoList);
        if (LogUtils.mIsDebug) {
            LogUtils.d(LOG_TAG, "setAdapterData() -> setTotalDataSize -> mInfoList.count=" + count);
        }
        this.mGridView.setTotalSize(count);
        this.mAdapter.updateDataList(this.mInfoList);
    }

    protected void setCountView() {
        this.mAllNumView.setText(this.mInfoList.size() + getString(R.string.app_item_count));
    }

    protected void prepareView() {
        CharSequence name = getIntent().getStringExtra("channelName");
        this.mAllTitleName = (TextView) findViewById(R.id.epg_app_title_text);
        if (!StringUtils.isEmpty(name)) {
            this.mAllTitleName.setText(name);
        }
        this.mAllNumView = (TextView) findViewById(R.id.epg_app_sum_text);
        this.mMenuDesView = findViewById(R.id.epg_app_page_menu_desc);
        setDescViewProperty(this.mMenuDesView);
        this.mAppMenu = (RelativeLayout) findViewById(R.id.epg_app_manage_dialog);
        this.mAppNotice = (TextView) findViewById(R.id.epg_app_name);
        this.mMenuTitle = (TextView) findViewById(R.id.epg_text_menu_des);
        this.mBringToTopItem = (TextView) findViewById(R.id.epg_top_btn);
        this.mBringUninstallItem = (TextView) findViewById(R.id.epg_uninstall_btn);
        this.mBringInstallItem = (TextView) findViewById(R.id.epg_install_btn);
        this.mBringToTopItem.setText("置顶");
        this.mBringUninstallItem.setText("卸载");
        this.mBringToTopItem.setOnClickListener(this);
        this.mBringUninstallItem.setOnClickListener(this);
        this.mBringInstallItem.setOnClickListener(this);
        this.mBringToTopItem.setOnFocusChangeListener(this);
        this.mBringUninstallItem.setOnFocusChangeListener(this);
        this.mBringInstallItem.setOnFocusChangeListener(this);
        this.mMenuTitle.setText(this.mMenuTitle.getText().toString());
    }

    protected void setContentView() {
        setContentView(R.layout.epg_activity_app_page);
    }

    protected void prepareListData() {
        LogUtils.d(LOG_TAG, "prepareListData()");
        this.mAppManager = GalaAppManager.createAppManager(getApplicationContext(), this);
        this.mAppManager.addObserver(this);
        this.mAppManager.registerReceiver();
        this.mTopCount = this.mAppManager.getTopCount();
        this.mInfoList = (ArrayList) this.mAppManager.getAllApps();
    }

    protected void initAdapter() {
        this.mAdapter = new AppListAdapter(this);
    }

    private void initGridView() {
        this.mGridView = (VerticalGridView) findViewById(R.id.epg_app_gridview_layout);
        this.mGridView.setNumRows(6);
        this.mGridView.setFocusLeaveForbidden(115);
        this.mGridView.setFocusLoop(true);
        this.mGridView.setFocusMode(1);
        this.mGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mGridView.setPadding(ResourceUtil.getDimen(R.dimen.dimen_22dp), 0, ResourceUtil.getDimen(R.dimen.dimen_4dp), 0);
        int ninePatchBorder = CloudUtils.calcNinePatchBorder(ResourceUtil.getDrawable(R.drawable.share_item_rect_btn_selector));
        this.mGridView.setVerticalMargin(ResourceUtil.getDimen(R.dimen.dimen_16dp) - (ninePatchBorder * 2));
        this.mGridView.setHorizontalMargin(ResourceUtil.getDimen(R.dimen.dimen_16dp) - (ninePatchBorder * 2));
        LogUtils.i(LOG_TAG, "ninePatchBorder = ", Integer.valueOf(ninePatchBorder));
        this.mGridView.setContentWidth(ResourceUtil.getDimen(R.dimen.dimen_188dp) + (ninePatchBorder * 2));
        this.mGridView.setContentHeight(ResourceUtil.getDimen(R.dimen.dimen_188dp) + (ninePatchBorder * 2));
        this.mGridView.setOnItemFocusChangedListener(this.mOnItemFocusChangedListener);
        this.mGridView.setOnItemClickListener(this.mOnItemClickListener);
        this.mGridView.setVerticalScrollBarEnabled(true);
        this.mGridView.setScrollBarDrawable(R.drawable.epg_thumb);
        this.mGridView.setAdapter(this.mAdapter);
        this.mGridView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                if (AppListActivity.this.mGridView.getWidth() != 0 && AppListActivity.this.mGridView.getHeight() != 0) {
                    int low = (ResourceUtil.getDimen(R.dimen.dimen_188dp) / 2) + ResourceUtil.getDimen(R.dimen.dimen_67dp);
                    AppListActivity.this.mGridView.setFocusPlace(low, AppListActivity.this.mGridView.getHeight() - low);
                }
            }
        });
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_app_container);
    }

    protected void onDestroy() {
        LogUtils.i(LOG_TAG, "onDestroy()");
        super.onDestroy();
        try {
            if (this.mAppManager != null) {
                this.mAppManager.deleteObserver(this);
                LogUtils.i(LOG_TAG, "onDestroy() -> mAppManager.deleteObservers()");
            }
        } catch (Exception e) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(LOG_TAG, "onDestroy() e:", e);
            }
        }
    }

    public void update(Observable observable, Object data) {
        LogUtils.d(LOG_TAG, "update()");
        if (data instanceof AppOperation) {
            AppOperation ao = (AppOperation) data;
            int type = ao.getType();
            int index = ao.getIndex();
            AppInfo app = ao.getApp();
            if (LogUtils.mIsDebug) {
                Log.d(LOG_TAG, "update() -> type= " + type + "index=" + index + "mInfoList.size = " + ListUtils.getCount(this.mInfoList));
                if (app != null) {
                    LogUtils.d(LOG_TAG, "update() -> app packageName=" + app.getAppPackageName() + "app name=" + app.getAppName());
                }
            }
            switch (type) {
                case 0:
                    if (this.mInfoList != null) {
                        this.mInfoList.add(app);
                        break;
                    }
                    break;
                case 1:
                    if (this.mInfoList != null && index >= 0 && index < this.mInfoList.size()) {
                        this.mInfoList.remove(index);
                        break;
                    }
                case 3:
                    if (this.mInfoList != null && index >= 0 && index < this.mInfoList.size()) {
                        this.mInfoList.add(this.mTopCount, (AppInfo) this.mInfoList.remove(index));
                        break;
                    }
                case 4:
                    if (this.mInfoList != null && index >= 0 && index < this.mInfoList.size()) {
                        AppInfo localApp = (AppInfo) this.mInfoList.get(index);
                        localApp.setAppName(app.getAppName());
                        localApp.setAppClassName(app.getAppClassName());
                        localApp.setUninstalled(app.isUninstalled());
                        localApp.setUpdateSystem(app.isUpdateSystem());
                        break;
                    }
                case 5:
                    if (this.mInfoList != null && index >= 0 && index < this.mInfoList.size()) {
                        ((AppInfo) this.mInfoList.get(index)).setUpdateSystem(app.isUpdateSystem());
                        break;
                    }
            }
            setCountView();
            this.mGridView.setTotalSize(this.mInfoList.size());
            if (LogUtils.mIsDebug) {
                Log.d(LOG_TAG, "update() -> mInfoList.size=" + ListUtils.getCount(this.mInfoList) + ",mSelectedPosition=" + this.mSelectedPosition);
            }
            if (type == 1) {
                this.mAdapter.setDataList(this.mInfoList);
                return;
            } else {
                setAdapterData();
                return;
            }
        }
        Log.e(LOG_TAG, "update()--data isn't instanceof AppOperation");
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        switch (event.getKeyCode()) {
            case 4:
                dealBackKey();
                return true;
            case 82:
                dealMenuKey();
                if (this.mFocusedView == null || !this.mAppMenu.isShown()) {
                    ((AppView) this.mFocusedView).rBDrawableHint();
                } else {
                    ((AppView) this.mFocusedView).rBDrawableShow();
                }
                return true;
            default:
                return super.handleKeyEvent(event);
        }
    }

    private void dealBackKey() {
        if (isAppMenuShown()) {
            hideAppMenu();
        } else {
            finish();
        }
    }

    private void dealMenuKey() {
        if (isAppMenuShown()) {
            hideAppMenu();
        } else {
            showAppMenu((AppInfo) this.mInfoList.get(this.mSelectedPosition), this.mSelectedPosition);
        }
    }

    private void showAppMenu(AppInfo appInfo, int index) {
        AnimationUtil.translateAnimationY(this.mAppMenu, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 0.0f, 200, new AccelerateDecelerateInterpolator());
        this.mAppMenu.setVisibility(0);
        this.mAppMenu.requestFocus();
        if (this.mSelectedPosition < this.mTopCount) {
            this.mBringToTopItem.setBackgroundResource(R.drawable.epg_applist_unuse_btn_selector);
        } else {
            this.mBringToTopItem.setBackgroundResource(R.drawable.epg_applist_focus_btn_selector);
        }
        if (appInfo.isUninstalled()) {
            this.mBringUninstallItem.setVisibility(8);
            this.mBringInstallItem.setVisibility(0);
            this.mBringToTopItem.requestFocus();
            return;
        }
        this.mIsSystemApp = this.mAppManager.isSystemApp(index);
        this.mIsUpdateSystem = this.mAppManager.isUpdateSystemApp(index);
        this.mAppName = appInfo.getAppName();
        this.mBringUninstallItem.setVisibility(0);
        this.mBringInstallItem.setVisibility(8);
        this.mBringToTopItem.requestFocus();
        if (this.mIsUpdateSystem) {
            this.mBringUninstallItem.setBackgroundResource(R.drawable.epg_applist_unuse_btn_selector);
        } else {
            this.mBringUninstallItem.setBackgroundResource(this.mIsSystemApp ? R.drawable.epg_applist_unuse_btn_selector : R.drawable.epg_applist_focus_btn_selector);
        }
    }

    private boolean isAppMenuShown() {
        return this.mAppMenu.getVisibility() == 0;
    }

    private void hideAppMenu() {
        ((AppView) this.mFocusedView).rBDrawableHint();
        AnimationUtil.translateAnimationY(this.mAppMenu, 0.0f, ThreeDimensionalParams.TEXT_SCALE_FOR_3D, 150, new AccelerateInterpolator());
        this.mAppMenu.setVisibility(8);
    }

    private void startApp(int index) {
        Intent intent = new Intent(this, AppLauncherActivity.class);
        intent.putExtra("third_app_position", index);
        intent.putExtra("start_app_form", "start_app_use_position");
        PageIOUtils.activityIn((Activity) this, intent);
    }

    public void bringToTop(int index) {
        this.mAppManager.stickApp(index);
    }

    public void uninstall(int index) {
        this.mAppManager.uninstallApp(index);
    }

    public void install(int index) {
        this.mAppManager.installApp(index);
    }

    public void onLoadDone(List<AppInfo> appInfos) {
        LogUtils.d(LOG_TAG, "--onLoadDone() -> ");
        if (ListUtils.isEmpty((List) appInfos)) {
            LogUtils.e(LOG_TAG, "onLoadDone() -> app list is empty !");
            return;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(LOG_TAG, "onLoadDone() -> mAppInfos.size()  =" + appInfos.size());
        }
        this.mInfoList = new ArrayList(5);
        for (int i = 0; i < 5; i++) {
            this.mInfoList.addAll(appInfos);
        }
        this.mGridView.setTotalSize(this.mInfoList.size());
        this.mAdapter.setDataList(this.mInfoList);
    }

    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.epg_top_btn) {
            if (this.mSelectedPosition == -1) {
                return;
            }
            if (this.mSelectedPosition > this.mTopCount) {
                bringToTop(this.mSelectedPosition);
                hideAppMenu();
            } else if (this.mSelectedPosition == this.mTopCount) {
                hideAppMenu();
            }
        } else if (i == R.id.epg_uninstall_btn) {
            if (this.mSelectedPosition == -1) {
                return;
            }
            if (!this.mIsSystemApp || this.mIsUpdateSystem) {
                uninstall(this.mSelectedPosition);
                hideAppMenu();
            }
        } else if (i == R.id.epg_install_btn && this.mSelectedPosition != -1) {
            install(this.mSelectedPosition);
            hideAppMenu();
        }
    }

    public void onFocusChange(View view, boolean hasFocus) {
        int i = view.getId();
        if (i == R.id.epg_top_btn) {
            if (hasFocus) {
                this.mBringToTopItem.setTextColor(ResourceUtil.getColor(R.color.card_title_color));
                if (!Project.getInstance().getBuild().isHomeVersion() || this.mTopCount <= 0 || this.mSelectedPosition >= this.mTopCount) {
                    this.mAppNotice.setText(getResources().getString(R.string.app_dialog_text));
                } else {
                    this.mAppNotice.setText(getResources().getString(R.string.app_lock_tip, new Object[]{this.mTopCount + ""}));
                }
                view.bringToFront();
                ((RelativeLayout) view.getParent()).invalidate();
            } else {
                this.mAppNotice.setText("");
                this.mBringToTopItem.setTextColor(ResourceUtil.getColor(R.color.card_grey_color));
            }
        } else if (i == R.id.epg_uninstall_btn) {
            if (hasFocus) {
                this.mBringUninstallItem.setTextColor(ResourceUtil.getColor(R.color.card_title_color));
                view.bringToFront();
                ((RelativeLayout) view.getParent()).invalidate();
                if (this.mIsSystemApp) {
                    String str;
                    String head = getResources().getString(R.string.app_menu_recovery_tip_head);
                    String tail = getResources().getString(R.string.app_menu_recovery_tip_tail);
                    if (this.mIsUpdateSystem) {
                        str = "<font color= '#bebebe'>" + head + "</font>" + "<font color='#ffb400'>" + this.mAppName + "</font>" + "<font color= '#bebebe'>" + tail + "</font>";
                    } else {
                        str = "<font color='#ffb400'>" + this.mAppName + "</font>" + "<font color= '#bebebe'>" + getResources().getString(R.string.app_menu_system_tip) + "</font>";
                    }
                    this.mAppNotice.setText(Html.fromHtml(str));
                } else {
                    this.mAppNotice.setText(Html.fromHtml("<font color= '#bebebe'>" + getResources().getString(R.string.app_menu_uninstall_tip) + this.mAppName + "</font>"));
                }
            } else {
                this.mAppNotice.setText("");
                this.mBringUninstallItem.setTextColor(ResourceUtil.getColor(R.color.card_grey_color));
            }
        } else if (i == R.id.epg_install_btn && hasFocus) {
            view.bringToFront();
            ((RelativeLayout) view.getParent()).invalidate();
        }
        AnimationUtil.zoomAnimation(view, hasFocus, 1.1f, 200);
    }

    public void onActionFlingEvent(KeyKind kind) {
    }

    public List<AbsVoiceAction> getSupportedVoices() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(LOG_TAG, "AppListActivity/List<AbsVoiceAction> getSupportedVoices()");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        for (AppInfo info : this.mInfoList) {
            actions.add(CreateInterfaceTools.createVoiceCommon().createAbsVoiceAction(info.getAppName(), new AppRunnable(info.getAppPackageName()), KeyWordType.FUZZY));
        }
        return actions;
    }

    protected void onItemClicked(int position) {
        if (position >= 0 && position < this.mInfoList.size()) {
            AppInfo app = (AppInfo) this.mInfoList.get(position);
            if (app == null) {
                return;
            }
            if (app.isUninstalled()) {
                this.mAppManager.installApp(position);
            } else {
                startApp(position);
            }
        }
    }

    protected void setDescViewProperty(View desView) {
        desView.setVisibility(0);
    }
}
