package com.gala.video.app.epg.home.component.card;

import android.content.Context;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.epg.home.component.item.NCarouselItem;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.pingback.ClickPingbackUtils;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingbackUtils;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.card.Card.CardActionPolicy;
import com.gala.video.lib.share.uikit.card.GridCard;
import com.gala.video.lib.share.uikit.item.Item;
import java.util.List;

public class NCarouselCard extends GridCard {
    private static final String TAG = "/home/NCarouselCard";
    private ActionPolicy mPolicy = new CarouselActionPolicy(this);

    private class CarouselActionPolicy extends CardActionPolicy {
        public CarouselActionPolicy(Card card) {
            super(card);
        }

        public void onScrollStart(ViewGroup parent) {
            super.onScrollStart(parent);
            LogUtils.m1568d(NCarouselCard.TAG, "sendStopMsg on card scroll");
            NCarouselItem carouseItem = isCarouseItem();
            if (carouseItem != null) {
                carouseItem.sendStopMsg();
            }
        }

        public void onScrollStop(ViewGroup parent) {
            super.onScrollStop(parent);
            NCarouselItem carouseItem = isCarouseItem();
            if (carouseItem != null) {
                carouseItem.sendStartMsg();
            }
        }

        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            int pos = holder.getLayoutPosition() - NCarouselCard.this.getBlockLayout().getFirstPosition();
            List items = NCarouselCard.this.getItems();
            if (ListUtils.isLegal(items, pos) && !ListUtils.isEmpty(items)) {
                Item currentItem = (Item) items.get(pos);
                if (currentItem == null || !(currentItem instanceof NCarouselItem)) {
                    LogUtils.m1574i(NCarouselCard.TAG, "OtherItemClick");
                    super.onItemClick(parent, holder);
                    return;
                }
                LogUtils.m1574i(NCarouselCard.TAG, "CarouseItemClick");
                carousePingback(parent.getContext(), pos, currentItem);
                ((NCarouselItem) currentItem).carouseItemClick();
            }
        }

        private void carousePingback(Context context, int position, Item item) {
            String cardLine = "" + ClickPingbackUtils.getLine(NCarouselCard.this.getParent(), item.getParent(), item);
            String allLine = "" + NCarouselCard.this.getAllLine();
            ClickPingbackUtils.itemClickForPingbackPost(ClickPingbackUtils.composeCommonItemPingMap(context, position + 1, NCarouselCard.this.mCardInfoModel.mCardId, cardLine, allLine, item));
            if (PingbackUtils.getPingbackPage(context) == PingbackPage.HomePage) {
                String itemIndex = (position + 1) + "";
                PingBackCollectionFieldUtils.setCardIndex(cardLine + "");
                PingBackCollectionFieldUtils.setItemIndex(itemIndex);
                PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_c_" + cardLine + "_item_" + itemIndex);
            }
        }

        private NCarouselItem isCarouseItem() {
            if (!ListUtils.isEmpty(NCarouselCard.this.getItems())) {
                Item firstItem = NCarouselCard.this.getItem(0);
                if (firstItem != null && (firstItem instanceof NCarouselItem)) {
                    return (NCarouselItem) firstItem;
                }
            }
            return null;
        }
    }

    public void start() {
        super.start();
        LogUtils.m1568d(TAG, "start");
    }

    public void stop() {
        super.stop();
        LogUtils.m1568d(TAG, "stop");
        stopCarouselWindow();
    }

    public ActionPolicy getActionPolicy() {
        return this.mPolicy;
    }

    private void stopCarouselWindow() {
        LogUtils.m1568d(TAG, "stopCarouselWindow");
        if (getItems() != null && getItemCount() > 0) {
            Item item = getItem(0);
            if (item instanceof NCarouselItem) {
                ((NCarouselItem) item).sendStopMsg();
            }
        }
    }

    private void startCarouselPlay() {
        if (getItems() != null && getItemCount() > 0) {
            Item item = getItem(0);
            if (item instanceof NCarouselItem) {
                ((NCarouselItem) item).sendStartMsg();
            }
        }
    }
}
