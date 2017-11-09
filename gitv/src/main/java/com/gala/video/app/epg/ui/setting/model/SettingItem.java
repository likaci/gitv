package com.gala.video.app.epg.ui.setting.model;

import java.io.Serializable;
import java.util.List;

public class SettingItem implements Serializable {
    private static final long serialVersionUID = -6517224682420694503L;
    private int id = -1;
    private boolean isGroup;
    private boolean isSelected;
    private String itemAction;
    private String itemActionType;
    private String itemBackground;
    private String itemClassName;
    private String itemDes;
    private String itemDevNameSuffix;
    private boolean itemFocusable;
    private String itemKey;
    private String itemLastState;
    private String itemName;
    private String itemOptionType;
    private List<String> itemOptions;
    private String itemPackageName;
    private String itemParams;
    private String itemTitle;
    private String pingbackBlock;
    private String pingbackRpage;
    private String pingbackRseat;

    public boolean isSelected() {
        return this.isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getItemActionType() {
        return this.itemActionType;
    }

    public void setItemActionType(String itemActionType) {
        this.itemActionType = itemActionType;
    }

    public String getItemPackageName() {
        return this.itemPackageName;
    }

    public void setItemPackageName(String itemPackageName) {
        this.itemPackageName = itemPackageName;
    }

    public String getItemClassName() {
        return this.itemClassName;
    }

    public void setItemClassName(String itemClassName) {
        this.itemClassName = itemClassName;
    }

    public String getItemTitle() {
        return this.itemTitle;
    }

    public void setItemTitle(String itemTitle) {
        this.itemTitle = itemTitle;
    }

    public String getItemName() {
        return this.itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDes() {
        return this.itemDes;
    }

    public void setItemDes(String itemDes) {
        this.itemDes = itemDes;
    }

    public String getItemLastState() {
        return this.itemLastState;
    }

    public void setItemLastState(String itemLastState) {
        this.itemLastState = itemLastState;
    }

    public List<String> getItemOptions() {
        return this.itemOptions;
    }

    public void setItemOptions(List<String> itemOptions) {
        this.itemOptions = itemOptions;
    }

    public String getItemAction() {
        return this.itemAction;
    }

    public void setItemAction(String itemAction) {
        this.itemAction = itemAction;
    }

    public boolean isItemFocusable() {
        return this.itemFocusable;
    }

    public void setItemFocusable(boolean itemFocus) {
        this.itemFocusable = itemFocus;
    }

    public String getItemOptionType() {
        return this.itemOptionType;
    }

    public void setItemOptionType(String itemOptionType) {
        this.itemOptionType = itemOptionType;
    }

    public String getItemBackground() {
        return this.itemBackground;
    }

    public void setItemBackground(String itemBackground) {
        this.itemBackground = itemBackground;
    }

    public String getPingbackBlock() {
        return this.pingbackBlock;
    }

    public void setPingbackBlock(String pingbackBlock) {
        this.pingbackBlock = pingbackBlock;
    }

    public String getPingbackRpage() {
        return this.pingbackRpage;
    }

    public void setPingbackRpage(String pingbackRpage) {
        this.pingbackRpage = pingbackRpage;
    }

    public String getPingbackRseat() {
        return this.pingbackRseat;
    }

    public void setPingbackRseat(String pingbackRseat) {
        this.pingbackRseat = pingbackRseat;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isGroup() {
        return this.isGroup;
    }

    public void setIsGroup(boolean isGroup) {
        this.isGroup = isGroup;
    }

    public String getItemKey() {
        return this.itemKey;
    }

    public void setItemKey(String itemKey) {
        this.itemKey = itemKey;
    }

    public String getItemDevNameSuffix() {
        return this.itemDevNameSuffix;
    }

    public void setItemDevNameSuffix(String itemDevNameSuffix) {
        this.itemDevNameSuffix = itemDevNameSuffix;
    }

    public String getItemParams() {
        return this.itemParams;
    }

    public void setItemParams(String params) {
        this.itemParams = params;
    }
}
