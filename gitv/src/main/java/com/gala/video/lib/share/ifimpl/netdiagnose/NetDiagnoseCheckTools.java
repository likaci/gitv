package com.gala.video.lib.share.ifimpl.netdiagnose;

import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDDoneListener;
import com.gala.video.lib.framework.coreservice.netdiagnose.INDUploadCallback;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.DNSJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.NetConnDiagnoseJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.job.ThirdSpeedTestJob;
import com.gala.video.lib.framework.coreservice.netdiagnose.model.NetDiagnoseInfo;
import com.gala.video.lib.framework.coreservice.netdiagnose.wrapper.NDBaseWrapper;
import com.gala.video.lib.share.ifimpl.netdiagnose.job.CollectInfoJob;
import com.gala.video.lib.share.ifimpl.netdiagnose.job.DynamicNsLookUpJob;
import com.gala.video.lib.share.ifimpl.netdiagnose.job.NsLookUpJob;
import com.gala.video.lib.share.ifimpl.netdiagnose.job.PingJob;
import com.gala.video.lib.share.ifimpl.netdiagnose.job.TracerouteJob;
import com.gala.video.lib.share.ifimpl.netdiagnose.wrapper.CDNWrapper;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;

public class NetDiagnoseCheckTools {
    public static final int ND_CDN = 2;
    public static final int ND_COLLECT = 7;
    public static final int ND_DNS = 5;
    public static final int ND_NETCONN = 1;
    public static final int ND_NSLOOkUP = 6;
    public static final int ND_THIRD_SPEED = 3;
    public static final int ND_TRACE_ROUTE = 4;
    public static final String NO_CHECK_FLAG = "*";

    public static NDBaseWrapper getNetConnWrapper(NetDiagnoseInfo info, INDDoneListener listener) {
        NDBaseWrapper netConn = new NDBaseWrapper(false);
        netConn.setJob(new NetConnDiagnoseJob(info));
        netConn.setDoneListener(listener);
        return netConn;
    }

    public static NDBaseWrapper getCdnWrapper(NetDiagnoseInfo info, int playerType, INDDoneListener listener, INDUploadCallback callback) {
        CDNWrapper cdn = new CDNWrapper(playerType, true);
        cdn.setDoneListener(listener);
        cdn.setNDUploadCallback(callback);
        return cdn;
    }

    public static NDBaseWrapper getThirdSpeedWrapper(NetDiagnoseInfo info, INDDoneListener listener) {
        NDBaseWrapper tst = new NDBaseWrapper(true);
        tst.setJob(new ThirdSpeedTestJob(info, GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getThridSpeedFirstURL(), GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getThridSpeedSecordURL()));
        tst.setDoneListener(listener);
        return tst;
    }

    public static NDBaseWrapper getTracerouteWrapper(NetDiagnoseInfo info, INDDoneListener listener) {
        NDBaseWrapper tr = new NDBaseWrapper(true);
        tr.setJob(new TracerouteJob(info, GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getTracertDomains()));
        tr.setDoneListener(listener);
        return tr;
    }

    public static NDBaseWrapper getDnsWrapper(NetDiagnoseInfo info, INDDoneListener listener) {
        NDBaseWrapper dns = new NDBaseWrapper(true);
        dns.setJob(new DNSJob(info));
        dns.setDoneListener(listener);
        return dns;
    }

    public static NDBaseWrapper getCollectInfoWrapper(NetDiagnoseInfo info, INDDoneListener listener) {
        NDBaseWrapper ci = new NDBaseWrapper();
        ci.setJob(new CollectInfoJob(info, GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getPingDomains()));
        ci.setDoneListener(listener);
        return ci;
    }

    public static NDBaseWrapper getCollectInfoWrapper(NetDiagnoseInfo info, INDDoneListener listener, boolean runNext) {
        NDBaseWrapper ci = new NDBaseWrapper(runNext);
        ci.setJob(new CollectInfoJob(info));
        ci.setDoneListener(listener);
        return ci;
    }

    public static NDBaseWrapper getNsLookupWrapper(NetDiagnoseInfo info, INDDoneListener listener) {
        NDBaseWrapper ns = new NDBaseWrapper();
        ns.setJob(new NsLookUpJob(info, GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getNsLookDomains()));
        ns.setDoneListener(listener);
        return ns;
    }

    public static NDBaseWrapper getDynamicNsLookupWrapper(NetDiagnoseInfo info, INDDoneListener listener, boolean runNext, String[] nsLookUpUrls) {
        NDBaseWrapper ns = new NDBaseWrapper(runNext);
        ns.setJob(new DynamicNsLookUpJob(info, nsLookUpUrls));
        ns.setDoneListener(listener);
        return ns;
    }

    public static NDBaseWrapper getNsLookupWrapper(NetDiagnoseInfo info, INDDoneListener listener, boolean runNext) {
        return getNsLookupWrapper(info, listener, runNext, null);
    }

    private static NDBaseWrapper getNsLookupWrapper(NetDiagnoseInfo info, INDDoneListener listener, boolean runNext, String nsLookUpUrls) {
        NDBaseWrapper ns = new NDBaseWrapper(runNext);
        ns.setJob(new NsLookUpJob(info, nsLookUpUrls));
        ns.setDoneListener(listener);
        return ns;
    }

    public static String[] getParseUrls(String urls) {
        if (StringUtils.isEmpty((CharSequence) urls)) {
            return null;
        }
        return urls.split(",");
    }

    public static NDBaseWrapper getPingWrapper(NetDiagnoseInfo info, INDDoneListener listener, boolean runNext, String[] pingUrl) {
        NDBaseWrapper ns = new NDBaseWrapper(runNext);
        ns.setJob(new PingJob(info, pingUrl));
        ns.setDoneListener(listener);
        return ns;
    }
}
