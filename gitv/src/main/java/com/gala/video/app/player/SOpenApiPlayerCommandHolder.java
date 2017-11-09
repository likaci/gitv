package com.gala.video.app.player;

import android.content.Context;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IAddInstanceHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder;
import com.gala.video.lib.share.ifmanager.bussnessIF.openplay.IOpenApiCommandHolder.Wrapper;

public class SOpenApiPlayerCommandHolder extends Wrapper {
    public Object getInterface() {
        try {
            return (IOpenApiCommandHolder) Class.forName("com.gala.video.app.player.openapi.OpenApiPlayerCommandHolder").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return this;
        }
    }

    public IAddInstanceHolder[] getCommandHolder(Context context) {
        return null;
    }
}
