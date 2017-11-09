package org.cybergarage.upnp.std.av.server.directory.file;

import android.os.Environment;
import java.io.File;
import java.io.InputStream;
import org.cybergarage.upnp.std.av.server.Directory;
import org.cybergarage.upnp.std.av.server.object.ContentNode;
import org.cybergarage.upnp.std.av.server.object.Format;
import org.cybergarage.upnp.std.av.server.object.FormatObject;
import org.cybergarage.upnp.std.av.server.object.item.file.FileItemNode;
import org.cybergarage.upnp.std.av.server.object.item.file.FileItemNodeList;
import org.cybergarage.upnp.std.av.server.object.item.file.FileNode;
import org.cybergarage.upnp.std.av.server.object.item.file.FileNodeList;
import org.cybergarage.util.Debug;

public class FileDirectory extends Directory implements FileNode {
    private File itemFile;
    private String path;

    public FileDirectory(String name, String path) {
        super(name);
        setPath(path);
    }

    public void setPath(String value) {
        this.path = value;
    }

    public String getPath() {
        return this.path;
    }

    private boolean updateFileNode(FileNode node, File file) {
        if (node instanceof FileDirectory) {
            ((FileDirectory) node).setTitle(node.getFile().getName());
        } else if (node instanceof FileItemNode) {
            Format format = getContentDirectory().getFormat(file);
            if (format == null) {
                return false;
            }
            FormatObject formatObj = format.createObject(file);
            FileItemNode itemNode = (FileItemNode) node;
            itemNode.setFile(file);
            String title = formatObj.getTitle();
            if (title.length() > 0) {
                itemNode.setTitle(title);
            }
            String creator = formatObj.getCreator();
            if (creator.length() > 0) {
                itemNode.setCreator(creator);
            }
            String mediaClass = format.getMediaClass();
            if (mediaClass.length() > 0) {
                itemNode.setUPnPClass(mediaClass);
            }
            itemNode.setDate(file.lastModified());
            try {
                itemNode.setStorageUsed(file.length());
            } catch (Exception e) {
                Debug.warning(e);
            }
            String absServerPath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(getContentDirectory().getMediaServer().getServerRootDir()).toString();
            String filePath = itemNode.getFile().getAbsolutePath();
            filePath = filePath.substring(absServerPath.length(), filePath.length());
            String protocol = "http-get:*:" + format.getMimeType() + ":*";
            itemNode.setResource(getContentDirectory().getContentExportURL(filePath, itemNode.getID()), protocol, formatObj.getAttributeList());
        }
        getContentDirectory().updateSystemUpdateID();
        return true;
    }

    private boolean updateItemNode(FileItemNode itemNode, File file) {
        Format format = getContentDirectory().getFormat(file);
        if (format == null) {
            return false;
        }
        FormatObject formatObj = format.createObject(file);
        itemNode.setFile(file);
        String title = formatObj.getTitle();
        if (title.length() > 0) {
            itemNode.setTitle(title);
        }
        String creator = formatObj.getCreator();
        if (creator.length() > 0) {
            itemNode.setCreator(creator);
        }
        String mediaClass = format.getMediaClass();
        if (mediaClass.length() > 0) {
            itemNode.setUPnPClass(mediaClass);
        }
        itemNode.setDate(file.lastModified());
        try {
            itemNode.setStorageUsed(file.length());
        } catch (Exception e) {
            Debug.warning(e);
        }
        String absServerPath = new StringBuilder(String.valueOf(Environment.getExternalStorageDirectory().getAbsolutePath())).append(getContentDirectory().getMediaServer().getServerRootDir()).toString();
        String filePath = itemNode.getFile().getAbsolutePath();
        filePath = filePath.substring(absServerPath.length() + 1, filePath.length());
        String protocol = "http-get:*:" + format.getMimeType() + ":*";
        itemNode.setResource(getContentDirectory().getContentExportURL(filePath, itemNode.getID()), protocol, formatObj.getAttributeList());
        getContentDirectory().updateSystemUpdateID();
        return true;
    }

    private FileItemNode createCompareItemNode(File file) {
        if (getContentDirectory().getFormat(file) == null) {
            return null;
        }
        FileItemNode itemNode = new FileItemNode();
        itemNode.setFile(file);
        return itemNode;
    }

