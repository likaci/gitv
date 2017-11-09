package org.cybergarage.xml;

import java.util.Vector;

public class NodeList extends Vector<Node> {
    private static final long serialVersionUID = 1;

    public Node getNode(int n) {
        try {
            return (Node) get(n);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Node getNode(String name) {
        if (name == null) {
            return null;
        }
        int nLists = size();
        for (int n = 0; n < nLists; n++) {
            Node node = getNode(n);
            if (node != null && name.compareTo(node.getName()) == 0) {
                return node;
            }
        }
        return null;
    }

    public Node getEndsWith(String name) {
        if (name == null) {
            return null;
        }
        int nLists = size();
        for (int n = 0; n < nLists; n++) {
            Node node = getNode(n);
            if (node != null) {
                String nodeName = node.getName();
                if (nodeName != null && nodeName.endsWith(name)) {
                    return node;
                }
            }
        }
        return null;
    }
}
