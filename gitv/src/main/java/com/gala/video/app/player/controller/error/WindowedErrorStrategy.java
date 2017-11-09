package com.gala.video.app.player.controller.error;

import android.content.Context;
import com.gala.sdk.player.FullScreenHintType;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.error.IApiError;
import com.gala.sdk.player.ui.IPlayerOverlay;
import com.gala.video.app.player.R;
import com.gala.video.app.player.init.task.IntertrustDrmPluginLoadTask;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.IFeedbackDialogController;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.multiscreen.IErrorHandler.ErrorType;

public class WindowedErrorStrategy extends AbsErrorStrategy {
    private final String TAG = ("Player/Error/WindowedErrorStrategy" + Integer.toHexString(hashCode()));
    private Runnable mClickedAction;

    public WindowedErrorStrategy(Context context, IFeedbackDialogController controller, IPlayerOverlay overlay) {
        super(context, controller, overlay);
    }

    public void handleVipAccountError(final String errorCode) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleVipAccountError" + errorCode);
        }
        this.mOverlay.showError(ErrorType.COMMON, ErrorDialogHelper.getAccountMessage(this.mContext, errorCode));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleVipAccountError(errorCode);
            }
        };
    }

    public void handleVideoOfflineError(final String insideMsg, final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleVideoOfflineError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, !StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(R.string.video_offline_error));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleVideoOfflineError(insideMsg, log, qRMsg);
            }
        };
    }

    public void handleForeignIpError(final String insideMsg, final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleForeignIpError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, !StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(R.string.foreign_ip_error));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleForeignIpError(insideMsg, log, qRMsg);
            }
        };
    }

    public void handleCopyrightRestrictionError(boolean needLogcat, String insideMsg, String log, String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleCopyrightRestrictionError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, !StringUtils.isEmpty((CharSequence) insideMsg) ? insideMsg : this.mContext.getString(R.string.copy_restriction_error));
        final boolean z = needLogcat;
        final String str = insideMsg;
        final String str2 = log;
        final String str3 = qRMsg;
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleCopyrightRestrictionError(z, str, str2, str3);
            }
        };
    }

    public void handleLiveCopyrightRestrictionError(final String insideMsg, final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleLiveCopyrightRestrictionError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.album_detail_window_error_tip));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleCopyrightRestrictionError(true, insideMsg, log, qRMsg);
            }
        };
    }

    public void handleNativePlayerBlockError(final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleNativePlayerBlockError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.native_player_block));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleNativePlayerBlockError(log, qRMsg);
            }
        };
    }

    public void handleNativePlayer4016Error(final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleNativePlayer4016Error" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.nativeerror_4016));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleNativePlayer4016Error(log, qRMsg);
            }
        };
    }

    public void handleNativePlayer4011And4012Error(final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleNativePlayer4011And4012Error" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.nativeerror_4011_4012));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleNativePlayer4011And4012Error(log, qRMsg);
            }
        };
    }

    public void handleSystemPlayerOfflinePlaybackError(final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleSystemPlayerOfflinePlaybackError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.offline_player_error));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleSystemPlayerOfflinePlaybackError(log, qRMsg);
            }
        };
    }

    public void showErrorWithServerMsg(String content, String errLog, String errCode, String qrMessage) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "showErrorWithServerMsg" + qrMessage);
        }
        this.mOverlay.showError(ErrorType.COMMON, content);
        final String str = content;
        final String str2 = errLog;
        final String str3 = errCode;
        final String str4 = qrMessage;
        this.mClickedAction = new Runnable() {
            public void run() {
                super.showErrorWithServerMsg(str, str2, str3, str4);
            }
        };
    }

    public void handleSystemPlayerCommonError(final String errorCode, final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleSystemPlayerCommonError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.system_player_error, new Object[]{errorCode}));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleSystemPlayerCommonError(errorCode, log, qRMsg);
            }
        };
    }

    public void handleNativePlayerCommonError(String errCode, String log, String qRMsg, int playerType) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleNativePlayerCommonError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.native_player_error, new Object[]{errCode}));
        final String str = errCode;
        final String str2 = log;
        final String str3 = qRMsg;
        final int i = playerType;
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleNativePlayerCommonError(str, str2, str3, i);
            }
        };
    }

    public void handleLiveProgramFinished(IVideo iBasicVideo) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleLiveProgramFinished called");
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.live_program_finished));
        this.mClickedAction = new Runnable() {
            public void run() {
                WindowedErrorStrategy.this.showToast(WindowedErrorStrategy.this.mContext.getString(R.string.toast_live_program_finished));
            }
        };
    }

    public void handleUserVipStatusIncorrectError(final IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleUserVipStatusIncorrectError");
        }
        this.mOverlay.showError(ErrorType.VIP, this.mContext.getString(R.string.window_cannot_preview));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleUserVipStatusIncorrectError(video);
            }
        };
    }

    public void handlePreviewFinished(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handlePreviewFinished");
        }
        this.mOverlay.showError(ErrorType.VIP, this.mContext.getString(R.string.window_preview_finish_tip));
        this.mClickedAction = null;
    }

    public void handleCommonApiError(final IApiError apiErr, final String qRMsg, final String log) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleCommonApiError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, CreateInterfaceTools.createFeedbackFactory().createFeedBack(apiErr.getApiException()).getErrorMsg());
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleCommonApiError(apiErr, qRMsg, log);
            }
        };
    }

    public void handleInvalidTvQidError() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleInvalidTvQidError");
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getResources().getString(R.string.invalid_tvQid_error));
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleInvalidTvQidError();
            }
        };
    }

    public void onErrorClicked() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "onErrorClicked" + this.mClickedAction);
        }
        if (this.mClickedAction != null) {
            this.mClickedAction.run();
        }
    }

    public void handleLiveCommonError(String errCode, String insideMsg, String log, String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleLiveCommonError");
        }
        this.mOverlay.showError(ErrorType.COMMON, this.mContext.getString(R.string.album_detail_window_error_tip));
        final String str = errCode;
        final String str2 = insideMsg;
        final String str3 = log;
        final String str4 = qRMsg;
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleLiveCommonError(str, str2, str3, str4);
            }
        };
    }

    public void handleCarouselCommonError(final String insideMsg, final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleCarouselCommonError");
        }
        this.mOverlay.showError(ErrorType.COMMON, "");
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleCarouselCommonError(insideMsg, log, qRMsg);
            }
        };
    }

    public void handleCarouselNativePlayerBlockError(final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleCarouselNativePlayerBlockError" + qRMsg);
        }
        this.mOverlay.showError(ErrorType.COMMON, "");
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleCarouselNativePlayerBlockError(log, qRMsg);
            }
        };
    }

    public void handleCarouselSpecialError(final IVideo video, final ISdkError error, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleCarouselSpecialError" + qRMsg);
        }
        SourceType sourceType = null;
        if (video.getProvider() != null) {
            sourceType = video.getProvider().getSourceType();
        }
        if (sourceType == SourceType.CAROUSEL) {
            this.mOverlay.showError(ErrorType.COMMON, "");
        } else if (sourceType == SourceType.LIVE) {
            this.mOverlay.showFullScreenHint(FullScreenHintType.LIVE);
        }
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleCarouselSpecialError(video, error, qRMsg);
            }
        };
    }

    public void handleDRMCommonError(final String errorCode, final String log, final String qRMsg) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(this.TAG, "handleDRMCommonError: errorCode=" + errorCode);
        }
        String msg = this.mContext.getString(R.string.intertrust_drm_error, new Object[]{errorCode});
        if (StringUtils.equals(errorCode, String.valueOf(ISdkError.CODE_INTERTRUST_DRM_DEVICE_NOT_SUPPORT))) {
            msg = this.mContext.getString(R.string.common_player_error, new Object[]{errorCode});
        }
        this.mOverlay.showError(null, msg);
        this.mClickedAction = new Runnable() {
            public void run() {
                super.handleDRMCommonError(errorCode, log, qRMsg);
            }
        };
        if (StringUtils.equals(errorCode, String.valueOf(ISdkError.CODE_INTERTRUST_DRM_MODULE_NOT_EXIST))) {
            new Thread(new IntertrustDrmPluginLoadTask(), "IntertrustDrmPluginLoadTask-error").start();
        }
    }
}
