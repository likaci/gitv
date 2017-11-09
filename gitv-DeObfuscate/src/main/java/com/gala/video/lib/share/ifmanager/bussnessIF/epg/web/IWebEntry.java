package com.gala.video.lib.share.ifmanager.bussnessIF.epg.web;

import android.content.Context;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import java.util.ArrayList;
import java.util.List;

public interface IWebEntry extends IInterfaceWrapper {

    public static abstract class Wrapper implements IWebEntry {
        public Object getInterface() {
            return this;
        }

        public static IWebEntry asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IWebEntry)) {
                return null;
            }
            return (IWebEntry) wrapper;
        }
    }

    void gotoCommonWebActivity(Context context, WebIntentParams webIntentParams);

    void gotoCommonWebActivity(Context context, String str);

    void gotoGetGoldActivity(Context context);

    void gotoMultiscreenActivity(Context context);

    void gotoRoleWebActivity(Context context);

    void gotoSearch(Context context);

    void gotoSubject(Context context, WebIntentParams webIntentParams);

    void gotoSubject(Context context, String str, String str2, String str3);

    void gotoSubject(Context context, String str, String str2, String str3, String str4);

    void gotoSubjectForLive(Context context, Album album, ArrayList<Album> arrayList, String str);

    void gotoSubjectForLive(Context context, Album album, ArrayList<Album> arrayList, String str, String str2);

    void gotoSubjectNotice(Context context, List<Album> list, String str);

    void gotoSubjectNotice(Context context, List<Album> list, String str, String str2);

    void onClickWebURI(Context context, String str);

    void startCouponActivity(Context context, WebIntentParams webIntentParams);

    void startFaqActivity(Context context);

    void startMemberRightsPage(Context context);

    void startMemberRightsPage(Context context, WebIntentParams webIntentParams);

    void startPurchasePage(Context context);

    void startPurchasePage(Context context, WebIntentParams webIntentParams);

    void startSignInActivity(Context context);
}
