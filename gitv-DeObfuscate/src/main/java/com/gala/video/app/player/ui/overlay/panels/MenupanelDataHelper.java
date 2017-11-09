package com.gala.video.app.player.ui.overlay.panels;

import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class MenupanelDataHelper {
    private static final String TAG = "MenupanelDataHelper";

    public boolean needScreenRatio(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> needScreenRatio, video=" + video);
        }
        boolean ret = true;
        SourceType type = video.getSourceType();
        if (type == SourceType.LIVE || type == SourceType.CAROUSEL) {
            ret = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< needScreenRatio, ret=" + ret);
        }
        return ret;
    }

    public boolean need2DTo3D(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> need2DTo3D, video=" + video);
        }
        boolean ret = false;
        if (PlayerAppConfig.isAdd2DTo3D() && shouldShow2Dto3D(video)) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< need2DTo3D, ret=" + ret);
        }
        return ret;
    }

    private boolean shouldShow2Dto3D(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> shouldShow2Dto3D, video=" + video);
        }
        boolean ret = true;
        if (video != null) {
            if (video.is3d()) {
                ret = false;
            } else if (video.getCurrentBitStream().getDefinition() == 10) {
                ret = PlayerAppConfig.isSupport2DTo3DFor4k();
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< shouldShow2Dto3D, ret=" + ret);
        }
        return ret;
    }

    private boolean needAssociativeViaSourceType(SourceType sourceType) {
        if (SourceType.CAROUSEL == sourceType || SourceType.LIVE == sourceType || SourceType.PUSH == sourceType || SourceType.BO_DAN == sourceType || SourceType.DAILY_NEWS == sourceType) {
            return false;
        }
        return true;
    }

    public boolean needEpisode(IVideo video) {
        boolean isFlowerShowRecommend;
        boolean isTrailer;
        boolean isRelated;
        boolean ret = false;
        SourceType sourceType = video.getSourceType();
        boolean showAsGallery = DataHelper.showEpisodeAsGallery(video.getAlbum());
        boolean isTvSeries = video.isTvSeries();
        boolean isNeedAssociative = needAssociativeViaSourceType(sourceType);
        if (video.getFlowerShowType() == 1 || video.getProvider().getPlaylistSource() == 1) {
            isFlowerShowRecommend = true;
        } else {
            isFlowerShowRecommend = false;
        }
        if (video.getPlaylistSource() == 4) {
            isTrailer = true;
        } else {
            isTrailer = false;
        }
        if (video.getPlaylistSource() == 5) {
            isRelated = true;
        } else {
            isRelated = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "needEpisode, isTvSeries=" + isTvSeries + ", showAsGallery=" + showAsGallery + ", isFlowerShowRecommend" + isFlowerShowRecommend + ",isTrailer" + isTrailer + " isRelated" + isRelated);
        }
        if (!(!isNeedAssociative || !isTvSeries || showAsGallery || isFlowerShowRecommend || isTrailer || isRelated)) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< needEpisode, ret=" + ret);
        }
        return ret;
    }

    public boolean needBodan(IVideo video) {
        boolean isBodan;
        boolean ret = false;
        SourceType sourceType = video.getSourceType();
        if (SourceType.BO_DAN == sourceType) {
            isBodan = true;
        } else {
            isBodan = false;
        }
        boolean isDailyNews;
        if (SourceType.DAILY_NEWS == sourceType) {
            isDailyNews = true;
        } else {
            isDailyNews = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "needBodan, sourceType=" + sourceType);
        }
        if (isBodan || isDailyNews) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> needBodan, ret=" + ret);
        }
        return ret;
    }

    public boolean needTrailers(IVideo video) {
        boolean ret = false;
        int playlistSource = video.getPlaylistSource();
        boolean isTrailers = 4 == playlistSource;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "needTrailers, sourceType=" + playlistSource);
        }
        if (isTrailers) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> needTrailers, ret=" + ret);
        }
        return ret;
    }

    public boolean needPrograms(IVideo video) {
        boolean isRecommend;
        boolean isTrailer;
        boolean isRelated;
        boolean ret = false;
        SourceType sourceType = video.getSourceType();
        boolean isSourceType = video.isSourceType();
        boolean showAsGallery = DataHelper.showEpisodeAsGallery(video.getAlbum());
        boolean isTvSeries = video.isTvSeries();
        boolean isNeedAssociative = needAssociativeViaSourceType(sourceType);
        if (video.getProvider().getPlaylistSource() == 1) {
            isRecommend = true;
        } else {
            isRecommend = false;
        }
        if (video.getPlaylistSource() == 4) {
            isTrailer = true;
        } else {
            isTrailer = false;
        }
        if (video.getPlaylistSource() == 5) {
            isRelated = true;
        } else {
            isRelated = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "needPrograms, sourceType=" + sourceType + ", isSourceType=" + isSourceType + ", isTvSeries=" + isTvSeries + ", showAsGallery=" + showAsGallery + ", isRecommend=" + isRecommend + ",isTrailer" + isTrailer + " isRelated" + isRelated);
        }
        if (isNeedAssociative && !((!isSourceType && (!isTvSeries || !showAsGallery)) || isRecommend || isTrailer || isRelated)) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> needPrograms, ret=" + ret);
        }
        return ret;
    }

    public boolean needRecommend(IVideo video) {
        boolean isRecommend = true;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> needRecommend, video=" + video);
        }
        boolean ret = false;
        SourceType sourceType = video.getSourceType();
        int channelId = video.getChannelId();
        boolean showAsGallery = DataHelper.showEpisodeAsGallery(video.getAlbum());
        boolean isNeedAssociative = needAssociativeViaSourceType(sourceType);
        if (video.getProvider().getPlaylistSource() != 1) {
            isRecommend = false;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "needRecommend, sourceType=" + sourceType + ", channelId=" + channelId + ", showAsGallery=" + showAsGallery + "isRecommend" + isRecommend);
        }
        if (isNeedAssociative && isRecommend) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< needRecommend, ret=" + ret);
        }
        return ret;
    }

    public boolean needRelated(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> needRelated, video=" + video);
        }
        boolean ret = false;
        boolean isRelated = video.getProvider().getPlaylistSource() == 5;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "needRelated," + isRelated);
        }
        if (isRelated) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< needRelated, ret=" + ret);
        }
        return ret;
    }

    public boolean needSkipHeaderTailer(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> needSkipHeaderTailer, video=" + video);
        }
        boolean ret = false;
        boolean hasHeaderTailTime = video.getHeaderTime() > 0 || video.getTailerTime() > 0;
        if (hasHeaderTailTime) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< needSkipHeaderTailer, ret=" + ret);
        }
        return ret;
    }
}
