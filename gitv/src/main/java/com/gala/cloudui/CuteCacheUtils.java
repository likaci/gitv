package com.gala.cloudui;

import com.gala.cloudui.block.Cute;
import com.gala.cloudui.block.CuteBg;
import com.gala.cloudui.block.CuteImage;
import com.gala.cloudui.block.CuteText;
import com.gala.cloudui.cache.CuteBgCache;
import com.gala.cloudui.cache.CuteImageCache;
import com.gala.cloudui.cache.CuteTextCache;
import com.gala.cloudui.constants.CuteConstants;
import com.gala.cloudui.utils.CloudUtilsGala;

public class CuteCacheUtils {
    public static void recycleCute(Cute[] mViewArray) {
        int arraySize = CloudUtilsGala.getArraySize(mViewArray);
        for (int i = 0; i < arraySize; i++) {
            Cute cute = mViewArray[i];
            if (cute instanceof CuteImage) {
                CuteImageCache.newInstance().push((CuteImage) cute);
            } else if (cute instanceof CuteText) {
                CuteTextCache.newInstance().push((CuteText) cute);
            } else if (cute instanceof CuteBg) {
                CuteBgCache.newInstance().push((CuteBg) cute);
            }
        }
    }

    public static Cute[] getCutes(CloudViewGala cloudView, Cute[] cutes) {
        Cute[] cuteArr = null;
        recycleCute(cloudView.a());
        if (!CloudUtilsGala.isArrayEmpty(cutes)) {
            int length = cutes.length;
            int i = 0;
            while (i < length) {
                Cute[] cuteArr2;
                Cute pop;
                if (cuteArr == null) {
                    cuteArr2 = new Cute[length];
                } else {
                    cuteArr2 = cuteArr;
                }
                Cute cute = cutes[i];
                String type = cute.getType();
                if (CuteConstants.TYPE_IMG.equals(type)) {
                    pop = CuteImageCache.newInstance().pop(cute);
                } else if (CuteConstants.TYPE_TXT.equals(type)) {
                    pop = CuteTextCache.newInstance().pop(cute);
                } else if ("bg".equals(type)) {
                    pop = CuteBgCache.newInstance().pop(cute);
                    cloudView.a((CuteBg) pop);
                } else {
                    i++;
                    cuteArr = cuteArr2;
                }
                pop.setCloudView(cloudView);
                cuteArr2[i] = pop;
                i++;
                cuteArr = cuteArr2;
            }
        }
        return cuteArr;
    }

    public static void clearOldCutesData(Cute[] oldCutes, Cute[] newCutes, CloudViewGala cloudView) {
        if (!CloudUtilsGala.isArrayEmpty(oldCutes)) {
            int length = oldCutes.length;
            for (int i = 0; i < length; i++) {
                Cute cute = oldCutes[i];
                cute.suck(newCutes[i]);
                cute.setCloudView(cloudView);
            }
        }
    }
}
