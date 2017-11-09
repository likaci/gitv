package com.gala.pingback;

public class PingbackStore {

    public static class ADCOUNT {
        public static final String KEY = "adcount";
        public static final PingbackItem NULL = new PingbackItem("adcount", "");

        public static final PingbackItem ADCOUNT_TYPE(String type) {
            return new PingbackItem("adcount", type);
        }
    }

    public static class AID {
        public static final String KEY = "aid";

        public static final PingbackItem AID_TYPE(String type) {
            return new PingbackItem("aid", type);
        }
    }

    public static class ALBUMLIST {
        public static final String KEY = "albumlist";

        public static final PingbackItem ALBUMLIST_TYPE(String type) {
            return new PingbackItem("albumlist", type);
        }
    }

    public static class ALLITEM {
        public static final String KEY = "allitem";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("allitem", value);
        }
    }

    public static class AREA {
        public static final String KEY = "area";

        public static final PingbackItem AREA_TYPE(String type) {
            return new PingbackItem("area", type);
        }
    }

    public static class BKT {
        public static final String KEY = "bkt";

        public static final PingbackItem BKT_TYPE(String type) {
            return new PingbackItem("bkt", type);
        }
    }

    public static class BLOCK {
        public static final PingbackItem DETAIL = new PingbackItem("block", "detail");
        public static final String KEY = "block";
        public static final PingbackItem REC = new PingbackItem("block", "rec");
        public static final PingbackItem SERIES = new PingbackItem("block", "series");
        public static final PingbackItem STAR = new PingbackItem("block", "star");
        public static final PingbackItem STARWORK = new PingbackItem("block", "starwork");
        public static final PingbackItem VIDEOLIST = new PingbackItem("block", "videolist");

        public static final PingbackItem BLOCK_TYPE(String type) {
            return new PingbackItem("block", type);
        }
    }

    public static class BTSP {
        public static final PingbackItem BSTP_1 = new PingbackItem("bstp", "1");
        public static final String KEY = "bstp";
    }

    public static class C1 {
        public static final String KEY = "c1";

        public static final PingbackItem C1_TYPE(String type) {
            return new PingbackItem("c1", type);
        }
    }

    public static class C2 {
        public static final PingbackItem C2_NULL = new PingbackItem("c2", "");
        public static final String KEY = "c2";

        public static final PingbackItem C2_TYPE(String type) {
            return new PingbackItem("c2", type);
        }
    }

    public static class CARD {
        public static final String KEY = "card";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem("card", type);
        }
    }

    public static class CHNID {
        public static final String KEY = "chnid";

        public static final PingbackItem CHNID_TYPE(String id) {
            return new PingbackItem(KEY, id);
        }
    }

    public static class CID {
        public static final String KEY = "cid";

        public static final PingbackItem CID_TYPE(String type) {
            return new PingbackItem("cid", type);
        }
    }

    public static class CONTENT_TYPE {
        public static final String KEY = "content_type";
        public static final PingbackItem NULL = new PingbackItem(KEY, "");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem(KEY, type);
        }
    }

    public static class COPY {
        public static final String KEY = "copy";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("copy", value);
        }
    }

    public static class COUNT {
        public static final PingbackItem COUNT_NULL = new PingbackItem("count", "");
        public static final String KEY = "count";

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("count", value);
        }
    }

    public static class CT {
        public static final PingbackItem EXIT = new PingbackItem("ct", "160621_detailexit");
        public static final String KEY = "ct";
        public static final PingbackItem LOADED = new PingbackItem("ct", "160621_detailloaded");
        public static final PingbackItem NULL = new PingbackItem("ct", "");

        public static final PingbackItem ITEM(String ctValue) {
            return new PingbackItem("ct", ctValue);
        }
    }

    public static class C0164E {
        public static final String KEY = "e";
        public static final PingbackItem NULL = new PingbackItem("e", "");

        public static final PingbackItem E_ID(String eventid) {
            return new PingbackItem("e", eventid);
        }
    }

    public static class HCDN {
        public static final String KEY = "hcdn";

        public static final PingbackItem HCDN_TYPE(String type) {
            return new PingbackItem("hcdn", type);
        }
    }

    public static class ISPLAYERSTART {
        public static final String KEY = "isplayerstart";

        public static final PingbackItem ST_TYPE(String type) {
            return new PingbackItem(KEY, type);
        }
    }

    public static class ST {
        public static final String KEY = "st";

        public static final PingbackItem ST_TYPE(String type) {
            return new PingbackItem("st", type);
        }
    }

    public static class TD {
        public static final String KEY = "td";
        public static final PingbackItem NULL = new PingbackItem("td", "");

        public static final PingbackItem TD_TYPE(String td) {
            return new PingbackItem("td", td);
        }
    }

    public static class CUSTOMERPAGEEXIT {

        public static class ITEM_CONTENT_TYPE extends CONTENT_TYPE {
        }

        public static class ITEM_CT extends CT {
        }

        public static class ITEM_E extends C0164E {
        }

        public static class ITEM_HCDN extends HCDN {
        }

        public static class ITEM_ISPLAYERSTART extends ISPLAYERSTART {
        }

        public static class ITEM_ST extends ST {
        }

        public static class ITEM_TD extends TD {
        }
    }

    public static class CUSTOMERPAGELOADED {

        public static class ITEM_CONTENT_TYPE extends CONTENT_TYPE {
        }

        public static class ITEM_CT extends CT {
        }

        public static class ITEM_E extends C0164E {
        }

        public static class ITEM_HCDN extends HCDN {
        }

        public static class ITEM_TD extends TD {
        }
    }

    public static class DEVICEID {
        public static final String KEY = "deviceid";
        public static final PingbackItem NULL = new PingbackItem("deviceid", "");

        public static final PingbackItem DEVICEID_TYPE(String type) {
            return new PingbackItem("deviceid", type);
        }
    }

    public static class DFTITEM {
        public static final String KEY = "dftitem";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("dftitem", value);
        }
    }

    public static class EC {
        public static final String KEY = "ec";

        public static final PingbackItem ST_TYPE(String type) {
            return new PingbackItem("ec", type);
        }
    }

    public static class EVENTID {
        public static final String KEY = "event_id";

        public static final PingbackItem EVENT_ID(String eventid) {
            return new PingbackItem("event_id", eventid);
        }
    }

    public static class FLOW {
        public static final String KEY = "flow";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("flow", value);
        }
    }

    public static class PPUID {
        public static final String KEY = "ppuid";

        public static final PingbackItem PPUID_TYPE(String type) {
            return new PingbackItem("ppuid", type);
        }
    }

    public static class RANK {
        public static final String KEY = "rank";

        public static final PingbackItem RANK_TYPE(String type) {
            return new PingbackItem("rank", type);
        }
    }

    public static class SOURCE {
        public static final String KEY = "source";

        public static final PingbackItem SOURCE_TYPE(String type) {
            return new PingbackItem("source", type);
        }
    }

    public static class TAID {
        public static final String KEY = "taid";

        public static final PingbackItem TAID_TYPE(String type) {
            return new PingbackItem("taid", type);
        }
    }

    public static class TCID {
        public static final String KEY = "tcid";

        public static final PingbackItem TCID_TYPE(String type) {
            return new PingbackItem("tcid", type);
        }
    }

    public static class GUESSYOURLIKE {

        public static class AIDTYPE extends AID {
        }

        public static class ALBUMLISTTYPE extends ALBUMLIST {
        }

        public static class AREATYPE extends AREA {
        }

        public static class BKTTYPE extends BKT {
        }

        public static class CIDTYPE extends CID {
        }

        public static class EVENTIDTYPE extends EVENTID {
        }

        public static class PPUIDTYPE extends PPUID {
        }

        public static class RANKTYPE extends RANK {
        }

        public static class SOURCETYPE extends SOURCE {
        }

        public static class TAIDTYPE extends TAID {
        }

        public static class TCIDTYPE extends TCID {
        }
    }

    public static class HISSRCH {
        public static final String KEY = "hissrch";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("hissrch", value);
        }
    }

    public static class IS1080P {
        public static final String KEY = "is1080p";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem(KEY, value);
        }
    }

    public static class IS4K {
        public static final String KEY = "is4k";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem(KEY, value);
        }
    }

    public static class ISAD {
        public static final String KEY = "isad";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("isad", value);
        }
    }

    public static class ISCONTENT {
        public static final String KEY = "iscontent";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("iscontent", value);
        }
    }

    public static class ISPREVUE {
        public static final PingbackItem ISPREVUE_NULL = new PingbackItem("isprevue", "");
        public static final String KEY = "isprevue";

        public static final PingbackItem ISPREVUE_TYPE(String type) {
            return new PingbackItem("isprevue", type);
        }
    }

    public static class ISQR {
        public static final String KEY = "isQR";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("isQR", value);
        }
    }

    public static class KEYWORD {
        public static final String KEY = "keyword";
        public static final PingbackItem NULL = new PingbackItem("keyword", "");

        public static final PingbackItem KEYWORD_TYPE(String type) {
            return new PingbackItem("keyword", type);
        }
    }

    public static class LETTER_EXIST {
        public static final String KEY = "letter_exist";
        public static final PingbackItem LETTER_EXIST_NULL = new PingbackItem("letter_exist", "");
    }

    public static class LINE {
        public static final String KEY = "line";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("line", value);
        }
    }

    public static class LOCALTIME {
        public static final String KEY = "localtime";

        public static final PingbackItem LOCALTIME_TYPE(String calltime) {
            return new PingbackItem("localtime", calltime);
        }
    }

    public static class NOW_C1 {
        public static final String KEY = "now_c1";
        public static final PingbackItem NULL = new PingbackItem("now_c1", "");

        public static final PingbackItem NOW_C1_TYPE(String type) {
            return new PingbackItem("now_c1", type);
        }
    }

    public static class NOW_C2 {
        public static final String KEY = "now_c2";
        public static final PingbackItem NULL = new PingbackItem("now_c2", "");

        public static final PingbackItem NOW_C2_TYPE(String type) {
            return new PingbackItem("now_c2", type);
        }
    }

    public static class NOW_EPISODE {
        public static final String KEY = "now_ep";
        public static final PingbackItem NULL = new PingbackItem("now_ep", "");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem("now_ep", type);
        }
    }

    public static class NOW_QPID {
        public static final String KEY = "now_qpid";
        public static final PingbackItem NULL = new PingbackItem("now_qpid", "");

        public static final PingbackItem NOW_QPID_TYPE(String type) {
            return new PingbackItem("now_qpid", type);
        }
    }

    public static class PLID {
        public static final String KEY = "plid";
        public static final PingbackItem PLID_NULL = new PingbackItem("plid", "");
    }

    public static class REC {
        public static final String KEY = "rec";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("rec", value);
        }
    }

    public static class RFR {
        public static final String KEY = "rfr";
        public static final PingbackItem NULL = new PingbackItem("rfr", "");
        public static final PingbackItem PLAYER = new PingbackItem("rfr", "player");
        public static final PingbackItem RESOURCE = new PingbackItem("rfr", "resource");

        public static final PingbackItem RFR_TYPE(String type) {
            return new PingbackItem("rfr", type);
        }
    }

    public static class RPAGE {
        public static final PingbackItem DETAIL = new PingbackItem("rpage", "detail");
        public static final String KEY = "rpage";
        public static final PingbackItem PLAYER = new PingbackItem("rpage", "player");

        public static final PingbackItem RPAGE_ID(String eventid) {
            return new PingbackItem("rpage", eventid);
        }
    }

    public static class RSEAT {
        public static final PingbackItem COUPON = new PingbackItem("rseat", "coupon");
        public static final PingbackItem DELFAV = new PingbackItem("rseat", "delfav");
        public static final PingbackItem FAV = new PingbackItem("rseat", "fav");
        public static final PingbackItem FULLSCREEN = new PingbackItem("rseat", "fullscreen");
        public static final PingbackItem INTRODUCTION = new PingbackItem("rseat", "introduction");
        public static final String KEY = "rseat";
        public static final PingbackItem NULL = new PingbackItem("rseat", "");
        public static final PingbackItem PAY = new PingbackItem("rseat", "pay");
        public static final PingbackItem PLAYER = new PingbackItem("rseat", "player");

        public static final PingbackItem RSEAT_TYPE(String type) {
            return new PingbackItem("rseat", type);
        }
    }

    public static class RT {
        public static final String KEY = "rt";
        public static final PingbackItem RT_I = new PingbackItem("rt", "i");
    }

    public static class C0165R {
        public static final String KEY = "r";

        public static final PingbackItem R_TYPE(String type) {
            return new PingbackItem("r", type);
        }
    }

    public static class S1 {
        public static final String KEY = "s1";
        public static final PingbackItem S1_NULL = new PingbackItem("s1", "");

        public static final PingbackItem S1_TYPE(String buysource) {
            return new PingbackItem("s1", buysource);
        }
    }

    public static class S2 {
        public static final String KEY = "s2";
        public static final PingbackItem NULL = new PingbackItem("s2", "");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem("s2", type);
        }
    }

    public static class SAWITEM {
        public static final String KEY = "sawitem";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("sawitem", value);
        }
    }

    public static class SERIES {
        public static final String KEY = "series";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("series", value);
        }
    }

    public static class SHOWPAY {
        public static final String KEY = "showpay";
        public static final PingbackItem NULL = new PingbackItem("showpay", "");

        public static final PingbackItem SHOWPAY_TYPE(String type) {
            return new PingbackItem("showpay", type);
        }
    }

    public static class STAR {
        public static final String KEY = "star";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("star", value);
        }
    }

    public static class STATE {
        public static final String KEY = "state";
        public static final PingbackItem NULL = new PingbackItem("state", "");

        public static final PingbackItem STATE_TYPE(String type) {
            return new PingbackItem("state", type);
        }
    }

    public static class TABID {
        public static final String KEY = "tabid";
        public static final PingbackItem NULL = new PingbackItem("tabid", "");

        public static final PingbackItem TABID_TYPE(String type) {
            return new PingbackItem("tabid", type);
        }
    }

    public static class TVSRCHSOURCE {
        public static final String KEY = "tvsrchsource";
        public static final PingbackItem NULL = new PingbackItem("tvsrchsource", "");

        public static final PingbackItem TVSRCHSOURCE_TYPE(String type) {
            return new PingbackItem("tvsrchsource", type);
        }
    }

    public static class VIDEOLIST {
        public static final String KEY = "videolist";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("videolist", value);
        }
    }

    public static class VIPRATE {
        public static final String KEY = "viprate";
        public static final PingbackItem NULL = new PingbackItem("viprate", "");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem("viprate", type);
        }
    }

    public static class PAGE_CLICK {

        public static class ADCOUNTTYPE extends ADCOUNT {
        }

        public static class ALLITEMTYPE extends ALLITEM {
        }

        public static class BLOCKTYPE extends BLOCK {
        }

        public static class C1TYPE extends C1 {
        }

        public static class C2TYPE extends C2 {
        }

        public static class COPYTYPE extends COPY {
        }

        public static class COUNTTYPE extends COUNT {
        }

        public static class DFTITEMTYPE extends DFTITEM {
        }

        public static class ETYPE extends C0164E {
        }

        public static class FLOWTYPE extends FLOW {
        }

        public static class HISSRCHTYPE extends HISSRCH {
        }

        public static class ISADTYPE extends ISAD {
        }

        public static class KEYWORDTYPE extends KEYWORD {
        }

        public static class LETTEREXISTTYPE extends LETTER_EXIST {
        }

        public static class LINETYPE extends LINE {
        }

        public static class NOW_C1TYPE extends NOW_C1 {
        }

        public static class NOW_C2TYPE extends NOW_C2 {
        }

        public static class NOW_EPISODETYPE extends NOW_EPISODE {
        }

        public static class NOW_QPIDTYPE extends NOW_QPID {
        }

        public static class PLIDTYPE extends PLID {
        }

        public static class PREVUETYPE extends ISPREVUE {
        }

        public static class RECTYPE extends REC {
        }

        public static class RFRTYPE extends RFR {
        }

        public static class RPAGETYPE extends RPAGE {
        }

        public static class RSEATTYPE extends RSEAT {
        }

        public static class RTTYPE extends RT {
        }

        public static class RTYPE extends C0165R {
        }

        public static class S1TYPE extends S1 {
        }

        public static class S2Type extends S2 {
        }

        public static class SAWITEMTYPE extends SAWITEM {
        }

        public static class SERIESTYPE extends SERIES {
        }

        public static class SHOWPAYTYPE extends SHOWPAY {
        }

        public static class STARTYPE extends STAR {
        }

        public static class STATETYPE extends STATE {
        }

        public static class TABIDTYPE extends TABID {
        }

        public static class TVSRCHSOURCETYPE extends TVSRCHSOURCE {
        }

        public static class VIDEOLISTTYPE extends VIDEOLIST {
        }

        public static class VIP_RATE extends VIPRATE {
        }
    }

    public static class QPLD {
        public static final String KEY = "qpid";
        public static final PingbackItem NULL = new PingbackItem("qpid", "");

        public static final PingbackItem QPLD_TYPE(String type) {
            return new PingbackItem("qpid", type);
        }
    }

    public static class QTCURL {
        public static final PingbackItem DETAIL = new PingbackItem("qtcurl", "detail");
        public static final String KEY = "qtcurl";
        public static final PingbackItem PLAYER = new PingbackItem("qtcurl", "player");

        public static final PingbackItem QTCURL_TYPE(String type) {
            return new PingbackItem("qtcurl", type);
        }
    }

    public static class QY_PRV {
        public static final String KEY = "qy_prv";
        public static final PingbackItem NULL = new PingbackItem("qy_prv", "");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem("qy_prv", type);
        }
    }

    public static class RLINK {
        public static final String KEY = "rlink";
        public static final PingbackItem NULL = new PingbackItem("rlink", "");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem("rlink", type);
        }
    }

    public static class SHOWBUYVIP {
        public static final String KEY = "showbuyvip";
        public static final PingbackItem NULL = new PingbackItem("showbuyvip", "");

        public static final PingbackItem SHOWBUGVIP_TYPE(String type) {
            return new PingbackItem("showbuyvip", type);
        }
    }

    public static class TABSRC {
        public static final String KEY = "tabsrc";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("tabsrc", value);
        }
    }

    public static class TVLOGIN {
        public static final String KEY = "tvlogin";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String type) {
            return new PingbackItem("tvlogin", type);
        }
    }

    public static class PAGE_SHOW {

        public static class ADCOUNTTYPE extends ADCOUNT {
        }

        public static class ALLITEMTYPE extends ALLITEM {
        }

        public static class BLOCKTYPE extends BLOCK {
        }

        public static class BTSPTYPE extends BTSP {
        }

        public static class C1TYPE extends C1 {
        }

        public static class C2TYPE extends C2 {
        }

        public static class CARDTYPE extends CARD {
        }

        public static class COUNTTYPE extends COUNT {
        }

        public static class DFTITEMTYPE extends DFTITEM {
        }

        public static class ETYPE extends C0164E {
        }

        public static class FLOWTYPE extends FLOW {
        }

        public static class IS1080PTYPE extends IS1080P {
        }

        public static class IS4KTYPE extends IS4K {
        }

        public static class ISCONTENTTYPE extends ISCONTENT {
        }

        public static class ISQRTYPE extends ISQR {
        }

        public static class LINETYPE extends LINE {
        }

        public static class NOW_C1TYPE extends NOW_C1 {
        }

        public static class PLIDTYPE extends PLID {
        }

        public static class QPLDTYPE extends QPLD {
        }

        public static class QTCURLTYPE extends QTCURL {
        }

        public static class QY_PRVTYPE extends QY_PRV {
        }

        public static class RECTYPE extends REC {
        }

        public static class RFRTYPE extends RFR {
        }

        public static class RLINKTYPE extends RLINK {
        }

        public static class RSEATTYEP extends RSEAT {
        }

        public static class RTYPE extends C0165R {
        }

        public static class S1TYPE extends S1 {
        }

        public static class S2TYPE extends S2 {
        }

        public static class SAWITEMTYPE extends SAWITEM {
        }

        public static class SERIESTYPE extends SERIES {
        }

        public static class SHOWBUYVIPTYPE extends SHOWBUYVIP {
        }

        public static class SHOWPAYTYPE extends SHOWPAY {
        }

        public static class STARTYPE extends STAR {
        }

        public static class TABIDTYPE extends TABID {
        }

        public static class TABSRCTYPE extends TABSRC {
        }

        public static class TDTYPE extends TD {
        }

        public static class TVLOGINTYPE extends TVLOGIN {
        }

        public static class TVSRCHSOURCETYPE extends TVSRCHSOURCE {
        }

        public static class VIDEOLISTTYPE extends VIDEOLIST {
        }

        public static class VIP_RATE extends VIPRATE {
        }
    }

    public static class PFEC {
        public static final String KEY = "pfec";

        public static final PingbackItem ST_TYPE(String type) {
            return new PingbackItem("pfec", type);
        }
    }

    public static class RTR {
        public static final String KEY = "rtr";
        public static final PingbackItem RTR_EXIST_NULL = new PingbackItem(KEY, "");
    }

    public static class TYPE {
        public static final String KEY = "type";
        public static final PingbackItem NULL = ITEM("");

        public static final PingbackItem ITEM(String value) {
            return new PingbackItem("type", value);
        }
    }

    public static class USRACT {
        public static final String KEY = "usract";
        public static final PingbackItem NULL = new PingbackItem("usract", "");

        public static final PingbackItem USRACT_TYPE(String type) {
            return new PingbackItem("usract", type);
        }
    }
}
