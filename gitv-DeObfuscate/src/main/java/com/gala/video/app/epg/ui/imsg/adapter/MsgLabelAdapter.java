package com.gala.video.app.epg.ui.imsg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class MsgLabelAdapter extends Adapter<MyViewHolder> {
    private static final String TAG = "EPG/MsgLabelAdapter";
    private static final int TYPE_LEVEL_1 = 1;
    private static final int TYPE_LEVEL_2 = 2;
    private final int DEFAULT_REMAINED_POS;
    private Context mContext;
    private int mGreenColor;
    private List<Tag> mLabelList;
    private int mRemainedPosition;

    static class MyViewHolder extends ViewHolder {
        public MyViewHolder(View view) {
            super(view);
        }
    }

    public MsgLabelAdapter(Context context) {
        this.mLabelList = new ArrayList(3);
        this.DEFAULT_REMAINED_POS = -1;
        this.mRemainedPosition = -1;
        this.mGreenColor = ResourceUtil.getColor(C0508R.color.gala_green);
        this.mContext = context;
    }

    public MsgLabelAdapter(Context context, VerticalGridView gridView) {
        this(context);
    }

    public void setList(List<Tag> list) {
        this.mLabelList.clear();
        if (!ListUtils.isEmpty((List) list)) {
            this.mLabelList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void updateList(List<Tag> list) {
        this.mLabelList.clear();
        if (!ListUtils.isEmpty((List) list)) {
            this.mLabelList.addAll(list);
        }
        notifyDataSetChanged();
    }

    public List<Tag> getList() {
        return this.mLabelList;
    }

    public int getCount() {
        return ListUtils.getCount(this.mLabelList);
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (holder == null || holder.itemView == null) {
            LogUtils.m1571e(TAG, "onBindViewHolder holder is null !");
        } else if (ListUtils.isEmpty(this.mLabelList)) {
            LogUtils.m1571e(TAG, "onBindViewHolder mLabelList is null !");
        } else {
            TextView textView = holder.itemView;
            if (textView != null) {
                textView.setText(((Tag) this.mLabelList.get(position)).getName());
                textView.setPadding(ResourceUtil.getDimen(C0508R.dimen.dimen_40dp), 0, 0, 0);
                if (this.mRemainedPosition == position) {
                    textView.setTextColor(this.mGreenColor);
                } else {
                    textView.setTextColor(ResourceUtil.getColorStateList(C0508R.drawable.epg_label_left_color_selector));
                }
            }
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = C0508R.layout.epg_label_item_view_msg;
        if (viewType == 1) {
            layoutId = C0508R.layout.epg_label_item_view_level1_msg;
        }
        return new MyViewHolder(LayoutInflater.from(this.mContext).inflate(layoutId, parent, false));
    }

    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        }
        return 2;
    }

    public int getRemainedPosition() {
        return this.mRemainedPosition;
    }

    public void setRemainedPosition(int pos) {
        this.mRemainedPosition = pos;
        notifyDataSetUpdate();
    }

    public void resetUIState() {
        this.mRemainedPosition = -1;
        notifyDataSetUpdate();
    }
}
