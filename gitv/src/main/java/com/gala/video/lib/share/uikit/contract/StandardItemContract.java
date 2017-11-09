package com.gala.video.lib.share.uikit.contract;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;

public class StandardItemContract {

    public interface Presenter extends com.gala.video.lib.share.uikit.contract.ItemContract.Presenter {
        void addLiveCornerObserver();

        void removeLiveCornerObserver();

        void setPlayingGif(boolean z);

        void setView(View view);
    }

    public interface StandardItemLoaderImp {
        void loadImage(View view, ItemInfoModel itemInfoModel);

        void recycleAndShowDefaultImage();
    }

    public interface View {
        boolean isCircleNoTitleType();

        boolean isCircleTitleType();

        boolean isTitleoutType();

        void onLoadImageFail();

        void onLoadImageSuccess(Bitmap bitmap);

        void onLoadImageSuccess(Drawable drawable);

        void setDefaultImage();

        void showLiveCorner(String str);

        void updatePlayingGifUI();
    }
}
