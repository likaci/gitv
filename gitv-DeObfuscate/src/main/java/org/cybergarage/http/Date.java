package org.cybergarage.http;

import java.util.Calendar;
import java.util.TimeZone;
import org.cybergarage.soap.SOAP;

public class Date {
    private static final String[] MONTH_STRING = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private static final String[] WEEK_STRING = new String[]{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
    private Calendar cal;

    public Date(Calendar cal) {
        this.cal = cal;
    }

    public Calendar getCalendar() {
        return this.cal;
    }

    public int getHour() {
        return getCalendar().get(11);
    }

    public int getMinute() {
        return getCalendar().get(12);
    }

    public int getSecond() {
        return getCalendar().get(13);
    }

    public static final Date getLocalInstance() {
        return new Date(Calendar.getInstance());
    }

    public static final Date getInstance() {
        return new Date(Calendar.getInstance(TimeZone.getTimeZone("GMT")));
    }

    public static final String toDateString(int value) {
        if (value < 10) {
            return "0" + Integer.toString(value);
        }
        return Integer.toString(value);
    }

    public static final String toMonthString(int value) {
        value += 0;
        if (value < 0 || value >= 12) {
            return "";
        }
        return MONTH_STRING[value];
    }

    public static final String toWeekString(int value) {
        value--;
        if (value < 0 || value >= 7) {
            return "";
        }
        return WEEK_STRING[value];
    }

    public static final String toTimeString(int value) {
        String str = "";
        if (value < 10) {
            str = new StringBuilder(String.valueOf(str)).append("0").toString();
        }
        return new StringBuilder(String.valueOf(str)).append(Integer.toString(value)).toString();
    }

    public String getDateString() {
        Calendar cal = getCalendar();
        return new StringBuilder(String.valueOf(toWeekString(cal.get(7)))).append(", ").append(toTimeString(cal.get(5))).append(" ").append(toMonthString(cal.get(2))).append(" ").append(Integer.toString(cal.get(1))).append(" ").append(toTimeString(cal.get(11))).append(SOAP.DELIM).append(toTimeString(cal.get(12))).append(SOAP.DELIM).append(toTimeString(cal.get(13))).append(" GMT").toString();
    }

    public String getTimeString() {
        Calendar cal = getCalendar();
        return new StringBuilder(String.valueOf(toDateString(cal.get(11)))).append(cal.get(13) % 2 == 0 ? SOAP.DELIM : " ").append(toDateString(cal.get(12))).toString();
    }
}
