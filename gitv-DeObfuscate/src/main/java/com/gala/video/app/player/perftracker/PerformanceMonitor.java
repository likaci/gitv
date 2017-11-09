package com.gala.video.app.player.perftracker;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import com.gala.sdk.utils.performance.AbsPerformanceMonitor;
import com.gala.video.app.player.C1291R;
import com.gala.video.app.player.perftracker.ITrackerConfig.ModuleType;
import com.gala.video.app.player.perftracker.ITrackerConfig.TrackType;
import com.gala.video.app.player.utils.debug.DebugOptionsCache;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.player.utils.PlayerDebugUtils;
import java.lang.ref.WeakReference;

public class PerformanceMonitor extends AbsPerformanceMonitor {
    private static final boolean ENABLE_DEBUG = PlayerDebugUtils.isEnablePerformanceDebug();
    private static String TAG;
    private ITrackerConfig mConfig = new C14481();
    private WeakReference<Context> mContextRef;
    private boolean mEnableFloatingWindow;
    private FloatingWindow mFloatingWindow;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());
    private String mPerfLogTag;

    class C14481 extends AbsTrackerConfig {
        C14481() {
        }

        public String getLogTag() {
            return "NONE";
        }

        public ModuleType getModuleType() {
            return ModuleType.NONE;
        }
    }

    public PerformanceMonitor(Context context, ITrackerConfig config) {
        this.mContextRef = new WeakReference(context);
        this.mConfig = config;
    }

    public void initialize() {
        TAG = "Player/Perf/PerformanceMonitor@" + Integer.toHexString(super.hashCode());
        this.mPerfLogTag = "[" + this.mConfig.getLogTag() + "][PERF]";
        TrackType type = this.mConfig.getTrackType();
        boolean z = isFloatingWindowEnabled() && (type == TrackType.FLOATWINDOW_ONLY || type == TrackType.BOTH);
        this.mEnableFloatingWindow = z;
        if (this.mEnableFloatingWindow) {
            this.mFloatingWindow = new FloatingWindow(getContext(), makeFloatingView());
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "initialize: mFloatingWindow=" + this.mFloatingWindow);
        }
    }

    public void updateTitle(String title) {
        updatePerformanceTitle(title);
    }

    public void updateContent(CharSequence content) {
        updatePerformanceContent(content);
    }

    public void start() {
        initialize();
        startTrack();
    }

    public void stop() {
        stopTrack();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("FloatingWindow").append("@").append(super.hashCode());
        builder.append("{");
        builder.append(", context=").append(this.mContextRef);
        builder.append("}");
        return builder.toString();
    }

    private void appendText(View contentView, int txtId, final CharSequence content) {
        if (contentView != null) {
            final TextView txtView = (TextView) contentView.findViewById(txtId);
            if (txtView != null) {
                this.mMainHandler.post(new Runnable() {
                    public void run() {
                        txtView.append(content);
                    }
                });
            }
        }
    }

    private void setText(View contentView, int txtId, final String content) {
        if (contentView != null) {
            final TextView txtView = (TextView) contentView.findViewById(txtId);
            if (txtView != null) {
                this.mMainHandler.post(new Runnable() {
                    public void run() {
                        txtView.setText(content);
                    }
                });
            }
        }
    }

    private Context getContext() {
        if (this.mContextRef != null) {
            return (Context) this.mContextRef.get();
        }
        return null;
    }

    private boolean isFloatingWindowEnabled() {
        return ENABLE_DEBUG || DebugOptionsCache.isPerfFloatingWindowEnabled((Context) this.mContextRef.get());
    }

    private View makeFloatingView() {
        return LayoutInflater.from(getContext()).inflate(C1291R.layout.player_floating_view, null);
    }

    private void recordLog(String logMsg) {
        switch (this.mConfig.getLogType()) {
            case LOGCAT:
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.mPerfLogTag, logMsg);
                    return;
                }
                return;
            case BOTH:
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(this.mPerfLogTag, logMsg);
                    return;
                }
                return;
            default:
                return;
        }
    }

    private void updatePerformanceTitle(String title) {
        setText(this.mFloatingWindow != null ? this.mFloatingWindow.getContentView() : null, C1291R.id.txt_currentrunning, title);
    }

    private void updatePerformanceContent(CharSequence content) {
        View contentView = this.mFloatingWindow != null ? this.mFloatingWindow.getContentView() : null;
        switch (this.mConfig.getTrackType()) {
            case FLOATWINDOW_ONLY:
                appendText(contentView, C1291R.id.txt_datarecord, content);
                return;
            case LOG_ONLY:
                recordLog(content.toString());
                return;
            case BOTH:
                appendText(contentView, C1291R.id.txt_datarecord, content);
                recordLog(content.toString());
                return;
            default:
                return;
        }
    }

    public void startTrack() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "startTrack: mFloatingWindow=" + this.mFloatingWindow);
        }
        if (this.mFloatingWindow != null) {
            this.mFloatingWindow.showWindow();
        }
    }

    public void stopTrack() {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "stopTrack: mFloatingWindow=" + this.mFloatingWindow);
        }
        if (this.mFloatingWindow != null) {
            this.mFloatingWindow.hideWindow();
        }
    }
}
