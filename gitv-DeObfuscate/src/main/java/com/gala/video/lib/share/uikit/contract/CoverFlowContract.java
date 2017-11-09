package com.gala.video.lib.share.uikit.contract;

import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.view.StandardItemView;
import com.gala.video.lib.share.uikit.view.widget.coverflow.OnScrollListener;
import java.util.List;

public class CoverFlowContract {

    public interface Presenter {
        void addOnScrollListener(OnScrollListener onScrollListener);

        StandardItemView createView();

        CardInfoModel getCardModel();

        int getFocusChildIndex();

        Item getItem(int i);

        List<Item> getItems();

        void removeOnScrollListener(OnScrollListener onScrollListener);

        void setCardModel(CardInfoModel cardInfoModel);

        void setItems(List<Item> list);

        void setView(View view);

        void updateAds(List<Item> list);
    }

    public interface View {
        void addOnScrollListener(OnScrollListener onScrollListener);

        int getFocusIndex();

        void notifyDataSetChange(Presenter presenter);

        void removeDelayedMessage();

        void removeOnScrollListener(OnScrollListener onScrollListener);

        void sendDelayedMessage();
    }
}
