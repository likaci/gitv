package com.qiyi.tv.client.feature.history;

import com.qiyi.tv.client.data.Media;

public interface OnHistoryChangedListener {
    public static final int ADD_HISTORY_ACTION = 1;
    public static final int CLEAR_HISTORY_ACTION = 3;
    public static final int REMOVE_HISTORY_ACTION = 2;

    void onHistoryChanged(int i, Media media);
}
