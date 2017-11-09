package com.gala.video.app.epg.web;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import com.gala.multiscreen.dmr.model.msg.Notify;
import com.gala.pingback.IPingbackContext;
import com.gala.pingback.IPingbackValueProvider;
import com.gala.pingback.PingbackItem;
import com.gala.sdk.player.constants.PlayerIntentConfig2;
import com.gala.tv.voice.service.AbsVoiceAction;
import com.gala.video.app.epg.C0508R;
import com.gala.video.app.epg.web.function.IPlayerListener;
import com.gala.video.app.epg.web.subject.play.PlayBaseControl;
import com.gala.video.app.epg.web.subject.play.PlayControlFactory;
import com.gala.video.app.epg.web.utils.WebDataUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.web.model.WebViewDataImpl;
import com.gala.video.lib.share.pingback.PingbackContext;
import com.gala.video.lib.share.project.Project;
import com.gala.video.webview.utils.WebSDKConstants;
import com.gala.video.widget.util.HomeMonitorHelper;
import com.gala.video.widget.util.HomeMonitorHelper.OnHomePressedListener;
import java.util.ArrayList;
import java.util.List;

public class WebSubjectActivity extends WebBaseActivity implements IPingbackContext, IPlayerListener {
    private static final String TAG = "EPG/WebSubjectActivity";
    private HomeMonitorHelper mHomeMonitorHelper;
    private boolean mIsRegisterHomeMonitor = false;
    private final IPingbackContext mPingbackContext = new PingbackContext();
    private PlayBaseControl mPlayControl;

    class C12561 implements OnHomePressedListener {
        C12561() {
        }

        public void onHomePressed() {
            WebSubjectActivity.this.finish();
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        initWindow();
        super.onCreate(savedInstanceState);
        LogUtils.m1568d(TAG, "isPlayerAlready:" + GetInterfaceTools.getPlayerFeatureProxy().isPlayerAlready() + ",isSupportSmallWindowPlay:" + Project.getInstance().getBuild().isSupportSmallWindowPlay() + ",supportPlayerMultiProcess:" + Project.getInstance().getBuild().supportPlayerMultiProcess());
        if (GetInterfaceTools.getPlayerFeatureProxy().isPlayerAlready() || !Project.getInstance().getBuild().isSupportSmallWindowPlay() || Project.getInstance().getBuild().supportPlayerMultiProcess()) {
            registerHomeKeyForLauncher();
            return;
        }
        LogUtils.m1571e(TAG, "PlayerFeatureProvider not already!!");
        finish();
    }

    private void initWindow() {
        getWindow().setFormat(-2);
        getWindow().addFlags(128);
        if (Project.getInstance().getBuild().isHomeVersion()) {
            setTheme(C0508R.style.AppTheme);
        }
    }

    private void initIntentData(Intent intent) {
        if (intent != null) {
            intent.putExtra("from", this.mBaseWebInfo.getFrom()).putExtra("buy_source", this.mBaseWebInfo.getBuySource()).putExtra(WebSDKConstants.PARAM_KEY_EVENTID, this.mBaseWebInfo.getEventId()).putExtra("state", this.mBaseWebInfo.getState()).putExtra("eventId", this.mBaseWebInfo.getEventId());
        }
    }

    protected void initWebView() {
        initIntentData(getIntent());
        this.mGaLaWebView.setIFunPlayer(this);
        this.mPlayControl = PlayControlFactory.create(this.mBaseWebInfo);
        initGalaWebView();
        this.mPlayControl.init(this, this.mGaLaWebView.getBasicEvent(), getIntent());
        this.mPlayControl.setPlayerContainer(this.mGaLaWebView.getPlayerContainer());
        this.mPlayControl.setScreenCallback(this.mGaLaWebView.getScreenCallback());
        showUrl(this.mWebUrl);
    }

