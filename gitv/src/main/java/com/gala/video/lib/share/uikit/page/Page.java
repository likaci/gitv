package com.gala.video.lib.share.uikit.page;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.util.SparseArray;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.LinearLayout;
import com.gala.video.albumlist.utils.LOG;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.uikit.ComponentGroup;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.actionpolicy.PageActionPolicy;
import com.gala.video.lib.share.uikit.adapter.GroupBaseAdapter;
import com.gala.video.lib.share.uikit.adapter.PageAdapter;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.contract.PageContract.Presenter;
import com.gala.video.lib.share.uikit.contract.PageContract.View;
import com.gala.video.lib.share.uikit.core.BinderViewHolder;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.dataparser.GroupParser;
import com.gala.video.lib.share.uikit.item.HeaderItem;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.item.LoadingItem;
import com.gala.video.lib.share.uikit.protocol.ServiceManager;
import com.gala.video.lib.share.uikit.resolver.BaseItemBinderResolver;
import com.gala.video.lib.share.uikit.utils.HandlerThreadPool;
import com.gala.video.lib.share.uikit.utils.HandlerThreadPool.ThreadHandler;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import com.gala.video.lib.share.uikit.utils.MethodUtils;
import com.gala.video.lib.share.uikit.view.LoadingView;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Page extends ComponentGroup implements Presenter {
    private static final int LOADING_HEIGHT = 300;
    private static final int MSG_APPEND_DATA = 3;
    private static final int MSG_BACK_TO_TOP = 7;
    private static final int MSG_CHANGE_DATA = 9;
    private static final int MSG_DATASET_CHANGED = 6;
    private static final int MSG_HIDE_ALL = 8;
    private static final int MSG_HIDE_LOADING = 5;
    private static final int MSG_INSERT_DATA = 11;
    private static final int MSG_REMOVE_DATA = 10;
    private static final int MSG_SET_DATA = 1;
    private static final int MSG_SHOW_LOADING = 4;
    private static final int MSG_UPDATE_DATA = 2;
    private static final String TAG = "UIKit-Page";
    private static final HandlerThreadPool sHandlerThreadPool = new HandlerThreadPool("page-thread", 3);
    private PageAdapter mBaseAdapter;
    private List<CardInfoModel> mCardInfoModelList = new ArrayList();
    private List<Card> mCardList = new ArrayList();
    private List<Card> mCardShown = new ArrayList();
    private SparseArray<Card> mCards = new SparseArray();
    private final Condition mCondition = this.mLock.newCondition();
    private GroupParser mDataParser;
    private boolean mDataSetChanged = false;
    private List<Card> mFirstPageCards = new ArrayList();
    private List<CardInfoModel> mFirstPageModel = new ArrayList();
    private boolean mIsDefaultPage = false;
    private List<Item> mItems = new ArrayList();
    private List<BlockLayout> mLayouts = new ArrayList();
    private LoadingItem mLoadingItem;
    private LinearLayout mLoadingLayout;
    private Lock mLock = new ReentrantLock();
    private MyHandler mMainHandler;
    private PageActionPolicy mPageActionPolicy = new PageActionPolicy(this);
    private ServiceManager mServiceManager;
    private boolean mShowLoading = false;
    private MyThreadHandler mThreadHandler;
    private List<ActionPolicy> mUserActionPolicies = new ArrayList();
    private View mView;

    private class LocalBundle {
        public int arg1;
        public int arg2;
        public int arg3;
        public Object obj;

        private LocalBundle() {
        }
    }

    private static class MyHandler extends Handler {
        public Page mPage;

        public MyHandler(Looper looper) {
            super(looper);
        }

        public void setPage(Page page) {
            this.mPage = page;
        }

        public void handleMessage(Message msg) {
            Log.d(Page.TAG, "MainHandler start msg = " + msg.what + " page = " + this.mPage);
            Page page = this.mPage;
            if (page == null) {
                Log.d(Page.TAG, "MainHandler handleMessage return!");
                return;
            }
            page.mLock.lock();
            if (page.isDestroy()) {
                page.mLock.unlock();
                return;
            }
            switch (msg.what) {
                case 1:
                    if (!page.mView.get().hasFocus()) {
                        AnimationUtil.clearZoomAnimation(page.mView.get().getFocusView());
                    }
                    page.setData(page, page.mItems, page.mLayouts);
                    page.getAdapter().notifyDataSetChanged();
                    page.mView.setFocusPosition(getFocusPosition(page, msg));
                    page.signal();
                    break;
                case 2:
                    Log.d(Page.TAG, "msg.arg1 = " + msg.arg1 + " msg.arg2 = " + msg.arg2);
                    page.setData(page, page.mItems, page.mLayouts);
                    page.getAdapter().notifyDataSetAdd(msg.arg1, msg.arg2);
                    page.signal();
                    break;
                case 3:
                    if (page.isLoadingShown()) {
                        page.getAdapter().notifyDataSetRemoved(page.getAdapter().getLastPosition());
                        CardFocusHelper.updateFocusDraw(page.getRoot().getContext(), 250);
                    }
                    page.setData(page, page.mItems, page.mLayouts);
                    page.getAdapter().notifyDataSetAdd();
                    page.signal();
                    break;
                case 4:
                    page.setData(page, page.mItems, page.mLayouts);
                    page.getAdapter().notifyDataSetAdd();
                    page.signal();
                    break;
                case 5:
                    int lastPosition = page.getAdapter().getLastPosition();
                    boolean isLoadingShow = page.isLoadingShown();
                    page.setData(page, page.mItems, page.mLayouts);
                    if (isLoadingShow) {
                        if (page.mView.get().isChildVisible(lastPosition)) {
                            page.getAdapter().notifyItemRemoved(lastPosition);
                        } else {
                            page.getAdapter().notifyDataSetRemoved(lastPosition);
                            page.getAdapter().notifyDataSetAdd();
                        }
                    }
                    page.signal();
                    break;
                case 8:
                    page.hide();
                    break;
                case 9:
                    LocalBundle bundle = msg.obj;
                    page.setData(page, page.mItems, page.mLayouts);
                    page.mView.setFocusPosition(getFocusPosition(page, msg));
                    page.getAdapter().notifyDataSetChanged(bundle.arg1, bundle.arg2, bundle.arg3);
                    page.signal();
                    break;
                case 10:
                    page.setData(page, page.mItems, page.mLayouts);
                    page.getAdapter().notifyItemRemoved(msg.arg1, msg.arg2, ((Boolean) msg.obj).booleanValue());
                    page.signal();
                    break;
            }
            page.mLock.unlock();
            Log.d(Page.TAG, "MainHandler end");
        }

        private int getFocusPosition(Page page, Message msg) {
            if (msg.arg1 >= 0) {
                return msg.arg1;
            }
            if (msg.arg2 < 0 || page.mCardShown.size() <= 0) {
                return -1;
            }
            int cardIndex = msg.arg2;
            int focusPosition = page.mView.get().getFocusPosition();
            if (cardIndex >= page.mCardShown.size()) {
                return ((Card) page.mCardShown.get(page.mCardShown.size() - 1)).getBlockLayout().getLastPosition();
            }
            Card card = (Card) page.mCardShown.get(cardIndex);
            if (card.getBlockLayout() == null || !card.getBlockLayout().isOutRang(focusPosition)) {
                return focusPosition;
            }
            if (focusPosition < card.getBlockLayout().getFirstPosition()) {
                return card.getBlockLayout().getFirstPosition();
            }
            return card.getBlockLayout().getLastPosition();
        }
    }

    private static class MyThreadHandler extends ThreadHandler {
        public Page mPage;

        private MyThreadHandler() {
        }

        public void setPage(Page page) {
            this.mPage = page;
        }

        public void handleMessage(Handler handler, Message msg) {
            Log.d(Page.TAG, "ThreadHandler start msg = " + msg.what + " page = " + this.mPage);
            Page page = this.mPage;
            if (page == null) {
                Log.d(Page.TAG, "ThreadHandler handleMessage return!");
                return;
            }
            page.mLock.lock();
            if (page.isDestroy()) {
                page.mLock.unlock();
                return;
            }
            List<CardInfoModel> cards;
            int cardIndex;
            Message message;
            switch (msg.what) {
                case 1:
                    cards = msg.obj;
                    if (cards != null) {
                        cardIndex = page.getCardIndex();
                        page.doSetData(cards);
                        message = page.mMainHandler.obtainMessage(1);
                        message.arg1 = -1;
                        message.arg2 = cardIndex;
                        page.mMainHandler.sendMessage(message);
                        page.waitForFinished(1);
                        break;
                    }
                    break;
                case 2:
                    Log.d(Page.TAG, "threadHandler MSG_UPDATE_DATA");
                    if (page.doUpdateCaredModel(msg.obj)) {
                        page.waitForFinished(2);
                    }
                    Log.d(Page.TAG, "after await");
                    break;
                case 3:
                    cards = (List) msg.obj;
                    if (cards == null) {
                        if (page.isShowLoading()) {
                            page.hideLoading();
                            CardFocusHelper.updateFocusDraw(page.getRoot().getContext(), 250);
                            break;
                        }
                    }
                    page.mCardInfoModelList.addAll(cards);
                    page.appendCards(page.mDataParser.parserGroup(page, cards));
                    page.mMainHandler.sendEmptyMessage(3);
                    page.waitForFinished(3);
                    break;
                    break;
                case 4:
                    page.mShowLoading = true;
                    page.initLoading();
                    page.addLoadingView();
                    page.mMainHandler.sendEmptyMessage(4);
                    page.waitForFinished(4);
                    break;
                case 5:
                    if (page.isShowLoading()) {
                        Log.d(Page.TAG, "hide loading");
                        page.mShowLoading = false;
                        page.mLayouts.remove(page.mLoadingLayout);
                        page.mItems.remove(page.mLoadingItem);
                        page.mMainHandler.sendEmptyMessage(5);
                        page.waitForFinished(5);
                        break;
                    }
                    break;
                case 6:
                    cardIndex = page.getCardIndex();
                    page.transformItems();
                    message = page.mMainHandler.obtainMessage(1);
                    message.arg1 = msg.arg1;
                    message.arg2 = cardIndex;
                    page.mMainHandler.sendMessage(message);
                    page.waitForFinished(1);
                    break;
                case 7:
                    if (page.mView.get().getLayoutManager().isCanScroll(false)) {
                        page.initDataList(page.mFirstPageModel);
                        page.setCards(page.mFirstPageCards);
                        message = page.mMainHandler.obtainMessage(1);
                        message.arg1 = 0;
                        page.mMainHandler.sendMessage(message);
                        page.waitForFinished(1);
                        break;
                    }
                    break;
                case 9:
                    page.dochangeCardModel((CardInfoModel) msg.obj);
                    page.waitForFinished(9);
                    break;
                case 10:
                    page.doRemoveCardModel(msg.arg1, msg.arg2, ((Boolean) msg.obj).booleanValue());
                    page.waitForFinished(10);
                    break;
                case 11:
                    page.doInsertCardModel(msg.arg1, (CardInfoModel) msg.obj);
                    page.waitForFinished(2);
                    break;
            }
            page.mLock.unlock();
            Log.d(Page.TAG, "ThreadHandler end page = " + page);
        }
    }

    public Page(ServiceManager serviceManager) {
        this.mServiceManager = serviceManager;
        this.mDataParser = new GroupParser(this.mServiceManager);
        this.mPageActionPolicy.keepFocusOnTop(true);
        initHandler((Context) serviceManager.getService(Context.class));
    }

    private void initHandler(Context context) {
        this.mMainHandler = new MyHandler(context.getMainLooper());
        this.mMainHandler.setPage(this);
        this.mThreadHandler = new MyThreadHandler();
        this.mThreadHandler.setPage(this);
        sHandlerThreadPool.registerHandler(this.mThreadHandler);
    }

    public void setData(List<CardInfoModel> cards) {
        Log.d(TAG, "setData cards = " + cards + " size = " + (cards != null ? cards.size() : 0) + " isAlive = " + this.mThreadHandler.getThread().isAlive() + " getState = " + this.mThreadHandler.getThread().getState());
        this.mThreadHandler.get().removeMessages(1);
        Message msg = this.mThreadHandler.get().obtainMessage(1);
        msg.obj = cards;
        this.mThreadHandler.get().sendMessage(msg);
    }

    public void setDataSync(List<CardInfoModel> cards) {
        doSetData(cards);
        setData(this, this.mItems, this.mLayouts);
        getAdapter().notifyDataSetChanged();
    }

    public void appendData(List<CardInfoModel> cards) {
        Log.d(TAG, "appendData cards = " + cards + " size = " + (cards != null ? cards.size() : 0));
        Message msg = this.mThreadHandler.get().obtainMessage(3);
        msg.obj = cards;
        this.mThreadHandler.get().sendMessage(msg);
    }

    public void updateCaredModel(CardInfoModel cardModel) {
        Log.d(TAG, "updateCaredModel cardModel = " + cardModel + " items = " + cardModel.getItemInfoModels());
        Message msg = this.mThreadHandler.get().obtainMessage(2);
        msg.obj = cardModel;
        this.mThreadHandler.get().sendMessage(msg);
    }

    public void changeCardModel(CardInfoModel cardModel) {
        Log.d(TAG, "changeCardModel cardModel = " + cardModel + " items = " + cardModel.getItemInfoModels());
        Message msg = this.mThreadHandler.get().obtainMessage(9);
        msg.obj = cardModel;
        this.mThreadHandler.get().sendMessage(msg);
    }

    public void insertCardModel(int index, CardInfoModel cardModel) {
        Log.d(TAG, "insertCardModel cardModel = " + cardModel + " items = " + cardModel.getItemInfoModels());
        Message msg = this.mThreadHandler.get().obtainMessage(11);
        msg.obj = cardModel;
        msg.arg1 = index;
        this.mThreadHandler.get().sendMessage(msg);
    }

    public void removeCardModel(int index, int count) {
        removeCardModel(index, count, true);
    }

    public void removeCardModel(int index, int count, boolean isAnimationRequired) {
        Log.d(TAG, "removeCardModel index = " + index + " count = " + count);
        Message msg = this.mThreadHandler.get().obtainMessage(10);
        msg.arg1 = index;
        msg.arg2 = count;
        msg.obj = Boolean.valueOf(isAnimationRequired);
        this.mThreadHandler.get().sendMessage(msg);
    }

    public void setTopBarHeight(int topBarHeight) {
        this.mPageActionPolicy.setTopBarHeight(topBarHeight);
    }

    public void onStart() {
        Log.d(TAG, "onStart");
        this.mMainHandler.removeMessages(8);
        sendStartEventToCards();
    }

    public void onStop() {
        Log.d(TAG, "onStop");
        sendStopEventToCards();
    }

    public void backToTop() {
        Log.d(TAG, "backToTop");
        if (this.mDataSetChanged) {
            this.mDataSetChanged = false;
            Message message = this.mThreadHandler.get().obtainMessage(6);
            message.arg1 = 0;
            this.mThreadHandler.get().sendMessage(message);
            return;
        }
        this.mThreadHandler.get().sendEmptyMessage(7);
    }

    protected void onDestroy() {
        Log.d(TAG, "onDestroy");
        this.mLock.lock();
        signal();
        this.mMainHandler.removeCallbacksAndMessages(null);
        this.mMainHandler.setPage(null);
        this.mThreadHandler.setPage(null);
        sHandlerThreadPool.unregisterHandler(this.mThreadHandler);
        this.mView.get().release();
        sendDestroyEventToCards();
        unregisterAllActionPolicy();
        if (this.mBaseAdapter != null) {
            this.mBaseAdapter.release();
            this.mBaseAdapter = null;
        }
        clearList();
        this.mLock.unlock();
    }

    private void clearList() {
        for (Field field : getClass().getDeclaredFields()) {
            try {
                if (field.getType() == List.class && field.getType() == SparseArray.class) {
                    if (field.getType() == List.class) {
                        ((List) field.get(this)).clear();
                    } else {
                        ((SparseArray) field.get(this)).clear();
                    }
                    field.setAccessible(true);
                    field.set(this, null);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Card> getCards() {
        return this.mCardShown;
    }

    public void sendStartEventToCards() {
        if (isStart()) {
            sendEventToCards("start");
        }
    }

    public void sendStopEventToCards() {
        sendEventToCards("stop");
    }

    public void sendDestroyEventToCards() {
        sendEventToCards("destroy");
    }

    public BlocksView getRoot() {
        return this.mView.get();
    }

    public boolean shouldLoadMore() {
        return isShowLoading();
    }

    private void initLoading() {
        if (this.mLoadingItem == null) {
            this.mLoadingItem = new LoadingItem();
            this.mLoadingItem.setHeight(ResourceUtil.getPx(300));
            this.mLoadingItem.setWidth(-1);
        }
        if (this.mLoadingLayout == null) {
            this.mLoadingLayout = new LinearLayout();
            this.mLoadingLayout.setItemCount(1);
        }
    }

    private void addLoadingView() {
        if (!this.mLayouts.contains(this.mLoadingLayout)) {
            this.mLayouts.add(this.mLoadingLayout);
        }
        if (!this.mItems.contains(this.mLoadingItem)) {
            this.mItems.add(this.mLoadingItem);
        }
    }

    public void showLoading() {
        this.mThreadHandler.get().sendEmptyMessage(4);
    }

    public void hideLoading() {
        this.mThreadHandler.get().sendEmptyMessage(5);
    }

    private boolean doUpdateCaredModel(CardInfoModel cardModel) {
        Card card = getCard(cardModel.getId());
        if (card == null) {
            LOG.e("Card not exist, id = " + cardModel.getId());
            return false;
        }
        int oldCount = card.getItems().size();
        int oldEnd = card.getBlockLayout().getLastPosition();
        card.setModel(cardModel);
        Message msg = this.mMainHandler.obtainMessage(2);
        msg.arg1 = oldEnd + 1;
        int itemCount = oldCount > 0 ? card.getItemCount() - oldCount : card.hasHeader() ? card.getItemCount() + 1 : card.getItemCount();
        msg.arg2 = itemCount;
        if (msg.arg2 <= 0) {
            return false;
        }
        transformItems();
        this.mMainHandler.sendMessage(msg);
        return true;
    }

    private void dochangeCardModel(CardInfoModel model) {
        Card card = getCard(model.getId());
        if (card == null) {
            LOG.e("Card not exist, id = " + model.getId());
            return;
        }
        int cardIndex = getCardIndex();
        int oldStart = card.getBlockLayout().getFirstPosition();
        int oldEnd = card.getBlockLayout().getLastPosition();
        boolean oldHasHeader = card.hasHeader();
        card.setModel(model);
        transformItems();
        LocalBundle bundle = new LocalBundle();
        bundle.arg1 = oldStart;
        bundle.arg2 = oldEnd;
        bundle.arg3 = card.getItemCount();
        Message msg = this.mMainHandler.obtainMessage(9);
        msg.obj = bundle;
        msg.arg1 = -1;
        msg.arg2 = cardIndex;
        if (oldHasHeader != card.hasHeader()) {
            int i;
            if (oldHasHeader) {
                i = msg.arg2 - 1;
                msg.arg2 = i;
            } else {
                i = msg.arg2 + 1;
                msg.arg2 = i;
            }
            msg.arg2 = i;
        }
        this.mMainHandler.sendMessage(msg);
    }

    private void doRemoveCardModel(int index, int count, boolean isAnimationRequired) {
        if (count > 0 && index > 0 && index < this.mCardInfoModelList.size()) {
            int i;
            CardInfoModel temp;
            CardInfoModel startModel = null;
            for (i = index; i < index + count; i++) {
                temp = (CardInfoModel) this.mCardInfoModelList.get(i);
                if (ListUtils.getArraySize(temp.getItemInfoModels()) > 0) {
                    startModel = temp;
                    break;
                }
            }
            CardInfoModel endModel = null;
            for (i = (index + count) - 1; i >= index; i--) {
                temp = (CardInfoModel) this.mCardInfoModelList.get(i);
                if (ListUtils.getArraySize(temp.getItemInfoModels()) > 0) {
                    endModel = temp;
                    break;
                }
            }
            if (startModel != null && endModel != null) {
                Card startCard = getCard(startModel.getId());
                Card endCard = getCard(endModel.getId());
                if (startCard != null && endCard != null) {
                    int firstPosition = this.mCardList.indexOf(startCard);
                    for (i = this.mCardList.indexOf(endCard); i >= firstPosition; i--) {
                        this.mCardList.remove((Card) this.mCardList.get(i));
                        this.mCardInfoModelList.remove(i);
                    }
                    int start = startCard.getBlockLayout().getFirstPosition();
                    if (startCard.hasHeader()) {
                        start--;
                    }
                    int end = endCard.getBlockLayout().getLastPosition();
                    transformItems();
                    Message msg = this.mMainHandler.obtainMessage(10);
                    msg.arg1 = start;
                    msg.arg2 = (end - start) + 1;
                    msg.obj = Boolean.valueOf(isAnimationRequired);
                    this.mMainHandler.sendMessage(msg);
                }
            }
        }
    }

    private void doInsertCardModel(int index, CardInfoModel model) {
        if (model != null && index < this.mCardInfoModelList.size() && insertToDataList(index, model)) {
            Card preCard = getCardBeforeInsert(index);
            Message msg = this.mMainHandler.obtainMessage(2);
            if (preCard == null) {
                msg.arg1 = 0;
                msg.arg2 = ((Card) this.mCardList.get(index)).getItemCount();
            } else {
                msg.arg1 = preCard.getBlockLayout().getLastPosition() + 1;
                msg.arg2 = ((Card) this.mCardList.get(index)).getItemCount();
            }
            this.mMainHandler.sendMessage(msg);
            transformItems();
        }
    }

    private boolean insertToDataList(int index, CardInfoModel model) {
        Card card = this.mDataParser.parserCard(this, model);
        if (card == null) {
            return false;
        }
        this.mCardList.add(index, card);
        this.mCardInfoModelList.add(index, model);
        if (index < this.mFirstPageModel.size()) {
            this.mFirstPageModel.add(index, model);
        }
        if (index < this.mFirstPageCards.size()) {
            this.mFirstPageCards.add(index, card);
        }
        return true;
    }

    private Card getCardBeforeInsert(int index) {
        while (true) {
            index--;
            if (index < 0) {
                return null;
            }
            Card temp = (Card) this.mCardList.get(index);
            if (temp != null && temp.getItemCount() > 0) {
                return temp;
            }
        }
    }

    private Card getCard(String modelId) {
        return (Card) this.mCards.get(modelId.hashCode());
    }

    private void transformItems() {
        synchronized (this) {
            this.mLayouts.clear();
            this.mItems.clear();
            this.mCards.clear();
            this.mCardShown.clear();
            int line = 0;
            for (Card card : this.mCardList) {
                int line2;
                this.mCards.append(card.getId(), card);
                if (card.getItemCount() > 0) {
                    if (card.hasHeader()) {
                        HeaderItem titleItem = card.getHeaderItem();
                        if (titleItem != null) {
                            this.mItems.add(titleItem);
                        }
                        this.mLayouts.add(new LinearLayout().setItemCount(1));
                    }
                    line2 = line + 1;
                    card.setLine(line);
                    this.mCardShown.add(card);
                } else {
                    line2 = line;
                }
                this.mLayouts.add(card.createBlockLayout());
                this.mItems.addAll(card.getItems());
                line = line2;
            }
            if (this.mShowLoading) {
                Log.d(TAG, "add loading");
                addLoadingView();
            }
            int count = 0;
            for (BlockLayout layout : this.mLayouts) {
                layout.setRang(count, (layout.getItemCount() + count) - 1);
                count += layout.getItemCount();
            }
        }
    }

    public void setIsDefaultPage(boolean isDefaultPage) {
        this.mIsDefaultPage = isDefaultPage;
    }

    public boolean isDefaultPage() {
        return this.mIsDefaultPage;
    }

    private void setCards(List<Card> cards) {
        Log.d(TAG, "setCards");
        this.mCardList.clear();
        this.mCardList.addAll(cards);
        this.mFirstPageCards.clear();
        this.mFirstPageCards.addAll(this.mCardList);
        transformItems();
    }

    private void appendCards(List<Card> cards) {
        Log.d(TAG, "appendCards");
        this.mCardList.addAll(cards);
        transformItems();
    }

    public Card getCard(int position) {
        return (Card) this.mCardList.get(position);
    }

    public Item getItem(int position) {
        return (Item) this.mBaseAdapter.getComponent(position);
    }

    public int getItemCount() {
        if (this.mBaseAdapter != null) {
            return this.mBaseAdapter.getCount();
        }
        return 0;
    }

    private PageAdapter newAdapter() {
        return new PageAdapter(this, (Context) this.mServiceManager.getService(Context.class), (BaseItemBinderResolver) this.mServiceManager.getService(BaseItemBinderResolver.class));
    }

    public ActionPolicy getActionPolicy() {
        return this.mPageActionPolicy;
    }

    public GroupBaseAdapter<Item> getAdapter() {
        if (this.mBaseAdapter == null) {
            this.mBaseAdapter = newAdapter();
        }
        return this.mBaseAdapter;
    }

    public void onShow() {
        Log.d(TAG, "onShow");
        LogUtils.i(TAG, "onShow isStart() = ", Boolean.valueOf(isStart()));
        LogUtils.i(TAG, "onShow mDataSetChanged = " + this.mDataSetChanged, " AppRuntimeEnv.get().isPlayInHome() = ", Boolean.valueOf(AppRuntimeEnv.get().isPlayInHome()));
        if (isStart()) {
            showAllItems();
            if (this.mDataSetChanged && !AppRuntimeEnv.get().isPlayInHome()) {
                this.mDataSetChanged = false;
                Message message = this.mThreadHandler.get().obtainMessage(6);
                message.arg1 = -1;
                this.mThreadHandler.get().sendMessage(message);
            }
        }
    }

    private void showAllItems() {
        BlocksView root = this.mView.get();
        int first = root.getFirstAttachedPosition();
        int last = root.getLastAttachedPosition();
        if (first >= 0 && last >= 0) {
            for (int i = first; i <= last; i++) {
                ViewHolder vh = root.getViewHolderByPosition(i);
                if (vh != null) {
                    ((BinderViewHolder) vh).show();
                }
            }
        }
    }

    public void onHide() {
        Log.d(TAG, "onHide");
        hideAllItems();
    }

    private void hideAllItems() {
        BlocksView root = this.mView.get();
        int first = root.getFirstAttachedPosition();
        int last = root.getLastAttachedPosition();
        if (first >= 0 && last >= 0) {
            for (int i = first; i <= last; i++) {
                ViewHolder vh = root.getViewHolderByPosition(i);
                if (vh != null) {
                    ((BinderViewHolder) vh).hide();
                }
            }
        }
    }

    public int getType() {
        return 0;
    }

    public void notifyDataSetChanged() {
        this.mDataSetChanged = true;
    }

    public void setView(View view) {
        this.mView = view;
    }

    public List<CardInfoModel> getModel() {
        return this.mCardInfoModelList;
    }

    public boolean isOnTop() {
        return this.mView.isOnTop();
    }

    public boolean isChildVisible(Item child, boolean fully) {
        BlocksView root = this.mView.get();
        return root.isChildVisibleToUser(root.getViewByPosition(this.mItems.indexOf(child)), fully);
    }

    public int getChildPosition(Card card) {
        return this.mCardList.indexOf(card);
    }

    private void sendEventToCards(String name) {
        this.mLock.lock();
        for (Card card : this.mCardList) {
            MethodUtils.invoke(card, name, new Object[0]);
        }
        this.mLock.unlock();
    }

    public void sendEventToAttachedCards(String name, boolean toActionPolicy, Object... objs) {
        BlocksView root = this.mView.get();
        int first = root.getFirstAttachedPosition();
        int last = root.getLastAttachedPosition();
        BlockLayout blockLayout = null;
        if (first >= 0 && last >= 0) {
            int i = first;
            while (i <= last) {
                if (blockLayout == null || blockLayout.isOutRang(i)) {
                    Item item = getItem(i);
                    if (item != null) {
                        Card card = item.getParent();
                        if (card != null) {
                            Object object;
                            if (toActionPolicy) {
                                object = card.getActionPolicy();
                            } else {
                                Card object2 = card;
                            }
                            MethodUtils.invoke(object, name, objs);
                            blockLayout = card.getBlockLayout();
                        }
                    }
                }
                i++;
            }
        }
    }

    public void registerActionPolicy(ActionPolicy actionPolicy) {
        synchronized (this.mUserActionPolicies) {
            if (!this.mUserActionPolicies.contains(actionPolicy)) {
                this.mUserActionPolicies.add(actionPolicy);
            }
        }
    }

    public void unregisterActionPolicy(ActionPolicy actionPolicy) {
        synchronized (this.mUserActionPolicies) {
            this.mUserActionPolicies.remove(actionPolicy);
        }
    }

    public void unregisterAllActionPolicy() {
        synchronized (this.mUserActionPolicies) {
            this.mUserActionPolicies.clear();
        }
    }

    public void dispatchUserActionPolicy(String name, Object... objs) {
        synchronized (this.mUserActionPolicies) {
            for (ActionPolicy actionPolicy : this.mUserActionPolicies) {
                MethodUtils.invoke(actionPolicy, name, objs);
            }
        }
    }

    private void initDataList(List<CardInfoModel> cards) {
        this.mCardInfoModelList.clear();
        this.mCardInfoModelList.addAll(cards);
        this.mFirstPageModel.clear();
        this.mFirstPageModel.addAll(this.mCardInfoModelList);
    }

    private void doSetData(List<CardInfoModel> cards) {
        if (cards != null) {
            this.mThreadHandler.get().removeCallbacksAndMessages(null);
            sendDestroyEventToCards();
            initDataList(cards);
            setCards(this.mDataParser.parserGroup(this, this.mCardInfoModelList));
        }
    }

    private int getCardIndex() {
        Item item = getItem(this.mView.get().getFocusPosition());
        if (item == null) {
            return -1;
        }
        Card card = item.getParent();
        if (card != null) {
            return this.mCardShown.indexOf(card);
        }
        return -1;
    }

    private void waitForFinished(int what) {
        if (this.mMainHandler.hasMessages(what)) {
            Log.d(TAG, "before await " + System.identityHashCode(this.mCondition));
            try {
                this.mCondition.await();
            } catch (InterruptedException e) {
                Log.d(TAG, e.toString());
            }
            Log.d(TAG, "after await " + System.identityHashCode(this.mCondition));
        }
    }

    private void signal() {
        Log.d(TAG, "before notify " + System.identityHashCode(this.mCondition));
        this.mCondition.signal();
        Log.d(TAG, "after notify " + System.identityHashCode(this.mCondition));
    }

    private void setData(Page page, List<Item> items, List<BlockLayout> layouts) {
        synchronized (page) {
            page.getAdapter().setData(items);
            page.mView.setLayouts(layouts);
        }
    }

    private boolean isLoadingShown() {
        android.view.View view = this.mView.get().getViewByPosition(getAdapter().getLastPosition());
        return view != null && (view instanceof LoadingView);
    }

    private boolean isShowLoading() {
        if (!this.mShowLoading) {
            return false;
        }
        Item item = getItem(getAdapter().getLastPosition());
        if (item == null || item.getType() != 999) {
            return false;
        }
        return true;
    }
}
