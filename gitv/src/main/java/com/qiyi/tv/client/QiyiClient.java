package com.qiyi.tv.client;

import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.a.a;
import com.qiyi.tv.client.feature.account.AccountManager;
import com.qiyi.tv.client.feature.common.PlayParams;
import com.qiyi.tv.client.feature.history.HistoryManager;
import com.qiyi.tv.client.feature.playstate.PlayStateManager;
import com.qiyi.tv.client.feature.viprights.VipRightsManager;
import com.qiyi.tv.client.impl.Utils;
import com.qiyi.tv.client.impl.a.b;
import com.qiyi.tv.client.impl.a.l;
import com.qiyi.tv.client.impl.a.m;
import com.qiyi.tv.client.impl.a.n;
import com.qiyi.tv.client.impl.a.o;
import com.qiyi.tv.client.impl.a.p;
import com.qiyi.tv.client.impl.a.r;
import java.util.ArrayList;
import java.util.List;

public class QiyiClient extends BaseClient {
    private static QiyiClient a = new QiyiClient();
    private a f841a;
    private AccountManager f842a;
    private com.qiyi.tv.client.feature.b.a f843a;
    private HistoryManager f844a;
    private PlayStateManager f845a;
    private VipRightsManager f846a;
    private b f847a;
    private m f848a;
    private o f849a;

    public static QiyiClient instance() {
        return a;
    }

    private m m218a() {
        if (this.f848a == null) {
            this.f848a = new m(this.mContext);
        }
        return this.f848a;
    }

    private o m219a() {
        if (this.f849a == null) {
            this.f849a = new o(this.mContext);
        }
        return this.f849a;
    }

    private b a() {
        if (this.f847a == null) {
            this.f847a = new b(this.mContext);
        }
        return this.f847a;
    }

    private void m220a() {
        this.f848a = null;
        this.f849a = null;
        this.f842a = null;
        this.f847a = null;
        this.f841a = null;
        this.f846a = null;
    }

    private void b() {
        if (this.f844a != null) {
            this.f844a.stop();
            this.f844a = null;
        }
    }

    private void c() {
        if (this.f843a != null) {
            this.f843a.a();
            this.f843a = null;
        }
    }

    private void d() {
        if (this.f845a != null) {
            this.f845a.stop();
            this.f845a = null;
        }
    }

    protected QiyiClient() {
    }

    protected synchronized void onInitlized() {
        super.onInitlized();
    }

    protected synchronized void onRelease() {
        b();
        c();
        d();
        a();
        super.onRelease();
    }

    protected void onConnected() {
        super.onConnected();
    }

    protected void onAuthSuccess() {
        super.onAuthSuccess();
    }

    protected void onDisconnected() {
        b();
        c();
        d();
        a();
        super.onDisconnected();
    }

    protected void onError(int code) {
        super.onError(code);
    }

    public Result<List<Channel>> getChannelList() {
        return a().a();
    }

    public Result<List<Media>> getChannelMedia(Channel channel, int maxCount) {
        return a().a(channel, maxCount);
    }

    public Result<List<Media>> getChannelMedia(Channel channel, String classTag, int maxCount) {
        return a().a(channel, classTag, maxCount);
    }

    public Result<List<Media>> getChannelMedia(Channel channel, List<String> filterTags, String sort, int maxCount) {
        return a().a(channel, filterTags, sort, maxCount);
    }

    public Result<List<Media>> getChannelRecommendedMedia(Channel channel, int maxCount) {
        return a().b(channel, maxCount);
    }

    public Result<List<Media>> getChannelRecommendedMediaForTab(Channel channel, int maxCount) {
        return a().c(channel, maxCount);
    }

    public Result<List<Media>> getRecommendation(int position) {
        return a().a(position);
    }

    public Result<List<Media>> getResourceMedia(String resourceId, int maxCount) {
        return a().a(resourceId, maxCount);
    }

    public Result<List<String>> getHotSearch() {
        return a().b();
    }

    public Result<List<String>> getSearchSuggestion(String keyword) {
        return a().a(keyword);
    }

    public Result<Media> getAlbumInfo(Media media) {
        return a().a(media);
    }

    public Result<Media> getMediaDetail(Media media) {
        return a().b(media);
    }

    public Result<List<String>> getPictureUrl(int pictureSize, ArrayList<String> urls) {
        return a().a(pictureSize, (ArrayList) urls);
    }

