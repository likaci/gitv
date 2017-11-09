package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.view.ViewGroup;
import com.gala.tvapi.tv2.model.TVChannelCarouselTag;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class CarouselLabelAdapter extends Adapter<CarouselLabelListViewHolder> {
    private static final String TAG = "CarouselChannelAdapter";
    protected final List<TVChannelCarouselTag> mAllChannelList = new ArrayList();
    protected Context mContext;
    private int mPosition = -1;

    public CarouselLabelAdapter(Context context) {
        this.mContext = context;
    }

    public int getCount() {
        return this.mAllChannelList.size();
    }

    public void onBindViewHolder(CarouselLabelListViewHolder holder, int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "onBindViewHolder position=" + position);
        }
        if (holder == null || holder.itemView == null) {
            LogUtils.m1571e(TAG, "onBindViewHolder holder is null !");
            return;
        }
        holder.itemView.setFocusable(true);
        updateData(holder, position);
    }

    public CarouselLabelListViewHolder onCreateViewHolder(ViewGroup arg0, int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "CarouselChannelListViewHolder() position=" + position);
        }
        return new CarouselLabelListViewHolder(new CarouseLabelListViewItem(this.mContext));
    }

    private void updateData(CarouselLabelListViewHolder holder, int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "updateData() position=" + position);
        }
        if (this.mAllChannelList != null && this.mAllChannelList.size() > position) {
            ((CarouseLabelListViewItem) holder.listItemLayout).setLabelInfo((TVChannelCarouselTag) this.mAllChannelList.get(position));
            if (position == this.mPosition) {
                ((CarouseLabelListViewItem) holder.listItemLayout).setSelectedColor();
            }
        }
    }

    public void setAllLabelList(List<TVChannelCarouselTag> allChannelList) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "setAllChannelList() size=" + allChannelList.size());
        }
        this.mAllChannelList.clear();
        this.mAllChannelList.addAll(allChannelList);
    }

    public void setSelectedPosition(int position) {
        this.mPosition = position;
    }
}
