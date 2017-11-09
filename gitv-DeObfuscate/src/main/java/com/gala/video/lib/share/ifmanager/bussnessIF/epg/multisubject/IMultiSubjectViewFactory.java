package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject;

import android.content.Context;
import android.view.View;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.adapter.IMultiSubjectVAdapter;

public interface IMultiSubjectViewFactory extends IInterfaceWrapper {

    public static abstract class Wrapper implements IMultiSubjectViewFactory {
        public Object getInterface() {
            return this;
        }

        public static IMultiSubjectViewFactory asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IMultiSubjectViewFactory)) {
                return null;
            }
            return (IMultiSubjectViewFactory) wrapper;
        }
    }

    View createHGridView(int i, Context context, IMultiSubjectVAdapter iMultiSubjectVAdapter);

    MultiSubjectImp createItem(int i);

    int getCardHeight(CardModel cardModel);

    int getCardVisiblePaddingBottom();

    int getItemExtraHeight(int i, int i2);

    int getItemImageHeight(int i, int i2);

    int getItemNinePatchLeftRight();

    int getItemViewHorizontalSpace(int i);

    int getItemViewHorizontalSpaceNew(int i);

    int getItemViewPaddingLeft();

    int getItemViewPaddingTop(int i);

    int getVPaddingBottom(int i);
}
