package org.cybergarage.xml.parser;

import java.io.InputStream;
import org.apache.xerces.parsers.DOMParser;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

public class XercesParser extends Parser {
    public Node parse(Node parentNode, org.w3c.dom.Node domNode, int rank) {
        int domNodeType = domNode.getNodeType();
        String domNodeName = domNode.getNodeName();
        String domNodeValue = domNode.getNodeValue();
        NamedNodeMap attrs = domNode.getAttributes();
        if (attrs != null) {
            int arrrsLen = attrs.getLength();
        }
        if (domNodeType == 3) {
            parentNode.setValue(domNodeValue);
            return parentNode;
        } else if (domNodeType != 1) {
            return parentNode;
        } else {
            Node node = new Node();
            node.setName(domNodeName);
            node.setValue(domNodeValue);
            if (parentNode != null) {
                parentNode.addNode(node);
            }
            NamedNodeMap attrMap = domNode.getAttributes();
            int attrLen = attrMap.getLength();
            for (int n = 0; n < attrLen; n++) {
                org.w3c.dom.Node attr = attrMap.item(n);
                node.setAttribute(attr.getNodeName(), attr.getNodeValue());
            }
            org.w3c.dom.Node child = domNode.getFirstChild();
            if (child == null) {
                node.setValue("");
                return node;
            }
            do {
                parse(node, child, rank + 1);
                child = child.getNextSibling();
            } while (child != null);
            return node;
        }
    }

    public Node parse(Node parentNode, org.w3c.dom.Node domNode) {
        return parse(parentNode, domNode, 0);
    }

    public Node parse(InputStream inStream) throws ParserException {
        Node root = null;
        try {
            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(inStream));
            Element docElem = parser.getDocument().getDocumentElement();
            if (docElem != null) {
                root = parse(null, docElem);
            }
            return root;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
