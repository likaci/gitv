package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.uikit.contract.StandardItemContract.Presenter;

public class AllEntryItemView extends StandardItemView {
    public AllEntryItemView(Context context) {
        super(context);
    }

    protected void initUIStyle(Presenter object) {
        setStyleByName(StringUtils.append("allentry", object.getModel().getSkinEndsWith()));
    }

    protected boolean canTitleChangedToTwoLines() {
        return true;
    }
}
