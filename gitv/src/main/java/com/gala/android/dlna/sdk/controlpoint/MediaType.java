package com.gala.android.dlna.sdk.controlpoint;

public enum MediaType {
    VIDEO("object.item.videoItem.movie"),
    MUSIC(AudioType),
    IMAGE("object.item.imageItem.photo");
    
    private static final String AudioType = "object.item.audioItem.musicTrack";
    private static final String ImageType = "object.item.imageItem.photo";
    private static final String videoType = "object.item.videoItem.movie";
    private String typeName;

    private MediaType(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeName() {
        return this.typeName;
    }
}
