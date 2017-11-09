package com.gala.sdk.player.data.job;

import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.utils.job.Job;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class VideoJobSwitcher extends VideoJob {
    private VideoJob f699a;
    private final List<JobHolder> f700a = new ArrayList();

    public interface ISwitchCondition {
        boolean checkPass(IVideo iVideo);
    }

    private class JobHolder {
        VideoJob f697a;
        ISwitchCondition f698a;

        public JobHolder(VideoJobSwitcher videoJobSwitcher, ISwitchCondition condition, VideoJob job) {
            this.f698a = condition;
            this.f697a = job;
        }
    }

    public void onRun(com.gala.sdk.utils.job.JobController r8) {
        /* JADX: method processing error */
/*
Error: java.lang.NullPointerException
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.fixSplitterBlock(BlockFinish.java:63)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:34)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r7 = this;
        r3 = 0;
        r4 = r7.f700a;
        monitor-enter(r4);
        r1 = r7.f700a;	 Catch:{ all -> 0x0065 }
        r5 = r1.iterator();	 Catch:{ all -> 0x0065 }
    L_0x000a:
        r1 = r5.hasNext();	 Catch:{ all -> 0x0065 }
        if (r1 == 0) goto L_0x006e;	 Catch:{ all -> 0x0065 }
    L_0x0010:
        r1 = r5.next();	 Catch:{ all -> 0x0065 }
        r1 = (com.gala.sdk.player.data.job.VideoJobSwitcher.JobHolder) r1;	 Catch:{ all -> 0x0065 }
        r6 = r1.f698a;	 Catch:{ all -> 0x0065 }
        r2 = r7.getData();	 Catch:{ all -> 0x0065 }
        r2 = (com.gala.sdk.player.data.IVideo) r2;	 Catch:{ all -> 0x0065 }
        r2 = r6.checkPass(r2);	 Catch:{ all -> 0x0065 }
        if (r2 == 0) goto L_0x000a;	 Catch:{ all -> 0x0065 }
    L_0x0024:
        r1 = r1.f697a;	 Catch:{ all -> 0x0065 }
    L_0x0026:
        r2 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;	 Catch:{ all -> 0x0065 }
        if (r2 == 0) goto L_0x0040;	 Catch:{ all -> 0x0065 }
    L_0x002a:
        r2 = "Player/Lib/Data/VideoJobSwitcher";	 Catch:{ all -> 0x0065 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0065 }
        r5 = "onRun() find job ";	 Catch:{ all -> 0x0065 }
        r3.<init>(r5);	 Catch:{ all -> 0x0065 }
        r3 = r3.append(r1);	 Catch:{ all -> 0x0065 }
        r3 = r3.toString();	 Catch:{ all -> 0x0065 }
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r2, r3);	 Catch:{ all -> 0x0065 }
    L_0x0040:
        if (r1 != 0) goto L_0x005e;	 Catch:{ all -> 0x0065 }
    L_0x0042:
        r2 = com.gala.video.lib.framework.core.utils.LogUtils.mIsDebug;	 Catch:{ all -> 0x0065 }
        if (r2 == 0) goto L_0x005c;	 Catch:{ all -> 0x0065 }
    L_0x0046:
        r2 = "Player/Lib/Data/VideoJobSwitcher";	 Catch:{ all -> 0x0065 }
        r3 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0065 }
        r5 = "onRun() find null job ";	 Catch:{ all -> 0x0065 }
        r3.<init>(r5);	 Catch:{ all -> 0x0065 }
        r1 = r3.append(r1);	 Catch:{ all -> 0x0065 }
        r1 = r1.toString();	 Catch:{ all -> 0x0065 }
        com.gala.video.lib.framework.core.utils.LogUtils.m1568d(r2, r1);	 Catch:{ all -> 0x0065 }
    L_0x005c:
        r1 = r7.f699a;	 Catch:{ all -> 0x0065 }
    L_0x005e:
        monitor-exit(r4);	 Catch:{ all -> 0x0065 }
        if (r1 == 0) goto L_0x0064;
    L_0x0061:
        r1.run(r8);
    L_0x0064:
        return;
    L_0x0065:
        r1 = move-exception;
        r0 = r8;
        r8 = r1;
    L_0x0068:
        monitor-exit(r4);	 Catch:{ all -> 0x006a }
        throw r8;
    L_0x006a:
        r1 = move-exception;
        r0 = r8;
        r8 = r1;
        goto L_0x0068;
    L_0x006e:
        r1 = r3;
        goto L_0x0026;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.sdk.player.data.job.VideoJobSwitcher.onRun(com.gala.sdk.utils.job.JobController):void");
    }

    public VideoJobSwitcher(IVideo video, VideoJobListener listener) {
        super("Player/Lib/Data/VideoJobSwitcher", video, listener);
    }

    public synchronized void link(ISwitchCondition condition, VideoJob job) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("Player/Lib/Data/VideoJobSwitcher", "link(" + condition + ", " + job + ")");
        }
        if (condition != null) {
            this.f700a.add(new JobHolder(this, condition, job));
        } else {
            this.f699a = job;
        }
    }

    public void link(Job<IVideo>... jobArr) {
        throw new UnsupportedOperationException("Don't link jobs for they will not be run.");
    }

    public List<Job<IVideo>> getNextJobs() {
        List<Job<IVideo>> arrayList = new ArrayList();
        synchronized (this.f700a) {
            for (JobHolder jobHolder : this.f700a) {
                arrayList.add(jobHolder.f697a);
            }
        }
        return arrayList;
    }
}
