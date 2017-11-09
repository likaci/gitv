package com.gala.video.app.epg.home.data.hdata.task;

import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.IVrsCallback;
import com.gala.tvapi.vrs.VrsHelper;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.tvapi.vrs.model.ResourceGroup;
import com.gala.tvapi.vrs.result.ApiResultGroupDetail;
import com.gala.tvapi.vrs.result.ApiResultRecommendListQipu;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.home.data.bus.HomeDataObservable;
import com.gala.video.app.epg.home.data.hdata.HomeDataType;
import com.gala.video.app.epg.home.data.pingback.HomePingbackFactory;
import com.gala.video.app.epg.home.data.provider.DailyNewsProvider;
import com.gala.video.app.epg.home.data.tool.DataBuildTool;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.WidgetChangeStatus;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.HomePingbackType.CommonPingback;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.pingback.IHomePingback;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.utils.Precondition;
import java.util.ArrayList;
import java.util.List;

public class DailyNewsRequestTask extends BaseRequestTask {
    private static String ID = "218858812";
    private static final int MAX_SIZE_ITEM = 10;
    private static final int MAX_SIZE_LABEL = 6;
    private static final String NO_RECOMMEND_DATA = "none";
    private static final String TAG = "home/DailyNewsRequestTask";
    private static String mTime = "";

    class C06321 implements IVrsCallback<ApiResultGroupDetail> {
        C06321() {
        }

