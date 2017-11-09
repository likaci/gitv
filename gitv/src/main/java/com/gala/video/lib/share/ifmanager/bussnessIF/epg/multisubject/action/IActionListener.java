package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action;

import android.content.Context;
import android.view.KeyEvent;
import android.view.ViewParent;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;

public interface IActionListener {
    void onCardShow(int i);

    void onCardVisibleToInvisibile(int i);

    void onHorizontalAttachedToWindow(CardModel cardModel);

    void onHorizontalDetachedFromWindow(CardModel cardModel);

    boolean onHorizontalDispatchKeyEvent(KeyEvent keyEvent, CardModel cardModel);

    void onHorizontalItemClick(Context context, RecyclerView recyclerView, ViewHolder viewHolder, CardModel cardModel);

    void onHorizontalItemFocusChanged(ViewHolder viewHolder, boolean z, CardModel cardModel);

    void onHorizontalScroll(CardModel cardModel, int i, int i2, int i3);

    void onHorizontalScrollBefore(CardModel cardModel);

    void onHorizontalScrollStart(CardModel cardModel);

    void onHorizontalScrollStop(CardModel cardModel, RecyclerView recyclerView);

    void onVerticalAttachedToWindow();

    void onVerticalCreate();

    void onVerticalDetachedFromWindow();

    boolean onVerticalDispatchKeyEvent(KeyEvent keyEvent);

    void onVerticalResume();

    void onVerticalScroll(ViewParent viewParent, int i, int i2, int i3);

    void onVerticalScrollBefore(int i);

    boolean onVerticalScrollCloselyTop(int i);

    void onVerticalScrollStart();

    void onVerticalScrollStop();
}
