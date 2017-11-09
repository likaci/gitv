package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import java.util.List;

public class TabSortedAdapter {
    public static final int FLAG_ALL_TAB_NOT_SORTABLE = -2;
    public static final int FLAG_ALL_TAB_SORTABLE = -1;
    private static final String TAG = "TabSortedAdapter";
    private Context mContext;
    private List<TabModel> mTabInfoList;

    public TabSortedAdapter(Context context, List<TabModel> tabInfoList) {
        this.mTabInfoList = tabInfoList;
        this.mContext = context;
    }

    public int getCount() {
        return ListUtils.isEmpty(this.mTabInfoList) ? 0 : this.mTabInfoList.size();
    }

    public List<TabModel> getTabInfoList() {
        return this.mTabInfoList;
    }

    public void remove(TabModel tabModel) {
        for (TabModel item : this.mTabInfoList) {
            if (item.getId() == tabModel.getId()) {
                this.mTabInfoList.remove(item);
                return;
            }
        }
    }

    public void updateData(List<TabModel> tabModelList) {
        this.mTabInfoList = tabModelList;
    }

    public int getMaxUnsortableTabIndex() {
        int index = -1;
        if (ListUtils.isEmpty(this.mTabInfoList)) {
            return -1;
        }
        for (TabModel tabModel : this.mTabInfoList) {
            if (!tabModel.isSupportSort()) {
                index = this.mTabInfoList.indexOf(tabModel);
            }
        }
        return index;
    }

    public int getMinSortableTabIndex() {
        int nextIndex = getMaxUnsortableTabIndex() + 1;
        if (nextIndex <= getCount() - 1) {
            return nextIndex;
        }
        return -2;
    }

    public int getSortableTabCount() {
        if (getMinSortableTabIndex() == -2) {
            return 0;
        }
        return getCount() - getMinSortableTabIndex();
    }

    public TabSortedItemView getView(int pos, View convertView, ViewGroup viewGroup) {
        TabModel tabModel = (TabModel) this.mTabInfoList.get(pos);
        TabSortedItemView itemView = new TabSortedItemView(this.mContext);
        itemView.setText(tabModel.getTitle());
        itemView.setData(tabModel);
        return itemView;
    }

    public void onClick(TabSortedItemView tabSortedItemView, int index) {
        if (getSortableTabCount() == 1) {
            tabSortedItemView.updateStatus(TabSortedState.FOCUS);
            return;
        }
        int size = getCount();
        int minSortableChildIndex = getMinSortableTabIndex();
        int maxSortableChildIndex = size - 1;
        if (tabSortedItemView.getCurTabState() == TabSortedState.FOCUS_GUIDE || tabSortedItemView.getCurTabState() == TabSortedState.FOCUS) {
            if (index == minSortableChildIndex) {
                tabSortedItemView.updateStatus(TabSortedState.FOCUS_ACTIVATED_ARROW_RIGHT);
            }
            if (index == maxSortableChildIndex) {
                tabSortedItemView.updateStatus(TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT);
            }
            if (index > minSortableChildIndex && index < maxSortableChildIndex) {
                tabSortedItemView.updateStatus(TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT_RIGHT);
            }
        } else if (tabSortedItemView.getCurTabState() == TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT_RIGHT || tabSortedItemView.getCurTabState() == TabSortedState.FOCUS_ACTIVATED_ARROW_LEFT || tabSortedItemView.getCurTabState() == TabSortedState.FOCUS_ACTIVATED_ARROW_RIGHT) {
            tabSortedItemView.updateStatus(TabSortedState.FOCUS);
        }
    }
}
