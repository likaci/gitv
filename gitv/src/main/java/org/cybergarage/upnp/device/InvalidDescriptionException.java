package org.cybergarage.upnp.device;

import java.io.File;

public class InvalidDescriptionException extends Exception {
    public InvalidDescriptionException(String s) {
        super(s);
    }

    public InvalidDescriptionException(String s, File file) {
        super(new StringBuilder(String.valueOf(s)).append(" (").append(file.toString()).append(")").toString());
    }

    public InvalidDescriptionException(Exception e) {
        super(e.getMessage());
    }
}
