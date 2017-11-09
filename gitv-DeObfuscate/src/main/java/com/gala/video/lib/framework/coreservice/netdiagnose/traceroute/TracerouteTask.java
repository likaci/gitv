package com.gala.video.lib.framework.coreservice.netdiagnose.traceroute;

import android.util.Log;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.cybergarage.soap.SOAP;

public class TracerouteTask {
    private static final String EXCEED_PING = "exceed";
    private static final String FROM_PING = "From";
    private static final String PARENTHESE_CLOSE_PING = ")";
    private static final String PARENTHESE_OPEN_PING = "(";
    private static final String PING = "PING";
    private static final String SMALL_FROM_PING = "from";
    private static final String TIME_PING = "time=";
    private static final String UNREACHABLE_PING = "100%";
    private final int MAX_TTL = 30;
    private String TAG = "TracerouteTask";
    private float mElapsedTime;
    private String mIpToPing;
    private boolean mIsRunning = false;
    private int mMaxTtl;
    private long mStartTime;
    private TracerouteStateListener mTracerouteStateListener;
    private String mUrl;
    private List<TracerouteContainer> traces = new ArrayList();
    private int ttl = 1;

    public TracerouteTask(int maxTtl, String url, TracerouteStateListener listener) {
        this.mMaxTtl = maxTtl;
        this.mUrl = url;
        this.mTracerouteStateListener = listener;
    }

    public void initParams() {
        Log.d(this.TAG, "initParams");
        this.mStartTime = System.currentTimeMillis();
        this.mIsRunning = true;
    }

    public void startTraceroute() {
        Log.v(this.TAG, "isRunning = " + this.mIsRunning);
        if (this.mIsRunning) {
            Log.d(this.TAG, "start traceroute:" + this.mUrl);
            String res = "";
            try {
                CharSequence res2 = launchPing(this.mUrl);
                if (StringUtils.isEmpty(res2)) {
                    this.mTracerouteStateListener.onTraceFailed(this.mUrl, "trace error, do not get ping result");
                    return;
                }
                TracerouteContainer trace;
                if (!res2.contains(UNREACHABLE_PING) || res2.contains(EXCEED_PING)) {
                    trace = new TracerouteContainer("", parseIpFromPing(res2), this.ttl == this.mMaxTtl ? Float.parseFloat(parseTimeFromPing(res2)) : this.mElapsedTime);
                } else {
                    trace = new TracerouteContainer("", parseIpFromPing(res2), this.mElapsedTime);
                }
                try {
                    trace.setHostname(InetAddress.getByName(trace.getIp()).getHostName());
                    this.traces.add(trace);
                    Log.d(this.TAG, "add trace:" + trace);
                    if (((TracerouteContainer) this.traces.get(this.traces.size() - 1)).getIp().equals(this.mIpToPing)) {
                        if (this.ttl < this.mMaxTtl) {
                            this.ttl = this.mMaxTtl;
                            this.traces.remove(this.traces.size() - 1);
                            startTraceroute();
                            return;
                        }
                        this.mTracerouteStateListener.onTraceSuccess(this.mUrl, this.mIpToPing, this.traces);
                    } else if (this.ttl < this.mMaxTtl) {
                        this.ttl++;
                        startTraceroute();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                    this.mTracerouteStateListener.onTraceFailed(this.mUrl, "trace error:" + e.getMessage());
                }
            } catch (Exception e1) {
                this.mTracerouteStateListener.onTraceFailed(this.mUrl, "trace error:" + e1);
                e1.printStackTrace();
            }
        }
    }

    private String launchPing(String url) throws IOException {
        String command = "";
        command = String.format("ping -c 1 -t %d ", new Object[]{Integer.valueOf(this.ttl)});
        long startTime = System.nanoTime();
        Process p = Runtime.getRuntime().exec(command + url);
        BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
        String res = "";
        while (true) {
            String s = stdInput.readLine();
            if (s == null) {
                break;
            }
            res = res + s + "\n";
            if (s.contains(FROM_PING) || s.contains("from")) {
                this.mElapsedTime = ((float) (System.nanoTime() - startTime)) / 1000000.0f;
            }
        }
        p.destroy();
        if (res.equals("")) {
            throw new IOException("Trace host error.");
        }
        if (this.ttl == 1) {
            this.mIpToPing = parseIpToPingFromPing(res);
        }
        return res;
    }

    private String parseIpFromPing(String ping) {
        String ip = "";
        if (ping.contains(FROM_PING)) {
            ip = ping.substring(ping.indexOf(FROM_PING) + 5);
            if (ip.contains(PARENTHESE_OPEN_PING)) {
                int indexOpen = ip.indexOf(PARENTHESE_OPEN_PING);
                return ip.substring(indexOpen + 1, ip.indexOf(PARENTHESE_CLOSE_PING));
            }
            int index;
            ip = ip.substring(0, ip.indexOf("\n"));
            if (ip.contains(SOAP.DELIM)) {
                index = ip.indexOf(SOAP.DELIM);
            } else {
                index = ip.indexOf(" ");
            }
            return ip.substring(0, index);
        }
        indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
        return ping.substring(indexOpen + 1, ping.indexOf(PARENTHESE_CLOSE_PING));
    }

    private String parseIpToPingFromPing(String ping) {
        String ip = "";
        if (!ping.contains(PING)) {
            return ip;
        }
        int indexOpen = ping.indexOf(PARENTHESE_OPEN_PING);
        return ping.substring(indexOpen + 1, ping.indexOf(PARENTHESE_CLOSE_PING));
    }

    private String parseTimeFromPing(String ping) {
        String time = "";
        if (!ping.contains(TIME_PING)) {
            return time;
        }
        time = ping.substring(ping.indexOf(TIME_PING) + 5);
        return time.substring(0, time.indexOf(" "));
    }

    public boolean isRunning() {
        return this.mIsRunning;
    }

    public void cancel() {
        Log.d(this.TAG, "cancel()");
        this.mIsRunning = false;
        this.mTracerouteStateListener.onTraceFailed(this.mUrl, "time limit,cost time = " + (System.currentTimeMillis() - this.mStartTime) + "\n");
    }
}
