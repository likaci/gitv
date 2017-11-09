package com.gala.video.lib.framework.core.network.check;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.OnNetStateChangedListener;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class NetWorkManager implements INetWorkManager {
    private static final int CHECK_DELAY = 10000;
    private static final String[] DEFAULT_REQUEST_URL = new String[]{"http://www.baidu.com", "http://www.qq.com", "http://www.163.com", "http://www.taobao.com", "http://www.sina.com.cn"};
    private static final long GAP_TIME = 3600000;
    private static final String PPPOE_ACTION = "com.android.sky.action.pppoe";
    private static final int REQUEST_CONN_TIMEOUT = 10000;
    private static final int REQUEST_READ_TIMEOUT = 10000;
    private static final int STATE_COMPLETED_FAILED = 1;
    private static final int STATE_COMPLETED_SUCCESS = 2;
    private static final String TAG = "NetworkManager";
    private static final TimeUnit TIME_UNIT = TimeUnit.MILLISECONDS;
    private static final NetWorkManager mNetWorkManager = new NetWorkManager();
    private static ExecutorService mPool = Executors.newCachedThreadPool(sThreadFactory);
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        public Thread newThread(Runnable r) {
            return new Thread(r, "[Async QY Pool]#" + this.mCount.getAndIncrement());
        }
    };
    private Context mContext;
    private int mCurrentState = 0;
    private HashMap<String, FutureTask<Integer>> mFutureTaskMap = new HashMap();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mIsChecking;
    private boolean mIsReceiveSystemNetworkConnectionMessage = false;
    private long mLastCheckTime;
    private final Object mLock = new Object();
    private ArrayList<StateCallback> mNetStateCallbacks = new ArrayList();
    private NetWorkConnectionReceiver mNetWorkConnectReceiver;
    private boolean mPppoeState = false;
    private String[] mRequestUrl = DEFAULT_REQUEST_URL;
    private ArrayList<OnNetStateChangedListener> mStateChangedListeners = new ArrayList();
    private long mTimeOut = IOpenApiCommandHolder.OAA_CONNECT_INTERVAL;
    private WifiInfo mWifiInfo;
    private WifiManager mWifiManager;

    private class NetWorkConnectionReceiver extends BroadcastReceiver {
        private NetWorkConnectionReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            Log.d(NetWorkManager.TAG, "onReceive: intent=" + intent + ", extra=" + intent.getExtras());
            NetWorkManager.this.mIsReceiveSystemNetworkConnectionMessage = true;
            String action = intent.getAction();
            Log.d(NetWorkManager.TAG, "action=" + action);
            if (action.equals(NetWorkManager.PPPOE_ACTION)) {
                NetWorkManager.this.mPppoeState = intent.getStringExtra("pppoe").equals("true");
            }
            if (action.equals("android.net.conn.CONNECTIVITY_CHANGE") || action.equals(NetWorkManager.PPPOE_ACTION)) {
                NetWorkManager.this.checkNetWork();
                NetWorkManager.this.onNetStateChanged(NetWorkManager.this.getRealState(true));
            }
        }
    }

    @SuppressLint({"NewApi"})
    private static class Task extends AsyncTask<Void, Void, Void> {
        private Runnable runnable;

        public Task(Runnable run) {
            this.runnable = run;
        }

        protected Void doInBackground(Void... params) {
            if (this.runnable != null) {
                Process.setThreadPriority(10);
                this.runnable.run();
            }
            this.runnable = null;
            return null;
        }
    }

    private NetWorkManager() {
    }

    public static synchronized NetWorkManager getInstance() {
        NetWorkManager netWorkManager;
        synchronized (NetWorkManager.class) {
            netWorkManager = mNetWorkManager;
        }
        return netWorkManager;
    }

    public void initNetWorkManager(Context context) {
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        registerLanReceiver();
    }

    public void initNetWorkManager(Context context, String domain) {
        if (domain.equals(BuildDefaultDocument.APK_DOMAIN_NAME)) {
            this.mRequestUrl = new String[]{"http://probe-cdn.ptqy.gitv.tv/tv_test", "http://www.baidu.com", "http://www.360.cn", "http://www.163.com"};
        }
        this.mContext = context;
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        registerLanReceiver();
    }

    private void registerLanReceiver() {
        this.mNetWorkConnectReceiver = new NetWorkConnectionReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        intentFilter.addAction(PPPOE_ACTION);
        this.mContext.registerReceiver(this.mNetWorkConnectReceiver, intentFilter);
    }

    private void unLanRegisterReceiver() {
        this.mContext.unregisterReceiver(this.mNetWorkConnectReceiver);
    }

    public int getNetState() {
        if (SystemClock.elapsedRealtime() - this.mLastCheckTime > 3600000) {
            checkNetWork();
        }
        Log.d(TAG, "getNetState: ret=" + this.mCurrentState);
        return this.mCurrentState;
    }

    public void checkNetWork() {
        checkNetWork(null);
    }

    public static void execute(Runnable runnable) {
        if (runnable != null) {
            new Task(runnable).executeOnExecutor(mPool, new Void[0]);
        }
    }

    public void checkNetWork(StateCallback callback) {
        if (this.mContext == null) {
            throw new IllegalStateException("You must call initNetWorkManager(Context context) first!");
        }
        synchronized (this.mLock) {
            if (callback != null) {
                if (!this.mNetStateCallbacks.contains(callback)) {
                    this.mNetStateCallbacks.add(callback);
                }
            }
        }
        if (isNetworkAvailable() || this.mPppoeState) {
            synchronized (this.mLock) {
                if (this.mIsChecking) {
                    return;
                }
                this.mIsChecking = true;
                execute(new Runnable() {
                    public void run() {
                        while (!checkInternet()) {
                            try {
                                Thread.sleep(10000);
                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                NetWorkManager.this.mIsChecking = false;
                            }
                        }
                    }

                    private boolean checkInternet() {
                        boolean hasNet = NetWorkManager.this.tryConnectFirst();
                        if (!hasNet) {
                            hasNet = NetWorkManager.this.retryConnectOthers();
                        }
                        NetWorkManager.this.notifyResult(NetWorkManager.this.getRealState(hasNet));
                        return hasNet;
                    }
                });
                return;
            }
        }
        notifyResult(0);
    }

    private int getRealState(boolean hasNet) {
        NetworkInfo activeNetInfo = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        Log.d(TAG, "getRealState: activeNetInfo={" + activeNetInfo + "}" + (activeNetInfo != null ? ", isConnected=" + activeNetInfo.isConnected() : ""));
        if (activeNetInfo == null || !activeNetInfo.isConnected()) {
            return 0;
        }
        switch (activeNetInfo.getType()) {
            case 1:
                this.mWifiInfo = this.mWifiManager.getConnectionInfo();
                Log.d(TAG, "getRealState: wifi info=" + this.mWifiInfo);
                return hasNet ? 1 : 3;
            default:
                return hasNet ? 2 : 4;
        }
    }

    private void notifyResult(final int state) {
        this.mHandler.post(new Runnable() {
            public void run() {
                NetWorkManager.this.onNetStateChanged(state);
                synchronized (NetWorkManager.this.mLock) {
                    int size = NetWorkManager.this.mNetStateCallbacks.size();
                    ArrayList<StateCallback> callbacks = new ArrayList(size);
                    for (int i = 0; i < size; i++) {
                        StateCallback stateCallback = (StateCallback) NetWorkManager.this.mNetStateCallbacks.get(i);
                        stateCallback.getStateResult(state);
                        callbacks.add(stateCallback);
                    }
                    NetWorkManager.this.mNetStateCallbacks.removeAll(callbacks);
                }
            }
        });
    }

    private void onNetStateChanged(int newState) {
        Log.d(TAG, "onNetStateChanged(" + newState + ")");
        if (this.mCurrentState != newState) {
            for (int i = this.mStateChangedListeners.size() - 1; i >= 0; i--) {
                ((OnNetStateChangedListener) this.mStateChangedListeners.get(i)).onStateChanged(this.mCurrentState, newState);
            }
            this.mCurrentState = newState;
            this.mLastCheckTime = SystemClock.elapsedRealtime();
        }
    }

    private int request(String requestUrl, long timeStamp) {
        Log.d(TAG, ">> request(" + requestUrl + ", " + timeStamp + ")");
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(requestUrl).openConnection();
            Log.d(TAG, "request(" + requestUrl + ", " + timeStamp + "): openConnection");
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            if (conn.getResponseCode() >= 400 || conn.getResponseCode() <= 0) {
                Log.e(TAG, "<< request(" + requestUrl + ", " + timeStamp + "): connect internet failed, url=" + requestUrl);
                if (conn != null) {
                    conn.disconnect();
                }
                synchronized (this.mFutureTaskMap) {
                    this.mFutureTaskMap.remove(requestUrl);
                }
                return 1;
            }
            Log.d(TAG, "<< request(" + requestUrl + ", " + timeStamp + "): connect internet success, url=" + requestUrl);
            if (conn != null) {
                conn.disconnect();
            }
            synchronized (this.mFutureTaskMap) {
                this.mFutureTaskMap.remove(requestUrl);
            }
            return 2;
        } catch (Exception e) {
            Log.e(TAG, "<< request(" + requestUrl + ", " + timeStamp + "): exception happened", e);
            if (conn != null) {
                conn.disconnect();
            }
            synchronized (this.mFutureTaskMap) {
                this.mFutureTaskMap.remove(requestUrl);
                return 1;
            }
        } catch (Throwable th) {
            if (conn != null) {
                conn.disconnect();
            }
            synchronized (this.mFutureTaskMap) {
                this.mFutureTaskMap.remove(requestUrl);
            }
        }
    }

    private boolean tryConnectFirst() {
        int result;
        boolean z;
        long time = SystemClock.elapsedRealtime();
        try {
            result = ((Integer) getRequestTask(this.mRequestUrl[0]).get(this.mTimeOut, TIME_UNIT)).intValue();
        } catch (Exception e) {
            Log.e(TAG, "tryConnectFirst: exception happened", e);
            result = 1;
        }
        String str = TAG;
        StringBuilder stringBuilder = new StringBuilder("tryConnectFirst: result=");
        if (result == 2) {
            z = true;
        } else {
            z = false;
        }
        Log.d(str, stringBuilder.append(z).append(", time consumed=").append(SystemClock.elapsedRealtime() - time).append("ms").toString());
        if (result == 2) {
            return true;
        }
        return false;
    }

    private boolean retryConnectOthers() {
        int i;
        long time = SystemClock.elapsedRealtime();
        int length = this.mRequestUrl.length;
        ArrayList<FutureTask<Integer>> tasks = new ArrayList();
        for (i = 1; i < length; i++) {
            tasks.add(getRequestTask(this.mRequestUrl[i]));
        }
        boolean ret = false;
        int size = tasks.size();
        for (i = 0; i < size; i++) {
            if (i == 0) {
                try {
                    if (((Integer) ((FutureTask) tasks.get(i)).get(this.mTimeOut, TIME_UNIT)).intValue() == 2) {
                        ret = true;
                        break;
                    }
                } catch (Exception e) {
                    Log.e(TAG, "retryConnectOthers: exception happened", e);
                }
            } else if (((Integer) ((FutureTask) tasks.get(i)).get(0, TIME_UNIT)).intValue() == 2) {
                ret = true;
                break;
            }
        }
        Log.d(TAG, "retryConnectOthers: result=" + ret + ", time consumed=" + (SystemClock.elapsedRealtime() - time) + "ms");
        return ret;
    }

    private FutureTask<Integer> getRequestTask(final String url) {
        FutureTask<Integer> task;
        Log.d(TAG, ">> getRequestTask(" + url + ")");
        synchronized (this.mFutureTaskMap) {
            if (this.mFutureTaskMap.containsKey(url)) {
                task = (FutureTask) this.mFutureTaskMap.get(url);
                Log.d(TAG, "getRequestTask(" + url + "): use existing task");
            } else {
                final long timeStamp = SystemClock.elapsedRealtime();
                task = new FutureTask(new Callable<Integer>() {
                    public Integer call() throws Exception {
                        return Integer.valueOf(NetWorkManager.this.request(url, timeStamp));
                    }
                });
                this.mFutureTaskMap.put(url, task);
                mPool.submit(task);
                if (mPool instanceof ThreadPoolExecutor) {
                    ThreadPoolExecutor exec = mPool;
                    Log.d(TAG, "getRequestTask(" + url + "): current pool size=" + exec.getPoolSize() + ", queue=" + exec.getQueue());
                }
                Log.d(TAG, "getRequestTask(" + url + "): submitted task={" + task + ", " + timeStamp + "}");
            }
            Log.d(TAG, "<< getRequestTask(" + url + "): returned task=" + task);
        }
        return task;
    }

    public void registerStateChangedListener(final OnNetStateChangedListener internetStateChangedListener) {
        if (internetStateChangedListener == null) {
            throw new IllegalArgumentException("registerStateChangedListener: listener is null");
        }
        if (this.mStateChangedListeners.contains(internetStateChangedListener)) {
            synchronized (this.mLock) {
                this.mStateChangedListeners.remove(internetStateChangedListener);
            }
        }
        synchronized (this.mLock) {
            this.mStateChangedListeners.add(internetStateChangedListener);
        }
        this.mHandler.post(new Runnable() {
            public void run() {
                if (internetStateChangedListener != null && NetWorkManager.this.mStateChangedListeners.contains(internetStateChangedListener)) {
                    internetStateChangedListener.onStateChanged(NetWorkManager.this.mCurrentState, NetWorkManager.this.mCurrentState);
                }
            }
        });
    }

    public void unRegisterStateChangedListener(OnNetStateChangedListener internetStateChangedListener) {
        synchronized (this.mLock) {
            this.mStateChangedListeners.remove(internetStateChangedListener);
        }
    }

    public long getTimeOut() {
        return this.mTimeOut;
    }

    public void setTimeOut(long timeOut) {
        this.mTimeOut = timeOut;
    }

    public String[] getRequestUrl() {
        return this.mRequestUrl;
    }

    public void setRequestUrl(String[] requestUrl) {
        this.mRequestUrl = requestUrl;
    }

    public WifiInfo getWifiInfo() {
        return this.mWifiInfo;
    }

    public int getWifiStrength() {
        return this.mWifiInfo != null ? WifiManager.calculateSignalLevel(this.mWifiInfo.getRssi(), 5) : 0;
    }

    public String getWifiSsid() {
        return this.mWifiInfo != null ? this.mWifiInfo.getSSID() : null;
    }

    public void destroy() {
        unLanRegisterReceiver();
    }

    public static String getStateDescription(int state) {
        switch (state) {
            case 0:
                return "STATE_NONE";
            case 1:
                return "STATE_WIFI_NORMAL";
            case 2:
                return "STATE_WIRED_NORMAL";
            case 3:
                return "STATE_WIFI_ERROR";
            case 4:
                return "STATE_WIRED_ERROR";
            default:
                return "[unknown:" + state + AlbumEnterFactory.SIGN_STR;
        }
    }

    public int checkNetWorkSync() {
        if (this.mContext == null) {
            throw new IllegalStateException("You must call initNetWorkManager(Context context) first!");
        } else if (isNetworkAvailable() || this.mPppoeState) {
            boolean hasNet = tryConnectFirst();
            if (!hasNet) {
                hasNet = retryConnectOthers();
            }
            int result = getRealState(hasNet);
            notifyResult(result);
            return result;
        } else {
            notifyResult(0);
            return 0;
        }
    }

    private boolean isNetworkAvailable() {
        NetworkInfo info = ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo();
        if (info != null) {
            return info.isAvailable();
        }
        return false;
    }

    public boolean isReceiveSystemNetWorkConnectionMessage() {
        return this.mIsReceiveSystemNetworkConnectionMessage;
    }
}
