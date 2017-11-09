package com.gala.video.app.epg.ui.setting;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.IImageProvider;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.video.app.epg.C0508R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.widget.ProgressBarItem;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.List;

public class ConcernWeChatActivity extends QMultiScreenActivity {
    private static final String LOG_TAG = "EPG/ConcernWeChatActivity";
    private static final long PROGRESSBAR_DELAY_MILLIS = 1500;
    private static final boolean mIsProgressBarDelayed = true;
    private IDynamicResult mDynamicResult;
    private View mErrorLayout;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private IImageProvider mImageProvider;
    private ImageView mImageView;
    private ProgressBarItem mProgressBar;
    private Runnable mShowProgressBarRunnable = new C10415();

    class C10371 implements Runnable {
        C10371() {
        }

        public void run() {
            ConcernWeChatActivity.this.loadData();
        }
    }

    class C10382 implements IImageCallback {
        C10382() {
        }

        public void onSuccess(ImageRequest imageRequest, Bitmap bitmap) {
            ConcernWeChatActivity.this.showBitmap(bitmap);
        }

        public void onFailure(ImageRequest imageRequest, Exception e) {
            ConcernWeChatActivity.this.showErrorLayout();
        }
    }

    class C10393 implements Runnable {
        C10393() {
        }

        public void run() {
            ConcernWeChatActivity.this.mImageView.setVisibility(4);
            ConcernWeChatActivity.this.mProgressBar.setVisibility(4);
            if (ConcernWeChatActivity.this.mErrorLayout == null) {
                ConcernWeChatActivity.this.mErrorLayout = ((ViewStub) ConcernWeChatActivity.this.findViewById(C0508R.id.epg_concern_wechat_error_layout_viewstub)).inflate().findViewById(C0508R.id.epg_concern_wechat_error_layout);
                ((TextView) ConcernWeChatActivity.this.mErrorLayout.findViewById(C0508R.id.epg_concern_wechat_error_tv)).setTextColor(ResourceUtil.getColor(C0508R.color.albumview_yellow_color));
                ((ImageView) ConcernWeChatActivity.this.mErrorLayout.findViewById(C0508R.id.epg_concern_wechat_error_iv)).setImageDrawable(ConcernWeChatActivity.this.getResources().getDrawable(C0508R.drawable.epg_no_album_text_warn));
            }
            ConcernWeChatActivity.this.mErrorLayout.setVisibility(0);
        }
    }

    class C10415 implements Runnable {
        C10415() {
        }

        public void run() {
            if (ConcernWeChatActivity.this.mProgressBar != null) {
                ConcernWeChatActivity.this.mProgressBar.setVisibility(0);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(C0508R.layout.epg_activity_concern_wechat);
        initView();
        ThreadUtils.execute(new C10371());
    }

    private void loadData() {
        this.mDynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (this.mDynamicResult == null) {
            showErrorLayout();
            return;
        }
        Bitmap bitmap = fetchBitmapFromLocal();
        if (bitmap == null) {
            setBitmapFromServer();
        } else {
            showBitmap(bitmap);
        }
    }

    private void setBitmapFromServer() {
        this.mImageProvider = ImageProviderApi.getImageProvider();
        LogUtils.m1576i(LOG_TAG, "setBitmapFromServer --- mDynamicResult.screenWeChatInteractive = ", this.mDynamicResult.getScreenWeChatInteractive());
        this.mImageProvider.loadImage(new ImageRequest(url, this.mImageView), new C10382());
    }

    private void showErrorLayout() {
        this.mHandler.removeCallbacks(this.mShowProgressBarRunnable);
        LogUtils.m1571e(LOG_TAG, "showErrorLayout");
        runOnUiThread(new C10393());
    }

    private void showBitmap(final Bitmap bitmap) {
        this.mHandler.removeCallbacks(this.mShowProgressBarRunnable);
        LogUtils.m1576i(LOG_TAG, "showBitmap --- bitmap = ", bitmap);
        if (bitmap != null) {
            runOnUiThread(new Runnable() {
                public void run() {
                    ConcernWeChatActivity.this.mImageView.setImageBitmap(bitmap);
                    ConcernWeChatActivity.this.mImageView.setVisibility(0);
                    ConcernWeChatActivity.this.mProgressBar.setVisibility(4);
                    if (ConcernWeChatActivity.this.mErrorLayout != null) {
                        ConcernWeChatActivity.this.mErrorLayout.setVisibility(4);
                    }
                }
            });
        }
    }

    private Bitmap fetchBitmapFromLocal() {
        List paths = this.mDynamicResult.getScreenWechatInteractiveImagePath();
        if (ListUtils.isEmpty(paths)) {
            return null;
        }
        CharSequence path = (String) paths.get(0);
        if (StringUtils.isEmpty(path)) {
            return null;
        }
        LogUtils.m1576i(LOG_TAG, "fetchBitmap --- path = ", path);
        return BitmapFactory.decodeFile(path);
    }

    private void initView() {
        this.mImageView = (ImageView) findViewById(C0508R.id.epg_concern_wechat_iv);
        this.mProgressBar = (ProgressBarItem) findViewById(C0508R.id.epg_concern_wechat_progressbar);
        this.mProgressBar.setText(ResourceUtil.getStr(C0508R.string.album_list_loading));
        this.mImageView.setVisibility(4);
        this.mHandler.postDelayed(this.mShowProgressBarRunnable, PROGRESSBAR_DELAY_MILLIS);
    }

    protected void onPause() {
        this.mHandler.removeCallbacks(this.mShowProgressBarRunnable);
        super.onPause();
    }

    protected View getBackgroundContainer() {
        return findViewById(C0508R.id.epg_concern_wechat_main_layout);
    }
}
