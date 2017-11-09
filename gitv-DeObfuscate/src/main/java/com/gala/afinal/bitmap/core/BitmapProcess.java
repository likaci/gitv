package com.gala.afinal.bitmap.core;

import android.graphics.Bitmap;
import com.gala.afinal.bitmap.core.BytesBufferPool.BytesBuffer;
import com.gala.afinal.bitmap.download.Downloader;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.imageprovider.base.ImageRequest.ImageType;
import com.gala.imageprovider.base.ImageRequest.ScaleType;
import com.gala.imageprovider.p000private.C0123G;
import com.gala.imageprovider.p000private.C0126b;

public class BitmapProcess {
    private static final BytesBufferPool f18a = new BytesBufferPool(4, 204800);
    private BitmapCache f19a;
    private Downloader f20a;

    public BitmapProcess(Downloader downloader, BitmapCache cache) {
        this.f20a = downloader;
        this.f19a = cache;
    }

    public Bitmap getBitmap(ImageRequest imageRequest, String tag) {
        Bitmap fromDisk = getFromDisk(imageRequest, tag);
        if (fromDisk == null) {
            byte[] download = this.f20a.download(imageRequest.getUrl());
            if (imageRequest.getStopFlag() && imageRequest.getShouldBeKilled()) {
                C0123G.m279a(tag, ">>>>>【afinal】, BitmapProcess ExitTasksEarly [from http], url-" + imageRequest.getUrl());
                return null;
            } else if (download == null || download.length <= 0) {
                return fromDisk;
            } else {
                fromDisk = BitmapDecoder.decodeSampledBitmapFromByteArray(download, 0, download.length, imageRequest.getTargetWidth(), imageRequest.getTargetHeight(), imageRequest);
                if (imageRequest.getTargetWidth() > 0 && imageRequest.getTargetHeight() > 0 && imageRequest.getScaleType() != ScaleType.NO_CROP) {
                    fromDisk = C0126b.m289a(fromDisk, imageRequest.getTargetWidth(), imageRequest.getTargetHeight(), imageRequest.getUrl());
                }
                if (imageRequest.getImageType() == ImageType.ROUND) {
                    fromDisk = C0126b.m288a(fromDisk, imageRequest.getRadius());
                }
                this.f19a.addToDiskCache(imageRequest.getUrl(), download);
                if (fromDisk != null) {
                    if (!C0123G.f541a) {
                        return fromDisk;
                    }
                    C0123G.m279a(tag, ">>>>>【afinal】, bitmap from [HTTP], url: " + imageRequest.getUrl() + "[w/h]-" + fromDisk.getWidth() + "/" + fromDisk.getHeight() + " ,  [reqW/reqH]-" + imageRequest.getTargetWidth() + "/" + imageRequest.getTargetHeight());
                    return fromDisk;
                } else if (!C0123G.f541a || !C0123G.f541a) {
                    return fromDisk;
                } else {
                    C0123G.m282b(tag, ">>>>>【afinal】, bitmap from http, url: " + imageRequest.getUrl());
                    return fromDisk;
                }
            }
        } else if (!C0123G.f541a) {
            return fromDisk;
        } else {
            C0123G.m279a(tag, ">>>>>【afinal】, bitmap from [cache], url: " + imageRequest.getUrl() + "[w/h]-" + fromDisk.getWidth() + "/" + fromDisk.getHeight() + " ,  [reqW/reqH]-" + imageRequest.getTargetWidth() + "/" + imageRequest.getTargetHeight());
            return fromDisk;
        }
    }

    public Bitmap getFromDisk(ImageRequest request, String tag) {
        Bitmap bitmap = null;
        BytesBuffer bytesBuffer = f18a.get();
        try {
            boolean imageData = this.f19a.getImageData(request.getUrl(), bytesBuffer);
            if (request.getStopFlag() && request.getShouldBeKilled()) {
                C0123G.m279a(tag, ">>>>>【afinal】, BitmapProcess ExitTasksEarly [from disk], url-" + request.getUrl());
                return bitmap;
            }
            if (imageData) {
                if (bytesBuffer.length - bytesBuffer.offset > 0) {
                    bitmap = BitmapDecoder.decodeSampledBitmapFromByteArray(bytesBuffer.data, bytesBuffer.offset, bytesBuffer.length, request.getTargetWidth(), request.getTargetHeight(), request);
                }
            }
            f18a.recycle(bytesBuffer);
            if (bitmap != null && request.getTargetWidth() > 0 && request.getTargetHeight() > 0 && request.getScaleType() != ScaleType.NO_CROP) {
                bitmap = C0126b.m289a(bitmap, request.getTargetWidth(), request.getTargetHeight(), request.getUrl());
            }
            if (bitmap == null || request.getImageType() != ImageType.ROUND) {
                return bitmap;
            }
            return C0126b.m288a(bitmap, request.getRadius());
        } finally {
            bitmap = f18a;
            bitmap.recycle(bytesBuffer);
        }
    }
}
