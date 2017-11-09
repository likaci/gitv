package com.gala.sdk.player.data;

import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVProgramCarousel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarouselChannelDetail {
    private TVChannelCarousel f693a;
    private TVProgramCarousel f694a;
    private String f695a;
    private TVProgramCarousel f696b;

    public CarouselChannelDetail(TVChannelCarousel channel) {
        this.f693a = channel;
    }

    public void setTableNo(String tableNo) {
        this.f695a = tableNo;
    }

    public String getTableNo() {
        return this.f695a;
    }

    public void setCurrentProgram(TVProgramCarousel program) {
        this.f694a = program;
    }

    public void setNextProgram(TVProgramCarousel program) {
        this.f696b = program;
    }

    public TVProgramCarousel getCurrentProgram() {
        return this.f694a;
    }

    public TVProgramCarousel getNextProgram() {
        return this.f696b;
    }

    public long getChannelId() {
        return this.f693a.id;
    }

    public String getCurrentProgramName() {
        return this.f694a != null ? this.f694a.name : "";
    }

    public String getCurrentProgramStartTime() {
        return this.f694a != null ? String.valueOf(this.f694a.bt) : "";
    }

    public String getCurrentProgramEndTime() {
        return this.f694a != null ? String.valueOf(this.f694a.et) : "";
    }

    public String getNextProgramName() {
        return this.f696b != null ? this.f696b.name : "";
    }

    public String getNextProgramStartTime() {
        return this.f696b != null ? String.valueOf(this.f696b.bt) : "";
    }

    public String getNextProgramEndTime() {
        return this.f696b != null ? String.valueOf(this.f696b.et) : "";
    }

    public String getCurrentDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CarouselChannelDetail@").append(Integer.toHexString(hashCode())).append("{");
        stringBuilder.append("chanelId=").append(this.f693a.id);
        stringBuilder.append(", channelName=").append(this.f693a.name);
        stringBuilder.append(", currentProgram.name=").append(this.f694a != null ? this.f694a.name : "");
        stringBuilder.append(", currentProgram.startTime=").append(this.f694a != null ? getCurrentDate(this.f694a.bt) : "");
        stringBuilder.append(", currentProgram.endTime=").append(this.f694a != null ? getCurrentDate(this.f694a.et) : "");
        stringBuilder.append(", nextProgram.name=").append(this.f696b != null ? this.f696b.name : "");
        stringBuilder.append(", nextProgram.startTime=").append(this.f696b != null ? getCurrentDate(this.f696b.bt) : "");
        stringBuilder.append(", currentProgram.endTime=").append(this.f696b != null ? getCurrentDate(this.f696b.et) : "");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
