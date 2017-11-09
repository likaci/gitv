package com.tvos.downloadmanager.process;

import android.util.Log;
import com.tvos.downloadmanager.data.DownloadParam;

public class PauseProcess extends BaseProcess {
    private static final String TAG = "PauseProcess";

    public boolean process(int id) {
        try {
            Log.d(TAG, "pause process id=" + id);
            if (getDownloadData() == null || getDownload() == null) {
                return false;
            }
            DownloadParam param = getDownloadData().getDownloadParm(id);
            if (param == null) {
                return false;
            }
            int status = param.getStatus();
            if (status == 4 || status == 5) {
                getDownload().stop(id);
            } else if (status != 1) {
                return true;
            } else {
                if (param.getDownloadSize() > 0) {
                    param.setStatus(2);
                    getDownloadData().updateDownloadParm(param);
                    getDownloadStatusListener().onPaused(id);
                } else {
                    param.setStatus(0);
                    getDownloadData().updateDownloadParm(param);
                    getDownloadStatusListener().onStop(id);
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
