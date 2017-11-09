package com.gala.video.app.epg.ui.multisubject.util;

import com.gala.video.app.epg.HomeDebug;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class MultiSubjectPingbackUtils {
    private static final String LOG_TAG = "EPG/multisubject/MultiSubjectPingbackUtils";

    public static class CardShowPingbackModel {
        public String allitem;
        public String block;
        public String dftitem;
        public String f1938e;
        public String line;
        public String plid;
        public String qpid;
        public String s1;
        public String s2;
        public String sawitem;

        public String toString() {
            return "CardShowPingbackModel [block=" + this.block + ", qpid=" + this.qpid + ", plid=" + this.plid + ", e=" + this.f1938e + ", s1=" + this.s1 + ", s2=" + this.s2 + ", allitem=" + this.allitem + ", dftitem=" + this.dftitem + ", sawitem=" + this.sawitem + ", line=" + this.line + AlbumEnterFactory.SIGN_STR;
        }
    }

    public static class PageClickPingbackModel {
        public String allitem;
        public String block;
        public String dftitem;
        public String f1939e;
        public String line;
        public String plid;
        public String f1940r;
        public String rseat;
        public String s2;

        public String toString() {
            return "PageClickPingbackModel [plid=" + this.plid + ", s2=" + this.s2 + ", e=" + this.f1939e + ", allitem=" + this.allitem + ", dftitem=" + this.dftitem + ", line=" + this.line + ", block=" + this.block + ", rseat=" + this.rseat + ", r=" + this.f1940r + AlbumEnterFactory.SIGN_STR;
        }
    }

    public static class PageShowPingbackModel {
        public String f1941e;
        public String plid;
        public String s1;
        public String s2;

        public String toString() {
            return "PageShowPingbackModel [e=" + this.f1941e + ", s1=" + this.s1 + ", s2=" + this.s2 + ", plid=" + this.plid + AlbumEnterFactory.SIGN_STR;
        }
    }

    public static void sendPageShowPingback(PageShowPingbackModel model) {
        if (model != null) {
            String bstp = "1";
            String tabsrc = PingBackUtils.getTabSrc();
            String qtcurl = MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI;
            String block = MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI;
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.m1576i(LOG_TAG, "sendPageShowPingback --- tabsrc = ", tabsrc, " qtcurl = ", qtcurl, " block = ", block, " model = ", model);
            }
            PingBackParams params = new PingBackParams();
            params.add("bstp", bstp).add("e", model.f1941e).add("s1", model.s1).add("s2", model.s2).add("plid", model.plid).add("tabsrc", tabsrc).add("qtcurl", qtcurl).add("block", block).add(Keys.f2035T, "21");
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    public static void sendCardShowPingback(CardShowPingbackModel model) {
        if (model != null) {
            String bstp = "1";
            String tabsrc = PingBackUtils.getTabSrc();
            String qtcurl = MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI;
            if (HomeDebug.DEBUG_LOG) {
                LogUtils.m1576i(LOG_TAG, "sendCardShowPingback --- model = ", model);
            }
            PingBackParams params = new PingBackParams();
            params.add("bstp", bstp).add("e", model.f1938e).add("s1", model.s1).add("plid", model.plid).add("s2", model.s2).add("tabsrc", tabsrc).add("qtcurl", qtcurl).add("block", model.block).add("qpid", model.qpid).add("allitem", model.allitem).add("dftitem", model.dftitem).add("sawitem", model.sawitem).add("line", model.line).add(Keys.f2035T, "21");
            PingBack.getInstance().postPingBackToLongYuan(params.build());
        }
    }

    public static void sendPageClickPingback(PageClickPingbackModel model) {
        String rt = "i";
        String rpage = MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI;
        if (HomeDebug.DEBUG_LOG) {
            LogUtils.m1576i(LOG_TAG, "sendPageClickPingback --- model = ", model);
        }
        PingBackParams params = new PingBackParams();
        params.add("allitem", model.allitem).add("block", model.block).add("dftitem", model.dftitem).add("e", model.f1939e).add("line", model.line).add("plid", model.plid).add("r", model.f1940r).add("rseat", model.rseat).add("s2", model.s2).add("rt", rt).add("rpage", rpage);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
