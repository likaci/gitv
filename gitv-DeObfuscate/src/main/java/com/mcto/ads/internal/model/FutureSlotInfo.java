package com.mcto.ads.internal.model;

import com.mcto.ads.internal.common.JsonBundleConstants;
import org.json.JSONException;
import org.json.JSONObject;

public class FutureSlotInfo {
    private int sequenceId;
    private long startTime;
    private int type;

    FutureSlotInfo(JSONObject jsonSlot) throws JSONException {
        if (jsonSlot.has("type")) {
            this.type = jsonSlot.getInt("type");
        }
        if (jsonSlot.has("startTime")) {
            this.startTime = (long) (jsonSlot.getInt("startTime") * 1000);
        }
        if (jsonSlot.has(JsonBundleConstants.FUTURE_SLOT_SEQUENCE_ID)) {
            this.sequenceId = jsonSlot.getInt(JsonBundleConstants.FUTURE_SLOT_SEQUENCE_ID);
        }
    }

    public long getStartTime() {
        return this.startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getSequenceId() {
        return this.sequenceId;
    }

    public void setSequenceId(int sequenceId) {
        this.sequenceId = sequenceId;
    }
}
