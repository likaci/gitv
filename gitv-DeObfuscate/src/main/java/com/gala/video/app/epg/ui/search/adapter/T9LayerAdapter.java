package com.gala.video.app.epg.ui.search.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.utils.QSizeUtils;

public class T9LayerAdapter extends BaseAdapter {
    private Context mContext;
    private int mCount;

    public T9LayerAdapter(Context context, int count) {
        this.mContext = context;
        this.mCount = count;
    }

    public int getCount() {
        return this.mCount;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int background = C0508R.drawable.epg_t9_layer_bg;
        int textColor = C0508R.color.gala_green;
        TextView view = new TextView(this.mContext);
        view.setBackgroundResource(background);
        view.setTextColor(getColor(textColor));
        QSizeUtils.setTextSize(view, C0508R.dimen.dimen_29dp);
        view.setGravity(17);
        return view;
    }

    public void changeTextColor(View view, boolean hasFocus) {
        ((TextView) view).setTextColor(getColor(hasFocus ? C0508R.color.gala_write : C0508R.color.gala_green));
    }

    public void setSpecialLayerBg(View specialView) {
    }

    private int getColor(int id) {
        return this.mContext.getResources().getColor(id);
    }
}
