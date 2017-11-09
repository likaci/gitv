package com.tvos.appdetailpage.ui;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gala.sdk.player.constants.PlayerIntentConfig;
import com.gala.video.app.epg.ui.setting.SettingConstants;
import com.mcto.ads.internal.net.SendFlag;
import com.push.mqttv3.internal.ClientDefaults;
import com.squareup.picasso.Picasso;
import com.tvos.appdetailpage.client.AppStoreClient;
import com.tvos.appdetailpage.client.AppStoreClient.Builder;
import com.tvos.appdetailpage.client.Constants;
import com.tvos.appdetailpage.config.APIConstants;
import com.tvos.appdetailpage.config.CommResponseCallback;
import com.tvos.appdetailpage.config.ContextHolder;
import com.tvos.appdetailpage.config.DataCache;
import com.tvos.appdetailpage.info.AppCategoriesResponse;
import com.tvos.appdetailpage.info.AppCategory;
import com.tvos.appdetailpage.info.AppDetail;
import com.tvos.appdetailpage.info.AppDetail.ResInfo;
import com.tvos.appdetailpage.info.AppDetailResponse;
import com.tvos.appdetailpage.info.RecommendApp;
import com.tvos.appdetailpage.info.RecommendResponse;
import com.tvos.appdetailpage.info.StatusResponse;
import com.tvos.appdetailpage.model.AppDetailInfo;
import com.tvos.appdetailpage.ui.adapter.AppDetailRecommdAdapter;
import com.tvos.appdetailpage.ui.adapter.AppDetailThumbNailAdapter;
import com.tvos.appdetailpage.utils.AppCommUtils;
import com.tvos.appdetailpage.utils.CommonUtils;
import com.tvos.appdetailpage.utils.DownloadManagerUtil;
import com.tvos.appdetailpage.utils.DownloadManagerUtil.DownloadState;
import com.tvos.appdetailpage.utils.NetWorkManager;
import com.tvos.appdetailpage.utils.RootCmdUtils;
import com.tvos.appdetailpage.utils.StringUtils;
import com.tvos.appdetailpage.utils.SysUtils;
import com.tvos.appdetailpage.widget.ActionView;
import com.tvos.appdetailpage.widget.ProgressBarItem;
import com.tvos.appdetailpage.widget.RatingBarView;
import com.tvos.appmanager.AppManager;
import com.tvos.appmanager.IAppManager;
import com.tvos.appmanager.model.IAppInfo;
import com.tvos.apps.utils.LogUtils;
import com.tvos.downloadmanager.DownloadManagerFactory;
import com.tvos.downloadmanager.IDownloadManager;
import com.tvos.widget.VGridView;
import com.tvos.widget.VGridView.OnItemFocusChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AppStoreDetailActivity extends BaseActivity implements Observer {
    private static final int ACTION_MODE_DOWNLOAD = 1;
    private static final int ACTION_MODE_DOWNLOADING = 8;
    private static final int ACTION_MODE_INSTALL = 2;
    private static final int ACTION_MODE_INSTALLED = 3;
    private static final int ACTION_MODE_INSTALLED_STICK = 4;
    private static final int ACTION_MODE_INSTALLED_UPGRADE = 5;
    private static final int ACTION_MODE_INSTALLED_UPGRADE_STICK = 6;
    private static final int ACTION_MODE_SYSTEM_APP = 7;
    private static final int ACTION_MODE_UPGRADING = 9;
    private static final String APP_STORE_ENTRY = "normal";
    private static final String CHANNEL_ID = "TVLauncher";
    private static final String DBNAME = "downloadmanager.db";
    private static final int MSG_QUIT = 0;
    private static final int MSG_REFRESHAPPSTATUS = 3;
    private static final int MSG_REFRESHCATEGORY = 7;
    private static final int MSG_REFRESHDOWNLOADSTATUS = 4;
    private static final int MSG_REFRESHPACKAGEADD = 5;
    private static final int MSG_REFRESHRECOMMENDAPP = 6;
    private static final int MSG_UPDATEAPPDETAIL = 1;
    private static final int MSG_UPDATELOCALAPP = 2;
    private static boolean MixFlag = false;
    private static final String PRODUCT = "8003";
    private static final String TAG = "AppStoreDetailActivity";
    private static List<String> filterPkgNameList;
    private static IAppManager mIAppManager;
    private static IDownloadManager mIDownloadManager;
    private int[] PROGRESS_IMAGES;
    private ActionView mActionFourth;
    private ActionView mActionFst;
    private int mActionMode = 1;
    private ActionView mActionSnd;
    private ActionView mActionThr;
    private Callback<AppDetailResponse> mAppDetailCallback = new C20392();
    private String mAppDownloadDir;
    private String mAppDownloadUrl;
    private String mAppFileName;
    private AppDetailInfo mAppInfo;
    private String mAppPackageName;
    private AppStoreClient mAppStoreClient;
    private AppDetailThumbNailAdapter mAppThumbNailAdapter;
    private String mAppVersion;
    private Callback<StatusResponse> mCallback = new C20447();
    private Drawable mDefautDrawable;
    private boolean mDownloadClickable = true;
    private boolean mDownloading = false;
    private float[] mGrayColorMatrix = new float[]{0.25f, 0.0f, 0.0f, 0.0f, 47.625f, 0.0f, 0.25f, 0.0f, 0.0f, 47.625f, 0.0f, 0.0f, 0.25f, 0.0f, 47.625f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private Handler mHandler;
    private ImageView mImageAppIcon;
    private ImageView mImageBackGround;
    private ImageView mImageProgress;
    private com.squareup.picasso.Callback mImgCallback = new C20458();
    private RelativeLayout mLayoutActionView;
    private RelativeLayout mLayoutDetailView;
    private Drawable mLeftArrowEnableDrawable;
    private Drawable mLeftArrowUnableDrawable;
    private float[] mNormalColorMatrix = new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f};
    private OnClickListener mOnClickListener = new C20425();
    ProgressBarHandler mProgressBarHandler = new ProgressBarHandler();
    private ProgressBarItem mProgressBarItem;
    private RatingBarView mRatingAppGrade;
    BroadcastReceiver mReceiver = new C20436();
    private AppDetailRecommdAdapter mRecommAppAdapter;
    private Callback<RecommendResponse> mRecommAppCallback = new C20403();
    private VGridView mRecommAppVGridView;
    private ArrayList<RecommendApp> mRecommApps;
    private Intent mReturnIntent;
    private Drawable mRightArrowEnableDrawable;
    private Drawable mRightArrowUnableDrawable;
    private View mRootView = null;
    private boolean mShowProgress = false;
    private TextView mTextAppCategory;
    private TextView mTextAppDownload;
    private TextView mTextAppName;
    private TextView mTextAppSize;
    private TextView mTextAppVersion;
    private TextView mTextAppVersionTitle;
    private TextView mTextDescription;
    private TextView mTextProgress;
    private Thread mThread = new Thread(new C20381());
    private VGridView mThumbnailVGridView;
    private Callback<AppCategoriesResponse> mUpdateCategiriesCallback = new C20414();
    private boolean mUpgradeClickable = true;

    public interface OnItemClickedListener {
        void onItemClicked(View view, int i);
    }

    class C20381 implements Runnable {

        class C20371 extends Handler {

            class C20351 implements Runnable {
                C20351() {
                }

                public void run() {
                    AppStoreDetailActivity.this.refreshRecommdApp(AppStoreDetailActivity.this.mRecommApps);
                }
            }

            C20371() {
            }

            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:
                        AppStoreDetailActivity.this.mHandler = null;
                        Looper.myLooper().quit();
                        return;
                    case 1:
                        if (msg.obj != null) {
                            AppStoreDetailActivity.this.updataAppDetail(AppStoreDetailActivity.this.createAppInfo(msg.obj));
                            return;
                        }
                        return;
                    case 2:
                        if (msg.obj != null) {
                            AppStoreDetailActivity.this.queryLocalAppShow(msg.obj);
                            return;
                        }
                        return;
                    case 3:
                        if (msg.obj != null) {
                            AppStoreDetailActivity.this.refreshAppState(msg.obj);
                            return;
                        }
                        return;
                    case 4:
                        if (msg.obj != null) {
                            AppStoreDetailActivity.this.refreshDownloadState(msg.obj);
                            return;
                        }
                        return;
                    case 5:
                        if (msg.obj != null) {
                            AppStoreDetailActivity.this.refreshPackageAdded((String) msg.obj);
                            return;
                        }
                        return;
                    case 6:
                        if (AppStoreDetailActivity.this.mRecommApps != null) {
                            AppStoreDetailActivity.this.runOnUiThread(new C20351());
                            return;
                        }
                        return;
                    case 7:
                        if (msg.obj != null && AppStoreDetailActivity.this.mAppInfo != null) {
                            final List<AppCategory> allAppCategoryList = msg.obj;
                            for (int i = 0; i < allAppCategoryList.size(); i++) {
                                if (AppStoreDetailActivity.this.mAppInfo.getAppCategory().equals(((AppCategory) allAppCategoryList.get(i)).id)) {
                                    final int index = i;
                                    AppStoreDetailActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            AppStoreDetailActivity.this.mTextAppCategory.setText(((AppCategory) allAppCategoryList.get(index)).category_name);
                                        }
                                    });
                                }
                            }
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }

        C20381() {
        }

        public void run() {
            Looper.prepare();
            AppStoreDetailActivity.this.mHandler = new C20371();
            AppStoreDetailActivity.this.updateAppDetailInfo();
            Looper.loop();
        }
    }

    class C20392 implements Callback<AppDetailResponse> {
        C20392() {
        }

        public void success(AppDetailResponse resp, Response ignore) {
            if (resp.data != null) {
                Log.d(AppStoreDetailActivity.TAG, "mAppDetailCallback success resp.data = " + resp.data.basic);
                if (AppStoreDetailActivity.this.mHandler != null) {
                    AppStoreDetailActivity.this.mHandler.sendMessage(AppStoreDetailActivity.this.mHandler.obtainMessage(1, resp.data));
                    return;
                }
                return;
            }
            LogUtils.m1738e(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---mAppDetailCallback() -> getAppDetail AppDetailResponse resp is null---");
            if (AppStoreDetailActivity.this.mHandler != null) {
                AppStoreDetailActivity.this.mHandler.sendMessage(AppStoreDetailActivity.this.mHandler.obtainMessage(2, AppStoreDetailActivity.this.mAppInfo.getAppPackageName()));
            }
        }

        public void failure(RetrofitError e) {
            Log.e(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---mAppDetailCallback() ->-failure() retrofitErrot---- " + e.getMessage());
            if (AppStoreDetailActivity.this.mHandler != null) {
                AppStoreDetailActivity.this.mHandler.sendMessage(AppStoreDetailActivity.this.mHandler.obtainMessage(2, AppStoreDetailActivity.this.mAppInfo.getAppPackageName()));
            }
        }
    }

    class C20403 implements Callback<RecommendResponse> {
        C20403() {
        }

        public void success(RecommendResponse resp, Response ignore) {
            if (AppStoreDetailActivity.this.mAppInfo != null) {
                LogUtils.m1737d(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---refreshRecommd() >>>> success mRecommAppCallback----" + AppStoreDetailActivity.this.mAppInfo.getAppId());
                if (resp.data != null) {
                    LogUtils.m1737d(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---mRecommAppCallback() ->mAppStoreClient.getAssociatedAppsAndDetail success()---" + resp.data);
                    if (AppStoreDetailActivity.this.mHandler != null) {
                        AppStoreDetailActivity.this.mHandler.sendMessage(AppStoreDetailActivity.this.mHandler.obtainMessage(6));
                    }
                    AppStoreDetailActivity.this.mRecommApps = resp.data;
                    return;
                }
                LogUtils.m1738e(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---mRecommAppCallback() ->mAppStoreClient.getAssociatedAppsAndDetail AppDetailResponse resp is null---");
            }
        }

        public void failure(RetrofitError e) {
            Log.e(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---mRecommAppCallback() ->-failure() retrofitErrot---- " + e.getMessage());
        }
    }

    class C20414 implements Callback<AppCategoriesResponse> {
        C20414() {
        }

        public void success(AppCategoriesResponse resp, Response ignore) {
            if (resp.data != null) {
                LogUtils.m1737d(AppStoreDetailActivity.TAG, "---mAppCategiriesCallback() ->mAppStoreClient." + resp.data);
                if (AppStoreDetailActivity.this.mAppInfo != null && AppStoreDetailActivity.this.mHandler != null) {
                    AppStoreDetailActivity.this.mHandler.sendMessage(AppStoreDetailActivity.this.mHandler.obtainMessage(7, resp.data));
                    return;
                }
                return;
            }
            LogUtils.m1738e(AppStoreDetailActivity.TAG, "getAppCategiries AppCategoriesResponse resp is null---");
        }

        public void failure(RetrofitError e) {
            Log.e(AppStoreDetailActivity.TAG, "-mAppCategiriesCallback() ->-failure() retrofitErrot---- " + e.getMessage());
        }
    }

    class C20425 implements OnClickListener {
        C20425() {
        }

        public void onClick(View view) {
            if (AppStoreDetailActivity.this.mActionMode == 1) {
                if (view == AppStoreDetailActivity.this.mActionFst.getContent() && AppStoreDetailActivity.this.isNetConnected() && AppStoreDetailActivity.this.mDownloadClickable) {
                    AppStoreDetailActivity.this.refreshFstActionBackGround(AppStoreDetailActivity.this.getResId("drawable", "apps_bt_download"), AppStoreDetailActivity.this.getResId("string", "apps_appdetails_action_downloading"));
                    AppStoreDetailActivity.this.mDownloadClickable = false;
                    AppStoreDetailActivity.this.downloadApp();
                }
            } else if (AppStoreDetailActivity.this.mActionMode == 2) {
                if (view == AppStoreDetailActivity.this.mActionFst.getContent()) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_install---");
                    AppStoreDetailActivity.this.installApp();
                } else if (view == AppStoreDetailActivity.this.mActionSnd.getContent()) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_clear---");
                    AppStoreDetailActivity.this.clearApp();
                }
            } else if (AppStoreDetailActivity.this.mActionMode == 3) {
                if (view == AppStoreDetailActivity.this.mActionFst.getContent()) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_open---");
                    AppStoreDetailActivity.this.openApp();
                } else if (view == AppStoreDetailActivity.this.mActionSnd.getContent()) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_uninstall---");
                    AppStoreDetailActivity.this.uninstallApp();
                }
            } else if (AppStoreDetailActivity.this.mActionMode == 5) {
                if (view == AppStoreDetailActivity.this.mActionFst.getContent() && AppStoreDetailActivity.this.mUpgradeClickable) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_open---");
                    AppStoreDetailActivity.this.openApp();
                } else if (view == AppStoreDetailActivity.this.mActionSnd.getContent() && AppStoreDetailActivity.this.mUpgradeClickable) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_uninstall---");
                    AppStoreDetailActivity.this.uninstallApp();
                } else if (view == AppStoreDetailActivity.this.mActionThr.getContent() && AppStoreDetailActivity.this.isNetConnected() && AppStoreDetailActivity.this.mUpgradeClickable) {
                    AppStoreDetailActivity.this.mUpgradeClickable = false;
                    AppStoreDetailActivity.this.updateApp();
                }
            } else if (AppStoreDetailActivity.this.mActionMode == 7) {
                if (view == AppStoreDetailActivity.this.mActionFst.getContent()) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_open---");
                    AppStoreDetailActivity.this.openApp();
                }
            } else if (AppStoreDetailActivity.this.mActionMode == 4) {
                if (view == AppStoreDetailActivity.this.mActionFst.getContent()) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_open---");
                    AppStoreDetailActivity.this.openApp();
                } else if (view == AppStoreDetailActivity.this.mActionSnd.getContent()) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_uninstall---");
                    AppStoreDetailActivity.this.uninstallApp();
                }
            } else if (AppStoreDetailActivity.this.mActionMode != 6) {
            } else {
                if (view == AppStoreDetailActivity.this.mActionFst.getContent() && AppStoreDetailActivity.this.mUpgradeClickable) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_open---");
                    AppStoreDetailActivity.this.openApp();
                } else if (view == AppStoreDetailActivity.this.mActionSnd.getContent() && AppStoreDetailActivity.this.mUpgradeClickable) {
                    LogUtils.m1739i(AppStoreDetailActivity.TAG, "---mOnItemClickListener ->bt_uninstall---");
                    AppStoreDetailActivity.this.uninstallApp();
                } else if (view == AppStoreDetailActivity.this.mActionThr.getContent() && AppStoreDetailActivity.this.isNetConnected() && AppStoreDetailActivity.this.mUpgradeClickable) {
                    AppStoreDetailActivity.this.mUpgradeClickable = false;
                    AppStoreDetailActivity.this.updateApp();
                }
            }
        }
    }

    class C20436 extends BroadcastReceiver {
        C20436() {
        }

        public void onReceive(Context context, Intent intent) {
            int resultCode = intent.getIntExtra(APIConstants.BROADCAST_EXTRA_COMM, -1);
            String pkgname = intent.getStringExtra(APIConstants.BROADCAST_EXTRA_PKG_NAME);
            if (pkgname != null && pkgname.equals(AppStoreDetailActivity.this.mAppPackageName)) {
                switch (resultCode) {
                    case 9:
                        Log.d(AppStoreDetailActivity.TAG, "BROADCAST_INSTALL_APP");
                        if (AppStoreDetailActivity.this.mHandler != null) {
                            AppStoreDetailActivity.this.mHandler.sendMessage(AppStoreDetailActivity.this.mHandler.obtainMessage(5, AppStoreDetailActivity.this.mAppPackageName));
                            return;
                        }
                        return;
                    default:
                        return;
                }
            }
        }
    }

    class C20447 implements Callback<StatusResponse> {
        C20447() {
        }

        public void success(StatusResponse statusResponse, Response response) {
            LogUtils.m1737d(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---mStatusResponsePingBack success() Pingback success! status = " + response.getStatus() + " url=" + response.getUrl());
        }

        public void failure(RetrofitError e) {
            LogUtils.m1737d(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---mStatusResponsePingBack failure() Pingback failure! status = " + e.getCause() + " url=" + e.getUrl());
        }
    }

    class C20458 implements com.squareup.picasso.Callback {
        C20458() {
        }

        public void onError() {
            LogUtils.m1737d(AppStoreDetailActivity.TAG, "mImgCallback onError");
        }

        public void onSuccess() {
            LogUtils.m1737d(AppStoreDetailActivity.TAG, "mImgCallback onSuccess");
        }
    }

    private class GridViewListener implements OnItemFocusChangeListener {
        private GridViewListener() {
        }

        public void onItemFocusChange(View view, int position, boolean hasFocus) {
            if (hasFocus) {
                view.setBackgroundResource(AppStoreDetailActivity.this.getResId("drawable", "apps_item_focused_bg"));
            } else {
                view.setBackgroundResource(0);
            }
        }
    }

    private class ProgressBarHandler {
        private int mCurrentIndex;
        private Handler mHandler;
        private Runnable mRunnable;

        class C20471 implements Runnable {
            C20471() {
            }

            public void run() {
                if (AppStoreDetailActivity.this.mShowProgress && AppStoreDetailActivity.this.PROGRESS_IMAGES.length - 1 > ProgressBarHandler.this.mCurrentIndex) {
                    ImageView access$31 = AppStoreDetailActivity.this.mImageProgress;
                    int[] access$30 = AppStoreDetailActivity.this.PROGRESS_IMAGES;
                    ProgressBarHandler progressBarHandler = ProgressBarHandler.this;
                    int access$1 = progressBarHandler.mCurrentIndex + 1;
                    progressBarHandler.mCurrentIndex = access$1;
                    access$31.setImageResource(access$30[access$1]);
                    if (ProgressBarHandler.this.mCurrentIndex < AppStoreDetailActivity.this.PROGRESS_IMAGES.length - 2) {
                        ProgressBarHandler.this.mHandler.postDelayed(ProgressBarHandler.this.mRunnable, 200);
                    }
                }
            }
        }

        class C20504 implements Runnable {
            C20504() {
            }

            public void run() {
                AppStoreDetailActivity.this.mImageProgress.setImageResource(AppStoreDetailActivity.this.PROGRESS_IMAGES[AppStoreDetailActivity.this.PROGRESS_IMAGES.length - 1]);
            }
        }

        class C20515 implements Runnable {
            C20515() {
            }

            public void run() {
                if (AppStoreDetailActivity.this.mImageAppIcon.getDrawable() != null) {
                    AppStoreDetailActivity.this.normalTransformation(AppStoreDetailActivity.this.mImageAppIcon.getDrawable());
                }
                AppStoreDetailActivity.this.mImageProgress.setVisibility(4);
                AppStoreDetailActivity.this.mTextProgress.setVisibility(4);
                AppStoreDetailActivity.this.mDownloading = false;
            }
        }

        private ProgressBarHandler() {
            this.mCurrentIndex = 0;
            this.mHandler = new Handler();
            this.mRunnable = new C20471();
        }

        public void startProgressBar(final int autoUpdateInterval, final int txtRes) {
            AppStoreDetailActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    AppStoreDetailActivity.this.mDownloading = true;
                    if (AppStoreDetailActivity.this.mImageAppIcon.getDrawable() != null) {
                        AppStoreDetailActivity.this.grayTransformation(AppStoreDetailActivity.this.mImageAppIcon.getDrawable());
                    }
                    AppStoreDetailActivity.this.mImageProgress.setVisibility(0);
                    AppStoreDetailActivity.this.mTextProgress.setVisibility(0);
                    AppStoreDetailActivity.this.mTextProgress.setText(txtRes);
                    AppStoreDetailActivity.this.mImageProgress.setImageResource(AppStoreDetailActivity.this.PROGRESS_IMAGES[0]);
                    if (autoUpdateInterval > 0) {
                        ProgressBarHandler.this.mCurrentIndex = 0;
                        ProgressBarHandler.this.mHandler.postDelayed(ProgressBarHandler.this.mRunnable, 200);
                    }
                    AppStoreDetailActivity.this.mShowProgress = true;
                }
            });
        }

        public void setProgressPercent(final int percent, final int txtRes) {
            AppStoreDetailActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    if (AppStoreDetailActivity.this.mImageAppIcon.getDrawable() != null) {
                        AppStoreDetailActivity.this.grayTransformation(AppStoreDetailActivity.this.mImageAppIcon.getDrawable());
                    }
                    AppStoreDetailActivity.this.mImageProgress.setVisibility(0);
                    AppStoreDetailActivity.this.mTextProgress.setVisibility(0);
                    AppStoreDetailActivity.this.mTextProgress.setText(txtRes);
                    int index = (((percent - 1) * (AppStoreDetailActivity.this.PROGRESS_IMAGES.length - 2)) / 99) + 2;
                    if (index < 0) {
                        index = 0;
                    } else if (index >= AppStoreDetailActivity.this.PROGRESS_IMAGES.length) {
                        index = AppStoreDetailActivity.this.PROGRESS_IMAGES.length - 1;
                    }
                    AppStoreDetailActivity.this.mImageProgress.setImageResource(AppStoreDetailActivity.this.PROGRESS_IMAGES[index]);
                }
            });
        }

        public void stopProgressBar() {
            AppStoreDetailActivity.this.mShowProgress = false;
            AppStoreDetailActivity.this.runOnUiThread(new C20504());
        }

        public void hideProgressBar() {
            AppStoreDetailActivity.this.runOnUiThread(new C20515());
        }
    }

    public static void setMixFlag(boolean mix) {
        MixFlag = mix;
    }

    public static boolean getMixFlag() {
        return MixFlag;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---onCreate()---");
        this.mRootView = LayoutInflater.from(this).inflate(getResId("layout", "apps_activity_appdetails"), null);
        setContentView(this.mRootView);
        this.mAppInfo = new AppDetailInfo();
        createReturnIntent(getIntent());
        if (((AppDetailInfo) getIntent().getSerializableExtra("singleAppInfo")) == null) {
            AppStoreClient.setIMEI(getIntent().getStringExtra("deviceid"));
            AppStoreClient.setAnonymousUserID(getIntent().getStringExtra("deviceid"));
            AppStoreClient.setDeviceID(getIntent().getStringExtra("deviceid"));
            AppStoreClient.setUUID(getIntent().getStringExtra("uuid"));
        }
        this.mAppInfo.setAppId(getIntent().getStringExtra("appid"));
        this.mAppInfo.setAppPackageName(getIntent().getStringExtra(APIConstants.BUNDLE_EXTRA_DETAILAPP_APP_PKG));
        initClient();
        initManagers();
        initProgressImage();
        initLayout();
        registerReceiver();
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---mAppInfo:" + this.mAppInfo);
        if ((this.mAppInfo.getAppPackageName() != null && this.mAppInfo.getAppPackageName() != "") || (this.mAppInfo.getAppId() != null && this.mAppInfo.getAppId() != "")) {
            showProgressBarItem(true);
            this.mThread.start();
        }
    }

    private void createReturnIntent(Intent intent) {
        if (intent != null) {
            if (intent.hasExtra(APIConstants.BUNDLE_EXTRA_RETURNINTENTPKGNAME) && intent.hasExtra(APIConstants.BUNDLE_EXTRA_RETURNINTENTACTIVITYNAME) && intent.hasExtra(APIConstants.BUNDLE_EXTRA_RETURNINTENTFLAGS)) {
                String returnIntentPkgName = intent.getStringExtra(APIConstants.BUNDLE_EXTRA_RETURNINTENTPKGNAME);
                String returnIntentActivityName = intent.getStringExtra(APIConstants.BUNDLE_EXTRA_RETURNINTENTACTIVITYNAME);
                int returnIntentFlag = intent.getIntExtra(APIConstants.BUNDLE_EXTRA_RETURNINTENTFLAGS, ClientDefaults.MAX_MSG_SIZE);
                ComponentName componentName = new ComponentName(returnIntentPkgName, returnIntentActivityName);
                this.mReturnIntent = new Intent();
                this.mReturnIntent.setComponent(componentName);
                this.mReturnIntent.setFlags(returnIntentFlag);
                return;
            }
            this.mReturnIntent = null;
        }
    }

    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if ((keyCode != 4 && keyCode != 30) || this.mReturnIntent == null) {
            return super.onKeyUp(keyCode, event);
        }
        startActivity(this.mReturnIntent);
        finish();
        return true;
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---onNewIntent()---");
        createReturnIntent(intent);
        this.mAppInfo = new AppDetailInfo();
        this.mAppInfo.setAppId(intent.getStringExtra("appid"));
        this.mAppInfo.setAppPackageName(intent.getStringExtra(APIConstants.BUNDLE_EXTRA_DETAILAPP_APP_PKG));
        if ((this.mAppInfo.getAppPackageName() != null && this.mAppInfo.getAppPackageName() != "") || (this.mAppInfo.getAppId() != null && this.mAppInfo.getAppId() != "")) {
            showProgressBarItem(true);
            updateAppDetailInfo();
        }
    }

    protected void onStart() {
        super.onStart();
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---onStart()---");
    }

    protected void onResume() {
        super.onResume();
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---onResume()---");
        registerObserver();
        if (this.mAppInfo != null) {
            LogUtils.m1739i(TAG, "AppStoreDetailActivity---onResume() >>>>  mAppInfo is not null---");
            refreshActionMode();
            refreshActionViews();
        }
    }

    protected void onPause() {
        super.onPause();
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---onPause()---");
        unregisterObserver();
    }

    protected void onStop() {
        super.onStop();
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---onStop()---");
    }

    private void release() {
        if (this.mHandler != null) {
            this.mHandler.removeMessages(1);
            this.mHandler.removeMessages(2);
            this.mHandler.removeMessages(3);
            this.mHandler.removeMessages(4);
            this.mHandler.removeMessages(5);
            this.mHandler.removeMessages(6);
            this.mHandler.removeMessages(7);
            this.mHandler.sendEmptyMessage(0);
        }
        this.mAppInfo = null;
        this.mProgressBarHandler = null;
        if (this.mRecommApps != null) {
            this.mRecommApps.clear();
            this.mRecommApps = null;
        }
        this.mDefautDrawable = null;
        this.mLeftArrowEnableDrawable = null;
        this.mLeftArrowUnableDrawable = null;
        this.mRightArrowEnableDrawable = null;
        this.mRightArrowUnableDrawable = null;
        unregisterReceiver();
        ContextHolder.setContext(null);
        if (this.mRecommAppAdapter != null) {
            this.mRecommAppAdapter.release();
            this.mRecommAppAdapter = null;
        }
        if (this.mAppThumbNailAdapter != null) {
            this.mAppThumbNailAdapter.release();
            this.mAppThumbNailAdapter = null;
        }
        this.mAppStoreClient = null;
        AppStoreClient.destroyInstance();
    }

    public void onDestroy() {
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---onDestroy()---");
        super.onDestroy();
        release();
    }

    private void initClient() {
        String deviceId = AppStoreClient.getDeviceID();
        new Builder().setDeviceID(deviceId).setAppStoreEntry("normal").setAppVersion(new StringBuilder(String.valueOf(AppCommUtils.getMyPackageInfo(this).versionCode)).toString()).setUserID(deviceId, null).setImei(deviceId).setCookie(null).setChannelID(CHANNEL_ID).setMacID(SysUtils.getMacAddr(this)).setOsVersion(new StringBuilder(String.valueOf(VERSION.SDK_INT)).toString()).setOsRooted(Boolean.valueOf(false)).setCharSet("UTF-8").setPlatform("3", Constants.PINGBACK_4_0_P_TV_APP).setUserPlatform("9").setProduct("312", PRODUCT).setServers("http://qiyu.ptqy.gitv.tv", "http://search.video.ptqy.gitv.tv", "http://msg.video.ptqy.gitv.tv", "http://msg.ptqy.gitv.tv", "http://store.ptqy.gitv.tv", "http://msg.71.am", "http://store.ptqy.gitv.tv").build();
    }

    private void initManagers() {
        ContextHolder.setContext(this);
        NetWorkManager.getInstance().initNetWorkManager(getApplicationContext());
        this.mAppStoreClient = AppStoreClient.getInstance();
        this.mAppDownloadDir = getFilesDir() + "/";
        this.mRecommApps = new ArrayList();
        initAppManager();
        initDownloadManager();
        DownloadManagerUtil.initDownloadManager();
    }

    public static List<String> getFilterPkgNameList() {
        if (filterPkgNameList == null) {
            setFilterPkgNameList();
        }
        return filterPkgNameList;
    }

    private static void setFilterPkgNameList() {
        if (filterPkgNameList == null) {
            filterPkgNameList = new ArrayList();
            filterPkgNameList.add("com.gitv.tvappstore");
            filterPkgNameList.add("com.tvos.tvappstore");
            filterPkgNameList.add(PlayerIntentConfig.URI_AUTH);
        }
    }

    public void initAppManager() {
        setFilterPkgNameList();
        mIAppManager = AppManager.createAppManager(getApplicationContext());
        mIAppManager.setBlackPkgList(filterPkgNameList);
    }

    public static IAppManager getAppManager() {
        return mIAppManager;
    }

    private void initDownloadManager() {
        mIDownloadManager = DownloadManagerFactory.createDownloadManager(getApplicationContext(), "downloadmanager.db");
    }

    public static IDownloadManager getDownloadManager() {
        return mIDownloadManager;
    }

    private void initProgressImage() {
        this.PROGRESS_IMAGES = new int[]{getResId("drawable", "apps_progress_0001"), getResId("drawable", "apps_progress_0002"), getResId("drawable", "apps_progress_0003"), getResId("drawable", "apps_progress_0004"), getResId("drawable", "apps_progress_0005"), getResId("drawable", "apps_progress_0006"), getResId("drawable", "apps_progress_0007"), getResId("drawable", "apps_progress_0008"), getResId("drawable", "apps_progress_0009"), getResId("drawable", "apps_progress_0010"), getResId("drawable", "apps_progress_0011"), getResId("drawable", "apps_progress_0012"), getResId("drawable", "apps_progress_0013"), getResId("drawable", "apps_progress_0014"), getResId("drawable", "apps_progress_0015"), getResId("drawable", "apps_progress_0016"), getResId("drawable", "apps_progress_0017"), getResId("drawable", "apps_progress_0018"), getResId("drawable", "apps_progress_0019"), getResId("drawable", "apps_progress_0020"), getResId("drawable", "apps_progress_0021"), getResId("drawable", "apps_progress_0022"), getResId("drawable", "apps_progress_0023"), getResId("drawable", "apps_progress_0024"), getResId("drawable", "apps_progress_0025"), getResId("drawable", "apps_progress_0026"), getResId("drawable", "apps_progress_0027"), getResId("drawable", "apps_progress_0028"), getResId("drawable", "apps_progress_0029"), getResId("drawable", "apps_progress_0030"), getResId("drawable", "apps_progress_0031"), getResId("drawable", "apps_progress_0032"), getResId("drawable", "apps_progress_0033"), getResId("drawable", "apps_progress_0034"), getResId("drawable", "apps_progress_0035"), getResId("drawable", "apps_progress_0036"), getResId("drawable", "apps_progress_0037")};
    }

    private void initLayout() {
        this.mImageBackGround = (ImageView) findViewById(getResId("id", "apps_bgView"));
        this.mDefautDrawable = getResources().getDrawable(getResId("drawable", "apps_image_default_bg"));
        this.mLeftArrowUnableDrawable = getResources().getDrawable(getResId("drawable", "apps_thumbnail_left"));
        this.mRightArrowUnableDrawable = getResources().getDrawable(getResId("drawable", "apps_thumbnail_right"));
        this.mLeftArrowEnableDrawable = getResources().getDrawable(getResId("drawable", "apps_thumbnail_left"));
        this.mRightArrowEnableDrawable = getResources().getDrawable(getResId("drawable", "apps_thumbnail_right"));
        this.mLayoutDetailView = (RelativeLayout) findViewById(getResId("id", "apps_view_details"));
        this.mImageAppIcon = (ImageView) findViewById(getResId("id", "apps_icon"));
        this.mImageProgress = (ImageView) findViewById(getResId("id", "apps_progress_image"));
        this.mTextProgress = (TextView) findViewById(getResId("id", "apps_progress_text"));
        this.mTextAppName = (TextView) findViewById(getResId("id", "apps_name"));
        this.mRatingAppGrade = (RatingBarView) findViewById(getResId("id", "apps_grade"));
        this.mTextAppCategory = (TextView) findViewById(getResId("id", "apps_category"));
        this.mTextAppDownload = (TextView) findViewById(getResId("id", "apps_download"));
        this.mTextAppSize = (TextView) findViewById(getResId("id", "apps_size"));
        this.mTextAppVersion = (TextView) findViewById(getResId("id", "apps_version"));
        this.mTextAppVersionTitle = (TextView) findViewById(getResId("id", "apps_version_title"));
        this.mTextDescription = (TextView) findViewById(getResId("id", "apps_details"));
        this.mProgressBarItem = (ProgressBarItem) findViewById(getResId("id", "apps_loading_progress"));
        this.mProgressBarItem.setText(getString(getResId("string", "apps_album_list_loading")));
        this.mLayoutActionView = (RelativeLayout) findViewById(getResId("id", "apps_view_actions"));
        RelativeLayout actionContainer = (RelativeLayout) findViewById(getResId("id", "apps_view_actions"));
        this.mActionFst = (ActionView) findViewById(getResId("id", "apps_action_fst"));
        this.mActionSnd = (ActionView) findViewById(getResId("id", "apps_action_snd"));
        this.mActionThr = (ActionView) findViewById(getResId("id", "apps_action_thr"));
        this.mActionFourth = (ActionView) findViewById(getResId("id", "apps_action_fourth"));
        this.mActionFst.setContainer(actionContainer);
        this.mActionSnd.setContainer(actionContainer);
        this.mActionThr.setContainer(actionContainer);
        this.mActionFourth.setContainer(actionContainer);
        this.mActionFst.getContent().setOnClickListener(this.mOnClickListener);
        this.mActionSnd.getContent().setOnClickListener(this.mOnClickListener);
        this.mActionThr.getContent().setOnClickListener(this.mOnClickListener);
        this.mActionFourth.getContent().setOnClickListener(this.mOnClickListener);
        this.mThumbnailVGridView = (VGridView) findViewById(getResId("id", "apps_tvos_thumbnail"));
        this.mRecommAppVGridView = (VGridView) findViewById(getResId("id", "apps_recomm_apps"));
        this.mRecommAppVGridView.setDescendantFocusability(SendFlag.FLAG_KEY_PINGBACK_MID);
        showProgressBarItem(false);
    }

    private void showProgressBarItem(boolean show) {
        int i;
        int i2 = 0;
        ProgressBarItem progressBarItem = this.mProgressBarItem;
        if (show) {
            i = 8;
        } else {
            i = 0;
        }
        progressBarItem.setVisibility(i);
        RelativeLayout relativeLayout = this.mLayoutActionView;
        if (show) {
            i = 0;
        } else {
            i = 8;
        }
        relativeLayout.setVisibility(i);
        RelativeLayout relativeLayout2 = this.mLayoutDetailView;
        if (!show) {
            i2 = 8;
        }
        relativeLayout2.setVisibility(i2);
    }

    private void updateAppDetailInfo() {
        Log.d(TAG, "updateAppDetailInfo:" + this.mAppInfo);
        this.mAppStoreClient.getAppDetail(this.mAppInfo.getAppId(), this.mAppInfo.getAppPackageName(), "", "basic,res", this.mAppDetailCallback);
    }

    private void updataAppDetail(final AppDetailInfo mAppInfo) {
        if (mAppInfo != null) {
            this.mAppInfo = mAppInfo;
            this.mAppVersion = mAppInfo.getAppVersion();
            this.mAppPackageName = mAppInfo.getAppPackageName();
            this.mAppDownloadUrl = mAppInfo.getAppDownloadUrl();
            this.mAppFileName = StringUtils.fetchAppFileName(this.mAppDownloadUrl);
            refreshActionMode();
            final IAppInfo localAppInfo = AppCommUtils.getLocalAppInfo(mAppInfo.getAppPackageName());
            runOnUiThread(new Runnable() {
                public void run() {
                    if (localAppInfo != null) {
                        AppStoreDetailActivity.this.refreshInstalledAppPage(mAppInfo, localAppInfo);
                    } else {
                        AppStoreDetailActivity.this.refreshOtherPage(mAppInfo);
                    }
                    AppStoreDetailActivity.this.refreshTextData(mAppInfo);
                    AppStoreDetailActivity.this.refreshAppThumbnail(mAppInfo.getAppImageUrl());
                    AppStoreDetailActivity.this.refreshActionViews();
                    AppStoreDetailActivity.this.refreshRecommdAppData();
                    if (AppStoreDetailActivity.this.getIntent().getBooleanExtra("click", false)) {
                        AppStoreDetailActivity.this.mActionFst.getContent().performClick();
                    }
                }
            });
            sendOnVisit("detail", "detail", "0");
        }
    }

    private void queryLocalAppShow(final String appPackageName) {
        final IAppInfo localAppInfo = AppCommUtils.getLocalAppInfo(appPackageName);
        if (localAppInfo != null) {
            refreshActionMode();
            runOnUiThread(new Runnable() {
                public void run() {
                    AppStoreDetailActivity.this.mTextAppName.setText(localAppInfo.getAppName());
                    AppStoreDetailActivity.this.mAppPackageName = appPackageName;
                    AppStoreDetailActivity.this.mAppInfo.setAppIsSystem(localAppInfo.isSystemApp());
                    AppStoreDetailActivity.this.mImageAppIcon.setImageDrawable(AppStoreDetailActivity.this.getLocalAppIcon(localAppInfo));
                    AppStoreDetailActivity.this.refreshActionViews();
                }
            });
        }
    }

    private void refreshInstalledAppPage(AppDetailInfo mAppInfo, IAppInfo localAppInfo) {
        int i;
        if (mAppInfo.isAppIsSystem()) {
            i = 7;
        } else {
            i = 3;
        }
        this.mActionMode = i;
        this.mImageAppIcon.setImageDrawable(getLocalAppIcon(localAppInfo));
    }

    private void refreshOtherPage(AppDetailInfo mAppInfo) {
        Picasso.with(getApplicationContext()).load(mAppInfo.getAppLogoUrl()).placeholder(getResId("drawable", "apps_ic_launcher")).fit().into(this.mImageAppIcon, this.mImgCallback);
    }

    private void refreshTextData(AppDetailInfo mAppInfo) {
        this.mTextAppName.setText(mAppInfo.getAppTitle());
        setCategoryType();
        this.mTextAppDownload.setText(mAppInfo.getAppNum() + getResources().getString(getResId("string", "times")));
        if (StringUtils.isEmpty(mAppInfo.getAppPublishTime())) {
            this.mTextAppVersionTitle.setVisibility(4);
        } else {
            this.mTextAppVersionTitle.setVisibility(0);
            this.mTextAppVersion.setText(StringUtils.TimeStampDate(mAppInfo.getAppPublishTime()));
        }
        this.mTextAppSize.setText(StringUtils.ByteToTrillion(mAppInfo.getApppackageSize()));
        this.mTextDescription.setText(StringUtils.ToHalfStr(mAppInfo.getAppDescription()));
        int dimen20Id = getResId("dimen", "dimen_20dp");
        this.mRatingAppGrade.setParams(getDimen(dimen20Id), getDimen(dimen20Id), Float.valueOf((new Random().nextFloat() * 2.0f) + 3.0f).floatValue(), 5);
    }

    private void refreshAppThumbnail(List<String> listUrl) {
        if (CommonUtils.isListEmpty(listUrl)) {
            this.mThumbnailVGridView.setVisibility(8);
            return;
        }
        this.mThumbnailVGridView.setVisibility(0);
        this.mThumbnailVGridView.setArrayType(1, 1);
        this.mThumbnailVGridView.setScrollMarginLeft(12);
        this.mThumbnailVGridView.setHorizontalSpace(12);
        this.mAppThumbNailAdapter = new AppDetailThumbNailAdapter(listUrl, this, this.mDefautDrawable, this.mLeftArrowUnableDrawable, this.mLeftArrowEnableDrawable, this.mRightArrowUnableDrawable, this.mRightArrowEnableDrawable, this.mRootView);
        this.mAppThumbNailAdapter.setOnItemClickedListener(new OnItemClickedListener() {
            public void onItemClicked(View v, int position) {
                AppStoreDetailActivity.this.sendOnClick("detail", APIConstants.BLOCK_PREVIEW, "1-" + (position + 1));
            }
        });
        this.mThumbnailVGridView.setOnItemFocusChangeListener(new GridViewListener());
        this.mThumbnailVGridView.setAdapter(this.mAppThumbNailAdapter);
        this.mThumbnailVGridView.setNextFocusRightId(this.mThumbnailVGridView.getId());
    }

    private void refreshRecommdApp(final ArrayList<RecommendApp> mRecommApps) {
        if (mRecommApps.isEmpty()) {
            this.mRecommAppVGridView.setVisibility(8);
            return;
        }
        this.mRecommAppVGridView.setVisibility(0);
        this.mRecommAppVGridView.setArrayType(1, 1);
        this.mRecommAppVGridView.setHorizontalSpace(10);
        this.mRecommAppVGridView.setScrollMarginLeft(10);
        this.mRecommAppAdapter = new AppDetailRecommdAdapter(this, this.mDefautDrawable, mRecommApps, new OnClickListener() {
            public void onClick(View v) {
                int position = ((Integer) v.getTag(AppDetailRecommdAdapter.TAG_POSITION)).intValue();
                AppStoreDetailActivity.this.sendOnClick("detail", APIConstants.BLOCK_RECOMMENDED, "1-" + (position + 1));
                RecommendApp recommApp = (RecommendApp) mRecommApps.get(position);
                Intent intent = new Intent(AppStoreDetailActivity.this.getApplicationContext(), AppStoreDetailActivity.class);
                intent.putExtra(APIConstants.BUNDLE_EXTRA_DETAILAPP_APP_PKG, recommApp.package_name);
                intent.putExtra("appid", "");
                intent.putExtra("deviceid", AppStoreClient.getDeviceID());
                intent.putExtra("uuid", AppStoreClient.getUUID());
                AppStoreDetailActivity.this.startActivity(intent);
            }
        });
        this.mRecommAppVGridView.setOnItemFocusChangeListener(new GridViewListener());
        this.mRecommAppVGridView.setAdapter(this.mRecommAppAdapter);
    }

    private void refreshActionMode() {
        if (this.mAppInfo.isAppIsSystem()) {
            this.mActionMode = 7;
        } else if (!AppCommUtils.isAppInstalled(this.mAppPackageName)) {
            int state = DownloadManagerUtil.queryDownloadState(this.mAppDownloadDir + this.mAppFileName);
            if (state == 8) {
                if (AppCommUtils.isCompleteAppPkgExist(this.mAppDownloadDir + this.mAppFileName)) {
                    this.mActionMode = 2;
                } else {
                    this.mActionMode = 1;
                }
            } else if (state == 1) {
                update(null, new DownloadState(this.mAppDownloadDir + this.mAppFileName, 1, 0, null));
            }
        } else if (AppCommUtils.isAppHasUpgrade(this, this.mAppVersion, this.mAppPackageName)) {
            this.mActionMode = 5;
        } else if (this.mAppInfo.getAppId() == null || this.mAppInfo.getAppId().equals("")) {
            this.mActionMode = 3;
        } else {
            this.mActionMode = 3;
        }
    }

    private void refreshActionViews() {
        if (this.mActionMode == 1) {
            this.mActionFst.setImageResource(getResId("drawable", "apps_bt_download"));
            this.mActionFst.setTextResource(getResId("string", "apps_appdetails_action_download"));
            this.mActionSnd.setVisibility(8);
            this.mActionThr.setVisibility(8);
            this.mActionFourth.setVisibility(8);
        } else if (this.mActionMode == 2) {
            this.mActionFst.setImageResource(getResId("drawable", "apps_bt_download"));
            this.mActionFst.setTextResource(getResId("string", "apps_appdetails_action_install"));
            this.mActionSnd.setImageResource(getResId("drawable", "apps_bt_clear"));
            this.mActionSnd.setTextResource(getResId("string", "apps_appdetails_action_clear"));
            this.mActionSnd.setVisibility(0);
            this.mActionThr.setVisibility(8);
            this.mActionFourth.setVisibility(8);
        } else if (this.mActionMode == 3) {
            this.mActionFst.setImageResource(getResId("drawable", "apps_bt_open"));
            this.mActionFst.setTextResource(getResId("string", "apps_appdetails_action_open"));
            this.mActionSnd.setImageResource(getResId("drawable", "apps_bt_clear"));
            this.mActionSnd.setTextResource(getResId("string", "apps_appdetails_action_uninstall"));
            this.mActionSnd.setVisibility(0);
            this.mActionThr.setVisibility(8);
            this.mActionFourth.setVisibility(8);
        } else if (this.mActionMode == 5) {
            this.mActionFst.setImageResource(getResId("drawable", "apps_bt_open"));
            this.mActionFst.setTextResource(getResId("string", "apps_appdetails_action_open"));
            this.mActionSnd.setImageResource(getResId("drawable", "apps_bt_clear"));
            this.mActionSnd.setTextResource(getResId("string", "apps_appdetails_action_uninstall"));
            this.mActionThr.setImageResource(getResId("drawable", "apps_bt_update"));
            this.mActionThr.setTextResource(getResId("string", "apps_appdetails_action_update"));
            this.mActionSnd.setVisibility(0);
            this.mActionThr.setVisibility(0);
            this.mActionFourth.setVisibility(8);
        } else if (this.mActionMode == 7) {
            this.mActionFst.setImageResource(getResId("drawable", "apps_bt_open"));
            this.mActionFst.setTextResource(getResId("string", "apps_appdetails_action_open"));
            this.mActionSnd.setVisibility(8);
            this.mActionThr.setVisibility(8);
            this.mActionFourth.setVisibility(8);
        } else if (this.mActionMode == 4) {
            this.mActionFst.setImageResource(getResId("drawable", "apps_bt_open"));
            this.mActionFst.setTextResource(getResId("string", "apps_appdetails_action_open"));
            this.mActionSnd.setImageResource(getResId("drawable", "apps_bt_clear"));
            this.mActionSnd.setTextResource(getResId("string", "apps_appdetails_action_uninstall"));
            this.mActionThr.setImageResource(getResId("drawable", "apps_bt_stick"));
            this.mActionThr.setTextResource(getResId("string", "apps_appdetails_action_on_top"));
            this.mActionFst.setVisibility(0);
            this.mActionSnd.setVisibility(0);
            this.mActionThr.setVisibility(0);
            this.mActionFourth.setVisibility(8);
        } else if (this.mActionMode == 6) {
            this.mActionFst.setImageResource(getResId("drawable", "apps_bt_open"));
            this.mActionFst.setTextResource(getResId("string", "apps_appdetails_action_open"));
            this.mActionSnd.setImageResource(getResId("drawable", "apps_bt_clear"));
            this.mActionSnd.setTextResource(getResId("string", "apps_appdetails_action_uninstall"));
            this.mActionThr.setImageResource(getResId("drawable", "apps_bt_update"));
            this.mActionThr.setTextResource(getResId("string", "apps_appdetails_action_update"));
            this.mActionFourth.setImageResource(getResId("drawable", "apps_bt_stick"));
            this.mActionFourth.setTextResource(getResId("string", "apps_appdetails_action_on_top"));
            this.mActionFst.setVisibility(0);
            this.mActionSnd.setVisibility(0);
            this.mActionThr.setVisibility(0);
            this.mActionFourth.setVisibility(0);
        }
        this.mProgressBarHandler.stopProgressBar();
        this.mProgressBarHandler.hideProgressBar();
    }

    private void refreshRecommdAppData() {
        LogUtils.m1737d(TAG, "AppStoreDetailActivity---refreshRecommd() >>>> refresh Recommd app data----" + this.mAppInfo.getAppId());
        this.mAppStoreClient.getAssociatedAppsAndDetail(Constants.RECOMMEND_AREA_GUEST_YOU_LIKE_TV, "0", this.mAppInfo.getAppId(), 10, this.mRecommAppCallback);
    }

    private void refreshFstActionBackGround(final int imgResId, final int txtResId) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    AppStoreDetailActivity.this.mActionFst.setImageResource(imgResId);
                    AppStoreDetailActivity.this.mActionFst.setTextResource(txtResId);
                    AppStoreDetailActivity.this.mActionFst.invalidate();
                } catch (Exception e) {
                    LogUtils.m1738e(AppStoreDetailActivity.TAG, "AppStoreDetailActivity---refreshFstActionBackGround() >>>> Exception:" + e.getMessage());
                }
            }
        });
    }

    private boolean isNetConnected() {
        if (DataCache.getNetState() == 0 && !AppCommUtils.isNetError()) {
            return true;
        }
        LogUtils.m1739i(TAG, "AppStoreDetailActivity---isNetConnected() >>>> -network is not connect---");
        Toast.makeText(this, getResId("string", "apps_netstate_error"), 0).show();
        return false;
    }

    private void setCategoryType() {
        this.mTextAppCategory.setText("暂无分类");
        this.mAppStoreClient.getAppCategiries(0, this.mUpdateCategiriesCallback);
    }

    private void refreshDownloadFailed(String name) {
        showToast(new StringBuilder(String.valueOf(name)).append("下载失败,请稍后下载").toString());
        sendAppDownloadFailedPingBack("detail", "download", "1");
        runOnUiThread(new Runnable() {
            public void run() {
                AppStoreDetailActivity.this.refreshActionMode();
                AppStoreDetailActivity.this.refreshActionViews();
                AppStoreDetailActivity.this.mDownloadClickable = true;
                AppStoreDetailActivity.this.mUpgradeClickable = true;
            }
        });
    }

    private void refreshDownlaodSuccess(final String path) {
        sendAppDownloadedPingBack("detail", "download", "1");
        LogUtils.m1739i(TAG, "refreshDownlaodSuccess() >>>> 下载完成 pingback  onAppDownloaded()----");
        runOnUiThread(new Runnable() {
            public void run() {
                AppStoreDetailActivity.this.refreshActionMode();
                AppStoreDetailActivity.this.refreshActionViews();
                AppStoreDetailActivity.this.mDownloadClickable = true;
                AppStoreDetailActivity.this.mUpgradeClickable = true;
                if (AppStoreDetailActivity.this.rootFile(path)) {
                    Log.e(AppStoreDetailActivity.TAG, "refreshDownlaodSuccess()>>>> rootFile() permissiontest---获权成功,可以打开应用");
                    AppStoreDetailActivity.this.openFileInstall(path);
                    return;
                }
                Log.e(AppStoreDetailActivity.TAG, "refreshDownlaodSuccess()>>>> rootFile() permissiontest---获权失败");
            }
        });
    }

    private void grayTransformation(Drawable d) {
        d.setColorFilter(new ColorMatrixColorFilter(this.mGrayColorMatrix));
    }

    private void normalTransformation(Drawable d) {
        d.setColorFilter(new ColorMatrixColorFilter(this.mNormalColorMatrix));
    }

    private void openFileInstall(String path) {
        this.mProgressBarHandler.startProgressBar(200, getResId("string", "apps_appdetails_progress_installing"));
        this.mDownloadClickable = true;
        this.mUpgradeClickable = true;
        sendInstallAppPingBack("detail", "install", "5");
        if (!mIAppManager.installApp(path)) {
            this.mProgressBarHandler.hideProgressBar();
        }
        refreshFstActionBackGround(getResId("drawable", "apps_bt_download"), getResId("string", "apps_appdetails_action_installing"));
    }

    private void updateApp() {
        if (StringUtils.isEmpty(this.mAppPackageName) || !AppCommUtils.isAppInstalled(this.mAppPackageName)) {
            Toast.makeText(this, "应用不存在", 0).show();
        } else {
            updateDownloadApp();
        }
        sendUpdateAppPingBack("detail", "update", "3");
        sendOnClick("detail", "update", "3");
        Log.i(TAG, "AppStoreDetailActivity-- updateApp() >>>>  update app ---");
    }

    private void downloadApp() {
        if (StringUtils.isEmpty(this.mAppDownloadUrl)) {
            LogUtils.m1737d(TAG, "AppStoreDetailActivitydownloadApp() >>>> mAppDownloadUrl is null");
            this.mDownloadClickable = true;
            return;
        }
        download();
        this.mDownloadClickable = true;
    }

    private void download() {
        switch (DownloadManagerUtil.download(this.mAppDownloadDir + this.mAppFileName, this.mAppDownloadUrl, this.mAppInfo.getAppTitle())) {
            case 4:
                Toast.makeText(this, "正在下载", 0).show();
                return;
            case 5:
                Log.i(TAG, "downloadApp()  文件存在");
                openFileInstall(this.mAppDownloadDir + this.mAppFileName);
                return;
            case 7:
                refreshFstActionBackGround(getResId("drawable", "apps_bt_download"), getResId("string", "apps_appdetails_action_download"));
                Toast.makeText(this, "下载队列已满，请稍后点击", 0).show();
                return;
            default:
                onDownloadStart();
                return;
        }
    }

    private void updateDownloadApp() {
        Log.i(TAG, "AppStoreDetailActivity---updateDownloadApp() ---");
        if (StringUtils.isEmpty(this.mAppDownloadUrl)) {
            LogUtils.m1737d(TAG, "---updateDownloadApp() >>>> mAppDownloadUrl is null");
            this.mUpgradeClickable = true;
            return;
        }
        download();
    }

    private void installApp() {
        this.mProgressBarHandler.startProgressBar(200, getResId("string", "apps_appdetails_progress_installing"));
        LogUtils.m1737d(TAG, "----installApp() package name:" + this.mAppPackageName);
        openFileInstall(this.mAppDownloadDir + this.mAppFileName);
        sendOnClick("detail", "install", "5");
    }

    private void uninstallApp() {
        LogUtils.m1737d(TAG, "----uninstallApp() package name:" + this.mAppPackageName);
        if (this.mAppInfo.isAppIsSystem() || !AppCommUtils.isAppInstalled(this.mAppPackageName)) {
            Toast.makeText(this, "应用不存在", 0).show();
            return;
        }
        sendUnInstallAppPingBack("detail", "remove", "4");
        sendOnClick("detail", "remove", "4");
        mIAppManager.uninstallApp(this.mAppPackageName);
    }

    private void openApp() {
        if (StringUtils.isEmpty(this.mAppPackageName) || !AppCommUtils.isAppInstalled(this.mAppPackageName)) {
            Toast.makeText(this, "应用不存在", 0).show();
            return;
        }
        mIAppManager.startApp(this.mAppPackageName);
        Intent intent = new Intent(APIConstants.BROADCAST_FILTER_ACTION);
        intent.putExtra(APIConstants.BROADCAST_EXTRA_COMM, 5);
        intent.putExtra(APIConstants.BROADCAST_EXTRA_PKG_NAME, this.mAppPackageName);
        sendBroadcast(intent);
        sendLaunchAppPingBack("detail", "open", "2");
        sendOnClick("detail", "open", "2");
    }

    @SuppressLint({"NewApi"})
    private void clearApp() {
        try {
            if (!(this.mAppFileName == null || this.mAppFileName.isEmpty())) {
                File file = new File(this.mAppDownloadDir, this.mAppFileName);
                if (file.exists()) {
                    file.delete();
                    sendClearAppPingBack("detail", "dedone", "6");
                    sendOnClick("detail", "dedone", "6");
                }
            }
            refreshActionMode();
            refreshActionViews();
            Intent intent = new Intent(APIConstants.BROADCAST_FILTER_ACTION);
            intent.putExtra(APIConstants.BROADCAST_EXTRA_COMM, 8);
            intent.putExtra(APIConstants.BROADCAST_EXTRA_PKG_NAME, this.mAppInfo.getAppPackageName());
            sendBroadcast(intent);
        } catch (Exception e) {
            LogUtils.m1738e(TAG, "AppStoreDetailActivity---clearApp() >>>> removeFile--" + e.getMessage());
        }
    }

    @SuppressLint({"NewApi"})
    protected void refreshPackageAdded(String str) {
        sendAppInstalledPingBack("detail", "install", "5");
        LogUtils.m1739i(TAG, "refreshPackageAdded(),add packageName is," + str);
        if (!StringUtils.isEmpty(str)) {
            if (!StringUtils.isEmpty(this.mAppPackageName)) {
                if (str.equals(this.mAppPackageName)) {
                    LogUtils.m1739i(TAG, "refreshPackageAdded()  package is equals str:" + str);
                    refreshActionMode();
                    runOnUiThread(new Runnable() {
                        public void run() {
                            AppStoreDetailActivity.this.refreshActionViews();
                        }
                    });
                }
                try {
                    if (this.mAppFileName == null || this.mAppFileName.isEmpty()) {
                        Log.d(TAG, "AppStoreDetailActivityfile is not download");
                        return;
                    }
                    File file = new File(this.mAppDownloadDir, this.mAppFileName);
                    if (file.exists()) {
                        file.delete();
                        Log.d(TAG, "AppStoreDetailActivityfile is delete");
                        return;
                    }
                    return;
                } catch (Exception e) {
                    LogUtils.m1738e(TAG, "AppStoreDetailActivity---refreshPackageAdded() >>>> removeFile--" + e.getMessage());
                    return;
                }
            }
        }
        LogUtils.m1739i(TAG, "refreshPackageAdded(),  package is null");
    }

    protected void refreshPackageRemoved(String str) {
        sendAppUninstalledPingBack("detail", "remove", "4");
        LogUtils.m1739i(TAG, "AppStoreDetailActivity----refreshPackageRemoved() >>>> remove packageName:" + str);
        if (str.equals(this.mAppPackageName)) {
            refreshActionMode();
            runOnUiThread(new Runnable() {
                public void run() {
                    AppStoreDetailActivity.this.refreshActionViews();
                }
            });
        }
    }

    protected void refreshPackageReplaced(String str) {
        sendAppUpdatedPingBack("detail", "update", "3");
        LogUtils.m1739i(TAG, "AppStoreDetailActivity---refreshPackageReplaced() >>>>  更新 update packageName :" + str);
    }

    private boolean isInstalledApp(String packagename) {
        if (StringUtils.isEmpty(this.mAppPackageName) || mIAppManager == null || mIAppManager.getOriginAppInfoByPkg(this.mAppPackageName) == null) {
            return false;
        }
        return true;
    }

    private boolean rootFile(String filePath) {
        return RootCmdUtils.runCommand("chmod 775 " + filePath + "\n");
    }

    private boolean isAppInfoNull() {
        if (this.mAppInfo == null) {
            return false;
        }
        if ((this.mAppInfo.getAppId() == null || this.mAppInfo.getAppId().equals("")) && (this.mAppInfo.getAppVersion() == null || this.mAppInfo.getAppVersion().equals(""))) {
            return false;
        }
        return true;
    }

    private Drawable getLocalAppIcon(IAppInfo appInfo) {
        if (appInfo != null) {
            LogUtils.m1737d(TAG, "getDrawableIcon () >>>>  DwawableIcon is not null---");
            return appInfo.getAppIcon();
        }
        LogUtils.m1737d(TAG, "getDrawableIcon() >>>> DataCache.CACHE_MYAPP_LIST is null");
        return getResources().getDrawable(getResId("drawable", "apps_ic_launcher"));
    }

    protected void showToast(final String str) {
        if (!StringUtils.isEmpty(str)) {
            runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(AppStoreDetailActivity.this, str, 0).show();
                }
            });
        }
    }

    public AppDetailInfo createAppInfo(AppDetail detail) {
        AppDetailInfo appDetailInfo = new AppDetailInfo();
        appDetailInfo.setAppPackageName(detail.basic.app_package_name);
        appDetailInfo.setAppId(detail.basic.app_id);
        appDetailInfo.setAppLogoUrl(detail.basic.app_logo);
        appDetailInfo.setAppDescription(detail.basic.app_desc);
        appDetailInfo.setAppNum(detail.basic.total_download);
        appDetailInfo.setApppackageSize(String.valueOf(detail.basic.app_package_size));
        appDetailInfo.setAppRating(detail.basic.total_rate);
        appDetailInfo.setAppTitle(detail.basic.app_name);
        appDetailInfo.setAppType(detail.basic.app_type);
        appDetailInfo.setAppCategory(detail.basic.app_top_category);
        appDetailInfo.setAppDownloadUrl(detail.basic.app_download_url);
        appDetailInfo.setAppImageUrl(getAppImageUrl(detail.res));
        appDetailInfo.setAppPublishTime(detail.basic.publish_time);
        appDetailInfo.setAppVersion(detail.basic.latest_version);
        return appDetailInfo;
    }

    private List<String> getAppImageUrl(ArrayList<ResInfo> resImageInfos) {
        if (CommonUtils.isListEmpty(resImageInfos)) {
            LogUtils.m1737d(TAG, "AppStoreDetailActivity---getAppImageUrl() mediaURLs is null ---");
            return null;
        }
        int resImageSize = resImageInfos.size();
        List<String> appImageLists = new ArrayList();
        if (resImageSize > 5) {
            resImageSize = 5;
        }
        for (int i = 0; i < resImageSize; i++) {
            appImageLists.add(((ResInfo) resImageInfos.get(i)).media_url);
        }
        return appImageLists;
    }

    private void registerReceiver() {
        Log.d(TAG, "registerReceiver");
        try {
            registerReceiver(this.mReceiver, new IntentFilter(APIConstants.BROADCAST_FILTER_ACTION));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void unregisterReceiver() {
        Log.d(TAG, "unregisterReceiver");
        try {
            unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerObserver() {
        DownloadManagerUtil.registerObserver(this);
        if (mIAppManager != null) {
            mIAppManager.addObserver(this);
        }
    }

    private void unregisterObserver() {
        DownloadManagerUtil.unregisterObserver(this);
        if (mIAppManager != null) {
            mIAppManager.deleteObserver(this);
        }
    }

    public void update(Observable observable, Object data) {
        if (data instanceof Bundle) {
            if (this.mHandler != null) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(3, data));
            }
        } else if ((data instanceof DownloadState) && this.mHandler != null) {
            this.mHandler.sendMessage(this.mHandler.obtainMessage(4, data));
        }
    }

    private void refreshAppState(Object data) {
        int action = ((Bundle) data).getInt("action");
        String pkgname = ((Bundle) data).getString(SettingConstants.ACTION_TYPE_PACKAGE_NAME);
        if (pkgname != null && pkgname.equals(this.mAppPackageName)) {
            switch (action) {
                case 0:
                    Log.d(TAG, "PACKAGE_ADDED");
                    refreshPackageAdded(this.mAppPackageName);
                    return;
                case 1:
                    Log.d(TAG, "PACKAGE_REMOVED");
                    refreshPackageRemoved(this.mAppPackageName);
                    return;
                case 2:
                    refreshPackageReplaced(this.mAppPackageName);
                    return;
                default:
                    return;
            }
        }
    }

    private void refreshDownloadState(Object data) {
        DownloadState state = (DownloadState) data;
        if (state.filePath != null && state.filePath.equals(this.mAppDownloadDir + this.mAppFileName)) {
            switch (state.operation) {
                case 0:
                    onDownloadProcess(state);
                    return;
                case 1:
                    onDownloadStart();
                    return;
                case 2:
                    onDownloadComplete(state);
                    return;
                case 4:
                    Log.d(TAG, "DOWNLOAD_OPERATION_ON_ERROR " + state.reason);
                    refreshDownloadFailed(this.mAppInfo.getAppTitle());
                    return;
                case 5:
                    onDownloadStop();
                    return;
                default:
                    return;
            }
        }
    }

    private void onDownloadProcess(DownloadState state) {
        Log.d(TAG, "DOWNLOAD_OPERATION_ON_PROCESS = " + state.process);
        int progress = state.process;
        try {
            refreshFstActionBackGround(getResId("drawable", "apps_bt_download"), getResId("string", "apps_appdetails_action_downloading"));
            if (this.mActionMode == 1) {
                this.mProgressBarHandler.setProgressPercent(progress, getResId("string", "apps_appdetails_progress_downloading"));
            } else if (this.mActionMode == 5 || this.mActionMode == 6) {
                this.mProgressBarHandler.setProgressPercent(progress, getResId("string", "apps_appdetails_progress_updating"));
            }
        } catch (Exception e) {
            LogUtils.m1739i(TAG, "onProgress():" + progress + e.getMessage());
        }
    }

    private void onDownloadStop() {
        Log.d(TAG, "DOWNLOAD_OPERATION_ON_STOP ");
        sendAppDownloadCanceledPingBack("detail", "download", "1");
        sendOnClick("detail", "download", "1");
    }

    private void onDownloadComplete(DownloadState state) {
        Log.d(TAG, "DOWNLOAD_OPERATION_ON_COMPLETE ");
        this.mProgressBarHandler.stopProgressBar();
        refreshDownlaodSuccess(state.filePath);
        Intent intent = new Intent(APIConstants.BROADCAST_FILTER_ACTION);
        intent.putExtra(APIConstants.BROADCAST_EXTRA_COMM, 7);
        sendBroadcast(intent);
    }

    private void onDownloadStart() {
        Log.d(TAG, "DOWNLOAD_OPERATION_ON_START");
        refreshFstActionBackGround(getResId("drawable", "apps_bt_download"), getResId("string", "apps_appdetails_action_downloading"));
        if (this.mActionMode == 1) {
            this.mProgressBarHandler.startProgressBar(0, getResId("string", "apps_appdetails_progress_downloading"));
        } else if (this.mActionMode == 5 || this.mActionMode == 6) {
            this.mProgressBarHandler.startProgressBar(0, getResId("string", "apps_appdetails_progress_updating"));
        }
        sendAppDownloadAppPingBack("detail", "download", "1");
        sendOnClick("detail", "download", "1");
    }

    private void sendInstallAppPingBack(String rpage, String block, String rseat) {
        if (isAppInfoNull()) {
            this.mAppStoreClient.onInstallApp(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendInstallAppPingBack", "onInstallApp"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppInstalledPingBack()-> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppInstalledPingBack(String rpage, String block, String rseat) {
        if (isAppInfoNull()) {
            this.mAppStoreClient.onAppInstalled(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), this.mCallback);
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppInstalledPingBack()-> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppUninstalledPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppUninstalledPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onAppUninstalled(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), this.mCallback);
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppUninstalledPingBack()-> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendUnInstallAppPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendUnInstallAppPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onUninstallApp(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendUnInstallAppPingBack", "onUninstallApp"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendUnInstallAppPingBack()-> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppDownloadedPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadedPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onAppDownloaded(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendAppDownloadedPingBack", "onAppDownloaded"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadedPingBack()-> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppDownloadAppPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadAppPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onDownloadApp(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendAppDownloadAppPingBack", "onDownloadApp"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadAppPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppDownloadCanceledPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadCanceledPingBack() >>>> cancel download Task isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onAppDownloadCanceled(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, new CommResponseCallback(TAG, "sendAppDownloadCanceledPingBack", "onAppDownloadCanceled"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadCanceledPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppDownloadFailedPingBack(String rpage, String block, String rseat) {
        if (isAppInfoNull()) {
            LogUtils.m1739i(TAG, "AppStoreDetailActivity---sendAppDownloadFailedPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
            String str = rpage;
            String str2 = block;
            String str3 = rseat;
            this.mAppStoreClient.onAppDownloadFailed(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), str, str2, str3, "", this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendAppDownloadFailedPingBack", "onAppDownloadFailed"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadFailedPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendUpdateAppPingBack(String rpage, String block, String rseat) {
        if (isAppInfoNull()) {
            this.mAppStoreClient.onUpdateApp(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendUpdateAppPingBack", "onUpdateApp"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendUpdateAppPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppUpdatedPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppUpdatedPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onAppUpdated(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), this.mCallback);
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppDownloadFailedPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendClearAppPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendClearAppPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onRemoveApp(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendClearAppPingBack", "onRemoveApp"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendClearAppPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendLaunchAppPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendLaunchAppPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onLaunchApp(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, new CommResponseCallback(TAG, "sendLaunchAppPingBack", "onLaunchApp"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendLaunchAppPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendAppUnstallFailedPingBack(String rpage, String block, String rseat) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppUnstallFailedPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            String str = rpage;
            String str2 = block;
            String str3 = rseat;
            this.mAppStoreClient.onAppUninstallFailed(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), str, str2, str3, this.mAppInfo.getAppType(), "", new CommResponseCallback(TAG, "sendAppUnstallFailedPingBack", "onAppUninstallFailed"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendAppUnstallFailedPingBack()-> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendDownloadRecommendedPingBack(RecommendResponse resp, String area, String rank, String taid, String aid) {
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendDownloadRecommendedPingBack()-> isAppInfoNull:" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onDownloadRecommendedApp(resp, area, rank, taid, aid, new CommResponseCallback(TAG, "sendDownloadRecommendedPingBack", "onDownloadRecommendedApp"));
            return;
        }
        Log.e(TAG, "AppStoreDetailActivity---sendDownloadRecommendedPingBack() >>>> isAppInfoNull:" + isAppInfoNull());
    }

    private void sendOnClick(String rpage, String block, String rseat) {
        LogUtils.m1739i(TAG, "AppStoreDetailActivity---sendOnClick () pingback on click" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onClick(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendOnClick", "onClick"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---sendOnClick()  click not result:" + isAppInfoNull());
    }

    private void sendOnVisit(String rpage, String block, String rseat) {
        LogUtils.m1739i(TAG, "AppStoreDetailActivity---sendOnVisit() >>>> pingback on Visit,isAppInfoNull" + isAppInfoNull());
        if (isAppInfoNull()) {
            this.mAppStoreClient.onVisit(this.mAppInfo.getAppId(), this.mAppInfo.getAppVersion(), rpage, block, rseat, this.mAppInfo.getAppTitle(), new CommResponseCallback(TAG, "sendOnVisit", "onVisit"));
            return;
        }
        LogUtils.m1738e(TAG, "AppStoreDetailActivity---mAppInfo is null,sendOnVisit() >>>> pingback on Visit not result isAppInfoNull:" + isAppInfoNull());
    }
}
