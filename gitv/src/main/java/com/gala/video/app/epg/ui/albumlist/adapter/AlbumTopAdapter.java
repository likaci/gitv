package com.gala.video.app.epg.ui.albumlist.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.home.data.actionbar.ActionBarItemInfo;
import com.gala.video.app.epg.home.data.actionbar.ActionBarType;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarAdapter;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarPageType;
import com.gala.video.app.epg.ui.albumlist.constant.IFootConstant;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.app.epg.ui.search.SearchEnterUtils;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebIntentParams;
import com.gala.video.lib.share.pingback.PingBackCollectionFieldUtils;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import java.util.List;

public class AlbumTopAdapter extends ActionBarAdapter {
    private static final String TAG = "AlbumTopAdapter";
    private AlbumInfoModel mAlbumInfoModel;
    private onFocusChangedListener mOnFocusChangedListener;

    public interface onFocusChangedListener {
        void onFocusChanged(View view, boolean z);
    }

    public AlbumTopAdapter(List<ActionBarItemInfo> itemInfos, Context mContext) {
        super(itemInfos, mContext);
    }

    public AlbumTopAdapter(Context context, AlbumInfoModel model) {
        this(buildActionBarData(model), context);
        this.mAlbumInfoModel = model;
        this.from = "top_list";
        this.mActionBarPageType = ActionBarPageType.EPG_PAGE;
        this.entertype = 12;
        this.buy_from = "list_top";
        if (AlbumInfoFactory.isSearchResultPage(model.getPageType())) {
            this.mLeftTopActionBtnType = ActionBarType.MY;
        }
    }

    public void setOnFocusChangedListener(onFocusChangedListener listener) {
        this.mOnFocusChangedListener = listener;
    }

    public void onChildFocusChanged(View v, boolean hasFocus) {
        if (this.mOnFocusChangedListener != null) {
            this.mOnFocusChangedListener.onFocusChanged(v, hasFocus);
        }
        super.onChildFocusChanged(v, hasFocus);
    }

    public void onClickSearchBtn(String rseat, int position) {
        String channelName;
        if (TextUtils.equals(this.mAlbumInfoModel.getChannelName(), IFootConstant.STR_FILM_FOOT_PLAY)) {
            channelName = IFootConstant.STR_FILM_FOOT;
        } else {
            channelName = this.mAlbumInfoModel.getChannelName();
        }
        SearchEnterUtils.startSearchActivity(this.mContext, this.mAlbumInfoModel.getChannelId(), channelName, 67108864);
        clickTopPingback(rseat);
    }

    public void onClickMyBtn(String rseat, int position) {
        GetInterfaceTools.getLoginProvider().startLoginForAlbum(this.mContext, 67108864);
        clickTopPingback(rseat);
    }

    public void onClickVipBtn(String rseat, int position) {
        onVipBtnJump();
        clickTopPingback(rseat, this.copy);
    }

    public void onClickCheckInBtn(String rseat, int position) {
        onCheckInJump();
        clickTopPingback(rseat);
    }

    protected void onVipBtnJump() {
        CharSequence url = "";
        IDynamicResult dynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (dynamicResult != null) {
            url = dynamicResult.getHomeHeaderVipUrl();
        }
        if (GetInterfaceTools.getIGalaVipManager().needShowActivationPage()) {
            GetInterfaceTools.getLoginProvider().startActivateActivity(this.mContext, this.from, 7);
        } else if (StringUtils.isEmpty(url)) {
            LogUtils.w(TAG, "vip click url is empty");
            String incomeSrc = PingBackCollectionFieldUtils.getTabName() + "_" + PingBackCollectionFieldUtils.getTabIndex() + "_p_0_" + "vip_4";
            WebIntentParams params2 = new WebIntentParams();
            params2.incomesrc = incomeSrc;
            params2.from = this.from;
            params2.buySource = LoginConstant.S1_FROM_HOMEBAR;
            params2.pageType = 2;
            params2.enterType = this.entertype;
            params2.buyFrom = this.buy_from;
            GetInterfaceTools.getWebEntry().startPurchasePage(this.mContext, params2);
        } else {
            LogUtils.d(TAG, "vip click url = " + url);
            WebIntentParams params = new WebIntentParams();
            params.pageUrl = url;
            params.from = this.from;
            params.buyFrom = this.buy_from;
            GetInterfaceTools.getWebEntry().gotoCommonWebActivity(this.mContext, params);
        }
    }

    private void clickTopPingback(String rseat) {
        clickTopPingback(rseat, "");
    }

    private void clickTopPingback(String rseat, String copyStr) {
        PingBackParams params = new PingBackParams();
        params.add("r", rseat).add("block", "top").add("rt", "i").add("rseat", rseat).add(Keys.T, "20").add("rpage", AlbumInfoFactory.isSearchResultPage(this.mAlbumInfoModel.getPageType()) ? "搜索结果" : this.mAlbumInfoModel.getChannelName()).add("copy", copyStr);
        PingBack.getInstance().postPingBackToLongYuan(params.build());
    }

    private static List<ActionBarItemInfo> buildActionBarData(AlbumInfoModel model) {
        if (AlbumInfoFactory.isSearchResultPage(model.getPageType())) {
            return ActionBarDataFactory.buildActionBarSearchData();
        }
        return ActionBarDataFactory.buildActionBarData();
    }

    public void updateCheckInView() {
        setCheckInMessageVisible();
    }
}
