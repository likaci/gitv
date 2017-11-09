package com.gala.video.app.player.ui.config;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.gala.video.app.player.C1291R;

public class LoadingViewUiConfig4Litchi implements ILoadingViewUiConfig {
    public Bitmap getLoadingLogo() {
        return null;
    }

    public BitmapDrawable getLoadingBg() {
        return null;
    }

    public int getAnimWidth() {
        return C1291R.dimen.litchi_loading_anim_width;
    }

    public int getAnimHeight() {
        return C1291R.dimen.litchi_loading_anim_height;
    }

    public int getLogoWidth() {
        return 0;
    }

    public int getLogoHeight() {
        return 0;
    }

    public int getLogoMarginLeft() {
        return 0;
    }

    public int getSpeedTxtSize() {
        return C1291R.dimen.loading_speed_txt_size;
    }

    public int getSpeedTxtMargiTop() {
        return C1291R.dimen.loading_speed_txt_margintop;
    }

    public int getTxtNameMarginLeftAndRight() {
        return C1291R.dimen.loading_txt_name_margin_left_and_right;
    }

    public int getTitleImageViewWidth() {
        return C1291R.dimen.litchi_loading_titleimage_width;
    }

    public int getTitleImageViewHeight() {
        return C1291R.dimen.litchi_loading_titleimage_height;
    }

    public int getTxtNameSize() {
        return C1291R.dimen.video_play_text_size;
    }

    public int getDeriveWidth() {
        return 0;
    }

    public int getDeriveHeight() {
        return 0;
    }

    public int getDeriveMarginTop() {
        return 0;
    }

    public void checkLoadingFrameAnim() {
    }

    public void checkLoadingBackground() {
    }

    public int getAnimMarginTop() {
        return 0;
    }

    public int getHelpTipSizeFull() {
        return 0;
    }

    public int getHelpTipMarginTopFull() {
        return 0;
    }

    public int getHelpTipTxtSizeFull() {
        return 0;
    }
}
