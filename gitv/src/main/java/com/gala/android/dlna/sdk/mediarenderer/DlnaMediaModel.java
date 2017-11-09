package com.gala.android.dlna.sdk.mediarenderer;

public class DlnaMediaModel {
    private String album = "";
    private String albumiconuri = "";
    private String artist = "";
    private String objectclass = "";
    private String title = "";
    private String uri = "";

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        if (title == null) {
            title = "";
        }
        this.title = title;
    }

    public String getArtist() {
        return this.artist;
    }

    public void setArtist(String artist) {
        if (artist == null) {
            artist = "";
        }
        this.artist = artist;
    }

    public void setAlbum(String album) {
        if (album == null) {
            album = "";
        }
        this.album = album;
    }

    public String getAlbum() {
        return this.album;
    }

    public void setObjectClass(String objectClass) {
        if (objectClass == null) {
            objectClass = "";
        }
        this.objectclass = objectClass;
    }

    public String getObjectClass() {
        return this.objectclass;
    }

    public void setUrl(String uri) {
        if (uri == null) {
            uri = "";
        }
        this.uri = uri;
    }

    public String getUrl() {
        return this.uri;
    }

    public String getAlbumUri() {
        return this.albumiconuri;
    }

    public void setAlbumUri(String albumiconuri) {
        if (albumiconuri == null) {
            albumiconuri = "";
        }
        this.albumiconuri = albumiconuri;
    }
}
