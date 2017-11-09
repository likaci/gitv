package com.gala.video.app.epg.home.data.actionbar;

public class ActionBarItemInfo {
    private static final String TAG = "home/data/ActionBarItemInfo";
    private int id;
    private ActionBarType mAction;
    private int mIconWidthResId;
    private boolean mIsMy;
    private boolean mIsVip;
    private int mMessageBgDrawable;
    private String mName = "";
    private int mNormalIcon;

    public static class Builder {
        private int id;
        private ActionBarType mAction;
        private int mIconWidthResId;
        private boolean mIsMy;
        private boolean mIsVip;
        private int mMessageBgDrawable;
        private String mName;
        private int mNormalIcon;

        public Builder(String name, int icon, ActionBarType action) {
            this.mName = name;
            this.mNormalIcon = icon;
            this.mAction = action;
        }

        public Builder setIsVip() {
            this.mIsVip = true;
            return this;
        }

        public Builder setIsMy() {
            this.mIsMy = true;
            return this;
        }

        public Builder id(int id) {
            this.id = id;
            return this;
        }

        public Builder messageBgDrawable(int drawableId) {
            this.mMessageBgDrawable = drawableId;
            return this;
        }

        public Builder iconWith(int resDimenId) {
            this.mIconWidthResId = resDimenId;
            return this;
        }

        public ActionBarItemInfo build() {
            return new ActionBarItemInfo(this);
        }
    }

    public ActionBarItemInfo(Builder builder) {
        this.mName = builder.mName;
        this.mNormalIcon = builder.mNormalIcon;
        this.mIsVip = builder.mIsVip;
        this.mIsMy = builder.mIsMy;
        this.mAction = builder.mAction;
        this.id = builder.id;
        this.mMessageBgDrawable = builder.mMessageBgDrawable;
        this.mIconWidthResId = builder.mIconWidthResId;
    }

    public String getName() {
        return this.mName;
    }

    public int getIcon() {
        return this.mNormalIcon;
    }

    public boolean isVip() {
        return this.mIsVip;
    }

    public boolean isMy() {
        return this.mIsMy;
    }

    public ActionBarType getActionType() {
        return this.mAction;
    }

    public int getId() {
        return this.id;
    }

    public int getMessageBgDrawable() {
        return this.mMessageBgDrawable;
    }

    public int getIconWidth() {
        return this.mIconWidthResId;
    }

    public String toString() {
        return "ActionBarItemInfo{mName='" + this.mName + '\'' + ", mNormalIcon=" + this.mNormalIcon + ", mIsVip=" + this.mIsVip + ", mIsMy=" + this.mIsMy + ", id=" + this.id + ", mMessageBgDrawable=" + this.mMessageBgDrawable + ", mAction=" + this.mAction + '}';
    }
}
