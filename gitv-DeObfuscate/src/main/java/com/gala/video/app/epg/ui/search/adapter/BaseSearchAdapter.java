package com.gala.video.app.epg.ui.search.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.utils.QSizeUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.util.List;

public class BaseSearchAdapter<T> extends BaseTabAdapter<T> {
    protected Context mContext;
    protected List<T> mDataList;
    protected LayoutInflater mInflater;

    static class ViewHolder {
        TextView searchTextView;

        ViewHolder() {
        }
    }

    public BaseSearchAdapter(Context context, List<T> dataList) {
        this.mContext = context;
        this.mDataList = dataList;
        this.mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return ListUtils.getCount(this.mDataList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = new TextView(this.mContext);
            holder.searchTextView = (TextView) convertView;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        setViewParams(convertView, position);
        fillData(holder, convertView, position);
        return convertView;
    }

    protected void fillData(ViewHolder holder, View convertView, int position) {
    }

    protected void setViewParams(View convertView, int position) {
        TextView textView = (TextView) convertView;
        textView.setBackgroundResource(C0508R.drawable.share_keyboard_key_bg);
        textView.setSingleLine(true);
        textView.setEllipsize(TruncateAt.END);
        textView.setTextColor(this.mContext.getResources().getColor(C0508R.color.search_right_text_color));
        QSizeUtils.setTextSize(textView, C0508R.dimen.dimen_24dp);
    }

    public void notifyDataSetChanged(List<T> list) {
        this.mDataList = list;
        super.notifyDataSetChanged();
    }

    protected void requestBitmapSucc(Bitmap netBitmap, Object cookie) {
    }

    protected void requestBitmapFailed(ImageRequest imageRequest, Exception exception) {
    }
}
