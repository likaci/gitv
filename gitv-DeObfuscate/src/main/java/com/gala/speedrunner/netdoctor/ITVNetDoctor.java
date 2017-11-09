package com.gala.speedrunner.netdoctor;

import android.content.Context;
import com.gala.speedrunner.speedrunner.IOneAlbumProvider;
import com.gala.speedrunner.speedrunner.IRunCheckCallback;
import com.gala.tvapi.tv2.model.Album;
import com.netdoc.FileType;

public interface ITVNetDoctor {
    void checkPlay(Context context, Album album, boolean z, FileType fileType, String str, String str2, int i, String str3);

    void checkPlay(Context context, Album album, boolean z, FileType fileType, String str, String str2, int i, String str3, String str4, String str5);

    void checkPlay(Context context, FileType fileType, int i, IOneAlbumProvider iOneAlbumProvider, String str);

    void initNetDoctor(String str, String str2);

    void sendLogInfo(String str);

    void setSpeedListener(IRunCheckCallback iRunCheckCallback);

    void stopPlay();
}
