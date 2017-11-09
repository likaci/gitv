package com.gala.video.lib.share.pingback;

import android.content.Context;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.tvapi.tv2.model.ThreeLevelTag;
import com.gala.tvapi.tv2.model.TwoLevelTag;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ItemKvs;
import com.gala.tvapi.vrs.model.TVTag;
import com.gala.tvapi.vrs.model.TVTags;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.R;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.ClickPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.data.RecordPingbackData;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.uikit.action.data.SubscribeBtnActionData;
import com.gala.video.lib.share.uikit.card.Card;
import com.gala.video.lib.share.uikit.item.Item;
import com.gala.video.lib.share.uikit.page.Page;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.HashMap;
import java.util.List;

public class ClickPingbackUtils {
    public static final String PINGBACK_CLICK_TYPE = "actionType";
    public static final String TAG = "ClickPingbackUtils";

    public static void itemClickForPingbackPost(HashMap<String, String> hashMapData) {
        HashMap<String, String> data = hashMapData;
        if (data == null) {
            data = new HashMap();
        }
        GetInterfaceTools.getIHomePingback().createPingback(ClickPingback.ITEM_CLICK_PINGBACK).addItem(data).post();
    }

    public static void subscribeClickForPingbackPost(Context context, SubscribeBtnActionData data) {
        GetInterfaceTools.getIHomePingback().createPingback(ClickPingback.ITEM_CLICK_PINGBACK).addItem(composeSubscribePingMap(context, data)).post();
    }

    public static void recordClickForPingbackPost(Context context, RecordPingbackData data) {
        HashMap<String, String> pingMap = new HashMap();
        composeRecordPingMap(context, data, pingMap);
        GetInterfaceTools.getIHomePingback().createPingback(ClickPingback.ITEM_CLICK_PINGBACK).addItem(pingMap).post();
    }

