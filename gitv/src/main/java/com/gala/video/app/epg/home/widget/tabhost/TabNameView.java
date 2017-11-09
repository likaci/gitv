package com.gala.video.app.epg.home.widget.tabhost;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.view.ViewDebug;
import com.gala.video.app.epg.home.widget.text.ShaderTextView;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;

public class TabNameView extends ShaderTextView {
    private static final boolean DBG = ViewDebug.DBG;
    private static final String TAG = "/tabhost/TabNameView";
    private int mCurrentKeyCode = -1;
    private int mFocusColor;
    private int mNormalColor;

    public TabNameView(Context context) {
        super(context);
        init(context);
    }

    public TabNameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TabNameView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mNormalColor = context.getResources().getColor(R.color.home_tab_name_text_normal);
        this.mFocusColor = context.getResources().getColor(R.color.home_tab_name_text_focus);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (DBG) {
            Log.d(TAG, "dispatchKeyEvent keycode = " + event.getKeyCode() + " action = " + event.getAction());
        }
        this.mCurrentKeyCode = event.getKeyCode();
        return super.dispatchKeyEvent(event);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setTextColor(this.mFocusColor);
            disableSelectedShader();
        } else if (this.mCurrentKeyCode <= 0 || this.mCurrentKeyCode == 4 || this.mCurrentKeyCode == 21 || this.mCurrentKeyCode == 22) {
            setTextColor(this.mNormalColor);
            if (DBG) {
                Log.d(TAG, "text name view on focus channge  unfocus ");
            }
        } else {
            if (DBG) {
                Log.d(TAG, "text name view on focus channge selected ");
            }
            enableSelectedShader();
        }
    }

    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (DBG) {
            Log.d(TAG, "text name view is foucusd = " + isFocused() + ",is selected = " + selected);
        }
        if (selected) {
            enableSelectedShader();
        } else {
            scaleTextSmaller();
        }
    }

    protected void scaleTextSmaller() {
        disableSelectedShader();
        setTextColor(this.mNormalColor);
        getPaint().setFakeBoldText(false);
    }

    public void setTextShaderColor(int start, int end) {
        setShaderColor(start, end);
    }

    public void updateTextColor(boolean isVip) {
        Resources resources = AppRuntimeEnv.get().getApplicationContext().getResources();
        this.mNormalColor = resources.getColor(R.color.home_tab_name_text_normal);
        setTextColor(this.mNormalColor);
        int startResId = R.color.home_tab_name_text_focus_shader_start;
        int endResId = R.color.home_tab_name_text_focus_shader_end;
        if (isVip) {
            startResId = R.color.home_vip_tab_name_text_focus_shader_start;
            endResId = R.color.home_vip_tab_name_text_focus_shader_end;
        }
        setShaderColor(resources.getColor(startResId), resources.getColor(endResId));
    }
}
