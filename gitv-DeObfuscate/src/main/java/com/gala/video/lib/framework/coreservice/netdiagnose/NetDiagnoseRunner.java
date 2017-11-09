package com.gala.video.lib.framework.coreservice.netdiagnose;

import android.content.Context;
import com.gala.video.lib.framework.core.job.Job;
import com.gala.video.lib.framework.core.job.JobController;
import com.gala.video.lib.framework.core.job.JobControllerImpl;
import com.gala.video.lib.framework.core.job.JobExecutor;
import com.gala.video.lib.framework.core.utils.NameExecutors;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import java.util.concurrent.ExecutorService;

public class NetDiagnoseRunner {
    private Context mContext;
    private JobController mController;
    private JobExecutor<NetDiagnoseInfo> mExecutor = new C16251();
    private NetDiagnoseInfo mInfo;
    private ExecutorService mSingleExecutor = NameExecutors.newSingleThreadExecutor("NetDiagnoseRunner");

    class C16251 implements JobExecutor<NetDiagnoseInfo> {
        C16251() {
        }

        public void submit(final JobController controller, final Job<NetDiagnoseInfo> job) {
            NetDiagnoseRunner.this.mSingleExecutor.submit(new Runnable() {
                public void run() {
                    if (job != null) {
                        job.run(controller);
                    }
                }
            });
        }
    }

    public NetDiagnoseRunner(Context context, NetDiagnoseInfo info) {
        this.mContext = context;
        this.mInfo = info;
    }

    public void submit(NetDiagnoseJob header) {
        if (this.mController == null) {
            this.mController = new JobControllerImpl(this.mContext);
        }
        this.mExecutor.submit(this.mController, header);
    }

    protected NetDiagnoseInfo getInfo() {
        return this.mInfo;
    }

    public void cancel() {
        if (this.mController != null) {
            this.mController.cancel();
        }
    }
}
