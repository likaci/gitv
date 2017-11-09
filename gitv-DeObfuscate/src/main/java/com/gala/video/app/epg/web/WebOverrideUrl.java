package com.gala.video.app.epg.web;

import android.content.Context;
import android.content.Intent;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.ui.albumlist.model.AlbumIntentModel;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.lib.share.utils.PageIOUtils;
import com.mcto.ads.internal.common.JsonBundleConstants;
import com.push.mqttv3.internal.ClientDefaults;
import com.xcrash.crashreporter.utils.CrashConst;
import java.io.Serializable;

class WebOverrideUrl {
    private static final String TAG = "EPG/WebOverrideUrl";

    WebOverrideUrl() {
    }

    public void onClickWebURI(Context context, String uri) {
        if (!StringUtils.isEmpty((CharSequence) uri)) {
            Intent intent = new Intent();
            try {
                uri = uri.replace("androiduri://", "");
                int splitIndex = uri.indexOf("/");
                if (splitIndex > 0) {
                    CharSequence action = uri.substring(0, splitIndex);
                    String data = uri.substring(splitIndex + 1, uri.length());
                    LogUtils.m1568d(TAG, "onClickWebURI action:" + action + ", data:" + data);
                    if (!StringUtils.isEmpty(action)) {
                        intent.setAction(Project.getInstance().getBuild().getPackageName() + action);
                        JSONObject jo = JSON.parseObject(data);
                        if (jo != null) {
                            JSONArray paramArray = jo.getJSONArray(JsonBundleConstants.A71_TRACKING_PARAMS);
                            for (int i = 0; i < paramArray.size(); i++) {
                                JSONObject param = paramArray.getJSONObject(i);
                                LogUtils.m1568d(TAG, "onClickWebURI param:" + param.toJSONString());
                                if ("string".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), param.getString("value"));
                                } else if ("boolean".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), param.getBoolean("value"));
                                } else if ("int".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), param.getInteger("value"));
                                } else if ("long".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), param.getLong("value"));
                                } else if ("float".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), param.getFloat("value"));
                                } else if ("double".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), param.getDouble("value"));
                                } else if (CrashConst.KEY_ANR_DATE.equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), param.getDate("value"));
                                } else if ("album".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), (Serializable) DataUtils.parseToObject(param.getString("value"), Album.class));
                                } else if ("albumlist".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), DataUtils.parseToList(param.getString("value"), Album.class));
                                } else if ("channellable".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), (Serializable) DataUtils.parseToObject(param.getString("value"), ChannelLabel.class));
                                } else if ("albumintentmodel".equalsIgnoreCase(param.getString("type"))) {
                                    intent.putExtra(param.getString(Album.KEY), (Serializable) DataUtils.parseToObject(param.getString("value"), AlbumIntentModel.class));
                                }
                            }
                        }
                        intent.addFlags(ClientDefaults.MAX_MSG_SIZE);
                        PageIOUtils.activityIn(context, intent);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
