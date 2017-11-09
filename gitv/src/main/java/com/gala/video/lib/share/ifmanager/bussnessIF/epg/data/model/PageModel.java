package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model;

import com.gala.video.lib.share.utils.Precondition;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PageModel extends HomeModel {
    private String mBannerIds = "";
    private String mBannerPlaces = "";
    private List<CardModel> mCardList;
    private int mId;
    private boolean mIsNew;
    private boolean mIsPlaceChange;
    private boolean mIsVipTab;
    private int mPageLayoutIndex = -1;
    private String mResourceId = "";
    private String mTvBackgroundId = "";
    private WidgetChangeStatus mWidgetChangeStatus = WidgetChangeStatus.NoChange;

    public void setIsNew(boolean isNew) {
        this.mIsNew = isNew;
    }

    public boolean isNew() {
        return this.mIsNew;
    }

    public boolean isPlaceChanged() {
        return this.mIsPlaceChange;
    }

    public void setIsPlaceChanged(boolean isPlaceChange) {
        this.mIsPlaceChange = isPlaceChange;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public int getId() {
        return this.mId;
    }

    public String getResourceId() {
        return this.mResourceId;
    }

    public void setResourceId(String resourceId) {
        this.mResourceId = resourceId;
    }

    public void setCardList(List<CardModel> cardList) {
        this.mCardList = cardList;
    }

    public synchronized List<CardModel> getCardList() {
        return this.mCardList;
    }

    public void addCardModel(CardModel model) {
        if (this.mCardList == null) {
            this.mCardList = new CopyOnWriteArrayList();
        }
        this.mCardList.add(model);
    }

    public WidgetChangeStatus getWidgetChangeStatus() {
        return this.mWidgetChangeStatus;
    }

    public void setWidgetChangeStatus(WidgetChangeStatus widgetChangeStatus) {
        this.mWidgetChangeStatus = widgetChangeStatus;
    }

    public void setAllWidgetChangeStatus(WidgetChangeStatus widgetChangeStatus) {
        setWidgetChangeStatus(widgetChangeStatus);
        if (!Precondition.isEmpty(this.mCardList)) {
            for (CardModel card : this.mCardList) {
                card.setWidgetChangeStatus(widgetChangeStatus);
                if (!Precondition.isEmpty(card.getItemModelList())) {
                    for (ItemModel item : card.getItemModelList()) {
                        item.setWidgetChangeStatus(widgetChangeStatus);
                    }
                }
            }
        }
    }

    public boolean isVipTab() {
        return this.mIsVipTab;
    }

    public void setIsVipTab(boolean mIsVipTab) {
        this.mIsVipTab = mIsVipTab;
    }

    public int getPageLayoutIndex() {
        return this.mPageLayoutIndex;
    }

    public void setPageLayoutIndex(int pageLayoutIndex) {
        this.mPageLayoutIndex = pageLayoutIndex;
    }

    public void setTvBackgroundId(String tvBackgroundId) {
        this.mTvBackgroundId = tvBackgroundId;
    }

    public String getTvBackgroundId() {
        return this.mTvBackgroundId;
    }

    public String getBannerPlaces() {
        return this.mBannerPlaces;
    }

    public void setBannerPlaces(String bannerPlaces) {
        this.mBannerPlaces = bannerPlaces;
    }

    public String getBannerIds() {
        return this.mBannerIds;
    }

    public void setBannerIds(String bannerIds) {
        this.mBannerIds = bannerIds;
    }
}
