package com.gala.video.lib.share.uikit.data.data.processor.Item;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumFrom;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import java.util.HashMap;

public class TitleBuildTool {
    public static void buildTitle(ChannelLabel label, HashMap<String, HashMap<String, String>> cuteViewDatas, ItemDataType itemDataType) {
        String title = null;
        if (label.albumFrom == null) {
            label.albumFrom = AlbumFrom.DEFAULT;
        }
        switch (label.albumFrom) {
            case DEFAULT:
                title = getTitle(label, itemDataType);
                break;
            case RECOMMAND:
                if (label.getType() == ResourceType.VIDEO) {
                    if (label.isSeries == 1) {
                        if (!label.sourceId.equals("") && !label.sourceId.equals("0")) {
                            title = label.itemShortDisplayName;
                            break;
                        } else {
                            title = label.itemName;
                            break;
                        }
                    }
                    title = label.itemShortDisplayName;
                    break;
                }
                title = label.itemName;
                break;
                break;
            case TRAILERS:
                title = label.itemShortDisplayName;
                break;
            case PLAYLIST:
                title = StringUtils.isEmpty(label.itemShortDisplayName) ? label.shortTitle : label.itemShortDisplayName;
                break;
            case RECOMMAND_VIDEO:
                title = label.itemShortDisplayName;
                break;
        }
        setCuteViewData(title, cuteViewDatas);
    }

    public static void buildTitle(String title, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        setCuteViewData(title, cuteViewDatas);
    }

    public static void buildHistoryTitle(Album album, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        String title = null;
        if (album.getType() == AlbumType.PLAYLIST) {
            title = album.name;
        } else if (album.getType() == AlbumType.ALBUM) {
            if (album.getAlbumSubName() == null || album.getAlbumSubName().equals("")) {
                title = album.getAlbumSubTvName();
            } else {
                title = album.getAlbumSubName();
            }
        } else if (album.getType() == AlbumType.VIDEO) {
            if (album.isSeries()) {
                if (album.sourceCode == null || album.sourceCode.equals("") || album.sourceCode.equals("0")) {
                    title = StringUtils.isEmpty(album.name) ? album.tvName : album.name;
                } else {
                    title = album.tvName;
                }
            } else if (album.chnId == 1 || album.chnId == 2 || album.chnId == 4 || album.chnId == 15) {
                if (album.getAlbumSubTvName() == null || album.getAlbumSubTvName().equals("")) {
                    title = album.getAlbumSubName();
                } else {
                    title = album.getAlbumSubTvName();
                }
            } else if (album.getAlbumSubName() == null || album.getAlbumSubName().equals("")) {
                title = album.shortName;
            } else {
                title = album.getAlbumSubName();
            }
        }
        if (title != null) {
            buildTitle(title, cuteViewDatas);
        }
    }

    private static void setCuteViewData(String title, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        HashMap<String, String> titleMap = new HashMap();
        titleMap.put("text", title);
        cuteViewDatas.put("ID_TITLE", titleMap);
    }

    public static String getTitle(ChannelLabel label, ItemDataType itemDataType) {
        if (itemDataType == ItemDataType.TV_TAG || itemDataType == ItemDataType.TV_TAG_ALL) {
            if (label.getItemKvs() != null) {
                return label.getItemKvs().tvShowName;
            }
            return null;
        } else if (!StringUtils.isEmpty(label.itemShortDisplayName)) {
            return label.itemShortDisplayName;
        } else {
            if (!StringUtils.isEmpty(label.itemName)) {
                return label.itemName;
            }
            if (StringUtils.isEmpty(label.shortTitle)) {
                return label.name;
            }
            return label.shortTitle;
        }
    }
}
