package com.gala.video.lib.share.ifimpl.openplay.service.tools;

import android.os.Bundle;
import android.os.Parcelable;
import com.gala.video.lib.share.ifimpl.openplay.service.ServerParamsHelper;
import java.util.ArrayList;

public class OpenApiResultCreater {
    public static <T extends Parcelable> Bundle createResultBundle(int code) {
        Bundle bundle = new Bundle();
        ServerParamsHelper.setResultCode(bundle, code);
        return bundle;
    }

    public static <T extends Parcelable> Bundle createResultBundle(int code, T data) {
        Bundle bundle = new Bundle();
        ServerParamsHelper.setResultCode(bundle, code);
        ServerParamsHelper.setResultData(bundle, (Parcelable) data);
        return bundle;
    }

    public static <T extends Parcelable> Bundle createResultBundle(int code, ArrayList<T> dataList) {
        Bundle bundle = new Bundle();
        ServerParamsHelper.setResultCode(bundle, code);
        ServerParamsHelper.setResultData(bundle, (ArrayList) dataList);
        return bundle;
    }

    public static Bundle createResultBundleOfString(int code, ArrayList<String> dataList) {
        Bundle bundle = new Bundle();
        ServerParamsHelper.setResultCode(bundle, code);
        ServerParamsHelper.setResultDataOfArrayString(bundle, dataList);
        return bundle;
    }
}
