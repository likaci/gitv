package org.cybergarage.soap;

import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPResponse;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

public class SOAPResponse extends HTTPResponse {
    private Node rootNode;

    public SOAPResponse() {
        setRootNode(SOAP.createEnvelopeBodyNode());
        setContentType("text/xml; charset=\"utf-8\"");
    }

    public SOAPResponse(HTTPResponse httpRes) {
        super(httpRes);
        setRootNode(SOAP.createEnvelopeBodyNode());
        setContentType("text/xml; charset=\"utf-8\"");
    }

    public SOAPResponse(SOAPResponse soapRes) {
        super((HTTPResponse) soapRes);
        setEnvelopeNode(soapRes.getEnvelopeNode());
        setContentType("text/xml; charset=\"utf-8\"");
    }

    private void setRootNode(Node node) {
        this.rootNode = node;
    }

    private Node getRootNode() {
        return this.rootNode;
    }

    public void setEnvelopeNode(Node node) {
        setRootNode(node);
    }

    public Node getEnvelopeNode() {
        return getRootNode();
    }

    public Node getBodyNode() {
        Node envNode = getEnvelopeNode();
        if (envNode == null) {
            return null;
        }
        return envNode.getNodeEndsWith(SOAP.BODY);
    }

    public Node getMethodResponseNode(String name) {
        Node bodyNode = getBodyNode();
        if (bodyNode == null) {
            return null;
        }
        return bodyNode.getNodeEndsWith(new StringBuilder(String.valueOf(name)).append(SOAP.RESPONSE).toString());
    }

    public Node getFaultNode() {
        Node bodyNode = getBodyNode();
        if (bodyNode == null) {
            return null;
        }
        return bodyNode.getNodeEndsWith(SOAP.FAULT);
    }

    public Node getFaultCodeNode() {
        Node faultNode = getFaultNode();
        if (faultNode == null) {
            return null;
        }
        return faultNode.getNodeEndsWith(SOAP.FAULT_CODE);
    }

    public Node getFaultStringNode() {
        Node faultNode = getFaultNode();
        if (faultNode == null) {
            return null;
        }
        return faultNode.getNodeEndsWith(SOAP.FAULT_STRING);
    }

    public Node getFaultActorNode() {
        Node faultNode = getFaultNode();
        if (faultNode == null) {
            return null;
        }
        return faultNode.getNodeEndsWith(SOAP.FAULTACTOR);
    }

    public Node getFaultDetailNode() {
        Node faultNode = getFaultNode();
        if (faultNode == null) {
            return null;
        }
        return faultNode.getNodeEndsWith("detail");
    }

    public String getFaultCode() {
        Node node = getFaultCodeNode();
        if (node == null) {
            return "";
        }
        return node.getValue();
    }

    public String getFaultString() {
        Node node = getFaultStringNode();
        if (node == null) {
            return "";
        }
        return node.getValue();
    }

    public String getFaultActor() {
        Node node = getFaultActorNode();
        if (node == null) {
            return "";
        }
        return node.getValue();
    }

    public void setContent(Node node) {
        setContent(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("" + "<?xml version=\"1.0\" encoding=\"utf-8\"?>")).append("\n").toString())).append(node.toString()).toString());
    }

    public void print() {
        System.out.println("------------------------------DUMP SOAPResponse [Start]------------------------------");
        Debug.message(toString().replace(HTTP.CRLF, HTTP.TAB));
        if (hasContent()) {
            System.out.println("-------------------------------DUMP SOAPResponse [End]-------------------------------");
            return;
        }
        Node rootElem = getRootNode();
        if (rootElem == null) {
            System.out.println("-------------------------------DUMP SOAPResponse [End]-------------------------------");
            return;
        }
        Debug.message(rootElem.toString().replace(HTTP.CRLF, HTTP.TAB));
        System.out.println("-------------------------------DUMP SOAPResponse [End]-------------------------------");
    }
}
