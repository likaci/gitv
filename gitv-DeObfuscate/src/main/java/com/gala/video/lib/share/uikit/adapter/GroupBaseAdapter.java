package com.gala.video.lib.share.uikit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView.Adapter;
import com.gala.video.lib.share.uikit.Component;
import com.gala.video.lib.share.uikit.core.BinderViewHolder;
import com.gala.video.lib.share.uikit.protocol.ControlBinder;
import com.gala.video.lib.share.uikit.resolver.BaseBinderResolver;
import java.util.ArrayList;
import java.util.List;

public abstract class GroupBaseAdapter<T extends Component> extends Adapter<BinderViewHolder<T, ? extends View>> {
    private BaseBinderResolver<T> mBaseCardBinderResolver;
    protected List<T> mComponents = new ArrayList();
    private Context mContext;

    public GroupBaseAdapter(Context context, BaseBinderResolver<T> baseViewBinderResolver) {
        this.mContext = context;
        this.mBaseCardBinderResolver = baseViewBinderResolver;
    }

    public void setData(List<T> data) {
        this.mComponents.clear();
        this.mComponents.addAll(data);
    }

    public BinderViewHolder<T, ? extends View> onCreateViewHolder(ViewGroup parent, int viewType) {
        return createViewHolder((ControlBinder) this.mBaseCardBinderResolver.create(viewType), this.mContext, null);
    }

    public <V extends View> BinderViewHolder<T, V> createViewHolder(ControlBinder<T, V> binder, Context context, ViewGroup parent) {
        return new BinderViewHolder(binder.createView(context, parent), binder);
    }

    public int getItemViewType(int position) {
        return ((Component) this.mComponents.get(position)).getType();
    }

    public int getCount() {
        return this.mComponents == null ? 0 : this.mComponents.size();
    }

    public void onBindViewHolder(BinderViewHolder<T, ? extends View> holder, int position) {
        holder.bind((Component) this.mComponents.get(position));
    }

    public T getComponent(int position) {
        if (position < 0 || position >= getCount()) {
            return null;
        }
        return (Component) this.mComponents.get(position);
    }

    public void release() {
        this.mComponents.clear();
        this.mComponents = null;
        this.mContext = null;
    }
}
