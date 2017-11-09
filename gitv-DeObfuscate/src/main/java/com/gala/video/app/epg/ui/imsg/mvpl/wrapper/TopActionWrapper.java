package com.gala.video.app.epg.ui.imsg.mvpl.wrapper;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarLayout;
import com.gala.video.app.epg.home.widget.actionbar.VipAnimationView;
import com.gala.video.app.epg.ui.imsg.adapter.MsgTopAdapter;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;

public class TopActionWrapper {
    private ActionBarLayout mActionBarLayout;
    private TextView mActionTip;
    private Context mContext;
    private MsgTopAdapter mMsgTopAdapter;
    private VipAnimationView mVipAnimationView;
    private MyObserver mVipOperateTxtObserver = new C09221();

    class C09221 implements MyObserver {
        C09221() {
        }

        public void update(final String event) {
            TopActionWrapper.this.mActionTip.post(new Runnable() {
                public void run() {
                    LogUtils.m1568d("updateviptext", event);
                    TopActionWrapper.this.mMsgTopAdapter.updateVipTips(TopActionWrapper.this.mActionTip);
                }
            });
        }
    }

    public TopActionWrapper(Context context, ActionBarLayout actionBarLayout, TextView actionTip, VipAnimationView vipAnimationView) {
        this.mContext = context;
        this.mActionBarLayout = actionBarLayout;
        this.mActionTip = actionTip;
        this.mVipAnimationView = vipAnimationView;
        initView();
    }

    private void initView() {
        this.mMsgTopAdapter = new MsgTopAdapter(ActionBarDataFactory.buildActionBarMsgCenterData(), this.mContext);
        this.mActionBarLayout.setAdapter(this.mMsgTopAdapter);
        this.mActionBarLayout.setVisibility(0);
        this.mVipAnimationView.setActionBarAdpter(this.mMsgTopAdapter);
    }

    public void updateTopActionUI() {
        this.mMsgTopAdapter.updateActionBar();
        this.mMsgTopAdapter.updateVipTips(this.mActionTip);
    }

    public void registerTipUpdateObserver() {
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT, this.mVipOperateTxtObserver);
    }

    public void unRegisterTipUpdateObserver() {
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT, this.mVipOperateTxtObserver);
    }

    public void onStart() {
        this.mMsgTopAdapter.onStart();
    }

    public void removeAccountListener() {
        this.mMsgTopAdapter.onStop();
    }

    public void setNextFocusDownViewForTopBar(View view) {
        if (view != null) {
            view.setNextFocusUpId(this.mMsgTopAdapter.getPreFocusId());
            this.mMsgTopAdapter.setNextFocusDownId(view.getId());
        }
    }

    public void startVipAnimation(boolean delay) {
        this.mVipAnimationView.startAnimation(delay);
    }

    public void stopVipAnimation() {
        this.mVipAnimationView.stopAnimation();
    }
}
