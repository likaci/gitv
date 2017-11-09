package com.gala.video.app.epg.ui.ucenter.record.adapter;

import android.content.Context;
import android.view.View;
import com.gala.video.app.epg.ui.albumlist.widget.FavoriteHistoryItemView;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;

public class FavouriteAdapter extends DeleteModeAdapter {
    public FavouriteAdapter(Context context) {
        super(context);
    }

    public FavouriteAdapter(Context context, AlbumViewType type) {
        super(context, type);
    }

    protected View onCreateItemViewHolder(int viewType) {
        FavoriteHistoryItemView albumView = new FavoriteHistoryItemView(this.mContext.getApplicationContext(), AlbumViewType.VERTICAL);
        albumView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
        albumView.setPageType("favorite");
        albumView.setImageDrawable(getDefaultDrawable());
        return albumView;
    }
}
