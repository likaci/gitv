package com.gala.video.lib.share.uikit.card;

import android.view.ViewGroup;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.utils.LOG;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.C1632R;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.pingback.ClickPingbackUtils;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingbackUtils;
import com.gala.video.lib.share.uikit.ComponentGroup;
import com.gala.video.lib.share.uikit.action.model.BaseActionModel;
import com.gala.video.lib.share.uikit.action.model.PlstGroupActionModel;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.contract.CardContract.Presenter;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.Item.ItemInfoBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.uikit.item.HeaderItem;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.uikit.protocol.ServiceManager;
import com.gala.video.lib.share.uikit.resolver.ItemResolver;
import com.gala.video.lib.share.utils.AnimationUtil;
import java.util.ArrayList;
import java.util.List;

public abstract class Card extends ComponentGroup implements Presenter {
    private ActionPolicy mActionPolicy = new CardActionPolicy(this);
    private BlockLayout mBlockLayout;
    protected CardInfoModel mCardInfoModel;
    private boolean mHasTitle;
    private HeaderItem mHeaderItem;
    private int mId = 0;
    protected List<Item> mItems = new ArrayList();
    private Page mParent;
    protected ServiceManager mServiceManager;

    public class CardActionPolicy extends ActionPolicy {
        protected Card mCard;

        public CardActionPolicy(Card card) {
            this.mCard = card;
        }

        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            if (hasFocus) {
                LOG.m869d("holder.itemView = " + holder.itemView + " position = " + holder.getLayoutPosition());
                holder.itemView.bringToFront();
            }
            try {
                Item item = Card.this.getParent().getItem(holder.getLayoutPosition());
                if (item != null) {
                    float scale = 0.0f;
                    if (item.getModel() != null) {
                        scale = item.getModel().getScale();
                    }
                    if (scale > 0.0f) {
                        holder.itemView.setTag(C1632R.id.focus_end_scale, Float.valueOf(scale));
                        AnimationUtil.zoomAnimation(holder.itemView, hasFocus, scale, 200, true);
                    } else {
                        float cardScale = this.mCard.getModel().getScale();
                        holder.itemView.setTag(C1632R.id.focus_end_scale, Float.valueOf(cardScale));
                        AnimationUtil.zoomAnimation(holder.itemView, hasFocus, cardScale, 200, true);
                    }
                    if (hasFocus) {
                        LogUtils.m1568d("Card/Item", "gainFocus, " + item.getModel() + "," + item.getParent().getModel());
                    }
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                holder.itemView.setTag(C1632R.id.focus_end_scale, Float.valueOf(1.1f));
                AnimationUtil.zoomAnimation(holder.itemView, hasFocus, 1.1f, 200, true);
            }
        }

        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            int pos = holder.getLayoutPosition() - Card.this.getBlockLayout().getFirstPosition();
            if (pos >= 0 && pos < this.mCard.getParent().getItemCount()) {
                performClick(parent, pos, pos + 1, this.mCard.getParent().getItem(holder.getLayoutPosition()));
            }
        }

        protected void performClick(ViewGroup parent, int realViewPos, int itemPingbackPos, Item item) {
            if (item.getModel() == null) {
                LogUtils.m1568d("onItemClick", "pos=" + realViewPos + ",itemPingbackPos=" + itemPingbackPos + ",actionModel=null");
                return;
            }
            BaseActionModel actionModel = item.getModel().getActionModel();
            LogUtils.m1568d("onItemClick", "pos=" + realViewPos + ",itemPingbackPos=" + itemPingbackPos + ",actionModel=" + actionModel);
            if (actionModel != null && actionModel.isCanAction()) {
                if (actionModel.getItemType() == ItemDataType.PLST_GROUP) {
                    ((PlstGroupActionModel) actionModel).setPlId(Card.this.mCardInfoModel.mCardId);
                }
                String cardLine = "" + ClickPingbackUtils.getLine(Card.this.getParent(), item.getParent(), item);
                String allline = "" + Card.this.getAllLine();
                if (PingbackUtils.getPingbackPage(parent.getContext()) == PingbackPage.HomePage) {
                    PingBackCollectionFieldUtils.setCardIndex(cardLine + "");
                    PingBackCollectionFieldUtils.setItemIndex(itemPingbackPos + "");
                    PingBackCollectionFieldUtils.setIncomeSrc(PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_c_" + cardLine + "_item_" + itemPingbackPos);
                }
                actionModel.onItemClick(parent.getContext());
                ClickPingbackUtils.itemClickForPingbackPost(ClickPingbackUtils.composeCommonItemPingMap(parent.getContext(), itemPingbackPos, Card.this.mCardInfoModel.mCardId, cardLine, allline, item));
            }
        }
    }

    public abstract BlockLayout onCreateBlockLayout();

