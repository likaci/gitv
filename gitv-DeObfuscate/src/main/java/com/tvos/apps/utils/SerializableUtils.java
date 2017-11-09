package com.tvos.apps.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SerializableUtils {
    @SuppressLint({"WorldReadableFiles"})
    public static void write(Object rsls, String fileName, Context context) throws IOException {
        FileOutputStream fstream = context.openFileOutput(fileName, 1);
        ObjectOutputStream ostream = new ObjectOutputStream(fstream);
        ostream.writeObject(rsls);
        ostream.flush();
        ostream.close();
        fstream.close();
    }

    public static Object read(String fileName, Context context) throws Exception {
        FileInputStream fstream = context.openFileInput(fileName);
        ObjectInputStream s = new ObjectInputStream(fstream);
        Object obj = s.readObject();
        s.close();
        fstream.close();
        return obj;
    }
}
