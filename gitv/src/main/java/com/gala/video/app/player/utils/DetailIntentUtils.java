package com.gala.video.app.player.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.sdk.utils.performance.GlobalPerformanceTracker;
import com.gala.tv.voice.core.VoiceUtils;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.player.MultiProcAlbumDetailActivity;
import com.gala.video.app.player.MultiProcNewsDetailActivity;
import com.gala.video.app.player.NewsDetailActivity;
import com.gala.video.app.player.albumdetail.AlbumDetailActivity;
import com.gala.video.lib.framework.core.cache.LoopCache;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.IPlayerFeatureProxy.OnStateChangedListener;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.lib.share.utils.IntentUtils;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;

public class DetailIntentUtils {
    private static final String EXTRA_INDEX = "EXTRA_DETAIL_INDEX";
    public static final String EXTRA_PENDING_INTENT = "PENDING_INTENT";
    private static final String TAG = "DetailIntentUtils";
    private static String sEventId;
    private static final LoopCache<Bundle> sIntentBundle = new LoopCache(5);

    private static class MyPluginStateListener implements OnStateChangedListener {
        private Context mContext;
        private Intent mIntent;
        private boolean mIsOut;

        public MyPluginStateListener(Context context, Intent intent) {
            this.mContext = context;
            this.mIntent = intent;
        }

        public MyPluginStateListener(Context context, Intent intent, boolean isOut) {
            this.mContext = context;
            this.mIntent = intent;
            this.mIsOut = isOut;
        }

