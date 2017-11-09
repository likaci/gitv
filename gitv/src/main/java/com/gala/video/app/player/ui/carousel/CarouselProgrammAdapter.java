package com.gala.video.app.player.ui.carousel;

import android.content.Context;
import android.view.ViewGroup;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CarouselProgrammAdapter extends Adapter<CarouseProgrammeListViewHolder> {
    private static final String TAG = "CarouselProgrammAdapter";
    protected Context mContext;
    protected final List<IVideo> mList = new ArrayList();

    public CarouselProgrammAdapter(List<IVideo> allList, Context context) {
        this.mList.clear();
        this.mList.addAll(allList);
        this.mContext = context;
    }

    public int getCount() {
        return this.mList.size();
    }

    public CarouseProgrammeListViewHolder onCreateViewHolder(ViewGroup parent, int arg1) {
        return new CarouseProgrammeListViewHolder(new CarouseProgrammeListViewItem(this.mContext));
    }

    public void onBindViewHolder(CarouseProgrammeListViewHolder holder, int position) {
        if (holder == null || holder.itemView == null) {
            LogUtils.e(TAG, "onBindViewHolder holder is null !");
        } else if (ListUtils.isEmpty(this.mList)) {
            LogUtils.e(TAG, "onBindViewHolder mDataList is null !");
        } else {
            holder.itemView.setFocusable(true);
            updateData(holder, position);
        }
    }

    private void updateData(CarouseProgrammeListViewHolder holder, int position) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "updateData() position=" + position);
        }
        if (!ListUtils.isEmpty(this.mList)) {
            IVideo video = (IVideo) this.mList.get(position);
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "updateData() video=" + video);
            }
            if (video != null) {
                Album album = video.getAlbum();
                ((CarouseProgrammeListViewItem) holder.listItemLayout).setProgrammeName(album.name);
                ((CarouseProgrammeListViewItem) holder.listItemLayout).setProgrammeTime(formatTime(album.sliveTime));
                ((CarouseProgrammeListViewItem) holder.listItemLayout).setEndTime(album.eliveTime);
                ((CarouseProgrammeListViewItem) holder.listItemLayout).setIsLive(album.isLive);
            }
        }
    }

    private String formatTime(String time) {
        String videoTime = null;
        if (!StringUtils.isEmpty((CharSequence) time)) {
            long startTime = Long.parseLong(time);
            long day = (DeviceUtils.getServerTimeMillis() + 28800000) / 86400000;
            if (startTime < (day * 86400000) - 28800000) {
                videoTime = formatTime(startTime);
            } else if ((day * 86400000) - 28800000 > startTime || startTime >= ((1 + day) * 86400000) - 28800000) {
                videoTime = formatTime(startTime);
            } else {
                videoTime = formatTime(startTime);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "formatTime(" + videoTime + ")");
        }
        return videoTime;
    }

    private String formatTime(long startTime) {
        return new SimpleDateFormat("HH:mm").format(new Date(startTime));
    }
}
