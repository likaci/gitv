package com.gala.video.app.epg.home.data.provider;

import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.SerializableUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.utils.Precondition;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HomeMenuProvider {
    private static final HomeMenuProvider provider = new HomeMenuProvider();
    private List<ItemModel> mDataList;
    public boolean mIsDefault = true;
    private String mTitle = "";

    public static HomeMenuProvider getInstance() {
        return provider;
    }

    private boolean isDefault() {
        return this.mIsDefault;
    }

    public List<ItemModel> getDataList() {
        if (Precondition.isEmpty(this.mDataList)) {
            try {
                this.mDataList = (List) SerializableUtils.read(HomeDataConfig.HOME_MENU_DIR);
                if (Precondition.isEmpty(this.mDataList)) {
                    return getDefaultDataList();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.mIsDefault = false;
        return this.mDataList;
    }

    public void setDataList(List<ItemModel> list) {
        this.mIsDefault = false;
        this.mDataList = list;
        try {
            SerializableUtils.write(this.mDataList, HomeDataConfig.HOME_MENU_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ItemModel> getDefaultDataList() {
        this.mIsDefault = true;
        List<ItemModel> items = new ArrayList(5);
        ItemModel item1 = new ItemModel();
        item1.setItemType(ItemDataType.SEARCH);
        item1.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_SEARCH);
        items.add(item1);
        ItemModel item2 = new ItemModel();
        item2.setItemType(ItemDataType.RECORD);
        item2.setTitle("记录");
        items.add(item2);
        ItemModel item3 = new ItemModel();
        item3.setItemType(ItemDataType.LOGIN);
        if (GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext())) {
            item3.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_MY);
        } else {
            item3.setTitle(ActionBarDataFactory.TOP_BAR_TIME_NAME_LOGIN);
        }
        items.add(item3);
        ItemModel item4 = new ItemModel();
        item4.setItemType(ItemDataType.FEEDBACK);
        item4.setTitle("客服反馈");
        items.add(item4);
        ItemModel item5 = new ItemModel();
        item5.setItemType(ItemDataType.SETTING);
        item5.setTitle("设置");
        items.add(item5);
        return items;
    }

    public String getTitle() {
        if (Precondition.isEmpty(this.mTitle)) {
            try {
                this.mTitle = (String) SerializableUtils.read(HomeDataConfig.HOME_MENU_NAME_DIR);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.mTitle;
    }

    public void setTitle(String name) {
        this.mTitle = name;
        try {
            SerializableUtils.write(this.mTitle, HomeDataConfig.HOME_MENU_NAME_DIR);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
