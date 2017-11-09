package com.gala.sdk.player.data;

import com.gala.sdk.player.data.job.VideoJob;
import com.gala.sdk.player.data.job.VideoJobListener;

public interface IVideoJobFactory {
    VideoJob createEpisodeFromCacheJob(IVideo iVideo, VideoJobListener videoJobListener);

    VideoJob createFetchEpisodeJob(IVideo iVideo, VideoJobListener videoJobListener);

    VideoJob createFetchFullEpisodeJob(IVideo iVideo, VideoJobListener videoJobListener, boolean z);
}
