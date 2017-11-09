package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Build.VERSION;
import java.io.Closeable;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

class ReLinker {
    private static final int COPY_BUFFER_SIZE = 8192;
    private static final String LIB_DIR = "lib";
    private static final String MAPPED_BASE_LIB_NAME = System.mapLibraryName("gala_gif");
    private static final int MAX_TRIES = 5;

    private ReLinker() {
    }

    @SuppressLint({"UnsafeDynamicallyLoadedCode"})
    static void loadLibrary(Context context) {
        synchronized (ReLinker.class) {
            System.load(unpackLibrary(context).getAbsolutePath());
        }
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.io.File unpackLibrary(android.content.Context r8) {
        /*
        r2 = 0;
        r3 = 0;
        r4 = MAPPED_BASE_LIB_NAME;
        r0 = new java.io.File;
        r1 = "lib";
        r1 = r8.getDir(r1, r2);
        r0.<init>(r1, r4);
        r1 = r0.isFile();
        if (r1 == 0) goto L_0x0017;
    L_0x0016:
        return r0;
    L_0x0017:
        r1 = new java.io.File;
        r5 = r8.getCacheDir();
        r1.<init>(r5, r4);
        r4 = r1.isFile();
        if (r4 == 0) goto L_0x0028;
    L_0x0026:
        r0 = r1;
        goto L_0x0016;
    L_0x0028:
        r4 = "pl_droidsonroids_gif_surface";
        r4 = java.lang.System.mapLibraryName(r4);
        r5 = new pl.droidsonroids.gif.ReLinker$1;
        r5.<init>(r4);
        clearOldLibraryFiles(r0, r5);
        clearOldLibraryFiles(r1, r5);
        r4 = r8.getApplicationInfo();
        r5 = new java.io.File;
        r4 = r4.sourceDir;
        r5.<init>(r4);
        r5 = openZipFile(r5);	 Catch:{ all -> 0x00b3 }
    L_0x0049:
        r6 = r2 + 1;
        r4 = 5;
        if (r2 >= r4) goto L_0x0090;
    L_0x004e:
        r2 = findLibraryEntry(r5);	 Catch:{ all -> 0x0073 }
        if (r2 != 0) goto L_0x007b;
    L_0x0054:
        r0 = new java.lang.IllegalStateException;	 Catch:{ all -> 0x0073 }
        r1 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0073 }
        r2 = "Library ";
        r1.<init>(r2);	 Catch:{ all -> 0x0073 }
        r2 = MAPPED_BASE_LIB_NAME;	 Catch:{ all -> 0x0073 }
        r1 = r1.append(r2);	 Catch:{ all -> 0x0073 }
        r2 = " for supported ABIs not found in APK file";
        r1 = r1.append(r2);	 Catch:{ all -> 0x0073 }
        r1 = r1.toString();	 Catch:{ all -> 0x0073 }
        r0.<init>(r1);	 Catch:{ all -> 0x0073 }
        throw r0;	 Catch:{ all -> 0x0073 }
    L_0x0073:
        r0 = move-exception;
        r3 = r5;
    L_0x0075:
        if (r3 == 0) goto L_0x007a;
    L_0x0077:
        r3.close();	 Catch:{ IOException -> 0x00b1 }
    L_0x007a:
        throw r0;
    L_0x007b:
        r4 = r5.getInputStream(r2);	 Catch:{ IOException -> 0x0099, all -> 0x00a8 }
        r2 = new java.io.FileOutputStream;	 Catch:{ IOException -> 0x00ba, all -> 0x00b5 }
        r2.<init>(r0);	 Catch:{ IOException -> 0x00ba, all -> 0x00b5 }
        copy(r4, r2);	 Catch:{ IOException -> 0x00bd, all -> 0x00b7 }
        closeSilently(r4);	 Catch:{ all -> 0x0073 }
        closeSilently(r2);	 Catch:{ all -> 0x0073 }
        setFilePermissions(r0);	 Catch:{ all -> 0x0073 }
    L_0x0090:
        if (r5 == 0) goto L_0x0016;
    L_0x0092:
        r5.close();	 Catch:{ IOException -> 0x0096 }
        goto L_0x0016;
    L_0x0096:
        r1 = move-exception;
        goto L_0x0016;
    L_0x0099:
        r2 = move-exception;
        r2 = r3;
        r4 = r3;
    L_0x009c:
        r7 = 2;
        if (r6 <= r7) goto L_0x00a0;
    L_0x009f:
        r0 = r1;
    L_0x00a0:
        closeSilently(r4);	 Catch:{ all -> 0x0073 }
        closeSilently(r2);	 Catch:{ all -> 0x0073 }
        r2 = r6;
        goto L_0x0049;
    L_0x00a8:
        r0 = move-exception;
        r4 = r3;
    L_0x00aa:
        closeSilently(r4);	 Catch:{ all -> 0x0073 }
        closeSilently(r3);	 Catch:{ all -> 0x0073 }
        throw r0;	 Catch:{ all -> 0x0073 }
    L_0x00b1:
        r1 = move-exception;
        goto L_0x007a;
    L_0x00b3:
        r0 = move-exception;
        goto L_0x0075;
    L_0x00b5:
        r0 = move-exception;
        goto L_0x00aa;
    L_0x00b7:
        r0 = move-exception;
        r3 = r2;
        goto L_0x00aa;
    L_0x00ba:
        r2 = move-exception;
        r2 = r3;
        goto L_0x009c;
    L_0x00bd:
        r7 = move-exception;
        goto L_0x009c;
        */
        throw new UnsupportedOperationException("Method not decompiled: pl.droidsonroids.gif.ReLinker.unpackLibrary(android.content.Context):java.io.File");
    }

    private static ZipEntry findLibraryEntry(ZipFile zipFile) {
        for (String entry : getSupportedABIs()) {
            ZipEntry entry2 = getEntry(zipFile, entry);
            if (entry2 != null) {
                return entry2;
            }
        }
        return getEntry(zipFile, Build.CPU_ABI);
    }

    @SuppressLint({"NewApi"})
    private static String[] getSupportedABIs() {
        if (VERSION.SDK_INT >= 21) {
            return Build.SUPPORTED_ABIS;
        }
        return new String[]{Build.CPU_ABI, Build.CPU_ABI2};
    }

    private static ZipEntry getEntry(ZipFile zipFile, String abi) {
        return zipFile.getEntry("lib/" + abi + "/" + MAPPED_BASE_LIB_NAME);
    }

    private static ZipFile openZipFile(File apkFile) {
        ZipFile zipFile;
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (i >= 5) {
                break;
            }
            try {
                zipFile = new ZipFile(apkFile, 1);
                break;
            } catch (IOException e) {
                i = i2;
            }
        }
        zipFile = null;
        if (zipFile != null) {
            return zipFile;
        }
        throw new IllegalStateException("Could not open APK file: " + apkFile.getAbsolutePath());
    }

    private static void clearOldLibraryFiles(File outputFile, FilenameFilter filter) {
        File[] listFiles = outputFile.getParentFile().listFiles(filter);
        if (listFiles != null) {
            for (File delete : listFiles) {
                delete.delete();
            }
        }
    }

    @SuppressLint({"SetWorldReadable"})
    private static void setFilePermissions(File outputFile) {
        outputFile.setReadable(true, false);
        outputFile.setExecutable(true, false);
        outputFile.setWritable(true);
    }

    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] bArr = new byte[8192];
        while (true) {
            int read = in.read(bArr);
            if (read != -1) {
                out.write(bArr, 0, read);
            } else {
                return;
            }
        }
    }

    private static void closeSilently(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }
}
