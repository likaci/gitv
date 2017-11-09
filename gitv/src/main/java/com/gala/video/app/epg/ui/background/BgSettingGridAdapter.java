package com.gala.video.app.epg.ui.background;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gala.video.albumlist4.widget.RecyclerView.Adapter;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.system.preference.SystemConfigPreference;
import java.util.List;

public class BgSettingGridAdapter extends Adapter<MyViewHolder> {
    private static final String TAG = "BgSettingGridAdapter";
    private Context mContext;
    private List<String> mList;
    private int mSelectedPosition = -1;

    class MyViewHolder extends ViewHolder {
        ImageView albumImage;
        ImageView selectedIcon;

        public MyViewHolder(View view, ImageView albumImage, ImageView selectedIcon) {
            super(view);
            this.albumImage = albumImage;
            this.selectedIcon = (ImageView) view.findViewById(R.id.epg_setting_bg_item_selected);
        }
    }

    public BgSettingGridAdapter(Context context) {
        this.mContext = context;
    }

    public void setList(List<String> list) {
        this.mList = list;
        notifyDataSetChanged();
    }

    public void updateList(List<String> list) {
        this.mList = list;
        notifyDataSetUpdate();
    }

    public List<String> getList() {
        return this.mList;
    }

    public int getCount() {
        return this.mList.size();
    }

    public void setSelectedPosition(int postion) {
        this.mSelectedPosition = postion;
    }

    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.itemView.setFocusable(true);
        holder.itemView.setBackgroundDrawable(this.mContext.getResources().getDrawable(R.drawable.share_item_rect_selector));
        ImageView albumImage = holder.albumImage;
        ImageView selectedIcon = holder.selectedIcon;
        String path = (String) this.mList.get(position);
        LogUtils.d(TAG, "onBindViewHolder---position =  " + position + " ;path =  " + path);
        if (path != null) {
            if (path.equals(SystemConfigPreference.SETTING_BACKGROUND_NIGHT_DEFAULT)) {
                albumImage.setBackgroundColor(this.mContext.getResources().getColor(R.color.setting_night_bg));
            } else {
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                if (bitmap != null) {
                    albumImage.setImageBitmap(bitmap);
                }
            }
        }
        if (position == this.mSelectedPosition) {
            selectedIcon.setVisibility(0);
        } else {
            selectedIcon.setVisibility(4);
        }
    }

    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(this.mContext).inflate(R.layout.epg_view_setting_bg_page_item, parent, false);
        return new MyViewHolder(view, (ImageView) view.findViewById(R.id.epg_setting_bg_item_image), (ImageView) view.findViewById(R.id.epg_setting_bg_item_selected));
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public int getNumRows(int position) {
        return 1;
    }
}
