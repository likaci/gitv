package org.cybergarage.upnp.control;

import org.cybergarage.http.HTTPRequest;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.Service;
import org.cybergarage.upnp.StateVariable;
import org.cybergarage.xml.Node;

public class QueryRequest extends ControlRequest {
    public QueryRequest(HTTPRequest httpReq) {
        set(httpReq);
    }

    private Node getVarNameNode() {
        Node bodyNode = getBodyNode();
        if (bodyNode == null || !bodyNode.hasNodes()) {
            return null;
        }
        Node queryStateVarNode = bodyNode.getNode(0);
        if (queryStateVarNode == null || !queryStateVarNode.hasNodes()) {
            return null;
        }
        return queryStateVarNode.getNode(0);
    }

    public String getVarName() {
        Node node = getVarNameNode();
        if (node == null) {
            return "";
        }
        return node.getValue();
    }

    public void setRequest(StateVariable stateVar) {
        Service service = stateVar.getService();
        String ctrlURL = service.getControlURL();
        setRequestHost(service);
        setEnvelopeNode(SOAP.createEnvelopeBodyNode());
        Node envNode = getEnvelopeNode();
        getBodyNode().addNode(createContentNode(stateVar));
        setContent(envNode);
        setSOAPAction(Control.QUERY_SOAPACTION);
    }

    private Node createContentNode(StateVariable stateVar) {
        Node queryVarNode = new Node();
        queryVarNode.setName("u", Control.QUERY_STATE_VARIABLE);
        queryVarNode.setNameSpace("u", Control.XMLNS);
        Node varNode = new Node();
        varNode.setName("u", Control.VAR_NAME);
        varNode.setValue(stateVar.getName());
        queryVarNode.addNode(varNode);
        return queryVarNode;
    }

    public QueryResponse post(boolean isKeepAlive) {
        return new QueryResponse(postMessage(getRequestHost(), getRequestPort(), true, isKeepAlive));
    }
}
