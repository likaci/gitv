package com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatItemTempletList extends Table {
    public static FlatItemTempletList getRootAsFlatItemTempletList(ByteBuffer _bb) {
        return getRootAsFlatItemTempletList(_bb, new FlatItemTempletList());
    }

    public static FlatItemTempletList getRootAsFlatItemTempletList(ByteBuffer _bb, FlatItemTempletList obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatItemTempletList __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public FlatItemTemplet itemStyleList(int j) {
        return itemStyleList(new FlatItemTemplet(), j);
    }

    public FlatItemTemplet itemStyleList(FlatItemTemplet obj, int j) {
        int o = __offset(4);
        return o != 0 ? obj.__assign(__indirect(__vector(o) + (j * 4)), this.bb) : null;
    }

    public int itemStyleListLength() {
        int o = __offset(4);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static int createFlatItemTempletList(FlatBufferBuilder builder, int itemStyleListOffset) {
        builder.startObject(1);
        addItemStyleList(builder, itemStyleListOffset);
        return endFlatItemTempletList(builder);
    }

    public static void startFlatItemTempletList(FlatBufferBuilder builder) {
        builder.startObject(1);
    }

    public static void addItemStyleList(FlatBufferBuilder builder, int itemStyleListOffset) {
        builder.addOffset(0, itemStyleListOffset, 0);
    }

    public static int createItemStyleListVector(FlatBufferBuilder builder, int[] data) {
        builder.startVector(4, data.length, 4);
        for (int i = data.length - 1; i >= 0; i--) {
            builder.addOffset(data[i]);
        }
        return builder.endVector();
    }

    public static void startItemStyleListVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems, 4);
    }

    public static int endFlatItemTempletList(FlatBufferBuilder builder) {
        return builder.endObject();
    }

    public static void finishFlatItemTempletListBuffer(FlatBufferBuilder builder, int offset) {
        builder.finish(offset);
    }
}
