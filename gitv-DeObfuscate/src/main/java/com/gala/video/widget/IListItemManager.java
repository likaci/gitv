package com.gala.video.widget;

public interface IListItemManager {
    public static final int CONTENT_ID = 4098;
    public static final int IMAGE_ID = 4096;
    public static final int LAYOUT_ID = 4099;
    public static final int TITLE_ID = 4097;

    void setBgBackgroundResource(int i);

    void setContent(CharSequence charSequence);

    void setContentColor(int i);

    void setContentSize(float f);

    void setImage(int i);

    void setTitle(CharSequence charSequence);

    void setTitleColor(int i);

    void setTitleSize(float f);
}
