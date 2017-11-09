package com.gala.video.lib.share.uikit.data.flatbuffers.Model.cardlayout;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatCard extends Table {
    public static FlatCard getRootAsFlatCard(ByteBuffer _bb) {
        return getRootAsFlatCard(_bb, new FlatCard());
    }

    public static FlatCard getRootAsFlatCard(ByteBuffer _bb, FlatCard obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatCard __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public int layoutId() {
        int o = __offset(4);
        return o != 0 ? this.bb.getInt(this.bb_pos + o) : 0;
    }

    public FlatCardStyle layoutStyle() {
        return layoutStyle(new FlatCardStyle());
    }

    public FlatCardStyle layoutStyle(FlatCardStyle obj) {
        int o = __offset(6);
        return o != 0 ? obj.__assign(__indirect(this.bb_pos + o), this.bb) : null;
    }

    public static int createFlatCard(FlatBufferBuilder builder, int layout_id, int layout_styleOffset) {
        builder.startObject(2);
        addLayoutStyle(builder, layout_styleOffset);
        addLayoutId(builder, layout_id);
        return endFlatCard(builder);
    }

    public static void startFlatCard(FlatBufferBuilder builder) {
        builder.startObject(2);
    }

    public static void addLayoutId(FlatBufferBuilder builder, int layoutId) {
        builder.addInt(0, layoutId, 0);
    }

    public static void addLayoutStyle(FlatBufferBuilder builder, int layoutStyleOffset) {
        builder.addOffset(1, layoutStyleOffset, 0);
    }

    public static int endFlatCard(FlatBufferBuilder builder) {
        return builder.endObject();
    }
}
