package com.gala.sdk.player.data;

import com.gala.tvapi.tv2.model.TVChannelCarousel;
import com.gala.tvapi.tv2.model.TVProgramCarousel;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CarouselChannelDetail {
    private TVChannelCarousel a;
    private TVProgramCarousel f353a;
    private String f354a;
    private TVProgramCarousel b;

    public CarouselChannelDetail(TVChannelCarousel channel) {
        this.a = channel;
    }

    public void setTableNo(String tableNo) {
        this.f354a = tableNo;
    }

    public String getTableNo() {
        return this.f354a;
    }

    public void setCurrentProgram(TVProgramCarousel program) {
        this.f353a = program;
    }

    public void setNextProgram(TVProgramCarousel program) {
        this.b = program;
    }

    public TVProgramCarousel getCurrentProgram() {
        return this.f353a;
    }

    public TVProgramCarousel getNextProgram() {
        return this.b;
    }

    public long getChannelId() {
        return this.a.id;
    }

    public String getCurrentProgramName() {
        return this.f353a != null ? this.f353a.name : "";
    }

    public String getCurrentProgramStartTime() {
        return this.f353a != null ? String.valueOf(this.f353a.bt) : "";
    }

    public String getCurrentProgramEndTime() {
        return this.f353a != null ? String.valueOf(this.f353a.et) : "";
    }

    public String getNextProgramName() {
        return this.b != null ? this.b.name : "";
    }

    public String getNextProgramStartTime() {
        return this.b != null ? String.valueOf(this.b.bt) : "";
    }

    public String getNextProgramEndTime() {
        return this.b != null ? String.valueOf(this.b.et) : "";
    }

    public String getCurrentDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(time));
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("CarouselChannelDetail@").append(Integer.toHexString(hashCode())).append("{");
        stringBuilder.append("chanelId=").append(this.a.id);
        stringBuilder.append(", channelName=").append(this.a.name);
        stringBuilder.append(", currentProgram.name=").append(this.f353a != null ? this.f353a.name : "");
        stringBuilder.append(", currentProgram.startTime=").append(this.f353a != null ? getCurrentDate(this.f353a.bt) : "");
        stringBuilder.append(", currentProgram.endTime=").append(this.f353a != null ? getCurrentDate(this.f353a.et) : "");
        stringBuilder.append(", nextProgram.name=").append(this.b != null ? this.b.name : "");
        stringBuilder.append(", nextProgram.startTime=").append(this.b != null ? getCurrentDate(this.b.bt) : "");
        stringBuilder.append(", currentProgram.endTime=").append(this.b != null ? getCurrentDate(this.b.et) : "");
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
