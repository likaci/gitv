package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import com.gala.video.app.player.data.ContentsCreatorParams;
import java.util.List;

public interface IContentsCreator {
    void createDynamicContents(Context context, ContentsCreatorParams contentsCreatorParams, List<ContentHolder> list);

    void createMajorContents(Context context, ContentsCreatorParams contentsCreatorParams, List<ContentHolder> list);

    void createRestContents(Context context, ContentsCreatorParams contentsCreatorParams, List<ContentHolder> list);
}
