package com.gala.video.test;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.gala.video.widget.adapter.ViewAdapter;
import com.gala.video.widget.test.R;
import java.util.List;

public class ListAdapter<T> extends ViewAdapter<T> {
    private List<Integer> datas = null;
    private LayoutInflater mInflater;

    public class ViewHolder {
        ImageView iv;
    }

    public ListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public final int getCount() {
        return (this.datas == null || this.datas.size() <= 0) ? 0 : this.datas.size();
    }

    public final Object getItem(int position) {
        return this.datas.get(position);
    }

    public final long getItemId(int position) {
        return (long) position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.listview_item_demo, null);
            holder.iv = (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.iv.setImageResource(((Integer) this.datas.get(position)).intValue());
        return convertView;
    }
}
