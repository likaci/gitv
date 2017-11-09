package com.gala.video.app.epg.home.data.tool;

import android.util.Log;
import com.gala.video.app.epg.HomeDebug;
import com.gala.video.app.epg.home.data.provider.TabProvider;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.stub.Thread8K;
import com.gala.video.lib.framework.core.env.AppRuntimeEnv;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel;
import com.gala.video.lib.share.utils.MemoryLevelInfo;
import com.gala.video.webview.utils.WebSDKConstants;
import com.mcto.ads.internal.net.PingbackConstants;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

public class TabModelManager {
    private static final String CACHE_FILE = (AppRuntimeEnv.get().getApplicationContext().getFilesDir().getPath() + "/home/tabmodel.json");
    private static final boolean DEBUG = HomeDebug.DEBUG_LOG;
    public static final int ERROR_DUPLICATE = 2;
    public static final int ERROR_OVERFLOW = 1;
    public static final int ERROR_UNKNOWN = 3;
    public static final int SUCCESS = 0;
    private static final int TAB_MAX_VAR = (MemoryLevelInfo.isLowMemoryDevice() ? 7 : 8);
    private static final String TAG = "TabModelManager";
    private static final int VERSION = 2;
    private static int editFlag = 0;
    private static List<Item> mAlternativePool;
    private static List<Item> mCurrentTabList;
    private static final Object mLock = new Object();
    private static List<Item> mUserOrder;
    private static int mVarTabSize;

    public interface Editor {
        void commit();

        boolean hide(TabModel tabModel);

        boolean isFull();

        boolean moveBackward(int i);

        boolean moveBackward(TabModel tabModel);

        boolean moveForward(int i);

        boolean moveForward(TabModel tabModel);

        boolean show(TabModel tabModel);
    }

    private static class InnerEditor implements Editor {
        private LinkedList<TabModel> alternativePool;
        private LinkedList<TabModel> data;
        private int editFlag;
        private int indexOffset;

        private InnerEditor(List<TabModel> dataList, List<TabModel> alterPool) {
            this.editFlag = TabModelManager.editFlag;
            if (dataList != null) {
                int i = dataList.size() - 1;
                while (i >= 0 && ((TabModel) dataList.get(i)).isSupportSort()) {
                    i--;
                }
                if (i >= 0) {
                    this.indexOffset = i + 1;
                    dataList = dataList.subList(i + 1, dataList.size());
                    Log.d(TabModelManager.TAG, this.indexOffset + " items not support sort");
                }
                this.data = new LinkedList();
                this.data.addAll(dataList);
            }
            if (alterPool != null) {
                this.alternativePool = new LinkedList();
                this.alternativePool.addAll(alterPool);
            }
        }

