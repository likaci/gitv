package com.gala.video.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import com.gala.video.widget.util.LogUtils;
import java.util.List;

@Deprecated
public class QRadioGroup extends GridView {
    protected static final String TAG = "QRadioGroup";
    private OnCheckedChangeListener listener;
    private GridAdapter mAdapter;
    private int mColorContentBgDefault = -13750738;
    private int mColorContentBgDisabled = -1300793481;
    private int mColorContentBgSelected = -10120182;
    private int mColorViewBg = -11184811;
    private int mIndex;
    private int mSpacing = 3;
    private List<String> mStringList;
    private int mTextColor = -1;
    private int mTextColorDisabled = -13224394;
    private int mTextSize = 30;

    class GridAdapter extends BaseAdapter {
        List<String> dataList;
        LayoutInflater mInflater;

        public GridAdapter(Context context, List<String> data) {
            this.mInflater = LayoutInflater.from(context);
            this.dataList = data;
        }

        public int getCount() {
            return this.dataList != null ? this.dataList.size() : 0;
        }

        public Object getItem(int position) {
            return Integer.valueOf(position);
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView text;
            if (convertView == null) {
                text = new TextView(QRadioGroup.this.getContext());
                text.setTextColor(QRadioGroup.this.mTextColor);
                text.setTextSize(0, (float) QRadioGroup.this.mTextSize);
                text.setGravity(17);
            } else {
                text = (TextView) convertView;
            }
            text.setLayoutParams(new LayoutParams(-1, parent.getHeight() - (QRadioGroup.this.mSpacing * 2)));
            text.setText((CharSequence) this.dataList.get(position));
            if (!QRadioGroup.this.isEnabled()) {
                text.setBackgroundColor(QRadioGroup.this.mColorContentBgDisabled);
            } else if (position == QRadioGroup.this.mIndex) {
                text.setBackgroundColor(QRadioGroup.this.mColorContentBgSelected);
            } else {
                text.setBackgroundColor(QRadioGroup.this.mColorContentBgDefault);
            }
            if (QRadioGroup.this.isEnabled()) {
                text.setTextColor(QRadioGroup.this.mTextColor);
            } else {
                text.setTextColor(QRadioGroup.this.mTextColorDisabled);
            }
            return text;
        }
    }

    public interface OnCheckedChangeListener {
        void onCheckedChanged(int i);

        void onItemChecked(int i);
    }

    public QRadioGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public QRadioGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public QRadioGroup(Context context) {
        super(context);
        init();
    }

    private void init() {
        setBackgroundColor(this.mColorViewBg);
        setHorizontalSpacing(this.mSpacing);
        setVerticalSpacing(this.mSpacing);
        setFocusable(true);
    }

    public int getCheckedIndex() {
        return this.mIndex;
    }

    public void setDataSource(List<String> strList, int index, OnCheckedChangeListener onCheckedChangeListener) {
        this.listener = onCheckedChangeListener;
        this.mStringList = strList;
        if (index < 0) {
            this.mIndex = 0;
        } else if (index >= this.mStringList.size()) {
            this.mIndex = this.mStringList.size() - 1;
        } else {
            this.mIndex = index;
        }
        LogUtils.d(TAG, "setDataSource: data size=" + (strList != null ? Integer.valueOf(strList.size()) : "NULL") + ", initial index=" + this.mIndex);
        setNumColumns(this.mStringList.size());
        this.mAdapter = new GridAdapter(getContext(), this.mStringList);
        setAdapter(this.mAdapter);
        setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View arg1, int position, long arg3) {
                if (QRadioGroup.this.mIndex != position) {
                    QRadioGroup.this.mIndex = position;
                    QRadioGroup.this.mAdapter.notifyDataSetChanged();
                    if (QRadioGroup.this.listener != null) {
                        QRadioGroup.this.listener.onCheckedChanged(position);
                    }
                }
                if (QRadioGroup.this.listener != null) {
                    QRadioGroup.this.listener.onItemChecked(position);
                }
            }
        });
    }

    public void setTextColor(int color) {
        this.mTextColor = color;
    }

    public void setTextColorDisabled(int disabledTxtColor) {
        this.mTextColorDisabled = disabledTxtColor;
    }

    public void setTextSize(int size) {
        this.mTextSize = size;
    }

    public void setSpace(int space) {
        this.mSpacing = space;
    }

    public int getSpace() {
        return this.mSpacing;
    }

    public void setDefaultContentBgColor(int color_default) {
        this.mColorContentBgDefault = color_default;
    }

    public void setSelectedContentBgColor(int color_selected) {
        this.mColorContentBgSelected = color_selected;
    }

    public void setViewBgColor(int color_background) {
        this.mColorViewBg = color_background;
    }

    public void setDisabledContentBgColor(int disabledColor) {
        this.mColorContentBgDisabled = disabledColor;
    }

    public void setEnabled(boolean enabled) {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            getChildAt(i).setEnabled(enabled);
        }
        clearFocus();
        setFocusable(enabled);
        super.setEnabled(enabled);
        if (this.mAdapter != null) {
            this.mAdapter.notifyDataSetChanged();
        }
    }

    public void setSelectedChildIndex(int selectedIndex) {
        if (this.mAdapter == null) {
            throw new IllegalStateException("Please invoke setDataSource first!!");
        }
        int size = this.mStringList.size();
        LogUtils.d(TAG, "setSelectedChildIndex: total data size=" + size);
        this.mIndex = Math.max(0, Math.min(size, selectedIndex));
        this.mAdapter.notifyDataSetChanged();
        LogUtils.d(TAG, "setSelectedChildIndex: wanted=" + selectedIndex + ", result=" + this.mIndex);
    }
}
