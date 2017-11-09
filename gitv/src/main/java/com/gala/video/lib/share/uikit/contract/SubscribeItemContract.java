package com.gala.video.lib.share.uikit.contract;

import android.content.Context;

public interface SubscribeItemContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.StandardItemContract.Presenter {
        void addSubscribeObserver();

        int getSubscribeType();

        void onBtnClick(CharSequence charSequence, Context context);

        void removeSubscribeObserver();

        void setView(View view);
    }

    public interface View {
        void updateBtn(int i);
    }
}
