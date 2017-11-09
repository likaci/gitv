package com.gala.video.app.epg.ui.albumlist.fragment.right.recommend;

import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.adapter.BaseGridAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.ChannelHorizontalAdapter;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ChannelRecommend3Fragment extends ChannelRecommend1Fragment {
    protected static final int MAX_DISPLAY_SIZE = 4;

    protected int getBigViewWidth() {
        return ResourceUtil.getPx(1452);
    }

    protected float getGridAnimationScale() {
        return 1.09f;
    }

    protected int getLayoutResId() {
        return C0508R.layout.epg_channel_recommed_page3;
    }

    protected int getBigViewHeight() {
        return ResourceUtil.getPx(478);
    }

    protected void setBigViewData() {
        super.setBigViewData();
        this.mBigView.setFocusScale(1.04f);
    }

    protected BaseGridAdapter<IData> getAdapter() {
        return new ChannelHorizontalAdapter(this.mContext, AlbumViewType.RECOMMEND_HORIZONTAL);
    }

    protected int getMaxDisplaySize() {
        return 4;
    }
}
