package com.gala.video.app.epg.home.data.hdata.task;

import java.io.Serializable;

public class DailyTabInfo implements Serializable {
    public String id;
    public String label;

    public DailyTabInfo(String label, String id) {
        this.label = label;
        this.id = id;
    }
}
