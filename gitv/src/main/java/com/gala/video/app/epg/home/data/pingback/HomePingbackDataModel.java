package com.gala.video.app.epg.home.data.pingback;

public class HomePingbackDataModel {
    public static final String TAG = "pingback/HomePingbackDataModel";
    private String block = "";
    private String cardLine = "";
    private String hissrch = "";
    private SearchRecordType searchRecordType;
    private String subscribeBtnName = "";

    public static class Builder {
        private String block = "";
        private String cardLine = "";
        private String hissrch = "";
        private SearchRecordType searchRecordType;
        private String subscribeBtnName = "";

        public Builder hissrch(String hissrch) {
            this.hissrch = hissrch;
            return this;
        }

        public Builder block(String block) {
            this.block = block;
            return this;
        }

        public Builder searchRecordType(SearchRecordType searchRecordType) {
            this.searchRecordType = searchRecordType;
            return this;
        }

        public Builder subscribleBtnName(String subscribleBtnName) {
            this.subscribeBtnName = subscribleBtnName;
            return this;
        }

        public Builder cardLine(String cardLine) {
            this.cardLine = cardLine;
            return this;
        }

        public HomePingbackDataModel build() {
            return new HomePingbackDataModel(this);
        }
    }

    public enum SearchRecordType {
        NONE,
        SEARCH,
        RECORD
    }

    public HomePingbackDataModel(Builder builder) {
        this.hissrch = builder.hissrch;
        this.block = builder.block;
        this.searchRecordType = builder.searchRecordType;
        this.subscribeBtnName = builder.subscribeBtnName;
        this.cardLine = builder.cardLine;
    }

    public String getHissrch() {
        return this.hissrch;
    }

    public String getBlock() {
        return this.block;
    }

    public SearchRecordType getSearchRecordType() {
        return this.searchRecordType;
    }

    public String getSubscribeBtnName() {
        return this.subscribeBtnName;
    }

    public String getCardLine() {
        return this.cardLine;
    }
}
