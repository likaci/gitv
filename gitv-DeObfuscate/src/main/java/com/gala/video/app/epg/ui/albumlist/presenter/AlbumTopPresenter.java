package com.gala.video.app.epg.ui.albumlist.presenter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarLayout;
import com.gala.video.app.epg.home.widget.actionbar.VipAnimationView;
import com.gala.video.app.epg.ui.albumlist.adapter.AlbumTopAdapter;
import com.gala.video.app.epg.ui.albumlist.adapter.AlbumTopAdapter.onFocusChangedListener;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;

public class AlbumTopPresenter {
    private final ActionBarLayout mActionBar;
    private TextView mActionTip;
    private AlbumTopAdapter mAdapter;
    private Context mContext;
    private final VipAnimationView mVipAnimationView;
    private MyObserver mVipOperateTxtObserver = new C08681();

    class C08681 implements MyObserver {
        C08681() {
        }

        public void update(final String event) {
            AlbumTopPresenter.this.mActionTip.post(new Runnable() {
                public void run() {
                    LogUtils.m1568d("updateviptext", event);
                    AlbumTopPresenter.this.mAdapter.updateVipTips(AlbumTopPresenter.this.mActionTip);
                }
            });
        }
    }

    public AlbumTopPresenter(Context context, View root, AlbumInfoModel model) {
        this.mContext = context;
        this.mActionBar = (ActionBarLayout) root.findViewById(C0508R.id.epg_album_action_bar);
        this.mActionTip = (TextView) root.findViewById(C0508R.id.epg_album_actionbar_tip);
        this.mActionTip.setSelected(true);
        this.mVipAnimationView = (VipAnimationView) root.findViewById(C0508R.id.epg_vip_animation);
        this.mAdapter = new AlbumTopAdapter(this.mContext, model);
        this.mActionBar.setAdapter(this.mAdapter);
        this.mActionBar.setVisibility(0);
        this.mVipAnimationView.setActionBarAdpter(this.mAdapter);
    }

    public void setNextFocusDownId(int id) {
        this.mAdapter.setNextFocusDownId(id);
    }

    public void onMessageReceive(IMsgContent content) {
        this.mAdapter.updateMessageCountOnReceive(content);
    }

    public int getPreFocusUpId() {
        return this.mAdapter.getPreFocusId();
    }

    public void onStart() {
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT, this.mVipOperateTxtObserver);
        this.mAdapter.updateActionBar();
        this.mAdapter.updateVipTips(this.mActionTip);
        this.mAdapter.updateMessageCountOnStart();
        this.mAdapter.onStart();
    }

    public void onStop() {
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT, this.mVipOperateTxtObserver);
        this.mAdapter.onStop();
    }

    public void setFocusChangedListener(onFocusChangedListener listener) {
        if (this.mAdapter != null) {
            this.mAdapter.setOnFocusChangedListener(listener);
        }
    }

    public boolean hasFocus() {
        return this.mActionBar.hasFocus();
    }

    public void startVipAnimation(boolean delay) {
        this.mVipAnimationView.startAnimation(delay);
    }

    public void stopVipAnimation() {
        this.mVipAnimationView.stopAnimation();
    }
}
