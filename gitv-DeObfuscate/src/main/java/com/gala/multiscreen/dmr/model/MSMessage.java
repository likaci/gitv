package com.gala.multiscreen.dmr.model;

public class MSMessage {
    public static String VALUE_BOSS = "";
    public static String VALUE_CTYPE = "";
    public static String VALUE_KEY = "";
    public static String VALUE_SESSION = "";
    public static String VALUE_TITLE = "";
    public static String VALUE_TV_VERSION = "";

    public enum KeyKind {
        UNKOWN,
        RIGHT,
        UP,
        LEFT,
        DOWN,
        HOME,
        CLICK,
        BACK,
        MENU,
        VOLUME_UP,
        VOLUME_DOWN
    }

    public static class MSVALUE_CTYPE {
        public static final String ADVERTISEMENT = "2";
        public static final String LIVE = "3";
        public static final String SPECIAL = "1";
        public static final String VIEDO = "0";
    }

    public enum PlayKind {
        PLAY,
        PAUSE,
        STOP,
        PREVIOUS,
        NEXT,
        MUTE,
        VOLUME
    }

    public enum PushKind {
        VIDEO,
        MUSIC,
        PHOTO
    }

    public class RemoteCode {
        public static final byte BACK = (byte) 51;
        public static final byte CLICK = (byte) 50;
        public static final byte DOWN = (byte) 1;
        public static final byte FLING_LEFT = (byte) 57;
        public static final byte FLING_RIGHT = (byte) 58;
        public static final byte HOME = (byte) 49;
        public static final byte LEFT = (byte) 2;
        public static final byte MENU = (byte) 52;
        public static final byte RIGHT = (byte) 3;
        public static final byte SEEK_LEFT = (byte) 53;
        public static final byte SEEK_RIGHT = (byte) 54;
        public static final byte UP = (byte) 0;
        public static final byte UP_IOS = Byte.MAX_VALUE;
        public static final byte VOLUME_BOTTOM = (byte) 56;
        public static final byte VOLUME_DOWN = (byte) 81;
        public static final byte VOLUME_TOP = (byte) 55;
        public static final byte VOLUME_UP = (byte) 80;
    }

    public enum RequestKind {
        ONLINE,
        OFFLINE,
        PULLVIDEO
    }

    public enum SeekTimeKind {
        ABSOLUTE_TIME,
        ABSOLUTE_COUNT,
        RELATIVE_TIME,
        RELATIVE_COUNT
    }
}
