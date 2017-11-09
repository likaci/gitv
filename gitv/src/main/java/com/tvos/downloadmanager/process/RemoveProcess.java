package com.tvos.downloadmanager.process;

import android.util.Log;
import com.tvos.downloadmanager.data.DownloadParam;

public class RemoveProcess extends BaseProcess {
    private static final String TAG = "RemoveProcess";

    public boolean process(int id) {
        try {
            Log.d(TAG, "remove process id=" + id);
            if (getDownload() == null || getDownloadData() == null) {
                return false;
            }
            DownloadParam param = getDownloadData().getDownloadParm(id);
            if (param == null) {
                return false;
            }
            int status = param.getStatus();
            if (status == 4 || status == 5) {
                getDownload().remove(id);
            }
            getDownloadData().remove(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
