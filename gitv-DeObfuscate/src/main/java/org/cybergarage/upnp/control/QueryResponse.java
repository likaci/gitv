package org.cybergarage.upnp.control;

import org.cybergarage.soap.SOAPResponse;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.xml.Node;

public class QueryResponse extends ControlResponse {
    public QueryResponse(SOAPResponse soapRes) {
        super(soapRes);
    }

    private Node getReturnNode() {
        Node bodyNode = getBodyNode();
        if (bodyNode == null || !bodyNode.hasNodes()) {
            return null;
        }
        Node queryResNode = bodyNode.getNode(0);
        if (queryResNode == null || !queryResNode.hasNodes()) {
            return null;
        }
        return queryResNode.getNode(0);
    }

    public String getReturnValue() {
        Node node = getReturnNode();
        if (node == null) {
            return "";
        }
        return node.getValue();
    }

    public void setResponse(StateVariable stateVar) {
        String var = stateVar.getValue_dlna();
        setStatusCode(200);
        getBodyNode().addNode(createResponseNode(var));
        setContent(getEnvelopeNode());
    }

    private Node createResponseNode(String var) {
        Node queryResNode = new Node();
        queryResNode.setName("u", Control.QUERY_STATE_VARIABLE_RESPONSE);
        queryResNode.setNameSpace("u", Control.XMLNS);
        Node returnNode = new Node();
        returnNode.setName(Control.RETURN);
        returnNode.setValue(var);
        queryResNode.addNode(returnNode);
        return queryResNode;
    }
}
