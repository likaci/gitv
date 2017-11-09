package com.gala.video.app.epg.ui.albumlist.adapter;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;
import com.gala.sdk.player.TipType;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ChannelHorizontalAdapter extends GridAdapter {
    public static final int HEIGHT = ResourceUtil.getPx(TipType.CONCRETE_TYPE_HISTORY);
    public static final int WIDTH = ResourceUtil.getPx(369);

    public ChannelHorizontalAdapter(Context context, AlbumViewType type) {
        super(context, type);
    }

    public void initLayoutParamsParams(ViewHolder holder, LayoutParams params) {
        params.width = WIDTH;
        params.height = HEIGHT;
    }
}
