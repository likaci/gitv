package com.gala.afinal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.gala.afinal.exception.DbException;
import com.gala.imageprovider.p000private.C0126b;
import com.gala.imageprovider.p000private.C0127c;
import com.gala.imageprovider.p000private.C0128d;
import com.gala.imageprovider.p000private.C0129e;
import com.gala.imageprovider.p000private.C0130j;
import com.gala.imageprovider.p000private.C0131f;
import com.gala.imageprovider.p000private.C0132g;
import com.gala.imageprovider.p000private.C0133h;
import com.gala.imageprovider.p000private.C0134i;
import com.gala.imageprovider.p000private.C0135k;
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
        r3 = com.gala.imageprovider.p000private.C0126b.m296a(r2, r5, r4);	 Catch:{ Exception -> 0x0020, all -> 0x0031 }
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
        List<C0132g> a = C0126b.m292a(entity);
        StringBuffer stringBuffer = new StringBuffer();
        C0129e c0129e = null;
        if (a != null && a.size() > 0) {
            C0129e c0129e2 = new C0129e();
            stringBuffer.append("INSERT INTO ");
            stringBuffer.append(C0135k.m341a(entity.getClass()).m342a());
            stringBuffer.append(" (");
            for (C0132g c0132g : a) {
                stringBuffer.append(c0132g.m335a()).append(",");
                c0129e2.m320a(c0132g.m335a());
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append(") VALUES ( ");
            int size = a.size();
            for (int i = 0; i < size; i++) {
                stringBuffer.append("?,");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append(")");
            c0129e2.m321a(stringBuffer.toString());
            c0129e = c0129e2;
        }
        exeSqlInfo(c0129e);
    }

    public boolean saveBindId(Object entity) {
        checkTableExist(entity.getClass());
        List a = C0126b.m292a(entity);
        if (a == null || a.size() <= 0) {
            return false;
        }
        C0135k a2 = C0135k.m341a(entity.getClass());
        ContentValues contentValues = new ContentValues();
        insertContentValues(a, contentValues);
        Long valueOf = Long.valueOf(this.db.insert(a2.m342a(), null, contentValues));
        if (valueOf.longValue() == -1) {
            return false;
        }
        a2.m342a().m326a(entity, valueOf);
        return true;
    }

    private void insertContentValues(List<C0132g> list, ContentValues cv) {
        if (list == null || cv == null) {
            Log.w(TAG, "insertContentValues: List<KeyValue> is empty or ContentValues is empty!");
            return;
        }
        for (C0132g c0132g : list) {
            cv.put(c0132g.m335a(), c0132g.m335a().toString());
        }
    }

    public void update(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(C0126b.m309b(entity));
    }

    public void update(Object entity, String strWhere) {
        checkTableExist(entity.getClass());
        exeSqlInfo(C0126b.m293a(entity, strWhere));
    }

    public void delete(Object entity) {
        checkTableExist(entity.getClass());
        exeSqlInfo(C0126b.m292a(entity));
    }

    public void deleteById(Class<?> clazz, Object id) {
        checkTableExist(clazz);
        exeSqlInfo(C0126b.m291a((Class) clazz, id));
    }

    public void deleteByWhere(Class<?> clazz, String strWhere) {
        checkTableExist(clazz);
        String a = C0126b.m304a((Class) clazz, strWhere);
        debugSql(a);
        this.db.execSQL(a);
    }

    public void deleteAll(Class<?> clazz) {
        checkTableExist(clazz);
        String a = C0126b.m304a((Class) clazz, null);
        debugSql(a);
        this.db.execSQL(a);
    }

    public void dropTable(Class<?> clazz) {
        checkTableExist(clazz);
        String str = "DROP TABLE " + C0135k.m341a(clazz).m342a();
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

    private void exeSqlInfo(C0129e sqlInfo) {
        if (sqlInfo != null) {
            debugSql(sqlInfo.m319a());
            this.db.execSQL(sqlInfo.m319a(), sqlInfo.m319a());
            return;
        }
        Log.e(TAG, "sava error:sqlInfo is null");
    }

    public <T> T findById(Object id, Class<T> clazz) {
        T a;
        checkTableExist(clazz);
        C0129e b = C0126b.m308b((Class) clazz, id);
        if (b != null) {
            debugSql(b.m319a());
            Cursor rawQuery = this.db.rawQuery(b.m319a(), b.m319a());
            try {
                if (rawQuery.moveToNext()) {
                    a = C0126b.m296a(rawQuery, (Class) clazz, this);
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
        String a = C0126b.m291a((Class) clazz, id);
        debugSql(a);
        C0126b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadManyToOne$482427bc(findDbModelBySQL$742e7adf, C0126b.m297a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, new Class[0]);
        }
        return null;
    }

    public <T> T findWithManyToOneById(Object id, Class<T> clazz, Class<?>... findClass) {
        checkTableExist(clazz);
        String a = C0126b.m291a((Class) clazz, id);
        debugSql(a);
        C0126b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadManyToOne$482427bc(findDbModelBySQL$742e7adf, C0126b.m297a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, findClass);
        }
        return null;
    }

    public <T> T loadManyToOne$482427bc(C0126b dbModel, T entity, Class<T> clazz, Class<?>... findClass) {
        if (entity != null) {
            try {
                for (C0133h c0133h : C0135k.m341a(clazz).f571c.values()) {
                    Object a;
                    if (dbModel != null) {
                        a = dbModel.m305a(c0133h.mo652a());
                    } else if (c0133h.m324a((Object) entity).getClass() != C0127c.class || c0133h.m324a((Object) entity) == null) {
                        a = null;
                    } else {
                        a = ((C0127c) c0133h.m324a((Object) entity)).m316b();
                    }
                    if (a != null) {
                        Object obj;
                        if (findClass == null || findClass.length == 0) {
                            obj = 1;
                        } else {
                            obj = null;
                        }
                        for (Class<?> cls : findClass) {
                            if (c0133h.mo652a() == cls) {
                                obj = 1;
                                break;
                            }
                        }
                        if (obj != null) {
                            obj = findById(Integer.valueOf(a.toString()), c0133h.mo652a());
                            if (obj != null) {
                                if (c0133h.m324a((Object) entity).getClass() == C0127c.class) {
                                    if (c0133h.m324a((Object) entity) == null) {
                                        c0133h.m326a(entity, new C0127c(entity, clazz, c0133h.mo652a(), this));
                                    }
                                    ((C0127c) c0133h.m324a((Object) entity)).m315a(obj);
                                } else {
                                    c0133h.m326a(entity, obj);
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
        String a = C0126b.m291a((Class) clazz, id);
        debugSql(a);
        C0126b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadOneToMany(C0126b.m297a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, new Class[0]);
        }
        return null;
    }

    public <T> T findWithOneToManyById(Object id, Class<T> clazz, Class<?>... findClass) {
        checkTableExist(clazz);
        String a = C0126b.m291a((Class) clazz, id);
        debugSql(a);
        C0126b findDbModelBySQL$742e7adf = findDbModelBySQL$742e7adf(a);
        if (findDbModelBySQL$742e7adf != null) {
            return loadOneToMany(C0126b.m297a(findDbModelBySQL$742e7adf, (Class) clazz), clazz, findClass);
        }
        return null;
    }

    public <T> T loadOneToMany(T entity, Class<T> clazz, Class<?>... findClass) {
        if (entity != null) {
            try {
                Collection<C0134i> values = C0135k.m341a(clazz).f570b.values();
                Object a = C0135k.m341a(clazz).m342a().m324a((Object) entity);
                for (C0134i c0134i : values) {
                    Object obj;
                    if (findClass == null || findClass.length == 0) {
                        obj = 1;
                    } else {
                        obj = null;
                    }
                    for (Class<?> cls : findClass) {
                        if (c0134i.mo652a() == cls) {
                            obj = 1;
                            break;
                        }
                    }
                    if (obj != null) {
                        List findAllByWhere = findAllByWhere(c0134i.mo652a(), c0134i.mo652a() + SearchCriteria.EQ + a);
                        if (findAllByWhere == null) {
                            continue;
                        } else if (c0134i.m330b() == C0128d.class) {
                            c0134i.m324a((Object) entity);
                            C0128d.m318a();
                        } else {
                            c0134i.m326a(entity, findAllByWhere);
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
        return findAllBySql(clazz, C0126b.m302a((Class) clazz));
    }

    public <T> List<T> findAll(Class<T> clazz, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, C0126b.m302a((Class) clazz) + " ORDER BY " + orderBy);
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
        checkTableExist(clazz);
        return findAllBySql(clazz, C0126b.m310b((Class) clazz, strWhere));
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String orderBy) {
        checkTableExist(clazz);
        return findAllBySql(clazz, C0126b.m310b((Class) clazz, strWhere) + " ORDER BY " + orderBy);
    }

    public C0126b findDbModelBySQL$742e7adf(String strSQL) {
        C0126b c0126b = null;
        debugSql(strSQL);
        Cursor rawQuery = this.db.rawQuery(strSQL, null);
        try {
            if (rawQuery.moveToNext()) {
                c0126b = C0126b.m290a(rawQuery);
            } else {
                rawQuery.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            rawQuery.close();
        }
        return c0126b;
    }

    public List<C0126b> findDbModelListBySQL(String strSQL) {
        debugSql(strSQL);
        Cursor rawQuery = this.db.rawQuery(strSQL, null);
        List<C0126b> arrayList = new ArrayList();
        while (rawQuery.moveToNext()) {
            try {
                arrayList.add(C0126b.m290a(rawQuery));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                rawQuery.close();
            }
        }
        return arrayList;
    }

    private void checkTableExist(Class<?> clazz) {
        if (!tableIsExist(C0135k.m341a(clazz))) {
            C0135k a = C0135k.m341a(clazz);
            C0131f a2 = a.m342a();
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("CREATE TABLE IF NOT EXISTS ");
            stringBuffer.append(a.m342a());
            stringBuffer.append(" ( ");
            Class b = a2.m330b();
            if (b == Integer.TYPE || b == Integer.class || b == Long.TYPE || b == Long.class) {
                stringBuffer.append(a2.mo652a()).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
            } else {
                stringBuffer.append(a2.mo652a()).append(" TEXT PRIMARY KEY,");
            }
            for (C0130j c0130j : a.f568a.values()) {
                stringBuffer.append(c0130j.mo652a());
                Class b2 = c0130j.m330b();
                if (b2 == Integer.TYPE || b2 == Integer.class || b2 == Long.TYPE || b2 == Long.class) {
                    stringBuffer.append(" INTEGER");
                } else if (b2 == Float.TYPE || b2 == Float.class || b2 == Double.TYPE || b2 == Double.class) {
                    stringBuffer.append(" REAL");
                } else if (b2 == Boolean.TYPE || b2 == Boolean.class) {
                    stringBuffer.append(" NUMERIC");
                }
                stringBuffer.append(",");
            }
            for (C0133h a3 : a.f571c.values()) {
                stringBuffer.append(a3.mo652a()).append(" INTEGER,");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);
            stringBuffer.append(" )");
            String stringBuffer2 = stringBuffer.toString();
            debugSql(stringBuffer2);
            this.db.execSQL(stringBuffer2);
        }
    }

    private boolean tableIsExist(C0135k table) {
        Cursor cursor = null;
        if (table.m342a()) {
            return true;
        }
        try {
            String str = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='" + table.m342a() + "' ";
            debugSql(str);
            cursor = this.db.rawQuery(str, null);
            if (cursor == null || !cursor.moveToNext() || cursor.getInt(0) <= 0) {
                if (cursor != null) {
                    cursor.close();
                }
                return false;
            }
            table.m342a();
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
