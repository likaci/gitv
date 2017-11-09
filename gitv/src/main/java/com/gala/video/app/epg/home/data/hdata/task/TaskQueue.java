package com.gala.video.app.epg.home.data.hdata.task;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class TaskQueue {
    private LinkedBlockingQueue<TaskAction> mPendingTaskActionsQueue = new LinkedBlockingQueue();
    private LinkedBlockingQueue<TaskAction> mRunningTaskActionsQueue = new LinkedBlockingQueue();
    private AtomicInteger mTaskCount = new AtomicInteger(0);

    public TaskQueue() {
        this.mTaskCount.set(0);
    }

    public LinkedBlockingQueue<TaskAction> getRunningTaskQueue() {
        return this.mRunningTaskActionsQueue;
    }

    public LinkedBlockingQueue<TaskAction> getPendingTaskActionsQueue() {
        return this.mPendingTaskActionsQueue;
    }

    public void addRunningTask(TaskAction task) {
        this.mRunningTaskActionsQueue.add(task);
        this.mTaskCount.incrementAndGet();
    }

    public void addPendingTask(TaskAction task) {
        if (!this.mPendingTaskActionsQueue.contains(task)) {
            this.mPendingTaskActionsQueue.add(task);
        }
    }

    public void clearAllTasks() {
        clearRunningTasks();
        clearPendingTasks();
    }

    public void clearRunningTasks() {
        this.mTaskCount.set(0);
        this.mRunningTaskActionsQueue.clear();
    }

    public void clearPendingTasks() {
        this.mPendingTaskActionsQueue.clear();
    }

    public boolean isRunFinish() {
        return this.mTaskCount.get() == 0;
    }

    public boolean isRunningTaskQueueEmpty() {
        return this.mRunningTaskActionsQueue.isEmpty();
    }

    public boolean isPendingTaskQueueEmpty() {
        return this.mPendingTaskActionsQueue.isEmpty();
    }

    public int getRunningTaskQueueSize() {
        return this.mRunningTaskActionsQueue.size();
    }

    public int getPendingTaskQueueSize() {
        return this.mPendingTaskActionsQueue.size();
    }

    public void removeRunningTask(TaskAction task) {
        this.mRunningTaskActionsQueue.remove(task);
        this.mTaskCount.decrementAndGet();
    }

    public void removePendingTask(TaskAction task) {
        this.mPendingTaskActionsQueue.remove(task);
    }

    public TaskAction getRunningTask() throws InterruptedException {
        return (TaskAction) this.mRunningTaskActionsQueue.take();
    }

    public void decreaseRunningTaskCount() {
        this.mTaskCount.decrementAndGet();
    }
}
