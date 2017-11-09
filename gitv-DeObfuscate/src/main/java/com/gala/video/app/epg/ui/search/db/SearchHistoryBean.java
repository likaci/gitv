package com.gala.video.app.epg.ui.search.db;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class SearchHistoryBean {
    private int id;
    private String keyword;
    private String qpid;
    private String type;

    SearchHistoryBean() {
    }

    public SearchHistoryBean(String keyword) {
        this.keyword = keyword;
    }

    public SearchHistoryBean(String keyword, String qpid) {
        this.keyword = keyword;
        setQpid(qpid);
    }

    public SearchHistoryBean(String keyword, String qpid, String type) {
        this.keyword = keyword;
        setQpid(qpid);
        this.type = type;
    }

    public SearchHistoryBean(int id, String keyword, String qpid, String type) {
        this.id = id;
        this.qpid = qpid;
        this.keyword = keyword;
        this.type = type;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKeyword() {
        return this.keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getQpid() {
        return this.qpid;
    }

    public void setQpid(String qpid) {
        this.qpid = qpid;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toString() {
        return "QSearchHistoryBean [id=" + this.id + ", qpid=" + this.qpid + ", keyword=" + this.keyword + ", type=" + this.type + AlbumEnterFactory.SIGN_STR;
    }
}
