package com.gitv.tvappstore.model;

import java.io.Serializable;

public class VersionInfo implements Serializable {
    private static final long serialVersionUID = 1;
    private String verAppDesc;
    private String verAppDownloadUrl;
    private String verAppHomepage;
    private String verAppLogoUrl;
    private String verAppNewFeature;
    private String verAppPackageName;
    private String verAppPackageSize;
    private String verAppTag;
    private String verAppTitle;
    private int verCode;
    private String verName;

    public String getVerName() {
        return this.verName;
    }

    public void setVerName(String verName) {
        this.verName = verName;
    }

    public int getVerCode() {
        return this.verCode;
    }

    public void setVerCode(int verCode) {
        this.verCode = verCode;
    }

    public String getVerAppTitle() {
        return this.verAppTitle;
    }

    public void setVerAppTitle(String verAppTitle) {
        this.verAppTitle = verAppTitle;
    }

    public String getVerAppDownloadUrl() {
        return this.verAppDownloadUrl;
    }

    public void setVerAppDownloadUrl(String verAppDownloadUrl) {
        this.verAppDownloadUrl = verAppDownloadUrl;
    }

    public String getVerAppLogoUrl() {
        return this.verAppLogoUrl;
    }

    public void setVerAppLogoUrl(String verAppLogoUrl) {
        this.verAppLogoUrl = verAppLogoUrl;
    }

    public String getVerAppNewFeature() {
        return this.verAppNewFeature;
    }

    public void setVerAppNewFeature(String verAppNewFeature) {
        this.verAppNewFeature = verAppNewFeature;
    }

    public String getVerAppPackageSize() {
        return this.verAppPackageSize;
    }

    public void setVerAppPackageSize(String verAppPackageSize) {
        this.verAppPackageSize = verAppPackageSize;
    }

    public String getVerAppPackageName() {
        return this.verAppPackageName;
    }

    public void setVerAppPackageName(String verAppPackageName) {
        this.verAppPackageName = verAppPackageName;
    }

    public String getVerAppDesc() {
        return this.verAppDesc;
    }

    public void setVerAppDesc(String verAppDesc) {
        this.verAppDesc = verAppDesc;
    }

    public String getVerAppHomepage() {
        return this.verAppHomepage;
    }

    public void setVerAppHomepage(String verAppHomepage) {
        this.verAppHomepage = verAppHomepage;
    }

    public String getVerAppTag() {
        return this.verAppTag;
    }

    public void setVerAppTag(String verAppTag) {
        this.verAppTag = verAppTag;
    }
}
