package com.gala.video.lib.share.uikit.cache;

import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import java.util.ArrayList;
import java.util.List;

public class UikitCardListData {
    private List<CardInfoModel> mCardList;
    private boolean mIsLock = false;

    public void setLock(boolean isLock) {
        this.mIsLock = isLock;
    }

    public boolean isLock() {
        return this.mIsLock;
    }

    public List<CardInfoModel> getCardList() {
        return this.mCardList;
    }

    public void setCardList(List<CardInfoModel> cardList) {
        if (this.mCardList != null) {
            synchronized (this.mCardList) {
                this.mCardList.clear();
                for (CardInfoModel card : cardList) {
                    this.mCardList.add(card);
                }
            }
            return;
        }
        this.mCardList = new ArrayList(8);
        for (CardInfoModel card2 : cardList) {
            this.mCardList.add(card2);
        }
    }

    public void addCard(CardInfoModel card) {
        if (this.mCardList == null) {
            this.mCardList = new ArrayList(8);
        }
        this.mCardList.add(card);
    }

    public int isContainCard(String cardId) {
        if (this.mCardList == null || this.mCardList.size() == 0) {
            return -1;
        }
        for (int i = 0; i < this.mCardList.size(); i++) {
            if (((CardInfoModel) this.mCardList.get(i)).mCardId.equals(cardId)) {
                return i;
            }
        }
        return -1;
    }

    public void updateCard(int index, CardInfoModel model) {
        if (this.mCardList != null && this.mCardList.size() != 0) {
            this.mCardList.set(index, model);
        }
    }

    public void updateCard(String cardId, CardInfoModel model) {
        if (this.mCardList != null && this.mCardList.size() != 0) {
            for (int i = 0; i < this.mCardList.size(); i++) {
                if (((CardInfoModel) this.mCardList.get(i)).mCardId.equals(cardId)) {
                    this.mCardList.set(i, model);
                }
            }
        }
    }

    public void updateItems(String cardId, ItemInfoModel[][] list) {
        if (this.mCardList != null && this.mCardList.size() != 0) {
            int i = 0;
            while (i < this.mCardList.size()) {
                if (((CardInfoModel) this.mCardList.get(i)).mCardId != null && ((CardInfoModel) this.mCardList.get(i)).mCardId.equals(cardId)) {
                    ((CardInfoModel) this.mCardList.get(i)).setItemInfoModels(list);
                }
                i++;
            }
        }
    }

    public CardInfoModel getCard(String cardId) {
        if (this.mCardList == null || this.mCardList.size() == 0) {
            return null;
        }
        for (CardInfoModel card : this.mCardList) {
            if (card.mCardId != null && card.mCardId.equals(cardId)) {
                return card;
            }
        }
        return null;
    }
}
