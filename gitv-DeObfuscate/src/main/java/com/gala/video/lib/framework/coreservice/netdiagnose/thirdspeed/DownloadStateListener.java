package com.gala.video.lib.framework.coreservice.netdiagnose.thirdspeed;

public interface DownloadStateListener {
    void onDownloadCancled(String str, long j, long j2);

    void onDownloadComplete(String str, long j, long j2);

    void onDownloadFailed(String str, String str2);
}
