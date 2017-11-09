package com.gala.video.app.epg.utils;

import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION;
import com.gala.video.lib.framework.core.utils.LogUtils;
import dalvik.system.DexClassLoader;
import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class HookAppLoad {
    protected static final int M = 23;
    private static final String TAG = "HookAppLoad";
    private static String sPackageName;

    private static class V14_22 {
        private static Class sDexElementClass;
        private static Constructor sDexElementConstructor;
        protected static Field sDexPathList_nativeLibraryDirectories_field;
        protected static Field sPathListField;

        private V14_22() {
        }

        public static void initField(ClassLoader classLoader) {
            if (sPathListField == null) {
                sPathListField = HookAppLoad.getDeclaredField(DexClassLoader.class.getSuperclass(), "pathList");
            }
            if (sPathListField != null) {
                Object pathList = HookAppLoad.getValue(sPathListField, classLoader);
                if (sDexPathList_nativeLibraryDirectories_field == null) {
                    sDexPathList_nativeLibraryDirectories_field = HookAppLoad.getDeclaredField(pathList.getClass(), "nativeLibraryDirectories");
                }
            }
        }

        public static void expandNativeLibraryDirectories(ClassLoader classLoader, List<File> libPaths) {
            if (sPathListField != null) {
                Object pathList = HookAppLoad.getValue(sPathListField, classLoader);
                if (pathList != null) {
                    if (sDexPathList_nativeLibraryDirectories_field == null) {
                        sDexPathList_nativeLibraryDirectories_field = HookAppLoad.getDeclaredField(pathList.getClass(), "nativeLibraryDirectories");
                        if (sDexPathList_nativeLibraryDirectories_field == null) {
                            return;
                        }
                    }
                    try {
                        HookAppLoad.expandArray(pathList, sDexPathList_nativeLibraryDirectories_field, libPaths.toArray(), true);
                    } catch (Exception e) {
                        LogUtils.e(HookAppLoad.TAG, "V14_22 expand native library directories exception =", e);
                    }
                }
            }
        }

        protected static Object makeNativeLibraryElement(File pkg) throws Exception {
            if (sDexElementClass == null) {
                sDexElementClass = Class.forName("dalvik.system.DexPathList$Element");
            }
            if (sDexElementConstructor == null) {
                sDexElementConstructor = sDexElementClass.getConstructors()[0];
            }
            return sDexElementConstructor.newInstance(new Object[]{pkg, Boolean.valueOf(true), null, null});
        }

        public static boolean isExistInNativeLibrary(ClassLoader classLoader) {
            boolean exist;
            Object pathList = HookAppLoad.getValue(sPathListField, classLoader);
            Object[] nativeLib = null;
            if (pathList != null) {
                try {
                    if (sDexPathList_nativeLibraryDirectories_field != null) {
                        nativeLib = (Object[]) sDexPathList_nativeLibraryDirectories_field.get(pathList);
                    }
                } catch (Exception e) {
                    LogUtils.i(HookAppLoad.TAG, "isExistInNativeLibrary", e);
                    return true;
                }
            }
            if (nativeLib == null || nativeLib.length <= 0) {
                exist = false;
            } else {
                boolean tmp = false;
                for (Object path : nativeLib) {
                    String existPath = path != null ? path.toString() : "";
                    LogUtils.i(HookAppLoad.TAG, "apkNativePath=" + existPath);
                    if (existPath.contains(HookAppLoad.sPackageName)) {
                        tmp = true;
                        break;
                    }
                }
                exist = tmp;
            }
            return exist;
        }
    }

    private static final class V23_ extends V14_22 {
        private static Field sDexPathList_nativeLibraryPathElements_field;

        private V23_() {
            super();
        }

        public static void expandNativeLibraryDirectories(ClassLoader classLoader, List<File> libPaths) {
            if (sPathListField != null) {
                Object pathList = HookAppLoad.getValue(sPathListField, classLoader);
                if (pathList != null) {
                    if (sDexPathList_nativeLibraryDirectories_field == null) {
                        sDexPathList_nativeLibraryDirectories_field = HookAppLoad.getDeclaredField(pathList.getClass(), "nativeLibraryDirectories");
                        if (sDexPathList_nativeLibraryDirectories_field == null) {
                            return;
                        }
                    }
                    try {
                        List<File> paths = (List) HookAppLoad.getValue(sDexPathList_nativeLibraryDirectories_field, pathList);
                        if (paths != null) {
                            paths.addAll(libPaths);
                            if (sDexPathList_nativeLibraryPathElements_field == null) {
                                sDexPathList_nativeLibraryPathElements_field = HookAppLoad.getDeclaredField(pathList.getClass(), "nativeLibraryPathElements");
                            }
                            if (sDexPathList_nativeLibraryPathElements_field != null) {
                                int N = libPaths.size();
                                Object[] elements = new Object[N];
                                for (int i = 0; i < N; i++) {
                                    elements[i] = V14_22.makeNativeLibraryElement((File) libPaths.get(i));
                                }
                                HookAppLoad.expandArray(pathList, sDexPathList_nativeLibraryPathElements_field, elements, true);
                            }
                        }
                    } catch (Exception e) {
                        LogUtils.e(HookAppLoad.TAG, "V23_ expand native library directories exception =", e);
                    }
                }
            }
        }
    }

    public static void expandNativeLibrary(ClassLoader classLoader, ApplicationInfo ai) {
        sPackageName = ai.packageName;
        String appLib = ai.nativeLibraryDir;
        int sdkVersion = VERSION.SDK_INT;
        File nativeLib = new File(appLib);
        try {
            if (nativeLib.exists()) {
                V14_22.initField(classLoader);
                if (!V14_22.isExistInNativeLibrary(classLoader)) {
                    List<File> files = new ArrayList();
                    files.add(nativeLib);
                    if (sdkVersion < 23) {
                        V14_22.expandNativeLibraryDirectories(classLoader, files);
                    } else {
                        V23_.expandNativeLibraryDirectories(classLoader, files);
                    }
                }
            }
        } catch (Exception e) {
            LogUtils.i(TAG, "expandNativeLibrary", e);
        }
    }

    private static Field getDeclaredField(Class cls, String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            LogUtils.e(TAG, "no such field exception = ", e);
            return null;
        }
    }

    private static void expandArray(Object target, Field arrField, Object[] extraElements, boolean push) throws IllegalAccessException {
        Object[] original = (Object[]) arrField.get(target);
        Object[] combined = (Object[]) Array.newInstance(original.getClass().getComponentType(), original.length + extraElements.length);
        if (push) {
            System.arraycopy(extraElements, 0, combined, 0, extraElements.length);
            System.arraycopy(original, 0, combined, extraElements.length, original.length);
        } else {
            System.arraycopy(original, 0, combined, 0, original.length);
            System.arraycopy(extraElements, 0, combined, original.length, extraElements.length);
        }
        arrField.set(target, combined);
    }

    private static <T> T getValue(Field field, Object target) {
        T t = null;
        if (field != null) {
            try {
                t = field.get(target);
            } catch (IllegalAccessException e) {
                LogUtils.e(TAG, "get value exception = ", e);
            }
        }
        return t;
    }
}
