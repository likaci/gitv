package com.gala.video.lib.share.ifimpl.dynamic;

import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicQDataProvider;

public class DynamicQDataProviderCreator {
    public static IDynamicQDataProvider create() {
        return new DynamicQDataProvider();
    }
}
