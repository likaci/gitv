package com.gala.tvapi.p023c;

import com.gala.multiscreen.dmr.model.MSMessage.RemoteCode;
import java.io.UnsupportedEncodingException;

public class C0253a {
    private static /* synthetic */ boolean f904a = (!C0253a.class.desiredAssertionStatus());

    static abstract class C0250a {
        public int f889a;
        public byte[] f890a;

        C0250a() {
        }
    }

    static class C0251b extends C0250a {
        private static final int[] f891a = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, -1, 63, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, -1, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        private int f892b = 0;
        private final int[] f893b = f891a;

        static {
            int[] iArr = new int[]{-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 62, -1, -1, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, -1, -1, -1, -2, -1, -1, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, -1, -1, -1, -1, 63, -1, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        }

        public C0251b(byte[] bArr) {
            this.a = bArr;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean m612a(byte[] r13, int r14) {
            /*
            r12 = this;
            r11 = -2;
            r10 = -1;
            r9 = 6;
            r3 = 0;
            r0 = r12.f892b;
            if (r0 != r9) goto L_0x000a;
        L_0x0008:
            r0 = r3;
        L_0x0009:
            return r0;
        L_0x000a:
            r0 = r12.f892b;
            r6 = r12.a;
            r7 = r12.f893b;
            r1 = r3;
            r2 = r3;
            r5 = r0;
            r0 = r3;
        L_0x0014:
            if (r2 >= r14) goto L_0x00f6;
        L_0x0016:
            if (r5 != 0) goto L_0x005d;
        L_0x0018:
            r4 = r2 + 4;
            if (r4 > r14) goto L_0x005b;
        L_0x001c:
            r1 = r13[r2];
            r1 = r1 & 255;
            r1 = r7[r1];
            r1 = r1 << 18;
            r4 = r2 + 1;
            r4 = r13[r4];
            r4 = r4 & 255;
            r4 = r7[r4];
            r4 = r4 << 12;
            r1 = r1 | r4;
            r4 = r2 + 2;
            r4 = r13[r4];
            r4 = r4 & 255;
            r4 = r7[r4];
            r4 = r4 << 6;
            r1 = r1 | r4;
            r4 = r2 + 3;
            r4 = r13[r4];
            r4 = r4 & 255;
            r4 = r7[r4];
            r1 = r1 | r4;
            if (r1 < 0) goto L_0x005b;
        L_0x0045:
            r4 = r0 + 2;
            r8 = (byte) r1;
            r6[r4] = r8;
            r4 = r0 + 1;
            r8 = r1 >> 8;
            r8 = (byte) r8;
            r6[r4] = r8;
            r4 = r1 >> 16;
            r4 = (byte) r4;
            r6[r0] = r4;
            r0 = r0 + 3;
            r2 = r2 + 4;
            goto L_0x0018;
        L_0x005b:
            if (r2 >= r14) goto L_0x00f6;
        L_0x005d:
            r4 = r2 + 1;
            r2 = r13[r2];
            r2 = r2 & 255;
            r2 = r7[r2];
            switch(r5) {
                case 0: goto L_0x006a;
                case 1: goto L_0x0078;
                case 2: goto L_0x0088;
                case 3: goto L_0x00a8;
                case 4: goto L_0x00e0;
                case 5: goto L_0x00ef;
                default: goto L_0x0068;
            };
        L_0x0068:
            r2 = r4;
            goto L_0x0014;
        L_0x006a:
            if (r2 < 0) goto L_0x0072;
        L_0x006c:
            r1 = r5 + 1;
            r5 = r1;
            r1 = r2;
            r2 = r4;
            goto L_0x0014;
        L_0x0072:
            if (r2 == r10) goto L_0x0068;
        L_0x0074:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
        L_0x0078:
            if (r2 < 0) goto L_0x0082;
        L_0x007a:
            r1 = r1 << 6;
            r1 = r1 | r2;
            r2 = r5 + 1;
            r5 = r2;
            r2 = r4;
            goto L_0x0014;
        L_0x0082:
            if (r2 == r10) goto L_0x0068;
        L_0x0084:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
        L_0x0088:
            if (r2 < 0) goto L_0x0092;
        L_0x008a:
            r1 = r1 << 6;
            r1 = r1 | r2;
            r2 = r5 + 1;
            r5 = r2;
            r2 = r4;
            goto L_0x0014;
        L_0x0092:
            if (r2 != r11) goto L_0x00a1;
        L_0x0094:
            r2 = r0 + 1;
            r5 = r1 >> 4;
            r5 = (byte) r5;
            r6[r0] = r5;
            r0 = 4;
            r5 = r0;
            r0 = r2;
            r2 = r4;
            goto L_0x0014;
        L_0x00a1:
            if (r2 == r10) goto L_0x0068;
        L_0x00a3:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
        L_0x00a8:
            if (r2 < 0) goto L_0x00c4;
        L_0x00aa:
            r1 = r1 << 6;
            r1 = r1 | r2;
            r2 = r0 + 2;
            r5 = (byte) r1;
            r6[r2] = r5;
            r2 = r0 + 1;
            r5 = r1 >> 8;
            r5 = (byte) r5;
            r6[r2] = r5;
            r2 = r1 >> 16;
            r2 = (byte) r2;
            r6[r0] = r2;
            r0 = r0 + 3;
            r2 = r4;
            r5 = r3;
            goto L_0x0014;
        L_0x00c4:
            if (r2 != r11) goto L_0x00d9;
        L_0x00c6:
            r2 = r0 + 1;
            r5 = r1 >> 2;
            r5 = (byte) r5;
            r6[r2] = r5;
            r2 = r1 >> 10;
            r2 = (byte) r2;
            r6[r0] = r2;
            r0 = r0 + 2;
            r2 = 5;
            r5 = r2;
            r2 = r4;
            goto L_0x0014;
        L_0x00d9:
            if (r2 == r10) goto L_0x0068;
        L_0x00db:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
        L_0x00e0:
            if (r2 != r11) goto L_0x00e8;
        L_0x00e2:
            r2 = r5 + 1;
            r5 = r2;
            r2 = r4;
            goto L_0x0014;
        L_0x00e8:
            if (r2 == r10) goto L_0x0068;
        L_0x00ea:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
        L_0x00ef:
            if (r2 == r10) goto L_0x0068;
        L_0x00f1:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
        L_0x00f6:
            r2 = r1;
            switch(r5) {
                case 0: goto L_0x00fa;
                case 1: goto L_0x0101;
                case 2: goto L_0x0106;
                case 3: goto L_0x010f;
                case 4: goto L_0x011e;
                default: goto L_0x00fa;
            };
        L_0x00fa:
            r12.f892b = r5;
            r12.a = r0;
            r0 = 1;
            goto L_0x0009;
        L_0x0101:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
        L_0x0106:
            r1 = r0 + 1;
            r2 = r2 >> 4;
            r2 = (byte) r2;
            r6[r0] = r2;
            r0 = r1;
            goto L_0x00fa;
        L_0x010f:
            r1 = r0 + 1;
            r3 = r2 >> 10;
            r3 = (byte) r3;
            r6[r0] = r3;
            r0 = r1 + 1;
            r2 = r2 >> 2;
            r2 = (byte) r2;
            r6[r1] = r2;
            goto L_0x00fa;
        L_0x011e:
            r12.f892b = r9;
            r0 = r3;
            goto L_0x0009;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.gala.tvapi.c.a.b.a(byte[], int):boolean");
        }
    }

    static class C0252c extends C0250a {
        private static final byte[] f894b = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, RemoteCode.VOLUME_UP, RemoteCode.VOLUME_DOWN, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, RemoteCode.HOME, RemoteCode.CLICK, RemoteCode.BACK, RemoteCode.MENU, RemoteCode.SEEK_LEFT, RemoteCode.SEEK_RIGHT, RemoteCode.VOLUME_TOP, RemoteCode.VOLUME_BOTTOM, RemoteCode.FLING_LEFT, (byte) 43, (byte) 47};
        private static final byte[] f895c = new byte[]{(byte) 65, (byte) 66, (byte) 67, (byte) 68, (byte) 69, (byte) 70, (byte) 71, (byte) 72, (byte) 73, (byte) 74, (byte) 75, (byte) 76, (byte) 77, (byte) 78, (byte) 79, RemoteCode.VOLUME_UP, RemoteCode.VOLUME_DOWN, (byte) 82, (byte) 83, (byte) 84, (byte) 85, (byte) 86, (byte) 87, (byte) 88, (byte) 89, (byte) 90, (byte) 97, (byte) 98, (byte) 99, (byte) 100, (byte) 101, (byte) 102, (byte) 103, (byte) 104, (byte) 105, (byte) 106, (byte) 107, (byte) 108, (byte) 109, (byte) 110, (byte) 111, (byte) 112, (byte) 113, (byte) 114, (byte) 115, (byte) 116, (byte) 117, (byte) 118, (byte) 119, (byte) 120, (byte) 121, (byte) 122, (byte) 48, RemoteCode.HOME, RemoteCode.CLICK, RemoteCode.BACK, RemoteCode.MENU, RemoteCode.SEEK_LEFT, RemoteCode.SEEK_RIGHT, RemoteCode.VOLUME_TOP, RemoteCode.VOLUME_BOTTOM, RemoteCode.FLING_LEFT, (byte) 45, (byte) 95};
        private static /* synthetic */ boolean f896d;
        public final boolean f897a;
        private int f898b;
        public final boolean f899b;
        private int f900c;
        public final boolean f901c;
        private final byte[] f902d;
        private final byte[] f903e;

        static {
            boolean z;
            if (C0253a.class.desiredAssertionStatus()) {
                z = false;
            } else {
                z = true;
            }
            f896d = z;
        }

        public C0252c(int i) {
            boolean z;
            boolean z2 = true;
            this.a = null;
            this.f897a = (i & 1) == 0;
            if ((i & 2) == 0) {
                z = true;
            } else {
                z = false;
            }
            this.f899b = z;
            if ((i & 4) == 0) {
                z2 = false;
            }
            this.f901c = z2;
            this.f903e = (i & 8) == 0 ? f894b : f895c;
            this.f902d = new byte[2];
            this.f898b = 0;
            this.f900c = this.f899b ? 19 : -1;
        }

        /* JADX WARNING: inconsistent code. */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final boolean m613a(byte[] r13, int r14) {
            /*
            r12 = this;
            r0 = 2;
            r11 = 13;
            r10 = 10;
            r2 = 1;
            r3 = 0;
            r7 = r12.f903e;
            r8 = r12.a;
            r4 = r12.f900c;
            r1 = -1;
            r5 = r12.f898b;
            switch(r5) {
                case 0: goto L_0x00a2;
                case 1: goto L_0x00a6;
                case 2: goto L_0x00c2;
                default: goto L_0x0013;
            };
        L_0x0013:
            r5 = r1;
            r1 = r3;
        L_0x0015:
            r6 = -1;
            if (r5 == r6) goto L_0x01f0;
        L_0x0018:
            r6 = r5 >> 18;
            r6 = r6 & 63;
            r6 = r7[r6];
            r8[r3] = r6;
            r6 = r5 >> 12;
            r6 = r6 & 63;
            r6 = r7[r6];
            r8[r2] = r6;
            r6 = r5 >> 6;
            r6 = r6 & 63;
            r6 = r7[r6];
            r8[r0] = r6;
            r6 = 3;
            r0 = 4;
            r5 = r5 & 63;
            r5 = r7[r5];
            r8[r6] = r5;
            r4 = r4 + -1;
            if (r4 != 0) goto L_0x01ec;
        L_0x003c:
            r4 = r12.f901c;
            if (r4 == 0) goto L_0x0044;
        L_0x0040:
            r4 = 4;
            r0 = 5;
            r8[r4] = r11;
        L_0x0044:
            r5 = r0 + 1;
            r8[r0] = r10;
            r0 = 19;
            r6 = r0;
        L_0x004b:
            r0 = r1 + 3;
            if (r0 > r14) goto L_0x00e0;
        L_0x004f:
            r0 = r13[r1];
            r0 = r0 & 255;
            r0 = r0 << 16;
            r4 = r1 + 1;
            r4 = r13[r4];
            r4 = r4 & 255;
            r4 = r4 << 8;
            r0 = r0 | r4;
            r4 = r1 + 2;
            r4 = r13[r4];
            r4 = r4 & 255;
            r0 = r0 | r4;
            r4 = r0 >> 18;
            r4 = r4 & 63;
            r4 = r7[r4];
            r8[r5] = r4;
            r4 = r5 + 1;
            r9 = r0 >> 12;
            r9 = r9 & 63;
            r9 = r7[r9];
            r8[r4] = r9;
            r4 = r5 + 2;
            r9 = r0 >> 6;
            r9 = r9 & 63;
            r9 = r7[r9];
            r8[r4] = r9;
            r4 = r5 + 3;
            r0 = r0 & 63;
            r0 = r7[r0];
            r8[r4] = r0;
            r4 = r1 + 3;
            r1 = r5 + 4;
            r0 = r6 + -1;
            if (r0 != 0) goto L_0x01e7;
        L_0x0091:
            r0 = r12.f901c;
            if (r0 == 0) goto L_0x01e4;
        L_0x0095:
            r0 = r1 + 1;
            r8[r1] = r11;
        L_0x0099:
            r5 = r0 + 1;
            r8[r0] = r10;
            r0 = 19;
            r1 = r4;
            r6 = r0;
            goto L_0x004b;
        L_0x00a2:
            r5 = r1;
            r1 = r3;
            goto L_0x0015;
        L_0x00a6:
            if (r0 > r14) goto L_0x0013;
        L_0x00a8:
            r1 = r12.f902d;
            r1 = r1[r3];
            r1 = r1 & 255;
            r1 = r1 << 16;
            r5 = r13[r3];
            r5 = r5 & 255;
            r5 = r5 << 8;
            r1 = r1 | r5;
            r5 = r13[r2];
            r5 = r5 & 255;
            r1 = r1 | r5;
            r12.f898b = r3;
            r5 = r1;
            r1 = r0;
            goto L_0x0015;
        L_0x00c2:
            if (r14 <= 0) goto L_0x0013;
        L_0x00c4:
            r1 = r12.f902d;
            r1 = r1[r3];
            r1 = r1 & 255;
            r1 = r1 << 16;
            r5 = r12.f902d;
            r5 = r5[r2];
            r5 = r5 & 255;
            r5 = r5 << 8;
            r1 = r1 | r5;
            r5 = r13[r3];
            r5 = r5 & 255;
            r1 = r1 | r5;
            r12.f898b = r3;
            r5 = r1;
            r1 = r2;
            goto L_0x0015;
        L_0x00e0:
            r0 = r12.f898b;
            r0 = r1 - r0;
            r4 = r14 + -1;
            if (r0 != r4) goto L_0x0146;
        L_0x00e8:
            r0 = r12.f898b;
            if (r0 <= 0) goto L_0x013f;
        L_0x00ec:
            r0 = r12.f902d;
            r0 = r0[r3];
            r3 = r1;
            r1 = r2;
        L_0x00f2:
            r0 = r0 & 255;
            r4 = r0 << 4;
            r0 = r12.f898b;
            r0 = r0 - r1;
            r12.f898b = r0;
            r1 = r5 + 1;
            r0 = r4 >> 6;
            r0 = r0 & 63;
            r0 = r7[r0];
            r8[r5] = r0;
            r0 = r1 + 1;
            r4 = r4 & 63;
            r4 = r7[r4];
            r8[r1] = r4;
            r1 = r12.f897a;
            if (r1 == 0) goto L_0x011d;
        L_0x0111:
            r1 = r0 + 1;
            r4 = 61;
            r8[r0] = r4;
            r0 = r1 + 1;
            r4 = 61;
            r8[r1] = r4;
        L_0x011d:
            r1 = r12.f899b;
            if (r1 == 0) goto L_0x012f;
        L_0x0121:
            r1 = r12.f901c;
            if (r1 == 0) goto L_0x012a;
        L_0x0125:
            r1 = r0 + 1;
            r8[r0] = r11;
            r0 = r1;
        L_0x012a:
            r1 = r0 + 1;
            r8[r0] = r10;
            r0 = r1;
        L_0x012f:
            r1 = r3;
            r5 = r0;
        L_0x0131:
            r0 = f896d;
            if (r0 != 0) goto L_0x01cf;
        L_0x0135:
            r0 = r12.f898b;
            if (r0 == 0) goto L_0x01cf;
        L_0x0139:
            r0 = new java.lang.AssertionError;
            r0.<init>();
            throw r0;
        L_0x013f:
            r4 = r1 + 1;
            r0 = r13[r1];
            r1 = r3;
            r3 = r4;
            goto L_0x00f2;
        L_0x0146:
            r0 = r12.f898b;
            r0 = r1 - r0;
            r4 = r14 + -2;
            if (r0 != r4) goto L_0x01b7;
        L_0x014e:
            r0 = r12.f898b;
            if (r0 <= r2) goto L_0x01ab;
        L_0x0152:
            r0 = r12.f902d;
            r0 = r0[r3];
            r3 = r2;
        L_0x0157:
            r0 = r0 & 255;
            r9 = r0 << 10;
            r0 = r12.f898b;
            if (r0 <= 0) goto L_0x01b1;
        L_0x015f:
            r0 = r12.f902d;
            r4 = r3 + 1;
            r0 = r0[r3];
            r3 = r4;
        L_0x0166:
            r0 = r0 & 255;
            r0 = r0 << 2;
            r0 = r0 | r9;
            r4 = r12.f898b;
            r3 = r4 - r3;
            r12.f898b = r3;
            r3 = r5 + 1;
            r4 = r0 >> 12;
            r4 = r4 & 63;
            r4 = r7[r4];
            r8[r5] = r4;
            r4 = r3 + 1;
            r5 = r0 >> 6;
            r5 = r5 & 63;
            r5 = r7[r5];
            r8[r3] = r5;
            r3 = r4 + 1;
            r0 = r0 & 63;
            r0 = r7[r0];
            r8[r4] = r0;
            r0 = r12.f897a;
            if (r0 == 0) goto L_0x01e2;
        L_0x0191:
            r0 = r3 + 1;
            r4 = 61;
            r8[r3] = r4;
        L_0x0197:
            r3 = r12.f899b;
            if (r3 == 0) goto L_0x01a9;
        L_0x019b:
            r3 = r12.f901c;
            if (r3 == 0) goto L_0x01a4;
        L_0x019f:
            r3 = r0 + 1;
            r8[r0] = r11;
            r0 = r3;
        L_0x01a4:
            r3 = r0 + 1;
            r8[r0] = r10;
            r0 = r3;
        L_0x01a9:
            r5 = r0;
            goto L_0x0131;
        L_0x01ab:
            r4 = r1 + 1;
            r0 = r13[r1];
            r1 = r4;
            goto L_0x0157;
        L_0x01b1:
            r4 = r1 + 1;
            r0 = r13[r1];
            r1 = r4;
            goto L_0x0166;
        L_0x01b7:
            r0 = r12.f899b;
            if (r0 == 0) goto L_0x0131;
        L_0x01bb:
            if (r5 <= 0) goto L_0x0131;
        L_0x01bd:
            r0 = 19;
            if (r6 == r0) goto L_0x0131;
        L_0x01c1:
            r0 = r12.f901c;
            if (r0 == 0) goto L_0x01e0;
        L_0x01c5:
            r0 = r5 + 1;
            r8[r5] = r11;
        L_0x01c9:
            r5 = r0 + 1;
            r8[r0] = r10;
            goto L_0x0131;
        L_0x01cf:
            r0 = f896d;
            if (r0 != 0) goto L_0x01db;
        L_0x01d3:
            if (r1 == r14) goto L_0x01db;
        L_0x01d5:
            r0 = new java.lang.AssertionError;
            r0.<init>();
            throw r0;
        L_0x01db:
            r12.a = r5;
            r12.f900c = r6;
            return r2;
        L_0x01e0:
            r0 = r5;
            goto L_0x01c9;
        L_0x01e2:
            r0 = r3;
            goto L_0x0197;
        L_0x01e4:
            r0 = r1;
            goto L_0x0099;
        L_0x01e7:
            r6 = r0;
            r5 = r1;
            r1 = r4;
            goto L_0x004b;
        L_0x01ec:
            r6 = r4;
            r5 = r0;
            goto L_0x004b;
        L_0x01f0:
            r6 = r4;
            r5 = r3;
            goto L_0x004b;
            */
            throw new UnsupportedOperationException("Method not decompiled: com.gala.tvapi.c.a.c.a(byte[], int):boolean");
        }
    }

    public static byte[] m615a(byte[] bArr) {
        int length = bArr.length;
        C0251b c0251b = new C0251b(new byte[((length * 3) / 4)]);
        if (!c0251b.m612a(bArr, length)) {
            throw new IllegalArgumentException("bad base-64");
        } else if (c0251b.a == c0251b.a.length) {
            return c0251b.a;
        } else {
            byte[] bArr2 = new byte[c0251b.a];
            System.arraycopy(c0251b.a, 0, bArr2, 0, c0251b.a);
            return bArr2;
        }
    }

    public static String m614a(byte[] bArr) {
        try {
            return new String(C0253a.m616a(bArr, 3), "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }

    public static byte[] m616a(byte[] bArr, int i) {
        int length = bArr.length;
        C0252c c0252c = new C0252c(i);
        int i2 = (length / 3) << 2;
        if (!c0252c.f897a) {
            switch (length % 3) {
                case 0:
                    break;
                case 1:
                    i2 += 2;
                    break;
                case 2:
                    i2 += 3;
                    break;
                default:
                    break;
            }
        } else if (length % 3 > 0) {
            i2 += 4;
        }
        if (c0252c.f899b && length > 0) {
            i2 += (c0252c.f901c ? 2 : 1) * (((length - 1) / 57) + 1);
        }
        c0252c.a = new byte[i2];
        c0252c.m613a(bArr, length);
        if (f904a || c0252c.a == i2) {
            return c0252c.a;
        }
        throw new AssertionError();
    }

    private C0253a() {
    }
}
