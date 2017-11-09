package com.gitv.tvappstore.model;

import java.io.Serializable;

public class AppCategoryInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String cDes;
    private String cIconUrl;
    private int cId;
    private String cLevel = "";
    private String cName;
    private String cParentId;

    public int getId() {
        return this.cId;
    }

    public void setId(int id) {
        this.cId = id;
    }

    public String getParentId() {
        return this.cParentId;
    }

    public void setParentId(String cParentId) {
        this.cParentId = cParentId;
    }

    public String getLevel() {
        return this.cLevel;
    }

    public void setLevel(String cLevel) {
        this.cLevel = cLevel;
    }

    public String getName() {
        return this.cName;
    }

    public void setName(String cName) {
        this.cName = cName;
    }

    public String getIconUrl() {
        return this.cIconUrl;
    }

    public void setIconUrl(String cIconUrl) {
        this.cIconUrl = cIconUrl;
    }

    public String getDes() {
        return this.cDes;
    }

    public void setDes(String cDes) {
        this.cDes = cDes;
    }
}
