package com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatItemData extends Table {
    public static FlatItemData getRootAsFlatItemData(ByteBuffer _bb) {
        return getRootAsFlatItemData(_bb, new FlatItemData());
    }

    public static FlatItemData getRootAsFlatItemData(ByteBuffer _bb, FlatItemData obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatItemData __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public String id() {
        int o = __offset(4);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer idAsByteBuffer() {
        return __vector_as_bytebuffer(4, 1);
    }

    public String type() {
        int o = __offset(6);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer typeAsByteBuffer() {
        return __vector_as_bytebuffer(6, 1);
    }

    public short zOrder() {
        int o = __offset(8);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public FlatStyle style() {
        return style(new FlatStyle());
    }

    public FlatStyle style(FlatStyle obj) {
        int o = __offset(10);
        return o != 0 ? obj.__assign(__indirect(this.bb_pos + o), this.bb) : null;
    }

    public static int createFlatItemData(FlatBufferBuilder builder, int idOffset, int typeOffset, short z_order, int styleOffset) {
        builder.startObject(4);
        addStyle(builder, styleOffset);
        addType(builder, typeOffset);
        addId(builder, idOffset);
        addZOrder(builder, z_order);
        return endFlatItemData(builder);
    }

    public static void startFlatItemData(FlatBufferBuilder builder) {
        builder.startObject(4);
    }

    public static void addId(FlatBufferBuilder builder, int idOffset) {
        builder.addOffset(0, idOffset, 0);
    }

    public static void addType(FlatBufferBuilder builder, int typeOffset) {
        builder.addOffset(1, typeOffset, 0);
    }

    public static void addZOrder(FlatBufferBuilder builder, short zOrder) {
        builder.addShort(2, zOrder, 0);
    }

    public static void addStyle(FlatBufferBuilder builder, int styleOffset) {
        builder.addOffset(3, styleOffset, 0);
    }

    public static int endFlatItemData(FlatBufferBuilder builder) {
        return builder.endObject();
    }
}
