package com.gala.video.lib.share.uikit.core;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.lib.share.uikit.core.ViewHolderCreator.ViewHolder;
import com.gala.video.lib.share.uikit.protocol.ControlBinder;

public class BaseViewBinder<C, T extends ViewHolder, V extends View> implements ControlBinder<C, V> {
    protected static final String TAG = "BaseCellBinder";
    private MVHelper mMVHelper;
    private DefaultViewCreator<V> mViewCreator;
    private ViewHolderCreator<T, V> mViewHolderCreator;

    public BaseViewBinder(Class<V> viewClz, MVHelper helper) {
        this.mMVHelper = helper;
        this.mViewCreator = new DefaultViewCreator(viewClz);
    }

    public BaseViewBinder(ViewHolderCreator<T, V> viewHolderCreator, MVHelper helper) {
        this.mMVHelper = helper;
        this.mViewHolderCreator = viewHolderCreator;
    }

    public V createView(Context context, ViewGroup parent) {
        if (this.mViewHolderCreator != null) {
            return this.mViewHolderCreator.create(context, parent);
        }
        return this.mViewCreator.create(context, parent);
    }

    public void bindView(C data, V view) {
        this.mMVHelper.bindView(data, view);
    }

    public void unbindView(C data, V view) {
        this.mMVHelper.unbindView(data, view);
    }

    public void showView(C data, V view) {
        this.mMVHelper.showView(data, view);
    }

    public void hideView(C data, V view) {
        this.mMVHelper.hideView(data, view);
    }
}
