package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.home.data.tool.TabModelManager;
import com.gala.video.app.epg.home.data.tool.TabModelManager.Editor;
import com.gala.video.app.epg.screensaver.constants.ScreenSaverConstants.ScreenSaverPingBack;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class TabManagerActivity extends QMultiScreenActivity {
    private static final String TAG = "tabmanager/TabManagerActivity";
    private OnClickListener mExitCancelBtnClicker = new OnClickListener() {
        public void onClick(View v) {
            String from = TabManagerActivity.this.getIntent().getStringExtra("from");
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_EXIT_DIALOG_CLICK_PINGBACK).addItem("rpage", "desk_manage").addItem("block", "exit").addItem("rseat", "no").addItem("rt", "i").addItem("e", HomePingbackSender.getInstance().getTabManagerE()).addItem("count", TabManagerActivity.this.mTabSortedLayout.getOldChildSortedResult());
            String str = "s1";
            if (StringUtils.isEmpty((CharSequence) from)) {
                from = ISearchConstant.TVSRCHSOURCE_OTHER;
            }
            addItem.addItem(str, from).addItem("td", (SystemClock.elapsedRealtime() - TabManagerActivity.this.mStartTime) + "").setOthersNull().post();
            TabManagerActivity.this.finish();
            TabManagerActivity.this.mGlobalExitDialog.dismiss();
            TabManagerActivity.this.mGlobalExitDialog = null;
        }
    };
    private OnClickListener mExitOkBtnClicker = new OnClickListener() {
        public void onClick(View v) {
            TabManagerActivity.this.mTabModelManagerEditor.commit();
            String from = TabManagerActivity.this.getIntent().getStringExtra("from");
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_EXIT_DIALOG_CLICK_PINGBACK).addItem("rpage", "desk_manage").addItem("block", "exit").addItem("rseat", ScreenSaverPingBack.SEAT_KEY_OK).addItem("rt", "i").addItem("e", HomePingbackSender.getInstance().getTabManagerE()).addItem("count", TabManagerActivity.this.mTabSortedLayout.getChildSortedResult());
            String str = "s1";
            if (StringUtils.isEmpty((CharSequence) from)) {
                from = ISearchConstant.TVSRCHSOURCE_OTHER;
            }
            addItem.addItem(str, from).addItem("td", (SystemClock.elapsedRealtime() - TabManagerActivity.this.mStartTime) + "").setOthersNull().post();
            TabManagerActivity.this.sendTabAddedResultActionPingback();
            TabManagerActivity.this.mGlobalExitDialog.dismiss();
            TabManagerActivity.this.finish();
            TabManagerActivity.this.mGlobalExitDialog = null;
            TabProvider.getInstance().updateTabSetting(500);
        }
    };
    private OnKeyListener mExitOnKeyListener = new OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == 4 && event.getAction() == 0) {
                String from = TabManagerActivity.this.getIntent().getStringExtra("from");
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_EXIT_DIALOG_CLICK_PINGBACK).addItem("rpage", "desk_manage").addItem("block", "exit").addItem("rt", "i").addItem("e", HomePingbackSender.getInstance().getTabManagerE());
                String str = "s1";
                if (StringUtils.isEmpty((CharSequence) from)) {
                    from = ISearchConstant.TVSRCHSOURCE_OTHER;
                }
                addItem.addItem(str, from).addItem("rseat", "back").setOthersNull().post();
            }
            return false;
        }
    };
    private GlobalDialog mGlobalExitDialog;
    private GlobalDialog mGlobalNotSupportSortDialog;
    private boolean mIsTabHasActivated = false;
    private boolean mIsTabHasAddedOrRemoved = false;
    private boolean mIsTabHasMoved = false;
    private OnKeyListener mNotSupportDialogOnKeyListener = new OnKeyListener() {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (!(keyCode == 23 || keyCode == 66 || keyCode != 4)) {
                TabManagerActivity.this.finish();
                TabManagerActivity.this.mGlobalNotSupportSortDialog.dismiss();
                TabManagerActivity.this.mGlobalNotSupportSortDialog = null;
            }
            return false;
        }
    };
    private OnClickListener mNotSupportOkBtnClicker = new OnClickListener() {
        public void onClick(View v) {
            TabManagerActivity.this.finish();
            TabManagerActivity.this.mGlobalNotSupportSortDialog.dismiss();
            TabManagerActivity.this.mGlobalNotSupportSortDialog = null;
        }
    };
    private List<TabModel> mOriginalTabList = new ArrayList();
    private long mStartTime;
    private List<TabModel> mTabBufferPool = new CopyOnWriteArrayList();
    private Editor mTabModelManagerEditor;
    private TabSortedActivatedListener mTabSortedActivatedListener = new TabSortedActivatedListener() {
        long mStartActivationTime = 0;

        public void startActivation(TabSortedItemView tabSortedItemView) {
            this.mStartActivationTime = SystemClock.elapsedRealtime();
            TabManagerActivity.this.setActivatedPromoteText();
            TabManagerActivity.this.mIsTabHasActivated = true;
            TabManagerActivity.this.mIsTabHasMoved = false;
            TabManagerActivity.this.sendTabActivatedClickPingback(tabSortedItemView);
        }

        public void cancelActivation(TabSortedItemView tabSortedItemView) {
            TabManagerActivity.this.setNonActivatedPromoteText();
            String from = TabManagerActivity.this.getIntent().getStringExtra("from");
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_CANCEL_ACTIVATED_CLICK_PINGBACK).addItem("rpage", "desk_manage").addItem("block", "move").addItem("rseat", TabManagerActivity.this.mIsTabHasMoved ? "deactmove" : "deactstay").addItem("e", HomePingbackSender.getInstance().getTabManagerE()).addItem("r", "tab_" + tabSortedItemView.getData().getTitle()).addItem("td", (SystemClock.elapsedRealtime() - this.mStartActivationTime) + "");
            String str = "s1";
            if (StringUtils.isEmpty((CharSequence) from)) {
                from = ISearchConstant.TVSRCHSOURCE_OTHER;
            }
            addItem.addItem(str, from).setOthersNull().post();
        }
    };
    private TabSortedAdapter mTabSortedAdapter;
    private TabSortedLayout mTabSortedLayout;
    private TabSortedMovingListener mTabSortedMovingListener = new TabSortedMovingListener() {
        public void moveForward(TabModel tabModel) {
            TabManagerActivity.this.mTabModelManagerEditor.moveForward(tabModel);
            TabManagerActivity.this.mIsTabHasMoved = true;
        }

        public void moveBackward(TabModel tabModel) {
            TabManagerActivity.this.mTabModelManagerEditor.moveBackward(tabModel);
            TabManagerActivity.this.mIsTabHasMoved = true;
        }
    };
    private TextView mTabSortedPromoteTextView;
    private TabVisibilityAdapter mTabVisibilityAdapter;
    private TabVisibilityItemOnKeyListener mTabVisibilityItemOnKeyListener = new TabVisibilityItemOnKeyListener() {
        long mLastSpringBackAnimationTime = 0;

        public void onDpadUpKeyEvent(TabVisibilityItemView itemView, KeyEvent event) {
            if (TabManagerActivity.this.mTabSortedLayout.getAdapter().getMinSortableTabIndex() == -2) {
                long duration = AnimationUtils.currentAnimationTimeMillis() - this.mLastSpringBackAnimationTime;
                if (itemView.isAtFirstLine() && duration > 500) {
                    itemView.startAnimation(AnimationUtils.loadAnimation(TabManagerActivity.this, R.anim.epg_shake_y));
                    this.mLastSpringBackAnimationTime = AnimationUtils.currentAnimationTimeMillis();
                }
            }
        }
    };
    private TabVisibilityLayout mTabVisibilityLayout;
    private TabVisibilityListener mTabVisibilityListener = new TabVisibilityListener() {
        public boolean addTab(TabModel tabModel, TabVisibilityItemView itemView) {
            if (TabManagerActivity.this.mTabModelManagerEditor.isFull()) {
                QToast.makeText(TabManagerActivity.this, R.string.album_tip_above, 5000).show();
                LogUtils.w(TabManagerActivity.TAG, "the current tabs are full, must not add some tab");
                return false;
            } else if (TabManagerActivity.this.mTabModelManagerEditor.show(tabModel)) {
                itemView.updateState();
                TabManagerActivity.this.mTabSortedLayout.getAdapter().getTabInfoList().add(tabModel);
                TabManagerActivity.this.mTabSortedLayout.addItemView(tabModel);
                TabManagerActivity.this.mIsTabHasAddedOrRemoved = true;
                TabManagerActivity.this.addTabToBuffer(tabModel);
                String from = TabManagerActivity.this.getIntent().getStringExtra("from");
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_ADD_REMOVE_CLICK_PINGBACK).addItem("rpage", "desk_manage").addItem("block", "addremove").addItem("rseat", "add").addItem("r", "tab_" + tabModel.getTitle()).addItem("e", HomePingbackSender.getInstance().getTabManagerE());
                String str = "s1";
                if (StringUtils.isEmpty((CharSequence) from)) {
                    from = ISearchConstant.TVSRCHSOURCE_OTHER;
                }
                addItem.addItem(str, from).setOthersNull().post();
                return true;
            } else {
                LogUtils.w(TabManagerActivity.TAG, "add tab failed, tab data:" + tabModel);
                return false;
            }
        }

        public boolean removeTab(TabModel tabModel, TabVisibilityItemView itemView) {
            if (TabManagerActivity.this.mTabSortedLayout.getAdapter().getCount() == 1) {
                QToast.makeText(TabManagerActivity.this, R.string.tab_manage_min, 3000).show();
                return false;
            } else if (TabManagerActivity.this.mTabModelManagerEditor.hide(tabModel)) {
                itemView.updateState();
                TabManagerActivity.this.mTabSortedLayout.getAdapter().remove(tabModel);
                TabManagerActivity.this.mTabSortedLayout.removeItemView(tabModel);
                TabManagerActivity.this.mIsTabHasAddedOrRemoved = true;
                TabManagerActivity.this.removeTabFromBuffer(tabModel);
                String from = TabManagerActivity.this.getIntent().getStringExtra("from");
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_ADD_REMOVE_CLICK_PINGBACK).addItem("rpage", "desk_manage").addItem("block", "addremove").addItem("rseat", "remove").addItem("r", "tab_" + tabModel.getTitle()).addItem("e", HomePingbackSender.getInstance().getTabManagerE());
                String str = "s1";
                if (StringUtils.isEmpty((CharSequence) from)) {
                    from = ISearchConstant.TVSRCHSOURCE_OTHER;
                }
                addItem.addItem(str, from).setOthersNull().post();
                return true;
            } else {
                LogUtils.w(TabManagerActivity.TAG, "add tab failed, tab data:" + tabModel);
                return false;
            }
        }
    };
    private TextView mTabVisibilityPromoteTxt;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_activity_tab_manager);
        initView();
        setData();
        sendPageShowPingback();
        getWindow().setFormat(-2);
    }

    protected void onStart() {
        super.onStart();
        this.mStartTime = SystemClock.elapsedRealtime();
        if (this.mTabSortedAdapter != null && this.mTabSortedAdapter.getSortableTabCount() == 0 && ListUtils.isEmpty(TabProvider.getInstance().getTabHideInfo())) {
            showNotSupportSortDialog();
        }
    }

    private void initView() {
        this.mTabSortedLayout = (TabSortedLayout) findViewById(R.id.epg_tab_manager_layout_tab_moving);
        this.mTabSortedPromoteTextView = (TextView) findViewById(R.id.epg_tab_manager_txt_promote);
        this.mTabVisibilityLayout = (TabVisibilityLayout) findViewById(R.id.epg_tab_manager_layout_tab_visibility);
        this.mTabVisibilityPromoteTxt = (TextView) findViewById(R.id.epg_tab_manager_txt_visibility_promote);
        setNonActivatedPromoteText();
        setTabVisibilityPromoteText();
    }

    private void setData() {
        List<TabModel> tabModelList = TabProvider.getInstance().getTabInfo();
        this.mOriginalTabList = tabModelList;
        List<TabModel> tabVisibilityList = TabProvider.getInstance().getTabHideInfo();
        List tabModelList2 = TabModelManager.cloneTabModel(tabModelList);
        List tabVisibilityList2 = TabModelManager.cloneTabModel(tabVisibilityList);
        this.mTabModelManagerEditor = TabModelManager.edit(tabModelList2, tabVisibilityList2);
        if (ListUtils.isEmpty(tabModelList2)) {
            LogUtils.w(TAG, "The result of reading tab info from local cache: tab info data is empty");
        } else {
            this.mTabSortedAdapter = new TabSortedAdapter(this, tabModelList2);
            this.mTabSortedLayout.setAdapter(this.mTabSortedAdapter);
            this.mTabSortedLayout.setMovingListener(this.mTabSortedMovingListener);
            this.mTabSortedLayout.setActivatedListener(this.mTabSortedActivatedListener);
        }
        if (ListUtils.isEmpty(tabVisibilityList2)) {
            LogUtils.w(TAG, "The result of reading tab info from local cache: tab visibility tab info  is empty");
            return;
        }
        this.mTabVisibilityAdapter = new TabVisibilityAdapter(this, tabVisibilityList2);
        this.mTabVisibilityLayout.setAdapter(this.mTabVisibilityAdapter);
        this.mTabVisibilityLayout.setTabVisibilityListener(this.mTabVisibilityListener);
        this.mTabVisibilityLayout.setTabVisibilityItemOnKeyListener(this.mTabVisibilityItemOnKeyListener);
    }

    private void sendPageShowPingback() {
        String from = getIntent().getStringExtra("from");
        HomePingbackSender.getInstance().setTabManagerE(PingBackUtils.createEventId());
        HomePingbackFactory.instance().createPingback(ShowPingback.TAB_MANAGER_PAGE_SHOW_PINGBACK).addItem("qtcurl", "desk_manage").addItem("block", "desk_manage").addItem("bstp", "1").addItem("e", HomePingbackSender.getInstance().getTabManagerE()).post();
    }

    private void sendTabActivatedClickPingback(TabSortedItemView tabSortedItemView) {
        String from = getIntent().getStringExtra("from");
        IHomePingback addItem = HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_ACTIVATED_CLICK_PINGBACK).addItem("rpage", "desk_manage").addItem("block", "move").addItem("rseat", LoginConstant.LGTTYPE_ACTIVE).addItem("r", "tab_" + tabSortedItemView.getData().getTitle()).addItem("e", HomePingbackSender.getInstance().getTabManagerE());
        String str = "s1";
        if (StringUtils.isEmpty((CharSequence) from)) {
            from = ISearchConstant.TVSRCHSOURCE_OTHER;
        }
        addItem.addItem(str, from).addItem("rt", "i").setOthersNull().post();
    }

    private void sendExitDialogShowPingback() {
        HomePingbackFactory.instance().createPingback(ShowPingback.TAB_MANAGER_EXIT_DIALOG_SHOW_PINGBACK).addItem("qtcurl", "desk_manage").addItem("block", "exit").addItem("bstp", "1").post();
    }

    private void showExitDialog() {
        this.mGlobalExitDialog = new GlobalDialog(this);
        this.mGlobalExitDialog.setParams((CharSequence) "是否保存对桌面的调整?", "保存", this.mExitOkBtnClicker, "不保存", this.mExitCancelBtnClicker, false);
        this.mGlobalExitDialog.show();
        this.mGlobalExitDialog.setOnKeyListener(this.mExitOnKeyListener);
        sendExitDialogShowPingback();
    }

    private void showNotSupportSortDialog() {
        this.mGlobalNotSupportSortDialog = new GlobalDialog(this);
        this.mGlobalNotSupportSortDialog.setParams("暂不支持对当前桌面进行管理", "知道了", this.mNotSupportOkBtnClicker);
        this.mGlobalNotSupportSortDialog.setOnKeyListener(this.mNotSupportDialogOnKeyListener);
        this.mGlobalNotSupportSortDialog.show();
    }

    public void onBackPressed() {
        if (this.mIsTabHasActivated || this.mIsTabHasAddedOrRemoved) {
            showExitDialog();
        } else {
            finish();
        }
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_tab_manager_container);
    }

    private void setNonActivatedPromoteText() {
        SpannableStringBuilder builder = new SpannableStringBuilder("按OK键调整桌面位置");
        ForegroundColorSpan white_0 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow));
        ForegroundColorSpan yellowSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.yellow));
        ForegroundColorSpan white_2 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        ForegroundColorSpan white_3 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        builder.setSpan(white_0, 0, 1, 33);
        builder.setSpan(yellowSpan, 1, 4, 33);
        builder.setSpan(white_2, 4, 10, 33);
        if (this.mTabSortedPromoteTextView != null) {
            this.mTabSortedPromoteTextView.setText(builder);
        }
    }

    private void setActivatedPromoteText() {
        SpannableStringBuilder builder = new SpannableStringBuilder("按左右键移动桌面，按OK键确定桌面位置");
        ForegroundColorSpan white_0 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        ForegroundColorSpan white_1 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        ForegroundColorSpan white_2 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow));
        ForegroundColorSpan yellowSpan2 = new ForegroundColorSpan(getResources().getColor(R.color.yellow));
        builder.setSpan(white_0, 0, 1, 33);
        builder.setSpan(yellowSpan, 1, 4, 33);
        builder.setSpan(white_1, 4, 10, 33);
        builder.setSpan(yellowSpan2, 10, 13, 33);
        builder.setSpan(white_2, 13, 19, 33);
        if (this.mTabSortedPromoteTextView != null) {
            this.mTabSortedPromoteTextView.setText(builder);
        }
    }

    private void setTabVisibilityPromoteText() {
        SpannableStringBuilder builder = new SpannableStringBuilder("按OK键添加/隐藏桌面");
        ForegroundColorSpan white_0 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        ForegroundColorSpan yellowSpan = new ForegroundColorSpan(getResources().getColor(R.color.yellow));
        ForegroundColorSpan white_2 = new ForegroundColorSpan(getResources().getColor(R.color.albumview_normal_color));
        builder.setSpan(white_0, 0, 1, 33);
        builder.setSpan(yellowSpan, 1, 4, 33);
        builder.setSpan(white_2, 4, 11, 33);
        if (this.mTabVisibilityPromoteTxt != null) {
            this.mTabVisibilityPromoteTxt.setText(builder);
        }
    }

    private void addTabToBuffer(TabModel tabModel) {
        for (TabModel model : this.mOriginalTabList) {
            if (model.getId() == tabModel.getId()) {
                return;
            }
        }
        this.mTabBufferPool.add(tabModel);
    }

    private void removeTabFromBuffer(TabModel tabModel) {
        for (TabModel model : this.mTabBufferPool) {
            if (model.getId() == tabModel.getId()) {
                this.mTabBufferPool.remove(model);
            }
        }
    }

    private void sendTabAddedResultActionPingback() {
        for (TabModel tabModel : this.mTabBufferPool) {
            HomePingbackFactory.instance().createPingback(ClickPingback.TAB_MANAGER_ADDED_RESULT_ADD_ACTION_PINGBACK).addItem("rpage", "桌面管理").addItem("st", "1").addItem("tab", "tab_" + tabModel.getTitle()).addItem("a", "addtab").setOthersNull().post();
        }
    }
}
