package com.squareup.picasso;

import android.graphics.Bitmap;

public interface Cache {
    public static final Cache NONE = new C20231();

    class C20231 implements Cache {
        C20231() {
        }

        public Bitmap get(String key) {
            return null;
        }

        public void set(String key, Bitmap bitmap) {
        }

        public int size() {
            return 0;
        }

        public int maxSize() {
            return 0;
        }

        public void clear() {
        }
    }

    void clear();

    Bitmap get(String str);

    int maxSize();

    void set(String str, Bitmap bitmap);

    int size();
}
