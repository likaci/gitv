package com.gala.video.app.player.ui.widget.tabhost;

import android.view.View;
import com.gala.video.app.player.ui.overlay.contents.IContent;
import java.util.List;

public interface ISimpleTabHostAdapter {

    public interface OnDataChangedListener {
        void onDataChanged();
    }

    int getCount();

    String getTitle(int i);

    View getView(int i);

    void notifyDataChanged();

    void setOnDataChangedListener(OnDataChangedListener onDataChangedListener);

    void updateData(List<IContent<?, ?>> list);
}
