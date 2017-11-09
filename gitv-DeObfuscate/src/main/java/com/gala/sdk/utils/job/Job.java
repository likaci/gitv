package com.gala.sdk.utils.job;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Job<DataType> {
    public static final int STATE_CANCELLED = 4;
    public static final int STATE_FAIL = 3;
    public static final int STATE_IDLE = 0;
    public static final int STATE_RUNNING = 1;
    public static final int STATE_SUCCESS = 2;
    private int f621a;
    private JobError f622a;
    private JobListener<Job<DataType>> f623a;
    private DataType f624a;
    private final String f625a;
    private final CopyOnWriteArrayList<Job<DataType>> f626a;
    private boolean f627a;
    private final String f628b;

    public Job(String name, DataType data) {
        this(name, data, null);
    }

    public Job(String name, DataType data, JobListener<Job<DataType>> listener) {
        this.f626a = new CopyOnWriteArrayList();
        this.f627a = true;
        this.f628b = name;
        this.f625a = name + "@" + Integer.toHexString(hashCode());
        this.f624a = data;
        this.f623a = listener;
        this.f621a = 0;
    }

    public void setListener(JobListener<Job<DataType>> listener) {
        this.f623a = listener;
    }

    public JobListener<Job<DataType>> getListener() {
        return this.f623a;
    }

    public void setRunNextWhenFail(boolean run) {
        this.f627a = run;
    }

    public boolean isRunNextWhenFail() {
        return this.f627a;
    }

    public synchronized DataType getData() {
        return this.f624a;
    }

    public synchronized JobError getError() {
        return this.f622a;
    }

    public synchronized void setError(JobError error) {
        this.f622a = error;
    }

    public synchronized int getState() {
        return this.f621a;
    }

    public void link(Job<DataType>... jobs) {
        if (jobs != null) {
            for (Object add : jobs) {
                this.f626a.add(add);
            }
        }
    }

    private final void m415a(JobController jobController) {
        Iterator it = this.f626a.iterator();
        while (it.hasNext()) {
            ((Job) it.next()).run(jobController);
        }
    }

    protected void notifyDone() {
        if (this.f623a != null) {
            this.f623a.onJobDone(this);
        }
    }

    public final void notifyJobSuccess(JobController controller) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.f625a, "notifyJobSuccess() " + this);
        }
        this.f621a = 2;
        this.f622a = null;
        notifyDone();
        m415a(controller);
    }

    public final void notifyJobFail(JobController controller, JobError error) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.f625a, "notifyJobFail(" + error + ") " + this);
        }
        this.f621a = 3;
        this.f622a = error;
        notifyDone();
        if (this.f627a) {
            m415a(controller);
        }
    }

    public void run(JobController controller) {
        boolean z = controller != null && controller.isCancelled();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.f625a, "isCancelled() " + this + " return " + z);
        }
        if (z) {
            this.f621a = 4;
            notifyDone();
            m415a(controller);
            return;
        }
        this.f621a = 1;
        try {
            onRun(controller);
        } catch (Throwable e) {
            LogUtils.m1572e(this.f625a, "run() Unknown error!", e);
            notifyJobFail(controller, new JobError("unknown", "unknown", e.getMessage(), "", new ApiException("job error")));
        }
    }

    public void onRun(JobController jobController) {
    }

    public String toString() {
        return "Job[" + this.f625a + "](mState=" + this.f621a + ", mData=" + (this.f624a != null ? this.f624a.getClass().getSimpleName() + this.f624a.hashCode() : "NULL") + ", mError=" + this.f622a + ")";
    }

    public List<Job<DataType>> getNextJobs() {
        return this.f626a;
    }

    public String getName() {
        return this.f628b;
    }
}
