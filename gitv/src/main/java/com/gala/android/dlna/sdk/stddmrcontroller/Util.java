package com.gala.android.dlna.sdk.stddmrcontroller;

import android.text.TextUtils;
import com.gala.android.dlna.sdk.controlpoint.MediaType;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.ACTION_ARGUMENT;
import com.gala.multiscreen.dmr.model.MSMessage.RemoteCode;
import com.google.gson.Gson;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.Device;
import org.cybergarage.upnp.ssdp.SSDPPacket;
import org.cybergarage.util.Debug;

public class Util {
    protected static final String ARGUMENT_VALUE_CHANNEL_MASTER = "Master";
    protected static final String ARGUMENT_VALUE_UNIT_ABSTIME = "ABS_TIME";
    protected static final String ARGUMENT_VALUE_UNIT_REALTIME = "REL_TIME";
    public static final String FUNCTION_TAG_ACTION = "Action";
    public static final String FUNCTION_TAG_CONTENT = "Content";
    public static final String FUNCTION_TAG_DLNA = "DLNA";
    public static final String FUNCTION_TAG_FLAVOR = "Flavor";
    public static final String MANUFACTURER_SHARP = "Sharp Corporation";
    public static final String MANUFACTURER_SONY = "Sony Corporation";
    public static final String MANUFACTURER_SUMSUNG = "Samsung Electronics";
    public static final String MANUFACTURER_XIAOMI = "Xiaomi";
    public static final String MUTE = "1";
    public static final String NOT_IMPLEMENTED = "NOT_IMPLEMENTED";
    public static final String SERVER_TAG_MI = "Teleal-Cling";
    public static final String UNMUTE = "0";
    private static final HashSet<String> UNSPPORT_SERVER_TAGS = new HashSet<String>() {
    };
    private static final Gson mGson = new Gson();
    private static final String sTag;
    private static final String sTagUpper = new String(new byte[]{(byte) 73, RemoteCode.VOLUME_DOWN, (byte) 73, (byte) 89, (byte) 73});

    static {
        byte[] bytes = new byte[]{(byte) 105, (byte) 113, (byte) 105, (byte) 121, (byte) 105};
        sTag = new String(bytes);
        UNSPPORT_SERVER_TAGS.add(MANUFACTURER_SUMSUNG);
        UNSPPORT_SERVER_TAGS.add(MANUFACTURER_SONY);
    }

    public static String toJson(Object obj) {
        return mGson.toJson(obj);
    }

    public static boolean isStdDmrDevice(Device device) {
        if (device == null) {
            return false;
        }
        String deviceType = device.getDeviceType();
        SSDPPacket sSDPPacket = device.getSSDPPacket();
        if (deviceType == null || !deviceType.contains("urn:schemas-upnp-org:device:MediaRenderer:1") || sSDPPacket == null || sSDPPacket.isGalaServer() || isDeviceInBlackList(device.getManufacture())) {
            return false;
        }
        return true;
    }

