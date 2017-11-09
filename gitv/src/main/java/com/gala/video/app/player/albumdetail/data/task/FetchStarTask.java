package com.gala.video.app.player.albumdetail.data.task;

import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Star;
import com.gala.tvapi.tv2.result.ApiResultStars;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.utils.AlbumTextHelper;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import java.util.ArrayList;
import java.util.List;

public class FetchStarTask {
    private static final int MAX_STARS_COUNT = 30;
    private static final String TAG = "FetchStarTask";
    private AlbumInfo mAlbumInfo;
    private IFetchStarTaskListener mListener;

    public interface IFetchStarTaskListener {
        void onFailed(ApiException apiException);

        void onSuccess(List<Star> list);
    }

    private class StarApiCallback implements IApiCallback<ApiResultStars> {
        private StarApiCallback() {
        }

        public void onSuccess(ApiResultStars apiResultStars) {
            LogRecordUtils.logd(FetchStarTask.TAG, ">> stars.onSuccess");
            if (apiResultStars == null) {
                LogRecordUtils.loge(FetchStarTask.TAG, "stars.onSuccess, result is null.");
            } else if (ListUtils.isEmpty(apiResultStars.data)) {
                LogRecordUtils.loge(FetchStarTask.TAG, "stars.onSuccess, result.data is empty.");
            } else {
                List<Star> stars = apiResultStars.data;
                LogRecordUtils.logd(FetchStarTask.TAG, "stars.onSuccess, result data size=" + stars.size());
                FetchStarTask.this.mListener.onSuccess(stars);
                LogRecordUtils.logd(FetchStarTask.TAG, "<< stars.onSuccess");
            }
        }

        public void onException(ApiException e) {
            LogRecordUtils.logd(FetchStarTask.TAG, ">> stars.onException");
            FetchStarTask.this.mListener.onFailed(e);
        }
    }

    public FetchStarTask(AlbumInfo albumInfo) {
        this.mAlbumInfo = albumInfo;
    }

    public void setTaskListener(IFetchStarTaskListener listener) {
        this.mListener = listener;
    }

    public void execute() {
        List<String> corrected = correctStarIDList(AlbumTextHelper.getStarIDList(this.mAlbumInfo));
        if (corrected.size() > 30) {
            corrected = corrected.subList(0, 30);
        }
        LogRecordUtils.logd(TAG, ">> onRun, corrected=" + corrected);
        CharSequence idString = AlbumTextHelper.convertListToString(corrected, ",");
        LogRecordUtils.logd(TAG, ">> onRun, strStars=" + idString);
        if (StringUtils.isEmpty(idString)) {
            this.mListener.onFailed(null);
            return;
        }
        TVApi.stars.callSync(new StarApiCallback(), idString);
    }

    private List<String> correctStarIDList(List<String> list) {
        LogRecordUtils.logd(TAG, ">> correctStarIDList, list=" + list);
        List<String> corrected = new ArrayList();
        for (String each : list) {
            if (!corrected.contains(each)) {
                corrected.add(each);
            }
        }
        LogRecordUtils.logd(TAG, "<< correctStarIDList, list=" + corrected);
        return corrected;
    }
}
