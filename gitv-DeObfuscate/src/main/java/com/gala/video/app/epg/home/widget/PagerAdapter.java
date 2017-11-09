package com.gala.video.app.epg.home.widget;

import android.database.DataSetObservable;
import android.database.DataSetObserver;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;

public abstract class PagerAdapter {
    public static final int POSITION_NONE = -2;
    public static final int POSITION_UNCHANGED = -1;
    private final DataSetObservable mObservable = new DataSetObservable();
    private DataSetObserver mViewPagerObserver;

    public abstract int getCount();

    public abstract boolean isViewFromObject(View view, Object obj);

    public void startUpdate(ViewGroup container) {
    }

    public Object instantiateItem(ViewGroup container, int position) {
        throw new UnsupportedOperationException("Required method instantiateItem was not overridden");
    }

    public void destroyItem(ViewGroup container, int position, Object object) {
        throw new UnsupportedOperationException("Required method destroyItem was not overridden");
    }

    public void setPrimaryItem(ViewGroup container, int position, Object object) {
    }

    public void finishUpdate(ViewGroup container) {
    }

    public Parcelable saveState() {
        return null;
    }

    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    public int getItemPosition(Object object) {
        return -1;
    }

    public void notifyDataSetChanged() {
        synchronized (this) {
            if (this.mViewPagerObserver != null) {
                this.mViewPagerObserver.onChanged();
            }
        }
        this.mObservable.notifyChanged();
    }

    public void registerDataSetObserver(DataSetObserver observer) {
        this.mObservable.registerObserver(observer);
    }

    public void unregisterDataSetObserver(DataSetObserver observer) {
        this.mObservable.unregisterObserver(observer);
    }

    void setViewPagerObserver(DataSetObserver observer) {
        synchronized (this) {
            this.mViewPagerObserver = observer;
        }
    }

    public CharSequence getPageTitle(int position) {
        return null;
    }

    public float getPageWidth(int position) {
        return 1.0f;
    }
}
