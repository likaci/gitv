package com.gala.video.lib.share.uikit.resolver;

import com.gala.video.lib.share.uikit.card.Card;

public class CardResolver extends ClassResolver<Card> {
    public boolean hasType(int type) {
        return this.mSparseArray.get(type) != null;
    }
}
