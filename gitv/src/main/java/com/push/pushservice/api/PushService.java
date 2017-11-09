package com.push.pushservice.api;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Process;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.text.TextUtils;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.mcto.ads.internal.net.PingbackConstants;
import com.push.mqttv3.MqttCallback;
import com.push.mqttv3.MqttClient;
import com.push.mqttv3.MqttConnectOptions;
import com.push.mqttv3.MqttDeliveryToken;
import com.push.mqttv3.MqttException;
import com.push.mqttv3.MqttPersistenceException;
import com.push.mqttv3.MqttTopic;
import com.push.mqttv3.internal.MemoryPersistence;
import com.push.mqttv3.internal.wire.MqttReceivedMessage;
import com.push.nativeprocess.NativeProcess;
import com.push.nativeprocess.WatchDog;
import com.push.pushservice.IPushService.Stub;
import com.push.pushservice.IPushServiceCallback;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.constants.MessageType;
import com.push.pushservice.constants.PushConstants;
import com.push.pushservice.data.AppInfo;
import com.push.pushservice.data.AppInfoManager;
import com.push.pushservice.data.AppListInfo;
import com.push.pushservice.net.HttpUtils;
import com.push.pushservice.pingback.PingBackAgent;
import com.push.pushservice.sharepreference.PushPrefUtils;
import com.push.pushservice.utils.LogUtils;
import com.push.pushservice.utils.NetUtils;
import com.push.pushservice.utils.PushUtils;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

