package com.gala.video.app.epg.ui.subjectreview;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.sdk.player.IMediaPlayer;
import com.gala.tvapi.type.ResourceType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemClickListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemFocusChangedListener;
import com.gala.video.albumlist4.widget.RecyclerView.OnItemRecycledListener;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.albumlist4.widget.VerticalGridView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt;
import com.gala.video.app.epg.ui.albumlist.common.NetworkPrompt.INetworkStateListener;
import com.gala.video.app.epg.ui.albumlist.utils.ItemUtils;
import com.gala.video.app.epg.ui.multisubject.MultiSubjectEnterUtils;
import com.gala.video.app.epg.ui.subjectreview.data.TasksRepository;
import com.gala.video.lib.framework.core.network.check.INetWorkManager.StateCallback;
import com.gala.video.lib.framework.core.network.check.NetWorkManager;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.albumlist.ErrorKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.utils.AnimationUtil;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.mcto.ads.internal.net.SendFlag;
import java.util.List;

public class QSubjectReviewActivity extends QMultiScreenActivity implements View {
    private static final String LOG_TAG = "EPG/album4/QSubjectReviewActivity";
    private final int DELAY_MILLIS = 250;
    protected SubjectReviewAdapter mAdapter;
    protected List<ChannelLabel> mChannelLabelList;
    private TextView mCountView;
    private String mFrom;
    protected VerticalGridView mGridView;
    private Handler mHandler = new Handler();
    private String mId;
    private boolean mIsFirstEntry = true;
    private NetworkPrompt mNetworkPrompt = new NetworkPrompt(this);
    private INetworkStateListener mNetworkStateListener = new C11187();
    protected GlobalQRFeedbackPanel mNoResultPanel;
    private OnItemClickListener mOnItemClickListener = new C11112();
    private OnItemFocusChangedListener mOnItemFocusChangedListener = new C11101();
    private OnItemRecycledListener mOnItemRecycledListener = new C11123();
    private Presenter mPresenter;
    protected float mScaleBig = 1.1f;
    private Runnable mShowGridViewRunnable = new C11198();
    private View mTopCuttingLine;

    class C11101 implements OnItemFocusChangedListener {
        C11101() {
        }

        public void onItemFocusChanged(ViewGroup parent, ViewHolder viewHolder, boolean hasFocus) {
            if (viewHolder != null) {
                AnimationUtil.zoomAnimation(viewHolder.itemView, hasFocus, QSubjectReviewActivity.this.mScaleBig, 200, true);
            }
        }
    }

    class C11112 implements OnItemClickListener {
        C11112() {
        }

        public void onItemClick(ViewGroup parent, ViewHolder viewHolder) {
            if (viewHolder != null) {
                int position = viewHolder.getLayoutPosition();
                LogUtils.m1576i(QSubjectReviewActivity.LOG_TAG, "OnItemClickListener-->itemClick, position = ", Integer.valueOf(position));
                if (ListUtils.isLegal(QSubjectReviewActivity.this.mChannelLabelList, position)) {
                    ChannelLabel channelLabel = (ChannelLabel) QSubjectReviewActivity.this.mChannelLabelList.get(position);
                    if (channelLabel != null) {
                        String buySourceTopic = "topic";
                        if (channelLabel.getType() == ResourceType.RESOURCE_GROUP) {
                            MultiSubjectEnterUtils.start(QSubjectReviewActivity.this, channelLabel.itemId, buySourceTopic, QSubjectReviewActivity.this.mFrom);
                        } else {
                            ItemUtils.openDetailOrPlay(QSubjectReviewActivity.this, (ChannelLabel) QSubjectReviewActivity.this.mChannelLabelList.get(position), null, QSubjectReviewActivity.this.mFrom, buySourceTopic, null);
                        }
                    }
                } else if (LogUtils.mIsDebug) {
                    LogUtils.m1571e(QSubjectReviewActivity.LOG_TAG, "itemClick ----- mChnLabelList.size = " + ListUtils.getCount(QSubjectReviewActivity.this.mChannelLabelList));
                }
            }
        }
    }

    class C11123 implements OnItemRecycledListener {
        C11123() {
        }

        public void onItemRecycled(ViewGroup parent, ViewHolder viewHolder) {
            if (viewHolder != null && viewHolder.itemView != null) {
                QSubjectReviewActivity.this.mAdapter.releaseData(viewHolder.itemView);
            }
        }
    }

    class C11134 implements Runnable {
        C11134() {
        }

        public void run() {
            QSubjectReviewActivity.this.mNoResultPanel.requestFocus();
        }
    }

    class C11145 implements Runnable {
        C11145() {
        }

