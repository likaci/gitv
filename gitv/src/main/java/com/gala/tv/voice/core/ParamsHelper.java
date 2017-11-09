package com.gala.tv.voice.core;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import com.gala.tv.voice.core.Params.Extras;
import com.gala.tv.voice.core.Params.ResultDataType;
import java.util.ArrayList;

public class ParamsHelper {
    private ParamsHelper() {
    }

    public static Intent getStartIntent(Context context, String str, String str2, int i) {
        Intent intent = new Intent("com.gala.tv.sdk.ACTION_VOICE_SERVICE");
        intent.setPackage(str);
        intent.putExtra(Extras.EXTRA_CUSTOMER_VERSION_NAME, str2);
        intent.putExtra(Extras.EXTRA_CUSTOMER_VERSION_CODE, i);
        intent.putExtra(Extras.EXTRA_CUSTOMER_PACKAGE_NAME, context.getPackageName());
        VoiceUtils.dumpIntent("getStartIntent()", intent);
        return intent;
    }

    public static String getClientVersionName(Bundle bundle) {
        if (bundle != null) {
            return bundle.getString(Extras.EXTRA_CUSTOMER_VERSION_NAME);
        }
        return null;
    }

    public static int getClientVersionCode(Bundle bundle) {
        if (bundle != null) {
            return bundle.getInt(Extras.EXTRA_CUSTOMER_VERSION_CODE, -1);
        }
        return -1;
    }

    public static String getClientPackageName(Bundle bundle) {
        if (bundle != null) {
            return bundle.getString(Extras.EXTRA_CUSTOMER_PACKAGE_NAME);
        }
        return null;
    }

    public static int parseResultCode(Bundle bundle, int i) {
        VoiceUtils.dumpBundle("ParamsHelper", "parseResultCode()", bundle);
        if (bundle != null && bundle.containsKey(Extras.EXTRA_RESULT_CODE)) {
            i = bundle.getInt(Extras.EXTRA_RESULT_CODE, i);
        }
        Log.d("ParamsHelper", "parseResultCode() return " + i);
        return i;
    }

    public static void setResultCode(Bundle bundle, int i) {
        if (bundle != null) {
            bundle.putInt(Extras.EXTRA_RESULT_CODE, i);
        }
        Log.d("ParamsHelper", "setResultCode(" + i + ")");
    }

    public static <T> T parseResultData(Bundle bundle) {
        VoiceUtils.dumpBundle("ParamsHelper", "parseResultData()", bundle);
        if (bundle == null) {
            return null;
        }
        int i = bundle.getInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_UNKNOWN);
        if (ResultDataType.RESULT_TYPE_PARCELABLE_ITEM == i) {
            return bundle.getParcelable(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        if (ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY == i) {
            return bundle.getParcelableArray(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        if (ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY_LIST == i) {
            return bundle.getParcelableArrayList(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        if (ResultDataType.RESULT_TYPE_STRING_ARRAY_LIST == i) {
            return bundle.getStringArrayList(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        if (ResultDataType.RESULT_TYPE_BOOLEAN == i) {
            return bundle.get(Extras.EXTRA_RESULT_DATA_VALUE);
        }
        return null;
    }

    public static int parseResultDataType(Bundle bundle) {
        int i = ResultDataType.RESULT_TYPE_UNKNOWN;
        if (bundle != null) {
            bundle.getInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_UNKNOWN);
        }
        return i;
    }

    public static <T extends Parcelable> void setResultData(Bundle bundle, T t) {
        Log.d("ParamsHelper", "setResultData(" + t + ")");
        if (bundle != null) {
            bundle.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_PARCELABLE_ITEM);
            bundle.putParcelable(Extras.EXTRA_RESULT_DATA_VALUE, t);
        }
        VoiceUtils.dumpBundle("ParamsHelper", "setResultData()", bundle);
    }

    public static void setResultDataOfArrayString(Bundle bundle, ArrayList<String> arrayList) {
        Log.d("ParamsHelper", "setResultDataOfArrayString(" + arrayList + ")");
        if (bundle != null) {
            bundle.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_STRING_ARRAY_LIST);
            bundle.putStringArrayList(Extras.EXTRA_RESULT_DATA_VALUE, arrayList);
        }
        VoiceUtils.dumpBundle("ParamsHelper", "setResultDataOfArrayString()", bundle);
    }

    public static <T extends Parcelable> void setResultData(Bundle bundle, ArrayList<T> arrayList) {
        Log.d("ParamsHelper", "setResultData(" + arrayList + ")");
        if (bundle != null) {
            bundle.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY_LIST);
            bundle.putParcelableArrayList(Extras.EXTRA_RESULT_DATA_VALUE, arrayList);
        }
        VoiceUtils.dumpBundle("ParamsHelper", "setResultData()", bundle);
    }

    public static <T extends Parcelable> void setResultData(Bundle bundle, T[] tArr) {
        Log.d("ParamsHelper", "setResultData(" + tArr + ")");
        if (bundle != null) {
            bundle.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_PARCELABLE_ARRAY);
            bundle.putParcelableArray(Extras.EXTRA_RESULT_DATA_VALUE, tArr);
        }
        VoiceUtils.dumpBundle("ParamsHelper", "setResultData()", bundle);
    }

    public static void setResultData(Bundle bundle, boolean z) {
        Log.d("ParamsHelper", "setResultData(" + z + ")");
        if (bundle != null) {
            bundle.putInt(Extras.EXTRA_RESULT_DATA_TYPE, ResultDataType.RESULT_TYPE_BOOLEAN);
            bundle.putBoolean(Extras.EXTRA_RESULT_DATA_VALUE, z);
        }
        VoiceUtils.dumpBundle("ParamsHelper", "setResultData()", bundle);
    }

    public static void setKeyword(Bundle bundle, String str) {
        bundle.putString(Extras.EXTRA_KEYWORD, str);
    }

    public static String parseKeyword(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_KEYWORD);
    }

    public static void setOperationTarget(Bundle bundle, int i) {
        bundle.putInt(Extras.EXTRA_OPERATION_TARGET, i);
    }

    public static int parseOperationTarget(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_OPERATION_TARGET);
    }

    public static void setOperationType(Bundle bundle, int i) {
        bundle.putInt(Extras.EXTRA_OPERATION_TYPE, i);
    }

    public static int parseOperationType(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_OPERATION_TYPE);
    }

    public static void setChannelName(Bundle bundle, String str) {
        bundle.putString(Extras.EXTRA_CHANNEL_NAME, str);
    }

    public static String parseChannelName(Bundle bundle) {
        return bundle.getString(Extras.EXTRA_CHANNEL_NAME);
    }

    public static void setEpisodeIndex(Bundle bundle, int i) {
        bundle.putInt(Extras.EXTRA_EPISODE_INDEX, i);
    }

    public static int parseEpisodeIndex(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_EPISODE_INDEX);
    }

    public static void setRow(Bundle bundle, int i) {
        bundle.putInt(Extras.EXTRA_ROW, i);
    }

    public static int parseRow(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_ROW);
    }

    public static void setColumn(Bundle bundle, int i) {
        bundle.putInt(Extras.EXTRA_COLUMN, i);
    }

    public static int parseColumn(Bundle bundle) {
        return bundle.getInt(Extras.EXTRA_COLUMN);
    }
}