    protected String getWebUrl() {
        String pageUrl;
        if (Project.getInstance().getBuild().isTestErrorCodeAndUpgrade()) {
            pageUrl = WebConstants.DEFAULT_WEB_SITE_SUBJECT_TEST;
        } else {
            pageUrl = GetInterfaceTools.getIJSConfigDataProvider().getJSConfigResult().getUrlSubject();
        }
        if (StringUtils.isEmpty((CharSequence) pageUrl)) {
            pageUrl = WebConstants.DEFAULT_WEB_SITE_SUBJECT;
        }
        return WebDataUtils.parseWebUrl(pageUrl);
    }

    protected WebViewDataImpl generateJsonParams() {
        return this.mPlayControl.generateJsonParams(super.generateJsonParams());
    }

    public void onAlbumSelected(String albumInfo) {
        this.mPlayControl.onAlbumSelected(albumInfo);
    }

    public void checkLiveInfo(String albumInfo) {
        this.mPlayControl.checkLiveInfo(albumInfo);
    }

    public void startWindowPlay(String playInfo) {
        this.mPlayControl.startWindowPlay(playInfo);
    }

    public void switchScreenMode(String mode) {
        this.mPlayControl.switchScreenMode(mode);
    }

    public void switchPlay(String playInfo) {
        this.mPlayControl.switchPlay(playInfo);
    }

    protected void onResume() {
        super.onResume();
        this.mPlayControl.onResume();
    }

    public void onPause() {
        super.onPause();
        this.mPlayControl.onPause(isFinishing());
    }

    protected void onStop() {
        super.onStop();
        this.mPlayControl.onStop();
    }

    protected void onDestroy() {
        super.onDestroy();
        this.mPlayControl.onDestroy();
        this.mPlayControl = null;
        synchronized (this) {
            if (this.mHomeMonitorHelper != null) {
                this.mHomeMonitorHelper.onDestory();
            }
            this.mHomeMonitorHelper = null;
            this.mIsRegisterHomeMonitor = false;
        }
    }

    public boolean handleKeyEvent(KeyEvent event) {
        if (event.getAction() == 1) {
            return super.handleKeyEvent(event);
        }
        if (this.mPlayControl.handleKeyEvent(event)) {
            return true;
        }
        if (this.mPlayControl.isFullscreen()) {
            return super.handleKeyEvent(event);
        }
        return super.handleJsKeyEvent(event);
    }

    public Notify onPhoneSync() {
        return this.mPlayControl.onPhoneSync();
    }

    public boolean onKeyChanged(int keycode) {
        return this.mPlayControl.onKeyChanged(keycode);
    }

    public void onActionScrollEvent(KeyKind keyKind) {
        this.mPlayControl.onActionScrollEvent(keyKind);
    }

    public List<AbsVoiceAction> getSupportedVoices() {
        return this.mPlayControl.getSupportedVoices(new ArrayList());
    }

    public boolean onResolutionChanged(String newRes) {
        return this.mPlayControl.onResolutionChanged(newRes);
    }

    public boolean onSeekChanged(long newPosition) {
        return this.mPlayControl.onSeekChanged(newPosition);
    }

    public long getPlayPosition() {
        return this.mPlayControl.getPlayPosition();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LogUtils.m1568d(TAG, "onActivityResult resultCode:" + resultCode);
        this.mPlayControl.onActivityResult(requestCode, resultCode, data);
        getIntent().putExtra(PlayerIntentConfig2.INTENT_PARAM_OPEN_PAY_PAGE, false);
    }

    public PingbackItem getItem(String key) {
        return this.mPingbackContext.getItem(key);
    }

    public void setItem(String key, PingbackItem item) {
        this.mPingbackContext.setItem(key, item);
    }

    public void setPingbackValueProvider(IPingbackValueProvider provider) {
        this.mPingbackContext.setPingbackValueProvider(provider);
    }

    private synchronized void registerHomeKeyForLauncher() {
        if (!this.mIsRegisterHomeMonitor) {
            this.mHomeMonitorHelper = new HomeMonitorHelper(new C12561(), this);
            this.mIsRegisterHomeMonitor = true;
        }
    }
}
