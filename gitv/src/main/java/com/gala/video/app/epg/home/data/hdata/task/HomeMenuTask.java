package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.result.ApiResultChannelLabels;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.HomeMenuProvider;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.Precondition;
import java.util.ArrayList;
import java.util.List;

public class HomeMenuTask extends BaseRequestTask {
    private static final String ID = "212628412";
    private static final String IS_FREE = "1";

    public void invoke() {
        VrsHelper.channelLabels.callSync(new IVrsCallback<ApiResultChannelLabels>() {
            public void onSuccess(ApiResultChannelLabels apiResultChannelLabels) {
                if (apiResultChannelLabels != null && apiResultChannelLabels.data != null) {
                    List<ChannelLabel> list = apiResultChannelLabels.data.items;
                    if (!Precondition.isEmpty((List) list)) {
                        HomeMenuProvider.getInstance().setTitle(apiResultChannelLabels.data.name);
                        List<ItemModel> items = new ArrayList();
                        for (ChannelLabel data : list) {
                            if (data.getType() == ResourceType.DIY && data.itemKvs != null) {
                                String tvFunction = data.itemKvs.tvfunction;
                                ItemModel item = new ItemModel();
                                item.setIcon(data.itemKvs.tvPic);
                                if (tvFunction.equals(ItemDataType.SEARCH.getValue())) {
                                    item.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_SEARCH);
                                    item.setItemType(ItemDataType.SEARCH);
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.RECORD.getValue())) {
                                    item.setTitle("记录");
                                    item.setItemType(ItemDataType.RECORD);
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.SETTING.getValue())) {
                                    item.setTitle("设置");
                                    item.setItemType(ItemDataType.SETTING);
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.FEEDBACK.getValue())) {
                                    item.setTitle("客服反馈");
                                    item.setItemType(ItemDataType.FEEDBACK);
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.LOGIN.getValue())) {
                                    item.setItemType(ItemDataType.LOGIN);
                                    if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
                                        item.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_MY);
                                    } else {
                                        item.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_LOGIN);
                                    }
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.VIP_ATTRIBUTE.getValue())) {
                                    item.setItemType(ItemDataType.VIP_ATTRIBUTE);
                                    item.setTitle(UserUtil.getUserTypeText());
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.MODE_SWITCH.getValue())) {
                                    item.setItemType(ItemDataType.MODE_SWITCH);
                                    item.setTitle("黑天模式");
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.PLAY_PROMPT.getValue())) {
                                    item.setItemType(ItemDataType.PLAY_PROMPT);
                                    item.setTitle("播放显示");
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.NETWORK.getValue())) {
                                    item.setItemType(ItemDataType.NETWORK);
                                    item.setTitle("网络");
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.BACKGROUND.getValue())) {
                                    item.setItemType(ItemDataType.BACKGROUND);
                                    item.setTitle("背景设置");
                                    items.add(item);
                                } else if (tvFunction.equals(ItemDataType.TAB_MANAGE.getValue())) {
                                    item.setItemType(ItemDataType.TAB_MANAGE);
                                    item.setTitle("桌面管理");
                                    items.add(item);
                                }
                            }
                        }
                        HomeMenuProvider.getInstance().setDataList(items);
                    }
                }
            }

            public void onException(ApiException e) {
                String str;
                IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
                String str2 = "pfec";
                if (e == null) {
                    str = "";
                } else {
                    str = e.getCode();
                }
                addItem = addItem.addItem(str2, str);
                str2 = Keys.ERRURL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getUrl();
                }
                addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "HomeMenuTask");
                str2 = Keys.ERRDETAIL;
                if (e == null) {
                    str = "";
                } else {
                    str = e.getMessage();
                }
                addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.T, "0").setOthersNull().post();
            }
        }, ID, "1");
    }

    public void onOneTaskFinished() {
        HomeDataObservable.getInstance().post(HomeDataType.HOME_MENU, WidgetChangeStatus.DataChange, null);
    }
}
