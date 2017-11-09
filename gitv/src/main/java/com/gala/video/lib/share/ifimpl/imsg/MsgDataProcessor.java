package com.gala.video.lib.share.ifimpl.imsg;

import android.content.Context;
import android.util.Log;
import com.alibaba.fastjson.JSON;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.imsg.model.IMsg;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.utils.Precondition;

class MsgDataProcessor {
    MsgDataProcessor() {
    }

    IMsgContent checkMsg(Context context, int appId, String msg) {
        IMsgContent returnMsg = null;
        ReceiveMsgPingbackSender mPingbackSender = new ReceiveMsgPingbackSender();
        try {
            if (appId == IMsgUtils.sAppId) {
                IMsg imsg = (IMsg) JSON.parseObject(msg, IMsg.class);
                if (!(imsg == null || imsg.content == null || imsg.content.length() <= 0)) {
                    IMsgContent content = (IMsgContent) JSON.parseObject(imsg.content, IMsgContent.class);
                    if (!Precondition.isNull(content)) {
                        mPingbackSender.setContent(content);
                        if (imsg.type == 39 || (IMsgUtils.isSupportSubscribe && imsg.type == 60)) {
                            String title = content.msg_title;
                            if (!(title == null || title.length() == 0)) {
                                if (isVersionSupport(content)) {
                                    Log.d(IMsgUtils.TAG, "isVersionSupport=true, IMsgUtils.isShowDialog = " + IMsgUtils.isShowDialog());
                                    if (!isShowDialog(content) || IMsgUtils.isShowDialog()) {
                                        mPingbackSender.isShow = "1";
                                    } else {
                                        mPingbackSender.isShow = "2";
                                    }
                                    if (imsg.type == 60) {
                                        mPingbackSender.content = content.content;
                                    }
                                    content.isRead = false;
                                    switch (content.page_jumping) {
                                        case 3:
                                        case 4:
                                            if (StringUtils.hasEmpty(content.related_aids, content.related_vids)) {
                                                return null;
                                            }
                                            break;
                                    }
                                    returnMsg = pushMsgToApp(context, content);
                                } else {
                                    Log.d(IMsgUtils.TAG, "nonsupport, appver = " + IMsgUtils.sAppVersion + ", minversion = " + content.min_support_version);
                                }
                            }
                        } else if (imsg.type == 61) {
                            new IMsgBroadcast().sendBroadcast(61, content);
                        } else {
                            Log.e(IMsgUtils.TAG, "unknow msg, type = " + imsg.type);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mPingbackSender.sendPingback();
        return returnMsg;
    }

    IMsgContent pushMsgToApp(Context context, IMsgContent msg) {
        IMsgContent returnMsg = null;
        if (msg.valid_till < TVApiBase.getTVApiProperty().getCurrentTime() && msg.valid_till != 0) {
            Log.d(IMsgUtils.TAG, "msg out of time");
            return null;
        } else if (GetInterfaceTools.getMsgCenter().isMsgExist(msg)) {
            Log.d(IMsgUtils.TAG, "msg is exist");
            return null;
        } else {
            Log.d(IMsgUtils.TAG, "msg isn't exist");
            boolean isShowDialog = isShowDialog(msg) && IMsgUtils.isShowDialog();
            msg.isShowDialog = isShowDialog;
            if (msg.msg_level == 6) {
                return msg;
            }
            GetInterfaceTools.getMsgCenter().insertIMsg(msg);
            if (isShowDialog) {
                returnMsg = msg;
            }
            if (IMsgUtils.isAppLive(context)) {
                new IMsgBroadcast().sendBroadcast(39, msg);
            }
            return returnMsg;
        }
    }

    private boolean isShowDialog(IMsgContent msg) {
        if (msg.msg_level == 1 || msg.msg_level == 2) {
            return true;
        }
        if ((msg.msg_level == 5 && msg.msg_type == 3) || msg.msg_level == 6) {
            return true;
        }
        return false;
    }

    private boolean isVersionSupport(IMsgContent content) {
        if (Precondition.isEmpty(content.min_support_version)) {
            return true;
        }
        if (!Precondition.isEmpty(IMsgUtils.sAppVersion)) {
            String[] apv = IMsgUtils.sAppVersion.split("\\.");
            String[] mpv = content.min_support_version.split("\\.");
            if (!Precondition.isEmpty(apv) && apv.length >= 2 && !Precondition.isEmpty(mpv) && mpv.length >= 2) {
                int ap0 = Integer.parseInt(apv[0]);
                int ap1 = Integer.parseInt(apv[1]);
                int mp0 = Integer.parseInt(mpv[0]);
                int mp1 = Integer.parseInt(mpv[1]);
                if (ap0 > mp0) {
                    return true;
                }
                if (ap0 == mp0 && ap1 >= mp1) {
                    return true;
                }
            }
        }
        return false;
    }
}
