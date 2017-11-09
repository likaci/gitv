package com.gala.video.app.epg.home.component.item.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.View.OnFocusChangeListener;
import com.gala.video.cloudui.CloudView;
import com.gala.video.cloudui.CuteImageView;
import com.gala.video.cloudui.CuteTextView;
import com.gala.video.lib.share.common.configs.ViewConstant;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import java.util.ArrayList;
import java.util.List;

public class ComplexItemCloudView extends CloudView implements FocusObservable {
    private final List<OnFocusChangeListener> mFocusObsevers = new ArrayList();

    public ComplexItemCloudView(Context context) {
        super(context);
        initStyle(ItemCloudViewType.DEFAULT);
    }

    public ComplexItemCloudView(Context context, ItemCloudViewType viewType) {
        super(context);
        initStyle(viewType);
    }

    private void initStyle(ItemCloudViewType viewType) {
        switch (viewType) {
            case SETTINGS:
                setStyle("home/settingitem.json");
                return;
            case APP:
                setStyle("home/appsitem.json");
                return;
            case CIRCLE:
                setStyle("home/circleitem.json");
                return;
            case SKEW:
                setStyle("home/skewitem.json");
                return;
            case DAILYNEWS:
                setStyle("home/dailynewsitem.json");
                return;
            case CHANNELLIST:
                setStyle("home/channellistitem.json");
                return;
            case ALLENTRYFUNCTION:
                setStyle("home/allentryfunctionitem.json");
                return;
            case CONFIGURE:
                setStyle("home/appsitem.json");
                return;
            default:
                setStyle("home/complexitem.json");
                return;
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == 66 || event.getKeyCode() == 23) && event.getAction() == 0) {
            return performClick() ? true : super.dispatchKeyEvent(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public CuteImageView getCoreImageView() {
        return getImageView("ID_IMAGE");
    }

    public CuteImageView getCornerRTView() {
        return getImageView("ID_CORNER_R_T");
    }

    public CuteImageView getCornerLTView() {
        return getImageView("ID_CORNER_L_T");
    }

    public CuteImageView getCornerBgLeftView() {
        return getImageView("ID_CORNER_BG_LEFT");
    }

    public CuteImageView getCornerLB1View() {
        return getImageView("ID_CORNER_L_B_1");
    }

    public CuteImageView getCornerLB2View() {
        return getImageView("ID_CORNER_L_B_2");
    }

    public CuteImageView getRankView() {
        return getImageView(UIKitConfig.ID_CORNER_RANK);
    }

    public CuteImageView getTitleBgView() {
        return getImageView("ID_TITLE_BG");
    }

    public CuteImageView getChnIdBgView() {
        return getImageView("ID_CHNID_BG");
    }

    public CuteImageView getBottomBgView() {
        return getImageView(ViewConstant.ID_BOTTOM_BG);
    }

    public CuteImageView getPlayingGif() {
        return getImageView(UIKitConfig.ID_PLAYING_GIF);
    }

    public CuteTextView getScoreView() {
        return getTextView("ID_SCORE");
    }

    public CuteTextView getRBDescView() {
        return getTextView(UIKitConfig.ID_DESC_R_B);
    }

    public CuteTextView getToBeOnLineView() {
        return getTextView("ID_TOBE_ONLINE");
    }

    public CuteTextView getChannelIdView() {
        return getTextView(UIKitConfig.ID_CHANNEL_ID);
    }

    public CuteTextView getTitleView() {
        return getTextView("ID_TITLE");
    }

    public CuteTextView getLTBubbleView() {
        return getTextView("ID_LT_BUBBLE");
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        for (OnFocusChangeListener listener : this.mFocusObsevers) {
            listener.onFocusChange(this, gainFocus);
        }
    }

    public void registerFocusChangeListener(OnFocusChangeListener listener) {
        if (listener != null) {
            synchronized (this.mFocusObsevers) {
                if (!this.mFocusObsevers.contains(listener)) {
                    this.mFocusObsevers.add(listener);
                }
            }
        }
    }

    public void unregisterFocusChangeListener(OnFocusChangeListener listener) {
        if (listener != null) {
            synchronized (this.mFocusObsevers) {
                this.mFocusObsevers.remove(listener);
            }
        }
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
    }
}
