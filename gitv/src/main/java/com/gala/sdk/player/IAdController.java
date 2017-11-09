package com.gala.sdk.player;

import java.util.List;

public interface IAdController {
    List<Integer> getShownAdType();

    void hideAd(int i);

    void hideAdHint(int i);

    boolean isEnabledClickThroughAd();

    boolean isEnabledSkipAd();

    void notifyWindowSizeChanged();

    void showAd(int i);

    void showAdHint(int i);

    void skipAd(int i);
}
