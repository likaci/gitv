package com.gala.multiscreen.dmr.logic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.gala.android.dlna.sdk.mediarenderer.MediaRenderer;
import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.multiscreen.dmr.logic.listener.CustomListener;
import com.gala.multiscreen.dmr.logic.listener.RemoteListener;
import com.gala.multiscreen.dmr.logic.listener.StandardListener;
import com.gala.multiscreen.dmr.logic.listener.TVListener;
import com.gala.multiscreen.dmr.model.MSMessage;
import com.gala.multiscreen.dmr.model.msg.DlnaMessage;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.multiscreen.dmr.util.ContextProfile;
import com.gala.multiscreen.dmr.util.MSLog;
import com.gala.multiscreen.dmr.util.MSLog.LogType;
import com.gala.multiscreen.dmr.util.MsgBuilder;
import com.gala.multiscreen.dmr.util.NetProfile;
import com.gala.video.lib.share.common.configs.WebConstants;
import org.cybergarage.upnp.IconList;
import org.cybergarage.upnp.device.ST;

public class MSHelper {
    private static final String DEFAULT_DEVICE_NAME = "TCL-GALA-TV";
    public static final String DEVICE_TYPE_ONLY_QIMO = (ST.URN_DEVICE + Util.getTag(true) + "BOX:1");
    public static final String DEVICE_TYPE_QIMO_AND_STANDARD = "urn:schemas-upnp-org:device:MediaRenderer:1";
    private static final int IPCHANGE_MSG = 4;
    private static final int SEND_MSG = 3;
    private static final int START_MSG = 1;
    private static final int STOP_MSG = 2;
    private static final String THREAD_NAME = "Multiscreen_thread";
    private static final MSHelper gGalaDlna = new MSHelper();
    private boolean isLaunching = false;
    private String mDeviceType;
    private StandardListener mDlnaChannel = new StandardListener();
    private RemoteListener mGalaChannel1 = new RemoteListener();
    private CustomListener mGalaChannel2 = new CustomListener();
    private int mGalaDevice;
    private final IconList mIconList = new IconList();
    private MediaRenderer mMediaRenderer;
    private String mModeNumber = "";
    private String mName = "";
    private TVListener mOnlineChannel = new TVListener();
    private String mPackageName = "";
    private WorkHandler mWorkHandler;

