package com.gala.sdk.player;

import android.content.Context;
import com.gala.sdk.player.data.IVideo;

public interface INetDiagnoseProvider {

    public interface INetDiagnoseCallback {
        void onDiagnoseResult(String str);
    }

    void getDiagnoseInfoAsync(INetDiagnoseCallback iNetDiagnoseCallback, IVideo iVideo, Context context, int i, IPlayerProfile iPlayerProfile);
}
