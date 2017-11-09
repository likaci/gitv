package com.gala.video.app.player.controller.error;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.DialogInterface.OnKeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.ErrorDialogListener;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.error.ErrorConstants;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.PlayerActivity;
import com.gala.video.app.player.utils.PlayerUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.GlobalDialog;
import com.gala.video.lib.share.ifimpl.netdiagnose.model.CDNNetDiagnoseInfo;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.DataUtils;

public class ErrorDialogHelper {
    private static final String TAG = "Player/Error/ErrorDialogHelper";
    private static GlobalDialog sCurrentDialog;
    private static DialogType sCurrentDialogType;

    public enum DialogType {
        OPEN_VIP,
        PURCHASE_ALBUM,
        PREVIEW_FINISH_LITCHI,
        NETWORK,
        VIDEO_OFFLINE,
        OFFLINE_PLAYBACK,
        PLAYER_CRASH,
        PLAYER_COMMON,
        RETRY_PLAYBACK,
        COMMON,
        SHOW_INFO,
        PUSH_LIVE
    }

    private static class MyFinishListener implements OnDismissListener {
        private final ErrorDialogListener mListener;

        public MyFinishListener(ErrorDialogListener listener) {
            this.mListener = listener;
        }

        public void onDismiss(DialogInterface dialog) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(ErrorDialogHelper.TAG, "MyFinishListener.onDismiss(" + dialog + ") current dialog=" + ErrorDialogHelper.sCurrentDialog);
            }
            ErrorDialogHelper.sCurrentDialog = null;
            if (this.mListener != null) {
                this.mListener.onErrorFinished();
            }
        }
    }

    private static class MyOnKeyListener implements OnKeyListener {
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (event.getAction() == 0) {
                switch (keyCode) {
                    case 4:
                    case 23:
                        dialog.dismiss();
                        return true;
                    case 19:
                    case 20:
                    case 21:
                    case 22:
                    case 82:
                        return true;
                }
            }
            return false;
        }
    }

    private ErrorDialogHelper() {
    }

    public static void clearCurrentDialog() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "clearCurrentDialog: current dialog=" + sCurrentDialog);
        }
        if (sCurrentDialog != null && sCurrentDialog.isShowing()) {
            sCurrentDialog.setOnDismissListener(null);
            sCurrentDialog.dismiss();
        }
        sCurrentDialog = null;
    }

    private static void setCurrentDialog(GlobalDialog dialog, DialogType type) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setCurrentDialog(" + dialog + "): old dialog=" + sCurrentDialog);
        }
        sCurrentDialog = dialog;
        sCurrentDialogType = type;
    }

    public static DialogType getCurrentDialogType() {
        return sCurrentDialogType;
    }

    public static boolean isDialogShowing(DialogType type) {
        if (type == null) {
            if (sCurrentDialog == null || !sCurrentDialog.isShowing()) {
                return false;
            }
            return true;
        } else if (sCurrentDialogType == type && sCurrentDialog != null && sCurrentDialog.isShowing()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDialogType(DialogType type) {
        if (type == null) {
            if (sCurrentDialog != null) {
                return true;
            }
            return false;
        } else if (sCurrentDialogType != type || sCurrentDialog == null) {
            return false;
        } else {
            return true;
        }
    }

    public static GlobalDialog createVipAccountErrorDialog(Context context, String errorCode, OnDismissListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createVipAccountErrorDialog");
        }
        return createCommonDialog(context, getAccountMessage(context, errorCode), listener);
    }

    public static String getAccountMessage(Context context, String errorCode) {
        if (ErrorConstants.API_ERR_CODE_PASSWORD_CHANGED.equals(errorCode)) {
            return context.getString(C1291R.string.account_error_password_changed);
        }
        if (ErrorConstants.API_ERR_CODE_TOO_MANY_USERS.equals(errorCode)) {
            return context.getString(C1291R.string.account_error_multi_people);
        }
        if (ErrorConstants.API_ERR_CODE_VIP_ACCOUNT_BANNED.equals(errorCode) || ErrorConstants.API_ERRO_CODE_Q311.equals(errorCode)) {
            return context.getString(C1291R.string.account_error_multi_place);
        }
        return null;
    }

    public static GlobalDialog createCommonDialog(Context context, String content, OnDismissListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createPushLiveDialog");
        }
        GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(context);
        dialog.setParams(content);
        dialog.setOnDismissListener(listener);
        dialog.show();
        setCurrentDialog(dialog, DialogType.COMMON);
        return dialog;
    }

    public static GlobalDialog createOpenVipDialog(Context context, boolean isPreview, ErrorDialogListener listener, SourceType sourceType) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createOpenVipDialog: isPreview=" + isPreview + ", listener=" + listener);
        }
        final GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(context);
        String message = "";
        if (sourceType == SourceType.PUSH) {
            message = context.getString(C1291R.string.vip_push_error_message);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createOpenVipDialog() from string.xml message=" + message);
            }
            IDynamicQDataProvider provider = GetInterfaceTools.getIDynamicQDataProvider();
            if (!(provider.getDynamicQDataModel() == null || StringUtils.isEmpty(provider.getDynamicQDataModel().getVipPushPreviewEndTip()))) {
                message = provider.getDynamicQDataModel().getVipPushPreviewEndTip();
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "createOpenVipDialog() from DynamicQDataModel message=" + message);
                }
            }
        } else {
            message = context.getString(C1291R.string.window_preview_finish_tip);
        }
        dialog.setParams(message);
        dialog.setGravity(3);
        dialog.setOnDismissListener(new MyFinishListener(listener));
        dialog.setOnKeyListener(new OnKeyListener() {
            public boolean onKey(DialogInterface dialog1, int keyCode, KeyEvent event) {
                if (event.getAction() == 0) {
                    switch (keyCode) {
                        case 4:
                        case 23:
                        case 66:
                            dialog.dismiss();
                            return true;
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                        case 82:
                            return true;
                    }
                }
                return false;
            }
        });
        dialog.show();
        setCurrentDialog(dialog, DialogType.OPEN_VIP);
        return dialog;
    }

    public static GlobalDialog createPreviewFinishDialogForLitchi(Context context, ErrorDialogListener listener) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createPreviewFinishDialogForLitchi: listener=" + listener);
        }
        GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(context);
        dialog.setParams(context.getString(C1291R.string.litchi_preview_end_tip));
        dialog.setGravity(3);
        dialog.setOnKeyListener(new MyOnKeyListener());
        dialog.setOnDismissListener(new MyFinishListener(listener));
        dialog.show();
        setCurrentDialog(dialog, DialogType.PREVIEW_FINISH_LITCHI);
        return dialog;
    }

    public static GlobalDialog createNetworkErrorDialog(Context context, String message, ErrorDialogListener listener) {
        GlobalDialog dialog;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createNetworkErrorDialog: message=" + message + ", listener=" + listener);
        }
        if (Project.getInstance().getBuild().isHomeVersion()) {
            dialog = CreateInterfaceTools.createNetworkProvider().makeDialogAsNetworkError(context, message);
            dialog.setOnDismissListener(new MyFinishListener(listener));
        } else {
            dialog = Project.getInstance().getControl().getGlobalDialog(context);
            dialog.setOnDismissListener(new MyFinishListener(listener));
            dialog.setParams(message);
        }
        dialog.show();
        setCurrentDialog(dialog, DialogType.NETWORK);
        return dialog;
    }

    public static void createStartDiagnoseDialog(Context context, IVideo video, int lagSeconds, final OnClickListener cancelListener, int playerType) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createCheckNetworkDialog: lagSeconds=" + lagSeconds + ", video=" + video);
        }
        final GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(context);
        String ok = context.getString(C1291R.string.comfirm);
        String cancel = context.getString(C1291R.string.Cancel);
        String contentText = context.getString(C1291R.string.player_check_net);
        final Context context2 = context;
        final IVideo iVideo = video;
        final int i = lagSeconds;
        final int i2 = playerType;
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                boolean isPlayerActivity = context2 instanceof PlayerActivity;
                LogUtils.m1568d(ErrorDialogHelper.TAG, "ok clicked isPlayerActivity = " + isPlayerActivity);
                if (isPlayerActivity) {
                    ((Activity) context2).finish();
                }
                PlayerUtils.startNetDiagnoseActivity(context2, new CDNNetDiagnoseInfo(iVideo.getAlbum(), GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), GetInterfaceTools.getIGalaAccountManager().getUID(), GetInterfaceTools.getIGalaAccountManager().getUserType(), i, BitStream.getBid(iVideo.getCurrentBitStream()), DataUtils.createReverForNetDoctor(iVideo.getCurrentBitStream())), i2);
            }
        };
        dialog.setParams(contentText, ok, okListener, cancel, new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                if (cancelListener != null) {
                    cancelListener.onClick(v);
                }
            }
        });
        dialog.show();
    }

    public static void createStartDiagnoseDialog(Context context, Album album, int diagnosePosition, BitStream bitStream, OnClickListener cancelListener, int playerType) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createCheckNetworkDialog: lagSeconds=" + diagnosePosition + ", video=" + album);
        }
        final GlobalDialog dialog = Project.getInstance().getControl().getGlobalDialog(context);
        String ok = context.getString(C1291R.string.comfirm);
        String cancel = context.getString(C1291R.string.Cancel);
        String contentText = context.getString(C1291R.string.player_check_net);
        final int bid = BitStream.getBid(bitStream);
        final String rever = DataUtils.createReverForNetDoctor(bitStream);
        final Context context2 = context;
        final Album album2 = album;
        final int i = diagnosePosition;
        final int i2 = playerType;
        OnClickListener okListener = new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                boolean isPlayerActivity = context2 instanceof PlayerActivity;
                LogUtils.m1568d(ErrorDialogHelper.TAG, "ok clicked isPlayerActivity = " + isPlayerActivity);
                if (isPlayerActivity) {
                    ((Activity) context2).finish();
                }
                PlayerUtils.startNetDiagnoseActivity(context2, new CDNNetDiagnoseInfo(album2, GetInterfaceTools.getIGalaAccountManager().getAuthCookie(), GetInterfaceTools.getIGalaAccountManager().getUID(), GetInterfaceTools.getIGalaAccountManager().getUserType(), i, bid, rever), i2);
            }
        };
        final OnClickListener onClickListener = cancelListener;
        dialog.setParams(contentText, ok, okListener, cancel, new OnClickListener() {
            public void onClick(View v) {
                dialog.dismiss();
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });
        dialog.show();
    }
}
