package com.gala.tvapi.tools;

import com.gala.tvapi.tv2.TVApiBase;
import com.tvos.apps.utils.DateUtil;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import org.cybergarage.soap.SOAP;

public class DateLocalThread {
    private static ThreadLocal<SimpleDateFormat> f926a = new C02631();
    private static Date f927a = new Date();
    private static ThreadLocal<SimpleDateFormat> f928b = new C02642();
    private static ThreadLocal<SimpleDateFormat> f929c = new C02653();
    private static ThreadLocal<SimpleDateFormat> f930d = new C02664();
    private static ThreadLocal<SimpleDateFormat> f931e = new C02675();
    private static ThreadLocal<SimpleDateFormat> f932f = new C02686();
    private static ThreadLocal<SimpleDateFormat> f933g = new C02697();

    static class C02631 extends ThreadLocal<SimpleDateFormat> {
        C02631() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m635a();
        }

        private synchronized SimpleDateFormat m635a() {
            return new SimpleDateFormat(DateUtil.PATTERN_STANDARD19H, Locale.getDefault());
        }
    }

    static class C02642 extends ThreadLocal<SimpleDateFormat> {
        C02642() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m636a();
        }

        private synchronized SimpleDateFormat m636a() {
            return new SimpleDateFormat(DateUtil.PATTERN_STANDARD14W, Locale.getDefault());
        }
    }

    static class C02653 extends ThreadLocal<SimpleDateFormat> {
        C02653() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m637a();
        }

        private synchronized SimpleDateFormat m637a() {
            return new SimpleDateFormat(DateUtil.PATTERN_STANDARD08W, Locale.getDefault());
        }
    }

    static class C02664 extends ThreadLocal<SimpleDateFormat> {
        C02664() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m638a();
        }

        private synchronized SimpleDateFormat m638a() {
            return new SimpleDateFormat(DateUtil.PATTERN_STANDARD10H, Locale.getDefault());
        }
    }

    static class C02675 extends ThreadLocal<SimpleDateFormat> {
        C02675() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m639a();
        }

        private synchronized SimpleDateFormat m639a() {
            return new SimpleDateFormat("yyyy年MM月dd日", Locale.getDefault());
        }
    }

    static class C02686 extends ThreadLocal<SimpleDateFormat> {
        C02686() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m640a();
        }

        private synchronized SimpleDateFormat m640a() {
            return new SimpleDateFormat("MM月dd日 HH:mm", Locale.getDefault());
        }
    }

    static class C02697 extends ThreadLocal<SimpleDateFormat> {
        C02697() {
        }

        protected final /* synthetic */ Object initialValue() {
            return m641a();
        }

        private synchronized SimpleDateFormat m641a() {
            return new SimpleDateFormat("MM月dd日", Locale.getDefault());
        }
    }

    public static Date parseYH(String textdate) throws ParseException {
        return ((SimpleDateFormat) f926a.get()).parse(textdate);
    }

    public static String formatYH(Date date) {
        return ((SimpleDateFormat) f926a.get()).format(date);
    }

    public static Date parseYH1(String textdate) throws ParseException {
        return ((SimpleDateFormat) f928b.get()).parse(textdate);
    }

    public static Date parseY(String textdate) throws ParseException {
        return ((SimpleDateFormat) f929c.get()).parse(textdate);
    }

    public static Date parseY1(String textdate) throws ParseException {
        return ((SimpleDateFormat) f930d.get()).parse(textdate);
    }

    public static String formatY1(Date date) {
        return ((SimpleDateFormat) f930d.get()).format(date);
    }

    public static Date parseY2(String textdate) throws ParseException {
        return ((SimpleDateFormat) f931e.get()).parse(textdate);
    }

    public static String formatY2(Date date) {
        return ((SimpleDateFormat) f931e.get()).format(date);
    }

    public static Date parseM(String textdate) throws ParseException {
        return ((SimpleDateFormat) f932f.get()).parse(textdate);
    }

    public static String formatM(Date date) {
        return ((SimpleDateFormat) f932f.get()).format(date);
    }

    public static Date parseM1(String textdate) throws ParseException {
        return ((SimpleDateFormat) f933g.get()).parse(textdate);
    }

    public static String formatM1(Date date) {
        return ((SimpleDateFormat) f933g.get()).format(date);
    }

    public static long getTime(String initIssueTime) {
        if (!(initIssueTime == null || initIssueTime.isEmpty())) {
            Date parseYH;
            if (initIssueTime.contains("-") && initIssueTime.contains(SOAP.DELIM)) {
                try {
                    parseYH = parseYH(initIssueTime);
                    f927a = parseYH;
                    return parseYH.getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (!(initIssueTime.length() != 14 || initIssueTime.contains("-") || initIssueTime.contains(SOAP.DELIM))) {
                try {
                    parseYH = parseYH1(initIssueTime);
                    f927a = parseYH;
                    return parseYH.getTime();
                } catch (ParseException e2) {
                    e2.printStackTrace();
                }
            }
            if (initIssueTime.length() == 13 && !initIssueTime.contains("-") && !initIssueTime.contains(SOAP.DELIM)) {
                return Long.parseLong(initIssueTime);
            }
            if (initIssueTime.length() == 8) {
                try {
                    parseYH = parseY(initIssueTime);
                    f927a = parseYH;
                    return parseYH.getTime();
                } catch (ParseException e22) {
                    e22.printStackTrace();
                }
            }
        }
        return 0;
    }

    public static String parseIssueTime(long time) {
        long currentTime = TVApiBase.getTVApiProperty().getCurrentTime() - time;
        if (currentTime < 60000) {
            return "1分钟前";
        }
        if (currentTime >= 6000 && currentTime < 3600000) {
            return String.valueOf((int) ((currentTime / 1000) / 60)) + "分钟前";
        } else if (currentTime < 3600000 || currentTime >= 86400000) {
            f927a.setTime(time);
            return formatY1(f927a);
        } else {
            return String.valueOf((currentTime / 3600) / 1000) + "小时前";
        }
    }

    public static String parseTimeForHomeCard(long time) {
        long currentTimeMillis = System.currentTimeMillis() - time;
        if (currentTimeMillis < 60000) {
            return "1分钟前";
        }
        if (currentTimeMillis >= 6000 && currentTimeMillis < 3600000) {
            return String.valueOf((int) ((currentTimeMillis / 1000) / 60)) + "分钟前";
        } else if (currentTimeMillis < 3600000 || currentTimeMillis >= 86400000) {
            f927a.setTime(time);
            return formatM1(f927a);
        } else {
            return String.valueOf((currentTimeMillis / 3600) / 1000) + "小时前";
        }
    }
}