    public static boolean isDeviceInBlackList(String manufacture) {
        if (TextUtils.isEmpty(manufacture)) {
            return false;
        }
        Iterator it = UNSPPORT_SERVER_TAGS.iterator();
        while (it.hasNext()) {
            String serverTag = (String) it.next();
            if (!TextUtils.isEmpty(serverTag) && manufacture.toLowerCase(Locale.US).contains(serverTag.toLowerCase(Locale.US))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isMiDmrDevice(Device device) {
        if (!isStdDmrDevice(device)) {
            return false;
        }
        String manufacturer = device.getManufacture();
        Debug.message("isMiDmrDevice() manufacture = " + manufacturer);
        if (TextUtils.isEmpty(manufacturer) || !manufacturer.toLowerCase(Locale.US).contains(MANUFACTURER_XIAOMI.toLowerCase(Locale.US))) {
            return false;
        }
        return true;
    }

    public static boolean isValidPositionStr(String PostionStr) {
        if (isEmpty(PostionStr)) {
            return false;
        }
        String separator = SOAP.DELIM;
        if (!PostionStr.contains(separator)) {
            return false;
        }
        String[] strs = PostionStr.split(separator);
        if (strs.length != 3) {
            return false;
        }
        try {
            int first = Integer.parseInt(strs[0]);
            int second = Integer.parseInt(strs[1]);
            int third = Integer.parseInt(strs[2]);
            if (first < 0 || second < 0 || second >= 60 || third < 0 || third >= 60) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isIntStrInRange(String intStr, int min, int max) {
        if (isEmpty(intStr) || min > max) {
            return false;
        }
        try {
            int value = Integer.parseInt(intStr);
            if (value < min || value > max) {
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isEmpty(String str) {
        return TextUtils.isEmpty(str) || "null".equals(str);
    }

    private static String getMetaData(String MediaType, String title) {
        return "<DIDL-Lite xmlns=\"urn:schemas-upnp-org:metadata-1-0/DIDL-Lite/\" xmlns:dc=\"http://purl.org/dc/elements/1.1/\" xmlns:upnp=\"urn:schemas-upnp-org:metadata-1-0/upnp/\"><item id=\"" + title + "\" parentID=\"-1\" restricted=\"1\">" + "<upnp:genre>Unknown</upnp:genre>" + "<upnp:storageMedium>UNKNOWN</upnp:storageMedium>" + "<upnp:writeStatus>UNKNOWN</upnp:writeStatus>" + "<upnp:class>" + MediaType + "</upnp:class>" + "<dc:title>" + title + "</dc:title>" + "</item></DIDL-Lite>";
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getDefaultArgumentValues() {
        Hashtable<ACTION_ARGUMENT, String> argumentValues = new Hashtable();
        argumentValues.put(ACTION_ARGUMENT.InstanceID, "0");
        return argumentValues;
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getPlayArgumentValues() {
        Hashtable<ACTION_ARGUMENT, String> argumentValues = getDefaultArgumentValues();
        argumentValues.put(ACTION_ARGUMENT.Speed, "1");
        return argumentValues;
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getSeekABSArgumentValues(String targetPosition) {
        Hashtable<ACTION_ARGUMENT, String> argumentValues = getDefaultArgumentValues();
        argumentValues.put(ACTION_ARGUMENT.Unit, ARGUMENT_VALUE_UNIT_ABSTIME);
        argumentValues.put(ACTION_ARGUMENT.Target, targetPosition);
        return argumentValues;
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getSeekRELArgumentValues(String targetPosition) {
        Hashtable<ACTION_ARGUMENT, String> argumentValues = getDefaultArgumentValues();
        argumentValues.put(ACTION_ARGUMENT.Unit, ARGUMENT_VALUE_UNIT_REALTIME);
        argumentValues.put(ACTION_ARGUMENT.Target, targetPosition);
        return argumentValues;
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getSoundArgumentValues() {
        Hashtable<ACTION_ARGUMENT, String> argumentValues = getDefaultArgumentValues();
        argumentValues.put(ACTION_ARGUMENT.Channel, "Master");
        return argumentValues;
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getSetMuteArgumentValues(boolean mute) {
        Hashtable<ACTION_ARGUMENT, String> argumentValues = getSoundArgumentValues();
        argumentValues.put(ACTION_ARGUMENT.DesiredMute, mute ? "1" : "0");
        return argumentValues;
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getSetVolumeArgumentValues(int percent) {
        Hashtable<ACTION_ARGUMENT, String> argumentValues = getSoundArgumentValues();
        argumentValues.put(ACTION_ARGUMENT.DesiredVolume, String.valueOf(percent));
        return argumentValues;
    }

    protected static Hashtable<ACTION_ARGUMENT, String> getPushUrlArgumentValues(String path, String title, MediaType type) {
        String metaData = getMetaData(type.getTypeName(), title);
        Hashtable<ACTION_ARGUMENT, String> argumentValues = getDefaultArgumentValues();
        argumentValues.put(ACTION_ARGUMENT.CurrentURI, path);
        argumentValues.put(ACTION_ARGUMENT.CurrentURIMetaData, metaData);
        return argumentValues;
    }

    public static MediaType getMediaTypeByName(String name) {
        if (isEmpty(name)) {
            return null;
        }
        for (MediaType type : MediaType.values()) {
            if (type.name().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static String getPositionStringBySecondStr(String positioninsecond) {
        if (isEmpty(positioninsecond)) {
            return null;
        }
        try {
            long seconds = Long.parseLong(positioninsecond);
            if (seconds < 0) {
                return null;
            }
            long second = seconds % 60;
            long minutes = seconds / 60;
            long minute = minutes % 60;
            long hours = minutes / 60;
            return new StringBuilder(String.valueOf(hours < 9 ? "0" + hours : String.valueOf(hours))).append(SOAP.DELIM).append(minute < 9 ? "0" + minute : String.valueOf(minute)).append(SOAP.DELIM).append(second < 9 ? "0" + second : String.valueOf(second)).toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean getMuteByStr(String str) {
        return Boolean.parseBoolean(str);
    }

    public static int getVolumeByStr(String str) {
        try {
            return Integer.parseInt(str);
        } catch (Exception e) {
            return -1;
        }
    }

    public static boolean hasCode(byte[] data, byte[] mode) {
        if (data == null || mode == null) {
            return false;
        }
        int dataLength = data.length;
        int modeLength = mode.length;
        if (dataLength < modeLength) {
            return false;
        }
        int i = 0;
        while (i < dataLength - modeLength) {
            int j = 0;
            while (j < modeLength && data[i + j] == mode[j]) {
                j++;
            }
            if (j == modeLength) {
                return true;
            }
            i++;
        }
        return false;
    }

    public static String getTag(boolean isUpper) {
        return isUpper ? sTagUpper : sTag;
    }

    public static byte[] copyFromByte(byte[] search, byte[] findStart, byte[] findEnd) {
        if (search == null || findStart == null || search.length == 0 || findStart.length == 0 || search.length < findStart.length) {
            return null;
        }
        int start = byteIndexOf(search, findStart, 0) + 4;
        if (start == -1) {
            return null;
        }
        int end = byteIndexOf(search, findEnd, start);
        if (end == -1) {
            return null;
        }
        if (end < start) {
            Debug.message("Util copyFromByte end < start");
            return null;
        }
        byte[] copy = new byte[(end - start)];
        System.arraycopy(search, start, copy, 0, end - start);
        return copy;
    }

    public static int byteIndexOf(byte[] search, byte[] find, int start) {
        if (search == null || find == null || search.length == 0 || find.length == 0 || search.length < find.length) {
            return -1;
        }
        int i = start;
        while (i < (search.length - find.length) + 1) {
            if (search[i] == find[0]) {
                int j = 1;
                while (j < find.length && search[i + j] == find[j]) {
                    j++;
                }
                if (j == find.length) {
                    return i;
                }
            }
            i++;
        }
        return -1;
    }
}
