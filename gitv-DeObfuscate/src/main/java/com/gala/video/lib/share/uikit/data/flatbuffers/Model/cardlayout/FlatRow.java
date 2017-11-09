package com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatRow extends Table {
    public static FlatRow getRootAsFlatRow(ByteBuffer _bb) {
        return getRootAsFlatRow(_bb, new FlatRow());
    }

    public static FlatRow getRootAsFlatRow(ByteBuffer _bb, FlatRow obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatRow __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public FlatItem items(int j) {
        return items(new FlatItem(), j);
    }

    public FlatItem items(FlatItem obj, int j) {
        int o = __offset(4);
        return o != 0 ? obj.__assign(__indirect(__vector(o) + (j * 4)), this.bb) : null;
    }

    public int itemsLength() {
        int o = __offset(4);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static int createFlatRow(FlatBufferBuilder builder, int itemsOffset) {
        builder.startObject(1);
        addItems(builder, itemsOffset);
        return endFlatRow(builder);
    }

    public static void startFlatRow(FlatBufferBuilder builder) {
        builder.startObject(1);
    }

    public static void addItems(FlatBufferBuilder builder, int itemsOffset) {
        builder.addOffset(0, itemsOffset, 0);
    }

    public static int createItemsVector(FlatBufferBuilder builder, int[] data) {
        builder.startVector(4, data.length, 4);
        for (int i = data.length - 1; i >= 0; i--) {
            builder.addOffset(data[i]);
        }
        return builder.endVector();
    }

    public static void startItemsVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems, 4);
    }

    public static int endFlatRow(FlatBufferBuilder builder) {
        return builder.endObject();
    }
}
