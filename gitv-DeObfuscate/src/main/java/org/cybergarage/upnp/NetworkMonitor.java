package org.cybergarage.upnp;

import java.util.Iterator;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

public class NetworkMonitor {
    private static final int BAD_QUEUE_LENGTH = 1;
    public static final long BAD_RESPONSE_TIME = 2000;
    private static final long GOOD_RESPONSE_TIME = 200;
    private static final int NORMAL_QUEUE_LENGTH = 1;
    private static final long NORMAL_RESPONSE_TIME = 1000;
    public static final long SUPER_BAD_RESPONSE_TIME = 4000;
    private static NetworkMonitor sInstance = null;
    private LinkedBlockingQueue<Long> mBadQueue = null;
    private long mLastResponseTime = 0;
    private Vector<NetworkStatusListener> mListeners = null;
    private NETWORK_STATUS mNetStatus = NETWORK_STATUS.OK;
    private LinkedBlockingQueue<Long> mNotBadQueue = null;

    public static NetworkMonitor getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkMonitor();
        }
        return sInstance;
    }

    private NetworkMonitor() {
        if (this.mListeners == null) {
            this.mListeners = new Vector();
        } else {
            this.mListeners.clear();
        }
        this.mNetStatus = NETWORK_STATUS.OK;
        this.mLastResponseTime = 0;
        checkQueuesAvailable();
    }

    public NETWORK_STATUS getNetworkStatus() {
        return this.mNetStatus;
    }

    public long getLastResponseTime() {
        return this.mLastResponseTime;
    }

    private void checkQueuesAvailable() {
        if (this.mBadQueue == null) {
            this.mBadQueue = new LinkedBlockingQueue(1);
        }
        if (this.mNotBadQueue == null) {
            this.mNotBadQueue = new LinkedBlockingQueue(1);
        }
    }

    public void release() {
        if (this.mBadQueue != null) {
            this.mBadQueue.clear();
            this.mBadQueue = null;
        }
        if (this.mNotBadQueue != null) {
            this.mNotBadQueue.clear();
            this.mNotBadQueue = null;
        }
        if (this.mListeners != null) {
            this.mListeners.clear();
            this.mListeners = null;
        }
        this.mLastResponseTime = 0;
        sInstance = null;
    }

    void notifyResponseTime(long responseTime) {
        if (responseTime > 0) {
            this.mLastResponseTime = responseTime;
            if (!(this.mListeners == null || this.mListeners.isEmpty())) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    ((NetworkStatusListener) it.next()).OnResponseTimeGot(responseTime);
                }
            }
            if (responseTime <= 1000) {
                notifyNotBad(responseTime);
            } else if (responseTime >= 2000) {
                notifyBad(responseTime);
            } else {
                notifyOther(responseTime);
            }
        }
    }

    private void notifyNotBad(long responseTime) {
        checkQueuesAvailable();
        if (!this.mNotBadQueue.offer(Long.valueOf(responseTime)) || responseTime <= GOOD_RESPONSE_TIME) {
            this.mBadQueue.clear();
            setStatus(NETWORK_STATUS.OK, responseTime);
        }
    }

    private void notifyBad(long responseTime) {
        checkQueuesAvailable();
        if (!this.mBadQueue.offer(Long.valueOf(responseTime)) || responseTime >= SUPER_BAD_RESPONSE_TIME) {
            this.mNotBadQueue.clear();
            setStatus(NETWORK_STATUS.BAD, responseTime);
        }
    }

    private void notifyOther(long responseTime) {
    }

    private void setStatus(NETWORK_STATUS status, long responseTime) {
        if (status != null && status != this.mNetStatus) {
            this.mNetStatus = status;
            if (this.mListeners != null && !this.mListeners.isEmpty()) {
                Iterator it = this.mListeners.iterator();
                while (it.hasNext()) {
                    ((NetworkStatusListener) it.next()).OnNetworkStatusChanged(status);
                }
            }
        }
    }

    public boolean addNetworkStatusListener(NetworkStatusListener listener) {
        if (listener == null) {
            return false;
        }
        if (this.mListeners == null) {
            this.mListeners = new Vector();
        }
        return this.mListeners.add(listener);
    }

    public boolean removeNetworkStatusListener(NetworkStatusListener listener) {
        if (listener == null || this.mListeners == null || this.mListeners.isEmpty()) {
            return false;
        }
        return this.mListeners.remove(listener);
    }
}
