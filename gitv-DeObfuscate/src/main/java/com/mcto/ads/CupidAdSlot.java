package com.mcto.ads;

import java.util.HashMap;
import java.util.Map;

public class CupidAdSlot {
    public static final int SLOT_TYPE_COMMON_OVERLAY = 10;
    public static final int SLOT_TYPE_CORNER = 4;
    public static final int SLOT_TYPE_MARK = 5;
    public static final int SLOT_TYPE_MID_ROLL = 2;
    public static final int SLOT_TYPE_OVERLAY = 9;
    public static final int SLOT_TYPE_PAGE = 0;
    public static final int SLOT_TYPE_PAUSE = 6;
    public static final int SLOT_TYPE_POST_ROLL = 3;
    public static final int SLOT_TYPE_PRE_ROLL = 1;
    public static final int SLOT_TYPE_TOOLBAR = 7;
    public static final int SLOT_TYPE_VIEWPOINT = 8;
    private String adZoneId;
    private int duration;
    private int offsetInTimeline;
    private Map<String, Object> slotExtras;
    private int slotId;
    private int slotType;

    public CupidAdSlot(int slotId, int slotType, int offsetInTimeline, int duration, String adZoneId, Map<String, Object> slotExtras) {
        this.slotId = slotId;
        this.slotType = slotType;
        this.offsetInTimeline = offsetInTimeline;
        this.duration = duration;
        this.adZoneId = adZoneId;
        this.slotExtras = slotExtras;
    }

    public int getSlotId() {
        return this.slotId;
    }

    public int getSlotType() {
        return this.slotType;
    }

    public int getOffsetInTimeline() {
        return this.offsetInTimeline;
    }

    public int getDuration() {
        return this.duration;
    }

    public String getAdZoneId() {
        if (this.adZoneId == null) {
            return "";
        }
        return this.adZoneId;
    }

    public Map<String, Object> getSlotExtras() {
        if (this.slotExtras == null) {
            return new HashMap();
        }
        return this.slotExtras;
    }
}
