package com.gala.video.app.epg.ui.albumlist.widget.adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class LabelAlbumAdapter extends Adapter<MyViewHolder> {
    public static final int DEFAULT_SELECT = -1;
    private static final String TAG = "EPG/album/LabelAlbumAdapter";
    protected static final int TYPE_DEFAULT = 0;
    protected static final int TYPE_LEVEL_1 = 1;
    protected static final int TYPE_LEVEL_2 = 2;
    private ColorStateList mColorStateList = ResourceUtil.getColorStateList(R.drawable.epg_label_left_color_selector);
    private Context mContext;
    protected List<Tag> mDataList;
    private int mGreenColor = ResourceUtil.getColor(R.color.gala_green);
    private AlbumInfoModel mInfoModel;
    private int mSelect = -1;

    static class MyViewHolder extends ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    public LabelAlbumAdapter(Context context, List<Tag> list, AlbumInfoModel infoModel) {
        this.mContext = context;
        this.mInfoModel = infoModel;
        this.mDataList = list;
    }

    public List<Tag> getList() {
        return this.mDataList;
    }

    public int getCount() {
        return ListUtils.getCount(this.mDataList);
    }

    public int getItemViewType(int position) {
        Tag tag = (Tag) this.mDataList.get(position);
        if (!AlbumInfoFactory.isNewVipChannel(this.mInfoModel.getChannelId()) && !AlbumInfoFactory.isLiveChannel(this.mInfoModel.getChannelId(), this.mInfoModel.getPageType())) {
            return 0;
        }
        if (tag.getLevel() == 2) {
            return 2;
        }
        return 1;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder == null || holder.itemView == null) {
            LogUtils.e(TAG, "onBindViewHolder holder is null !");
        } else if (ListUtils.isEmpty(this.mDataList)) {
            LogUtils.e(TAG, "onBindViewHolder mDataList is null !");
        } else {
            Tag tag = (Tag) this.mDataList.get(position);
            if (tag == null) {
                LogUtils.e(TAG, "onBindViewHolder tag is null !");
                return;
            }
            String name = tag.getName();
            TextView title = (TextView) holder.itemView.findViewById(R.id.epg_label_tv);
            if (title != null) {
                title.setPadding(ResourceUtil.getDimen(R.dimen.dimen_56dp), 0, ResourceUtil.getDimen(R.dimen.dimen_20dp), 0);
                if (this.mSelect == position) {
                    title.setTextColor(this.mGreenColor);
                } else {
                    title.setTextColor(this.mColorStateList);
                }
                title.setText(name);
            }
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = R.layout.epg_label_item_view;
        if (viewType == 0 || viewType == 2) {
            layout = R.layout.epg_label_item_view;
        } else if (viewType == 1) {
            layout = R.layout.epg_label_item_view_level1;
        }
        return new MyViewHolder(LayoutInflater.from(this.mContext).inflate(layout, parent, false));
    }

    public void setSelect(int position) {
        this.mSelect = position;
    }

    public int getSelect() {
        return this.mSelect;
    }

    public void setSelectDefault() {
        this.mSelect = -1;
        notifyDataSetUpdate();
    }
}
