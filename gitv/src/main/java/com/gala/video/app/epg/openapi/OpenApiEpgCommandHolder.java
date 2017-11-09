package com.gala.video.app.epg.openapi;

import android.content.Context;
import com.gala.video.app.epg.openapi.feature.DeviceAuthCommand;
import com.gala.video.app.epg.openapi.feature.account.GetLoginStatusCommand;
import com.gala.video.app.epg.openapi.feature.account.GetVipInfoCommand;
import com.gala.video.app.epg.openapi.feature.account.LoginCommand;
import com.gala.video.app.epg.openapi.feature.account.LogoutCommand;
import com.gala.video.app.epg.openapi.feature.account.MergeAnonymousFavoriteCommand;
import com.gala.video.app.epg.openapi.feature.account.MergeAnonymousHistoryCommand;
import com.gala.video.app.epg.openapi.feature.data.GetAlbumInfoCommand;
import com.gala.video.app.epg.openapi.feature.data.GetChannelListCommand;
import com.gala.video.app.epg.openapi.feature.data.GetChannelMediaListCommand;
import com.gala.video.app.epg.openapi.feature.data.GetChannelRecommendCommand;
import com.gala.video.app.epg.openapi.feature.data.GetChannelRecommendForTabCommand;
import com.gala.video.app.epg.openapi.feature.data.GetHistoryMediaListCommand;
import com.gala.video.app.epg.openapi.feature.data.GetMediaDetailCommand;
import com.gala.video.app.epg.openapi.feature.data.GetPictureUrlCommand;
import com.gala.video.app.epg.openapi.feature.data.GetQRCodeUrlCommand;
import com.gala.video.app.epg.openapi.feature.data.GetRecommendationCommand;
import com.gala.video.app.epg.openapi.feature.data.GetResourceMediaListCommand;
import com.gala.video.app.epg.openapi.feature.data.GetResourcePictureUrlCommand;
import com.gala.video.app.epg.openapi.feature.data.GetSearchHotCommand;
import com.gala.video.app.epg.openapi.feature.data.GetSearchMediaCommand;
import com.gala.video.app.epg.openapi.feature.data.GetSearchSuggestionCommand;
import com.gala.video.app.epg.openapi.feature.data.GetTVQRCodeUrlCommand;
import com.gala.video.app.epg.openapi.feature.data.GetTvAppStoreAppsInfoCommand;
import com.gala.video.app.epg.openapi.feature.favorite.ClearAnonymousFavoriteCommand;
import com.gala.video.app.epg.openapi.feature.favorite.ClearFavoriteCommand;
import com.gala.video.app.epg.openapi.feature.favorite.DeleteAnonymousFavoriteCommand;
import com.gala.video.app.epg.openapi.feature.favorite.DeleteFavoriteCommand;
import com.gala.video.app.epg.openapi.feature.favorite.GetFavoriteListCommand;
import com.gala.video.app.epg.openapi.feature.history.ClearAnonymousHistoryCommand;
import com.gala.video.app.epg.openapi.feature.history.ClearHistoryCommand;
import com.gala.video.app.epg.openapi.feature.history.DeleteAnonymousHistoryCommand;
import com.gala.video.app.epg.openapi.feature.history.DeleteHistoryCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenAccountCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenActivatePageCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenAppListCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenBuyVipPageCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenChannelCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenFavoriteCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenFootHistoryCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenHistoryCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenHomeTabCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenNetSpeedCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenPlayerSettingCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenSearchCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenSearchResultCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenTvAppStoreAppDetailCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenTvAppStoreCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenTvAppStoreSearchCommand;
import com.gala.video.app.epg.openapi.feature.open.OpenVipPageCommand;
import com.gala.video.app.epg.openapi.feature.viprights.CheckActivationCodeCommand;
import com.gala.video.app.epg.openapi.feature.viprights.GetActivationStateCommand;
import com.gala.video.app.epg.openapi.feature.viprights.OpenActivationPageCommand;
import com.gala.video.lib.share.ifimpl.openplay.service.AddInstanceHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IAddInstanceHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder.Wrapper;

