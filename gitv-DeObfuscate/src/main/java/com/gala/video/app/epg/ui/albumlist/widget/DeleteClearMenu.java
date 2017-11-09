package com.gala.video.app.epg.ui.albumlist.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.internal.net.SendFlag;

public class DeleteClearMenu extends FrameLayout {
    public static final int MENU_DELETE_ALL = 1;
    public static final int MENU_DELETE_SINGLE = 0;
    private final float END_SCALE = 1.1f;
    private final int SCALE_TIME = 200;
    private final float START_SCALE = 1.0f;
    private RelativeLayout mClearLayout;
    private RelativeLayout mContainer;
    private RelativeLayout mDeleteLayout;
    private OnFocusChangeListener mFocusListener1 = new C08732();
    private OnFocusChangeListener mFocusListener2 = new C08743();
    private View mForceRequestView;
    private TextView mMenuItem_01;
    private TextView mMenuItem_02;
    private onClickCallback mOnClickCallback;
    private OnClickListener mOnClickListener = new C08721();
    private OnKeyListener mOnKeyListener;

    class C08721 implements OnClickListener {
        C08721() {
        }

        public void onClick(View v) {
            DeleteClearMenu.this.onClickAction(v);
        }
    }

    class C08732 implements OnFocusChangeListener {
        C08732() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            float end = 1.1f;
            if (v != null) {
                float start;
                if (hasFocus) {
                    DeleteClearMenu.this.mContainer.invalidate();
                    new Throwable().printStackTrace();
                    DeleteClearMenu.this.mMenuItem_01.setTextColor(ResourceUtil.getColor(C0508R.color.detail_title_text_color_new));
                } else {
                    DeleteClearMenu.this.mMenuItem_01.setTextColor(ResourceUtil.getColor(C0508R.color.searchHistorytitle_normal_color));
                }
                if (hasFocus) {
                    start = 1.0f;
                } else {
                    start = 1.1f;
                }
                if (!hasFocus) {
                    end = 1.0f;
                }
                AnimationUtil.scaleAnimation(v, start, end, 200);
            }
        }
    }

    class C08743 implements OnFocusChangeListener {
        C08743() {
        }

        public void onFocusChange(View v, boolean hasFocus) {
            float end = 1.1f;
            if (v != null) {
                float start;
                if (hasFocus) {
                    DeleteClearMenu.this.mContainer.invalidate();
                    new Throwable().printStackTrace();
                    DeleteClearMenu.this.mMenuItem_02.setTextColor(ResourceUtil.getColor(C0508R.color.detail_title_text_color_new));
                } else {
                    DeleteClearMenu.this.mMenuItem_02.setTextColor(ResourceUtil.getColor(C0508R.color.searchHistorytitle_normal_color));
                }
                if (hasFocus) {
                    start = 1.0f;
                } else {
                    start = 1.1f;
                }
                if (!hasFocus) {
                    end = 1.0f;
                }
                AnimationUtil.scaleAnimation(v, start, end, 200);
            }
        }
    }

    public interface onClickCallback {
        void OnClickCallback(int i);
    }

    public DeleteClearMenu(Context context) {
        super(context);
        init(context);
    }

    public DeleteClearMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DeleteClearMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(C0508R.layout.epg_q_delete_clear_menu, this);
        this.mContainer = (RelativeLayout) findViewById(C0508R.id.epg_menu_container);
        this.mMenuItem_01 = (TextView) findViewById(C0508R.id.epg_menu_item1);
        this.mMenuItem_02 = (TextView) findViewById(C0508R.id.epg_menu_item2);
        this.mDeleteLayout = (RelativeLayout) this.mMenuItem_01.findViewById(C0508R.id.epg_foot_menu_layout);
        this.mClearLayout = (RelativeLayout) this.mMenuItem_02.findViewById(C0508R.id.epg_foot_menu_layout);
        this.mMenuItem_01.setContentDescription("单个删除");
        this.mMenuItem_02.setContentDescription("全部删除");
        this.mMenuItem_01.setTag(Integer.valueOf(0));
        this.mMenuItem_01.setOnFocusChangeListener(this.mFocusListener1);
        this.mMenuItem_01.setOnClickListener(this.mOnClickListener);
        this.mMenuItem_02.setTag(Integer.valueOf(1));
        this.mMenuItem_02.setOnFocusChangeListener(this.mFocusListener2);
        this.mMenuItem_02.setOnClickListener(this.mOnClickListener);
        initChildrenFocusID();
        this.mMenuItem_01.setText("单个删除");
        this.mMenuItem_02.setText("全部删除");
        this.mMenuItem_01.setTextColor(ResourceUtil.getColor(C0508R.color.searchHistorytitle_normal_color));
        this.mMenuItem_02.setTextColor(ResourceUtil.getColor(C0508R.color.searchHistorytitle_normal_color));
        setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
    }

    private void onClickAction(View focusView) {
        if (focusView != null && this.mOnClickCallback != null) {
            this.mOnClickCallback.OnClickCallback(((Integer) focusView.getTag()).intValue());
        }
    }

    private void initChildrenFocusID() {
        this.mMenuItem_01.setNextFocusLeftId(C0508R.id.epg_menu_item1);
        this.mMenuItem_01.setNextFocusUpId(C0508R.id.epg_menu_item1);
        this.mMenuItem_01.setNextFocusDownId(C0508R.id.epg_menu_item1);
        this.mMenuItem_01.setNextFocusRightId(C0508R.id.epg_menu_item2);
        this.mMenuItem_02.setNextFocusUpId(C0508R.id.epg_menu_item2);
        this.mMenuItem_02.setNextFocusRightId(C0508R.id.epg_menu_item2);
        this.mMenuItem_02.setNextFocusDownId(C0508R.id.epg_menu_item2);
        this.mMenuItem_02.setNextFocusLeftId(C0508R.id.epg_menu_item1);
    }

    public void setOnClickCallback(onClickCallback callback) {
        this.mOnClickCallback = callback;
    }

    public void setOnKeyListener(OnKeyListener onKeyListener) {
        this.mOnKeyListener = onKeyListener;
    }

    public void dispatchKey(KeyEvent event) {
        if (this.mOnKeyListener != null) {
            this.mOnKeyListener.onKey(this, event.getKeyCode(), event);
        }
    }

    public void requestFocusByIndex(int index) {
        if (index == 1) {
            this.mForceRequestView = this.mMenuItem_02;
        } else if (index == 0) {
            this.mForceRequestView = this.mMenuItem_01;
        } else {
            this.mForceRequestView = null;
        }
        requestFocus();
    }

    protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
        if (this.mForceRequestView == null) {
            return super.onRequestFocusInDescendants(direction, previouslyFocusedRect);
        }
        this.mForceRequestView.requestFocus();
        this.mForceRequestView = null;
        return true;
    }
}
