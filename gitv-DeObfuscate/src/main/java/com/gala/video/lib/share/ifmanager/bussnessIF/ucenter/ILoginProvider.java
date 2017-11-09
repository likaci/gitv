package com.gala.video.lib.share.ifmanager.bussnessIF.ucenter;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface ILoginProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements ILoginProvider {
        public Object getInterface() {
            return this;
        }

        public static ILoginProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof ILoginProvider)) {
                return null;
            }
            return (ILoginProvider) wrapper;
        }
    }

    void startActivateActivity(Context context, String str, int i);

    void startActivateActivityOpenApi(Context context, String str, int i);

    void startLoginActivity(Context context, String str, int i);

    void startLoginActivity(Context context, String str, int i, int i2);

    void startLoginActivityForCoupon(Context context, String str, String str2, String str3, String str4, String str5, int i);

    void startLoginActivityOpenApi(Context context, int i);

    void startLoginForAlbum(Context context, int i);

    void startUcenterActivity(Context context);

    void startUcenterActivityFromCardSetting(Context context);

    void startUcenterActivityFromHomeTab(Context context);

    void startUcenterActivityFromSetting(Context context);

    void startUcenterActivityFromSettingLayer(Context context);

    void startUcenterActivityFromSoloTab(Context context);

    void startVipGuideActivity(Context context, int i);
}
