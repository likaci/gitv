package com.tvos.appdetailpage.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.tvos.appdetailpage.utils.ResourcesUtils;

public class ActionView extends RelativeLayout {
    private Context mContext;
    private ImageView mImageActionIcon;
    private LayoutInflater mLayoutInflater;
    private RelativeLayout mParentContainer;
    private RelativeLayout mRootLayout;
    private TextView mTextActionName;

    public ActionView(Context context) {
        super(context);
        init(context);
    }

    public ActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActionView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mLayoutInflater = (LayoutInflater) this.mContext.getSystemService("layout_inflater");
        this.mRootLayout = (RelativeLayout) this.mLayoutInflater.inflate(getResId("layout", "apps_widget_actionview"), null, false);
        addView(this.mRootLayout);
        this.mImageActionIcon = (ImageView) findViewById(getResId("id", "apps_action_icon"));
        this.mTextActionName = (TextView) findViewById(getResId("id", "apps_action_name"));
    }

    @SuppressLint({"NewApi"})
    public void setTextResource(int resId) {
        this.mTextActionName.setText(resId);
    }

    public void setText(String text) {
        this.mTextActionName.setText(text);
    }

    @SuppressLint({"NewApi"})
    public void setImageResource(int resId) {
        if (resId == 0) {
            this.mRootLayout.removeView(this.mImageActionIcon);
            LayoutParams lp = new LayoutParams(-1, -1);
            lp.setMargins(0, 0, 0, 0);
            this.mTextActionName.setLayoutParams(lp);
            this.mTextActionName.setGravity(17);
            return;
        }
        this.mImageActionIcon.setImageResource(resId);
    }

    public void setContainer(RelativeLayout container) {
        this.mParentContainer = container;
    }

    public View getContent() {
        return this.mRootLayout;
    }

    private int getResId(String className, String name) {
        return ResourcesUtils.getResourceId(this.mContext, className, name);
    }
}