    private class WorkHandler extends Handler {
        public WorkHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message msg) {
            MSLog.log("WorkHandler receive msg " + msg.what);
            switch (msg.what) {
                case 1:
                    MSHelper.this.doStart();
                    return;
                case 2:
                    MSHelper.this.doStop();
                    return;
                case 3:
                    MSHelper.this.doSendMessage((String) msg.obj);
                    return;
                case 4:
                    MSHelper.this.startAsync();
                    if (MSHelper.this.mMediaRenderer != null) {
                        MSHelper.this.mMediaRenderer.setIpListRefreshStatus(true);
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    private MSHelper() {
        HandlerThread handlerThread = new HandlerThread(THREAD_NAME);
        handlerThread.start();
        this.mWorkHandler = new WorkHandler(handlerThread.getLooper());
    }

    public static MSHelper get() {
        return gGalaDlna;
    }

    public void setName(String name) {
        if (name == null || "".equals(name)) {
            this.mName = DEFAULT_DEVICE_NAME;
        } else {
            this.mName = name;
        }
    }

    public void changeName(String name) {
        if (this.mMediaRenderer != null) {
            setName(name);
            String ip = NetProfile.getIp();
            if (ip != null) {
                this.mMediaRenderer.setFriendlyName(this.mName + "(" + NetProfile.getIpIndentity(ip) + ")");
            }
        }
    }

    public void setDeviceId(String deviceId) {
        this.mModeNumber = deviceId;
    }

    public void setPackageName(String packageName) {
        this.mPackageName = packageName;
    }

    public void setGalaDevice(int device) {
        this.mGalaDevice = device;
    }

    public void setDeviceType(String type) {
        this.mDeviceType = type;
    }

    public void setTvVersionString(String version) {
        MSMessage.VALUE_TV_VERSION = version;
        MSLog.log("setTvVersionString : " + version, LogType.PARAMETER);
    }

    public void addIcon(MSIcon icon) {
        MSLog.log("MultiScreen addIcon : " + icon, LogType.PARAMETER);
        if (icon != null) {
            this.mIconList.add(icon);
        }
    }

    public StandardListener getStandardDlna() {
        return this.mDlnaChannel;
    }

    public void sendMessage(DlnaMessage dlnaMessage) {
        if (this.mMediaRenderer != null) {
            String str = JSON.toJSONString(dlnaMessage);
            if (dlnaMessage instanceof Notify) {
                str = MsgBuilder.buildResultMsg((Notify) dlnaMessage);
            }
            Message msg = Message.obtain();
            msg.what = 3;
            msg.obj = str;
            this.mWorkHandler.sendMessage(msg);
        }
    }

    public void doSendMessage(String message) {
        MSLog.log("doSendMessage() message=" + message + ", render=" + this.mMediaRenderer, LogType.MS_TO_PHONE);
        if (this.mMediaRenderer != null) {
            this.mMediaRenderer.NotifyMessage(message);
        }
    }

    @SuppressLint({"NewApi"})
    public synchronized void startAsync() {
        MSLog.log("MultiScreen start", LogType.BASE);
        if (this.isLaunching || this.mName.isEmpty()) {
            MSLog.log("Returned unexpected isLaunching=" + this.isLaunching + ", mName=" + this.mName, LogType.BASE);
        } else {
            this.isLaunching = true;
            MSLog.log("MultiScreen ready to send msg 1", LogType.BASE);
            this.mWorkHandler.sendEmptyMessage(1);
        }
    }

    public synchronized void onNetWorkChange() {
        MSLog.log("MultiScreen ready to send msg 4", LogType.BASE);
        this.mWorkHandler.sendEmptyMessage(4);
    }

    private void doStart() {
        Context context = ContextProfile.getContext();
        MSLog.log("checkNetwork", LogType.BASE);
        if (twiceNetworkAvaliable(context)) {
            MSLog.log("installDlna", LogType.BASE);
            installDlna(context);
            delay(5000);
        }
        this.isLaunching = false;
    }

    private boolean twiceNetworkAvaliable(Context context) {
        if (!NetProfile.isAvaliable(context)) {
            delay(5000);
            if (!NetProfile.isAvaliable(context)) {
                return false;
            }
        }
        return true;
    }

    private void installDlna(Context context) {
        try {
            if (this.mMediaRenderer != null) {
                this.mMediaRenderer.stop();
            }
            this.mMediaRenderer = createMediaRenderer(context);
            if (this.mMediaRenderer != null) {
                MSLog.log("MultiScreen launch result : " + this.mMediaRenderer.start(), LogType.BASE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Error e2) {
            e2.printStackTrace();
        }
    }

    private MediaRenderer createMediaRenderer(Context context) {
        IconList iconList = null;
        String ip = NetProfile.getIp();
        if (ip == null) {
            return null;
        }
        MSLog.log("MultiScreen launch ip : " + ip + ", mPackageName=" + this.mPackageName, LogType.BASE);
        MediaRenderer mMediaRenderer = new MediaRenderer(1, 0);
        mMediaRenderer.setSdkVersion(2);
        mMediaRenderer.setManufacture("qDlna");
        mMediaRenderer.setManufactureURL(WebConstants.WEB_SITE_BASE_HTTP + this.mPackageName);
        mMediaRenderer.setFriendlyName(this.mName + "(" + NetProfile.getIpIndentity(ip) + ")");
        if (!this.mIconList.isEmpty()) {
            iconList = this.mIconList;
        }
        mMediaRenderer.setIconList(iconList);
        mMediaRenderer.setServerIP(ip);
        mMediaRenderer.setModelDescription("QDlna AV Media Renderer Device");
        mMediaRenderer.setModelName("QDlna AV Media Renderer Device");
        mMediaRenderer.setModelURL(WebConstants.WEB_SITE_BASE_HTTP + this.mPackageName + "/qDlna");
        mMediaRenderer.setModelNumber(this.mModeNumber);
        mMediaRenderer.setPackageName(this.mPackageName);
        mMediaRenderer.setIGALADEVICE(this.mGalaDevice);
        mMediaRenderer.setDeviceType(this.mDeviceType);
        if (context == null || context.getFilesDir() == null || context.getFilesDir().getPath() == null) {
            MSLog.log("MultiScreen set drm log path fail!", LogType.BASE);
        } else {
            Log.d("aaa", context.getFilesDir().getPath());
            mMediaRenderer.setDmrLogPath(context.getFilesDir().getPath() + "/");
            MSLog.log("MultiScreen set drm log path success!", LogType.BASE);
        }
        mMediaRenderer.initialize();
        this.mDlnaChannel.init(mMediaRenderer);
        this.mGalaChannel1.init(mMediaRenderer);
        this.mGalaChannel2.init(mMediaRenderer);
        this.mOnlineChannel.init(mMediaRenderer);
        return mMediaRenderer;
    }

    public void stop() {
        this.mWorkHandler.sendEmptyMessage(2);
    }

    private void doStop() {
        MSLog.log("doStop()!! renderer=" + this.mMediaRenderer);
        if (this.mMediaRenderer != null) {
            this.mMediaRenderer.stop();
        }
    }

    private void delay(int seconds) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
