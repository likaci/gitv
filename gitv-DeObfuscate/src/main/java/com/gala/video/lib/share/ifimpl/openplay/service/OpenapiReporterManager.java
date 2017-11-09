package com.gala.video.lib.share.ifimpl.openplay.service;

import com.gala.tvapi.tv2.model.Album;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenapiReporterManager.Wrapper;
import com.qiyi.tv.client.data.Media;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public class OpenapiReporterManager extends Wrapper {
    private static final String TAG = "OpenapiReporterManager";
    private static ExecutorService mExec = Executors.newSingleThreadExecutor(new C17081());
    private OpenApiManager mOpenApiManager = OpenApiManager.instance();

    static class C17081 implements ThreadFactory {
        C17081() {
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread8K(r, "OpenapiReporterManager#");
            t.setPriority(1);
            return t;
        }
    }

    class C17114 implements Runnable {
        C17114() {
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(OpenapiReporterManager.TAG, "deleteAllPlayRecord()");
            }
            if (OpenapiReporterManager.this.mOpenApiManager.isAuthSuccess()) {
                OpenapiReporterManager.this.mOpenApiManager.getHistoryChangedReporter().reportHistoryChanged(3, null);
            }
        }
    }

    class C17125 implements Runnable {
        C17125() {
        }

        public void run() {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(OpenapiReporterManager.TAG, "deleteAllFavRecord()");
            }
            if (OpenapiReporterManager.this.mOpenApiManager.isAuthSuccess()) {
                OpenapiReporterManager.this.mOpenApiManager.getFavoriteChangedReporter().reportFavoriteChanged(3, null);
            }
        }
    }

    public void onAddPlayRecord(final Album album) {
        mExec.execute(new Runnable() {
            public void run() {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(OpenapiReporterManager.TAG, "onAddPlayRecord()");
                }
                if (album != null && OpenapiReporterManager.this.mOpenApiManager.isAuthSuccess()) {
                    Media media = OpenApiUtils.createSdkMedia(album);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(OpenapiReporterManager.TAG, "addPlayRecord(), media = " + media);
                    }
                    OpenapiReporterManager.this.mOpenApiManager.getHistoryChangedReporter().reportHistoryChanged(1, media);
                }
            }
        });
    }

    public void onAddFavRecord(final Album album) {
        mExec.execute(new Runnable() {
            public void run() {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(OpenapiReporterManager.TAG, "onAddFavRecord()");
                }
                if (album != null && OpenapiReporterManager.this.mOpenApiManager.isAuthSuccess()) {
                    Media media = OpenApiUtils.createSdkMedia(album);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(OpenapiReporterManager.TAG, "addFavRecord(), media = " + media);
                    }
                    OpenapiReporterManager.this.mOpenApiManager.getFavoriteChangedReporter().reportFavoriteChanged(1, media);
                }
            }
        });
    }

    public void onDeleteAllPlayRecord() {
        mExec.execute(new C17114());
    }

    public void onDeleteAllFavRecord() {
        mExec.execute(new C17125());
    }

    public void onDeleteSingleFavRecord(final Album album) {
        mExec.execute(new Runnable() {
            public void run() {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(OpenapiReporterManager.TAG, "onDeleteSingleFavRecord(), album = " + album);
                }
                if (album != null && OpenapiReporterManager.this.mOpenApiManager.isAuthSuccess()) {
                    Media media = OpenApiUtils.createSdkMedia(album);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(OpenapiReporterManager.TAG, "deleteSingleFavRecord(), media = " + media);
                    }
                    OpenapiReporterManager.this.mOpenApiManager.getFavoriteChangedReporter().reportFavoriteChanged(2, media);
                }
            }
        });
    }

    public void onDeleteSinglePlayRecord(final Album album) {
        mExec.execute(new Runnable() {
            public void run() {
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(OpenapiReporterManager.TAG, "onDeleteSinglePlayRecord(), album = " + album);
                }
                if (album != null && OpenapiReporterManager.this.mOpenApiManager.isAuthSuccess()) {
                    Media media = OpenApiUtils.createSdkMedia(album);
                    if (LogUtils.mIsDebug) {
                        LogUtils.m1568d(OpenapiReporterManager.TAG, "deleteSingleFavRecord(), media = " + media);
                    }
                    OpenapiReporterManager.this.mOpenApiManager.getHistoryChangedReporter().reportHistoryChanged(2, media);
                }
            }
        });
    }
}
