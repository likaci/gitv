package com.gala.video.app.epg.ui.setting.utils;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;

public class SettingPingbackUtils {
    private static final String LOG_TAG = "EPG/SettingPingbackUtils";

    public static void feedbackPageShow() {
        sendMyPageShow("1", "", "suggestfb", "", "", null, "", "", "", "", "suggestfb", null, "", "", "", "", "", null, "", "", "", "", "");
    }

    public static void feedbackButtonClick() {
        String rt = "i";
        String str = "";
        sendMyPageClick(str, "suggestfb", rt, "contact", "suggestfb", null, "", "", "", "", "", null, "", "", "", "", "", null, "", "", "", "", "");
    }

    private static void sendMyPageClick(String r, String block, String rt, String rseat, String rpage, System nullValueA, String isprevue, String c1, String plid, String letter_exist, String c2, System nullValueB, String count, String e, String rfr, String tabid, String tvsrchsource, System nullValueC, String keyword, String now_c1, String now_qpid, String now_c2, String state) {
        LogUtils.m1576i(LOG_TAG, "sendMyPageClick, r = ", r, " block = ", block, " rt = ", rt, " rseat = ", rseat, " rpage = ", rpage, " isprevue = ", isprevue, " c1 = ", c1, " plid = ", plid, " letter_exist = ", letter_exist, " c2 = ", c2, " count = ", count, " s1 = ", "", " e = ", e, " rfr = ", rfr, " tabid = ", tabid, " tvsrchsource = ", tvsrchsource, " keyword = ", keyword);
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "20").add("r", r).add("isprevue", isprevue).add("rseat", rseat).add("rpage", rpage).add("block", block).add("rt", "i").add("plid", plid).add("c1", c1).add("letter_exist", letter_exist).add("c2", c2).add("count", count).add("e", e).add("rfr", rfr).add("tabid", tabid).add("tvsrchsource", tvsrchsource).add("keyword", keyword).add("now_c1", now_c1).add("now_qpid", now_qpid).add("now_c2", now_c2).add("state", state);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private static void sendMyPageShow(String bstp, String c1, String qtcurl, String qpId, String rfr, System nullValueA, String showpay, String showbuyvip, String e, String td, String block, System nullValueB, String plid, String c2, String qy_prv, String r, String s2, System nullValueC, String tabid, String rlink, String tvsrchsource, String card, String tvlogin) {
        LogUtils.m1576i(LOG_TAG, "sendMyPageShow, bstp = ", bstp, " c1 = ", c1, " qtcurl = ", qtcurl, " qpId = ", qpId, " rfr = ", rfr, " showpay = ", showpay, " showbuyvip = ", showbuyvip, " e = ", e, " td = ", td, " block = ", block, " plid = ", plid, " c2 = ", c2, " qy_prv = ", qy_prv, " r = ", r, " s2 = ", s2, " tabid = ", tabid, " rlink = ", rlink, " tvsrchsource = ", tvsrchsource, " card = ", card);
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "21").add("bstp", "1").add("c1", c1).add("qpid", qpId).add("qtcurl", qtcurl).add("block", block).add("rfr", rfr).add("showpay", showpay).add("showbuyvip", showbuyvip).add("e", e).add("td", td).add("plid", plid).add("c2", c2).add("qy_prv", qy_prv).add("r", r).add("s2", s2).add("tabid", tabid).add("rlink", rlink).add("tvsrchsource", tvsrchsource).add("card", card).add("tvlogin", tvlogin);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public static void apiError(ApiException exception, String apiname) {
        String s1 = "";
        String r = "";
        String ec = "315008";
        String pfec = exception != null ? exception.getCode() : "";
        String errurl = exception != null ? exception.getUrl() : "";
        String e = PingBackUtils.createEventId();
        String playmode = "";
        String isWindow = "";
        String vvfrom = "";
        String ichannel_name = "";
        String news_type = "";
        String plid = "";
        String s2 = "";
        String qy_prv = "";
        String player = "";
        String c1 = "";
        String ra = "";
        String errdetail = "";
        String sdkv = "";
        String erreason = "";
        String activity = "";
        String excptnnm = "";
        String crashtype = "";
        PingBackParams params = new PingBackParams();
        params.add(Keys.f2035T, "0").add("ec", ec).add("pfec", pfec).add(Keys.ERRURL, errurl).add("e", e);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
