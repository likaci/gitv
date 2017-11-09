package com.gala.video.lib.share.uikit.action.data;

import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import java.io.Serializable;

public class SubscribeBtnActionData implements Serializable {
    private String mAllLine = "";
    private String mCardId;
    private int mCardLine;
    private BaseActionModel mChannelLabelActionModel;
    private int mChnId;
    private int mLine;
    private int mPos;
    private String mQpId;
    private int mSubscribeType;

    public int getSubscribeType() {
        return this.mSubscribeType;
    }

    public void setSubscribeType(int subscribeType) {
        this.mSubscribeType = subscribeType;
    }

    public String getQpId() {
        return this.mQpId;
    }

    public void setQpId(String qpId) {
        this.mQpId = qpId;
    }

    public int getChnId() {
        return this.mChnId;
    }

    public void setChnId(int chnId) {
        this.mChnId = chnId;
    }

    public void setCardLine(int cardLine) {
        this.mCardLine = cardLine;
    }

    public String getCardId() {
        return this.mCardId;
    }

    public void setCardId(String cardId) {
        this.mCardId = cardId;
    }

    public void setLine(int line) {
        this.mLine = line;
    }

    public int getLine() {
        return this.mLine;
    }

    public int getPos() {
        return this.mPos;
    }

    public void setPos(int pos) {
        this.mPos = pos;
    }

    public int getCardLine() {
        return this.mCardLine;
    }

    public void setAllLine(String allLine) {
        this.mAllLine = allLine;
    }

    public String getAllLine() {
        return this.mAllLine;
    }

    public void setChannelLabelActionModel(BaseActionModel actionModel) {
        this.mChannelLabelActionModel = actionModel;
    }

    public BaseActionModel getChannelLabelActionModel() {
        return this.mChannelLabelActionModel;
    }
}
