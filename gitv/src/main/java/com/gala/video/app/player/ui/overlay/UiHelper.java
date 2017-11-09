package com.gala.video.app.player.ui.overlay;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.Button;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class UiHelper {
    private static final String TAG = "AlbumDetail/UI/UiHelper";

    public static void setButtonContent(Context context, Button button, int textResId, int drawableLeftResId) {
        setButtonContent(context, button, context.getResources().getString(textResId), drawableLeftResId);
    }

    public static void setButtonContent(Context context, int textcolor, int drawableLeftResId, Button button) {
        float textSize = button.getTextSize();
        int textWidth = Math.round(button.getPaint().measureText(button.getText().toString()));
        LogUtils.d(TAG, "setButtonContent: textWidth=" + textWidth);
        int buttonWidth = button.getLayoutParams().width;
        if (LogUtils.mIsDebug) {
            LogUtils.i(TAG, "setButtonContent: textSize=" + textSize);
        }
        int drawableSize = PlayerAppConfig.getButtonLeftDrawableSizeForDetailPage(textSize);
        if (LogUtils.mIsDebug) {
            LogUtils.i(TAG, "setButtonContent: drawableSize=" + drawableSize);
        }
        Drawable drawableLeft = context.getResources().getDrawable(drawableLeftResId);
        drawableLeft.setBounds(new Rect(0, 0, drawableSize, drawableSize));
        button.setCompoundDrawables(drawableLeft, null, null, null);
        int contentSpacing = context.getResources().getDimensionPixelSize(R.dimen.dimen_3dp);
        button.setCompoundDrawablePadding(contentSpacing);
        Rect bgRect = getBgDrawablePaddings(button.getBackground());
        int blankWidth = ((bgRect.left + bgRect.right) + drawableSize) + (contentSpacing * 2);
        if (textWidth > buttonWidth - blankWidth) {
            int needWidth = (textWidth + blankWidth) + (contentSpacing * 2);
            MarginLayoutParams params = (MarginLayoutParams) button.getLayoutParams();
            params.width = needWidth;
            button.setLayoutParams(params);
            buttonWidth = needWidth;
        }
        int hPadding = ((buttonWidth - drawableSize) - textWidth) - contentSpacing;
        button.setPadding(hPadding / 2, 0, hPadding / 2, 0);
        if (textcolor != 0) {
            button.setTextColor(textcolor);
        }
    }

    public static void setButtonContent(Context context, Button button, String text, int drawableLeftResId) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">>setButtonContent" + text + ";drawableLeftResId:" + drawableLeftResId);
        }
        button.setText(text);
        setButtonContent(context, 0, drawableLeftResId, button);
    }

    public static void setButtonContent(Context context, Button button, String text, int textcolor, int drawableLeftResId) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">>setButtonContent" + text + ";drawableLeftResId:" + drawableLeftResId);
        }
        button.setText(text);
        setButtonContent(context, textcolor, drawableLeftResId, button);
    }

    public static Rect getBgDrawablePaddings(Drawable d) {
        Rect bgDrawablePaddings = new Rect();
        if (d != null) {
            d.getPadding(bgDrawablePaddings);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getBgDrawablePaddings: " + bgDrawablePaddings);
        }
        return bgDrawablePaddings;
    }

    public static SpannableString getHighLightString(String content, String[] highLights, int highLightColor) {
        SpannableString sb = new SpannableString(content);
        for (String hlItem : highLights) {
            int start = content.indexOf(hlItem);
            if (start > 0) {
                sb.setSpan(new ForegroundColorSpan(highLightColor), start, start + hlItem.length(), 33);
            }
        }
        return sb;
    }
}
