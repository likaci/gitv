package com.gala.video.lib.share.ifimpl.netdiagnose.job;

import android.util.Log;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobControllerHolder;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJobListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.framework.coreservice.netdiagnose.traceroute.TracerouteContainer;
import com.gala.video.lib.framework.coreservice.netdiagnose.traceroute.TracerouteStateListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.traceroute.TracerouteTask;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import com.gala.video.lib.share.ifimpl.netdiagnose.collection.PingConfig;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.cybergarage.http.HTTP;

public class TracerouteJob extends NetDiagnoseJob {
    private static final long sTime = 60000;
    private final String TAG;
    private StringBuilder mTraceResult;
    private String mTraceRouteUrls;
    private TracerouteTask mTracerouteTask;

    class C17021 extends TimerTask {
        C17021() {
        }

        public void run() {
            Log.d(TracerouteJob.this.TAG, "60s later, is Task Running:" + TracerouteJob.this.mTracerouteTask.isRunning());
            if (TracerouteJob.this.mTracerouteTask.isRunning()) {
                TracerouteJob.this.mTracerouteTask.cancel();
            }
        }
    }

    private class Callback extends JobControllerHolder implements TracerouteStateListener {
        public Callback(JobController controller) {
            super(controller);
        }

        public void onTraceSuccess(String domain, String ipToPing, List<TracerouteContainer> traceList) {
            TracerouteJob.this.mTraceResult.append("Trace domain : " + domain + ", IP:" + ipToPing);
            TracerouteJob.this.mTraceResult.append(HTTP.CRLF);
            TracerouteJob.this.mTraceResult.append(TracerouteJob.convertTraceList(traceList));
        }

        public void onTraceFailed(String domain, String msg) {
            TracerouteJob.this.mTraceResult.append("Trace domain " + domain + " failed: " + msg);
        }
    }

    public TracerouteJob(NetDiagnoseInfo data) {
        super(data);
        this.TAG = "NetDiagnoseJob/TracerouteJob@" + hashCode();
    }

    public TracerouteJob(NetDiagnoseInfo data, NetDiagnoseJobListener listener) {
        super(data, listener);
        this.TAG = "NetDiagnoseJob/TracerouteJob@" + hashCode();
    }

    public TracerouteJob(NetDiagnoseInfo data, String traceRouteUrls) {
        this(data);
        this.mTraceRouteUrls = traceRouteUrls;
    }

    public void onRun(JobController controller) {
        super.onRun(controller);
        LogUtils.m1568d(this.TAG, ">> onRun");
        this.mTraceResult = new StringBuilder();
        LogUtils.m1568d(this.TAG, "onRun tracertDomains: " + this.mTraceRouteUrls);
        if (StringUtils.isEmpty(this.mTraceRouteUrls)) {
            checkDefaultTraceRoute(controller);
        } else if (NetDiagnoseCheckTools.NO_CHECK_FLAG.equals(this.mTraceRouteUrls.trim())) {
            this.mTraceResult.append("--------no traceroute job-------\r\n");
        } else {
            String[] urls = NetDiagnoseCheckTools.getParseUrls(this.mTraceRouteUrls);
            if (StringUtils.isEmpty(urls)) {
                checkDefaultTraceRoute(controller);
            } else {
                LogUtils.m1574i(this.TAG, "onRun: use online traceroute domain");
                for (CharSequence url : urls) {
                    if (!StringUtils.isEmpty(url)) {
                        LogUtils.m1574i(this.TAG, "traceroute url: " + url);
                        startTraceRoute(url.trim(), new Callback(controller));
                    }
                }
            }
        }
        ((NetDiagnoseInfo) getData()).setTracerouteResult(this.mTraceResult.toString());
        this.mIsJobComplete = true;
        notifyJobSuccess(controller);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "<< onRun");
        }
    }

    private void checkDefaultTraceRoute(JobController controller) {
        startTraceRoute(PingConfig.DATA2_ITV_PTQY_GITV_TV, new Callback(controller));
        startTraceRoute(PingConfig.DATA_VIDEO_PTQY_GITV_TV, new Callback(controller));
    }

    public void startTraceRoute(String domain, TracerouteStateListener listener) {
        LogUtils.m1568d(this.TAG, "start traceroute:" + domain);
        this.mTracerouteTask = new TracerouteTask(64, domain, listener);
        new Timer().schedule(new C17021(), sTime);
        this.mTracerouteTask.initParams();
        this.mTracerouteTask.startTraceroute();
    }

    private static String convertTraceList(List<TracerouteContainer> traceList) {
        if (traceList == null || traceList.size() == 0) {
            return "trace list empty";
        }
        StringBuilder sb = new StringBuilder();
        for (TracerouteContainer container : traceList) {
            sb.append("IP:" + container.getIp());
            sb.append("   ElapsedTime:" + container.getElapsedTime());
            sb.append(HTTP.CRLF);
        }
        return sb.toString();
    }
}
