package com.gala.video.lib.share.uikit.card;

import android.view.ViewGroup;
import com.gala.video.albumlist.widget.BlocksView.ViewHolder;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.card.Card.CardActionPolicy;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class TimeLineCard extends GridCard {

    public class TimeLineActionPolicy extends CardActionPolicy {
        public TimeLineActionPolicy(Card card) {
            super(card);
        }

        public void onFocusPositionChanged(ViewGroup parent, int position, boolean hasFocus) {
            TimeLineCard.this.getHeaderItem().setFocusPosition(hasFocus ? position - TimeLineCard.this.getBlockLayout().getFirstPosition() : -1);
        }

        public void onFocusLost(ViewGroup parent, ViewHolder holder) {
            super.onFocusLost(parent, holder);
            TimeLineCard.this.getHeaderItem().setFocusPosition(-1);
        }

        public void onItemFocusChanged(ViewGroup parent, ViewHolder holder, boolean hasFocus) {
            super.onItemFocusChanged(parent, holder, hasFocus);
            TimeLineCard.this.getHeaderItem().setFocusPosition(hasFocus ? holder.getLayoutPosition() - TimeLineCard.this.getBlockLayout().getFirstPosition() : -1);
        }
    }

    public void setModel(CardInfoModel cardInfoModel) {
        super.setModel(cardInfoModel);
        List<Integer> viewCenterList = new ArrayList();
        List<Integer> viewRightList = new ArrayList();
        List<Integer> viewLeftList = new ArrayList();
        List<String> timeLineList = new ArrayList();
        int timeTextMaxWidth = 0;
        int blankSpace = ResourceUtil.getPx(24);
        int left = getModel().getBodyPaddingLeft();
        int modelLen = ListUtils.getArraySize(getModel().getItemInfoModels());
        for (int i = 0; i < modelLen; i++) {
            ItemInfoModel[] ItemInfoModels = getModel().getItemInfoModels()[i];
            int len = ListUtils.getArraySize(ItemInfoModels);
            for (int j = 0; j < len; j++) {
                ItemInfoModel itemInfo = ItemInfoModels[j];
                if (itemInfo != null) {
                    int viewCenter;
                    timeLineList.add(itemInfo.getCuteViewData(UIKitConfig.SPECIAL_DATA, UIKitConfig.KEY_SPECIAL_DATA_TIMELINETITLE));
                    if (i == 0) {
                        viewCenter = left + (itemInfo.getWidth() / 2);
                        if (itemInfo.getWidth() > itemInfo.getHeight()) {
                            timeTextMaxWidth = ResourceUtil.getPx(300);
                        } else {
                            timeTextMaxWidth = ResourceUtil.getPx(180);
                        }
                    } else {
                        viewCenter = (itemInfo.getSpaceH() + left) + (itemInfo.getWidth() / 2);
                    }
                    int right = left + itemInfo.getWidth();
                    viewCenterList.add(Integer.valueOf(viewCenter));
                    viewRightList.add(Integer.valueOf(right));
                    viewLeftList.add(Integer.valueOf(left));
                    left = ((itemInfo.getWidth() / 2) + viewCenter) + getModel().getSpaceH();
                }
            }
        }
        getHeaderItem().setTimeLine(timeLineList);
        getHeaderItem().setViewCenterList(viewCenterList);
        getHeaderItem().setViewLeftList(viewLeftList);
        getHeaderItem().setViewRightList(viewRightList);
        getHeaderItem().setTimeTextMaxWidth(timeTextMaxWidth);
        getHeaderItem().setBlankSpace(blankSpace);
    }

    public boolean hasHeader() {
        return true;
    }

    public ActionPolicy getActionPolicy() {
        return new TimeLineActionPolicy(this);
    }
}
