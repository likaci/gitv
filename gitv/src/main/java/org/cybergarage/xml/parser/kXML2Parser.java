package org.cybergarage.xml.parser;

import java.io.InputStream;
import java.io.InputStreamReader;
import org.cybergarage.xml.Node;
import org.cybergarage.xml.Parser;
import org.cybergarage.xml.ParserException;
import org.kxml2.io.KXmlParser;

public class kXML2Parser extends Parser {
    public Node parse(InputStream inStream) throws ParserException {
        Node rootNode = null;
        Node currNode = null;
        try {
            InputStreamReader inReader = new InputStreamReader(inStream);
            KXmlParser xpp = new KXmlParser();
            xpp.setInput(inReader);
            for (int eventType = xpp.getEventType(); eventType != 1; eventType = xpp.next()) {
                switch (eventType) {
                    case 2:
                        Node node = new Node();
                        node.setName(xpp.getName());
                        int attrsLen = xpp.getAttributeCount();
                        for (int n = 0; n < attrsLen; n++) {
                            node.setAttribute(xpp.getAttributeName(n), xpp.getAttributeValue(n));
                        }
                        if (currNode != null) {
                            currNode.addNode(node);
                        }
                        currNode = node;
                        if (rootNode != null) {
                            break;
                        }
                        rootNode = node;
                        break;
                    case 3:
                        currNode = currNode.getParentNode();
                        break;
                    case 4:
                        String value = xpp.getText();
                        if (currNode == null) {
                            break;
                        }
                        currNode.setValue(value);
                        break;
                    default:
                        break;
                }
            }
            return rootNode;
        } catch (Exception e) {
            throw new ParserException(e);
        }
    }
}
