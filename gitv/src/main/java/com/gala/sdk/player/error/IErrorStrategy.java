package com.gala.sdk.player.error;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View.OnClickListener;
import com.gala.report.core.upload.tracker.TrackerRecord;
import com.gala.sdk.player.BitStream;
import com.gala.sdk.player.ErrorDialogListener;
import com.gala.sdk.player.IMedia;
import com.gala.sdk.player.ISdkError;
import com.gala.sdk.player.OnUserReplayListener;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;

public interface IErrorStrategy {

    public interface IDiagnoseInfoProvider {
        Album getAlbum();

        BitStream getBitStream();

        int getDiagnosePosition();

        int getPlayerType();
    }

    public interface IDiagnoseListener {
        IDiagnoseInfoProvider getDiagnoseInfoProvider();

        void onDiagnoseCanceled();

        void onDiagnosePreparing();

        void onDiagnoseStarted();
    }

    void clearCurrentDialog();

    void handleCarouselCommonError(String str, String str2, String str3);

    void handleCarouselNativePlayerBlockError(String str, String str2);

    void handleCarouselSpecialError(IVideo iVideo, ISdkError iSdkError, String str);

    void handleCommonApiError(IApiError iApiError, String str, String str2);

    void handleCopyrightRestrictionError(boolean z, String str, String str2, String str3);

    void handleDRMCommonError(String str, String str2, String str3);

    void handleForeignIpError(String str, String str2, String str3);

    void handleInvalidTvQidError();

    boolean handleKeyEvent(KeyEvent keyEvent);

    void handleLiveCommonError(String str, String str2, String str3, String str4);

    void handleLiveCopyrightRestrictionError(String str, String str2, String str3);

    void handleLiveProgramFinished(IVideo iVideo);

    void handleMediaPlayerError(int i);

    void handleNativePlayer4011And4012Error(String str, String str2);

    void handleNativePlayer4016Error(String str, String str2);

    void handleNativePlayerBlockError(String str, String str2);

    void handleNativePlayerCommonError(String str, String str2, String str3, int i);

    void handleNetworkConnected(int i, boolean z);

    void handleNetworkError(int i);

    void handlePreviewFinished(IVideo iVideo);

    void handlePushLiveError();

    void handleSystemPlayerCommonError(String str, String str2, String str3);

    void handleSystemPlayerOfflinePlaybackError(String str, String str2);

    void handleUserVipStatusIncorrectError(IVideo iVideo);

    void handleVideoOfflineError(String str, String str2, String str3);

    void handleVipAccountError(String str);

    boolean handleWithServerMsg(IVideo iVideo, ISdkError iSdkError, String str);

    void onDialogListenerRetryClicked(ErrorDialogListener errorDialogListener);

    void onErrorClicked();

    void setDiagnoseListener(IDiagnoseListener iDiagnoseListener);

    void setErrorDialogListener(ErrorDialogListener errorDialogListener);

    void setErrorInfo(ISdkError iSdkError);

    void setEventId(String str);

    void setFeedbackCallback(IFeedbackCallback iFeedbackCallback);

    void setMedia(IMedia iMedia);

    void setOnUserReplayListener(OnUserReplayListener onUserReplayListener);

    void setToastEnabled(boolean z);

    void setTrackerRecord(TrackerRecord trackerRecord);

    void showDiagnoseDialog(Context context, IVideo iVideo, int i, OnClickListener onClickListener, int i2);

    void showErrorWithServerMsg(String str, String str2, String str3, String str4);

    void skipError();
}
