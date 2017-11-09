package com.gala.video.lib.share.uikit.contract;

import com.gala.video.albumlist.layout.BlockLayout;
import com.gala.video.albumlist.widget.BlocksView;
import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.adapter.GroupBaseAdapter;
import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.item.Item;
import java.util.List;

public class PageContract {

    public interface View {
        BlocksView get();

        boolean isOnTop();

        void setFocusPosition(int i);

        void setLayouts(List<BlockLayout> list);
    }

    public interface Presenter {
        ActionPolicy getActionPolicy();

        GroupBaseAdapter<Item> getAdapter();

        List<CardInfoModel> getModel();

        void setView(View view);
    }
}
