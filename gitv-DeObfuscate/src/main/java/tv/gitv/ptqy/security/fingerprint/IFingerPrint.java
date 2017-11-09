package tv.gitv.ptqy.security.fingerprint;

import android.content.Context;
import tv.gitv.ptqy.security.fingerprint.callback.FingerPrintCallBack;
import tv.gitv.ptqy.security.fingerprint.exception.FingerPrintException;

public interface IFingerPrint {
    String getEnvInfo(Context context);

    String getFingerPrint(Context context, FingerPrintCallBack fingerPrintCallBack) throws FingerPrintException;
}
