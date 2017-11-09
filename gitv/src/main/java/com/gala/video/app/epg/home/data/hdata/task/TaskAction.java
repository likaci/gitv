package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.video.app.epg.home.data.hdata.DataRequestTaskStrategy.onExecuteListener;

public class TaskAction {
    private static final String TAG = "home/taskAction";
    private long mDelay;
    private int mExecuteAction = 0;
    private long mFixedRefreshInterval = 86400000;
    private final int mPeriodismLevel;
    private Priority mPriority = Priority.NORMAL;
    private final BaseRequestTask mTask;

    public static class Builder {
        private long mDelay = -1;
        private int mExecuteAction = 0;
        private long mFixedRefreshInterval = 86400000;
        private int mLevel = 2;
        private Priority mPriority = Priority.NORMAL;
        private BaseRequestTask mTask;

        public Builder(BaseRequestTask task, int executeAction, int level) {
            this.mTask = task;
            this.mExecuteAction = executeAction;
            this.mLevel = level;
        }

        public Builder delay(long delay) {
            this.mDelay = delay;
            return this;
        }

        public Builder fixedInterval(long fixedRefreshInterval) {
            this.mFixedRefreshInterval = fixedRefreshInterval;
            return this;
        }

        public Builder priority(Priority priority) {
            this.mPriority = priority;
            return this;
        }

        public TaskAction build() {
            return new TaskAction(this);
        }
    }

    public enum Priority {
        LOW,
        NORMAL,
        HIGH
    }

    public TaskAction(Builder builder) {
        this.mTask = builder.mTask;
        this.mExecuteAction = builder.mExecuteAction;
        this.mPeriodismLevel = builder.mLevel;
        this.mFixedRefreshInterval = builder.mFixedRefreshInterval;
        this.mDelay = builder.mDelay;
        this.mPriority = builder.mPriority;
    }

    public BaseRequestTask getTask() {
        return this.mTask;
    }

    public long getFixedRefreshInterval() {
        return this.mFixedRefreshInterval;
    }

    public int getTaskExecuteAction() {
        return this.mExecuteAction;
    }

    public int getPeriodismLevel() {
        return this.mPeriodismLevel;
    }

    public long getDelay() {
        return this.mDelay;
    }

    public void setTaskCompleteCallback(onExecuteListener listener) {
        if (this.mTask != null) {
            this.mTask.setTaskCallBack(listener);
        }
    }

    public String toString() {
        String result = super.toString();
        if (this.mTask != null) {
            return "task action:(" + " task name = " + this.mTask.getClass().getName() + " execute action type = " + this.mExecuteAction + (" refresh level = " + this.mPeriodismLevel);
        }
        return result;
    }

    private Priority getPriority() {
        return this.mPriority;
    }

    public boolean equals(Object o) {
        boolean result = super.equals(o);
        if (result || !(o instanceof TaskAction)) {
            return result;
        }
        return this.mTask.identifier().equals(((TaskAction) o).getTask().identifier());
    }

    public int hashCode() {
        return this.mTask.identifier().hashCode();
    }
}
