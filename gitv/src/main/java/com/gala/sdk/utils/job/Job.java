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
    private int a;
    private JobError f320a;
    private JobListener<Job<DataType>> f321a;
    private DataType f322a;
    private final String f323a;
    private final CopyOnWriteArrayList<Job<DataType>> f324a;
    private boolean f325a;
    private final String b;

    public Job(String name, DataType data) {
        this(name, data, null);
    }

    public Job(String name, DataType data, JobListener<Job<DataType>> listener) {
        this.f324a = new CopyOnWriteArrayList();
        this.f325a = true;
        this.b = name;
        this.f323a = name + "@" + Integer.toHexString(hashCode());
        this.f322a = data;
        this.f321a = listener;
        this.a = 0;
    }

    public void setListener(JobListener<Job<DataType>> listener) {
        this.f321a = listener;
    }

    public JobListener<Job<DataType>> getListener() {
        return this.f321a;
    }

    public void setRunNextWhenFail(boolean run) {
        this.f325a = run;
    }

    public boolean isRunNextWhenFail() {
        return this.f325a;
    }

    public synchronized DataType getData() {
        return this.f322a;
    }

    public synchronized JobError getError() {
        return this.f320a;
    }

    public synchronized void setError(JobError error) {
        this.f320a = error;
    }

    public synchronized int getState() {
        return this.a;
    }

    public void link(Job<DataType>... jobs) {
        if (jobs != null) {
            for (Object add : jobs) {
                this.f324a.add(add);
            }
        }
    }

    private final void a(JobController jobController) {
        Iterator it = this.f324a.iterator();
        while (it.hasNext()) {
            ((Job) it.next()).run(jobController);
        }
    }

    protected void notifyDone() {
        if (this.f321a != null) {
            this.f321a.onJobDone(this);
        }
    }

    public final void notifyJobSuccess(JobController controller) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.f323a, "notifyJobSuccess() " + this);
        }
        this.a = 2;
        this.f320a = null;
        notifyDone();
        a(controller);
    }

    public final void notifyJobFail(JobController controller, JobError error) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.f323a, "notifyJobFail(" + error + ") " + this);
        }
        this.a = 3;
        this.f320a = error;
        notifyDone();
        if (this.f325a) {
            a(controller);
        }
    }

    public void run(JobController controller) {
        boolean z = controller != null && controller.isCancelled();
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.f323a, "isCancelled() " + this + " return " + z);
        }
        if (z) {
            this.a = 4;
            notifyDone();
            a(controller);
            return;
        }
        this.a = 1;
        try {
            onRun(controller);
        } catch (Throwable e) {
            LogUtils.e(this.f323a, "run() Unknown error!", e);
            notifyJobFail(controller, new JobError("unknown", "unknown", e.getMessage(), "", new ApiException("job error")));
        }
    }

    public void onRun(JobController jobController) {
    }

    public String toString() {
        return "Job[" + this.f323a + "](mState=" + this.a + ", mData=" + (this.f322a != null ? this.f322a.getClass().getSimpleName() + this.f322a.hashCode() : "NULL") + ", mError=" + this.f320a + ")";
    }

    public List<Job<DataType>> getNextJobs() {
        return this.f324a;
    }

    public String getName() {
        return this.b;
    }
}
