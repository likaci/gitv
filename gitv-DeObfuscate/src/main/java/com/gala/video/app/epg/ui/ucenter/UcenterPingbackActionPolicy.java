package com.gala.video.app.epg.ui.ucenter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.app.epg.uikit.card.UCenterCard;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ShowPingback;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.uikit.view.StandardItemView;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class UcenterPingbackActionPolicy extends ActionPolicy {
    private String TAG = "UcenterPingbackActionPolicy";
    private int mConstant = 1000;
    private Context mContext;
    private String mE;
    private Page mPage;
    private SparseArray<Long> mShowList = new SparseArray();

    public UcenterPingbackActionPolicy(Context context, Page page, String pingback_e) {
        this.mContext = context;
        this.mPage = page;
        this.mE = pingback_e;
    }

    public void onFirstLayout(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void onScrollStop(ViewGroup parent) {
        initTimestamp(parent);
    }

    public void initTimestamp(ViewGroup parent) {
        try {
            int first = cast(parent).getFirstAttachedPosition();
            int last = cast(parent).getLastAttachedPosition();
            for (int i = first; i <= last; i++) {
                if (i < 0) {
                    LogUtils.m1577w(this.TAG, "initTimestamp. i <0 ;i = " + i);
                    return;
                }
                Item item = this.mPage.getItem(i);
                if (item == null) {
                    LogUtils.m1577w(this.TAG, "initTimestamp. item==null. i = " + i);
                    return;
                }
                Card card = item.getParent();
                if (card == null) {
                    LogUtils.m1577w(this.TAG, "initTimestamp. card==null. i = " + i);
                    return;
                }
                int key = (card.getLine() * this.mConstant) + item.getLine();
                if (!isItemViewVisiable(cast(parent), i)) {
                    this.mShowList.remove(key);
                } else if (this.mShowList.indexOfKey(key) < 0) {
                    this.mShowList.append(key, Long.valueOf(System.currentTimeMillis()));
                }
            }
        } catch (Exception e) {
            LogUtils.m1571e(this.TAG, "initTimestamp.Exception e.getMessage()= " + e.getMessage());
            e.printStackTrace();
        }
    }

    boolean isItemViewVisiable(BlocksView blocksView, int position) {
        View view = blocksView.getViewByPosition(position);
        int top = view.getTop() - blocksView.getScrollY();
        int bottom = view.getBottom() - blocksView.getScrollY();
        if ((view instanceof StandardItemView) && ((StandardItemView) view).isTitleoutType()) {
            bottom -= ResourceUtil.getPx(34);
        }
        int parentHeight = blocksView.getHeight();
        if (top < 0 || top >= parentHeight || bottom <= 0 || bottom > parentHeight) {
            return false;
        }
        return true;
    }

    public void onScroll(ViewGroup parent, int firstAttachedItem, int lastAttachedItem, int totalItemCount) {
        onSendUcenterCardShowPingback(cast(parent), this.mPage, true);
    }

    public void onItemAnimatorFinished(ViewGroup parent) {
        initTimestamp(parent);
    }

    void clearShowList() {
        this.mShowList.clear();
    }

    public int getLine(Page page, Card currentCard, Item item) {
        int line;
        synchronized (page) {
            line = 0;
            List<Card> cardList = page.getCards();
            int size = cardList.size();
            for (int i = 0; i < size; i++) {
                Card card = (Card) cardList.get(i);
                if (card == null) {
                    LogUtils.m1577w(this.TAG, "getLine. card==null.");
                } else if (currentCard == card) {
                    break;
                } else {
                    line += card.getAllLine();
                }
            }
            line += item.getLine();
        }
        return line;
    }

    public void onSendUcenterCardShowPingback(BlocksView blocksView, Page page, boolean isScroll) {
        try {
            int first = blocksView.getFirstAttachedPosition();
            int last = blocksView.getLastAttachedPosition();
            boolean mIsLogin = GetInterfaceTools.getIGalaAccountManager().isLogin(this.mContext);
            int i = first;
            while (i <= last) {
                if (i < 0) {
                    LogUtils.m1577w(this.TAG, "onSendCardShowPingback. i <0 ;i = " + i);
                } else {
                    Item item = page.getItem(i);
                    if (item == null) {
                        LogUtils.m1577w(this.TAG, "onSendCardShowPingback. item==null. i = " + i);
                    } else {
                        Card card = item.getParent();
                        if (card == null || (card instanceof UCenterCard)) {
                            LogUtils.m1577w(this.TAG, "onSendCardShowPingback. card==null|| card instanceof UCenterCard. card = " + card + " ;i = " + i);
                        } else {
                            int key = (card.getLine() * this.mConstant) + item.getLine();
                            if (((isScroll && !isItemViewVisiable(blocksView, i)) || !isScroll) && this.mShowList.indexOfKey(key) >= 0) {
                                if (System.currentTimeMillis() - ((Long) this.mShowList.get(key)).longValue() > 500) {
                                    GetInterfaceTools.getIHomePingback().createPingback(ShowPingback.UCENTER_CARD_SHOW_PINGBACK).addItem("qtcurl", mIsLogin ? "mine_loggedin" : "mine_guest").addItem("block", card.getModel().mCardId).addItem("e", this.mE).addItem("line", (getLine(page, card, item) + 1) + "").post();
                                }
                                this.mShowList.remove(key);
                            }
                        }
                    }
                }
                i++;
            }
            if (!isScroll) {
                clearShowList();
            }
        } catch (Exception e) {
            LogUtils.m1571e(this.TAG, "onSendCardShowPingback.Exception e.getMessage()= " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void setPingbackE(String e) {
        this.mE = e;
    }
}
