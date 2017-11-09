package com.gala.video.app.epg.ui.albumlist.utils;

import android.content.Context;
import android.view.View;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.sdk.plugin.PluginType;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.AlbumType;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.IChannelItem;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.constant.IFootConstant;
import com.gala.video.app.epg.ui.albumlist.data.BaseDataApi;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel.SearchInfoModel;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.ifimpl.logrecord.utils.LogRecordUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.pingback.PingBackParams.Values;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class QAPingback {
    private static final String LOG_TAG = "QAPingback";

    public static void albumClickPingback(String r, int c1, String plid, AlbumInfoModel infoModel) {
        String str = r;
        sendMyPageClick(str, getBlock(infoModel), "i", getRseat(infoModel), getRpageOrQcturl(infoModel), null, String.valueOf(c1), plid, getTvsrchsource(infoModel), getKeyword(infoModel), "");
    }

    public static void searchResultClickPingback(String r, int c1, String plid, AlbumInfoModel infoModel) {
        String s3;
        String block = getBlock(infoModel);
        String rt = "i";
        String rseat = getRseat(infoModel);
        String rpage = getRpageOrQcturl(infoModel);
        String tvsrchsource = getTvsrchsource(infoModel);
        String keyword = getKeyword(infoModel);
        if (infoModel.getChannelId() <= 0) {
            s3 = "";
        } else {
            s3 = String.valueOf(infoModel.getChannelId());
        }
        sendMyPageClick(r, block, rt, rseat, rpage, null, String.valueOf(c1), plid, tvsrchsource, keyword, s3);
    }

    private static String getBlock(AlbumInfoModel infoModel) {
        if (infoModel == null) {
            return "";
        }
        CharSequence block = infoModel.isMultiHasData() ? IAlbumConfig.STR_FILTER : infoModel.getDataTagName();
        return StringUtils.isEmpty(block) ? IAlbumConfig.STR_ALL : block;
    }

    private static String getRseat(AlbumInfoModel infoModel) {
        if (infoModel == null) {
            return "";
        }
        return (infoModel.getSelectRow() + 1) + "_" + (infoModel.getSelectColumn() + 1);
    }

    private static String getRpageOrQcturl(AlbumInfoModel infoModel) {
        if (infoModel == null) {
            return "";
        }
        String result = "";
        String mPageType = infoModel.getPageType();
        if (IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY.equals(mPageType) || IAlbumConfig.UNIQUE_FOOT_FAVOURITE.equals(mPageType) || IAlbumConfig.UNIQUE_FOOT_SUBSCRIBLE.equals(mPageType)) {
            return IFootConstant.STR_FILM_FOOT;
        }
        if (AlbumInfoFactory.isSearchResultPage(infoModel.getPageType())) {
            return IAlbumConfig.STR_SEARCH_RESULT;
        }
        return infoModel.getChannelName();
    }

    public static void labelTagClickPingback(String tagName, AlbumInfoModel infoModel) {
        if (infoModel != null) {
            String block;
            String all = IAlbumConfig.STR_ALL;
            if (StringUtils.isEmpty((CharSequence) tagName)) {
                block = all;
            } else {
                block = tagName;
            }
            String rpage = getRpageOrQcturl(infoModel);
            String rseat = "0";
            String s3 = AlbumInfoFactory.isSearchResultPage(infoModel.getPageType()) ? infoModel.getChannelId() <= 0 ? "" : String.valueOf(infoModel.getChannelId()) : "";
            sendMyPageClick("", block, "i", rseat, rpage, null, "", "", getTvsrchsource(infoModel), getKeyword(infoModel), s3);
        }
    }

    public static String getTvsrchsource(AlbumInfoModel infoModel) {
        if (infoModel == null) {
            return "";
        }
        if (!AlbumInfoFactory.isSearchResultPage(infoModel.getPageType())) {
            return "";
        }
        String channelName = infoModel.getChannelName();
        int channelId = infoModel.getChannelId();
        String tvsrchsource = "";
        if (StringUtils.equals(IFootConstant.STR_FILM_FOOT, channelName)) {
            return channelName;
        }
        if (channelId > 0) {
            return channelName;
        }
        return "tab";
    }

    public static String getKeyword(AlbumInfoModel infoModel) {
        if (infoModel == null) {
            return "";
        }
        if (!AlbumInfoFactory.isSearchResultPage(infoModel.getPageType())) {
            return "";
        }
        SearchInfoModel searchInfoModel = infoModel.getSearchModel();
        String keyword = "";
        if (searchInfoModel != null) {
            return searchInfoModel.getKeyWord();
        }
        return keyword;
    }

    public static void multiMenuAfterLoad(Context context, AlbumInfoModel infoModel, BaseDataApi dataApi, View view, long startLoadingTime) {
        if (infoModel != null && dataApi != null && view != null && infoModel.isMultiHasData() && ListUtils.getCount(dataApi.getOriginalDataList()) > 0) {
            Album album = (Album) dataApi.getOriginalDataList().get(0);
            if (dataApi.getCurPage() <= 1) {
                PingBackParams params = new PingBackParams();
                LogUtils.d(LOG_TAG, ">>>>>ptype = 0-0");
                params.add(Keys.T, "5").add("a", "0").add("s1", "1").add("s2", "3").add("rt", Values.value18).add("e", album.eventId).add("c1", String.valueOf(infoModel.getChannelId())).add(Keys.PTYPE, "0-0").add(Keys.POS, "").add("bkt", "").add(Keys.QISOST, "").add(Keys.TARGET, "").add("site", "gala");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            }
            requestMultiMenuDataPingback(context, infoModel, album, startLoadingTime);
        }
    }

    private static void requestMultiMenuDataPingback(Context context, AlbumInfoModel infoModel, Album album, long start) {
        if (album != null) {
            long timeToken = System.currentTimeMillis() - start;
            PingBackParams params1 = new PingBackParams();
            params1.add(Keys.T, "9").add("s1", "1").add("s2", "3").add("rt", Values.value18).add("e", album.eventId).add("c1", String.valueOf(infoModel.getChannelId())).add("bkt", album.bkt).add(Keys.DOCS, album.docs).add(Keys.SEARCH_TIME, album.searchtime).add("url", album.url).add("time", String.valueOf(timeToken)).add(Keys.USER_AGENT, getUserAgent()).add(Keys.DOCIDS, "").add(Keys.REF, "");
            PingBack.getInstance().postPingBackToLongYuan(params1.build());
        }
    }

    private static String getUserAgent() {
        String userAgent = "";
        try {
            userAgent = System.getProperty("http.agent");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userAgent;
    }

    public static void channelItemClick(Album album, AlbumInfoModel infoModel, int mIndexOfCurPage) {
        if (album != null) {
            int c1 = album.isLive == 1 ? 101221 : album.chnId;
            String r = "";
            String plid = "";
            if (AlbumType.PLAYLIST.equals(album.getType())) {
                r = album.qpId;
                plid = album.qpId;
            } else {
                r = GetInterfaceTools.getAlbumInfoHelper().isSingleType(album) ? album.tvQid : album.qpId;
            }
            albumClickPingback(r, c1, plid, infoModel);
            if (infoModel.isMultiHasData()) {
                PingBackParams params = new PingBackParams();
                LogUtils.d(LOG_TAG, ">>>>>ptype = 1-1");
                params.add(Keys.T, "5").add("s1", "1").add("s2", "3").add("rt", Values.value18).add("e", album.eventId).add("c1", String.valueOf(c1)).add("bkt", album.bkt).add(Keys.TARGET, album.doc_id).add(Keys.QISOST, album.qisost + plid).add(Keys.PTYPE, "1-1").add("site", "gala").add(Keys.POS, String.valueOf(mIndexOfCurPage)).add("a", "0");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
            }
        }
    }

    public static void clickChannelLabelItem(AlbumInfoModel infoModel, ChannelLabel label) {
        IChannelItem item = label.getResourceItem();
        Album album = GetInterfaceTools.getCornerProvider().getRealAlbum(label);
        if (album != null && infoModel != null) {
            int c1;
            String r = "";
            String plid = "";
            if (ResourceType.LIVE.equals(label.getType())) {
                r = album.qpId;
                c1 = 101221;
            } else if (ResourceType.COLLECTION.equals(label.getType())) {
                r = item.plId;
                c1 = label.categoryId;
                plid = label.id;
            } else if (ResourceType.RESOURCE_GROUP.equals(label.getType())) {
                r = label.itemId;
                c1 = label.channelId;
                plid = label.itemId;
            } else {
                r = album.qpId;
                c1 = label.channelId;
                plid = "";
            }
            sendMyPageClick(r, ResourceUtil.getStr(R.string.label_recommend), "i", infoModel.getRseat(), getRpageOrQcturl(infoModel), null, String.valueOf(c1), plid, getTvsrchsource(infoModel), getKeyword(infoModel), "");
            if (ResourceType.DIY.equals(label.getType())) {
                sendMyPageClick("H5page", ResourceUtil.getStr(R.string.label_recommend), "i", infoModel.getRseat(), infoModel.getChannelName(), null, "", plid, getTvsrchsource(infoModel), getKeyword(infoModel), "");
            }
        }
    }

    public static void searchRequestPingback(Context context, List<?> list, long time, int pageNum, int itemCount, int clickType, String keyword) {
        String source = "";
        if (clickType == 0) {
            source = IAlbumConfig.BUY_SOURCE_HOT;
        } else if (clickType == 1) {
            source = "suggest";
        } else if (clickType == 2) {
            source = "history";
        }
        PingBackParams params1;
        if (ListUtils.isEmpty((List) list)) {
            params1 = new PingBackParams();
            params1.add(Keys.T, "9").add("s1", "1").add("s2", "3").add("rt", "3").add("source", source).add("r", keyword).add("keyword", keyword).add("first_docs", "0").add("second_docs", "0").add("time", String.valueOf(time)).add(Keys.PAGE, String.valueOf(pageNum));
            PingBack.getInstance().postPingBackToLongYuan(params1.build());
        } else if (list.get(0) instanceof Album) {
            List<Album> list1 = list;
            Album album = (Album) list1.get(0);
            String e = album.eventId;
            String c1 = "";
            String url = "";
            StringBuilder docIds = new StringBuilder();
            for (Album a : list1) {
                docIds.append(a.doc_id).append(",").append(a.site).append(",").append(a.chnId == 0 ? a.c1 : a.chnId).append(";");
            }
            params1 = new PingBackParams();
            params1.add(Keys.T, "9").add("s1", "1").add("s2", "3").add("rt", "3").add("source", source).add(Keys.DOCIDS, docIds.toString()).add("c1", c1).add("e", e).add("bkt", album.bkt).add(Keys.SEARCH_TIME, String.valueOf(album.searchtime)).add("url", url).add("r", keyword).add("keyword", keyword).add("first_docs", "0").add("second_docs", "0").add("time", String.valueOf(time)).add(Keys.PAGE, String.valueOf(pageNum));
            PingBack.getInstance().postPingBackToLongYuan(params1.build());
        }
    }

    public static void searchItemClickPingback(Context context, Album album, int position, int clickType, String keyword, int curPageNum, boolean isShowingCard) {
        if (album != null) {
            String m_n = ((position / 60) + 1) + "_" + (position % 60);
            PingBackParams params1;
            if (clickType == 0) {
                params1 = new PingBackParams();
                params1.add(Keys.T, "5").add("s1", "1").add("s2", "3").add("rt", "3").add("a", "0").add(Keys.PTYPE, "1-1").add("c1", String.valueOf(album.c1)).add("e", album.eventId).add("bkt", album.bkt).add("site", album.site).add(Keys.TARGET, album.doc_id).add("r", keyword).add("keyword", keyword).add(Keys.POS, m_n).add(Keys.IS_CARD, isShowingCard ? "1" : "0").add(Keys.PAGE, String.valueOf(curPageNum));
                PingBack.getInstance().postPingBackToLongYuan(params1.build());
            } else if (clickType == 1) {
                params1 = new PingBackParams();
                params1.add(Keys.T, "5").add("s1", "1").add("s2", "3").add("rt", "3").add("a", "0").add(Keys.PTYPE, "1-2").add("c1", String.valueOf(album.c1)).add("e", album.eventId).add("bkt", album.bkt).add("site", album.site).add(Keys.TARGET, album.doc_id).add("r", keyword).add("keyword", keyword).add(Keys.POS, m_n).add(Keys.IS_CARD, isShowingCard ? "1" : "0").add(Keys.PAGE, String.valueOf(curPageNum));
                PingBack.getInstance().postPingBackToLongYuan(params1.build());
            } else if (clickType == 2) {
                params1 = new PingBackParams();
                params1.add(Keys.T, "5").add("s1", "1").add("s2", "3").add("rt", "3").add("a", "0").add(Keys.PTYPE, "").add("c1", String.valueOf(album.c1)).add("e", album.eventId).add("bkt", album.bkt).add("site", album.site).add(Keys.TARGET, album.doc_id).add("r", keyword).add("keyword", keyword).add(Keys.POS, m_n).add(Keys.IS_CARD, isShowingCard ? "1" : "0").add(Keys.PAGE, String.valueOf(curPageNum));
                PingBack.getInstance().postPingBackToLongYuan(params1.build());
            }
        }
    }

    public static void recordDeleteClearPingback(int type) {
        sendMyPageClick("", "dltlayer", "i", getRseatForMenu(type), IFootConstant.STR_FILM_FOOT, null, "", "", "", "", "");
    }

    public static void recordClearDialogPingback(int type) {
        sendMyPageClick("", "dltalldlg", "i", getRseatForClearDialog(type), IFootConstant.STR_FILM_FOOT, null, "", "", "", "", "");
    }

    public static void recordDeleteLayerPingback(int type) {
        sendMyPageClick("", "dltonelayer", "i", getRseatForLayer(type), IFootConstant.STR_FILM_FOOT, null, "", "", "", "", "");
    }

    private static String getRseatForMenu(int type) {
        switch (type) {
            case 0:
                return "dltone";
            case 1:
                return "dltall";
            case 3:
                return "back";
            case 4:
                return "menu";
            default:
                return "";
        }
    }

    private static String getRseatForClearDialog(int type) {
        switch (type) {
            case 0:
                return PluginType.EMPTY_TYPE;
            case 1:
                return LoginConstant.CLICK_RESEAT_MERGE_CANCEL;
            case 3:
                return "back";
            default:
                return "";
        }
    }

    private static String getRseatForLayer(int type) {
        switch (type) {
            case 0:
                return "dlt";
            case 3:
                return "back";
            case 4:
                return "menu";
            default:
                return "";
        }
    }

    private static String getBlockForLayer(int type) {
        switch (type) {
            case 0:
                return "dltlayer";
            case 1:
                return "dltalldlg";
            case 2:
                return "dltonelayer";
            default:
                return "";
        }
    }

    public static void recordLayerShowPingback(int type, int card, AlbumInfoModel infoModel) {
        String qtcurl = IFootConstant.STR_FILM_FOOT;
        String block = getBlockForLayer(type);
        String s2 = null;
        if (infoModel != null) {
            s2 = infoModel.getFrom();
        }
        sendMyPageShow("", qtcurl, "", "", null, null, block, "", "", s2, null, "", card + "", "", "");
    }

    public static void searchShowPingback() {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "21").add("bstp", "1").add("qtcurl", "search").add("block", "search").add("s2", null);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public static void searchBannerAdShowPingback() {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "21").add("qtcurl", "search").add("block", "通栏广告").add("c1", "").add("qpid", "通栏广告").add("bstp", "1");
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public static void error(String lOG_TAG, final String channelId, final String dataTagName, final ApiException e) {
        ThreadUtils.execute(new Runnable() {
            public void run() {
                String s;
                if (e != null) {
                    s = "code=" + e.getCode() + ",url=" + e.getUrl() + ",message=" + e.getDetailMessage();
                } else {
                    s = "e=null";
                }
                String eventId = PingBackUtils.createEventId();
                String pfec = e != null ? e.getCode() : "";
                String apiname = "chnId=" + channelId + ",tag.name=" + dataTagName;
                PingBackParams params = new PingBackParams();
                params.add(Keys.T, "0").add("e", eventId).add("ec", "315008").add("pfec", pfec).add(Keys.APINAME, apiname);
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
                    GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.EPG, HOSTSTATUS.FAIL);
                }
                LogRecordUtils.setEventID(eventId);
            }
        });
    }

    public static void sendAlbumPageShowPingback(int pageIndex, int listSize, AlbumInfoModel infoModel, long consumeTime, boolean needSend, int card) {
        if (needSend && pageIndex <= 1 && listSize > 0 && IAlbumConfig.PROJECT_NAME_BASE_LINE.equals(infoModel.getProjectName())) {
            sendMyPageShow(IAlbumConfig.CHANNEL_PAGE.equals(infoModel.getPageType()) ? String.valueOf(infoModel.getChannelId()) : "", getRpageOrQcturl(infoModel), "", "", null, consumeTime + "", infoModel.isMultiHasData() ? IAlbumConfig.STR_FILTER : infoModel.getDataTagName(), "", "", infoModel.getFrom(), null, getTvsrchsource(infoModel), String.valueOf(card), infoModel.getE(), "");
        }
    }

    public static void sendMyPageClick(String r, String block, String rt, String rseat, String rpage, System nullValueA, String c1, String plid, String tvsrchsource, String keyword, String s3, System nullValueB, String qtcurl, String allitem, String dftitem, String sawitem, String line, System nullValueC, String s1) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "20").add("r", r).add("block", block).add("rt", rt).add("rseat", rseat).add("rpage", rpage).add("c1", c1).add("plid", plid).add("tvsrchsource", tvsrchsource).add("keyword", keyword).add(Keys.S3, s3).add("qtcurl", qtcurl).add("allitem", allitem).add("dftitem", dftitem).add("sawitem", sawitem).add("line", line).add("s1", s1);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    public static void sendMyPageClick(String r, String block, String rt, String rseat, String rpage, System nullValueA, String c1, String plid, String tvsrchsource, String keyword, String s3) {
        sendMyPageClick(r, block, rt, rseat, rpage, null, c1, plid, tvsrchsource, keyword, s3, null, "", "", "", "", "", null, "");
    }

    public static void sendMyPageShow(String c1, String qtcurl, String qpId, String rfr, System nullValueA, String td, String block, String plid, String r, String s2, System nullValueB, String tvsrchsource, String card, String e, String s1) {
        PingBackParams params = new PingBackParams();
        params.add(Keys.T, "21").add("bstp", "1").add("tabsrc", PingBackUtils.getTabSrc()).add("c1", c1).add("qtcurl", qtcurl).add("qpid", qpId).add("rfr", rfr).add("td", td).add("block", block).add("tvsrchsource", tvsrchsource).add("plid", plid).add("r", r).add("s2", s2).add("card", card).add("e", e).add("s1", s1);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }
}
