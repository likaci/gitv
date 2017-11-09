package com.gala.video.app.player.config;

import android.util.Pair;
import com.alibaba.fastjson.JSONObject;
import com.gala.sdk.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class PlayerConfigJsFunc2Java {
    public static final String CONFIG_KEY_DISABLEADCACHE = "disableADCache";
    public static final String CONFIG_KEY_DISABLEGIFFORCAROUSEL = "disableGifForCarousel";
    public static final String CONFIG_KEY_DISABLEGIFFORDETAILPAGE = "disableGifForDetailPage";
    public static final String CONFIG_KEY_DISABLE_4K_H264 = "disable_4k_h264";
    public static final String CONFIG_KEY_DISABLE_AD_CASTER = "disable_ad_caster";
    public static final String CONFIG_KEY_DISABLE_HCDN_MULTI_PROCESS = "disable_hcdn_multi_proc";
    public static final String CONFIG_KEY_ENABLE_LOCAL_SERVER_F4V2HLS = "enable_localserver_f4v2hls";
    public static final String CONFIG_KEY_ENABLE_LOCAL_SERVER_STREAM = "enable_localserver_stream";
    public static final String CONFIG_KEY_MULTI_PROC_SWITCH = "multiProcessSwitch";
    public static final String CONFIG_KEY_OPEN_PLUGIN_IO_BALANCE = "open_plugin_io_balance";
    public static final String CONFIG_KEY_PAUSE_BEFORE_SEEK = "pause_before_seek";
    public static final String CONFIG_KEY_PLAYERTYPE = "playerType";
    public static final String CONFIG_KEY_PLAYER_TYPE_CONFIG = "player_type_config";
    public static final String CONFIG_KEY_SEEK_BEFORE_START = "seek_before_start";
    public static final String CONFIG_KEY_SEEK_BEFORE_START_AD = "seek_before_start_ad";
    public static final String CONFIG_KEY_SETFIXEDSIZE = "setFixedSize";
    public static final String CONFIG_KEY_SMALLWINDOW = "smallWindow";
    public static final String CONFIG_KEY_SUPPORT_4K = "support_4k";
    public static final String CONFIG_KEY_SUPPORT_4K_H211 = "support_4k_h211";
    public static final String CONFIG_KEY_SUPPORT_ANIMATION = "supportAnimation";
    public static final String CONFIG_KEY_SUPPORT_DOLBYVISION = "support_dolbyvision";
    public static final String CONFIG_KEY_SUPPORT_H211 = "support_h265";
    public static final String CONFIG_KEY_SUPPORT_HDR10 = "support_hdr10";
    public static final String CONFIG_KEY_SURFACEFORMAT = "surfaceFormat";
    public static final String CONFIG_KEY_UNIPLAYER_DATA_CONFIG_JSON = "uniplayer_data_config_json";
    public static final String CONFIG_KEY_UPDATE_SURFACEVIEW_AFTER_START = "update_surfaceview_after_start";
    public static final String CONFIG_KEY_USE_FD_LOCAL_PLAYBACK = "use_fd_local_playback";
    public static final String CONFIG_KEY_USE_MEDIAPLAYER_RESET = "use_mediaplayer_reset";
    public static final String CONFIG_KEY_USE_NATIVEPLAYER_CAROUSEL = "use_nativeplayer_carousel";
    private static final String REG_EXP_MTK = "mt(\\d+)[^\\d]*";

    static class C13411 extends ArrayList<String> {
        C13411() {
            add("rtd");
        }
    }

    static class C13422 extends ArrayList<String> {
        C13422() {
            add("mt8685");
        }
    }

    static class C13433 extends ArrayList<String> {
        C13433() {
            add("magicbox1s_plus");
        }
    }

    static class C13444 extends ArrayList<String> {
        C13444() {
            add("letv new c1s");
        }
    }

    static class C13455 extends ArrayList<String> {
        C13455() {
            add("magicbox_m13");
            add("mitv2-49");
            add("mibox3_pro");
            add("lcd-s3a-01");
            add("skyworth-8h83-6000");
            add("letv-x3-55");
            add("i71s+");
            add("mini");
            add("a810");
            add("m321");
            add("m330");
            add("dm1016");
            add("hi3798mv100");
            add("rk3368-box");
            add("mitv3s-48");
            add("himedia_3798m");
            add("db2116");
            add("inphic_i10m");
            add("9s51_q7");
        }
    }

    static class C13466 extends ArrayList<String> {
        C13466() {
            add("magicbox2");
            add("inphic_i9h");
            add("mibox3");
            add("mibox3_pro");
            add("rk3368-box");
            add("kbe_h8");
            add("inphic_h3");
            add("hi3798mv100");
            add("magicbox1s_plus");
            add("magicbox1s_pro");
            add("i71s+");
            add("hmd_tvbox");
            add("mibox2");
            add("vision-tv");
            add("vidda_tv");
            add("rtd299x_tv030");
            add("9s51_q7");
            add("mibox3s");
            add("q65x1s");
            add("55s9d");
            add("65s9d");
            add("px9900-led65mu9600x3duc");
            add("px9900-led55mu9600x3duc");
            add("55q3a");
            add("55x2");
            add("65x2");
            add("55p605");
        }
    }

    static class C13477 extends ArrayList<String> {
        C13477() {
            add("magicbox_m13");
            add("mitv2-49");
            add("mibox3_pro");
            add("lcd-s3a-01");
            add("skyworth-8h83-6000");
            add("letv-x3-55");
            add("m321");
            add("dm1016");
            add("hi3798mv100");
            add("rk3368-box");
            add("i71s+");
            add("mini");
            add("a810");
            add("9s51_q7");
        }
    }

    static class C13488 extends ArrayList<String> {
        C13488() {
        }
    }

    static class C13499 extends ArrayList<String> {
        C13499() {
            add("9s51_q7");
            add("mibox3s");
            add("q65x1s");
            add("55s9d");
            add("65s9d");
            add("px9900-led65mu9600x3duc");
            add("px9900-led55mu9600x3duc");
            add("55q3a");
            add("55x2");
            add("65x2");
            add("55p605");
        }
    }

    public static final String getPlayerConfig(String cpu, String product, String model, int ram, String androidVersion, String apkVersion, String uuid) {
        String modelInfo;
        Map<String, Object> result = new HashMap();
        HashMap<String, Integer> surfaceFormatModelMap = new HashMap();
        surfaceFormatModelMap.put("a810", Integer.valueOf(1));
        surfaceFormatModelMap.put("mini", Integer.valueOf(1));
        int surfaceFormatType = 0;
        if (model != null && surfaceFormatModelMap.containsKey(model.toLowerCase())) {
            surfaceFormatType = ((Integer) surfaceFormatModelMap.get(model.toLowerCase())).intValue();
        }
        result.put(CONFIG_KEY_SURFACEFORMAT, Integer.valueOf(surfaceFormatType));
        result.put("playerType", Integer.valueOf(4));
        List<String> notSupportCpuList = new C13411();
        List<String> exceptCpuList = new C13422();
        HashMap<String, String> disableSmallWindowSpecialProductsMap = new HashMap();
        disableSmallWindowSpecialProductsMap.put("phoenix", "rtk_phoenix");
        HashMap<String, String> supportSmallWindowSpecialModelMap = new HashMap();
        supportSmallWindowSpecialModelMap.put("mt8693", "mibox3_pro");
        boolean isSupportSmallWindow = true;
        if (cpu != null) {
            boolean isSupportStateFetched = false;
            String cpuinfo = cpu.toLowerCase();
            String productInfo = null;
            if (product != null) {
                productInfo = product.toLowerCase();
            }
            for (String deviceCpu : disableSmallWindowSpecialProductsMap.keySet()) {
                if (cpuinfo.contains(deviceCpu) && disableSmallWindowSpecialProductsMap.get(deviceCpu) != null && ((String) disableSmallWindowSpecialProductsMap.get(deviceCpu)).equals(productInfo)) {
                    isSupportSmallWindow = false;
                    isSupportStateFetched = true;
                    break;
                }
            }
            if (!isSupportStateFetched) {
                modelInfo = null;
                if (model != null) {
                    modelInfo = model.toLowerCase();
                }
                for (String deviceCpu2 : supportSmallWindowSpecialModelMap.keySet()) {
                    if (cpuinfo.contains(deviceCpu2) && supportSmallWindowSpecialModelMap.get(deviceCpu2) != null && ((String) supportSmallWindowSpecialModelMap.get(deviceCpu2)).equals(modelInfo)) {
                        isSupportSmallWindow = true;
                        isSupportStateFetched = true;
                        break;
                    }
                }
            }
            if (!isSupportStateFetched) {
                for (String exceptCpu : exceptCpuList) {
                    if (cpuinfo.contains(exceptCpu)) {
                        isSupportSmallWindow = true;
                        isSupportStateFetched = true;
                        break;
                    }
                }
            }
            if (!isSupportStateFetched) {
                for (String notSupportCpu : notSupportCpuList) {
                    if (cpuinfo.contains(notSupportCpu)) {
                        isSupportSmallWindow = false;
                        isSupportStateFetched = true;
                        break;
                    }
                }
            }
            if (!isSupportStateFetched && Pattern.compile(REG_EXP_MTK, 2).matcher(cpuinfo).find()) {
                isSupportSmallWindow = false;
            }
        }
        result.put(CONFIG_KEY_SMALLWINDOW, Boolean.valueOf(isSupportSmallWindow));
        HashMap<String, Integer> fixedSizeTypeModelMap = new HashMap();
        fixedSizeTypeModelMap.put("letv x3-40", Integer.valueOf(101));
        fixedSizeTypeModelMap.put("magicbox2", Integer.valueOf(101));
        int fixedSizeType = 100;
        if (model != null && fixedSizeTypeModelMap.containsKey(model.toLowerCase())) {
            fixedSizeType = ((Integer) fixedSizeTypeModelMap.get(model.toLowerCase())).intValue();
        }
        result.put(CONFIG_KEY_SETFIXEDSIZE, Integer.valueOf(fixedSizeType));
        boolean disableGifForCarousel = false;
        List<String> disableGifViewModelList = new C13433();
        if (model != null && disableGifViewModelList.contains(model.toLowerCase())) {
            disableGifForCarousel = true;
        }
        result.put(CONFIG_KEY_DISABLEGIFFORCAROUSEL, Boolean.valueOf(disableGifForCarousel));
        boolean disableGifForDetailPage = false;
        List<String> disableGifViewForDetailModelList = new C13444();
        if (model != null && disableGifViewForDetailModelList.contains(model.toLowerCase())) {
            disableGifForDetailPage = true;
        }
        result.put(CONFIG_KEY_DISABLEGIFFORDETAILPAGE, Boolean.valueOf(disableGifForDetailPage));
        boolean isSupportH211 = false;
        List<String> supportH211ModelList = new C13455();
        if (model != null && supportH211ModelList.contains(model.toLowerCase())) {
            isSupportH211 = true;
        }
        result.put(CONFIG_KEY_SUPPORT_H211, Boolean.valueOf(isSupportH211));
        boolean isSupport4k = false;
        List<String> support4kModelList = new C13466();
        if (model != null && support4kModelList.contains(model.toLowerCase())) {
            isSupport4k = true;
        }
        result.put(CONFIG_KEY_SUPPORT_4K, Boolean.valueOf(isSupport4k));
        boolean isSupport4kH211 = false;
        List<String> support4kH211ModelList = new C13477();
        if (model != null && support4kH211ModelList.contains(model.toLowerCase())) {
            isSupport4kH211 = true;
        }
        result.put(CONFIG_KEY_SUPPORT_4K_H211, Boolean.valueOf(isSupport4kH211));
        boolean isDisable4kH264 = false;
        List<String> disable4kH264ModelList = new C13488();
        if (model != null && disable4kH264ModelList.contains(model.toLowerCase())) {
            isDisable4kH264 = true;
        }
        result.put(CONFIG_KEY_DISABLE_4K_H264, Boolean.valueOf(isDisable4kH264));
        boolean isSupportHDR10 = false;
        List<String> supportHDR10ModelList = new C13499();
        if (model != null && supportHDR10ModelList.contains(model.toLowerCase())) {
            isSupportHDR10 = true;
        }
        result.put(CONFIG_KEY_SUPPORT_HDR10, Boolean.valueOf(isSupportHDR10));
        boolean isSupportDolbyVision = false;
        List<String> supportDolbyVisionModelList = new ArrayList<String>() {
            {
                add("9s51_q7");
                add("q65x1s");
                add("55s9d");
                add("65s9d");
                add("px9900-led65mu9600x3duc");
                add("px9900-led55mu9600x3duc");
                add("55q3a");
                add("55x2");
                add("65x2");
                add("55p605");
            }
        };
        if (model != null && supportDolbyVisionModelList.contains(model.toLowerCase())) {
            isSupportDolbyVision = true;
        }
        result.put(CONFIG_KEY_SUPPORT_DOLBYVISION, Boolean.valueOf(isSupportDolbyVision));
        boolean isPlayerMultiProcessOpen = false;
        HashMap<String, ArrayList<String>> openMutiProcessUuidModel = new HashMap();
        if (openMutiProcessUuidModel.containsKey(uuid)) {
            List models = (ArrayList) openMutiProcessUuidModel.get(uuid);
            if (!ListUtils.isEmpty(models) && models.contains(model)) {
                isPlayerMultiProcessOpen = true;
            }
        }
        result.put(CONFIG_KEY_MULTI_PROC_SWITCH, Boolean.valueOf(isPlayerMultiProcessOpen));
        boolean isPauseBeforeSeek = false;
        List<String> pauseBeforeSeekModelList = new ArrayList<String>() {
            {
                add("magicbox_m13");
            }
        };
        if (model != null && pauseBeforeSeekModelList.contains(model.toLowerCase())) {
            isPauseBeforeSeek = true;
        }
        result.put(CONFIG_KEY_PAUSE_BEFORE_SEEK, Boolean.valueOf(isPauseBeforeSeek));
        boolean isSeekBeforeStart = true;
        List<String> seekAfterStartModelList = new ArrayList<String>() {
            {
                add("inphic_i9s1");
                add("vision_tv");
                add("wtv55k1");
                add("c3000i");
                add("dm1016");
            }
        };
        if (model != null && seekAfterStartModelList.contains(model.toLowerCase())) {
            isSeekBeforeStart = false;
        }
        result.put(CONFIG_KEY_SEEK_BEFORE_START, Boolean.valueOf(isSeekBeforeStart));
        boolean isSeekBeforeStartForAD = true;
        List<String> seekAfterStartADModelList = new ArrayList<String>() {
            {
                add("inphic_i9s1");
                add("rk3368-box");
            }
        };
        if (model != null && seekAfterStartADModelList.contains(model.toLowerCase())) {
            isSeekBeforeStartForAD = false;
        }
        result.put(CONFIG_KEY_SEEK_BEFORE_START_AD, Boolean.valueOf(isSeekBeforeStartForAD));
        boolean isUseNativePlayerCarousel = false;
        List<String> useNativePlayerCarouselModelList = new ArrayList<String>() {
            {
                add("bravia 4k 2015");
            }
        };
        if (model != null && useNativePlayerCarouselModelList.contains(model.toLowerCase())) {
            isUseNativePlayerCarousel = true;
        }
        result.put(CONFIG_KEY_USE_NATIVEPLAYER_CAROUSEL, Boolean.valueOf(isUseNativePlayerCarousel));
        boolean isSupportAnimation = true;
        List<String> disableSupportModelList = new ArrayList<String>() {
            {
                add("vidaa_tv");
            }
        };
        if (model != null && disableSupportModelList.contains(model.toLowerCase())) {
            isSupportAnimation = false;
        }
        result.put(CONFIG_KEY_SUPPORT_ANIMATION, Boolean.valueOf(isSupportAnimation));
        boolean isDisableADCache = false;
        List<String> disableADCacheModelList = new ArrayList<String>() {
            {
                add("vidaa_tv");
            }
        };
        if (model != null && disableADCacheModelList.contains(model.toLowerCase())) {
            isDisableADCache = true;
        }
        result.put(CONFIG_KEY_DISABLEADCACHE, Boolean.valueOf(isDisableADCache));
        boolean isEnableLocalServerF4v2Hls = true;
        List<String> unsupportLocalServerF4v2HlsModelList = new ArrayList<String>() {
        };
        if (model != null && unsupportLocalServerF4v2HlsModelList.contains(model.toLowerCase())) {
            isEnableLocalServerF4v2Hls = false;
        }
        result.put(CONFIG_KEY_ENABLE_LOCAL_SERVER_F4V2HLS, Boolean.valueOf(isEnableLocalServerF4v2Hls));
        boolean isEnableLocalServerConvergeStream = true;
        List<String> unsupportLocalServerConvergeStreamModelList = new ArrayList<String>() {
        };
        if (model != null && unsupportLocalServerConvergeStreamModelList.contains(model.toLowerCase())) {
            isEnableLocalServerConvergeStream = false;
        }
        result.put(CONFIG_KEY_ENABLE_LOCAL_SERVER_STREAM, Boolean.valueOf(isEnableLocalServerConvergeStream));
        boolean updateSurfaceViewAfterStart = false;
        List<String> updateSurfaceViewAfterStartModelList = new ArrayList<String>() {
            {
                add("magicbox2");
            }
        };
        if (model != null && updateSurfaceViewAfterStartModelList.contains(model.toLowerCase())) {
            updateSurfaceViewAfterStart = true;
        }
        result.put(CONFIG_KEY_UPDATE_SURFACEVIEW_AFTER_START, Boolean.valueOf(updateSurfaceViewAfterStart));
        boolean useFdForLocalPlayback = false;
        List<String> useFdForLocalPlaybackModelList = new ArrayList<String>() {
            {
                add("magicbox1s_pro");
            }
        };
        if (model != null && useFdForLocalPlaybackModelList.contains(model.toLowerCase())) {
            useFdForLocalPlayback = true;
        }
        result.put(CONFIG_KEY_USE_FD_LOCAL_PLAYBACK, Boolean.valueOf(useFdForLocalPlayback));
        boolean disableAdCaster = false;
        List<Pair<String, String>> disableAdCasterDevices = new ArrayList<Pair<String, String>>() {
            {
                add(new Pair("c3000i", "mt5880"));
            }
        };
        if (model != null && cpu != null) {
            modelInfo = model.toLowerCase();
            String cpuInfo = cpu.toLowerCase();
            for (Pair<String, String> device : disableAdCasterDevices) {
                if (StringUtils.equals(modelInfo, (String) device.first) && cpuInfo.contains((CharSequence) device.second)) {
                    disableAdCaster = true;
                    break;
                }
            }
        }
        result.put(CONFIG_KEY_DISABLE_AD_CASTER, Boolean.valueOf(disableAdCaster));
        boolean openPluginIOBalance = true;
        List<Pair<String, String>> closeIOBalanceDevices = new ArrayList<Pair<String, String>>() {
        };
        if (model != null && cpu != null) {
            modelInfo = model.toLowerCase();
            cpuInfo = cpu.toLowerCase();
            if (!closeIOBalanceDevices.isEmpty()) {
                for (Pair<String, String> device2 : closeIOBalanceDevices) {
                    if (StringUtils.equals(modelInfo, (String) device2.first) && cpuInfo.contains((CharSequence) device2.second)) {
                        openPluginIOBalance = false;
                        break;
                    }
                }
            }
        }
        result.put(CONFIG_KEY_OPEN_PLUGIN_IO_BALANCE, Boolean.valueOf(openPluginIOBalance));
        boolean useMediaPlayerReset = false;
        List<String> useResetModelList = new ArrayList<String>() {
            {
                add("inphic_i9h");
                add("长虹智能电视");
                add("c3000i");
            }
        };
        if (model != null && useResetModelList.contains(model.toLowerCase())) {
            useMediaPlayerReset = true;
        }
        result.put(CONFIG_KEY_USE_MEDIAPLAYER_RESET, Boolean.valueOf(useMediaPlayerReset));
        boolean disableHcdnMultiProcess = false;
        List<String> disableHcdnMultiProcModelList = new ArrayList<String>() {
        };
        if (model != null && disableHcdnMultiProcModelList.contains(model.toLowerCase())) {
            disableHcdnMultiProcess = true;
        }
        result.put(CONFIG_KEY_DISABLE_HCDN_MULTI_PROCESS, Boolean.valueOf(disableHcdnMultiProcess));
        String uniplayerdata_config_open_exclude_f4v2hls = "{\"uniplayerdata_config\":{\"is_open\":\"1\",\"modules\":\"#default#rawf4v#rawmp4#rawhls#rawts#remuxaudiotrackhls#remuxaudiotrackts\"}}";
        List<String> modelListCloseF4v2Hls = new ArrayList<String>() {
            {
                add("skyworth 9r08 e6000");
            }
        };
        String uniplayerDataConfigJson = "{\"uniplayerdata_config\":{\"is_open\":\"1\",\"modules\":\"#\"}}";
        if (model != null && modelListCloseF4v2Hls.contains(model.toLowerCase())) {
            uniplayerDataConfigJson = uniplayerdata_config_open_exclude_f4v2hls;
        }
        result.put(CONFIG_KEY_UNIPLAYER_DATA_CONFIG_JSON, uniplayerDataConfigJson);
        String alternative_player_type_carousel_puma = "{\"common\":\"2,1\"}";
        String alternativePlayerType = "{\"common\":\"2,1\",\"carousel\":\"1\"}";
        if (model != null && useNativePlayerCarouselModelList.contains(model.toLowerCase())) {
            alternativePlayerType = alternative_player_type_carousel_puma;
        }
        result.put(CONFIG_KEY_PLAYER_TYPE_CONFIG, alternativePlayerType);
        JSONObject returnResult = new JSONObject();
        returnResult.putAll(result);
        return returnResult.toJSONString();
    }
}
