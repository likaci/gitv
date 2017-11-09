package com.tvos.apps.utils;

public class RarUtil {
    private static final String TAG = "RarUtil";

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean UnRarFolder(java.io.File r16, java.lang.String r17, boolean r18) {
        /*
        r3 = new java.io.File;
        r0 = r17;
        r3.<init>(r0);
        r8 = 1;
        r14 = r3.exists();
        if (r14 != 0) goto L_0x0011;
    L_0x000e:
        r3.mkdirs();
    L_0x0011:
        r1 = 0;
        r2 = new de.innosystec.unrar.Archive;	 Catch:{ Exception -> 0x0106 }
        r0 = r16;
        r2.<init>(r0);	 Catch:{ Exception -> 0x0106 }
        if (r2 == 0) goto L_0x010e;
    L_0x001b:
        r5 = r2.nextFileHeader();	 Catch:{ Exception -> 0x00ec }
    L_0x001f:
        if (r5 != 0) goto L_0x0026;
    L_0x0021:
        r2.close();	 Catch:{ Exception -> 0x00ec }
        r1 = r2;
    L_0x0025:
        return r8;
    L_0x0026:
        r14 = r5.getFileNameString();	 Catch:{ Exception -> 0x00ec }
        r6 = r14.trim();	 Catch:{ Exception -> 0x00ec }
        r14 = "\\\\";
        r15 = "/";
        r6 = r6.replaceAll(r14, r15);	 Catch:{ Exception -> 0x00ec }
        r14 = r5.isDirectory();	 Catch:{ Exception -> 0x00ec }
        if (r14 == 0) goto L_0x0062;
    L_0x003e:
        r7 = new java.io.File;	 Catch:{ Exception -> 0x00ec }
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ec }
        r15 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x00ec }
        r14.<init>(r15);	 Catch:{ Exception -> 0x00ec }
        r15 = java.io.File.separator;	 Catch:{ Exception -> 0x00ec }
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x00ec }
        r14 = r14.append(r6);	 Catch:{ Exception -> 0x00ec }
        r14 = r14.toString();	 Catch:{ Exception -> 0x00ec }
        r7.<init>(r14);	 Catch:{ Exception -> 0x00ec }
        r7.mkdirs();	 Catch:{ Exception -> 0x00ec }
    L_0x005d:
        r5 = r2.nextFileHeader();	 Catch:{ Exception -> 0x00ec }
        goto L_0x001f;
    L_0x0062:
        r11 = new java.io.File;	 Catch:{ Exception -> 0x00ec }
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00ec }
        r15 = java.lang.String.valueOf(r17);	 Catch:{ Exception -> 0x00ec }
        r14.<init>(r15);	 Catch:{ Exception -> 0x00ec }
        r15 = java.io.File.separator;	 Catch:{ Exception -> 0x00ec }
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x00ec }
        r14 = r14.append(r6);	 Catch:{ Exception -> 0x00ec }
        r14 = r14.toString();	 Catch:{ Exception -> 0x00ec }
        r11.<init>(r14);	 Catch:{ Exception -> 0x00ec }
        r9 = 0;
        r14 = r11.exists();	 Catch:{ Exception -> 0x00f4 }
        if (r14 == 0) goto L_0x0088;
    L_0x0085:
        r11.delete();	 Catch:{ Exception -> 0x00f4 }
    L_0x0088:
        r12 = r11.getParentFile();	 Catch:{ Exception -> 0x00f4 }
        r14 = r12.exists();	 Catch:{ Exception -> 0x00f4 }
        if (r14 != 0) goto L_0x009b;
    L_0x0092:
        r14 = r12.isDirectory();	 Catch:{ Exception -> 0x00f4 }
        if (r14 == 0) goto L_0x009b;
    L_0x0098:
        r12.mkdirs();	 Catch:{ Exception -> 0x00f4 }
    L_0x009b:
        r11.createNewFile();	 Catch:{ Exception -> 0x00f4 }
        r10 = new java.io.FileOutputStream;	 Catch:{ Exception -> 0x00f4 }
        r10.<init>(r11);	 Catch:{ Exception -> 0x00f4 }
        r2.extractFile(r5, r10);	 Catch:{ Exception -> 0x010b, all -> 0x0108 }
        if (r10 == 0) goto L_0x00ab;
    L_0x00a8:
        r10.close();	 Catch:{ Exception -> 0x00ec }
    L_0x00ab:
        r9 = r10;
    L_0x00ac:
        if (r18 == 0) goto L_0x005d;
    L_0x00ae:
        r14 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00e6 }
        r15 = "chmod 666 ";
        r14.<init>(r15);	 Catch:{ Exception -> 0x00e6 }
        r0 = r17;
        r14 = r14.append(r0);	 Catch:{ Exception -> 0x00e6 }
        r15 = java.io.File.separator;	 Catch:{ Exception -> 0x00e6 }
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x00e6 }
        r15 = r5.getFileNameString();	 Catch:{ Exception -> 0x00e6 }
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x00e6 }
        r15 = "\n";
        r14 = r14.append(r15);	 Catch:{ Exception -> 0x00e6 }
        r13 = r14.toString();	 Catch:{ Exception -> 0x00e6 }
        r14 = com.tvos.apps.utils.RootCmdUtils.runCommand(r13);	 Catch:{ Exception -> 0x00e6 }
        if (r14 != 0) goto L_0x005d;
    L_0x00db:
        r14 = "RarUtil";
        r15 = "no permission";
        android.util.Log.d(r14, r15);	 Catch:{ Exception -> 0x00e6 }
        goto L_0x005d;
    L_0x00e6:
        r4 = move-exception;
        r4.printStackTrace();	 Catch:{ Exception -> 0x00ec }
        goto L_0x005d;
    L_0x00ec:
        r4 = move-exception;
        r1 = r2;
    L_0x00ee:
        r8 = 0;
        r4.printStackTrace();
        goto L_0x0025;
    L_0x00f4:
        r4 = move-exception;
    L_0x00f5:
        r8 = 0;
        r4.printStackTrace();	 Catch:{ all -> 0x00ff }
        if (r9 == 0) goto L_0x00ac;
    L_0x00fb:
        r9.close();	 Catch:{ Exception -> 0x00ec }
        goto L_0x00ac;
    L_0x00ff:
        r14 = move-exception;
    L_0x0100:
        if (r9 == 0) goto L_0x0105;
    L_0x0102:
        r9.close();	 Catch:{ Exception -> 0x00ec }
    L_0x0105:
        throw r14;	 Catch:{ Exception -> 0x00ec }
    L_0x0106:
        r4 = move-exception;
        goto L_0x00ee;
    L_0x0108:
        r14 = move-exception;
        r9 = r10;
        goto L_0x0100;
    L_0x010b:
        r4 = move-exception;
        r9 = r10;
        goto L_0x00f5;
    L_0x010e:
        r1 = r2;
        goto L_0x0025;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.tvos.apps.utils.RarUtil.UnRarFolder(java.io.File, java.lang.String, boolean):boolean");
    }
}
