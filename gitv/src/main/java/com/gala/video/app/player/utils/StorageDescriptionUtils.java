package com.gala.video.app.player.utils;

import android.content.Context;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;

public class StorageDescriptionUtils {
    private static final String TAG = "Player/Ui/StorageDescriptionUtils";

    public static ArrayList<String> getDefaultDescription(Context context, ArrayList<String> descriptionList, ArrayList<Boolean> removableList) {
        int i;
        ArrayList<String> descriptions = new ArrayList();
        for (i = 0; i < descriptionList.size(); i++) {
            String description = (String) descriptionList.get(i);
            boolean isRemovable = ((Boolean) removableList.get(i)).booleanValue();
            if (description == null) {
                if (isRemovable) {
                    description = context.getResources().getString(R.string.external_storage);
                } else {
                    description = context.getResources().getString(R.string.internal_storage);
                }
            }
            descriptions.add(description);
        }
        HashMap<String, Integer> nameCounts = new HashMap();
        HashMap<String, Integer> firstNameIndex = new HashMap();
        int volumeCount = descriptions.size();
        for (i = 0; i < volumeCount; i++) {
            String name = (String) descriptions.get(i);
            Integer count = (Integer) nameCounts.get(name);
            if (count != null) {
                nameCounts.put(name, Integer.valueOf(count.intValue() + 1));
                descriptions.set(i, name + "(" + (count.intValue() + 1) + ")");
            } else {
                nameCounts.put(name, Integer.valueOf(1));
                firstNameIndex.put(name, Integer.valueOf(i));
            }
        }
        for (String name2 : firstNameIndex.keySet()) {
            if (((Integer) nameCounts.get(name2)).intValue() > 1) {
                descriptions.set(((Integer) firstNameIndex.get(name2)).intValue(), name2 + "(1)");
            }
        }
        LogUtils.e(TAG, "getDefaultDescription: " + descriptions);
        return descriptions;
    }
}
