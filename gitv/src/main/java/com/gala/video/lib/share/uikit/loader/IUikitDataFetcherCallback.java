package com.gala.video.lib.share.uikit.loader;

import com.gala.video.lib.share.uikit.data.CardInfoModel;
import java.util.List;

public interface IUikitDataFetcherCallback {
    void onFailed();

    void onSuccess(List<CardInfoModel> list, String str);
}
