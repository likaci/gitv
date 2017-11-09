package com.push.pushservice.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.push.pushservice.constants.DataConst;
import com.push.pushservice.data.AppInfo;
import com.push.pushservice.data.AppInfoManager;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.List;

public class PushUtils {
    private static final String TAG = "PushUtils";
    private static final long WAY_MASK = 240;

    public static long getDeltDays(long oldTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        int nowDay = cal.get(6);
        int nowYear = cal.get(1);
        cal.setTimeInMillis(oldTime);
        return (long) (((nowYear * 365) + nowDay) - ((cal.get(1) * 365) + cal.get(6)));
    }

    public static String encodeMD5(String str) {
        if (!TextUtils.isEmpty(str)) {
            byte[] content = DataUtil.stringToByteAry(str);
            if (!(content == null || content.length == 0)) {
                try {
                    MessageDigest mdInst = MessageDigest.getInstance("MD5");
                    mdInst.reset();
                    mdInst.update(content);
                    byte[] byteArray = mdInst.digest();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < byteArray.length; i++) {
                        if (Integer.toHexString(byteArray[i] & 255).length() == 1) {
                            sb.append("0").append(Integer.toHexString(byteArray[i] & 255));
                        } else {
                            sb.append(Integer.toHexString(byteArray[i] & 255));
                        }
                    }
                    str = sb.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    public static void delay(long mis) {
        try {
            Thread.sleep(mis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sendErrorBroadcast(Context context, int type, int appid, int errcode, String msg) {
        if (context != null) {
            Intent intent = new Intent("com.push.pushservice.action.RECEIVE");
            intent.putExtra(DataConst.EXTRA_ERROR_TYPE, type);
            intent.putExtra("appid", appid);
            intent.putExtra(DataConst.EXTRA_ERROR_CODE, errcode);
            intent.putExtra(DataConst.EXTRA_ERROR_MSG, msg);
            context.sendBroadcast(intent);
        }
    }

    public static void sendErrorBroadcast(Context context, int type, int appid, int errcode, long msgSeqId, String msg) {
        if (context != null) {
            Intent intent = new Intent("com.push.pushservice.action.RECEIVE");
            intent.putExtra(DataConst.EXTRA_ERROR_TYPE, type);
            intent.putExtra("appid", appid);
            intent.putExtra(DataConst.EXTRA_ERROR_CODE, errcode);
            intent.putExtra(DataConst.EXTRA_ERROR_MSG, msg);
            intent.putExtra(DataConst.EXTRA_PUSH_MESSAGE_ID, msgSeqId);
            context.sendBroadcast(intent);
        }
    }

    public static void sendMessage(Context context, String msg, int appId, long msgId) {
        if (context == null || appId < 0) {
            LogUtils.logd(TAG, "sendMessage error appId = " + appId);
            return;
        }
        Intent intent = new Intent("com.push.pushservice.action.MESSAGE");
        intent.setAction("com.push.pushservice.action.MESSAGE");
        intent.putExtra(DataConst.EXTRA_PUSH_MESSAGE, DataUtil.getNotNullString(msg));
        intent.putExtra("appid", appId);
        intent.putExtra(DataConst.EXTRA_PUSH_MESSAGE_ID, msgId);
        List<AppInfo> appList = AppInfoManager.getInstance(context.getApplicationContext()).getInfo(context.getApplicationContext()).getAppList();
        if (appList != null && appList.size() > 0) {
            for (int idx = 0; idx < appList.size(); idx++) {
                AppInfo appInfo = (AppInfo) appList.get(idx);
                if (appId != appInfo.getAppid()) {
                    LogUtils.logd(TAG, "appId =  " + appId + "  appInfo.getAppid" + appInfo.getAppid());
                } else {
                    String packageName = appInfo.getPackageName();
                    if (!TextUtils.isEmpty(packageName)) {
                        reflectReceiver(context, packageName, intent);
                        LogUtils.logd(TAG, "sendMessage msg to " + packageName);
                    }
                }
            }
        }
    }

    private static String getReceiver(Context context, String paramString1, String paramString2) {
        if (TextUtils.isEmpty(paramString1) || TextUtils.isEmpty(paramString2) || context == null) {
            return null;
        }
        String str = null;
        Intent localIntent = new Intent(paramString2);
        localIntent.setPackage(paramString1);
        List localList = context.getPackageManager().queryBroadcastReceivers(localIntent, 0);
        if (localList != null && localList.size() > 0) {
            for (int i = 0; i < localList.size(); i++) {
                str = ((ResolveInfo) localList.get(i)).activityInfo.name;
                if (str != null) {
                    break;
                }
            }
        }
        LogUtils.logd(TAG, "str = " + str);
        return str;
    }

    private static void reflectReceiver(Context ctx, String packageName, Intent paramIntent) {
        if (ctx == null) {
            LogUtils.logd(TAG, "reflectReceiver error ctx = null");
        } else if (TextUtils.isEmpty(packageName)) {
            LogUtils.logd(TAG, "reflectReceiver error packageName = null");
            sendBroadcast(ctx, packageName, paramIntent);
        } else {
            String str = getReceiver(ctx, packageName, paramIntent.getAction());
            if (TextUtils.isEmpty(str)) {
                LogUtils.logd(TAG, " reflectReceiver error: receiver for: " + paramIntent.getAction() + " not found, package: " + packageName);
                sendBroadcast(ctx, packageName, paramIntent);
                return;
            }
            try {
                LogUtils.logd(TAG, "reflectReceiver calling onReceive() for package: " + packageName);
                Context context = ctx.createPackageContext(packageName, 3);
                LogUtils.logd(TAG, "reflectReceiver applicationContext = " + context.getPackageName());
                Class localClass = Class.forName(str, true, context.getClassLoader());
                Object localObject1 = localClass.getConstructor(new Class[0]).newInstance(new Object[0]);
                Method localMethod = localClass.getMethod("onReceive", new Class[]{Context.class, Intent.class});
                paramIntent.setClassName(packageName, str);
                localMethod.invoke(localObject1, new Object[]{context, paramIntent});
            } catch (Exception e) {
                LogUtils.logd(TAG, "reflectReceiver e: " + e);
                LogUtils.logd(TAG, "reflectReceiver broadcast: " + packageName);
                e.printStackTrace();
                sendBroadcast(ctx, packageName, paramIntent);
            }
        }
    }

    private static void sendBroadcast(Context ctx, String packageName, Intent paramIntent) {
        if (ctx == null || paramIntent == null) {
            LogUtils.logd(TAG, "sendBroadcast param error");
            return;
        }
        if (!TextUtils.isEmpty(packageName)) {
            paramIntent.setPackage(packageName);
        }
        ctx.sendBroadcast(paramIntent);
    }

    public static boolean isNewGlobalMessage(long messageId) {
        return ((int) (((WAY_MASK & messageId) >> 4) & 15)) == 6;
    }

    public static String getSecretKey(int appId) {
        switch (appId) {
            case 1020:
                return "c2b75b2101570efd1bbd4e257ad00dfb";
            case 1021:
                return "f563406cd2f24054aac3691fcbfb2d89";
            case 1022:
                return "515c0e12e56930574016319ca91ae147";
            case 1023:
                return "310120dcd3c01f8718559c7d1657c621";
            default:
                return null;
        }
    }
}
