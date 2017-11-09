package com.gala.video.lib.share.ifimpl.openplay.service.tools;

import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.ifimpl.openplay.service.LocalUserTags;
import com.gala.video.lib.share.ifimpl.openplay.service.OpenApiManager;
import com.qiyi.tv.client.data.UserTags;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class OpenApiCrypt {
    private static final String DESC_KEY = "vt_yiqi";
    private static final String TAG = "OpenApiCrypt";
    private static DesUtils sDesUtils;

    private static synchronized DesUtils getDesUtils() {
        DesUtils desUtils;
        synchronized (OpenApiCrypt.class) {
            if (sDesUtils == null) {
                try {
                    sDesUtils = new DesUtils(DESC_KEY);
                } catch (Exception e) {
                    LogUtils.w(TAG, "getDesUtils()", e);
                }
            }
            desUtils = sDesUtils;
        }
        return desUtils;
    }

    public static String decrypt(String str) {
        String decoded = str;
        if (!(!OpenApiManager.instance().needEncrypt() || getDesUtils() == null || str == null)) {
            try {
                decoded = getDesUtils().decrypt(str);
            } catch (Exception e) {
                LogUtils.w(TAG, "decrypt()", e);
            }
        }
        return decoded;
    }

    public static String encrypt(String str) {
        String encoded = str;
        if (!(!OpenApiManager.instance().needEncrypt() || getDesUtils() == null || str == null)) {
            try {
                encoded = getDesUtils().encrypt(str);
            } catch (Exception e) {
                LogUtils.w(TAG, "encrypt()", e);
            }
        }
        return encoded;
    }

    public static void decrpyt(UserTags target, UserTags source) {
        if (source != null && target != null) {
            Iterator it = new HashSet(source.keySet()).iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object obj = source.get(key);
                boolean needDecrypt = OpenApiManager.instance().needEncrypt();
                if (LocalUserTags.EXTRA_RESOURCE_480_270.equals(key) || LocalUserTags.EXTRA_RESOURCE_495_495.equals(key) || LocalUserTags.EXTRA_RESOURCE_570_570.equals(key) || LocalUserTags.EXTRA_RESOURCE_950_470.equals(key) || LocalUserTags.EXTRA_MEDIA_PIC_URL.equals(key)) {
                    needDecrypt = false;
                }
                if (obj instanceof ArrayList) {
                    ArrayList<String> encodedValues = new ArrayList((ArrayList) obj);
                    ArrayList<String> decodedValues = new ArrayList(encodedValues.size());
                    Iterator it2 = encodedValues.iterator();
                    while (it2.hasNext()) {
                        String encoded = (String) it2.next();
                        if (needDecrypt) {
                            decodedValues.add(decrypt(encoded));
                        } else {
                            decodedValues.add(encoded);
                        }
                    }
                    target.putStringArrayList(key, decodedValues);
                } else if (needDecrypt) {
                    target.putString(key, decrypt((String) obj));
                } else {
                    target.putString(key, (String) obj);
                }
            }
        }
    }

    public static void encrypt(UserTags target, UserTags source) {
        if (source != null && target != null) {
            Iterator it = new HashSet(source.keySet()).iterator();
            while (it.hasNext()) {
                String key = (String) it.next();
                Object obj = source.get(key);
                boolean needEncrypt = OpenApiManager.instance().needEncrypt();
                if (LocalUserTags.EXTRA_RESOURCE_480_270.equals(key) || LocalUserTags.EXTRA_RESOURCE_495_495.equals(key) || LocalUserTags.EXTRA_RESOURCE_570_570.equals(key) || LocalUserTags.EXTRA_RESOURCE_950_470.equals(key) || LocalUserTags.EXTRA_MEDIA_PIC_URL.equals(key)) {
                    needEncrypt = false;
                }
                if (obj instanceof ArrayList) {
                    ArrayList<String> decodedValues = new ArrayList((ArrayList) obj);
                    ArrayList<String> encodedValues = new ArrayList(decodedValues.size());
                    Iterator it2 = decodedValues.iterator();
                    while (it2.hasNext()) {
                        String decoded = (String) it2.next();
                        if (needEncrypt) {
                            encodedValues.add(encrypt(decoded));
                        } else {
                            encodedValues.add(decoded);
                        }
                    }
                    target.putStringArrayList(key, encodedValues);
                } else if (needEncrypt) {
                    target.putString(key, encrypt((String) obj));
                } else {
                    target.putString(key, (String) obj);
                }
            }
        }
    }
}
