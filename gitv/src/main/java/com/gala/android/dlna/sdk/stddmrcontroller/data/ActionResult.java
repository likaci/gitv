package com.gala.android.dlna.sdk.stddmrcontroller.data;

import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import com.gala.android.dlna.sdk.stddmrcontroller.enums.RESULT_DESCRIPTION;

public class ActionResult {
    private final boolean isSuccessful;
    private final RESULT_DESCRIPTION mResultDescription;
    private final String mResultStr;

    public ActionResult(boolean isSuccessful, String resultStr, RESULT_DESCRIPTION resultDescription) {
        this.isSuccessful = isSuccessful;
        this.mResultStr = resultStr;
        this.mResultDescription = resultDescription;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }

    public String getResultString() {
        return this.mResultStr;
    }

    public RESULT_DESCRIPTION getResultDescription() {
        return this.mResultDescription;
    }

    public String toString() {
        return Util.toJson(this);
    }
}
