package com.gala.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.gala.afinal.exception.DbException;
import com.gala.imageprovider.private.b;
import com.gala.imageprovider.private.c;
import com.gala.imageprovider.private.d;
import com.gala.imageprovider.private.e;
import com.gala.imageprovider.private.f;
import com.gala.imageprovider.private.g;
import com.gala.imageprovider.private.h;
import com.gala.imageprovider.private.i;
import com.gala.imageprovider.private.j;
import com.gala.imageprovider.private.k;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import org.cybergarage.upnp.std.av.server.object.SearchCriteria;

public class FinalDb {
    private static final String TAG = "FinalDb";
    private static HashMap<String, FinalDb> daoMap = new HashMap();
    private DaoConfig config;
    private SQLiteDatabase db;

    public static class DaoConfig {
        private DbUpdateListener dbUpdateListener;
        private int dbVersion = 1;
        private boolean debug = true;
        private Context mContext = null;
        private String mDbName = "afinal.db";
        private String targetDirectory;

        public Context getContext() {
            return this.mContext;
        }

        public void setContext(Context context) {
            this.mContext = context;
        }

        public String getDbName() {
            return this.mDbName;
        }

        public void setDbName(String dbName) {
            this.mDbName = dbName;
        }

        public int getDbVersion() {
            return this.dbVersion;
        }

        public void setDbVersion(int dbVersion) {
            this.dbVersion = dbVersion;
        }

        public boolean isDebug() {
            return this.debug;
        }

        public void setDebug(boolean debug) {
            this.debug = debug;
        }

        public DbUpdateListener getDbUpdateListener() {
            return this.dbUpdateListener;
        }

        public void setDbUpdateListener(DbUpdateListener dbUpdateListener) {
            this.dbUpdateListener = dbUpdateListener;
        }

        public String getTargetDirectory() {
            return this.targetDirectory;
        }

        public void setTargetDirectory(String targetDirectory) {
            this.targetDirectory = targetDirectory;
        }
    }

