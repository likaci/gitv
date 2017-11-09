package com.gala.video.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.alibaba.fastjson.asm.Opcodes;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import com.gala.video.widget.IListItemManager;

public class ListItemLayout extends FrameLayout implements IListItemManager {
    private TextView mContent;
    private ImageView mImage;
    private RelativeLayout mLayout;
    private TextView mTitle;

    public ListItemLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public ListItemLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ListItemLayout(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mLayout = new RelativeLayout(context);
        this.mImage = new ImageView(context);
        this.mImage.setId(4096);
        LayoutParams imageParams = new LayoutParams(Opcodes.GOTO, 113);
        imageParams.addRule(9);
        imageParams.addRule(15);
        this.mLayout.addView(this.mImage, imageParams);
        this.mTitle = new TextView(context);
        this.mTitle.setId(4097);
        LayoutParams titleParams = new LayoutParams(-2, -2);
        titleParams.addRule(1, 4096);
        titleParams.addRule(6, 4096);
        this.mLayout.addView(this.mTitle, titleParams);
        this.mContent = new TextView(context);
        LayoutParams contentParams = new LayoutParams(-2, -2);
        contentParams.addRule(5, 4097);
        contentParams.addRule(3, 4097);
        this.mLayout.addView(this.mContent, contentParams);
        addView(this.mLayout, new LayoutParams(WidgetType.ITEM_TEXTVIEW, Opcodes.INVOKEINTERFACE));
    }

    public void setBgBackgroundResource(int resid) {
        this.mLayout.setBackgroundResource(resid);
    }

    public void setImage(int resId) {
        this.mImage.setImageResource(resId);
        this.mImage.setScaleType(ScaleType.FIT_XY);
    }

    public void setTitle(CharSequence text) {
        this.mTitle.setText(text);
    }

    public void setTitleColor(int color) {
        this.mTitle.setTextColor(color);
    }

    public void setTitleSize(float size) {
        this.mTitle.setTextSize(0, size);
    }

    public void setContent(CharSequence text) {
        this.mContent.setText(text);
    }

    public void setContentColor(int color) {
        this.mContent.setTextColor(color);
    }

    public void setContentSize(float size) {
        this.mContent.setTextSize(0, size);
    }
}
