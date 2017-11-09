package org.cybergarage.upnp;

import org.cybergarage.upnp.xml.ArgumentData;
import org.cybergarage.xml.Node;

public class Argument {
    private static final String DIRECTION = "direction";
    public static final String ELEM_NAME = "argument";
    public static final String IN = "in";
    private static final String NAME = "name";
    public static final String OUT = "out";
    private static final String RELATED_STATE_VARIABLE = "relatedStateVariable";
    private Node argumentNode;
    private Node serviceNode;
    private Object userData;

    public Node getArgumentNode() {
        return this.argumentNode;
    }

    private Node getServiceNode() {
        return this.serviceNode;
    }

    public Service getService() {
        return new Service(getServiceNode());
    }

    void setService(Service s) {
        s.getServiceNode();
    }

    public Node getActionNode() {
        Node argumentLinstNode = getArgumentNode().getParentNode();
        if (argumentLinstNode == null) {
            return null;
        }
        Node actionNode = argumentLinstNode.getParentNode();
        if (actionNode == null) {
            return null;
        }
        if (Action.isActionNode(actionNode)) {
            return actionNode;
        }
        return null;
    }

    public Action getAction() {
        return new Action(getServiceNode(), getActionNode());
    }

    public Argument() {
        this.userData = null;
        this.argumentNode = new Node(ELEM_NAME);
        this.serviceNode = null;
    }

    public Argument(Node servNode) {
        this.userData = null;
        this.argumentNode = new Node(ELEM_NAME);
        this.serviceNode = servNode;
    }

    public Argument(Node servNode, Node argNode) {
        this.userData = null;
        this.serviceNode = servNode;
        this.argumentNode = argNode;
    }

    public Argument(String name, String value) {
        this();
        setName(name);
        setValue(value);
    }

    public static boolean isArgumentNode(Node node) {
        return ELEM_NAME.equals(node.getName());
    }

    public void setName(String value) {
        getArgumentNode().setNode("name", value);
    }

    public String getName() {
        return getArgumentNode().getNodeValue("name");
    }

    public void setDirection(String value) {
        getArgumentNode().setNode(DIRECTION, value);
    }

    public String getDirection() {
        return getArgumentNode().getNodeValue(DIRECTION);
    }

    public boolean isInDirection() {
        String dir = getDirection();
        if (dir == null) {
            return false;
        }
        return dir.equalsIgnoreCase(IN);
    }

    public boolean isOutDirection() {
        return !isInDirection();
    }

    public void setRelatedStateVariableName(String value) {
        getArgumentNode().setNode(RELATED_STATE_VARIABLE, value);
    }

    public String getRelatedStateVariableName() {
        return getArgumentNode().getNodeValue(RELATED_STATE_VARIABLE);
    }

    public StateVariable getRelatedStateVariable() {
        Service service = getService();
        if (service == null) {
            return null;
        }
        return service.getStateVariable(getRelatedStateVariableName());
    }

    private ArgumentData getArgumentData() {
        Node node = getArgumentNode();
        ArgumentData userData = (ArgumentData) node.getUserData();
        if (userData != null) {
            return userData;
        }
        userData = new ArgumentData();
        node.setUserData(userData);
        userData.setNode(node);
        return userData;
    }

    public void setValue(String value) {
        getArgumentData().setValue(value);
        if (value != null) {
            Node argument = getArgumentNode();
            if (argument != null) {
                Node relateNode = argument.getNode(RELATED_STATE_VARIABLE);
                if (relateNode != null) {
                    ServiceStateTable stateTable = getService().getServiceStateTable();
                    int tableSize = stateTable.size();
                    for (int n = 0; n < tableSize; n++) {
                        StateVariable var = stateTable.getStateVariable(n);
                        if (relateNode.getValue().compareTo(var.getStateVariableNode().getNodeValue("name")) == 0) {
                            var.setValue(value, false);
                            return;
                        }
                    }
                }
            }
        }
    }

    public void setValue(int value) {
        setValue(Integer.toString(value));
    }

    public String getValue() {
        return getArgumentData().getValue();
    }

    public int getIntegerValue() {
        try {
            return Integer.parseInt(getValue());
        } catch (Exception e) {
            return 0;
        }
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }
}
