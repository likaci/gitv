package com.gala.video.lib.share.ifimpl.openplay.service;

import android.content.Context;
import android.util.AndroidRuntimeException;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiInitHelper;
import java.util.List;

public class OpenApiPlayerInitHelper extends IOpenApiInitHelper {
    private Context mContext;
    private List<String> mFeatures = null;

    class C17061 implements Runnable {
        C17061() {
        }

        public void run() {
            try {
                Thread.sleep(1000);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            OpenApiUtils.addCommand(OpenApiPlayerInitHelper.this.mFeatures, CreateInterfaceTools.createPlayerOpenApiHolder().getCommandHolder(OpenApiPlayerInitHelper.this.mContext));
        }
    }

    public void init(Context context, List<String> features) {
        this.mContext = context;
        this.mFeatures = features;
        initOpenApiFeatures();
    }

    public void initOpenApiFeatures() {
        try {
            OpenApiUtils.addCommand(this.mFeatures, CreateInterfaceTools.createPlayerOpenApiHolder().getCommandHolder(this.mContext));
        } catch (AndroidRuntimeException e) {
            e.printStackTrace();
            new Thread8K(new C17061(), "OpenApiPlayerInitHelper").start();
        }
    }
}
