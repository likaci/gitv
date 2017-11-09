package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.view.ViewGroup;
import com.gala.sdk.player.data.CarouselChannelDetail;
import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class CarouselChannelAdapter extends Adapter<CarouselChannelListViewHolder> {
    private static final String TAG = "CarouselChannelAdapter";
    protected final List<TVChannelCarousel> mAllChannelList = new ArrayList();
    protected List<CarouselChannelDetail> mAllDetailInfo = new ArrayList();
    protected Context mContext;
    protected int mPlayingIndex = -1;

    public CarouselChannelAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        return this.mAllChannelList.size();
    }

    public void onBindViewHolder(CarouselChannelListViewHolder holder, int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onBindViewHolder position=" + position);
        }
        if (holder == null || holder.itemView == null) {
            LogUtils.m1571e(TAG, "onBindViewHolder holder is null !");
            return;
        }
        holder.itemView.setFocusable(true);
        updateData(holder, position);
        if (position == this.mPlayingIndex) {
            setIndicatorIndex(holder);
        } else {
            clearIndicatorIndex(holder);
        }
    }

    public CarouselChannelListViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "CarouselChannelListViewHolder() position=" + position);
        }
        return new CarouselChannelListViewHolder(new CarouselDetailListViewItem(this.mContext));
    }

    private void clearIndicatorIndex(CarouselChannelListViewHolder holder) {
        ((CarouselDetailListViewItem) holder.listItemLayout).setPlaying(false);
    }

    private void setIndicatorIndex(CarouselChannelListViewHolder holder) {
        ((CarouselDetailListViewItem) holder.listItemLayout).setPlaying(true);
    }

    private void updateData(CarouselChannelListViewHolder holder, int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateData() position=" + position);
        }
        if (this.mAllDetailInfo != null && this.mAllDetailInfo.size() > position) {
            ((CarouselDetailListViewItem) holder.listItemLayout).setChannelInfo((CarouselChannelDetail) this.mAllDetailInfo.get(position));
        }
        ((CarouselDetailListViewItem) holder.listItemLayout).setChannelList((TVChannelCarousel) this.mAllChannelList.get(position));
    }

    public void setAllChannelList(List<TVChannelCarousel> allChannelList) {
        this.mAllChannelList.clear();
        this.mAllChannelList.addAll(allChannelList);
        this.mAllDetailInfo.clear();
    }

    public void updateData(List<CarouselChannelDetail> allDetailInfo) {
        this.mAllDetailInfo.clear();
        this.mAllDetailInfo.addAll(allDetailInfo);
    }

    public void setPlayingIndex(int index) {
        this.mPlayingIndex = index;
    }
}
