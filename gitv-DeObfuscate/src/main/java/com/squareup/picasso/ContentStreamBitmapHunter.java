package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.squareup.picasso.Picasso.LoadedFrom;
import java.io.IOException;
import java.io.InputStream;

class ContentStreamBitmapHunter extends BitmapHunter {
    final Context context;

    ContentStreamBitmapHunter(Context context, Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        super(picasso, dispatcher, cache, stats, action);
        this.context = context;
    }

    Bitmap decode(Request data) throws IOException {
        return decodeContentStream(data);
    }

    LoadedFrom getLoadedFrom() {
        return LoadedFrom.DISK;
    }

    protected Bitmap decodeContentStream(Request data) throws IOException {
        InputStream inputStream;
        ContentResolver contentResolver = this.context.getContentResolver();
        Options options = BitmapHunter.createBitmapOptions(data);
        if (BitmapHunter.requiresInSampleSize(options)) {
            inputStream = null;
            try {
                inputStream = contentResolver.openInputStream(data.uri);
                BitmapFactory.decodeStream(inputStream, null, options);
                BitmapHunter.calculateInSampleSize(data.targetWidth, data.targetHeight, options);
            } finally {
                Utils.closeQuietly(inputStream);
            }
        }
        inputStream = contentResolver.openInputStream(data.uri);
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            return decodeStream;
        } finally {
            Utils.closeQuietly(inputStream);
        }
    }
}
