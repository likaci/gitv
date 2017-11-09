package com.gala.video.app.epg.ui.imsg.mvpl;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.albumprovider.model.Tag;
import com.gala.video.albumlist4.widget.ListView;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.app.epg.QBaseFragment;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.widget.actionbar.ActionBarLayout;
import com.gala.video.app.epg.home.widget.actionbar.VipAnimationView;
import com.gala.video.app.epg.ui.imsg.IMsgCenterKeyEventListener;
import com.gala.video.app.epg.ui.imsg.MsgCenterActivity;
import com.gala.video.app.epg.ui.imsg.mvpl.MsgContract.Presenter;
import com.gala.video.app.epg.ui.imsg.mvpl.MsgContract.View;
import com.gala.video.app.epg.ui.imsg.mvpl.wrapper.ContentViewWrapper;
import com.gala.video.app.epg.ui.imsg.mvpl.wrapper.ErrorViewWrapper;
import com.gala.video.app.epg.ui.imsg.mvpl.wrapper.LabelViewWrapper;
import com.gala.video.app.epg.ui.imsg.mvpl.wrapper.LabelViewWrapper.OnLabelSwitchListener;
import com.gala.video.app.epg.ui.imsg.mvpl.wrapper.TopActionWrapper;
import com.gala.video.app.epg.ui.imsg.utils.MsgClickUtil;
import com.gala.video.app.epg.ui.imsg.widget.MessageCenterMenuView;
import com.gala.video.app.epg.utils.ActivityUtils;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class MsgFragment extends QBaseFragment implements IMsgCenterKeyEventListener, View {
    private static final int FIRST_POSITION = 0;
    private static final String LOG_TAG = "MsgFragment";
    private final String TOP_DESC = ResourceUtil.getStr(R.string.message_center_top_desc);
    private TextView mActionTip;
    private ContentViewWrapper mContentViewWrapper;
    private ErrorViewWrapper mErrorViewWrapper;
    private android.view.View mFocusView;
    private LabelViewWrapper mLabelViewWrapper;
    private ListView mLeftView;
    private TextView mMenuDescView;
    private MessageCenterMenuView mMenuView;
    private MsgPresenter mMsgPresenter;
    private OnFocusChangeListener mOnFocusChangeListener = new OnFocusChangeListener() {
        public void onFocusChange(android.view.View view, boolean hasFocus) {
            if (hasFocus && view != null && MsgFragment.this.mTopActionWrapper != null) {
                MsgFragment.this.mTopActionWrapper.setNextFocusDownViewForTopBar(view);
            }
        }
    };
    private VerticalGridView mRightView;
    private android.view.View mRootView;
    private ActionBarLayout mTopActionBar;
    private TopActionWrapper mTopActionWrapper;
    private ImageView mTopCuttingLine;
    private TextView mTopDescTextView;
    private TextView mTopTagTextView;
    private VipAnimationView mVipAnimationView;

    public void setPresenter(Presenter presenter) {
        LogUtils.i(LOG_TAG, "MsgFragment --- setPresenter, presenter = ", presenter);
        this.mMsgPresenter = (MsgPresenter) ActivityUtils.checkNotNull(presenter);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        LogUtils.i(LOG_TAG, "MsgFragment --- onAttach()");
        if (activity != null) {
            ((MsgCenterActivity) activity).registerKeyEventListener(this);
        }
    }

    public void onDetach() {
        super.onDetach();
        LogUtils.i(LOG_TAG, "MsgFragment --- onDetach()");
        if (getActivity() != null) {
            ((MsgCenterActivity) getActivity()).unRegisterKeyEventListener(this);
        }
    }

    public android.view.View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        android.view.View root = inflater.inflate(R.layout.epg_fragment_msgcenter, container, false);
        LogUtils.i(LOG_TAG, "MsgFragment --- onCreateView");
        this.mRootView = root;
        initViews();
        return root;
    }

    public void onStart() {
        super.onStart();
        LogUtils.i(LOG_TAG, "MsgFragment --- onStart");
        this.mMsgPresenter.start(this.mLabelViewWrapper.getLastLabelItemPos());
        this.mTopActionWrapper.updateTopActionUI();
        this.mTopActionWrapper.registerTipUpdateObserver();
        this.mTopActionWrapper.onStart();
    }

    public void onResume() {
        super.onResume();
        this.mTopActionWrapper.startVipAnimation(false);
    }

    public void onPause() {
        super.onPause();
        this.mTopActionWrapper.stopVipAnimation();
    }

    public void onStop() {
        super.onStop();
        LogUtils.i(LOG_TAG, "MsgFragment --- onStop");
        this.mTopActionWrapper.unRegisterTipUpdateObserver();
        this.mTopActionWrapper.removeAccountListener();
        this.mMsgPresenter.onStop();
    }

    public void onDestroy() {
        super.onDestroy();
        LogUtils.i(LOG_TAG, "MsgFragment --- onDestroy");
        this.mLabelViewWrapper.onDestroy();
        this.mContentViewWrapper.onDestroy();
    }

    private void initViews() {
        LogUtils.i(LOG_TAG, "MsgFragment --- initViews --- mMsgPresenter = ", this.mMsgPresenter);
        this.mRightView = (VerticalGridView) this.mRootView.findViewById(R.id.epg_msg_right_view_id);
        this.mLeftView = (ListView) this.mRootView.findViewById(R.id.epg_msg_left_view_id);
        this.mTopActionBar = (ActionBarLayout) this.mRootView.findViewById(R.id.epg_album_action_bar);
        this.mTopTagTextView = (TextView) this.mRootView.findViewById(R.id.epg_q_album_channel_name_txt);
        this.mTopDescTextView = (TextView) this.mRootView.findViewById(R.id.epg_q_album_tag_des);
        this.mTopCuttingLine = (ImageView) this.mRootView.findViewById(R.id.epg_q_album_tag_cutting_line);
        this.mMenuDescView = (TextView) this.mRootView.findViewById(R.id.epg_msg_menu_des_id);
        this.mMenuView = (MessageCenterMenuView) this.mRootView.findViewById(R.id.epg_msg_menu_view_id);
        this.mActionTip = (TextView) this.mRootView.findViewById(R.id.epg_album_actionbar_tip);
        this.mActionTip.setSelected(true);
        this.mVipAnimationView = (VipAnimationView) this.mRootView.findViewById(R.id.epg_vip_animation);
        this.mLabelViewWrapper = new LabelViewWrapper(this.mLeftView);
        this.mLabelViewWrapper.setOnLabelSwitchListener(new OnLabelSwitchListener() {
            public void onLabelSwitched(int newPosition) {
                MsgFragment.this.mContentViewWrapper.setFocusPosition(0);
                MsgFragment.this.mMsgPresenter.onLabelSwitch(newPosition);
            }
        });
        this.mLabelViewWrapper.setOnFocusChangeListener(this.mOnFocusChangeListener);
        this.mContentViewWrapper = new ContentViewWrapper(this.mRightView);
        this.mContentViewWrapper.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(ViewGroup parent, ViewHolder viewHolder) {
                if (viewHolder != null) {
                    MsgFragment.this.mMsgPresenter.onMsgClick(MsgFragment.this.mLabelViewWrapper.getLastLabelItemPos(), viewHolder.getLayoutPosition());
                    MsgFragment.this.mContentViewWrapper.updateMsgUI(viewHolder);
                }
            }
        });
        this.mContentViewWrapper.setOnFocusChangeListener(this.mOnFocusChangeListener);
        initTopView();
        initMenuDescView();
        initMenuView();
    }

    private void initMenuView() {
        this.mMenuView.setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                MsgFragment.this.mMsgPresenter.onMenuViewClick(MsgFragment.this.mLabelViewWrapper.getLastLabelItemPos());
                MsgFragment.this.mContentViewWrapper.updateAllMsgsUI();
                MsgFragment.this.hideMenuView();
            }
        });
    }

    private void initTopView() {
        this.mTopActionWrapper = new TopActionWrapper(getActivity(), this.mTopActionBar, this.mActionTip, this.mVipAnimationView);
        this.mTopCuttingLine.setVisibility(0);
        this.mTopDescTextView.setVisibility(0);
    }

    private void initMenuDescView() {
        this.mMenuDescView.setText(Html.fromHtml("<font color= '#" + ResourceUtil.getColorLength6(R.color.albumview_menu_color) + "'>按</font><font color='#" + ResourceUtil.getColorLength6(R.color.albumview_yellow_color) + "'>" + ResourceUtil.getStr(R.string.alter_menukey_text) + "</font><font color= '#" + ResourceUtil.getColorLength6(R.color.albumview_menu_color) + "'>整理消息</font>"));
    }

    public void jumpToPage(IMsgContent msgContent) {
        MsgClickUtil.jumpTo(getActivity(), msgContent);
    }

    private void showMenuView() {
        if (this.mMenuView != null && getActivity() != null) {
            this.mFocusView = getActivity().getWindow().getDecorView().findFocus();
            this.mMenuView.setVisibility(0);
            this.mMenuView.requestFocus();
        }
    }

    private void hideMenuView() {
        if (this.mMenuView != null) {
            if (this.mFocusView != null) {
                this.mFocusView.requestFocus();
            }
            this.mMenuView.setVisibility(4);
        }
    }

    public boolean onKeyEvent(int keyCode) {
        if (keyCode == 82) {
            if (isMenuViewVisible()) {
                hideMenuView();
                return true;
            } else if (!this.mContentViewWrapper.hasMsgData()) {
                return true;
            } else {
                showMenuView();
                return true;
            }
        } else if ((keyCode != 4 && keyCode != 111) || !isMenuViewVisible()) {
            return false;
        } else {
            hideMenuView();
            return true;
        }
    }

    private boolean isMenuViewVisible() {
        return this.mMenuView != null && this.mMenuView.isShown();
    }

    public void updateTopTagName(String tagName) {
        setViewText(this.mTopTagTextView, tagName);
    }

    public void updateUnreadMsgCount(int unreadMsgCount) {
        setViewText(this.mTopDescTextView, unreadMsgCount + this.TOP_DESC);
    }

    private void runOnUiThread(Runnable runnable) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(runnable);
        }
    }

    private void showMenuDescView() {
        setViewVisible(this.mMenuDescView, 0);
    }

    private void hideMenuDescView() {
        setViewVisible(this.mMenuDescView, 4);
    }

    private void showErrorView() {
        runOnUiThread(new Runnable() {
            public void run() {
                if (MsgFragment.this.mErrorViewWrapper == null) {
                    MsgFragment.this.mErrorViewWrapper = new ErrorViewWrapper(MsgFragment.this.mRootView);
                }
                MsgFragment.this.mErrorViewWrapper.setVisibility(0);
            }
        });
    }

    private void hideErrorView() {
        if (this.mErrorViewWrapper != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    MsgFragment.this.mErrorViewWrapper.setVisibility(4);
                }
            });
        }
    }

    private void setViewVisible(final android.view.View view, final int visible) {
        if (view != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    view.setVisibility(visible);
                }
            });
        }
    }

    private void setViewText(final TextView view, final String text) {
        if (view != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    view.setText(text);
                }
            });
        }
    }

    private void setLabelFocusLeaveForbidden(int direction) {
        this.mLabelViewWrapper.setFocusLeaveForbidden(direction);
    }

    public void showLabels(List<Tag> list) {
        this.mLabelViewWrapper.showLabels(list);
    }

    public void showMsgContentsAndMenuDesc(List<IMsgContent> list) {
        LogUtils.i(LOG_TAG, "showMsgContentsAndMenuDesc --- mContentViewWrapper.getMsgCount() = ", Integer.valueOf(this.mContentViewWrapper.getMsgCount()));
        LogUtils.i(LOG_TAG, "showMsgContentsAndMenuDesc --- ListUtils.getCount(list) = ", Integer.valueOf(ListUtils.getCount((List) list)));
        if (this.mContentViewWrapper.getMsgCount() < ListUtils.getCount((List) list)) {
            this.mContentViewWrapper.setFocusPosition(0);
        }
        this.mContentViewWrapper.showMsgs(list);
        if (ListUtils.isEmpty((List) list)) {
            showErrorView();
            hideMenuDescView();
            setLabelFocusLeaveForbidden(194);
            return;
        }
        hideErrorView();
        showMenuDescView();
        setLabelFocusLeaveForbidden(Service.CISCO_FNA);
    }
}
