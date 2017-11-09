package com.gala.video.lib.share.uikit.data.data.processor.Item;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.uikit.data.data.Model.cardlayout.Item;
import com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool;
import com.gala.video.lib.share.uikit.data.data.processor.UIKitConfig;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.lib.share.utils.Precondition;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.HashMap;

public class ImgBuildTool {
    private static final String SIZE_195_260 = "_195_260";
    private static final String SIZE_260_360 = "_260_360";
    public static final String SIZE_300_300 = "_300_300";
    private static final String SIZE_320_180 = "_320_180";
    private static final String SIZE_480_270 = "_480_270";
    private static final short[][] SPECIAL_SEZE = new short[][]{new short[]{(short) 252, (short) 436}, new short[]{(short) 402, (short) 314}};

    public static boolean buildImg(String img, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        if (Precondition.isEmpty(img)) {
            return false;
        }
        cuteViewDatas.put("ID_IMAGE", getImgMap(img));
        return true;
    }

    public static boolean buildBg(String img, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        if (Precondition.isEmpty(img)) {
            return false;
        }
        cuteViewDatas.put(UIKitConfig.ID_BG, getImgMap(img));
        return true;
    }

    public static boolean buildHistoryImg(Album album, Item item, HashMap<String, HashMap<String, String>> cuteViewDatas) {
        String pic;
        if (item.f2041w > item.f2040h) {
            pic = resizeImage(album.pic, SIZE_320_180);
        } else if (album.getType() != AlbumType.VIDEO || Precondition.isEmpty(album.sourceCode)) {
            if (Precondition.isEmpty(album.tvPic)) {
                pic = resizeImage(album.pic, "_260_360");
            } else {
                pic = resizeImage(album.tvPic, "_260_360");
            }
        } else if (Precondition.isEmpty(album.pic)) {
            pic = resizeImage(album.tvPic, SIZE_195_260);
        } else {
            pic = resizeImage(album.pic, SIZE_195_260);
        }
        cuteViewDatas.put("ID_IMAGE", getImgMap(pic));
        return true;
    }

    public static boolean buildImg(ChannelLabel label, Item item, ItemDataType itemDataType, HashMap<String, HashMap<String, String>> cuteViewDatas, boolean hasBack) {
        String pic;
        if (itemDataType == ItemDataType.TV_TAG_ALL || itemDataType == ItemDataType.TV_TAG || itemDataType == ItemDataType.ENTER_ALL) {
            pic = label.itemKvs.tvPic;
            if (Precondition.isEmpty(pic)) {
                return false;
            }
            cuteViewDatas.put("ID_IMAGE", getImgMap(pic));
            return true;
        }
        pic = null;
        if (!hasBack) {
            pic = buildGif(label, item);
            if (!Precondition.isEmpty(pic)) {
                cuteViewDatas.put("ID_IMAGE", getGifMap(pic));
                return true;
            } else if (isUseOperationimg(item) && label.itemKvs != null) {
                if (!(Precondition.isEmpty(label.itemKvs.extraImage) || Precondition.isEmpty(label.itemKvs.extraImage_size))) {
                    pic = label.itemKvs.extraImage;
                }
                if (!(!isImgInvalid(pic) || Precondition.isEmpty(label.itemKvs.defImg_size) || Precondition.isEmpty(label.itemImageUrl))) {
                    pic = label.itemImageUrl;
                }
            }
        }
        if (isImgInvalid(pic)) {
            if (!item.style.startsWith("circle")) {
                if (label.itemKvs != null) {
                    pic = label.itemKvs.defaultpic;
                }
                if (isImgInvalid(pic)) {
                    pic = getSizeImg(label, itemDataType, item);
                }
            } else if (itemDataType != ItemDataType.PERSON) {
                return false;
            } else {
                pic = resizeImage(label.imageUrl, "_300_300");
            }
        }
        if (isImgInvalid(pic)) {
            return false;
        }
        cuteViewDatas.put("ID_IMAGE", getImgMap(pic));
        return true;
    }

    private static boolean isUseOperationimg(Item item) {
        for (short[] size : SPECIAL_SEZE) {
            if (item.f2041w == ResourceUtil.getPxShort(size[0]) && item.f2040h == ResourceUtil.getPxShort(size[1])) {
                return false;
            }
        }
        return true;
    }

    private static HashMap<String, String> getGifMap(String gif) {
        HashMap<String, String> map = new HashMap();
        map.put(UIKitConfig.KEY_GIF, gif);
        return map;
    }

    private static HashMap<String, String> getImgMap(String img) {
        HashMap<String, String> map = new HashMap();
        map.put("value", img);
        return map;
    }

    private static String getSizeImg(ChannelLabel label, ItemDataType itemDataType, Item item) {
        String size;
        if (item.f2041w >= item.f2040h) {
            size = "_480_270";
            switch (itemDataType) {
                case LIVE_CHANNEL:
                    return resizeImage(label.logo, size);
                case ALBUM:
                case VIDEO:
                case LIVE:
                case PERSON:
                case PLAY_LIST:
                case TRAILERS:
                    return resizeImage(label.imageUrl, size);
                default:
                    return null;
            }
        }
        size = "_260_360";
        switch (itemDataType) {
            case LIVE_CHANNEL:
                return resizeImage(label.logo, size);
            case ALBUM:
            case VIDEO:
            case LIVE:
            case PERSON:
            case PLAY_LIST:
            case TRAILERS:
                return resizeImage(Precondition.isEmpty(label.postImage) ? label.imageUrl : label.postImage, size);
            default:
                return null;
        }
    }

    public static String resizeImage(String url, String size) {
        if (!Precondition.isEmpty(url)) {
            int i = url.lastIndexOf(".");
            if (i >= 0) {
                StringBuilder builder = new StringBuilder(url);
                builder.insert(i, size);
                return builder.toString();
            }
        }
        return "";
    }

    private static String buildGif(ChannelLabel label, Item item) {
        if (item.style.startsWith("circle") || !GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getIsSupportGif() || MemoryLevelInfo.isLowConfigDevice() || item.f2041w > (short) 552 || label == null || label.itemKvs == null) {
            return null;
        }
        ItemKvs kvs = label.getItemKvs();
        if (Precondition.isEmpty(kvs.imageGif) || Precondition.isEmpty(kvs.imageGif_size)) {
            return null;
        }
        int[] size = DataBuildTool.getImageSize(kvs.imageGif_size);
        if (Precondition.isEmpty(size) || size.length != 2) {
            return null;
        }
        return kvs.imageGif;
    }

    private static boolean isImgInvalid(String pic) {
        if (Precondition.isEmpty(pic)) {
            return true;
        }
        return false;
    }
}