        public void onSuccess(ApiResultGroupDetail result) {
            if (result != null && !result.latest && result.data != null) {
                List<ResourceGroup> groups = result.data.items;
                List<DailyLabelModel> labels = new ArrayList(6);
                if (groups != null && groups.size() > 0) {
                    int size = groups.size() > 6 ? 6 : groups.size();
                    for (int i = 0; i < size; i++) {
                        ResourceGroup group = (ResourceGroup) groups.get(i);
                        if (!(Precondition.isEmpty(group.id) || group.groupKvs == null || Precondition.isEmpty(group.groupKvs.card_name))) {
                            final DailyLabelModel label = new DailyLabelModel(group.id, group.groupKvs.card_name);
                            List<ChannelLabel> items = group.items;
                            if (items != null && items.size() > 0) {
                                List<Album> albums = new ArrayList();
                                for (ChannelLabel item : items) {
                                    if (item.getType() == ResourceType.VIDEO && !item.getVideo().isVipVideo() && DataBuildTool.checkRegionAvailable(item)) {
                                        Album album = item.getVideo();
                                        if (!Precondition.isNull(item.itemKvs)) {
                                            album.imageGif = item.itemKvs.imageGif;
                                        }
                                        albums.add(album);
                                    }
                                }
                                label.mDailyNewModelList = albums;
                            }
                            if (label.mDailyNewModelList == null) {
                                label.mDailyNewModelList = new ArrayList();
                            }
                            if (!(label.mDailyNewModelList.size() >= 10 || Precondition.isEmpty(group.groupKvs.area) || group.groupKvs.area.equals("none"))) {
                                VrsHelper.recommendListQipu.callSync(new IVrsCallback<ApiResultRecommendListQipu>() {
                                    public void onSuccess(ApiResultRecommendListQipu apiResultRecommendListQipu) {
                                        if (apiResultRecommendListQipu != null && !Precondition.isEmpty(apiResultRecommendListQipu.getAlbumList())) {
                                            int length = label.mDailyNewModelList.size();
                                            int size = 10 - length;
                                            List list = apiResultRecommendListQipu.getAlbumList();
                                            List<Album> cList = new ArrayList(label.mDailyNewModelList);
                                            int index = 0;
                                            if (!Precondition.isEmpty(list) && size > 0) {
                                                for (int i = 0; i < list.size(); i++) {
                                                    Album album = (Album) list.get(i);
                                                    if (!album.isVipVideo()) {
                                                        if (index != size) {
                                                            boolean isExisted = false;
                                                            for (int j = 0; j < length; j++) {
                                                                if (album.tvQid.equals(((Album) cList.get(j)).tvQid)) {
                                                                    isExisted = true;
                                                                    break;
                                                                }
                                                            }
                                                            if (!isExisted) {
                                                                album.tvPic = "";
                                                                label.mDailyNewModelList.add(album);
                                                                index++;
                                                            }
                                                        } else {
                                                            return;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }

                                    public void onException(ApiException e) {
                                        String str;
                                        LogUtils.m1571e(DailyNewsRequestTask.TAG, "Fetch Daily New Items failed-recommend");
                                        IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
                                        String str2 = "pfec";
                                        if (e == null) {
                                            str = "";
                                        } else {
                                            str = e.getCode();
                                        }
                                        addItem = addItem.addItem(str2, str);
                                        str2 = Keys.ERRURL;
                                        if (e == null) {
                                            str = "";
                                        } else {
                                            str = e.getUrl();
                                        }
                                        addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "recommendListQipu-DailyNews");
                                        str2 = Keys.ERRDETAIL;
                                        if (e == null) {
                                            str = "";
                                        } else {
                                            str = e.getMessage();
                                        }
                                        addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.CRASHTYPE, "").addItem(Keys.f2035T, "0").setOthersNull().post();
                                    }
                                }, UserUtil.getLoginUserId(), UserUtil.getLogoutUserId(), String.valueOf(10), "-4", group.groupKvs.area, "1");
                            }
                            if (!(Precondition.isEmpty(group.groupKvs.area) || group.groupKvs.area.equals("none"))) {
                                label.setArea(group.groupKvs.area);
                            }
                            labels.add(label);
                            if (!Precondition.isEmpty(label.mDailyNewModelList)) {
                                LogUtils.m1568d(DailyNewsRequestTask.TAG, "Daily Label " + label.mLabelName + ", Daily Items Size " + label.mDailyNewModelList.size() + "-recommend");
                            }
                        }
                    }
                }
                DailyNewsProvider.getInstance().writeDailyNewModelListToCache(labels);
                if (labels.size() > 0) {
                    DailyNewsRequestTask.mTime = result.timestamp;
                } else {
                    DailyNewsRequestTask.mTime = "";
                }
            }
        }

        public void onException(ApiException e) {
            String str;
            LogUtils.m1571e(DailyNewsRequestTask.TAG, "Fetch Daily New Items failed-groupdetail");
            DailyNewsRequestTask.mTime = "";
            IHomePingback addItem = HomePingbackFactory.instance().createPingback(CommonPingback.DATA_ERROR_PINGBACK).addItem("ec", "315008");
            String str2 = "pfec";
            if (e == null) {
                str = "";
            } else {
                str = e.getCode();
            }
            addItem = addItem.addItem(str2, str);
            str2 = Keys.ERRURL;
            if (e == null) {
                str = "";
            } else {
                str = e.getUrl();
            }
            addItem = addItem.addItem(str2, str).addItem(Keys.APINAME, "groupdetail-DailyNews");
            str2 = Keys.ERRDETAIL;
            if (e == null) {
                str = "";
            } else {
                str = e.getMessage();
            }
            addItem.addItem(str2, str).addItem("activity", "HomeActivity").addItem(Keys.CRASHTYPE, "").addItem(Keys.f2035T, "0").setOthersNull().post();
        }
    }

    public String identifier() {
        return getClass().getName();
    }

    public void invoke() {
        fetchDailyNewsData();
    }

    private void fetchDailyNewsData() {
        List<DailyLabelModel> list = DailyNewsProvider.getInstance().getDailyNewModelList();
        if (list == null || list.size() == 0) {
            mTime = "";
        }
        VrsHelper.groupDetail.callSync(new C06321(), ID, mTime);
    }

    public void onOneTaskFinished() {
        HomeDataObservable.getInstance().post(HomeDataType.DAILY_INFO, WidgetChangeStatus.DataChange, null);
    }
}
