package com.gala.video.app.epg.home.widget.actionbar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import org.greenrobot.eventbus.EventBus;

public class ActionBarLayout extends LinearLayout implements OnFocusChangeListener, OnClickListener {
    private static final String TAG = "home/ActionBarLayout";
    private ActionBarStateListener mActionBarAdapter;
    private Context mContext;
    private View mFocusView;

    public ActionBarLayout(Context context) {
        super(context);
        init(context);
    }

    public ActionBarLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ActionBarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setOrientation(0);
        setClipChildren(false);
        setClipToPadding(false);
        this.mContext = context;
    }

    public void onFocusChange(View v, boolean hasFocus) {
        this.mFocusView = v;
        LogUtils.d(TAG, "onFocusChange() --- hasFocus = " + hasFocus + "  v.tag = " + v.getTag());
        this.mActionBarAdapter.onChildFocusChanged(v, hasFocus);
    }

    public void setAdapter(ActionBarStateListener actionBarAdapter) {
        this.mActionBarAdapter = actionBarAdapter;
        int length = this.mActionBarAdapter.getCount();
        int i = 0;
        while (i < length) {
            View childView = this.mActionBarAdapter.getView(i, null, null);
            MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
            if (childView.getLayoutParams() == null) {
                layoutParams = new LayoutParams(-2, -2);
            }
            if (i != length - 1) {
                layoutParams.rightMargin = ResourceUtil.getDimen(R.dimen.dimen_5dp);
            }
            layoutParams.height = this.mContext.getResources().getDimensionPixelSize(R.dimen.dimen_56dp);
            childView.setOnClickListener(this);
            childView.setOnFocusChangeListener(this);
            if (i == length - 1) {
                if (this.mActionBarAdapter.getActionBarPageType() != ActionBarPageType.HOME_PAGE) {
                    childView.setNextFocusRightId(childView.getId());
                }
                if (i - 1 >= 0) {
                    childView.setNextFocusLeftId(this.mActionBarAdapter.getItemId(i - 1));
                }
            }
            if (i == 0) {
                childView.setNextFocusLeftId(childView.getId());
                if (i + 1 < length) {
                    childView.setNextFocusRightId(this.mActionBarAdapter.getItemId(i + 1));
                }
            }
            if (i > 0 && i < length - 1) {
                childView.setNextFocusLeftId(this.mActionBarAdapter.getItemId(i - 1));
                childView.setNextFocusRightId(this.mActionBarAdapter.getItemId(i + 1));
            }
            childView.setNextFocusUpId(childView.getId());
            addView(childView, layoutParams);
            i++;
        }
    }

    public void setLastFocusRightViewId(int id) {
        View lastView = getChildAt(getChildCount() - 1);
        if (lastView != null) {
            lastView.setNextFocusRightId(id);
        }
    }

    public void setLastFocusHimself() {
        View lastView = getChildAt(getChildCount() - 1);
        if (lastView != null) {
            lastView.setNextFocusRightId(lastView.getId());
        }
    }

    public void onClick(View v) {
        int position = indexOfChild(v) + 1;
        this.mActionBarAdapter.onClick(v, position);
        LogUtils.d(TAG, "onClick(),  v.tag = " + v.getTag() + ",  position = " + position);
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        this.mActionBarAdapter.dispatchKeyEvent(event);
        if ((event.getKeyCode() != 66 && event.getKeyCode() != 23) || event.getAction() != 0 || this.mFocusView == null) {
            return super.dispatchKeyEvent(event);
        }
        this.mFocusView.performClick();
        return true;
    }

    public boolean hasFocus() {
        return getFocusedChild() != null;
    }

    public void resetAdapter() {
        removeAllViews();
        setAdapter(this.mActionBarAdapter);
        EventBus.getDefault().post(new CheckInBuildEvent());
    }
}
