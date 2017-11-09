package com.gala.sdk.player.data;

import java.util.List;

public interface IStarData {
    List<IVideo> getStarArticles();

    String getStarCover();

    String getStarID();

    String getStarName();

    void setArticles(List<IVideo> list);
}
