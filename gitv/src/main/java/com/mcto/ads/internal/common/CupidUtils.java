package com.mcto.ads.internal.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.mcto.ads.constants.Interaction;
import com.mcto.ads.internal.net.PingbackConstants;
import com.tvos.apps.utils.DateUtil;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;
import org.xbill.DNS.Message;

public class CupidUtils {
    private static final int MAX_RESPONSE_LENGTH = 300;
    private static Map<String, Object> status = new HashMap();

    public static String join(List<String> list, String separator) {
        if (list == null || separator == null) {
            return null;
        }
        if (list.isEmpty()) {
            return "";
        }
        StringBuilder buf = new StringBuilder();
        int listSize = list.size();
        for (int i = 0; i < listSize; i++) {
            buf.append(((String) list.get(i)).toString());
            if (i != listSize - 1) {
                buf.append(separator);
            }
        }
        return buf.toString();
    }

    public static JSONArray generateJsonArray(List<String> strings) {
        JSONArray array = new JSONArray();
        if (strings == null) {
            return array;
        }
        try {
            if (strings.isEmpty()) {
                return array;
            }
            for (String string : strings) {
                array.put(string);
            }
            return array;
        } catch (Exception ex) {
            Log.d("a71_ads_client", "generateJsonArray(): ", ex);
            return null;
        }
    }

    public static String getCurrentTime() {
        try {
            return new SimpleDateFormat(DateUtil.PATTERN_STANDARD19H, Locale.getDefault()).format(new Date());
        } catch (Exception ex) {
            Log.d("a71_ads_client", "getCurrentTime(): ", ex);
            return null;
        }
    }

    public static String getLightResponse(String heavyResponse) {
        String rtn = "";
        if (heavyResponse == null) {
            return rtn;
        }
        if (heavyResponse.length() > 300) {
            String pre = heavyResponse.substring(0, 150);
            rtn = pre + "||" + heavyResponse.substring(heavyResponse.length() - 150);
        } else {
            rtn = heavyResponse;
        }
        return rtn;
    }

    public static String getFirstPart(String str, String separator) {
        String rtn = str;
        if (str == null || separator == null) {
            return rtn;
        }
        int index = str.indexOf(separator);
        if (-1 != index) {
            rtn = str.substring(0, index);
        }
        return rtn;
    }

    public static String strReverse(String str) {
        String rtn = "";
        if (!isValidStr(str)) {
            return rtn;
        }
        StringBuffer stringBuffer = new StringBuffer(str);
        if (stringBuffer == null) {
            return rtn;
        }
        return stringBuffer.reverse().toString();
    }

    public static int stringToInt(String str) {
        int rtn = -1;
        if (str != null) {
            try {
                rtn = Integer.parseInt(str);
            } catch (NumberFormatException ex) {
                Log.d("a71_ads_client", "stringToInt(): ", ex);
            }
        }
        return rtn;
    }

    public static int objectToInt(Object value) {
        int rtn = -1;
        try {
            rtn = ((Integer) value).intValue();
        } catch (Exception ex) {
            Log.d("a71_ads_client", "objectToInt(): ", ex);
        }
        return rtn;
    }

    public static int generateResultId(int result_id_seed) {
        return (result_id_seed % Message.MAXLENGTH) << 16;
    }

    public static int generateSlotId(int resultId, int seed) {
        return (seed << 8) | resultId;
    }

    public static int generateAdId(int slotId, int seed) {
        return slotId | seed;
    }

    public static int getSlotIdByAdId(int adId) {
        return adId & SettingConstants.ID_GROUP_MASK;
    }

    public static int getResultIdByAdId(int adId) {
        return -65536 & adId;
    }

    public static int getResultIdBySlotId(int slotId) {
        return -65536 & slotId;
    }

    public static synchronized void setSdkStatus(Map<String, Object> sdkStatus) {
        synchronized (CupidUtils.class) {
            if (sdkStatus != null) {
                Object passportId = sdkStatus.get(PingbackConstants.PASSPORT_ID);
                if (passportId != null) {
                    if (String.valueOf(passportId).equals("0")) {
                        status.put(PingbackConstants.PASSPORT_ID, "");
                    } else {
                        status.put(PingbackConstants.PASSPORT_ID, passportId);
                    }
                }
            }
        }
    }

    public static synchronized String getPassportId() {
        String rtn;
        synchronized (CupidUtils.class) {
            rtn = null;
            if (!status.isEmpty()) {
                Object passportId = status.get(PingbackConstants.PASSPORT_ID);
                if (passportId != null) {
                    rtn = String.valueOf(passportId);
                }
            }
        }
        return rtn;
    }

    public static Map<String, Object> convertJson2Map(JSONObject jsonObj) {
        Map<String, Object> map = new HashMap();
        if (jsonObj != null) {
            try {
                Iterator<?> it = jsonObj.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    map.put(key, jsonObj.get(key));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    public static String md5(String str) {
        byte[] byteMd5 = new byte[0];
        try {
            byteMd5 = MessageDigest.getInstance("MD5").digest(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e2) {
            e2.printStackTrace();
        }
        char[] toDigits = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int l = byteMd5.length;
        char[] out = new char[(l << 1)];
        int j = 0;
        for (int i = 0; i < l; i++) {
            int i2 = j + 1;
            out[j] = toDigits[(byteMd5[i] & 240) >>> 4];
            j = i2 + 1;
            out[i2] = toDigits[byteMd5[i] & 15];
        }
        return new String(out);
    }

    public static boolean isValidStr(String str) {
        if (str == null || str.equals("")) {
            return false;
        }
        return true;
    }

    public static void setJsonKeyValue(JSONStringer jsonStringer, String key, String value) throws JSONException {
        if (jsonStringer != null && isValidStr(key) && isValidStr(value)) {
            jsonStringer.key(key).value(value);
        }
    }

    public static void setDebugTime(Context context, String requestId, int debugTime) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences("cupid_private", 0);
            String localRequestId = sharedPreferences.getString(JsonBundleConstants.REQUEST_ID, "");
            if (!isValidStr(localRequestId) || !localRequestId.equals(requestId)) {
                Editor editor = sharedPreferences.edit();
                editor.putInt(Interaction.KEY_DEBUG_TIME, debugTime);
                editor.putString(JsonBundleConstants.REQUEST_ID, requestId);
                editor.commit();
            }
        }
    }

    public static int getDebugTime(Context context, String requestId) {
        int debugTime = Integer.MAX_VALUE;
        if (context == null) {
            return Integer.MAX_VALUE;
        }
        SharedPreferences sharedPreferences = context.getSharedPreferences("cupid_private", 0);
        String localRequestId = sharedPreferences.getString(JsonBundleConstants.REQUEST_ID, "");
        if (isValidStr(localRequestId) && localRequestId.equals(requestId)) {
            debugTime = sharedPreferences.getInt(Interaction.KEY_DEBUG_TIME, Integer.MAX_VALUE);
        }
        return debugTime;
    }
}
