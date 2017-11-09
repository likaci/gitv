package com.gala.video.app.stub;

public class Thread8K extends Thread {
    private static final long STACK_SIZE_4K = 8192;
    private static int thread4KInitNumber;

    private static synchronized int nextThreadNum() {
        int i;
        synchronized (Thread8K.class) {
            i = thread4KInitNumber;
            thread4KInitNumber = i + 1;
        }
        return i;
    }

    public Thread8K() {
        super(null, null, "Thread8K-" + nextThreadNum(), STACK_SIZE_4K);
    }

    public Thread8K(Runnable target) {
        super(null, target, "Thread8K-" + nextThreadNum(), STACK_SIZE_4K);
    }

    public Thread8K(String name) {
        super(null, null, name, STACK_SIZE_4K);
    }

    public Thread8K(Runnable target, String name) {
        super(null, target, name, STACK_SIZE_4K);
    }
}