@SuppressLint({"NewApi"})
public class PushService extends Service implements MqttCallback {
    private static final boolean MQTT_CLEAN_SESSION = true;
    private static short MQTT_KEEP_ALIVE_INTERVAL_SECOND = (short) 240;
    private static short MQTT_KEEP_ALIVE_INTERVAL_SECOND_NO_WIFI = (short) 240;
    private static short MQTT_KEEP_ALIVE_INTERVAL_SECOND_WIFI = (short) 240;
    private static final int MQTT_KEEP_ALIVE_QOS = 1;
    public static final String TAG = "PushService";
    public static boolean isServiceStop = false;
    private static AlarmManager mAlarmManager = null;
    private static MqttClient mClient = null;
    private static boolean mStarted = false;
    public static Object mSyncLock = new Object();
    private static boolean misForceStop = false;
    private String currentUrl = "";
    private List<String> hostList = null;
    private int lastNetType = -1;
    private Stub mBinder = new Stub() {
        public int request(Bundle mBundle) throws RemoteException {
            LogUtils.logd("PushService", "+++ request +++");
            PushService.this.handRequest(mBundle);
            LogUtils.logd("PushService", "--- request ---");
            return 0;
        }

        public void registerCallback(Bundle mBundle, IPushServiceCallback cb) throws RemoteException {
            if (cb != null && PushService.this.mCallbacks != null) {
                LogUtils.logd("PushService", "registerCallback isRegistered:" + PushService.this.mCallbacks.register(cb));
            }
        }

        public void unregisterCallback(Bundle mBundle, IPushServiceCallback cb) throws RemoteException {
            if (cb != null && PushService.this.mCallbacks != null) {
                LogUtils.logd("PushService", "registerCallback isUnregistered" + PushService.this.mCallbacks.unregister(cb));
            }
        }
    };
    private final CusRemoteCallbackList<IPushServiceCallback> mCallbacks = new CusRemoteCallbackList();
    private final BroadcastReceiver mConnectivityReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = (NetworkInfo) intent.getParcelableExtra("networkInfo");
            if (info == null) {
                PushService.this.lastNetType = -1;
                PushService.this.stop(false);
                LogUtils.logd("PushService", "NetworkInfo is null ! stop the service");
            } else if (PushService.misForceStop) {
                LogUtils.logd("PushService", "mConnectivityReceiver is not valid,because misForceStop is true");
            } else {
                int netType = info.getType();
                if (PushService.this.lastNetType != netType) {
                    boolean hasConnectivity;
                    PushService.this.lastNetType = netType;
                    if (info == null || !info.isConnected()) {
                        hasConnectivity = false;
                    } else {
                        hasConnectivity = true;
                    }
                    LogUtils.logd("PushService", "new Connectivity changed: connected=" + hasConnectivity);
                    if (hasConnectivity) {
                        LogUtils.logd("PushService", "reconnect... is true!!");
                        PushService.this.reconnectIfNecessary();
                        return;
                    }
                    PushService.this.lastNetType = -1;
                    PushService.this.stop(false);
                    LogUtils.logd("PushService", "hasConnectivity  is false! stop it! lastNetType = -1 !!");
                    return;
                }
                LogUtils.logd("PushService", "NetType no changge.So doing nothing!");
            }
        }
    };
    private MemoryPersistence mMemStore = null;
    private MqttConnectOptions mOpts = new MqttConnectOptions();
    private final BroadcastReceiver mRegistrationReceiver = new BroadcastReceiver() {
        public void onReceive(Context arg0, Intent arg1) {
            String packageName = arg1.getDataString().substring(8);
            if (packageName != null && packageName.length() > 0) {
                AppListInfo appListInfo = AppInfoManager.getInstance(PushService.this.getApplicationContext()).getInfo(PushService.this.getApplicationContext());
                if (appListInfo != null) {
                    AppInfo appinfo = appListInfo.getAppInfoSame(packageName);
                    if (appinfo != null) {
                        int appid = appinfo.getAppid();
                        LogUtils.logd("PushService", "packageName remove appid:" + appid);
                        if (appid > 0) {
                            String cmd = String.format("{\"cmd\":%d,\"appId\":%d}", new Object[]{Byte.valueOf(MessageType.SYS_TYPE), Integer.valueOf(appid)});
                            try {
                                String topic = new String(new byte[]{MessageType.SYS_TYPE}, PushConstants.CHARACTER_CODE);
                                LogUtils.logd("PushService", "packageName remove:" + packageName);
                                PushService.this.publishMessage(topic, cmd, appid, -1);
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            } catch (Exception e2) {
                            }
                        }
                    }
                }
            }
        }
    };

    private class CusRemoteCallbackList<E extends IInterface> extends RemoteCallbackList<E> {
        private CusRemoteCallbackList() {
        }

        public void onCallbackDied(E callback) {
            LogUtils.logd("PushService", "CusRemoteCallbackList onCallbackDied 1");
            super.onCallbackDied(callback);
        }

        public void onCallbackDied(E callback, Object cookie) {
            LogUtils.logd("PushService", "CusRemoteCallbackList onCallbackDied 2");
            super.onCallbackDied(callback, cookie);
        }
    }

    private List<String> getHostList() {
        if (this.hostList == null) {
            AppInfoManager appInfoManager = AppInfoManager.getInstance(getApplicationContext());
            AppListInfo appListinfo = appInfoManager.getInfo(getApplicationContext());
            if (appListinfo != null) {
                this.hostList = appListinfo.getHostList();
                if (this.hostList != null && this.hostList.size() == 0) {
                    String[] list = getBrokerList(2);
                    if (list != null) {
                        for (String host : list) {
                            this.hostList.add(host);
                        }
                        appInfoManager.saveInfo(getApplicationContext(), appListinfo);
                        LogUtils.logd("PushService", " hostList save success!");
                    }
                    return this.hostList;
                }
            }
        }
        return this.hostList;
    }

    private List<String> refreshHostList() {
        int i = 0;
        String[] list = getBrokerList(2);
        if (list == null || list.length == 0) {
            return null;
        }
        AppInfoManager appInfoManager = AppInfoManager.getInstance(getApplicationContext());
        AppListInfo appListinfo = appInfoManager.getInfo(getApplicationContext());
        int length;
        if (appListinfo == null) {
            this.hostList = new ArrayList();
            length = list.length;
            while (i < length) {
                this.hostList.add(list[i]);
                i++;
            }
            return this.hostList;
        }
        this.hostList = appListinfo.getHostList();
        if (this.hostList == null) {
            return null;
        }
        this.hostList.clear();
        for (String host : list) {
            this.hostList.add(host);
        }
        appInfoManager.saveInfo(getApplicationContext(), appListinfo);
        return this.hostList;
    }

    public String getCurrentUrl() {
        return this.currentUrl;
    }

    public void setCurrentUrl(String currentUrl) {
        this.currentUrl = currentUrl;
    }

    private String[] getBrokerList(int version) {
        synchronized (mSyncLock) {
            String deviceId = PushPrefUtils.getDeviceId(getApplicationContext());
            String globalDeviceId = PushPrefUtils.getGlobalDeviceId(getApplicationContext());
            String appid = String.valueOf(PushPrefUtils.getAppId(getApplicationContext()));
            String brokersUrl = PushConstants.SERVER_GET_BROKERS_URL;
            List params = new ArrayList();
            params.add(new BasicNameValuePair("ver", version + ""));
            params.add(new BasicNameValuePair(DataConst.APP_INFO_APP_ID, appid));
            params.add(new BasicNameValuePair("device_id", deviceId));
            params.add(new BasicNameValuePair("platform_type", PushConstants.PLATFORM_TYPE));
            params.add(new BasicNameValuePair("api_level", VERSION.SDK_INT + ""));
            String result = HttpUtils.doGetRequestForString(brokersUrl, params);
            if (result != null && result.length() > 0) {
                try {
                    JSONObject json = new JSONObject(result);
                    String code = json.getString(PingbackConstants.CODE);
                    if (code == null || !code.equals(IAlbumConfig.NET_ERROE_CODE)) {
                        LogUtils.logd("PushService", " getBrokerList failure:" + result);
                    } else {
                        String data = json.getString("data");
                        if (data != null && data.length() > 0) {
                            String[] strArray = data.split(",");
                            LogUtils.logd("PushService", " getBrokerList success:" + result);
                            PingBackAgent.sendHostListStatisticsAsync(code, deviceId, globalDeviceId);
                            return strArray;
                        }
                    }
                    PingBackAgent.sendHostListStatisticsAsync(code, deviceId, globalDeviceId);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e2) {
                    LogUtils.logd("PushService", "getBrokerList e = " + e2);
                }
            }
            return null;
        }
    }

    public void actionDispatch(Context ctx, String action) {
        Intent i = new Intent(ctx, PushService.class);
        i.setAction(action);
        i.setPackage(getPackageName());
        ctx.startService(i);
    }

    public IBinder onBind(Intent intent) {
        if (intent == null || PushService.class.getName() == null || !PushService.class.getName().equals(intent.getAction())) {
            return null;
        }
        handleCommand(intent);
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    public void onRebind(Intent intent) {
    }

    private boolean initMqttSSLContext() {
        String password = updatePassword();
        if (TextUtils.isEmpty(password)) {
            return false;
        }
        this.mOpts.setCleanSession(true);
        Context context = getApplicationContext();
        if (context != null) {
            if (NetUtils.isWifi(context)) {
                MQTT_KEEP_ALIVE_INTERVAL_SECOND = MQTT_KEEP_ALIVE_INTERVAL_SECOND_WIFI;
            } else if (NetUtils.isMobile(context)) {
                MQTT_KEEP_ALIVE_INTERVAL_SECOND = MQTT_KEEP_ALIVE_INTERVAL_SECOND_NO_WIFI;
            }
        }
        this.mOpts.setKeepAliveInterval(MQTT_KEEP_ALIVE_INTERVAL_SECOND);
        this.mOpts.setConnectionTimeout(10);
        this.mOpts.setUserName(updateUsername());
        this.mOpts.setPassword(password.toCharArray());
        return true;
    }

    private void init() {
        disconnect();
        this.mMemStore = new MemoryPersistence();
        mAlarmManager = (AlarmManager) getSystemService("alarm");
        connect();
    }

    private String updateUsername() {
        byte[] rawheader = new byte[5];
        rawheader[0] = (byte) 0;
        rawheader[1] = (byte) 2;
        rawheader[2] = (byte) 0;
        rawheader[3] = (byte) 24;
        int netType = NetUtils.getNetType(getApplicationContext());
        LogUtils.logd("PushService", "netType:" + netType);
        rawheader[4] = (byte) (netType & 255);
        try {
            String userName = new String(rawheader, "UTF-8");
            if (userName != null && userName.length() > 0) {
                return userName;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (Exception e2) {
        }
        return "";
    }

    private String updatePassword() {
        short appId = (short) PushPrefUtils.getAppId(getApplicationContext());
        String appVer = PushPrefUtils.getAppVer(getApplicationContext());
        LogUtils.logd("PushService", "updatePassword appId = " + appId + " appVer = " + appVer);
        if (appId <= (short) 0 || TextUtils.isEmpty(appVer)) {
            return null;
        }
        return appId + "|" + appVer;
    }

    @SuppressLint({"NewApi"})
    public void onCreate() {
        super.onCreate();
        LogUtils.logd("PushService", "onCreate enter---");
        if (VERSION.SDK_INT > 9) {
            StrictMode.setThreadPolicy(new Builder().permitAll().build());
        }
        String processName = IMsgUtils.PUSH_SERVICE_NAME;
        LogUtils.logd("PushService", "onCreate call JNI ---");
        NativeProcess.setPackageName(getPackageName());
        NativeProcess.setServiceName(PushService.class.getName());
        if (NativeProcess.useNativeProcess()) {
            NativeProcess.create(this, WatchDog.class, processName);
        }
        registerBroadcastReceive();
        int appId = PushPrefUtils.getAppId(getApplicationContext());
        LogUtils.logd("PushService", "init appId = " + appId);
        if (appId > 0) {
            new Thread() {
                public void run() {
                    PushService.this.init();
                }
            }.start();
        }
    }

    @SuppressLint({"NewApi"})
    public void onDestroy() {
        LogUtils.logd("PushService", ".......onDestroy begin.....");
        super.onDestroy();
        if (mClient != null) {
            try {
                mClient.disconnect();
            } catch (MqttException e) {
                e.printStackTrace();
            } catch (Exception e2) {
            }
            mClient = null;
        }
        if (this.mCallbacks != null) {
            this.mCallbacks.kill();
        }
        unRegisterBroadcastReceive();
        LogUtils.logd("PushService", "onDestroy end.....");
        LogUtils.logd("PushService", "onDestroy start again");
        Intent mIntent = new Intent(PushService.class.getName());
        mIntent.setPackage(getPackageName());
        startService(mIntent);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtils.logd("PushService", "onStartCommand ---");
        handleCommand(intent);
        return 1;
    }

    private void handleCommand(Intent intent) {
        if (intent == null) {
            LogUtils.logd("PushService", "handleCommand() pid:" + Process.myPid() + " tid:" + Process.myTid());
            return;
        }
        String action = intent.getAction();
        if (action == null) {
            LogUtils.logd("PushService", "Starting service with no action  Probably from a crash");
            return;
        }
        LogUtils.logd("PushService", "handleCommand() pid:" + Process.myPid() + " tid:" + Process.myTid() + " action = " + action);
        if (action.equals(PushConstants.ACTION_START)) {
            LogUtils.logd("PushService", "Received action ACTION_START");
            isServiceStop = false;
            start();
        } else if (action.equals(PushConstants.ACTION_STOP)) {
            LogUtils.logd("PushService", "Received action ACTION_STOP");
            isServiceStop = true;
            stop(false);
            LogUtils.logd("PushService", "stop(false) finish ++++++++++++++++++++++");
        } else if (action.equals(PushConstants.ACTION_SET_KEEPALIVE)) {
            Bundle bundle = intent.getBundleExtra(PushConstants.EXTRA_BUNDLE_DATA);
            if (bundle != null) {
                int keepAliveTimes = bundle.getInt(PushConstants.EXTRA_KEEP_ALIVE_TIMES, MQTT_KEEP_ALIVE_INTERVAL_SECOND);
                if (keepAliveTimes <= 0) {
                    keepAliveTimes = MQTT_KEEP_ALIVE_INTERVAL_SECOND;
                }
                sendKeepAliveTimes(keepAliveTimes);
                LogUtils.logd("PushService", "Received action ACTION_KEEPALIVE :" + keepAliveTimes);
                return;
            }
            sendKeepAliveTimes(MQTT_KEEP_ALIVE_INTERVAL_SECOND);
            LogUtils.logd("PushService", "Received action ACTION_KEEPALIVE DEFAULT");
        } else if (action.equals(PushConstants.ACTION_FORCE_STOP)) {
            misForceStop = true;
            stop(true);
            LogUtils.logd("PushService", "ForceStop is true");
        } else if (action.equals(PushConstants.ACTION_SCHEDULE_CONNECT)) {
            if (NetUtils.isNetworkAvailable(getApplicationContext()) && !misForceStop) {
                start();
                LogUtils.logd("PushService", "Received action ACTION_SCHEDULE_CONNECT  and Network is Available and misForceStop is false");
            } else if (misForceStop) {
                LogUtils.logd("PushService", "Received action ACTION_SCHEDULE_CONNECT  and misForceStop is true. Not connect it!");
            } else {
                LogUtils.logd("PushService", "Received action ACTION_SCHEDULE_CONNECT  and Network is not Available. Not connect it!");
            }
        } else if (action.equals(PushConstants.ACTION_SCHEDULE_DISCONNECT)) {
            misForceStop = true;
            stop(true);
            LogUtils.logd("PushService", "Received action ACTION_SCHEDULE_DISCONNECT");
        } else if (action.equals(PushConstants.ACTION_PONG)) {
            LogUtils.logd("PushService", "Received action ACTION_PONG");
        } else if (action.equals(PushConstants.ACTION_CONNECTIONLOST)) {
            LogUtils.logd("PushService", "Received action ACTION_CONNECTIONLOST");
        } else if (action.equals(PushConstants.ACTION_PINGTIMER)) {
            LogUtils.logd("PushService", "Received action ACTION_PINGTIMER");
        } else if (action.equals("com.push.pushservice.api.PushService")) {
            LogUtils.logd("PushService", "Received action SERVICE_CLASSNAME");
            checkAppId();
            start();
        } else {
            LogUtils.logd("PushService", "Received action default  action" + action);
            checkAppId();
        }
    }

    private void registerBroadcastReceive() {
        LogUtils.logd("PushService", "registerBroadcastReceive call");
        try {
            registerReceiver(this.mConnectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            registerReceiver(this.mRegistrationReceiver, new IntentFilter("android.intent.action.PACKAGE_REMOVED"));
        } catch (Exception e) {
            LogUtils.logd("PushService", "registerBroadcastReceive failure");
        }
    }

    private void unRegisterBroadcastReceive() {
        if (this.mRegistrationReceiver != null) {
            try {
                unregisterReceiver(this.mRegistrationReceiver);
            } catch (IllegalArgumentException e) {
            } catch (Exception e2) {
            }
        }
        if (this.mConnectivityReceiver != null) {
            try {
                unregisterReceiver(this.mConnectivityReceiver);
            } catch (IllegalArgumentException e3) {
                e3.printStackTrace();
            } catch (Exception e4) {
                e4.printStackTrace();
            }
        }
        LogUtils.logd("PushService", "unRegisterBroadcastReceive call");
    }

    private synchronized void start() {
        if (mStarted) {
            LogUtils.logd("PushService", "Attempt to start while already started");
            checkAppId();
        } else {
            isServiceStop = false;
            new Thread() {
                public void run() {
                    PushService.this.connect();
                }
            }.start();
        }
    }

    private synchronized void stop(boolean isforce) {
        LogUtils.logd("PushService", "call stop");
        if (!mStarted) {
            LogUtils.logd("PushService", "Attemtpign to stop connection that isn't running");
        } else if (isforce) {
            if (this.mConnectivityReceiver != null) {
                try {
                    unregisterReceiver(this.mConnectivityReceiver);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void checkAppId() {
        int appId = PushPrefUtils.getAppId(getApplicationContext());
        LogUtils.logd("PushService", "checkAppId appId = " + appId);
        if (appId <= 0) {
            if (isConnected()) {
                disconnect();
            }
            stopTryConnectTast();
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void connect() {
        /*
        r19 = this;
        r16 = mSyncLock;
        monitor-enter(r16);
        r15 = r19.getApplicationContext();	 Catch:{ all -> 0x0072 }
        r2 = com.push.pushservice.sharepreference.PushPrefUtils.getDeviceId(r15);	 Catch:{ all -> 0x0072 }
        r15 = r19.getApplicationContext();	 Catch:{ all -> 0x0072 }
        r1 = com.push.pushservice.sharepreference.PushPrefUtils.getAppId(r15);	 Catch:{ all -> 0x0072 }
        r15 = r19.getApplicationContext();	 Catch:{ all -> 0x0072 }
        r5 = com.push.pushservice.sharepreference.PushPrefUtils.getGlobalDeviceId(r15);	 Catch:{ all -> 0x0072 }
        r15 = "PushService";
        r17 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r17.<init>();	 Catch:{ all -> 0x0072 }
        r18 = "connect id = ";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r17 = r0.append(r2);	 Catch:{ all -> 0x0072 }
        r18 = " appid = ";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r17 = r0.append(r1);	 Catch:{ all -> 0x0072 }
        r17 = r17.toString();	 Catch:{ all -> 0x0072 }
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        r15 = android.text.TextUtils.isEmpty(r2);	 Catch:{ all -> 0x0072 }
        if (r15 != 0) goto L_0x004e;
    L_0x004c:
        if (r1 > 0) goto L_0x005c;
    L_0x004e:
        r15 = r19.isConnected();	 Catch:{ all -> 0x0072 }
        if (r15 == 0) goto L_0x0057;
    L_0x0054:
        r19.disconnect();	 Catch:{ all -> 0x0072 }
    L_0x0057:
        r19.stopTryConnectTast();	 Catch:{ all -> 0x0072 }
        monitor-exit(r16);	 Catch:{ all -> 0x0072 }
    L_0x005b:
        return;
    L_0x005c:
        r15 = r19.isConnected();	 Catch:{ all -> 0x0072 }
        if (r15 == 0) goto L_0x0075;
    L_0x0062:
        r15 = "PushService";
        r17 = "connect return true ,just return !";
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        r19.stopTryConnectTast();	 Catch:{ all -> 0x0072 }
        monitor-exit(r16);	 Catch:{ all -> 0x0072 }
        goto L_0x005b;
    L_0x0072:
        r15 = move-exception;
        monitor-exit(r16);	 Catch:{ all -> 0x0072 }
        throw r15;
    L_0x0075:
        r7 = r19.getHostList();	 Catch:{ all -> 0x0072 }
        r10 = 0;
        r4 = "";
        r12 = 1;
        if (r7 == 0) goto L_0x015c;
    L_0x0080:
        r13 = r19.initMqttSSLContext();	 Catch:{ all -> 0x0072 }
        if (r13 == 0) goto L_0x0232;
    L_0x0086:
        r6 = new java.util.HashMap;	 Catch:{ all -> 0x0072 }
        r6.<init>();	 Catch:{ all -> 0x0072 }
        r11 = r7.size();	 Catch:{ all -> 0x0072 }
        r9 = 0;
        r8 = 0;
    L_0x0091:
        r15 = r7.size();	 Catch:{ all -> 0x0072 }
        if (r8 >= r15) goto L_0x015c;
    L_0x0097:
        r0 = r19;
        r9 = r0.selectHostId(r11);	 Catch:{ all -> 0x0072 }
        r15 = java.lang.Integer.valueOf(r9);	 Catch:{ all -> 0x0072 }
        r15 = r6.containsKey(r15);	 Catch:{ all -> 0x0072 }
        if (r15 == 0) goto L_0x00b3;
    L_0x00a7:
        r15 = r6.size();	 Catch:{ all -> 0x0072 }
        r17 = r7.size();	 Catch:{ all -> 0x0072 }
        r0 = r17;
        if (r15 != r0) goto L_0x0097;
    L_0x00b3:
        r15 = java.lang.Integer.valueOf(r9);	 Catch:{ all -> 0x0072 }
        r17 = java.lang.Integer.valueOf(r9);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r6.put(r15, r0);	 Catch:{ all -> 0x0072 }
        r14 = r7.get(r9);	 Catch:{ all -> 0x0072 }
        r14 = (java.lang.String) r14;	 Catch:{ all -> 0x0072 }
        r15 = "PushService";
        r17 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r17.<init>();	 Catch:{ all -> 0x0072 }
        r18 = "host index:";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r17 = r0.append(r9);	 Catch:{ all -> 0x0072 }
        r18 = " Connecting with URL log: ";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r17 = r0.append(r14);	 Catch:{ all -> 0x0072 }
        r17 = r17.toString();	 Catch:{ all -> 0x0072 }
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        r15 = "PushService";
        r17 = new java.lang.StringBuilder;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r17.<init>();	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r18 = "Connecting with MemStore getmDeviceId：";
        r17 = r17.append(r18);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r0 = r17;
        r17 = r0.append(r2);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r17 = r17.toString();	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r15 = new com.push.mqttv3.MqttClient;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r0 = r19;
        r0 = r0.mMemStore;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r17 = r0;
        r0 = r17;
        r15.<init>(r14, r2, r0);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        mClient = r15;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r15 = mClient;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r0 = r19;
        r0 = r0.mOpts;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r17 = r0;
        r0 = r17;
        r15.connect(r0);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r15 = mClient;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r0 = r19;
        r15.setCallback(r0);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r15 = 0;
        misForceStop = r15;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r15 = 1;
        mStarted = r15;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r10 = 1;
        r0 = r19;
        r0.setCurrentUrl(r14);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r15 = "PushService";
        r17 = new java.lang.StringBuilder;	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r17.<init>();	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r18 = "Successfully connected ：";
        r17 = r17.append(r18);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r0 = r17;
        r17 = r0.append(r14);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r17 = r17.toString();	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ MqttException -> 0x019e, Exception -> 0x01f0 }
    L_0x015c:
        if (r10 != 0) goto L_0x0235;
    L_0x015e:
        r15 = isServiceStop;	 Catch:{ all -> 0x0072 }
        if (r15 != 0) goto L_0x0235;
    L_0x0162:
        r15 = "PushService";
        r17 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r17.<init>();	 Catch:{ all -> 0x0072 }
        r18 = "connect fail needRefreshHost = ";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r17 = r0.append(r12);	 Catch:{ all -> 0x0072 }
        r17 = r17.toString();	 Catch:{ all -> 0x0072 }
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        if (r12 == 0) goto L_0x0188;
    L_0x0182:
        com.push.pushservice.pingback.PingBackAgent.sendConnectionStatisticsAsync(r4, r2, r5);	 Catch:{ all -> 0x0072 }
        r19.refreshHostList();	 Catch:{ all -> 0x0072 }
    L_0x0188:
        r15 = -1;
        r0 = r19;
        r0.lastNetType = r15;	 Catch:{ all -> 0x0072 }
        r15 = "PushService";
        r17 = "所有的连接方法都失败，开始执行重试连接操作!";
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        r19.startTryConnectTast();	 Catch:{ all -> 0x0072 }
    L_0x019b:
        monitor-exit(r16);	 Catch:{ all -> 0x0072 }
        goto L_0x005b;
    L_0x019e:
        r3 = move-exception;
        r15 = 0;
        mClient = r15;	 Catch:{ all -> 0x0072 }
        r10 = 0;
        r15 = "PushService";
        r17 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r17.<init>();	 Catch:{ all -> 0x0072 }
        r18 = "connect url:";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r17 = r0.append(r14);	 Catch:{ all -> 0x0072 }
        r18 = "fail";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r18 = r3.toString();	 Catch:{ all -> 0x0072 }
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r17 = r17.toString();	 Catch:{ all -> 0x0072 }
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        r15 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r15.<init>();	 Catch:{ all -> 0x0072 }
        r17 = "";
        r0 = r17;
        r15 = r15.append(r0);	 Catch:{ all -> 0x0072 }
        r17 = r3.getReasonCode();	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r15 = r15.append(r0);	 Catch:{ all -> 0x0072 }
        r4 = r15.toString();	 Catch:{ all -> 0x0072 }
    L_0x01ec:
        r8 = r8 + 1;
        goto L_0x0091;
    L_0x01f0:
        r3 = move-exception;
        r15 = "PushService";
        r17 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0072 }
        r17.<init>();	 Catch:{ all -> 0x0072 }
        r18 = "Exception connect url:";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r0 = r17;
        r17 = r0.append(r14);	 Catch:{ all -> 0x0072 }
        r18 = "fail！e.getStackTrace():";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r18 = r3.toString();	 Catch:{ all -> 0x0072 }
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r18 = " e.getMessage:";
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r18 = r3.getMessage();	 Catch:{ all -> 0x0072 }
        r17 = r17.append(r18);	 Catch:{ all -> 0x0072 }
        r17 = r17.toString();	 Catch:{ all -> 0x0072 }
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        r4 = "-1";
        r10 = 0;
        goto L_0x01ec;
    L_0x0232:
        r12 = 0;
        goto L_0x015c;
    L_0x0235:
        r15 = "PushService";
        r17 = "连接成功，取消重连的尝试!";
        r0 = r17;
        com.push.pushservice.utils.LogUtils.logd(r15, r0);	 Catch:{ all -> 0x0072 }
        r19.stopTryConnectTast();	 Catch:{ all -> 0x0072 }
        goto L_0x019b;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.push.pushservice.api.PushService.connect():void");
    }

    private int selectHostId(int max) {
        if (max > 0) {
            return Math.abs(new Random(System.currentTimeMillis()).nextInt()) % max;
        }
        return 0;
    }

    private synchronized void startTryConnectTast() {
        LogUtils.logd("PushService", "重新连接机制启动！");
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(PushConstants.ACTION_SCHEDULE_CONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        if (!(pi == null || mAlarmManager == null)) {
            mAlarmManager.setRepeating(0, System.currentTimeMillis() + 300000, 300000, pi);
        }
    }

    private synchronized void stopTryConnectTast() {
        LogUtils.logd("PushService", "重新连接机制停止！");
        Intent i = new Intent();
        i.setClass(this, PushService.class);
        i.setAction(PushConstants.ACTION_SCHEDULE_CONNECT);
        PendingIntent pi = PendingIntent.getService(this, 0, i, 0);
        if (!(mAlarmManager == null || pi == null)) {
            mAlarmManager.cancel(pi);
        }
    }

    private synchronized void sendKeepAliveTimes(final int keepAliveTimes) {
        if (isConnected()) {
            new Thread() {
                public void run() {
                    PushService.this.sendKeepAlive(keepAliveTimes);
                }
            }.start();
        }
    }

    private synchronized void reconnectIfNecessary() {
        if (mStarted || mClient != null) {
            checkAppId();
            LogUtils.logd("PushService", "reconnectIfNecessary do nothing!");
        } else {
            LogUtils.logd("PushService", "reconnectIfNecessary begin to run!!!");
            new Thread() {
                public void run() {
                    PushUtils.delay(IOpenApiCommandHolder.OAA_CONNECT_INTERVAL);
                    PushService.this.connect();
                }
            }.start();
        }
    }

    public boolean isConnected() {
        if (mClient != null && mStarted && mClient.isConnected()) {
            return true;
        }
        return false;
    }

    public void disconnect() {
        LogUtils.logd("PushService", "call disconnect");
        if (mClient != null) {
            try {
                mClient.disconnect();
            } catch (MqttPersistenceException e) {
                LogUtils.logd("PushService", "MqttException" + e.toString());
            } catch (MqttException e2) {
                e2.printStackTrace();
                LogUtils.logd("PushService", "MqttException" + e2.toString());
            } catch (Exception e3) {
            }
        }
        mStarted = false;
        mClient = null;
    }

    private synchronized void sendKeepAlive(int keepAliveTimes) {
        if (isConnected()) {
            byte[] header = new byte[]{MessageType.KEEPALIVE_TYPE};
            try {
                if (mClient != null) {
                    MqttTopic mKeepAliveTopic = mClient.getTopic(new String(header, PushConstants.CHARACTER_CODE));
                    LogUtils.logd("PushService", "Sending Keepalive to " + getCurrentUrl());
                    MqttReceivedMessage message = new MqttReceivedMessage(new byte[]{(byte) (keepAliveTimes >> 8), (byte) (keepAliveTimes & 255)});
                    message.setQos(0);
                    if (mKeepAliveTopic != null) {
                        try {
                            mKeepAliveTopic.publish(message);
                        } catch (MqttPersistenceException e) {
                            e.printStackTrace();
                            LogUtils.logd("PushService", e.getMessage());
                        } catch (MqttException e2) {
                            e2.printStackTrace();
                            LogUtils.logd("PushService", "心跳发送异常！" + e2.getMessage());
                        } catch (Exception e3) {
                        }
                    }
                }
            } catch (UnsupportedEncodingException e4) {
                e4.printStackTrace();
            } catch (Exception e5) {
            }
        }
    }

    private boolean publishMessage(String topic, String msg, int appid, long msgSeqId) {
        final int i = appid;
        final long j = msgSeqId;
        final String str = topic;
        final String str2 = msg;
        new Thread(new Runnable() {
            public void run() {
                if (PushService.mClient == null) {
                    PushUtils.sendErrorBroadcast(PushService.this.getApplicationContext(), DataConst.EXTRA_MESSAGE_CALLBACK, i, 20004, j, "你已经强制关闭了service与云推送服务器之间的连接，所以请先执行重新连接，再发送消息");
                    return;
                }
                if (PushService.mClient.isConnected()) {
                    LogUtils.logd("PushService", "mClient.isConnected() == true");
                } else {
                    LogUtils.logd("PushService", "I am not connected,So I can not publish Message.Now I must connect at once");
                    PushService.this.connect();
                    if (PushService.mClient == null) {
                        PushUtils.sendErrorBroadcast(PushService.this.getApplicationContext(), DataConst.EXTRA_MESSAGE_CALLBACK, i, 20004, j, "你已经强制关闭了service与云推送服务器之间的连接，所以请先执行重新连接，再发送消息");
                        return;
                    }
                }
                try {
                    MqttTopic mTopic = PushService.mClient.getTopic(str);
                    if (mTopic == null) {
                        LogUtils.logd("PushService", "mTopic == null");
                        PushUtils.sendErrorBroadcast(PushService.this.getApplicationContext(), DataConst.EXTRA_MESSAGE_CALLBACK, i, 20003, j, "mTopic为空，这是不正常的！");
                        return;
                    }
                    MqttDeliveryToken toekn = mTopic.publish(new MqttReceivedMessage(str2.getBytes()));
                    LogUtils.logd("PushService", "msgSeqId: " + j);
                    if (!(j == -1 || toekn == null)) {
                        LogUtils.logd("PushService", "publishMessage : " + str2);
                        PushUtils.sendErrorBroadcast(PushService.this.getApplicationContext(), DataConst.EXTRA_MESSAGE_CALLBACK, i, 0, j, "");
                    }
                    if (toekn == null) {
                        LogUtils.logd("PushService", "toekn ==null " + j);
                    }
                } catch (MqttPersistenceException e) {
                    e.printStackTrace();
                    if (j != -1) {
                        PushUtils.sendErrorBroadcast(PushService.this.getApplicationContext(), DataConst.EXTRA_MESSAGE_CALLBACK, i, 20003, j, "发送消息异常");
                    }
                } catch (MqttException e2) {
                    e2.printStackTrace();
                    if (j != -1) {
                        PushUtils.sendErrorBroadcast(PushService.this.getApplicationContext(), DataConst.EXTRA_MESSAGE_CALLBACK, i, 20003, j, e2.toString());
                    }
                } catch (Exception e3) {
                    PushUtils.sendErrorBroadcast(PushService.this.getApplicationContext(), DataConst.EXTRA_MESSAGE_CALLBACK, i, 20003, j, "发送消息异常，原因未知");
                }
            }
        }).start();
        return false;
    }

    private void saveLog(short appid, String deviceId, String packageName, String appVer) {
        AppInfoManager appInfoManager = AppInfoManager.getInstance(getApplicationContext());
        AppListInfo appListInfo = appInfoManager.getInfo(getApplicationContext());
        if (appListInfo != null) {
            appListInfo.addAppInfo(appid, deviceId, packageName, appVer);
            appInfoManager.saveInfo(getApplicationContext(), appListInfo);
        }
    }

    public void handRequest(Bundle bundle) {
        if (bundle != null) {
            int what = bundle.getInt("msgtype");
            LogUtils.logd("PushService", "what:" + what);
            switch (what) {
                case 1000:
                    String topic = bundle.getString("topic");
                    String info = bundle.getString(PushConstants.EXTRA_INFO);
                    publishMessage(topic, info, bundle.getInt("appid"), bundle.getLong(DataConst.EXTRA_PUSH_MESSAGE_ID));
                    LogUtils.logd("PushService", "MESSAGE_TYPE_PUSH request:" + info);
                    return;
                case 1003:
                    short appid2 = bundle.getShort("appid");
                    String packageName = bundle.getString("package_name");
                    String appVer = bundle.getString(PushConstants.EXTRA_APP_VER);
                    String deviceId = PushPrefUtils.getDeviceId(getApplicationContext());
                    LogUtils.logd("PushService", "SERVICE_START step a1");
                    saveLog(appid2, deviceId, packageName, appVer);
                    LogUtils.logd("PushService", "SERVICE_START step a2");
                    actionDispatch(this, PushConstants.ACTION_START);
                    LogUtils.logd("PushService", "SERVICE_START request, deviceid:" + deviceId + " appid2:" + appid2);
                    return;
                case 1004:
                    actionDispatch(this, PushConstants.ACTION_STOP);
                    LogUtils.logd("PushService", "SERVICE_STOP request");
                    return;
                case 1007:
                    boolean debugOn = bundle.getBoolean(PushConstants.EXTRA_DEBUG_MODE);
                    LogUtils.setDebug(debugOn);
                    LogUtils.logd("PushService", "SET_DEBUG_ON_OF request:" + debugOn);
                    return;
                default:
                    return;
            }
        }
    }

    public void connectionLost(Throwable arg0) {
        LogUtils.logd("PushService", "connectionLost = true");
        if (!isConnected() && !misForceStop) {
            LogUtils.logd("PushService", "connectionLost is true! begin to start again!");
            actionDispatch(this, PushConstants.ACTION_CONNECTIONLOST);
            PushUtils.delay(10000);
            disconnect();
            start();
        }
    }

    public void deliveryComplete(MqttDeliveryToken arg0) {
    }

    public void messageArrived(MqttTopic topic, MqttReceivedMessage message) throws Exception {
        dispatchMsg(topic, message);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void dispatchMsg(com.push.mqttv3.MqttTopic r21, com.push.mqttv3.internal.wire.MqttReceivedMessage r22) {
        /*
        r20 = this;
        r3 = r20.getApplicationContext();
        r5 = com.push.pushservice.utils.NetUtils.getDetailNetType(r3);
        r3 = r20.getApplicationContext();
        r4 = com.push.pushservice.sharepreference.PushPrefUtils.getDeviceId(r3);
        r3 = r20.getApplicationContext();
        r8 = com.push.pushservice.sharepreference.PushPrefUtils.getGlobalDeviceId(r3);
        if (r21 != 0) goto L_0x0021;
    L_0x001a:
        r3 = -1;
        r6 = 0;
        com.push.pushservice.pingback.PingBackAgent.sendMessageStatisticsAsync(r3, r4, r5, r6, r8);
    L_0x0020:
        return;
    L_0x0021:
        if (r22 != 0) goto L_0x002a;
    L_0x0023:
        r3 = -2;
        r6 = 0;
        com.push.pushservice.pingback.PingBackAgent.sendMessageStatisticsAsync(r3, r4, r5, r6, r8);
        goto L_0x0020;
    L_0x002a:
        r3 = r21.getName();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = "ISO-8859-1";
        r0 = r18;
        r14 = r3.getBytes(r0);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = "PushService";
        r18 = new java.lang.StringBuilder;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18.<init>();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r19 = "messageArrived: header[0] = ";
        r18 = r18.append(r19);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r19 = 0;
        r19 = r14[r19];	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = r18.append(r19);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = r18.toString();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        com.push.pushservice.utils.LogUtils.logd(r3, r0);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        if (r14 == 0) goto L_0x0080;
    L_0x0059:
        r3 = r14.length;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = 1;
        r0 = r18;
        if (r3 != r0) goto L_0x0080;
    L_0x0060:
        r3 = 0;
        r3 = r14[r3];	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = com.push.pushservice.constants.MessageType.SYS_TYPE;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        if (r3 != r0) goto L_0x0080;
    L_0x0069:
        r3 = -3;
        r6 = 0;
        com.push.pushservice.pingback.PingBackAgent.sendMessageStatisticsAsync(r3, r4, r5, r6, r8);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        goto L_0x0020;
    L_0x0070:
        r13 = move-exception;
        r13.printStackTrace();
        r3 = "PushService";
        r18 = "Mqtt接收到消息出了异常！";
        r0 = r18;
        com.push.pushservice.utils.LogUtils.logd(r3, r0);
        goto L_0x0020;
    L_0x0080:
        if (r14 == 0) goto L_0x00b5;
    L_0x0082:
        r3 = r14.length;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = 1;
        r0 = r18;
        if (r3 != r0) goto L_0x00b5;
    L_0x0089:
        r3 = 0;
        r3 = r14[r3];	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = com.push.pushservice.constants.MessageType.KEEPALIVE_TYPE;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        if (r3 != r0) goto L_0x00b5;
    L_0x0092:
        r3 = -4;
        r6 = 0;
        com.push.pushservice.pingback.PingBackAgent.sendMessageStatisticsAsync(r3, r4, r5, r6, r8);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = "PushService.PONG";
        r0 = r20;
        r1 = r20;
        r0.actionDispatch(r1, r3);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        goto L_0x0020;
    L_0x00a4:
        r13 = move-exception;
        r13.printStackTrace();
        r3 = "PushService";
        r18 = "字符编码错误，这个错误是由服务器的引起的";
        r0 = r18;
        com.push.pushservice.utils.LogUtils.logd(r3, r0);
        goto L_0x0020;
    L_0x00b5:
        r2 = 0;
        r3 = r14.length;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = 3;
        r0 = r18;
        if (r3 != r0) goto L_0x00dd;
    L_0x00bd:
        r3 = 0;
        r3 = r14[r3];	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = com.push.pushservice.constants.MessageType.USER_TYPE;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        if (r3 == r0) goto L_0x00cf;
    L_0x00c6:
        r3 = 0;
        r3 = r14[r3];	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = com.push.pushservice.constants.MessageType.IM_TYPE;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        if (r3 != r0) goto L_0x00dd;
    L_0x00cf:
        r3 = 1;
        r3 = r14[r3];	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = r3 << 8;
        r9 = (short) r3;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = 2;
        r3 = r14[r3];	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = r3 & 255;
        r10 = (short) r3;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r2 = r9 + r10;
    L_0x00dd:
        r17 = new java.lang.String;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = r22.getPayload();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r17;
        r0.<init>(r3);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r6 = r22.getMessageId();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = "PushService";
        r18 = new java.lang.StringBuilder;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18.<init>();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r19 = "messageArrived:";
        r18 = r18.append(r19);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        r1 = r17;
        r18 = r0.append(r1);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r19 = " msg_id:";
        r18 = r18.append(r19);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        r18 = r0.append(r6);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r19 = " appid = ";
        r18 = r18.append(r19);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        r18 = r0.append(r2);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = r18.toString();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        com.push.pushservice.utils.LogUtils.logd(r3, r0);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = 0;
        com.push.pushservice.pingback.PingBackAgent.sendMessageStatisticsAsync(r3, r4, r5, r6, r8);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = com.push.pushservice.utils.PushUtils.isNewGlobalMessage(r6);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        if (r3 == 0) goto L_0x016d;
    L_0x0130:
        r3 = "PushService";
        r18 = new java.lang.StringBuilder;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18.<init>();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r19 = "msgID = ";
        r18 = r18.append(r19);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        r18 = r0.append(r6);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r19 = " is a global message";
        r18 = r18.append(r19);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = r18.toString();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r18;
        com.push.pushservice.utils.LogUtils.logd(r3, r0);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r18 = com.push.pushservice.sharepreference.PushPrefUtils.getMsgId(r20);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = (r6 > r18 ? 1 : (r6 == r18 ? 0 : -1));
        if (r3 <= 0) goto L_0x016d;
    L_0x015d:
        r3 = "PushService";
        r18 = "update the global msgID in SP";
        r0 = r18;
        com.push.pushservice.utils.LogUtils.logd(r3, r0);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r20;
        com.push.pushservice.sharepreference.PushPrefUtils.setMsgId(r0, r6);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
    L_0x016d:
        r3 = r20.getApplicationContext();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r17;
        com.push.pushservice.utils.PushUtils.sendMessage(r3, r0, r2, r6);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r16 = new android.os.Bundle;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r16.<init>();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = "message";
        r0 = r16;
        r1 = r17;
        r0.putString(r3, r1);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3 = "appid";
        r0 = r16;
        r0.putInt(r3, r2);	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r0 = r20;
        r3 = r0.mCallbacks;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r11 = r3.beginBroadcast();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r15 = r11 + -1;
    L_0x0197:
        if (r15 < 0) goto L_0x01b6;
    L_0x0199:
        r0 = r20;
        r3 = r0.mCallbacks;	 Catch:{ Exception -> 0x01ab, MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4 }
        r3 = r3.getBroadcastItem(r15);	 Catch:{ Exception -> 0x01ab, MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4 }
        r3 = (com.push.pushservice.IPushServiceCallback) r3;	 Catch:{ Exception -> 0x01ab, MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4 }
        r0 = r16;
        r3.response(r0);	 Catch:{ Exception -> 0x01ab, MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4 }
    L_0x01a8:
        r15 = r15 + -1;
        goto L_0x0197;
    L_0x01ab:
        r12 = move-exception;
        r12.printStackTrace();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        goto L_0x01a8;
    L_0x01b0:
        r12 = move-exception;
        r12.printStackTrace();
        goto L_0x0020;
    L_0x01b6:
        r0 = r20;
        r3 = r0.mCallbacks;	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        r3.finishBroadcast();	 Catch:{ MqttException -> 0x0070, UnsupportedEncodingException -> 0x00a4, Exception -> 0x01b0 }
        goto L_0x0020;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.push.pushservice.api.PushService.dispatchMsg(com.push.mqttv3.MqttTopic, com.push.mqttv3.internal.wire.MqttReceivedMessage):void");
    }
}
