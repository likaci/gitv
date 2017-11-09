package org.cybergarage.upnp;

import org.cybergarage.xml.Node;

public class AllowedValue {
    public static final String ELEM_NAME = "allowedValue";
    private Node allowedValueNode;

    public Node getAllowedValueNode() {
        return this.allowedValueNode;
    }

    public AllowedValue(Node node) {
        this.allowedValueNode = node;
    }

    public AllowedValue(String value) {
        this.allowedValueNode = new Node(ELEM_NAME);
        setValue(value);
    }

    public static boolean isAllowedValueNode(Node node) {
        return ELEM_NAME.equals(node.getName());
    }

    public void setValue(String value) {
        getAllowedValueNode().setValue(value);
    }

    public String getValue() {
        return getAllowedValueNode().getValue();
    }
}
