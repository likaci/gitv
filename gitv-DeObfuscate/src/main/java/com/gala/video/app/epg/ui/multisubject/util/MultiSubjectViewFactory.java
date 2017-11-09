package com.gala.video.app.epg.ui.multisubject.util;

import android.content.Context;
import android.view.View;
import com.gala.video.app.epg.home.component.Widget;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig.ItemSize;
import com.gala.video.lib.share.ifimpl.multisubject.MultiSubjectHGridView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectViewFactory.Wrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.MultiSubjectImp;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.adapter.IMultiSubjectVAdapter;

public class MultiSubjectViewFactory extends Wrapper {
    private static final String TAG = "EPG/multisubject/MultiSubjectViewFactory";

    public int getItemImageHeight(int cardWidgetType, int row) {
        switch (cardWidgetType) {
            case 1:
                return ItemSize.ITEM_410;
            case 2:
            case 3:
                return ItemSize.ITEM_230;
            case 4:
                break;
            case 5:
                if (row == 0) {
                    return ItemSize.ITEM_360;
                }
                break;
            case 6:
                return ItemSize.ITEM_410;
            case 7:
                return 302;
            case 8:
                return ItemSize.ITEM_226;
            case 9:
            case 10:
            case 11:
            case 29:
                return ItemSize.ITEM_360;
            case 12:
                return 227;
            case 13:
            case 14:
                return ItemSize.ITEM_226;
            case 15:
            case 26:
                return ItemSize.ITEM_360;
            case 16:
                return 402;
            case 17:
                return ItemSize.ITEM_410;
            case 18:
            case 23:
            case 28:
            case 30:
                return 260;
            case 19:
                return 250;
            case 31:
            case 32:
                return 150;
            default:
                return 0;
        }
        if (row == 0) {
            return ItemSize.ITEM_410;
        }
        return ItemSize.ITEM_226;
    }

    public int getItemExtraHeight(int cardWidgetType, int itemWidgetType) {
        int h = 0;
        switch (cardWidgetType) {
            case 1:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 28:
            case 30:
            case 254:
                LogUtils.m1571e(TAG, "getItemExtraHeight，widgettype error is not support, cardType:" + cardWidgetType);
                break;
            case 2:
                h = 60;
                break;
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 29:
                h = 64;
                if (itemWidgetType == WidgetType.ITEM_TITLE_OUT) {
                    h = 64 + getExtraHeightForTitleOutItem();
                    break;
                }
                break;
            case 26:
                h = 106;
                break;
        }
        return (getItemNinePatchTopBottom() * 2) + h;
    }

    private int getExtraHeightForTitleOutItem() {
        return 18;
    }

    public int getVPaddingBottom(int cardType) {
        switch (cardType) {
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 29:
            case 31:
            case 32:
                return 36;
            case 23:
            case 28:
            case 30:
                return 6;
            default:
                return 0;
        }
    }

    public int getItemViewHorizontalSpace(int cardWidgetType) {
        switch (cardWidgetType) {
            case 2:
            case 3:
                return 26;
            case 12:
                return 5;
            default:
                return -18;
        }
    }

    public int getItemViewHorizontalSpaceNew(int cardWidgetType) {
        switch (cardWidgetType) {
            case 2:
            case 3:
                return 90;
            default:
                return -18;
        }
    }

    public int getItemViewPaddingLeft() {
        return 25;
    }

    public int getItemNinePatchLeftRight() {
        return 21;
    }

    private int getItemNinePatchTopBottom() {
        return 21;
    }

    public int getItemViewPaddingTop(int cardWidgetType) {
        switch (cardWidgetType) {
            case 1:
            case 4:
            case 5:
            case 6:
            case 7:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 24:
            case 254:
                LogUtils.m1571e(TAG, "getItemViewPaddingTop，widgettype error is not support, type:" + cardWidgetType);
                return 0;
            case 2:
            case 3:
                return 30;
            case 8:
                return 20;
            case 9:
            case 11:
                return 28;
            case 10:
            case 13:
            case 15:
            case 26:
                return 122;
            case 12:
                return 37;
            case 14:
            case 29:
                return 28;
            case 23:
            case 28:
            case 30:
                return 30;
            case 31:
            case 32:
                return -3;
            default:
                return 0;
        }
    }

    private int getItemViewPaddingBottom(int itemWidgetType) {
        int viewPaddingBottom = getCardVisiblePaddingBottom() - getItemNinePatchTopBottom();
        if (itemWidgetType == WidgetType.ITEM_TITLE_OUT) {
            return viewPaddingBottom - getExtraHeightForTitleOutItem();
        }
        return viewPaddingBottom;
    }

    public int getCardVisiblePaddingBottom() {
        return 38;
    }

    public int getCardHeight(CardModel cardModel) {
        if (cardModel == null || ListUtils.isEmpty(cardModel.getItemModelList()) || cardModel.getItemModelList().get(0) == null) {
            return 0;
        }
        int widgetType = cardModel.getWidgetType();
        int itemWidgetType = ((ItemModel) cardModel.getItemModelList().get(0)).getWidgetType();
        return (getItemViewPaddingTop(widgetType) + (getItemImageHeight(widgetType, 1) + getItemExtraHeight(widgetType, itemWidgetType))) + getItemViewPaddingBottom(itemWidgetType);
    }

    public MultiSubjectImp createItem(int widgettype) {
        Widget widget = Widget.createWidget(widgettype);
        if (widget == null) {
            LogUtils.m1571e(TAG, "createItem error is type:" + widgettype);
        }
        if (widget instanceof MultiSubjectImp) {
            return (MultiSubjectImp) widget;
        }
        LogUtils.m1571e(TAG, "createItem error, and widget is not MultiSubjectImp. type:" + widgettype);
        return null;
    }

    public View createHGridView(int widgettype, Context context, IMultiSubjectVAdapter adapter) {
        switch (widgettype) {
            case 1:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 24:
            case 254:
                return new MultiSubjectHGridView(context, adapter);
            case 2:
            case 3:
            case 31:
            case 32:
                return new MultiSubjectHGridView(context, adapter);
            default:
                return new MultiSubjectHGridView(context, adapter);
        }
    }
}
