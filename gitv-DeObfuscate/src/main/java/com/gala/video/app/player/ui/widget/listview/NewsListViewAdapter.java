package com.gala.video.app.player.ui.widget.listview;

import android.content.Context;
import android.view.View;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.ui.widget.listview.BaseDetailListAdapter.DetailListViewHolder;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;

public class NewsListViewAdapter extends BaseDetailListAdapter {
    public NewsListViewAdapter(List<Album> mDatas, Context mContext) {
        super(mDatas, mContext);
    }

    public View initCovertView(int positon) {
        NewsDetaiListViewItem item = new NewsDetaiListViewItem(this.mContext);
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d("NewsListViewAdapter", "initCovertView positon " + positon);
        }
        item.setId(positon);
        DetailListViewHolder holder = new DetailListViewHolder();
        holder.listItemLayout = item;
        item.setTag(holder);
        return item;
    }
}
