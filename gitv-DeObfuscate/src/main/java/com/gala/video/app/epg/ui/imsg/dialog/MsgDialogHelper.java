package com.gala.video.app.epg.ui.imsg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import com.gala.video.app.epg.apkupgrade.UpdateManager;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher;
import com.gala.video.app.epg.ui.imsg.dialog.MsgDialog.MsgDialogStatusListener;
import com.gala.video.app.epg.ui.imsg.dialog.MsgDialog.OnClickListener;
import com.gala.video.app.epg.ui.imsg.utils.DialogClickUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.imsg.IMsgReceiver;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifimpl.imsg.utils.Permissionschecker;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgDialogCacheManager;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgOnMsgListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.MsgDialogParams;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class MsgDialogHelper {
    private static final String TAG = "imsg/MsgDialogHelper";
    private static IMsgDialogCacheManager cacheManager = GetInterfaceTools.getMsgCenter().getMsgDialogCache();
    private static MsgDialogHelper gProcessor;
    private static ExecutorService mExec = Executors.newSingleThreadExecutor(new C08891());
    private boolean isOutApp = true;
    private boolean isShow = false;
    private long mLastShowTime = 0;
    private IMsgOnMsgListener mMsgListener;
    private Permissionschecker mPermissionschecker;
    private Context sContext = null;
    private boolean sIsHomeActivity = false;

    static class C08891 implements ThreadFactory {
        C08891() {
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setPriority(1);
            return t;
        }
    }

    class C08912 implements IMsgOnMsgListener {
        C08912() {
        }

        public void onMessage(final Context context, final IMsgContent content) {
            if (MsgDialogHelper.this.isOutApp && context != null && !IMsgUtils.isAppLive(context)) {
                LogUtils.m1568d(MsgDialogHelper.TAG, "onReceivermsg, content = " + content.toString());
                ThreadUtils.execute(new Runnable() {
                    public void run() {
                        MsgDialogHelper.cacheManager.onOutAppMsg(content);
                        MsgDialogHelper.this.show(context, true);
                    }
                });
            }
        }
    }

    private MsgDialogHelper(Context context) {
        boolean z = true;
        if (!(IMsgUtils.isOutApp(context) && checkPremission(context))) {
            z = false;
        }
        this.isOutApp = z;
    }

    public static MsgDialogHelper get() {
        if (gProcessor == null) {
            gProcessor = new MsgDialogHelper(AppRuntimeEnv.get().getApplicationContext());
        }
        return gProcessor;
    }

    public void setReceiverListener() {
        IMsgReceiver.msgListener = getOnMsgListener();
    }

    public IMsgOnMsgListener getOnMsgListener() {
        if (this.mMsgListener == null) {
            this.mMsgListener = new C08912();
        }
        return this.mMsgListener;
    }

    public void setHomeActivityFlag(final boolean isHomeActivity, Context context) {
        Log.d(TAG, "setHomeActivityFlag, isHomeActivity = " + isHomeActivity + ", context = " + context);
        this.sIsHomeActivity = isHomeActivity;
        if (!isHomeActivity) {
            this.sContext = null;
        } else if (context != null) {
            this.sContext = context;
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    MsgDialogHelper.cacheManager.setHomeActivatyFlag(isHomeActivity);
                    MsgDialogHelper.this.show(MsgDialogHelper.this.sContext, false);
                }
            });
        }
    }

    public void onMessage(final IMsgContent content) {
        LogUtils.m1568d(TAG, "onMessage, sIsHomeActivity = " + this.sIsHomeActivity + ", sContext = " + this.sContext);
        if (this.sIsHomeActivity && this.sContext != null) {
            ThreadUtils.execute(new Runnable() {
                public void run() {
                    MsgDialogHelper.cacheManager.onInAppMsg(content);
                    MsgDialogHelper.this.show(MsgDialogHelper.this.sContext, false);
                }
            });
        } else if (!this.isOutApp && IMsgUtils.isAppLive(AppRuntimeEnv.get().getApplicationContext())) {
            if ((!this.sIsHomeActivity || UpdateManager.getInstance().isShowingDialog()) && content != null) {
                LogUtils.m1568d(TAG, "dialog block by: sIsHomeActivity = " + this.sIsHomeActivity + ", UpdateManager.getInstance().isShowingDialog()=" + UpdateManager.getInstance().isShowingDialog());
                new MsgDialogPingbackSender(this.isOutApp, "", cacheManager.getAllContent(this.isOutApp)).block();
            }
        }
    }

    public void buildDialog(Context context, boolean isOutApp, boolean isSystem, MsgDialogParams params) {
        MsgDialog mDialog = new MsgDialog(context, params.position, isSystem);
        mDialog.setOnClickListener(getOnClickListener(context, params.contents));
        mDialog.setMsgDialogStatusListener(getMsgDialogStatusListener(isOutApp, params.contents));
        mDialog.setData(params.imgUrl, params.showName, params.style);
    }

    private MsgDialogStatusListener getMsgDialogStatusListener(final boolean isOutApp, final IMsgContent... contents) {
        return new MsgDialogStatusListener() {
            public void onShow(Dialog dialog) {
                GetInterfaceTools.getMsgCenter().updateIsShowFlag(0, contents);
                MsgDialogHelper.this.isShow = true;
                new MsgDialogPingbackSender(isOutApp, "", contents).show();
            }

            public void onCancel(Dialog dialog) {
                if (isOutApp) {
                    GetInterfaceTools.getMsgCenter().updateIsShowFlag(1, contents);
                }
                MsgDialogHelper.this.isShow = false;
                MsgDialogHelper.this.show(MsgDialogHelper.this.sContext, isOutApp);
            }
        };
    }

    private OnClickListener getOnClickListener(final Context context, final IMsgContent... contents) {
        return new OnClickListener() {
            public void onClick(Dialog dialog, KeyEvent event) {
                int code = event.getKeyCode();
                new MsgDialogPingbackSender(IMsgUtils.isOutApp(context), "", contents).click(code);
                if (code == 66 || code == 23 || code == 4) {
                    dialog.dismiss();
                    MsgDialogHelper.this.isShow = false;
                    if (code == 66 || code == 23) {
                        for (IMsgContent content : contents) {
                            if (content.msg_level == 6) {
                                if (GetInterfaceTools.getMsgCenter().isMsgExist(content)) {
                                    Log.d(IMsgUtils.TAG, "msg is exist");
                                } else {
                                    content.isShowDialog = false;
                                    GetInterfaceTools.getMsgCenter().insertIMsg(content);
                                }
                            }
                        }
                        if (contents.length == 1 && IMsgUtils.isAppLive(context)) {
                            GetInterfaceTools.getMsgCenter().updateIsReadFlag(contents[0]);
                            MessagePromptDispatcher.get().onMessageUpdate();
                        }
                        DialogClickUtils.onClick(context, contents);
                        return;
                    }
                    MsgDialogHelper.this.show(MsgDialogHelper.this.sContext, IMsgUtils.isOutApp(MsgDialogHelper.this.sContext));
                }
            }
        };
    }

    private boolean isShowScreenSaver() {
        return GetInterfaceTools.getIScreenSaver().isShowScreenSaver();
    }

    public void show(final Context context, final boolean isOutApp) {
        this.mLastShowTime = System.currentTimeMillis();
        mExec.execute(new Runnable() {
            public void run() {
                while (System.currentTimeMillis() - MsgDialogHelper.this.mLastShowTime < 1000) {
                    LogUtils.m1568d(MsgDialogHelper.TAG, "wait to show, System.currentTimeMillis() - mLastShowTime=" + (System.currentTimeMillis() - MsgDialogHelper.this.mLastShowTime));
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
                if (MsgDialogHelper.cacheManager.getCount(isOutApp) == 0) {
                    LogUtils.m1568d(MsgDialogHelper.TAG, "needShowList.size==0");
                } else if (context == null) {
                    LogUtils.m1568d(MsgDialogHelper.TAG, "context == null");
                } else if (MsgDialogHelper.this.isShow) {
                    LogUtils.m1568d(MsgDialogHelper.TAG, "dialog block by: isShow = " + MsgDialogHelper.this.isShow);
                    new MsgDialogPingbackSender(IMsgUtils.isOutApp(context), "", MsgDialogHelper.cacheManager.getAllContent(isOutApp)).block();
                } else {
                    if (!isOutApp && IMsgUtils.isAppLive(context)) {
                        if (!MsgDialogHelper.this.sIsHomeActivity || UpdateManager.getInstance().isShowingDialog()) {
                            LogUtils.m1568d(MsgDialogHelper.TAG, "dialog block by: sIsHomeActivity = " + MsgDialogHelper.this.sIsHomeActivity + ", UpdateManager.getInstance().isShowingDialog()=" + UpdateManager.getInstance().isShowingDialog());
                            new MsgDialogPingbackSender(isOutApp, "", MsgDialogHelper.cacheManager.getAllContent(isOutApp)).block();
                            return;
                        } else if (MsgDialogHelper.this.isShowScreenSaver()) {
                            LogUtils.m1568d(MsgDialogHelper.TAG, "dialog block by: isShowScreenSaver = " + MsgDialogHelper.this.isShowScreenSaver());
                            return;
                        }
                    }
                    if (IMsgUtils.isShowDialog()) {
                        MsgDialogHelper.this.sContext = context;
                        try {
                            Handler mHandler = new Handler(context.getMainLooper());
                            final MsgDialogParams params = MsgDialogHelper.cacheManager.getDialogParams(isOutApp);
                            mHandler.post(new Runnable() {
                                public void run() {
                                    try {
                                        MsgDialogHelper.this.buildDialog(context, isOutApp, isOutApp, params);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            return;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                            return;
                        }
                    }
                    LogUtils.m1568d(MsgDialogHelper.TAG, "dialog block by: IMsgUtils.isShowDialog = " + IMsgUtils.isShowDialog());
                }
            }
        });
    }

    private synchronized boolean checkPremission(Context context) {
        boolean z = true;
        synchronized (this) {
            try {
                if (this.mPermissionschecker == null) {
                    this.mPermissionschecker = new Permissionschecker(context);
                }
                if (this.mPermissionschecker.lacksPermissions("android.permission.SYSTEM_ALERT_WINDOW")) {
                    Log.d(IMsgUtils.TAG, "no permission");
                    z = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return z;
    }
}
