package com.gala.video.app.epg.ui.search.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridParams;
import com.gala.video.app.epg.ui.albumlist.widget.PhotoGridView;
import com.gala.video.app.epg.ui.albumlist.widget.WidgetStatusListener;
import com.gala.video.app.epg.ui.search.adapter.T9LayerAdapter;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class T9Layer extends RelativeLayout {
    private Context mContext;
    private T9LayerAdapter mLayerAdapter;
    private View mLayerButtom;
    private View mLayerCenter;
    private String mLayerContent;
    private int mLayerItemHeight;
    private int mLayerItemWidth;
    private View mLayerLeft;
    private PhotoGridView mLayerMainView;
    private View mLayerRight;
    private View mLayerTop;
    private LayerTypeEnum mLayerType = LayerTypeEnum.MAX;
    private OnT9LayerClickListener mT9LayerClickListener;
    private WidgetStatusListener mWidgetStatusListener = new C10291();

    public interface OnT9LayerClickListener {
        void onClick(ViewGroup viewGroup, View view, int i);
    }

    class C10291 implements WidgetStatusListener {
        C10291() {
        }

        public void onLoseFocus(ViewGroup arg0, View arg1, int arg2) {
        }

        public void onItemTouch(View arg0, MotionEvent arg1, int arg2) {
        }

        public void onItemSelectChange(View view, int arg1, boolean hasFocus) {
            float scale = Project.getInstance().getBuild().isLitchi() ? 1.0f : 1.1f;
            if (hasFocus) {
                view.bringToFront();
                T9Layer.this.mLayerAdapter.changeTextColor(view, hasFocus);
                AnimationUtil.scaleAnimation(view, 1.0f, scale, 200);
                return;
            }
            T9Layer.this.mLayerAdapter.changeTextColor(view, hasFocus);
            AnimationUtil.scaleAnimation(view, 1.0f, 1.0f, 200);
        }

        public void onItemClick(ViewGroup viewGroup, View view, int pos) {
            if (T9Layer.this.mT9LayerClickListener != null) {
                T9Layer.this.mT9LayerClickListener.onClick(viewGroup, view, pos);
            }
        }
    }

    public enum LayerTypeEnum {
        MIN,
        MID,
        MAX
    }

    public T9Layer(Context context) {
        super(context);
        this.mContext = context;
    }

    public T9Layer(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void setParams(int width, int height, String content) {
        this.mLayerItemWidth = width;
        this.mLayerItemHeight = height;
        this.mLayerContent = content;
        initLayerType();
        generateLayer();
    }

    private void initLayerType() {
        if (!StringUtils.isEmpty(this.mLayerContent)) {
            if (this.mLayerContent.length() == 2) {
                this.mLayerType = LayerTypeEnum.MIN;
            } else if (this.mLayerContent.length() == 4) {
                this.mLayerType = LayerTypeEnum.MID;
            } else if (this.mLayerContent.length() == 5) {
                this.mLayerType = LayerTypeEnum.MAX;
            }
        }
    }

    private void generateLayer() {
        this.mLayerMainView = new PhotoGridView(this.mContext);
        this.mLayerMainView.setLayoutParams(new LayoutParams(-2, -2));
        initLayerParams();
        addLayer();
    }

    private void addLayer() {
        LayoutParams lp = new LayoutParams(-2, -2);
        lp.addRule(13);
        addView(this.mLayerMainView, lp);
    }

    private void initLayerParams() {
        this.mLayerMainView.setNextDownFocusLeaveAvail(false);
        this.mLayerMainView.setNextUpFocusLeaveAvail(false);
        this.mLayerMainView.setNextLeftFocusLeaveAvail(false);
        this.mLayerMainView.setNextRightFocusLeaveAvail(false);
        switch (this.mLayerType) {
            case MIN:
                this.mLayerAdapter = new T9LayerAdapter(this.mContext, 2);
                this.mLayerMainView.setParams(getMinParam());
                this.mLayerMainView.setAdapter(this.mLayerAdapter);
                this.mLayerLeft = this.mLayerMainView.getViewByPos(0);
                this.mLayerRight = this.mLayerMainView.getViewByPos(1);
                this.mLayerLeft.setNextFocusUpId(this.mLayerLeft.getId());
                this.mLayerLeft.setNextFocusDownId(this.mLayerLeft.getId());
                this.mLayerRight.setNextFocusUpId(this.mLayerRight.getId());
                this.mLayerRight.setNextFocusDownId(this.mLayerRight.getId());
                break;
            case MID:
                this.mLayerAdapter = new T9LayerAdapter(this.mContext, 6);
                this.mLayerMainView.setParams(getMidParam());
                this.mLayerMainView.setAdapter(this.mLayerAdapter);
                this.mLayerMainView.getViewByPos(0).setVisibility(4);
                this.mLayerMainView.getViewByPos(2).setVisibility(4);
                this.mLayerLeft = this.mLayerMainView.getViewByPos(3);
                this.mLayerRight = this.mLayerMainView.getViewByPos(5);
                this.mLayerCenter = this.mLayerMainView.getViewByPos(4);
                this.mLayerTop = this.mLayerMainView.getViewByPos(1);
                this.mLayerTop.setNextFocusUpId(this.mLayerTop.getId());
                this.mLayerTop.setNextFocusLeftId(this.mLayerLeft.getId());
                this.mLayerTop.setNextFocusRightId(this.mLayerRight.getId());
                this.mLayerCenter.setNextFocusUpId(this.mLayerTop.getId());
                this.mLayerLeft.setNextFocusUpId(this.mLayerTop.getId());
                this.mLayerRight.setNextFocusUpId(this.mLayerTop.getId());
                break;
            case MAX:
                this.mLayerAdapter = new T9LayerAdapter(this.mContext, 9);
                this.mLayerMainView.setParams(getMaxParam());
                this.mLayerMainView.setAdapter(this.mLayerAdapter);
                this.mLayerMainView.getViewByPos(0).setVisibility(4);
                this.mLayerMainView.getViewByPos(2).setVisibility(4);
                this.mLayerMainView.getViewByPos(6).setVisibility(4);
                this.mLayerMainView.getViewByPos(8).setVisibility(4);
                this.mLayerLeft = this.mLayerMainView.getViewByPos(3);
                this.mLayerRight = this.mLayerMainView.getViewByPos(5);
                this.mLayerCenter = this.mLayerMainView.getViewByPos(4);
                this.mLayerTop = this.mLayerMainView.getViewByPos(1);
                this.mLayerButtom = this.mLayerMainView.getViewByPos(7);
                this.mLayerTop.setNextFocusUpId(this.mLayerTop.getId());
                this.mLayerTop.setNextFocusLeftId(this.mLayerLeft.getId());
                this.mLayerTop.setNextFocusRightId(this.mLayerRight.getId());
                this.mLayerCenter.setNextFocusDownId(this.mLayerButtom.getId());
                this.mLayerCenter.setNextFocusUpId(this.mLayerTop.getId());
                this.mLayerLeft.setNextFocusDownId(this.mLayerButtom.getId());
                this.mLayerLeft.setNextFocusUpId(this.mLayerTop.getId());
                this.mLayerRight.setNextFocusDownId(this.mLayerButtom.getId());
                this.mLayerRight.setNextFocusUpId(this.mLayerTop.getId());
                this.mLayerButtom.setNextFocusDownId(this.mLayerButtom.getId());
                this.mLayerButtom.setNextFocusLeftId(this.mLayerLeft.getId());
                this.mLayerButtom.setNextFocusRightId(this.mLayerRight.getId());
                this.mLayerAdapter.setSpecialLayerBg(this.mLayerCenter);
                break;
        }
        this.mLayerLeft.setNextFocusLeftId(this.mLayerLeft.getId());
        this.mLayerRight.setNextFocusRightId(this.mLayerRight.getId());
        this.mLayerMainView.setListener(this.mWidgetStatusListener);
        generateT9Content(this.mLayerContent);
    }

    private PhotoGridParams getMinParam() {
        return getPhotoGridParam(2, this.mLayerItemWidth, this.mLayerItemHeight);
    }

    private PhotoGridParams getMidParam() {
        return getPhotoGridParam(3, this.mLayerItemWidth, this.mLayerItemHeight);
    }

    private PhotoGridParams getMaxParam() {
        return getPhotoGridParam(3, this.mLayerItemWidth, this.mLayerItemHeight);
    }

    private PhotoGridParams getPhotoGridParam(int columnNum, int layerItemWidth, int layerItemHeight) {
        PhotoGridParams p = new PhotoGridParams();
        p.columnNum = columnNum;
        p.verticalSpace = ResourceUtil.getDimen(C0508R.dimen.dimen_2dp);
        p.horizontalSpace = ResourceUtil.getDimen(C0508R.dimen.dimen_2dp);
        p.contentHeight = layerItemHeight;
        p.contentWidth = layerItemWidth;
        p.scaleRate = 1.1f;
        return p;
    }

    private void generateT9Content(String content) {
        if (!StringUtils.isEmpty((CharSequence) content)) {
            switch (this.mLayerType) {
                case MIN:
                    ((TextView) this.mLayerLeft).setText("" + content.charAt(0));
                    ((TextView) this.mLayerRight).setText("" + content.charAt(1));
                    return;
                case MID:
                    ((TextView) this.mLayerTop).setText("" + content.charAt(0));
                    ((TextView) this.mLayerLeft).setText("" + content.charAt(1));
                    ((TextView) this.mLayerCenter).setText("" + content.charAt(2));
                    ((TextView) this.mLayerRight).setText("" + content.charAt(3));
                    return;
                case MAX:
                    ((TextView) this.mLayerTop).setText("" + content.charAt(0));
                    ((TextView) this.mLayerLeft).setText("" + content.charAt(1));
                    ((TextView) this.mLayerCenter).setText("" + content.charAt(2));
                    ((TextView) this.mLayerRight).setText("" + content.charAt(3));
                    ((TextView) this.mLayerButtom).setText("" + content.charAt(4));
                    return;
                default:
                    return;
            }
        }
    }

    public void layerChanged(String content) {
        this.mLayerContent = content;
        initLayerType();
        initLayerParams();
    }

    public View getLayerCenter() {
        return this.mLayerCenter;
    }

    public View getLayerLeft() {
        return this.mLayerLeft;
    }

    public View getLayerRight() {
        return this.mLayerRight;
    }

    public View getLayerTop() {
        return this.mLayerTop;
    }

    public View getLayerButtom() {
        return this.mLayerButtom;
    }

    public LayerTypeEnum getT9LayerType() {
        return this.mLayerType;
    }

    public void focusChanged() {
        switch (this.mLayerType) {
            case MIN:
                this.mLayerLeft.requestFocus();
                return;
            case MID:
            case MAX:
                this.mLayerCenter.requestFocus();
                return;
            default:
                return;
        }
    }

    public void setT9LayerClickListener(OnT9LayerClickListener listener) {
        this.mT9LayerClickListener = listener;
    }
}
