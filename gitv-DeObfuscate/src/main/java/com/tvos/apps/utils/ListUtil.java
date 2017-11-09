package com.tvos.apps.utils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListUtil {
    public static <T> List<T> updateList(List<T> infoListFromDB, List<T> infoList) {
        if (infoListFromDB == null) {
            return infoList;
        }
        List<T> tmpInfoList = new ArrayList();
        for (int i = 0; i < infoList.size(); i++) {
            T info = infoList.get(i);
            if (infoListFromDB.contains(info)) {
                tmpInfoList.add(infoListFromDB.get(infoListFromDB.indexOf(info)));
            } else {
                tmpInfoList.add(info);
            }
        }
        return tmpInfoList;
    }

    public static <T> boolean updateList(List<T> infoListFromDB, List<T> infoListFromServer, List<T> tempList) {
        if (tempList == null) {
            tempList = new ArrayList();
        }
        if (infoListFromServer == null || infoListFromServer.isEmpty()) {
            tempList.addAll(infoListFromDB);
            return false;
        } else if (infoListFromDB == null || infoListFromDB.isEmpty()) {
            tempList.addAll(infoListFromServer);
            return true;
        } else {
            boolean shouldUpdate = false;
            for (int i = 0; i < infoListFromServer.size(); i++) {
                T info = infoListFromServer.get(i);
                if (infoListFromDB.contains(info)) {
                    int index = infoListFromDB.indexOf(info);
                    T infoInDB = infoListFromDB.get(index);
                    if (index != i) {
                        shouldUpdate = true;
                    }
                    tempList.add(infoInDB);
                } else {
                    tempList.add(info);
                    shouldUpdate = true;
                }
            }
            if (infoListFromDB.size() != infoListFromServer.size()) {
                return true;
            }
            return shouldUpdate;
        }
    }

    public static <T> List<T> cloneList(List<T> originList) {
        if (originList == null || originList.isEmpty()) {
            return null;
        }
        String cloneMethodName = "clone";
        List<T> cloneList = new ArrayList();
        Method cloneMethod = null;
        try {
            cloneMethod = originList.get(0).getClass().getMethod(cloneMethodName, new Class[0]);
        } catch (NoSuchMethodException e1) {
            e1.printStackTrace();
        }
        if (cloneMethod == null) {
            return null;
        }
        Iterator it = originList.iterator();
        while (it.hasNext()) {
            Object item = it.next();
            try {
                if (cloneMethod.invoke(item, new Object[0]) != null) {
                    cloneList.add(item);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cloneList;
    }
}
