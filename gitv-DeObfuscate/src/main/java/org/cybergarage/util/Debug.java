package org.cybergarage.util;

import java.io.PrintStream;

public final class Debug {
    private static Debug debug = new Debug();
    private static boolean enabled = false;
    private PrintStream out = System.out;

    private Debug() {
    }

    public synchronized PrintStream getOut() {
        return this.out;
    }

    public synchronized void setOut(PrintStream out) {
        if (out != null) {
            this.out = out;
        }
    }

    public static Debug getDebug() {
        return debug;
    }

    public static final void on() {
        enabled = true;
    }

    public static final void off() {
        enabled = false;
    }

    public static boolean isOn() {
        return enabled;
    }

    public static final void message(String s) {
        if (enabled) {
            debug.getOut().println("igaladlna: " + s);
        }
    }

    public static final void message(String m1, String m2) {
        if (enabled) {
            debug.getOut().println("igaladlna: ");
            debug.getOut().println(m1);
            debug.getOut().println(m2);
        }
    }

    public static final void warning(String s) {
        if (enabled) {
            debug.getOut().println("igaladlna warning : " + s);
        }
    }

    public static final void warning(String m, Exception e) {
        if (e.getMessage() == null) {
            debug.getOut().println("igaladlna warning : " + m + " START");
            e.printStackTrace(debug.getOut());
            debug.getOut().println("igaladlna warning : " + m + " END");
            return;
        }
        debug.getOut().println("igaladlna warning : " + m + " (" + e.getMessage() + ")");
        e.printStackTrace(debug.getOut());
    }

    public static final void warning(Exception e) {
        warning(e.getMessage());
        e.printStackTrace(debug.getOut());
    }
}
