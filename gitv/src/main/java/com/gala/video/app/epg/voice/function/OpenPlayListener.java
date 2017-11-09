package com.gala.video.app.epg.voice.function;

import android.content.Context;
import com.gala.tv.voice.VoiceEvent;
import com.gala.tv.voice.VoiceEventFactory;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.tv.voice.service.VoiceEventParser;
import com.gala.tv.voice.service.VoiceManager;
import com.gala.tvapi.tv2.TVApi;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.ChnList;
import com.gala.tvapi.tv2.model.Episode;
import com.gala.tvapi.tv2.result.ApiResultAlbumList;
import com.gala.tvapi.tv2.result.ApiResultEpisodeList;
import com.gala.video.api.ApiException;
import com.gala.video.api.IApiCallback;
import com.gala.video.app.epg.voice.utils.EntryUtils;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.voice.VoiceListener;
import java.util.ArrayList;
import java.util.List;

public class OpenPlayListener extends VoiceListener {
    private static final String TAG = "OpenPlayListener";

    private class MyEpisodeListener implements IApiCallback<ApiResultEpisodeList> {
        private ApiResultEpisodeList mResult;

        private MyEpisodeListener() {
        }

        public void onException(ApiException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(OpenPlayListener.TAG, "onException()", e);
            }
            this.mResult = null;
        }

