package com.gala.video.app.epg.web.type;

import android.content.Context;
import com.gala.video.app.epg.home.data.provider.DailyNewsProvider;
import com.gala.video.app.epg.web.model.WebBaseTypeParams;
import com.gala.video.lib.framework.core.pingback.PingBackUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.model.TabDataItem;
import com.gala.video.lib.share.common.model.player.NewsDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.NewsParams;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.DailyLabelModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.lib.share.network.NetworkStatePresenter;
import com.gala.video.lib.share.project.Project;
import java.util.ArrayList;
import java.util.List;

public class MoreDailyNewsType implements IWebBaseClickType {
    private static final String TAG = "EPG/web/MoreDailyNewsType";
    private Context mContext;

    public MoreDailyNewsType(Context context) {
        this.mContext = context;
    }

    public void onClick(WebBaseTypeParams params) {
        WebViewDataImpl webViewDataImpl = params.getWebViewDataImpl();
        String from = null;
        String buySource = null;
        if (webViewDataImpl != null) {
            from = webViewDataImpl.getFrom();
            buySource = webViewDataImpl.getBuySource();
        }
        List<TabDataItem> dataItems = new ArrayList();
        List<DailyLabelModel> mDailyNewsList = DailyNewsProvider.getInstance().getDailyNewModelList();
        if (mDailyNewsList != null && mDailyNewsList.size() > 0) {
            for (DailyLabelModel daily : mDailyNewsList) {
                TabDataItem tab = CreateInterfaceTools.createModelHelper().convertToTabDataItem(daily);
                if (tab != null) {
                    dataItems.add(tab);
                }
            }
        } else if (Project.getInstance().getBuild().isLitchi()) {
            NetworkStatePresenter.getInstance().handleNoData();
            LogUtils.e(TAG, "gotoMoreDailyNews() -> daily news no data");
            return;
        }
        if (dataItems != null && dataItems.size() > 0) {
            NewsDetailPlayParamBuilder builder = new NewsDetailPlayParamBuilder();
            builder.setBuySource(buySource).setFrom(from).setTabSource(PingBackUtils.getTabSrc());
            builder.setChannelName(GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel().getDailyName());
            builder.setNewParams(new NewsParams(dataItems, 0));
            GetInterfaceTools.getPlayerPageProvider().startNewsDetailPlayerPage(this.mContext, builder);
        }
    }
}
