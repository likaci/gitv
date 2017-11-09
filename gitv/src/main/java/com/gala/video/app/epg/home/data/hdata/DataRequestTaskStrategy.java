package com.gala.video.app.epg.home.data.hdata;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import com.gala.video.app.epg.home.data.hdata.task.TaskAction;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.NameExecutors;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverStatusDispatcher.IStatusListener;
import com.gala.video.lib.share.uikit.data.provider.DataRefreshPeriodism;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class DataRequestTaskStrategy {
    private static final int ACTIVE_STATE_INTERVAL = 600000;
    private static final int DATA_REQUEST_PARALLEL_SIZE;
    private static final int MSG_ACTIVE_STATE_CHANGED = 100;
    private static final String TAG = "home/DataRequestTaskExecutors";
    private static final ScheduledExecutorService mParallelExecutor = NameExecutors.newScheduledThreadPool(DATA_REQUEST_PARALLEL_SIZE, "DataRequestTaskStrategy");
    private static final ExecutorService mSerialExecutor = NameExecutors.newScheduledThreadPool(1, "DataRequestTaskStrategy");
    private static DataRequestTaskStrategy sInstance = new DataRequestTaskStrategy();
    private onExecuteListener mCallBack = new onExecuteListener() {
        public void onFinished() {
            DataRequestTaskStrategy.this.mTaskCount.decrementAndGet();
            LogUtils.d(DataRequestTaskStrategy.TAG, "execute task action onFinished task count reserved = " + DataRequestTaskStrategy.this.mTaskCount.get());
        }
    };
    private ArrayList<TaskAction> mFlightTaskActions = new ArrayList();
    private volatile boolean mIsHomeActive = true;
    private volatile boolean mIsInForeground = false;
    private Object mLock = new Object();
    private LinkedBlockingQueue<TaskAction> mPendingTaskActionsQueue = new LinkedBlockingQueue();
    private final RefreshHandler mRefreshHandler = new RefreshHandler();
    private LinkedBlockingQueue<TaskAction> mRunningTaskActionsQueue = new LinkedBlockingQueue();
    IStatusListener mScreenSaverListener = new IStatusListener() {
        public void onStart() {
            LogUtils.d(DataRequestTaskStrategy.TAG, "screen saver start");
        }

        public void onStop() {
            LogUtils.d(DataRequestTaskStrategy.TAG, "screen saver stop");
            if (DataRequestTaskStrategy.this.mIsInForeground) {
                DataRequestTaskStrategy.this.notifyHomeKeyEvent();
            }
        }
    };
    private AtomicInteger mTaskCount = new AtomicInteger(0);
    private TimeHandler mTimeHandler = new TimeHandler();

    public interface onExecuteListener {
        void onFinished();
    }

    private class Dispatcher extends Thread8K {
        public Dispatcher() {
            super("Dispatcher");
        }

        public void run() {
            while (true) {
                try {
                    DataRequestTaskStrategy.this.taskOneAndExecute();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    LogUtils.e(DataRequestTaskStrategy.TAG, "execute task exception : " + e);
                }
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class RefreshHandler extends Handler {
        private RefreshHandler() {
        }

        public void handleMessage(Message msg) {
            TaskAction action = msg.obj;
            LogUtils.d(DataRequestTaskStrategy.TAG, "handle message action = " + action + " level = " + action.getPeriodismLevel());
            if (DataRequestTaskStrategy.this.shouldSchedule() || action.getPeriodismLevel() == -2) {
                DataRequestTaskStrategy.this.doScheduleTaskForTimer(action);
                DataRequestTaskStrategy.this.scheduleNextTask(action);
            }
        }
    }

    @SuppressLint({"HandlerLeak"})
    private class TimeHandler extends Handler {
        private TimeHandler() {
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 100:
                    DataRequestTaskStrategy.this.mIsHomeActive = false;
                    LogUtils.d(DataRequestTaskStrategy.TAG, "time is up to fall in in active state,do not refresh data");
                    return;
                default:
                    return;
            }
        }
    }

    static {
        int i = 2;
        if (Runtime.getRuntime().availableProcessors() <= 2) {
            i = 1;
        }
        DATA_REQUEST_PARALLEL_SIZE = i;
    }

    private DataRequestTaskStrategy() {
    }

    public static DataRequestTaskStrategy getInstance() {
        return sInstance;
    }

    public void notifyHomeKeyEvent() {
        LogUtils.d(TAG, "notifyHomeKeyEvent");
        if (!shouldScheduleOnlyForEvent()) {
            this.mTimeHandler.removeCallbacksAndMessages(null);
            this.mTimeHandler.sendEmptyMessageDelayed(100, 600000);
            this.mIsHomeActive = true;
            doPendingTask();
        }
    }

    public void addTask(TaskAction action) {
        this.mRunningTaskActionsQueue.add(action);
        this.mTaskCount.incrementAndGet();
    }

    public void addTask(List<TaskAction> actions) {
        if (actions != null) {
            for (TaskAction action : actions) {
                this.mRunningTaskActionsQueue.add(action);
                this.mTaskCount.incrementAndGet();
            }
        }
    }

    public void notifyHomeResumed() {
        LogUtils.d(TAG, "notify home fragment resumed");
        if (!this.mIsInForeground) {
            this.mIsInForeground = true;
            doPendingTask();
        }
    }

    public void notifyHomeStopped() {
        LogUtils.d(TAG, "notify home fragment stopped");
        this.mIsInForeground = false;
    }

    private void doPendingTask() {
        if (!this.mPendingTaskActionsQueue.isEmpty()) {
            LogUtils.d(TAG, "execute pending task ,task queue size = " + this.mPendingTaskActionsQueue.size());
            if (!this.mPendingTaskActionsQueue.isEmpty()) {
                Iterator it = this.mPendingTaskActionsQueue.iterator();
                while (it.hasNext()) {
                    TaskAction pendingAction = (TaskAction) it.next();
                    this.mRunningTaskActionsQueue.add(pendingAction);
                    this.mTaskCount.incrementAndGet();
                    this.mPendingTaskActionsQueue.remove(pendingAction);
                    scheduleNextTask(pendingAction);
                }
            }
        }
    }

    private void doScheduleTaskForTimer(TaskAction action) {
        LogUtils.d(TAG, "scheduleTask ,pendding queue size = " + this.mPendingTaskActionsQueue.size() + " shouldScheduleOnlyForEvent = " + shouldScheduleOnlyForEvent() + " isActive : " + this.mIsHomeActive);
        if ((this.mIsInForeground && this.mIsHomeActive && !shouldScheduleOnlyForEvent()) || -2 == action.getPeriodismLevel()) {
            LogUtils.d(TAG, "task real executed action= " + action);
            this.mRunningTaskActionsQueue.add(action);
            this.mTaskCount.incrementAndGet();
            return;
        }
        if (!this.mPendingTaskActionsQueue.contains(action)) {
            this.mPendingTaskActionsQueue.add(action);
        }
        LogUtils.d(TAG, "added task to pending queue ,queue size = " + this.mPendingTaskActionsQueue.size());
    }

    private void scheduleNextTask(TaskAction action) {
        long interval;
        int level = action.getPeriodismLevel();
        if (level == -2) {
            if (-1 > 0) {
                interval = -1;
            } else {
                interval = action.getFixedRefreshInterval();
            }
        } else if (level == -1) {
            return;
        } else {
            if (-1 > 0) {
                interval = -1;
            } else {
                interval = (long) DataRefreshPeriodism.instance().getRefreshInterval(level);
            }
        }
        Message msg = Message.obtain();
        msg.arg1 = level;
        msg.obj = action;
        this.mRefreshHandler.removeCallbacksAndMessages(action);
        this.mRefreshHandler.sendMessageDelayed(msg, interval);
        synchronized (this.mLock) {
            if (!this.mFlightTaskActions.contains(action)) {
                this.mFlightTaskActions.add(action);
            }
        }
        LogUtils.d(TAG, "schedule next task ,level = " + level + " interval: " + interval + " action = " + action);
    }

    private boolean shouldSchedule() {
        boolean result = false;
        if (GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel() != null) {
            result = !GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsHomeRequestOnlyForLaunch();
        }
        LogUtils.d(TAG, "should schedule : " + result);
        return result;
    }

    private boolean shouldScheduleOnlyForEvent() {
        boolean result = false;
        if (GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel() != null) {
            result = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsHomeRequestForLaunchAndEvent();
        }
        LogUtils.d(TAG, "should schedule only for event: " + result);
        return result;
    }

    public void start() {
        LogUtils.d(TAG, "start task dispatcher");
        new Dispatcher().start();
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().register(this.mScreenSaverListener);
    }

    private void taskOneAndExecute() throws InterruptedException {
        TaskAction action = (TaskAction) this.mRunningTaskActionsQueue.take();
        LogUtils.d(TAG, "execute task action = " + action + " running task size : " + this.mRunningTaskActionsQueue.size());
        if (action != null && action.getTask() != null) {
            action.getTask().setTaskCallBack(this.mCallBack);
            if (action.getTaskExecuteAction() == 1) {
                mSerialExecutor.execute(action.getTask());
            } else if (action.getDelay() > 0) {
                mParallelExecutor.schedule(action.getTask(), action.getDelay(), TimeUnit.MILLISECONDS);
            } else {
                mParallelExecutor.execute(action.getTask());
            }
            if (shouldSchedule()) {
                synchronized (this.mLock) {
                    if (this.mFlightTaskActions.contains(action)) {
                        TaskAction fond = findInFlightTaskAction(action);
                        if (fond != null) {
                            this.mRefreshHandler.removeCallbacksAndMessages(fond);
                        }
                        this.mFlightTaskActions.remove(fond);
                    }
                }
                this.mPendingTaskActionsQueue.remove(action);
                scheduleNextTask(action);
            }
        }
    }

    private TaskAction findInFlightTaskAction(TaskAction action) {
        TaskAction result = null;
        Iterator it = this.mFlightTaskActions.iterator();
        while (it.hasNext()) {
            TaskAction cacheAction = (TaskAction) it.next();
            if (cacheAction.equals(action)) {
                result = cacheAction;
                break;
            }
        }
        LogUtils.d(TAG, "find in flight action@" + result);
        return result;
    }

    public boolean isAllTaskFinished() {
        LogUtils.d(TAG, "isAllTaskFinished task reserved : " + this.mTaskCount.get());
        return this.mTaskCount.get() == 0;
    }

    public void clear() {
        this.mTimeHandler.removeCallbacksAndMessages(null);
        this.mRefreshHandler.removeCallbacksAndMessages(null);
        this.mRunningTaskActionsQueue.clear();
        this.mPendingTaskActionsQueue.clear();
        this.mFlightTaskActions.clear();
        this.mTaskCount = new AtomicInteger(0);
        GetInterfaceTools.getIScreenSaver().getStatusDispatcher().unRegister(this.mScreenSaverListener);
    }
}
