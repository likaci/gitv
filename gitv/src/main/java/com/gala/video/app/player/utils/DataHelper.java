package com.gala.video.app.player.utils;

import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.error.ErrorConstants;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.CollectType;
import com.gala.video.app.player.R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.ui.config.AlbumDetailUiConfig;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.HomeDataConfig.ItemSize;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetType;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataHelper {
    private static final String API_ERR_CODE_Q503 = "Q00503";
    private static final String API_ERR_CODE_Q505 = "Q00505";
    private static final String TAG = "AlbumDetail/Data/DataHelper";
    public static String favSubKey = null;
    public static String favSubType = null;
    private static final String mDailyNewsDateFormat = "MM月dd日";
    private static ThreadLocal<SimpleDateFormat> mDailyNewsTreadLocal = new ThreadLocal<SimpleDateFormat>() {
        protected synchronized SimpleDateFormat initialValue() {
            return new SimpleDateFormat(DataHelper.mDailyNewsDateFormat, Locale.getDefault());
        }
    };
    private static int[] sChannelsShowAsGallery = PlayerAppConfig.showEpisodeAsGallery();
    private static SimpleDateFormat sFormatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss:SS", Locale.US);

    public static void updateFavData(Album album) {
        if (album == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "updateFavData: invalid album");
            }
        } else if (album.subType == 0 || StringUtils.isEmpty(album.subKey)) {
            if (album.isSeries()) {
                favSubType = String.valueOf(CollectType.SERIES.getValue());
            } else {
                favSubType = String.valueOf(CollectType.SINGLE.getValue());
            }
            switch (album.chnId) {
                case 1:
                    favSubKey = album.tvQid;
                    break;
                case 2:
                case 3:
                case 4:
                case 15:
                    favSubKey = album.qpId;
                    break;
                default:
                    favSubKey = album.qpId;
                    break;
            }
            LogUtils.e(TAG, "updateFavData: subType=" + favSubType + ", subKey=" + favSubKey);
        } else {
            favSubKey = album.subKey;
            favSubType = String.valueOf(album.subType);
            LogUtils.e(TAG, "album.subType != 0 updateFavData: subType=" + favSubType + ", subKey=" + favSubKey);
        }
    }

    public static IVideo findVideoByOrder(List<IVideo> videos, int order) {
        LogUtils.d(TAG, "findEpisodeByOrder: order=" + order + ", list size=" + (videos != null ? Integer.valueOf(videos.size()) : "NULL"));
        if (ListUtils.isEmpty((List) videos)) {
            LogUtils.d(TAG, "findEpisodeByOrder returns null");
            return null;
        }
        for (int i = videos.size() - 1; i >= 0; i--) {
            IVideo video = (IVideo) videos.get(i);
            if (order == video.getPlayOrder()) {
                LogUtils.d(TAG, "findEpisodeByOrder returns " + video.toStringBrief());
                return video;
            }
        }
        LogUtils.d(TAG, "findEpisodeByOrder returns null");
        return null;
    }

    public static AlbumInfo findVideoByOrderAlbum(List<AlbumInfo> albumInfos, int order) {
        LogUtils.d(TAG, "findEpisodeByOrder: order=" + order + ", list size=" + (albumInfos != null ? Integer.valueOf(albumInfos.size()) : "NULL"));
        if (ListUtils.isEmpty((List) albumInfos)) {
            LogUtils.d(TAG, "findEpisodeByOrder returns null");
            return null;
        }
        for (int i = albumInfos.size() - 1; i >= 0; i--) {
            AlbumInfo albumInfo = (AlbumInfo) albumInfos.get(i);
            if (order == albumInfo.getPlayOrder()) {
                LogUtils.d(TAG, "findEpisodeByOrder returns " + albumInfo.toStringBrief());
                return albumInfo;
            }
        }
        LogUtils.d(TAG, "findEpisodeByOrder returns null");
        return null;
    }

    public static Album findAlbumByTvId(List<Album> albums, String tvId) {
        LogUtils.d(TAG, "findAlbumByTvId tvId = " + tvId);
        if (ListUtils.isEmpty((List) albums) || StringUtils.isEmpty((CharSequence) tvId)) {
            return null;
        }
        for (int i = albums.size() - 1; i >= 0; i--) {
            Album album = (Album) albums.get(i);
            if (tvId.equals(album.tvQid)) {
                return album;
            }
        }
        return null;
    }

    public static int findAlbumIndexByTvId(String tvId, List<Album> list) {
        LogUtils.d(TAG, "findIndexByTvId tvId" + tvId);
        if (StringUtils.isEmpty((CharSequence) tvId) || ListUtils.isEmpty((List) list)) {
            return -1;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (tvId.equals(((Album) list.get(i)).tvQid)) {
                return i;
            }
        }
        return -1;
    }

    public static boolean showEpisodeAsGallery(Album album) {
        if (album == null) {
            return false;
        }
        for (int i : sChannelsShowAsGallery) {
            if (album.chnId == i) {
                return true;
            }
        }
        if (album.isSourceType() && album.isSeries() && !new AlbumDetailUiConfig().isEnableWindowPlay()) {
            return true;
        }
        return false;
    }

    public static List<Album> getUniqItemAlbums(List<Album> originalList, List<Album> newList) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">>getUniqItemAlbums newListSize" + newList.size());
        }
        if (!(ListUtils.isEmpty((List) originalList) || ListUtils.isEmpty((List) newList))) {
            for (Album album : originalList) {
                Album newAlbum = findAlbumByTvId(newList, album.tvQid);
                if (newAlbum != null) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.d(TAG, "find album");
                    }
                    newList.remove(newAlbum);
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "<<getUniqItemAlbums ret" + newList.size());
            }
        }
        return newList;
    }

    public static ArrayList<TabDataItem> getNewTabItemInstance(List<TabDataItem> tabDataItems) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">>getNewTabItemInstance");
        }
        ArrayList<TabDataItem> newTabDataItems = new ArrayList();
        if (!ListUtils.isEmpty((List) tabDataItems)) {
            for (TabDataItem item : tabDataItems) {
                newTabDataItems.add(item.copyFrom());
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "<<getNewTabItemInstance ret" + newTabDataItems.size());
            }
        }
        return newTabDataItems;
    }

    public static String getFormatTime() {
        String result = "";
        return sFormatter.format(new Date());
    }

    public static boolean isUserCannotPreviewCode(String errorCode) {
        boolean ret = ErrorConstants.API_ERR_CODE_Q302.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q310.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q304.equals(errorCode) || ErrorConstants.API_ERR_CODE_Q305.equals(errorCode) || "Q00505".equals(errorCode) || "Q00503".equals(errorCode);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "isUserCannotPreviewCode" + errorCode);
        }
        return ret;
    }

    public static boolean isBodanOrDailyNews(IVideo video) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> isBodanOrDailyNews");
        }
        boolean ret = false;
        SourceType type = video.getSourceType();
        if (SourceType.BO_DAN == type || SourceType.DAILY_NEWS == type) {
            ret = true;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "<< isBodanOrDailyNews, ret=" + ret);
        }
        return ret;
    }

    public static String parseDailyNewsIssueTime(long timeStamp) {
        long timedelta = DeviceUtils.getServerTimeMillis() - timeStamp;
        LogUtils.d(TAG, "setAlbum album.timeStamp=" + timeStamp + "timederta" + timedelta);
        if (timedelta < 60000) {
            return "1分钟前";
        }
        if (timedelta >= 6000 && timedelta < 3600000) {
            return String.valueOf((int) ((timedelta / 1000) / 60)) + "分钟前";
        } else if (timedelta < 3600000 || timedelta >= 86400000) {
            return dailyNewsformat(new Date(timeStamp));
        } else {
            return String.valueOf((timedelta / 3600) / 1000) + "小时前";
        }
    }

    private static String dailyNewsformat(Date date) {
        return ((SimpleDateFormat) mDailyNewsTreadLocal.get()).format(date);
    }

    public static String getVideoDesc(AlbumInfo albumInfo) {
        return PlayerAppConfig.getAlbumDesc(albumInfo);
    }

    public static String getVideoTitle(AlbumInfo albumInfo) {
        String ret = "";
        if (albumInfo == null || StringUtils.isEmpty(albumInfo.getAlbumSubName())) {
            return ret;
        }
        return albumInfo.getAlbumSubName();
    }

    public static String getVideoImageURL(AlbumInfo albumInfo) {
        String ret = "";
        if (albumInfo == null) {
            return ret;
        }
        if (StringUtils.isEmpty(albumInfo.getAlbumDetailPic())) {
            return albumInfo.getAlbumPic() == null ? "" : albumInfo.getAlbumPic();
        } else {
            return albumInfo.getAlbumDetailPic();
        }
    }

    public static String getItemPic(Album album, int cardWidget) {
        String url = StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic;
        if (cardWidget == 12) {
            return PicSizeUtils.getUrlWithSize(PhotoSize._480_270, url);
        }
        if (cardWidget == 9) {
            return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, url);
        }
        LogRecordUtils.loge(TAG, ">> getItemPic = album = " + album + "cardWidget" + cardWidget);
        return MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
    }

    public static String getRecommendPic(Album album, int cardWidget) {
        if (cardWidget == 12) {
            return PicSizeUtils.getUrlWithSize(PhotoSize._480_270, album.tvPic);
        } else if (cardWidget == 9) {
            return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, !StringUtils.isEmpty(album.pic) ? album.pic : album.tvPic);
        } else {
            LogRecordUtils.loge(TAG, ">> getRecommendPic = album = " + album + "cardWidget" + cardWidget);
            return MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR;
        }
    }

    private static String getDescRB(Album album) {
        if (album == null) {
            return "";
        }
        String str = "";
        AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
        if (albumType == AlbumKind.SIGLE_UNIT) {
            return GetInterfaceTools.getCornerProvider().getLength(album);
        }
        if (albumType != AlbumKind.SIGLE_SERIES) {
            return str;
        }
        if (StringUtils.isEmpty(GetInterfaceTools.getCornerProvider().getDateShort(album))) {
            return str;
        }
        return ResourceUtil.getStr(R.string.album_item_update, format);
    }

    public static String getAlbumTitle(Album album) {
        if (album == null) {
            return "";
        }
        String str = "";
        AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
        if (albumType == AlbumKind.SIGLE_SERIES || albumType == AlbumKind.SIGLE_VIDEO) {
            return album.tvName;
        }
        return album.name;
    }

    public static CardModel convertEpisodesToCardModel(List<IVideo> episodes) {
        List<ItemModel> listItem;
        LogRecordUtils.logd(TAG, ">> convertEpisodeListToCardModel");
        CardModel card = new CardModel();
        card.setId(DetailConstants.CONTENT_TAG_PROGRAM);
        card.setCardLine(1);
        card.setWidgetType(12);
        card.setTitle(DetailConstants.CONTENT_TITLE_PROGRAMS);
        if (ListUtils.isEmpty((List) episodes)) {
            listItem = new ArrayList();
        } else {
            listItem = new ArrayList(episodes.size());
            for (IVideo v : episodes) {
                ItemModel item = CreateInterfaceTools.createModelHelper().convertAlbumToItemModel(v.getAlbum());
                item.setWidgetType(WidgetType.ITEM_TITLE_OUT);
                item.setWidth(ItemSize.ITEM_480);
                item.setHigh(270);
                item.setItemPic(getItemPic(v.getAlbum(), 12));
                item.setDesL1RBString(getDescRB(v.getAlbum()));
                item.setTitle(v.getAlbum().tvName);
                listItem.add(item);
            }
        }
        card.setItemModelList(listItem);
        LogRecordUtils.logd(TAG, "<< convertEpisodeListToCardModel");
        return card;
    }

    public static CardModel convertEpisodesToCardModelAlbum(List<AlbumInfo> episodes) {
        List<ItemModel> listItem;
        LogRecordUtils.logd(TAG, ">> convertEpisodeListToCardModel");
        CardModel card = new CardModel();
        card.setId(DetailConstants.CONTENT_TAG_PROGRAM);
        card.setCardLine(1);
        card.setWidgetType(12);
        card.setTitle(DetailConstants.CONTENT_TITLE_PROGRAMS);
        if (ListUtils.isEmpty((List) episodes)) {
            listItem = new ArrayList();
        } else {
            listItem = new ArrayList(episodes.size());
            for (AlbumInfo v : episodes) {
                ItemModel item = CreateInterfaceTools.createModelHelper().convertAlbumToItemModel(v.getAlbum());
                item.setWidgetType(WidgetType.ITEM_TITLE_OUT);
                item.setWidth(ItemSize.ITEM_480);
                item.setHigh(270);
                item.setItemPic(getItemPic(v.getAlbum(), 12));
                item.setDesL1RBString(getDescRB(v.getAlbum()));
                item.setTitle(v.getAlbum().tvName);
                listItem.add(item);
            }
        }
        card.setItemModelList(listItem);
        LogRecordUtils.logd(TAG, "<< convertEpisodeListToCardModel");
        return card;
    }

    public static int findVideoIndexByTvid(List<IVideo> list, IVideo item) {
        if (item == null || ListUtils.isEmpty((List) list)) {
            return -1;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (item.getTvId().equals(((IVideo) list.get(i)).getTvId())) {
                return i;
            }
        }
        return -1;
    }

    public static int findVideoIndexByTvidAlbum(List<AlbumInfo> list, AlbumInfo item) {
        if (item == null || ListUtils.isEmpty((List) list)) {
            return -1;
        }
        int size = list.size();
        for (int i = 0; i < size; i++) {
            if (item.getTvId().equals(((AlbumInfo) list.get(i)).getTvId())) {
                return i;
            }
        }
        return -1;
    }

    public static boolean isSameVideoByTVID(AlbumInfo first, AlbumInfo second) {
        if (first == null || second == null || !com.gala.sdk.utils.StringUtils.equals(first.getAlbumId(), second.getAlbumId()) || !com.gala.sdk.utils.StringUtils.equals(first.getTvId(), second.getTvId())) {
            return false;
        }
        return true;
    }
}
