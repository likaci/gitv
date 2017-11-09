package com.gala.video.lib.share.uikit.core;

import android.view.View;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.share.uikit.protocol.ControlBinder;

public class BinderViewHolder<C, V extends View> extends ViewHolder {
    public ControlBinder<C, V> controller;
    public C data;
    public V itemView;

    public BinderViewHolder(V itemView, ControlBinder<C, V> binder) {
        super(itemView);
        this.itemView = itemView;
        this.controller = binder;
    }

    public void bind(C data) {
        this.controller.bindView(data, this.itemView);
        this.data = data;
    }

    public void unbind() {
        if (this.data != null) {
            this.controller.unbindView(this.data, this.itemView);
        }
    }

    public void show() {
        if (this.data != null) {
            this.controller.showView(this.data, this.itemView);
        }
    }

    public void hide() {
        if (this.data != null) {
            this.controller.hideView(this.data, this.itemView);
        }
    }
}
