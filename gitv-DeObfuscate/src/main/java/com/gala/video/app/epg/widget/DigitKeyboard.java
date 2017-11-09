package com.gala.video.app.epg.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.config.EpgAppConfig;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class DigitKeyboard extends RelativeLayout {
    private int PHONE_NUMBER_LIMIT = 11;
    private final String TAG = "DigitKeyboard";
    private Button mKey0;
    private Button mKey1;
    private Button mKey2;
    private Button mKey3;
    private Button mKey4;
    private Button mKey5;
    private Button mKey6;
    private Button mKey7;
    private Button mKey8;
    private Button mKey9;
    private Button mKeyClear;
    private Button mKeyDel;
    private OnClickListener mListener = new C12711();
    private TextView mTargetView;

    class C12711 implements OnClickListener {
        C12711() {
        }

        public void onClick(View v) {
            LogUtils.m1568d("DigitKeyboard", "onClick, mTargetView = " + (DigitKeyboard.this.mTargetView == null ? "mTargetView is null" : DigitKeyboard.this.mTargetView.getText()));
            if (DigitKeyboard.this.mTargetView == null) {
                LogUtils.m1568d("DigitKeyboard", "no targetView, return");
                return;
            }
            StringBuilder sb = new StringBuilder(DigitKeyboard.this.mTargetView.getText());
            LogUtils.m1568d("DigitKeyboard", "text length=" + sb.length() + "keyCode = " + ((TextView) v).getText());
            if (sb.length() < DigitKeyboard.this.PHONE_NUMBER_LIMIT || v.getId() == C0508R.id.epg_key_del || v.getId() == C0508R.id.epg_key_clear) {
                int i = v.getId();
                if (i == C0508R.id.epg_key_0) {
                    sb.append("0");
                } else if (i == C0508R.id.epg_key_1) {
                    sb.append("1");
                } else if (i == C0508R.id.epg_key_2) {
                    sb.append("2");
                } else if (i == C0508R.id.epg_key_3) {
                    sb.append("3");
                } else if (i == C0508R.id.epg_key_4) {
                    sb.append("4");
                } else if (i == C0508R.id.epg_key_5) {
                    sb.append("5");
                } else if (i == C0508R.id.epg_key_6) {
                    sb.append("6");
                } else if (i == C0508R.id.epg_key_7) {
                    sb.append("7");
                } else if (i == C0508R.id.epg_key_8) {
                    sb.append("8");
                } else if (i == C0508R.id.epg_key_9) {
                    sb.append("9");
                } else if (i == C0508R.id.epg_key_del) {
                    if (sb.length() > 0) {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                } else if (i == C0508R.id.epg_key_clear && sb.length() > 0) {
                    sb.delete(0, sb.length());
                }
                DigitKeyboard.this.mTargetView.setText(sb);
            }
        }
    }

    public DigitKeyboard(Context context) {
        super(context);
        init(context);
    }

    public DigitKeyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DigitKeyboard(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        View child = ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(EpgAppConfig.getDigitKeyboardLayoutResId(), null);
        addView(child);
        this.mKey0 = (Button) child.findViewById(C0508R.id.epg_key_0);
        this.mKey0.setOnClickListener(this.mListener);
        this.mKey1 = (Button) child.findViewById(C0508R.id.epg_key_1);
        this.mKey1.setOnClickListener(this.mListener);
        this.mKey2 = (Button) child.findViewById(C0508R.id.epg_key_2);
        this.mKey2.setOnClickListener(this.mListener);
        this.mKey3 = (Button) child.findViewById(C0508R.id.epg_key_3);
        this.mKey3.setOnClickListener(this.mListener);
        this.mKey4 = (Button) child.findViewById(C0508R.id.epg_key_4);
        this.mKey4.setOnClickListener(this.mListener);
        this.mKey5 = (Button) child.findViewById(C0508R.id.epg_key_5);
        this.mKey5.setOnClickListener(this.mListener);
        this.mKey6 = (Button) child.findViewById(C0508R.id.epg_key_6);
        this.mKey6.setOnClickListener(this.mListener);
        this.mKey7 = (Button) child.findViewById(C0508R.id.epg_key_7);
        this.mKey7.setOnClickListener(this.mListener);
        this.mKey8 = (Button) child.findViewById(C0508R.id.epg_key_8);
        this.mKey8.setOnClickListener(this.mListener);
        this.mKey9 = (Button) child.findViewById(C0508R.id.epg_key_9);
        this.mKey9.setOnClickListener(this.mListener);
        this.mKeyDel = (Button) child.findViewById(C0508R.id.epg_key_del);
        this.mKeyDel.setOnClickListener(this.mListener);
        this.mKeyClear = (Button) child.findViewById(C0508R.id.epg_key_clear);
        this.mKeyClear.setOnClickListener(this.mListener);
    }

    public void bindView(TextView view) {
        this.mTargetView = view;
    }

    public void requestDefaultFocus() {
        this.mKey1.requestFocus();
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        if (gainFocus) {
            requestDefaultFocus();
        }
    }
}
