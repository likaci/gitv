package com.gala.video.app.epg.ui.setting.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;
import com.gala.video.albumlist4.widget.LayoutManager.FocusPlace;
import com.gala.video.albumlist4.widget.LayoutManager.Orientation;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider;
import com.gala.video.app.epg.ui.setting.CustomSettingProvider.SettingType;
import com.gala.video.app.epg.ui.setting.ISettingConstant;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.gala.video.app.epg.ui.setting.adapter.SettingGridAdapter;
import com.gala.video.app.epg.ui.setting.model.SettingItem;
import com.gala.video.app.epg.ui.setting.model.SettingModel;
import com.gala.video.app.epg.ui.setting.update.ISettingUpdate;
import com.gala.video.app.epg.ui.setting.utils.SettingUtils;
import com.gala.video.cloudui.CloudUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.DebugHelper;
import com.gala.video.lib.framework.core.utils.DebugHelper.OnDebugTriggerListener;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.setting.ISetting;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.setting.SettingSharepreference;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressLint({"ValidFragment"})
public class SettingMainFragment extends SettingBaseFragment {
    private static final int COUNT_TIMES = 5;
    private static final int FIRST_DEFAULT_INDEX = 0;
    private static int tempCount = 1;
    private static long tempTimes = 0;
    private final String LOG_TAG = "EPG/setting/SettingMainFragment";
    private int mCurrentFocusPos = 0;
    private DebugHelper mDebugHelper;
    private SettingGridAdapter mGridAdapter;
    private List<SettingItem> mItems;
    private String mJsonPath;
    private View mMainView;
    private boolean mPingbackable = true;
    private SettingItem mPreSettingItem;
    private int mSelectedPos = -1;
    private SettingItem mSelfSettingItem;
    private VerticalGridView mSettingGridView;
    private SettingModel mSettingModel;
    private ISettingUpdate mSettingUpdateState;
    private TextView mTitleDesc;
    private ImageView mTitleIcon;
    private View mTitleLine;
    private TextView mTitleName;
    private boolean orderBefore = false;

    class C10631 implements OnItemFocusChangedListener {
        C10631() {
        }

        public void onItemFocusChanged(ViewGroup parent, final ViewHolder holder, boolean hasFocus) {
            SettingMainFragment.this.mCurrentFocusPos = holder.getLayoutPosition();
            if (SettingMainFragment.this.mGridAdapter != null) {
                SettingMainFragment.this.mGridAdapter.changeTextColor(holder.itemView, hasFocus, holder.getLayoutPosition());
            }
            if (hasFocus) {
                holder.itemView.post(new Runnable() {
                    public void run() {
                        holder.itemView.bringToFront();
                    }
                });
                SettingMainFragment.this.mSelectedPos = holder.getLayoutPosition();
                AnimationUtil.scaleAnimation(holder.itemView, 1.0f, 1.03f, 200);
                return;
            }
            SettingMainFragment.this.mPingbackable = true;
            AnimationUtil.scaleAnimation(holder.itemView, 1.0f, 1.0f, 200);
        }
    }

