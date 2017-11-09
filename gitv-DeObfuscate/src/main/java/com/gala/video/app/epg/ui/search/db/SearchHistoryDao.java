package com.gala.video.app.epg.ui.search.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.List;

public class SearchHistoryDao {
    private static final String TAG = "EPG/search/SearchHistoryDao";
    private DBHelper mHelper;

    public void deleteAllHistory() {
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
        r4 = this;
        r0 = 0;
        r3 = r4.mHelper;	 Catch:{ SQLException -> 0x0013, all -> 0x001d }
        r0 = r3.getWritableDatabase();	 Catch:{ SQLException -> 0x0013, all -> 0x001d }
        r2 = "delete * from searchhistory";	 Catch:{ SQLException -> 0x0013, all -> 0x001d }
        r0.execSQL(r2);	 Catch:{ SQLException -> 0x0013, all -> 0x001d }
        if (r0 == 0) goto L_0x0012;
    L_0x000f:
        r0.close();
    L_0x0012:
        return;
    L_0x0013:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ SQLException -> 0x0013, all -> 0x001d }
        if (r0 == 0) goto L_0x0012;
    L_0x0019:
        r0.close();
        goto L_0x0012;
    L_0x001d:
        r3 = move-exception;
        if (r0 == 0) goto L_0x0023;
    L_0x0020:
        r0.close();
    L_0x0023:
        throw r3;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.epg.ui.search.db.SearchHistoryDao.deleteAllHistory():void");
    }

    public void save(com.gala.video.app.epg.ui.search.db.SearchHistoryBean r7) {
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
        r0 = 0;
        r2 = r6.mHelper;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r0 = r2.getWritableDatabase();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2 = 3;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2 = new java.lang.Object[r2];	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 0;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = "SQL_TEST";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 1;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = "SearchHistoryDao --- save --- SearchHistoryBean : ";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 2;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = r7.toString();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        com.gala.video.lib.framework.core.utils.LogUtils.m1576i(r2);	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2 = 5;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2 = new java.lang.Object[r2];	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 0;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = "SQL_TEST";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 1;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = "SearchHistoryDao --- save --- bean.getQpid(): ";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 2;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = r7.getQpid();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 3;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = " --- bean.getKeyword: ";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = r7.getKeyword();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        com.gala.video.lib.framework.core.utils.LogUtils.m1576i(r2);	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2 = 5;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2 = new java.lang.Object[r2];	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 0;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = "SQL_TEST";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 1;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = "SearchHistoryDao --- save --- bean.getType(): ";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 2;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = r7.getType();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 3;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = " --- bean.getKeyword: ";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = r7.getKeyword();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2[r3] = r4;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        com.gala.video.lib.framework.core.utils.LogUtils.m1576i(r2);	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r2 = "insert into searchhistory (keyword,qpid,type) values(?,?,?)";	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = 3;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3 = new java.lang.Object[r3];	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = 0;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r5 = r7.getKeyword();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3[r4] = r5;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = 1;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r5 = r7.getQpid();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3[r4] = r5;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r4 = 2;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r5 = r7.getType();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r3[r4] = r5;	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        r0.execSQL(r2, r3);	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        if (r0 == 0) goto L_0x008f;
    L_0x008c:
        r0.close();
    L_0x008f:
        return;
    L_0x0090:
        r1 = move-exception;
        r1.printStackTrace();	 Catch:{ SQLException -> 0x0090, all -> 0x009a }
        if (r0 == 0) goto L_0x008f;
    L_0x0096:
        r0.close();
        goto L_0x008f;
    L_0x009a:
        r2 = move-exception;
        if (r0 == 0) goto L_0x00a0;
    L_0x009d:
        r0.close();
    L_0x00a0:
        throw r2;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.app.epg.ui.search.db.SearchHistoryDao.save(com.gala.video.app.epg.ui.search.db.SearchHistoryBean):void");
    }

    public SearchHistoryDao(Context context) {
        this.mHelper = new DBHelper(context);
    }

    public void insertHistory(SearchHistoryBean history, int maxCount, boolean isUnique) {
        if (history != null) {
            LogUtils.m1576i("SQL_TEST", "SearchHistoryDao ---- ", history.toString(), " --- 【 KeyWords : ", history.getKeyword(), " 】");
            String keyword = history.getKeyword();
            if (isUnique && queryHistoryByKeyword(keyword) != null) {
                deleteHistoryByKeyword(keyword);
            }
            try {
                int allSize = queryAllHistory().size();
                if (maxCount == -1 || allSize < maxCount) {
                    save(history);
                } else if (allSize >= maxCount) {
                    int needDeleteCount = (allSize - maxCount) + 1;
                    for (int i = 0; i < needDeleteCount; i++) {
                        deleteFirstHistory();
                    }
                    save(history);
                }
                LogUtils.m1570d(TAG, "---databse insertHistory--success--", history.getKeyword());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.m1570d(TAG, "---databse insertHistory--fail--", history.getKeyword());
            }
        }
    }

    public SearchHistoryBean queryHistoryByKeyword(String keyword) {
        SearchHistoryBean bean = null;
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.mHelper.getWritableDatabase();
            cursor = db.rawQuery("select * from searchhistory where keyword=?", new String[]{keyword});
            if (cursor.moveToNext()) {
                bean = new SearchHistoryBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
            }
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return bean;
    }

    public List<SearchHistoryBean> queryHistory(int count) {
        try {
            List<SearchHistoryBean> queryForAll = queryAllHistory();
            int size = queryForAll.size();
            List<SearchHistoryBean> subList = queryForAll.subList(size - count < 0 ? 0 : size - count, size);
            LogUtils.m1568d(TAG, "databse queryHistory--sucess--");
            return subList;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.m1568d(TAG, "databse queryHistory--fail--");
            return null;
        }
    }

    public List<SearchHistoryBean> queryAllHistory() {
        List<SearchHistoryBean> queryForAll = new ArrayList();
        SQLiteDatabase db = null;
        Cursor cursor = null;
        try {
            db = this.mHelper.getReadableDatabase();
            cursor = db.rawQuery("select * from searchhistory order by id desc", null);
            while (cursor.moveToNext()) {
                queryForAll.add(new SearchHistoryBean(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3)));
            }
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return queryForAll;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return queryForAll;
    }

    public boolean deleteFirstHistory() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int result = 0;
        try {
            db = this.mHelper.getWritableDatabase();
            cursor = db.rawQuery("select * from searchhistory", null);
            cursor.moveToFirst();
            result = db.delete("searchhistory", "id=?", new String[]{String.valueOf(cursor.getInt(0))});
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        if (result != 0) {
            return true;
        }
        return false;
    }

    public boolean deleteHistoryByKeyword(String keyword) {
        String[] whereArgs = new String[]{keyword};
        SQLiteDatabase db = null;
        int result = 0;
        try {
            db = this.mHelper.getWritableDatabase();
            result = db.delete("searchhistory", "keyword=?", whereArgs);
            LogUtils.m1576i("SQL_TEST", "SearchHistoryDao --- deleteHistoryByKeyword --- keywords : ", keyword);
            if (db != null) {
                db.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (db != null) {
                db.close();
            }
        } catch (Throwable th) {
            if (db != null) {
                db.close();
            }
        }
        if (result != 0) {
            return true;
        }
        return false;
    }
}
