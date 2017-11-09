package com.gala.video.app.epg.ui.multisubject.uikit.pingback;

import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.app.epg.home.data.pingback.HomePingbackDataUtils;
import com.gala.video.app.epg.ui.multisubject.util.MultiSubjectPingbackUtils;
import com.gala.video.app.epg.ui.multisubject.util.MultiSubjectPingbackUtils.CardShowPingbackModel;
import com.gala.video.app.epg.ui.multisubject.util.MultiSubjectPingbackUtils.PageClickPingbackModel;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.ItemDataType;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.IPingbackListener;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.pingback.MultiSubjectPingBackModel;

public class MultiSubjectPingBack implements IPingbackListener {
    private static final String LOG_TAG = "MultiSubjectPingBack";
    private IMultiSubjectInfoModel mInfoModel = null;

    public MultiSubjectPingBack(IMultiSubjectInfoModel infoModel) {
        this.mInfoModel = (IMultiSubjectInfoModel) ActivityUtils.checkNotNull(infoModel);
    }

    public void sendItemClickPingback(ViewHolder viewHolder, CardModel cardModel, int posInV, MultiSubjectPingBackModel pingbackModel) {
        int pos = viewHolder.getLayoutPosition();
        ItemData itemData = (ItemData) CreateInterfaceTools.createModelHelper().convertToDataSource((HomeModel) cardModel.getItemModelList().get(pos));
        PageClickPingbackModel model = new PageClickPingbackModel();
        model.allitem = String.valueOf(pingbackModel.allitem);
        model.block = cardModel.getId();
        model.dftitem = String.valueOf(pingbackModel.dftitem);
        model.e = this.mInfoModel.getE();
        model.line = String.valueOf(pingbackModel.line);
        model.plid = this.mInfoModel.getItemId();
        model.r = getQpId(itemData);
        model.rseat = String.valueOf(pos + 1);
        model.s2 = this.mInfoModel.getFrom();
        MultiSubjectPingbackUtils.sendPageClickPingback(model);
    }

    public void sendCardShowPingback(int posInV, CardModel cardModel, int focusPosition, MultiSubjectPingBackModel pingbackModel) {
        if (cardModel != null && pingbackModel != null) {
            CardShowPingbackModel model = new CardShowPingbackModel();
            model.allitem = String.valueOf(pingbackModel.allitem);
            model.line = String.valueOf(pingbackModel.line);
            model.sawitem = String.valueOf(pingbackModel.sawitem);
            model.dftitem = String.valueOf(pingbackModel.dftitem);
            model.block = cardModel.getId();
            model.qpid = model.block;
            model.e = this.mInfoModel.getE();
            model.s1 = this.mInfoModel.getBuysource();
            model.s2 = this.mInfoModel.getFrom();
            model.plid = this.mInfoModel.getItemId();
            MultiSubjectPingbackUtils.sendCardShowPingback(model);
        }
    }

    private String getQpId(ItemData itemData) {
        String qpId = "";
        if (itemData == null) {
            return qpId;
        }
        ItemDataType itemDataType = itemData.getItemType();
        if (itemDataType == null) {
            LogUtils.e(LOG_TAG, "getQpId itemDataType = null");
            return "";
        }
        switch (itemDataType) {
            case PLAY_LIST:
                qpId = HomePingbackDataUtils.getPlayListQipuId(itemData);
                break;
            case ALBUM:
            case VIDEO:
                qpId = HomePingbackDataUtils.getQipuId(itemData);
                break;
        }
        return qpId;
    }
}
