package com.gala.video.app.epg.home.widget.actionbar;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.tvapi.type.UserType;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.home.data.actionbar.ActionBarItemInfo;
import com.gala.video.app.epg.home.data.actionbar.ActionBarType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.pingback.HomePingbackSender;
import com.gala.video.app.epg.home.utils.PromotionUtil;
import com.gala.video.app.epg.home.widget.checkin.CheckInTimeHelper;
import com.gala.video.app.epg.home.widget.checkin.CheckInWrapper;
import com.gala.video.app.epg.home.widget.guidelogin.CheckInHelper;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.app.epg.ui.subjectreview.QSubjectUtils;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ViewUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder;
import com.gala.video.lib.share.ifimpl.ucenter.account.impl.LoginCallbackRecorder.LoginCallbackRecorderListener;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingbackUtils;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class ActionBarAdapter implements ActionBarStateListener, IActionBarClickListener {
    private static final String TAG = "home/widget/ActionBarAdapter";
    protected String buy_from = "top";
    protected String copy = "NA";
    protected int entertype = 7;
    protected String from = "top_tab";
    private boolean isFocusChanged = false;
    protected ActionBarPageType mActionBarPageType = ActionBarPageType.HOME_PAGE;
    private final List<View> mBarViewList;
    private boolean mCheckInHome = true;
    protected ActionBarItemView mCheckInView;
    private CheckInWrapper mCheckInWrapper;
    protected Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private List<ActionBarItemInfo> mItemInfos = new ArrayList();
    private long mLastAnimationX = 0;
    private long mLastAnimationY = 0;
    protected ActionBarType mLeftTopActionBtnType = ActionBarType.SEARCH;
    private final LoginCallbackRecorderListener mLoginListener = new C07002();
    protected ActionBarItemView mMyChildView;
    private int mPreFocusedId;
    protected ActionBarItemView mVipChildView;
    private VipOnFocusChangeListener mVipOnFocusChangeListener;
    private TextView mVipTipsView;

    class C06971 implements Runnable {
        C06971() {
        }

        public void run() {
            final int count = GetInterfaceTools.getMsgCenter().getUnreadIMsgListCount();
            LogUtils.m1568d(ActionBarAdapter.TAG, "updateMessageCountOnStart, unread message count = " + count);
            if (!ActionBarAdapter.this.mHandler.post(new Runnable() {
                public void run() {
                    LogUtils.m1568d(ActionBarAdapter.TAG, "updateMessageCountOnStart, update msg text count");
                    ActionBarAdapter.this.rejectMsgCheckIn(count);
                }
            })) {
                LogUtils.m1568d(ActionBarAdapter.TAG, "updateMessageCountOnStart, failed to post a runnable to the main thread");
            }
        }
    }

    class C07002 implements LoginCallbackRecorderListener {

        class C06981 implements Runnable {
            C06981() {
            }

            public void run() {
                ActionBarAdapter.this.updateMyAndLoginView();
                ActionBarAdapter.this.updateVipViewText();
            }
        }

        class C06992 implements Runnable {
            C06992() {
            }

            public void run() {
                ActionBarAdapter.this.updateMyAndLoginView();
                ActionBarAdapter.this.updateVipViewText();
            }
        }

        C07002() {
        }

        public void onLogout(String uid) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(ActionBarAdapter.TAG, "Logout =" + uid);
            }
            ActionBarAdapter.this.mHandler.post(new C06981());
        }

        public void onLogin(String uid) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(ActionBarAdapter.TAG, "Login =" + uid);
            }
            ActionBarAdapter.this.mHandler.post(new C06992());
        }
    }

    public interface VipOnFocusChangeListener {
        void onFocusChanged(boolean z);
    }

    public ActionBarAdapter(List<ActionBarItemInfo> itemInfos, Context mContext) {
        this.mItemInfos = itemInfos;
        this.mContext = mContext;
        this.mBarViewList = new ArrayList();
    }

    public void setNextFocusDownId(int id) {
        for (View view : this.mBarViewList) {
            view.setNextFocusDownId(id);
        }
    }

    public void setVipOnFocusChangedListener(VipOnFocusChangeListener listener) {
        this.mVipOnFocusChangeListener = listener;
    }

    public void onChildFocusChanged(View v, boolean hasFocus) {
        ActionBarItemView itemView = (ActionBarItemView) v;
        ActionBarType type = (ActionBarType) v.getTag();
        if (hasFocus) {
            this.mPreFocusedId = v.getId();
        }
        if (type == ActionBarType.SEARCH) {
            if (hasFocus) {
                itemView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_text_focus));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(C0508R.drawable.epg_action_bar_search_focused));
            } else {
                itemView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_text_normal));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(C0508R.drawable.epg_action_bar_search_default));
            }
        }
        if (type == ActionBarType.MY) {
            if (hasFocus) {
                itemView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_text_focus));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(C0508R.drawable.epg_action_bar_my_focused));
            } else {
                itemView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_text_normal));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(C0508R.drawable.epg_action_bar_my_default));
            }
        }
        if (type == ActionBarType.CHECKIN) {
            if (hasFocus) {
                itemView.setTextColor(ResourceUtil.getColor(C0508R.color.action_bar_text_focus));
                itemView.setIconDrawable(ResourceUtil.getDrawable(C0508R.drawable.epg_action_bar_checkin_default));
            } else {
                itemView.setTextColor(ResourceUtil.getColor(C0508R.color.action_bar_text_normal));
                itemView.setIconDrawable(ResourceUtil.getDrawable(C0508R.drawable.epg_action_bar_checkin_default));
            }
        }
        if (type == ActionBarType.VIP) {
            this.mVipOnFocusChangeListener.onFocusChanged(hasFocus);
            if (hasFocus) {
                LogUtils.m1568d(TAG, "vip on focus change");
                itemView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_vip_text_focus));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(C0508R.drawable.epg_action_bar_vip_focused));
            } else {
                itemView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_vip_text_normal));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(C0508R.drawable.epg_action_bar_vip_default));
            }
        }
        this.isFocusChanged = true;
        AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, ActionBarAnimaitonUtils.getDelay());
        ActionBarAnimaitonUtils.setDelay(180);
    }

    public void onClick(View v, int position) {
        switch ((ActionBarType) v.getTag()) {
            case SEARCH:
                String searchName = "";
                if (v instanceof ActionBarItemView) {
                    onClickSearchBtn(((ActionBarItemView) v).getName(), position);
                    return;
                }
                return;
            case MY:
                String myName = "";
                if (v instanceof ActionBarItemView) {
                    myName = ((ActionBarItemView) v).getName();
                    if (this.mMyChildView != null && this.mMyChildView.getVisibility() == 0) {
                        this.mMyChildView.setMessageVisible(8);
                    }
                    onClickMyBtn(myName, position);
                    return;
                }
                return;
            case CHECKIN:
                if (v instanceof ActionBarItemView) {
                    onClickCheckInBtn(((ActionBarItemView) v).getName(), position);
                    return;
                }
                return;
            case VIP:
                String vipName = "";
                if (v != null && (v instanceof ActionBarItemView)) {
                    onClickVipBtn(((ActionBarItemView) v).getName(), position);
                    return;
                }
                return;
            case MULTI_SUBJECT:
                MultiSubjectEnterUtils.start(this.mContext, "211660112", "", "");
                return;
            case QSUBJECT:
                QSubjectUtils.startQSubjectActivity(this.mContext, null, null);
                return;
            default:
                return;
        }
    }

    protected void onVipBtnJump() {
        CharSequence url = "";
        IDynamicResult dynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (dynamicResult != null) {
            url = dynamicResult.getHomeHeaderVipUrl();
        }
        if (GetInterfaceTools.getIGalaVipManager().needShowActivationPage()) {
            GetInterfaceTools.getLoginProvider().startActivateActivity(this.mContext, this.from, 7);
        } else if (StringUtils.isEmpty(url)) {
            LogUtils.m1577w(TAG, "vip click url is empty");
            String incomeSrc = PingBackCollectionFieldUtils.getIncomeSrc();
            WebIntentParams params2 = new WebIntentParams();
            params2.incomesrc = incomeSrc;
            params2.from = this.from;
            params2.pageType = 2;
            params2.enterType = this.entertype;
            params2.buyFrom = this.buy_from;
            GetInterfaceTools.getWebEntry().startPurchasePage(this.mContext, params2);
        } else {
            LogUtils.m1568d(TAG, "vip click url = " + url);
            WebIntentParams params = new WebIntentParams();
            params.pageUrl = url;
            params.from = this.from;
            params.buyFrom = this.buy_from;
            GetInterfaceTools.getWebEntry().gotoCommonWebActivity(this.mContext, params);
        }
    }

    protected void onCheckInJump() {
        if (this.mCheckInView != null) {
            this.mCheckInView.setMessageVisible(8);
            LogUtils.m1574i(TAG, "onCheckIn close ");
            CheckInTimeHelper.setShowedState();
        }
        CheckInHelper.startCheckInActivity(this.mContext);
    }

    public int getCount() {
        return this.mItemInfos.size();
    }

    public int getItemId(int pos) {
        if (pos < 0 || pos >= this.mItemInfos.size()) {
            return -1;
        }
        return ((ActionBarItemInfo) this.mItemInfos.get(pos)).getId();
    }

    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ActionBarItemView topBarItemView = new ActionBarItemView(this.mContext);
        this.mBarViewList.add(topBarItemView);
        topBarItemView.setId(ViewUtils.generateViewId());
        ActionBarItemInfo actionBarItemInfo = (ActionBarItemInfo) this.mItemInfos.get(position);
        topBarItemView.setTag(actionBarItemInfo.getActionType());
        topBarItemView.setText(actionBarItemInfo.getName());
        LogUtils.m1568d(TAG, "getView name = " + actionBarItemInfo.getName());
        topBarItemView.setId(actionBarItemInfo.getId());
        if (actionBarItemInfo.isVip()) {
            this.mVipChildView = topBarItemView;
            this.mVipChildView.setChildFocusBackgroundResource(C0508R.drawable.epg_action_bar_vip_bg_focused);
        }
        if (actionBarItemInfo.isMy()) {
            this.mMyChildView = topBarItemView;
            this.mMyChildView.setMessageBGDrawable(actionBarItemInfo.getMessageBgDrawable());
        }
        if (actionBarItemInfo.getActionType() == ActionBarType.CHECKIN) {
            this.mCheckInView = topBarItemView;
            this.mCheckInView.setMessageBGDrawable(actionBarItemInfo.getMessageBgDrawable());
            this.mCheckInWrapper = new CheckInWrapper(this.mCheckInView, this.mMyChildView);
        }
        topBarItemView.setIconDrawable(this.mContext.getResources().getDrawable(actionBarItemInfo.getIcon()));
        topBarItemView.setIconDrawableWidth(actionBarItemInfo.getIconWidth());
        if (position == getCount() - 1) {
            topBarItemView.setNextFocusRightId(topBarItemView.getId());
        }
        if (position == 0) {
            this.mPreFocusedId = topBarItemView.getId();
        }
        return topBarItemView;
    }

    public int getPreFocusId() {
        return this.mPreFocusedId;
    }

    public void updateActionBar() {
        LogUtils.m1568d(TAG, "updateActionBar()");
        updateVipViewText();
        updateMyAndLoginView();
        updateCheckInView();
    }

    public void updateCheckInView() {
        LogUtils.m1574i(TAG, "updateCheckInView mCheckInHome:" + this.mCheckInHome);
        if (this.mCheckInView == null || this.mCheckInWrapper == null) {
            LogUtils.m1571e(TAG, "updateCheckInView mCheckInView : " + this.mCheckInView + ",mCheckInWrapper:" + this.mCheckInWrapper);
        } else if (this.mCheckInHome) {
            LogUtils.m1574i(TAG, "updateCheckInView first enter ");
            this.mCheckInHome = false;
            this.mCheckInWrapper.start();
        } else {
            boolean isEqualDayTime = CheckInTimeHelper.isEqualDayTime();
            LogUtils.m1574i(TAG, "updateCheckInView isEqualDayTime: " + isEqualDayTime);
            if (!isEqualDayTime) {
                this.mCheckInWrapper.start();
            }
            setCheckInMessageVisible();
        }
    }

    protected void setCheckInMessageVisible() {
        if (this.mCheckInView != null) {
            boolean isNeedShowPoint = CheckInTimeHelper.isNeedShowPoint();
            LogUtils.m1574i(TAG, "updateCheckInView isNeedShowPoint: " + isNeedShowPoint);
            this.mCheckInView.setMessageVisible(isNeedShowPoint ? 0 : 8);
        }
    }

    public void onStart() {
        LoginCallbackRecorder.get().addListener(this.mLoginListener);
        if (this.mCheckInWrapper != null) {
            this.mCheckInWrapper.register();
        }
    }

    private void updateMyAndLoginView() {
        if (this.mMyChildView != null) {
            this.mMyChildView.setVisibility(0);
            if (GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext)) {
                this.mMyChildView.setText(ActionBarDataFactory.TOP_BAR_TIME_NAME_MY);
                LogUtils.m1568d(TAG, "updateMyAndLoginView name = 我的");
                return;
            }
            this.mMyChildView.setText(ActionBarDataFactory.TOP_BAR_TIME_NAME_MY);
            LogUtils.m1568d(TAG, "updateMyAndLoginView name = 登录");
        }
    }

    public void onStop() {
        LoginCallbackRecorder.get().removeListener(this.mLoginListener);
        if (this.mCheckInWrapper != null) {
            this.mCheckInWrapper.unRegister();
        }
    }

    public void updateVipViewText() {
        boolean isAccountExpire = false;
        if (this.mVipChildView != null) {
            this.mVipChildView.setVisibility(0);
            if (this.mVipChildView.hasFocus()) {
                this.mVipChildView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_vip_text_focus));
            } else {
                this.mVipChildView.setTextColor(this.mContext.getResources().getColor(C0508R.color.action_bar_vip_text_normal));
            }
            if (GetInterfaceTools.getIGalaVipManager().needShowActivationPage()) {
                this.mVipChildView.setText(ActionBarDataFactory.TOP_BAR_TIME_NAME_GET_VIP);
            } else if (this.mContext != null) {
                CharSequence cookie = GetInterfaceTools.getIGalaAccountManager().getAuthCookie();
                LogUtils.m1568d(TAG, "updateVipViewText() --- cookie = " + cookie);
                if (StringUtils.isEmpty(cookie)) {
                    LogUtils.m1568d(TAG, "updateVipViewText() --- 账号未登录");
                    this.mVipChildView.setText(ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP);
                    return;
                }
                LogUtils.m1568d(TAG, "updateVipViewText() --- 账号已经登录");
                UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
                if (userType != null) {
                    LogUtils.m1568d(TAG, "updateVipViewText() -- userType = " + userType.toJsonString());
                } else {
                    LogUtils.m1568d(TAG, "updateVipViewText() -- userType = null");
                }
                if (userType != null) {
                    isAccountExpire = userType.isExpire();
                }
                LogUtils.m1568d(TAG, "updateVipViewText() --- isAccountExpire = " + isAccountExpire);
                if (UserUtil.isOTTVip()) {
                    LogUtils.m1568d(TAG, "updateVipViewText() --- text = 续费VIP");
                    this.mVipChildView.setText(ActionBarDataFactory.TOP_BAR_TIME_NAME_RENEW_VIP);
                    return;
                }
                LogUtils.m1568d(TAG, "updateVipViewText() --- text = 开通VIP");
                this.mVipChildView.setText(ActionBarDataFactory.TOP_BAR_TIME_NAME_OPEN_VIP);
            }
        }
    }

    public void onClickCheckInBtn(String rseat, int position) {
        onCheckInJump();
        if (PingbackUtils.getPingbackPage(this.mContext) == PingbackPage.HomePage) {
            PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_p_0_" + "sign_" + position);
        }
        HomePingbackFactory.instance().createPingback(ClickPingback.ACTION_BAR_CLICK_PINGBACK).addItem("r", rseat).addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("block", "top").addItem("rseat", rseat).addItem("copy", "").addItem("count", HomePingbackSender.getInstance().getTabIndex()).setOthersNull().post();
    }

    public void onClickSearchBtn(String rseat, int position) {
        if (PingbackUtils.getPingbackPage(this.mContext) == PingbackPage.HomePage) {
            PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_p_0_" + "srch_" + position);
        }
        SearchEnterUtils.startSearchActivity(this.mContext);
        HomePingbackFactory.instance().createPingback(ClickPingback.ACTION_BAR_CLICK_PINGBACK).addItem("r", rseat).addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("block", "top").addItem("rseat", rseat).addItem("copy", "").addItem("count", HomePingbackSender.getInstance().getTabIndex()).setOthersNull().post();
    }

    public void onClickMyBtn(String rseat, int position) {
        if (PingbackUtils.getPingbackPage(this.mContext) == PingbackPage.HomePage) {
            PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_p_0_" + "my_" + position);
        }
        GetInterfaceTools.getLoginProvider().startUcenterActivityFromHomeTab(this.mContext);
        HomePingbackFactory.instance().createPingback(ClickPingback.ACTION_BAR_CLICK_PINGBACK).addItem("r", rseat).addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("block", "top").addItem("rseat", rseat).addItem("copy", "").addItem("count", HomePingbackSender.getInstance().getTabIndex()).setOthersNull().post();
    }

    public void onClickVipBtn(String rseat, int position) {
        if (PingbackUtils.getPingbackPage(this.mContext) == PingbackPage.HomePage) {
            PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_p_0_" + "vip_" + position);
        }
        onVipBtnJump();
        HomePingbackFactory.instance().createPingback(ClickPingback.ACTION_BAR_CLICK_PINGBACK).addItem("r", rseat).addItem("rpage", "tab_" + HomePingbackSender.getInstance().getTabName()).addItem("block", "top").addItem("rseat", rseat).addItem("copy", this.copy).addItem("count", HomePingbackSender.getInstance().getTabIndex()).setOthersNull().post();
    }

    public ActionBarPageType getActionBarPageType() {
        return this.mActionBarPageType;
    }

    public void updateVipTips(TextView actionBarTip) {
        this.mVipTipsView = actionBarTip;
        if (actionBarTip != null) {
            IDynamicResult dynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
            if (dynamicResult != null) {
                CharSequence vipTipTxt = dynamicResult.getHomeHeaderVipText();
                if (!StringUtils.isEmpty(vipTipTxt)) {
                    actionBarTip.setVisibility(0);
                    actionBarTip.setText(vipTipTxt);
                    this.copy = "vip";
                    return;
                }
            }
            if (GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext)) {
                UserType userType = GetInterfaceTools.getIGalaAccountManager().getUserType();
                if (userType == null) {
                    actionBarTip.setVisibility(8);
                    return;
                } else if (userType.isLitchi() || userType.isPlatinum()) {
                    LogUtils.m1568d(TAG, "updateVipTips, user is litchi VIP or Platinum VIP");
                    long expireTime = GetInterfaceTools.getIGalaAccountManager().getVipTimeStamp();
                    LogUtils.m1568d(TAG, "updateVipTips, user expire time = " + expireTime);
                    if (expireTime <= 0) {
                        actionBarTip.setVisibility(8);
                        return;
                    }
                    long curTime = DeviceUtils.getServerTimeMillis();
                    if (curTime == 0) {
                        curTime = System.currentTimeMillis();
                    }
                    long leftDays = (expireTime - curTime) / 86400000;
                    if (leftDays < 0 || leftDays >= 7) {
                        actionBarTip.setVisibility(8);
                        return;
                    }
                    actionBarTip.setVisibility(0);
                    actionBarTip.setText((1 + leftDays) + ActionBarDataFactory.TOP_BAR_TIME_TXT_EXPIRE);
                    LogUtils.m1568d(TAG, "updateVipTips, set vip text visible");
                    this.copy = "deadline";
                    return;
                }
            }
            actionBarTip.setVisibility(8);
        }
    }

    public void updateMessageCountOnReceive(IMsgContent content) {
        if (this.mMyChildView != null && content != null) {
            int count = GetInterfaceTools.getMsgCenter().getUnreadIMsgListCount();
            LogUtils.m1568d(TAG, "updateMessageCountOnReceive, unread message count = " + count);
            rejectMsgCheckIn(count);
        }
    }

    public void updateMessageCountOnStart() {
        if (this.mMyChildView != null) {
            new Thread8K(new C06971(), "ActionBarAdapter").start();
        }
    }

    private void rejectMsgCheckIn(int count) {
        int i = 8;
        if (this.mMyChildView != null) {
            if (CheckInTimeHelper.isNeedShowPoint()) {
                this.mMyChildView.setMessageVisible(8);
                return;
            }
            ActionBarItemView actionBarItemView = this.mMyChildView;
            if (count > 0) {
                i = 0;
            }
            actionBarItemView.setMessageVisible(i);
        }
    }

    public void onMessageUpdate() {
        updateMessageCountOnStart();
    }

    public void lastXAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationX > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake));
            this.mLastAnimationX = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public void lastYAnimation(View view) {
        if (AnimationUtils.currentAnimationTimeMillis() - this.mLastAnimationY > 500) {
            view.startAnimation(AnimationUtils.loadAnimation(this.mContext, C0508R.anim.epg_shake_y));
            this.mLastAnimationY = AnimationUtils.currentAnimationTimeMillis();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (this.mPreFocusedId == -1) {
            return true;
        }
        View v = ((Activity) this.mContext).findViewById(this.mPreFocusedId);
        ActionBarType type = (ActionBarType) v.getTag();
        switch (event.getKeyCode()) {
            case 19:
                if (v.hasFocus() && !this.isFocusChanged) {
                    lastYAnimation(v);
                }
                this.isFocusChanged = false;
                break;
            case 21:
                if (type == this.mLeftTopActionBtnType && !this.isFocusChanged) {
                    lastXAnimation(v);
                }
                this.isFocusChanged = false;
                break;
            case 22:
                if (type == ActionBarType.VIP && !this.isFocusChanged) {
                    if (ActionBarPageType.HOME_PAGE != this.mActionBarPageType || PromotionUtil.judgmentNextFocusIsSelf(2, v)) {
                        lastXAnimation(v);
                    } else {
                        this.mPreFocusedId = -1;
                    }
                }
                this.isFocusChanged = false;
                break;
        }
        return false;
    }

    public int getLastViewId() {
        if (ListUtils.isEmpty(this.mBarViewList)) {
            return -1;
        }
        View view = (View) this.mBarViewList.get(this.mBarViewList.size() - 1);
        if (view != null) {
            return view.getId();
        }
        return -1;
    }

    public int getAnimationLength() {
        int start = this.mVipChildView.getLeft();
        int end = this.mVipChildView.getRight();
        if (this.mVipTipsView != null && this.mVipTipsView.getVisibility() == 0) {
            end = this.mVipTipsView.getRight() - ResourceUtil.getDimensionPixelSize(C0508R.dimen.dimen_65dp);
        }
        LogUtils.m1568d(TAG, "vip left=" + start + ",vip right=" + this.mVipChildView.getRight() + ",tip right=" + this.mVipTipsView.getRight());
        return end - start;
    }

    public int getTipViewWidth() {
        if (this.mVipTipsView.getVisibility() != 0) {
            return 0;
        }
        return ((LayoutParams) this.mVipTipsView.getLayoutParams()).leftMargin + this.mVipTipsView.getWidth();
    }

    public int getVipViewLeft() {
        return this.mVipChildView.getLeft() + ResourceUtil.getDimensionPixelSize(C0508R.dimen.dimen_40dp);
    }

    public int getVipViewWidth() {
        return this.mVipChildView.getWidth();
    }

    public boolean isResetActionBar() {
        if ((this.mCheckInView == null) == CheckInHelper.getIsShowActionBar()) {
            return true;
        }
        return false;
    }

    public void resetData() {
        LogUtils.m1568d(TAG, "resetData");
        this.mItemInfos = ActionBarDataFactory.buildActionBarData();
        this.mBarViewList.clear();
    }
}
