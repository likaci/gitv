package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject;

import android.content.Context;
import android.view.View;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.DataSource;

public interface MultiSubjectImp {
    int getHeight();

    int getWidth();

    void loadImage();

    void onBindViewHolder(DataSource dataSource);

    View onCreateViewHolder(Context context);

    void onDestroy();

    void recycleAndShowDefaultImage();

    void setActualItemHeight(int i);

    void setActualItemWidth(int i);
}
