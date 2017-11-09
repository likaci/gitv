package com.gala.video.app.player.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import java.io.File;
import java.util.List;

public class PlayerUIHelper {
    private static final int SCREEN_HEIGHT_1080P = 1080;
    private static final int SCREEN_WIDTH_1080P = 1920;
    private static final String TAG = "Player/Ui/PlayerUiHelper";

    private interface IBitmapDecoder {
        Bitmap decodeWithOptions(Options options);
    }

    private static class FileDecoder implements IBitmapDecoder {
        private String mFilePath;

        public FileDecoder(String path) {
            this.mFilePath = path;
        }

        public Bitmap decodeWithOptions(Options opt) {
            return BitmapFactory.decodeFile(this.mFilePath, opt);
        }
    }

    private static class ResDecoder implements IBitmapDecoder {
        private Resources mRes;
        private int mResId;

        public ResDecoder(Resources res, int resId) {
            this.mResId = resId;
            this.mRes = res;
        }

        public Bitmap decodeWithOptions(Options opt) {
            return BitmapFactory.decodeResource(this.mRes, this.mResId, opt);
        }
    }

    public static SpannableString getHighLightString(String content, String highLightString) {
        SpannableString sb = new SpannableString(content);
        int start = content.indexOf(highLightString);
        if (start >= 0) {
            sb.setSpan(new ForegroundColorSpan(-19200), start, start + highLightString.length(), 33);
        }
        return sb;
    }

    public static SpannableString getVIPHighLightString(String highLightString) {
        SpannableString sb = new SpannableString(highLightString);
        if (null >= null) {
            sb.setSpan(new ForegroundColorSpan(Color.parseColor("#F4C288")), 0, highLightString.length(), 33);
        }
        return sb;
    }

    public static Bitmap getVideoDeriveBitmap(String albumId) {
        Bitmap bitmap;
        CharSequence jstv = getJSTVList();
        CharSequence pps = getPPSList();
        if (albumId == null) {
            bitmap = getGala();
        } else if (!StringUtils.isEmpty(jstv) && jstv.contains(albumId)) {
            bitmap = getJSTV();
        } else if (StringUtils.isEmpty(pps) || !pps.contains(albumId)) {
            bitmap = getGala();
        } else {
            bitmap = getPPS();
        }
        if (PlayerAppConfig.getVideoDeriveBitmap() != null) {
            return PlayerAppConfig.getVideoDeriveBitmap();
        }
        return bitmap;
    }

