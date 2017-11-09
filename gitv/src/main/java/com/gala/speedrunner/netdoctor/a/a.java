package com.gala.speedrunner.netdoctor.a;

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

public final class a {
    private static final a a = new a();
    private final NetDocConnector f384a = new NetDocConnector();
    private final Map<String, b> f385a = new ConcurrentHashMap();
    private short f386a = (short) 0;
    private boolean f387a = false;

    private a() {
    }

    public static a a() {
        return a;
    }

    public final void a(String str, String str2) {
        if (!this.f387a) {
            this.f384a.initNetDoctor(str, PlatformType.TYPE_TV.ordinal(), str2);
            this.f384a.setListener(new NetDocListenerInterface(this) {
                private /* synthetic */ a a;

                {
                    this.a = r1;
                }

                public final void onDownloadProgress(String vid, int percent, int spdsec) {
                    Log.d("SpeedRunner", "------onDownloadProgres------" + vid + "---" + percent + "---" + spdsec);
                    synchronized (this.a.f385a) {
                        if (this.a.f385a.size() > 0) {
                            Log.d("SpeedRunner", "------onDownloadProgres------callback list size=" + this.a.f385a.size());
                            for (Entry key : this.a.f385a.entrySet()) {
                                b bVar = (b) this.a.f385a.get(key.getKey());
                                if (!(bVar == null || bVar.a == null)) {
                                    bVar.a.onDownloadProgress(vid, percent, spdsec);
                                }
                            }
                        }
                    }
                }

                public final void onSendlogResult(int recode) {
                    Log.d("SpeedRunner", "------onSendlogResult------" + recode);
                    synchronized (this.a.f385a) {
                        if (this.a.f385a.size() > 0) {
                            Log.d("SpeedRunner", "------onSendlogResult------callback list size=" + this.a.f385a.size());
                            for (Entry key : this.a.f385a.entrySet()) {
                                b bVar = (b) this.a.f385a.get(key.getKey());
                                if (!(bVar == null || bVar.a == null)) {
                                    bVar.a.onSendLogResult(recode);
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
                                synchronized (this.a.f385a) {
                                    if (this.a.f385a != null && this.a.f385a.size() > 0) {
                                        Log.d("SpeedRunner", "------onTestResult------callback list size=" + this.a.f385a.size());
                                        for (Entry key : this.a.f385a.entrySet()) {
                                            b bVar = (b) this.a.f385a.get(key.getKey());
                                            if (!(bVar == null || bVar.a == null)) {
                                                bVar.a.onSuccess(i, speedRunnerResult.getPlayResult().getCache_Status().avg_speed, result);
                                            }
                                        }
                                    }
                                }
                                return;
                            }
                        }
                    }
                    synchronized (this.a.f385a) {
                        if (this.a.f385a != null && this.a.f385a.size() > 0) {
                            Log.d("SpeedRunner", "------onTestResult------callback list size=" + this.a.f385a.size());
                            for (Entry key2 : this.a.f385a.entrySet()) {
                                b bVar2 = (b) this.a.f385a.get(key2.getKey());
                                if (!(bVar2 == null || bVar2.a == null)) {
                                    bVar2.a.onFailed(result);
                                }
                            }
                        }
                    }
                }

                public final void onTestState(String vid, int step) {
                    Log.d("SpeedRunner", "------onTestState------" + vid + "---" + step);
                    synchronized (this.a.f385a) {
                        if (this.a.f385a != null && this.a.f385a.size() > 0) {
                            Log.d("SpeedRunner", "------onTestState------callback list size=" + this.a.f385a.size());
                            for (Entry key : this.a.f385a.entrySet()) {
                                b bVar = (b) this.a.f385a.get(key.getKey());
                                if (!(bVar == null || bVar.a == null)) {
                                    bVar.a.onReportStatus(vid, step);
                                }
                            }
                        }
                    }
                }
            });
            this.f387a = true;
        }
    }

    public final void a(String str) {
        this.f384a.sendLogInfo(str);
    }

    public final short m71a() {
        short s = (short) (this.f386a + 1);
        this.f386a = s;
        return s;
    }

    public final void a(b bVar) {
        synchronized (this.f385a) {
            this.f385a.put(String.valueOf(bVar.f389a), bVar);
        }
    }

    public final void a(TaskInfo taskInfo, FileType fileType, String str) {
        if (fileType.ordinal() == FileType.TYPE_F4V.ordinal()) {
            this.f384a.checkPlay(fileType.ordinal(), taskInfo, "");
        } else if (fileType.ordinal() == FileType.TYPE_HLS.ordinal()) {
            this.f384a.checkPlay(fileType.ordinal(), taskInfo, str);
        }
    }

    public final void a(LiveTaskInfo liveTaskInfo, FileType fileType) {
        this.f384a.checkLivePlay(fileType.ordinal(), liveTaskInfo, "");
    }

    public final void b(b bVar) {
        if (bVar != null) {
            if (bVar.f390a) {
                Log.d("SpeedRunner", "------stop------channelid=" + bVar.b + "-id=" + bVar.f389a);
                this.f384a.stopLivePlay(bVar.b);
            } else {
                Log.d("SpeedRunner", "------stop------vid=" + bVar.f388a + "-id=" + bVar.f389a);
                this.f384a.stopPlay(bVar.f388a);
            }
            synchronized (this.f385a) {
                for (Entry entry : this.f385a.entrySet()) {
                    if (((String) entry.getKey()).equals(String.valueOf(bVar.f389a))) {
                        this.f385a.remove(entry.getKey());
                        break;
                    }
                }
            }
        }
    }
}
