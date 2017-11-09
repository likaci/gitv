package com.gala.video.lib.share.uikit.core;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.lib.share.C1632R;

public class ViewHolderCreator<T extends ViewHolder, V extends View> {
    private static final int ID_TAG = C1632R.id.share_global_error_panel_layout;
    public static final String TAG = "ViewHolderCreator";
    public final Class<T> mClz;
    public final int mLayoutResId;
    public final Class<V> viewClz;

    public static abstract class ViewHolder {
        protected final Context mContext;

        protected abstract void onRootViewCreated(View view);

        public ViewHolder(Context context) {
            this.mContext = context;
        }
    }

    public ViewHolderCreator(int layoutResId, Class<T> clz, Class<V> viewClz) {
        this.mLayoutResId = layoutResId;
        this.mClz = clz;
        this.viewClz = viewClz;
    }

    public V create(Context context, ViewGroup parent) {
        try {
            View view = (View) this.viewClz.cast(LayoutInflater.from(context).inflate(this.mLayoutResId, parent, false));
            ViewHolder holder = (ViewHolder) this.mClz.getConstructor(new Class[]{Context.class}).newInstance(new Object[]{context});
            holder.onRootViewCreated(view);
            view.setTag(ID_TAG, holder);
            return view;
        } catch (Exception e) {
            Log.e(TAG, "Exception when inflate layout: " + context.getResources().getResourceName(this.mLayoutResId) + " stack: " + Log.getStackTraceString(e), e);
            return null;
        }
    }

    public static ViewHolder getViewHolderFromView(View view) {
        Object holder = view.getTag(ID_TAG);
        if (holder instanceof ViewHolder) {
            return (ViewHolder) holder;
        }
        return null;
    }
}
