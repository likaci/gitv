package com.gala.video.app.player.ui.overlay.panels;

import android.content.Context;
import android.graphics.LinearGradient;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.TextView;
import com.gala.video.app.player.R;
import com.gala.video.app.player.ui.config.LoadingViewAnimManager;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class PlayerErrorPanel {
    private final String TAG = ("Player/UI/PlayerErrorPanel@" + Integer.toHexString(hashCode()));
    private ImageView mBackgroud;
    private Context mContext;
    private TextView mCornerText;
    private TextView mMainText;
    private View mPanelView;

    public static class PlayerErrorPanelInfo {
        private Drawable mBackground = LoadingViewAnimManager.getInstance(ResourceUtil.getContext()).getBackground();
        private String mCornerText = "";
        private String mMainText = "";

        public PlayerErrorPanelInfo setBackgroundDrawable(Drawable drawable) {
            if (drawable != null) {
                this.mBackground = drawable;
            }
            return this;
        }

        public PlayerErrorPanelInfo setMainText(String text) {
            if (text != null) {
                this.mMainText = text;
            }
            return this;
        }

        public PlayerErrorPanelInfo setCornerText(String text) {
            if (text != null) {
                this.mCornerText = text;
            }
            return this;
        }

        public String toString() {
            return "PlayerErrorPanelInfo{mBackground=" + this.mBackground + ", mMainText='" + this.mMainText + '\'' + ", mCornerText='" + this.mCornerText + '\'' + '}';
        }
    }

    public static class PlayerErrorPanelUIConfig {
        private int mBackgroundColor = 0;
        private ScaleType mBackgroundScaleType = ScaleType.FIT_XY;
        private int mCornerTextEndColor = -65536;
        private int mCornerTextSize = ResourceUtil.getDimensionPixelSize(R.dimen.dimen_20dp);
        private int mCornerTextStartColor = -65536;
        private int mMainTextEndColor = -65536;
        private int mMainTextGravity = 17;
        private int mMainTextSize = ResourceUtil.getDimensionPixelSize(R.dimen.dimen_20dp);
        private int mMainTextStartColor = -65536;
        private int mMainTextTopMargin = 0;

        public PlayerErrorPanelUIConfig setMainTextColor(int startColor, int endColor) {
            this.mMainTextStartColor = startColor;
            this.mMainTextEndColor = endColor;
            return this;
        }

        public PlayerErrorPanelUIConfig setMainTextSize(int size) {
            this.mMainTextSize = size;
            return this;
        }

        public PlayerErrorPanelUIConfig setMainTextGravity(int gravity) {
            this.mMainTextGravity = gravity;
            return this;
        }

        public PlayerErrorPanelUIConfig setMainTextTopMargin(int margin) {
            this.mMainTextTopMargin = margin;
            return this;
        }

        public PlayerErrorPanelUIConfig setCornerTextColor(int startColor, int endColor) {
            this.mCornerTextStartColor = startColor;
            this.mCornerTextEndColor = endColor;
            return this;
        }

        public PlayerErrorPanelUIConfig setCornerTextSize(int size) {
            this.mCornerTextSize = size;
            return this;
        }

        public PlayerErrorPanelUIConfig setBackgroundScaleType(ScaleType type) {
            if (type != null) {
                this.mBackgroundScaleType = type;
            }
            return this;
        }

        public PlayerErrorPanelUIConfig setBackgroundColor(int color) {
            this.mBackgroundColor = color;
            return this;
        }

        public String toString() {
            return "PlayerErrorPanelUIConfig{mMainTextStartColor=" + Integer.toHexString(this.mMainTextStartColor) + ", mMainTextEndColor=" + Integer.toHexString(this.mMainTextEndColor) + ", mMainTextSize=" + this.mMainTextSize + ", mCornerTextStartColor=" + Integer.toHexString(this.mCornerTextStartColor) + ", mCornerTextEndColor=" + Integer.toHexString(this.mCornerTextEndColor) + ", mCornerTextSize=" + this.mCornerTextSize + ", mBackgroundScaleType=" + this.mBackgroundScaleType + '}';
        }
    }

    public PlayerErrorPanel(Context context) {
        this.mContext = context;
    }

    public View getView() {
        if (this.mPanelView == null) {
            initView();
        }
        return this.mPanelView;
    }

    private void initView() {
        LogRecordUtils.logd(this.TAG, ">> initView");
        this.mPanelView = LayoutInflater.from(this.mContext).inflate(R.layout.player_error_panel, null);
        this.mBackgroud = (ImageView) this.mPanelView.findViewById(R.id.player_error_panel_bg);
        this.mMainText = (TextView) this.mPanelView.findViewById(R.id.player_error_panel_main_text);
        this.mCornerText = (TextView) this.mPanelView.findViewById(R.id.player_error_panel_corner_text);
    }

    public void show(PlayerErrorPanelUIConfig config, PlayerErrorPanelInfo info) {
        LogRecordUtils.logd(this.TAG, ">> show, config=" + config + ", info=" + info);
        showBackground(config, info);
        showMainText(config, info);
        showCornerText(config, info);
        if (this.mPanelView.getVisibility() != 0) {
            this.mPanelView.setVisibility(0);
        }
    }

    private void showBackground(PlayerErrorPanelUIConfig config, PlayerErrorPanelInfo info) {
        Drawable drawable = info.mBackground;
        if (drawable != null) {
            this.mBackgroud.setImageDrawable(drawable);
            this.mBackgroud.setScaleType(config.mBackgroundScaleType);
            this.mBackgroud.setBackgroundColor(config.mBackgroundColor);
            this.mBackgroud.setVisibility(0);
            return;
        }
        LogRecordUtils.logd(this.TAG, "showBackground, background drawable is null.");
        this.mBackgroud.setVisibility(8);
    }

    private void showMainText(PlayerErrorPanelUIConfig config, PlayerErrorPanelInfo info) {
        CharSequence text = info.mMainText;
        if (StringUtils.isEmpty(text)) {
            LogRecordUtils.logd(this.TAG, "showMainText, main text is empty.");
            this.mMainText.setVisibility(8);
            return;
        }
        this.mMainText.setText(text);
        this.mMainText.setTextSize(0, (float) config.mMainTextSize);
        adjustMainTextUIStyle(config);
        this.mMainText.setVisibility(0);
    }

    private void adjustMainTextUIStyle(PlayerErrorPanelUIConfig config) {
        LayoutParams lp = (LayoutParams) this.mMainText.getLayoutParams();
        if (lp != null) {
            lp.gravity = config.mMainTextGravity;
            lp.topMargin = config.mMainTextTopMargin;
        } else {
            lp = new LayoutParams(-2, -2);
            lp.gravity = 17;
            LogRecordUtils.logd(this.TAG, "adjustMainTextUIStyle, use default layout params.");
        }
        this.mMainText.setLayoutParams(lp);
        if (this.mMainText.getLineCount() > 1) {
            this.mMainText.setGravity(17);
        } else {
            this.mMainText.setGravity(3);
        }
        if (config.mMainTextStartColor != config.mMainTextEndColor) {
            LogRecordUtils.logd(this.TAG, "showMainText, set shader, mMainText.getTextSize()=" + this.mMainText.getTextSize());
            this.mMainText.getPaint().setShader(new LinearGradient(0.0f, 0.0f, 0.0f, this.mMainText.getTextSize(), config.mMainTextStartColor, config.mMainTextEndColor, TileMode.MIRROR));
            return;
        }
        LogRecordUtils.logd(this.TAG, "showMainText, set text color");
        this.mMainText.setTextColor(config.mMainTextStartColor);
    }

    private void showCornerText(PlayerErrorPanelUIConfig config, PlayerErrorPanelInfo info) {
        CharSequence text = info.mCornerText;
        if (StringUtils.isEmpty(text)) {
            LogRecordUtils.logd(this.TAG, "showCornerText, corner text is empty.");
            this.mCornerText.setVisibility(8);
            return;
        }
        this.mCornerText.setText(text);
        this.mCornerText.setTextSize(0, (float) config.mCornerTextSize);
        this.mCornerText.setTextColor(config.mCornerTextStartColor);
        this.mCornerText.setGravity(3);
        this.mCornerText.setVisibility(0);
    }

    public void hide() {
        if (this.mPanelView == null) {
            LogRecordUtils.logd(this.TAG, "hide, mPanelView is null.");
        } else if (this.mPanelView.getVisibility() == 0) {
            this.mPanelView.setVisibility(8);
        }
    }
}
