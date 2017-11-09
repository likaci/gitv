package com.gala.video.lib.share.uikit.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.lib.share.uikit.core.BinderViewHolder;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.uikit.resolver.BaseBinderResolver;

public class PageAdapter extends GroupBaseAdapter<Item> {
    public PageAdapter(Page page, Context context, BaseBinderResolver<Item> baseViewBinderResolver) {
        super(context, baseViewBinderResolver);
    }

    public void onBindViewHolder(BinderViewHolder<Item, ? extends View> holder, int position) {
        super.onBindViewHolder((BinderViewHolder) holder, position);
        Item item = (Item) getComponent(position);
        LayoutParams params = holder.itemView.getLayoutParams();
        params.width = item.getWidth();
        params.height = item.getHeight();
    }

    public boolean isFocusable(int position) {
        Item item = (Item) getComponent(position);
        if (item == null || item.getType() == 999) {
            return false;
        }
        return true;
    }
}
