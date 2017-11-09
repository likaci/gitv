package com.gala.video.app.player.ui.widget.listview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.gala.tvapi.tv2.model.Album;
import java.util.ArrayList;
import java.util.List;

public abstract class BaseDetailListAdapter extends BaseAdapter {
    private static final String TAG = "BaseDetailListAdapter";
    protected Context mContext;
    protected final List<Album> mDatas = new ArrayList();
    protected int mPlayingIndex = -1;

    public class DetailListViewHolder {
        public AbsDetailListViewItem listItemLayout;
        public String vrsTvId;
    }

    public abstract View initCovertView(int i);

    public BaseDetailListAdapter(List<Album> datas, Context context) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        this.mContext = context;
    }

    public void updateData(List<Album> datas) {
        this.mDatas.clear();
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public Object getItem(int position) {
        return this.mDatas.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public void setPlayingInicatorIndex(int index) {
        this.mPlayingIndex = index;
        notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = initCovertView(position);
        }
        DetailListViewHolder holder = (DetailListViewHolder) convertView.getTag();
        updateData(holder, position);
        if (position == this.mPlayingIndex) {
            setIndicatorIndex(holder);
        } else {
            clearIndicatorIndex(holder);
        }
        return convertView;
    }

    public void updateData(DetailListViewHolder holder, int position) {
        holder.listItemLayout.setAlbum((Album) this.mDatas.get(position));
        holder.listItemLayout.setId(position);
    }

    public void setIndicatorIndex(DetailListViewHolder holder) {
        holder.listItemLayout.setPlaying(true);
    }

    public void clearIndicatorIndex(DetailListViewHolder holder) {
        holder.listItemLayout.setPlaying(false);
    }
}
