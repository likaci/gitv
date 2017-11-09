package com.gala.video.lib.share.uikit.item;

import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.common.configs.ApiConstants;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.adapter.CoverFlowAdapter;
import com.gala.video.lib.share.uikit.contract.CoverFlowContract.Presenter;
import com.gala.video.lib.share.uikit.contract.CoverFlowContract.View;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.utils.LogUtils;
import com.gala.video.lib.share.uikit.view.StandardItemView;
import com.gala.video.lib.share.uikit.view.widget.coverflow.OnScrollListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CoverFlowItem extends Item implements Presenter {
    private static final String TAG = "CoverFlowItem";
    private CoverFlowAdapter mCoverFlowAdapter = new CoverFlowAdapter(AppRuntimeEnv.get().getApplicationContext());
    private CardInfoModel mModel;
    private View mView;

    public void setItems(List<Item> items) {
        this.mCoverFlowAdapter.clear();
        this.mCoverFlowAdapter.addAllItems(items);
    }

    public List<Item> getItems() {
        return this.mCoverFlowAdapter.getItems();
    }

    public Item getItem(int position) {
        return this.mCoverFlowAdapter.getItem(position);
    }

    public void setView(View view) {
        this.mView = view;
    }

    public View getView() {
        return this.mView;
    }

    public int getFocusChildIndex() {
        if (this.mView == null) {
            return -1;
        }
        return this.mView.getFocusIndex();
    }

    public void setCardModel(CardInfoModel model) {
        this.mModel = model;
    }

    public CardInfoModel getCardModel() {
        return this.mModel;
    }

    public int getWidth() {
        return this.mModel.getWidth();
    }

    public int getHeight() {
        return this.mModel.getBodyHeight();
    }

    public int getType() {
        return ApiConstants.IMAGE_CORNER_SIZE_219;
    }

    public void updateAds(List<Item> adItemList) {
        List curItemList = new ArrayList();
        curItemList.addAll(getItems());
        if (isHasAdItem(curItemList)) {
            LogUtils.d(TAG, "CoverFlowItem@" + hashCode() + ",updateAds, !isHasAdItem(curItemList) = false ,current coverflowcard item size : " + ListUtils.getCount(curItemList));
            List<Integer> adItemIndexList = new ArrayList();
            Iterator<Item> curItemListIterator = curItemList.iterator();
            while (curItemListIterator.hasNext()) {
                Item item = (Item) curItemListIterator.next();
                if (ItemDataType.FOCUS_IMAGE_AD.equals(item.getModel().getActionModel().getItemType())) {
                    adItemIndexList.add(Integer.valueOf(curItemList.indexOf(item)));
                    curItemListIterator.remove();
                }
            }
            boolean hasAdsBeFiltered = false;
            if (adItemIndexList.size() > 0 && adItemIndexList.contains(Integer.valueOf(0))) {
                hasAdsBeFiltered = true;
                LogUtils.d(TAG, "CoverFlowCard@" + hashCode() + ", hasAdsBeFiltered = true");
            }
            LogUtils.d(TAG, "CoverFlowCard@" + hashCode());
            for (Integer position : adItemIndexList) {
                removeAdItem(position.intValue());
            }
            addAdItem(adItemList, hasAdsBeFiltered);
        } else {
            LogUtils.d(TAG, "CoverFlowItem@" + hashCode() + ", updateAds, (curItemList) = true");
            addAdItem(adItemList, false);
        }
        notifyDataSetChange();
    }

    private boolean isHasAdItem(List<Item> list) {
        for (Item item : list) {
            if (ItemDataType.FOCUS_IMAGE_AD.equals(item.getModel().getActionModel().getItemType())) {
                return true;
            }
        }
        return false;
    }

    public StandardItemView createView() {
        return this.mCoverFlowAdapter.getView();
    }

    public void addOnScrollListener(OnScrollListener onScrollListener) {
        if (this.mView != null) {
            this.mView.addOnScrollListener(onScrollListener);
        }
    }

    public void removeOnScrollListener(OnScrollListener onScrollListener) {
        if (this.mView != null) {
            this.mView.removeOnScrollListener(onScrollListener);
        }
    }

    private void removeAdItem(int position) {
        this.mCoverFlowAdapter.remove(position);
    }

    private void addAdItem(List<Item> adItemList, boolean hasAdsBeFiltered) {
        this.mCoverFlowAdapter.addAdItem(adItemList, hasAdsBeFiltered);
    }

    private void notifyDataSetChange() {
        if (this.mView != null) {
            this.mView.notifyDataSetChange(this);
        }
    }
}
