package com.gala.tvapi.vrs.model;

import java.util.List;

public class TVTags extends Model {
    private static final long serialVersionUID = 1;
    public int channelId = 0;
    public String channelName = "";
    public List<TVTag> tags;

    public String getTags() {
        String str = "";
        if (this.tags != null && this.tags.size() > 0) {
            String str2 = str;
            for (TVTag tVTag : this.tags) {
                str2 = str2 + "," + tVTag.value;
            }
            str = str2;
        }
        return str.replaceFirst(",", "");
    }
}
