package com.gala.video.lib.share.pingback;

import android.content.Context;
import android.util.Log;
import com.gala.sdk.player.PlayParams;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.common.activity.QBaseActivity;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginPingbackUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

public class PingbackUtils {
    private static final String TAG = "PingbackUtils";
    private static PingbackPage mCurrentPingbackPage = PingbackPage.HomePage;
    private static WeakHashMap<Context, WeakReference<PingbackPage>> mPingbackPage = new WeakHashMap();

    public static String getPageS2(Context context, String suffix) {
        PingbackPage pingbackPage = getPingbackPage(context);
        String s2 = "";
        if (suffix == null) {
            suffix = "";
        }
        switch (pingbackPage) {
            case HomePage:
                return PingBackUtils.getTabName() + suffix;
            case SoloTab:
                return SoloTabPingbackUitls.getInstance().getS2();
            case MultiSubject:
                return MultiSubjectPingbackUitls.getInstance().getS2();
            case Ucenter:
                return LoginPingbackUtils.getInstance().getS2();
            case AlbumDetail:
                return AlbumDetailPingbackUtils.getInstance().getS2();
            case DetailAll:
                return AlbumDetailPingbackUtils.getInstance().getAllViewS2();
            default:
                return s2;
        }
    }

    public static String getPageS1(Context context) {
        String s1 = "";
        switch (getPingbackPage(context)) {
            case MultiSubject:
                return MultiSubjectPingbackUitls.getInstance().getS1();
            default:
                return s1;
        }
    }

    public static String getTabSource(Context context) {
        String tabSource = "";
        switch (getPingbackPage(context)) {
            case HomePage:
            case SoloTab:
            case MultiSubject:
            case Ucenter:
                return "tab_" + PingBackUtils.getTabName();
            case AlbumDetail:
            case DetailAll:
                return AlbumDetailPingbackUtils.getInstance().getTabSrc();
            default:
                return tabSource;
        }
    }

    public static PlayParams getPlayParmas(Context context, IMultiSubjectInfoModel multiSubjectInfoModel) {
        switch (getPingbackPage(context)) {
            case AlbumDetail:
            case DetailAll:
                PlayParams playParams = new PlayParams();
                playParams.h5PlayType = multiSubjectInfoModel.getPlayType();
                playParams.playListId = multiSubjectInfoModel.getItemId();
                return playParams;
            default:
                return null;
        }
    }

    public static PingbackPage getPingbackPage(Context context) {
        WeakReference<PingbackPage> pw = (WeakReference) mPingbackPage.get(context);
        if (pw != null) {
            PingbackPage pingbackPage = (PingbackPage) pw.get();
            Log.d(TAG, "cache getPingbackPage = " + pingbackPage);
            mCurrentPingbackPage = pingbackPage;
            return pingbackPage;
        }
        if (context instanceof QBaseActivity) {
            pingbackPage = ((QBaseActivity) context).getPingbackPage();
        } else {
            pingbackPage = PingbackPage.HomePage;
            Log.e(TAG, "You should set PingbackPage type int onCreate method of your Activity,the default type is HomePage");
        }
        if (pingbackPage == null) {
            pingbackPage = PingbackPage.HomePage;
        } else {
            mPingbackPage.put(context, new WeakReference(pingbackPage));
            mCurrentPingbackPage = pingbackPage;
        }
        return pingbackPage;
    }

    public static void forceSetSpecialPingbackPage(Context context, PingbackPage forcePingbackPage) {
        PingbackPage pingbackPage;
        WeakReference<PingbackPage> pw = (WeakReference) mPingbackPage.get(context);
        if (pw != null) {
            pingbackPage = (PingbackPage) pw.get();
            Log.d(TAG, "forceSetSpecialPingbackPage = " + forcePingbackPage + "  pingbackPage = " + pingbackPage);
            if (pingbackPage != forcePingbackPage) {
                pingbackPage = forcePingbackPage;
            }
        } else {
            Log.d(TAG, "forceSetSpecialPingbackPage mPingbackPage is null case");
            pingbackPage = forcePingbackPage;
            if (context instanceof QBaseActivity) {
                ((QBaseActivity) context).setPingbackPage(pingbackPage);
            }
        }
        mPingbackPage.put(context, new WeakReference(pingbackPage));
        mCurrentPingbackPage = pingbackPage;
    }

    public static PingbackPage getCurrentPingbackPage() {
        return mCurrentPingbackPage;
    }
}
