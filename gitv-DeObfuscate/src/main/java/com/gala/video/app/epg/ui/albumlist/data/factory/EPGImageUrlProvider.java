package com.gala.video.app.epg.ui.albumlist.data.factory;

import android.text.TextUtils;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ChannelPlayListLabel;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;

public class EPGImageUrlProvider {
    public static String getAlbumImageUrl(IData info, QLayoutKind layout) {
        Album album = info.getAlbum();
        if (DataInfoProvider.isCardData(album)) {
            if (album.getType() == AlbumType.PLAYLIST) {
                if (DataInfoProvider.isCardShowing(info)) {
                    return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, album.vimg);
                }
                return PicSizeUtils.getUrlWithSize(PhotoSize._320_180, album.himg);
            } else if (info.isShowingCard()) {
                layout = QLayoutKind.PORTRAIT;
            }
        }
        switch (layout) {
            case LANDSCAPE:
                if (album.getType() != AlbumType.PEOPLE) {
                    return PicSizeUtils.getUrlWithSize(PhotoSize._320_180, album.pic);
                }
                return PicSizeUtils.getUrlWithSize(PhotoSize._180_101, StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic);
            default:
                if (album.getType() != AlbumType.PEOPLE) {
                    AlbumKind albumType = GetInterfaceTools.getAlbumInfoHelper().getAlbumType(album);
                    if (albumType == AlbumKind.SERIES_ALBUM || albumType == AlbumKind.SOURCE_ALBUM || albumType == AlbumKind.SIGLE_VIDEO) {
                        return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, StringUtils.isEmpty(album.tvPic) ? album.pic : album.tvPic);
                    }
                    return PicSizeUtils.getUrlWithSize(PhotoSize._195_260, StringUtils.isEmpty(album.pic) ? album.tvPic : album.pic);
                } else if (StringUtils.isEmpty(album.tvPic)) {
                    return album.pic;
                } else {
                    return album.tvPic;
                }
        }
    }

    public static String getAlbumImageUrl(ChannelLabel mLabel, int type, QLayoutKind mLayout, boolean mIsShowingCard) {
        switch (type) {
            case 1:
                return mLabel.itemImageUrl;
            default:
                if (ResourceType.DIY.equals(mLabel.getType())) {
                    if (TextUtils.isEmpty(mLabel.itemImageUrl)) {
                        return mLabel.imageUrl;
                    }
                    return mLabel.itemImageUrl;
                } else if (ResourceType.LIVE.equals(mLabel.getType())) {
                    if (mIsShowingCard) {
                        return TextUtils.isEmpty(mLabel.itemImageUrl) ? PicSizeUtils.getUrlWithSize(PhotoSize._195_260, mLabel.imageUrl) : mLabel.itemImageUrl;
                    } else {
                        if (!TextUtils.isEmpty(mLabel.itemImageUrl)) {
                            return mLabel.itemImageUrl;
                        }
                        return PicSizeUtils.getUrlWithSize(mLayout == QLayoutKind.LANDSCAPE ? PhotoSize._320_180 : PhotoSize._195_260, mLabel.imageUrl);
                    }
                } else if (mLayout == QLayoutKind.LANDSCAPE) {
                    return PicSizeUtils.getUrlWithSize(PhotoSize._320_180, mLabel.imageUrl);
                } else {
                    if (ResourceType.COLLECTION.equals(mLabel.getType())) {
                        return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, mLabel.imageUrl);
                    }
                    if (!TextUtils.isEmpty(mLabel.postImage)) {
                        return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, mLabel.postImage);
                    }
                    if (TextUtils.isEmpty(mLabel.albumImage)) {
                        return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, mLabel.imageUrl);
                    }
                    return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, mLabel.albumImage);
                }
        }
    }

    public static String getAlbumImageUrl(ChannelPlayListLabel mPlayListLabel) {
        return PicSizeUtils.getUrlWithSize(PhotoSize._320_180, mPlayListLabel.picUrl);
    }
}
