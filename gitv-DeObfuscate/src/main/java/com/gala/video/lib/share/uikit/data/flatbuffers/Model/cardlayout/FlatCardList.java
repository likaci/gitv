package com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatCardList extends Table {
    public static FlatCardList getRootAsFlatCardList(ByteBuffer _bb) {
        return getRootAsFlatCardList(_bb, new FlatCardList());
    }

    public static FlatCardList getRootAsFlatCardList(ByteBuffer _bb, FlatCardList obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatCardList __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public FlatCard cardlist(int j) {
        return cardlist(new FlatCard(), j);
    }

    public FlatCard cardlist(FlatCard obj, int j) {
        int o = __offset(4);
        return o != 0 ? obj.__assign(__indirect(__vector(o) + (j * 4)), this.bb) : null;
    }

    public int cardlistLength() {
        int o = __offset(4);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static int createFlatCardList(FlatBufferBuilder builder, int cardlistOffset) {
        builder.startObject(1);
        addCardlist(builder, cardlistOffset);
        return endFlatCardList(builder);
    }

    public static void startFlatCardList(FlatBufferBuilder builder) {
        builder.startObject(1);
    }

    public static void addCardlist(FlatBufferBuilder builder, int cardlistOffset) {
        builder.addOffset(0, cardlistOffset, 0);
    }

    public static int createCardlistVector(FlatBufferBuilder builder, int[] data) {
        builder.startVector(4, data.length, 4);
        for (int i = data.length - 1; i >= 0; i--) {
            builder.addOffset(data[i]);
        }
        return builder.endVector();
    }

    public static void startCardlistVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems, 4);
    }

    public static int endFlatCardList(FlatBufferBuilder builder) {
        return builder.endObject();
    }

    public static void finishFlatCardListBuffer(FlatBufferBuilder builder, int offset) {
        builder.finish(offset);
    }
}