        public void onSuccess(ApiResultEpisodeList result) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(OpenPlayListener.TAG, "onSuccess() result=" + result);
            }
            this.mResult = result;
        }

        public Album getVideo(int episodeIndex) {
            Album video = null;
            if (this.mResult != null) {
                List<Episode> episodeList = this.mResult.data;
                if (episodeList != null) {
                    for (Episode episode : episodeList) {
                        if (episode.order == episodeIndex) {
                            video = new Album(episode);
                            break;
                        }
                    }
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(OpenPlayListener.TAG, "getVideo() return=" + video);
            }
            return video;
        }
    }

    private static class MyListener implements IApiCallback<ApiResultAlbumList> {
        private ApiResultAlbumList mResult;

        private MyListener() {
        }

        public ApiResultAlbumList getAlbumList() {
            return this.mResult;
        }

        public void onException(ApiException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(OpenPlayListener.TAG, "onException()", e);
            }
            this.mResult = null;
        }

        public void onSuccess(ApiResultAlbumList result) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(OpenPlayListener.TAG, "onSuccess() result=" + result);
            }
            this.mResult = result;
        }

        public static String getChannelId(ApiResultAlbumList list, String channelName) {
            if (StringUtils.isTrimEmpty(channelName) || list == null || list.chnList == null) {
                return null;
            }
            for (ChnList channel : list.chnList) {
                if (channelName.equals(channel.chnName)) {
                    return channel.chnId + "";
                }
            }
            return null;
        }
    }

    public OpenPlayListener(Context context, int priority) {
        super(context, priority);
    }

    protected List<AbsVoiceAction> doOpenAction() {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "OpenPlayHelper/getSupportedVoices()/TYPE_PLAY");
        }
        List<AbsVoiceAction> actions = new ArrayList();
        try {
            actions.add(new AbsVoiceAction(VoiceEventFactory.createVoiceEvent(16, "")) {
                protected boolean dispatchVoiceEvent(VoiceEvent event) {
                    PingBackUtils.setTabSrc("其他");
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(OpenPlayListener.TAG, "OpenPlayHelper/dispatchVoiceEvent()/TYPE_PLAY");
                    }
                    return OpenPlayListener.this.dispatchVoiceEvent(event);
                }
            });
        } catch (Exception e) {
            LogUtils.e(TAG, "OpenPlayHelper/getSupportedVoices()/TYPE_PLAY Exception e = " + e.getMessage());
            e.printStackTrace();
        }
        return actions;
    }

    private boolean dispatchVoiceEvent(final VoiceEvent event) {
        boolean handled = false;
        if (!(event == null || event.getType() != 16 || StringUtils.isTrimEmpty(event.getKeyword()))) {
            handled = true;
            new Thread(new Runnable() {
                public void run() {
                    String keyword = event.getKeyword();
                    String channelName = VoiceEventParser.parseChannelName(event);
                    int episodeIndex = VoiceEventParser.parseEpisodeIndex(event);
                    if (StringUtils.isTrimEmpty(channelName) && episodeIndex <= 0) {
                        OpenPlayListener.this.playNomal(keyword);
                    } else if (StringUtils.isTrimEmpty(channelName)) {
                        OpenPlayListener.this.playByEpisodeIndex(keyword, episodeIndex);
                    } else if (episodeIndex <= 0) {
                        OpenPlayListener.this.playByChannelName(keyword, channelName);
                    } else {
                        OpenPlayListener.this.playByChannelAndEpisode(keyword, channelName, episodeIndex);
                    }
                }
            }).start();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "dispatchVoiceEvent(return=" + handled + ")");
        }
        return handled;
    }

    private boolean playNomal(String keyword) {
        boolean handled = false;
        ApiResultAlbumList result = search(keyword, null);
        if (!(result == null || result.getAlbumList() == null || result.getAlbumList().size() <= 0)) {
            EntryUtils.startPlayerActivity(VoiceManager.instance().getSmartContext(), (Album) result.getAlbumList().get(0), "openAPI");
            handled = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "playNomal(return=" + handled + ")");
        }
        return handled;
    }

    private boolean playByEpisodeIndex(String keyword, int episodeIndex) {
        boolean handled = false;
        ApiResultAlbumList result = search(keyword, "2");
        if (!(result == null || result.getAlbumList() == null || result.getAlbumList().size() <= 0)) {
            Album episode = (Album) result.getAlbumList().get(0);
            if (episode != null && episode.tvCount >= episodeIndex) {
                Album episodeItem = fetchEpisodeItem(episode, episodeIndex);
                if (episodeItem != null) {
                    EntryUtils.startPlayerActivity(VoiceManager.instance().getSmartContext(), episodeItem, "openAPI");
                    handled = true;
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "playByEpisodeIndex(return=" + handled + ")");
        }
        return handled;
    }

    private boolean playByChannelName(String keyword, String channelName) {
        boolean handled = false;
        ApiResultAlbumList result = search(keyword, null);
        if (result != null) {
            String channelId = MyListener.getChannelId(result, channelName);
            if (!StringUtils.isTrimEmpty(channelId)) {
                result = search(keyword, channelId);
                if (!(result == null || result.getAlbumList() == null || result.getAlbumList().size() <= 0)) {
                    EntryUtils.startPlayerActivity(VoiceManager.instance().getSmartContext(), (Album) result.getAlbumList().get(0), "openAPI");
                    handled = true;
                }
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "playByChannelName(return=" + handled + ")");
        }
        return handled;
    }

    private boolean playByChannelAndEpisode(String keyword, String channelName, int episodeIndex) {
        boolean handled = false;
        ApiResultAlbumList result = search(keyword, null);
        if (result != null) {
            String channelId = MyListener.getChannelId(result, channelName);
            if (!StringUtils.isTrimEmpty(channelId) && channelId.equals("2")) {
                handled = playByEpisodeIndex(keyword, episodeIndex);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "playByChannelAndEpisode(return=" + handled + ")");
        }
        return handled;
    }

    private ApiResultAlbumList search(String value, String channelId) {
        MyListener listener = new MyListener();
        TVApi.albumSearch.callSync(listener, value, channelId, "1", "20");
        return listener.getAlbumList();
    }

    private Album fetchEpisodeItem(Album episode, int episodeIndex) {
        int pageNo = episodeIndex / 10;
        if (episodeIndex % 10 != 0) {
            pageNo++;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fetchEpisodeItem(episode=" + episode + ", index=" + episodeIndex + ")");
        }
        MyEpisodeListener listener = new MyEpisodeListener();
        TVApi.episodeList.callSync(listener, episode.qpId, "", "0", String.valueOf(pageNo), String.valueOf(10));
        Album episodeItem = listener.getVideo(episodeIndex);
        if (episodeItem != null) {
            episodeItem.qpId = episode.qpId;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "fetchEpisodeItem() return " + episode);
        }
        return episodeItem;
    }
}
