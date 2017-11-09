package com.gala.video.lib.share.uikit.contract;

import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.adapter.GroupBaseAdapter;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.Item;

public class HScrollContract {

    public interface Presenter {
        ActionPolicy getActionPolicy();

        GroupBaseAdapter<Item> getAdapter();

        CardInfoModel getCardModel();

        void setFocusPosition(int i);

        void setView(View view);
    }

    public interface View {
        void setFocusPosition(int i);
    }
}
