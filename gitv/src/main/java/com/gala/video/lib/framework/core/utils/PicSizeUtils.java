package com.gala.video.lib.framework.core.utils;

import android.text.TextUtils;
import com.gala.video.lib.share.common.configs.ApiConstants;
import com.gala.video.lib.share.common.configs.HomeDataConfig.ItemSize;
import com.gala.video.lib.share.ifmanager.bussnessIF.ads.AdsConstants;

public class PicSizeUtils {
    private static final int HASH_CODE_128_128 = PhotoSize._128_128.toString().hashCode();
    private static final int HASH_CODE_160_90 = PhotoSize._160_90.toString().hashCode();
    private static final int HASH_CODE_195_260 = PhotoSize._195_260.toString().hashCode();
    private static final int HASH_CODE_195_270 = PhotoSize._195_270.toString().hashCode();
    private static final int HASH_CODE_230_230 = PhotoSize._230_230.toString().hashCode();
    private static final int HASH_CODE_260_360 = PhotoSize._260_360.toString().hashCode();
    private static final int HASH_CODE_280_280 = PhotoSize._280_280.toString().hashCode();
    private static final int HASH_CODE_320_180 = PhotoSize._320_180.toString().hashCode();
    private static final int HASH_CODE_354_490 = PhotoSize._354_490.toString().hashCode();
    private static final int HASH_CODE_470_230 = PhotoSize._470_230.toString().hashCode();
    private static final int HASH_CODE_480_270 = PhotoSize._480_270.toString().hashCode();
    private static final int HASH_CODE_495_495 = PhotoSize._495_495.toString().hashCode();

    public enum PhotoSize {
        _260_360,
        _320_180,
        _354_490,
        _195_270,
        _230_230,
        _470_230,
        _280_280,
        _480_270,
        _160_90,
        _128_128,
        _495_495,
        _195_260,
        _480_360,
        _180_101
    }

    private static int getHashcode(String str) {
        if (StringUtils.isEmpty((CharSequence) str)) {
            return 0;
        }
        int index = str.lastIndexOf(".");
        return str.substring(index - 8, index).hashCode();
    }

    public static int[] parseSize(String url) {
        int hasCode = getHashcode(url);
        if (HASH_CODE_260_360 == hasCode) {
            return new int[]{260, ItemSize.ITEM_360};
        }
        if (HASH_CODE_195_260 == hasCode) {
            return new int[]{ApiConstants.IMAGE_CORNER_SIZE_195, 260};
        }
        if (HASH_CODE_320_180 == hasCode) {
            return new int[]{320, 180};
        }
        if (HASH_CODE_354_490 == hasCode) {
            return new int[]{ItemSize.ITEM_354, ItemSize.ITEM_490};
        }
        if (HASH_CODE_195_270 == hasCode) {
            return new int[]{ApiConstants.IMAGE_CORNER_SIZE_195, 270};
        }
        if (HASH_CODE_230_230 == hasCode) {
            return new int[]{ItemSize.ITEM_230, ItemSize.ITEM_230};
        }
        if (HASH_CODE_470_230 == hasCode) {
            return new int[]{AdsConstants.HOME_FOCUS_IMAGE_AD_HEIGHT_DEFAULT, ItemSize.ITEM_230};
        }
        if (HASH_CODE_280_280 == hasCode) {
            return new int[]{280, 280};
        }
        if (HASH_CODE_480_270 == hasCode) {
            return new int[]{ItemSize.ITEM_480, 270};
        }
        if (HASH_CODE_160_90 == hasCode) {
            return new int[]{160, 90};
        }
        if (HASH_CODE_128_128 == hasCode) {
            return new int[]{128, 128};
        }
        if (HASH_CODE_495_495 == hasCode) {
            return new int[]{495, 495};
        }
        return new int[]{260, ItemSize.ITEM_360};
    }

    public static String getUrlWithSize(PhotoSize pageType, String url) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        int index = url.lastIndexOf(".");
        return index >= 0 ? index : url;
    }

    private static PhotoSize getPhotoSizeByPictureSize(int pictureSize) {
        PhotoSize photoSize = PhotoSize._260_360;
        switch (pictureSize) {
            case 1:
                return PhotoSize._260_360;
            case 2:
                return PhotoSize._320_180;
            case 3:
                return PhotoSize._354_490;
            case 4:
                return PhotoSize._195_270;
            case 5:
                return PhotoSize._230_230;
            case 6:
                return PhotoSize._470_230;
            case 7:
                return PhotoSize._280_280;
            case 8:
                return PhotoSize._480_270;
            case 9:
                return PhotoSize._160_90;
            case 10:
                return PhotoSize._128_128;
            case 11:
                return PhotoSize._495_495;
            case 12:
                return PhotoSize._195_260;
            default:
                return photoSize;
        }
    }

    public static String exchangePictureUrl(String url, int pictureSize) {
        if (TextUtils.isEmpty(url)) {
            return null;
        }
        int index = url.lastIndexOf(".");
        if (index < 0 || !isEndWithPictureSizeType(url.substring(index - 8, index))) {
            return null;
        }
        return url.substring(0, index - 8) + getPhotoSizeByPictureSize(pictureSize).toString() + url.substring(index);
    }

    private static boolean isEndWithPictureSizeType(String pictureSize) {
        for (PhotoSize photoSize : PhotoSize.values()) {
            if (pictureSize.equals(photoSize.toString())) {
                return true;
            }
        }
        return false;
    }
}
