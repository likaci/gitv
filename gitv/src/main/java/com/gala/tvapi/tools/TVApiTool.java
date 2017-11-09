package com.gala.tvapi.tools;

import android.annotation.SuppressLint;
import android.util.Log;
import com.gala.tvapi.log.a;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.type.PayMarkType;
import com.gala.tvapi.type.PlatformType;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.cybergarage.xml.XML;

public class TVApiTool {
    private Album a;

    @SuppressLint({"DefaultLocale"})
    public int getVidId(String vid) {
        String toLowerCase = vid.toLowerCase();
        if (toLowerCase.equals("300")) {
            return 1;
        }
        if (toLowerCase.equals("600")) {
            return 2;
        }
        if (toLowerCase.equals("1000")) {
            return 3;
        }
        if (toLowerCase.equals("720p")) {
            return 4;
        }
        if (toLowerCase.equals("1080p")) {
            return 5;
        }
        if (toLowerCase.equals("jisu_drm")) {
            return 6;
        }
        if (toLowerCase.equals("300_drm")) {
            return 7;
        }
        if (toLowerCase.equals("600_drm")) {
            return 8;
        }
        if (toLowerCase.equals("720_drm")) {
            return 9;
        }
        if (toLowerCase.equals("4k")) {
            return 10;
        }
        if (toLowerCase.equals("5m")) {
            return 11;
        }
        if (toLowerCase.equals("8m")) {
            return 12;
        }
        if (toLowerCase.equals("1000_dolby")) {
            return 13;
        }
        if (toLowerCase.equals("720p_dolby")) {
            return 14;
        }
        if (toLowerCase.equals("1080p_dolby")) {
            return 15;
        }
        if (toLowerCase.equals("4k_dolby")) {
            return 16;
        }
        if (toLowerCase.equals("720p_h265")) {
            return 17;
        }
        if (toLowerCase.equals("1080p_h265")) {
            return 18;
        }
        if (toLowerCase.equals("4k_h265")) {
            return 19;
        }
        if (toLowerCase.equals("600_dolby")) {
            return 20;
        }
        return -1;
    }

    public Album getRecommendAlbum() {
        return this.a;
    }

    public void setRecommednAlbum(Album album) {
        this.a = album;
    }

    public static String parseLicenceUrl(String newUrl) {
        CharSequence domain = TVApiBase.getTVApiProperty().getDomain();
        if (TVApiBase.getTVApiProperty().getPlatform() == PlatformType.TAIWAN && newUrl.contains("data2.itv.igala.com")) {
            newUrl = newUrl.replace("data2.itv", "tv").replace("gala.com", domain);
        }
        if (domain.charAt(1) == domain.charAt(3) && domain.contains("i.com")) {
            return newUrl.replace("gala.com", domain);
        }
        return newUrl.replace("igala.com", domain).replace("gala.com", domain);
    }

    public String encodeParam(String param) {
        if (!(param == null || param.isEmpty())) {
            try {
                return URLEncoder.encode(param, XML.CHARSET_UTF8);
            } catch (UnsupportedEncodingException e) {
                a.b("URL Encode", e);
            }
        }
        return "";
    }

    public static PayMarkType getPayMarkType(int payMark) {
        switch (payMark) {
            case 0:
                return PayMarkType.NONE_MARK;
            case 1:
                return PayMarkType.VIP_MARK;
            case 2:
                return PayMarkType.PAY_ON_DEMAND_MARK;
            case 3:
                return PayMarkType.COUPONS_ON_DEMAND_MARK;
            default:
                return PayMarkType.NONE_MARK;
        }
    }

    public static int getPayMarkValue(PayMarkType type) {
        if (type == PayMarkType.VIP_MARK) {
            return 1;
        }
        if (type == PayMarkType.PAY_ON_DEMAND_MARK) {
            return 2;
        }
        if (type == PayMarkType.COUPONS_ON_DEMAND_MARK) {
            return 3;
        }
        return 0;
    }

