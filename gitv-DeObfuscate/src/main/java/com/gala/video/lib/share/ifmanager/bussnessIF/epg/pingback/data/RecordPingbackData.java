package com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.data;

public class RecordPingbackData {
    private String mAllline = "";
    private String mCardId;
    private String mCardLine;
    private String mHissrch;
    private String mLine;
    private int mPos;
    private int mSearchRecordType;

    public void setPos(int pos) {
        this.mPos = pos;
    }

    public void setCardId(String cardId) {
        this.mCardId = cardId;
    }

    public void setLine(String line) {
        this.mLine = line;
    }

    public void setHissrch(String hissrch) {
        this.mHissrch = hissrch;
    }

    public void setSearchRecordType(int searchRecordType) {
        this.mSearchRecordType = searchRecordType;
    }

    public void setCardLine(String cardLine) {
        this.mCardLine = cardLine;
    }

    public void setAllline(String allline) {
        this.mAllline = allline;
    }

    public int getPos() {
        return this.mPos;
    }

    public String getCardId() {
        return this.mCardId;
    }

    public String getLine() {
        return this.mLine;
    }

    public String getHissrch() {
        return this.mHissrch;
    }

    public int getSearchRecordType() {
        return this.mSearchRecordType;
    }

    public String getCardLine() {
        return this.mCardLine;
    }

    public String getAllline() {
        return this.mAllline;
    }
}
