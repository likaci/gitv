package com.gala.video.app.epg.home.widget.guidelogin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.FrameLayout;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.share.utils.AnimationUtil;

public class CheckInView extends FrameLayout implements OnClickListener, OnFocusChangeListener {
    private Context mContext;
    private Button mIn;
    private OnCheckClickListener mOnClickListener;
    private Button mOut;

    public interface OnCheckClickListener {
        void onClick(View view);
    }

    public CheckInView(Context context) {
        this(context, null);
    }

    public CheckInView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CheckInView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context.getApplicationContext()).inflate(C0508R.layout.epg_checkin_view, this, true);
        initView();
    }

    private void initView() {
        this.mIn = (Button) findViewById(C0508R.id.check_in);
        this.mOut = (Button) findViewById(C0508R.id.check_out);
        this.mIn.setOnClickListener(this);
        this.mOut.setOnClickListener(this);
        this.mIn.setOnFocusChangeListener(this);
        this.mOut.setOnFocusChangeListener(this);
    }

    public void onFocusChange(View v, boolean hasFocus) {
        AnimationUtil.zoomAnimation(v, hasFocus ? 1.1f : 1.0f, 200);
    }

    public void setOnClickListener(OnCheckClickListener onClickListener) {
        this.mOnClickListener = onClickListener;
    }

    public void onClick(View v) {
        if (this.mOnClickListener != null) {
            this.mOnClickListener.onClick(v);
        }
    }
}
