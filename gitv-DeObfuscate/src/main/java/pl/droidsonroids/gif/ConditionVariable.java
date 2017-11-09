package pl.droidsonroids.gif;

class ConditionVariable {
    private volatile boolean mCondition;

    ConditionVariable() {
    }

    synchronized void set(boolean state) {
        if (state) {
            open();
        } else {
            close();
        }
    }

    synchronized void open() {
        boolean z = this.mCondition;
        this.mCondition = true;
        if (!z) {
            notify();
        }
    }

    synchronized void close() {
        this.mCondition = false;
    }

    synchronized void block() throws InterruptedException {
        while (!this.mCondition) {
            wait();
        }
    }
}
