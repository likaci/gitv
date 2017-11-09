package com.gala.video.lib.framework.coreservice.multiscreen.impl;

import android.content.Context;
import com.gala.multiscreen.dmr.IGalaMSExpand;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.coreservice.multiscreen.http.NanoHTTPD;
import com.gala.video.lib.framework.coreservice.multiscreen.http.NanoHTTPD.Method;
import com.gala.video.lib.framework.coreservice.multiscreen.http.NanoHTTPD.Response;
import com.gala.video.lib.share.ifimpl.netdiagnose.NetDiagnoseCheckTools;
import java.util.Map;

class MSHttpServer extends NanoHTTPD {
    private static final String TAG = MSHttpServer.class.getName();
    private static boolean mHasStarted = false;
    private static MSHttpServer mMsHttpServer = null;
    private Context mContext = null;
    private MSHttpService mOperatorService = null;

    private MSHttpServer(Context context, IGalaMSExpand callback) {
        super(32566);
        this.mContext = context;
        this.mOperatorService = new MSHttpService(this.mContext, callback);
    }

    public Response serve(String uri, Method method, Map<String, String> map, Map<String, String> parms, Map<String, String> map2) {
        LogUtils.i(TAG, "ms http server serve uri:" + uri);
        Response resp = new Response(this.mOperatorService.handleRequest(uri, parms));
        resp.addHeader("Access-Control-Allow-Origin", NetDiagnoseCheckTools.NO_CHECK_FLAG);
        return resp;
    }

    public static void startServer(Context context, IGalaMSExpand callback) {
        if (mMsHttpServer == null) {
            mMsHttpServer = new MSHttpServer(context, callback);
        }
        try {
            if (mHasStarted) {
                mMsHttpServer.stop();
                mHasStarted = false;
            }
            LogUtils.i(MSHttpServer.class.getName(), "ms http server start");
            mMsHttpServer.start();
            mHasStarted = true;
        } catch (Exception e) {
            LogUtils.i(MSHttpServer.class.getName(), "ms http server start error:" + e);
        }
    }
}
