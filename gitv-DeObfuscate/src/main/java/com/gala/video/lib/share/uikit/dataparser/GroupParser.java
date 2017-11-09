package com.gala.video.lib.share.uikit.dataparser;

import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.PageInfoModel;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.uikit.protocol.ServiceManager;
import com.gala.video.lib.share.uikit.resolver.CardResolver;
import java.util.ArrayList;
import java.util.List;

public class GroupParser {
    private CardResolver mCardResolver;
    private ServiceManager mServiceManager;

    public GroupParser(ServiceManager serviceManager) {
        this.mServiceManager = serviceManager;
        this.mCardResolver = (CardResolver) serviceManager.getService(CardResolver.class);
    }

    public List<Card> parserGroup(PageInfoModel pageInfoModel) {
        List<Card> cardList = new ArrayList();
        if (!(pageInfoModel == null || pageInfoModel.getCardInfoModels() == null)) {
            for (CardInfoModel cardInfoModel : pageInfoModel.getCardInfoModels()) {
                Card card = (Card) this.mCardResolver.create(cardInfoModel.getCardType());
                if (card != null) {
                    card.setServiceManager(this.mServiceManager);
                    card.setModel(cardInfoModel);
                    cardList.add(card);
                }
            }
        }
        return cardList;
    }

    public List<Card> parserGroup(Page page, List<CardInfoModel> cards) {
        List<Card> cardList = new ArrayList();
        if (cards != null) {
            for (CardInfoModel cardInfoModel : cards) {
                Card card = (Card) this.mCardResolver.create(cardInfoModel.getCardType());
                if (card != null) {
                    card.assignParent(page);
                    card.setServiceManager(this.mServiceManager);
                    card.setModel(cardInfoModel);
                    cardList.add(card);
                }
            }
        }
        return cardList;
    }

    public Card parserCard(Page page, CardInfoModel cardInfoModel) {
        Card card = (Card) this.mCardResolver.create(cardInfoModel.getCardType());
        if (card != null) {
            card.assignParent(page);
            card.setServiceManager(this.mServiceManager);
            card.setModel(cardInfoModel);
        }
        return card;
    }
}
