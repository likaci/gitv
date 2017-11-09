package com.gala.video.lib.share.uikit.loader;

public interface IUikitDataLoader {
    void firstCardList();

    void onGetEvent(UikitEvent uikitEvent);

    void onPostEvent(UikitEvent uikitEvent);

    void register();

    void setBannerAdId(int i);

    void setChannelId(int i);

    void setSourceID(String str);

    void setVipLoader(boolean z);

    void unRegisterThread3();

    void unregister();
}
