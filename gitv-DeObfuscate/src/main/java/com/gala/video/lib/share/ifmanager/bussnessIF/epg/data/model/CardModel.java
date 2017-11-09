package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

import com.gala.tvapi.vrs.model.GroupKvs;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.share.utils.Precondition;
import java.util.List;

public class CardModel extends HomeModel {
    private boolean isVipTab;
    public GroupKvs kv;
    private int mAllEntryPosition = -1;
    private int mCardLine = 0;
    private CharSequence mCharTitle = "";
    private boolean mHasAllEntry = false;
    private String mId = "";
    private boolean mIsHasCarousel = false;
    private boolean mIsNew;
    private List<ItemModel> mItemModelList = null;
    private float mScaleFactor = 1.1f;
    private WidgetChangeStatus mStatus = WidgetChangeStatus.NoChange;
    private String mTemplateId = "";
    private String mTitle = "";
    private int mWidgetType;

    public CardModel(boolean vipTab) {
        this.isVipTab = vipTab;
    }

    public void setHasAllEntry(boolean allEntry) {
        this.mHasAllEntry = allEntry;
    }

    public void setAllEntryPosition(int position) {
        this.mAllEntryPosition = position;
    }

    public int getAllEntryPosition() {
        return this.mAllEntryPosition;
    }

    public boolean hasAllEntry() {
        return this.mHasAllEntry;
    }

    public void setCardLine(int cardLine) {
        this.mCardLine = cardLine;
    }

    public int getCardLine() {
        return this.mCardLine;
    }

    public void setIsNew(boolean isNew) {
        this.mIsNew = isNew;
    }

    public boolean isNew() {
        return this.mIsNew;
    }

    public void setIsHasCarousel(boolean isHasCarousel) {
        this.mIsHasCarousel = isHasCarousel;
    }

    public boolean isIsHasCarousel() {
        return this.mIsHasCarousel;
    }

    public void setWidgetType(int widgetType) {
        this.mWidgetType = widgetType;
    }

    public int getWidgetType() {
        return this.mWidgetType;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public String getId() {
        return this.mId;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setCharSqTitle(CharSequence charSequence) {
        this.mCharTitle = charSequence;
    }

    public CharSequence getCharSqTitle() {
        return this.mCharTitle;
    }

    public void setTemplateId(String templateId) {
        this.mTemplateId = templateId;
    }

    public String getTemplateId() {
        return this.mTemplateId;
    }

    public void setWidgetChangeStatus(WidgetChangeStatus status) {
        this.mStatus = status;
    }

    public WidgetChangeStatus getWidgetChangeStatus() {
        return this.mStatus;
    }

    public List<ItemModel> getItemModelList() {
        return this.mItemModelList;
    }

    public synchronized void setItemModelList(List<ItemModel> itemModelList) {
        this.mItemModelList = itemModelList;
    }

    public float getScaleFactor() {
        return this.mScaleFactor;
    }

    public void setScaleFactor(float scaleFactor) {
        this.mScaleFactor = scaleFactor;
    }

    public boolean isVipTab() {
        return this.isVipTab;
    }

    public boolean isEmpty() {
        return Precondition.isEmpty(this.mItemModelList);
    }

    public int getSize() {
        if (Precondition.isEmpty(this.mItemModelList)) {
            return 0;
        }
        return this.mItemModelList.size();
    }

    public String toString() {
        return "CardModel [mIsNew=" + this.mIsNew + ", mIsHasCarousel=" + this.mIsHasCarousel + ", isVipTab=" + this.isVipTab + ", mWidgetType=" + this.mWidgetType + ", mId=" + this.mId + ", mTitle=" + this.mTitle + ", mTemplateId=" + this.mTemplateId + ", mStatus=" + this.mStatus + ", mItemModelList=" + this.mItemModelList + ", mHasAllEntry=" + this.mHasAllEntry + ", mCardLine=" + this.mCardLine + AlbumEnterFactory.SIGN_STR;
    }
}