        public void run() {
            if (ListUtils.isEmpty(QSubjectReviewActivity.this.mChannelLabelList)) {
                QSubjectReviewActivity.this.mCountView.setVisibility(4);
                QSubjectReviewActivity.this.mTopCuttingLine.setVisibility(4);
                QSubjectReviewActivity.this.mGridView.setVisibility(4);
                QSubjectReviewActivity.this.showNoResultLayout(ErrorKind.NO_RESULT_AND_NO_MENU, null);
                return;
            }
            QSubjectReviewActivity.this.mCountView.setVisibility(0);
            QSubjectReviewActivity.this.mTopCuttingLine.setVisibility(0);
            if (QSubjectReviewActivity.this.mNoResultPanel != null) {
                QSubjectReviewActivity.this.mNoResultPanel.setVisibility(4);
            }
            QSubjectReviewActivity.this.mGridView.setVisibility(0);
            QSubjectReviewActivity.this.mGridView.setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
            QSubjectReviewActivity.this.mAdapter.updateData(QSubjectReviewActivity.this.mChannelLabelList);
            QSubjectReviewActivity.this.mGridView.setTotalSize(ListUtils.getCount(QSubjectReviewActivity.this.mChannelLabelList));
            QSubjectReviewActivity.this.mCountView.setText(QSubjectReviewActivity.this.getString(C0508R.string.subject_review_count, new Object[]{Integer.valueOf(size)}));
        }
    }

    class C11187 implements INetworkStateListener {
        C11187() {
        }

        public void onConnected(boolean isChanged) {
            LogUtils.m1576i(QSubjectReviewActivity.LOG_TAG, "INetworkStateListener-->onConnected(), isChanged = ", Boolean.valueOf(isChanged));
            if (isChanged && ListUtils.isEmpty(QSubjectReviewActivity.this.mChannelLabelList)) {
                QSubjectReviewActivity.this.mPresenter.start(QSubjectReviewActivity.this.mId);
            }
        }
    }

    class C11198 implements Runnable {
        C11198() {
        }

