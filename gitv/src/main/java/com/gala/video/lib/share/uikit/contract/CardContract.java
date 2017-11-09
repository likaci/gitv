package com.gala.video.lib.share.uikit.contract;

import com.gala.video.lib.share.uikit.actionpolicy.ActionPolicy;
import com.gala.video.lib.share.uikit.data.CardInfoModel;

public class CardContract {

    public interface Presenter {
        ActionPolicy getActionPolicy();

        CardInfoModel getModel();
    }
}
