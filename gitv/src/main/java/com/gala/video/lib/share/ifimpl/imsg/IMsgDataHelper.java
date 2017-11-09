package com.gala.video.lib.share.ifimpl.imsg;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.gala.tvapi.tv2.TVApiBase;
import com.gala.video.lib.share.ifimpl.imsg.utils.IMsgUtils.DBColumns;
import com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent;

class IMsgDataHelper {
    private static final String TAG = "imsg/imsgdatahelper";
    private static final IMsgDBOpenHelper mDBHelper = new IMsgDBOpenHelper();

    java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent> getDialogList() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0043 in list []
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
        r12 = this;
        r11 = new java.util.ArrayList;
        r11.<init>();
        r1 = mDBHelper;
        r0 = r1.getReadableDatabase();
        if (r0 == 0) goto L_0x0049;
    L_0x000d:
        r9 = 0;
        r0.beginTransaction();	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r3 = "show=1";	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r1 = "imsg";	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r2 = 0;	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r4 = 0;	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r5 = 0;	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r6 = 0;	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r7 = "_id desc";	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        if (r9 == 0) goto L_0x004d;	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
    L_0x0024:
        r9.moveToFirst();	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
    L_0x0027:
        r1 = r9.isAfterLast();	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        if (r1 != 0) goto L_0x004a;	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
    L_0x002d:
        r8 = r12.query(r9);	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        if (r8 == 0) goto L_0x0036;	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
    L_0x0033:
        r11.add(r8);	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
    L_0x0036:
        r9.moveToNext();	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        goto L_0x0027;
    L_0x003a:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
        if (r9 == 0) goto L_0x0043;
    L_0x0040:
        r9.close();
    L_0x0043:
        r0.setTransactionSuccessful();
        r0.endTransaction();
    L_0x0049:
        return r11;
    L_0x004a:
        r9.close();	 Catch:{ Exception -> 0x003a, all -> 0x0059 }
    L_0x004d:
        if (r9 == 0) goto L_0x0052;
    L_0x004f:
        r9.close();
    L_0x0052:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        goto L_0x0049;
    L_0x0059:
        r1 = move-exception;
        if (r9 == 0) goto L_0x005f;
    L_0x005c:
        r9.close();
    L_0x005f:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.ifimpl.imsg.IMsgDataHelper.getDialogList():java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent>");
    }

    boolean isMsgExist(com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x00c6 in list []
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
        r12 = this;
        r11 = 1;
        r1 = mDBHelper;
        r0 = r1.getReadableDatabase();
        if (r0 == 0) goto L_0x00bb;
    L_0x0009:
        r9 = 0;
        r0.beginTransaction();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1.<init>();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = "MSG_ID=";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = r13.msg_id;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r3 = r1.toString();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1 = "imsg";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r4 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r5 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r6 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r7 = "_id desc";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r9 == 0) goto L_0x005c;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x0033:
        r1 = r9.getCount();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r1 == 0) goto L_0x005c;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x0039:
        r9.moveToFirst();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r8 = r12.query(r9);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r8 == 0) goto L_0x004c;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x0042:
        r1 = "imsg/imsgdatahelper";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = r8.toString();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        android.util.Log.d(r1, r2);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x004c:
        r9.close();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r9 == 0) goto L_0x0054;
    L_0x0051:
        r9.close();
    L_0x0054:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        r1 = r11;
    L_0x005b:
        return r1;
    L_0x005c:
        r1 = r13.msg_type;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = 3;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r1 != r2) goto L_0x00b0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x0061:
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1.<init>();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = "type=3 and aid=";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = r13.related_aids;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r3 = r1.toString();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r1 = "imsg";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r4 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r5 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r6 = 0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r7 = "_id desc";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r9 == 0) goto L_0x00b0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x0087:
        r1 = r9.getCount();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r1 == 0) goto L_0x00b0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x008d:
        r9.moveToFirst();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r8 = r12.query(r9);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r8 == 0) goto L_0x00a0;	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x0096:
        r1 = "imsg/imsgdatahelper";	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        r2 = r8.toString();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        android.util.Log.d(r1, r2);	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
    L_0x00a0:
        r9.close();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r9 == 0) goto L_0x00a8;
    L_0x00a5:
        r9.close();
    L_0x00a8:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        r1 = r11;
        goto L_0x005b;
    L_0x00b0:
        if (r9 == 0) goto L_0x00b5;
    L_0x00b2:
        r9.close();
    L_0x00b5:
        r0.setTransactionSuccessful();
        r0.endTransaction();
    L_0x00bb:
        r1 = 0;
        goto L_0x005b;
    L_0x00bd:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ Exception -> 0x00bd, all -> 0x00cd }
        if (r9 == 0) goto L_0x00c6;
    L_0x00c3:
        r9.close();
    L_0x00c6:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        goto L_0x00bb;
    L_0x00cd:
        r1 = move-exception;
        if (r9 == 0) goto L_0x00d3;
    L_0x00d0:
        r9.close();
    L_0x00d3:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.ifimpl.imsg.IMsgDataHelper.isMsgExist(com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent):boolean");
    }

    java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent> query() {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0041 in list []
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
        r12 = this;
        r11 = new java.util.ArrayList;
        r11.<init>();
        r1 = mDBHelper;
        r0 = r1.getReadableDatabase();
        if (r0 == 0) goto L_0x0047;
    L_0x000d:
        r9 = 0;
        r0.beginTransaction();	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r1 = "imsg";	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r2 = 0;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r3 = 0;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r4 = 0;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r5 = 0;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r6 = 0;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r7 = "_id desc";	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        if (r9 == 0) goto L_0x004b;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
    L_0x0022:
        r9.moveToFirst();	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
    L_0x0025:
        r1 = r9.isAfterLast();	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        if (r1 != 0) goto L_0x0048;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
    L_0x002b:
        r8 = r12.query(r9);	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        if (r8 == 0) goto L_0x0034;	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
    L_0x0031:
        r11.add(r8);	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
    L_0x0034:
        r9.moveToNext();	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        goto L_0x0025;
    L_0x0038:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
        if (r9 == 0) goto L_0x0041;
    L_0x003e:
        r9.close();
    L_0x0041:
        r0.setTransactionSuccessful();
        r0.endTransaction();
    L_0x0047:
        return r11;
    L_0x0048:
        r9.close();	 Catch:{ Exception -> 0x0038, all -> 0x0057 }
    L_0x004b:
        if (r9 == 0) goto L_0x0050;
    L_0x004d:
        r9.close();
    L_0x0050:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        goto L_0x0047;
    L_0x0057:
        r1 = move-exception;
        if (r9 == 0) goto L_0x005d;
    L_0x005a:
        r9.close();
    L_0x005d:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.ifimpl.imsg.IMsgDataHelper.query():java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent>");
    }

    java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent> query(int r13) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Can't find block by offset: 0x0054 in list []
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
        r12 = this;
        r11 = new java.util.ArrayList;
        r11.<init>();
        r1 = mDBHelper;
        r0 = r1.getReadableDatabase();
        if (r0 == 0) goto L_0x005a;
    L_0x000d:
        r9 = 0;
        r0.beginTransaction();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r1 = new java.lang.StringBuilder;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r1.<init>();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r2 = "type=";	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r1 = r1.append(r2);	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r1 = r1.append(r13);	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r3 = r1.toString();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r1 = "imsg";	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r2 = 0;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r4 = 0;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r5 = 0;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r6 = 0;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r7 = "_id desc";	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        r9 = r0.query(r1, r2, r3, r4, r5, r6, r7);	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        if (r9 == 0) goto L_0x005e;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
    L_0x0035:
        r9.moveToFirst();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
    L_0x0038:
        r1 = r9.isAfterLast();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        if (r1 != 0) goto L_0x005b;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
    L_0x003e:
        r8 = r12.query(r9);	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        if (r8 == 0) goto L_0x0047;	 Catch:{ Exception -> 0x004b, all -> 0x006a }
    L_0x0044:
        r11.add(r8);	 Catch:{ Exception -> 0x004b, all -> 0x006a }
    L_0x0047:
        r9.moveToNext();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        goto L_0x0038;
    L_0x004b:
        r10 = move-exception;
        r10.printStackTrace();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
        if (r9 == 0) goto L_0x0054;
    L_0x0051:
        r9.close();
    L_0x0054:
        r0.setTransactionSuccessful();
        r0.endTransaction();
    L_0x005a:
        return r11;
    L_0x005b:
        r9.close();	 Catch:{ Exception -> 0x004b, all -> 0x006a }
    L_0x005e:
        if (r9 == 0) goto L_0x0063;
    L_0x0060:
        r9.close();
    L_0x0063:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        goto L_0x005a;
    L_0x006a:
        r1 = move-exception;
        if (r9 == 0) goto L_0x0070;
    L_0x006d:
        r9.close();
    L_0x0070:
        r0.setTransactionSuccessful();
        r0.endTransaction();
        throw r1;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.video.lib.share.ifimpl.imsg.IMsgDataHelper.query(int):java.util.List<com.gala.video.lib.share.ifmanager.bussnessIF.imsg.IMsgContent>");
    }

    IMsgDataHelper() {
    }

    int getUnreadMsgCount() {
        int count = 0;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        if (db != null) {
            Cursor cursor = null;
            try {
                cursor = db.query("imsg", null, "read=0 and valid_till>" + TVApiBase.getTVApiProperty().getCurrentTime(), null, null, null, null);
                if (cursor != null) {
                    count = cursor.getCount();
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Throwable th) {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return count;
    }

    private IMsgContent query(Cursor cursor) {
        boolean z = true;
        IMsgContent content = new IMsgContent();
        content.valid_till = cursor.getLong(30);
        if (content.valid_till < TVApiBase.getTVApiProperty().getCurrentTime()) {
            return null;
        }
        boolean z2;
        content.msg_template_id = cursor.getInt(1);
        content.msg_title = cursor.getString(2);
        content.msg_level = cursor.getInt(3);
        content.msg_type = cursor.getInt(4);
        content.min_support_version = cursor.getString(5);
        content.pic_url = cursor.getString(6);
        content.description = cursor.getString(7);
        content.button_name = cursor.getString(8);
        content.is_detailpage = cursor.getInt(9);
        content.page_jumping = cursor.getInt(10);
        content.url = cursor.getString(11);
        content.related_plids = cursor.getString(12);
        content.related_aids = cursor.getString(13);
        content.related_vids = cursor.getString(14);
        if (cursor.getInt(15) == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        content.isRead = z2;
        content.tv_type = cursor.getInt(16);
        if (cursor.getInt(17) == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        content.isSeries = z2;
        content.sourceCode = cursor.getString(18);
        content.channelId = cursor.getInt(19);
        content.msg_id = cursor.getInt(21);
        content.localTime = cursor.getLong(22);
        content.album = cursor.getString(23);
        if (cursor.getInt(24) != 1) {
            z = false;
        }
        content.isShowDialog = z;
        content.content = cursor.getString(25);
        content.style = cursor.getInt(26);
        content.url_window = cursor.getString(27);
        content.coupon_key = cursor.getString(28);
        content.coupon_sign = cursor.getString(29);
        return content;
    }

    void insert(IMsgContent content) {
        if (content != null) {
            int isRead;
            int isSeries;
            int isShow;
            ContentValues initialValues = new ContentValues();
            initialValues.put(DBColumns.TEMPLATE_ID, Integer.valueOf(content.msg_template_id));
            initialValues.put("title", content.msg_title);
            initialValues.put(DBColumns.LEVEL, Integer.valueOf(content.msg_level));
            initialValues.put("type", Integer.valueOf(content.msg_type));
            initialValues.put("version", content.min_support_version);
            initialValues.put(DBColumns.PIC, content.pic_url);
            initialValues.put(DBColumns.DESCRIPTION, content.description);
            initialValues.put(DBColumns.BUTTON_NAME, content.button_name);
            initialValues.put("detail", Integer.valueOf(content.is_detailpage));
            initialValues.put("jump", Integer.valueOf(content.page_jumping));
            initialValues.put("url", content.url);
            initialValues.put("plid", content.related_plids);
            initialValues.put("aid", content.related_aids);
            initialValues.put("tvid", content.related_vids);
            if (content.isRead) {
                isRead = 1;
            } else {
                isRead = 0;
            }
            initialValues.put("read", Integer.valueOf(isRead));
            initialValues.put(DBColumns.TV_TYPE, Integer.valueOf(content.tv_type));
            if (content.isSeries) {
                isSeries = 1;
            } else {
                isSeries = 0;
            }
            initialValues.put("series", Integer.valueOf(isSeries));
            initialValues.put("source", content.sourceCode);
            initialValues.put("channel", Integer.valueOf(content.channelId));
            initialValues.put(DBColumns.MSG_ID, Integer.valueOf(content.msg_id));
            initialValues.put(DBColumns.LOCAL_TIME, Long.valueOf(content.localTime));
            initialValues.put("album", content.album);
            if (content.isShowDialog) {
                isShow = 1;
            } else {
                isShow = 0;
            }
            initialValues.put(DBColumns.IS_NEED_SHOW, Integer.valueOf(isShow));
            initialValues.put(DBColumns.CONTENT, content.content);
            initialValues.put("style", Integer.valueOf(content.style));
            initialValues.put(DBColumns.URL_WINDOW, content.url_window);
            initialValues.put(DBColumns.COUPON_KEY, content.coupon_key);
            initialValues.put(DBColumns.COUPON_SIGN, content.coupon_sign);
            initialValues.put(DBColumns.VALID_TILL, Long.valueOf(content.valid_till));
            SQLiteDatabase db = mDBHelper.getWritableDatabase();
            if (db != null) {
                try {
                    db.beginTransaction();
                    db.insert("imsg", null, initialValues);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    db.setTransactionSuccessful();
                    db.endTransaction();
                }
            }
        }
    }

    void setIsRead(String where, String[] whereArgs) {
        ContentValues initialvalues = new ContentValues();
        initialvalues.put("read", Integer.valueOf(1));
        initialvalues.put(DBColumns.IS_NEED_SHOW, Integer.valueOf(0));
        update(initialvalues, where, whereArgs);
    }

    void setIsNeedShow(String where, String[] whereArgs, int isNeedShow) {
        ContentValues initialvalues = new ContentValues();
        initialvalues.put(DBColumns.IS_NEED_SHOW, Integer.valueOf(isNeedShow));
        update(initialvalues, where, whereArgs);
    }

    void update(ContentValues initialValues, String where, String[] whereArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db != null) {
            try {
                db.update("imsg", initialValues, where, whereArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void delete(String where, String[] whereArgs) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        if (db != null) {
            try {
                db.delete("imsg", where, whereArgs);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
