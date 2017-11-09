package com.qiyi.tv.client;

import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.feature.account.AccountManager;
import com.qiyi.tv.client.feature.common.PlayParams;
import com.qiyi.tv.client.feature.history.HistoryManager;
import com.qiyi.tv.client.feature.p033a.C1989a;
import com.qiyi.tv.client.feature.p034b.C1991a;
import com.qiyi.tv.client.feature.playstate.PlayStateManager;
import com.qiyi.tv.client.feature.viprights.VipRightsManager;
import com.qiyi.tv.client.impl.Utils;
import com.qiyi.tv.client.impl.p035a.C1993a;
import com.qiyi.tv.client.impl.p035a.C1994b;
import com.qiyi.tv.client.impl.p035a.C2010l;
import com.qiyi.tv.client.impl.p035a.C2011m;
import com.qiyi.tv.client.impl.p035a.C2013n;
import com.qiyi.tv.client.impl.p035a.C2014o;
import com.qiyi.tv.client.impl.p035a.C2016p;
import com.qiyi.tv.client.impl.p035a.C2018r;
import java.util.ArrayList;
import java.util.List;

public class QiyiClient extends BaseClient {
    private static QiyiClient f2057a = new QiyiClient();
    private C1989a f2058a;
    private AccountManager f2059a;
    private C1991a f2060a;
    private HistoryManager f2061a;
    private PlayStateManager f2062a;
    private VipRightsManager f2063a;
    private C1994b f2064a;
    private C2011m f2065a;
    private C2014o f2066a;

    public static QiyiClient instance() {
        return f2057a;
    }

    private C2011m m1611a() {
        if (this.f2065a == null) {
            this.f2065a = new C2011m(this.mContext);
        }
        return this.f2065a;
    }

    private C2014o m1612a() {
        if (this.f2066a == null) {
            this.f2066a = new C2014o(this.mContext);
        }
        return this.f2066a;
    }

    private C1994b m1610a() {
        if (this.f2064a == null) {
            this.f2064a = new C1994b(this.mContext);
        }
        return this.f2064a;
    }

    private void m1613a() {
        this.f2065a = null;
        this.f2066a = null;
        this.f2059a = null;
        this.f2064a = null;
        this.f2058a = null;
        this.f2063a = null;
    }

    private void m1614b() {
        if (this.f2061a != null) {
            this.f2061a.stop();
            this.f2061a = null;
        }
    }

    private void m1615c() {
        if (this.f2060a != null) {
            this.f2060a.mo4357a();
            this.f2060a = null;
        }
    }

    private void m1616d() {
        if (this.f2062a != null) {
            this.f2062a.stop();
            this.f2062a = null;
        }
    }

    protected QiyiClient() {
    }

    protected synchronized void onInitlized() {
        super.onInitlized();
    }

    protected synchronized void onRelease() {
        m1614b();
        m1615c();
        m1616d();
        m1610a();
        super.onRelease();
    }

    protected void onConnected() {
        super.onConnected();
    }

    protected void onAuthSuccess() {
        super.onAuthSuccess();
    }

    protected void onDisconnected() {
        m1614b();
        m1615c();
        m1616d();
        m1610a();
        super.onDisconnected();
    }

    protected void onError(int code) {
        super.onError(code);
    }

    public Result<List<Channel>> getChannelList() {
        return m1610a().m1679a();
    }

    public Result<List<Media>> getChannelMedia(Channel channel, int maxCount) {
        return m1610a().m1683a(channel, maxCount);
    }

    public Result<List<Media>> getChannelMedia(Channel channel, String classTag, int maxCount) {
        return m1610a().m1684a(channel, classTag, maxCount);
    }

    public Result<List<Media>> getChannelMedia(Channel channel, List<String> filterTags, String sort, int maxCount) {
        return m1610a().m1685a(channel, filterTags, sort, maxCount);
    }

    public Result<List<Media>> getChannelRecommendedMedia(Channel channel, int maxCount) {
        return m1610a().m1692b(channel, maxCount);
    }

    public Result<List<Media>> getChannelRecommendedMediaForTab(Channel channel, int maxCount) {
        return m1610a().m1695c(channel, maxCount);
    }

    public Result<List<Media>> getRecommendation(int position) {
        return m1610a().m1677a(position);
    }

    public Result<List<Media>> getResourceMedia(String resourceId, int maxCount) {
        return m1610a().m1689a(resourceId, maxCount);
    }

    public Result<List<String>> getHotSearch() {
        return m1610a().m1691b();
    }

    public Result<List<String>> getSearchSuggestion(String keyword) {
        return m1610a().m1688a(keyword);
    }

    public Result<Media> getAlbumInfo(Media media) {
        return m1610a().m1686a(media);
    }

    public Result<Media> getMediaDetail(Media media) {
        return m1610a().m1693b(media);
    }

    public Result<List<String>> getPictureUrl(int pictureSize, ArrayList<String> urls) {
        return m1610a().m1682a(pictureSize, (ArrayList) urls);
    }