    @Deprecated
    public Result<String> getResourcePictureUrl(Media media, int pictureType) {
        return getPictureUrl(media, pictureType, 100);
    }

    public Result<String> getPictureUrl(Media media, int pictureType, int pictureSize) {
        return a().a(media, pictureType, pictureSize);
    }

    public int openChannel(Channel channel, String title) {
        return a().a(channel, title);
    }

    public int openActivatePage(String activateCode) {
        return a().a(activateCode);
    }

    public int openChannel(Channel channel, List<String> filterTags, String sort, String title) {
        return a().a(channel, (List) filterTags, sort, title);
    }

    public int openChannel(Channel channel, String classTag, String title) {
        return a().a(channel, classTag, title);
    }

    public int openChannel(Channel channel, List<String> filterTags, String sort, String title, int count) {
        return a().a(channel, filterTags, sort, title, count);
    }

    public int openChannel(Channel channel, String classTag, String title, int count) {
        return a().a(channel, classTag, title, count);
    }

    public int openMedia(Media media) {
        return openMedia(media, new PlayParams());
    }

    public int openMedia(Media media, PlayParams params) {
        return a().a(media, params);
    }

    public int playMedia(Media media) {
        return playMedia(media, null);
    }

    public int playMedia(Media media, PlayParams params) {
        return a().a(media, params, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public int playMedia(Media media, PlayParams params, int intentFlags) {
        return a().a(media, params, intentFlags);
    }

    public int playMedia(Media media, String pluginPackageName, String pluginProviderClassName) {
        return a().a(media, pluginPackageName, pluginProviderClassName);
    }

    public int playVrsMedia(Media media, String vid) {
        return a().a(media, vid);
    }

    public int openSearchResult(String keyword, int intentFlag) {
        return a().a(keyword, intentFlag);
    }

    public int openSearchResult(String keyword) {
        return a().a(keyword, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public int open(int pageType, int intentFlag) {
        return a().a(pageType, intentFlag);
    }

    public int open(int pageType) {
        return a().a(pageType, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public int open(int pageType, String title) {
        return a().a(pageType, title, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public AccountManager getAccountManager() {
        if (this.f842a == null) {
            this.f842a = new com.qiyi.tv.client.impl.a.a(this.mContext);
        }
        return this.f842a;
    }

    public HistoryManager getHistoryManager() {
        if (this.f844a == null) {
            this.f844a = new n(this.mContext);
        }
        return this.f844a;
    }

    public PlayStateManager getPlayStateManager() {
        if (this.f845a == null) {
            this.f845a = new p(this.mContext);
        }
        return this.f845a;
    }

    public com.qiyi.tv.client.feature.b.a getFavoriteManager() {
        if (this.f843a == null) {
            this.f843a = new l();
        }
        return this.f843a;
    }

    public VipRightsManager getVipRightsManager() {
        if (this.f846a == null) {
            this.f846a = new r(this.mContext);
        }
        return this.f846a;
    }

    public Result<String> getQrCodeUrl() {
        return a().c();
    }

    public Result<List<Media>> getSearchMediaList(String keyword, int channelId, int maxCount) {
        return a().a(keyword, channelId, maxCount);
    }

    public int setPullMedia(Media media) {
        return a().a(media);
    }

    @Deprecated
    public int setScreenScale(boolean isFullScreen) {
        return a().a(isFullScreen);
    }

    public int setFullScreen(boolean isFullScreen) {
        return a().a(isFullScreen);
    }

    public Result<Boolean> isFullScreen() {
        return a().a();
    }

    public a getAppInfoManager() {
        if (this.f841a == null) {
            this.f841a = new a();
        }
        return this.f841a;
    }

    public Result<String> getDeviceIdUrl() {
        return a().d();
    }

    public Result<Integer> getStreamType() {
        return a().b();
    }

    public int setStreamType(int type) {
        return a().a(type);
    }

    public Result<Boolean> isSkipHeaderTailer() {
        return a().c();
    }

    public int setSkipHeaderTailer(boolean isSkip) {
        return a().b(isSkip);
    }

    public Result<List<AppInfo>> getAppStoreAppsInfo(int category, int maxCount) {
        return a().a(category, maxCount);
    }

    public int openAppStoreAppDetail(AppInfo appInfo) {
        return a().a(appInfo);
    }

    public int openHomeTab(int type) {
        return a().a(type);
    }
}
