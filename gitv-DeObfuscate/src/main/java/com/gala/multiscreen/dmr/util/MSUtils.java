package com.gala.multiscreen.dmr.util;

import com.gala.multiscreen.dmr.model.MSMessage.KeyKind;
import org.cybergarage.soap.SOAP;

public class MSUtils {
    public static short getKeyCode(KeyKind keyKind) {
        switch (keyKind) {
            case LEFT:
                return (short) 2;
            case RIGHT:
                return (short) 3;
            case UP:
                return (short) 0;
            case DOWN:
                return (short) 1;
            case CLICK:
                return (short) 50;
            case BACK:
                return (short) 51;
            case HOME:
                return (short) 49;
            case MENU:
                return (short) 52;
            default:
                return (short) -1;
        }
    }

    public static long getSeconds(String time) {
        try {
            String[] arr = time.split(SOAP.DELIM);
            return (long) (((((Integer.parseInt(arr[0]) * 60) * 60) * 1000) + ((Integer.parseInt(arr[1]) * 60) * 1000)) + (Integer.parseInt(arr[2]) * 1000));
        } catch (Exception e) {
            return 0;
        }
    }
}
