package com.gala.sdk.player;

import android.content.Context;
import com.gala.sdk.player.data.IFetchEpisodeTaskFactory;
import com.gala.sdk.player.data.IFetchPlayListBySourceTaskFactory;
import com.gala.sdk.player.data.IFetchRecommendListTaskFactory;
import com.gala.sdk.player.data.IVideoItemFactory;
import com.gala.sdk.player.data.IVideoJobFactory;

public interface IPlayerFeature {
    public static final int FEATURE_GALAVIDEOPLAYER = 1;
    public static final int INIT_COMPONENT_FULL = 100;
    public static final int INIT_COMPONENT_JAVA = 101;

    void enableHCDNPreDeploy(boolean z);

    AdCacheManager getAdCacheManager();

    IFetchEpisodeTaskFactory getFetchEpisodeTaskFactory();

    IFetchPlayListBySourceTaskFactory getFetchPlayListBySourceTaskFactory();

    IFetchRecommendListTaskFactory getFetchRecommendListTaskFactory();

    IGalaVideoPlayerFactory getGalaVideoPlayerFactory();

    IPlayerLogProviderFactory getPlayerLogProviderFactory();

    String getPlayerModulesVersion();

    int getPlayerTypeConfig(int i, boolean z, boolean z2, boolean z3);

    IVideoItemFactory getVideoItemFactory();

    IVideoJobFactory getVideoJobFactory();

    void initialize(Context context, IPlayerProfile iPlayerProfile);

    void initialize(Context context, IPlayerProfile iPlayerProfile, int i);

    boolean isSupportDolby();

    boolean isSupportH211();

    void setHCDNCleanAvailable(boolean z);

    void setIntertrustDrmModulePath(String str);

    void updateAuthorization(String str);

    void updateDeviceCheckInfo(String str, String str2);
}
