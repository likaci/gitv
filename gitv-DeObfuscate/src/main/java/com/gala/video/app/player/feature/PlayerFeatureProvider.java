package com.gala.video.app.player.feature;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import com.gala.sdk.player.IPlayerFeature;
import com.gala.sdk.player.IPlayerProfile;
import com.gala.sdk.plugin.AbsPluginProvider;
import com.gala.sdk.plugin.HostPluginInfo;
import com.gala.sdk.plugin.LoadProviderException;
import com.gala.sdk.plugin.Result;
import com.gala.sdk.plugin.ResultCode.ERROR_TYPE;
import com.gala.sdk.plugin.server.PluginManager;
import com.gala.sdk.plugin.server.pingback.IPluginDeleteListener;
import com.gala.sdk.plugin.server.pingback.IPluginDownloadListener;
import com.gala.sdk.plugin.server.pingback.PluginPingbackParams;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.utils.MyPlayerProfile;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.internal.net.TrackingConstants;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class PlayerFeatureProvider {
    public static final int FAIL_COUNT_TO_JAR = 3;
    private static final String LOAD_PLUGIN_THREAD = "load-playerplugin";
    private static final String LastLoadDay = "playerpluginloadtime";
    private static final String LastLoadState = "playerpluginloadstate";
    private static final int MSG_LOAD_HASDIALOG = 2;
    private static final int MSG_LOAD_NODIALOG = 1;
    private static final int MS_DELAY_INIT = 6000;
    private static final String SP_PLAYER_PLUGIN = "spplayerplugin";
    private static final String TAG = "Player/PlayerFeatureProvider";
    private static PlayerFeatureProvider sInstance;
    private final IPluginDeleteListener mDeleteListener = new C14151();
    private final IPluginDownloadListener mDownloadlistnener = new C14162();
    private AtomicInteger mFailCount = new AtomicInteger(0);
    private HostPluginInfo mHPluginInfo = null;
    private final Object mHandlerLock = new Object();
    private Context mHostContext;
    private AtomicBoolean mIsFirstLoad = new AtomicBoolean(false);
    private AtomicBoolean mIsPlayerFeatureReady = new AtomicBoolean(false);
    private final Object mLock = new Object();
    private IPlayerFeature mPlayerFeature;
    private IPlayerProfile mPlayerProfile;
    private PluginManager mPluginManager;
    private String mSdkVersion = "";
    private WorkHandler mWorkHandler;

    class C14151 implements IPluginDeleteListener {
        C14151() {
        }

        public void deletedOldFile(PluginPingbackParams pluginPingbackParams) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PlayerFeatureProvider.TAG, "deletedOldFile");
            }
            Map<String, String> pingbackInfos = new HashMap();
            pingbackInfos.put("11", pluginPingbackParams.get("11"));
            pingbackInfos.put(PluginPingbackParams.PINGBACK_CT_PLUGIN_DELETE_OLD_FILE, pluginPingbackParams.get(PluginPingbackParams.PINGBACK_CT_PLUGIN_DELETE_OLD_FILE));
            pingbackInfos.put(PluginPingbackParams.PLUGINID, pluginPingbackParams.get(PluginPingbackParams.PLUGINID));
            pingbackInfos.put(PluginPingbackParams.DELETE_DEX, pluginPingbackParams.get(PluginPingbackParams.DELETE_DEX));
            pingbackInfos.put(PluginPingbackParams.DELETE_OLDCOUNT, pluginPingbackParams.get(PluginPingbackParams.DELETE_OLDCOUNT));
            pingbackInfos.put(PluginPingbackParams.DELETE_OLDNAME_LIST, pluginPingbackParams.get(PluginPingbackParams.DELETE_OLDNAME_LIST));
            pingbackInfos.put("td", pluginPingbackParams.get("td"));
            Map<String, String> map = new PingBackParams().build();
            map.putAll(pingbackInfos);
            PingBack.getInstance().postPingBackToLongYuan(map);
        }
    }

    class C14162 implements IPluginDownloadListener {
        C14162() {
        }

        public void downloaded(PluginPingbackParams pluginPingbackParams) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PlayerFeatureProvider.TAG, TrackingConstants.TRACKING_EVENT_DOWNLOADED);
            }
            Map<String, String> pingbackInfos = new HashMap();
            pingbackInfos.put("11", pluginPingbackParams.get("11"));
            pingbackInfos.put(PluginPingbackParams.PINGBACK_CT_PLUGIN_DOWNLOAD, pluginPingbackParams.get(PluginPingbackParams.PINGBACK_CT_PLUGIN_DOWNLOAD));
            pingbackInfos.put(PluginPingbackParams.PLUGINID, pluginPingbackParams.get(PluginPingbackParams.PLUGINID));
            pingbackInfos.put(PluginPingbackParams.DOWNLOAD_OLDVERSION, pluginPingbackParams.get(PluginPingbackParams.DOWNLOAD_OLDVERSION));
            pingbackInfos.put(PluginPingbackParams.DOWNLOAD_NEWVERSION, pluginPingbackParams.get(PluginPingbackParams.DOWNLOAD_NEWVERSION));
            pingbackInfos.put("success", pluginPingbackParams.get("success"));
            Map<String, String> map = new PingBackParams().build();
            map.putAll(pingbackInfos);
            PingBack.getInstance().postPingBackToLongYuan(map);
        }
    }

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            PluginStateChangedListener listener = null;
            if (msg.obj != null && (msg.obj instanceof PluginStateChangedListener)) {
                listener = msg.obj;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(PlayerFeatureProvider.TAG, ">> handleMessage() playerFeature=" + PlayerFeatureProvider.this.mPlayerFeature + ", listener=" + listener);
            }
            synchronized (PlayerFeatureProvider.this.mHandlerLock) {
                if (!PlayerFeatureProvider.this.isPlayerAlready()) {
                    PlayerFeatureProvider.this.loadPlayerFeatureAndInitialize(listener);
                } else if (listener != null) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(PlayerFeatureProvider.TAG, ">> handleMessage() onSuccess!!");
                    }
                    listener.onSuccess();
                }
            }
        }
    }

    private PlayerFeatureProvider() {
    }

    public static synchronized PlayerFeatureProvider instance() {
        PlayerFeatureProvider playerFeatureProvider;
        synchronized (PlayerFeatureProvider.class) {
            if (sInstance == null) {
                sInstance = new PlayerFeatureProvider();
            }
            playerFeatureProvider = sInstance;
        }
        return playerFeatureProvider;
    }

    public synchronized void initialize(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> initialize");
        }
        this.mPluginManager = PluginManager.instance();
        this.mPluginManager.setDeleteListener(this.mDeleteListener);
        this.mPluginManager.setDownloadListener(this.mDownloadlistnener);
        this.mIsFirstLoad.set(true);
        this.mHostContext = context.getApplicationContext();
        this.mPlayerProfile = new MyPlayerProfile();
        HandlerThread loadPluginThread = new HandlerThread(LOAD_PLUGIN_THREAD);
        loadPluginThread.setName("loadPluginThread");
        loadPluginThread.start();
        this.mWorkHandler = new WorkHandler(loadPluginThread.getLooper());
        this.mWorkHandler.sendEmptyMessageDelayed(1, 6000);
        this.mHPluginInfo = new HostPluginInfo("pluginplayer", Project.getInstance().getBuild().getVersionString());
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< initialize" + this.mHPluginInfo);
        }
    }

    public boolean isPlayerAlready() {
        return this.mIsPlayerFeatureReady.get();
    }

    public synchronized IPlayerFeature getPlayerFeature() {
        if (!isPlayerAlready()) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "why direct getPlayerFeature() !!??");
            }
            loadPlayerFeatureAndInitialize(null);
        }
        return this.mPlayerFeature;
    }

    public synchronized IPlayerFeature getPlayerFeatureOnlyInitJava() {
        loadPlayerFeature(null);
        initializePlayerJava(this.mHostContext, this.mPlayerFeature);
        return this.mPlayerFeature;
    }

    public void loadPlayerPluginAsync(Context activity, OnStateChangedListener listener, boolean showLoading) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "loadPlayerPluginAsync() playerFeature=" + this.mPlayerFeature + ", listener=" + listener);
        }
        if (isPlayerAlready()) {
            listener.onSuccess();
            return;
        }
        synchronized (this.mWorkHandler) {
            if (!(activity instanceof Activity)) {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1571e(TAG, "what is this" + activity);
                }
            }
            PluginStateChangedListener myListener = new PluginStateChangedListener(activity, Looper.myLooper(), listener);
            if (showLoading) {
                myListener.onLoading();
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "noLoading dialog() ");
            }
            loadPlayerPluginAsync(2, myListener);
        }
    }

    private void loadPlayerPluginAsync(int what, PluginStateChangedListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "loadPlayerPluginAsync() what=" + what + ", listener=" + listener);
        }
        Message msg = Message.obtain();
        msg.what = what;
        msg.obj = listener;
        this.mWorkHandler.sendMessage(msg);
    }

    private void loadPlayerFeature(PluginStateChangedListener listener) {
        synchronized (this.mLock) {
            if (this.mPlayerFeature == null) {
                long startTime = SystemClock.elapsedRealtime();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, ">> loadPluginPlayerFeature: mPlayerFeature=" + this.mPlayerFeature);
                }
                String eventId = PingBackUtils.createEventId();
                try {
                    long start = SystemClock.uptimeMillis();
                    Result<AbsPluginProvider> result = this.mPluginManager.loadProvider(this.mHPluginInfo);
                    long end = SystemClock.uptimeMillis();
                    if (result.getCode() == 0) {
                        for (LoadProviderException exception : result.getExceptions()) {
                            if (getMsgFromThrowable(exception.getThrowable()).contains(PluginStateChangedListener.ERROR_TYPE_NO_SPACE)) {
                                if (listener != null) {
                                    listener.setErrorType(PluginStateChangedListener.ERROR_TYPE_NO_SPACE);
                                }
                                this.mFailCount.set(3);
                                if (listener != null) {
                                    listener.setEventId(eventId);
                                    listener.setFailCount(this.mFailCount.get());
                                }
                                this.mFailCount.incrementAndGet();
                                LogUtils.m1573e(TAG, "loadPluginPlayerFeature() fail!", Integer.valueOf(this.mFailCount.get()));
                            }
                        }
                        if (listener != null) {
                            listener.setEventId(eventId);
                            listener.setFailCount(this.mFailCount.get());
                        }
                        this.mFailCount.incrementAndGet();
                        LogUtils.m1573e(TAG, "loadPluginPlayerFeature() fail!", Integer.valueOf(this.mFailCount.get()));
                    }
                    sendLoadPluginPingback(result, "pluginplayer", end - start, eventId);
                    if (this.mIsFirstLoad.get()) {
                        this.mIsFirstLoad.set(false);
                    }
                    if (result.getData() != null) {
                        this.mPlayerFeature = (IPlayerFeature) ((AbsPluginProvider) result.getData()).getFeature(1);
                    }
                } catch (Exception e) {
                    LogUtils.m1572e(TAG, "loadPluginPlayerFeature() fail!", e);
                }
                LogUtils.m1568d(TAG, "loadPluginPlayerFeature timeCost=" + (SystemClock.elapsedRealtime() - startTime));
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "mPlayerFeature already has =" + this.mPlayerFeature);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< loadPluginPlayerFeature: mPlayerFeature=" + this.mPlayerFeature);
        }
    }

    private synchronized void loadPlayerFeatureAndInitialize(PluginStateChangedListener listener) {
        if (this.mPlayerFeature == null) {
            loadPlayerFeature(listener);
        }
        if (this.mPlayerFeature != null) {
            testDealyLoadFeature();
            initializePlayer(this.mHostContext, this.mPlayerFeature);
            checkPlayerPluginConsistency();
            this.mIsPlayerFeatureReady.set(true);
        }
        if (isPlayerAlready() && listener != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, ">> loadPlayerFeatureAndInitialize() onSuccess!!");
            }
            listener.onSuccess();
        } else if (listener != null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, ">> loadPlayerFeatureAndInitialize() onFailed!!");
            }
            listener.onFailed();
        }
    }

    private String getMsgFromThrowable(Throwable throwable) {
        return throwable != null ? throwable.toString() : "NA";
    }

    private void sendLoadPluginPingback(Result<AbsPluginProvider> result, String pluginId, long td, String eventId) {
        Map<String, String> pingbackInfos = new HashMap();
        String st = "";
        st = String.valueOf(result.getCode());
        String loadPeriod = String.valueOf(result.getPeriod());
        pingbackInfos.put(ERROR_TYPE.ERROR_LOAD_ASSETS, "NA");
        pingbackInfos.put(ERROR_TYPE.ERROR_LOAD_DOWNLOAD, "NA");
        pingbackInfos.put(ERROR_TYPE.ERROR_LOAD_LOCAL, "NA");
        pingbackInfos.put(ERROR_TYPE.ERROR_LOAD_ASSETS_SD, "NA");
        for (LoadProviderException exception : result.getExceptions()) {
            pingbackInfos.put(exception.getType(), getMsgFromThrowable(exception.getThrowable()));
        }
        if (!(this.mPluginManager == null || this.mPluginManager.getProvider(this.mHPluginInfo) == null)) {
            this.mSdkVersion = this.mPluginManager.getProvider(this.mHPluginInfo).getVersionName();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "sendLoadPluginPingback pluginManager:" + this.mPluginManager + ", provider=" + (this.mPluginManager != null ? this.mPluginManager.getProvider(this.mHPluginInfo) : ""));
        }
        if (!st.equals("0") || this.mIsFirstLoad.get()) {
            pingbackInfos.put("td", String.valueOf(td));
            pingbackInfos.put("st", st);
            pingbackInfos.put("delay", String.valueOf(result.getDelayTime()));
            pingbackInfos.put("sdkv", this.mSdkVersion);
            pingbackInfos.put(PluginPingbackParams.PLUGINID, pluginId);
            pingbackInfos.put("ct", "160225_pluginload");
            pingbackInfos.put(Keys.FIRSTLOAD, this.mIsFirstLoad.get() ? "1" : "0");
            pingbackInfos.put("e", eventId);
            pingbackInfos.put("loadperiod", loadPeriod);
            pingbackInfos.put(Keys.f2035T, "11");
            pingbackInfos.put("istodayfirst", isTodayFirst(st) ? "1" : "0");
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "sendLoadPluginPingback: info=[" + pingbackInfos + AlbumEnterFactory.SIGN_STR);
            }
            Map<String, String> map = new PingBackParams().build();
            map.putAll(pingbackInfos);
            PingBack.getInstance().postPingBackToLongYuan(map);
            if (st.equals("0")) {
                new PlayerFeatureLogSender(new PlayerFeatureLogReader().getLogcatBuffer(), pingbackInfos).send();
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "no first fail");
        }
        saveDay(st);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "send tracker");
        }
    }

    private void saveDay(String st) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "saveDay");
        }
        AppPreference preference = new AppPreference(ResourceUtil.getContext(), SP_PLAYER_PLUGIN);
        int day = Calendar.getInstance().get(5);
        preference.save(LastLoadDay, String.valueOf(day));
        preference.save(LastLoadState, st);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "saveDay" + day);
        }
    }

    private boolean isTodayFirst(String st) {
        AppPreference preference = new AppPreference(ResourceUtil.getContext(), SP_PLAYER_PLUGIN);
        String lastLoadDayString = preference.get(LastLoadDay);
        int day = Calendar.getInstance().get(5);
        long lastLoadDay = (long) StringUtils.parse(lastLoadDayString, -1);
        String lastState = preference.get(LastLoadState);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "isTodayFirst now is ->" + day + " last is->" + lastLoadDay);
        }
        if (((long) day) == lastLoadDay && StringUtils.equals(st, lastState)) {
            return false;
        }
        return true;
    }

    private void initializePlayerJava(Context context, IPlayerFeature feature) {
        long startTime = SystemClock.elapsedRealtime();
        if (feature != null) {
            feature.initialize(this.mHostContext, this.mPlayerProfile, 101);
            LogUtils.m1568d(TAG, "initializePlayerJava timeCost=" + (SystemClock.elapsedRealtime() - startTime));
        }
    }

    private void initializePlayer(Context context, IPlayerFeature feature) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> initializePlayer: " + feature);
        }
        if (feature != null) {
            feature.initialize(this.mHostContext, this.mPlayerProfile, 100);
            if (!Project.getInstance().getBuild().supportPlayerMultiProcess()) {
                SystemConfigPreference.setPlayerCoreSupportDolby(context, feature.isSupportDolby());
                SystemConfigPreference.setPlayerCoreSupportH265(context, feature.isSupportH211());
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "<< initializePlayer: " + feature);
            }
        }
    }

    private int getDelayTimeSec() {
        int testDelaySeconds = PlayerDebugUtils.getTestPlayerPluginLoadDelayTime();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getDelayTimeS: return " + testDelaySeconds);
        }
        return testDelaySeconds;
    }

    private void testDealyLoadFeature() {
        int testDelaySeconds = getDelayTimeSec();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> testLoadPluginDelay: testDelayMs=" + testDelaySeconds);
        }
        if (testDelaySeconds > 0) {
            try {
                Thread.sleep((long) (testDelaySeconds * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< testLoadPluginDelay");
        }
    }

    public int getFailCount() {
        return this.mFailCount.get();
    }

    private void checkPlayerPluginConsistency() {
    }
}
