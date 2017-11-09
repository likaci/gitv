package com.gala.video.lib.share.ifmanager.bussnessIF.epg.data;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.tv2.model.Star;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;

public interface IModelHelper extends IInterfaceWrapper {

    public static abstract class Wrapper implements IModelHelper {
        public Object getInterface() {
            return this;
        }

        public static IModelHelper asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof IModelHelper)) {
                return null;
            }
            return (IModelHelper) wrapper;
        }
    }

    ItemModel convertAlbumToItemModel(Album album);

    ItemModel convertStarToItemModel(Star star);

    ItemModel convertTabModelToItemModel(TabModel tabModel);

    DataSource convertToDataSource(HomeModel homeModel);

    TabDataItem convertToTabDataItem(DailyLabelModel dailyLabelModel);
}
