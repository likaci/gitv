package com.gala.video.lib.share.ifimpl.openplay.service.tools;

import com.gala.tvapi.tv2.constants.ChannelId;
import com.qiyi.tv.client.data.Channel;

public class OpenApiChannelMap {
    private static final int[][] CHANNEL_MAP = new int[][]{new int[]{ChannelId.CHANNEL_ID_1080P, 104}, new int[]{ChannelId.CHANNEL_ID_3D, 103}, new int[]{10002, 105}, new int[]{26, 17}, new int[]{4, 3}, new int[]{33, 102}, new int[]{3, 15}, new int[]{10003, 106}, new int[]{12, 8}, new int[]{7, 6}, new int[]{2, 2}, new int[]{13, 11}, new int[]{1, 1}, new int[]{24, 14}, new int[]{22, 12}, new int[]{15, 16}, new int[]{21, 13}, new int[]{28, 19}, new int[]{5, 4}, new int[]{25, 18}, new int[]{10, 7}, new int[]{17, 9}, new int[]{9, 10}, new int[]{6, 5}, new int[]{1000002, 109}, new int[]{100004, 108}, new int[]{ChannelId.CHANNEL_ID_LAW, 1006}, new int[]{ChannelId.CHANNEL_ID_EMOTION, 1007}, new int[]{ChannelId.CHANNEL_ID_HEALTH, 1008}, new int[]{ChannelId.CHANNEL_ID_ENCODE, 1009}, new int[]{ChannelId.CHANNEL_ID_OPUSCULUM, Channel.ID_OPUSCULUM}, new int[]{ChannelId.CHANNEL_ID_LIYUANCHUN, 10011}, new int[]{ChannelId.CHANNEL_ID_SQUARE, Channel.ID_SQUARE}, new int[]{10009, 20}, new int[]{29, 29}, new int[]{ChannelId.CHANNEL_ID_H265, 107}, new int[]{10005, 10013}};
    private static final int CHANNEL_OFFSET = 10000;

    public static int encodeChannelId(int id) {
        int sdkId = -1;
        for (int[] pair : CHANNEL_MAP) {
            if (pair[0] == id) {
                sdkId = pair[1];
            }
        }
        if (sdkId < 0) {
            return id + 10000;
        }
        return sdkId;
    }

    public static int decodeChannelId(int id) {
        int localId = -1;
        for (int[] pair : CHANNEL_MAP) {
            if (pair[1] == id) {
                localId = pair[0];
            }
        }
        if (localId >= 0 || id < 10000) {
            return localId;
        }
        return id - 10000;
    }
}
