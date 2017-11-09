package com.tvos.appmanager.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameDBUtil {
    private String COLUMNNAME_PKGNAME = AppDBHelper.COLUMN_PKGNAME;
    private String TABLENAME_GAMEINFO = "gameinfo";
    private Context mContext;
    private SQLiteDatabase mDB;
    private String mGameDBPath;

    public GameDBUtil(Context context) {
        this.mContext = context;
        this.mGameDBPath = this.mContext.getDatabasePath("gamemanager.db").getAbsolutePath();
    }

    private boolean getGameDB() {
        if (!new File(this.mGameDBPath).exists()) {
            return false;
        }
        this.mDB = SQLiteDatabase.openDatabase(this.mGameDBPath, null, 1);
        if (this.mDB != null) {
            return true;
        }
        return false;
    }

    public List<String> getInstalledGameList() {
        Exception e;
        Throwable th;
        List<String> list = null;
        Cursor cursor = null;
        try {
            if (getGameDB()) {
                if (this.mDB != null) {
                    cursor = this.mDB.query(this.TABLENAME_GAMEINFO, new String[]{this.COLUMNNAME_PKGNAME}, null, null, null, null, null);
                    if (cursor.getCount() > 0) {
                        List<String> pkgNameList = new ArrayList();
                        while (cursor.moveToNext()) {
                            try {
                                pkgNameList.add(cursor.getString(cursor.getColumnIndex(this.COLUMNNAME_PKGNAME)));
                            } catch (Exception e2) {
                                e = e2;
                                list = pkgNameList;
                            } catch (Throwable th2) {
                                th = th2;
                                list = pkgNameList;
                            }
                        }
                        list = pkgNameList;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (this.mDB != null) {
                    this.mDB.close();
                }
                return list;
            }
            if (cursor != null) {
                cursor.close();
            }
            if (this.mDB == null) {
                return null;
            }
            this.mDB.close();
            return null;
        } catch (Exception e3) {
            e = e3;
            try {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
                if (this.mDB != null) {
                    this.mDB.close();
                }
                return list;
            } catch (Throwable th3) {
                th = th3;
                if (cursor != null) {
                    cursor.close();
                }
                if (this.mDB != null) {
                    this.mDB.close();
                }
                throw th;
            }
        }
    }

    public boolean isGame(String pkgName) {
        Cursor cursor = null;
        boolean result = false;
        try {
            if (getGameDB()) {
                if (this.mDB != null) {
                    cursor = this.mDB.query(this.TABLENAME_GAMEINFO, null, this.COLUMNNAME_PKGNAME + "= ?", new String[]{pkgName}, null, null, null);
                    if (cursor.getCount() > 0) {
                        result = true;
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
                if (this.mDB != null) {
                    this.mDB.close();
                }
                return result;
            }
            if (cursor != null) {
                cursor.close();
            }
            if (this.mDB == null) {
                return false;
            }
            this.mDB.close();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            if (cursor != null) {
                cursor.close();
            }
            if (this.mDB != null) {
                this.mDB.close();
            }
        } catch (Throwable th) {
            if (cursor != null) {
                cursor.close();
            }
            if (this.mDB != null) {
                this.mDB.close();
            }
        }
    }
}
