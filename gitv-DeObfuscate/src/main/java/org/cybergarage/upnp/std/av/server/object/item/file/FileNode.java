package org.cybergarage.upnp.std.av.server.object.item.file;

import java.io.File;
import java.io.InputStream;

public interface FileNode {
    boolean equals(File file);

    byte[] getContent();

    InputStream getContentInputStream();

    long getContentLength();

    File getFile();

    long getFileTimeStamp();

    void setFile(File file);
}
