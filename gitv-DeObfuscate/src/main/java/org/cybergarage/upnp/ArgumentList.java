package org.cybergarage.upnp;

import java.util.Vector;

public class ArgumentList extends Vector<Argument> {
    public static final String ELEM_NAME = "argumentList";
    private static final long serialVersionUID = 1;

    public Argument getArgument(int n) {
        try {
            return (Argument) get(n);
        } catch (Exception e) {
            return null;
        }
    }

    public Argument getArgument(String name) {
        int nArgs = size();
        for (int n = 0; n < nArgs; n++) {
            Argument arg = getArgument(n);
            String argName = arg.getName();
            if (argName != null && argName.equals(name)) {
                return arg;
            }
        }
        return null;
    }

    public void set(ArgumentList inArgList) {
        int nInArgs = inArgList.size();
        for (int n = 0; n < nInArgs; n++) {
            Argument inArg = inArgList.getArgument(n);
            Argument arg = getArgument(inArg.getName());
            if (arg != null) {
                arg.setValue(inArg.getValue());
            }
        }
    }

    public void setReqArgs(ArgumentList inArgList) {
        int nArgs = size();
        for (int n = 0; n < nArgs; n++) {
            Argument arg = getArgument(n);
            if (arg.isInDirection()) {
                String argName = arg.getName();
                Argument inArg = inArgList.getArgument(argName);
                if (inArg == null) {
                    throw new IllegalArgumentException("Argument \"" + argName + "\" missing.");
                }
                arg.setValue(inArg.getValue());
            }
        }
    }

    public void setResArgs(ArgumentList outArgList) {
        int nArgs = size();
        for (int n = 0; n < nArgs; n++) {
            Argument arg = getArgument(n);
            if (arg.isOutDirection()) {
                String argName = arg.getName();
                Argument outArg = outArgList.getArgument(argName);
                if (outArg == null) {
                    throw new IllegalArgumentException("Argument \"" + argName + "\" missing.");
                }
                arg.setValue(outArg.getValue());
            }
        }
    }
}
