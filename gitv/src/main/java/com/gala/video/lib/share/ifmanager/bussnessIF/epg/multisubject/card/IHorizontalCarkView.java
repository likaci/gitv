package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card;

import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;

public interface IHorizontalCarkView extends ICardView {
    void endTimeKeep();

    int fetchSawItem(boolean z);

    void initial(int i, int i2, CardModel cardModel);

    boolean isTimeKeeping();

    void reLoadTask();

    void recycle();

    void resetSawItem();

    void setCardModel(CardModel cardModel);

    void startTimeKeep();
}
