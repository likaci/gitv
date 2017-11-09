package com.gala.video.app.epg.ui.applist.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import com.gala.appmanager.appinfo.AppInfo;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.applist.widget.AppView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class AppListAdapter extends Adapter<ItemViewHolder> {
    private Context mContext;
    private List<AppInfo> mDataList;

    class ItemViewHolder extends ViewHolder {
        public ItemViewHolder(View v) {
            super(v);
        }
    }

    public AppListAdapter(Context context) {
        this.mContext = context;
    }

    public void setDataList(List<AppInfo> list) {
        this.mDataList = list;
        notifyDataSetChanged();
    }

    public void updateDataList(List<AppInfo> list) {
        this.mDataList = list;
        notifyDataSetUpdate();
    }

    public List<AppInfo> getDataList() {
        return this.mDataList;
    }

    public int getCount() {
        return ListUtils.getCount(this.mDataList);
    }

    public void onBindViewHolder(ItemViewHolder viewHolder, int position) {
        if (viewHolder != null && viewHolder.itemView != null && ListUtils.isLegal(this.mDataList, position)) {
            viewHolder.itemView.setFocusable(true);
            AppInfo appInfo = (AppInfo) this.mDataList.get(position);
            if (appInfo != null) {
                ((AppView) viewHolder.itemView).setTitle(appInfo.getAppName());
                ((AppView) viewHolder.itemView).setDrawable(appInfo.getAppIcon());
                ((AppView) viewHolder.itemView).setRBDrawable(ResourceUtil.getDrawable(C0508R.drawable.share_applist_focus));
            }
        }
    }

    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ItemViewHolder(new AppView(this.mContext));
    }
}
