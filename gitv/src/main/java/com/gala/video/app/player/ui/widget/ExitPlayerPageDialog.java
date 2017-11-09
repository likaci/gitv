package com.gala.video.app.player.ui.widget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.FocusFinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.pingback.IPingback;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.PingbackFactory;
import com.gala.pingback.PingbackStore;
import com.gala.pingback.PingbackStore.BLOCK;
import com.gala.pingback.PingbackStore.C1;
import com.gala.pingback.PingbackStore.PAGE_CLICK.RTTYPE;
import com.gala.pingback.PingbackStore.PAGE_SHOW.BTSPTYPE;
import com.gala.pingback.PingbackStore.RSEAT;
import com.gala.sdk.player.PlayParams;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideoItemFactory;
import com.gala.tvapi.tv2.model.Album;
import com.gala.video.api.ApiException;
import com.gala.video.app.player.R;
import com.gala.video.app.player.config.PlayerAppConfig;
import com.gala.video.app.player.data.task.FetchExitAlbumsTask;
import com.gala.video.app.player.data.task.FetchExitAlbumsTask.ITaskResultListener;
import com.gala.video.app.player.ui.config.style.common.IGalleryUIStyle;
import com.gala.video.app.player.ui.overlay.UiHelper;
import com.gala.video.app.player.ui.overlay.contents.GalleryListContent;
import com.gala.video.app.player.ui.overlay.contents.IContent.IItemListener;
import com.gala.video.app.player.utils.MyPlayerProfile;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.IntentConfig2;
import com.gala.video.lib.share.common.model.player.AlbumDetailPlayParamBuilder;
import com.gala.video.lib.share.common.model.player.BasePlayParamBuilder;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.DataUtils;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;
import org.xbill.DNS.WKSRecord.Service;

