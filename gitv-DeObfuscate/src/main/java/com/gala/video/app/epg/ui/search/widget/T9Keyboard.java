package com.gala.video.app.epg.ui.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.tv.voice.core.Log;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridParams;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.WidgetStatusListener;
import com.gala.video.app.epg.ui.search.IKeyboardListener;
import com.gala.video.app.epg.ui.search.adapter.T9KeyboardAdapter;
import com.gala.video.app.epg.ui.search.widget.T9Layer.LayerTypeEnum;
import com.gala.video.app.epg.ui.search.widget.T9Layer.OnT9LayerClickListener;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.NineDrawableUtils;

public class T9Keyboard extends RelativeLayout {
    private static final int DEFAULT_FOCUS_POS = 4;
    public static final int TAG_KEY = C0508R.id.epg_keyboard_t9;
    private Context mContext;
    private boolean mIsLayerShow;
    private int mKeyHeight;
    private int mKeyHorizontalSpace;
    private int mKeyVerticalSpace;
    private int mKeyWidth;
    private IKeyboardListener mKeyboardListener;
    private OnT9LayerClickListener mLayerClickListener = new C10272();
    private int mLayerHeight;
    private int mLayerWidth;
    private View mPreView;
    private OnT9FocusListener mT9FocusListener;
    private PhotoGridView mT9KeyPanel;
    WidgetStatusListener mT9KeyPanelListener = new C10261();
    private T9KeyboardAdapter mT9KeyboardAdapter;
    private T9Layer mT9Layer;

    public interface OnT9FocusListener {
        void onFocus(View view, int i, boolean z);
    }

    class C10261 implements WidgetStatusListener {
        C10261() {
        }

        public void onLoseFocus(ViewGroup arg0, View arg1, int arg2) {
        }

        public void onItemTouch(View arg0, MotionEvent arg1, int arg2) {
        }

        public void onItemSelectChange(View v, int pos, boolean focus) {
            if (T9Keyboard.this.mT9FocusListener != null) {
                T9Keyboard.this.mT9FocusListener.onFocus(v, pos, focus);
            }
        }

        public void onItemClick(ViewGroup arg0, View view, int arg2) {
            if (T9Keyboard.this.mT9Layer != null && view != null) {
                Object tag = view.getTag(T9Keyboard.TAG_KEY);
                if (tag != null) {
                    T9Keyboard.this.mT9Layer.layerChanged(tag.toString());
                    T9Keyboard.this.showLayer(view);
                } else if (T9Keyboard.this.mKeyboardListener != null) {
                    T9Keyboard.this.mKeyboardListener.onTextChange(((TextView) view).getText().toString());
                }
            }
        }
    }

    class C10272 implements OnT9LayerClickListener {
        C10272() {
        }

        public void onClick(ViewGroup viewGroup, View view, int pos) {
            T9Keyboard.this.mPreView.setVisibility(0);
            T9Keyboard.this.mPreView.requestFocus();
            T9Keyboard.this.mT9Layer.setVisibility(4);
            T9Keyboard.this.mIsLayerShow = false;
            if (T9Keyboard.this.mKeyboardListener != null) {
                T9Keyboard.this.mKeyboardListener.onTextChange(((TextView) view).getText().toString());
            }
        }
    }

    public T9Keyboard(Context context) {
        super(context);
        this.mContext = context;
    }

