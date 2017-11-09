package com.gala.video.lib.share.ifimpl.openplay.service;

import android.content.Context;
import android.util.AndroidRuntimeException;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.FavoriteChangedReporter;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.HistoryChangedReporter;
import com.gala.video.lib.share.ifimpl.openplay.service.feature.reporter.VideoPlayStateReporter;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiInitHelper;
import java.util.List;

public class OpenApiEpgInitHelper extends IOpenApiInitHelper {
    private Context mContext;
    private List<String> mFeatures = null;

    public void init(Context context, List<String> features) {
        this.mContext = context;
        this.mFeatures = features;
        initOpenApiFeatures();
    }

    public void initOpenApiFeatures() {
        try {
            OpenApiUtils.addCommand(this.mFeatures, CreateInterfaceTools.createEpgOpenApiHolder().getCommandHolder(this.mContext));
        } catch (AndroidRuntimeException e) {
            e.printStackTrace();
            new Thread8K(new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                    OpenApiUtils.addCommand(OpenApiEpgInitHelper.this.mFeatures, CreateInterfaceTools.createEpgOpenApiHolder().getCommandHolder(OpenApiEpgInitHelper.this.mContext));
                }
            }, "OpenApiEpgInitHelper").start();
        }
        initReporter();
    }

    private void initReporter() {
        if (this.mFeatures.contains("VideoPlayStateReporter")) {
            OpenApiManager.instance().setVideoPlayStateReporter(new VideoPlayStateReporter(this.mContext));
        }
        if (this.mFeatures.contains("FavoriteChangedReporter")) {
            OpenApiManager.instance().setFavoriteChangedReporter(new FavoriteChangedReporter(this.mContext));
        }
        if (this.mFeatures.contains("HistoryChangedReporter")) {
            OpenApiManager.instance().setHistoryChangedReporter(new HistoryChangedReporter(this.mContext));
        }
        if (this.mFeatures.contains("NeedEncrypt")) {
            OpenApiManager.instance().setNeedEncrypt(true);
        }
    }
}