    public interface DbUpdateListener {
        void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2);
    }

    class SqliteDbHelper extends SQLiteOpenHelper {
        private DbUpdateListener mDbUpdateListener;

        public SqliteDbHelper(Context context, String name, int version, DbUpdateListener dbUpdateListener) {
            super(context, name, null, version);
            this.mDbUpdateListener = dbUpdateListener;
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (this.mDbUpdateListener != null) {
                this.mDbUpdateListener.onUpgrade(db, oldVersion, newVersion);
            } else {
                FinalDb.this.dropDb();
            }
        }
    }

    private <T> java.util.List<T> findAllBySql(java.lang.Class<T> r5, java.lang.String r6) {
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
        r1 = 0;
        r4.checkTableExist(r5);
        r4.debugSql(r6);
        r0 = r4.db;
        r2 = r0.rawQuery(r6, r1);
        r0 = new java.util.ArrayList;	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
        r0.<init>();	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
    L_0x0012:
        r3 = r2.moveToNext();	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
        if (r3 == 0) goto L_0x002b;	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
    L_0x0018:
        r3 = com.gala.imageprovider.private.b.a(r2, r5, r4);	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
        r0.add(r3);	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
        goto L_0x0012;
    L_0x0020:
        r0 = move-exception;
        r0.printStackTrace();	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
        if (r2 == 0) goto L_0x0029;
    L_0x0026:
        r2.close();
    L_0x0029:
        r0 = r1;
    L_0x002a:
        return r0;
    L_0x002b:
        if (r2 == 0) goto L_0x002a;
    L_0x002d:
        r2.close();
        goto L_0x002a;
    L_0x0031:
        r0 = move-exception;
        if (r2 == 0) goto L_0x0037;
    L_0x0034:
        r2.close();
    L_0x0037:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.afinal.FinalDb.findAllBySql(java.lang.Class, java.lang.String):java.util.List<T>");
    }

    private FinalDb(DaoConfig config) {
        if (config == null) {
            throw new DbException("daoConfig is null");
        } else if (config.getContext() == null) {
            throw new DbException("android context is null");
        } else {
            if (config.getTargetDirectory() == null || config.getTargetDirectory().trim().length() <= 0) {
                this.db = new SqliteDbHelper(config.getContext().getApplicationContext(), config.getDbName(), config.getDbVersion(), config.getDbUpdateListener()).getWritableDatabase();
            } else {
                this.db = createDbFileOnSDCard(config.getTargetDirectory(), config.getDbName());
            }
            this.config = config;
        }
    }

    private static synchronized FinalDb getInstance(DaoConfig daoConfig) {
        FinalDb finalDb;
        synchronized (FinalDb.class) {
            finalDb = (FinalDb) daoMap.get(daoConfig.getDbName());
            if (finalDb == null) {
                finalDb = new FinalDb(daoConfig);
                daoMap.put(daoConfig.getDbName(), finalDb);
            }
        }
        return finalDb;
    }

    public static FinalDb create(Context context) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        return create(daoConfig);
    }

    public static FinalDb create(Context context, boolean isDebug) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setDebug(isDebug);
        return create(daoConfig);
    }

    public static FinalDb create(Context context, String dbName) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setDbName(dbName);
        return create(daoConfig);
    }

    public static FinalDb create(Context context, String dbName, boolean isDebug) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setDbName(dbName);
        daoConfig.setDebug(isDebug);
        return create(daoConfig);
    }

    public static FinalDb create(Context context, String targetDirectory, String dbName) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setDbName(dbName);
        daoConfig.setTargetDirectory(targetDirectory);
        return create(daoConfig);
    }

    public static FinalDb create(Context context, String targetDirectory, String dbName, boolean isDebug) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setTargetDirectory(targetDirectory);
        daoConfig.setDbName(dbName);
        daoConfig.setDebug(isDebug);
        return create(daoConfig);
    }

    public static FinalDb create(Context context, String dbName, boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setDbName(dbName);
        daoConfig.setDebug(isDebug);
        daoConfig.setDbVersion(dbVersion);
        daoConfig.setDbUpdateListener(dbUpdateListener);
        return create(daoConfig);
    }

    public static FinalDb create(Context context, String targetDirectory, String dbName, boolean isDebug, int dbVersion, DbUpdateListener dbUpdateListener) {
        DaoConfig daoConfig = new DaoConfig();
        daoConfig.setContext(context);
        daoConfig.setTargetDirectory(targetDirectory);
        daoConfig.setDbName(dbName);
        daoConfig.setDebug(isDebug);
        daoConfig.setDbVersion(dbVersion);
        daoConfig.setDbUpdateListener(dbUpdateListener);
        return create(daoConfig);
    }

    public static FinalDb create(DaoConfig daoConfig) {
        return getInstance(daoConfig);
    }

    public void save(Object entity) {
        checkTableExist(entity.getClass());
        List<g> a = b.a(entity);
        StringBuffer stringBuffer = new StringBuffer();
        e eVar = null;
        if (a != null && a.size() > 0) {
            e eVar2 = new e();
            stringBuffer.append("INSERT INTO ");
            stringBuffer.append(k.a(entity.getClass()).a());
            stringBuffer.append(" (");
            for (g gVar : a) {
                stringBuffer.append(gVar.a()).append(",");
                eVar2.a(gVar.a());
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append(") VALUES ( ");
            int size = a.size();
            for (int i = 0; i < size; i++) {
                stringBuffer.append("?,");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append(")");
            eVar2.a(stringBuffer.toString());
            eVar = eVar2;
        }
        exeSqlInfo(eVar);
    }

    public boolean saveBindId(Object entity) {
        checkTableExist(entity.getClass());
        List a = b.a(entity);
        if (a == null || a.size() <= 0) {
            return false;
        }
        k a2 = k.a(entity.getClass());
        ContentValues contentValues = new ContentValues();
        insertContentValues(a, contentValues);
        Long valueOf = Long.valueOf(this.db.insert(a2.a(), null, contentValues));
        if (valueOf.longValue() == -1) {
            return false;
        }
        a2.a().a(entity, valueOf);
        return true;
    }

    private void insertContentValues(List<g> list, ContentValues cv) {
        if (list == null || cv == null) {
            Log.w(TAG, "insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
            return;
        }
        for (g gVar : list) {
            cv.put(gVar.a(), gVar.a().toString());
        }
    }

    public void update(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(b.b(entity));
    }

    public void update(Object entity, String strWhere) {
        checkTableExist(entity.getClass());
        exeSqlInfo(b.a(entity, strWhere));
    }

    public void delete(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(b.a(entity));
    }

    public void deleteById(Class<?> clazz, Object id) {
        checkTableExist(clazz);
        exeSqlInfo(b.a((Class) clazz, id));
    }

    public void deleteByWhere(Class<?> clazz, String strWhere) {
        checkTableExist(clazz);
        String a = b.a((Class) clazz, strWhere);
        debugSql(a);
        this.db.execSQL(a);
    }

    public void deleteAll(Class<?> clazz) {
        checkTableExist(clazz);
        String a = b.a((Class) clazz, null);
        debugSql(a);
        this.db.execSQL(a);
    }

    public void dropTable(Class<?> clazz) {
        checkTableExist(clazz);
        String str = "DROP TABLE " + k.a(clazz).a();
        debugSql(str);
        this.db.execSQL(str);
    }

    public void dropDb() {
        Cursor rawQuery = this.db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
        if (rawQuery != null) {
            while (rawQuery.moveToNext()) {
                this.db.execSQL("DROP TABLE " + rawQuery.getString(0));
            }
        }
        if (rawQuery != null) {
            rawQuery.close();
        }
    }

    private void exeSqlInfo(e sqlInfo) {
        if (sqlInfo != null) {
            debugSql(sqlInfo.a());
            this.db.execSQL(sqlInfo.a(), sqlInfo.a());
            return;
        }
        Log.e(TAG, "sava error:sqlInfo is null");
    }

    public <T> T findById(Object id, Class<T> clazz) {
        checkTableExist(clazz);
        e b = b.b((Class) clazz, id);
        if (b != null) {
            debugSql(b.a());
            Cursor rawQuery = this.db.rawQuery(b.a(), b.a());
            T a;
            try {
                if (rawQuery.moveToNext()) {
                    a = b.a(rawQuery, (Class) clazz, this);
                    return a;
                }
                rawQuery.close();
            } catch (Exception e) {
                a = e;
                a.printStackTrace();
            } finally {
                rawQuery.close();
            }
        }
        return null;
    }

    public <T> T findWithManyToOneById(Object id, Class<T> clazz) {
        checkTableExist(clazz);
        String a = b.a((Class) clazz, id);
        debugSql(a);
        b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadManyToOne$482427bc(findDbModelBySQL$742e7adf, b.a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, new Class[0]);
        }
        return null;
    }

    public <T> T findWithManyToOneById(Object id, Class<T> clazz, Class<?>... findClass) {
        checkTableExist(clazz);
        String a = b.a((Class) clazz, id);
        debugSql(a);
        b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadManyToOne$482427bc(findDbModelBySQL$742e7adf, b.a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, findClass);
        }
        return null;
    }

    public <T> T loadManyToOne$482427bc(b dbModel, T entity, Class<T> clazz, Class<?>... findClass) {
        if (entity != null) {
            try {
                for (h hVar : k.a(clazz).c.values()) {
                    Object a;
                    if (dbModel != null) {
                        a = dbModel.a(hVar.a());
                    } else if (hVar.a((Object) entity).getClass() != c.class || hVar.a((Object) entity) == null) {
                        a = null;
                    } else {
                        a = ((c) hVar.a((Object) entity)).b();
                    }
                    if (a != null) {
                        Object obj;
                        if (findClass == null || findClass.length == 0) {
                            obj = 1;
                        } else {
                            obj = null;
                        }
                        for (Class<?> cls : findClass) {
                            if (hVar.a() == cls) {
                                obj = 1;
                                break;
                            }
                        }
                        if (obj != null) {
                            obj = findById(Integer.valueOf(a.toString()), hVar.a());
                            if (obj != null) {
                                if (hVar.a((Object) entity).getClass() == c.class) {
                                    if (hVar.a((Object) entity) == null) {
                                        hVar.a(entity, new c(entity, clazz, hVar.a(), this));
                                    }
                                    ((c) hVar.a((Object) entity)).a(obj);
                                } else {
                                    hVar.a(entity, obj);
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public <T> T findWithOneToManyById(Object id, Class<T> clazz) {
        checkTableExist(clazz);
        String a = b.a((Class) clazz, id);
        debugSql(a);
        b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadOneToMany(b.a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, new Class[0]);
        }
        return null;
    }

    public <T> T findWithOneToManyById(Object id, Class<T> clazz, Class<?>... findClass) {
        checkTableExist(clazz);
        String a = b.a((Class) clazz, id);
        debugSql(a);
        b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadOneToMany(b.a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, findClass);
        }
        return null;
    }

    public <T> T loadOneToMany(T entity, Class<T> clazz, Class<?>... findClass) {
        if (entity != null) {
            try {
                Collection<i> values = k.a(clazz).b.values();
                Object a = k.a(clazz).a().a((Object) entity);
                for (i iVar : values) {
                    Object obj;
                    if (findClass == null || findClass.length == 0) {
                        obj = 1;
                    } else {
                        obj = null;
                    }
                    for (Class<?> cls : findClass) {
                        if (iVar.a() == cls) {
                            obj = 1;
                            break;
                        }
                    }
                    if (obj != null) {
                        List findAllByWhere = findAllByWhere(iVar.a(), iVar.a() + SearchCriteria.EQ + a);
                        if (findAllByWhere == null) {
                            continue;
                        } else if (iVar.b() == d.class) {
                            iVar.a((Object) entity);
                            d.a();
                        } else {
                            iVar.a(entity, findAllByWhere);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return entity;
    }

    public <T> List<T> findAll(Class<T> clazz) {
        checkTableExist(clazz);
        return findAllBySql(clazz, b.a((Class) clazz));
    }

    public <T> List<T> findAll(Class<T> clazz, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, b.a((Class) clazz) + " ORDER BY " + orderBy);
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(clazz, b.b((Class) clazz, strWhere));
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, b.b((Class) clazz, strWhere) + " ORDER BY " + orderBy);
    }

    public b findDbModelBySQL$742e7adf(String strSQL) {
        b bVar = null;
        debugSql(strSQL);
        Cursor rawQuery = this.db.rawQuery(strSQL, null);
        try {
            if (rawQuery.moveToNext()) {
                bVar = b.a(rawQuery);
            } else {
                rawQuery.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rawQuery.close();
        }
        return bVar;
    }

    public List<b> findDbModelListBySQL(String strSQL) {
        debugSql(strSQL);
        Cursor rawQuery = this.db.rawQuery(strSQL, null);
        List<b> arrayList = new ArrayList();
        while (rawQuery.moveToNext()) {
            try {
                arrayList.add(b.a(rawQuery));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rawQuery.close();
            }
        }
        return arrayList;
    }

    private void checkTableExist(Class<?> clazz) {
        if (!tableIsExist(k.a(clazz))) {
            k a = k.a(clazz);
            f a2 = a.a();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("CREATE TABLE IF NOT EXISTS ");
            stringBuffer.append(a.a());
            stringBuffer.append(" ( ");
            Class b = a2.b();
            if (b == Integer.TYPE || b == Integer.class || b == Long.TYPE || b == Long.class) {
                stringBuffer.append(a2.a()).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            } else {
                stringBuffer.append(a2.a()).append(" TEXT PRIMARY KEY,");
            }
            for (j jVar : a.f19a.values()) {
                stringBuffer.append(jVar.a());
                Class b2 = jVar.b();
                if (b2 == Integer.TYPE || b2 == Integer.class || b2 == Long.TYPE || b2 == Long.class) {
                    stringBuffer.append(" INTEGER");
                } else if (b2 == Float.TYPE || b2 == Float.class || b2 == Double.TYPE || b2 == Double.class) {
                    stringBuffer.append(" REAL");
                } else if (b2 == Boolean.TYPE || b2 == Boolean.class) {
                    stringBuffer.append(" NUMERIC");
                }
                stringBuffer.append(",");
            }
            for (h a3 : a.c.values()) {
                stringBuffer.append(a3.a()).append(" INTEGER,");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append(" )");
            String stringBuffer2 = stringBuffer.toString();
            debugSql(stringBuffer2);
            this.db.execSQL(stringBuffer2);
        }
    }

    private boolean tableIsExist(k table) {
        Cursor cursor = null;
        if (table.a()) {
            return true;
        }
        try {
            String str = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + table.a() + "' ";
            debugSql(str);
            cursor = this.db.rawQuery(str, null);
            if (cursor == null || !cursor.moveToNext() || cursor.getInt(0) <= 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return false;
            }
            table.a();
            if (cursor == null) {
                return true;
            }
            cursor.close();
            return true;
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
    }

    private void debugSql(String sql) {
        if (this.config != null && this.config.isDebug()) {
            Log.d("Debug SQL", ">>>>>>  " + sql);
        }
    }

    private SQLiteDatabase createDbFileOnSDCard(String sdcardPath, String dbfilename) {
        File file = new File(sdcardPath, dbfilename);
        if (file.exists()) {
            return SQLiteDatabase.openOrCreateDatabase(file, null);
        }
        try {
            if (file.createNewFile()) {
                return SQLiteDatabase.openOrCreateDatabase(file, null);
            }
            return null;
        } catch (Throwable e) {
            throw new DbException("数据库文件创建失败", e);
        }
    }
}
