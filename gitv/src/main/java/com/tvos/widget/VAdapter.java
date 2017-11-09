package com.tvos.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout.LayoutParams;

public abstract class VAdapter {
    private final DataSetObservable mDataSetObservable = new DataSetObservable();

    public abstract int getCount();

    public abstract View getView(int i, ViewGroup viewGroup);

    public abstract void onScrollItemIn(int i, View view);

    public abstract void onScrollItemOut(int i, View view);

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mDataSetObservable.unregisterObserver(observer);
    }

    public int getColumnSpan(int position) {
        return 1;
    }

    public int getRowSpan(int position) {
        return 1;
    }

    public LayoutParams getLayoutParams(int position) {
        return null;
    }

    public boolean isEmpty() {
        return getCount() == 0;
    }

    public void notifyDataSetChanged() {
        this.mDataSetObservable.notifyChanged();
    }

    public void notifyDataSetInvalidated() {
        this.mDataSetObservable.notifyInvalidated();
    }
}
