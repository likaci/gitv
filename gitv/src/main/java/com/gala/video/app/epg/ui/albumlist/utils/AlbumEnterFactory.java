package com.gala.video.app.epg.ui.albumlist.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.gala.report.msghandler.MsgHanderEnum.HOSTMODULE;
import com.gala.report.msghandler.MsgHanderEnum.HOSTSTATUS;
import com.gala.tvapi.tv2.model.Channel;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.constant.IFootConstant;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel.SearchIntentModel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumOpenApiModel;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.interaction.ActionSet;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.push.mqttv3.internal.ClientDefaults;

public class AlbumEnterFactory {
    public static final String CHANNEL_STR = "channel[";
    public static final int INTENT_FLAG_INVALID = -1;
    public static final int LOAD_LIMIT_INVALID = 0;
    public static final String SIGN_STR = "]";
    private final String LIVE_CHANNEL = "livechannel";
    private boolean NOLOG;
    private final String TAG = "EPG/AlbumEnterFactory";
    private final String VIP_CHANNEL = "vipchannel";

    public AlbumEnterFactory() {
        this.NOLOG = !DebugUtils.ALBUM4_NEEDLOG;
    }

    public void startChannelPage(Context context, Channel channel) {
        startChannelPage(context, channel, null);
    }

    public void startChannelPage(Context context, Channel channel, String form) {
        String str = null;
        if (channel == null) {
            if (!this.NOLOG) {
                str = "--startChannelPage--context=" + context + "---channel=" + channel;
            }
            log(str);
            return;
        }
        int id = StringUtils.parse(channel.id, 0);
        String buysource = null;
        if (channel.type == 0 || id == 1000004) {
            buysource = CHANNEL_STR + id + SIGN_STR + "_" + IAlbumConfig.BUY_SOURCE_NEED_REPLACE;
        }
        if (id == 1000002) {
            buysource = CHANNEL_STR + id + SIGN_STR;
        }
        startChannelPage(context, id, channel.name, 0, -1, null, null, form, buysource, null, false);
    }

