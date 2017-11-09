package com.gala.video.lib.share.common.activity;

import android.view.KeyEvent;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.MSMessage.RequestKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.multiscreen.dmr.model.msg.PushVideo;
import com.gala.multiscreen.dmr.model.msg.Video;
import com.gala.multiscreen.dmr.model.type.Action;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.IVocal;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.IMSGalaCustomListener;
import com.gala.video.lib.framework.coreservice.multiscreen.impl.MultiScreen;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.screensaver.IScreenSaverOperate;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.PageIOUtils;
import java.util.ArrayList;
import java.util.List;

public class QMultiScreenActivity extends QBaseActivity implements IVocal {
    private static final String TAG = "QMultiScreenActivity";
    private IMSGalaCustomListener mUICallback = new C16411();

    class C16411 implements IMSGalaCustomListener {
        C16411() {
        }

        public void onFlingEvent(final KeyKind kind) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(QMultiScreenActivity.TAG, "onFlingEvent = " + kind);
            }
            QMultiScreenActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    IScreenSaverOperate ioperate = GetInterfaceTools.getIScreenSaver();
                    if (ioperate == null || !ioperate.isShowScreenSaver()) {
                        QMultiScreenActivity.this.onActionFlingEvent(kind);
                    } else {
                        QMultiScreenActivity.this.hideAndRestartScreenSaver();
                    }
                }
            });
        }

        public void onSeekEvent(final KeyKind kind) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(QMultiScreenActivity.TAG, "onSeekEvent" + kind);
            }
            QMultiScreenActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(QMultiScreenActivity.TAG, "onSeekEvent(" + kind + ")");
                    }
                    if (GetInterfaceTools.getIScreenSaver().isShowScreenSaver()) {
                        QMultiScreenActivity.this.hideAndRestartScreenSaver();
                    } else {
                        QMultiScreenActivity.this.onActionScrollEvent(kind);
                    }
                }
            });
        }

        public void onNotifyEvent(final RequestKind kind, final String message) {
            QMultiScreenActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(QMultiScreenActivity.TAG, "onNotifyEvent(" + kind + ", " + message + ")");
                    }
                    QMultiScreenActivity.this.onActionNotifyEvent(kind, message);
                }
            });
        }

        public void onInput(final String value, final boolean isOk) {
            QMultiScreenActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    QMultiScreenActivity.this.onActionInput(value, isOk);
                }
            });
        }

        public Notify onPhoneSync() {
            return QMultiScreenActivity.this.onPhoneSync();
        }

        public boolean onActionChanged(Action action) {
            if (GetInterfaceTools.getIScreenSaver().isShowScreenSaver()) {
                QMultiScreenActivity.this.hideAndRestartScreenSaver();
                if (action != Action.BACK) {
                    return false;
                }
            }
            return QMultiScreenActivity.this.onActionChanged(action);
        }

        public boolean onKeyChanged(int keycode) {
            LogUtils.m1568d(QMultiScreenActivity.TAG, "event.getKeyCode() = " + keycode);
            if (!GetInterfaceTools.getIScreenSaver().isShowScreenSaver()) {
                return QMultiScreenActivity.this.onKeyChanged(keycode);
            }
            QMultiScreenActivity.this.hideAndRestartScreenSaver();
            return false;
        }

        public boolean onSeekChanged(long newPosition) {
            if (!GetInterfaceTools.getIScreenSaver().isShowScreenSaver()) {
                return QMultiScreenActivity.this.onSeekChanged(newPosition);
            }
            QMultiScreenActivity.this.hideAndRestartScreenSaver();
            return false;
        }

        public long getPlayPosition() {
            return QMultiScreenActivity.this.getPlayPosition();
        }

        public boolean onResolutionChanged(String newRes) {
            if (GetInterfaceTools.getIScreenSaver().isShowScreenSaver()) {
                return false;
            }
            return QMultiScreenActivity.this.onResolutionChanged(newRes);
        }

        public boolean onPushPlayList(List<Video> playList) {
            return QMultiScreenActivity.this.onPushPlayList(playList);
        }

        public void onPushVideoEvent(PushVideo pushVideo) {
            IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
            if (Project.getInstance().getBuild().isHomeVersion()) {
                GetInterfaceTools.getIScreenSaver().exitHomeVersionScreenSaver(QMultiScreenActivity.this.getApplicationContext());
            } else if (iOperate.isShowScreenSaver()) {
                iOperate.hideScreenSaver();
            }
        }
    }

    protected void onResume() {
        super.onResume();
        MultiScreen.get().getGalaCustomOperator().setMSListener(this.mUICallback);
        MultiScreen.get().getGalaCustomOperator().setMSEnable(true);
        if (GetInterfaceTools.isPlayerLoaded()) {
        }
        if (Project.getInstance().getBuild().isSupportVoice()) {
            VoiceManager.instance().onActivityResume(this);
        }
    }

    protected void onPause() {
        if (Project.getInstance().getBuild().isSupportVoice()) {
            VoiceManager.instance().onActivityPause(this);
        }
        MultiScreen.get().getGalaCustomOperator().setMSEnable(false);
        if (GetInterfaceTools.isPlayerLoaded()) {
            super.onPause();
        } else {
            super.onPause();
        }
    }

    public List<AbsVoiceAction> getSupportedVoices() {
        return new ArrayList();
    }

    private void hideAndRestartScreenSaver() {
        if (Project.getInstance().getBuild().isHomeVersion()) {
            GetInterfaceTools.getIScreenSaver().exitHomeVersionScreenSaver(getApplicationContext());
            return;
        }
        IScreenSaverOperate iOperate = GetInterfaceTools.getIScreenSaver();
        if (iOperate.isShowScreenSaver()) {
            iOperate.hideScreenSaver();
        }
        iOperate.reStart();
    }

    protected boolean onPushPlayList(List<Video> list) {
        return false;
    }

    public void onActionFlingEvent(KeyKind kind) {
        LogUtils.m1568d(TAG, "onActionFlingEvent ");
        MultiScreen.get().sendSysKey(this, kind);
    }

    public String onActionNotifyEvent(RequestKind kind, String message) {
        return null;
    }

    public void onActionScrollEvent(KeyKind keyKind) {
    }

    public void onActionInput(String value, boolean isOk) {
        if (isOk) {
            search(value, false);
        }
    }

    public Notify onPhoneSync() {
        return null;
    }

    public boolean onResolutionChanged(String newRes) {
        return false;
    }

    public long getPlayPosition() {
        return 0;
    }

    public boolean onSeekChanged(long newPosition) {
        return false;
    }

    public boolean onActionChanged(Action action) {
        return false;
    }

    protected boolean onKeyChanged(int keycode) {
        return false;
    }

    private void search(String value, boolean isActor) {
        CreateInterfaceTools.createEpgEntry().search(this, value, isActor);
    }

    public boolean handleKeyEvent(KeyEvent event) {
        return super.handleKeyEvent(event);
    }

    public void finish() {
        LogUtils.m1568d(TAG, "----- finish -----");
        super.finish();
        PageIOUtils.activityOutAnim(this);
    }
}
