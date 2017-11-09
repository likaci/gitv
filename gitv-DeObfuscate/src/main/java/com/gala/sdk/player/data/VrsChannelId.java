package com.gala.sdk.player.data;

public class VrsChannelId {
    public static final int VRS_CHANNELID_CARTOON = 4;
    public static final int VRS_CHANNELID_CHILDREN = 15;
    public static final int VRS_CHANNELID_DOCUMENTARY = 3;
    public static final int VRS_CHANNELID_EDUCATION = 12;
    public static final int VRS_CHANNELID_EPISODE = 2;
    public static final int VRS_CHANNELID_FILM = 1;
    public static final int VRS_CHANNELID_MCRO_FILM = 16;
    public static final int VRS_CHANNELID_PUBLIC_CLASS = 11;
    public static final int VRS_CHANNELID_TRAVEL = 9;
    public static final int VRS_CHANNELID_VARIETY = 6;

    public static String getArea(int vrsChannelId) {
        String str = "t_bee";
        switch (vrsChannelId) {
            case 1:
            case 2:
            case 3:
            case 4:
            case 6:
            case 9:
            case 11:
            case 12:
            case 15:
            case 16:
                return str;
            default:
                return "t_swan";
        }
    }

    public static String getEndArea() {
        return "t_squirrel";
    }
}
