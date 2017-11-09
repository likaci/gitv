package com.gala.video.lib.share.uikit;

import android.content.Context;
import android.view.View;
import com.gala.sdk.player.constants.PlayerPlatformConstants;
import com.gala.video.lib.share.common.configs.ApiConstants;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.card.CarouseChannelCard;
import com.gala.video.lib.share.uikit.card.CoverFlowCard;
import com.gala.video.lib.share.uikit.card.GridCard;
import com.gala.video.lib.share.uikit.card.HScrollCard;
import com.gala.video.lib.share.uikit.card.LoadingCard;
import com.gala.video.lib.share.uikit.card.RecordDailyCard;
import com.gala.video.lib.share.uikit.card.SubscribeCard;
import com.gala.video.lib.share.uikit.card.TimeLineCard;
import com.gala.video.lib.share.uikit.card.VipCard;
import com.gala.video.lib.share.uikit.card.settingapp.SettingAppCard;
import com.gala.video.lib.share.uikit.core.MVHelper;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.item.AppItem;
import com.gala.video.lib.share.uikit.item.CoverFlowItem;
import com.gala.video.lib.share.uikit.item.HeaderItem;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.item.LoadingItem;
import com.gala.video.lib.share.uikit.item.RecordItem;
import com.gala.video.lib.share.uikit.item.SettingItem;
import com.gala.video.lib.share.uikit.item.StandardItem;
import com.gala.video.lib.share.uikit.item.SubscribeItem;
import com.gala.video.lib.share.uikit.item.VipItem;
import com.gala.video.lib.share.uikit.resolver.BaseCardBinderResolver;
import com.gala.video.lib.share.uikit.resolver.BaseItemBinderResolver;
import com.gala.video.lib.share.uikit.resolver.CardResolver;
import com.gala.video.lib.share.uikit.resolver.ItemResolver;
import com.gala.video.lib.share.uikit.view.AllEntryItemView;
import com.gala.video.lib.share.uikit.view.AppItemView;
import com.gala.video.lib.share.uikit.view.CarouselChannelItemView;
import com.gala.video.lib.share.uikit.view.ChannellistItemView;
import com.gala.video.lib.share.uikit.view.FlowCardView;
import com.gala.video.lib.share.uikit.view.HeaderView;
import com.gala.video.lib.share.uikit.view.HistoryAllEntryItemView;
import com.gala.video.lib.share.uikit.view.LoadingView;
import com.gala.video.lib.share.uikit.view.SettingItemView;
import com.gala.video.lib.share.uikit.view.StandardItemView;
import com.gala.video.lib.share.uikit.view.SubscribeItemView;
import com.gala.video.lib.share.uikit.view.VipItemView;
import com.gala.video.lib.share.uikit.view.widget.record.RecordItemView;

public class UIKitBuilder {
    private Context mContext;
    private DefaultResolverRegistry mDefaultResolverRegistry;

    static UIKitBuilder newInstance(Context context) {
        DefaultResolverRegistry registry = new DefaultResolverRegistry();
        installDefaultRegistry(registry);
        return new UIKitBuilder(context, registry);
    }

    private static void installDefaultRegistry(DefaultResolverRegistry registry) {
        registry.registerItem(201, StandardItem.class, StandardItemView.class);
        registry.registerItem(210, AppItem.class, AppItemView.class);
        registry.registerItem(213, Item.class, ChannellistItemView.class);
        registry.registerItem(214, Item.class, CarouselChannelItemView.class);
        registry.registerItem(217, RecordItem.class, RecordItemView.class);
        registry.registerItem(215, SubscribeItem.class, SubscribeItemView.class);
        registry.registerItem(211, SettingItem.class, SettingItemView.class);
        registry.registerItem(212, StandardItem.class, AllEntryItemView.class);
        registry.registerItem((int) ApiConstants.IMAGE_CORNER_SIZE_219, CoverFlowItem.class, FlowCardView.class);
        registry.registerItem(999, LoadingItem.class, LoadingView.class);
        registry.registerItem((int) UIKitConfig.ITEM_TYPE_HEADER, HeaderItem.class, HeaderView.class);
        registry.registerItem(221, VipItem.class, VipItemView.class);
        registry.registerItem((int) PlayerPlatformConstants.L1_PRODUCT_ANDROID_PHONE_APP, Item.class, HistoryAllEntryItemView.class);
        registry.registerCard(101, GridCard.class);
        registry.registerCard(106, HScrollCard.class);
        registry.registerCard(109, RecordDailyCard.class);
        registry.registerCard(103, TimeLineCard.class);
        registry.registerCard(104, SubscribeCard.class);
        registry.registerCard(107, SettingAppCard.class);
        registry.registerCard(999, LoadingCard.class);
        registry.registerCard(105, CarouseChannelCard.class);
        registry.registerCard(102, CoverFlowCard.class);
        registry.registerCard(111, VipCard.class);
    }

    private UIKitBuilder(Context context, DefaultResolverRegistry registry) {
        this.mContext = context;
        this.mDefaultResolverRegistry = registry;
    }

    public <V extends View> void registerSpecialItem(int type, Class<? extends Item> itemClz, Class<V> viewClz) {
        this.mDefaultResolverRegistry.registerItem(type, (Class) itemClz, (Class) viewClz);
    }

    public void registerSpecialCard(int type, Class<? extends Card> cardClz) {
        this.mDefaultResolverRegistry.registerCard(type, cardClz);
    }

    public UIKitEngine build() {
        UIKitEngine engine = new UIKitEngine();
        engine.register(ItemResolver.class, this.mDefaultResolverRegistry.mDefaultItemResolver);
        engine.register(CardResolver.class, this.mDefaultResolverRegistry.mDefaultCardResolver);
        engine.register(MVHelper.class, this.mDefaultResolverRegistry.mMVHelper);
        engine.register(BaseCardBinderResolver.class, this.mDefaultResolverRegistry.mDefaultCardBinderResolver);
        engine.register(BaseItemBinderResolver.class, this.mDefaultResolverRegistry.mDefaultItemBinderResolver);
        engine.register(Context.class, this.mContext);
        return engine;
    }
}