    class C10642 implements OnItemClickListener {
        C10642() {
        }

        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            LogUtils.m1576i("EPG/setting/SettingMainFragment", "onItemClick pos =", Integer.valueOf(holder.getLayoutPosition()));
            SettingMainFragment.this.itemClickEvent((SettingItem) SettingMainFragment.this.mItems.get(holder.getLayoutPosition()), holder.getLayoutPosition());
        }
    }

    class C10653 implements OnDebugTriggerListener {
        C10653() {
        }

        public void onDebugTrigger() {
            PageIOUtils.activityIn(SettingMainFragment.this.mContext, new Intent(ActionSet.ACT_DEBUG_OPTIONS));
        }
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (this.mSettingEvent != null) {
            LogUtils.m1574i("EPG/setting/SettingMainFragment", "onAttach --- mSettingEvent.onAttachActivity(this)");
            this.mSettingEvent.onAttachActivity(this);
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mMainView = inflater.inflate(C0508R.layout.epg_fragment_setting_main, null);
        initData();
        initUI();
        initGridView();
        sendDisplayPingback();
        return this.mMainView;
    }

    private void sendDisplayPingback() {
        if (this.mSettingModel != null) {
            CharSequence qtcurl = this.mSettingModel.getPingbackQtcurl();
            CharSequence block = this.mSettingModel.getPingbackBlock();
            if (!StringUtils.isEmpty(qtcurl) || !StringUtils.isEmpty(block)) {
                LogUtils.m1570d("EPG/setting/SettingMainFragment", ">>>>>pingback pageshow >>> qtcurl: ", qtcurl, " ,block:", block);
                PingBackParams params1 = new PingBackParams();
                params1.add(Keys.f2035T, "21").add("bstp", "1").add("qtcurl", qtcurl).add("block", block);
                PingBack.getInstance().postPingBackToLongYuan(params1.build());
            }
        }
    }

    private void initGridView() {
        this.mSettingGridView.setOrientation(Orientation.VERTICAL);
        this.mSettingGridView.setNumRows(1);
        this.mSettingGridView.setContentWidth(getDimen(C0508R.dimen.dimen_1034dp));
        this.mSettingGridView.setContentHeight(getDimen(C0508R.dimen.dimen_106dp));
        this.mSettingGridView.setVerticalMargin(getDimen(C0508R.dimen.dimen_028dp));
        this.mSettingGridView.setFocusPlace(FocusPlace.FOCUS_CENTER);
        if (((SettingItem) this.mItems.get(0)).isItemFocusable()) {
            this.mSettingGridView.setFocusPosition(0);
            this.mCurrentFocusPos = 0;
        } else {
            this.mCurrentFocusPos = 1;
            this.mSettingGridView.setFocusPosition(1);
        }
        this.mSettingGridView.setFocusMode(0);
        if (this.mSettingModel != null && !ListUtils.isEmpty(this.mItems)) {
            this.mGridAdapter = new SettingGridAdapter(this.mContext, this.mItems);
            this.mSettingGridView.setAdapter(this.mGridAdapter);
            this.mSettingGridView.setOnItemFocusChangedListener(new C10631());
            this.mSettingGridView.setOnItemClickListener(new C10642());
        }
    }

    public void onResume() {
        super.onResume();
        LogUtils.m1574i("EPG/setting/SettingMainFragment", "TEST mCurFragment.updateItem onResume()");
        this.mSettingGridView.requestFocus();
        updateItem(this.mSelfSettingItem);
    }

    private void initUI() {
        this.mTitleIcon = (ImageView) this.mMainView.findViewById(C0508R.id.epg_setting_title_icon);
        this.mTitleLine = this.mMainView.findViewById(C0508R.id.epg_setting_title_line);
        this.mTitleName = (TextView) this.mMainView.findViewById(C0508R.id.epg_setting_title_name);
        this.mTitleDesc = (TextView) this.mMainView.findViewById(C0508R.id.epg_setting_title_desc);
        this.mSettingGridView = (VerticalGridView) this.mMainView.findViewById(C0508R.id.epg_setting_item_gridview);
        if (!(this.mBundle == null || this.mSettingModel == null || ListUtils.isEmpty(this.mItems) || ((SettingItem) this.mItems.get(0)).isItemFocusable() || this.mBundle.getInt(ISettingConstant.SETTING_FLAG_KEY) != 0)) {
            LayoutParams lp = new LayoutParams(this.mSettingGridView.getLayoutParams());
            lp.setMargins(getDimen(C0508R.dimen.dimen_98dp), getDimen(C0508R.dimen.dimen_58dp), 0, 0);
            this.mSettingGridView.setLayoutParams(lp);
        }
        if (this.mSettingModel != null) {
            this.mTitleName.setText(this.mSettingModel.getTitleName());
            this.mTitleDesc.setText(this.mSettingModel.getTitleDes());
            if (StringUtils.isEmpty(this.mSettingModel.getTitleIcon())) {
                this.mTitleIcon.setVisibility(8);
                this.mTitleLine.setVisibility(8);
                return;
            }
            this.mTitleIcon.setImageResource(this.mContext.getResources().getIdentifier(this.mSettingModel.getTitleIcon(), "drawable", AppClientUtils.getResourcePkgName()));
        }
    }

    private void initData() {
        this.mBundle = getArguments();
        if (this.mBundle != null) {
            this.mJsonPath = this.mBundle.getString(ISettingConstant.KEY_JSON_PATH);
            if (StringUtils.isEmpty(this.mJsonPath)) {
                this.mSettingModel = initModelByFlag(this.mBundle.getInt(ISettingConstant.SETTING_FLAG_KEY));
            } else {
                this.mPreSettingItem = (SettingItem) this.mBundle.getSerializable(ISettingConstant.KEY_SETTING_ITEM);
                this.mSettingModel = initModelByAction(this.mJsonPath);
            }
            initSettingUpdate();
            if (this.mSettingUpdateState != null) {
                this.mSettingModel = this.mSettingUpdateState.updateSettingModel(this.mSettingModel);
            }
            this.mItems = this.mSettingModel.getItems();
            if (this.mSettingModel.isDebugHelper) {
                initDebugHelper();
                return;
            }
            return;
        }
        LogUtils.m1571e("EPG/setting/SettingMainFragment", "initData exception bundle is null");
    }

    private void initDebugHelper() {
        this.mDebugHelper = new DebugHelper();
        this.mDebugHelper.setKeyOrderList(25, 24, 82);
        this.mDebugHelper.setTotalTime(1000);
        this.mDebugHelper.setOnDebugTriggerListener(new C10653());
    }

    private void initSettingUpdate() {
        CharSequence className = this.mSettingModel.getUpdateClass();
        if (!StringUtils.isEmpty(className)) {
            try {
                this.mSettingUpdateState = (ISettingUpdate) Class.forName(className).newInstance();
            } catch (Exception e) {
                LogUtils.m1572e("EPG/setting/SettingMainFragment", "Class.forName(className) Exception", e);
            }
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mDebugHelper != null) {
            this.mDebugHelper.monitorKeyEvent(event);
        }
        switch (event.getKeyCode()) {
            case 4:
                setBack(true);
                this.mSettingEvent.onPopStackFragment(null);
                return true;
            case 21:
                if (isFaultFeedbackSettingItem()) {
                    feedbackForPlay(event, this.mContext);
                    return true;
                }
                switchOptions(event);
                return true;
            case 22:
                if (this.mSettingModel == null || ListUtils.isEmpty(this.mSettingModel.getItems())) {
                    return true;
                }
                SettingItem item = (SettingItem) this.mSettingModel.getItems().get(this.mCurrentFocusPos);
                if (item == null || !ListUtils.isEmpty(item.getItemOptions()) || StringUtils.isEmpty(item.getItemAction())) {
                    switchOptions(event);
                    return true;
                }
                itemClickEvent(item, this.mCurrentFocusPos);
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private boolean isFaultFeedbackSettingItem() {
        if (ListUtils.isEmpty(this.mSettingModel.getItems())) {
            return false;
        }
        SettingItem item = (SettingItem) this.mSettingModel.getItems().get(this.mCurrentFocusPos);
        if (item == null || !ListUtils.isEmpty(item.getItemOptions()) || StringUtils.isEmpty(item.getItemAction()) || !item.getItemAction().equals(ActionSet.ACT_FEEDBACK_FAULT)) {
            return false;
        }
        return true;
    }

    private void feedbackForPlay(KeyEvent event, Context context) {
        if (event.getAction() == 0) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - tempTimes > 500 || tempTimes == 0) {
                tempCount = 1;
                LogUtils.m1568d("EPG/setting/SettingMainFragment", "tempCount = " + tempCount);
            } else {
                tempCount++;
                LogUtils.m1568d("EPG/setting/SettingMainFragment", "tempCount = " + tempCount);
                if (tempCount == 5) {
                    GetInterfaceTools.getPlayerPageProvider().startPlayerAdapterSettingPage(this.mContext);
                }
            }
            tempTimes = currentTime;
        }
    }

    private void switchOptions(KeyEvent event) {
        if (!ListUtils.isEmpty(this.mItems)) {
            View convertView = this.mSettingGridView.getViewByPosition(this.mCurrentFocusPos);
            if (convertView != null) {
                CharSequence lastState = this.mGridAdapter.switchOptions(convertView, this.mCurrentFocusPos, event);
                if (!StringUtils.isEmpty(lastState)) {
                    LogUtils.m1576i("EPG/setting/SettingMainFragment", "dispatchKeyEvent setItemLastState =", lastState);
                    SettingItem item = (SettingItem) this.mSettingModel.getItems().get(this.mCurrentFocusPos);
                    item.setItemLastState(lastState);
                    if (this.mPingbackable) {
                        LogUtils.m1576i("EPG/setting/SettingMainFragment", "sendClickPingback mPingbackable = ", Boolean.valueOf(this.mPingbackable));
                        sendClickPingback(item);
                        this.mPingbackable = false;
                    }
                    if (this.mSettingUpdateState != null) {
                        this.mSettingUpdateState.saveNewCacheByPos(item);
                    }
                }
            }
        }
    }

    private SettingModel initModelByFlag(int flag) {
        if (this.mContext != null) {
            LogUtils.m1574i("EPG/setting/SettingMainFragment", "initModelByFlag parse JSON ");
            String jsonRoot = Project.getInstance().getConfig().getSettingJsonRoot();
            String jsonPath = "";
            SettingType settingType = SettingType.UNKNOWN;
            switch (flag) {
                case 0:
                    jsonPath = Project.getInstance().getConfig().getPlaySettingJsonPath() + ISettingConstant.PATH_SETTING_PLAYSHOW;
                    settingType = SettingType.PLAY_DISPLAY;
                    break;
                case 1:
                    jsonPath = jsonRoot + ISettingConstant.PATH_SETTING_NETWORK;
                    settingType = SettingType.NETWORK;
                    break;
                case 2:
                    jsonPath = Project.getInstance().getConfig().getCommonSettingJsonRoot() + ISettingConstant.PATH_SETTING_COMMON;
                    settingType = SettingType.COMMON;
                    break;
                case 3:
                    jsonPath = jsonRoot + ISettingConstant.PATH_SETTING_ABOUT;
                    settingType = SettingType.ABOUT;
                    break;
                case 4:
                    jsonPath = jsonRoot + ISettingConstant.PATH_SETTING_FEEDBACK;
                    settingType = SettingType.FEED_BACK;
                    break;
            }
            List modelList = JSON.parseArray(CloudUtils.getStreamFromAssets(this.mContext, jsonPath), SettingModel.class);
            if (ListUtils.isEmpty(modelList)) {
                LogUtils.m1571e("EPG/setting/SettingMainFragment", "parse json failed mSettingModel is null");
            } else {
                LogUtils.m1576i("EPG/setting/SettingMainFragment", "parse json success list size is =", Integer.valueOf(modelList.size()));
                this.mSettingModel = (SettingModel) modelList.get(0);
                LogUtils.m1574i("EPG/setting/SettingMainFragment", "system out:  " + JSON.toJSONString(this.mSettingModel));
            }
            if (!(flag != 2 || !Project.getInstance().getBuild().isHomeVersion() || ListUtils.isEmpty(modelList) || modelList.get(0) == null || ListUtils.isEmpty(((SettingModel) modelList.get(0)).getItems()))) {
                for (SettingItem old : ((SettingModel) modelList.get(0)).getItems()) {
                    if (old.getId() == 513) {
                        if (Project.getInstance().getConfig().isSkyworthVersion()) {
                            old.setItemActionType("activity");
                            old.setItemAction("android.intent.action.BOX_NAME");
                            ISetting iSetting = Project.getInstance().getConfig().getSystemSetting();
                            CharSequence deviceName = iSetting.getCurrDeviceName();
                            SettingSharepreference.saveDeviceNameResult(this.mContext, deviceName);
                            List nameOptions = iSetting.getAllDeviceName();
                            if (!(ListUtils.isEmpty(nameOptions) || StringUtils.isEmpty(deviceName))) {
                                old.setItemOptions(nameOptions);
                                old.setItemLastState(deviceName);
                            }
                        } else {
                            String lastStateName = SettingSharepreference.getDeviceName(AppRuntimeEnv.get().getApplicationContext());
                            String suffixName = SettingSharepreference.getDeviceNameSuffix(AppRuntimeEnv.get().getApplicationContext());
                            String suffix = CustomSettingProvider.getInstance().getDevNameSuffix();
                            if (!(suffixName.equals(suffix) || StringUtils.isEmpty((CharSequence) lastStateName))) {
                                lastStateName = lastStateName.replace(suffixName, suffix);
                                SettingSharepreference.saveDeviceNameResult(AppRuntimeEnv.get().getApplicationContext(), lastStateName);
                            }
                            Context context = AppRuntimeEnv.get().getApplicationContext();
                            SettingSharepreference.saveDeviceNameSuffix(context, CustomSettingProvider.getInstance().getDevNameSuffix());
                            List<String> oldOption = old.getItemOptions();
                            List<String> newOption = new ArrayList();
                            for (String s : oldOption) {
                                newOption.add(s + SettingSharepreference.getDeviceNameSuffix(context));
                            }
                            old.setItemOptions(newOption);
                            old.setItemLastState(lastStateName);
                        }
                    }
                }
            }
            if (!(((!Project.getInstance().getBuild().isHomeVersion() || !Project.getInstance().getBuild().isSupportContentProvider()) && Project.getInstance().getBuild().isHomeVersion()) || Project.getInstance().getBuild().isIsSupportScreenSaver() || this.mSettingModel == null)) {
                List<SettingItem> sourceList = this.mSettingModel.getItems();
                int index = 0;
                while (index < sourceList.size()) {
                    if (((SettingItem) sourceList.get(index)).getId() == 515) {
                        sourceList.remove(index);
                    } else {
                        index++;
                    }
                }
            }
            if (Project.getInstance().getBuild().isHomeVersion() && this.mSettingModel != null) {
                if (settingType != SettingType.PLAY_DISPLAY) {
                    List settingItems = parseSettingModel(settingType);
                    if (!ListUtils.isEmpty(settingItems)) {
                        orderItems(this.mSettingModel, settingItems, settingType);
                    }
                } else {
                    List customPlayItems = parseSettingModel(SettingType.PLAY);
                    List customDisplayItems = parseSettingModel(SettingType.DISPLAY);
                    if (!ListUtils.isEmpty(customPlayItems)) {
                        orderItems(this.mSettingModel, customPlayItems, SettingType.PLAY);
                    }
                    if (!ListUtils.isEmpty(customDisplayItems)) {
                        orderItems(this.mSettingModel, customDisplayItems, SettingType.DISPLAY);
                    }
                }
            }
        }
        if (flag == 3) {
            if (Project.getInstance().getBuild().isHomeVersion() && CustomSettingProvider.getInstance().hasSignalSource()) {
                SettingItem customerItem = new SettingItem();
                customerItem.setItemName("升级");
                customerItem.setItemAction("dummy");
                customerItem.setItemActionType("none");
                List upgradeItems = parseSettingModel(SettingType.UPGRADE);
                if (!ListUtils.isEmpty(upgradeItems)) {
                    customerItem = (SettingItem) upgradeItems.get(0);
                    if ("action".equalsIgnoreCase(customerItem.getItemActionType())) {
                        customerItem.setItemActionType(SettingConstants.ACTION_TYPE_CUSTOM_APP_ACTION);
                    } else if (SettingConstants.ACTION_TYPE_PACKAGE_NAME.equalsIgnoreCase(customerItem.getItemActionType())) {
                        customerItem.setItemPackageName(customerItem.getItemAction());
                        customerItem.setItemAction("dummy");
                    }
                }
                customerItem.setItemFocusable(true);
                SettingItem it = (SettingItem) this.mSettingModel.getItems().get(0);
                if (it.isGroup()) {
                    customerItem.setItemBackground(SettingConstants.SETTING_ITEM_BG_CIRCLE);
                } else {
                    customerItem.setItemBackground(SettingConstants.SETTING_ITEM_BG_TOP);
                    it.setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                }
                this.mSettingModel.addHead(customerItem);
            }
            ensureItem();
        }
        return this.mSettingModel;
    }

    private void orderItems(SettingModel source, List<SettingItem> custom, SettingType settingType) {
        boolean before = this.orderBefore;
        List<SettingItem> sourceList = source.getItems();
        List<SettingItem> replaced = new LinkedList();
        for (SettingItem replace : custom) {
            if (replace.getId() > 0) {
                for (SettingItem old : sourceList) {
                    if (old.getId() == replace.getId()) {
                        replaced.add(replace);
                        if ("action".equalsIgnoreCase(replace.getItemActionType())) {
                            old.setItemActionType(SettingConstants.ACTION_TYPE_CUSTOM_APP_ACTION);
                            old.setItemAction(replace.getItemAction());
                        } else if (SettingConstants.ACTION_TYPE_PACKAGE_NAME.equalsIgnoreCase(replace.getItemActionType())) {
                            old.setItemActionType(replace.getItemActionType());
                            old.setItemPackageName(replace.getItemAction());
                            old.setItemAction("dummy");
                            old.setItemClassName(replace.getItemClassName());
                        }
                    }
                }
            }
        }
        if (replaced.size() > 0) {
            custom.removeAll(replaced);
        }
        SettingUtils.assertCustomItemCount(custom);
        int c = custom.size();
        boolean groupStart = true;
        for (int i = 0; i < c; i++) {
            SettingItem item = (SettingItem) custom.get(i);
            if (item.isGroup()) {
                item.setItemFocusable(false);
                item.setItemTitle(item.getItemName());
                groupStart = true;
            } else {
                item.setItemOptionType("max");
                item.setItemFocusable(true);
                if ("action".equalsIgnoreCase(item.getItemActionType())) {
                    item.setItemActionType(SettingConstants.ACTION_TYPE_CUSTOM_APP_ACTION);
                } else if (SettingConstants.ACTION_TYPE_PACKAGE_NAME.equalsIgnoreCase(item.getItemActionType())) {
                    item.setItemPackageName(item.getItemAction());
                    item.setItemAction("dummy");
                }
                if (i == 0 || groupStart) {
                    groupStart = false;
                    if (i < c - 1) {
                        if (!((SettingItem) custom.get(i + 1)).isGroup()) {
                            item.setItemBackground(SettingConstants.SETTING_ITEM_BG_TOP);
                        }
                    }
                    item.setItemBackground(SettingConstants.SETTING_ITEM_BG_CIRCLE);
                } else {
                    if (i < c - 1) {
                        if (!((SettingItem) custom.get(i + 1)).isGroup()) {
                            item.setItemBackground(SettingConstants.SETTING_ITEM_BG_NORMAL);
                        }
                    }
                    item.setItemBackground(SettingConstants.SETTING_ITEM_BG_BOTTOM);
                }
            }
        }
        if (settingType == SettingType.PLAY) {
            SettingUtils.insertToGroup(sourceList, custom, 1);
        } else if (settingType == SettingType.DISPLAY) {
            SettingUtils.insertToGroup(sourceList, custom, 2);
        } else if (settingType != SettingType.UNKNOWN) {
            SettingUtils.insertCustomItems(sourceList, custom, before);
        }
    }

    private List<SettingItem> parseSettingModel(SettingType type) {
        return CustomSettingProvider.getInstance().getItems(type);
    }

    private void ensureItem() {
        if (!MultiScreen.get().isSupportMS()) {
            removeItem(this.mSettingModel, SettingConstants.ID_MULTISCREEN);
        }
        boolean found = false;
        for (PackageInfo pi : this.mContext.getPackageManager().getInstalledPackages(0)) {
            if (pi.packageName.equals("com.skyworthdigital.jdsmart")) {
                found = true;
                break;
            }
        }
        if (!found) {
            removeItem(this.mSettingModel, SettingConstants.ID_JDSMART);
        }
    }

    private SettingModel initModelByAction(String jsonPath) {
        LogUtils.m1574i("EPG/setting/SettingMainFragment", "initModelByAction parse JSON = " + jsonPath);
        List modelList = JSON.parseArray(CloudUtils.getStreamFromAssets(this.mContext, jsonPath), SettingModel.class);
        if (ListUtils.isEmpty(modelList)) {
            LogUtils.m1571e("EPG/setting/SettingMainFragment", "parse json failed mPlayDisplayModels is null");
        } else {
            LogUtils.m1576i("EPG/setting/SettingMainFragment", "parse json success list size is ", Integer.valueOf(modelList.size()));
            this.mSettingModel = (SettingModel) modelList.get(0);
            LogUtils.m1576i("EPG/setting/SettingMainFragment", "system out:  ", JSON.toJSONString(this.mSettingModel));
        }
        return this.mSettingModel;
    }

    private void itemClickEvent(SettingItem item, int pos) {
        this.mSelfSettingItem = item;
        this.mSelectedPos = pos;
        if (!StringUtils.isEmpty(item.getItemAction()) || !StringUtils.isEmpty(item.getItemPackageName())) {
            String actionType = item.getItemActionType();
            CharSequence action = item.getItemAction();
            if ("activity".equals(actionType)) {
                String action2 = IntentUtils.getActionName(action);
                if (this.mSettingUpdateState != null) {
                    this.mSettingUpdateState.startActivityByAction(this.mContext, action2, item.getId(), item.getItemParams());
                }
            } else if (SettingConstants.ACTION_TYPE_FRAGMENT.equals(actionType) && !StringUtils.isEmpty(action)) {
                try {
                    this.mSettingEvent.onSwitchFragment((SettingBaseFragment) Class.forName(action).newInstance(), this.mBundle);
                } catch (Exception e) {
                    LogUtils.m1572e("EPG/setting/SettingMainFragment", "Class.forName(className) Exception", e);
                }
            } else if (SettingConstants.ACTON_TYPE_JSON.equals(actionType) && this.mSettingEvent != null) {
                this.mBundle.putSerializable(ISettingConstant.KEY_SETTING_ITEM, item);
                this.mBundle.putString(ISettingConstant.KEY_JSON_PATH, action);
                this.mSettingEvent.onSwitchFragment(new SettingMainFragment(), this.mBundle);
            } else if (SettingConstants.ACTION_TYPE_PACKAGE_NAME.equals(actionType)) {
                String packageName = item.getItemPackageName();
                String className = item.getItemClassName();
                LogUtils.m1571e("EPG/setting/SettingMainFragment", "packageName/className = :" + packageName + "/" + className);
                SettingUtils.startAppByPkgClassName(this.mContext, packageName, className, item.getId(), item.getItemParams());
            } else if (SettingConstants.ACTION_TYPE_CUSTOM_APP_ACTION.equals(actionType)) {
                SettingUtils.startActivityByAction(this.mContext, action, item.getId(), item.getItemParams());
            } else if ("none".equals(actionType) && Project.getInstance().getBuild().isHomeVersion()) {
                new UpdateCheckApk((Activity) this.mSettingEvent).checkApk();
            }
            sendClickPingback(this.mSelfSettingItem);
        } else if (SettingUtils.isCustomId(item.getId())) {
            SettingUtils.handleExceptionItem(item);
        } else if (this.mSettingEvent != null) {
            String lastState = item.getItemName();
            if (this.mSettingUpdateState != null) {
                this.mSettingUpdateState.saveNewCache(lastState);
            }
            if (this.mPreSettingItem != null) {
                this.mPreSettingItem.setItemLastState(lastState);
            }
            setBack(true);
            this.mSettingEvent.onPopStackFragment(this.mPreSettingItem);
        }
    }

    private void sendClickPingback(SettingItem item) {
        if (item != null) {
            LogUtils.m1574i("EPG/setting/SettingMainFragment", "sendClickPingback");
            if (!StringUtils.isEmpty(item.getPingbackBlock(), item.getPingbackRpage(), item.getPingbackRseat())) {
                PingBackParams params = new PingBackParams();
                params.add(Keys.f2035T, "20").add("rseat", rseat).add("rpage", rpage).add("block", block).add("rt", "i");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            }
        }
    }

    private void refreshOptionsUI(SettingGridAdapter adapter, int outPos, String value) {
        adapter.refreshOptionsText(this.mSettingGridView.getViewByPosition(outPos), value);
    }

    public void updateItem(SettingItem settingItem) {
        if (this.mSelectedPos >= 0) {
            int selectedPos = this.mSelectedPos;
            LogUtils.m1576i("EPG/setting/SettingMainFragment", "TEST refresh focus setFocusPosition(selectedPos)", Integer.valueOf(selectedPos));
            this.mSettingGridView.setFocusPosition(selectedPos);
            if (settingItem == null) {
                settingItem = this.mSelfSettingItem;
            }
            if (settingItem != null) {
                this.mSettingModel.getItems().set(selectedPos, settingItem);
                if (!ListUtils.isEmpty(settingItem.getItemOptions())) {
                    LogUtils.m1579w("EPG/setting/SettingMainFragment", "mSettingUpdateState is null ?", this.mSettingUpdateState);
                    if (this.mSettingUpdateState != null) {
                        String lastState = this.mSettingUpdateState.getLastStateByPos(settingItem);
                        if (StringUtils.isEmpty((CharSequence) lastState)) {
                            lastState = settingItem.getItemLastState();
                        }
                        refreshOptionsUI(this.mGridAdapter, selectedPos, lastState);
                        this.mSettingUpdateState.reupdateSettingMode(settingItem);
                    }
                } else if (this.mSettingUpdateState != null) {
                    refreshLastStateUI(this.mGridAdapter, selectedPos, this.mSettingUpdateState.getLastStateByPos(settingItem));
                }
            }
        }
    }

    private void refreshLastStateUI(SettingGridAdapter adapter, int outPos, String lastState) {
        adapter.refreshLastState(this.mSettingGridView.getViewByPosition(outPos), lastState);
    }

    private void removeItem(SettingModel model, int id) {
        if (model != null) {
            List<SettingItem> items = model.getItems();
            if (!ListUtils.isEmpty((List) items)) {
                for (SettingItem item : items) {
                    if (item.getId() == id) {
                        items.remove(item);
                        return;
                    }
                }
            }
        }
    }
}
