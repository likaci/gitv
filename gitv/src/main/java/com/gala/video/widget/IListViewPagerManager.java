package com.gala.video.widget;

import java.util.List;

public interface IListViewPagerManager<T> {
    public static final int DEFAULT_ANIMATION = 1;
    public static final int DEFAULT_SCROLL = 0;
    public static final int LIST_ITEM_VISIBLE = 4;
    public static final int LIST_SMOOTH_SCROLL_DURATION = 500;
    public static final int NONE_ANIMATION = 0;
    public static final int SCALE_ANIMATION = 2;
    public static final int SMOOTH_SCROLL = 1;
    public static final float ZOOM_DEFAULT_MULTIPLE = 1.0f;
    public static final int ZOOM_IN_DURATION = 300;
    public static final float ZOOM_IN_MULTIPLE = 1.1f;
    public static final int ZOOM_OUT_DURATION = 150;
    public static final float ZOOM_OUT_MULTIPLE = 1.1f;

    void setDataSource(List<T> list, Class<?> cls);

    void setScroll(int i);

    void setZoomInBg(int i);

    void setZoomOutBg(int i);
}