    public T9Keyboard(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    private void initT9KeyLayer() {
        this.mT9Layer = new T9Layer(this.mContext);
        this.mT9Layer.setParams(this.mLayerWidth, this.mLayerHeight, null);
        addView(this.mT9Layer);
        this.mT9Layer.setT9LayerClickListener(this.mLayerClickListener);
        this.mT9Layer.setVisibility(4);
    }

    private void initT9KeyPanel() {
        this.mT9KeyPanel = new PhotoGridView(this.mContext);
        this.mT9KeyPanel.setLayoutParams(new LayoutParams(-2, -2));
        this.mT9KeyPanel.setNextDownFocusLeaveAvail(true);
        this.mT9KeyPanel.setNextUpFocusLeaveAvail(true);
        this.mT9KeyPanel.setNextLeftFocusLeaveAvail(false);
        this.mT9KeyPanel.setNextRightFocusLeaveAvail(true);
        this.mT9KeyPanel.setParams(getT9KeyPanelParam());
        this.mT9KeyboardAdapter = new T9KeyboardAdapter(this.mContext);
        this.mT9KeyPanel.setAdapter(this.mT9KeyboardAdapter);
        this.mT9KeyPanel.setListener(this.mT9KeyPanelListener);
        setPadding(this.mLayerWidth, this.mLayerHeight, 0, 0);
        setClipToPadding(false);
        addView(this.mT9KeyPanel);
    }

    private PhotoGridParams getT9KeyPanelParam() {
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = 3;
        p.verticalSpace = this.mKeyVerticalSpace;
        p.horizontalSpace = this.mKeyHorizontalSpace;
        p.contentHeight = this.mKeyHeight;
        p.contentWidth = this.mKeyWidth;
        p.scaleRate = 1.1f;
        return p;
    }

    private void showLayer(View view) {
        if (this.mPreView != null) {
            this.mPreView.setVisibility(0);
        }
        this.mPreView = view;
        layerLocation(view);
        this.mT9Layer.setVisibility(0);
        bringToFront();
        this.mIsLayerShow = true;
        this.mT9Layer.focusChanged();
        view.setVisibility(4);
    }

    private void layerLocation(View view) {
        int layerTopMargin;
        MarginLayoutParams viewMargin = new MarginLayoutParams(view.getLayoutParams());
        LayoutParams l = (LayoutParams) view.getLayoutParams();
        int viewButtomLeft = l.leftMargin + (viewMargin.width / 2);
        int viewButtomTop = l.topMargin + viewMargin.height;
        LogUtils.m1574i("T9", ">>>>>l.leftMargin: " + l.leftMargin + "--Width:" + viewMargin.width + "---Height:" + viewMargin.height);
        this.mT9Layer.measure(MeasureSpec.makeMeasureSpec(0, 0), MeasureSpec.makeMeasureSpec(0, 0));
        int height = this.mT9Layer.getMeasuredHeight();
        int width = this.mT9Layer.getMeasuredWidth();
        int layerLeftMargin = viewButtomLeft - (width / 2);
        switch (this.mT9Layer.getT9LayerType()) {
            case MIN:
                layerTopMargin = (viewButtomTop - (viewMargin.height / 2)) - (height / 2);
                break;
            default:
                layerTopMargin = (viewButtomTop - (this.mLayerHeight * 2)) - (height % this.mLayerHeight);
                break;
        }
        LayoutParams lp = new LayoutParams(this.mT9Layer.getLayoutParams());
        lp.setMargins(layerLeftMargin, layerTopMargin, 0, 0);
        this.mT9Layer.setLayoutParams(lp);
        Log.m527e("jaunce", "layerTopMargin:" + layerTopMargin);
        LogUtils.m1574i("T9", ">>>>>mT9Layer.getWidth:" + width + "---mT9Layer.getHeight:" + height + " ---" + NineDrawableUtils.calNinePatchBorder(this.mContext, getResources().getDrawable(C0508R.drawable.share_btn_focus)));
    }

    private void hideLayer() {
        this.mPreView.setVisibility(0);
        this.mPreView.requestFocus();
        this.mT9Layer.setVisibility(4);
        this.mIsLayerShow = false;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        LayerTypeEnum t9LayerType = this.mT9Layer.getT9LayerType();
        switch (event.getKeyCode()) {
            case 4:
                if (!this.mIsLayerShow) {
                    return false;
                }
                hideLayer();
                return true;
            case 19:
                if (t9LayerType == LayerTypeEnum.MIN) {
                    if (!this.mT9Layer.getLayerLeft().isFocused() && !this.mT9Layer.getLayerRight().isFocused()) {
                        return false;
                    }
                    hideLayer();
                    return false;
                } else if (!this.mT9Layer.getLayerTop().isFocused()) {
                    return false;
                } else {
                    hideLayer();
                    return false;
                }
            case 20:
                if (t9LayerType == LayerTypeEnum.MIN) {
                    if (!this.mT9Layer.getLayerLeft().isFocused() && !this.mT9Layer.getLayerRight().isFocused()) {
                        return false;
                    }
                    hideLayer();
                    return false;
                } else if (t9LayerType == LayerTypeEnum.MAX) {
                    if (!this.mT9Layer.getLayerButtom().isFocused()) {
                        return false;
                    }
                    hideLayer();
                    return false;
                } else if (!this.mT9Layer.getLayerCenter().isFocused() && !this.mT9Layer.getLayerLeft().isFocused() && !this.mT9Layer.getLayerRight().isFocused()) {
                    return false;
                } else {
                    hideLayer();
                    return false;
                }
            case 21:
                if (!this.mT9Layer.getLayerLeft().isFocused()) {
                    return false;
                }
                hideLayer();
                return false;
            case 22:
                if (!this.mT9Layer.getLayerRight().isFocused()) {
                    return false;
                }
                hideLayer();
                return false;
            default:
                return false;
        }
    }

    public void setKeyboardListener(IKeyboardListener keyboardListener) {
        this.mKeyboardListener = keyboardListener;
    }

    public void setKeyPanelParams(int itemWidth, int itemHeight, int verticalSpace, int horizontalSpace) {
        this.mKeyWidth = itemWidth;
        this.mKeyHeight = itemHeight;
        this.mKeyVerticalSpace = verticalSpace;
        this.mKeyHorizontalSpace = horizontalSpace;
    }

    public void setLayerParams(int itemWidth, int itemHeight) {
        this.mLayerWidth = itemWidth;
        this.mLayerHeight = itemHeight;
    }

    public void generate() {
        initT9KeyPanel();
        initT9KeyLayer();
    }

    public void requestDefaultFocus() {
        if (this.mT9KeyPanel.getViewByPos(4) != null) {
            this.mT9KeyPanel.getViewByPos(4).requestFocus();
        }
    }

    public void setOnFocusListener(OnT9FocusListener listener) {
        this.mT9FocusListener = listener;
    }

    public T9KeyboardAdapter getT9KeyboardAdapter() {
        return this.mT9KeyboardAdapter;
    }
}
