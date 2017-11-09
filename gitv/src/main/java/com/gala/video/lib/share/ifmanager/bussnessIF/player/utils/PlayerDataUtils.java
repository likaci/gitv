package com.gala.video.lib.share.ifmanager.bussnessIF.player.utils;

import android.content.Context;
import com.gala.sdk.player.BitStream;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;

public class PlayerDataUtils {
    private static final String TAG = "PlayerDataUtils";

    public static String getBitStreamString(Context context, BitStream bitStream) {
        String bitStreamName = "";
        if (context == null || bitStream == null) {
            LogRecordUtils.logd(TAG, "<< getBitStreamString, error");
            new Throwable().printStackTrace();
            return bitStreamName;
        }
        switch (bitStream.getDefinition()) {
            case 1:
                bitStreamName = context.getResources().getString(R.string.definition_standard);
                break;
            case 2:
                bitStreamName = context.getResources().getString(R.string.definition_high);
                break;
            case 4:
                bitStreamName = context.getResources().getString(R.string.definition_720P);
                break;
            case 5:
                bitStreamName = context.getResources().getString(R.string.definition_1080P);
                break;
            case 10:
                if (bitStream.getCodecType() != 0) {
                    bitStreamName = context.getResources().getString(R.string.definition_4K);
                    break;
                }
                bitStreamName = context.getResources().getString(R.string.definition_4K_HD);
                break;
            default:
                LogRecordUtils.logd(TAG, "invalid bitstream, definition=" + bitStream.getDefinition());
                return bitStreamName;
        }
        if (bitStream.getAudioType() == 1) {
            bitStreamName = bitStreamName + context.getResources().getString(R.string.definition_postfix_dolby);
        }
        return bitStreamName;
    }
}