public class ExitPlayerPageDialog extends AlertDialog {
    private static final String TAG = "ExitPlayerPageDialog";
    private TextView mBottomLeft;
    private TextView mBottomRight;
    private GalleryListContent mContent;
    private FrameLayout mContentContainer;
    private IItemListener<IVideo> mContentItemListener = new IItemListener<IVideo>() {
        public void onItemClicked(IVideo data, int index) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(ExitPlayerPageDialog.TAG, ">> onItemClicked, index=" + index + ", data=" + data);
            }
            ExitPlayerPageDialog.this.sendContentItemClickedPingback(data, index);
            if (index == 0) {
                ExitPlayerPageDialog.this.handleHomeItemClicked();
            } else {
                ExitPlayerPageDialog.this.handleVideoItemClicked(data);
            }
            ExitPlayerPageDialog.this.dismiss();
            if (ExitPlayerPageDialog.this.mContext instanceof Activity) {
                ((Activity) ExitPlayerPageDialog.this.mContext).finish();
            }
        }

        public void onItemSelected(IVideo data, int index) {
        }

        public void onItemFilled() {
        }
    };
    private Context mContext;
    private FetchExitAlbumsTask mExitAlbumsTask;
    private OnFocusChangeListener mFocusChangedListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            AnimationUtil.zoomAnimation(v, hasFocus, 1.1f, 200, true);
        }
    };
    private OnKeyListener mKeyEventListener = new OnKeyListener() {
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(ExitPlayerPageDialog.TAG, ">> mKeyEventListener.onKey, v=" + v + ", event=" + event);
            }
            if (19 == keyCode && event.getAction() == 0) {
                ExitPlayerPageDialog.this.mContent.getFocusableView().setNextFocusDownId(v.getId());
            }
            return false;
        }
    };
    private IGalleryUIStyle mPortGalleryUIStyle = PlayerAppConfig.getUIStyle().getExitDialogPortGalleryUIStyle();
    private ITaskResultListener mTaskListener = new ITaskResultListener() {
        public void onSuccess(List<IVideo> list) {
            list.add(0, ExitPlayerPageDialog.this.getVideoDataForHomeItem());
            ExitPlayerPageDialog.this.mContent.setData((List) list);
        }

        public void onFailed(ApiException e) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(ExitPlayerPageDialog.TAG, ">> FetchExitAlbumsTask, onFailed, e=" + e.getMessage());
            }
        }
    };
    private IVideo mVideoForHomeItem;

    public ExitPlayerPageDialog(Context context) {
        super(context, R.style.Theme_Dialog_Exit_App_Translucent_NoTitle);
        init(context);
    }

    public ExitPlayerPageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public ExitPlayerPageDialog(Context context, int theme) {
        super(context, theme);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        this.mContent = new GalleryListContent(context, this.mPortGalleryUIStyle, "", false, false, true);
        this.mExitAlbumsTask = new FetchExitAlbumsTask();
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_layout_play_page_exit_dialog);
        initWindow();
        initViews();
        setupViews();
        initFocus();
        this.mExitAlbumsTask.setTaskResultListener(this.mTaskListener);
        this.mExitAlbumsTask.excute();
    }

    private void initWindow() {
        getWindow().setLayout(-1, -1);
        LayoutParams lp = getWindow().getAttributes();
        lp.dimAmount = 0.0f;
        getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawableResource(R.color.exit_app_dialog_background_color);
    }

    private void initViews() {
        Rect bgPadding = UiHelper.getBgDrawablePaddings(ResourceUtil.getDrawable(R.drawable.share_exit_app_ad_dialog_btn_selector));
        this.mBottomLeft = (TextView) findViewById(R.id.bottom_left_button);
        this.mBottomRight = (TextView) findViewById(R.id.bottom_right_button);
        this.mContentContainer = (FrameLayout) findViewById(R.id.content_container);
        this.mContentContainer.addView(this.mContent.getView(), new FrameLayout.LayoutParams(-1, ResourceUtil.getDimen(R.dimen.dimen_370dp)));
        this.mContent.show();
        LinearLayout.LayoutParams bottomLP = new LinearLayout.LayoutParams((ResourceUtil.getDimen(R.dimen.dimen_195dp) + bgPadding.left) + bgPadding.right, (ResourceUtil.getDimen(R.dimen.dimen_55dp) + bgPadding.top) + bgPadding.bottom);
        this.mBottomLeft.setLayoutParams(bottomLP);
        this.mBottomRight.setLayoutParams(bottomLP);
    }

    private void setupViews() {
        this.mBottomLeft.setOnFocusChangeListener(this.mFocusChangedListener);
        this.mBottomRight.setOnFocusChangeListener(this.mFocusChangedListener);
        this.mBottomLeft.setOnKeyListener(this.mKeyEventListener);
        this.mBottomRight.setOnKeyListener(this.mKeyEventListener);
        this.mBottomLeft.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExitPlayerPageDialog.this.handleExitButtonClicked();
            }
        });
        this.mBottomRight.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                ExitPlayerPageDialog.this.sendWaitButtonClickedPingback();
                ExitPlayerPageDialog.this.dismiss();
            }
        });
        this.mContent.setItemListener(this.mContentItemListener);
        this.mContent.getFocusableView().setNextFocusDownId(this.mBottomLeft.getId());
    }

    private void initFocus() {
        this.mBottomLeft.setFocusable(false);
        this.mBottomRight.setFocusable(false);
        List list = new ArrayList();
        list.add(getVideoDataForHomeItem());
        this.mContent.setData(list);
        this.mContent.getFocusableView().requestFocus();
        this.mBottomLeft.setFocusable(true);
        this.mBottomRight.setFocusable(true);
    }

    private IVideo getVideoDataForHomeItem() {
        if (this.mVideoForHomeItem == null) {
            IVideoItemFactory factory = GetInterfaceTools.getPlayerFeatureProxy().getPlayerFeature().getVideoItemFactory();
            Album album = new Album();
            album.tvQid = "-2";
            album.qpId = "-2";
            this.mVideoForHomeItem = factory.createVideoItem(SourceType.COMMON, album, new MyPlayerProfile());
        }
        return this.mVideoForHomeItem;
    }

    private void handleExitButtonClicked() {
        dismiss();
        if (this.mContext instanceof Activity) {
            ((Activity) this.mContext).finish();
        }
    }

    private void handleHomeItemClicked() {
        CreateInterfaceTools.createEpgEntry().startHomeActivity(this.mContext, true);
    }

    private void handleVideoItemClicked(IVideo video) {
        Album album = video.getAlbum();
        if (album != null) {
            switch (GetInterfaceTools.getAlbumInfoHelper().getJumpType(album)) {
                case PLAY:
                    gotoPlayerActivity(album);
                    return;
                case DETAILS:
                    gotoDetailActivity(album);
                    return;
                default:
                    LogUtils.e(TAG, "album is " + DataUtils.albumInfoToString(album));
                    return;
            }
        }
    }

    private void gotoPlayerActivity(Album album) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> gotoPlayerActivity, album=" + album);
        }
        String from = IntentConfig2.FROM_EXIT_DIALOG;
        BasePlayParamBuilder builder = new BasePlayParamBuilder();
        PlayParams playParam = new PlayParams();
        playParam.sourceType = SourceType.COMMON;
        builder.setPlayParams(playParam);
        builder.setAlbumInfo(album);
        builder.setFrom(from);
        builder.setBuySource("");
        GetInterfaceTools.getPlayerPageProvider().startBasePlayerPage(this.mContext, builder);
    }

    private void gotoDetailActivity(Album album) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> gotoDetailActivity, album=" + album);
        }
        String from = IntentConfig2.FROM_EXIT_DIALOG;
        AlbumDetailPlayParamBuilder builder = new AlbumDetailPlayParamBuilder();
        builder.setAlbumInfo(album);
        builder.setFrom(from);
        builder.setBuySource("");
        builder.setTabSource("其他");
        GetInterfaceTools.getPlayerPageProvider().startAlbumDetailPlayerPage(this.mContext, builder);
    }

    public void dismiss() {
        super.dismiss();
    }

    public void onBackPressed() {
        super.onBackPressed();
        if (this.mContext instanceof Activity) {
            ((Activity) this.mContext).finish();
        }
    }

    public void show() {
        super.show();
        sendExitDialogPageShowPingback();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, ">> focus debug, dispatchKeyEvent, event=" + event);
        }
        boolean requestResult = false;
        if (20 == event.getKeyCode() && event.getAction() == 0) {
            View focused = getWindow().getDecorView().findFocus();
            View next = FocusFinder.getInstance().findNextFocus((ViewGroup) getWindow().getDecorView(), focused, Service.CISCO_FNA);
            if (next == null || !next.requestFocus(Service.CISCO_FNA)) {
                requestResult = false;
            } else {
                requestResult = true;
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "focus debug, focused=" + focused + ", next=" + next + ", requestResult=" + requestResult);
            }
        }
        if (requestResult || super.dispatchKeyEvent(event)) {
            return true;
        }
        return false;
    }

    private void sendExitDialogPageShowPingback() {
        if (this.mContext instanceof IPingbackContext) {
            IPingbackContext pingbackContext = this.mContext;
            PingbackFactory.instance().createPingback(38).addItem(BTSPTYPE.BSTP_1).addItem(pingbackContext.getItem("qtcurl")).addItem(pingbackContext.getItem("rfr")).addItem(pingbackContext.getItem("e")).addItem(pingbackContext.getItem("now_c1")).addItem(BLOCK.BLOCK_TYPE("exit")).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "sendExitDialogPageShowPingback, mContext is not instance of IPingbackContext!!");
        }
    }

    private void sendWaitButtonClickedPingback() {
        if (this.mContext instanceof IPingbackContext) {
            IPingbackContext pingbackContext = this.mContext;
            PingbackFactory.instance().createPingback(39).addItem(PingbackStore.R.R_TYPE("")).addItem(BLOCK.BLOCK_TYPE("exit")).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE("wait")).addItem(pingbackContext.getItem("rpage")).addItem(C1.C1_TYPE("")).addItem(pingbackContext.getItem("now_c1")).addItem(pingbackContext.getItem("now_qpid")).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "sendWaitButtonClickedPingback, mContext is not instance of IPingbackContext!!");
        }
    }

    private void sendContentItemClickedPingback(IVideo video, int index) {
        if (this.mContext instanceof IPingbackContext) {
            String str;
            IPingbackContext pingbackContext = this.mContext;
            IPingback addItem = PingbackFactory.instance().createPingback(39).addItem(PingbackStore.R.R_TYPE(index == 0 ? String.valueOf("首页") : video.getAlbumId())).addItem(BLOCK.BLOCK_TYPE("exit")).addItem(RTTYPE.RT_I).addItem(RSEAT.RSEAT_TYPE(String.valueOf(index + 1))).addItem(pingbackContext.getItem("rpage"));
            if (index == 0) {
                str = "";
            } else {
                str = String.valueOf(video.getChannelId());
            }
            addItem.addItem(C1.C1_TYPE(str)).addItem(pingbackContext.getItem("now_c1")).addItem(pingbackContext.getItem("now_qpid")).post();
        } else if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "sendContentItemClickedPingback, mContext is not instance of IPingbackContext!!");
        }
    }
}
