package com.gala.appmanager.b;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.util.Log;
import com.gala.appmanager.utils.SerializableList;
import com.gala.video.db.DBManager;

public class a {
    private DBManager a;

    public a(Context context) {
        this.a = DBManager.getInstance(context);
        b();
    }

    public void m64a() {
        if (this.a != null) {
            this.a.open();
        }
    }

    public void a(String str) {
        if (this.a != null) {
            this.a.insert("INSERT INTO order_list VALUES('" + str + "'," + (a() + 1) + ")");
        }
    }

    public void b(String str) {
        if (this.a != null) {
            this.a.delete("DELETE FROM order_list WHERE package_name='" + str + "'");
        }
    }

    public void c(String str) {
        if (this.a != null) {
            this.a.update("UPDATE order_list SET sequence=" + (a() + 1) + " WHERE " + "package_name" + "='" + str + "'");
        }
    }

    public SerializableList<String> m63a() {
        SerializableList<String> serializableList = new SerializableList();
        if (this.a != null) {
            Cursor rawQuery = this.a.rawQuery("SELECT package_name FROM order_list ORDER BY sequence DESC", null);
            if (rawQuery != null) {
                while (rawQuery.moveToNext()) {
                    try {
                        String string = rawQuery.getString(rawQuery.getColumnIndex("package_name"));
                        serializableList.add(string);
                        Log.d("DBManager", "DB: " + string);
                    } catch (Exception e) {
                        Log.d("DBManager", e.toString());
                    }
                }
                rawQuery.close();
            }
        }
        return serializableList;
    }

    public boolean m65a(String str) {
        if (this.a != null) {
            a();
            Cursor rawQuery = this.a.rawQuery("SELECT sequence FROM order_list WHERE package_name='" + str + "'", null);
            if (rawQuery != null) {
                if (rawQuery.moveToNext()) {
                    rawQuery.close();
                    return true;
                }
                rawQuery.close();
            }
        }
        return false;
    }

    private void b() {
        try {
            this.a.createTable("CREATE TABLE IF NOT EXISTS order_list (package_name VARCHAR(256) PRIMARY KEY, sequence INTEGER)");
        } catch (SQLiteException e) {
            Log.e("DBManager", e.toString());
        } catch (Exception e2) {
            Log.e("DBManager", e2.toString());
        }
    }

    private int a() {
        int i = -1;
        if (this.a != null) {
            Cursor rawQuery = this.a.rawQuery("SELECT sequence FROM order_list ORDER BY sequence DESC LIMIT 0,1", null);
            if (rawQuery != null) {
                try {
                    if (rawQuery.moveToNext()) {
                        i = rawQuery.getInt(rawQuery.getColumnIndex("sequence"));
                    }
                } catch (Exception e) {
                    Log.e("DBManager", e.toString());
                }
                rawQuery.close();
            }
        }
        return i;
    }
}
