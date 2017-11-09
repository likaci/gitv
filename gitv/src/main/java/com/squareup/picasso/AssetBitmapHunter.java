package com.squareup.picasso;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import com.squareup.picasso.Picasso.LoadedFrom;
import java.io.IOException;
import java.io.InputStream;

class AssetBitmapHunter extends BitmapHunter {
    protected static final String ANDROID_ASSET = "android_asset";
    private static final int ASSET_PREFIX_LENGTH = "file:///android_asset/".length();
    private final AssetManager assetManager;

    public AssetBitmapHunter(Context context, Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        super(picasso, dispatcher, cache, stats, action);
        this.assetManager = context.getAssets();
    }

    Bitmap decode(Request data) throws IOException {
        return decodeAsset(data.uri.toString().substring(ASSET_PREFIX_LENGTH));
    }

    LoadedFrom getLoadedFrom() {
        return LoadedFrom.DISK;
    }

    Bitmap decodeAsset(String filePath) throws IOException {
        InputStream inputStream;
        Options options = BitmapHunter.createBitmapOptions(this.data);
        if (BitmapHunter.requiresInSampleSize(options)) {
            inputStream = null;
            try {
                inputStream = this.assetManager.open(filePath);
                BitmapFactory.decodeStream(inputStream, null, options);
                BitmapHunter.calculateInSampleSize(this.data.targetWidth, this.data.targetHeight, options);
            } finally {
                Utils.closeQuietly(inputStream);
            }
        }
        inputStream = this.assetManager.open(filePath);
        try {
            Bitmap decodeStream = BitmapFactory.decodeStream(inputStream, null, options);
            return decodeStream;
        } finally {
            Utils.closeQuietly(inputStream);
        }
    }
}
