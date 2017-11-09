package com.tvos.appmanager.util;

import com.tvos.apps.utils.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TimeUtil {
    public static String LongToString(long time) {
        return new SimpleDateFormat(DateUtil.PATTERN_STANDARD19H, Locale.getDefault()).format(new Date(time));
    }

    public static long StringToLong(String time) throws ParseException {
        return new SimpleDateFormat(DateUtil.PATTERN_STANDARD19H, Locale.getDefault()).parse(time).getTime();
    }
}
