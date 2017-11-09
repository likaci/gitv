package com.gala.albumprovider.model;

import com.gala.tvapi.vrs.model.Entity;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tag {
    private int f243a = 0;
    private QLayoutKind f244a = QLayoutKind.LANDSCAPE;
    private String f245a = "";
    private List<Tag> f246a = null;
    private Map<String, Entity> f247a = new HashMap();
    public String available = "";
    private String f248b = "";
    private String f249c = "";
    public String channelId = "";
    public String channelId_forLive = "";
    private String f250d;
    private String f251e = "";
    private String f252f = "";
    private String f253g = "";
    private String f254h = "";
    private String f255i = "";
    public boolean isTopic = false;
    private String f256j = "";
    private String f257k = "";

    public Tag(String id, String name) {
        this.f245a = id;
        this.f248b = name;
    }

    public Tag(String id, String name, String type) {
        this.f245a = id;
        this.f248b = name;
        this.f249c = type;
    }

    public Tag(String id, String name, String type, QLayoutKind layout) {
        this.f245a = id;
        this.f248b = name;
        this.f249c = type;
        this.f244a = layout;
    }

    public Tag(String id, String name, String type, QLayoutKind layout, String chnId) {
        this.f245a = id;
        this.f248b = name;
        this.f249c = type;
        this.f244a = layout;
        this.channelId = chnId;
    }

    public Tag(String id, String name, QLayoutKind layout) {
        this.f245a = id;
        this.f248b = name;
        this.f244a = layout;
        this.f249c = "-100";
    }

    public Tag(String id, String name, String type, QLayoutKind layout, String chnId, String chnId_forLive) {
        this.f245a = id;
        this.f248b = name;
        this.f249c = type;
        this.f244a = layout;
        this.channelId = chnId;
        this.channelId_forLive = chnId_forLive;
    }

    public String getID() {
        return this.f245a;
    }

    public String getName() {
        return this.f248b;
    }

    public String getType() {
        return this.f249c;
    }

    public QLayoutKind getLayout() {
        return this.f244a;
    }

    public void setAlbumIds(String ids) {
        this.f250d = ids;
    }

    public String getAlbumIds() {
        return this.f250d;
    }

    public void setReason(int type, String... names) {
        if (names != null && names.length > 0) {
            if (type == 1 || type == 2 || type == 4) {
                this.f251e = LibString.ReasonGuessLike;
            } else if (type == 3) {
                this.f251e = LibString.ReasonHistory;
            }
            for (int i = 0; i < names.length; i++) {
                if (i == names.length - 1) {
                    this.f251e += "《" + names[i] + "》";
                } else {
                    this.f251e += "《" + names[i] + "》,";
                }
            }
        }
    }

    public String getReason() {
        return this.f251e;
    }

    public void setTagsList(List<Tag> list) {
        this.f246a = list;
    }

    public List<Tag> getTagList() {
        return this.f246a;
    }

    public void setResourceType(String type) {
        this.f252f = type;
    }

    public String getResourceType() {
        return this.f252f;
    }

    public void setIcon(String image) {
        this.f253g = image;
    }

    public String getIcon() {
        return this.f253g;
    }

    public void setLevel(int l) {
        this.f243a = l;
    }

    public int getLevel() {
        return this.f243a;
    }

    public void setParentName(String name) {
        this.f254h = name;
    }

    public String getParentName() {
        return this.f254h;
    }

    public void setName(String name) {
        this.f248b = name;
    }

    public void setType(String type) {
        this.f249c = type;
    }

    public void setSource(String source) {
        this.f255i = source;
    }

    public String getSource() {
        return this.f255i;
    }

    public void setESources(String source) {
        this.f256j = source;
    }

    public String getESources() {
        return this.f256j;
    }

    public void setFocusIcon(String icon) {
        this.f257k = icon;
    }

    public String getFocusIcon() {
        return this.f257k;
    }

    public void addAlbumInfo(String id, Entity e) {
        this.f247a.put(id, e);
    }

    public Map<String, Entity> getAlbumInfos() {
        return this.f247a;
    }

    public void setLayout(QLayoutKind layout) {
        this.f244a = layout;
    }

    public String getAvailable() {
        return this.available;
    }

    public void setAvailable(String available) {
        this.available = available;
    }
}
