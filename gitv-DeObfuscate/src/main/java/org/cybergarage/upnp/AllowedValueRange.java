package org.cybergarage.upnp;

import org.cybergarage.xml.Node;

public class AllowedValueRange {
    public static final String ELEM_NAME = "allowedValueRange";
    private static final String MAXIMUM = "maximum";
    private static final String MINIMUM = "minimum";
    private static final String STEP = "step";
    private Node allowedValueRangeNode;

    public Node getAllowedValueRangeNode() {
        return this.allowedValueRangeNode;
    }

    public AllowedValueRange(Node node) {
        this.allowedValueRangeNode = node;
    }

    public AllowedValueRange() {
        this.allowedValueRangeNode = new Node(ELEM_NAME);
    }

    public AllowedValueRange(Number max, Number min, Number step) {
        this.allowedValueRangeNode = new Node(ELEM_NAME);
        if (max != null) {
            setMaximum(max.toString());
        }
        if (min != null) {
            setMinimum(min.toString());
        }
        if (step != null) {
            setStep(step.toString());
        }
    }

    public static boolean isAllowedValueRangeNode(Node node) {
        return ELEM_NAME.equals(node.getName());
    }

    public void setMinimum(String value) {
        getAllowedValueRangeNode().setNode(MINIMUM, value);
    }

    public String getMinimum() {
        return getAllowedValueRangeNode().getNodeValue(MINIMUM);
    }

    public void setMaximum(String value) {
        getAllowedValueRangeNode().setNode(MAXIMUM, value);
    }

    public String getMaximum() {
        return getAllowedValueRangeNode().getNodeValue(MAXIMUM);
    }

    public void setStep(String value) {
        getAllowedValueRangeNode().setNode(STEP, value);
    }

    public String getStep() {
        return getAllowedValueRangeNode().getNodeValue(STEP);
    }
}
