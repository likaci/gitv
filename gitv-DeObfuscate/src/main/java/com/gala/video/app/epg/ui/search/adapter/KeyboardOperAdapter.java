package com.gala.video.app.epg.ui.search.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.search.ISearchConstant;
import java.lang.reflect.Field;

public class KeyboardOperAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater = LayoutInflater.from(this.mContext);

    public KeyboardOperAdapter(Activity activity) {
        this.mContext = activity.getApplicationContext();
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
        return 2;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = this.mInflater.inflate(C0508R.layout.epg_search_keyboard_operate_item, null);
        TextView textView = (TextView) convertView.findViewById(C0508R.id.epg_operate_text);
        switch (position) {
            case 0:
                convertView.setTag(ISearchConstant.KEYBOARD_OPER_TAG_KEY, Integer.valueOf(1));
                textView.setText(this.mContext.getResources().getString(C0508R.string.search_keyboard_remove));
                break;
            case 1:
                convertView.setTag(ISearchConstant.KEYBOARD_OPER_TAG_KEY, Integer.valueOf(0));
                textView.setText(this.mContext.getResources().getString(C0508R.string.search_keyboard_clear));
                break;
        }
        convertView.setBackgroundResource(C0508R.drawable.share_keyboard_operkey_bg);
        return convertView;
    }
}
