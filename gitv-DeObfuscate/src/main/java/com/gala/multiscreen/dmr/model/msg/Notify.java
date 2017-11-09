package com.gala.multiscreen.dmr.model.msg;

import com.gala.multiscreen.dmr.model.MSMessage;
import java.util.List;

public class Notify extends DlnaMessage {
    public String album_id;
    public String boss;
    public String ctype;
    public String key;
    public int play_duration;
    public int play_position;
    public int play_state;
    public String res;
    public List<String> res_list;
    public String session;
    public String title;
    public String title_next;
    public String video_id;
    public boolean vip_purchase;

    public Notify() {
        this.album_id = "";
        this.video_id = "";
        this.title = "";
        this.title_next = "";
        this.boss = "";
        this.ctype = "";
        this.res = "";
        this.play_state = 0;
        this.key = "";
        this.session = "";
        this.play_position = 0;
        this.play_duration = 0;
        this.vip_purchase = false;
        this.key = MSMessage.VALUE_KEY;
        this.session = MSMessage.VALUE_SESSION;
    }
}
