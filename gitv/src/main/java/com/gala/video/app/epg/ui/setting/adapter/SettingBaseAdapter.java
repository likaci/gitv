package com.gala.video.app.epg.ui.setting.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.search.adapter.BaseTabAdapter;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.lang.reflect.Field;
import java.util.List;

public class SettingBaseAdapter<T> extends BaseTabAdapter<T> {
    protected Context mContext;
    protected List<T> mDataList;
    protected LayoutInflater mInflater;

    static class ViewHolder {
        TextView itemDescText;
        TextView itemLastText;
        View itemLastView;
        TextView itemNameText;
        View itemNameView;
        TextView itemOptionMaxText;
        View itemOptionMaxView;
        TextView itemOptionText;
        View itemOptionView;
        TextView itemTitleText;
        View itemTitleView;

        ViewHolder() {
        }
    }

    public SettingBaseAdapter(Context context, List<T> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mInflater = LayoutInflater.from(context);
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(this.mInflater, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        } catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
    }

    public int getCount() {
        return ListUtils.isEmpty(this.mDataList) ? 0 : this.mDataList.size();
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = this.mInflater.inflate(R.layout.epg_setting_item_view, null);
            holder.itemTitleView = convertView.findViewById(R.id.epg_setting_item_title);
            holder.itemTitleText = (TextView) convertView.findViewById(R.id.epg_setting_item_titletext);
            holder.itemNameView = convertView.findViewById(R.id.epg_setting_item_left);
            holder.itemNameText = (TextView) convertView.findViewById(R.id.epg_setting_item_name);
            holder.itemDescText = (TextView) convertView.findViewById(R.id.epg_setting_item_desc);
            holder.itemLastView = convertView.findViewById(R.id.epg_setting_laststate);
            holder.itemLastText = (TextView) convertView.findViewById(R.id.epg_laststate_text);
            holder.itemOptionView = convertView.findViewById(R.id.epg_setting_item_options);
            holder.itemOptionText = (TextView) convertView.findViewById(R.id.epg_option_text);
            holder.itemOptionMaxView = convertView.findViewById(R.id.epg_setting_item_options_max);
            holder.itemOptionMaxText = (TextView) convertView.findViewById(R.id.epg_option_text_max);
            convertView.setTag(holder);
            setViewParams(holder, convertView, position, parent);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        fillData(holder, convertView, position);
        return convertView;
    }

    protected void requestBitmapSucc(Bitmap netBitmap, Object cookie) {
    }

    protected void requestBitmapFailed(ImageRequest imageRequest, Exception exception) {
    }

    public void notifyDataSetChanged(List<T> list) {
        this.mDataList = list;
        super.notifyDataSetChanged();
    }

    protected void fillData(ViewHolder holder, View convertView, int position) {
    }

    protected void setViewParams(ViewHolder holder, View convertView, int position, ViewGroup parent) {
    }

    protected int getDimen(int id) {
        return this.mContext.getResources().getColor(id);
    }

    protected int getColor(int id) {
        return this.mContext.getResources().getColor(id);
    }
}
