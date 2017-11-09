package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.card;

import android.view.View;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action.IActionListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.IPingbackListener;
import java.util.List;

public interface IVerticalCardView extends ICardView {

    public interface IFocusableCallback {
        boolean isFocusable(int i);
    }

    void addHeaderView(View view, int i, int i2);

    List<CardModel> getData();

    boolean hasHeader();

    void hideLoading();

    void initPingbackState();

    boolean isViewVisible(int i, boolean z, int i2, int i3);

    void notifyDataSetAdd(CardModel cardModel, int i);

    void notifyDataSetAdd(List<CardModel> list);

    void notifyDataSetChanged();

    void notifyDataSetChanged(List<CardModel> list);

    void notifyDataSetRemoved(List<CardModel> list, int i);

    void notifyDataSetUpdate(List<CardModel> list);

    void notifyDataSetUpdate(List<CardModel> list, int i);

    void notifyItemRemoved(int i);

    void onPauseForPingback();

    void reLoadTask();

    void setActionListener(IActionListener iActionListener);

    void setCardTitle(String str, int i);

    void setChildFocusPosition(int i, int i2);

    void setData(List<CardModel> list);

    void setFocusableCallback(IFocusableCallback iFocusableCallback);

    void setPingbackListener(IPingbackListener iPingbackListener);

    void setTopShade(int i);

    void setVerticalScrollCloselyTopBarHeight(int i);

    void showLoading();

    void showLoading(int i);
}
