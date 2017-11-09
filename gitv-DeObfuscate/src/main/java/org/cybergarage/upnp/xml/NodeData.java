package org.cybergarage.upnp.xml;

import org.cybergarage.xml.Node;

public class NodeData {
    private Node node;

    public NodeData() {
        setNode(null);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return this.node;
    }
}
