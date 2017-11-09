package com.gala.video.app.epg.ui.albumlist.multimenu;

import android.content.Context;
import android.util.Log;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.widget.QToast;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.tvos.apps.utils.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MenuToastHelper {
    private static final String CLICK_COUNT_MENU_KEY = "click_count_menu_key";
    private static final int CLICK_MAX_COUNT = 2;
    private static final String MENU_TOAST_KEY = "menu_toast_key";
    private static final String NAME = "album_menu_pref";
    private static final String TAG = "MenuToastHelper";
    private static final String TOAST_COUNT_KEY = "toast_count_key";
    private static final int TOAST_MAX_COUNT = 5;
    private static final String TODAY_TIME = "today_time";
    private static String mLocalToday;
    private static String mNowTime;
    private static SimpleDateFormat sSimpleDate;

    public static void init() {
        if (sSimpleDate == null) {
            sSimpleDate = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H);
        }
        mNowTime = getNowTime();
        Log.i(TAG, "init: local" + mNowTime);
    }

    public static void onDestroy() {
        sSimpleDate = null;
    }

    private static boolean shouldShowMenuToast() {
        return new AppPreference(ResourceUtil.getContext(), NAME).getBoolean(MENU_TOAST_KEY, true);
    }

    private static void saveShowMenuToast(boolean isShow) {
        new AppPreference(ResourceUtil.getContext(), NAME).save(MENU_TOAST_KEY, isShow);
    }

    public static void showMenuToast(Context context) {
        QToast.makeTextAndShow(context, R.string.album_can_select, 3000);
    }

    public static void show(Context context) {
        if (isShow()) {
            saveShowMenuToast(false);
            saveToday(mNowTime);
            int count = getToastCount();
            Log.i(TAG, "showMenuToast: getToast : " + count);
            if (count < 5) {
                saveToastCount(count + 1);
            }
            showMenuToast(context);
        }
    }

    private static int getToastCount() {
        return new AppPreference(ResourceUtil.getContext(), NAME).getInt(TOAST_COUNT_KEY, 0);
    }

    private static void saveToastCount(int count) {
        new AppPreference(ResourceUtil.getContext(), NAME).save(TOAST_COUNT_KEY, count);
    }

    private static String getPrefToday() {
        return new AppPreference(ResourceUtil.getContext(), NAME).get(TODAY_TIME);
    }

    private static void saveToday(String time) {
        new AppPreference(ResourceUtil.getContext(), NAME).save(TODAY_TIME, time);
    }

    private static String getNowTime() {
        if (sSimpleDate == null) {
            sSimpleDate = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H);
        }
        return sSimpleDate.format(new Date());
    }

    private static boolean isEqualDayTime() {
        String local = mNowTime;
        String pfefDay = getPrefToday();
        Log.i(TAG, "isEqualDayTime local: " + local + ",pfefDay:" + pfefDay);
        if (StringUtils.isEmpty((CharSequence) local)) {
            local = "";
        }
        return local.equals(pfefDay);
    }

    private static boolean isShow() {
        if (isTodayNoShow()) {
            Log.i(TAG, "isShow: isTodayNoShow");
            return false;
        }
        reset();
        if (!shouldShowMenuToast()) {
            return false;
        }
        Log.i(TAG, "isShow: 没显示过");
        return true;
    }

    public static void reset() {
        if (!isEqualDayTime() && !shouldShowMenuToast()) {
            saveShowMenuToast(true);
            Log.i(TAG, LoginConstant.CLICK_RESEAT_CHANGE_PASSWORD);
        }
    }

    private static boolean isTodayNoShow() {
        return getToastCount() == 5 || getClickCount() == 2;
    }

    private static int getClickCount() {
        return new AppPreference(ResourceUtil.getContext(), NAME).getInt(CLICK_COUNT_MENU_KEY, 0);
    }

    private static void saveClickCount(int count) {
        new AppPreference(ResourceUtil.getContext(), NAME).save(CLICK_COUNT_MENU_KEY, count);
    }

    public static void onMenuClick() {
        int count = getClickCount();
        Log.i(TAG, "onMenuClick: count" + count);
        if (count < 2) {
            saveClickCount(count + 1);
        }
    }
}
