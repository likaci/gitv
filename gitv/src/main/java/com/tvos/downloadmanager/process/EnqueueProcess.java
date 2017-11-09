package com.tvos.downloadmanager.process;

import android.util.Log;
import com.tvos.apps.utils.MD5Utils;
import com.tvos.downloadmanager.data.DownloadParam;
import com.tvos.downloadmanager.data.RequestInfo;
import java.io.File;

public class EnqueueProcess extends BaseProcess {
    private static final String TAG = "EnqueueProcess";

    public boolean process(RequestInfo requestInfo) {
        if (requestInfo == null) {
            return false;
        }
        try {
            Log.d(TAG, "enqueue process uri=" + requestInfo.getUri());
            if (getDownload() == null || getDownloadData() == null) {
                return false;
            }
            DownloadParam param = getDownloadData().getDownloadParamByUrl(requestInfo.getUri());
            if (param == null) {
                param = getDownloadData().getDownloadParm(getDownloadData().insert(requestInfo));
            } else if (!(param.getStatus() == 2 || param.getStatus() == 0)) {
                if (param.getStatus() != 6) {
                    return true;
                }
                File file = new File(requestInfo.getDestination());
                if (file.exists()) {
                    if (MD5Utils.verifyFileByMd5(requestInfo.getDestination(), requestInfo.getMd5())) {
                        Log.d(TAG, "MD5Util.checkMd5");
                        getDownloadStatusListener().onComplete(param.getId());
                        getDownloadData().remove(param.getId());
                        return true;
                    }
                    Log.d(TAG, "delete file = " + file.getAbsolutePath() + ", condition 1");
                    file.delete();
                }
                getDownloadData().remove(param.getId());
                param = getDownloadData().getDownloadParm(getDownloadData().insert(requestInfo));
            }
            if (getDownload().isFull()) {
                param.setStatus(1);
                getDownloadData().updateDownloadParm(param);
                getDownloadStatusListener().onWait(param.getId());
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
