package com.gala.video.app.player.pingback;

import com.gala.pingback.IPingbackFactory;
import com.gala.video.app.player.pingback.common.BottomEpisodeClickPingback;
import com.gala.video.app.player.pingback.common.BottomRecommendClickPingback;
import com.gala.video.app.player.pingback.common.CardPageShowPingback;
import com.gala.video.app.player.pingback.common.EpisodeClickPingback;
import com.gala.video.app.player.pingback.common.EpisodePageShowPingback;
import com.gala.video.app.player.pingback.common.GuessYouLikeShowPingback;
import com.gala.video.app.player.pingback.common.GuessYoulikeClickPingback;
import com.gala.video.app.player.pingback.common.InitPingback;
import com.gala.video.app.player.pingback.common.MenuPanelBitStreamClickPingback;
import com.gala.video.app.player.pingback.common.PageExitPingback;
import com.gala.video.app.player.pingback.common.RecommendClickPingback;
import com.gala.video.app.player.pingback.detail.CarPageClickPingback;
import com.gala.video.app.player.pingback.detail.CustomerDetailExitPingback;
import com.gala.video.app.player.pingback.detail.CustomerDetailLoadedPingback;
import com.gala.video.app.player.pingback.detail.DetailBuyClickPingback;
import com.gala.video.app.player.pingback.detail.DetailFavClickPingback;
import com.gala.video.app.player.pingback.detail.DetailPageShowPingback;
import com.gala.video.app.player.pingback.detail.DetailPlayBtnClickPingback;
import com.gala.video.app.player.pingback.detail.PlayWindowClickPingback;
import com.gala.video.app.player.pingback.detail.StarPageShowPingback;
import com.gala.video.app.player.pingback.detail.SummaryClickPingback;
import com.gala.video.app.player.pingback.detail.SuperAlbumClickPingback;
import com.gala.video.app.player.pingback.detail.SuperAlbumShowPingback;
import com.gala.video.app.player.pingback.exitdialog.ExitDialogPageClickedPingback;
import com.gala.video.app.player.pingback.exitdialog.ExitDialogPageShowPingback;
import com.gala.video.app.player.pingback.player.BitStreamAdPingback;
import com.gala.video.app.player.pingback.player.CarouselChannelClickPingback;
import com.gala.video.app.player.pingback.player.CarouselChannelShowPingback;
import com.gala.video.app.player.pingback.player.CarouselInfoPingback;
import com.gala.video.app.player.pingback.player.CarouselProgrammeClickPingback;
import com.gala.video.app.player.pingback.player.CarouselProgrammeShowPingback;
import com.gala.video.app.player.pingback.player.DailyInfoShowPingback;
import com.gala.video.app.player.pingback.player.DailyinfoClickPingback;
import com.gala.video.app.player.pingback.player.EpisodeAdPingback;
import com.gala.video.app.player.pingback.player.HDRGuideClickPingback;
import com.gala.video.app.player.pingback.player.HDRGuidePageShowPingback;
import com.gala.video.app.player.pingback.player.KeyeventPingback;
import com.gala.video.app.player.pingback.player.LiveInteractionPingback;
import com.gala.video.app.player.pingback.player.LoginTipClickPingback;
import com.gala.video.app.player.pingback.player.LoginTipShowPingback;
import com.gala.video.app.player.pingback.player.MenuProgramShowPingback;
import com.gala.video.app.player.pingback.player.MenupanelAdPingback;
import com.gala.video.app.player.pingback.player.MenupanelShowPingback;
import com.gala.video.app.player.pingback.player.NewsItemPageClickPingback;
import com.gala.video.app.player.pingback.player.PageLoadPingback;
import com.gala.video.app.player.pingback.player.PanelHDRToggleClickPingback;
import com.gala.video.app.player.pingback.player.PanelHDRTogglePageShowPingback;
import com.gala.video.app.player.pingback.player.ScreenRatioShowPingback;
import com.gala.video.app.player.pingback.player.SelectionsShowPingback;
import com.gala.video.app.player.pingback.player.SkipHeadTailerClickPingback;
import com.gala.video.app.player.pingback.player.SkipHeaderShowPingback;
import com.gala.video.app.player.pingback.player.VideoRatioClickPingback;
import com.gala.video.lib.framework.core.utils.LogUtils;

public class PingbackFactoryImpl implements IPingbackFactory {
    private static final String TAG = "PingbackFactoryImpl";

    public Pingback createPingback(int flag) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "createPingback() flag=" + flag);
        }
        switch (flag) {
            case 1:
                return new InitPingback();
            case 2:
                return new DetailPlayBtnClickPingback();
            case 3:
                return new EpisodeClickPingback();
            case 4:
                return new DetailBuyClickPingback();
            case 5:
                return new DetailFavClickPingback();
            case 6:
                return new GuessYoulikeClickPingback();
            case 7:
                return new RecommendClickPingback();
            case 8:
                return new SuperAlbumClickPingback();
            case 9:
                return new PlayWindowClickPingback();
            case 10:
                return new SummaryClickPingback();
            case 11:
                return new DetailPageShowPingback();
            case 12:
                return new GuessYouLikeShowPingback();
            case 14:
                return new PageExitPingback();
            case 15:
                return new CardPageShowPingback();
            case 16:
                return new SuperAlbumShowPingback();
            case 17:
                return new MenupanelShowPingback();
            case 18:
                return new SelectionsShowPingback();
            case 19:
                return new VideoRatioClickPingback();
            case 20:
                return new SkipHeadTailerClickPingback();
            case 21:
                return new LiveInteractionPingback();
            case 22:
                return new CarouselProgrammeClickPingback();
            case 23:
                return new CarouselChannelClickPingback();
            case 24:
                return new CarouselProgrammeShowPingback();
            case 25:
                return new CarouselChannelShowPingback();
            case 26:
                return new CarouselInfoPingback();
            case 27:
                return new KeyeventPingback();
            case 28:
                return new PageLoadPingback();
            case 29:
                return new DailyinfoClickPingback();
            case 30:
                return new DailyInfoShowPingback();
            case 31:
                return new MenuPanelBitStreamClickPingback();
            case 32:
                return new EpisodePageShowPingback();
            case 33:
                return new NewsItemPageClickPingback();
            case 34:
                return new CarPageClickPingback();
            case 36:
                return new StarPageShowPingback();
            case 38:
                return new ExitDialogPageShowPingback();
            case 39:
                return new ExitDialogPageClickedPingback();
            case IPingbackFactory.BOTTOM_EPISODE_CLICK /*40*/:
                return new BottomEpisodeClickPingback();
            case 41:
                return new BottomRecommendClickPingback();
            case 42:
                return new CustomerDetailLoadedPingback();
            case 43:
                return new CustomerDetailExitPingback();
            case 44:
                return new HDRGuidePageShowPingback();
            case 45:
                return new PanelHDRTogglePageShowPingback();
            case 46:
                return new HDRGuideClickPingback();
            case 47:
                return new PanelHDRToggleClickPingback();
            case 48:
                return new BitStreamAdPingback();
            case 49:
                return new MenupanelAdPingback();
            case 50:
                return new SkipHeaderShowPingback();
            case 51:
                return new ScreenRatioShowPingback();
            case 52:
                return new MenuProgramShowPingback();
            case 53:
                return new EpisodeAdPingback();
            case 54:
                return new LoginTipShowPingback();
            case 55:
                return new LoginTipClickPingback();
            default:
                throw new RuntimeException("Do not exist pingbak type");
        }
    }
}
