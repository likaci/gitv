package com.gala.video.lib.share.uikit.loader;

import com.gala.video.lib.share.uikit.data.CardInfoModel;
import java.util.List;

public class UikitEvent {
    public String background;
    public String cardId;
    public CardInfoModel cardInfoModel;
    public List<CardInfoModel> cardList;
    public int eventType;
    public int layoutChange;
    public int pageNo;
    public String sourceId;
    public int uikitEngineId;

    public UikitEvent(UikitEvent event) {
        this.eventType = event.eventType;
        this.pageNo = event.pageNo;
        this.layoutChange = event.layoutChange;
        this.cardId = event.cardId;
        this.sourceId = event.sourceId;
        this.uikitEngineId = event.uikitEngineId;
        this.cardList = event.cardList;
        this.cardInfoModel = event.cardInfoModel;
    }

    public String toString() {
        return "event type = " + this.eventType + ", page number = " + this.pageNo + ", layout change = " + this.layoutChange + ", card id = " + this.cardId + ", source id = " + this.sourceId + ", uikit engine id = " + this.uikitEngineId;
    }
}
