package com.gala.video.app.epg.ui.search.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.search.widget.T9Keyboard;
import com.gala.video.lib.share.utils.ResourceUtil;

public class T9KeyboardAdapter extends BaseAdapter {
    private static final String[] T9_LETTER = new String[]{"zhanwei", "ABC", "DEF", "GHI", "JKL", "MNO", "PQRS", "TUV", "WXYZ"};
    private static final String[] T9_NUMBER = new String[]{"zhanwei", "2", "3", "4", "5", "6", "7", "8", "9"};
    private final int TYPE_NOMAL = 1;
    private final int TYPE_ONLY_NUM = 0;
    private Context mContext;
    private LayoutInflater mInflater;

    static class ViewHolder {
        TextView keyNumFirst;
        TextView keyNumSecond;
        TextView normalKeyLetter;
        TextView normalKeyNum;
        View numKeyLine;

        ViewHolder() {
        }
    }

    public T9KeyboardAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(this.mContext);
    }

    public int getCount() {
        return T9_NUMBER.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        int type = getItemViewType(position);
        switch (type) {
            case 0:
                convertView = this.mInflater.inflate(R.layout.epg_t9_key_item_num, null);
                convertView.findViewById(R.id.epg_key_line).setBackgroundColor(getColor(getNumKeyLineColor(false)));
                break;
            case 1:
                convertView = this.mInflater.inflate(R.layout.epg_t9_key_item_nomal, null);
                convertView.findViewById(R.id.epg_key_line).setVisibility(0);
                break;
        }
        if (position == 6 || position == 8) {
            View lineView = convertView.findViewById(R.id.epg_key_line);
            LayoutParams lp = lineView.getLayoutParams();
            lp.width = (int) this.mContext.getResources().getDimension(R.dimen.dimen_73dp);
            lineView.setLayoutParams(lp);
        }
        convertView.setBackgroundResource(R.drawable.epg_full_keyboard_bg);
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.numKeyLine = convertView.findViewById(R.id.epg_key_line);
        viewHolder.keyNumFirst = (TextView) convertView.findViewById(R.id.epg_key_num_first);
        viewHolder.keyNumSecond = (TextView) convertView.findViewById(R.id.epg_key_num_second);
        viewHolder.normalKeyNum = (TextView) convertView.findViewById(R.id.epg_nomal_key_num);
        viewHolder.normalKeyLetter = (TextView) convertView.findViewById(R.id.epg_nomal_key_letter);
        convertView.setTag(viewHolder);
        if (viewHolder.keyNumFirst != null) {
            viewHolder.keyNumFirst.setTextColor(getColor(getKeyNumFirstColor(false)));
        }
        if (viewHolder.keyNumSecond != null) {
            viewHolder.keyNumSecond.setTextColor(getColor(getKeyNumSecondColor(false)));
        }
        if (viewHolder.normalKeyNum != null) {
            viewHolder.normalKeyNum.setTextColor(getColor(getNormalKeyNumColor(false)));
        }
        if (viewHolder.normalKeyLetter != null) {
            viewHolder.normalKeyLetter.setTextColor(getColor(getNormalKeyLetterColor(false)));
        }
        fillData(convertView, position, type);
        return convertView;
    }

    private void fillData(View convertView, int position, int type) {
        if (type == 0) {
            convertView.setTag(T9Keyboard.TAG_KEY, "01");
            return;
        }
        TextView t1 = (TextView) convertView.findViewById(R.id.epg_nomal_key_num);
        TextView t2 = (TextView) convertView.findViewById(R.id.epg_nomal_key_letter);
        String num = T9_NUMBER[position];
        String letter = T9_LETTER[position];
        t1.setText(num);
        t2.setText(letter);
        convertView.setTag(T9Keyboard.TAG_KEY, num + letter);
    }

    public int getViewTypeCount() {
        return 2;
    }

    public int getItemViewType(int position) {
        return position == 0 ? 0 : 1;
    }

    public void changeTextColor(View view, int pos, boolean hasFocus) {
        int numKeyLineColor;
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        int keyNumFirstColor = 0;
        int keyNumSecondColor = 0;
        int normalKeyNumColor = 0;
        int normalKeyLetterColor = 0;
        if (pos == 0) {
            numKeyLineColor = getNumKeyLineColor(hasFocus);
            keyNumFirstColor = getKeyNumColor(hasFocus);
            keyNumSecondColor = getKeyNumColor(hasFocus);
        } else {
            numKeyLineColor = hasFocus ? R.color.gala_write : R.color.keyboard_letter;
            normalKeyNumColor = getKeyNumColor(hasFocus);
            normalKeyLetterColor = getNormalKeyLetterColor(hasFocus);
        }
        viewHolder.numKeyLine.setBackgroundColor(getColor(numKeyLineColor));
        if (normalKeyNumColor == 0) {
            viewHolder.keyNumFirst.setTextColor(getColor(keyNumFirstColor));
            viewHolder.keyNumSecond.setTextColor(getColor(keyNumSecondColor));
            return;
        }
        viewHolder.normalKeyNum.setTextColor(getColor(normalKeyNumColor));
        viewHolder.normalKeyLetter.setTextColor(getColor(normalKeyLetterColor));
    }

    private int getKeyNumFirstColor(boolean isFocus) {
        return getKeyNumColor(isFocus);
    }

    private int getKeyNumSecondColor(boolean isFocus) {
        return getKeyNumColor(isFocus);
    }

    private int getNormalKeyNumColor(boolean isFocus) {
        return getKeyNumColor(isFocus);
    }

    private int getNormalKeyLetterColor(boolean isFocus) {
        return isFocus ? R.color.search_t9_line_focus : R.color.keyboard_letter;
    }

    private int getKeyNumColor(boolean isFocus) {
        return isFocus ? R.color.search_t9_line_focus : R.color.keyboard_num;
    }

    private int getNumKeyLineColor(boolean isFocus) {
        return isFocus ? R.color.search_t9_line_focus : R.color.search_t9_line;
    }

    private int getColor(int id) {
        return ResourceUtil.getColor(id);
    }
}
