package com.gala.video.lib.share.uikit;

import android.util.SparseArray;
import android.view.View;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.core.BaseViewBinder;
import com.gala.video.lib.share.uikit.core.MVHelper;
import com.gala.video.lib.share.uikit.core.ViewHolderCreator;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.resolver.BaseCardBinderResolver;
import com.gala.video.lib.share.uikit.resolver.BaseItemBinderResolver;
import com.gala.video.lib.share.uikit.resolver.CardResolver;
import com.gala.video.lib.share.uikit.resolver.ItemResolver;

public class DefaultResolverRegistry {
    final BaseCardBinderResolver mDefaultCardBinderResolver = new BaseCardBinderResolver();
    final CardResolver mDefaultCardResolver = new CardResolver();
    final BaseItemBinderResolver mDefaultItemBinderResolver = new BaseItemBinderResolver();
    final ItemResolver mDefaultItemResolver = new ItemResolver();
    MVHelper mMVHelper = new MVHelper();
    private SparseArray<ViewHolderCreator> mViewHolderMap = new SparseArray(64);

    public <V extends View> void registerItem(int type, Class<? extends Item> itemClz, ViewHolderCreator viewHolderCreator) {
        this.mViewHolderMap.put(type, viewHolderCreator);
        registerItem(type, (Class) itemClz, viewHolderCreator.viewClz);
    }

    public <V extends View> void registerItem(int type, Class<V> viewClz) {
        registerItem(type, Item.class, (Class) viewClz);
    }

    public <V extends View> void registerItem(int type, Class<? extends Item> itemClz, Class<V> viewClz) {
        this.mDefaultItemResolver.register(type, itemClz);
        if (this.mViewHolderMap.get(type) == null) {
            this.mDefaultItemBinderResolver.register(type, new BaseViewBinder((Class) viewClz, this.mMVHelper));
        } else {
            this.mDefaultItemBinderResolver.register(type, new BaseViewBinder((ViewHolderCreator) this.mViewHolderMap.get(type), this.mMVHelper));
        }
        this.mMVHelper.register(viewClz);
    }

    public <V extends View> void registerCard(int type, Class<? extends Card> cardClz, Class<V> viewClz) {
        this.mDefaultCardResolver.register(type, cardClz);
        this.mDefaultCardBinderResolver.register(type, new BaseViewBinder((Class) viewClz, this.mMVHelper));
        this.mMVHelper.register(viewClz);
    }

    public <V extends View> void registerCard(int type, Class<? extends Card> cardClz) {
        this.mDefaultCardResolver.register(type, cardClz);
    }
}
