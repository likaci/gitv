package com.qiyi.tv.client.data;

import android.os.Bundle;
import android.os.Parcelable;
import java.io.Serializable;

public abstract class Media implements Parcelable, Serializable {
    private static final String KEY_CORNER_HINT = "media.corner_hint";
    private static final String KEY_FROM_SDK = "media.from_sdk";
    private static final String KEY_HOMEPAGE_TITLE = "media.itemkvs.homepage_title";
    private static final String KEY_ID = "media.id";
    private static final String KEY_ITEM_PROMPT = "media.item_prompt";
    private static final String KEY_NAME = "media.name";
    private static final String KEY_PICURL = "media.picurl";
    private static final String KEY_PLAY_COUNT = "media.play_count";
    private static final String KEY_SCORE = "media.score";
    private static final String KEY_SOURCE_CODE = "media.source_code";
    private static final String KEY_TITLE = "media.title";
    private static final String KEY_TYPE = "media.type";
    private static final String KEY_USER_TAGS = "media.user_tags";
    public static final int TYPE_ALBUM = 2;
    public static final int TYPE_PLAYLIST = 3;
    public static final int TYPE_VIDEO = 1;
    private static final long serialVersionUID = 1;
    private int mCornerHint;
    private boolean mFromSdk = true;
    private String mId;
    private String mItemPrompt;
    private String mName;
    private String mPicUrl;
    private String mPlayCount;
    private String mScore;
    private String mSourceCode;
    private String mTitle;
    private int mType;
    private final UserTags mUserTags = new UserTags();

    public Media(int type) {
        this.mType = type;
    }

    public int getType() {
        return this.mType;
    }

    public String getId() {
        return this.mId;
    }

    public String getName() {
        return this.mName;
    }

    public Picture getImage() {
        return new Picture(this.mPicUrl);
    }

    public String getPicUrl() {
        return this.mPicUrl;
    }

    public UserTags getUserTags() {
        return this.mUserTags;
    }

    public int getCornerHint() {
        return this.mCornerHint;
    }

    public String getSourceCode() {
        return this.mSourceCode;
    }

    public boolean fromSdk() {
        return this.mFromSdk;
    }

    public String getItemPrompt() {
        return this.mItemPrompt;
    }

    public void setCornerHint(int hint) {
        this.mCornerHint = hint;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setPicUrl(String url) {
        this.mPicUrl = url;
    }

    public void setSourceCode(String sourceCode) {
        this.mSourceCode = sourceCode;
    }

    public void setFromSdk(boolean fromSdk) {
        this.mFromSdk = fromSdk;
    }

    public void setItemPrompt(String itemPrompt) {
        this.mItemPrompt = itemPrompt;
    }

    public String getScore() {
        return this.mScore;
    }

    public void setScore(String score) {
        this.mScore = score;
    }

    public String getPlayCount() {
        return this.mPlayCount;
    }

    public void setPlayCount(String playCount) {
        this.mPlayCount = playCount;
    }

    @Deprecated
    public String getHomePageTitle() {
        return getTitle();
    }

    @Deprecated
    public void setHomePageTitle(String mHomePageTitle) {
        setTitle(mHomePageTitle);
    }

    public String getTitle() {
        return this.mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    protected void writeBundle(Bundle bundle) {
        bundle.putInt(KEY_TYPE, this.mType);
        bundle.putString(KEY_ID, this.mId);
        bundle.putString(KEY_NAME, this.mName);
        bundle.putString(KEY_PICURL, this.mPicUrl);
        bundle.putParcelable(KEY_USER_TAGS, this.mUserTags);
        bundle.putInt(KEY_CORNER_HINT, this.mCornerHint);
        bundle.putBoolean(KEY_FROM_SDK, this.mFromSdk);
        bundle.putString(KEY_ITEM_PROMPT, this.mItemPrompt);
        bundle.putString(KEY_SOURCE_CODE, this.mSourceCode);
        bundle.putString(KEY_PLAY_COUNT, this.mPlayCount);
        bundle.putString(KEY_SCORE, this.mScore);
        bundle.putString(KEY_TITLE, this.mTitle);
    }

    protected void readBundle(Bundle bundle) {
        this.mType = bundle.getInt(KEY_TYPE);
        this.mId = bundle.getString(KEY_ID);
        this.mName = bundle.getString(KEY_NAME);
        this.mPicUrl = bundle.getString(KEY_PICURL);
        this.mUserTags.copy((UserTags) bundle.getParcelable(KEY_USER_TAGS));
        this.mCornerHint = bundle.getInt(KEY_CORNER_HINT);
        this.mFromSdk = bundle.getBoolean(KEY_FROM_SDK);
        this.mItemPrompt = bundle.getString(KEY_ITEM_PROMPT);
        this.mSourceCode = bundle.getString(KEY_SOURCE_CODE);
        this.mPlayCount = bundle.getString(KEY_PLAY_COUNT);
        this.mScore = bundle.getString(KEY_SCORE);
        this.mTitle = bundle.getString(KEY_TITLE);
    }
}
