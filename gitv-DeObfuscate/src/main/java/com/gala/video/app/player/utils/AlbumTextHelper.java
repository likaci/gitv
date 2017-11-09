package com.gala.video.app.player.utils;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Director;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.app.player.albumdetail.data.cache.AlbumInfoCacheManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import org.cybergarage.soap.SOAP;

public class AlbumTextHelper {
    private static final String BIG_BLANK = "     ";
    private static final String SMALL_BLANK = "  ";
    private static final String TAG = "AlbumDetail/Utils/AlbumTextHelper";

    private AlbumTextHelper() {
    }

    public static String getReleaseTime(Album album) {
        String issueTime = album.time;
        String issueYear = "";
        if (TextUtils.isEmpty(issueTime) || issueTime.trim().length() < 4) {
            return issueYear;
        }
        if (issueTime.length() > 4) {
            return issueTime.substring(0, 4);
        }
        return issueTime;
    }

    public static String getDirector(AlbumInfo info, Context context) {
        CharSequence director = info.getDirector();
        if (info == null || StringUtils.isEmpty(director)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(context.getString(C1291R.string.detail_album_info_director).trim()).append(getTypeStr(director.trim(), 3));
        return sb.toString();
    }

    public static String getHost(Album album, Context context) {
        if (album.cast == null || StringUtils.isEmpty(album.cast.host)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String host = album.cast.host;
        sb.append(context.getString(C1291R.string.detail_album_info_host).trim()).append(getTypeStr(host.trim(), 3));
        return sb.toString();
    }

    public static String getGuest(Album album, Context context) {
        if (album.cast == null || StringUtils.isEmpty(album.cast.guest)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String guest = album.cast.guest;
        sb.append(context.getString(C1291R.string.detail_album_info_guest).trim()).append(getTypeStr(guest.trim(), 3));
        return sb.toString();
    }

    public static String getPerformer(AlbumInfo albumInfo, Context context) {
        CharSequence performer = albumInfo.getMainActor();
        if (albumInfo == null || albumInfo.getAlbum() == null || StringUtils.isEmpty(performer)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< getPerformer, performer=" + performer);
        }
        sb.append(context.getString(C1291R.string.detail_album_info_main_actor).trim()).append(getTypeStr(performer.trim(), 5));
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< getPerformer, ret=" + sb.toString());
        }
        return sb.toString();
    }

    public static String getEpisodeSet(AlbumInfo albumInfo, Context context) {
        int set = albumInfo.getAlbum().tvsets;
        StringBuilder sb = new StringBuilder();
        if (albumInfo.isSeries() && !albumInfo.isSourceType() && set > 0) {
            sb.append(context.getString(C1291R.string.tv_sum)).append(set).append(context.getString(C1291R.string.set));
        }
        return sb.toString();
    }

    public static String getEpisodeCount(AlbumInfo albumInfo, Context context) {
        int count = albumInfo.getAlbum().tvCount;
        StringBuilder sb = new StringBuilder();
        if (albumInfo.isSeries() && !albumInfo.isSourceType() && count > 0) {
            sb.append(context.getString(C1291R.string.tv_update)).append(count).append(context.getString(C1291R.string.set));
        }
        return sb.toString();
    }

    public static String getScore(AlbumInfo albumInfo, Context context) {
        CharSequence score = albumInfo.getScore();
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(score)) {
            sb.append(context.getString(C1291R.string.detail_album_info_score)).append(score);
        }
        return sb.toString();
    }

    public static String getPlayCount(AlbumInfo albumInfo, Context context) {
        CharSequence playCount = albumInfo.getPlayCount();
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(playCount)) {
            int length = playCount.length();
            sb.append(context.getString(C1291R.string.detail_album_info_play_count));
            if (length < 6) {
                sb.append(splitStr(playCount));
            } else if (length < 7) {
                sb.append(playCount.substring(0, 2));
                sb.append(".");
                sb.append(playCount.charAt(3));
                sb.append("万");
            } else if (length < 9) {
                sb.append(playCount.substring(0, length - 4));
                sb.append("万");
            } else {
                sb.append(playCount.substring(0, length - 8));
                sb.append(".");
                sb.append(playCount.substring(length - 8, length - 6));
                sb.append("亿");
            }
        }
        return sb.toString();
    }

