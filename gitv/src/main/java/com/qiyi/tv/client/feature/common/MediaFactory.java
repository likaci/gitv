package com.qiyi.tv.client.feature.common;

import com.qiyi.tv.client.data.Album;
import com.qiyi.tv.client.data.Media;
import com.qiyi.tv.client.data.Playlist;
import com.qiyi.tv.client.data.Video;

public class MediaFactory {
    public static final String TYPE_ALBUM = "ALBUM";
    public static final String TYPE_PLAYLIST = "PLAYLIST";
    public static final String TYPE_VIDEO = "VIDEO";

    public static Media createMedia(String type, String albumId, String videoId, String sourceCode, int series) {
        Media media;
        boolean z = true;
        if (series != 1) {
            z = false;
        }
        Media video;
        if (TYPE_VIDEO.equals(type)) {
            video = new Video();
            video.setId(videoId);
            video.setAlbumId(albumId);
            video.setIsSeries(z);
            media = video;
        } else if (TYPE_ALBUM.equals(type)) {
            video = new Album();
            video.setId(albumId);
            video.setVideoId(videoId);
            video.setIsSeries(z);
            media = video;
        } else {
            media = null;
        }
        if (media != null) {
            media.setSourceCode(sourceCode);
            media.setFromSdk(false);
        }
        return media;
    }

    public static Media createMedia(String type, String albumId, String videoId, String sourceCode, int series, int channelId) {
        Media media;
        boolean z = true;
        if (series != 1) {
            z = false;
        }
        Media video;
        if (TYPE_VIDEO.equals(type)) {
            video = new Video();
            video.setChannelId(channelId);
            video.setId(videoId);
            video.setAlbumId(albumId);
            video.setIsSeries(z);
            media = video;
        } else if (TYPE_ALBUM.equals(type)) {
            video = new Album();
            video.setId(albumId);
            video.setChannelId(channelId);
            video.setVideoId(videoId);
            video.setIsSeries(z);
            media = video;
        } else {
            media = null;
        }
        if (media != null) {
            media.setSourceCode(sourceCode);
            media.setFromSdk(false);
        }
        return media;
    }

    public static Media createMedia(String type, String mediaId) {
        Media media = null;
        if (TYPE_VIDEO.equals(type)) {
            media = new Video();
        } else if (TYPE_ALBUM.equals(type)) {
            media = new Album();
        } else if (TYPE_PLAYLIST.equals(type)) {
            media = new Playlist();
        }
        if (media != null) {
            media.setId(mediaId);
            media.setFromSdk(false);
        }
        return media;
    }
}
