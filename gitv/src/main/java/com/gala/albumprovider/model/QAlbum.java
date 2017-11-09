package com.gala.albumprovider.model;

import com.gala.tvapi.tv2.model.Album;
import java.util.ArrayList;
import java.util.List;

public class QAlbum extends Album {
    public static List<Album> getAlbumList(List<QAlbum> albums) {
        List<Album> arrayList = new ArrayList();
        for (QAlbum add : albums) {
            arrayList.add(add);
        }
        return arrayList;
    }

    public static List<Album> loadAggrAlbum(String channelId, List<Tag> albumTags) {
        List<Album> arrayList = new ArrayList();
        for (Tag loadAggrAlbum : albumTags) {
            arrayList.add(loadAggrAlbum(channelId, loadAggrAlbum));
        }
        return arrayList;
    }

    public static QAlbum loadAggrAlbum(String channelId, Tag albumTag) {
        return new QAlbum();
    }

    public static List<Album> filter(List<Album> list) {
        for (Album filter : list) {
            filter(filter);
        }
        return list;
    }

    public static Album filter(Album Album) {
        if (!Album.sourceCode.equals("")) {
            a(Album);
        }
        return Album;
    }

    private static void a(Album album) {
    }
}
