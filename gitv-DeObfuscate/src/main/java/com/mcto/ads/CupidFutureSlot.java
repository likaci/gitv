package com.mcto.ads;

public class CupidFutureSlot {
    private int sequenceId;
    private long startTime;
    private int type;

    public CupidFutureSlot(long startTime, int type, int sequenceId) {
        this.startTime = startTime;
        this.type = type;
        this.sequenceId = sequenceId;
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

    public int getStartTimes() {
        return (int) this.startTime;
    }
}
