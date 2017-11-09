package com.push.pushservice.api;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import com.push.pushservice.IPushService;
import com.push.pushservice.IPushServiceCallback;
import com.push.pushservice.IPushServiceCallback.Stub;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.constants.MessageType;
import com.push.pushservice.constants.PushConstants;
import com.push.pushservice.data.BasicPushInitParam;
import com.push.pushservice.sharepreference.PushPrefUtils;
import com.push.pushservice.utils.DataUtil;
import com.push.pushservice.utils.LogUtils;
import com.push.pushservice.utils.NetUtils;
import com.push.pushservice.utils.PushUtils;
import java.io.UnsupportedEncodingException;
import java.util.Observable;
import java.util.Observer;
import org.cybergarage.xml.XML;

public class PushManager {
    public static final String TAG = "PushManager";
    private static PushManager instance = null;
    private static long seqID = 1;
    private static boolean userStop = false;
    private String appVer;
    private short appid = (short) -1;
    private String deviceId;
    private boolean isInit = false;
    private IPushServiceCallback mCallBack = new Stub() {
        public void response(Bundle mBundle) throws RemoteException {
            if (mBundle != null) {
                try {
                    LogUtils.logd(PushManager.TAG, "IPushServiceCallback response...");
                    if (PushManager.this.mPushObservable != null) {
                        String msg = mBundle.getString(DataConst.EXTRA_PUSH_MESSAGE);
                        short appid = mBundle.getInt("appid", -1);
                        LogUtils.logd("mCallBack response appid = " + appid);
                        if (appid == PushManager.getInstance().getAppid()) {
                            LogUtils.logd(PushManager.TAG, "IPushServiceCallback:" + msg);
                            PushManager.this.mPushObservable.notifyPush(msg);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.logd(PushManager.TAG, "IPushServiceCallback exception handler called");
                    PushManager.this.BinderService(PushManager.this.mContext);
                }
            }
        }
    };
    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName componentName) {
            LogUtils.logd(PushManager.TAG, "onServiceDisconnected");
            if (PushManager.getInstance().getService() != null) {
                try {
                    PushManager.getInstance().getService().unregisterCallback(null, PushManager.this.mCallBack);
                    PushUtils.sendErrorBroadcast(PushManager.this.getContext(), DataConst.EXTRA_UNBIND, PushManager.getInstance().getAppid(), 0, "");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    PushUtils.sendErrorBroadcast(PushManager.this.getContext(), DataConst.EXTRA_UNBIND, PushManager.getInstance().getAppid(), 10002, e.toString());
                } catch (Exception e2) {
                    e2.printStackTrace();
                    PushUtils.sendErrorBroadcast(PushManager.this.getContext(), DataConst.EXTRA_UNBIND, PushManager.getInstance().getAppid(), 20001, e2.toString());
                }
            }
            PushManager.this.mService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            LogUtils.logd(PushManager.TAG, "onServiceConnected....");
            PushManager.this.mService = IPushService.Stub.asInterface(service);
            if (PushManager.this.mService == null) {
                PushUtils.sendErrorBroadcast(PushManager.this.getContext(), DataConst.EXTRA_UNBIND, PushManager.getInstance().getAppid(), 20001, "服务绑定失败，原因未知！");
            } else {
                new Thread(new Runnable() {
                    public void run() {
                        try {
                            PushManager.getInstance().getService().registerCallback(null, PushManager.this.mCallBack);
                            Bundle bundle = new Bundle();
                            bundle.putInt("msgtype", 1003);
                            bundle.putShort("appid", PushManager.this.getAppid());
                            bundle.putString("package_name", PushManager.this.getPackageName());
                            bundle.putString(PushConstants.EXTRA_APP_VER, PushManager.this.getAppVer());
                            LogUtils.logd(PushManager.TAG, "onServiceConnected request +++");
                            PushManager.getInstance().getService().request(bundle);
                            LogUtils.logd(PushManager.TAG, "onServiceConnected request ---");
                            PushUtils.sendErrorBroadcast(PushManager.this.getContext(), DataConst.EXTRA_BIND, PushManager.getInstance().getAppid(), 0, "");
                        } catch (RemoteException e) {
                            e.printStackTrace();
                            LogUtils.logd(PushManager.TAG, "onServiceConnected  RemoteException:" + e.toString());
                            PushUtils.sendErrorBroadcast(PushManager.this.getContext(), DataConst.EXTRA_BIND, PushManager.getInstance().getAppid(), 10002, e.toString());
                        } catch (IllegalArgumentException e2) {
                            e2.printStackTrace();
                        } catch (Exception e3) {
                            e3.printStackTrace();
                        }
                    }
                }).start();
            }
        }
    };
    private Context mContext = null;
    private BasicPushInitParam mParam;
    private PushObservable mPushObservable = new PushObservable();
    private IPushService mService = null;
    private String packageName;

