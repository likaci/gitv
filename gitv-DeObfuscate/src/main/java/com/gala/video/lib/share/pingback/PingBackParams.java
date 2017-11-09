package com.gala.video.lib.share.pingback;

import com.gala.sdk.player.IMediaProfile;
import com.gala.video.lib.framework.core.utils.NetworkUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import java.util.HashMap;
import java.util.Map;

public class PingBackParams {
    private Map<String, String> mPingBackParamsMap = new HashMap(20);

    public static class Keys {
        public static final String f2032A = "a";
        public static final String ACTIVITY = "activity";
        public static final String AD_COUNT = "adcount";
        public static final String AID = "aid";
        public static final String ALBUM_ID = "albumid";
        public static final String ALBUM_LIST = "albumlist";
        public static final String ALLITEM = "allitem";
        public static final String ALLLINE = "allline";
        public static final String APINAME = "apiname";
        public static final String AREA = "area";
        public static final String AUTO_START = "autostart";
        public static final String BKT = "bkt";
        public static final String BLOCK = "block";
        public static final String BSTP = "bstp";
        public static final String C1 = "c1";
        public static final String C2 = "c2";
        public static final String CARD = "card";
        public static final String CARDLINE = "cardline";
        public static final String CID = "cid";
        public static final String CLIENTTM = "clienttm";
        public static final String COMPETEAPK = "competeapk";
        public static final String CONTENT = "content";
        public static final String COPY = "copy";
        public static final String COUNT = "count";
        public static final String CRASHTYPE = "crashtype";
        public static final String CT = "ct";
        public static final String DFTITEM = "dftitem";
        public static final String DOCIDS = "docIDs";
        public static final String DOCS = "docs";
        public static final String f2033E = "e";
        public static final String EC = "ec";
        public static final String ERRDETAIL = "errdetail";
        public static final String ERREASON = "erreason";
        public static final String ERRURL = "errurl";
        public static final String EVENT_ID = "event_id";
        public static final String EXCPTNNM = "excptnnm";
        public static final String FBTYPE = "fbtype";
        public static final String FEEDBACK_ID = "feedbackid";
        public static final String FIRSTLOAD = "firstload";
        public static final String FIRSTOPEN = "firstopen";
        public static final String HCDN = "hcdn";
        public static final String HISSRCH = "hissrch";
        public static final String HOMEBUILDTD = "homebuildtd";
        public static final String HTTPCD = "httpcd";
        public static final String INCOMESRC = "incomesrc";
        public static final String INPUT = "input";
        public static final String ISACT = "isact";
        public static final String ISAD = "isad";
        public static final String ISCONTENT = "iscontent";
        public static final String ISPLUGIN = "isplugin";
        public static final String ISPREVUE = "isprevue";
        public static final String ISQR = "isQR";
        public static final String ISREAD = "isread";
        public static final String ISSHOW = "isshow";
        public static final String IS_CARD = "iscard";
        public static final String IS_PERSON = "isperson";
        public static final String IS_PLAY_PATH = "isplaypath";
        public static final String JUMP_TYPE = "jumptype";
        public static final String KEYBOARD = "keyboard";
        public static final String KEYWORD = "keyword";
        public static final String LAUNCHER = "launcher";
        public static final String LDTYPE = "ldtype";
        public static final String LEFTDATASIZE = "leftdatasize";
        public static final String LETTER_EXIST = "letter_exist";
        public static final String LGTTYPE = "lgttype";
        public static final String LINE = "line";
        public static final String LOCAL_TIME = "localtime";
        public static final String MSG_ID = "msgid";
        public static final String MSG_LEVEL = "msglevel";
        public static final String MSG_TYPE = "msgtype";
        public static final String NOW_C1 = "now_c1";
        public static final String NOW_C2 = "now_c2";
        public static final String NOW_EP = "now_ep";
        public static final String NOW_QPID = "now_qpid";
        public static final String OPEN_MODE = "openmode";
        public static final String PAGE = "page";
        public static final String PFEC = "pfec";
        public static final String PLATFORM = "platform";
        public static final String PLAYER = "player";
        public static final String PLID = "plid";
        public static final String PLUGINS_DOWNLOAD_SUCCESS = "plugin_download_success";
        public static final String PLUGIN_DOWNLOAD_ERROR = "plugin_download_error";
        public static final String PLUGIN_UPGRADE_INFO = "plugin_upgrade_info";
        public static final String PLUGIN_UPGRADE_REQUEST = "plugin_upgrade_request";
        public static final String POS = "pos";
        public static final String PPUID = "ppuid";
        public static final String PRE_LOAD_COMPLETED = "pre_load_completed";
        public static final String PRE_LOAD_EXCEPTION = "pre_load_exception";
        public static final String PRE_LOAD_START = "pre_load_start";
        public static final String PTYPE = "ptype";
        public static final String PUSH_POSITION = "pushposition";
        public static final String QISOST = "qisost";
        public static final String QPID = "qpid";
        public static final String QTCURL = "qtcurl";
        public static final String QY_PRV = "qy_prv";
        public static final String f2034R = "r";
        public static final String RA = "ra";
        public static final String RANK = "rank";
        public static final String REF = "ref";
        public static final String REGISTER_EXCEPTION = "re";
        public static final String RESOURCE = "resource";
        public static final String RESOURCE_SHOWN = "resourceshow";
        public static final String RFR = "rfr";
        public static final String RI = "ri";
        public static final String RLINK = "rlink";
        public static final String RPAGE = "rpage";
        public static final String RSEAT = "rseat";
        public static final String RT = "rt";
        public static final String S1 = "s1";
        public static final String S2 = "s2";
        public static final String S3 = "s3";
        public static final String SAWITEM = "sawitem";
        public static final String SCR_POSITION = "scrposition";
        public static final String SEARCH_TIME = "search_time";
        public static final String SHOW_BUY_VIP = "showbuyvip";
        public static final String SHOW_PAY = "showpay";
        public static final String SITE = "site";
        public static final String SOURCE = "source";
        public static final String SPEED = "speed";
        public static final String ST = "st";
        public static final String STATE = "state";
        public static final String f2035T = "t";
        public static final String TAB = "tab";
        public static final String TABSRC = "tabsrc";
        public static final String TAB_ID = "tabid";
        public static final String TAID = "taid";
        public static final String TARGET = "target";
        public static final String TCID = "tcid";
        public static final String TD = "td";
        public static final String TIME = "time";
        public static final String TM1 = "tm1";
        public static final String TVLOGIN = "tvlogin";
        public static final String TVSRCHSOURCE = "tvsrchsource";
        public static final String TYPE = "type";
        public static final String UPGRADE_REQUEST_EXCEPTION = "upgrade_request_exception";
        public static final String URL = "url";
        public static final String USER_AGENT = "userAgent";
        public static final String USRACT = "usract";
    }

