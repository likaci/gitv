package com.tvos.appmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.tvos.appmanager.model.AppInfo;
import com.tvos.appmanager.model.IAppInfo;
import java.util.ArrayList;
import java.util.List;

public class AppDBUtil {
    private AppDBHelper mAppDBHelper = null;
    private Context mContext = null;

    public AppDBUtil(Context context) {
        init(context);
    }

    public void init(Context context) {
        this.mContext = context;
        this.mAppDBHelper = new AppDBHelper(this.mContext);
    }

    public void release() {
        this.mContext = null;
        this.mAppDBHelper = null;
    }

    public synchronized boolean insert(AppInfo info) {
        boolean z = false;
        synchronized (this) {
            boolean flag = true;
            SQLiteDatabase mDatabase = null;
            try {
                if (this.mAppDBHelper == null) {
                    if (mDatabase != null) {
                        mDatabase.close();
                    }
                } else {
                    mDatabase = this.mAppDBHelper.getReadableDatabase();
                    mDatabase.insert(AppDBHelper.TABLE_NAME, null, DBUtil.getContentValues(info, false));
                    if (mDatabase != null) {
                        mDatabase.close();
                    }
                    z = flag;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
                if (mDatabase != null) {
                    mDatabase.close();
                }
            } catch (Throwable th) {
                if (mDatabase != null) {
                    mDatabase.close();
                }
            }
        }
        return z;
    }

    public synchronized boolean update(AppInfo info) {
        boolean z = false;
        synchronized (this) {
            boolean flag = true;
            SQLiteDatabase mDatabase = null;
            try {
                if (this.mAppDBHelper == null) {
                    if (mDatabase != null) {
                        mDatabase.close();
                    }
                } else {
                    mDatabase = this.mAppDBHelper.getWritableDatabase();
                    mDatabase.update(AppDBHelper.TABLE_NAME, DBUtil.getContentValues(info, false), "pkgName = ?", new String[]{info.getPkgName()});
                    if (mDatabase != null) {
                        mDatabase.close();
                    }
                    z = flag;
                }
            } catch (Exception e) {
                e.printStackTrace();
                flag = false;
                if (mDatabase != null) {
                    mDatabase.close();
                }
            } catch (Throwable th) {
                if (mDatabase != null) {
                    mDatabase.close();
                }
            }
        }
        return z;
    }

    public synchronized boolean delete(String pkgName) {
        boolean flag;
        flag = true;
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mAppDBHelper.getWritableDatabase();
            mDatabase.delete(AppDBHelper.TABLE_NAME, "pkgName = ?", new String[]{pkgName});
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return flag;
    }

    public synchronized boolean update(String pkgName, int status) {
        boolean flag;
        flag = true;
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mAppDBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", Integer.valueOf(status));
            mDatabase.update(AppDBHelper.TABLE_NAME, values, "pkgName = ?", new String[]{pkgName});
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return flag;
    }

    public synchronized boolean update(String pkgName, int status, long startTime) {
        boolean flag;
        flag = true;
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mAppDBHelper.getReadableDatabase();
            ContentValues values = new ContentValues();
            values.put("status", Integer.valueOf(status));
            values.put("startTime", Long.valueOf(startTime));
            mDatabase.update(AppDBHelper.TABLE_NAME, values, "pkgName = ?", new String[]{pkgName});
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return flag;
    }

    public synchronized AppInfo findAppInfoByPkgName(String pkgName) {
        AppInfo appInfo;
        Cursor cursor = null;
        SQLiteDatabase mDatabase = null;
        try {
            if (this.mAppDBHelper == null) {
                if (cursor != null) {
                    cursor.close();
                }
                if (mDatabase != null) {
                    mDatabase.close();
                }
                appInfo = null;
            } else {
                mDatabase = this.mAppDBHelper.getReadableDatabase();
                cursor = mDatabase.query(AppDBHelper.TABLE_NAME, AppDBHelper.COLUMNS, "pkgName = ?", new String[]{pkgName}, null, null, null);
                if (cursor == null || cursor.getCount() <= 0) {
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (mDatabase != null) {
                        mDatabase.close();
                    }
                    appInfo = null;
                } else {
                    appInfo = new AppInfo();
                    cursor.moveToNext();
                    DBUtil.getObject(appInfo, cursor, false);
                    if (cursor != null) {
                        cursor.close();
                    }
                    if (mDatabase != null) {
                        mDatabase.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return appInfo;
    }

    public synchronized int getAppStatusByPkgName(String pkgName) {
        int status;
        Cursor cursor = null;
        status = 2;
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mAppDBHelper.getReadableDatabase();
            cursor = mDatabase.query(AppDBHelper.TABLE_NAME, new String[]{"status"}, "pkgName = ?", new String[]{pkgName}, null, null, null);
            if (!(cursor == null || cursor.getCount() == 0)) {
                cursor.moveToNext();
                status = cursor.getInt(cursor.getColumnIndex("status"));
            }
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return status;
    }

    public synchronized long getAppStartTimeByPkgName(String pkgName) {
        long startTime;
        Cursor cursor = null;
        startTime = 0;
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mAppDBHelper.getReadableDatabase();
            cursor = mDatabase.query(AppDBHelper.TABLE_NAME, new String[]{"startTime"}, "pkgName = ?", new String[]{pkgName}, null, null, null);
            if (!(cursor == null || cursor.getCount() == 0)) {
                cursor.moveToNext();
                startTime = cursor.getLong(cursor.getColumnIndex("startTime"));
            }
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return startTime;
    }

    public synchronized List<IAppInfo> getAllRecord(int sortMethod) {
        List<IAppInfo> recordList;
        recordList = new ArrayList();
        Cursor cursor = null;
        SQLiteDatabase mDatabase = null;
        String order = null;
        try {
            mDatabase = this.mAppDBHelper.getReadableDatabase();
            switch (sortMethod) {
                case 1:
                    order = "appInstalledTime desc";
                    break;
                case 2:
                    order = "startTime desc";
                    break;
            }
            cursor = mDatabase.query(AppDBHelper.TABLE_NAME, AppDBHelper.COLUMNS, null, null, null, null, order);
            if (!(cursor == null || cursor.getCount() == 0)) {
                while (cursor.moveToNext()) {
                    AppInfo info = new AppInfo();
                    DBUtil.getObject(info, cursor, false);
                    recordList.add(info);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return recordList;
    }

    public synchronized List<IAppInfo> getStartedApp() {
        List<IAppInfo> recordList;
        recordList = new ArrayList();
        Cursor cursor = null;
        SQLiteDatabase mDatabase = null;
        try {
            mDatabase = this.mAppDBHelper.getReadableDatabase();
            String str = AppDBHelper.TABLE_NAME;
            String[] strArr = AppDBHelper.COLUMNS;
            cursor = mDatabase.query(str, strArr, "status = ?", new String[]{String.valueOf(2)}, null, null, "startTime desc");
            if (!(cursor == null || cursor.getCount() == 0)) {
                while (cursor.moveToNext()) {
                    AppInfo info = new AppInfo();
                    DBUtil.getObject(info, cursor, false);
                    recordList.add(info);
                }
            }
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (mDatabase != null) {
                mDatabase.close();
            }
        }
        return recordList;
    }
}