    public void parserItems(CardInfoModel cardInfoModel) {
        this.mItems.clear();
        ItemResolver itemResolver = (ItemResolver) this.mServiceManager.getService(ItemResolver.class);
        parseHeaderItem(cardInfoModel, itemResolver);
        int modelLen = ListUtils.getArraySize(cardInfoModel.getItemInfoModels());
        for (int i = 0; i < modelLen; i++) {
            ItemInfoModel[] itemInfoModels = cardInfoModel.getItemInfoModels()[i];
            int len = ListUtils.getArraySize(itemInfoModels);
            for (int j = 0; j < len; j++) {
                ItemInfoModel itemInfoModel = itemInfoModels[j];
                if (itemInfoModel != null) {
                    Item item = (Item) itemResolver.create(itemInfoModel.getItemType());
                    if (item != null) {
                        item.setModel(itemInfoModel);
                        item.assignParent(this);
                        item.setLine(i);
                        this.mItems.add(item);
                    }
                }
            }
        }
        LOG.m869d("item size = " + this.mItems.size());
    }

    public Item parserItem(ItemInfoModel itemInfoModel) {
        if (itemInfoModel == null) {
            return null;
        }
        Item item = (Item) ((ItemResolver) this.mServiceManager.getService(ItemResolver.class)).create(itemInfoModel.getItemType());
        if (item == null) {
            return item;
        }
        item.setModel(itemInfoModel);
        item.assignParent(this);
        return item;
    }

    private void parseHeaderItem(CardInfoModel cardInfoModel, ItemResolver itemResolver) {
        if (hasHeader()) {
            this.mHeaderItem = (HeaderItem) itemResolver.create(UIKitConfig.ITEM_TYPE_HEADER);
            this.mHeaderItem.assignParent(this);
            if (this.mHasTitle) {
                this.mHeaderItem.setTitle(cardInfoModel.getTitle());
                this.mHeaderItem.setSkinEndsWith(ItemInfoBuildTool.getSkinEndsWith(cardInfoModel.isVIPTag));
            }
        }
    }

    public HeaderItem getHeaderItem() {
        return this.mHeaderItem;
    }

    private boolean hasTitle() {
        String name = this.mCardInfoModel.getTitle();
        return (name == null || name.length() == 0) ? false : true;
    }

    public boolean hasHeader() {
        return this.mHasTitle;
    }

    public void setServiceManager(ServiceManager serviceManager) {
        this.mServiceManager = serviceManager;
    }

    public ServiceManager getServiceManager() {
        return this.mServiceManager;
    }

    public int getType() {
        return this.mCardInfoModel.getCardType();
    }

    public ActionPolicy getActionPolicy() {
        return this.mActionPolicy;
    }

    public void setModel(CardInfoModel model) {
        this.mCardInfoModel = model;
        this.mHasTitle = hasTitle();
        if (this.mCardInfoModel.getId() != null) {
            this.mId = this.mCardInfoModel.getId().hashCode();
        }
        parserItems(model);
    }

    public CardInfoModel getModel() {
        return this.mCardInfoModel;
    }

    public Item getItem(int position) {
        return (Item) this.mItems.get(position);
    }

    public void setItems(List<Item> items) {
        this.mItems.clear();
        this.mItems.addAll(items);
    }

    public List<Item> getItems() {
        return this.mItems;
    }

    public int getItemCount() {
        return getItems().size();
    }

    public int getId() {
        return this.mId;
    }

    public void notifyDataSetChanged() {
        setModel(this.mCardInfoModel);
        this.mParent.notifyDataSetChanged();
    }

    public BlockLayout getBlockLayout() {
        return this.mBlockLayout;
    }

    public BlockLayout createBlockLayout() {
        this.mBlockLayout = onCreateBlockLayout();
        if (!(this.mBlockLayout == null || this.mCardInfoModel == null)) {
            this.mBlockLayout.setMargins(getModel().getBodyMarginLeft(), getModel().getBodyMarginTop(), getModel().getBodyMarginRight(), getModel().getBodyMarginBottom());
            this.mBlockLayout.setPadding(getModel().getBodyPaddingLeft(), getModel().getBodyPaddingTop(), getModel().getBodyPaddingRight(), getModel().getBodyPaddingBottom());
            this.mBlockLayout.setVerticalMargin(getModel().getSpaceV());
            this.mBlockLayout.setHorizontalMargin(getModel().getSpaceH());
        }
        return this.mBlockLayout;
    }

    protected void onStart() {
    }

    protected void onStop() {
    }

    protected void onHide() {
    }

    protected void onShow() {
    }

    protected void onDestroy() {
    }

    public void assignParent(Page parent) {
        this.mParent = parent;
    }

    public Page getParent() {
        return this.mParent;
    }

    public boolean isChildVisible(Item child, boolean fully) {
        return this.mParent.isChildVisible(child, fully);
    }

    public int getAllLine() {
        int allLine = 0;
        ItemInfoModel[][] rowsItemInfoModels = this.mCardInfoModel.getItemInfoModels();
        int cardSize = ListUtils.getArraySize(rowsItemInfoModels);
        for (int i = 0; i < cardSize; i++) {
            ItemInfoModel[] columnItemInfoModels = rowsItemInfoModels[i];
            int itemSize = ListUtils.getArraySize(columnItemInfoModels);
            for (int j = 0; j < itemSize; j++) {
                if (columnItemInfoModels[j] != null) {
                    allLine++;
                    break;
                }
            }
        }
        return allLine;
    }
}