        public void onSuccess() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(DetailIntentUtils.TAG, "[PERF-LOADING]tm_plugin.load");
            }
            GlobalPerformanceTracker.instance().recordPerformanceStepEnd(DetailIntentUtils.sEventId, GlobalPerformanceTracker.PLUGIN_LOAD_STEP);
            GlobalPerformanceTracker.instance().recordPerformanceStepStart(DetailIntentUtils.sEventId, GlobalPerformanceTracker.ACTIVITY_CREATE_STEP);
            this.mIntent.addCategory("android.intent.category.DEFAULT");
            PageIOUtils.activityIn(this.mContext, this.mIntent);
            if (this.mIsOut) {
                ((Activity) this.mContext).finish();
            }
        }

        public void onFailed() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(DetailIntentUtils.TAG, "[PERF-LOADING] playerplugin.Load onFailed");
            }
        }

        public void onCanceled() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(DetailIntentUtils.TAG, "onCanceled");
            }
            if (this.mIsOut) {
                ((Activity) this.mContext).finish();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(DetailIntentUtils.TAG, "finish");
                }
            }
        }

        public void onLoading() {
            if (LogUtils.mIsDebug) {
                LogUtils.d(DetailIntentUtils.TAG, "onLoading");
            }
        }
    }

    private DetailIntentUtils() {
    }

    public static void setDefaultTabSource(String from) {
        if (StringUtils.equals(from, "openAPI")) {
            PingBackUtils.setTabSrc("其他");
        }
    }

    private static void startAlbumDetailInner(Context context, Album album, PlayParams params, boolean isComplete, String from, String buySource, String tabSource, boolean clearTaskFlag, boolean continueNextVideo) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> startAlbumDetailInner context=" + context + JsonBundleConstants.A71_TRACKING_PARAMS + params + ", from=" + from + ", isComplete=" + isComplete + ", buySource=" + buySource + ", tabSource=" + tabSource + clearTaskFlag + continueNextVideo + ", album=" + DataUtils.albumInfoToString(album));
        }
        Intent intent = new Intent();
        String className = getAlbumDetailActivityClass().getName();
        setDefaultTabSource(from);
        intent.setFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
        if (clearTaskFlag) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "add clear task flag");
            }
            intent.addFlags(32768);
        }
        sEventId = PingBackUtils.createEventId();
        if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            intent.putExtra("albumInfo", album);
            intent.putExtra("play_list_info", params);
            intent.putExtra("from", from);
            intent.putExtra("continue_play_next_video", continueNextVideo);
            intent.putExtra("buy_source", buySource);
            intent.putExtra(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            intent.putExtra("eventId", sEventId);
            intent.putExtra(IntentConfig2.INTENT_PARAM_ALBUM_INFO_COMPLETE, isComplete);
            recordCallTimeAndDelayPingback(intent.getExtras());
            PlayerUtils.fillOutsideIntentInfoIfNeed(intent, album, from);
            recordCallTimeAndDelayPingback(intent.getExtras());
        } else {
            Bundle extras = wrapIntentExtras(intent);
            extras.putSerializable("albumInfo", album);
            extras.putSerializable("play_list_info", params);
            extras.putSerializable("continue_play_next_video", Boolean.valueOf(continueNextVideo));
            extras.putString("from", from);
            extras.putString("buy_source", buySource);
            extras.putString(IntentConfig2.INTENT_PARAM_TAB_SOURCE, tabSource);
            extras.putString("eventId", sEventId);
            extras.putBoolean(IntentConfig2.INTENT_PARAM_ALBUM_INFO_COMPLETE, isComplete);
            PlayerUtils.fillOutsideInfoIfNeed(extras, album, from);
            recordCallTimeAndDelayPingback(extras);
        }
        intent.setAction(IntentUtils.getActionName(className));
        GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
    }

    public static void startDetailForLoop(Context context, int channelId, int loopPosition, String from, String channelName) {
        if (context != null && channelId >= 0) {
            LogUtils.e(TAG, ">>startDetailForLoop4 context=" + context + ", channelId = " + channelId + ", loopPosition" + loopPosition + ",channelName" + channelName + ", from=" + from);
            Intent intent = new Intent();
            if (!(context instanceof Activity)) {
                intent.setFlags(VoiceUtils.INTENT_FLAG_DEFAULT);
            }
            sEventId = PingBackUtils.createEventId();
            intent.setClass(context, getNewsDetailActivityClass());
            if (Project.getInstance().getBuild().supportPlayerMultiProcess()) {
                intent.putExtra(IntentConfig2.INTENT_PARAM_COMPONENT_NAME, getNewsDetailActivityClass().getName());
                intent.putExtra("from", from);
                intent.putExtra("videoType", SourceType.DAILY_NEWS);
                intent.putExtra("channelName", channelName);
                intent.putExtra("eventId", sEventId);
                recordCallTimeAndDelayPingback(intent.getExtras());
            } else {
                Bundle extras = wrapIntentExtras(intent);
                extras.putString("from", from);
                extras.putSerializable("videoType", SourceType.DAILY_NEWS);
                extras.putString("channelName", channelName);
                extras.putString("eventId", sEventId);
                recordCallTimeAndDelayPingback(extras);
            }
            GetInterfaceTools.getPlayerFeatureProxy().loadPlayerFeatureAsync(context, new MyPluginStateListener(context, intent), true);
        }
    }

    private static void recordCallTimeAndDelayPingback(Bundle extras) {
        extras.putLong(PlayerIntentConfig2.PERFORMANCE_PAGE_CALL, GlobalPerformanceTracker.instance().recordRoutineStart("pageInitToStarted"));
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "[PERF-LOADING]page.click");
        }
        GlobalPerformanceTracker.instance().recordPerformanceStepStart(sEventId, GlobalPerformanceTracker.PLUGIN_LOAD_STEP);
    }

    private static Bundle wrapIntentExtras(Intent intent) {
        Bundle bundle = new Bundle();
        int index = sIntentBundle.add(bundle);
        intent.putExtra(EXTRA_INDEX, index);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "wrapIntentExtras(), index=" + index);
        }
        return bundle;
    }

    public static Class<?> getAlbumDetailActivityClass() {
        return Project.getInstance().getBuild().supportPlayerMultiProcess() ? MultiProcAlbumDetailActivity.class : AlbumDetailActivity.class;
    }

    private static Class<?> getNewsDetailActivityClass() {
        return Project.getInstance().getBuild().supportPlayerMultiProcess() ? MultiProcNewsDetailActivity.class : NewsDetailActivity.class;
    }
}
