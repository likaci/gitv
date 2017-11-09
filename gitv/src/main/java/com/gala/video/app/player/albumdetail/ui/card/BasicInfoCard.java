package com.gala.video.app.player.albumdetail.ui.card;

import android.util.Log;
import android.view.ViewGroup;
import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.layout.LinearLayout;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.app.player.albumdetail.ui.overlay.DetailOverlay;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.card.Card.CardActionPolicy;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import java.util.Collections;
import org.xbill.DNS.WKSRecord.Service;

public class BasicInfoCard extends Card {
    private static String TAG = "BasicInfoCard";
    CardActionPolicy mCardActionPolicy = new MyActionPolicy(this);
    BasicInfoItem mItem;

    class MyActionPolicy extends CardActionPolicy {
        public MyActionPolicy(Card card) {
            super(card);
        }

        public void onFirstLayout(ViewGroup parent) {
            super.onFirstLayout(parent);
            Log.v(BasicInfoCard.TAG, "onFirstLayout");
        }

        public void onItemClick(ViewGroup parent, ViewHolder holder) {
            Log.v(BasicInfoCard.TAG, "onItemClick");
        }

        public void onFocusPositionChanged(ViewGroup parent, int position, boolean hasFocus) {
            super.onFocusPositionChanged(parent, position, hasFocus);
            Log.v(BasicInfoCard.TAG, "onFocusPositionChanged");
        }

        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            Log.v(BasicInfoCard.TAG, "onItemFocusChanged");
        }
    }

    public BlockLayout onCreateBlockLayout() {
        TAG = "BasicInfoCard@" + Integer.toHexString(hashCode());
        Log.v(TAG, "onCreateBlockLayout, @" + hashCode());
        LinearLayout layout = new LinearLayout();
        layout.setItemCount(1);
        return layout;
    }

    public void parserItems(CardInfoModel cardInfoModel) {
        Log.v(TAG, "parserItems, cardInfoModel@" + cardInfoModel.hashCode());
        Log.v(TAG, "parserItems, @" + hashCode());
        if (this.mItem == null) {
            this.mItem = new BasicInfoItem();
        }
        if (cardInfoModel.detailCreateInfo instanceof DetailOverlay) {
            this.mItem.setDetailOverlay((DetailOverlay) cardInfoModel.detailCreateInfo);
            super.parserItems(cardInfoModel);
            this.mItem.assignParent(this);
        }
        setItems(Collections.singletonList(this.mItem));
    }

    public int getType() {
        return Service.NNTP;
    }

    public ActionPolicy getActionPolicy() {
        return this.mCardActionPolicy;
    }

    public int getAllLine() {
        if (!this.mItem.getView().isComplextContent()) {
            return 0;
        }
        Log.v(TAG, "isComplextContent true");
        return 1;
    }
}
