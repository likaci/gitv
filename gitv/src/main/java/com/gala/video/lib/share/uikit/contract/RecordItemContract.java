package com.gala.video.lib.share.uikit.contract;

import com.gala.video.lib.share.uikit.view.widget.record.RecordItemView;

public class RecordItemContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.ItemContract.Presenter {
        void onClick(int i, int i2);

        void updateUI(RecordItemView recordItemView);
    }
}
