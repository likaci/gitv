package com.gala.video.lib.share.uikit.view;

import android.content.Context;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.LinearLayout.LayoutParams;
import com.gala.video.lib.share.common.widget.CardFocusHelper;
import com.gala.video.lib.share.uikit.contract.StandardItemContract;
import com.gala.video.lib.share.uikit.contract.SubscribeItemContract.Presenter;
import com.gala.video.lib.share.uikit.contract.SubscribeItemContract.View;
import com.gala.video.lib.share.uikit.data.ItemInfoModel;
import com.gala.video.lib.share.uikit.view.widget.SubscribeItemLayout;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SubscribeItemView extends SubscribeItemLayout implements IViewLifecycle<Presenter>, View {
    private static final String TAG = "SubscribeItemView";
    private SubscribeBtnItemView mButtonView;
    private Presenter mPresenter;
    private StandardItemView mTitleInView;

    public SubscribeItemView(Context context) {
        super(context);
        this.mTitleInView = new StandardItemView(context);
        this.mButtonView = new SubscribeBtnItemView(context);
        addView(this.mTitleInView);
        addView(this.mButtonView);
        initFocusListener(this.mTitleInView);
        initFocusListener(this.mButtonView);
        initItemClickListener(this.mTitleInView);
        initBtnClickListener(this.mButtonView);
    }

    private void initFocusListener(android.view.View v) {
        final OnFocusChangeListener listener = v.getOnFocusChangeListener();
        v.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(android.view.View v, boolean hasFocus) {
                if (listener != null) {
                    listener.onFocusChange(v, hasFocus);
                }
                if (hasFocus) {
                    v.bringToFront();
                }
                AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200, true);
                CardFocusHelper mgr = CardFocusHelper.getMgr(SubscribeItemView.this.getContext());
                if (hasFocus) {
                    if (mgr != null) {
                        mgr.viewGotFocus(v);
                    }
                } else if (mgr != null) {
                    mgr.viewLostFocus(v);
                }
            }
        });
    }

    public void onBind(Presenter object) {
        ItemInfoModel model = object.getModel();
        this.mPresenter = object;
        this.mTitleInView.onBind((StandardItemContract.Presenter) object);
        this.mButtonView.onBind(object);
        object.setView(this);
        LayoutParams titleInViewLayoutParams = (LayoutParams) this.mTitleInView.getLayoutParams();
        titleInViewLayoutParams.height = model.getHeight() - ResourceUtil.getPx(96);
        titleInViewLayoutParams.width = model.getWidth();
        LayoutParams buttonLayoutParams = (LayoutParams) this.mButtonView.getLayoutParams();
        buttonLayoutParams.topMargin = ResourceUtil.getPx(36);
        buttonLayoutParams.height = ResourceUtil.getPx(60);
        buttonLayoutParams.width = ResourceUtil.getPx(252);
    }

    public void onUnbind(Presenter object) {
        this.mTitleInView.onUnbind((StandardItemContract.Presenter) object);
        this.mButtonView.onUnbind(object);
    }

    public void onShow(Presenter object) {
        this.mTitleInView.onShow((StandardItemContract.Presenter) object);
        this.mButtonView.onShow(object);
    }

    public void onHide(Presenter object) {
        this.mTitleInView.onHide((StandardItemContract.Presenter) object);
        this.mButtonView.onHide(object);
    }

    public void updateBtn(int subscribeType) {
        this.mButtonView.updateState(subscribeType);
    }

    private void initItemClickListener(android.view.View coreView) {
        coreView.setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                ((android.view.View) v.getParent()).performClick();
            }
        });
    }

    private void initBtnClickListener(android.view.View btnView) {
        btnView.setOnClickListener(new OnClickListener() {
            public void onClick(android.view.View v) {
                SubscribeItemView.this.mPresenter.onBtnClick(SubscribeItemView.this.mTitleInView.getContentDescription(), SubscribeItemView.this.getContext());
            }
        });
    }
}
