package com.gala.video.app.epg.ui.multisubject.presenter;

import android.graphics.Bitmap;
import android.text.TextUtils;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.home.data.base.ICardModelCallback;
import com.gala.video.app.epg.ui.multisubject.imp.IMultiSubjectData;
import com.gala.video.app.epg.ui.multisubject.imp.IMultiSubjectPresenter;
import com.gala.video.app.epg.ui.multisubject.imp.IMultiSubjectView;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.uikit.utils.ImageLoader.IImageLoadCallback;
import java.util.ArrayList;

public class MultiSubjectPresenter implements IMultiSubjectPresenter {
    private String TAG = "EPG/multisubject/SubjectPresenter";
    private ArrayList<CardModel> mCardArray;
    private boolean mDestory;
    private IImageLoadCallback mImageLoadCallback = new IImageLoadCallback() {
        public void onSuccess(Bitmap bitmap) {
            MultiSubjectPresenter.this.onImageLoadCompleted(bitmap);
        }

        public void onFailed(String url) {
            MultiSubjectPresenter.this.onImageLoadCompleted(null);
        }
    };
    private IMultiSubjectView mSubjectView;
    private IMultiSubjectData mTasksRepository;

    public MultiSubjectPresenter(IMultiSubjectData tasksRepository, IMultiSubjectView statisticsView) {
        this.mTasksRepository = (IMultiSubjectData) ActivityUtils.checkNotNull(tasksRepository, "tasksRepository cannot be null");
        this.mSubjectView = (IMultiSubjectView) ActivityUtils.checkNotNull(statisticsView, "View cannot be null!");
    }

    public void fetchData(String resourceGroupID) {
        this.mTasksRepository.getData(resourceGroupID, new ICardModelCallback() {
            public void onSuccess(ArrayList<CardModel> cardArray, String tvBackgroundUrl) {
                MultiSubjectPresenter.this.onCardDataCompleteEvent(cardArray, tvBackgroundUrl);
            }

            public void onFailure(ApiException exception) {
                MultiSubjectPresenter.this.mSubjectView.showExceptionView(exception);
            }
        });
    }

    public void onDestroy() {
        this.mDestory = true;
    }

    private void onCardDataCompleteEvent(ArrayList<CardModel> cardArray, String url) {
        if (this.mDestory) {
            LogUtils.e(this.TAG, "onCardDataCompleteEvent --- return, mDestory = true");
            return;
        }
        this.mCardArray = cardArray;
        if (TextUtils.isEmpty(url)) {
            this.mSubjectView.showData(null, this.mCardArray);
        } else {
            this.mTasksRepository.loadImage(url, this.mImageLoadCallback);
        }
    }

    private void onImageLoadCompleted(Bitmap bitmap) {
        if (this.mDestory) {
            LogUtils.e(this.TAG, "onImageLoadCompleted --- return, mDestory = true");
        } else {
            this.mSubjectView.showData(bitmap, this.mCardArray);
        }
    }
}
