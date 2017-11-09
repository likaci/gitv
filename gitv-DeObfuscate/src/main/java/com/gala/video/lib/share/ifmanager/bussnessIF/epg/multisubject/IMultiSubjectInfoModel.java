package com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import java.util.List;

public interface IMultiSubjectInfoModel extends IInterfaceWrapper {

    public static abstract class Wrapper implements IMultiSubjectInfoModel {
        public Object getInterface() {
            return this;
        }

        public static IMultiSubjectInfoModel asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IMultiSubjectInfoModel)) {
                return null;
            }
            return (IMultiSubjectInfoModel) wrapper;
        }
    }

    List<Album> getAlbumList();

    String getBuyFrom();

    String getBuysource();

    CardModel getCardModel();

    String getE();

    int getEnterType();

    String getFrom();

    String getItemId();

    int getPlayIndex();

    String getPlayType();

    String getSourceType();

    void setAlbumList(List<Album> list);

    void setBuyFrom(String str);

    void setBuysource(String str);

    void setCardModel(CardModel cardModel);

    void setEnterType(int i);

    void setFrom(String str);

    void setItemId(String str);

    void setPlayIndex(int i);

    void setPlayType(String str);

    void setSourceType(String str);
}
