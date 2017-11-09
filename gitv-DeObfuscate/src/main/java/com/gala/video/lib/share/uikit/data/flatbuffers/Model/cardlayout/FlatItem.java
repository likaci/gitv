package com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatItem extends Table {
    public static FlatItem getRootAsFlatItem(ByteBuffer _bb) {
        return getRootAsFlatItem(_bb, new FlatItem());
    }

    public static FlatItem getRootAsFlatItem(ByteBuffer _bb, FlatItem obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatItem __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public short m1582w() {
        int o = __offset(4);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short m1581h() {
        int o = __offset(6);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public float scale() {
        int o = __offset(8);
        return o != 0 ? this.bb.getFloat(this.bb_pos + o) : 1.1f;
    }

    public short spaceV() {
        int o = __offset(10);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short spaceH() {
        int o = __offset(12);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public String style() {
        int o = __offset(14);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer styleAsByteBuffer() {
        return __vector_as_bytebuffer(14, 1);
    }

    public static int createFlatItem(FlatBufferBuilder builder, short w, short h, float scale, short space_v, short space_h, int styleOffset) {
        builder.startObject(6);
        addStyle(builder, styleOffset);
        addScale(builder, scale);
        addSpaceH(builder, space_h);
        addSpaceV(builder, space_v);
        addH(builder, h);
        addW(builder, w);
        return endFlatItem(builder);
    }

    public static void startFlatItem(FlatBufferBuilder builder) {
        builder.startObject(6);
    }

    public static void addW(FlatBufferBuilder builder, short w) {
        builder.addShort(0, w, 0);
    }

    public static void addH(FlatBufferBuilder builder, short h) {
        builder.addShort(1, h, 0);
    }

    public static void addScale(FlatBufferBuilder builder, float scale) {
        builder.addFloat(2, scale, 1.100000023841858d);
    }

    public static void addSpaceV(FlatBufferBuilder builder, short spaceV) {
        builder.addShort(3, spaceV, 0);
    }

    public static void addSpaceH(FlatBufferBuilder builder, short spaceH) {
        builder.addShort(4, spaceH, 0);
    }

    public static void addStyle(FlatBufferBuilder builder, int styleOffset) {
        builder.addOffset(5, styleOffset, 0);
    }

    public static int endFlatItem(FlatBufferBuilder builder) {
        return builder.endObject();
    }
}
