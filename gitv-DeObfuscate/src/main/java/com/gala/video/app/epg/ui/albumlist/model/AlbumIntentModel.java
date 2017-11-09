package com.gala.video.app.epg.ui.albumlist.model;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.Serializable;

public class AlbumIntentModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String buySource;
    private int channelId;
    private String channelName;
    private String dataTagId;
    private String dataTagName;
    private String dataTagType;
    private String f1930e;
    private String firstLabelLocationTagId;
    private String[] firstMultiLocationTagId;
    private String from;
    private boolean hasRecommendTag;
    private boolean jumpNextByRecTag;
    private QLayoutKind layoutKind;
    private int loadLimitSize;
    private int location4Playhistory = -1;
    private boolean noLeftFragment;
    private String pageType;
    private String projectName;
    private SearchIntentModel searchModel;

    public static class SearchIntentModel implements Serializable {
        private static final long serialVersionUID = 1;
        private int clickType;
        private String keyWord;
        private int pingbackPos;
        private String qpId;

        public String toString() {
            return "SearchIntentModel [keyWord=" + this.keyWord + ", clickType=" + this.clickType + ", qpId=" + this.qpId + ", pingbackPos=" + this.pingbackPos + AlbumEnterFactory.SIGN_STR;
        }

        public String getKeyWord() {
            return this.keyWord;
        }

        public void setKeyWord(String keyWord) {
            this.keyWord = keyWord;
        }

        public int getClickType() {
            return this.clickType;
        }

        public void setClickType(int clickType) {
            this.clickType = clickType;
        }

        public String getQpId() {
            return this.qpId;
        }

        public void setQpId(String qpId) {
            this.qpId = qpId;
        }

        public int getPingbackPos() {
            return this.pingbackPos;
        }

        public void setPingbackPos(int pingbackPos) {
            this.pingbackPos = pingbackPos;
        }
    }

    public boolean isNoLeftFragment() {
        return this.noLeftFragment;
    }

    public void setNoLeftFragment(boolean noLeftFragment) {
        this.noLeftFragment = noLeftFragment;
    }

    public int getLocation4Playhistory() {
        return this.location4Playhistory;
    }

    public void setLocation4Playhistory(int location4Playhistory) {
        this.location4Playhistory = location4Playhistory;
    }

    public String getDataTagId() {
        return this.dataTagId;
    }

    public void setDataTagId(String dataTagId) {
        this.dataTagId = dataTagId;
    }

    public String getDataTagName() {
        return this.dataTagName;
    }

    public void setDataTagName(String dataTagName) {
        this.dataTagName = dataTagName;
    }

    public String getDataTagType() {
        return this.dataTagType;
    }

    public void setDataTagType(String dataTagType) {
        this.dataTagType = dataTagType;
    }

    public QLayoutKind getLayoutKind() {
        return this.layoutKind;
    }

    public void setLayoutKind(QLayoutKind layoutKind) {
        this.layoutKind = layoutKind;
    }

    public String getFirstLabelLocationTagId() {
        return this.firstLabelLocationTagId;
    }

    public void setFirstLabelLocationTagId(String firstLabelLocationTagId) {
        this.firstLabelLocationTagId = firstLabelLocationTagId;
    }

    public String[] getFirstMultiLocationTagId() {
        return this.firstMultiLocationTagId;
    }

    public void setFirstMultiLocationTagId(String[] firstMultiLocationTagId) {
        this.firstMultiLocationTagId = firstMultiLocationTagId;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getBuySource() {
        return this.buySource;
    }

    public void setBuySource(String buySource) {
        this.buySource = buySource;
    }

    public String getE() {
        return this.f1930e;
    }

    public void setE(String e) {
        this.f1930e = e;
    }

    public String getPageType() {
        return this.pageType;
    }

    public void setPageType(String pageType) {
        this.pageType = pageType;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getChannelId() {
        return this.channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return this.channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public boolean isHasRecommendTag() {
        return this.hasRecommendTag;
    }

    public void setHasRecommendTag(boolean hasRecommendTag) {
        this.hasRecommendTag = hasRecommendTag;
    }

    public boolean isJumpNextByRecTag() {
        return this.jumpNextByRecTag;
    }

    public void setJumpNextByRecTag(boolean jumpNextByRecTag) {
        this.jumpNextByRecTag = jumpNextByRecTag;
    }

    public int getLoadLimitSize() {
        return this.loadLimitSize;
    }

    public void setLoadLimitSize(int loadLimitSize) {
        this.loadLimitSize = loadLimitSize;
    }

    public SearchIntentModel getSearchModel() {
        if (this.searchModel == null) {
            return new SearchIntentModel();
        }
        return this.searchModel;
    }

    public void setSearchModel(SearchIntentModel searchModel) {
        this.searchModel = searchModel;
    }

    public String toString() {
        return "AlbumIntentModel [pageType=" + this.pageType + ", channelId=" + this.channelId + ", channelName=" + this.channelName + ", from=" + this.from + ", buySource=" + this.buySource + ", loadLimitSize=" + this.loadLimitSize + ", dataTagId=" + this.dataTagId + ", dataTagName=" + this.dataTagName + ", dataTagType=" + this.dataTagType + ", labelFirstLocationTagId=" + this.firstLabelLocationTagId + ", noLeftFragment=" + this.noLeftFragment + ", location4Playhistory=" + this.location4Playhistory + ", searchModel=" + this.searchModel + AlbumEnterFactory.SIGN_STR;
    }
}
