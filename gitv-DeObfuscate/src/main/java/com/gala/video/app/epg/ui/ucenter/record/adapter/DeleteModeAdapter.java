package com.gala.video.app.epg.ui.ucenter.record.adapter;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.ui.albumlist.adapter.GridAdapter;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.common.widget.AlbumView;

public class DeleteModeAdapter extends GridAdapter {
    public static final int VIEW_TYPE_LOGIN = 19;
    private boolean mDeleteMode = false;
    private boolean mIsShowCacheView = false;

    public DeleteModeAdapter(Context context) {
        super(context);
    }

    public DeleteModeAdapter(Context context, AlbumViewType type) {
        super(context, type);
    }

    public int getDefalutCount() {
        if (this.mIsShowCacheView) {
            return 12;
        }
        return 0;
    }

    public void showCacheView(boolean isShowCacheView) {
        if (this.mIsShowCacheView != isShowCacheView) {
            this.mIsShowCacheView = isShowCacheView;
            notifyDataSetChanged();
        }
    }

    public void onBindItemViewHolder(ViewHolder holder, int position, LayoutParams params) {
        super.onBindItemViewHolder(holder, position, params);
        AlbumView itemView = holder.itemView;
        itemView.setRecycleCoverVisible(this.mDeleteMode ? 0 : 8);
        if (!isShowingDefault(itemView)) {
            setDescAndCorner(itemView);
        }
    }

    public void setDeleteMode(boolean mode) {
        this.mDeleteMode = mode;
    }

    public boolean isDeleteMode() {
        return this.mDeleteMode;
    }
}
