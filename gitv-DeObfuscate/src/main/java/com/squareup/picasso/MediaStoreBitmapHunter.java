package com.squareup.picasso;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.provider.MediaStore.Images;
import android.provider.MediaStore.Video.Thumbnails;
import java.io.IOException;

class MediaStoreBitmapHunter extends ContentStreamBitmapHunter {
    private static final String[] CONTENT_ORIENTATION = new String[]{"orientation"};

    enum PicassoKind {
        MICRO(3, 96, 96),
        MINI(1, 512, 384),
        FULL(2, -1, -1);
        
        final int androidKind;
        final int height;
        final int width;

        private PicassoKind(int androidKind, int width, int height) {
            this.androidKind = androidKind;
            this.width = width;
            this.height = height;
        }
    }

    static int getExifOrientation(android.content.ContentResolver r9, android.net.Uri r10) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x002d in list []
	at jadx.core.utils.BlockUtils.getBlockByOffset(BlockUtils.java:42)
	at jadx.core.dex.instructions.IfNode.initBlocks(IfNode.java:60)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.initBlocksInIfNodes(BlockFinish.java:48)
	at jadx.core.dex.visitors.blocksmaker.BlockFinish.visit(BlockFinish.java:33)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r8 = 0;
        r6 = 0;
        r2 = CONTENT_ORIENTATION;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r3 = 0;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r4 = 0;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r5 = 0;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r0 = r9;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r1 = r10;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        r6 = r0.query(r1, r2, r3, r4, r5);	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        if (r6 == 0) goto L_0x0015;	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
    L_0x000f:
        r0 = r6.moveToFirst();	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        if (r0 != 0) goto L_0x001c;
    L_0x0015:
        if (r6 == 0) goto L_0x001a;
    L_0x0017:
        r6.close();
    L_0x001a:
        r0 = r8;
    L_0x001b:
        return r0;
    L_0x001c:
        r0 = 0;
        r0 = r6.getInt(r0);	 Catch:{ RuntimeException -> 0x0027, all -> 0x002f }
        if (r6 == 0) goto L_0x001b;
    L_0x0023:
        r6.close();
        goto L_0x001b;
    L_0x0027:
        r7 = move-exception;
        if (r6 == 0) goto L_0x002d;
    L_0x002a:
        r6.close();
    L_0x002d:
        r0 = r8;
        goto L_0x001b;
    L_0x002f:
        r0 = move-exception;
        if (r6 == 0) goto L_0x0035;
    L_0x0032:
        r6.close();
    L_0x0035:
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.picasso.MediaStoreBitmapHunter.getExifOrientation(android.content.ContentResolver, android.net.Uri):int");
    }

    MediaStoreBitmapHunter(Context context, Picasso picasso, Dispatcher dispatcher, Cache cache, Stats stats, Action action) {
        super(context, picasso, dispatcher, cache, stats, action);
    }

    Bitmap decode(Request data) throws IOException {
        ContentResolver contentResolver = this.context.getContentResolver();
        setExifRotation(getExifOrientation(contentResolver, data.uri));
        String mimeType = contentResolver.getType(data.uri);
        boolean isVideo = mimeType != null && mimeType.startsWith("video/");
        if (data.hasSize()) {
            PicassoKind picassoKind = getPicassoKind(data.targetWidth, data.targetHeight);
            if (!isVideo && picassoKind == PicassoKind.FULL) {
                return super.decode(data);
            }
            Bitmap result;
            long id = ContentUris.parseId(data.uri);
            Options options = BitmapHunter.createBitmapOptions(data);
            options.inJustDecodeBounds = true;
            BitmapHunter.calculateInSampleSize(data.targetWidth, data.targetHeight, picassoKind.width, picassoKind.height, options);
            if (isVideo) {
                result = Thumbnails.getThumbnail(contentResolver, id, picassoKind == PicassoKind.FULL ? 1 : picassoKind.androidKind, options);
            } else {
                result = Images.Thumbnails.getThumbnail(contentResolver, id, picassoKind.androidKind, options);
            }
            if (result != null) {
                return result;
            }
        }
        return super.decode(data);
    }

    static PicassoKind getPicassoKind(int targetWidth, int targetHeight) {
        if (targetWidth <= PicassoKind.MICRO.width && targetHeight <= PicassoKind.MICRO.height) {
            return PicassoKind.MICRO;
        }
        if (targetWidth > PicassoKind.MINI.width || targetHeight > PicassoKind.MINI.height) {
            return PicassoKind.FULL;
        }
        return PicassoKind.MINI;
    }
}
