package com.gala.video.app.epg.home.presenter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.home.data.actionbar.ActionBarType;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarAdapter;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarItemView;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarLayout;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher.IMessageNotification;
import com.gala.video.app.epg.home.widget.actionbar.MessagePromptDispatcher.IMessageUpdating;
import com.gala.video.app.epg.home.widget.actionbar.VipAnimationView;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.AppClientUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus;
import com.gala.video.lib.share.ifmanager.bussnessIF.databus.IDataBus.MyObserver;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.utils.ResourceUtil;

public class ActionBarPresenter {
    private static final String TAG = "ActionBarPresenter";
    private String copy = "NA";
    private final ActionBarLayout mActionBar;
    private TextView mActionTip;
    private ActionBarAdapter mAdapter;
    private Context mContext;
    private MyObserver mDynamicRequestFinish = new MyObserver() {
        public void update(final String event) {
            ActionBarPresenter.this.mHandler.post(new Runnable() {
                public void run() {
                    ActionBarPresenter.this.resetActionBar();
                    LogUtils.d("updateviptext", event);
                    ActionBarPresenter.this.mAdapter.updateVipTips(ActionBarPresenter.this.mActionTip);
                }
            });
        }
    };
    private final Handler mHandler = new Handler(Looper.myLooper());
    private IMessageNotification mMessageNotification = new IMessageNotification() {
        public void onMessageReceive(IMsgContent content) {
            ActionBarPresenter.this.mAdapter.updateMessageCountOnReceive(content);
        }
    };
    private IMessageUpdating mMessageUpdating = new IMessageUpdating() {
        public void onMessageUpdate() {
            ActionBarPresenter.this.mAdapter.onMessageUpdate();
        }
    };
    private final VipAnimationView mVipAnimationView;

    public ActionBarPresenter(Context context, View root) {
        this.mContext = context;
        this.mActionBar = (ActionBarLayout) root.findViewById(R.id.epg_action_bar);
        this.mActionTip = (TextView) root.findViewById(R.id.epg_actionbar_tip);
        this.mActionTip.setSelected(true);
        this.mVipAnimationView = (VipAnimationView) root.findViewById(R.id.epg_vip_animation);
        this.mAdapter = new ActionBarAdapter(ActionBarDataFactory.buildActionBarData(), this.mContext);
        this.mActionBar.setAdapter(this.mAdapter);
        this.mVipAnimationView.setActionBarAdpter(this.mAdapter);
    }

    public void setNextFocusDownId(int id) {
        this.mAdapter.setNextFocusDownId(id);
    }

    public int getPreFocusUpId() {
        return this.mAdapter.getPreFocusId();
    }

    public void onStart() {
        MessagePromptDispatcher.get().register(this.mMessageNotification);
        MessagePromptDispatcher.get().register(this.mMessageUpdating);
        GetInterfaceTools.getDataBus().registerSubscriber(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT, this.mDynamicRequestFinish);
        this.mAdapter.updateActionBar();
        this.mAdapter.updateVipTips(this.mActionTip);
        this.mAdapter.updateMessageCountOnStart();
        this.mAdapter.onStart();
    }

    public void onStop() {
        MessagePromptDispatcher.get().unregister(this.mMessageNotification);
        MessagePromptDispatcher.get().unregister(this.mMessageUpdating);
        GetInterfaceTools.getDataBus().unRegisterSubscriber(IDataBus.DYNAMIC_REQUEST_FINISHED_EVENT, this.mDynamicRequestFinish);
        this.mAdapter.onStop();
    }

    public int getLastViewId() {
        return this.mAdapter.getLastViewId();
    }

    private void resetActionBar() {
        boolean isResetActionBar = this.mAdapter.isResetActionBar();
        LogUtils.i(TAG, "resetActionBar isResetActionBar:" + isResetActionBar);
        if (isResetActionBar) {
            if (this.mVipAnimationView != null) {
                stopVipAnimation();
            }
            this.mAdapter.resetData();
            this.mActionBar.resetAdapter();
        }
    }

    public void updateCheckInView() {
        this.mAdapter.updateCheckInView();
    }

    public void updateVipViewText() {
        this.mAdapter.updateVipViewText();
    }

    public void setActionBarResource(RelativeLayout rl) {
        ImageView divider = (ImageView) rl.findViewById(R.id.epg_divider);
        ImageView logoId = (ImageView) rl.findViewById(R.id.epg_logo_id);
        ((TextView) rl.findViewById(R.id.epg_time)).setTextColor(this.mContext.getResources().getColor(R.color.home_state_bar_time));
        AppClientUtils.setBackgroundDrawable(divider, this.mContext.getResources().getDrawable(R.color.home_state_bar_divider));
        logoId.setImageResource(R.drawable.share_gitv);
        for (int i = 0; i < this.mActionBar.getChildCount(); i++) {
            ActionBarItemView itemView = (ActionBarItemView) this.mActionBar.getChildAt(i);
            ActionBarType type = (ActionBarType) itemView.getTag();
            AppClientUtils.setBackgroundDrawable(itemView, this.mContext.getResources().getDrawable(R.drawable.epg_action_bar_bg_unfocused));
            if (type == ActionBarType.SEARCH) {
                itemView.setTextColor(this.mContext.getResources().getColor(R.color.action_bar_text_normal));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(R.drawable.epg_action_bar_search_default));
            }
            if (type == ActionBarType.MY) {
                itemView.setTextColor(this.mContext.getResources().getColor(R.color.action_bar_text_normal));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(R.drawable.epg_action_bar_my_default));
            }
            if (type == ActionBarType.CHECKIN) {
                itemView.setTextColor(ResourceUtil.getColor(R.color.action_bar_text_normal));
                itemView.setIconDrawable(ResourceUtil.getDrawable(R.drawable.epg_action_bar_checkin_default));
            }
            if (type == ActionBarType.VIP) {
                itemView.setTextColor(this.mContext.getResources().getColor(R.color.action_bar_vip_text_normal));
                itemView.setIconDrawable(this.mContext.getResources().getDrawable(R.drawable.epg_action_bar_vip_default));
            }
        }
    }

    public void update() {
        this.mAdapter.updateActionBar();
    }

    public void startVipAnimation(boolean delay) {
        this.mVipAnimationView.startAnimation(delay);
    }

    public void stopVipAnimation() {
        this.mVipAnimationView.stopAnimation();
    }

    public void setLastFocusRightViewId(int id) {
        if (this.mActionBar != null) {
            this.mActionBar.setLastFocusRightViewId(id);
        }
    }

    public void setLastFocusHimself() {
        if (this.mActionBar != null) {
            this.mActionBar.setLastFocusHimself();
        }
    }
}
