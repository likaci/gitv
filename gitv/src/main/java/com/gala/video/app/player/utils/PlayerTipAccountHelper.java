package com.gala.video.app.player.utils;

import android.content.Context;
import android.content.SharedPreferences;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PlayerTipAccountHelper {
    private static String PLAYER_TIP_FREQUENCY = "player_tip_frequency";
    private static final String TAG = "Player/Ui/PlayerTipAccountHelper";
    private final SimpleDateFormat mDateFormat = new SimpleDateFormat("yy/MM/dd", Locale.CHINA);
    private int[] mInt;
    private Date mToday;
    private String mVersion = Project.getInstance().getBuild().getVersionString();

    public void saveCount(Context context, String type, int count, int totalcount) {
        if (context != null) {
            SharedPreferences sharedPref = context.getSharedPreferences(PLAYER_TIP_FREQUENCY, 5);
            if (sharedPref != null) {
                if (this.mToday == null) {
                    this.mToday = new Date();
                }
                String content = this.mVersion + "," + this.mDateFormat.format(this.mToday) + "," + count + "," + totalcount;
                boolean success = sharedPref.edit().putString(type, content).commit();
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "saveCount content:" + content.toString());
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "success" + success);
                }
            }
        }
    }

    public int[] getCount(Context context, String type) {
        if (context == null) {
            return null;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(PLAYER_TIP_FREQUENCY, 5);
        if (sharedPref != null) {
            CharSequence content = sharedPref.getString(type, null);
            if (StringUtils.isEmpty(content)) {
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "content is null");
                }
                return null;
            }
            String[] array = splitContent(content);
            if (array != null && array.length == 4) {
                String version = array[0];
                String data = array[1];
                String dailycount = array[2];
                String totalcount = array[3];
                if (StringUtils.isEmpty(this.mVersion) || !this.mVersion.contentEquals(version)) {
                    this.mInt = new int[2];
                    this.mInt[0] = 0;
                    this.mInt[1] = 0;
                    return this.mInt;
                }
                if (this.mToday == null) {
                    this.mToday = new Date();
                }
                String currentdata = this.mDateFormat.format(this.mToday);
                if (LogUtils.mIsDebug) {
                    LogUtils.d(TAG, "currentdata：" + currentdata + ", version:" + version);
                }
                int total;
                if (currentdata.contentEquals(data)) {
                    int daily = parseString(dailycount);
                    total = parseString(totalcount);
                    if (daily >= 0 && total >= 0) {
                        this.mInt = new int[2];
                        this.mInt[0] = daily;
                        this.mInt[1] = total;
                    }
                    return this.mInt;
                }
                total = parseString(totalcount);
                if (total >= 0) {
                    this.mInt = new int[2];
                    this.mInt[0] = 0;
                    this.mInt[1] = total;
                }
                return this.mInt;
            }
        }
        return null;
    }

    public void clear() {
        this.mToday = null;
    }

    private String[] splitContent(String content) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "splitContent:" + content.toString());
        }
        try {
            if (content.contains(",")) {
                return content.split(",");
            }
        } catch (Exception e) {
        }
        return null;
    }

    private int parseString(String str) {
        if (!StringUtils.isEmpty((CharSequence) str)) {
            try {
                return Integer.valueOf(str).intValue();
            } catch (Exception e) {
            }
        }
        return -1;
    }
}
