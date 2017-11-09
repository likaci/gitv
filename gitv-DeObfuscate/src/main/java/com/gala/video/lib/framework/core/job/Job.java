package com.gala.video.lib.framework.core.job;

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
    private final String TAG;
    private DataType mData;
    private JobError mError;
    private JobListener<Job<DataType>> mListener;
    private final String mName;
    private final CopyOnWriteArrayList<Job<DataType>> mNextJobs;
    private boolean mRunNextWhenFail;
    private int mState;

    public Job(String name, DataType data) {
        this(name, data, null);
    }

    public Job(String name, DataType data, JobListener<Job<DataType>> listener) {
        this.mNextJobs = new CopyOnWriteArrayList();
        this.mRunNextWhenFail = true;
        this.mName = name;
        this.TAG = name + "@" + Integer.toHexString(hashCode());
        this.mData = data;
        this.mListener = listener;
        this.mState = 0;
    }

    public void setListener(JobListener<Job<DataType>> listener) {
        this.mListener = listener;
    }

    public JobListener<Job<DataType>> getListener() {
        return this.mListener;
    }

    public void setRunNextWhenFail(boolean run) {
        this.mRunNextWhenFail = run;
    }

    public boolean isRunNextWhenFail() {
        return this.mRunNextWhenFail;
    }

    public synchronized DataType getData() {
        return this.mData;
    }

    public synchronized JobError getError() {
        return this.mError;
    }

    public synchronized void setError(JobError error) {
        this.mError = error;
    }

    public synchronized int getState() {
        return this.mState;
    }

    public void link(Job<DataType>... jobs) {
        if (jobs != null) {
            for (Job<DataType> job : jobs) {
                this.mNextJobs.add(job);
            }
        }
    }

    private final void runNextJobs(JobController controller) {
        Iterator it = this.mNextJobs.iterator();
        while (it.hasNext()) {
            ((Job) it.next()).run(controller);
        }
    }

    protected void notifyDone() {
        if (this.mListener != null) {
            this.mListener.onJobDone(this);
        }
    }

    public final void notifyJobSuccess(JobController controller) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyJobSuccess() " + this);
        }
        if (controller.isCancelled()) {
            LogUtils.m1568d(this.TAG, "controller isCancelled" + this);
            this.mState = 4;
        } else {
            this.mState = 2;
            this.mError = null;
        }
        notifyDone();
        runNextJobs(controller);
    }

    public final void notifyJobFail(JobController controller, JobError error) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "notifyJobFail(" + error + ") " + this);
        }
        if (controller.isCancelled()) {
            LogUtils.m1568d(this.TAG, "controller isCancelled" + this);
            this.mState = 4;
        } else {
            this.mState = 3;
            this.mError = error;
        }
        notifyDone();
        if (this.mRunNextWhenFail) {
            runNextJobs(controller);
        }
    }

    public void run(JobController controller) {
        if (isCancelled(controller)) {
            this.mState = 4;
            notifyDone();
            runNextJobs(controller);
            return;
        }
        this.mState = 1;
        try {
            onRun(controller);
        } catch (Exception e) {
            LogUtils.m1572e(this.TAG, "run() Unknown error!", e);
            notifyJobFail(controller, new JobError("unknown", "unknown", e.getMessage(), "", new ApiException("job error")));
        }
    }

    private boolean isCancelled(JobController controller) {
        boolean cancel = controller != null && controller.isCancelled();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(this.TAG, "isCancelled() " + this + " return " + cancel);
        }
        return cancel;
    }

    public void onRun(JobController controller) {
    }

    public String toString() {
        return "Job[" + this.TAG + "](mState=" + this.mState + ", mData=" + (this.mData != null ? this.mData.getClass().getSimpleName() + this.mData.hashCode() : "NULL") + ", mError=" + this.mError + ")";
    }

    public List<Job<DataType>> getNextJobs() {
        return this.mNextJobs;
    }

    public String getName() {
        return this.mName;
    }
}
