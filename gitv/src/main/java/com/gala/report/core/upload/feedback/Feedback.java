package com.gala.report.core.upload.feedback;

public class Feedback {
    private String mAppVersion;
    private String mBrief;
    private String mCategoryId;
    private String mDetails;
    private String mEntryClass;
    private String mFbClass;
    private String mFlashVersion;
    private boolean mIsPlayingF4v;
    private String mRecord;
    private String mTitle;
    private String mVid;

    public Feedback(FeedbackType fb_class, FeedbackEntry entry_class, String fbbrief, String details, String record, String version) {
        boolean z = true;
        this.mFbClass = FeedbackType.COMMON.toString();
        this.mEntryClass = FeedbackEntry.DEFAULT.toString();
        this.mBrief = "";
        this.mDetails = "";
        this.mRecord = "";
        this.mAppVersion = "";
        this.mTitle = "";
        this.mVid = "";
        this.mCategoryId = "";
        this.mFlashVersion = "";
        this.mIsPlayingF4v = true;
        this.mEntryClass = entry_class.toString();
        this.mFbClass = fb_class.toString();
        setBrief(fbbrief);
        setDetails(details);
        setRecord(record);
        this.mAppVersion = version;
        if (entry_class == FeedbackEntry.SYSTEM_PLAYER) {
            z = false;
        }
        this.mIsPlayingF4v = z;
    }

    public void setBrief(String brief) {
        this.mBrief = brief;
    }

    public void setDetails(String details) {
        this.mDetails = details;
    }

    public void setRecord(String Record) {
        this.mRecord = Record;
    }

    public Feedback(FeedbackType fb_class, FeedbackEntry entry_class, String version) {
        this(fb_class, entry_class, "", "", "", version);
    }

    public Feedback(FeedbackType fb_class, FeedbackEntry entry_class, String fbbrief, String record, String version) {
        this(fb_class, entry_class, fbbrief, "", record, version);
    }

    public Feedback(FeedbackType fb_class, FeedbackEntry entry_class, String fbbrief, String version) {
        this(fb_class, entry_class, fbbrief, "", "", version);
    }

    public Feedback setAlbumInfo(String albumName, String vid, String categoryId) {
        if (vid == null) {
            vid = this.mVid;
        }
        this.mVid = vid;
        if (albumName == null) {
            albumName = this.mTitle;
        }
        this.mTitle = albumName;
        if (categoryId == null) {
            categoryId = this.mCategoryId;
        }
        this.mCategoryId = categoryId;
        return this;
    }

    public boolean isPlayingF4v() {
        return this.mIsPlayingF4v;
    }

    public String getEntryClass() {
        return this.mEntryClass;
    }

    public String getFbClass() {
        return this.mFbClass;
    }

    public String getBrief() {
        return this.mBrief;
    }

    public String getDetails() {
        return this.mDetails;
    }

    public String getPlayerVersion() {
        return this.mAppVersion;
    }

    public String getVid() {
        return this.mVid;
    }

    public String getTitle() {
        return this.mTitle;
    }

    public String getCategoryId() {
        return this.mCategoryId;
    }

    public String getRecord() {
        return this.mRecord;
    }

    public String getFlashVersion() {
        return this.mFlashVersion;
    }

    public void setFlashVersion(String mFlashVersion) {
        this.mFlashVersion = mFlashVersion;
    }
}
