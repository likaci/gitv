package com.gala.video.lib.share.ifimpl.ucenter.account.utils;

import com.gala.video.api.ApiException;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;

public class LoginPingbackUtils {
    private static LoginPingbackUtils mSelf;
    private String mS1;
    private String mS2;

    public static LoginPingbackUtils getInstance() {
        if (mSelf == null) {
            mSelf = new LoginPingbackUtils();
        }
        return mSelf;
    }

    public void payCode_activateSucc(String s2, long offTime) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "11").add("ct", "150619_code").add("s2", s2);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public void pageClick(String block, String rseat, String rpage, String s1) {
        PingBackParams params1 = new PingBackParams();
        params1.add(Keys.T, "20").add("block", block).add("rt", "i").add("rseat", rseat).add("rpage", rpage).add("s1", s1);
        PingBack.getInstance().postPingBackToLongYuan(params1.build());
    }

    public void pageDisplay(String qtcurl, String block, boolean isTvLogin, String s1) {
        String tvlogin = "";
        if (isTvLogin) {
            if (Project.getInstance().getBuild().isOpenKeyboardLogin()) {
                tvlogin = "1";
            } else {
                tvlogin = "0";
            }
        }
        PingBackParams params1 = new PingBackParams();
        params1.add(Keys.T, "21").add("bstp", "1").add("qtcurl", qtcurl).add("block", block).add("tvlogin", tvlogin).add("s1", s1);
        PingBack.getInstance().postPingBackToLongYuan(params1.build());
    }

    public void logOut(String s1, String lgttype) {
        PingBackParams params1 = new PingBackParams();
        params1.add(Keys.T, "5").add("a", "8").add("s1", s1).add(Keys.LGTTYPE, lgttype);
        PingBack.getInstance().postPingBackToLongYuan(params1.build());
    }

    public void logSucc(String param_type, String s1) {
        PingBackParams params1 = new PingBackParams();
        params1.add(Keys.T, "5").add("a", param_type).add("s1", s1);
        PingBack.getInstance().postPingBackToLongYuan(params1.build());
    }

    public void errorPingback(String ec, String pfec, String apiname, ApiException exception) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "0").add("ec", ec).add("pfec", pfec).add(Keys.ERRURL, exception != null ? exception.getUrl() : "").add("e", PingBackUtils.createEventId()).add(Keys.APINAME, apiname);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public void setS2(String var) {
        this.mS2 = var;
    }

    public String getS2() {
        return this.mS2;
    }
}
