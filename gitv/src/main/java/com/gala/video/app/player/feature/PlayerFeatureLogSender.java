package com.gala.video.app.player.feature;

import android.os.Build;
import android.util.Log;
import com.gala.sdk.plugin.ResultCode.ERROR_TYPE;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.project.Project;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import org.cybergarage.http.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerFeatureLogSender {
    private static final String TAG = "PlayerFeatureLogSender";
    private final String URL_TRACKER_REPORT;
    private String mAssetErrorMessage;
    private String mFirstLoad;
    private String mHardwareInfo;
    private String mIsTodayFirst;
    private String mMac;
    private String mParams;
    private String mUUID;
    private String mVersionCode;

    public PlayerFeatureLogSender(String log, Map<String, String> params) {
        this.URL_TRACKER_REPORT = "http://tracker.ptqy.gitv.tv/naja/log/collect_log";
        this.mUUID = "";
        this.mVersionCode = "";
        this.mHardwareInfo = Build.MODEL.replace(" ", "-");
        this.mMac = "";
        this.mFirstLoad = "";
        this.mIsTodayFirst = "";
        this.mAssetErrorMessage = "";
        this.mUUID = Project.getInstance().getBuild().getVrsUUID();
        this.mMac = DeviceUtils.getEthMAC();
        if (params != null) {
            this.mVersionCode = Project.getInstance().getBuild().getVersionString();
            this.mFirstLoad = (String) params.get(Keys.FIRSTLOAD);
            this.mIsTodayFirst = (String) params.get("istodayfirst");
            this.mAssetErrorMessage = (String) params.get(ERROR_TYPE.ERROR_LOAD_ASSETS);
        }
        this.mParams = buildJson(log);
    }

    private String buildJson(String log) {
        JSONObject js = new JSONObject();
        try {
            js.put("_bizType", "tv_feedback");
            js.put("log_type", "playerplugin_loadfail");
            js.put("versionCode", this.mVersionCode);
            js.put("QuesType", this.mFirstLoad);
            js.put("QuesDetail", this.mAssetErrorMessage);
            js.put("apiName", this.mIsTodayFirst);
            js.put("hardware_info", this.mHardwareInfo);
            js.put("uuid", this.mUUID);
            js.put("mac_address", this.mMac);
            js.put("log_content", log);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return js.toString();
    }

    public void send() {
        Thread t = new Thread8K(new Runnable() {
            public void run() {
                Log.d(PlayerFeatureLogSender.TAG, PlayerFeatureLogSender.this.sendPost("http://tracker.ptqy.gitv.tv/naja/log/collect_log", PlayerFeatureLogSender.this.mParams));
            }
        }, TAG);
        t.setName("URL_TRACKER_REPORT");
        t.setPriority(10);
        t.start();
    }

    private String sendPost(String url, String param) {
        Exception e;
        Throwable th;
        Log.d(TAG, "sendPost, url = " + url + ", param = " + param);
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setRequestMethod(HTTP.POST);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty(HTTP.CONTENT_TYPE, "application/octet-stream");
            connection.connect();
            PrintWriter out2 = new PrintWriter(connection.getOutputStream());
            try {
                out2.print(param);
                out2.flush();
                BufferedReader in2 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while (true) {
                    try {
                        String line = in2.readLine();
                        if (line == null) {
                            break;
                        }
                        result = result + line;
                    } catch (Exception e2) {
                        e = e2;
                        in = in2;
                        out = out2;
                    } catch (Throwable th2) {
                        th = th2;
                        in = in2;
                        out = out2;
                    }
                }
                Log.d(TAG, "response = " + result);
                if (out2 != null) {
                    try {
                        out2.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        in = in2;
                        out = out2;
                    }
                }
                if (in2 != null) {
                    in2.close();
                }
                in = in2;
                out = out2;
            } catch (Exception e3) {
                e = e3;
                out = out2;
                try {
                    System.out.println("发送 POST 请求出现异常！" + e);
                    e.printStackTrace();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex2) {
                            ex2.printStackTrace();
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                    return result;
                } catch (Throwable th3) {
                    th = th3;
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException ex22) {
                            ex22.printStackTrace();
                            throw th;
                        }
                    }
                    if (in != null) {
                        in.close();
                    }
                    throw th;
                }
            } catch (Throwable th4) {
                th = th4;
                out = out2;
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
            if (out != null) {
                out.close();
            }
            if (in != null) {
                in.close();
            }
            return result;
        }
        return result;
    }
}
