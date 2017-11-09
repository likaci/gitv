package com.gala.sdk.player;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.framework.core.utils.ListUtils;
import java.io.Serializable;
import java.util.List;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class PlayParams implements Serializable {
    public List<Album> continuePlayList;
    public String from;
    public String h5PlayType;
    public boolean isDetailEpisode;
    public boolean isDetailRelated;
    public boolean isDetailTrailer;
    public boolean isHomeAd;
    public boolean isPicVertical;
    public int playIndex;
    public String playListId;
    public String playListName;
    public SourceType sourceType;

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PlayParams@" + Integer.toHexString(hashCode())).append("{isDetailRelated-").append(this.isDetailRelated).append("isDetailTrailer-").append(this.isDetailTrailer).append(",isDetailEpisode-").append(this.isDetailEpisode).append(",isHomeAd-").append(this.isHomeAd).append(",sourceType=").append(this.sourceType).append(", playindex=").append(this.playIndex).append(", playlist id=").append(this.playListId).append(", playlist name=").append(this.playListName).append(", is picture vertical=").append(this.isPicVertical).append(", from=").append(this.from).append(", mH5PlayType=").append(this.h5PlayType).append(", playlist=[");
        if (ListUtils.isEmpty(this.continuePlayList)) {
            stringBuilder.append("EMPTY");
        } else {
            int size = this.continuePlayList.size();
            stringBuilder.append("size:").append(size).append(", content=");
            int min = Math.min(5, size);
            for (int i = 0; i < min; i++) {
                Album album = (Album) this.continuePlayList.get(i);
                stringBuilder.append(SearchCriteria.LT).append(album.qpId).append(", ").append(album.tvQid).append(", ").append(album.name).append(">; ");
            }
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