        public boolean moveForward(TabModel tab) {
            Log.d(TabModelManager.TAG, "tab move forward: " + tab.getTitle());
            if (tab.isSupportSort()) {
                ListIterator<TabModel> iterator = this.data.listIterator();
                while (iterator.hasNext()) {
                    TabModel t1 = (TabModel) iterator.next();
                    if ((t1 == tab || tab.getId() == t1.getId()) && iterator.hasNext()) {
                        iterator.remove();
                        iterator.next();
                        iterator.add(t1);
                        Log.d(TabModelManager.TAG, "success " + tab.getTitle());
                        this.editFlag = 1;
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean moveForward(int index) {
            Log.d(TabModelManager.TAG, "tab index move forward: " + index);
            index -= this.indexOffset;
            if (index < 0 || index >= this.data.size() - 1) {
                return false;
            }
            ListIterator<TabModel> iterator = this.data.listIterator(index);
            TabModel t1 = (TabModel) iterator.next();
            iterator.remove();
            iterator.next();
            iterator.add(t1);
            Log.d(TabModelManager.TAG, "success " + t1.getTitle());
            this.editFlag = 1;
            return true;
        }

        public boolean moveBackward(TabModel tab) {
            Log.d(TabModelManager.TAG, "tab move backward: " + tab.getTitle());
            if (tab.isSupportSort()) {
                ListIterator<TabModel> iterator = this.data.listIterator(1);
                while (iterator.hasNext()) {
                    TabModel t1 = (TabModel) iterator.next();
                    if (t1 != tab) {
                        if (tab.getId() == t1.getId()) {
                        }
                    }
                    iterator.remove();
                    iterator.previous();
                    iterator.add(t1);
                    Log.d(TabModelManager.TAG, "success " + tab.getTitle());
                    this.editFlag = 1;
                    return true;
                }
            }
            return false;
        }

        public boolean moveBackward(int index) {
            Log.d(TabModelManager.TAG, "tab index move forward: " + index);
            index -= this.indexOffset;
            if (index <= 0 || index >= this.data.size()) {
                return false;
            }
            ListIterator<TabModel> iterator = this.data.listIterator(index);
            TabModel t1 = (TabModel) iterator.next();
            iterator.remove();
            iterator.previous();
            iterator.add(t1);
            Log.d(TabModelManager.TAG, "success " + t1.getTitle());
            this.editFlag = 1;
            return true;
        }

        public boolean hide(TabModel tab) {
            Log.d(TabModelManager.TAG, "tab hide: " + tab.getTitle());
            Iterator<TabModel> iterator = this.data.listIterator();
            while (iterator.hasNext()) {
                if (((TabModel) iterator.next()).getId() == tab.getId()) {
                    tab.setShown(false);
                    iterator.remove();
                    this.editFlag = 1;
                    Log.d(TabModelManager.TAG, "success: " + tab.getTitle());
                    return true;
                }
            }
            return false;
        }

        public boolean show(TabModel tab) {
            Log.d(TabModelManager.TAG, "tab show: " + tab.getTitle());
            if (!(isFull() || this.alternativePool == null)) {
                Iterator it = this.alternativePool.iterator();
                while (it.hasNext()) {
                    TabModel t = (TabModel) it.next();
                    if (t.getId() == tab.getId()) {
                        tab.setShown(true);
                        this.data.add(t);
                        this.editFlag = 1;
                        Log.d(TabModelManager.TAG, "success " + tab.getTitle());
                        return true;
                    }
                }
            }
            return false;
        }

        public boolean isFull() {
            return this.data.size() >= TabModelManager.TAB_MAX_VAR;
        }

        public void commit() {
            Log.d(TabModelManager.TAG, "commit");
            synchronized (TabModelManager.mLock) {
                if (TabModelManager.mUserOrder != null) {
                    TabModelManager.mUserOrder.clear();
                }
                TabModelManager.mUserOrder = new ArrayList(this.data.size());
                Iterator it = this.data.iterator();
                while (it.hasNext()) {
                    TabModelManager.mUserOrder.add(new Item((TabModel) it.next()));
                }
                if (!(this.alternativePool == null || this.editFlag == 0)) {
                    TabModelManager.mAlternativePool = new ArrayList(this.alternativePool.size());
                    it = this.alternativePool.iterator();
                    while (it.hasNext()) {
                        TabModelManager.mAlternativePool.add(new Item((TabModel) it.next()));
                    }
                    TabModelManager.editFlag = this.editFlag;
                }
            }
            Log.d(TabModelManager.TAG, "save to file result: " + TabModelManager.saveToFile(TabModelManager.mUserOrder, this.editFlag, TabModelManager.mAlternativePool));
        }
    }

    private static class Item {
        int channelId;
        int id;
        String name;
        boolean show = true;

        public Item(TabModel tab) {
            this.id = tab.getId();
            this.name = tab.getTitle();
            this.channelId = tab.getChannelId();
            this.show = tab.isShown();
        }

        private Item() {
        }

        public String toString() {
            return "TabItem[id: " + this.id + ";title: " + this.name + ";chId: " + this.channelId + "; isShow: " + this.show + AlbumEnterFactory.SIGN_STR;
        }

        static void putJson(JSONStringer stringer, List<Item> list) throws JSONException {
            stringer.array();
            for (Item item : list) {
                stringer.object();
                stringer.key("id");
                stringer.value((long) item.id);
                stringer.key(WebSDKConstants.PARAM_KEY_PL_NAME);
                stringer.value(item.name);
                stringer.key(DBColumns.IS_NEED_SHOW);
                stringer.value(item.show);
                stringer.key(PingbackConstants.CHANNEL_ID);
                stringer.value((long) item.channelId);
                stringer.endObject();
            }
            stringer.endArray();
        }

        static List<Item> getJson(JSONArray array) {
            int size = array.length();
            List<Item> result = new ArrayList(size);
            int i = 0;
            while (i < size) {
                try {
                    JSONObject object = array.getJSONObject(i);
                    Item item = new Item();
                    item.id = object.getInt("id");
                    item.name = object.getString(WebSDKConstants.PARAM_KEY_PL_NAME);
                    item.show = object.getBoolean(DBColumns.IS_NEED_SHOW);
                    item.channelId = object.optInt(PingbackConstants.CHANNEL_ID, -1);
                    result.add(item);
                    i++;
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return result;
        }
    }

    public static void loadCache() {
        Exception e;
        Throwable th;
        File f = new File(CACHE_FILE);
        if (f.exists()) {
            InputStream is = null;
            try {
                InputStream is2 = new BufferedInputStream(new FileInputStream(f));
                try {
                    int size = (int) f.length();
                    byte[] buffer = new byte[size];
                    if (is2.read(buffer) != size) {
                        Log.e(TAG, "cache file length " + f.length());
                    }
                    String json = new String(buffer);
                    if (is2 != null) {
                        try {
                            is2.close();
                        } catch (IOException e2) {
                        }
                    }
                    try {
                        JSONObject object = new JSONObject(json);
                        Log.d(TAG, object.getInt("version") + " version vs: " + 2);
                        mUserOrder = Item.getJson(object.getJSONArray("data"));
                        editFlag = object.optInt("flag", 0);
                        Log.d(TAG, "edit flag: " + editFlag);
                        if (object.has("pool")) {
                            mAlternativePool = Item.getJson(object.getJSONArray("pool"));
                        }
                    } catch (Exception e3) {
                        e3.printStackTrace();
                    }
                    Log.d(TAG, "user tab list: " + mUserOrder);
                } catch (Exception e4) {
                    e3 = e4;
                    is = is2;
                    try {
                        e3.printStackTrace();
                        if (is != null) {
                            try {
                                is.close();
                                return;
                            } catch (IOException e5) {
                                return;
                            }
                        }
                        return;
                    } catch (Throwable th2) {
                        th = th2;
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e6) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    is = is2;
                    if (is != null) {
                        is.close();
                    }
                    throw th;
                }
            } catch (Exception e7) {
                e3 = e7;
                e3.printStackTrace();
                if (is != null) {
                    is.close();
                    return;
                }
                return;
            }
        }
        TabProvider tp = TabProvider.getInstance();
        mCurrentTabList = new ArrayList(11);
        List<TabModel> tl = tp.getTabInfo();
        if (tl != null) {
            int staticNum = 0;
            for (TabModel m : tl) {
                if (!m.isSupportSort()) {
                    staticNum++;
                }
                mCurrentTabList.add(new Item(m));
            }
            mVarTabSize = mCurrentTabList.size() - staticNum;
        }
        if (mAlternativePool == null) {
            mAlternativePool = new ArrayList(20);
            List<TabModel> al = tp.getTabHideInfo();
            if (al != null) {
                for (TabModel m2 : al) {
                    mAlternativePool.add(new Item(m2));
                }
            }
        }
    }

    public static void clearCache() {
        if (mUserOrder != null) {
            synchronized (mLock) {
                mUserOrder.clear();
                mUserOrder = null;
                if (mAlternativePool != null) {
                    mAlternativePool.clear();
                    mAlternativePool = null;
                }
            }
        }
    }

    public static int canAddChannel(int channelId) {
        Log.d(TAG, "canAddChannel: " + channelId);
        int result = 3;
        if (mCurrentTabList != null) {
            int i = 0;
            while (i < mCurrentTabList.size()) {
                if (((Item) mCurrentTabList.get(i)).channelId == channelId) {
                    result = 2;
                    break;
                }
                i++;
            }
            if (i >= mCurrentTabList.size()) {
                if (containsChannel(mUserOrder, channelId) != null) {
                    result = 2;
                } else if (containsChannel(mAlternativePool, channelId) == null) {
                    result = 3;
                } else if (mVarTabSize >= TAB_MAX_VAR) {
                    result = 1;
                } else {
                    result = 0;
                }
            }
        }
        Log.d(TAG, "result: " + result);
        return result;
    }

    public static int addChannel(int channelId) {
        Log.d(TAG, "addChannel: " + channelId);
        int result = 3;
        if (mCurrentTabList != null) {
            if (mVarTabSize >= TAB_MAX_VAR) {
                result = 1;
            } else {
                int i = 0;
                while (i < mCurrentTabList.size()) {
                    if (((Item) mCurrentTabList.get(i)).channelId == channelId) {
                        result = 2;
                        break;
                    }
                    i++;
                }
                if (i >= mCurrentTabList.size()) {
                    if (containsChannel(mUserOrder, channelId) != null) {
                        result = 2;
                    } else {
                        Item item = containsChannel(mAlternativePool, channelId);
                        if (item == null) {
                            result = 3;
                        } else {
                            item.show = true;
                            if (mUserOrder == null) {
                                mUserOrder = new ArrayList(mVarTabSize + 1);
                                int size = mCurrentTabList.size();
                                for (i = size - mVarTabSize; i < size; i++) {
                                    mUserOrder.add(mCurrentTabList.get(i));
                                }
                            }
                            mUserOrder.add(item);
                            editFlag = 1;
                            result = 0;
                            new Thread8K(TAG) {
                                public void run() {
                                    Log.v(TabModelManager.TAG, "add channel save");
                                    Log.d(TabModelManager.TAG, "add channel save result: " + TabModelManager.saveToFile(TabModelManager.mUserOrder, TabModelManager.editFlag, TabModelManager.mAlternativePool));
                                }
                            }.start();
                            TabProvider.getInstance().updateTabSetting(0);
                        }
                    }
                }
            }
        }
        Log.d(TAG, "result: " + result);
        return result;
    }

    private static Item containsChannel(Collection<Item> source, int channelTarget) {
        if (source == null) {
            return null;
        }
        for (Item item : source) {
            if (item.channelId == channelTarget) {
                return item;
            }
        }
        return null;
    }

    public static boolean processRaw(List<TabModel> dataList, List<TabModel> alterList) {
        return process(dataList, alterList, true);
    }

    public static boolean process(List<TabModel> dataList, List<TabModel> alters) {
        return process(dataList, alters, false);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean process(java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel> r23, java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel> r24, boolean r25) {
        /*
        r15 = 0;
        r20 = mLock;
        monitor-enter(r20);
        r19 = mUserOrder;	 Catch:{ all -> 0x0095 }
        if (r19 == 0) goto L_0x0043;
    L_0x0008:
        r19 = mUserOrder;	 Catch:{ all -> 0x0095 }
        r19 = r19.size();	 Catch:{ all -> 0x0095 }
        r21 = TAB_MAX_VAR;	 Catch:{ all -> 0x0095 }
        r0 = r19;
        r1 = r21;
        if (r0 <= r1) goto L_0x0028;
    L_0x0016:
        r19 = mUserOrder;	 Catch:{ all -> 0x0095 }
        r21 = 0;
        r22 = TAB_MAX_VAR;	 Catch:{ all -> 0x0095 }
        r0 = r19;
        r1 = r21;
        r2 = r22;
        r19 = r0.subList(r1, r2);	 Catch:{ all -> 0x0095 }
        mUserOrder = r19;	 Catch:{ all -> 0x0095 }
    L_0x0028:
        r19 = mUserOrder;	 Catch:{ all -> 0x0095 }
        r21 = mUserOrder;	 Catch:{ all -> 0x0095 }
        r21 = r21.size();	 Catch:{ all -> 0x0095 }
        r0 = r21;
        r0 = new com.gala.video.app.epg.home.data.tool.TabModelManager.Item[r0];	 Catch:{ all -> 0x0095 }
        r21 = r0;
        r0 = r19;
        r1 = r21;
        r19 = r0.toArray(r1);	 Catch:{ all -> 0x0095 }
        r0 = r19;
        r0 = (com.gala.video.app.epg.home.data.tool.TabModelManager.Item[]) r0;	 Catch:{ all -> 0x0095 }
        r15 = r0;
    L_0x0043:
        monitor-exit(r20);	 Catch:{ all -> 0x0095 }
        if (r24 != 0) goto L_0x004b;
    L_0x0046:
        r24 = new java.util.LinkedList;
        r24.<init>();
    L_0x004b:
        r5 = 0;
        r14 = r23.size();
        r13 = new java.util.LinkedList;
        r13.<init>();
        r3 = new java.util.LinkedList;
        r3.<init>();
        r0 = new com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel[r14];
        r19 = r0;
        r0 = r23;
        r1 = r19;
        r7 = r0.toArray(r1);
        r7 = (com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel[]) r7;
        r19 = r24.size();
        r0 = r19;
        r0 = new com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel[r0];
        r19 = r0;
        r0 = r24;
        r1 = r19;
        r4 = r0.toArray(r1);
        r4 = (com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel[]) r4;
        r0 = r4.length;
        r20 = r0;
        r19 = 0;
    L_0x0081:
        r0 = r19;
        r1 = r20;
        if (r0 >= r1) goto L_0x0098;
    L_0x0087:
        r18 = r4[r19];
        r21 = 0;
        r0 = r18;
        r1 = r21;
        r0.setShown(r1);
        r19 = r19 + 1;
        goto L_0x0081;
    L_0x0095:
        r19 = move-exception;
        monitor-exit(r20);	 Catch:{ all -> 0x0095 }
        throw r19;
    L_0x0098:
        r8 = r14 + -1;
    L_0x009a:
        if (r8 < 0) goto L_0x00a4;
    L_0x009c:
        r19 = r7[r8];
        r19 = r19.isSupportSort();
        if (r19 != 0) goto L_0x00b8;
    L_0x00a4:
        r11 = 0;
    L_0x00a5:
        if (r11 > r8) goto L_0x00bb;
    L_0x00a7:
        r19 = r7[r11];
        r20 = 1;
        r19.setShown(r20);
        r19 = r7[r11];
        r0 = r19;
        r13.add(r0);
        r11 = r11 + 1;
        goto L_0x00a5;
    L_0x00b8:
        r8 = r8 + -1;
        goto L_0x009a;
    L_0x00bb:
        r8 = r8 + 1;
        r17 = r8;
        r19 = DEBUG;
        if (r19 == 0) goto L_0x00e1;
    L_0x00c3:
        r19 = "TabModelManager";
        r20 = new java.lang.StringBuilder;
        r20.<init>();
        r21 = "process static tab num: ";
        r20 = r20.append(r21);
        r0 = r20;
        r1 = r17;
        r20 = r0.append(r1);
        r20 = r20.toString();
        android.util.Log.d(r19, r20);
    L_0x00e1:
        if (r25 == 0) goto L_0x00fc;
    L_0x00e3:
        r11 = r8;
    L_0x00e4:
        if (r11 >= r14) goto L_0x00f7;
    L_0x00e6:
        r19 = r7[r11];
        r20 = 0;
        r19.setShown(r20);
        r19 = r7[r11];
        r0 = r19;
        r3.add(r0);
        r11 = r11 + 1;
        goto L_0x00e4;
    L_0x00f7:
        r0 = r24;
        r3.addAll(r0);
    L_0x00fc:
        if (r15 != 0) goto L_0x0185;
    L_0x00fe:
        r19 = "TabModelManager";
        r20 = "no user order";
        android.util.Log.e(r19, r20);
        if (r25 == 0) goto L_0x0183;
    L_0x0109:
        r0 = r17;
        r11 = -r0;
        r13.clear();
        r19 = mCurrentTabList;
        r19.clear();
        r19 = r23.iterator();
    L_0x0118:
        r20 = r19.hasNext();
        if (r20 == 0) goto L_0x012b;
    L_0x011e:
        r18 = r19.next();
        r18 = (com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel) r18;
        r20 = TAB_MAX_VAR;
        r0 = r20;
        if (r11 < r0) goto L_0x0152;
    L_0x012a:
        r5 = 1;
    L_0x012b:
        r19 = mCurrentTabList;
        r19 = r19.size();
        r19 = r19 - r17;
        mVarTabSize = r19;
        r19 = r24.iterator();
    L_0x0139:
        r20 = r19.hasNext();
        if (r20 == 0) goto L_0x0171;
    L_0x013f:
        r12 = r19.next();
        r12 = (com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel) r12;
        r20 = mAlternativePool;
        r21 = new com.gala.video.app.epg.home.data.tool.TabModelManager$Item;
        r0 = r21;
        r0.<init>(r12);
        r20.add(r21);
        goto L_0x0139;
    L_0x0152:
        r20 = 1;
        r0 = r18;
        r1 = r20;
        r0.setShown(r1);
        r0 = r18;
        r13.add(r0);
        r20 = mCurrentTabList;
        r21 = new com.gala.video.app.epg.home.data.tool.TabModelManager$Item;
        r0 = r21;
        r1 = r18;
        r0.<init>(r1);
        r20.add(r21);
        r11 = r11 + 1;
        goto L_0x0118;
    L_0x0171:
        r24.clear();
        r0 = r24;
        r0.addAll(r3);
        if (r5 == 0) goto L_0x0183;
    L_0x017b:
        r23.clear();
        r0 = r23;
        r0.addAll(r13);
    L_0x0183:
        r6 = r5;
    L_0x0184:
        return r6;
    L_0x0185:
        r16 = 0;
        r11 = 0;
    L_0x0188:
        r0 = r15.length;
        r19 = r0;
        r0 = r19;
        if (r11 >= r0) goto L_0x0242;
    L_0x018f:
        r18 = r15[r11];
        r0 = r18;
        r9 = contains(r7, r8, r0);
        if (r9 < 0) goto L_0x01db;
    L_0x0199:
        r19 = DEBUG;
        if (r19 == 0) goto L_0x01b9;
    L_0x019d:
        r19 = "TabModelManager";
        r20 = new java.lang.StringBuilder;
        r20.<init>();
        r21 = "add tab from dataList: ";
        r20 = r20.append(r21);
        r21 = r7[r9];
        r20 = r20.append(r21);
        r20 = r20.toString();
        android.util.Log.d(r19, r20);
    L_0x01b9:
        if (r5 != 0) goto L_0x01c3;
    L_0x01bb:
        r19 = r13.size();
        r0 = r19;
        if (r9 == r0) goto L_0x01d9;
    L_0x01c3:
        r5 = 1;
    L_0x01c4:
        r19 = r7[r9];
        r0 = r19;
        r13.add(r0);
        r19 = r7[r9];
        r20 = 1;
        r19.setShown(r20);
        r19 = 0;
        r7[r9] = r19;
    L_0x01d6:
        r11 = r11 + 1;
        goto L_0x0188;
    L_0x01d9:
        r5 = 0;
        goto L_0x01c4;
    L_0x01db:
        r19 = 0;
        r0 = r19;
        r1 = r18;
        r9 = contains(r4, r0, r1);
        if (r9 < 0) goto L_0x021f;
    L_0x01e7:
        r19 = editFlag;
        if (r19 == 0) goto L_0x01d6;
    L_0x01eb:
        r19 = DEBUG;
        if (r19 == 0) goto L_0x020b;
    L_0x01ef:
        r19 = "TabModelManager";
        r20 = new java.lang.StringBuilder;
        r20.<init>();
        r21 = "add tab from alternative list: ";
        r20 = r20.append(r21);
        r21 = r4[r9];
        r20 = r20.append(r21);
        r20 = r20.toString();
        android.util.Log.d(r19, r20);
    L_0x020b:
        r5 = 1;
        r19 = r4[r9];
        r0 = r19;
        r13.add(r0);
        r19 = r4[r9];
        r20 = 1;
        r19.setShown(r20);
        r19 = 0;
        r4[r9] = r19;
        goto L_0x01d6;
    L_0x021f:
        r19 = "TabModelManager";
        r20 = new java.lang.StringBuilder;
        r20.<init>();
        r21 = "tab is deleted: ";
        r20 = r20.append(r21);
        r21 = r15[r11];
        r20 = r20.append(r21);
        r20 = r20.toString();
        android.util.Log.d(r19, r20);
        r19 = 0;
        r15[r11] = r19;
        r16 = 1;
        goto L_0x01d6;
    L_0x0242:
        if (r5 != 0) goto L_0x0251;
    L_0x0244:
        r19 = r13.size();
        r0 = r7.length;
        r20 = r0;
        r0 = r19;
        r1 = r20;
        if (r0 == r1) goto L_0x02af;
    L_0x0251:
        r5 = 1;
    L_0x0252:
        r19 = editFlag;
        if (r19 != 0) goto L_0x02b3;
    L_0x0256:
        r0 = r7.length;
        r19 = r0;
        r0 = r19;
        if (r8 >= r0) goto L_0x02b3;
    L_0x025d:
        r19 = r13.size();
        r19 = r19 - r17;
        r20 = TAB_MAX_VAR;
        r0 = r19;
        r1 = r20;
        if (r0 >= r1) goto L_0x02b3;
    L_0x026b:
        r19 = r7[r8];
        if (r19 == 0) goto L_0x02ac;
    L_0x026f:
        r19 = DEBUG;
        if (r19 == 0) goto L_0x028f;
    L_0x0273:
        r19 = "TabModelManager";
        r20 = new java.lang.StringBuilder;
        r20.<init>();
        r21 = "add tab from left dataList: ";
        r20 = r20.append(r21);
        r21 = r7[r8];
        r20 = r20.append(r21);
        r20 = r20.toString();
        android.util.Log.d(r19, r20);
    L_0x028f:
        if (r5 != 0) goto L_0x0299;
    L_0x0291:
        r19 = r13.size();
        r0 = r19;
        if (r8 == r0) goto L_0x02b1;
    L_0x0299:
        r5 = 1;
    L_0x029a:
        r19 = r7[r8];
        r0 = r19;
        r13.add(r0);
        r19 = r7[r8];
        r20 = 1;
        r19.setShown(r20);
        r19 = 0;
        r7[r8] = r19;
    L_0x02ac:
        r8 = r8 + 1;
        goto L_0x0256;
    L_0x02af:
        r5 = 0;
        goto L_0x0252;
    L_0x02b1:
        r5 = 0;
        goto L_0x029a;
    L_0x02b3:
        if (r25 == 0) goto L_0x02d9;
    L_0x02b5:
        r19 = r13.size();
        if (r19 != 0) goto L_0x02d9;
    L_0x02bb:
        r0 = r7.length;
        r19 = r0;
        r20 = 1;
        r0 = r19;
        r1 = r20;
        if (r0 <= r1) goto L_0x02d9;
    L_0x02c6:
        r5 = 1;
        r19 = 0;
        r19 = r7[r19];
        r0 = r19;
        r13.add(r0);
        r19 = 0;
        r19 = r7[r19];
        r20 = 1;
        r19.setShown(r20);
    L_0x02d9:
        r20 = mLock;
        monitor-enter(r20);
        if (r5 != 0) goto L_0x02e6;
    L_0x02de:
        r19 = mCurrentTabList;	 Catch:{ all -> 0x0314 }
        r19 = com.gala.video.lib.framework.core.utils.ListUtils.getCount(r19);	 Catch:{ all -> 0x0314 }
        if (r19 != 0) goto L_0x033b;
    L_0x02e6:
        r19 = mCurrentTabList;	 Catch:{ all -> 0x0314 }
        r19 = com.gala.video.lib.framework.core.utils.ListUtils.isEmpty(r19);	 Catch:{ all -> 0x0314 }
        if (r19 != 0) goto L_0x02f3;
    L_0x02ee:
        r19 = mCurrentTabList;	 Catch:{ all -> 0x0314 }
        r19.clear();	 Catch:{ all -> 0x0314 }
    L_0x02f3:
        r19 = r13.iterator();	 Catch:{ all -> 0x0314 }
    L_0x02f7:
        r21 = r19.hasNext();	 Catch:{ all -> 0x0314 }
        if (r21 == 0) goto L_0x0317;
    L_0x02fd:
        r12 = r19.next();	 Catch:{ all -> 0x0314 }
        r12 = (com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel) r12;	 Catch:{ all -> 0x0314 }
        r21 = mCurrentTabList;	 Catch:{ all -> 0x0314 }
        if (r21 == 0) goto L_0x02f7;
    L_0x0307:
        r21 = mCurrentTabList;	 Catch:{ all -> 0x0314 }
        r22 = new com.gala.video.app.epg.home.data.tool.TabModelManager$Item;	 Catch:{ all -> 0x0314 }
        r0 = r22;
        r0.<init>(r12);	 Catch:{ all -> 0x0314 }
        r21.add(r22);	 Catch:{ all -> 0x0314 }
        goto L_0x02f7;
    L_0x0314:
        r19 = move-exception;
        monitor-exit(r20);	 Catch:{ all -> 0x0314 }
        throw r19;
    L_0x0317:
        r19 = DEBUG;	 Catch:{ all -> 0x0314 }
        if (r19 == 0) goto L_0x033b;
    L_0x031b:
        r19 = "TabModelManager";
        r21 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0314 }
        r21.<init>();	 Catch:{ all -> 0x0314 }
        r22 = "after process, current tab: ";
        r21 = r21.append(r22);	 Catch:{ all -> 0x0314 }
        r22 = mCurrentTabList;	 Catch:{ all -> 0x0314 }
        r21 = r21.append(r22);	 Catch:{ all -> 0x0314 }
        r21 = r21.toString();	 Catch:{ all -> 0x0314 }
        r0 = r19;
        r1 = r21;
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x0314 }
    L_0x033b:
        r19 = mCurrentTabList;	 Catch:{ all -> 0x0314 }
        r19 = com.gala.video.lib.framework.core.utils.ListUtils.getCount(r19);	 Catch:{ all -> 0x0314 }
        r19 = r19 - r17;
        mVarTabSize = r19;	 Catch:{ all -> 0x0314 }
        if (r25 == 0) goto L_0x03d1;
    L_0x0347:
        r19 = mAlternativePool;	 Catch:{ all -> 0x0314 }
        r19.clear();	 Catch:{ all -> 0x0314 }
        r19 = r3.iterator();	 Catch:{ all -> 0x0314 }
    L_0x0350:
        r21 = r19.hasNext();	 Catch:{ all -> 0x0314 }
        if (r21 == 0) goto L_0x0369;
    L_0x0356:
        r12 = r19.next();	 Catch:{ all -> 0x0314 }
        r12 = (com.gala.video.lib.share.ifmanager.bussnessIF.epg.data.model.TabModel) r12;	 Catch:{ all -> 0x0314 }
        r21 = mAlternativePool;	 Catch:{ all -> 0x0314 }
        r22 = new com.gala.video.app.epg.home.data.tool.TabModelManager$Item;	 Catch:{ all -> 0x0314 }
        r0 = r22;
        r0.<init>(r12);	 Catch:{ all -> 0x0314 }
        r21.add(r22);	 Catch:{ all -> 0x0314 }
        goto L_0x0350;
    L_0x0369:
        r19 = DEBUG;	 Catch:{ all -> 0x0314 }
        if (r19 == 0) goto L_0x038d;
    L_0x036d:
        r19 = "TabModelManager";
        r21 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0314 }
        r21.<init>();	 Catch:{ all -> 0x0314 }
        r22 = "raw data, all tab is: ";
        r21 = r21.append(r22);	 Catch:{ all -> 0x0314 }
        r22 = mAlternativePool;	 Catch:{ all -> 0x0314 }
        r21 = r21.append(r22);	 Catch:{ all -> 0x0314 }
        r21 = r21.toString();	 Catch:{ all -> 0x0314 }
        r0 = r19;
        r1 = r21;
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x0314 }
    L_0x038d:
        if (r16 == 0) goto L_0x03d1;
    L_0x038f:
        r19 = mUserOrder;	 Catch:{ all -> 0x0314 }
        r19.clear();	 Catch:{ all -> 0x0314 }
        r0 = r15.length;	 Catch:{ all -> 0x0314 }
        r21 = r0;
        r19 = 0;
    L_0x0399:
        r0 = r19;
        r1 = r21;
        if (r0 >= r1) goto L_0x03ad;
    L_0x039f:
        r10 = r15[r19];	 Catch:{ all -> 0x0314 }
        if (r10 == 0) goto L_0x03aa;
    L_0x03a3:
        r22 = mUserOrder;	 Catch:{ all -> 0x0314 }
        r0 = r22;
        r0.add(r10);	 Catch:{ all -> 0x0314 }
    L_0x03aa:
        r19 = r19 + 1;
        goto L_0x0399;
    L_0x03ad:
        r19 = DEBUG;	 Catch:{ all -> 0x0314 }
        if (r19 == 0) goto L_0x03d1;
    L_0x03b1:
        r19 = "TabModelManager";
        r21 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0314 }
        r21.<init>();	 Catch:{ all -> 0x0314 }
        r22 = "after all, user tab is: ";
        r21 = r21.append(r22);	 Catch:{ all -> 0x0314 }
        r22 = mUserOrder;	 Catch:{ all -> 0x0314 }
        r21 = r21.append(r22);	 Catch:{ all -> 0x0314 }
        r21 = r21.toString();	 Catch:{ all -> 0x0314 }
        r0 = r19;
        r1 = r21;
        android.util.Log.d(r0, r1);	 Catch:{ all -> 0x0314 }
    L_0x03d1:
        monitor-exit(r20);	 Catch:{ all -> 0x0314 }
        if (r25 == 0) goto L_0x03df;
    L_0x03d4:
        r19 = new com.gala.video.app.epg.home.data.tool.TabModelManager$2;
        r20 = "TabModelManager";
        r19.<init>(r20);
        r19.start();
    L_0x03df:
        if (r5 == 0) goto L_0x03e9;
    L_0x03e1:
        r23.clear();
        r0 = r23;
        r0.addAll(r13);
    L_0x03e9:
        if (r25 == 0) goto L_0x03f3;
    L_0x03eb:
        r24.clear();
        r0 = r24;
        r0.addAll(r3);
    L_0x03f3:
        r6 = r5;
        goto L_0x0184;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.epg.home.data.tool.TabModelManager.process(java.util.List, java.util.List, boolean):boolean");
    }

    private static int contains(TabModel[] source, int srcPos, Item target) {
        for (int i = srcPos; i < source.length; i++) {
            TabModel t = source[i];
            if (t != null && target.id == t.getId()) {
                return i;
            }
        }
        return -1;
    }

    private static boolean saveToFile(List<Item> dataList, int editFlag, List<Item> option) {
        Exception e;
        Throwable th;
        JSONStringer stringer = new JSONStringer();
        try {
            stringer.object();
            stringer.key("version");
            stringer.value(2);
            stringer.key("flag");
            stringer.value((long) editFlag);
            stringer.key("data");
            Item.putJson(stringer, dataList);
            if (option != null) {
                stringer.key("pool");
                Item.putJson(stringer, option);
            }
            stringer.endObject();
            OutputStream os = null;
            try {
                File f = new File(CACHE_FILE);
                if (!f.getParentFile().exists()) {
                    f.mkdirs();
                }
                OutputStream os2 = new BufferedOutputStream(new FileOutputStream(f));
                try {
                    String s = stringer.toString();
                    os2.write(s.getBytes());
                    if (DEBUG) {
                        Log.d(TAG, "save file: " + s);
                    }
                    if (os2 != null) {
                        try {
                            os2.close();
                        } catch (IOException e2) {
                        }
                    }
                    return true;
                } catch (Exception e3) {
                    e = e3;
                    os = os2;
                    try {
                        e.printStackTrace();
                        if (os != null) {
                            return false;
                        }
                        try {
                            os.close();
                            return false;
                        } catch (IOException e4) {
                            return false;
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (os != null) {
                            try {
                                os.close();
                            } catch (IOException e5) {
                            }
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    os = os2;
                    if (os != null) {
                        os.close();
                    }
                    throw th;
                }
            } catch (Exception e6) {
                e = e6;
                e.printStackTrace();
                if (os != null) {
                    return false;
                }
                os.close();
                return false;
            }
        } catch (JSONException e7) {
            e7.printStackTrace();
            return false;
        }
    }

    public static List<TabModel> cloneTabModel(List<TabModel> src) {
        if (src == null) {
            return null;
        }
        List<TabModel> arrayList = new ArrayList(src.size());
        for (TabModel o : src) {
            TabModel t = new TabModel();
            arrayList.add(t);
            t.setShown(o.isShown());
            t.setIsSupportSort(o.isSupportSort());
            t.setId(o.getId());
            t.setChannelId(o.getChannelId());
            t.setTitle(o.getTitle());
        }
        return arrayList;
    }

    public static Editor edit(List<TabModel> dataList, List<TabModel> alterPool) {
        return new InnerEditor(dataList, alterPool);
    }
}