    private static String getJSTVList() {
        String str = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || StringUtils.isEmpty(model.getJstvList()))) {
            str = model.getJstvList();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getJSTVList:" + str);
        }
        return str;
    }

    private static String getPPSList() {
        String str = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || StringUtils.isEmpty(model.getPPSList()))) {
            str = model.getPPSList();
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getPPSList:" + str);
        }
        return str;
    }

    private static Bitmap getJSTV() {
        return decodeFile(getJSTVPath());
    }

    private static Bitmap getPPS() {
        return decodeFile(getPPSPath());
    }

    private static Bitmap getGala() {
        return decodeFile(getGalaPath());
    }

    private static String getJSTVPath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getJSTVUrl()))) {
            List jstvlist = model.getJSTVUrl();
            if (!ListUtils.isEmpty(jstvlist)) {
                path = (String) jstvlist.get(0);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getJSTVPath path = " + path);
        }
        return path;
    }

    private static String getPPSPath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getPPSUrl()))) {
            List ppslist = model.getPPSUrl();
            if (!ListUtils.isEmpty(ppslist)) {
                path = (String) ppslist.get(0);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getPPSPath path = " + path);
        }
        return path;
    }

    private static String getGalaPath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getGalaUrl()))) {
            List galalist = model.getGalaUrl();
            if (!ListUtils.isEmpty(galalist)) {
                path = (String) galalist.get(0);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getPath path = " + path);
        }
        return path;
    }

    private static Bitmap decodeFile(String cachePath) {
        Bitmap bitmap = null;
        if (!StringUtils.isEmpty((CharSequence) cachePath)) {
            File cacheFile = new File(cachePath);
            if (cacheFile.exists()) {
                bitmap = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "decodeFile bitmap = " + bitmap);
        }
        return bitmap;
    }

    public static String getJumpAdTip() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            CharSequence ad = model.getAdInfo();
            if (!StringUtils.isEmpty(ad)) {
                path = ad;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getJumpAdTip path = " + path);
        }
        return path;
    }

    public static String getcannotJumpAdTip() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            CharSequence ad = model.getCanntJumpAdvertising();
            if (!StringUtils.isEmpty(ad)) {
                path = ad;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getcanntJumpAdTip path=" + path);
        }
        return path;
    }

    public static String getPushVipPreviewTip() {
        String tip = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            CharSequence pushTip = model.getVipPushPreviewTip();
            if (!StringUtils.isEmpty(pushTip)) {
                tip = pushTip;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getPushVipPreviewTip tip=" + tip);
        }
        return tip;
    }

    public static String getPreviewNeedBuyTip() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            CharSequence iseUrl = model.getIseUrlString();
            if (!StringUtils.isEmpty(iseUrl)) {
                path = iseUrl;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getPreviewNeedBuyTip path=" + path);
        }
        return path;
    }

    public static String getPreviewCannotBuyTip() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            CharSequence onlyIsee = model.getOnlyIsee();
            if (!StringUtils.isEmpty(onlyIsee)) {
                path = onlyIsee;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getPreviewCannotBuyTip path=" + path);
        }
        return path;
    }

    public static String getNewPreviewPurchaseTip() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            CharSequence previewTip = model.getPurchaseGuideTipText();
            if (!StringUtils.isEmpty(previewTip)) {
                path = previewTip;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getNewPreviewPurchaseTip path=" + path);
        }
        return path;
    }

    public static String getLivePreviewPurchaseTip() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (model != null) {
            CharSequence previewTip = model.getLivePurchaseGuideTipText();
            if (!StringUtils.isEmpty(previewTip)) {
                path = previewTip;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getLivePreviewPurchaseTip path=" + path);
        }
        return path;
    }

    public static Object[] getPreviewPurchasePicture(Context context, boolean is3D) {
        return decodeTargetBitmap(context, new FileDecoder(getPreviewPurchasePicturePath()), is3D);
    }

    private static String getPreviewPurchasePicturePath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getPurchaseGuideTipUrl()))) {
            List list = model.getPurchaseGuideTipUrl();
            if (!ListUtils.isEmpty(list)) {
                path = (String) list.get(0);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getPreviewPurchasePicturePath path = " + path);
        }
        return path;
    }

    public static Object[] getLLivePurchasePicture(Context context) {
        return decodeTargetBitmap(context, new FileDecoder(getLivePreviewPurchasePicturePath()), false);
    }

    private static String getLivePreviewPurchasePicturePath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getLivePurchaseGuideTipUrl()))) {
            List list = model.getLivePurchaseGuideTipUrl();
            if (!ListUtils.isEmpty(list)) {
                path = (String) list.get(0);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getLivePreviewPurchasePicturePath path = " + path);
        }
        return path;
    }

    private static Object[] decodeTargetBitmap(Context context, IBitmapDecoder decoder, boolean is3D) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        float heightRatioScreen = ((float) dm.heightPixels) / 1080.0f;
        float widthRatioScreen = ((float) dm.widthPixels) / 1920.0f;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "checkPictureSize heightRatioScreen=" + heightRatioScreen);
        }
        Options option = new Options();
        option.inJustDecodeBounds = true;
        decoder.decodeWithOptions(option);
        float nativeWidth = (float) option.outWidth;
        float nativeHeight = (float) option.outHeight;
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "decodeTargetBitmap: original w/h=" + nativeWidth + "/" + nativeHeight);
        }
        int targetWidth = (int) (nativeWidth * widthRatioScreen);
        int targetHeight = (int) (nativeHeight * heightRatioScreen);
        r17 = new Object[3];
        r17[0] = createTargetBitmap(context, decoder, nativeWidth, nativeHeight, (float) targetWidth, (float) targetHeight, ScaleType.CENTER_INSIDE, Config.ARGB_8888);
        r17[1] = Integer.valueOf(targetWidth);
        r17[2] = Integer.valueOf(targetHeight);
        return r17;
    }

    private static Bitmap createTargetBitmap(Context context, IBitmapDecoder decoder, float nativeWidth, float nativeHeight, float targetWidth, float targetHeight, ScaleType type, Config config) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createTargetBitmap: target w/h=" + targetWidth + "/" + targetHeight + ", scale type=" + type);
        }
        try {
            Options options = createBasicOptions(config);
            float scaledWidth = targetWidth;
            float scaledHeight = targetHeight;
            options.inSampleSize = 1;
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createTargetBitmap: scaled w/h=" + scaledWidth + "/" + scaledHeight);
            }
            options.inJustDecodeBounds = false;
            return decoder.decodeWithOptions(options);
        } catch (OutOfMemoryError ex) {
            LogUtils.m1572e(TAG, "<< createTargetBitmap: OOM", ex);
            return null;
        }
    }

    private static Options createBasicOptions(Config config) {
        Options options = new Options();
        if (config != null) {
            options.inPreferredConfig = config;
            if (config != Config.ARGB_8888) {
                options.inDither = true;
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "createBasicOptions() return " + options);
        }
        return options;
    }

    public static Object[] getDefaultBitmap(Context context, boolean is3D) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getDefaultBitmap()");
        }
        return decodeTargetBitmap(context, new ResDecoder(context.getResources(), C1291R.drawable.player_tip_indication), is3D);
    }

    public static Object[] getLiveDefaultBitmap(Context context) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getLiveDefaultBitmap()");
        }
        return decodeTargetBitmap(context, new ResDecoder(context.getResources(), C1291R.drawable.player_live_tip_indication), false);
    }

    public static Object[] getHDRDefaultBitmap(Context context, boolean is3D) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getHDRDefaultBitmap()");
        }
        return decodeTargetBitmap(context, new ResDecoder(context.getResources(), C1291R.drawable.player_hdr_tip_indication), is3D);
    }

    public static Object[] getLoginBitmap(Context context, boolean is3D) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getLoginBitmap()");
        }
        return decodeTargetBitmap(context, new ResDecoder(context.getResources(), C1291R.drawable.player_login), is3D);
    }
}
