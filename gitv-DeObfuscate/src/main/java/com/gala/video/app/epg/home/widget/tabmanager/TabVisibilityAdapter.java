package com.gala.video.app.epg.home.widget.tabmanager;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import java.util.List;

public class TabVisibilityAdapter {
    private static final String TAG = "tabmanager/TabVisibilityAdapter";
    private Context mContext;
    private List<TabModel> mTabInfoList;

    public TabVisibilityAdapter(Context context, List<TabModel> tabInfoList) {
        this.mTabInfoList = tabInfoList;
        this.mContext = context;
    }

    public int getCount() {
        return ListUtils.isEmpty(this.mTabInfoList) ? 0 : this.mTabInfoList.size();
    }

    public List<TabModel> getTabInfoList() {
        return this.mTabInfoList;
    }

    public TabVisibilityItemView getView(int pos, View convertView, ViewGroup viewGroup) {
        TabModel tabModel = (TabModel) this.mTabInfoList.get(pos);
        TabVisibilityItemView itemView = new TabVisibilityItemView(this.mContext);
        itemView.setText(tabModel.getTitle());
        itemView.setData(tabModel);
        return itemView;
    }
}
