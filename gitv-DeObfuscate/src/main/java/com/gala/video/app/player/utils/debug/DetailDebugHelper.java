package com.gala.video.app.player.utils.debug;

import com.gala.tvapi.tv2.model.Episode;
import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.uikit.data.data.Model.ErrorEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DetailDebugHelper {
    private static final String TAG = "AlbumDetail/Debug/DetailDebugHelper";

    private DetailDebugHelper() {
    }

    public static ApiException checkForSimulatedDataError() {
        ApiException fakeException = null;
        if (DetailDebugOptions.testApiAllForE000012()) {
            fakeException = new ApiException(null, "E000012", ErrorEvent.HTTP_CODE_SUCCESS, null);
        } else if (DetailDebugOptions.testApiAllForE000054()) {
            fakeException = new ApiException(null, "E000054", ErrorEvent.HTTP_CODE_SUCCESS, null);
        } else if (DetailDebugOptions.testApiCommonForE000001()) {
            fakeException = new ApiException(null, ErrorEvent.API_CODE_FAIL_SERVICE, ErrorEvent.HTTP_CODE_SUCCESS, null);
        } else if (DetailDebugOptions.testHttpCommonErrorCode() != 0) {
            fakeException = new ApiException(null, "", String.valueOf(DetailDebugOptions.testHttpCommonErrorCode()), "http://fake.url.com");
        } else if (DetailDebugOptions.testHttpJsonFail()) {
            return new ApiException(null, "-100", ErrorEvent.HTTP_CODE_SUCCESS, "http://fake.url.com");
        }
        if (!LogUtils.mIsDebug) {
            return fakeException;
        }
        LogUtils.m1568d(TAG, "checkForSimulatedDataError: fakeException=" + fakeException);
        return fakeException;
    }

    private static int[] getTestLostEpisodes() {
        CharSequence lost = DetailDebugOptions.testEpisodeLost();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">>checkForEpisodeLostError: lost=" + lost);
        }
        if (StringUtils.isEmpty(lost)) {
            return null;
        }
        String[] epLost = lost.split(",");
        int length = epLost.length;
        int[] result = new int[length];
        int i = 0;
        while (i < length) {
            try {
                result[i] = StringUtils.parse(epLost[i], 0);
                i++;
            } catch (Exception e) {
            }
        }
        if (!LogUtils.mIsDebug) {
            return result;
        }
        LogUtils.m1568d(TAG, "checkForEpisodeLostError: return=" + result.length);
        return result;
    }

    public static void modifyForEpisodeLostError(List<Episode> episodes) {
        if (!ListUtils.isEmpty((List) episodes)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, ">>checkForEpisodeLostError: episodes=" + episodes.size());
            }
            int[] fakeLostEpisodeOrders = getTestLostEpisodes();
            if (fakeLostEpisodeOrders != null) {
                for (int fakeLostOrder : fakeLostEpisodeOrders) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(TAG, "fakeLostOrder=" + fakeLostOrder);
                    }
                    ArrayList<Episode> list = new ArrayList();
                    list.addAll(episodes);
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        Episode episode = (Episode) it.next();
                        if (episode.order == fakeLostOrder) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1568d(TAG, "remove=" + fakeLostOrder);
                            }
                            episodes.remove(episode);
                        }
                    }
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "<<checkForEpisodeLostError: episodes=" + episodes.size());
            }
        }
    }
}