    private int getDirectoryFileNodeList(File dirFile, FileNodeList fileNodeList) {
        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()) {
                FileDirectory directory = new FileDirectory(file.getName(), file.getAbsolutePath());
                directory.setChildCount(file.listFiles().length);
                directory.setFile(file);
                directory.setContentDirectory(getContentDirectory());
                fileNodeList.add(directory);
            } else if (file.isFile()) {
                FileNode itemNode = createCompareItemNode(file);
                if (itemNode != null) {
                    fileNodeList.add(itemNode);
                }
            }
        }
        return fileNodeList.size();
    }

    private int getDirectoryItemNodeList(File dirFile, FileItemNodeList itemNodeList) {
        for (File file : dirFile.listFiles()) {
            if (file.isDirectory()) {
                getDirectoryItemNodeList(file, itemNodeList);
            } else if (file.isFile()) {
                FileItemNode itemNode = createCompareItemNode(file);
                if (itemNode != null) {
                    itemNodeList.add(itemNode);
                }
            }
        }
        return itemNodeList.size();
    }

    private FileNodeList getCurrentDirectoryFileNodeList() {
        FileNodeList nodeList = new FileNodeList();
        getDirectoryFileNodeList(new File(getPath()), nodeList);
        return nodeList;
    }

    private FileItemNodeList getCurrentDirectoryItemNodeList() {
        FileItemNodeList itemNodeList = new FileItemNodeList();
        getDirectoryItemNodeList(new File(getPath()), itemNodeList);
        return itemNodeList;
    }

    private FileNode getFileNode(File file) {
        int nContents = getNContentNodes();
        for (int i = 0; i < nContents; i++) {
            FileNode fileNode = (FileNode) getContentNode(i);
            if (fileNode.equals(file)) {
                return fileNode;
            }
        }
        return null;
    }

    private FileItemNode getItemNode(File file) {
        int nContents = getNContentNodes();
        for (int n = 0; n < nContents; n++) {
            ContentNode cnode = getContentNode(n);
            if (cnode instanceof FileItemNode) {
                FileItemNode itemNode = (FileItemNode) cnode;
                if (itemNode.equals(file)) {
                    return itemNode;
                }
            }
        }
        return null;
    }

    private void addItemNode(FileItemNode itemNode) {
        addContentNode(itemNode);
    }

    private void addFileNode(FileNode node) {
        addContentNode((ContentNode) node);
    }

    private boolean updateFileNodeList(FileNode newFileNode) {
        File newNodeFile = newFileNode.getFile();
        FileNode currentNode = getFileNode(newNodeFile);
        if (currentNode == null) {
            int newItemID = -1;
            if (newFileNode instanceof FileDirectory) {
                newItemID = getContentDirectory().getNextContainerID();
            } else if (newFileNode instanceof FileItemNode) {
                newItemID = getContentDirectory().getNextItemID();
            }
            ((ContentNode) newFileNode).setID(newItemID);
            updateFileNode(newFileNode, newNodeFile);
            addFileNode(newFileNode);
            return true;
        } else if (newFileNode.getFileTimeStamp() == newFileNode.getFileTimeStamp()) {
            return false;
        } else {
            updateFileNode(currentNode, newNodeFile);
            return true;
        }
    }

    private boolean updateItemNodeList(FileItemNode newItemNode) {
        File newItemNodeFile = newItemNode.getFile();
        FileItemNode currItemNode = getItemNode(newItemNodeFile);
        if (currItemNode == null) {
            newItemNode.setID(getContentDirectory().getNextItemID());
            updateItemNode(newItemNode, newItemNodeFile);
            addItemNode(newItemNode);
            return true;
        } else if (currItemNode.getFileTimeStamp() == newItemNode.getFileTimeStamp()) {
            return false;
        } else {
            updateItemNode(currItemNode, newItemNodeFile);
            return true;
        }
    }

    private boolean updateItemNodeList() {
        int n;
        boolean updateFlag = false;
        int nContents = getNContentNodes();
        ContentNode[] cnode = new ContentNode[nContents];
        for (n = 0; n < nContents; n++) {
            cnode[n] = getContentNode(n);
        }
        for (n = 0; n < nContents; n++) {
            if (cnode[n] instanceof FileItemNode) {
                File itemFile = cnode[n].getFile();
                if (!(itemFile == null || itemFile.exists())) {
                    removeContentNode(cnode[n]);
                    updateFlag = true;
                }
            }
        }
        FileItemNodeList itemNodeList = getCurrentDirectoryItemNodeList();
        int itemNodeCnt = itemNodeList.size();
        for (n = 0; n < itemNodeCnt; n++) {
            if (updateItemNodeList(itemNodeList.getFileItemNode(n))) {
                updateFlag = true;
            }
        }
        return updateFlag;
    }

    private boolean updateFileNodeList() {
        int n;
        boolean updateFlag = false;
        int nContents = getNContentNodes();
        ContentNode[] cnode = new ContentNode[nContents];
        for (n = 0; n < nContents; n++) {
            cnode[n] = getContentNode(n);
        }
        for (n = 0; n < nContents; n++) {
            ContentNode subNode = cnode[n];
            if ((subNode instanceof FileNode) && !((FileNode) subNode).getFile().exists()) {
                removeContentNode(cnode[n]);
                updateFlag = true;
            }
        }
        FileNodeList nodeList = getCurrentDirectoryFileNodeList();
        int count = nodeList.size();
        for (int i = 0; i < count; i++) {
            FileNode fnode = nodeList.getFileNode(i);
            updateFlag = updateFileNodeList(fnode);
            boolean flag = false;
            if (fnode instanceof FileDirectory) {
                flag = ((FileDirectory) fnode).update();
            }
            updateFlag |= flag;
        }
        return updateFlag;
    }

    public boolean update() {
        return updateFileNodeList();
    }

    public void setFile(File file) {
        this.itemFile = file;
    }

    public File getFile() {
        return this.itemFile;
    }

    public long getFileTimeStamp() {
        long itemFileTimeStamp = 0;
        if (this.itemFile != null) {
            try {
                itemFileTimeStamp = this.itemFile.lastModified();
            } catch (Exception e) {
                Debug.warning(e);
            }
        }
        return itemFileTimeStamp;
    }

    public boolean equals(File file) {
        if (this.itemFile == null) {
            return false;
        }
        return this.itemFile.equals(file);
    }

    public byte[] getContent() {
        return null;
    }

    public long getContentLength() {
        return 0;
    }

    public InputStream getContentInputStream() {
        return null;
    }
}
