package com.gala.video.app.epg.ui.albumlist.model;

import android.text.TextUtils;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel.SearchIntentModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.Serializable;

public class AlbumInfoModel implements Serializable {
    private static final long serialVersionUID = 1;
    private String buySource;
    private int channelId;
    private String channelName;
    private String dataTagId;
    private String dataTagName;
    private String dataTagResourceType;
    private String dataTagType;
    private String f1929e;
    private boolean firstContentUpdate = true;
    private String firstLabelLocationTagId;
    private String[] firstMultiLocationTagId;
    private int focusPosition;
    private String from;
    private boolean hasRecommendTag;
    private String identification;
    private boolean jumpNextByRecTag;
    private QLayoutKind layoutKind;
    private boolean leftFragmentHasData;
    private int loadLimitSize;
    private boolean loadingData;
    private int location4Playhistory = -1;
    private String mtype;
    private boolean multiHasData;
    private boolean noLeftFragment;
    private String pageType;
    private String projectName;
    private boolean rightFragmentHasData;
    private String rseat;
    private SearchInfoModel searchModel;
    private int selectColumn;
    private int selectRow;
    private boolean showingCacheData;
    private String ttype;

    public static class SearchInfoModel implements Serializable {
        private static final long serialVersionUID = 1;
        private int clickType;
        private String keyWord;
        private String qpId;

        public SearchInfoModel(String keyWord, int clickType, String qpId) {
            this.keyWord = keyWord;
            this.clickType = clickType;
            this.qpId = qpId;
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

        public String toString() {
            return "SearchInfoModel [keyWord=" + this.keyWord + ", clickType=" + this.clickType + ", qpId=" + this.qpId + AlbumEnterFactory.SIGN_STR;
        }
    }

