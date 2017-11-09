package com.gala.video.app.epg.home.data;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;

public class CardData extends DataSource {
    private int adCount = 0;
    private boolean hasAllEntry = false;
    public String id = "";
    private int mAllEntryPosition = -1;
    private int mCardLine = 0;
    private int mCardType;
    private int mChildCount = 0;
    public int mDefItems;
    private int mSawItems = 0;
    private String mTemplateId = "";
    private boolean vipTab;

    public void setVipTab(boolean vip) {
        this.vipTab = vip;
    }

    public boolean isVipTab() {
        return this.vipTab;
    }

    public boolean hasAllEntry() {
        return this.hasAllEntry;
    }

    public void setAllEntry(boolean allEntry) {
        this.hasAllEntry = allEntry;
    }

    public int getAllEntryPosition() {
        return this.mAllEntryPosition;
    }

    public void setAllEntryPosition(int position) {
        this.mAllEntryPosition = position;
    }

    public int getCardType() {
        return this.mCardType;
    }

    public void setCardType(int cardType) {
        this.mCardType = cardType;
    }

    public void setCardLine(int line) {
        this.mCardLine = line;
    }

    public void setDefaultShowChildCount(int count) {
        this.mDefItems = count;
    }

    public void setCardChildCount(int count) {
        this.mChildCount = count;
    }

    public void setCardTemplateId(String id) {
        this.mTemplateId = id;
    }

    public int getCardLine() {
        return this.mCardLine;
    }

    public int getCardChildCount(int count) {
        return this.mChildCount;
    }

    public int getCardDefaultShowChildCount() {
        return this.mDefItems;
    }

    public String getCardTemplateId() {
        return this.mTemplateId;
    }

    public void setSawItems(int sawItems) {
        this.mSawItems = sawItems;
    }

    public int getSawItems() {
        return this.mSawItems;
    }

    public int getAdCount() {
        return this.adCount;
    }

    public void setAdCount(int adCount) {
        this.adCount = adCount;
    }
}
