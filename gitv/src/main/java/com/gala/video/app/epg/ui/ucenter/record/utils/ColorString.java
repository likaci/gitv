package com.gala.video.app.epg.ui.ucenter.record.utils;

import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import java.util.ArrayList;
import java.util.Iterator;

public class ColorString extends SpannableString {
    private ArrayList<ColorStringItem> mData = new ArrayList();
    private String mSource;

    public static class ColorStringItem {
        private int mColor;
        private String mStr;

        public ColorStringItem(String str, int color) {
            this.mStr = str;
            this.mColor = color;
        }

        public String getString() {
            return this.mStr;
        }

        public int getColor() {
            return this.mColor;
        }
    }

    public ColorString(CharSequence source) {
        super(source);
        this.mSource = source.toString();
    }

    public void setColor(ColorStringItem item) {
        this.mData.add(item);
    }

    public void setColor(int color) {
        setSpan(new ForegroundColorSpan(color), 0, this.mSource.length(), 33);
    }

    public ColorString build() {
        int subStart = 0;
        Iterator it = this.mData.iterator();
        while (it.hasNext()) {
            ColorStringItem item = (ColorStringItem) it.next();
            String str = item.getString();
            Integer color = Integer.valueOf(item.getColor());
            if (this.mSource.substring(subStart).contains(str)) {
                int start = this.mSource.indexOf(str, subStart);
                int end = start + str.length();
                setSpan(new ForegroundColorSpan(color.intValue()), start, end, 33);
                subStart = end;
            }
        }
        return this;
    }

    public void setColor(int start, int end, int color) {
        setSpan(new ForegroundColorSpan(color), start, end, 33);
    }
}