    public AlbumInfoModel(AlbumIntentModel m) {
        if (m != null) {
            this.pageType = m.getPageType();
            this.projectName = TextUtils.isEmpty(m.getProjectName()) ? IAlbumConfig.PROJECT_NAME_BASE_LINE : m.getProjectName();
            this.channelId = m.getChannelId();
            this.channelName = m.getChannelName();
            this.hasRecommendTag = m.isHasRecommendTag();
            this.jumpNextByRecTag = m.isJumpNextByRecTag();
            this.from = m.getFrom();
            this.buySource = m.getBuySource();
            this.f1929e = m.getE();
            this.loadLimitSize = m.getLoadLimitSize();
            this.dataTagId = m.getDataTagId();
            this.dataTagName = m.getDataTagName();
            this.dataTagType = m.getDataTagType();
            this.layoutKind = m.getLayoutKind();
            this.firstLabelLocationTagId = m.getFirstLabelLocationTagId();
            this.firstMultiLocationTagId = m.getFirstMultiLocationTagId();
            this.noLeftFragment = m.isNoLeftFragment();
            this.location4Playhistory = m.getLocation4Playhistory();
            SearchIntentModel s = m.getSearchModel();
            this.searchModel = new SearchInfoModel(s.getKeyWord(), s.getClickType(), s.getQpId());
        }
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

    public String getIdentification() {
        return this.identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
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
        return this.f1929e;
    }

    public void setE(String e) {
        this.f1929e = e;
    }

    public String getRseat() {
        return this.rseat;
    }

    public void setRseat(String rseat) {
        this.rseat = rseat;
    }

    public String getTtype() {
        return this.ttype;
    }

    public void setTtype(String ttype) {
        this.ttype = ttype;
    }

    public String getMtype() {
        return this.mtype;
    }

    public void setMtype(String mtype) {
        this.mtype = mtype;
    }

    public String getDataTagId() {
        return this.dataTagId;
    }

    public void setDataTagId(String tagId) {
        this.dataTagId = tagId;
    }

    public String getDataTagName() {
        return this.dataTagName;
    }

    public void setDataTagName(String tagName) {
        this.dataTagName = tagName;
    }

    public String getDataTagType() {
        return this.dataTagType;
    }

    public void setDataTagType(String tagType) {
        this.dataTagType = tagType;
    }

    public String getDataTagResourceType() {
        return this.dataTagResourceType;
    }

    public void setDataTagResourceType(String dataTagResourceType) {
        this.dataTagResourceType = dataTagResourceType;
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

    public void setFirstLabelLocationTagId(String locationTagId) {
        this.firstLabelLocationTagId = locationTagId;
    }

    public String[] getFirstMultiLocationTagId() {
        return this.firstMultiLocationTagId;
    }

    public void setFirstMultiLocationTagId(String[] firstMultiLocationTagId) {
        this.firstMultiLocationTagId = firstMultiLocationTagId;
    }

    public boolean isLoadingData() {
        return this.loadingData;
    }

    public void setLoadingData(boolean loadingData) {
        this.loadingData = loadingData;
    }

    public boolean isShowingCacheData() {
        return this.showingCacheData;
    }

    public void setShowingCacheData(boolean showingCacheData) {
        this.showingCacheData = showingCacheData;
    }

    public boolean isLeftFragmentHasData() {
        return this.leftFragmentHasData;
    }

    public void setLeftFragmentHasData(boolean leftHasData) {
        this.leftFragmentHasData = leftHasData;
    }

    public boolean isRightFragmentHasData() {
        return this.rightFragmentHasData;
    }

    public void setRightFragmentHasData(boolean rightHasData) {
        this.rightFragmentHasData = rightHasData;
    }

    public boolean isMultiHasData() {
        return this.multiHasData;
    }

    public void setMultiHasData(boolean multiHasData) {
        this.multiHasData = multiHasData;
    }

    public int getFocusPosition() {
        return this.focusPosition;
    }

    public void setFocusPosition(int focusPosition) {
        this.focusPosition = focusPosition;
    }

    public int getSelectRow() {
        return this.selectRow;
    }

    public void setSelectRow(int selectRow) {
        this.selectRow = selectRow;
    }

    public int getSelectColumn() {
        return this.selectColumn;
    }

    public void setSelectColumn(int selectColumn) {
        this.selectColumn = selectColumn;
    }

    public int getLoadLimitSize() {
        return this.loadLimitSize;
    }

    public void setLoadLimitSize(int loadLimitSize) {
        this.loadLimitSize = loadLimitSize;
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

    public SearchInfoModel getSearchModel() {
        if (this.searchModel == null) {
            return new SearchInfoModel();
        }
        return this.searchModel;
    }

    public void setSearchModel(SearchInfoModel search) {
        this.searchModel = search;
    }

    public String toString() {
        return "AlbumInfoModel [pageType=" + this.pageType + ", identification=" + this.identification + ", channelId=" + this.channelId + ", channelName=" + this.channelName + ", from=" + this.from + ", buySource=" + this.buySource + ", dataTagId=" + this.dataTagId + ", dataTagName=" + this.dataTagName + ", dataTagType=" + this.dataTagType + ", firstLabelLocationTagId=" + this.firstLabelLocationTagId + ", firstMultiLocationTagId=" + this.firstMultiLocationTagId + ", noLeftFragment=" + this.noLeftFragment + ", location4Playhistory=" + this.location4Playhistory + ", leftHasData=" + this.leftFragmentHasData + ", rightHasData=" + this.rightFragmentHasData + ", multiHasData=" + this.multiHasData + ", loadLimitSize=" + this.loadLimitSize + ", columnNum=" + this.selectColumn + ", selectRow=" + this.selectRow + ", searchModel=" + this.searchModel + AlbumEnterFactory.SIGN_STR;
    }

    public boolean isFirstContentUpdate() {
        return this.firstContentUpdate;
    }

    public void setFirstContentUpdate(boolean firstContentUpdate) {
        this.firstContentUpdate = firstContentUpdate;
    }
}
