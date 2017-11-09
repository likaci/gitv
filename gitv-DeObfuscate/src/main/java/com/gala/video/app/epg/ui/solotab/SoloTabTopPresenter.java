package com.gala.video.app.epg.ui.solotab;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarLayout;
import com.gala.video.app.epg.home.widget.actionbar.VipAnimationView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SoloTabTopPresenter {
    private final ActionBarLayout mActionBar;
    private TextView mActionTip;
    private SoloTabTopAdapter mAdapter;
    private Context mContext;
    private final SoloTabInfoModel mInfoModel;
    private final SoloTabManage mSoloTabManage;
    private final VipAnimationView mVipAnimationView;
    private MyObserver mVipOperateTxtObserver = new C10791();

    class C10791 implements MyObserver {
        C10791() {
        }

        public void update(final String event) {
            SoloTabTopPresenter.this.mActionTip.post(new Runnable() {
                public void run() {
                    LogUtils.m1568d("updateviptext", event);
                    SoloTabTopPresenter.this.mAdapter.updateVipTips(SoloTabTopPresenter.this.mActionTip);
                }
            });
        }
    }

    public SoloTabTopPresenter(Context context, View root, SoloTabInfoModel infoModel, SoloTabManage soloTabManage) {
        this.mContext = context;
        this.mInfoModel = infoModel;
        this.mSoloTabManage = soloTabManage;
        this.mActionBar = (ActionBarLayout) root.findViewById(C0508R.id.epg_solotab_action_bar);
        this.mActionTip = (TextView) root.findViewById(C0508R.id.epg_solotab_actionbar_tip);
        this.mActionTip.setSelected(true);
        this.mVipAnimationView = (VipAnimationView) root.findViewById(C0508R.id.epg_vip_animation);
        ((TextView) root.findViewById(C0508R.id.epg_q_solotab_channel_name_txt)).setText(infoModel.getTabName());
        root.findViewById(C0508R.id.epg_solotab_bar_line).setBackgroundDrawable(ResourceUtil.getDrawable(this.mInfoModel.isVip() ? C0508R.drawable.epg_tab_bar_decorated_vip_line : C0508R.drawable.epg_tab_bar_decorated_line));
        this.mAdapter = new SoloTabTopAdapter(ActionBarDataFactory.buildActionBarData(), this.mContext, this.mInfoModel, this.mSoloTabManage);
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
