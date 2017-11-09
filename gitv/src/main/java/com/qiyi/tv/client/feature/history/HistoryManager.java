package com.qiyi.tv.client.feature.history;

import com.qiyi.tv.client.Result;
import com.qiyi.tv.client.data.Media;
import java.util.List;

public interface HistoryManager {
    int clearAnonymousHistory();

    int clearHistory();

    int deleteAnonymousHistory(Media media);

    int deleteHistory(Media media);

    Result<List<Media>> getHistoryList(int i);

    Result<List<Media>> getHistoryList(int i, boolean z);

    boolean isRunning();

    void setOnHistoryChangedListener(OnHistoryChangedListener onHistoryChangedListener);

    void start();

    void stop();
}
