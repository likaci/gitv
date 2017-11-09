package com.gala.video.app.player.ui.config;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;

public interface ILoadingViewUiConfig {
    void checkLoadingBackground();

    void checkLoadingFrameAnim();

    int getAnimHeight();

    int getAnimMarginTop();

    int getAnimWidth();

    int getDeriveHeight();

    int getDeriveMarginTop();

    int getDeriveWidth();

    int getHelpTipMarginTopFull();

    int getHelpTipSizeFull();

    int getHelpTipTxtSizeFull();

    BitmapDrawable getLoadingBg();

    Bitmap getLoadingLogo();

    int getLogoHeight();

    int getLogoMarginLeft();

    int getLogoWidth();

    int getSpeedTxtMargiTop();

    int getSpeedTxtSize();

    int getTitleImageViewHeight();

    int getTitleImageViewWidth();

    int getTxtNameMarginLeftAndRight();

    int getTxtNameSize();
}
