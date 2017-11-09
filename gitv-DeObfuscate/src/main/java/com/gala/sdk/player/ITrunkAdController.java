package com.gala.sdk.player;

import android.view.KeyEvent;
import java.util.List;

public interface ITrunkAdController extends IAdController, IStateOverlay, IThreeDimensional {
    public static final int AD_REDIRECT_STATE_HIDDEN = 200;
    public static final int AD_REDIRECT_STATE_LOADED = 202;
    public static final int AD_REDIRECT_STATE_LOADING = 201;
    public static final int AD_REDIRECT_TYPE_H5 = 101;
    public static final int AD_REDIRECT_TYPE_H5_INSIDE = 100;
    public static final int AD_REDIRECT_TYPE_IMG = 102;

    void changeMode(boolean z, float f);

    void clearAd();

    boolean clickInteractionAd();

    BaseAdData getAdData(int i);

    List<Integer> getShowThroughState();

    List<Integer> getShowThroughType();

    boolean handleJsKeyEvent(KeyEvent keyEvent);

    void hideAll();

    void requestAd(int i, IAdDataProvider iAdDataProvider);

    void setEnableShow(boolean z);

    void startPurchasePage();
}
