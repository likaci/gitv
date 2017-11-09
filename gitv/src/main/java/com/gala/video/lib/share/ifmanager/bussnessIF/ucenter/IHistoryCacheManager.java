package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter;

import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.HistoryInfo;
import com.gala.video.lib.share.ifmanager.bussnessIF.ucenter.history.IHistoryResultCallBack;
import com.gala.video.lib.share.utils.TraceEx;
import java.util.List;

public interface IHistoryCacheManager extends IInterfaceWrapper {

    public static abstract class Wrapper implements IHistoryCacheManager {
        public Object getInterface() {
            return this;
        }

        public static IHistoryCacheManager asInterface(Object wrapper) {
            TraceEx.beginSection("IGalaAccountManager.asInterface");
            if (wrapper == null || !(wrapper instanceof IHistoryCacheManager)) {
                TraceEx.endSection();
                return null;
            }
            TraceEx.endSection();
            return (IHistoryCacheManager) wrapper;
        }
    }

    void clear();

    void clearLoginUserDb();

    void deleteHistory(String str, String str2);

    HistoryInfo getAlbumHistory(String str);

    List<HistoryInfo> getLatestLongVideoHistory(int i);

    List<HistoryInfo> getLatestVideoHistory(int i);

    HistoryInfo getTvHistory(String str);

    void loadHistoryList(int i, int i2, int i3, IHistoryResultCallBack iHistoryResultCallBack);

    void mergeDeviceAndCloudHistory();

    void synchronizeHistoryListForNoLogin();

    void synchronizeHistoryListFromCloud();

    void synchronizeHistoryListFromCloudDelay();

    void uploadHistory(HistoryInfo historyInfo);
}
