package com.qiyi.tv.client.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.qiyi.tv.client.data.Album;
import com.qiyi.tv.client.data.AppInfo;
import com.qiyi.tv.client.data.Channel;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Playlist;
import com.qiyi.tv.client.data.Video;
import com.qiyi.tv.client.feature.account.UserInfo;
import com.qiyi.tv.client.feature.account.VipInfo;
import com.qiyi.tv.client.feature.common.PlayParams;
import com.qiyi.tv.client.impl.Params.Extras;
import com.qiyi.tv.client.impl.Params.ResultDataType;
import java.util.ArrayList;
import java.util.List;

public class ParamsHelper {

    static class ClientInfoImpl implements ClientInfo {
        private final int f2078a;
        private final String f2079a;
        private final String f2080b;
        private final String f2081c;
        private final String f2082d;

        public ClientInfoImpl(Bundle bundle) {
            this.f2079a = bundle.getString(Extras.EXTRA_CUSTOMER_VERSION_NAME);
            this.f2078a = bundle.getInt(Extras.EXTRA_CUSTOMER_VERSION_CODE, -1);
            this.f2081c = bundle.getString(Extras.EXTRA_CUSTOMER_PACKAGE_NAME);
            this.f2080b = bundle.getString(Extras.EXTRA_CUSTOMER_SIGNATURE);
            this.f2082d = bundle.getString(Extras.EXTRA_CUSTOMER_UUID);
        }

        public String getVersionName() {
            return this.f2079a;
        }

        public int getVersionCode() {
            return this.f2078a;
        }

        public String getPackageName() {
            return this.f2081c;
        }

        public String getSignature() {
            return this.f2080b;
        }

        public String getUuid() {
            return this.f2082d;
        }

        public String toString() {
            return "ClientInfo(vn=" + this.f2079a + ", vc=" + this.f2078a + ", pk=" + this.f2081c + ")";
        }
    }

    private ParamsHelper() {
    }

    public static Intent getStartIntent(Context context, String signature, String servicePackageName) {
        Log.m1620d("ParamsHelper", " packagename = " + servicePackageName);
        Intent intent = new Intent(new StringBuilder(Extras.ACTION_SERVICE_PREFIX).append(signature).toString());
        if (servicePackageName != null) {
            intent.setPackage(servicePackageName);
        }
        Utils.dumpIntent("getStartIntent()", intent);
        return intent;
    }

    public static void setClientInfo(Bundle bundle, String versionName, int versionCode, String packageName, String signature) {
        if (bundle != null) {
            bundle.putString(Extras.EXTRA_CUSTOMER_VERSION_NAME, versionName);
            bundle.putInt(Extras.EXTRA_CUSTOMER_VERSION_CODE, versionCode);
            bundle.putString(Extras.EXTRA_CUSTOMER_PACKAGE_NAME, packageName);
            bundle.putString(Extras.EXTRA_CUSTOMER_SIGNATURE, signature);
        }
    }

    public static ClientInfo parseClientInfo(Bundle bundle) {
        return new ClientInfoImpl(bundle);
    }

