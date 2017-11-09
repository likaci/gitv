package com.gala.video.app.epg.ui.albumlist;

import android.content.Context;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.model.AlbumOpenApiModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class AlbumUtils {
    private static AlbumEnterFactory factory = new AlbumEnterFactory();

    public static void startChannelPage(Context context, Channel channel) {
        factory.startChannelPage(context, channel);
    }

    public static void startChannelPage(Context context, Channel channel, String form) {
        factory.startChannelPage(context, channel, form);
    }

    public static void startChannelPage(Context context, int channelId) {
        factory.startChannelPage(context, channelId);
    }

    public static void startChannelPage(Context context, int channelId, String channelName) {
        factory.startChannelPage(context, channelId, channelName);
    }

    public static void startChannelPage(Context context, int channelId, int loadLimitSize) {
        factory.startChannelPage(context, channelId, loadLimitSize);
    }

    public static void startChannelPage(Context context, int channelId, String channelName, int loadLimitSize) {
        factory.startChannelPage(context, channelId, channelName, loadLimitSize);
    }

    public static void startChannelPage(Context context, int channelId, String pingBack_s2, String pingBack_buySource) {
        factory.startChannelPage(context, channelId, pingBack_s2, pingBack_buySource);
    }

    public static void startChannelPage(Context context, int channelId, String pingBack_s2, String pingBack_buySource, boolean jumpNextByRecTag) {
        factory.startChannelPage(context, channelId, pingBack_s2, pingBack_buySource, jumpNextByRecTag);
    }

    public static void startChannelPage(Context context, String labelFirstLocationTagId, int channelId, String pingBack_s2, String pingBack_buySource) {
        factory.startChannelPage(context, labelFirstLocationTagId, channelId, pingBack_s2, pingBack_buySource);
    }

    public static void startChannelPage(Context context, String labelFirstLocationTagId, int channelId, String s2) {
        factory.startChannelPage(context, labelFirstLocationTagId, channelId, s2, null);
    }

    public static void startChannelMultiDataPage(Context context, String[] multiFirstLocationTagId, int channelId, String pingBack_s2, String pingBack_buySource) {
        factory.startChannelMultiDataPage(context, multiFirstLocationTagId, channelId, pingBack_s2, pingBack_buySource);
    }

    public static void startChannelPage(Context context, int channelId, String channelName, int loadLimitSize, int intentFlags) {
        factory.startChannelPage(context, channelId, channelName, loadLimitSize, intentFlags);
    }

    public static void startChannelPageOpenApi(Context context, int channelId, String channelName, int loadLimitSize, int intentFlags) {
        factory.startChannelPageOpenApi(context, channelId, channelName, loadLimitSize, intentFlags);
    }

    public static void startChannelPageOpenApi(Context context, AlbumOpenApiModel model) {
        factory.startChannelPageOpenApi(context, model);
    }

    public static void startChannelVipPage(Context context) {
        factory.startChannelVip(context);
    }

    public static void startChannelVipPage(Context context, int intentFlags) {
        factory.startChannelVip(context, intentFlags);
    }

    public static void startChannelNewVipPage(Context context) {
        factory.startChannelNewVip(context);
    }

    public static void startChannelNewVipPage(Context context, String pingBack_s2, String pingBack_buySource) {
        factory.startChannelPage(context, 1000002, pingBack_s2, pingBack_buySource);
    }

    public static void startChannelNewVipPage(Context context, int intentFlags) {
        factory.startChannelNewVip(context, intentFlags);
    }

    public static void startChannelNewVipPageOpenApi(Context context, int intentFlags) {
        factory.startChannelNewVipOpenApi(context, intentFlags);
    }

    public static void startChannelLivePage(Context context) {
        factory.startChannelLivePage(context);
    }

    public static void startChannelLivePageOpenApi(Context context, int intentFlags) {
        factory.startChannelLivePageOpenApi(context, intentFlags);
    }

    public static void startSearchPeoplePage(Context context, String keyword, String qpId) {
        factory.startSearchResultPage(context, -1, keyword, 1, qpId, -1, null, null, "3");
    }

    public static void startSearchPeoplePage(Context context, String keyword, String qpId, String from) {
        factory.startSearchResultPage(context, -1, keyword, 1, qpId, -1, null, null, from);
    }

    public static void startSearchPeoplePage(Context context, String keyword, String qpId, int intentFlags) {
        factory.startSearchResultPage(context, -1, keyword, 1, qpId, intentFlags, null, null, "3");
    }

    public static void startSearchPeoplePageOpenApi(Context context, String keyword, String qpId, int intentFlags) {
        factory.startSearchResultPageOpenApi(context, -1, keyword, 1, qpId, intentFlags, null, "3");
    }

    public static void startSearchResultPage(Context context, int channelId, String keyword, int clickType, String qpId, String channelName) {
        factory.startSearchResultPage(context, channelId, keyword, clickType, qpId, -1, null, channelName, "3");
    }

    public static void startSearchResultPage(Context context, int channelId, String keyword, int clickType, String qpId, int intentFlags, String channelName) {
        factory.startSearchResultPage(context, channelId, keyword, clickType, qpId, intentFlags, null, channelName, "3");
    }

    public static void startSearchResultPageOpenApi(Context context, int channelId, String keyword, int clickType, String qpId, int intentFlags, String channelName) {
        factory.startSearchResultPageOpenApi(context, channelId, keyword, clickType, qpId, intentFlags, channelName, "3");
    }

    public static void startSearchResultPageFromVoice(Context context, int channelId, String keyword, int clickType, String qpId, int intentFlags, String channelName, String fromVoice) {
        factory.startSearchResultPage(context, channelId, keyword, clickType, qpId, intentFlags, null, channelName, IAlbumConfig.FROM_VOICE);
    }

    public static void startSearchResultPageForResult(Context context, int channelId, String keyword, int clickType, String qpId, int requestCode, String channelName) {
        factory.startSearchResultPageForResult(context, channelId, keyword, clickType, qpId, requestCode, null, channelName, "3");
    }

    public static void startSearchResultPageForResultOpenApi(Context context, int channelId, String keyword, int clickType, String qpId, int requestCode, String channelName) {
        factory.startSearchResultPageForResultOpenApi(context, channelId, keyword, clickType, qpId, requestCode, channelName, "3");
    }

    public static void startFootPlayhistoryPage(Context context) {
        factory.startFootPlayhistory(context, -1);
    }

    public static void startFootPlayhistoryPage(Context context, int intentFlags) {
        factory.startFootPlayhistory(context, intentFlags);
    }

    public static void startFootPlayhistoryPageOpenApi(Context context, int intentFlags) {
        factory.startFootPlayhistoryOpenApi(context, intentFlags);
    }

    public static void startFootFavouritePage(Context context) {
        factory.startFootFavourite(context);
    }

    public static void startFootSubscribePage(Context context) {
        factory.startFootSubscribe(context);
    }

    public static void startPlayhistoryActivity(Context context) {
        factory.startPlayhistoryActivity(context, -1);
    }

    public static void startPlayhistoryActivity(Context context, int intentFlags) {
        factory.startPlayhistoryActivity(context, intentFlags);
    }

    public static void startPlayhistoryActivityOpenApi(Context context, int intentFlags) {
        factory.startPlayhistoryActivityOpenApi(context, intentFlags);
    }

    public static void startPlayhistoryAllvideoActivity(Context context) {
        factory.startPlayhistoryAllvideoActivity(context, -1);
    }

    public static void startPlayhistoryAllvideoActivity(Context context, int intentFlags) {
        factory.startPlayhistoryAllvideoActivity(context, intentFlags);
    }

    public static void startPlayhistoryLongvideoActivity(Context context) {
        factory.startPlayhistoryLongvideoActivity(context, -1);
    }

    public static void startPlayhistoryLongvideoActivity(Context context, int intentFlags) {
        factory.startPlayhistoryLongvideoActivity(context, intentFlags);
    }

    public static void startFavouriteActivity(Context context) {
        factory.startFavouriteActivity(context, -1, null);
    }

    public static void startFavouriteActivity(Context context, int intentFlags) {
        factory.startFavouriteActivity(context, intentFlags, null);
    }

    public static void startFavouriteActivityOpenApi(Context context, int intentFlags) {
        factory.startFavouriteActivityOpenApi(context, intentFlags);
    }
}
