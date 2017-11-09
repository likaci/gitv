package com.gala.video.app.epg.ui.solotab;

import android.view.ViewGroup;
import com.gala.video.albumlist.utils.LOG;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.share.uikit.UIKitEngine;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.loader.UikitEvent;
import com.gala.video.lib.share.uikit.page.Page;
import java.util.List;
import org.greenrobot.eventbus.EventBus;

public class SoloTabActionPolicy extends ActionPolicy {
    private static final int CACHE_CARD_SIZE = 4;
    public static String TAG = "SoloTabActionPolicy";
    private UIKitEngine mEngine;
    private boolean mRequestDefaultFocus = true;

    public SoloTabActionPolicy(UIKitEngine engine) {
        this.mEngine = engine;
    }

    public void onFirstLayout(ViewGroup parent) {
        this.mEngine.start();
        requestDefaultFocus(parent);
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        Page page = this.mEngine.getPage();
        Card card = page.getItem(cast(parent).getFocusPosition()).getParent();
        if (card != null && page.shouldLoadMore()) {
            synchronized (page) {
                List<Card> cardList = page.getCards();
                Card lastCard = (Card) cardList.get(cardList.size() - 1);
                if (lastCard != null && cardList.size() - cardList.indexOf(card) <= 4) {
                    UikitEvent uiEvent = new UikitEvent();
                    uiEvent.eventType = 17;
                    uiEvent.uikitEngineId = this.mEngine.getId();
                    uiEvent.cardInfoModel = lastCard.getModel();
                    EventBus.getDefault().postSticky(uiEvent);
                    LOG.d("UIKIT_ADD_CARDS mEngine.getId() = " + this.mEngine.getId());
                }
            }
        }
    }

    public void onFocusLost(ViewGroup parent, ViewHolder holder) {
        this.mRequestDefaultFocus = false;
    }

    private void requestDefaultFocus(ViewGroup parent) {
        if (this.mRequestDefaultFocus) {
            parent.requestFocus();
        }
    }
}
