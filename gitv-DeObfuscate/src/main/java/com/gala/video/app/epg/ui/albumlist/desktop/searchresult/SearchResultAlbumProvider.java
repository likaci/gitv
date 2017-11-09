package com.gala.video.app.epg.ui.albumlist.desktop.searchresult;

import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import com.gala.video.lib.framework.core.utils.DeviceUtils;
import com.gala.video.lib.share.utils.ResourceUtil;

public class SearchResultAlbumProvider {
    private static boolean DEBUG_LOG = true;
    private static final int SHOWTIMES = 4;
    private static final String TAG = "SearchResultAlbumProvider";
    private static final long mTimeOfmonth = 2592000;
    private static volatile SearchResultAlbumProvider sINSTANCE = null;
    private SearchResultDBHelper mAlbumDBHelper = new SearchResultDBHelper(ResourceUtil.getContext());
    private Handler mHandler;

    private void clearLessTimeById(java.lang.String r9, java.lang.String r10) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r8 = this;
        r0 = 0;
        r1 = 0;
        r5 = r8.mAlbumDBHelper;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r1 = r5.getWritableDatabase();	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r1.beginTransaction();	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r4 = "search_albumid = ? and time < ?";	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r5 = 2;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r3 = new java.lang.String[r5];	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r5 = 0;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r3[r5] = r9;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r5 = 1;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r6 = java.lang.String.valueOf(r10);	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r3[r5] = r6;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r5 = "search_album";	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r0 = r1.delete(r5, r4, r3);	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r1.setTransactionSuccessful();	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r5 = DEBUG_LOG;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        if (r5 == 0) goto L_0x0043;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
    L_0x0029:
        r5 = "SearchResultAlbumProvider";	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r6 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r6.<init>();	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r7 = "clearTimeById count:";	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r6 = r6.append(r7);	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r6 = r6.append(r0);	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        r6 = r6.toString();	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        android.util.Log.d(r5, r6);	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
    L_0x0043:
        if (r1 == 0) goto L_0x0048;
    L_0x0045:
        r1.endTransaction();
    L_0x0048:
        return;
    L_0x0049:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ Exception -> 0x0049, all -> 0x0053 }
        if (r1 == 0) goto L_0x0048;
    L_0x004f:
        r1.endTransaction();
        goto L_0x0048;
    L_0x0053:
        r5 = move-exception;
        if (r1 == 0) goto L_0x0059;
    L_0x0056:
        r1.endTransaction();
    L_0x0059:
        throw r5;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.epg.ui.albumlist.desktop.searchresult.SearchResultAlbumProvider.clearLessTimeById(java.lang.String, java.lang.String):void");
    }

    private void insert(int r7, long r8) {
        /* JADX: method processing error */
/*
Error: java.util.NoSuchElementException
	at java.util.HashMap$HashIterator.nextNode(HashMap.java:1431)
	at java.util.HashMap$KeyIterator.next(HashMap.java:1453)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.applyRemove(BlockFinallyExtract.java:535)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.extractFinally(BlockFinallyExtract.java:175)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.processExceptionHandler(BlockFinallyExtract.java:79)
	at jadx.core.dex.visitors.blocksmaker.BlockFinallyExtract.visit(BlockFinallyExtract.java:51)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r6 = this;
        r1 = 0;
        r3 = r6.mAlbumDBHelper;	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r1 = r3.getWritableDatabase();	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r1.beginTransaction();	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r0 = new android.content.ContentValues;	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r0.<init>();	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r3 = "search_albumid";	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r4 = java.lang.String.valueOf(r7);	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r0.put(r3, r4);	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r3 = "time";	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r4 = java.lang.Long.valueOf(r8);	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r0.put(r3, r4);	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r3 = "search_album";	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r4 = 0;	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r1.insertOrThrow(r3, r4, r0);	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        r1.setTransactionSuccessful();	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        if (r1 == 0) goto L_0x0032;
    L_0x002f:
        r1.endTransaction();
    L_0x0032:
        return;
    L_0x0033:
        r2 = move-exception;
        r2.printStackTrace();	 Catch:{ Exception -> 0x0033, all -> 0x003d }
        if (r1 == 0) goto L_0x0032;
    L_0x0039:
        r1.endTransaction();
        goto L_0x0032;
    L_0x003d:
        r3 = move-exception;
        if (r1 == 0) goto L_0x0043;
    L_0x0040:
        r1.endTransaction();
    L_0x0043:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.epg.ui.albumlist.desktop.searchresult.SearchResultAlbumProvider.insert(int, long):void");
    }

    public SearchResultAlbumProvider() {
        HandlerThread thread = new HandlerThread("desktop_post");
        thread.start();
        this.mHandler = new Handler(thread.getLooper());
    }

    public static SearchResultAlbumProvider getInstance() {
        if (sINSTANCE == null) {
            synchronized (SearchResultAlbumProvider.class) {
                if (sINSTANCE == null) {
                    sINSTANCE = new SearchResultAlbumProvider();
                }
            }
        }
        return sINSTANCE;
    }

    public static synchronized void reset() {
        synchronized (SearchResultAlbumProvider.class) {
            if (sINSTANCE != null) {
                sINSTANCE.mHandler.getLooper().quit();
                sINSTANCE.mAlbumDBHelper.close();
                sINSTANCE = null;
            }
        }
    }

    public void insert(final int id) {
        this.mHandler.post(new Runnable() {
            public void run() {
                SearchResultAlbumProvider.this.insert(id, SearchResultAlbumProvider.currentTimeSeconds());
            }
        });
    }

    public int getCountIn30DaysById(int id) {
        int count = 0;
        long time30 = currentTimeSeconds() - mTimeOfmonth;
        Cursor cursor = null;
        try {
            cursor = this.mAlbumDBHelper.getReadableDatabase().query(SearchResultDBHelper.TABLE_NAME, null, "search_albumid= ? and time between ? and ?", new String[]{String.valueOf(id), String.valueOf(time30), String.valueOf(currentTimeSeconds())}, null, null, null);
            if (cursor != null) {
                count = cursor.getCount();
            }
            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
        }
        Log.d(TAG, "getCountIn30DaysById count:" + count);
        return count;
    }

    public void clearIn30DaysById(int id) {
        long time = currentTimeSeconds() - mTimeOfmonth;
        Log.d(TAG, "time = " + time);
        clearLessTimeById(String.valueOf(id), String.valueOf(time));
    }

    private static long currentTimeSeconds() {
        return DeviceUtils.getServerTimeMillis() / 1000;
    }

    public boolean isShowDialog(int albumId) {
        clearIn30DaysById(albumId);
        if (getCountIn30DaysById(albumId) >= 4) {
            return true;
        }
        return false;
    }
}
