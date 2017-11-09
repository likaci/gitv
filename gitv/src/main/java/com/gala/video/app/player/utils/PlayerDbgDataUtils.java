package com.gala.video.app.player.utils;

import java.util.ArrayList;
import java.util.List;

public class PlayerDbgDataUtils {
    private PlayerDbgDataUtils() {
    }

    public static List<String> createDefinitionList() {
        List<String> defList = new ArrayList();
        for (int i = 4; i < 20; i++) {
            defList.add(i + "k");
        }
        return defList;
    }

    public static List<String> createStorageList() {
        List<String> storageList = new ArrayList();
        for (int i = 1; i < 20; i++) {
            storageList.add("设备" + i);
        }
        return storageList;
    }
}