    public static class Values {
        public static final String about = "about";
        public static final String buyvip = "buyvip";
        public static final String day = "day";
        public static final String f2036i = "i";
        public static final String login = "login";
        public static final String none = "";
        public static final String payalbum = "payalbum";
        public static final String renew = "renew";
        public static final String tvlogin = "tvlogin";
        public static final String tvsignup = "tvsignup";
        public static final String value0 = "0";
        public static final String value00001 = "00001";
        public static final String value1 = "1";
        public static final String value10 = "10";
        public static final String value11 = "11";
        public static final String value13 = "13";
        public static final String value14 = "14";
        public static final String value15 = "14";
        public static final String value16 = "16";
        public static final String value17 = "17";
        public static final String value18 = "18";
        public static final String value1_ = "-1";
        public static final String value2 = "2";
        public static final String value20 = "20";
        public static final String value21 = "21";
        public static final String value25 = "25";
        public static final String value3 = "3";
        public static final String value303 = "303";
        public static final String value315008 = "315008";
        public static final String value315009 = "315009";
        public static final String value315010 = "315010";
        public static final String value315011 = "315011";
        public static final String value4 = "4";
        public static final String value5 = "5";
        public static final String value5201 = "5201";
        public static final String value6 = "6";
        public static final String value8 = "8";
        public static final String value9 = "9";
    }

    public PingBackParams() {
        this.mPingBackParamsMap.put("rammode", getRamMode(GetInterfaceTools.getIJSConfigDataProvider().getMemoryLevel()));
        this.mPingBackParamsMap.put("pu", GetInterfaceTools.getIGalaAccountManager().getUID());
        this.mPingBackParamsMap.put("hu", GetInterfaceTools.getIGalaAccountManager().getHu());
        this.mPingBackParamsMap.put("network", NetworkUtils.isWifiConnected() ? "wifi" : "wired");
    }

    public PingBackParams add(String key, String value) {
        this.mPingBackParamsMap.put(key, value);
        return this;
    }

    public Map<String, String> build() {
        return this.mPingBackParamsMap;
    }

    private String getRamMode(int ramMode) {
        switch (ramMode) {
            case 0:
                return "lower";
            case 1:
                return "low";
            case 2:
                return IMediaProfile.FEATURE_NORMAL;
            default:
                return IMediaProfile.FEATURE_NORMAL;
        }
    }
}
