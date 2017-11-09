package org.cybergarage.upnp.control;

import org.cybergarage.soap.SOAP;
import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.UPnP;
import org.cybergarage.upnp.UPnPStatus;
import org.cybergarage.upnp.std.av.server.object.DIDLLite;
import org.cybergarage.xml.Node;

public class ControlResponse extends SOAPResponse {
    public static final String FAULT_CODE = "Client";
    public static final String FAULT_STRING = "UPnPError";
    private UPnPStatus upnpErr = new UPnPStatus();

    public ControlResponse() {
        setServer(UPnP.getServerName());
    }

    public ControlResponse(SOAPResponse soapRes) {
        super(soapRes);
    }

    public void setFaultResponse(int errCode, String errDescr) {
        setStatusCode(500);
        getBodyNode().addNode(createFaultResponseNode(errCode, errDescr));
        setContent(getEnvelopeNode());
    }

    public void setFaultResponse(int errCode) {
        setFaultResponse(errCode, UPnPStatus.code2String(errCode));
    }

    private Node createFaultResponseNode(int errCode, String errDescr) {
        Node faultNode = new Node("s:Fault");
        Node faultCodeNode = new Node(SOAP.FAULT_CODE);
        faultCodeNode.setValue("s:Client");
        faultNode.addNode(faultCodeNode);
        Node faultStringNode = new Node(SOAP.FAULT_STRING);
        faultStringNode.setValue("UPnPError");
        faultNode.addNode(faultStringNode);
        Node detailNode = new Node("detail");
        faultNode.addNode(detailNode);
        Node upnpErrorNode = new Node("UPnPError");
        upnpErrorNode.setAttribute(DIDLLite.XMLNS, Control.XMLNS);
        detailNode.addNode(upnpErrorNode);
        Node errorCodeNode = new Node(SOAP.ERROR_CODE);
        errorCodeNode.setValue(errCode);
        upnpErrorNode.addNode(errorCodeNode);
        Node errorDesctiprionNode = new Node(SOAP.ERROR_DESCRIPTION);
        errorDesctiprionNode.setValue(errDescr);
        upnpErrorNode.addNode(errorDesctiprionNode);
        return faultNode;
    }

    private Node createFaultResponseNode(int errCode) {
        return createFaultResponseNode(errCode, UPnPStatus.code2String(errCode));
    }

    private Node getUPnPErrorNode() {
        Node detailNode = getFaultDetailNode();
        if (detailNode == null) {
            return null;
        }
        return detailNode.getNodeEndsWith("UPnPError");
    }

    private Node getUPnPErrorCodeNode() {
        Node errorNode = getUPnPErrorNode();
        if (errorNode == null) {
            return null;
        }
        return errorNode.getNodeEndsWith(SOAP.ERROR_CODE);
    }

    private Node getUPnPErrorDescriptionNode() {
        Node errorNode = getUPnPErrorNode();
        if (errorNode == null) {
            return null;
        }
        return errorNode.getNodeEndsWith(SOAP.ERROR_DESCRIPTION);
    }

    public int getUPnPErrorCode() {
        int i = -1;
        Node errorCodeNode = getUPnPErrorCodeNode();
        if (errorCodeNode != null) {
            try {
                i = Integer.parseInt(errorCodeNode.getValue());
            } catch (Exception e) {
            }
        }
        return i;
    }

    public String getUPnPErrorDescription() {
        Node errorDescNode = getUPnPErrorDescriptionNode();
        if (errorDescNode == null) {
            return "";
        }
        return errorDescNode.getValue();
    }

    public UPnPStatus getUPnPError() {
        String desc = "";
        int code = getUPnPErrorCode();
        desc = getUPnPErrorDescription();
        this.upnpErr.setCode(code);
        this.upnpErr.setDescription(desc);
        return this.upnpErr;
    }
}
