package com.gala.video.app.epg.home.data.actionbar;

import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.actionbar.ActionBarItemInfo.Builder;
import com.gala.video.app.epg.home.widget.guidelogin.CheckInHelper;
import com.gala.video.lib.framework.core.utils.ViewUtils;
import java.util.ArrayList;
import java.util.List;

public class ActionBarDataFactory {
    private static final String TAG = "home/ActionBarDataFactory";
    public static final String TOP_BAR_TIME_NAME_GET_VIP = "领取VIP";
    public static final String TOP_BAR_TIME_NAME_LOGIN = "登录";
    public static final String TOP_BAR_TIME_NAME_MY = "我的";
    public static final String TOP_BAR_TIME_NAME_OPEN_VIP = "开通VIP";
    public static final String TOP_BAR_TIME_NAME_RENEW_VIP = "续费VIP";
    public static final String TOP_BAR_TIME_NAME_SEARCH = "搜索";
    public static final String TOP_BAR_TIME_TXT_EXPIRE = "天后到期";
    public static final String TOP_BAR_TIME_TXT_SIGN = "签到";

    public static List<ActionBarItemInfo> buildActionBarData() {
        List<ActionBarItemInfo> actionBarInfoList = new ArrayList();
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_SEARCH, C0508R.drawable.epg_action_bar_search_default, ActionBarType.SEARCH).id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_23dp).build());
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_MY, C0508R.drawable.epg_action_bar_my_default, ActionBarType.MY).messageBgDrawable(C0508R.drawable.epg_action_bar_item_message).setIsMy().id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_25dp).build());
        if (CheckInHelper.getIsShowActionBar()) {
            actionBarInfoList.add(buildCheckInItem());
        }
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_OPEN_VIP, C0508R.drawable.epg_action_bar_vip_default, ActionBarType.VIP).setIsVip().id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_25dp).build());
        return actionBarInfoList;
    }

    public static List<ActionBarItemInfo> buildActionBarSearchData() {
        List<ActionBarItemInfo> actionBarInfoList = new ArrayList();
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_MY, C0508R.drawable.epg_action_bar_my_default, ActionBarType.MY).setIsMy().id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_25dp).build());
        if (CheckInHelper.getIsShowActionBar()) {
            actionBarInfoList.add(buildCheckInItem());
        }
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_OPEN_VIP, C0508R.drawable.epg_action_bar_vip_default, ActionBarType.VIP).setIsVip().id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_25dp).build());
        return actionBarInfoList;
    }

    public static List<ActionBarItemInfo> buildActionBarMsgCenterData() {
        List<ActionBarItemInfo> actionBarInfoList = new ArrayList();
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_SEARCH, C0508R.drawable.epg_action_bar_search_default, ActionBarType.SEARCH).id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_23dp).build());
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_MY, C0508R.drawable.epg_action_bar_my_default, ActionBarType.MY).setIsMy().id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_25dp).build());
        if (CheckInHelper.getIsShowActionBar()) {
            actionBarInfoList.add(buildCheckInItem());
        }
        actionBarInfoList.add(new Builder(TOP_BAR_TIME_NAME_OPEN_VIP, C0508R.drawable.epg_action_bar_vip_default, ActionBarType.VIP).setIsVip().id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_25dp).build());
        return actionBarInfoList;
    }

    private static ActionBarItemInfo buildCheckInItem() {
        return new Builder(TOP_BAR_TIME_TXT_SIGN, C0508R.drawable.epg_action_bar_checkin_default, ActionBarType.CHECKIN).messageBgDrawable(C0508R.drawable.epg_action_bar_item_message).id(ViewUtils.generateViewId()).iconWith(C0508R.dimen.dimen_23dp).build();
    }
}