    @Deprecated
    public Result<String> getResourcePictureUrl(Media media, int pictureType) {
        return getPictureUrl(media, pictureType, 100);
    }

    public Result<String> getPictureUrl(Media media, int pictureType, int pictureSize) {
        return m1610a().m1687a(media, pictureType, pictureSize);
    }

    public int openChannel(Channel channel, String title) {
        return m1610a().m1704a(channel, title);
    }

    public int openActivatePage(String activateCode) {
        return m1610a().m1713a(activateCode);
    }

    public int openChannel(Channel channel, List<String> filterTags, String sort, String title) {
        return m1610a().m1707a(channel, (List) filterTags, sort, title);
    }

    public int openChannel(Channel channel, String classTag, String title) {
        return m1610a().m1705a(channel, classTag, title);
    }

    public int openChannel(Channel channel, List<String> filterTags, String sort, String title, int count) {
        return m1610a().m1708a(channel, filterTags, sort, title, count);
    }

    public int openChannel(Channel channel, String classTag, String title, int count) {
        return m1610a().m1706a(channel, classTag, title, count);
    }

    public int openMedia(Media media) {
        return openMedia(media, new PlayParams());
    }

    public int openMedia(Media media, PlayParams params) {
        return m1610a().m1709a(media, params);
    }

    public int playMedia(Media media) {
        return playMedia(media, null);
    }

    public int playMedia(Media media, PlayParams params) {
        return m1610a().m1710a(media, params, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public int playMedia(Media media, PlayParams params, int intentFlags) {
        return m1610a().m1710a(media, params, intentFlags);
    }

    public int playMedia(Media media, String pluginPackageName, String pluginProviderClassName) {
        return m1610a().m1712a(media, pluginPackageName, pluginProviderClassName);
    }

    public int playVrsMedia(Media media, String vid) {
        return m1610a().m1711a(media, vid);
    }

    public int openSearchResult(String keyword, int intentFlag) {
        return m1610a().m1714a(keyword, intentFlag);
    }

    public int openSearchResult(String keyword) {
        return m1610a().m1714a(keyword, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public int open(int pageType, int intentFlag) {
        return m1610a().m1701a(pageType, intentFlag);
    }

    public int open(int pageType) {
        return m1610a().m1701a(pageType, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public int open(int pageType, String title) {
        return m1610a().m1702a(pageType, title, (int) Utils.INTENT_FLAG_DEFAULT);
    }

    public AccountManager getAccountManager() {
        if (this.f2059a == null) {
            this.f2059a = new C1993a(this.mContext);
        }
        return this.f2059a;
    }

    public HistoryManager getHistoryManager() {
        if (this.f2061a == null) {
            this.f2061a = new C2013n(this.mContext);
        }
        return this.f2061a;
    }

    public PlayStateManager getPlayStateManager() {
        if (this.f2062a == null) {
            this.f2062a = new C2016p(this.mContext);
        }
        return this.f2062a;
    }

    public C1991a getFavoriteManager() {
        if (this.f2060a == null) {
            this.f2060a = new C2010l();
        }
        return this.f2060a;
    }

    public VipRightsManager getVipRightsManager() {
        if (this.f2063a == null) {
            this.f2063a = new C2018r(this.mContext);
        }
        return this.f2063a;
    }

    public Result<String> getQrCodeUrl() {
        return m1610a().m1694c();
    }

    public Result<List<Media>> getSearchMediaList(String keyword, int channelId, int maxCount) {
        return m1610a().m1690a(keyword, channelId, maxCount);
    }

    public int setPullMedia(Media media) {
        return m1610a().m1632a(media);
    }

    @Deprecated
    public int setScreenScale(boolean isFullScreen) {
        return m1610a().m1633a(isFullScreen);
    }

    public int setFullScreen(boolean isFullScreen) {
        return m1610a().m1633a(isFullScreen);
    }

    public Result<Boolean> isFullScreen() {
        return m1610a().m1634a();
    }

    public C1989a getAppInfoManager() {
        if (this.f2058a == null) {
            this.f2058a = new C1989a();
        }
        return this.f2058a;
    }

    public Result<String> getDeviceIdUrl() {
        return m1610a().m1696d();
    }

    public Result<Integer> getStreamType() {
        return m1610a().m1636b();
    }

    public int setStreamType(int type) {
        return m1610a().m1631a(type);
    }

    public Result<Boolean> isSkipHeaderTailer() {
        return m1610a().m1637c();
    }

    public int setSkipHeaderTailer(boolean isSkip) {
        return m1610a().m1635b(isSkip);
    }

    public Result<List<AppInfo>> getAppStoreAppsInfo(int category, int maxCount) {
        return m1610a().m1678a(category, maxCount);
    }

    public int openAppStoreAppDetail(AppInfo appInfo) {
        return m1610a().m1703a(appInfo);
    }

    public int openHomeTab(int type) {
        return m1610a().m1700a(type);
    }
}
