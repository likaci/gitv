package com.gala.video.app.epg.home.widget.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Shader.TileMode;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.TextView;
import com.gala.video.app.epg.R;

public class ShaderTextView extends TextView {
    private boolean mIsEnableSelectedshader;
    private LinearGradient mSelectedShader;
    private int mSelectedShaderEndColor;
    private int mSelectedShaderStartColor;

    public ShaderTextView(Context context) {
        this(context, null);
    }

    public ShaderTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShaderTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mIsEnableSelectedshader = false;
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.shadertextview);
        if (mTypedArray != null) {
            this.mSelectedShaderStartColor = mTypedArray.getColor(R.styleable.shadertextview_shader_startColor, context.getResources().getColor(R.color.home_tab_name_text_focus_shader_start));
            this.mSelectedShaderEndColor = mTypedArray.getColor(R.styleable.shadertextview_shader_endColor, context.getResources().getColor(R.color.home_tab_name_text_focus_shader_end));
            mTypedArray.recycle();
        }
    }

    public void setShaderColor(int start, int end) {
        this.mSelectedShaderStartColor = start;
        this.mSelectedShaderEndColor = end;
    }

    public void enableSelectedShader() {
        this.mIsEnableSelectedshader = true;
        invalidate();
    }

    public void disableSelectedShader() {
        this.mIsEnableSelectedshader = false;
        invalidate();
    }

    protected void onDraw(Canvas canvas) {
        if (this.mSelectedShader == null) {
            this.mSelectedShader = new LinearGradient(0.0f, 0.0f, 0.0f, (float) getHeight(), this.mSelectedShaderStartColor, this.mSelectedShaderEndColor, TileMode.CLAMP);
        }
        if (this.mIsEnableSelectedshader) {
            getPaint().setShader(this.mSelectedShader);
        } else if (isFocused()) {
            canvas.scale(1.1f, 1.1f, (float) (getWidth() / 2), (float) (getHeight() / 2));
            getPaint().setShader(null);
        } else {
            getPaint().setShader(null);
        }
        super.onDraw(canvas);
        canvas.scale(1.0f, 1.0f);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() != 66 && event.getKeyCode() != 23) || event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        performClick();
        return true;
    }
}
