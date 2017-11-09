package org.cybergarage.util;

public class ThreadCore implements Runnable {
    private volatile Thread mThreadObject = null;

    public void setThreadObject(Thread obj) {
        this.mThreadObject = obj;
    }

    public Thread getThreadObject() {
        return this.mThreadObject;
    }

    public void start(String name) {
        if (getThreadObject() == null) {
            Thread threadObject = new Thread(this, name);
            setThreadObject(threadObject);
            threadObject.start();
        }
    }

    public void run() {
    }

    public boolean isRunnable() {
        return Thread.currentThread() == getThreadObject();
    }

    public void stop() {
        Thread threadObject = getThreadObject();
        if (threadObject != null) {
            Debug.message("ThreadCore: stop thread..." + threadObject.getName());
            setThreadObject(null);
            threadObject.interrupt();
        }
    }

    public void restart(String name) {
        stop();
        start(name);
    }
}
