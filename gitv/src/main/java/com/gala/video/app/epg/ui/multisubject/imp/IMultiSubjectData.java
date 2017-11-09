package com.gala.video.app.epg.ui.multisubject.imp;

import com.gala.video.app.epg.home.data.base.ICardModelCallback;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;

public interface IMultiSubjectData {
    void getData(String str, ICardModelCallback iCardModelCallback);

    void loadImage(String str, IImageLoadCallback iImageLoadCallback);
}
