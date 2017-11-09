package com.gala.video.app.epg.web.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.gala.video.api.ApiException;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.web.event.WebBasicEventFactory;
import com.gala.video.app.epg.web.subject.api.IApi.IExceptionCallback;
import com.gala.video.app.epg.web.subject.api.NetWorkCheckApi;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.feedback.GlobalQRFeedbackPanel;
import com.gala.video.lib.share.utils.ResourceUtil;
import com.gala.video.webview.event.WebBaseEvent;
import com.gala.video.webview.widget.AbsWebView;

public class WebView extends AbsWebView {
    private GlobalQRFeedbackPanel mQrFeedbackPanel = null;

    public WebView(Context context) {
        super(context);
    }

    public WebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    protected void initView() {
        LayoutInflater.from(this.mContext).inflate(R.layout.epg_layout_webview, this, true);
        startLoading();
    }

    protected WebBaseEvent getBaseEvent() {
        boolean isAcceler = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().isAccelerateExclude(this.mWebUrl);
        LogUtils.d(this.TAG, " webview init WebEvent, url: " + this.mWebUrl + ", isAccelerateExclude: " + isAcceler);
        return WebBasicEventFactory.create(isAcceler);
    }

    protected View getLoadingView() {
        View view = findViewById(R.id.epg_webview_loading);
        view.setBackgroundColor(-16777216);
        return view;
    }

    protected LinearLayout getProgressBarItem() {
        ViewStub viewStub = (ViewStub) findViewById(R.id.epg_webview_lading_layout_viewstub);
        if (viewStub == null) {
            return null;
        }
        LinearLayout progressBar = (LinearLayout) viewStub.inflate();
        ((TextView) progressBar.findViewById(R.id.share_progress_tv)).setTextColor(ResourceUtil.getColor(R.color.albumview_focus_color));
        return progressBar;
    }

    private GlobalQRFeedbackPanel getGlobalQRFeedbackPanel() {
        if (this.mQrFeedbackPanel == null) {
            this.mQrFeedbackPanel = (GlobalQRFeedbackPanel) ((ViewStub) findViewById(R.id.epg_webview_qr_panel_layout_viewstub)).inflate();
            this.mQrFeedbackPanel.setQRTextColor(ResourceUtil.getColor(R.color.albumview_focus_color));
            this.mQrFeedbackPanel.post(new Runnable() {
                public void run() {
                    if (WebView.this.mQrFeedbackPanel != null) {
                        WebView.this.mQrFeedbackPanel.getButton().requestFocus();
                    }
                }
            });
        }
        return this.mQrFeedbackPanel;
    }

    protected void showErrorView(String error) {
        new NetWorkCheckApi().check(error, new IExceptionCallback() {
            public void getStateResult(final ApiException e) {
                WebView.this.mHandler.post(new Runnable() {
                    public void run() {
                        GetInterfaceTools.getUICreator().maketNoResultView(WebView.this.mContext, WebView.this.getGlobalQRFeedbackPanel(), null, e);
                    }
                });
            }
        });
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0) {
            if (event.getKeyCode() == 23 && event.getRepeatCount() > 0) {
                return false;
            }
            int keyCode = event.getKeyCode();
            if ((keyCode == 23 || keyCode == 66) && this.mQrFeedbackPanel != null && this.mQrFeedbackPanel.getButton() != null && this.mQrFeedbackPanel.getButton().getVisibility() == 0) {
                return super.dispatchKeyEvent(event);
            }
            if (handleJsKeyEvent(event)) {
                return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    protected boolean handleJsKeyEvent(KeyEvent event) {
        return false;
    }

    public String getTag() {
        return "EPG/web/WebView";
    }
}
