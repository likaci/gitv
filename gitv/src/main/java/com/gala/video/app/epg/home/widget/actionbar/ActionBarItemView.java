package com.gala.video.app.epg.home.widget.actionbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.text.InputFilter.LengthFilter;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ActionBarItemView extends FrameLayout {
    private static final String TAG = "home/TopBarItemView";
    private Context mContext;
    private int mFocusBackgroundResId = -1;
    private ImageView mIcon;
    private LinearLayout mIconNameLayout;
    private TextView mMessage;
    private TextView mName;
    private LinearGradient mShader;

    public ActionBarItemView(Context context) {
        super(context);
        init(context);
    }

    public ActionBarItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActionBarItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public void setBackgroundResource(int bgRes) {
        this.mIconNameLayout.setBackgroundResource(bgRes);
    }

    public void setBackgroundDrawable(Drawable background) {
        this.mIconNameLayout.setBackgroundDrawable(background);
    }

    public void setChildFocusBackgroundResource(int resid) {
        this.mFocusBackgroundResId = resid;
    }

    protected void onFocusChanged(boolean gainFocus, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
        int i = gainFocus ? this.mFocusBackgroundResId >= 0 ? this.mFocusBackgroundResId : R.drawable.epg_action_bar_bg_focused : R.drawable.epg_action_bar_bg_unfocused;
        setBackgroundResource(i);
    }

    protected void init(Context context) {
        this.mContext = context;
        LayoutInflater.from(context).inflate(R.layout.epg_home_topbar_item_view_new, this, true);
        this.mIcon = (ImageView) findViewById(R.id.epg_item_icon);
        this.mName = (TextView) findViewById(R.id.epg_item_name);
        this.mMessage = (TextView) findViewById(R.id.epg_item_message);
        this.mIconNameLayout = (LinearLayout) findViewById(R.id.epg_action_bar_icon_name);
        this.mName.setTextSize(0, (float) ResourceUtil.getDimensionPixelSize(R.dimen.dimen_19dp));
        this.mName.setTextColor(getResources().getColor(R.color.action_bar_text_normal));
        this.mName.setIncludeFontPadding(false);
        setFocusable(true);
        setBackgroundDrawable(getResources().getDrawable(R.drawable.epg_action_bar_bg));
        setDescendantFocusability(393216);
    }

    public void setIconDrawable(Drawable icon) {
        this.mIcon.setImageDrawable(icon);
    }

    public void setIconVisibily(int visibily) {
        this.mIcon.setVisibility(visibily);
    }

    public void setImageBitmap(Bitmap bitmap) {
        this.mIcon.setImageBitmap(bitmap);
    }

    public void setIconDrawableWidth(int resDimenId) {
        this.mIcon.getLayoutParams().width = this.mContext.getResources().getDimensionPixelSize(resDimenId);
    }

    public void setMaxTextCount(int count) {
        this.mName.setFilters(new InputFilter[]{new LengthFilter(count)});
    }

    public void setText(String str) {
        this.mName.setText(str);
    }

    public void setMessageText(String str) {
        this.mMessage.setText(str);
    }

    public void setMessageBGDrawable(int drawableId) {
        this.mMessage.setBackgroundResource(drawableId);
    }

    public void setMessageVisible(int visible) {
        this.mMessage.setVisibility(visible);
    }

    public String getName() {
        return String.valueOf(this.mName.getText());
    }

    public void setTextColor(int colorId) {
        this.mName.getPaint().setShader(null);
        this.mName.setTextColor(colorId);
        this.mName.invalidate();
    }

    public void setShaderColor(int startColorId, int endColorId) {
        this.mShader = new LinearGradient(0.0f, 0.0f, 0.0f, this.mName.getTextSize(), startColorId, endColorId, TileMode.CLAMP);
        this.mName.getPaint().setShader(this.mShader);
        this.mName.invalidate();
    }
}
