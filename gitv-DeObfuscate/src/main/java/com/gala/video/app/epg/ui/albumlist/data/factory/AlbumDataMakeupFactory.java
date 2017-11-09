package com.gala.video.app.epg.ui.albumlist.data.factory;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.video.app.epg.ui.albumlist.data.type.AlbumData;
import com.gala.video.app.epg.ui.albumlist.data.type.ChannelLabelData;
import com.gala.video.app.epg.ui.albumlist.data.type.ChannelPlayListData;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.lib.share.common.base.DataMakeupFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class AlbumDataMakeupFactory extends DataMakeupFactory {
    public static AlbumDataMakeupFactory get() {
        return new AlbumDataMakeupFactory();
    }

    protected IData dataMakeup(Object item, QLayoutKind layout, int locationPage, Object model) {
        if (item instanceof ChannelLabel) {
            return dataMakeup((ChannelLabel) item, layout, locationPage, (AlbumInfoModel) model);
        }
        if (item instanceof ChannelPlayListLabel) {
            return dataMakeup((ChannelPlayListLabel) item, locationPage, (AlbumInfoModel) model);
        }
        if (item instanceof Album) {
            return dataMakeup((Album) item, layout, locationPage);
        }
        if (item instanceof IData) {
            return (IData) item;
        }
        return null;
    }

    private static IData<Album> dataMakeup(Album album, QLayoutKind layout, int locationPage) {
        return new AlbumData(album, layout, locationPage);
    }

    private static IData<ChannelLabel> dataMakeup(ChannelLabel label, QLayoutKind layout, int locationPage, AlbumInfoModel model) {
        return new ChannelLabelData(label, layout, locationPage, model);
    }

    private static IData<ChannelPlayListLabel> dataMakeup(ChannelPlayListLabel label, int locationPage, AlbumInfoModel model) {
        return new ChannelPlayListData(label, locationPage, model);
    }
}
