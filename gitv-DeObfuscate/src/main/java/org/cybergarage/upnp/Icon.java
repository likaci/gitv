package org.cybergarage.upnp;

import org.cybergarage.xml.Node;

public class Icon {
    private static final String DEPTH = "depth";
    public static final String ELEM_NAME = "icon";
    private static final String HEIGHT = "height";
    private static final String MIME_TYPE = "mimetype";
    private static final String URL = "url";
    private static final String WIDTH = "width";
    private Node iconNode;
    private Object userData;

    public Node getIconNode() {
        return this.iconNode;
    }

    public Icon(Node node) {
        this.userData = null;
        this.iconNode = node;
    }

    public Icon(int width, int height, String depth, String url) {
        this(new Node(ELEM_NAME));
        setMimeType("image/png");
        setWidth(width);
        setHeight(height);
        setDepth(depth);
        setURL(url);
    }

    public Icon() {
        this.userData = null;
        this.iconNode = new Node(ELEM_NAME);
    }

    public static boolean isIconNode(Node node) {
        return ELEM_NAME.equals(node.getName());
    }

    public void setMimeType(String value) {
        getIconNode().setNode("mimetype", value);
    }

    public String getMimeType() {
        return getIconNode().getNodeValue("mimetype");
    }

    public void setWidth(String value) {
        getIconNode().setNode("width", value);
    }

    public void setWidth(int value) {
        try {
            setWidth(Integer.toString(value));
        } catch (Exception e) {
        }
    }

    public int getWidth() {
        try {
            return Integer.parseInt(getIconNode().getNodeValue("width"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setHeight(String value) {
        getIconNode().setNode("height", value);
    }

    public void setHeight(int value) {
        try {
            setHeight(Integer.toString(value));
        } catch (Exception e) {
        }
    }

    public int getHeight() {
        try {
            return Integer.parseInt(getIconNode().getNodeValue("height"));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setDepth(String value) {
        getIconNode().setNode(DEPTH, value);
    }

    public String getDepth() {
        return getIconNode().getNodeValue(DEPTH);
    }

    public void setURL(String value) {
        getIconNode().setNode("url", value);
    }

    public String getURL() {
        return getIconNode().getNodeValue("url");
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }
}