    private static void composeRecordPingMap(Context context, RecordPingbackData data, HashMap<String, String> pingMap) {
        String rValue;
        composeTabInfo(context, pingMap);
        String c1Value = "";
        switch (data.getSearchRecordType()) {
            case 12:
                rValue = "记录";
                break;
            default:
                rValue = "记录内容";
                break;
        }
        String pos = "" + data.getPos();
        String cardId = data.getCardId();
        String line = "" + data.getLine();
        String allline = data.getAllline();
        pingMap.put("block", cardId);
        pingMap.put("rseat", "" + pos);
        pingMap.put("line", line);
        pingMap.put("c1", c1Value);
        pingMap.put("r", rValue);
        pingMap.put("hissrch", data.getHissrch());
        pingMap.put("isad", "0");
        pingMap.put(Keys.CARDLINE, data.getCardLine());
        pingMap.put(Keys.ALLLINE, allline);
        pingMap.put(PINGBACK_CLICK_TYPE, ItemDataType.RECORD.getValue());
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void composeSpecialMap(java.util.HashMap<java.lang.String, java.lang.String> r46, com.gala.video.lib.share.uikit.data.ItemInfoModel r47) {
        /*
        r3 = r47.getActionModel();
        if (r3 != 0) goto L_0x0007;
    L_0x0006:
        return;
    L_0x0007:
        r8 = "";
        r25 = "";
        r44 = com.gala.video.lib.share.pingback.ClickPingbackUtils.AnonymousClass1.$SwitchMap$com$gala$video$lib$share$ifmanager$bussnessIF$epg$data$model$ItemDataType;	 Catch:{ Exception -> 0x00de }
        r45 = r3.getItemType();	 Catch:{ Exception -> 0x00de }
        r45 = r45.ordinal();	 Catch:{ Exception -> 0x00de }
        r44 = r44[r45];	 Catch:{ Exception -> 0x00de }
        switch(r44) {
            case 1: goto L_0x0047;
            case 2: goto L_0x0047;
            case 3: goto L_0x0047;
            case 4: goto L_0x00a2;
            case 5: goto L_0x00e8;
            case 6: goto L_0x012a;
            case 7: goto L_0x0141;
            case 8: goto L_0x0155;
            case 9: goto L_0x0179;
            case 10: goto L_0x0185;
            case 11: goto L_0x001c;
            case 12: goto L_0x001c;
            case 13: goto L_0x001c;
            case 14: goto L_0x01af;
            case 15: goto L_0x01be;
            case 16: goto L_0x0223;
            case 17: goto L_0x0269;
            case 18: goto L_0x0283;
            case 19: goto L_0x028e;
            case 20: goto L_0x02a2;
            case 21: goto L_0x02b1;
            case 22: goto L_0x02df;
            case 23: goto L_0x02fc;
            case 24: goto L_0x001c;
            case 25: goto L_0x0338;
            case 26: goto L_0x036e;
            case 27: goto L_0x0379;
            case 28: goto L_0x0384;
            case 29: goto L_0x038f;
            case 30: goto L_0x038f;
            case 31: goto L_0x039a;
            case 32: goto L_0x03a9;
            default: goto L_0x001c;
        };
    L_0x001c:
        r44 = "c1";
        r0 = r46;
        r1 = r44;
        r0.put(r1, r8);
        r44 = "r";
        r0 = r46;
        r1 = r44;
        r2 = r25;
        r0.put(r1, r2);
        r44 = "actionType";
        r45 = r3.getItemType();
        r45 = r45.getValue();
        r0 = r46;
        r1 = r44;
        r2 = r45;
        r0.put(r1, r2);
        goto L_0x0006;
    L_0x0047:
        r5 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r5 = (com.gala.video.lib.share.uikit.action.model.AlbumVideoLiveActionModel) r5;	 Catch:{ Exception -> 0x00de }
        r44 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de }
        r44.<init>();	 Catch:{ Exception -> 0x00de }
        r45 = "";
        r44 = r44.append(r45);	 Catch:{ Exception -> 0x00de }
        r45 = r5.getLabel();	 Catch:{ Exception -> 0x00de }
        r45 = r45.getVideo();	 Catch:{ Exception -> 0x00de }
        r0 = r45;
        r0 = r0.chnId;	 Catch:{ Exception -> 0x00de }
        r45 = r0;
        r44 = r44.append(r45);	 Catch:{ Exception -> 0x00de }
        r8 = r44.toString();	 Catch:{ Exception -> 0x00de }
        r44 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de }
        r44.<init>();	 Catch:{ Exception -> 0x00de }
        r45 = "";
        r44 = r44.append(r45);	 Catch:{ Exception -> 0x00de }
        r45 = r5.getLabel();	 Catch:{ Exception -> 0x00de }
        r45 = r45.getVideo();	 Catch:{ Exception -> 0x00de }
        r0 = r45;
        r0 = r0.qpId;	 Catch:{ Exception -> 0x00de }
        r45 = r0;
        r44 = r44.append(r45);	 Catch:{ Exception -> 0x00de }
        r25 = r44.toString();	 Catch:{ Exception -> 0x00de }
        r44 = com.gala.video.lib.share.pingback.PingbackUtils.getCurrentPingbackPage();	 Catch:{ Exception -> 0x00de }
        r45 = com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.PingbackPage.Ucenter;	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r1 = r45;
        if (r0 != r1) goto L_0x001c;
    L_0x009d:
        r25 = "记录内容";
        goto L_0x001c;
    L_0x00a2:
        r8 = "101221";
        r9 = "";
        r21 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r21 = (com.gala.video.lib.share.uikit.action.model.AlbumVideoLiveActionModel) r21;	 Catch:{ Exception -> 0x00de }
        r44 = r21.getLabel();	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r0 = r0.itemId;	 Catch:{ Exception -> 0x00de }
        r25 = r0;
        r44 = r21.getLabel();	 Catch:{ Exception -> 0x00de }
        r20 = r44.getLiveAlbumList();	 Catch:{ Exception -> 0x00de }
        if (r20 == 0) goto L_0x00e4;
    L_0x00c2:
        r44 = 0;
        r0 = r20;
        r1 = r44;
        r44 = r0.get(r1);	 Catch:{ Exception -> 0x00de }
        r44 = (com.gala.tvapi.tv2.model.Album) r44;	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r9 = r0.live_channelId;	 Catch:{ Exception -> 0x00de }
    L_0x00d2:
        r44 = "c2";
        r0 = r46;
        r1 = r44;
        r0.put(r1, r9);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x00de:
        r14 = move-exception;
        r14.printStackTrace();
        goto L_0x001c;
    L_0x00e4:
        r9 = "";
        goto L_0x00d2;
    L_0x00e8:
        r10 = "";
        r0 = r3 instanceof com.gala.video.lib.share.uikit.action.model.LiveChannelActionModel;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        if (r44 == 0) goto L_0x0117;
    L_0x00f1:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.LiveChannelActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r19 = r0;
        r18 = r19.getLabel();	 Catch:{ Exception -> 0x00de }
        r8 = "101221";
    L_0x00fd:
        if (r18 == 0) goto L_0x0126;
    L_0x00ff:
        r0 = r18;
        r0 = r0.id;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r26 = java.lang.String.valueOf(r44);	 Catch:{ Exception -> 0x00de }
    L_0x0109:
        r10 = r26;
        r44 = "c2";
        r0 = r46;
        r1 = r44;
        r0.put(r1, r10);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0117:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.CarouseHistoryActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r11 = r0;
        r18 = r11.getLabel();	 Catch:{ Exception -> 0x00de }
        r8 = "轮播最常看";
        r25 = "轮播最常看";
        goto L_0x00fd;
    L_0x0126:
        r26 = "";
        goto L_0x0109;
    L_0x012a:
        r8 = "101221";
        r44 = com.gala.video.lib.framework.core.env.AppRuntimeEnv.get();	 Catch:{ Exception -> 0x00de }
        r44 = r44.getApplicationContext();	 Catch:{ Exception -> 0x00de }
        r44 = r44.getResources();	 Catch:{ Exception -> 0x00de }
        r45 = com.gala.video.lib.share.R.string.carousel_window_name;	 Catch:{ Exception -> 0x00de }
        r25 = r44.getString(r45);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0141:
        r44 = com.gala.video.lib.framework.core.env.AppRuntimeEnv.get();	 Catch:{ Exception -> 0x00de }
        r44 = r44.getApplicationContext();	 Catch:{ Exception -> 0x00de }
        r44 = r44.getResources();	 Catch:{ Exception -> 0x00de }
        r45 = com.gala.video.lib.share.R.string.home_1;	 Catch:{ Exception -> 0x00de }
        r25 = r44.getString(r45);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0155:
        r15 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r15 = (com.gala.video.lib.share.uikit.action.model.H5ActionModel) r15;	 Catch:{ Exception -> 0x00de }
        r44 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00de }
        r44.<init>();	 Catch:{ Exception -> 0x00de }
        r45 = "H5_";
        r44 = r44.append(r45);	 Catch:{ Exception -> 0x00de }
        r45 = r15.getLabel();	 Catch:{ Exception -> 0x00de }
        r45 = com.gala.video.lib.share.uikit.data.data.processor.DataBuildTool.getPrompt(r45);	 Catch:{ Exception -> 0x00de }
        r44 = r44.append(r45);	 Catch:{ Exception -> 0x00de }
        r25 = r44.toString();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0179:
        r23 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r23 = (com.gala.video.lib.share.uikit.action.model.PersonActionModel) r23;	 Catch:{ Exception -> 0x00de }
        r25 = r23.getId();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0185:
        r24 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r24 = (com.gala.video.lib.share.uikit.action.model.PlayListActionModel) r24;	 Catch:{ Exception -> 0x00de }
        r44 = r24.getLabel();	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r0 = r0.id;	 Catch:{ Exception -> 0x00de }
        r25 = r0;
        r44 = r24.getLabel();	 Catch:{ Exception -> 0x00de }
        if (r44 == 0) goto L_0x01ab;
    L_0x019b:
        r44 = r24.getLabel();	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r0 = r0.categoryId;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r8 = java.lang.String.valueOf(r44);	 Catch:{ Exception -> 0x00de }
    L_0x01a9:
        goto L_0x001c;
    L_0x01ab:
        r8 = "";
        goto L_0x01a9;
    L_0x01af:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.SettingsActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r30 = r0;
        r44 = r30.getSettingsData();	 Catch:{ Exception -> 0x00de }
        r25 = r44.getName();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x01be:
        r32 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r32 = (com.gala.video.lib.share.uikit.action.model.TVTagActionModel) r32;	 Catch:{ Exception -> 0x00de }
        r31 = r32.getLabel();	 Catch:{ Exception -> 0x00de }
        if (r31 == 0) goto L_0x021f;
    L_0x01ca:
        r0 = r31;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        if (r44 == 0) goto L_0x021f;
    L_0x01d2:
        r0 = r31;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r44 = r44.getTVTag();	 Catch:{ Exception -> 0x00de }
        if (r44 == 0) goto L_0x021f;
    L_0x01de:
        r0 = r31;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r44 = r44.getTVTag();	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r0 = r0.channelId;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r8 = java.lang.String.valueOf(r44);	 Catch:{ Exception -> 0x00de }
    L_0x01f2:
        r0 = r31;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r35 = r44.getTVTag();	 Catch:{ Exception -> 0x00de }
        r44 = com.gala.video.lib.share.ifmanager.CreateInterfaceTools.createChannelProviderProxy();	 Catch:{ Exception -> 0x00de }
        r0 = r35;
        r0 = r0.channelId;	 Catch:{ Exception -> 0x00de }
        r45 = r0;
        r44 = r44.getChannelById(r45);	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r0 = r0.tags;	 Catch:{ Exception -> 0x00de }
        r36 = r0;
        r44 = "_";
        r0 = r35;
        r1 = r36;
        r2 = r44;
        r25 = getListPageTagName(r0, r1, r2);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x021f:
        r8 = "";
        goto L_0x01f2;
    L_0x0223:
        r33 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r33 = (com.gala.video.lib.share.uikit.action.model.TVTagActionModel) r33;	 Catch:{ Exception -> 0x00de }
        r34 = r33.getLabel();	 Catch:{ Exception -> 0x00de }
        if (r34 == 0) goto L_0x0265;
    L_0x022f:
        r0 = r34;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        if (r44 == 0) goto L_0x0265;
    L_0x0237:
        r0 = r34;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r44 = r44.getTVTag();	 Catch:{ Exception -> 0x00de }
        if (r44 == 0) goto L_0x0265;
    L_0x0243:
        r0 = r34;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r44 = r44.getTVTag();	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r0 = r0.channelId;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r8 = java.lang.String.valueOf(r44);	 Catch:{ Exception -> 0x00de }
    L_0x0257:
        r0 = r34;
        r0 = r0.itemKvs;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r0 = r44;
        r0 = r0.tvShowName;	 Catch:{ Exception -> 0x00de }
        r25 = r0;
        goto L_0x001c;
    L_0x0265:
        r8 = "";
        goto L_0x0257;
    L_0x0269:
        r12 = r47.getActionModel();	 Catch:{ Exception -> 0x00de }
        r12 = (com.gala.video.lib.share.uikit.action.model.ChannelActionModel) r12;	 Catch:{ Exception -> 0x00de }
        r44 = r12.getChannel();	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r8 = r0.id;	 Catch:{ Exception -> 0x00de }
        r44 = r12.getChannel();	 Catch:{ Exception -> 0x00de }
        r0 = r44;
        r0 = r0.name;	 Catch:{ Exception -> 0x00de }
        r25 = r0;
        goto L_0x001c;
    L_0x0283:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.ResourceGroupActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r28 = r0;
        r25 = r28.getItemId();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x028e:
        r44 = com.gala.video.lib.framework.core.env.AppRuntimeEnv.get();	 Catch:{ Exception -> 0x00de }
        r44 = r44.getApplicationContext();	 Catch:{ Exception -> 0x00de }
        r44 = r44.getResources();	 Catch:{ Exception -> 0x00de }
        r45 = com.gala.video.lib.share.R.string.home_subject_review;	 Catch:{ Exception -> 0x00de }
        r25 = r44.getString(r45);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x02a2:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.RecommendAppActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r27 = r0;
        r17 = r27.getChannelLable();	 Catch:{ Exception -> 0x00de }
        r25 = getRecommendAppKey(r17);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x02b1:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.ApplicationActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r6 = r0;
        r13 = r6.getData();	 Catch:{ Exception -> 0x00de }
        r25 = r13.getAppName();	 Catch:{ Exception -> 0x00de }
        r37 = com.gala.video.lib.share.uikit.loader.data.AppRequest.checkApp();	 Catch:{ Exception -> 0x00de }
        r7 = "";
        switch(r37) {
            case 1: goto L_0x02d3;
            case 2: goto L_0x02db;
            case 3: goto L_0x02d7;
            default: goto L_0x02c7;
        };	 Catch:{ Exception -> 0x00de }
    L_0x02c7:
        r44 = "block";
        r0 = r46;
        r1 = r44;
        r0.put(r1, r7);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x02d3:
        r7 = "全部应用";
        goto L_0x02c7;
    L_0x02d7:
        r7 = "应用";
        goto L_0x02c7;
    L_0x02db:
        r7 = "应用推荐";
        goto L_0x02c7;
    L_0x02df:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.AdActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r4 = r0;
        r44 = r4.getCommonAdData();	 Catch:{ Exception -> 0x00de }
        r25 = r44.getTitle();	 Catch:{ Exception -> 0x00de }
        r44 = "isad";
        r45 = "1";
        r0 = r46;
        r1 = r44;
        r2 = r45;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x02fc:
        r25 = "通栏广告";
        r8 = "";
        r44 = "block";
        r45 = "通栏广告";
        r0 = r46;
        r1 = r44;
        r2 = r45;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x00de }
        r44 = "rseat";
        r45 = "1";
        r0 = r46;
        r1 = r44;
        r2 = r45;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x00de }
        r44 = "c1";
        r0 = r46;
        r1 = r44;
        r0.put(r1, r8);	 Catch:{ Exception -> 0x00de }
        r44 = "r";
        r0 = r46;
        r1 = r44;
        r2 = r25;
        r0.put(r1, r2);	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0338:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.VipH5ActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r41 = r0;
        r25 = r41.getTitle();	 Catch:{ Exception -> 0x00de }
        r44 = com.gala.video.lib.share.ifmanager.GetInterfaceTools.getIGalaAccountManager();	 Catch:{ Exception -> 0x00de }
        r16 = r44.isVip();	 Catch:{ Exception -> 0x00de }
        r44 = "/";
        r0 = r25;
        r1 = r44;
        r42 = r0.split(r1);	 Catch:{ Exception -> 0x00de }
        r0 = r42;
        r0 = r0.length;	 Catch:{ Exception -> 0x00de }
        r44 = r0;
        r45 = 2;
        r0 = r44;
        r1 = r45;
        if (r0 != r1) goto L_0x001c;
    L_0x0361:
        if (r16 == 0) goto L_0x0369;
    L_0x0363:
        r44 = 1;
        r25 = r42[r44];	 Catch:{ Exception -> 0x00de }
    L_0x0367:
        goto L_0x001c;
    L_0x0369:
        r44 = 0;
        r25 = r42[r44];	 Catch:{ Exception -> 0x00de }
        goto L_0x0367;
    L_0x036e:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.VipH5ActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r40 = r0;
        r25 = r40.getTitle();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0379:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.MsgCenterActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r22 = r0;
        r25 = r22.getTitle();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x0384:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.VipVideoActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r43 = r0;
        r25 = r43.getTitle();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x038f:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.SubscribeCollectionActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r29 = r0;
        r25 = r29.getTitle();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x039a:
        r0 = r3;
        r0 = (com.gala.video.lib.share.uikit.action.model.UcenterRecordAllActionModel) r0;	 Catch:{ Exception -> 0x00de }
        r38 = r0;
        r39 = r38.getUcenterRecordAllData();	 Catch:{ Exception -> 0x00de }
        r25 = r39.getTitle();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
    L_0x03a9:
        r44 = com.gala.video.lib.share.pingback.AlbumDetailPingbackUtils.getInstance();	 Catch:{ Exception -> 0x00de }
        r25 = r44.getEntryAllTitle();	 Catch:{ Exception -> 0x00de }
        goto L_0x001c;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.pingback.ClickPingbackUtils.composeSpecialMap(java.util.HashMap, com.gala.video.lib.share.uikit.data.ItemInfoModel):void");
    }

    private static void composeTabInfo(Context context, HashMap<String, String> pingMap) {
        switch (PingbackUtils.getPingbackPage(context)) {
            case HomePage:
                pingMap.put("rpage", "tab_" + PingBackCollectionFieldUtils.getTabName());
                pingMap.put("count", PingBackCollectionFieldUtils.getTabIndex());
                return;
            case AlbumDetail:
                composeDetailInfoInfo(pingMap);
                return;
            case SoloTab:
                pingMap.put("e", SoloTabPingbackUitls.getInstance().getE());
                pingMap.put("s2", SoloTabPingbackUitls.getInstance().getS2());
                pingMap.put("rpage", "solo_" + SoloTabPingbackUitls.getInstance().getTabName());
                return;
            case MultiSubject:
                String cardID = (String) pingMap.get("block");
                pingMap.put("rpage", MultiSubjectEnterUtils.PLAY_TYPE_FROM_MULTI);
                pingMap.put("plid", cardID);
                pingMap.put("e", MultiSubjectPingbackUitls.getInstance().getE());
                pingMap.put("s2", MultiSubjectPingbackUitls.getInstance().getS2());
                return;
            case Ucenter:
                pingMap.put("rpage", GetInterfaceTools.getIGalaAccountManager().isLogin(AppRuntimeEnv.get().getApplicationContext()) ? "mine_loggedin" : "mine_guest");
                pingMap.put("e", UcenterPingbackUtils.getInstance().getE());
                return;
            case DetailAll:
                pingMap.put("rpage", "all_detail");
                pingMap.put("now_c1", PingBackCollectionFieldUtils.getNow_c1());
                pingMap.put("now_qpid", PingBackCollectionFieldUtils.getNow_qpid());
                return;
            default:
                return;
        }
    }

    private static void composeDetailInfoInfo(HashMap<String, String> pingMap) {
        pingMap.put("now_c1", PingBackCollectionFieldUtils.getNow_c1());
        pingMap.put("now_qpid", PingBackCollectionFieldUtils.getNow_qpid());
        pingMap.put("e", PingBackCollectionFieldUtils.getE());
        pingMap.put("rfr", PingBackCollectionFieldUtils.getRfr());
        pingMap.put("isprevue", "");
        pingMap.put("now_ep", "");
        pingMap.put("rt", "i");
        pingMap.put("rpage", "detail");
    }

    private static HashMap<String, String> composeSubscribePingMap(Context context, SubscribeBtnActionData data) {
        HashMap<String, String> pingMap = new HashMap();
        composeTabInfo(context, pingMap);
        String rValue = data.getQpId();
        String c1Value = "" + data.getChnId();
        String pos = "";
        String cardLine = "" + (data.getCardLine() + 1);
        switch (data.getSubscribeType()) {
            case -1:
                pos = "暂不支持预约";
                break;
            case 1:
            case 2:
                pos = "取消预约";
                break;
            case 3:
                break;
            default:
                pos = "预约";
                break;
        }
        String cardId = data.getCardId();
        String line = "" + data.getLine();
        String allline = data.getAllLine();
        pingMap.put("block", cardId);
        pingMap.put("rseat", "" + pos);
        pingMap.put("line", line);
        pingMap.put("c1", c1Value);
        pingMap.put("r", rValue);
        pingMap.put("isad", "0");
        pingMap.put(Keys.CARDLINE, cardLine);
        pingMap.put(Keys.ALLLINE, allline);
        pingMap.put(PINGBACK_CLICK_TYPE, ItemDataType.SUBSCRIBE_BTN.getValue());
        return pingMap;
    }

    public static HashMap<String, String> composeCommonItemPingMap(Context context, int pos, String mCardId, String cardLine, String allline, Item item) {
        HashMap<String, String> pingMap = new HashMap();
        pingMap.put("block", mCardId);
        pingMap.put("rseat", "" + pos);
        pingMap.put("line", cardLine);
        if (PingbackUtils.getPingbackPage(context) != PingbackPage.DetailAll) {
            pingMap.put("isad", "0");
            pingMap.put(Keys.ALLLINE, allline);
            pingMap.put(Keys.CARDLINE, "" + (item.getLine() + 1));
        }
        composeSpecialMap(pingMap, item.getModel());
        composeTabInfo(context, pingMap);
        return pingMap;
    }

    private static String getRecommendAppKey(ChannelLabel label) {
        ItemKvs kvs = label.getItemKvs();
        if (kvs != null) {
            String url;
            if (kvs.appkey.equalsIgnoreCase("childapp")) {
                url = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getChildAppUrl();
                if (url == null || url.isEmpty() || url.equalsIgnoreCase("none")) {
                    return "";
                }
            }
            if (kvs.appkey.equalsIgnoreCase("chinapokerapp")) {
                url = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getChinaPokerAppUrl();
                if (url == null || url.isEmpty() || url.equalsIgnoreCase("none")) {
                    return "";
                }
            }
            if (kvs.appkey.equalsIgnoreCase("chinapokerapp") || kvs.appkey.equalsIgnoreCase("childapp")) {
                return kvs.appkey;
            }
        }
        return "";
    }

    private static String getListPageTagName(TVTags tvTags, List<TwoLevelTag> twoLevelTagList, String separator) {
        if (tvTags == null) {
            return "";
        }
        List<TVTag> tags = tvTags.tags;
        CharSequence listPageOrTagName = "";
        boolean hasSortTag = false;
        if (!ListUtils.isEmpty((List) twoLevelTagList)) {
            for (TwoLevelTag twoLevelTag : twoLevelTagList) {
                if (twoLevelTag != null) {
                    for (ThreeLevelTag threeLevelTag : twoLevelTag.tags) {
                        if (threeLevelTag != null) {
                            String threeLevelTagName = threeLevelTag.n;
                            String threeLevelTagValue = threeLevelTag.v;
                            for (TVTag tvTag : tags) {
                                String tagValue = tvTag.value;
                                if (!(tagValue == null || !tagValue.equals(threeLevelTagValue) || threeLevelTagName == null || "全部".equals(threeLevelTagName))) {
                                    if ("最近热播".equals(threeLevelTagName) || "最近更新".equals(threeLevelTagName)) {
                                        hasSortTag = true;
                                    }
                                    String listPageOrTagName2 = listPageOrTagName2 + threeLevelTagName + separator;
                                }
                            }
                        }
                    }
                }
            }
        }
        if (!StringUtils.isEmpty(listPageOrTagName)) {
            listPageOrTagName = listPageOrTagName.substring(0, listPageOrTagName.length() - 1);
        }
        if (!(StringUtils.isEmpty(listPageOrTagName) || hasSortTag)) {
            listPageOrTagName = listPageOrTagName + separator + "最近热播";
        }
        if (StringUtils.isEmpty(listPageOrTagName)) {
            return getChannelNameByChannelId(tvTags.channelId);
        }
        return getChannelNameByChannelId(tvTags.channelId) + separator + listPageOrTagName;
    }

    private static String getChannelNameByChannelId(int channelId) {
        if (10006 == channelId) {
            return ResourceUtil.getStr(R.string.q_album_str_vip);
        }
        if (channelId == 1000002) {
            return ResourceUtil.getStr(R.string.q_album_str_new_vip);
        }
        Channel c = CreateInterfaceTools.createChannelProviderProxy().getChannelById(channelId);
        return c == null ? "" : c.name;
    }

    public static int getLine(Page page, Card currentCard, Item item) {
        int line = 0;
        List<Card> cardList = page.getCards();
        int size = cardList.size();
        for (int i = 0; i < size; i++) {
            Card card = (Card) cardList.get(i);
            if (card != null) {
                if (currentCard == card) {
                    break;
                }
                line += card.getAllLine();
            }
        }
        return line + (item.getLine() + 1);
    }
}