public class OpenApiEpgCommandHolder extends Wrapper {
    public IAddInstanceHolder[] getCommandHolder(Context context) {
        return new AddInstanceHolder[]{new AddInstanceHolder("DeviceAuthCommand", IOpenApiCommandHolder.OAA_CONNECT_DURATION, 3, IOpenApiCommandHolder.OAA_CONNECT_INTERVAL, 0, new DeviceAuthCommand(context)), new AddInstanceHolder("OpenAccountCommand", IOpenApiCommandHolder.OAA_CONNECT_DURATION, 8, IOpenApiCommandHolder.OAA_CONNECT_INTERVAL, 0, new OpenAccountCommand(context)), new AddInstanceHolder("OpenChannelCommand", 3600000, 3600000, 0, 0, new OpenChannelCommand(context)), new AddInstanceHolder("OpenFavoriteCommand", 3600000, 3600000, 0, 0, new OpenFavoriteCommand(context)), new AddInstanceHolder("OpenFootHistoryCommand", 3600000, 3600000, 0, 0, new OpenFootHistoryCommand(context)), new AddInstanceHolder("OpenHistoryCommand", 3600000, 3600000, 0, 0, new OpenHistoryCommand(context)), new AddInstanceHolder("OpenNetSpeedCommand", 3600000, 3600000, 0, 0, new OpenNetSpeedCommand(context)), new AddInstanceHolder("OpenActivatePageCommand", 3600000, 3600000, 0, 0, new OpenActivatePageCommand(context)), new AddInstanceHolder("OpenSearchCommand", 3600000, 3600000, 0, 0, new OpenSearchCommand(context)), new AddInstanceHolder("OpenSearchResultCommand", 3600000, 3600000, 0, 0, new OpenSearchResultCommand(context)), new AddInstanceHolder("OpenTvAppStoreCommand", 3600000, 3600000, 0, 0, new OpenTvAppStoreCommand(context)), new AddInstanceHolder("GetLoginStatusCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetLoginStatusCommand(context)), new AddInstanceHolder("LoginCommand", 3600000, 8, 0, 0, new LoginCommand(context)), new AddInstanceHolder("LogoutCommand", 3600000, 8, 0, 0, new LogoutCommand(context)), new AddInstanceHolder("MergeAnonymousFavoriteCommand", 3600000, 8, 0, 0, new MergeAnonymousFavoriteCommand(context)), new AddInstanceHolder("MergeAnonymousHistoryCommand", 3600000, 8, 0, 0, new MergeAnonymousHistoryCommand(context)), new AddInstanceHolder("GetChannelListCommand", 3600000, 8, 0, 0, new GetChannelListCommand(context)), new AddInstanceHolder("GetRecommendationCommand", 3600000, 8, 0, 60, new GetRecommendationCommand(context, 60)), new AddInstanceHolder("ClearAnonymousFavoriteCommand", 3600000, 3600000, 0, 0, new ClearAnonymousFavoriteCommand(context)), new AddInstanceHolder("ClearFavoriteCommand", 3600000, 3600000, 0, 0, new ClearFavoriteCommand(context)), new AddInstanceHolder("GetFavoriteListCommand", 3600000, 3600000, 0, 60, new GetFavoriteListCommand(context, 60)), new AddInstanceHolder("ClearAnonymousHistoryCommand", 3600000, 3600000, 0, 0, new ClearAnonymousHistoryCommand(context)), new AddInstanceHolder("ClearHistoryCommand", 3600000, 3600000, 0, 0, new ClearHistoryCommand(context)), new AddInstanceHolder("OpenAppListCommand", 3600000, 3600000, 0, 0, new OpenAppListCommand(context)), new AddInstanceHolder("OpenVipPageCommand", 3600000, 3600000, 0, 0, new OpenVipPageCommand(context)), new AddInstanceHolder("OpenBuyVipPageCommand", 3600000, 3600000, 0, 0, new OpenBuyVipPageCommand(context)), new AddInstanceHolder("OpenPlayerSettingCommand", 3600000, 3600000, 0, 0, new OpenPlayerSettingCommand(context)), new AddInstanceHolder("GetTvAppStoreAppsInfoCommand", 3600000, 8, 0, 0, new GetTvAppStoreAppsInfoCommand(context)), new AddInstanceHolder("OpenTvAppStoreAppDetailCommand", 3600000, 3600000, 0, 0, new OpenTvAppStoreAppDetailCommand(context)), new AddInstanceHolder("OpenTvAppStoreSearchCommand", 3600000, 3600000, 0, 0, new OpenTvAppStoreSearchCommand(context)), new AddInstanceHolder("OpenHomeTabCommand", 3600000, 3600000, 0, 0, new OpenHomeTabCommand(context)), new AddInstanceHolder("OpenActivationPageCommand", 3600000, 3600000, 0, 0, new OpenActivationPageCommand(context)), new AddInstanceHolder("GetActivationStateCommand", 3600000, 3600000, 0, 0, new GetActivationStateCommand(context)), new AddInstanceHolder("CheckActivationCodeCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new CheckActivationCodeCommand(context)), new AddInstanceHolder("GetVipInfoCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetVipInfoCommand(context)), new AddInstanceHolder("DeleteAnonymousHistoryCommand", 3600000, 3600000, 0, 0, new DeleteAnonymousHistoryCommand(context)), new AddInstanceHolder("DeleteHistoryCommand", 3600000, 3600000, 0, 0, new DeleteHistoryCommand(context)), new AddInstanceHolder("DeleteAnonymousFavoriteCommand", 3600000, 3600000, 0, 0, new DeleteAnonymousFavoriteCommand(context)), new AddInstanceHolder("DeleteFavoriteCommand", 3600000, 3600000, 0, 0, new DeleteFavoriteCommand(context)), new AddInstanceHolder("GetAlbumInfoCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetAlbumInfoCommand(context)), new AddInstanceHolder("GetChannelMediaListCommand", 3600000, 48, 0, 60, new GetChannelMediaListCommand(context, 60)), new AddInstanceHolder("GetChannelRecommendCommand", 3600000, 48, 0, 60, new GetChannelRecommendCommand(context, 60)), new AddInstanceHolder("GetChannelRecommendForTabCommand", 3600000, 8, 0, 60, new GetChannelRecommendForTabCommand(context, 60)), new AddInstanceHolder("GetHistoryMediaListCommand", 3600000, 60, 0, 60, new GetHistoryMediaListCommand(context, 60)), new AddInstanceHolder("GetMediaDetailCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetMediaDetailCommand(context)), new AddInstanceHolder("GetPictureUrlCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetPictureUrlCommand(context)), new AddInstanceHolder("GetQRCodeUrlCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetQRCodeUrlCommand(context)), new AddInstanceHolder("GetResourceMediaListCommand", 3600000, 40, 0, 60, new GetResourceMediaListCommand(context, 60)), new AddInstanceHolder("GetResourcePictureUrlCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetResourcePictureUrlCommand(context)), new AddInstanceHolder("GetSearchHotCommand", 3600000, 3600000, 0, 0, new GetSearchHotCommand(context)), new AddInstanceHolder("GetSearchMediaCommand", 3600000, 3600000, 0, 60, new GetSearchMediaCommand(context, 60)), new AddInstanceHolder("GetSearchSuggestionCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetSearchSuggestionCommand(context)), new AddInstanceHolder("GetTVQRCodeUrlCommand", 3600000, IOpenApiCommandHolder.OAA_NO_LIMIT, 0, 0, new GetTVQRCodeUrlCommand(context))};
    }
}
