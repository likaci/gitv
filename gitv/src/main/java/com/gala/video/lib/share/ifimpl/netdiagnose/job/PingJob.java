package com.gala.video.lib.share.ifimpl.netdiagnose.job;

import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJobListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.PingConfig;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PingJob extends NetDiagnoseJob {
    private final String TAG;
    private StringBuilder mPingResult;
    private String[] mPingUrl;

    public PingJob(NetDiagnoseInfo data) {
        super(data);
        this.TAG = "NetDiagnoseJob/PingJob@" + hashCode();
    }

    public PingJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener) {
        super(data, listener);
        this.TAG = "NetDiagnoseJob/PingJob@" + hashCode();
    }

    public PingJob(NetDiagnoseInfo data, String[] pingUrl) {
        this(data);
        this.mPingUrl = pingUrl;
    }

    public void setPingUrl(String[] pingUrl) {
        this.mPingUrl = pingUrl;
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        LogUtils.d(this.TAG, ">> onRun");
        this.mPingResult = new StringBuilder();
        if (StringUtils.isEmpty(this.mPingUrl)) {
            pingStr(PingConfig.DATA2_ITV);
            pingStr(PingConfig.CACHE_VIDEO);
            pingStr(PingConfig.CACHE_M);
            pingStr(PingConfig.PDATA_VIDEO);
            pingStr(PingConfig.ITV_VIDEO);
        } else {
            pingUrls(this.mPingUrl);
        }
        ((NetDiagnoseInfo) getData()).setPingResult(this.mPingResult.toString());
        this.mIsJobComplete = true;
        notifyJobSuccess(controller);
        LogUtils.d(this.TAG, "<< onRun");
    }

    public void pingUrls(String... urls) {
        for (String url : urls) {
            pingStr(url);
        }
    }

    private void pingStr(String str) {
        try {
            String line;
            Process p = Runtime.getRuntime().exec("ping -c 2 -w 10 " + str);
            int status = p.waitFor();
            InputStream input = p.getInputStream();
            InputStream errorInput = p.getErrorStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            BufferedReader errorIn = new BufferedReader(new InputStreamReader(errorInput));
            StringBuffer buffer = new StringBuffer();
            StringBuffer errorBuffer = new StringBuffer();
            while (true) {
                line = in.readLine();
                if (line != null) {
                    buffer.append(line).append("\n");
                } else {
                    while (true) {
                    }
                    this.mPingResult.append("\r\n------- Ping Test-------\r\n").append("\n @@@@ping " + str).append("\n normalbuffer = " + buffer.toString()).append("\n errorBuffer = " + errorBuffer.toString()).append("\n status = " + status).append("\n result = " + (status != 0 ? "success" : "failed") + "\n").append("\r\n-------end--------\r\n");
                }
            }
            line = errorIn.readLine();
            if (line != null) {
                errorBuffer.append(line).append("\n");
            } else {
                if (status != 0) {
                }
                this.mPingResult.append("\r\n------- Ping Test-------\r\n").append("\n @@@@ping " + str).append("\n normalbuffer = " + buffer.toString()).append("\n errorBuffer = " + errorBuffer.toString()).append("\n status = " + status).append("\n result = " + (status != 0 ? "success" : "failed") + "\n").append("\r\n-------end--------\r\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }
}
