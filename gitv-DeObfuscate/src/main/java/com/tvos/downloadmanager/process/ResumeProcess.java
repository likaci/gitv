package com.tvos.downloadmanager.process;

import android.util.Log;
import com.tvos.downloadmanager.data.DownloadParam;
import java.io.File;

public class ResumeProcess extends BaseProcess {
    private static final String TAG = "ResumeProcess";

    public boolean process(int id, int speedLimitDegree) {
        try {
            Log.d(TAG, "resume process id=" + id);
            if (getDownload() == null && getDownloadData() == null) {
                return false;
            }
            DownloadParam param = getDownloadData().getDownloadParm(id);
            if (param == null || param.getStatus() != 2) {
                return false;
            }
            boolean targetFileExist = false;
            String filePath = param.getDestination();
            if (param.isP2PDownload()) {
                File mFile1 = new File(new StringBuilder(String.valueOf(filePath)).append(".pmv").toString());
                File mFile2 = new File(new StringBuilder(String.valueOf(filePath)).append(".ctp").toString());
                if (mFile1.exists() || mFile2.exists()) {
                    targetFileExist = !param.isP2pDownloadError();
                }
            } else if (new File(filePath).exists()) {
                targetFileExist = true;
            }
            if (!targetFileExist) {
                Log.d(TAG, new StringBuilder(String.valueOf(filePath)).append(" not exists").toString());
                getDownloadData().removeDownloadFile(id);
                getDownloadData().resetDownloadRecord(id);
                param = getDownloadData().getDownloadParm(id);
            }
            param.setSpeedLimitDegree(speedLimitDegree);
            if (getDownload().isFull()) {
                param.setStatus(1);
                getDownloadData().updateDownloadParm(param);
                getDownloadStatusListener().onWait(id);
            } else {
                getDownload().start(param);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
