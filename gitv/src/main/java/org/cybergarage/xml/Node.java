package org.cybergarage.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import org.cybergarage.soap.SOAP;
import org.cybergarage.upnp.std.av.server.object.DIDLLite;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class Node {
    private AttributeList attrList;
    private String name;
    private NodeList nodeList;
    private Node parentNode;
    private Object userData;
    private String value;

    public Node() {
        this.parentNode = null;
        this.name = new String();
        this.value = new String();
        this.attrList = new AttributeList();
        this.nodeList = new NodeList();
        this.userData = null;
        setUserData(null);
        setParentNode(null);
    }

    public Node(String name) {
        this();
        setName(name);
    }

    public Node(String ns, String name) {
        this();
        setName(ns, name);
    }

    public void setParentNode(Node node) {
        this.parentNode = node;
    }

    public Node getParentNode() {
        return this.parentNode;
    }

    public Node getRootNode() {
        Node rootNode = null;
        Node parentNode = getParentNode();
        while (parentNode != null) {
            rootNode = parentNode;
            parentNode = rootNode.getParentNode();
        }
        return rootNode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setName(String ns, String name) {
        this.name = new StringBuilder(String.valueOf(ns)).append(SOAP.DELIM).append(name).toString();
    }

    public String getName() {
        return this.name;
    }

    public boolean isName(String value) {
        return this.name.equals(value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setValue(int value) {
        setValue(Integer.toString(value));
    }

    public void addValue(String value) {
        if (this.value == null) {
            this.value = value;
        } else if (value != null) {
            this.value += value;
        }
    }

    public String getValue() {
        return this.value;
    }

    public int getNAttributes() {
        return this.attrList.size();
    }

    public Attribute getAttribute(int index) {
        return this.attrList.getAttribute(index);
    }

    public Attribute getAttribute(String name) {
        return this.attrList.getAttribute(name);
    }

    public void addAttribute(Attribute attr) {
        this.attrList.add(attr);
    }

    public void insertAttributeAt(Attribute attr, int index) {
        this.attrList.insertElementAt(attr, index);
    }

    public void addAttribute(String name, String value) {
        addAttribute(new Attribute(name, value));
    }

    public boolean removeAttribute(Attribute attr) {
        return this.attrList.remove(attr);
    }

    public boolean removeAttribute(String name) {
        return removeAttribute(getAttribute(name));
    }

    public boolean hasAttributes() {
        if (getNAttributes() > 0) {
            return true;
        }
        return false;
    }

    public void setAttribute(String name, String value) {
        Attribute attr = getAttribute(name);
        if (attr != null) {
            attr.setValue(value);
        } else {
            addAttribute(new Attribute(name, value));
        }
    }

    public void setAttribute(String name, int value) {
        setAttribute(name, Integer.toString(value));
    }

    public String getAttributeValue(String name) {
        Attribute attr = getAttribute(name);
        if (attr != null) {
            return attr.getValue();
        }
        return "";
    }

    public int getAttributeIntegerValue(String name) {
        try {
            return Integer.parseInt(getAttributeValue(name));
        } catch (Exception e) {
            return 0;
        }
    }

    public void setNameSpace(String ns, String value) {
        setAttribute("xmlns:" + ns, value);
    }

    public void setNameSpace(String value) {
        setAttribute(DIDLLite.XMLNS, value);
    }

    public int getNNodes() {
        return this.nodeList.size();
    }

    public Node getNode(int index) {
        return this.nodeList.getNode(index);
    }

    public Node getNode(String name) {
        return this.nodeList.getNode(name);
    }

    public Node getNodeEndsWith(String name) {
        return this.nodeList.getEndsWith(name);
    }

    public void addNode(Node node) {
        node.setParentNode(this);
        if (!this.nodeList.contains(node)) {
            this.nodeList.add(node);
        }
    }

    public void insertNode(Node node, int index) {
        node.setParentNode(this);
        this.nodeList.insertElementAt(node, index);
    }

    public int getIndex(String name) {
        int index = -1;
        Iterator i = this.nodeList.iterator();
        while (i.hasNext()) {
            index++;
            if (((Node) i.next()).getName().equals(name)) {
                return index;
            }
        }
        return index;
    }

    public boolean removeNode(Node node) {
        node.setParentNode(null);
        return this.nodeList.remove(node);
    }

    public boolean removeNode(String name) {
        return this.nodeList.remove(getNode(name));
    }

    public void removeAllNodes() {
        this.nodeList.clear();
    }

    public boolean hasNodes() {
        if (getNNodes() > 0) {
            return true;
        }
        return false;
    }

    public void setNode(String name, String value) {
        Node node = getNode(name);
        if (node != null) {
            node.setValue(value);
            return;
        }
        node = new Node(name);
        node.setValue(value);
        addNode(node);
    }

    public String getNodeValue(String name) {
        Node node = getNode(name);
        if (node != null) {
            return node.getValue();
        }
        return "";
    }

    public void setUserData(Object data) {
        this.userData = data;
    }

    public Object getUserData() {
        return this.userData;
    }

    public String getIndentLevelString(int nIndentLevel) {
        return getIndentLevelString(nIndentLevel, "   ");
    }

    public String getIndentLevelString(int nIndentLevel, String space) {
        StringBuffer indentString = new StringBuffer(space.length() * nIndentLevel);
        for (int n = 0; n < nIndentLevel; n++) {
            indentString.append(space);
        }
        return indentString.toString();
    }

    public void outputAttributes(PrintWriter ps) {
        int nAttributes = getNAttributes();
        for (int n = 0; n < nAttributes; n++) {
            Attribute attr = getAttribute(n);
            ps.print(" " + attr.getName() + "=\"" + XML.escapeXMLChars(attr.getValue()) + "\"");
        }
    }

    public void outputAttributes(OutputStream out) {
        int nAttributes = getNAttributes();
        for (int n = 0; n < nAttributes; n++) {
            Attribute attr = getAttribute(n);
            try {
                out.write((" " + attr.getName() + "=\"" + XML.escapeXMLChars(attr.getValue()) + "\"").getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void output(PrintWriter ps, int indentLevel, boolean hasChildNode) {
        String indentString = getIndentLevelString(indentLevel);
        String name = getName();
        String value = getValue();
        if (hasNodes() && hasChildNode) {
            ps.print(new StringBuilder(String.valueOf(indentString)).append(SearchCriteria.LT).append(name).toString());
            outputAttributes(ps);
            ps.println(SearchCriteria.GT);
            int nChildNodes = getNNodes();
            for (int n = 0; n < nChildNodes; n++) {
                getNode(n).output(ps, indentLevel + 1, true);
            }
            ps.println(new StringBuilder(String.valueOf(indentString)).append("</").append(name).append(SearchCriteria.GT).toString());
            return;
        }
        ps.print(new StringBuilder(String.valueOf(indentString)).append(SearchCriteria.LT).append(name).toString());
        outputAttributes(ps);
        if (value == null || value.length() == 0) {
            ps.println("></" + name + SearchCriteria.GT);
        } else {
            ps.println(new StringBuilder(SearchCriteria.GT).append(XML.escapeXMLChars(value)).append("</").append(name).append(SearchCriteria.GT).toString());
        }
    }

    public void output(OutputStream out, int indentLevel, boolean hasChildNode) {
        String indentString = getIndentLevelString(indentLevel);
        String name = getName();
        String value = getValue();
        if (hasNodes() && hasChildNode) {
            try {
                out.write(new StringBuilder(String.valueOf(indentString)).append(SearchCriteria.LT).append(name).toString().getBytes());
                outputAttributes(out);
                out.write(SearchCriteria.GT.getBytes());
                int nChildNodes = getNNodes();
                for (int n = 0; n < nChildNodes; n++) {
                    getNode(n).output(out, indentLevel + 1, true);
                }
                out.write(new StringBuilder(String.valueOf(indentString)).append("</").append(name).append(SearchCriteria.GT).toString().getBytes());
                return;
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
        try {
            out.write(new StringBuilder(String.valueOf(indentString)).append(SearchCriteria.LT).append(name).toString().getBytes());
            outputAttributes(out);
            if (value == null || value.length() == 0) {
                out.write(("></" + name + SearchCriteria.GT).getBytes());
            } else {
                out.write(new StringBuilder(SearchCriteria.GT).append(XML.escapeXMLChars(value)).append("</").append(name).append(SearchCriteria.GT).toString().getBytes());
            }
        } catch (IOException e2) {
            e2.printStackTrace();
        }
    }

    public String toString(String enc, boolean hasChildNode) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        PrintWriter pr = new PrintWriter(byteOut);
        output(pr, 0, hasChildNode);
        pr.flush();
        if (enc != null) {
            try {
                if (enc.length() > 0) {
                    return byteOut.toString(enc);
                }
            } catch (UnsupportedEncodingException e) {
            }
        }
        return byteOut.toString();
    }

    public String toString() {
        return toString(XML.CHARSET_UTF8, true);
    }

    public String toXMLString(boolean hasChildNode) {
        return toString().replaceAll(SearchCriteria.LT, "&lt;").replaceAll(SearchCriteria.GT, "&gt;").replaceAll("&", "&amp;").replaceAll("\"", "&quot;").replaceAll("'", "&apos;");
    }

    public String toXMLString() {
        return toXMLString(true);
    }

    public void print(boolean hasChildNode) {
        PrintWriter pr = new PrintWriter(System.out);
        output(pr, 0, hasChildNode);
        pr.flush();
    }

    public void print() {
        print(true);
    }
}
