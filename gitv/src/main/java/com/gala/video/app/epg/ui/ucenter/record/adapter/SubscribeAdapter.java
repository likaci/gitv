package com.gala.video.app.epg.ui.ucenter.record.adapter;

import android.content.Context;
import android.view.View;
import com.gala.video.app.epg.ui.albumlist.widget.FavoriteHistoryItemView;
import com.gala.video.app.epg.ui.albumlist.widget.SubscribleItemView;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;

public class SubscribeAdapter extends DeleteModeAdapter {
    public SubscribeAdapter(Context context) {
        super(context);
    }

    public SubscribeAdapter(Context context, AlbumViewType type) {
        super(context, type);
    }

    protected View onCreateItemViewHolder(int viewType) {
        SubscribleItemView albumView = new SubscribleItemView(this.mContext.getApplicationContext(), AlbumViewType.VERTICAL);
        albumView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
        albumView.setPageType(FavoriteHistoryItemView.SubPage);
        albumView.setImageDrawable(getDefaultDrawable());
        return albumView;
    }
}
