package com.gala.video.app.epg.uikit.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.home.data.actionbar.ActionBarDataFactory;
import com.gala.video.app.epg.ui.ucenter.UcenterActivity;
import com.gala.video.app.epg.uikit.contract.UCenterItemContract.Presenter;
import com.gala.video.lib.framework.core.pingback.PingBack;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifimpl.ucenter.account.utils.LoginConstant;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.pingback.PingBackParams;
import com.gala.video.lib.share.pingback.PingBackParams.Keys;
import com.gala.video.lib.share.uikit.view.IViewLifecycle;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.internal.net.SendFlag;

public class UCenterItemView extends RelativeLayout implements IViewLifecycle<Presenter> {
    private static final String LOG_TAG = "EPG/UCenter/UCenterView";
    private Button mBtnLogin;
    private Context mContext;
    private Handler mHandler;
    private ImageView mImgBg;
    private ImageView mImgEyes;
    private ImageView mImgHead;
    private ViewGroup mMainView;
    private Intent mParamsIntent;
    private Presenter mPresenter;
    private TextView mTxtName;
    private TextView mTxtStatus;
    private TextView mTxtTips;
    private TextView mTxtUid;

    public UCenterItemView(Context context) {
        this(context, null);
    }

    public UCenterItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UCenterItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mHandler = new Handler(Looper.getMainLooper());
        this.mContext = context;
        if (context instanceof Activity) {
            this.mParamsIntent = ((Activity) context).getIntent();
        }
        initView();
    }

    private void initView() {
        this.mMainView = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.epg_ucenter_head_view, this, true);
        this.mImgBg = (ImageView) this.mMainView.findViewById(R.id.epg_img_ucenter_head_bg);
        this.mBtnLogin = (Button) this.mMainView.findViewById(R.id.epg_btn_ucenter_login);
        this.mTxtTips = (TextView) this.mMainView.findViewById(R.id.epg_txt_ucenter_no_login_tip);
        this.mImgEyes = (ImageView) this.mMainView.findViewById(R.id.epg_img_ucenter_eye);
        this.mImgHead = (ImageView) this.mMainView.findViewById(R.id.epg_img_ucenter_head);
        this.mTxtName = (TextView) this.mMainView.findViewById(R.id.epg_txt_ucenter_uname);
        this.mTxtUid = (TextView) this.mMainView.findViewById(R.id.epg_txt_ucenter_uid);
        this.mTxtStatus = (TextView) this.mMainView.findViewById(R.id.epg_txt_ucenter_status);
        this.mBtnLogin.setNextFocusLeftId(this.mBtnLogin.getId());
        this.mBtnLogin.setNextFocusUpId(this.mBtnLogin.getId());
        this.mBtnLogin.setOnFocusChangeListener(new OnFocusChangeListener() {
            public void onFocusChange(View view, boolean hasFocus) {
                AnimationUtil.zoomAnimation(view, hasFocus, 1.07f, 200);
                if (hasFocus) {
                    UCenterItemView.this.startFlashAnimation();
                } else {
                    UCenterItemView.this.stopFlashAnimation();
                }
            }
        });
        this.mBtnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PingBackParams params = new PingBackParams();
                String s1 = "";
                String e = "";
                if (UCenterItemView.this.mParamsIntent != null) {
                    s1 = UCenterItemView.this.mParamsIntent.getStringExtra(LoginConstant.S1_TAB);
                    e = UCenterItemView.this.mParamsIntent.getStringExtra(LoginConstant.E_TAB);
                }
                params.add("rt", "i").add("r", ActionBarDataFactory.TOP_BAR_TIME_NAME_LOGIN).add("block", "account").add("rseat", ActionBarDataFactory.TOP_BAR_TIME_NAME_LOGIN).add("c1", "").add("rpage", "mine_guest").add("s1", s1).add("e", e).add(Keys.T, "20");
                PingBack.getInstance().postPingBackToLongYuan(params.build());
                LogUtils.d(UCenterItemView.LOG_TAG, ">>>>>pingback - click type - [s1,e]= ", s1, ", ", e);
                GetInterfaceTools.getLoginProvider().startLoginActivity(UCenterItemView.this.mContext, s1, 2);
            }
        });
    }

    public void onBind(Presenter object) {
        this.mPresenter = object;
        if (this.mPresenter.isLogin()) {
            initUserUI(this.mPresenter.isVip());
        } else {
            initLoginUI();
        }
    }

    public void onUnbind(Presenter object) {
        if (this.mContext instanceof UcenterActivity) {
            ((UcenterActivity) this.mContext).unBindAnimation();
        }
    }

    public void onShow(Presenter object) {
    }

    public void onHide(Presenter object) {
    }

    private void initUserUI(boolean isVip) {
        hideLoginUI();
        this.mMainView.setFocusable(false);
        if (this.mImgBg != null) {
            this.mImgBg.setImageResource(isVip ? R.drawable.epg_ucenter_vip_login_bg : R.drawable.epg_ucenter_login_bg);
        }
        this.mTxtName.setVisibility(0);
        this.mTxtUid.setVisibility(0);
        this.mTxtStatus.setVisibility(0);
        if (this.mPresenter != null) {
            int color = this.mPresenter.isVip() ? R.color.action_bar_vip_text_normal : R.color.detail_title_text_color_new;
            this.mTxtName.setTextColor(ResourceUtil.getColor(color));
            this.mTxtUid.setTextColor(ResourceUtil.getColor(color));
            this.mTxtStatus.setTextColor(ResourceUtil.getColor(color));
        }
        String name = this.mPresenter.getUserName();
        String uid = this.mPresenter.getUid();
        String status = this.mPresenter.getStatus();
        TextView textView = this.mTxtName;
        int i = R.string.mycenter_uname;
        Object[] objArr = new Object[1];
        if (StringUtils.isEmpty((CharSequence) name)) {
            name = "";
        }
        objArr[0] = name;
        textView.setText(ResourceUtil.getStr(i, objArr));
        textView = this.mTxtUid;
        i = R.string.mycenter_uid;
        objArr = new Object[1];
        if (StringUtils.isEmpty((CharSequence) uid)) {
            uid = "";
        }
        objArr[0] = uid;
        textView.setText(ResourceUtil.getStr(i, objArr));
        textView = this.mTxtStatus;
        i = R.string.mycenter_ustatus;
        objArr = new Object[1];
        if (StringUtils.isEmpty((CharSequence) status)) {
            status = "";
        }
        objArr[0] = status;
        textView.setText(ResourceUtil.getStr(i, objArr));
    }

    private void initLoginUI() {
        this.mMainView.setFocusable(true);
        this.mMainView.setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        hideVipUI();
        if (this.mImgBg != null) {
            this.mImgBg.setImageResource(R.drawable.epg_ucenter_no_login_bg);
        }
        this.mBtnLogin.setVisibility(0);
        this.mTxtTips.setVisibility(0);
        this.mImgEyes.setVisibility(0);
        this.mImgHead.setVisibility(0);
        String tips = this.mPresenter.getLoginTips();
        TextView textView = this.mTxtTips;
        if (StringUtils.isEmpty((CharSequence) tips)) {
            tips = "";
        }
        textView.setText(tips);
        if (isFocused()) {
            this.mBtnLogin.requestFocus();
        }
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                UCenterItemView.this.startEyesAnimation();
            }
        }, 100);
    }

    private void hideVipUI() {
        if (this.mTxtName != null && this.mTxtName.getVisibility() == 0) {
            this.mTxtName.setVisibility(4);
        }
        if (this.mTxtUid != null && this.mTxtUid.getVisibility() == 0) {
            this.mTxtUid.setVisibility(4);
        }
        if (this.mTxtStatus != null && this.mTxtStatus.getVisibility() == 0) {
            this.mTxtStatus.setVisibility(4);
        }
    }

    private void hideLoginUI() {
        if (this.mTxtTips != null && this.mTxtTips.getVisibility() == 0) {
            this.mTxtTips.setVisibility(4);
        }
        if (this.mBtnLogin != null && this.mBtnLogin.getVisibility() == 0) {
            this.mBtnLogin.setVisibility(4);
        }
        if (this.mImgEyes != null && this.mImgEyes.getVisibility() == 0) {
            this.mImgEyes.clearAnimation();
            this.mImgEyes.setVisibility(4);
        }
        if (this.mImgHead != null && this.mImgHead.getVisibility() == 0) {
            this.mImgHead.setVisibility(4);
        }
    }

    private void startEyesAnimation() {
        TranslateAnimation anim = new TranslateAnimation(1, 0.0f, 1, 0.0f, 1, 0.0f, 1, 1.2f);
        anim.setInterpolator(new AccelerateInterpolator());
        anim.setFillAfter(true);
        anim.setRepeatMode(1);
        anim.setDuration(500);
        anim.setAnimationListener(new AnimationListener() {
            public void onAnimationStart(Animation animation) {
            }

            public void onAnimationEnd(Animation animation) {
                if ((UCenterItemView.this.mContext instanceof UcenterActivity) && UCenterItemView.this.mBtnLogin.hasFocus()) {
                    ((UcenterActivity) UCenterItemView.this.mContext).startFlashAnimation();
                }
            }

            public void onAnimationRepeat(Animation animation) {
            }
        });
        this.mImgEyes.startAnimation(anim);
    }

    private void startFlashAnimation() {
        if (this.mContext instanceof UcenterActivity) {
            ((UcenterActivity) this.mContext).startFlashAnimation();
        }
    }

    private void stopFlashAnimation() {
        if (this.mContext instanceof UcenterActivity) {
            ((UcenterActivity) this.mContext).stopFlashAnimation();
        }
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.dispatchKeyEvent(event);
        }
        if (!(this.mContext == null || this.mBtnLogin == null)) {
            int keyCode = event.getKeyCode();
            if (keyCode == 19) {
                AnimationUtil.shakeAnimation(this.mContext, this.mBtnLogin, 33, 500, 3.0f, 4.0f);
            } else if (keyCode != 21) {
                return super.dispatchKeyEvent(event);
            } else {
                AnimationUtil.shakeAnimation(this.mContext, this.mBtnLogin, 17, 500, 3.0f, 4.0f);
            }
        }
        return super.dispatchKeyEvent(event);
    }
}
