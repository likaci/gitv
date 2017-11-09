package com.gala.video.app.epg.ui.multisubject.uikit.action;

import android.content.Context;
import com.gala.video.albumlist4.widget.RecyclerView;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.home.data.ItemData;
import com.gala.video.lib.share.ifimpl.multisubject.MultiSubjectVGridView;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.CardModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.HomeModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.IMultiSubjectInfoModel;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.multisubject.action.DummyActionListener;

public class MultiSubjectAction extends DummyActionListener {
    private MultiSubjectVGridView mGridView;
    private IMultiSubjectInfoModel mInfoModel;

    public MultiSubjectAction(MultiSubjectVGridView mGridView, IMultiSubjectInfoModel infoModel) {
        this.mGridView = mGridView;
        this.mInfoModel = infoModel;
    }

    public boolean onVerticalScrollCloselyTop(int pos) {
        return true;
    }

    public void onVerticalScrollStart() {
        this.mGridView.setExtraPadding(100);
    }

    public void onHorizontalItemClick(Context mContext, RecyclerView hGridView, ViewHolder viewHolder, CardModel cardModel) {
        CreateInterfaceTools.createMultiSubjectUtils().OnItemClick(mContext, (ItemData) CreateInterfaceTools.createModelHelper().convertToDataSource((HomeModel) cardModel.getItemModelList().get(viewHolder.getLayoutPosition())), this.mInfoModel);
    }
}
