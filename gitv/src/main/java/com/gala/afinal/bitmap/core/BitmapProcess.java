package com.gala.afinal.bitmap.core;

import android.graphics.Bitmap;
import com.gala.afinal.bitmap.core.BytesBufferPool.BytesBuffer;
import com.gala.afinal.bitmap.download.Downloader;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ImageType;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.imageprovider.private.G;
import com.gala.imageprovider.private.b;

public class BitmapProcess {
    private static final BytesBufferPool a = new BytesBufferPool(4, 204800);
    private BitmapCache f5a;
    private Downloader f6a;

    public BitmapProcess(Downloader downloader, BitmapCache cache) {
        this.f6a = downloader;
        this.f5a = cache;
    }

    public Bitmap getBitmap(ImageRequest imageRequest, String tag) {
        Bitmap fromDisk = getFromDisk(imageRequest, tag);
        if (fromDisk == null) {
            byte[] download = this.f6a.download(imageRequest.getUrl());
            if (imageRequest.getStopFlag() && imageRequest.getShouldBeKilled()) {
                G.a(tag, ">>>>>【afinal】, BitmapProcess ExitTasksEarly [from http], url-" + imageRequest.getUrl());
                return null;
            } else if (download == null || download.length <= 0) {
                return fromDisk;
            } else {
                fromDisk = BitmapDecoder.decodeSampledBitmapFromByteArray(download, 0, download.length, imageRequest.getTargetWidth(), imageRequest.getTargetHeight(), imageRequest);
                if (imageRequest.getTargetWidth() > 0 && imageRequest.getTargetHeight() > 0 && imageRequest.getScaleType() != ScaleType.NO_CROP) {
                    fromDisk = b.a(fromDisk, imageRequest.getTargetWidth(), imageRequest.getTargetHeight(), imageRequest.getUrl());
                }
                if (imageRequest.getImageType() == ImageType.ROUND) {
                    fromDisk = b.a(fromDisk, imageRequest.getRadius());
                }
                this.f5a.addToDiskCache(imageRequest.getUrl(), download);
                if (fromDisk != null) {
                    if (!G.a) {
                        return fromDisk;
                    }
                    G.a(tag, ">>>>>【afinal】, bitmap from [HTTP], url: " + imageRequest.getUrl() + "[w/h]-" + fromDisk.getWidth() + "/" + fromDisk.getHeight() + " ,  [reqW/reqH]-" + imageRequest.getTargetWidth() + "/" + imageRequest.getTargetHeight());
                    return fromDisk;
                } else if (!G.a || !G.a) {
                    return fromDisk;
                } else {
                    G.b(tag, ">>>>>【afinal】, bitmap from http, url: " + imageRequest.getUrl());
                    return fromDisk;
                }
            }
        } else if (!G.a) {
            return fromDisk;
        } else {
            G.a(tag, ">>>>>【afinal】, bitmap from [cache], url: " + imageRequest.getUrl() + "[w/h]-" + fromDisk.getWidth() + "/" + fromDisk.getHeight() + " ,  [reqW/reqH]-" + imageRequest.getTargetWidth() + "/" + imageRequest.getTargetHeight());
            return fromDisk;
        }
    }

    public Bitmap getFromDisk(ImageRequest request, String tag) {
        Bitmap bitmap = null;
        BytesBuffer bytesBuffer = a.get();
        try {
            boolean imageData = this.f5a.getImageData(request.getUrl(), bytesBuffer);
            if (request.getStopFlag() && request.getShouldBeKilled()) {
                G.a(tag, ">>>>>【afinal】, BitmapProcess ExitTasksEarly [from disk], url-" + request.getUrl());
                return bitmap;
            }
            if (imageData) {
                if (bytesBuffer.length - bytesBuffer.offset > 0) {
                    bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(bytesBuffer.data, bytesBuffer.offset, bytesBuffer.length, request.getTargetWidth(), request.getTargetHeight(), request);
                }
            }
            a.recycle(bytesBuffer);
            if (bitmap != null && request.getTargetWidth() > 0 && request.getTargetHeight() > 0 && request.getScaleType() != ScaleType.NO_CROP) {
                bitmap = b.a(bitmap, request.getTargetWidth(), request.getTargetHeight(), request.getUrl());
            }
            if (bitmap == null || request.getImageType() != ImageType.ROUND) {
                return bitmap;
            }
            return b.a(bitmap, request.getRadius());
        } finally {
            bitmap = a;
            bitmap.recycle(bytesBuffer);
        }
    }
}
