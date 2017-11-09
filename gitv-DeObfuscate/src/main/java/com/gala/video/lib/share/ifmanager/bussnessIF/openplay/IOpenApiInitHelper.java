package com.gala.video.lib.share.ifmanager.bussnessIF.openplay;

import android.content.Context;
import java.util.List;

public class IOpenApiInitHelper {
    public void init(Context context, List<String> list) {
    }

    public IOpenApiInitHelper getEPGHelper() {
        try {
            return (IOpenApiInitHelper) Class.forName("com.gala.video.lib.share.ifimpl.openplay.service.OpenApiEpgInitHelper").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public IOpenApiInitHelper getPlayerHelper() {
        try {
            return (IOpenApiInitHelper) Class.forName("com.gala.video.lib.share.ifimpl.openplay.service.OpenApiPlayerInitHelper").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public void initOpenApiFeatures() {
    }
}
