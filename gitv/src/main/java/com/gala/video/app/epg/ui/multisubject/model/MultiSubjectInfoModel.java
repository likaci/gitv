package com.gala.video.app.epg.ui.multisubject.model;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel.Wrapper;
import java.io.Serializable;
import java.util.List;

public class MultiSubjectInfoModel extends Wrapper implements Serializable {
    private static final long serialVersionUID = 1;
    private String buyFrom;
    private String buysource;
    private CardModel cardModel;
    private String e = PingBackUtils.createEventId();
    private int enterType;
    private String from;
    private String itemId;
    private String mSourceType;
    private int playIndex;
    private String playType;
    List<Album> trailerlist;

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
        return this.e;
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

    public List<Album> getAlbumList() {
        return this.trailerlist;
    }

    public void setAlbumList(List<Album> albumList) {
        this.trailerlist = albumList;
    }

    public void setSourceType(String sourceType) {
        this.mSourceType = sourceType;
    }

    public String getSourceType() {
        return this.mSourceType;
    }

    public String getBuyFrom() {
        return this.buyFrom;
    }

    public void setBuyFrom(String buyFrom) {
        this.buyFrom = buyFrom;
    }

    public CardModel getCardModel() {
        return this.cardModel;
    }

    public void setCardModel(CardModel cardModel) {
        this.cardModel = cardModel;
    }
}
