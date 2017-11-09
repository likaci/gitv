package com.gala.tvapi.vrs.model;

import com.gala.tvapi.b.a;
import com.gala.tvapi.type.PackageType;

public class GalaVipInfo extends Model {
    private static final long serialVersionUID = 1;
    public DeadLine deadLine = null;
    public String level = null;
    public String mobile = null;
    public String name = null;
    public String payType = null;
    public String status = null;
    public String surplus = null;
    public String type = null;
    public String vipType = null;

    public void setDeadLine(DeadLine dl) {
        this.deadLine = dl;
    }

    public DeadLine getDeadLine() {
        return this.deadLine;
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.gala.tvapi.type.UserType getUserType() {
        /*
        r12 = this;
        r11 = 4;
        r10 = 2;
        r9 = 3;
        r8 = 0;
        r7 = 1;
        r0 = new com.gala.tvapi.type.UserType;
        r0.<init>();
        r1 = r12.vipType;
        r1 = r12.valueOf(r1);
        r2 = r12.surplus;
        r2 = r12.valueOf(r2);
        r3 = r12.status;
        r3 = r12.valueOf(r3);
        r4 = r12.payType;
        r4 = r12.valueOf(r4);
        r5 = 6;
        if (r1 != r5) goto L_0x002a;
    L_0x0025:
        if (r3 != r7) goto L_0x002a;
    L_0x0027:
        r0.setTWVIP(r7);
    L_0x002a:
        r5 = 5;
        if (r1 != r5) goto L_0x0032;
    L_0x002d:
        if (r3 != r7) goto L_0x00bc;
    L_0x002f:
        r0.setLitchi(r7);
    L_0x0032:
        if (r1 != r11) goto L_0x0038;
    L_0x0034:
        if (r2 <= 0) goto L_0x0038;
    L_0x0036:
        if (r3 == r7) goto L_0x0048;
    L_0x0038:
        if (r1 != r11) goto L_0x004b;
    L_0x003a:
        if (r3 != r7) goto L_0x004b;
    L_0x003c:
        r5 = new java.lang.String[r7];
        r6 = r12.surplus;
        r5[r8] = r6;
        r5 = com.gala.tvapi.b.a.a(r5);
        if (r5 == 0) goto L_0x004b;
    L_0x0048:
        r0.setPlatinum(r7);
    L_0x004b:
        if (r1 != r7) goto L_0x0051;
    L_0x004d:
        if (r2 <= 0) goto L_0x0051;
    L_0x004f:
        if (r3 == r7) goto L_0x0061;
    L_0x0051:
        if (r1 != r7) goto L_0x0064;
    L_0x0053:
        if (r3 != r7) goto L_0x0064;
    L_0x0055:
        r5 = new java.lang.String[r7];
        r6 = r12.surplus;
        r5[r8] = r6;
        r5 = com.gala.tvapi.b.a.a(r5);
        if (r5 == 0) goto L_0x0064;
    L_0x0061:
        r0.setGold(r7);
    L_0x0064:
        if (r1 != r9) goto L_0x006a;
    L_0x0066:
        if (r2 <= 0) goto L_0x006a;
    L_0x0068:
        if (r3 == r7) goto L_0x007a;
    L_0x006a:
        if (r1 != r9) goto L_0x007d;
    L_0x006c:
        if (r3 != r7) goto L_0x007d;
    L_0x006e:
        r5 = new java.lang.String[r7];
        r6 = r12.surplus;
        r5[r8] = r6;
        r5 = com.gala.tvapi.b.a.a(r5);
        if (r5 == 0) goto L_0x007d;
    L_0x007a:
        r0.setSilver(r7);
    L_0x007d:
        if (r1 != r7) goto L_0x0088;
    L_0x007f:
        if (r3 != r7) goto L_0x0088;
    L_0x0081:
        if (r4 == r7) goto L_0x0085;
    L_0x0083:
        if (r4 != r10) goto L_0x0088;
    L_0x0085:
        r0.setPhoneMonth(r7);
    L_0x0088:
        if (r1 <= 0) goto L_0x008e;
    L_0x008a:
        if (r2 <= 0) goto L_0x008e;
    L_0x008c:
        if (r3 == r7) goto L_0x009e;
    L_0x008e:
        if (r1 <= 0) goto L_0x00a1;
    L_0x0090:
        if (r3 != r7) goto L_0x00a1;
    L_0x0092:
        r2 = new java.lang.String[r7];
        r5 = r12.surplus;
        r2[r8] = r5;
        r2 = com.gala.tvapi.b.a.a(r2);
        if (r2 == 0) goto L_0x00a1;
    L_0x009e:
        r0.setMember(r7);
    L_0x00a1:
        if (r3 == r10) goto L_0x00a5;
    L_0x00a3:
        if (r3 != 0) goto L_0x00bb;
    L_0x00a5:
        r0.setExpire(r7);
        if (r1 != r7) goto L_0x00b1;
    L_0x00aa:
        if (r4 == r7) goto L_0x00ae;
    L_0x00ac:
        if (r4 != r10) goto L_0x00b1;
    L_0x00ae:
        r0.setPhoneMonth(r7);
    L_0x00b1:
        if (r1 != r7) goto L_0x00c3;
    L_0x00b3:
        r0.setGold(r7);
    L_0x00b6:
        if (r1 <= 0) goto L_0x00bb;
    L_0x00b8:
        r0.setMember(r7);
    L_0x00bb:
        return r0;
    L_0x00bc:
        if (r3 != r9) goto L_0x0032;
    L_0x00be:
        r0.setLitchiOverdue(r7);
        goto L_0x0032;
    L_0x00c3:
        if (r1 != r9) goto L_0x00c9;
    L_0x00c5:
        r0.setSilver(r7);
        goto L_0x00b6;
    L_0x00c9:
        if (r1 != r11) goto L_0x00b6;
    L_0x00cb:
        r0.setPlatinum(r7);
        goto L_0x00b6;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.gala.tvapi.vrs.model.GalaVipInfo.getUserType():com.gala.tvapi.type.UserType");
    }

    public PackageType getPackageType() {
        if (a.a(this.vipType)) {
            return PackageType.NO_PACKAGE;
        }
        int intValue = Integer.valueOf(this.vipType).intValue();
        if (intValue == 1) {
            return PackageType.GOLD_PACKAGE;
        }
        if (intValue == 3) {
            return PackageType.SILVER_PACKAGE;
        }
        if (intValue == 4) {
            return PackageType.PLATINUM_PACKAGE;
        }
        return PackageType.NO_PACKAGE;
    }

    private int valueOf(String s) {
        if (a.a(s)) {
            return -1;
        }
        return Integer.valueOf(s).intValue();
    }
}
