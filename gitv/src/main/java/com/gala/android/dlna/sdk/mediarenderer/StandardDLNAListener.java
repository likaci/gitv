package com.gala.android.dlna.sdk.mediarenderer;

public interface StandardDLNAListener {
    boolean GetMute(int i, String str);

    int GetVolume(int i, String str);

    void Next(int i);

    void Pause(int i);

    void Play(int i, String str);

    void Previous(int i);

    void Seek(int i, int i2, String str);

    void SetAVTransportURI(int i, String str, DlnaMediaModel dlnaMediaModel);

    void SetMute(int i, String str, boolean z);

    void SetNextAVTransportURI(int i, String str, DlnaMediaModel dlnaMediaModel);

    void SetPlayMode(int i, String str);

    void SetVolume(int i, String str, int i2);

    void Stop(int i);
}
