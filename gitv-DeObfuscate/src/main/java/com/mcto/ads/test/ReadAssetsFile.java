package com.mcto.ads.test;

import android.content.res.AssetManager;
import java.io.IOException;
import java.io.InputStream;
import org.apache.http.util.EncodingUtils;

final class ReadAssetsFile {
    ReadAssetsFile() {
    }

    static String readAssetsFile(AssetManager asset, String file) {
        String res = null;
        try {
            InputStream input_stream = asset.open(file);
            byte[] buffer = new byte[input_stream.available()];
            input_stream.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            input_stream.close();
            return res;
        } catch (IOException e) {
            e.printStackTrace();
            return res;
        }
    }
}
