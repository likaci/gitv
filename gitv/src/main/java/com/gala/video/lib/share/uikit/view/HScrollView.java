package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.util.AttributeSet;
import com.gala.video.albumlist.layout.GridLayout;
import com.gala.video.albumlist.widget.HorizontalGridView;
import com.gala.video.lib.share.uikit.contract.HScrollContract.Presenter;
import com.gala.video.lib.share.uikit.contract.HScrollContract.View;
import com.gala.video.lib.share.uikit.core.BinderViewHolder;
import java.util.Collections;

public class HScrollView extends HorizontalGridView implements IViewLifecycle<Presenter>, View {
    public HScrollView(Context context) {
        this(context, null);
    }

    public HScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setClipToPadding(false);
        setClipChildren(false);
        setFocusMode(1);
        setScrollRoteScale(0.8f, 1.0f, 2.5f);
        setQuickFocusLeaveForbidden(true);
        setFocusLeaveForbidden(83);
    }

    public void onBind(Presenter object) {
        object.setView(this);
        if (getAdapter() == null) {
            setAdapter(object.getAdapter());
            registerListener(object);
        }
        updateView(object);
    }

    private void registerListener(Presenter object) {
        setOnScrollListener(object.getActionPolicy());
        setOnItemClickListener(object.getActionPolicy());
        setOnItemFocusChangedListener(object.getActionPolicy());
        setOnFocusLostListener(object.getActionPolicy());
        setOnItemStateChangeListener(object.getActionPolicy());
        setOnFirstLayoutListener(object.getActionPolicy());
    }

    private void updateView(Presenter object) {
        boolean z;
        setHorizontalMargin(30);
        if (object.getCardModel().getShowPosition() == 1) {
            z = true;
        } else {
            z = false;
        }
        showPositionInfo(z);
        setPadding(25, object.getCardModel().getBodyPaddingTop(), 25, 0);
        GridLayout layout = new GridLayout();
        layout.setNumRows(1);
        layout.setItemCount(object.getAdapter().getCount());
        getLayoutManager().setLayouts(Collections.singletonList(layout));
    }

    public void onUnbind(Presenter object) {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        for (int i = first; i <= last; i++) {
            ((BinderViewHolder) getViewHolderByPosition(i)).unbind();
        }
    }

    public void onShow(Presenter object) {
        int first = getFirstAttachedPosition();
        int last = getLastAttachedPosition();
        for (int i = first; i <= last; i++) {
            ((BinderViewHolder) getViewHolderByPosition(i)).show();
        }
    }

    public void onHide(Presenter object) {
    }
}