    public void startChannelPage(Context context, int channelId) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---channelId=" + channelId);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, null, 0, -1, null, null, null, null, null, false);
        }
    }

    public void startChannelPage(Context context, int channelId, String s2, String buySource) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---channelId=" + channelId);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, null, 0, -1, null, null, s2, buySource + "_" + IAlbumConfig.BUY_SOURCE_NEED_REPLACE, null, false);
        }
    }

    public void startChannelPage(Context context, int channelId, String s2, String buySource, boolean jumpNextByRecTag) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---channelId=" + channelId);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, null, 0, -1, null, null, s2, buySource + "_" + IAlbumConfig.BUY_SOURCE_NEED_REPLACE, null, jumpNextByRecTag);
        }
    }

    public void startChannelPage(Context context, String labelFirstLocationTagId, int channelId, String s2, String buySource) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---labelFirstLocationTagId=" + labelFirstLocationTagId + "---channelId=" + channelId);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, null, 0, -1, labelFirstLocationTagId, null, s2, buySource + "_" + IAlbumConfig.BUY_SOURCE_NEED_REPLACE, null, false);
        }
    }

    public void startChannelMultiDataPage(Context context, String[] multiFirstLocationTagId, int channelId, String s2, String buySource) {
        log(this.NOLOG ? null : "--startChannelMultiDataPage---context=" + context + "---multiTagId=" + multiFirstLocationTagId + "---channelId=" + channelId);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, null, 0, -1, null, multiFirstLocationTagId, s2, buySource + "_" + IAlbumConfig.BUY_SOURCE_NEED_REPLACE, null, false);
        }
    }

    public void startChannelPage(Context context, int channelId, String channelName) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---channelId=" + channelId + "--channelName=" + channelName);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, channelName, 0, -1, null, null, null, null, null, false);
        }
    }

    public void startChannelPage(Context context, int channelId, int loadLimitSize) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---channelId=" + channelId + "--loadLimitSize=" + loadLimitSize);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, null, loadLimitSize, -1, null, null, null, null, null, false);
        }
    }

    public void startChannelPage(Context context, int channelId, String channelName, int loadLimitSize) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---channelId=" + channelId);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, channelName, loadLimitSize, -1, null, null, null, null, null, false);
        }
    }

    public void startChannelPage(Context context, int channelId, String channelName, int loadLimitSize, int intentFlags) {
        log(this.NOLOG ? null : "--startChannelPage---context=" + context + "---channelId=" + channelId);
        if (context != null && channelId > -1) {
            startChannelPage(context, channelId, channelName, loadLimitSize, intentFlags, null, null, null, null, null, false);
        }
    }

    public void startChannelPageOpenApi(Context context, int channelId, String channelName, int loadLimitSize, int intentFlags) {
        startChannelPage(context, channelId, channelName, loadLimitSize, intentFlags, null, null, null, null, IAlbumConfig.PROJECT_NAME_OPEN_API, false);
    }

    public void startChannelPage(Context context, int channelId, String channelName, int loadLimitSize, int intentFlags, String labelFirstLocationTagId, String[] multiFirstLocationTagId, String from, String buySource, String projectName, boolean jumpNextByRecTag) {
        if (context != null && channelId > -1) {
            AlbumIntentModel p = new AlbumIntentModel();
            p.setE(PingBackUtils.createEventId());
            p.setChannelId(channelId);
            p.setHasRecommendTag(AlbumInfoFactory.hasRecommendTag(channelId));
            boolean z = jumpNextByRecTag && !AlbumInfoFactory.isNewVipChannel(channelId);
            p.setJumpNextByRecTag(z);
            if (StringUtils.isEmpty((CharSequence) channelName)) {
                channelName = AlbumInfoFactory.getChannelNameByChannelId(channelId);
            }
            p.setChannelName(channelName);
            if (TextUtils.isEmpty(from)) {
                from = CHANNEL_STR + channelId + SIGN_STR;
            }
            p.setFrom(from);
            if (10009 == channelId) {
                p.setBuySource("hotlist");
            } else if (channelId != 1000002 && channelId != 1000004) {
                if (TextUtils.isEmpty(buySource)) {
                    buySource = CHANNEL_STR + channelId + SIGN_STR;
                }
                p.setBuySource(buySource);
            } else if (!TextUtils.isEmpty(buySource)) {
                p.setBuySource(buySource);
            } else if (channelId == 1000002) {
                p.setBuySource("vipchannel_need_replace");
                p.setFrom("vip");
            } else {
                p.setBuySource("livechannel_need_replace");
                p.setFrom("live");
            }
            p.setLoadLimitSize(loadLimitSize);
            p.setPageType(IAlbumConfig.CHANNEL_PAGE);
            if (TextUtils.isEmpty(projectName)) {
                projectName = IAlbumConfig.PROJECT_NAME_BASE_LINE;
            }
            p.setProjectName(projectName);
            p.setFirstLabelLocationTagId(labelFirstLocationTagId);
            p.setFirstMultiLocationTagId(multiFirstLocationTagId);
            Intent intent = getBaseIntent(context);
            intent.putExtra("intent_model", p);
            if (intentFlags != -1) {
                intent.setFlags(intentFlags);
            }
            PageIOUtils.activityIn(context, intent);
        }
    }

    public void startChannelVip(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startChannelVip--context=" + context);
        if (context != null) {
            Intent intent = getBaseIntent(context);
            intent.putExtra("intent_channel_id", 10006);
            if (intentFlags != -1) {
                intent.setFlags(intentFlags);
            }
            PageIOUtils.activityIn(context, intent);
        }
    }

    public void startChannelVip(Context context) {
        startChannelVip(context, -1);
    }

    public void startChannelNewVip(Context context) {
        startChannelNewVip(context, -1);
    }

    public void startChannelNewVip(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startChannelNewVip---context=" + context);
        if (context != null) {
            startChannelPage(context, 1000002, null, 0, intentFlags, null, null, null, null, null, false);
        }
    }

    public void startChannelNewVipOpenApi(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startChannelNewVipOpenApi---context=" + context);
        if (context != null) {
            startChannelPage(context, 1000002, null, 0, intentFlags, null, null, null, null, IAlbumConfig.PROJECT_NAME_OPEN_API, false);
        }
    }

    public void startChannelLivePage(Context context) {
        log(this.NOLOG ? null : "--startChannelLivePage---context=" + context);
        if (context != null) {
            startChannelPage(context, 1000004, null, 0, -1, null, null, null, null, null, false);
        }
    }

    public void startChannelLivePageOpenApi(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startChannelLivePageOpenApi---context=" + context);
        if (context != null) {
            startChannelPage(context, 1000004, null, 0, intentFlags, null, null, null, null, IAlbumConfig.PROJECT_NAME_OPEN_API, false);
        }
    }

    public void startSearchResultPageOpenApi(Context context, int channelId, String keyword, int clickType, String qpId, int intentFlags, String channelName, String from) {
        startSearchResultPage(context, channelId, keyword, clickType, qpId, intentFlags, IAlbumConfig.PROJECT_NAME_OPEN_API, channelName, from);
    }

    public void startSearchResultPage(Context context, int channelId, String keyword, int clickType, String qpId, int intentFlags, String projectName, String channelName, String from) {
        log(this.NOLOG ? null : "--startSearchResultPage--context=" + context + "---channelId=" + channelId + "--keyword=" + keyword + "---clickType=" + clickType + "---qpId=" + qpId);
        if (context != null) {
            Intent intent = initSearchResultIntent(context, channelId, keyword, clickType, qpId, projectName, channelName, from);
            if (intentFlags != -1) {
                intent.setFlags(intentFlags);
            }
            PageIOUtils.activityIn(context, intent);
        }
    }

    public void startSearchResultPageForResultOpenApi(Context context, int channelId, String keyword, int clickType, String qpId, int requestCode, String channelName, String from) {
        startSearchResultPageForResult(context, channelId, keyword, clickType, qpId, requestCode, IAlbumConfig.PROJECT_NAME_OPEN_API, channelName, from);
    }

    public void startSearchResultPageForResult(Context context, int channelId, String keyword, int clickType, String qpId, int requestCode, String projectName, String channelName, String from) {
        log(this.NOLOG ? null : "--startSearchResultPageForResult--context=" + context + "---channelId=" + channelId + "--keyword=" + keyword + "---clickType=" + clickType + "---qpId=" + qpId);
        if (context != null) {
            PageIOUtils.activityIn(context, initSearchResultIntent(context, channelId, keyword, clickType, qpId, projectName, channelName, from), requestCode);
        }
    }

    private Intent initSearchResultIntent(Context context, int channelId, String keyword, int clickType, String qpId, String projectName, String channelName, String from) {
        SearchIntentModel s = new SearchIntentModel();
        s.setKeyWord(keyword);
        s.setQpId(qpId);
        s.setClickType(clickType);
        AlbumIntentModel p = new AlbumIntentModel();
        p.setE(PingBackUtils.createEventId());
        p.setChannelId(channelId);
        p.setChannelName(channelName);
        p.setFrom(from);
        p.setSearchModel(s);
        p.setBuySource("3");
        p.setPageType(StringUtils.isEmpty((CharSequence) qpId) ? IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD : IAlbumConfig.UNIQUE_STAR_PAGE);
        if (TextUtils.isEmpty(projectName)) {
            projectName = IAlbumConfig.PROJECT_NAME_BASE_LINE;
        }
        p.setProjectName(projectName);
        Intent intent = getBaseIntent(context);
        if (!StringUtils.isEmpty((CharSequence) qpId)) {
            intent.setAction(ActionSet.ACT_STARS);
            if (TextUtils.equals("3", from)) {
                p.setBuySource("search");
            } else if (TextUtils.equals("detail", from)) {
                p.setBuySource("detail");
            } else {
                p.setBuySource("tab");
            }
        }
        intent.putExtra("intent_model", p);
        return intent;
    }

    public void startChannelPageOpenApi(Context context, AlbumOpenApiModel model) {
        log(this.NOLOG ? null : "--startChannelPageOpenApi--context=" + context + "---model=" + model);
        if (model != null) {
            AlbumIntentModel p = new AlbumIntentModel();
            p.setE(PingBackUtils.createEventId());
            p.setPageType(IAlbumConfig.CHANNEL_API_PAGE);
            p.setLoadLimitSize(model.getLoadLimitSize());
            p.setChannelId(model.getChannelId());
            p.setChannelName(model.getChannelName());
            p.setFrom(CHANNEL_STR + model.getChannelId() + SIGN_STR);
            p.setBuySource("openAPI");
            p.setProjectName(IAlbumConfig.PROJECT_NAME_OPEN_API);
            p.setDataTagId(model.getDataTagId());
            p.setDataTagName(model.getDataTagName());
            p.setDataTagType(model.getDataTagType());
            p.setLayoutKind(model.getLayoutKind());
            p.setNoLeftFragment(true);
            Intent intent = getBaseIntent(context);
            int intentFlag = model.getIntentFlag();
            if (intentFlag != -1) {
                intent.setFlags(intentFlag);
            }
            intent.putExtra("intent_model", p);
            PageIOUtils.activityIn(context, intent);
        }
    }

    public void startPlayhistoryActivity(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startPlayhistoryActivity--context=" + context);
        startPlayhistoryActivity(context, false, 2, intentFlags, null);
    }

    public void startPlayhistoryActivityOpenApi(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startPlayhistoryActivityOpenApi--context=" + context);
        startPlayhistoryActivity(context, false, 2, intentFlags, IAlbumConfig.PROJECT_NAME_OPEN_API);
    }

    public void startPlayhistoryAllvideoActivity(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startPlayhistoryAllvideoActivity--context=" + context);
        startPlayhistoryActivity(context, true, 0, intentFlags, null);
    }

    public void startPlayhistoryLongvideoActivity(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startPlayhistoryLongvideoActivity--context=" + context);
        startPlayhistoryActivity(context, true, 1, intentFlags, null);
    }

    private void startPlayhistoryActivity(Context context, boolean noLeftFragment, int location, int intentFlags, String projectName) {
        log(this.NOLOG ? null : "--startPlayhistoryActivity--context=" + context);
        if (context != null) {
            AlbumIntentModel p = new AlbumIntentModel();
            p.setPageType(IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY);
            if (TextUtils.isEmpty(projectName)) {
                projectName = IAlbumConfig.PROJECT_NAME_BASE_LINE;
            }
            p.setProjectName(projectName);
            p.setChannelId(-1);
            p.setChannelName(IFootConstant.STR_PLAYHISTORY);
            p.setNoLeftFragment(noLeftFragment);
            p.setLocation4Playhistory(location);
            Intent intent = new Intent(ActionSet.ACT_RECORD);
            intent.putExtra("intent_model", p);
            if (intentFlags != -1) {
                intent.setFlags(intentFlags);
            }
            PageIOUtils.activityIn(context, intent);
        }
    }

    public void startFavouriteActivity(Context context, int intentFlags, String projectName) {
        log(this.NOLOG ? null : "--startFavouriteActivity--context=" + context);
        if (context != null) {
            AlbumIntentModel p = new AlbumIntentModel();
            p.setPageType(IAlbumConfig.UNIQUE_FOOT_FAVOURITE);
            p.setChannelName(IFootConstant.STR_FAV);
            p.setChannelId(-1);
            p.setNoLeftFragment(false);
            if (TextUtils.isEmpty(projectName)) {
                projectName = IAlbumConfig.PROJECT_NAME_BASE_LINE;
            }
            p.setProjectName(projectName);
            Intent intent = new Intent(ActionSet.ACT_RECORD);
            intent.putExtra("intent_model", p);
            if (intentFlags != -1) {
                intent.setFlags(intentFlags);
            }
            PageIOUtils.activityIn(context, intent);
        }
    }

    public void startFavouriteActivityOpenApi(Context context, int intentFlags) {
        startFavouriteActivity(context, intentFlags, IAlbumConfig.PROJECT_NAME_OPEN_API);
    }

    public void startFootPlayhistory(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startFootPlayhistory--context=" + context);
        if (context != null) {
            startFootPage(context, IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY, intentFlags, null);
        }
    }

    public void startFootPlayhistoryOpenApi(Context context, int intentFlags) {
        log(this.NOLOG ? null : "--startFootPlayhistory--context=" + context);
        if (context != null) {
            startFootPage(context, IAlbumConfig.UNIQUE_FOOT_PLAYHISTORY, intentFlags, IAlbumConfig.PROJECT_NAME_OPEN_API);
        }
    }

    public void startFootFavourite(Context context) {
        log(this.NOLOG ? null : "--startFootFavourite--context=" + context);
        if (context != null) {
            startFootPage(context, IAlbumConfig.UNIQUE_FOOT_FAVOURITE, -1, null);
        }
    }

    public void startFootSubscribe(Context context) {
        startFootPage(context, IAlbumConfig.UNIQUE_FOOT_SUBSCRIBLE, -1, null);
    }

    private void startFootPage(Context context, String pageType, int intentFlag, String projectName) {
        AlbumIntentModel p = new AlbumIntentModel();
        p.setPageType(pageType);
        p.setChannelId(-1);
        p.setChannelName(AlbumInfoFactory.getChannelNameByPageType(pageType));
        if (TextUtils.isEmpty(projectName)) {
            projectName = IAlbumConfig.PROJECT_NAME_BASE_LINE;
        }
        p.setProjectName(projectName);
        Intent intent = new Intent(ActionSet.ACT_RECORD);
        if (intentFlag != -1) {
            intent.setFlags(intentFlag);
        }
        intent.putExtra("intent_model", p);
        PageIOUtils.activityIn(context, intent);
    }

    private Intent getBaseIntent(Context context) {
        if (GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore() != null) {
            GetInterfaceTools.getILogRecordProvider().getMsgHandlerCore().sendHostStatus(HOSTMODULE.EPG, HOSTSTATUS.START);
        }
        Intent i = new Intent(ActionSet.ACT_ALBUM);
        if (!(context instanceof Activity)) {
            i.addFlags(ClientDefaults.MAX_MSG_SIZE);
        }
        return i;
    }

    private void log(String str) {
        if (str != null) {
            LogUtils.e("EPG/AlbumEnterFactory", "qactivity/>>>> start  activity >>>" + str);
        }
    }
}
