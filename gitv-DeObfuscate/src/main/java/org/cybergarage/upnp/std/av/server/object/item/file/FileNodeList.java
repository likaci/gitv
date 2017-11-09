package org.cybergarage.upnp.std.av.server.object.item.file;

import java.io.File;
import java.util.Vector;

public class FileNodeList extends Vector {
    public FileNode getFileNode(int n) {
        return (FileNode) get(n);
    }

    public FileNode getFileNode(File file) {
        int cnt = size();
        for (int n = 0; n < cnt; n++) {
            FileNode node = getFileNode(n);
            if (node.getFile() != null && node.equals(file)) {
                return node;
            }
        }
        return null;
    }
}
