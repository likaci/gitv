package com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatCardStyle extends Table {
    public static FlatCardStyle getRootAsFlatCardStyle(ByteBuffer _bb) {
        return getRootAsFlatCardStyle(_bb, new FlatCardStyle());
    }

    public static FlatCardStyle getRootAsFlatCardStyle(ByteBuffer _bb, FlatCardStyle obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatCardStyle __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public short type() {
        int o = __offset(4);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short m1580w() {
        int o = __offset(6);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyH() {
        int o = __offset(8);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) -1;
    }

    public short bodyPdL() {
        int o = __offset(10);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyPdR() {
        int o = __offset(12);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyPdT() {
        int o = __offset(14);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyPdB() {
        int o = __offset(16);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyMgL() {
        int o = __offset(18);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyMgR() {
        int o = __offset(20);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyMgT() {
        int o = __offset(22);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bodyMgB() {
        int o = __offset(24);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short headerH() {
        int o = __offset(26);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short headerPdL() {
        int o = __offset(28);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short headerPdR() {
        int o = __offset(30);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short headerPdT() {
        int o = __offset(32);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short headerPdB() {
        int o = __offset(34);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short spaceV() {
        int o = __offset(36);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short spaceH() {
        int o = __offset(38);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short rowNolimit() {
        int o = __offset(40);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short defaultFocus() {
        int o = __offset(42);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short showPosition() {
        int o = __offset(44);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public float scale() {
        int o = __offset(46);
        return o != 0 ? this.bb.getFloat(this.bb_pos + o) : 1.1f;
    }

    public FlatRow rows(int j) {
        return rows(new FlatRow(), j);
    }

    public FlatRow rows(FlatRow obj, int j) {
        int o = __offset(48);
        return o != 0 ? obj.__assign(__indirect(__vector(o) + (j * 4)), this.bb) : null;
    }

    public int rowsLength() {
        int o = __offset(48);
        return o != 0 ? __vector_len(o) : 0;
    }

    public short backId() {
        int o = __offset(50);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public static int createFlatCardStyle(FlatBufferBuilder builder, short type, short w, short body_h, short body_pd_l, short body_pd_r, short body_pd_t, short body_pd_b, short body_mg_l, short body_mg_r, short body_mg_t, short body_mg_b, short header_h, short header_pd_l, short header_pd_r, short header_pd_t, short header_pd_b, short space_v, short space_h, short row_nolimit, short default_focus, short show_position, float scale, int rowsOffset, short back_id) {
        builder.startObject(24);
        addRows(builder, rowsOffset);
        addScale(builder, scale);
        addBackId(builder, back_id);
        addShowPosition(builder, show_position);
        addDefaultFocus(builder, default_focus);
        addRowNolimit(builder, row_nolimit);
        addSpaceH(builder, space_h);
        addSpaceV(builder, space_v);
        addHeaderPdB(builder, header_pd_b);
        addHeaderPdT(builder, header_pd_t);
        addHeaderPdR(builder, header_pd_r);
        addHeaderPdL(builder, header_pd_l);
        addHeaderH(builder, header_h);
        addBodyMgB(builder, body_mg_b);
        addBodyMgT(builder, body_mg_t);
        addBodyMgR(builder, body_mg_r);
        addBodyMgL(builder, body_mg_l);
        addBodyPdB(builder, body_pd_b);
        addBodyPdT(builder, body_pd_t);
        addBodyPdR(builder, body_pd_r);
        addBodyPdL(builder, body_pd_l);
        addBodyH(builder, body_h);
        addW(builder, w);
        addType(builder, type);
        return endFlatCardStyle(builder);
    }

    public static void startFlatCardStyle(FlatBufferBuilder builder) {
        builder.startObject(24);
    }

    public static void addType(FlatBufferBuilder builder, short type) {
        builder.addShort(0, type, 0);
    }

    public static void addW(FlatBufferBuilder builder, short w) {
        builder.addShort(1, w, 0);
    }

    public static void addBodyH(FlatBufferBuilder builder, short bodyH) {
        builder.addShort(2, bodyH, -1);
    }

    public static void addBodyPdL(FlatBufferBuilder builder, short bodyPdL) {
        builder.addShort(3, bodyPdL, 0);
    }

    public static void addBodyPdR(FlatBufferBuilder builder, short bodyPdR) {
        builder.addShort(4, bodyPdR, 0);
    }

    public static void addBodyPdT(FlatBufferBuilder builder, short bodyPdT) {
        builder.addShort(5, bodyPdT, 0);
    }

    public static void addBodyPdB(FlatBufferBuilder builder, short bodyPdB) {
        builder.addShort(6, bodyPdB, 0);
    }

    public static void addBodyMgL(FlatBufferBuilder builder, short bodyMgL) {
        builder.addShort(7, bodyMgL, 0);
    }

    public static void addBodyMgR(FlatBufferBuilder builder, short bodyMgR) {
        builder.addShort(8, bodyMgR, 0);
    }

    public static void addBodyMgT(FlatBufferBuilder builder, short bodyMgT) {
        builder.addShort(9, bodyMgT, 0);
    }

    public static void addBodyMgB(FlatBufferBuilder builder, short bodyMgB) {
        builder.addShort(10, bodyMgB, 0);
    }

    public static void addHeaderH(FlatBufferBuilder builder, short headerH) {
        builder.addShort(11, headerH, 0);
    }

    public static void addHeaderPdL(FlatBufferBuilder builder, short headerPdL) {
        builder.addShort(12, headerPdL, 0);
    }

    public static void addHeaderPdR(FlatBufferBuilder builder, short headerPdR) {
        builder.addShort(13, headerPdR, 0);
    }

    public static void addHeaderPdT(FlatBufferBuilder builder, short headerPdT) {
        builder.addShort(14, headerPdT, 0);
    }

    public static void addHeaderPdB(FlatBufferBuilder builder, short headerPdB) {
        builder.addShort(15, headerPdB, 0);
    }

    public static void addSpaceV(FlatBufferBuilder builder, short spaceV) {
        builder.addShort(16, spaceV, 0);
    }

    public static void addSpaceH(FlatBufferBuilder builder, short spaceH) {
        builder.addShort(17, spaceH, 0);
    }

    public static void addRowNolimit(FlatBufferBuilder builder, short rowNolimit) {
        builder.addShort(18, rowNolimit, 0);
    }

    public static void addDefaultFocus(FlatBufferBuilder builder, short defaultFocus) {
        builder.addShort(19, defaultFocus, 0);
    }

    public static void addShowPosition(FlatBufferBuilder builder, short showPosition) {
        builder.addShort(20, showPosition, 0);
    }

    public static void addScale(FlatBufferBuilder builder, float scale) {
        builder.addFloat(21, scale, 1.100000023841858d);
    }

    public static void addRows(FlatBufferBuilder builder, int rowsOffset) {
        builder.addOffset(22, rowsOffset, 0);
    }

    public static int createRowsVector(FlatBufferBuilder builder, int[] data) {
        builder.startVector(4, data.length, 4);
        for (int i = data.length - 1; i >= 0; i--) {
            builder.addOffset(data[i]);
        }
        return builder.endVector();
    }

    public static void startRowsVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems, 4);
    }

    public static void addBackId(FlatBufferBuilder builder, short backId) {
        builder.addShort(23, backId, 0);
    }

    public static int endFlatCardStyle(FlatBufferBuilder builder) {
        return builder.endObject();
    }
}
