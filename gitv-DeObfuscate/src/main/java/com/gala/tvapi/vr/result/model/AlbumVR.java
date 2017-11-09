package com.gala.tvapi.vr.result.model;

import com.gala.tvapi.type.AlbumType;
import java.io.Serializable;

public class AlbumVR implements Serializable {
    public CastVR cast;
    public int chnId;
    public String desc;
    public int exclusive;
    public int is3D;
    public int isPurchase;
    public int isSeries;
    public long len;
    public String name;
    public String pic;
    public String qipuId;
    public String score;
    public String shortName;
    public String sourceCode;
    public String superId;
    public String superName;
    public String tag;
    public String time;
    public int tvCount;
    public String tvPic;
    public int tvsets;
    public int type;

    public boolean isExclusive() {
        return this.exclusive == 1;
    }

    public AlbumType getType() {
        switch (this.type) {
            case 0:
                return AlbumType.VIDEO;
            case 1:
                return AlbumType.ALBUM;
            case 2:
                return AlbumType.PLAYLIST;
            case 14:
                return AlbumType.OFFLINE;
            case 99:
                return AlbumType.PEOPLE;
            default:
                return AlbumType.VIDEO;
        }
    }

    public boolean isPurchase() {
        return this.isPurchase > 0;
    }

    public boolean is3D() {
        return this.is3D == 1;
    }
}
