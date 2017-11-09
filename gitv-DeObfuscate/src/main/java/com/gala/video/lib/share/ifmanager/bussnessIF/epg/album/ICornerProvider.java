package com.gala.video.lib.share.ifmanager.bussnessIF.epg.album;

import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.share.ifmanager.IInterfaceWrapper;

public interface ICornerProvider extends IInterfaceWrapper {

    public static abstract class Wrapper implements ICornerProvider {
        public Object getInterface() {
            return this;
        }

        public static ICornerProvider asInterface(Object wrapper) {
            if (wrapper == null || !(wrapper instanceof ICornerProvider)) {
                return null;
            }
            return (ICornerProvider) wrapper;
        }
    }

    String getBigViewTitle(ChannelLabel channelLabel);

    String getChannelLabelTitle(ChannelLabel channelLabel);

    boolean getCornerInfo(Album album, int i);

    boolean getCornerInfo(ChannelLabel channelLabel, int i);

    String getDateShort(Album album);

    String getDescLB(Album album, QLayoutKind qLayoutKind);

    String getDescRB(Album album, QLayoutKind qLayoutKind);

    String getLength(Album album);

    Album getRealAlbum(ChannelLabel channelLabel);

    String getScoreRB(Album album);

    String getSubTitle(Album album);

    String getTitle(Album album, QLayoutKind qLayoutKind);

    String getTitle(ChannelLabel channelLabel, QLayoutKind qLayoutKind);

    boolean isSpecialChannel(int i);
}
