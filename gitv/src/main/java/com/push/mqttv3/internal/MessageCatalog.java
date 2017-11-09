package com.push.mqttv3.internal;

public abstract class MessageCatalog {
    private static MessageCatalog INSTANCE = null;

    protected abstract String getLocalizedMessage(int i);

    public static final String getMessage(int id) {
        if (INSTANCE == null) {
            if (ExceptionHelper.isClassAvailable("java.util.ResourceBundle")) {
                try {
                    INSTANCE = (MessageCatalog) Class.forName("ResourceBundleCatalog").newInstance();
                } catch (Exception e) {
                    return "";
                }
            } else if (ExceptionHelper.isClassAvailable("org.eclipse.paho.client.mqttv3.internal.MIDPCatalog")) {
                try {
                    INSTANCE = (MessageCatalog) Class.forName("org.eclipse.paho.client.mqttv3.internal.MIDPCatalog").newInstance();
                } catch (Exception e2) {
                    return "";
                }
            }
        }
        return INSTANCE.getLocalizedMessage(id);
    }
}
