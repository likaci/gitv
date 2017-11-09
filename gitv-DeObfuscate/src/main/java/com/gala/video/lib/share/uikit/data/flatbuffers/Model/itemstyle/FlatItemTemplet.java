package com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatItemTemplet extends Table {
    public static FlatItemTemplet getRootAsFlatItemTemplet(ByteBuffer _bb) {
        return getRootAsFlatItemTemplet(_bb, new FlatItemTemplet());
    }

    public static FlatItemTemplet getRootAsFlatItemTemplet(ByteBuffer _bb, FlatItemTemplet obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatItemTemplet __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public String styleType() {
        int o = __offset(4);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer styleTypeAsByteBuffer() {
        return __vector_as_bytebuffer(4, 1);
    }

    public FlatItemData styleList(int j) {
        return styleList(new FlatItemData(), j);
    }

    public FlatItemData styleList(FlatItemData obj, int j) {
        int o = __offset(6);
        return o != 0 ? obj.__assign(__indirect(__vector(o) + (j * 4)), this.bb) : null;
    }

    public int styleListLength() {
        int o = __offset(6);
        return o != 0 ? __vector_len(o) : 0;
    }

    public static int createFlatItemTemplet(FlatBufferBuilder builder, int style_typeOffset, int style_listOffset) {
        builder.startObject(2);
        addStyleList(builder, style_listOffset);
        addStyleType(builder, style_typeOffset);
        return endFlatItemTemplet(builder);
    }

    public static void startFlatItemTemplet(FlatBufferBuilder builder) {
        builder.startObject(2);
    }

    public static void addStyleType(FlatBufferBuilder builder, int styleTypeOffset) {
        builder.addOffset(0, styleTypeOffset, 0);
    }

    public static void addStyleList(FlatBufferBuilder builder, int styleListOffset) {
        builder.addOffset(1, styleListOffset, 0);
    }

    public static int createStyleListVector(FlatBufferBuilder builder, int[] data) {
        builder.startVector(4, data.length, 4);
        for (int i = data.length - 1; i >= 0; i--) {
            builder.addOffset(data[i]);
        }
        return builder.endVector();
    }

    public static void startStyleListVector(FlatBufferBuilder builder, int numElems) {
        builder.startVector(4, numElems, 4);
    }

    public static int endFlatItemTemplet(FlatBufferBuilder builder) {
        return builder.endObject();
    }
}
