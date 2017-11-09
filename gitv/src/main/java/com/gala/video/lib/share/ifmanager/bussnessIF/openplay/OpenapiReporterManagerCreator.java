package com.gala.video.lib.share.ifmanager.bussnessIF.openplay;

public class OpenapiReporterManagerCreator {
    public static IOpenapiReporterManager create() {
        try {
            return (IOpenapiReporterManager) Class.forName("com.gala.video.lib.share.ifimpl.openplay.service.OpenapiReporterManager").newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new NullOpenapiReporterManager();
        }
    }
}
