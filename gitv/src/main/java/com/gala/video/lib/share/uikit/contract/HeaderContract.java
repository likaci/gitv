package com.gala.video.lib.share.uikit.contract;

import com.gala.video.lib.share.uikit.data.CardInfoModel;
import com.gala.video.lib.share.uikit.view.TextCanvas;
import java.util.List;

public interface HeaderContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.ItemContract.Presenter {
        int getBlankSpace();

        CardInfoModel getCardInfoModel();

        int getFocusPosition();

        String getSkinEndsWith();

        List<String> getTimeLine();

        int getTimeTextMaxWidth();

        TextCanvas getTips();

        String getTitle();

        List<Integer> getViewCenterList();

        List<Integer> getViewLeftList();

        List<Integer> getViewRightList();

        void setView(View view);
    }

    public interface View {
        void invalidate();

        void setTips(TextCanvas textCanvas);
    }
}
