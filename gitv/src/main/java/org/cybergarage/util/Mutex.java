package org.cybergarage.util;

public class Mutex {
    private boolean syncLock = false;

    public synchronized void lock() {
        while (this.syncLock) {
            try {
                wait();
            } catch (Exception e) {
                Debug.warning(e);
            }
        }
        this.syncLock = true;
    }

    public synchronized void unlock() {
        this.syncLock = false;
        notifyAll();
    }
}
