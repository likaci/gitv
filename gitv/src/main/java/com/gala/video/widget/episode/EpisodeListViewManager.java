package com.gala.video.widget.episode;

interface EpisodeListViewManager {
    public static final int CHILD_ITEM_COUNT_PER_PAGE = 10;
    public static final int LEFT_ARROW_ID = 101523045;
    public static final int PARENT_ITEM_COUNT_PER_PAGE = 5;
    public static final int RIGHT_ARROW_ID = 101523046;

    void setDataSource(int i, int i2);

    void setItemBackgroundResource(int i);
}
