package com.gala.video.app.epg.ui.setting;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.utils.UserUtil;
import com.gala.video.app.epg.ui.setting.utils.SettingPingbackUtils;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.framework.core.utils.ThreadUtils;
import com.gala.video.lib.share.common.activity.QMultiScreenActivity;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import com.gala.video.lib.share.project.Project;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.utils.QRUtils;
import com.gala.video.webview.utils.WebSDKConstants;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class FeedbackActivity extends QMultiScreenActivity {
    private static final String LOG_TAG = "EPG/FeedbackActivity";
    private static final String QR_URL = "http://cms.ptqy.gitv.tv/common/tv/feedback/suggest.html";
    private IDynamicResult mDynamicResult;
    private TextView mMessageInfoTextView;
    private View mProgressBar;
    private View mProgressBarLayout;
    private TextView mProgressBarTextView;
    private View mQRColorBgView;
    private ImageView mQRView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.epg_activity_feedback);
        initView();
        loadData();
    }

    private void loadData() {
        this.mDynamicResult = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        ThreadUtils.execute(new Runnable() {
            public void run() {
                FeedbackActivity.this.setDocumentContent();
                FeedbackActivity.this.setQRViewBitmap();
            }
        });
    }

    private void setDocumentContent() {
        CharSequence messageInfo = fetchMessageInfo();
        LogUtils.i(LOG_TAG, "setDocumentContent --- messageInfo = ", messageInfo);
        if (!StringUtils.isEmpty(messageInfo)) {
            setText(this.mMessageInfoTextView, messageInfo);
        }
    }

    private String fetchMessageInfo() {
        if (this.mDynamicResult != null) {
            CharSequence document = this.mDynamicResult.getDocument();
            if (!StringUtils.isEmpty(document)) {
                LogUtils.i(LOG_TAG, "fetchMessageInfo --- mDynamicResult.document = ", document);
                return document.replace("\\n", "<br />");
            }
        }
        LogUtils.e(LOG_TAG, "fetchMessageInfo --- mDynamicResult or mDynamicResult.document is empty");
        return null;
    }

    private void setText(final TextView textView, final String s) {
        if (textView == null || StringUtils.isEmpty((CharSequence) s)) {
            LogUtils.e(LOG_TAG, "setText --- textView = ", textView, " s = ", s);
            return;
        }
        runOnUiThread(new Runnable() {
            public void run() {
                textView.setText(Html.fromHtml(s));
            }
        });
    }

    private String fetchFeedbackUrl() {
        return assembleUrl(new StringBuilder(QR_URL)).toString();
    }

    private StringBuilder assembleUrl(StringBuilder url) {
        if (StringUtils.isEmpty((CharSequence) url)) {
            LogUtils.e(LOG_TAG, "assembleUrl --- url is empty");
            return null;
        }
        String uid = GetInterfaceTools.getIGalaAccountManager().getUID();
        int usertype = GetInterfaceTools.getIGalaAccountManager().getUserTypeForH5();
        Boolean islitchi = Boolean.valueOf(GetInterfaceTools.getIGalaAccountManager().getIsLitchiVipForH5());
        String av = Project.getInstance().getBuild().getVersionString();
        String mac = DeviceUtils.getMacAddr();
        String uuid = Project.getInstance().getBuild().getVrsUUID();
        String p2 = Project.getInstance().getBuild().getPingbackP2();
        String hwver = Build.MODEL.replace(" ", "-");
        String cookie = "";
        if (UserUtil.isLogin()) {
            cookie = UserUtil.getLoginCookie();
        }
        assembleFirstKeyValuePair(url, WebSDKConstants.PARAM_KEY_UID, uid);
        assembleOtherKeyValuePair(url, WebConstants.USER_TYPE, String.valueOf(usertype));
        assembleOtherKeyValuePair(url, WebConstants.IS_LITCHI, String.valueOf(islitchi));
        assembleOtherKeyValuePair(url, WebConstants.AV, av);
        assembleOtherKeyValuePair(url, WebSDKConstants.PARAM_KEY_MAC, mac);
        assembleOtherKeyValuePair(url, "uuid", uuid);
        assembleOtherKeyValuePair(url, WebSDKConstants.PARAM_KEY_P2, p2);
        assembleOtherKeyValuePair(url, WebSDKConstants.PARAM_KEY_HWVER, hwver);
        assembleOtherKeyValuePair(url, WebSDKConstants.PARAM_KEY_COOKIE, cookie);
        LogUtils.i(LOG_TAG, "after assembleUrl the url is ", url);
        return url;
    }

    public void assembleFirstKeyValuePair(StringBuilder url, String key, String value) {
        if (StringUtils.isEmpty((CharSequence) url) || StringUtils.isEmpty((CharSequence) key)) {
            LogUtils.e(LOG_TAG, "assembleFirstKeyValuePair --- url or key is empty");
        } else {
            url.append("?").append(key).append(SearchCriteria.EQ).append(value);
        }
    }

    public static void assembleOtherKeyValuePair(StringBuilder url, String key, String value) {
        if (StringUtils.isEmpty((CharSequence) url) || StringUtils.isEmpty((CharSequence) key)) {
            LogUtils.e(LOG_TAG, "assembleOtherKeyValuePair --- url or key is empty");
        } else {
            url.append("&").append(key).append(SearchCriteria.EQ).append(value);
        }
    }

    private void setQRViewBitmap() {
        final Bitmap bitmap = QRUtils.createQRImage(fetchFeedbackUrl(), ResourceUtil.getDimen(R.dimen.dimen_343dp), ResourceUtil.getDimen(R.dimen.dimen_343dp));
        if (bitmap == null) {
            LogUtils.e(LOG_TAG, "setQRViewBitmap --- bitmap is null");
            setDefaultQRBitmap();
            return;
        }
        SettingPingbackUtils.feedbackPageShow();
        runOnUiThread(new Runnable() {
            public void run() {
                FeedbackActivity.this.mQRView.setImageBitmap(bitmap);
                FeedbackActivity.this.mProgressBarLayout.setVisibility(8);
                FeedbackActivity.this.mQRColorBgView.setBackgroundColor(ResourceUtil.getColor(R.color.gala_write));
            }
        });
    }

    private void initView() {
        this.mProgressBarLayout = findViewById(R.id.epg_progressbar_layout);
        this.mProgressBar = findViewById(R.id.epg_fb_progressbar);
        this.mProgressBarTextView = (TextView) findViewById(R.id.epg_fb_progressbar_textview);
        this.mQRView = (ImageView) findViewById(R.id.epg_fb_qr);
        this.mQRColorBgView = findViewById(R.id.epg_fb_qr_color_bg);
        this.mMessageInfoTextView = (TextView) findViewById(R.id.epg_fb_message_info);
        this.mQRColorBgView.setBackgroundColor(ResourceUtil.getColor(R.color.transparent));
    }

    private void setDefaultQRBitmap() {
        LogUtils.d(LOG_TAG, "setDefaultQRBitmap");
        runOnUiThread(new Runnable() {
            public void run() {
                FeedbackActivity.this.mQRColorBgView.setBackgroundColor(ResourceUtil.getColor(R.color.transparent));
                FeedbackActivity.this.mProgressBar.setVisibility(8);
                FeedbackActivity.this.mProgressBarTextView.setVisibility(8);
            }
        });
    }

    protected View getBackgroundContainer() {
        return findViewById(R.id.epg_fb_main_layout);
    }
}