        public void run() {
            if (QSubjectReviewActivity.this.mGridView != null) {
                QSubjectReviewActivity.this.mGridView.setVisibility(0);
            }
            if (QSubjectReviewActivity.this.mNoResultPanel != null) {
                QSubjectReviewActivity.this.mNoResultPanel.setVisibility(4);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        stopImageProvider();
        setContentLayout();
        initBundle();
        initTopView();
        initGridView();
        this.mPresenter = new SubjectReviewPresenter(this, new TasksRepository());
        this.mPresenter.start(this.mId);
    }

    private void initBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                this.mFrom = bundle.getString(QSubjectUtils.FROM);
                this.mId = bundle.getString("id");
            }
        }
    }

    private void initTopView() {
        this.mCountView = (TextView) findViewById(C0508R.id.epg_qsubject_sum_text_id);
        this.mTopCuttingLine = findViewById(C0508R.id.epg_qsubject_tag_cutting_line_id);
        this.mCountView.setVisibility(4);
        this.mTopCuttingLine.setVisibility(4);
    }

    public void onResume() {
        super.onResume();
        if (this.mAdapter != null) {
            this.mAdapter.onResume();
        }
        if (!this.mIsFirstEntry) {
            reloadBitmaps();
        }
        this.mIsFirstEntry = false;
        this.mNetworkPrompt.registerNetworkListener(this.mNetworkStateListener);
    }

    protected void onPause() {
        super.onPause();
        if (this.mAdapter != null) {
            this.mAdapter.onPause();
        }
        ImageProviderApi.getImageProvider().stopAllTasks();
        this.mNetworkPrompt.unregisterNetworkListener();
    }

    public void onStop() {
        super.onStop();
        recycleBitmaps();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (this.mHandler != null) {
            this.mHandler.removeCallbacksAndMessages(null);
            this.mHandler = null;
        }
    }

    protected void setContentLayout() {
        setContentView(C0508R.layout.epg_qsubject_review_activity);
    }

    protected void initGridView() {
        this.mGridView = (VerticalGridView) findViewById(C0508R.id.epg_qsubject_gridview_id);
        this.mAdapter = new SubjectReviewAdapter(this);
        this.mGridView.setFocusable(true);
        this.mGridView.setFocusLoop(true);
        this.mGridView.setNumRows(3);
        this.mGridView.setFocusMode(1);
        this.mGridView.setScrollRoteScale(0.8f, 1.0f, 2.5f);
        this.mGridView.setExtraPadding(IMediaPlayer.AD_INFO_OVERLAY_LOGIN_SUCCESS);
        this.mGridView.setPadding(getDimen(C0508R.dimen.dimen_42dp), getDimen(C0508R.dimen.dimen_30dp), getDimen(C0508R.dimen.dimen_10dp), getDimen(C0508R.dimen.dimen_42dp));
        this.mGridView.setVerticalMargin(ResourceUtil.getDimen(C0508R.dimen.dimen_12dp));
        this.mGridView.setHorizontalMargin(ResourceUtil.getDimen(C0508R.dimen.dimen_12dp));
        this.mGridView.setFocusLeaveForbidden(211);
        this.mGridView.setOnItemFocusChangedListener(this.mOnItemFocusChangedListener);
        this.mGridView.setOnItemClickListener(this.mOnItemClickListener);
        this.mGridView.setOnItemRecycledListener(this.mOnItemRecycledListener);
        this.mGridView.setVerticalScrollBarEnabled(true);
        this.mGridView.setScrollBarDrawable(C0508R.drawable.epg_thumb);
        this.mGridView.setAdapter(this.mAdapter);
        this.mGridView.setDescendantFocusability(393216);
        this.mGridView.setVisibility(4);
        this.mHandler.postDelayed(this.mShowGridViewRunnable, 250);
    }

    private void recycleBitmaps() {
        if (this.mGridView != null && this.mAdapter != null) {
            LogUtils.m1576i(LOG_TAG, "recycleBitmaps --- first = ", Integer.valueOf(this.mGridView.getFirstAttachedPosition()), " last = ", Integer.valueOf(this.mGridView.getLastAttachedPosition()));
            for (int i = first; i <= last; i++) {
                this.mAdapter.recycleBitmap(this.mGridView.getViewByPosition(i));
            }
        }
    }

    private void reloadBitmaps() {
        if (this.mGridView != null && this.mAdapter != null) {
            LogUtils.m1576i(LOG_TAG, "reloadBitmaps --- first = ", Integer.valueOf(this.mGridView.getFirstAttachedPosition()), " last = ", Integer.valueOf(this.mGridView.getLastAttachedPosition()));
            for (int i = first; i <= last; i++) {
                this.mAdapter.onReloadTasks(this.mGridView.getViewByPosition(i));
            }
        }
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() != 0) {
            return super.handleKeyEvent(event);
        }
        int keyCode = event.getKeyCode();
        if (keyCode == 4 || keyCode == 111) {
            stopImageProvider();
        }
        return super.handleKeyEvent(event);
    }

    private void stopImageProvider() {
        ImageProviderApi.getImageProvider().stopAllTasks();
    }

    protected int getDimen(int dimen) {
        return ResourceUtil.getDimen(dimen);
    }

    protected View getBackgroundContainer() {
        return findViewById(C0508R.id.qsubject_main_container);
    }

    protected void showNoResultLayout(ErrorKind errorKind, ApiException e) {
        if (this.mNoResultPanel == null) {
            this.mNoResultPanel = (GlobalQRFeedbackPanel) ((ViewStub) findViewById(C0508R.id.epg_qsubject_no_data_panel_layout_viewstub_id)).inflate().findViewById(C0508R.id.qsubject_no_data_panel);
        }
        this.mNoResultPanel.setVisibility(0);
        GetInterfaceTools.getUICreator().maketNoResultView(this, this.mNoResultPanel, errorKind, e);
        this.mNoResultPanel.post(new C11134());
    }

    public void showData(List<ChannelLabel> data) {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mShowGridViewRunnable);
            if (ListUtils.isEmpty(this.mChannelLabelList)) {
                this.mChannelLabelList = data;
                runOnUiThread(new C11145());
            }
        }
    }

    public void showExceptionView(final ApiException e) {
        if (this.mHandler != null) {
            this.mHandler.removeCallbacks(this.mShowGridViewRunnable);
            runOnUiThread(new Runnable() {

                class C11161 implements StateCallback {

                    class C11151 implements Runnable {
                        C11151() {
                        }

                        public void run() {
                            QSubjectReviewActivity.this.showNoResultLayout(ErrorKind.SHOW_QR, e);
                        }
                    }

                    C11161() {
                    }

                    public void getStateResult(int arg0) {
                        QSubjectReviewActivity.this.runOnUiThread(new C11151());
                    }
                }

                public void run() {
                    QSubjectReviewActivity.this.mCountView.setVisibility(4);
                    QSubjectReviewActivity.this.mTopCuttingLine.setVisibility(4);
                    NetWorkManager.getInstance().checkNetWork(new C11161());
                    QSubjectReviewActivity.this.mGridView.setVisibility(4);
                }
            });
        }
    }
}
