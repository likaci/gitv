package com.gala.albumprovider.model;

import com.gala.tvapi.vrs.model.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tag {
    private int a = 0;
    private QLayoutKind f56a = QLayoutKind.LANDSCAPE;
    private String f57a = "";
    private List<Tag> f58a = null;
    private Map<String, Entity> f59a = new HashMap();
    public String available = "";
    private String b = "";
    private String c = "";
    public String channelId = "";
    public String channelId_forLive = "";
    private String d;
    private String e = "";
    private String f = "";
    private String g = "";
    private String h = "";
    private String i = "";
    public boolean isTopic = false;
    private String j = "";
    private String k = "";

    public Tag(String id, String name) {
        this.f57a = id;
        this.b = name;
    }

    public Tag(String id, String name, String type) {
        this.f57a = id;
        this.b = name;
        this.c = type;
    }

    public Tag(String id, String name, String type, QLayoutKind layout) {
        this.f57a = id;
        this.b = name;
        this.c = type;
        this.f56a = layout;
    }

    public Tag(String id, String name, String type, QLayoutKind layout, String chnId) {
        this.f57a = id;
        this.b = name;
        this.c = type;
        this.f56a = layout;
        this.channelId = chnId;
    }

    public Tag(String id, String name, QLayoutKind layout) {
        this.f57a = id;
        this.b = name;
        this.f56a = layout;
        this.c = "-100";
    }

    public Tag(String id, String name, String type, QLayoutKind layout, String chnId, String chnId_forLive) {
        this.f57a = id;
        this.b = name;
        this.c = type;
        this.f56a = layout;
        this.channelId = chnId;
        this.channelId_forLive = chnId_forLive;
    }

    public String getID() {
        return this.f57a;
    }

    public String getName() {
        return this.b;
    }

    public String getType() {
        return this.c;
    }

    public QLayoutKind getLayout() {
        return this.f56a;
    }

    public void setAlbumIds(String ids) {
        this.d = ids;
    }

    public String getAlbumIds() {
        return this.d;
    }

    public void setReason(int type, String... names) {
        if (names != null && names.length > 0) {
            if (type == 1 || type == 2 || type == 4) {
                this.e = LibString.ReasonGuessLike;
            } else if (type == 3) {
                this.e = LibString.ReasonHistory;
            }
            for (int i = 0; i < names.length; i++) {
                if (i == names.length - 1) {
                    this.e += "《" + names[i] + "》";
                } else {
                    this.e += "《" + names[i] + "》,";
                }
            }
        }
    }

    public String getReason() {
        return this.e;
    }

    public void setTagsList(List<Tag> list) {
        this.f58a = list;
    }

    public List<Tag> getTagList() {
        return this.f58a;
    }

    public void setResourceType(String type) {
        this.f = type;
    }

    public String getResourceType() {
        return this.f;
    }

    public void setIcon(String image) {
        this.g = image;
    }

    public String getIcon() {
        return this.g;
    }

    public void setLevel(int l) {
        this.a = l;
    }

    public int getLevel() {
        return this.a;
    }

    public void setParentName(String name) {
        this.h = name;
    }

    public String getParentName() {
        return this.h;
    }

    public void setName(String name) {
        this.b = name;
    }

    public void setType(String type) {
        this.c = type;
    }

    public void setSource(String source) {
        this.i = source;
    }

    public String getSource() {
        return this.i;
    }

    public void setESources(String source) {
        this.j = source;
    }

    public String getESources() {
        return this.j;
    }

    public void setFocusIcon(String icon) {
        this.k = icon;
    }

    public String getFocusIcon() {
        return this.k;
    }

    public void addAlbumInfo(String id, Entity e) {
        this.f59a.put(id, e);
    }

    public Map<String, Entity> getAlbumInfos() {
        return this.f59a;
    }

    public void setLayout(QLayoutKind layout) {
        this.f56a = layout;
    }

    public String getAvailable() {
        return this.available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
