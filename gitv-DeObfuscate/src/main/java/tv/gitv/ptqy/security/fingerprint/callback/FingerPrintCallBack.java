package tv.gitv.ptqy.security.fingerprint.callback;

public interface FingerPrintCallBack {
    void onFailed(String str);

    void onSuccess(String str);
}