    public static String getTimeLength(Album album, Context context) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(album.len)) {
            int length = StringUtils.parse(album.len, 0);
            if (length >= 0) {
                if (length < 60) {
                    sb.append(length + context.getString(C1291R.string.play_second));
                } else {
                    sb.append((StringUtils.parse(album.len, 0) / 60) + context.getString(C1291R.string.play_minite));
                }
            }
        }
        return sb.toString();
    }

    public static String getClassTag(Album album) {
        if (StringUtils.isEmpty(album.tag)) {
            return "";
        }
        return getTypeStr(album.tag, 3);
    }

    public static SpannableStringBuilder getEpisodeMessage(AlbumInfo albumInfo, Context context) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (!StringUtils.isEmpty(getScore(albumInfo, context))) {
            ssb = firstInfoYellow(ssb, getScore(albumInfo, context));
        }
        ssb = appendInfo(context, appendInfo(context, ssb, getPlayCount(albumInfo, context)), getReleaseTime(albumInfo.getAlbum()));
        int set = albumInfo.getAlbum().tvsets;
        if (set > albumInfo.getAlbum().tvCount || set == 0) {
            ssb = appendInfo(context, ssb, getEpisodeCount(albumInfo, context));
        }
        return appendInfo(context, ssb, getEpisodeSet(albumInfo, context));
    }

    public static SpannableStringBuilder getVarietyMessage(AlbumInfo albumInfo, Context context) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (!StringUtils.isEmpty(getPlayCount(albumInfo, context))) {
            ssb = firstInfoYellow(ssb, getPlayCount(albumInfo, context));
        }
        return appendInfo(context, appendInfo(context, ssb, getScore(albumInfo, context)), getVarietyUpdate(albumInfo, context));
    }

    public static SpannableStringBuilder getFilmMessage(AlbumInfo albumInfo, Context context) {
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (!StringUtils.isEmpty(getScore(albumInfo, context))) {
            ssb = firstInfoYellow(ssb, getScore(albumInfo, context));
        }
        return appendInfo(context, appendInfo(context, appendInfo(context, appendInfo(context, ssb, getPlayCount(albumInfo, context)), getReleaseTime(albumInfo.getAlbum())), getTimeLength(albumInfo.getAlbum(), context)), getClassTag(albumInfo.getAlbum()));
    }

    public static String getDirectorStr(IVideo video, Context context) {
        if (video == null || video.getAlbum() == null) {
            return "";
        }
        int categoryId = video.getChannelId();
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getChannelId: " + categoryId);
        }
        StringBuilder sb = new StringBuilder();
        switch (categoryId) {
            case 1:
            case 2:
                List directors = video.getAlbum().director;
                if (!ListUtils.isEmpty(directors)) {
                    sb.append(context.getString(C1291R.string.detail_album_info_director).trim());
                    int length = directors.size();
                    for (int i = 0; i < length; i++) {
                        if (length - 1 == i) {
                            sb.append(((Director) directors.get(i)).name);
                        } else {
                            sb.append(((Director) directors.get(i)).name).append(" ");
                        }
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1574i(TAG, "director is: " + ((Director) directors.get(i)).name);
                        }
                    }
                    break;
                }
                break;
            case 3:
            case 9:
                CharSequence guest = video.getAlbum().cast != null ? video.getAlbum().cast.guest : "";
                CharSequence host = video.getAlbum().cast != null ? video.getAlbum().cast.host : "";
                if (StringUtils.isEmpty(host)) {
                    if (StringUtils.isEmpty(guest)) {
                        if (!StringUtils.isEmpty(video.getAlbum().tag)) {
                            sb.append(context.getString(C1291R.string.type)).append(getTypeStr(video.getAlbum().tag, 3));
                            break;
                        }
                    }
                    sb.append(context.getString(C1291R.string.detail_album_info_guest).trim()).append(getTypeStr(host.trim(), 5));
                    break;
                }
                sb.append(context.getString(C1291R.string.detail_album_info_host).trim()).append(getTypeStr(host.trim(), 5));
                break;
                break;
            default:
                if (!StringUtils.isEmpty(video.getAlbum().tag)) {
                    sb.append(context.getString(C1291R.string.type)).append(getTypeStr(video.getAlbum().tag, 3));
                    break;
                }
                break;
        }
        return sb.toString();
    }

    public static String getPerformerStr(IVideo video, Context context) {
        int categoryId = video.getChannelId();
        StringBuilder sb = new StringBuilder();
        switch (categoryId) {
            case 1:
            case 2:
                CharSequence director = video.getDirector();
                CharSequence performer = video.getMainActor();
                if (StringUtils.isEmpty(director) || StringUtils.isEmpty(performer)) {
                    if (!(StringUtils.isEmpty(director) || !StringUtils.isEmpty(performer) || StringUtils.isEmpty(video.getAlbum().tag))) {
                        sb.append(context.getString(C1291R.string.type)).append(getTypeStr(video.getAlbum().tag, 3));
                        break;
                    }
                }
                sb.append(context.getString(C1291R.string.detail_album_info_main_actor).trim()).append(getTypeStr(performer.trim(), 5));
                break;
                break;
            case 3:
            case 9:
                CharSequence guest = video.getAlbum().cast != null ? video.getAlbum().cast.guest : "";
                CharSequence host = video.getAlbum().cast != null ? video.getAlbum().cast.host : "";
                if (StringUtils.isEmpty(host) || StringUtils.isEmpty(guest)) {
                    if (!(StringUtils.isEmpty(host) || !StringUtils.isEmpty(guest) || StringUtils.isEmpty(video.getAlbum().tag))) {
                        sb.append(context.getString(C1291R.string.type)).append(getTypeStr(video.getAlbum().tag, 3));
                        break;
                    }
                }
                sb.append(context.getString(C1291R.string.detail_album_info_guest).trim()).append(getTypeStr(host.trim(), 5));
                break;
                break;
        }
        return sb.toString();
    }

    public static SpannableStringBuilder getPerformersSpannableString(int categoryId, List<Director> actors, List<Director> directors, Context context, int titleColor, int contentColor) {
        SpannableStringBuilder ssbDirector = getDirectorSpannableString(categoryId, (List) directors, context, titleColor, contentColor);
        SpannableStringBuilder ssbActor = getActorSpannableString(actors, context, titleColor, contentColor);
        if (ssbActor == null && ssbDirector == null) {
            return null;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        if (ssbDirector != null) {
            ssb.append(ssbDirector).append(BIG_BLANK);
        }
        if (ssbActor == null) {
            return ssb;
        }
        ssb.append(ssbActor);
        return ssb;
    }

    public static SpannableStringBuilder getActorSpannableString(List<Director> actors, Context context, int titleColor, int contentColor) {
        if (ListUtils.isEmpty((List) actors)) {
            return null;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString title = getSpannableStringWithColor(context.getString(C1291R.string.detail_album_info_main_actor).trim(), titleColor);
        List<String> actStrList = new ArrayList();
        for (Director director : actors) {
            actStrList.add(director.name);
        }
        ssb.append(title).append(getSpannableStringWithColor(getTypeStrList(actStrList, 5), contentColor));
        return ssb;
    }

    public static SpannableStringBuilder getDirectorSpannableString(int categoryId, List<Director> directors, Context context, int titleColor, int contentColor) {
        if (categoryId == 4 || ListUtils.isEmpty((List) directors)) {
            return null;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString title = getSpannableStringWithColor(context.getString(C1291R.string.detail_album_info_director).trim(), titleColor);
        List<String> directorStrList = new ArrayList();
        for (Director director : directors) {
            directorStrList.add(director.name);
        }
        ssb.append(title).append(getSpannableStringWithColor(getTypeStrList(directorStrList, new int[0]), contentColor));
        return ssb;
    }

    public static SpannableStringBuilder getDirectorSpannableString(int categoryId, String director, Context context, int titleColor, int contentColor) {
        if (categoryId == 4 || TextUtils.isEmpty(director)) {
            return null;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        SpannableString title = getSpannableStringWithColor(context.getString(C1291R.string.detail_album_info_director).trim(), titleColor);
        ssb.append(title).append(getSpannableStringWithColor(getTypeStr(director.trim(), new int[0]), contentColor));
        return ssb;
    }

    public static SpannableStringBuilder getVVCountStr(String countStr, int numberPx, Context context) {
        if (StringUtils.isEmpty((CharSequence) countStr)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableString = new SpannableString("总播放：");
        spannableString.setSpan(new ForegroundColorSpan(ResourceUtil.getColor(C1291R.color.detail_text_color_default)), 0, 4, 34);
        spannableString.setSpan(new AbsoluteSizeSpan(numberPx), 0, 4, 34);
        StringBuilder sb = new StringBuilder();
        int length = countStr.length();
        if (length < 6) {
            sb.append(splitStr(countStr));
        } else if (length < 7) {
            sb.append(countStr.substring(0, 2));
            sb.append(".");
            sb.append(countStr.charAt(3));
            sb.append("万");
        } else if (length < 9) {
            sb.append(countStr.substring(0, length - 4));
            sb.append("万");
        } else {
            sb.append(countStr.substring(0, length - 8));
            sb.append(".");
            sb.append(countStr.substring(length - 8, length - 6));
            sb.append("亿");
        }
        SpannableString spannableStringNum = new SpannableString(sb.toString());
        spannableStringNum.setSpan(new ForegroundColorSpan(context.getResources().getColor(C1291R.color.detail_count_point_text_color)), 0, sb.length(), 34);
        spannableStringNum.setSpan(new AbsoluteSizeSpan(numberPx), 0, sb.length(), 34);
        return spannableStringBuilder.append(spannableString).append(spannableStringNum);
    }

    public static String splitStr(String countStr) {
        if (StringUtils.isEmpty((CharSequence) countStr)) {
            return "";
        }
        List<String> ss = new ArrayList();
        while (countStr.length() > 3) {
            ss.add(countStr.substring(countStr.length() - 3));
            countStr = countStr.substring(0, countStr.length() - 3);
        }
        ss.add(countStr);
        StringBuilder sb = new StringBuilder();
        for (int i = ss.size() - 1; i > 0; i--) {
            sb.append((String) ss.get(i)).append(",");
        }
        return sb.append((String) ss.get(0)).toString();
    }

    public static List<String> getStarIDList(AlbumInfo albumInfo) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> getStarIDList, albumInfo=" + albumInfo);
        }
        if (albumInfo == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "getStarIDList, albumInfo is null.");
            }
            return new ArrayList(0);
        } else if (albumInfo.getCast() == null) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "getStarIDList, albumInfo.getCast() is null.");
            }
            return new ArrayList(0);
        } else {
            List<String> listFirst;
            List<String> listSecond;
            if (albumInfo.isSourceType()) {
                listFirst = convertStringToList(albumInfo.getCast().hostIds);
                listSecond = convertStringToList(albumInfo.getCast().guestIds);
            } else {
                listFirst = convertStringToList(albumInfo.getCast().mainActorIds);
                listSecond = convertStringToList(albumInfo.getCast().directorIds);
            }
            listFirst.addAll(listSecond);
            if (!LogUtils.mIsDebug) {
                return listFirst;
            }
            LogUtils.m1568d(TAG, "<< getStarIDs, listFirst=" + listFirst);
            return listFirst;
        }
    }

    public static List<String> convertStringToList(String s) {
        List<String> list = new ArrayList();
        if (!StringUtils.isEmpty((CharSequence) s)) {
            list.addAll(Arrays.asList(s.split(",")));
        }
        return list;
    }

    public static String convertListToString(List<String> list, String separator) {
        if (ListUtils.isEmpty((List) list)) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            builder.append((String) list.get(i));
            if (i < size - 1) {
                builder.append(separator);
            }
        }
        return builder.toString();
    }

    public static String getTypeStr(String s, int... max) {
        if (StringUtils.isEmpty((CharSequence) s)) {
            return "";
        }
        String[] array = s.split(",");
        int length = array.length;
        if (max != null && max.length > 0 && length > max[0]) {
            length = max[0];
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(array[i]);
            if (i < length - 1) {
                sb.append(SMALL_BLANK);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< getTypeStr, ret=" + sb.toString());
        }
        return sb.toString();
    }

    public static String getTypeStrList(List<String> list, int... max) {
        if (ListUtils.isEmpty((List) list)) {
            return "";
        }
        int length = list.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append((String) list.get(i));
            if (i < length - 1) {
                sb.append(SMALL_BLANK);
            }
        }
        return sb.toString();
    }

    public static String formatDate(String date) {
        if (StringUtils.isEmpty((CharSequence) date)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            sb.append(year).append("-").append(month).append("-").append(date.substring(6, 8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static SpannableStringBuilder getTimeInfoSpannableString(Album mAlbumInfo, Context context, int titleColor, int contentColor) {
        if (mAlbumInfo == null) {
            return null;
        }
        SpannableStringBuilder ssb = new SpannableStringBuilder();
        String issueTime = mAlbumInfo.time;
        if (!TextUtils.isEmpty(issueTime) && issueTime.trim().length() >= 4) {
            ssb.append(getSpannableStringWithColor(context.getString(C1291R.string.release_time), titleColor)).append(getSpannableStringWithColor((issueTime.length() > 4 ? issueTime.substring(0, 4) : issueTime) + context.getString(C1291R.string.year), contentColor)).append(BIG_BLANK);
        }
        int setCount;
        if (DataHelper.showEpisodeAsGallery(mAlbumInfo)) {
            if (mAlbumInfo.isSourceType()) {
                ssb.append(getSpannableStringWithColor(context.getString(C1291R.string.tv_update), titleColor)).append(getSpannableStringWithColor("" + formatDate(mAlbumInfo.time) + context.getString(C1291R.string.menu_alter_total_episode), contentColor)).append(BIG_BLANK);
            } else {
                if (mAlbumInfo.tvsets == 0) {
                    setCount = mAlbumInfo.tvCount;
                } else {
                    setCount = mAlbumInfo.tvsets;
                }
                ssb.append(getSpannableStringWithColor(context.getString(C1291R.string.tv_sets), titleColor)).append(getSpannableStringWithColor("" + setCount + context.getString(C1291R.string.set), contentColor)).append(BIG_BLANK);
            }
        } else if (mAlbumInfo.isSeries() && !mAlbumInfo.isSourceType()) {
            if (mAlbumInfo.tvsets == 0) {
                setCount = mAlbumInfo.tvCount;
            } else {
                setCount = mAlbumInfo.tvsets;
            }
            ssb.append(getSpannableStringWithColor(context.getString(C1291R.string.tv_sets), titleColor)).append(getSpannableStringWithColor("" + setCount + context.getString(C1291R.string.set), contentColor)).append(BIG_BLANK);
        } else if (!TextUtils.isEmpty(mAlbumInfo.len)) {
            int length = StringUtils.parse(mAlbumInfo.len, 0);
            if (length >= 0) {
                if (length < 60) {
                    ssb.append(getSpannableStringWithColor(context.getString(C1291R.string.play_length), titleColor)).append(getSpannableStringWithColor("" + length + context.getString(C1291R.string.play_second), contentColor)).append(BIG_BLANK);
                } else {
                    int minutes = length / 60;
                    ssb.append(getSpannableStringWithColor(context.getString(C1291R.string.play_length), titleColor)).append(getSpannableStringWithColor("" + minutes + context.getString(C1291R.string.play_minite), contentColor)).append(BIG_BLANK);
                }
            }
        }
        if (TextUtils.isEmpty(mAlbumInfo.tag)) {
            return ssb;
        }
        ssb.append(getSpannableStringWithColor(context.getString(C1291R.string.type), titleColor)).append(getSpannableStringWithColor(getTypeStr(mAlbumInfo.tag, 3), contentColor));
        return ssb;
    }

    public static SpannableString getSpannableStringWithColor(String string, int color) {
        SpannableString sb = new SpannableString(string);
        sb.setSpan(new ForegroundColorSpan(color), 0, string.length(), 33);
        return sb;
    }

    public static String getUpdateStrategy(IVideo mAlbumInfo, Context context) {
        StringBuilder sb = new StringBuilder();
        String strategy = mAlbumInfo.getAlbum().strategy;
        if (!TextUtils.isEmpty(strategy)) {
            sb.append("(").append(strategy).append(")");
        }
        return sb.toString();
    }

    public static String getUpdateInfo(AlbumInfo albumInfo, Context context) {
        if (albumInfo == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String strategy = albumInfo.getAlbum().strategy;
        if (!TextUtils.isEmpty(strategy)) {
            sb.append(context.getResources().getString(C1291R.string.left_brackets)).append(strategy).append(context.getResources().getString(C1291R.string.right_brackets));
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1577w(TAG, ">> getUpdateInfo():" + strategy);
        }
        return sb.toString();
    }

    public static String getEpisodeUpdate(IVideo mAlbumInfo, Context context) {
        StringBuilder sb = new StringBuilder();
        int count = mAlbumInfo.getAlbum().tvCount;
        int set = mAlbumInfo.getAlbum().tvsets;
        if (mAlbumInfo.isSeries() && !mAlbumInfo.isSourceType()) {
            sb.append(context.getString(C1291R.string.tv_sets));
            if (set == 0 && count != 0) {
                sb.append(context.getString(C1291R.string.tv_update)).append(count).append(context.getString(C1291R.string.set)).append(SMALL_BLANK).append(getUpdateStrategy(mAlbumInfo, context));
            } else if (count != set && count != 0) {
                sb.append(context.getString(C1291R.string.tv_update)).append(count).append(context.getString(C1291R.string.set)).append("/").append(context.getString(C1291R.string.tv_sum)).append(set).append(context.getString(C1291R.string.set)).append(SMALL_BLANK).append(getUpdateStrategy(mAlbumInfo, context));
            } else if (count == set && set != 0) {
                sb.append(context.getString(C1291R.string.tv_sum)).append(set).append(context.getString(C1291R.string.set)).append(SMALL_BLANK);
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "TvSets error : count is" + count + "set is" + set);
            }
        }
        return sb.toString();
    }

    public static String getVarietyUpdate(AlbumInfo albumInfo, Context context) {
        CharSequence sourceUpdateTime = AlbumInfoCacheManager.getInstance().getSourceUpdateTime();
        CharSequence time = albumInfo.getAlbum().time;
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isEmpty(sourceUpdateTime)) {
            sb.append(context.getString(C1291R.string.tv_update)).append(formatDate(sourceUpdateTime)).append(context.getString(C1291R.string.menu_alter_total_episode));
        } else if (!StringUtils.isEmpty(time)) {
            sb.append(context.getString(C1291R.string.tv_update)).append(formatDate(time)).append(context.getString(C1291R.string.menu_alter_total_episode));
        }
        return sb.toString();
    }

    public static String getVarietyDataForButton(AlbumInfo albumInfo, Context context) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isEmpty(albumInfo.getAlbum().time)) {
            return "";
        }
        if (albumInfo.isSeries()) {
            sb.append(context.getString(C1291R.string.detailorder)).append(formatDate(albumInfo.getAlbum().time)).append(context.getString(C1291R.string.set2));
        }
        return sb.toString();
    }

    public static String getCartoonUpdate(IVideo mAlbumInfo, Context context) {
        StringBuilder sb = new StringBuilder();
        int count = mAlbumInfo.getAlbum().tvCount;
        int set = mAlbumInfo.getAlbum().tvsets;
        if (mAlbumInfo.isSeries()) {
            sb.append(context.getString(C1291R.string.tv_sets));
            if (set == 0 && count != 0) {
                sb.append(context.getString(C1291R.string.tv_update)).append(count).append(context.getString(C1291R.string.set));
            } else if (count != set && count != 0) {
                sb.append(context.getString(C1291R.string.tv_update)).append(count).append(context.getString(C1291R.string.set)).append("/").append(context.getString(C1291R.string.tv_sum)).append(set).append(context.getString(C1291R.string.set));
            } else if (count == set && set != 0) {
                sb.append(context.getString(C1291R.string.tv_sum)).append(set).append(context.getString(C1291R.string.set));
            } else if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "TvSets error : count is" + count + "set is" + set);
            }
        }
        return sb.toString();
    }

    public static String getTimeInfoStr(Album mAlbumInfo, Context context) {
        if (mAlbumInfo == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        String issueTime = mAlbumInfo.time;
        if (!TextUtils.isEmpty(issueTime) && issueTime.trim().length() >= 4) {
            sb.append(context.getString(C1291R.string.release_time)).append(issueTime.length() > 4 ? issueTime.substring(0, 4) : issueTime).append(context.getString(C1291R.string.year)).append(BIG_BLANK);
        }
        int setCount;
        if (DataHelper.showEpisodeAsGallery(mAlbumInfo)) {
            if (mAlbumInfo.isSourceType()) {
                sb.append(context.getString(C1291R.string.tv_update)).append(formatDate(mAlbumInfo.time)).append(context.getString(C1291R.string.menu_alter_total_episode)).append(BIG_BLANK);
            } else {
                if (mAlbumInfo.tvsets == 0) {
                    setCount = mAlbumInfo.tvCount;
                } else {
                    setCount = mAlbumInfo.tvsets;
                }
                sb.append(context.getString(C1291R.string.tv_sets)).append(setCount).append(context.getString(C1291R.string.set)).append(BIG_BLANK);
            }
        } else if (mAlbumInfo.isSeries() && !mAlbumInfo.isSourceType()) {
            if (mAlbumInfo.tvsets == 0) {
                setCount = mAlbumInfo.tvCount;
            } else {
                setCount = mAlbumInfo.tvsets;
            }
            sb.append(context.getString(C1291R.string.tv_sets)).append(setCount).append(context.getString(C1291R.string.set)).append(BIG_BLANK);
        } else if (!TextUtils.isEmpty(mAlbumInfo.len)) {
            int length = StringUtils.parse(mAlbumInfo.len, 0);
            if (length >= 0) {
                if (length < 60) {
                    sb.append(context.getString(C1291R.string.play_length)).append(length + context.getString(C1291R.string.play_second)).append(BIG_BLANK);
                } else {
                    sb.append(context.getString(C1291R.string.play_length)).append((StringUtils.parse(mAlbumInfo.len, 0) / 60) + context.getString(C1291R.string.play_minite)).append(BIG_BLANK);
                }
            }
        }
        return sb.toString();
    }

    public static SpannableStringBuilder getPointStr(String score, int integerPx, int decimalPx, int unitPx, Context context) {
        if (TextUtils.isEmpty(score)) {
            return null;
        }
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        SpannableString spannableStringPoint = null;
        if (!TextUtils.isEmpty(score.trim())) {
            if (score.contains(".")) {
                spannableStringPoint = new SpannableString(score);
            } else {
                spannableStringPoint = new SpannableString(String.format("%s.0", new Object[]{score}));
            }
            spannableStringPoint.setSpan(new AbsoluteSizeSpan(integerPx), 0, spannableStringPoint.length() - 2, 34);
            spannableStringPoint.setSpan(new AbsoluteSizeSpan(decimalPx), spannableStringPoint.length() - 2, spannableStringPoint.length(), 34);
            spannableStringPoint.setSpan(new ForegroundColorSpan(context.getResources().getColor(C1291R.color.detail_count_point_text_color)), 0, spannableStringPoint.length(), 34);
        }
        SpannableString spannableString = new SpannableString("分");
        spannableString.setSpan(new ForegroundColorSpan(context.getResources().getColor(C1291R.color.detail_count_point_text_color)), 0, 1, 34);
        spannableString.setSpan(new AbsoluteSizeSpan(unitPx), 0, 1, 34);
        return spannableStringBuilder.append(spannableStringPoint).append(spannableString);
    }

    public static String getFormatPlayLengthStr(int length) {
        StringBuffer sb = new StringBuffer();
        int second = length;
        try {
            StringBuffer append;
            int minutes = second / 60;
            int hour = minutes / 60;
            minutes %= 60;
            second %= 60;
            if (hour > 0) {
                if (hour < 10) {
                    append = sb.append("0");
                } else {
                    append = sb;
                }
                append.append(hour).append(SOAP.DELIM);
            }
            if (minutes < 10) {
                append = sb.append("0");
            } else {
                append = sb;
            }
            append.append(minutes + SOAP.DELIM);
            if (second < 10) {
                append = sb.append("0");
            } else {
                append = sb;
            }
            append.append(second);
        } catch (Exception e) {
        }
        return sb.toString();
    }

    public static String[] getDateAndWeekForNews() {
        String[] time = new String[2];
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        if (c.get(1) < 2013) {
            LogUtils.m1571e(TAG, "invalid time");
        } else {
            String mYear = String.valueOf(c.get(1));
            String mMonth = String.valueOf(c.get(2) + 1);
            String mDay = String.valueOf(c.get(5));
            String mWay = String.valueOf(c.get(7));
            if (TextUtils.isEmpty(mYear) || TextUtils.isEmpty(mMonth) || TextUtils.isEmpty(mDay)) {
                LogUtils.m1571e(TAG, "time is null");
            } else {
                time[0] = mYear + "-" + mMonth + "-" + mDay;
                if (!TextUtils.isEmpty(mWay)) {
                    if ("1".equals(mWay)) {
                        mWay = "日";
                    } else if ("2".equals(mWay)) {
                        mWay = "一";
                    } else if ("3".equals(mWay)) {
                        mWay = "二";
                    } else if ("4".equals(mWay)) {
                        mWay = "三";
                    } else if ("5".equals(mWay)) {
                        mWay = "四";
                    } else if ("6".equals(mWay)) {
                        mWay = "五";
                    } else if ("7".equals(mWay)) {
                        mWay = "六";
                    }
                    time[1] = "星期" + mWay;
                }
            }
        }
        return time;
    }

    public static String formatPlayerErrorMessage(String ori) {
        String msg = "";
        LogRecordUtils.logd(TAG, "formatPlayerErrorMessage, ori=" + ori);
        if (!StringUtils.isEmpty((CharSequence) ori)) {
            msg = ori.replace('\n', '，').replace("，，", "，").replace("。，", "，");
        }
        LogRecordUtils.logd(TAG, "formatPlayerErrorMessage, msg=" + msg);
        return msg;
    }

    public static SpannableStringBuilder appendInfo(Context context, SpannableStringBuilder ssb, String msg) {
        if (!StringUtils.isEmpty((CharSequence) msg)) {
            ForegroundColorSpan gray = new ForegroundColorSpan(ResourceUtil.getColor(C1291R.color.detail_album_info_divider_color_new));
            if (!StringUtils.isEmpty((CharSequence) ssb)) {
                int start = ssb.length();
                ssb.append(context.getString(C1291R.string.detail_album_info_division));
                ssb.setSpan(gray, start, ssb.length(), 33);
            }
            ssb.append(msg);
        }
        return ssb;
    }

    public static SpannableStringBuilder firstInfoYellow(SpannableStringBuilder ssb, String msg) {
        if (!StringUtils.isEmpty((CharSequence) msg)) {
            ForegroundColorSpan gray = new ForegroundColorSpan(ResourceUtil.getColor(C1291R.color.detail_album_info_first_color));
            if (ssb != null) {
                int start = ssb.length();
                ssb.append(msg);
                ssb.setSpan(gray, start, ssb.length(), 33);
            }
        }
        return ssb;
    }

    public static SpannableStringBuilder updateInfoText(SpannableStringBuilder ssb, String msg) {
        if (!StringUtils.isEmpty((CharSequence) msg)) {
            ForegroundColorSpan gray = new ForegroundColorSpan(ResourceUtil.getColor(C1291R.color.detail_text_color_default));
            if (ssb != null) {
                int start = ssb.length();
                ssb.append(msg);
                ssb.setSpan(gray, start, ssb.length(), 33);
                ssb.setSpan(new AbsoluteSizeSpan(ResourceUtil.getDimensionPixelSize(C1291R.dimen.dimen_20dp)), start, ssb.length(), 33);
            }
        }
        return ssb;
    }
}