    private class PushObservable extends Observable {
        private PushObservable() {
        }

        public void notifyPush(Object message) {
            setChanged();
            notifyObservers(message);
        }
    }

    public static PushManager getInstance() {
        if (instance == null) {
            instance = new PushManager();
        }
        return instance;
    }

    public String getPackageName() {
        return this.packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppVer() {
        return this.appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public IPushService getService() {
        return this.mService;
    }

    public void setService(IPushService messenger) {
        this.mService = messenger;
    }

    public short getAppid() {
        return this.appid;
    }

    public void setAppid(short appid) {
        this.appid = appid;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceId() {
        return this.deviceId;
    }

    public Context getContext() {
        return this.mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    private void gInit(BasicPushInitParam param) {
        LogUtils.logd(TAG, "gInit version = 1.3.10-tv buildDate = 2017-0608-1043");
        this.mParam = param;
        if (param != null) {
            this.isInit = true;
            String newDeviceId = param.getAppId() + param.getDeviceId();
            if (newDeviceId.length() > 64) {
                newDeviceId = newDeviceId.substring(0, 64);
            }
            setAppid(param.getAppId());
            setPackageName(param.getPackageName());
            setAppVer(param.getAppVer());
            setDeviceId(newDeviceId);
            setContext(param.getContext());
            saveToPref(getContext(), param, newDeviceId);
        }
    }

    public static void init(BasicPushInitParam param) {
        getInstance().gInit(param);
    }

    private void saveToPref(Context context, BasicPushInitParam param, String deviceId) {
        PushPrefUtils.setDeviceId(context, deviceId);
        PushPrefUtils.setAppId(context, param.getAppId());
        PushPrefUtils.setAppVer(context, param.getAppVer());
        PushPrefUtils.setGlobalDeviceId(context, param.getDeviceId());
        PushPrefUtils.setPackageName(context, param.getPackageName());
    }

    private void startPush(Context context) {
        LogUtils.logd(TAG, "startPush");
        try {
            getInstance().BinderService(context);
        } catch (Exception e) {
        }
    }

    private void gStartWork(final Context context) {
        LogUtils.logd(TAG, "gStartWork");
        new Thread() {
            public void run() {
                String deviceId = PushManager.this.getDeviceId();
                if (PushManager.this.isInit && PushManager.this.mParam != null) {
                    boolean isPushType = NetUtils.getPushType(context, PushManager.this.mParam.getDeviceId(), deviceId, PushManager.this.mParam.getClientId(), PushManager.this.mParam.getPassPortId(), PushManager.this.mParam.getKeplerAppId(), PushManager.this.mParam.getAppVer(), PushManager.this.mParam.getLocalArea().value(), PushManager.this.mParam.getPlatForm());
                    LogUtils.logd(PushManager.TAG, "gStartWork new isPushType = " + isPushType);
                    if (PushManager.userStop) {
                        LogUtils.logd(PushManager.TAG, "userStop return");
                    } else if (isPushType) {
                        PushManager.this.startPush(context);
                    }
                }
            }
        }.start();
    }

    public static synchronized void startWork(Context context) {
        synchronized (PushManager.class) {
            LogUtils.logd(TAG, "startWork");
            userStop = false;
            getInstance().gStartWork(context);
        }
    }

    public static synchronized void stopWork(Context context) {
        synchronized (PushManager.class) {
            userStop = true;
            try {
                getInstance().UnbinderService(context);
            } catch (Exception e) {
            }
        }
    }

    public static void registerListener(Observer listener) {
        try {
            getInstance().registerPushListener(listener);
        } catch (Exception e) {
        }
    }

    public static void unRegisterListener(Observer listener) {
        try {
            getInstance().unRegisterPushListener(listener);
        } catch (Exception e) {
        }
    }

    public static boolean enableDebugMode(boolean debugEnabled) {
        try {
            LogUtils.setDebug(debugEnabled);
            if (getInstance().getService() == null) {
                return false;
            }
            Bundle bundle = new Bundle();
            bundle.putInt("msgtype", 1007);
            bundle.putBoolean(PushConstants.EXTRA_DEBUG_MODE, debugEnabled);
            getInstance().getService().request(bundle);
            return true;
        } catch (RemoteException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e2) {
            return false;
        }
    }

    public static void pushMessage(final Context context, final String userID, final String msg) {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Bundle bundle = new Bundle();
                    String topic = new String(DataUtil.mergeByteArray(new byte[]{MessageType.USER_TYPE, (byte) (appid >> 8), (byte) (PushManager.getInstance().getAppid() & 255)}, userID.getBytes(XML.CHARSET_UTF8)), PushConstants.CHARACTER_CODE);
                    bundle.putInt("msgtype", 1000);
                    bundle.putInt("appid", appid);
                    bundle.putString("topic", topic);
                    bundle.putString(PushConstants.EXTRA_INFO, msg);
                    if (PushManager.getInstance().getService() != null) {
                        PushManager.seqID = PushManager.seqID + 1;
                        bundle.putLong(DataConst.EXTRA_PUSH_MESSAGE_ID, PushManager.seqID);
                        PushManager.getInstance().getService().request(bundle);
                        return;
                    }
                    PushUtils.sendErrorBroadcast(context, DataConst.EXTRA_MESSAGE_CALLBACK, PushManager.getInstance().getAppid(), 10002, "你已经解绑的服务或者没有启动服务！请先启动服务试试!");
                } catch (RemoteException e) {
                    e.printStackTrace();
                    PushUtils.sendErrorBroadcast(context, DataConst.EXTRA_MESSAGE_CALLBACK, PushManager.getInstance().getAppid(), 10002, e.toString());
                } catch (UnsupportedEncodingException e2) {
                    e2.printStackTrace();
                    PushUtils.sendErrorBroadcast(context, DataConst.EXTRA_MESSAGE_CALLBACK, PushManager.getInstance().getAppid(), 20002, e2.toString());
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint({"NewApi"})
    private void BinderService(Context context) {
        LogUtils.logd(TAG, "BinderService +++");
        if (context == null) {
            try {
                LogUtils.logd(TAG, "BinderService context == null");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.mContext = context.getApplicationContext();
            Intent mIntent = new Intent("com.push.pushservice.api.PushService");
            mIntent.setPackage(getPackageName());
            this.mContext.startService(mIntent);
            if (!this.mContext.bindService(mIntent, this.mConnection, 1)) {
                LogUtils.logd(TAG, "bindService failure");
            }
            LogUtils.logd(TAG, "BinderService ---");
        }
    }

    @SuppressLint({"NewApi"})
    private void UnbinderService(Context context) {
        if (context == null) {
            LogUtils.logd(TAG, "UnbinderService context == null");
            return;
        }
        try {
            if (!(this.mService == null || this.mConnection == null)) {
                Bundle bundle = new Bundle();
                bundle.putInt("msgtype", 1004);
                bundle.putShort("appid", getAppid());
                bundle.putString("package_name", getPackageName());
                bundle.putString(PushConstants.EXTRA_APP_VER, getAppVer());
                getInstance().getService().request(bundle);
                this.mService.unregisterCallback(null, this.mCallBack);
                this.mContext = context.getApplicationContext();
                if (this.mContext != null) {
                    this.mContext.unbindService(this.mConnection);
                }
                PushUtils.sendErrorBroadcast(context, DataConst.EXTRA_UNBIND, getInstance().getAppid(), 0, "");
            }
        } catch (RemoteException e) {
            e.printStackTrace();
            PushUtils.sendErrorBroadcast(context, DataConst.EXTRA_UNBIND, getInstance().getAppid(), 10002, e.toString());
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (Exception e3) {
            e3.printStackTrace();
        }
        try {
            Intent mIntent = new Intent("com.push.pushservice.api.PushService");
            mIntent.setPackage(getPackageName());
            this.mContext = context.getApplicationContext();
            if (this.mContext.stopService(mIntent)) {
                LogUtils.logd(TAG, "UnbinderService stopService return true!");
            } else {
                LogUtils.logd(TAG, "UnbinderService stopService return false!");
            }
        } catch (Exception e32) {
            LogUtils.logd(TAG, "UnbinderService stopService is exception:" + e32.toString());
        }
        this.mService = null;
    }

    public void registerPushListener(Observer observer) {
        if (observer != null) {
            this.mPushObservable.addObserver(observer);
        }
    }

    public void unRegisterPushListener(Observer observer) {
        if (observer != null) {
            this.mPushObservable.deleteObserver(observer);
        }
    }
}
