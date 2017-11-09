package com.gala.tvapi.tv2.model;

public class SearchPy extends Model {
    private static final long serialVersionUID = 1;
    public AlbumDocInfo albumDocInfo;
    public String doc_id = "";
    public int pos = 0;

    public Album getAlbum() {
        int i;
        int i2 = 1;
        Album album = new Album();
        album.doc_id = this.doc_id;
        album.pos = this.pos;
        album.qpId = "".equals(this.albumDocInfo.qpid) ? String.valueOf(this.albumDocInfo.albumId) : this.albumDocInfo.qpid;
        try {
            album.tvQid = String.valueOf(((VideoInfo) this.albumDocInfo.videoinfos.get(0)).qipu_id);
        } catch (Exception e) {
        }
        album.name = this.albumDocInfo.albumTitle;
        album.desc = this.albumDocInfo.description;
        if (this.albumDocInfo.videoDocType == 1 && this.albumDocInfo.series) {
            i = 1;
        } else {
            i = 0;
        }
        album.type = i;
        try {
            String[] split = this.albumDocInfo.channel.split(",");
            if (split.length == 2) {
                album.chnName = split[0];
                album.chnId = Integer.valueOf(split[1]).intValue();
            }
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        album.sourceCode = this.albumDocInfo.sourceCode;
        Cast cast = new Cast();
        cast.director = this.albumDocInfo.director;
        cast.actor = this.albumDocInfo.star;
        album.cast = cast;
        album.time = this.albumDocInfo.releaseDate;
        album.tvsets = this.albumDocInfo.itemTotalNumber;
        album.pic = this.albumDocInfo.albumImg;
        album.isPurchase = this.albumDocInfo.isPurchase;
        album.score = String.valueOf(this.albumDocInfo.score);
        album.isSeries = this.albumDocInfo.series ? 1 : 0;
        if (this.albumDocInfo.isDownload) {
            i = 1;
        } else {
            i = 0;
        }
        album.isDownload = i;
        if (!this.albumDocInfo.is3D) {
            i2 = 0;
        }
        album.is3D = i2;
        album.focus = this.albumDocInfo.tvFocus;
        return album;
    }
}
