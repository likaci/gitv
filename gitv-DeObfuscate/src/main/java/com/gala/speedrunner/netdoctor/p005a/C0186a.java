package com.gala.speedrunner.netdoctor.p005a;

import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.gala.speedrunner.model.SpeedRunnerResult;
import com.netdoc.FileType;
import com.netdoc.LiveTaskInfo;
import com.netdoc.NetDocConnector;
import com.netdoc.NetDocListenerInterface;
import com.netdoc.PlatformType;
import com.netdoc.StepType;
import com.netdoc.TaskInfo;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public final class C0186a {
    private static final C0186a f757a = new C0186a();
    private final NetDocConnector f758a = new NetDocConnector();
    private final Map<String, C0187b> f759a = new ConcurrentHashMap();
    private short f760a = (short) 0;
    private boolean f761a = false;

    class C01851 implements NetDocListenerInterface {
        private /* synthetic */ C0186a f756a;

        C01851(C0186a c0186a) {
            this.f756a = c0186a;
        }

        public final void onDownloadProgress(String vid, int percent, int spdsec) {
            Log.d("SpeedRunner", "------onDownloadProgres------" + vid + "---" + percent + "---" + spdsec);
            synchronized (this.f756a.f759a) {
                if (this.f756a.f759a.size() > 0) {
                    Log.d("SpeedRunner", "------onDownloadProgres------callback list size=" + this.f756a.f759a.size());
                    for (Entry key : this.f756a.f759a.entrySet()) {
                        C0187b c0187b = (C0187b) this.f756a.f759a.get(key.getKey());
                        if (!(c0187b == null || c0187b.f762a == null)) {
                            c0187b.f762a.onDownloadProgress(vid, percent, spdsec);
                        }
                    }
                }
            }
        }

        public final void onSendlogResult(int recode) {
            Log.d("SpeedRunner", "------onSendlogResult------" + recode);
            synchronized (this.f756a.f759a) {
                if (this.f756a.f759a.size() > 0) {
                    Log.d("SpeedRunner", "------onSendlogResult------callback list size=" + this.f756a.f759a.size());
                    for (Entry key : this.f756a.f759a.entrySet()) {
                        C0187b c0187b = (C0187b) this.f756a.f759a.get(key.getKey());
                        if (!(c0187b == null || c0187b.f762a == null)) {
                            c0187b.f762a.onSendLogResult(recode);
                        }
                    }
                }
            }
        }

        public final void onTestResult(String vid, String result) {
            Log.d("SpeedRunner", "------onTestResult------" + vid + "---" + result);
            if (!(result == null || result.equals(""))) {
                SpeedRunnerResult speedRunnerResult = (SpeedRunnerResult) JSON.parseObject(result, SpeedRunnerResult.class);
                if (!(speedRunnerResult == null || speedRunnerResult.getPlayResult() == null)) {
                    int i = speedRunnerResult.getPlayResult().step;
                    if (i == StepType.COMPLETE.ordinal()) {
                        synchronized (this.f756a.f759a) {
                            if (this.f756a.f759a != null && this.f756a.f759a.size() > 0) {
                                Log.d("SpeedRunner", "------onTestResult------callback list size=" + this.f756a.f759a.size());
                                for (Entry key : this.f756a.f759a.entrySet()) {
                                    C0187b c0187b = (C0187b) this.f756a.f759a.get(key.getKey());
                                    if (!(c0187b == null || c0187b.f762a == null)) {
                                        c0187b.f762a.onSuccess(i, speedRunnerResult.getPlayResult().getCache_Status().avg_speed, result);
                                    }
                                }
                            }
                        }
                        return;
                    }
                }
            }
            synchronized (this.f756a.f759a) {
                if (this.f756a.f759a != null && this.f756a.f759a.size() > 0) {
                    Log.d("SpeedRunner", "------onTestResult------callback list size=" + this.f756a.f759a.size());
                    for (Entry key2 : this.f756a.f759a.entrySet()) {
                        C0187b c0187b2 = (C0187b) this.f756a.f759a.get(key2.getKey());
                        if (!(c0187b2 == null || c0187b2.f762a == null)) {
                            c0187b2.f762a.onFailed(result);
                        }
                    }
                }
            }
        }

        public final void onTestState(String vid, int step) {
            Log.d("SpeedRunner", "------onTestState------" + vid + "---" + step);
            synchronized (this.f756a.f759a) {
                if (this.f756a.f759a != null && this.f756a.f759a.size() > 0) {
                    Log.d("SpeedRunner", "------onTestState------callback list size=" + this.f756a.f759a.size());
                    for (Entry key : this.f756a.f759a.entrySet()) {
                        C0187b c0187b = (C0187b) this.f756a.f759a.get(key.getKey());
                        if (!(c0187b == null || c0187b.f762a == null)) {
                            c0187b.f762a.onReportStatus(vid, step);
                        }
                    }
                }
            }
        }
    }

    private C0186a() {
    }

    public static C0186a m492a() {
        return f757a;
    }

    public final void m499a(String str, String str2) {
        if (!this.f761a) {
            this.f758a.initNetDoctor(str, PlatformType.TYPE_TV.ordinal(), str2);
            this.f758a.setListener(new C01851(this));
            this.f761a = true;
        }
    }

    public final void m498a(String str) {
        this.f758a.sendLogInfo(str);
    }

    public final short m494a() {
        short s = (short) (this.f760a + 1);
        this.f760a = s;
        return s;
    }

    public final void m495a(C0187b c0187b) {
        synchronized (this.f759a) {
            this.f759a.put(String.valueOf(c0187b.f764a), c0187b);
        }
    }

    public final void m497a(TaskInfo taskInfo, FileType fileType, String str) {
        if (fileType.ordinal() == FileType.TYPE_F4V.ordinal()) {
            this.f758a.checkPlay(fileType.ordinal(), taskInfo, "");
        } else if (fileType.ordinal() == FileType.TYPE_HLS.ordinal()) {
            this.f758a.checkPlay(fileType.ordinal(), taskInfo, str);
        }
    }

    public final void m496a(LiveTaskInfo liveTaskInfo, FileType fileType) {
        this.f758a.checkLivePlay(fileType.ordinal(), liveTaskInfo, "");
    }

    public final void m500b(C0187b c0187b) {
        if (c0187b != null) {
            if (c0187b.f765a) {
                Log.d("SpeedRunner", "------stop------channelid=" + c0187b.f766b + "-id=" + c0187b.f764a);
                this.f758a.stopLivePlay(c0187b.f766b);
            } else {
                Log.d("SpeedRunner", "------stop------vid=" + c0187b.f763a + "-id=" + c0187b.f764a);
                this.f758a.stopPlay(c0187b.f763a);
            }
            synchronized (this.f759a) {
                for (Entry entry : this.f759a.entrySet()) {
                    if (((String) entry.getKey()).equals(String.valueOf(c0187b.f764a))) {
                        this.f759a.remove(entry.getKey());
                        break;
                    }
                }
            }
        }
    }
}
