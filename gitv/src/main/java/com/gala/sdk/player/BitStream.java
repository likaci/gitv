package com.gala.sdk.player;

import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;

public class BitStream implements Comparable<BitStream> {
    private int mAudioType;
    private int mBenefitType;
    private int mCodecType;
    private int mCtrlType;
    private int mDefinition;
    private int mDynamicRangeType;
    private int mPreviewTime;

    public static final class AudioType {
        public static final int DOLBY = 1;
        public static final int NORMAL = 0;
    }

    public static final class BenefitType {
        public static final int CAN_NOT_PLAY = 1;
        public static final int CAN_PLAY = 0;
        public static final int PREVIEW = 2;
    }

    public static final class CodecType {
        public static final int H211 = 1;
        public static final int H264 = 0;
    }

    public static final class CtrlType {
        public static final int CTRL_LOGIN = 1;
        public static final int CTRL_NONE = -1;
        public static final int CTRL_VIP = 0;
    }

    public static final class Definition {
        public static final int DEFINITION_1080P = 5;
        public static final int DEFINITION_4K = 10;
        public static final int DEFINITION_720P = 4;
        public static final int DEFINITION_HIGH = 2;
        public static final int DEFINITION_STANDARD = 1;
        public static final int DEFINITION_UNKNOWN = 0;
    }

    public static final class DynamicRangeType {
        public static final int DOLBY_VISION = 1;
        public static final int HDR10 = 2;
        public static final int SDR = 0;
    }

    public BitStream(int definition) {
        this.mDefinition = definition;
    }

    public void setDefinition(int definition) {
        this.mDefinition = definition;
    }

    public void setAudioType(int audioType) {
        this.mAudioType = audioType;
    }

    public void setCodecType(int codecType) {
        this.mCodecType = codecType;
    }

    public void setDynamicRangeType(int hdrType) {
        this.mDynamicRangeType = hdrType;
    }

    public void setBenefitType(int playType) {
        this.mBenefitType = playType;
    }

    public int getBenefitType() {
        return this.mBenefitType;
    }

    public int getCtrlType() {
        return this.mCtrlType;
    }

    public void setPreviewTime(int previewTime) {
        this.mPreviewTime = previewTime;
    }

    public int getPreviewTime() {
        return this.mPreviewTime;
    }

    public int getDefinition() {
        return this.mDefinition;
    }

    public int getAudioType() {
        return this.mAudioType;
    }

    public int getCodecType() {
        return this.mCodecType;
    }

    public int getDynamicRangeType() {
        return this.mDynamicRangeType;
    }

    public boolean isVip() {
        return this.mCtrlType == 0;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("BitStream[");
        stringBuilder.append("definition:").append(this.mDefinition);
        stringBuilder.append(", codecType:").append(this.mCodecType);
        stringBuilder.append(", audioType:").append(this.mAudioType);
        stringBuilder.append(", dynamicRangeType:").append(this.mDynamicRangeType);
        stringBuilder.append(", ctrlType:").append(this.mCtrlType);
        stringBuilder.append(", benefitType:").append(this.mBenefitType);
        stringBuilder.append(", previewTime:").append(this.mPreviewTime);
        stringBuilder.append(AlbumEnterFactory.SIGN_STR);
        return stringBuilder.toString();
    }

    public int compareTo(BitStream another) {
        return getDefinition() - another.getDefinition();
    }

    public boolean equal(BitStream another) {
        return this.mDefinition == another.getDefinition() && this.mAudioType == another.getAudioType() && this.mDynamicRangeType == another.getDynamicRangeType();
    }

    public static BitStream buildBitStreamFrom(int bid) {
        BitStream bitStream = new BitStream();
        switch (bid) {
            case 1:
                bitStream.setDefinition(1);
                break;
            case 2:
                bitStream.setDefinition(2);
                break;
            case 4:
                bitStream.setDefinition(4);
                break;
            case 5:
                bitStream.setDefinition(5);
                break;
            case 10:
                bitStream.setDefinition(10);
                break;
            case 14:
                bitStream.setDefinition(4);
                bitStream.setAudioType(1);
                break;
            case 15:
                bitStream.setDefinition(5);
                bitStream.setAudioType(1);
                break;
            case 16:
                bitStream.setDefinition(10);
                bitStream.setAudioType(1);
                break;
            case 17:
                bitStream.setDefinition(4);
                bitStream.setCodecType(1);
                break;
            case 18:
                bitStream.setDefinition(5);
                bitStream.setCodecType(1);
                break;
            case 19:
                bitStream.setDefinition(10);
                bitStream.setCodecType(1);
                break;
        }
        return bitStream;
    }

    public static int getBid(BitStream bitStream) {
        int definition = bitStream.getDefinition();
        switch (definition) {
            case 1:
            case 2:
                return definition;
            case 4:
                if (bitStream.getAudioType() == 1) {
                    definition = 14;
                }
                if (bitStream.getCodecType() == 1) {
                    return 17;
                }
                return definition;
            case 5:
                if (bitStream.getAudioType() == 1) {
                    definition = 15;
                }
                if (bitStream.getCodecType() == 1) {
                    return 18;
                }
                return definition;
            case 10:
                if (bitStream.getAudioType() == 1) {
                    definition = 16;
                }
                if (bitStream.getCodecType() == 1) {
                    return 19;
                }
                return definition;
            default:
                return 0;
        }
    }
}
