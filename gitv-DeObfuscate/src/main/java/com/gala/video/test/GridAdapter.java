package com.gala.video.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.widget.adapter.ViewAdapter;
import com.gala.video.widget.test.R;
import com.gala.video.widget.util.LogUtils;
import com.gala.video.widget.view.GridItemLayout;
import java.util.ArrayList;
import java.util.List;

public class GridAdapter<T> extends ViewAdapter<T> {
    private List<Integer> mDataList = new ArrayList();
    private LayoutInflater mInflater;

    public class ViewHolder {
        GridItemLayout layout;
    }

    public GridAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return (this.mDataList == null || this.mDataList.size() <= 0) ? 0 : this.mDataList.size();
    }

    public Object getItem(int position) {
        return this.mDataList.get(position);
    }

    public long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.item, null);
            holder.layout = (GridItemLayout) convertView.findViewById(R.id.grid_item_layout);
            holder.layout.setTextSize(24.0f);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.layout.setImageResource(position, ((Integer) this.mDataList.get(position)).intValue());
        return convertView;
    }

    public void notifyDataSetChanged(List<T> datas) {
        this.mDataList.clear();
        this.mDataList.addAll(datas);
        notifyDataSetChanged();
        log("GridAdapter  notifyDataSetChanged :   ok !");
    }

    private void log(String msg) {
        LogUtils.m1601e("GridViewPager", msg);
    }
}
