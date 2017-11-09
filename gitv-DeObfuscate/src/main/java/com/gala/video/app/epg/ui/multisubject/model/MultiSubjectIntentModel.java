package com.gala.video.app.epg.ui.multisubject.model;

import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import java.io.Serializable;

public class MultiSubjectIntentModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String buysource;
    private CardModel cardModel;
    private String f1936e = PingBackUtils.createEventId();
    private int enterType = 13;
    private String from;
    private String itemId;
    private int playIndex;
    private String playType = MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI;

    public String getItemId() {
        return this.itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getPlayType() {
        return this.playType;
    }

    public void setPlayType(String playType) {
        this.playType = playType;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBuysource() {
        return this.buysource;
    }

    public void setBuysource(String buysource) {
        this.buysource = buysource;
    }

    public String getE() {
        return this.f1936e;
    }

    public int getEnterType() {
        return this.enterType;
    }

    public void setEnterType(int enterType) {
        this.enterType = enterType;
    }

    public int getPlayIndex() {
        return this.playIndex;
    }

    public void setPlayIndex(int playIndex) {
        this.playIndex = playIndex;
    }

    public CardModel getCardModel() {
        return this.cardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.cardModel = cardModel;
    }
}
