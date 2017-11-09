package com.gala.video.app.epg.ui.star.model;

public class StarTaskParams {
    public long end;
    public long start;

    public long getGapTime() {
        return this.end - this.start;
    }
}
