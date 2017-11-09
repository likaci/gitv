package com.gala.video.app.player.ui.widget.tabhost;

import android.view.View;
import com.gala.video.app.player.ui.overlay.contents.IContent;
import com.gala.video.app.player.ui.widget.tabhost.ISimpleTabHostAdapter.OnDataChangedListener;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class SimpleTabHostAdapter implements ISimpleTabHostAdapter {
    private static final String TAG = "SimpleTabHostAdapter";
    private OnDataChangedListener mDataChangeListener;
    private List<IContent<?, ?>> mDatas = new CopyOnWriteArrayList();

    public SimpleTabHostAdapter(List<IContent<?, ?>> list) {
        if (!ListUtils.isEmpty(this.mDatas)) {
            this.mDatas.clear();
        }
        this.mDatas.addAll(list);
    }

    public void updateData(List<IContent<?, ?>> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> updateData");
        }
        if (list != null) {
            this.mDatas.clear();
            this.mDatas.addAll(list);
        }
    }

    public void notifyDataChanged() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> notifyDataChanged");
        }
        if (this.mDataChangeListener != null) {
            this.mDataChangeListener.onDataChanged();
        }
    }

    public View getView(int position) {
        if (position >= 0 && position < this.mDatas.size()) {
            return ((IContent) this.mDatas.get(position)).getView();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "Error in getView, invalid position=" + position);
        }
        return null;
    }

    public String getTitle(int position) {
        if (position >= 0 && position < this.mDatas.size()) {
            return ((IContent) this.mDatas.get(position)).getTitle();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "Error in getTitle, invalid position=" + position);
        }
        return null;
    }

    public int getCount() {
        return this.mDatas.size();
    }

    public void setOnDataChangedListener(OnDataChangedListener listener) {
        this.mDataChangeListener = listener;
    }
}
