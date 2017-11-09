package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action;

import android.content.Context;
import android.view.KeyEvent;
import android.view.ViewParent;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;

public class DummyActionListener implements IActionListener {
    public void onHorizontalScrollBefore(CardModel cardModel) {
    }

    public void onHorizontalScrollStart(CardModel cardModel) {
    }

    public void onHorizontalScroll(CardModel cardModel, int firstVisibleItem, int lastVisibleItem, int totalItemCount) {
    }

    public void onHorizontalScrollStop(CardModel cardModel, RecyclerView hGridView) {
    }

    public void onHorizontalItemClick(Context mContext, RecyclerView hGridView, ViewHolder viewHolder, CardModel cardModel) {
    }

    public void onHorizontalItemFocusChanged(ViewHolder holder, boolean hasFocus, CardModel cardModel) {
    }

    public void onHorizontalAttachedToWindow(CardModel cardModel) {
    }

    public void onHorizontalDetachedFromWindow(CardModel cardModel) {
    }

    public void onVerticalCreate() {
    }

    public void onVerticalResume() {
    }

    public void onVerticalScrollBefore(int position) {
    }

    public void onVerticalScrollStart() {
    }

    public void onVerticalScroll(ViewParent parent, int firstVisibleItem, int lastVisibleItem, int totalItemCount) {
    }

    public void onVerticalScrollStop() {
    }

    public void onCardVisibleToInvisibile(int position) {
    }

    public void onCardShow(int position) {
    }

    public void onVerticalAttachedToWindow() {
    }

    public void onVerticalDetachedFromWindow() {
    }

    public boolean onHorizontalDispatchKeyEvent(KeyEvent event, CardModel cardModel) {
        return false;
    }

    public boolean onVerticalDispatchKeyEvent(KeyEvent event) {
        return false;
    }

    public boolean onVerticalScrollCloselyTop(int pos) {
        return false;
    }
}
