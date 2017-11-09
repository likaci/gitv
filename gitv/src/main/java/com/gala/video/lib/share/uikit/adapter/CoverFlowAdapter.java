package com.gala.video.lib.share.uikit.adapter;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import com.gala.video.lib.share.uikit.view.StandardItemView;
import java.util.ArrayList;
import java.util.List;

public class CoverFlowAdapter {
    private List<Item> items = new ArrayList();
    private Context mContext;

    public CoverFlowAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        return this.items == null ? 0 : this.items.size();
    }

    public StandardItemView getView() {
        StandardItemView standardItemView = new StandardItemView(this.mContext);
        standardItemView.setTag(R.id.focus_start_scale, Float.valueOf(1.1f));
        standardItemView.setTag(R.id.focus_end_scale, Float.valueOf(1.15f));
        standardItemView.setTag(CardFocusHelper.FOCUS_ANIMATION_TIME, Integer.valueOf(250));
        return standardItemView;
    }

    public void clear() {
        this.items.clear();
    }

    public void addAllItems(List<Item> list) {
        this.items.addAll(list);
        resortItems(this.items);
    }

    public List<Item> getItems() {
        return this.items;
    }

    public Item getItem(int position) {
        if (ListUtils.isLegal(this.items, position)) {
            return (Item) this.items.get(position);
        }
        return null;
    }

    public void addAdItem(List<Item> adList, boolean hasAdsBeFiltered) {
        if (!ListUtils.isEmpty(this.items) && !ListUtils.isEmpty((List) adList)) {
            if (!hasAdsBeFiltered) {
                this.items.add(this.items.size(), (Item) this.items.get(0));
                this.items.remove(0);
            }
            this.items.add(0, (Item) adList.get(adList.size() - 1));
            if (adList.size() > 1) {
                this.items.addAll(adList.subList(0, adList.size() - 1));
            }
        }
    }

    public void remove(int pos) {
        if (ListUtils.isLegal(this.items, pos)) {
            this.items.remove(pos);
        } else {
            LogUtils.d("", "coverflowAdapter ,remove ,exception , illegal postion :" + pos + ", current items size : " + this.items.size());
        }
    }

    private void resortItems(List<Item> list) {
        if (!ListUtils.isEmpty((List) list)) {
            list.add(0, (Item) list.get(list.size() - 1));
            list.remove(list.size() - 1);
        }
    }
}
