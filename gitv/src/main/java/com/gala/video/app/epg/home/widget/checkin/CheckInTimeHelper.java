package com.gala.video.app.epg.home.widget.checkin;

import android.util.Log;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.system.preference.AppPreference;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.tvos.apps.utils.DateUtil;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CheckInTimeHelper {
    private static final String CHECK_IN_STATE = "check_in_state";
    private static final String CHECK_IN_TIME_KEY = "check_in_time_key";
    private static final String NAME = "check_in_pref";
    private static final String TAG = "CheckInTimeHelper";

    private static String getPrefToday() {
        return new AppPreference(ResourceUtil.getContext(), NAME).get(CHECK_IN_TIME_KEY);
    }

    private static void savePrefToday(String time) {
        new AppPreference(ResourceUtil.getContext(), NAME).save(CHECK_IN_TIME_KEY, time);
    }

    public static void savePrefToday() {
        savePrefToday(getNowTime());
    }

    public static boolean getShowState() {
        return new AppPreference(ResourceUtil.getContext(), NAME).getBoolean(CHECK_IN_STATE, true);
    }

    public static void saveShowState(boolean state) {
        new AppPreference(ResourceUtil.getContext(), NAME).save(CHECK_IN_STATE, state);
    }

    public static void setShowedState() {
        boolean state = getShowState();
        Log.i(TAG, "setShowedState state: " + state);
        if (state) {
            saveShowState(false);
        }
    }

    private static String formatTime(long currentTime) {
        String result = new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H).format(new Date(currentTime));
        Log.i(TAG, "formatTime result: " + result);
        return result;
    }

    private static String getNowTime() {
        return formatTime(DeviceUtils.getServerTimeMillis());
    }

    public static boolean isEqualDayTime() {
        String nowTime = getNowTime();
        String pfefTime = getPrefToday();
        Log.i(TAG, "isEqualDayTime local: " + nowTime + ",pfefDay:" + pfefTime);
        return nowTime.equals(pfefTime);
    }

    public static boolean isNeedShowPoint() {
        boolean state = getShowState();
        if (isEqualDayTime()) {
            return state;
        }
        resetShowState();
        return getShowState();
    }

    public static void resetShowState() {
        if (!getShowState()) {
            saveShowState(true);
            Log.i(TAG, LoginConstant.CLICK_RESEAT_CHANGE_PASSWORD);
        }
    }
}
