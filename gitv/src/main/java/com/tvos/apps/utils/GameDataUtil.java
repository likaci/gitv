package com.tvos.apps.utils;

import android.util.Log;

public class GameDataUtil {
    public static final int GAMETYPE_FREE = 0;
    public static final int GAMETYPE_PAYGAME_ALLFREE = 3;
    public static final int GAMETYPE_PAYGAME_DIFFDISCOUNT = 9;
    public static final int GAMETYPE_PAYGAME_NODISCOUNT = 7;
    public static final int GAMETYPE_PAYGAME_SAMEDISCOUNT = 8;
    public static final int GAMETYPE_PAYGAME_VIPDISCOUNT = 6;
    public static final int GAMETYPE_PAYGAME_VIPFREE = 4;
    public static final int GAMETYPE_PAYGAME_VIPFREE_NORMALDISCOUNT = 5;
    public static final int GAMETYPE_VIPFREE = 1;
    public static final int GAMETYPE_VIPFREE_NORMALDISCOUNT = 2;
    private static final String TAG = "GameDataUtil";

    public static int getGameType(float price, boolean hasDiscount, boolean vipNeedPay, float discountedPrice, float vipDiscountedPrice) {
        Log.i(TAG, "getGameType, price = " + price + " , hasDiscount = " + hasDiscount + " , vipNeedPay = " + vipNeedPay + " , discountedPrice = " + discountedPrice + " , vipDiscountedPrice = " + vipDiscountedPrice);
        int gameType = 0;
        if (price == 0.0f) {
            gameType = 0;
        } else {
            boolean discountedPriceNormal = isDiscountNormal(price, discountedPrice);
            boolean vipDiscountedPriceNormal = isDiscountNormal(price, vipDiscountedPrice);
            if (!hasDiscount && !vipNeedPay) {
                gameType = 1;
            } else if (hasDiscount && !vipNeedPay && discountedPriceNormal) {
                gameType = 2;
            } else if (hasDiscount && vipNeedPay && discountedPrice == 0.0f && vipDiscountedPrice == 0.0f) {
                gameType = 3;
            } else if (!hasDiscount && vipNeedPay && vipDiscountedPrice == 0.0f) {
                gameType = 4;
            } else if (hasDiscount && vipNeedPay && discountedPriceNormal && vipDiscountedPrice == 0.0f) {
                gameType = 5;
            } else if (!hasDiscount && vipNeedPay && vipDiscountedPriceNormal) {
                gameType = 6;
            } else if (!hasDiscount && vipNeedPay && vipDiscountedPrice == price) {
                gameType = 7;
            } else if (hasDiscount && vipNeedPay && discountedPrice == vipDiscountedPrice && discountedPriceNormal) {
                gameType = 8;
            } else if (hasDiscount && vipNeedPay && discountedPriceNormal && vipDiscountedPriceNormal && discountedPrice != vipDiscountedPrice) {
                gameType = 9;
            }
        }
        Log.i(TAG, "getGameType, gameType = " + gameType);
        return gameType;
    }

    private static boolean isDiscountNormal(float price, float discountPrice) {
        return discountPrice > 0.0f && discountPrice < price;
    }
}