    public static String parsePackageName(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_CUSTOMER_PACKAGE_NAME);
    }

    public static void setPackageName(Bundle bundle, String packageName) {
        bundle.putString(Extras.EXTRA_CUSTOMER_PACKAGE_NAME, packageName);
    }

    public static Intent parseIntent(Bundle bundle) {
        if (bundle == null || !bundle.containsKey(Extras.EXTRA_INTENT)) {
            return null;
        }
        return (Intent) bundle.getParcelable(Extras.EXTRA_INTENT);
    }

    public static int parseIntentFlag(Bundle bundle) {
        if (bundle == null || !bundle.containsKey(Extras.EXTRA_INTENT_FLAG)) {
            return -1;
        }
        return bundle.getInt(Extras.EXTRA_INTENT_FLAG, -1);
    }

    public static void setIntentFlag(Bundle bundle, int flag) {
        bundle.putInt(Extras.EXTRA_INTENT_FLAG, flag);
    }

    public static int parseResultCode(Bundle params) {
        int i = 1;
        Utils.dumpBundle("parseResultCode()", params);
        if (params != null && params.containsKey(Extras.EXTRA_RESULT_CODE)) {
            i = params.getInt(Extras.EXTRA_RESULT_CODE, 1);
        }
        Log.m1620d("ParamsHelper", "parseResultCode() return " + i);
        return i;
    }

    public static void setResultCode(Bundle params, int code) {
        if (params != null) {
            params.putInt(Extras.EXTRA_RESULT_CODE, code);
        }
        Log.m1620d("ParamsHelper", "setResultCode(" + code + ")");
    }

    public static <T> T parseResultData(Bundle params) {
        Utils.dumpBundle("parseResultData()", params);
        if (params == null) {
            return null;
        }
        int i = params.getInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_UNKNOWN);
        if (ResultDataType.RESULT_TYPE_PARCELABLE_ITEM == i) {
            return params.getParcelable(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        if (ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY == i) {
            return params.getParcelableArray(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        if (ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY_LIST == i) {
            return params.getParcelableArrayList(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        if (ResultDataType.RESULT_TYPE_STRING_ARRAY_LIST == i) {
            return params.getStringArrayList(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        return null;
    }

    public static int parseResultDataType(Bundle params) {
        int i = ResultDataType.RESULT_TYPE_UNKNOWN;
        if (params != null) {
            params.getInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_UNKNOWN);
        }
        return i;
    }

    public static <T extends Parcelable> void setResultData(Bundle params, T data) {
        Log.m1620d("ParamsHelper", "setResultData(" + data + ")");
        if (params != null) {
            params.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_PARCELABLE_ITEM);
            params.putParcelable(Extras.EXTRA_RESULT_DATA_VALUE, data);
        }
        Utils.dumpBundle("setResultData()", params);
    }

    public static void setResultDataOfArrayString(Bundle params, ArrayList<String> data) {
        Log.m1620d("ParamsHelper", "setResultDataOfArrayString(" + data + ")");
        if (params != null) {
            params.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_STRING_ARRAY_LIST);
            params.putStringArrayList(Extras.EXTRA_RESULT_DATA_VALUE, data);
        }
        Utils.dumpBundle("setResultDataOfArrayString()", params);
    }

    public static <T extends Parcelable> void setResultData(Bundle params, ArrayList<T> dataList) {
        Log.m1620d("ParamsHelper", "setResultData(" + dataList + ")");
        if (params != null) {
            params.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY_LIST);
            params.putParcelableArrayList(Extras.EXTRA_RESULT_DATA_VALUE, dataList);
        }
        Utils.dumpBundle("setResultData()", params);
    }

    public static <T extends Parcelable> void setResultData(Bundle params, T[] dataArray) {
        Log.m1620d("ParamsHelper", "setResultData(" + dataArray + ")");
        if (params != null) {
            params.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY);
            params.putParcelableArray(Extras.EXTRA_RESULT_DATA_VALUE, dataArray);
        }
        Utils.dumpBundle("setResultData()", params);
    }

    public static void setKeyword(Bundle bundle, String keyword) {
        bundle.putString(Extras.EXTRA_KEYWORD, keyword);
    }

    public static String parseKeyword(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_KEYWORD);
    }

    public static void setChannel(Bundle bundle, Channel channel) {
        bundle.putParcelable(Extras.EXTRA_CHANNEL, channel);
    }

    public static Channel parseChannel(Bundle bundle) {
        return (Channel) bundle.getParcelable(Extras.EXTRA_CHANNEL);
    }

    public static void setMedia(Bundle bundle, Media media) {
        if (media instanceof Playlist) {
            bundle.putParcelable(Extras.EXTRA_PLAYLIST, (Playlist) media);
        } else if (media instanceof Album) {
            bundle.putParcelable(Extras.EXTRA_ALBUM, (Album) media);
        } else if (media instanceof Video) {
            bundle.putParcelable(Extras.EXTRA_VIDEO, (Video) media);
        }
    }

    public static Media parseMedia(Bundle bundle) {
        if (bundle.containsKey(Extras.EXTRA_PLAYLIST)) {
            return (Playlist) bundle.getParcelable(Extras.EXTRA_PLAYLIST);
        }
        if (bundle.containsKey(Extras.EXTRA_ALBUM)) {
            return (Album) bundle.getParcelable(Extras.EXTRA_ALBUM);
        }
        if (bundle.containsKey(Extras.EXTRA_VIDEO)) {
            return (Video) bundle.getParcelable(Extras.EXTRA_VIDEO);
        }
        return null;
    }

    public static void setFilterTags(Bundle bundle, List<String> filterFlags) {
        if (filterFlags != null && !filterFlags.isEmpty()) {
            ArrayList arrayList = new ArrayList();
            for (String add : filterFlags) {
                arrayList.add(add);
            }
            bundle.putStringArrayList(Extras.EXTRA_FILTER_TAGS, arrayList);
        }
    }

    public static List<String> parseFilterTags(Bundle bundle) {
        return bundle.getStringArrayList(Extras.EXTRA_FILTER_TAGS);
    }

    public static void setClassTag(Bundle bundle, String classTag) {
        bundle.putString(Extras.EXTRA_CLASS_TAG, classTag);
    }

    public static String parseClassTag(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_CLASS_TAG);
    }

    public static void setMaxCount(Bundle bundle, int maxCount) {
        bundle.putInt(Extras.EXTRA_MAX_COUNT, maxCount);
    }

    public static int parseMaxCount(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_MAX_COUNT, -1);
    }

    public static void setOnlyLongVide(Bundle bundle, boolean isLong) {
        if (isLong) {
            bundle.putString(Extras.EXTRA_ONLY_LONG_VIDEO, "1");
        } else {
            bundle.putString(Extras.EXTRA_ONLY_LONG_VIDEO, "0");
        }
    }

    public static String parseOnlyLongVideo(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_ONLY_LONG_VIDEO);
    }

    public static void setSort(Bundle bundle, String sort) {
        bundle.putString(Extras.EXTRA_SORT, sort);
    }

    public static String parseSort(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_SORT);
    }

    public static void setPosition(Bundle bundle, int position) {
        bundle.putInt(Extras.EXTRA_POSITION, position);
    }

    public static int parsePosition(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_POSITION);
    }

    public static void setTitle(Bundle bundle, String title) {
        bundle.putString(Extras.EXTRA_TITLE, title);
    }

    public static String parseTitle(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_TITLE);
    }

    public static void setActivateCode(Bundle bundle, String activateCode) {
        bundle.putString(Extras.EXTRA_ACTIVATE_CODE, activateCode);
    }

    public static String parseActivateCode(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_ACTIVATE_CODE);
    }

    public static void setCount(Bundle bundle, int count) {
        bundle.putInt(Extras.EXTRA_COUNT, count);
    }

    public static int parseCount(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_COUNT, -1);
    }

    public static void setOperationTarget(Bundle bundle, int target) {
        bundle.putInt(Extras.EXTRA_OPERATION_TARGET, target);
    }

    public static int parseOperationTarget(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_OPERATION_TARGET);
    }

    public static void setOperationType(Bundle bundle, int operationType) {
        bundle.putInt(Extras.EXTRA_OPERATION_TYPE, operationType);
    }

    public static int parseOperationType(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_OPERATION_TYPE);
    }

    public static void setOperationDataType(Bundle bundle, int dataType) {
        bundle.putInt(Extras.EXTRA_OPERATION_DATA_TYPE, dataType);
    }

    public static int parseOperationDataType(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_OPERATION_DATA_TYPE);
    }

    public static void setPageNo(Bundle bundle, int pageNo) {
        bundle.putInt(Extras.EXTRA_PAGE_NO, pageNo);
    }

    public static int parsePageNo(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_PAGE_NO);
    }

    public static void setPageSize(Bundle bundle, int pageSize) {
        bundle.putInt(Extras.EXTRA_PAGE_SIZE, pageSize);
    }

    public static int parsePageSize(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_PAGE_SIZE);
    }

    public static void setPageMaxSize(Bundle bundle, int pageMaxSize) {
        bundle.putInt(Extras.EXTRA_PAGE_MAX_SIZE, pageMaxSize);
    }

    public static int parsePageMaxSize(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_PAGE_MAX_SIZE, -1);
    }

    public static void setPlayState(Bundle bundle, int state) {
        bundle.putInt(Extras.EXTRA_PLAY_STATE, state);
    }

    public static int parsePlayState(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_PLAY_STATE);
    }

    public static void setCommandContinue(Bundle bundle, boolean continueCommand) {
        bundle.putBoolean(Extras.EXTRA_COMMAND_CONTINUE, continueCommand);
    }

    public static boolean parseCommandContinue(Bundle bundle) {
        return bundle.getBoolean(Extras.EXTRA_COMMAND_CONTINUE, true);
    }

    public static void setLoginUserInfo(Bundle bundle, UserInfo userInfo) {
        bundle.putString(Extras.EXTRA_ACCOUNT_UID, userInfo.getUid());
        bundle.putString(Extras.EXTRA_ACCOUNT_AUTHCOOKIE, userInfo.getAuthCookie());
        bundle.putString(Extras.EXTRA_ACCOUNT_TOKEN, userInfo.getToken());
        bundle.putLong(Extras.EXTRA_ACCOUNT_EXPIRE, userInfo.getExpire());
        bundle.putString(Extras.EXTRA_ACCOUNT_NAME, userInfo.getName());
        bundle.putString(Extras.EXTRA_ACCOUNT_NICKNAME, userInfo.getNickName());
        bundle.putInt(Extras.EXTRA_ACCOUNT_GENDER, userInfo.getGender());
        bundle.putString(Extras.EXTRA_ACCOUNT_ICONURL, userInfo.getIconUrl());
    }

    public static UserInfo parseLoginUserInfo(Bundle bundle) {
        String string = bundle.getString(Extras.EXTRA_ACCOUNT_UID);
        String string2 = bundle.getString(Extras.EXTRA_ACCOUNT_AUTHCOOKIE);
        String string3 = bundle.getString(Extras.EXTRA_ACCOUNT_TOKEN);
        long j = bundle.getLong(Extras.EXTRA_ACCOUNT_EXPIRE);
        String string4 = bundle.getString(Extras.EXTRA_ACCOUNT_NAME);
        String string5 = bundle.getString(Extras.EXTRA_ACCOUNT_NICKNAME);
        int i = bundle.getInt(Extras.EXTRA_ACCOUNT_GENDER);
        String string6 = bundle.getString(Extras.EXTRA_ACCOUNT_ICONURL);
        String string7 = bundle.getString(Extras.EXTRA_ACCOUNT_REFRESH_TOKEN);
        String string8 = bundle.getString(Extras.EXTRA_ACCOUNT_TOKEN_SECRET);
        UserInfo userInfo = new UserInfo(string, string3, string5, j);
        userInfo.setAuthCookie(string2);
        userInfo.setName(string4);
        userInfo.setGender(i);
        userInfo.setIconUrl(string6);
        userInfo.setRefreshToken(string7);
        userInfo.setTokenSecret(string8);
        return userInfo;
    }

    public static void setLoginStatus(Bundle bundle, int status) {
        bundle.putInt(Extras.EXTRA_ACCOUNT_STATUS, status);
    }

    public static int parseLoginStatus(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_ACCOUNT_STATUS);
    }

    public static void setHistoryChangedAction(Bundle bundle, int action) {
        bundle.putInt(Extras.EXTRA_HISTORY_CHANGED_ACTION, action);
    }

    public static int parseHistoryChangedAction(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_HISTORY_CHANGED_ACTION);
    }

    public static void setFavoriteChangedAction(Bundle bundle, int action) {
        bundle.putInt(Extras.EXTRA_FAVORITE_CHANGED_ACTION, action);
    }

    public static int parseFavoriteChangedAction(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_FAVORITE_CHANGED_ACTION);
    }

    public static void setPictureSize(Bundle bundle, int pictureSize) {
        bundle.putInt(Extras.EXTRA_PICTURE_SIZE, pictureSize);
    }

    public static int parsePictureSize(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_PICTURE_SIZE);
    }

    public static void setPictureUrl(Bundle bundle, ArrayList<String> urls) {
        bundle.putStringArrayList(Extras.EXTRA_PICTURE_URL, urls);
    }

    public static List<String> parsePictureUrl(Bundle bundle) {
        return bundle.getStringArrayList(Extras.EXTRA_PICTURE_URL);
    }

    public static void setResourceId(Bundle bundle, String resourceId) {
        bundle.putString(Extras.EXTRA_RESOURCE_ID, resourceId);
    }

    public static String parseResourceId(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_RESOURCE_ID);
    }

    public static void setPictureType(Bundle bundle, int pictureType) {
        bundle.putInt(Extras.EXTRA_PICTURE_TYPE, pictureType);
    }

    public static int parsePictureType(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_PICTURE_TYPE);
    }

    public static void setResourcePictureUrl(Bundle bundle, String resourcePictureUrl) {
        bundle.putString(Extras.EXTRA_RESOURCE_PICTURE_URL, resourcePictureUrl);
    }

    public static String parseResourcePictureUrl(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_RESOURCE_PICTURE_URL);
    }

    public static void setQrCodeUrl(Bundle bundle, String qrCodeUrl) {
        bundle.putString(Extras.EXTRA_QRCODE_URL, qrCodeUrl);
    }

    public static String parseQrCodeUrl(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_QRCODE_URL);
    }

    public static void setChannelId(Bundle bundle, int channelId) {
        bundle.putInt(Extras.EXTRA_CHANNELID, channelId);
    }

    public static int parseChannelId(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_CHANNELID);
    }

    public static void setIsFullScreen(Bundle bundle, boolean isFullScreen) {
        bundle.putBoolean(Extras.EXTRA_SCREEN_SCALE, isFullScreen);
    }

    public static boolean parseIsFullScreen(Bundle bundle) {
        return bundle.getBoolean(Extras.EXTRA_SCREEN_SCALE);
    }

    public static void setPluginProvider(Bundle bundle, String providerClassName) {
        bundle.putString(Extras.EXTRA_PLUGIN_PROVIDER, providerClassName);
    }

    public static String parsePluginProvider(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_PLUGIN_PROVIDER);
    }

    public static void setApiKey(Bundle params, String apiKey) {
        params.putString(Extras.EXTRA_API_KEY, apiKey);
    }

    public static String parseApiKey(Bundle params) {
        return params.getString(Extras.EXTRA_API_KEY);
    }

    public static void setUUID(Bundle params, String uuid) {
        params.putString(Extras.EXTRA_CURRENT_UUID, uuid);
    }

    public static String parseUUID(Bundle params) {
        return params.getString(Extras.EXTRA_CURRENT_UUID);
    }

    public static void setTvPackageName(Bundle params, String packageName) {
        params.putString(Extras.EXTRA_CURRENT_TV_PACKAGENAME, packageName);
    }

    public static String parseTvPackageName(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_CURRENT_TV_PACKAGENAME);
    }

    public static void setStreamType(Bundle params, int type) {
        params.putInt(Extras.EXTRA_STREAM_TYPE, type);
    }

    public static int parseStreamType(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_STREAM_TYPE);
    }

    public static void setSkipHeaderTailer(Bundle params, boolean isSkip) {
        params.putBoolean(Extras.EXTRA_SKIP_HEADER_TAILER, isSkip);
    }

    public static boolean parseIsSkipHeaderTailer(Bundle bundle) {
        return bundle.getBoolean(Extras.EXTRA_SKIP_HEADER_TAILER);
    }

    public static void setFromThirdUser(Bundle params, boolean fromThird) {
        params.putBoolean(Extras.EXTRA_FROM_THIRD_USER, fromThird);
    }

    public static boolean fromThirdUser(Bundle params) {
        return params.getBoolean(Extras.EXTRA_FROM_THIRD_USER);
    }

    public static void setPlayParams(Bundle params, PlayParams parms) {
        params.putParcelable(Extras.EXTRA_MEDIA_PLAY_PARAMS, parms);
    }

    public static PlayParams parsePlayParams(Bundle params) {
        return (PlayParams) params.getParcelable(Extras.EXTRA_MEDIA_PLAY_PARAMS);
    }

    public static void setAppCategory(Bundle params, int category) {
        params.putInt(Extras.EXTRA_APP_CATEGORY, category);
    }

    public static int parseAppCategory(Bundle params) {
        return params.getInt(Extras.EXTRA_APP_CATEGORY);
    }

    public static void setAppInfo(Bundle params, AppInfo appInfo) {
        params.putParcelable(Extras.EXTRA_APP_INFO, appInfo);
    }

    public static AppInfo parseAppInfo(Bundle bundle) {
        return (AppInfo) bundle.getParcelable(Extras.EXTRA_APP_INFO);
    }

    public static void setActivationState(Bundle params, int state) {
        params.putInt(Extras.EXTRA_ACTIVATION_STATE, state);
    }

    public static int parseActivationState(Bundle params) {
        return params.getInt(Extras.EXTRA_ACTIVATION_STATE, 0);
    }

    public static void setHomeTabType(Bundle params, int type) {
        params.putInt(Extras.EXTRA_HOME_TAB, type);
    }

    public static int parseHomeTabType(Bundle params) {
        return params.getInt(Extras.EXTRA_HOME_TAB, 0);
    }

    public static void setVipInfo(Bundle params, VipInfo vipInfo) {
        params.putParcelable(Extras.EXTRA_VIP_INFO, vipInfo);
    }

    public static VipInfo parseVipInfo(Bundle bundle) {
        return (VipInfo) bundle.getParcelable(Extras.EXTRA_VIP_INFO);
    }

    public static boolean parseBoolean(Bundle params) {
        return params.getBoolean(Extras.EXTRA_BOOLEAN, false);
    }

    public static void setBoolean(Bundle params, boolean state) {
        params.putBoolean(Extras.EXTRA_BOOLEAN, state);
    }

    public static String parseString(Bundle params) {
        return params.getString(Extras.EXTRA_STRING, "");
    }

    public static void setString(Bundle params, String str) {
        params.putString(Extras.EXTRA_STRING, str);
    }

    public static void setActivationCode(Bundle params, String code) {
        params.putString(Extras.EXTRA_ACTIVATION_CODE, code);
    }
}
