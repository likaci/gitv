package com.gala.video.app.player;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.media.MediaCodecList;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.WindowManager;
import android.widget.TextView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.project.Project;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class SystemInfoTestActivity extends QMultiScreenActivity {
    private static final boolean IS_CPU_SUPPORTED;
    private static final boolean IS_PREFER_USE_ADVANCE_MODE;
    private static final String[] NATIVE_PLAYER_SUPPORTED_CPU_LIST = new String[]{"Amlogic", "AMLOGIC", "rtd", "edison", "bigfish", "tn8", "MT8685", "sun6i"};
    private static CodecType OVERRIDE_CODEC_TYPE = null;
    private static final Map<String, CodecType> OVERRIDE_CODEC_TYPE_MAP = new HashMap();
    private static final Map<String, String> PREFER_USE_ADVANCE_MODE_MAP = new HashMap();
    private static final String TAG = "SystemInfoTester";
    private static final String sCpuInfo = getCpuInfo();
    private TextView tvCodec;
    private TextView tvCpuInfo;
    private TextView tvMemInfo;
    private TextView tvSystem;
    private TextView tvUseAdvance;

    public enum CodecType {
        ACC_By_MediaCodec,
        ACC_By_SDK
    }

    static {
        int i = 0;
        PREFER_USE_ADVANCE_MODE_MAP.put("i71", "Amlogic");
        PREFER_USE_ADVANCE_MODE_MAP.put("i71C", "Amlogic");
        PREFER_USE_ADVANCE_MODE_MAP.put("i71S", "Amlogic");
        PREFER_USE_ADVANCE_MODE_MAP.put("MagicBox2", "Amlogic Meson8 platform");
        PREFER_USE_ADVANCE_MODE_MAP.put("MagicBox_M11_MEIZU", "MT8685");
        PREFER_USE_ADVANCE_MODE_MAP.put("MagicBox1s_Pro", "sun6i");
        PREFER_USE_ADVANCE_MODE_MAP.put("M321", "bigfish");
        OVERRIDE_CODEC_TYPE_MAP.put("MT8685", CodecType.ACC_By_MediaCodec);
        OVERRIDE_CODEC_TYPE_MAP.put("bigfish", CodecType.ACC_By_MediaCodec);
        OVERRIDE_CODEC_TYPE = null;
        boolean support = false;
        String[] strArr = NATIVE_PLAYER_SUPPORTED_CPU_LIST;
        int length = strArr.length;
        while (i < length) {
            if (sCpuInfo.contains(strArr[i])) {
                support = true;
                break;
            }
            i++;
        }
        IS_CPU_SUPPORTED = support;
        LogUtils.m1568d(TAG, "static<init>: is native player supported=" + support);
        boolean isPreferUseAdvanceMode = false;
        for (String model : PREFER_USE_ADVANCE_MODE_MAP.keySet()) {
            if (model.equalsIgnoreCase(Build.MODEL)) {
                String preferCpu = (String) PREFER_USE_ADVANCE_MODE_MAP.get(model);
                if (sCpuInfo.contains(preferCpu)) {
                    isPreferUseAdvanceMode = true;
                    OVERRIDE_CODEC_TYPE = (CodecType) OVERRIDE_CODEC_TYPE_MAP.get(preferCpu);
                    break;
                }
            }
        }
        IS_PREFER_USE_ADVANCE_MODE = isPreferUseAdvanceMode;
        LogUtils.m1568d(TAG, "static<init>: is prefer advance mode=" + IS_PREFER_USE_ADVANCE_MODE);
    }

    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFormat(-2);
        getWindow().addFlags(128);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            setTheme(C1291R.style.AppTheme);
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "onCreate: setTheme for home version");
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(C1291R.layout.player_system_info_test);
        this.tvSystem = (TextView) findViewById(C1291R.id.textView1);
        this.tvCpuInfo = (TextView) findViewById(C1291R.id.textView2);
        this.tvUseAdvance = (TextView) findViewById(C1291R.id.textView3);
        this.tvCodec = (TextView) findViewById(C1291R.id.textView4);
        this.tvMemInfo = (TextView) findViewById(C1291R.id.textView5);
        this.tvMemInfo.setText(getTotalMemory());
        this.tvSystem.setText(getSystemInfo(this));
        this.tvCpuInfo.setText(getCpuInfo());
        this.tvUseAdvance.setText("isPreferUseAdvanceMode = " + IS_PREFER_USE_ADVANCE_MODE + ", codec = " + OVERRIDE_CODEC_TYPE);
        this.tvUseAdvance.setVisibility(8);
        this.tvCodec.setText(getDeviceSupportCodec());
    }

    public String getSystemInfo(Context context) {
        DisplayMetrics metric = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(metric);
        int width = metric.widthPixels;
        int height = metric.heightPixels;
        float density = metric.density;
        String systemInfo = "-------- System Environment --------\n SDK=" + VERSION.SDK_INT + "\n VERSION:" + VERSION.RELEASE + "\n WIDTH:" + width + ", HEIGHT=" + height + "\n DENSITY=" + density + "\n DENSITYDPI=" + metric.densityDpi + "\n BOARD=" + Build.BOARD + "\n DEVICE=" + Build.DEVICE + "\n DISPLAY=" + Build.DISPLAY + "\n FINGERPRINT=" + Build.FINGERPRINT + "\n HARDWARE=" + Build.HARDWARE + "\n ID=" + Build.ID + "\n MANUFACTURER=" + Build.MANUFACTURER + "\n MODEL=" + Build.MODEL + "\n PRODUCT=" + Build.PRODUCT + "\n SERIAL=" + Build.SERIAL + "\n BRAND=" + Build.BRAND;
        LogUtils.m1568d(TAG, systemInfo);
        return systemInfo;
    }

    private static String getCpuInfo() {
        Exception e;
        Throwable th;
        String cpuInfoPath = "proc/cpuinfo";
        StringBuilder info = new StringBuilder();
        FileReader fr = null;
        BufferedReader localBufferedReader = null;
        try {
            BufferedReader localBufferedReader2;
            FileReader fr2 = new FileReader(cpuInfoPath);
            try {
                localBufferedReader2 = new BufferedReader(fr2, 8192);
            } catch (Exception e2) {
                e = e2;
                fr = fr2;
                try {
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e);
                    }
                    if (localBufferedReader != null) {
                        try {
                            localBufferedReader.close();
                        } catch (IOException e3) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e3);
                            }
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    LogUtils.m1568d(TAG, info.toString());
                    return info.toString();
                } catch (Throwable th2) {
                    th = th2;
                    if (localBufferedReader != null) {
                        try {
                            localBufferedReader.close();
                        } catch (IOException e32) {
                            if (LogUtils.mIsDebug) {
                                LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e32);
                            }
                            throw th;
                        }
                    }
                    if (fr != null) {
                        fr.close();
                    }
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                fr = fr2;
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
            try {
                info.append("-------- CpuInfo --------\n");
                while (true) {
                    String line = localBufferedReader2.readLine();
                    if (line == null) {
                        break;
                    }
                    info.append(line);
                    info.append("\n");
                }
                if (localBufferedReader2 != null) {
                    try {
                        localBufferedReader2.close();
                    } catch (IOException e322) {
                        if (LogUtils.mIsDebug) {
                            LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e322);
                        }
                        localBufferedReader = localBufferedReader2;
                        fr = fr2;
                    }
                }
                if (fr2 != null) {
                    fr2.close();
                }
                localBufferedReader = localBufferedReader2;
                fr = fr2;
            } catch (Exception e4) {
                e = e4;
                localBufferedReader = localBufferedReader2;
                fr = fr2;
                if (LogUtils.mIsDebug) {
                    LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e);
                }
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
                if (fr != null) {
                    fr.close();
                }
                LogUtils.m1568d(TAG, info.toString());
                return info.toString();
            } catch (Throwable th4) {
                th = th4;
                localBufferedReader = localBufferedReader2;
                fr = fr2;
                if (localBufferedReader != null) {
                    localBufferedReader.close();
                }
                if (fr != null) {
                    fr.close();
                }
                throw th;
            }
        } catch (Exception e5) {
            e = e5;
            if (LogUtils.mIsDebug) {
                LogUtils.m1572e(TAG, "getCpuInfo: exception happened:", e);
            }
            if (localBufferedReader != null) {
                localBufferedReader.close();
            }
            if (fr != null) {
                fr.close();
            }
            LogUtils.m1568d(TAG, info.toString());
            return info.toString();
        }
        LogUtils.m1568d(TAG, info.toString());
        return info.toString();
    }

    @TargetApi(16)
    public static String getDeviceSupportCodec() {
        boolean hasSupportDolbyCodec = false;
        boolean hasSupportH211Codec = false;
        StringBuilder sb = new StringBuilder();
        sb.append("\n-------- Check Dolby/H211-Support Codec --------\n");
        if (VERSION.SDK_INT >= 16) {
            int i = 0;
            while (i < MediaCodecList.getCodecCount()) {
                try {
                    MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
                    if (info != null) {
                        for (String type : info.getSupportedTypes()) {
                            if ("audio/eac3".equals(type) || "audio/ec3".equals(type) || "audio/ac3".equals(type)) {
                                hasSupportDolbyCodec = true;
                                sb.append("[Check dolby]  FOUND, current type = " + type + "\n");
                                CodecCapabilities caps = info.getCapabilitiesForType(type);
                                if (caps != null) {
                                    sb.append("[Check dolby]  ProfileLevels size:" + caps.profileLevels.length + "\n");
                                    for (CodecProfileLevel pl : caps.profileLevels) {
                                        if (pl != null) {
                                            sb.append("[Check dolby]  CodecProfileLevel(profile:" + pl.profile + ", level:" + pl.level + ")" + "\n");
                                        }
                                    }
                                }
                            }
                            if (type != null && (type.toLowerCase().contains("hevc") || type.toLowerCase().contains("h265") || type.toLowerCase().contains("h.265"))) {
                                hasSupportH211Codec = true;
                                sb.append("[Check H211] FOUND, current type = " + type + "\n");
                            }
                            if (hasSupportH211Codec && hasSupportH211Codec) {
                                break;
                            }
                        }
                    }
                    i++;
                } catch (Exception e) {
                    sb.append("[Check dolby/H211]  Exception occurs!" + e + "\n");
                }
            }
        }
        if (hasSupportDolbyCodec) {
            sb.append(">>> Dolby-Support codec IS detected!\n");
        } else {
            sb.append(">>> Dolby-Support codec NOT detected!\n");
        }
        if (hasSupportH211Codec) {
            sb.append(">>> H211-Support codec IS detected!\n");
        } else {
            sb.append(">>> H211-Support codec NOT detected!\n");
        }
        LogUtils.m1568d(TAG, sb.toString());
        return sb.toString();
    }

    public String getTotalMemory() {
        String str1 = "/proc/meminfo";
        String str2 = "";
        StringBuilder info = new StringBuilder();
        info.append("\n-------- Meminfo --------\n");
        Pattern p = Pattern.compile("[^0-9]");
        try {
            BufferedReader localBufferedReader = new BufferedReader(new FileReader(str1), 8192);
            do {
                str2 = localBufferedReader.readLine();
                if (str2 == null) {
                    break;
                }
            } while (!str2.contains("MemTotal"));
            String memSize = p.matcher(str2).replaceAll("").trim();
            info.append(str2);
        } catch (IOException e) {
        }
        LogUtils.m1568d(TAG, info.toString());
        return info.toString();
    }
}
