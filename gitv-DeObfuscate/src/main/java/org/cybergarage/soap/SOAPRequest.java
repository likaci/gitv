package org.cybergarage.soap;

import com.gala.android.dlna.sdk.stddmrcontroller.Util;
import java.io.ByteArrayInputStream;
import org.cybergarage.http.HTTP;
import org.cybergarage.http.HTTPRequest;
import org.cybergarage.util.Debug;
import org.cybergarage.xml.Node;

public class SOAPRequest extends HTTPRequest {
    private static final String SOAPACTION = "SOAPACTION";
    private Node rootNode;

    public SOAPRequest() {
        setContentType("text/xml; charset=\"utf-8\"");
        setMethod(HTTP.POST);
        setStringHeader("User-Agent", Util.getTag(true) + "DLNA/" + Util.getTag(false) + "dlna/1.0");
    }

    public SOAPRequest(HTTPRequest httpReq) {
        set(httpReq);
    }

    public void setSOAPAction(String action) {
        setStringHeader("SOAPACTION", action);
    }

    public String getSOAPAction() {
        return getStringHeaderValue("SOAPACTION");
    }

    public boolean isSOAPAction(String value) {
        String headerValue = getHeaderValue("SOAPACTION");
        if (headerValue == null) {
            return false;
        }
        if (headerValue.equals(value)) {
            return true;
        }
        String soapAction = getSOAPAction();
        if (soapAction != null) {
            return soapAction.equals(value);
        }
        return false;
    }

    public SOAPResponse postMessage(String host, int port, boolean isNeedReply, boolean isKeepAlive) {
        if (isNeedReply) {
            SOAPResponse soapRes = new SOAPResponse(post(host, port, isKeepAlive));
            byte[] content = soapRes.getContent();
            if (content.length <= 0) {
                return soapRes;
            }
            try {
                soapRes.setEnvelopeNode(SOAP.getXMLParser().parse(new ByteArrayInputStream(content)));
                return soapRes;
            } catch (Exception e) {
                Debug.warning(e);
                return soapRes;
            }
        } else if (justPost(host, port, isKeepAlive)) {
            return new SOAPResponse();
        } else {
            return null;
        }
    }

    private void setRootNode(Node node) {
        this.rootNode = node;
    }

    private synchronized Node getRootNode() {
        Node node;
        if (this.rootNode != null) {
            node = this.rootNode;
        } else {
            try {
                this.rootNode = SOAP.getXMLParser().parse(new ByteArrayInputStream(getContent()));
            } catch (Exception e) {
                Debug.warning(e);
            }
            node = this.rootNode;
        }
        return node;
    }

    public void setEnvelopeNode(Node node) {
        setRootNode(node);
    }

    public Node getEnvelopeNode() {
        return getRootNode();
    }

    public Node getBodyNode() {
        Node envNode = getEnvelopeNode();
        if (envNode != null && envNode.hasNodes()) {
            return envNode.getNode(0);
        }
        return null;
    }

    public void setContent(Node node) {
        setContent(new StringBuilder(String.valueOf(new StringBuilder(String.valueOf("" + "<?xml version=\"1.0\" encoding=\"utf-8\"?>")).append("\n").toString())).append(node.toString()).toString().trim());
    }

    public void print() {
        System.out.println("------------------------------DUMP SOAPRequest [Start]------------------------------");
        Debug.message(toString().replace(HTTP.CRLF, HTTP.TAB));
        if (hasContent()) {
            System.out.println("-------------------------------DUMP SOAPRequest [End]-------------------------------");
            return;
        }
        Node rootElem = getRootNode();
        if (rootElem == null) {
            System.out.println("-------------------------------DUMP SOAPRequest [End]-------------------------------");
            return;
        }
        Debug.message(rootElem.toString().replace(HTTP.CRLF, HTTP.TAB));
        System.out.println("-------------------------------DUMP SOAPRequest [End]-------------------------------");
    }
}
