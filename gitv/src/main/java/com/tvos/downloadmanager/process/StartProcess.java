package com.tvos.downloadmanager.process;

import android.util.Log;
import com.tvos.downloadmanager.data.DownloadParam;

public class StartProcess extends BaseProcess {
    private static final String TAG = "StartProcess";

    public boolean process(int id, int speedLimitDegree) {
        try {
            Log.d(TAG, "start process id=" + id);
            if (getDownload() == null || getDownloadData() == null) {
                return false;
            }
            DownloadParam param = getDownloadData().getDownloadParm(id);
            if (param == null) {
                return false;
            }
            if (param.getStatus() == 4) {
                Log.d(TAG, " DOWNLOAD_STATUS_STARTTED ");
                return true;
            }
            getDownloadData().removeDownloadFile(id);
            getDownloadData().resetDownloadRecord(id);
            DownloadParam downloadParam = getDownloadData().getDownloadParm(id);
            downloadParam.setSpeedLimitDegree(speedLimitDegree);
            if (getDownload().isFull()) {
                downloadParam.setStatus(1);
                getDownloadData().updateDownloadParm(downloadParam);
                getDownloadStatusListener().onWait(id);
            } else {
                getDownload().start(downloadParam);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