    public static ContentType getContentType(String contentType) {
        ContentType contentType2 = ContentType.FEATURE_FILM;
        if ("".equals(contentType)) {
            return contentType2;
        }
        if ("SPECIAL".equals(contentType)) {
            return ContentType.SPECIAL;
        }
        if ("PREVUE".equals(contentType)) {
            return ContentType.PREVUE;
        }
        if ("TRAILER".equals(contentType)) {
            return ContentType.TRAILER;
        }
        if ("TITBIT".equals(contentType)) {
            return ContentType.TITBIT;
        }
        if ("PROPAGANDA".equals(contentType)) {
            return ContentType.PROPAGANDA;
        }
        if ("CLIP".equals(contentType)) {
            return ContentType.CLIP;
        }
        if ("OTHER".equals(contentType)) {
            return ContentType.OTHER;
        }
        return contentType2;
    }

    public static String getContentTypeValue(ContentType type) {
        String str = "FEATURE_FILM";
        if (type == null || "".equals(type)) {
            return str;
        }
        if (type == ContentType.SPECIAL) {
            return "SPECIAL";
        }
        if (type == ContentType.PREVUE) {
            return "PREVUE";
        }
        if (type == ContentType.TRAILER) {
            return "TRAILER";
        }
        if (type == ContentType.TITBIT) {
            return "TITBIT";
        }
        if (type == ContentType.PROPAGANDA) {
            return "PROPAGANDA";
        }
        if (type == ContentType.CLIP) {
            return "CLIP";
        }
        if (type == ContentType.OTHER) {
            return "OTHER";
        }
        return str;
    }

    public static ContentType getContentType(int contentType) {
        ContentType contentType2 = ContentType.FEATURE_FILM;
        if (ContentType.SPECIAL.getValue() == contentType) {
            return ContentType.SPECIAL;
        }
        if (ContentType.PREVUE.getValue() == contentType) {
            return ContentType.PREVUE;
        }
        if (ContentType.TRAILER.getValue() == contentType) {
            return ContentType.TRAILER;
        }
        if (ContentType.TITBIT.getValue() == contentType) {
            return ContentType.TITBIT;
        }
        if (ContentType.PROPAGANDA.getValue() == contentType) {
            return ContentType.PROPAGANDA;
        }
        if (ContentType.CLIP.getValue() == contentType) {
            return ContentType.CLIP;
        }
        if (ContentType.OTHER.getValue() == contentType) {
            return ContentType.OTHER;
        }
        return contentType2;
    }

    public static String getDrmType(String drmType) {
        String str = "1";
        if (drmType == null || drmType.isEmpty()) {
            return "1";
        }
        String str2 = "";
        String[] split = drmType.replace("[", "").replace(AlbumEnterFactory.SIGN_STR, "").split(",");
        if (split != null && split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                if (split[i].equals("3")) {
                    str2 = str2 + "2,";
                } else if (split[i].equals("5")) {
                    str2 = str2 + "3,";
                }
            }
            if (str2.length() > 1) {
                return str2.substring(0, str2.length() - 1);
            }
        }
        return str;
    }

    public static String getHDRType(String hdr) {
        if (hdr == null || hdr.isEmpty()) {
            return "";
        }
        String replace = hdr.replace("[", "").replace(AlbumEnterFactory.SIGN_STR, "");
        if (replace == null || replace.isEmpty()) {
            return "";
        }
        String str = "";
        String[] split = replace.split(",");
        if (split != null && split.length > 0) {
            for (int i = 0; i < split.length; i++) {
                if (split[i].equals("-1")) {
                    return "";
                }
                if (split[i].equals("1")) {
                    str = str + "dolby_vision,";
                } else if (split[i].equals("2")) {
                    str = str + "hdr_10,";
                }
            }
            if (str.length() > 1) {
                return str.substring(0, str.length() - 1);
            }
        }
        return "";
    }

    public static boolean checkStringSize(String str) {
        if (str.length() <= 10485760) {
            return true;
        }
        Log.e("<TVAPI>", "response's size out of 10MB, size = " + str.length());
        return false;
    }
}
