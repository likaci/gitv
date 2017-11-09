package com.gala.video.lib.share.uikit.data.flatbuffers.Model.itemstyle;

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.Table;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public final class FlatStyle extends Table {
    public static FlatStyle getRootAsFlatStyle(ByteBuffer _bb) {
        return getRootAsFlatStyle(_bb, new FlatStyle());
    }

    public static FlatStyle getRootAsFlatStyle(ByteBuffer _bb, FlatStyle obj) {
        _bb.order(ByteOrder.LITTLE_ENDIAN);
        return obj.__assign(_bb.getInt(_bb.position()) + _bb.position(), _bb);
    }

    public void __init(int _i, ByteBuffer _bb) {
        this.bb_pos = _i;
        this.bb = _bb;
    }

    public FlatStyle __assign(int _i, ByteBuffer _bb) {
        __init(_i, _bb);
        return this;
    }

    public String value() {
        int o = __offset(4);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer valueAsByteBuffer() {
        return __vector_as_bytebuffer(4, 1);
    }

    public short pdL() {
        int o = __offset(6);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short pdR() {
        int o = __offset(8);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short pdT() {
        int o = __offset(10);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short pdB() {
        int o = __offset(12);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short w() {
        int o = __offset(14);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short h() {
        int o = __offset(16);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short mgL() {
        int o = __offset(18);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short mgR() {
        int o = __offset(20);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short mgT() {
        int o = __offset(22);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short mgB() {
        int o = __offset(24);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short visible() {
        int o = __offset(26);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 1;
    }

    public short clipPadding() {
        int o = __offset(28);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 1;
    }

    public short clipType() {
        int o = __offset(30);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short scaleType() {
        int o = __offset(32);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short gravity() {
        int o = __offset(34);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 5;
    }

    public String focusValue() {
        int o = __offset(36);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer focusValueAsByteBuffer() {
        return __vector_as_bytebuffer(36, 1);
    }

    public String defaultValue() {
        int o = __offset(38);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer defaultValueAsByteBuffer() {
        return __vector_as_bytebuffer(38, 1);
    }

    public String text() {
        int o = __offset(40);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer textAsByteBuffer() {
        return __vector_as_bytebuffer(40, 1);
    }

    public short font() {
        int o = __offset(42);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public float skewX() {
        int o = __offset(44);
        return o != 0 ? this.bb.getFloat(this.bb_pos + o) : 0.25f;
    }

    public short fontSize() {
        int o = __offset(46);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public String fontColor() {
        int o = __offset(48);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer fontColorAsByteBuffer() {
        return __vector_as_bytebuffer(48, 1);
    }

    public String focusFontColor() {
        int o = __offset(50);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer focusFontColorAsByteBuffer() {
        return __vector_as_bytebuffer(50, 1);
    }

    public short lines() {
        int o = __offset(52);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 1;
    }

    public short titleType() {
        int o = __offset(54);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short lineSpace() {
        int o = __offset(56);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short ellipsize() {
        int o = __offset(58);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public float marqSpeed() {
        int o = __offset(60);
        return o != 0 ? this.bb.getFloat(this.bb_pos + o) : 1.0f;
    }

    public short marqDelay() {
        int o = __offset(62);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 500;
    }

    public short marqTextSpace() {
        int o = __offset(64);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short shadowDx() {
        int o = __offset(66);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short shadowDy() {
        int o = __offset(68);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public String shadowColor() {
        int o = __offset(70);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer shadowColorAsByteBuffer() {
        return __vector_as_bytebuffer(70, 1);
    }

    public float shadowRadius() {
        int o = __offset(72);
        return o != 0 ? this.bb.getFloat(this.bb_pos + o) : 0.0f;
    }

    public String bgValue() {
        int o = __offset(74);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer bgValueAsByteBuffer() {
        return __vector_as_bytebuffer(74, 1);
    }

    public String bgFocusValue() {
        int o = __offset(76);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer bgFocusValueAsByteBuffer() {
        return __vector_as_bytebuffer(76, 1);
    }

    public short bgScaleType() {
        int o = __offset(78);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgVisible() {
        int o = __offset(80);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 1;
    }

    public short bgClipPadding() {
        int o = __offset(82);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 1;
    }

    public short bgW() {
        int o = __offset(84);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgH() {
        int o = __offset(86);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgPdL() {
        int o = __offset(88);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgPdR() {
        int o = __offset(90);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgPdT() {
        int o = __offset(92);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgPdB() {
        int o = __offset(94);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public String defaultText() {
        int o = __offset(96);
        return o != 0 ? __string(this.bb_pos + o) : null;
    }

    public ByteBuffer defaultTextAsByteBuffer() {
        return __vector_as_bytebuffer(96, 1);
    }

    public short bgGravity() {
        int o = __offset(98);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 5;
    }

    public short bgMgL() {
        int o = __offset(100);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgMgR() {
        int o = __offset(102);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgMgT() {
        int o = __offset(104);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public short bgMgB() {
        int o = __offset(106);
        return o != 0 ? this.bb.getShort(this.bb_pos + o) : (short) 0;
    }

    public static int createFlatStyle(FlatBufferBuilder builder, int valueOffset, short pd_l, short pd_r, short pd_t, short pd_b, short w, short h, short mg_l, short mg_r, short mg_t, short mg_b, short visible, short clip_padding, short clip_type, short scale_type, short gravity, int focus_valueOffset, int default_valueOffset, int textOffset, short font, float skew_x, short font_size, int font_colorOffset, int focus_font_colorOffset, short lines, short title_type, short line_space, short ellipsize, float marq_speed, short marq_delay, short marq_text_space, short shadow_dx, short shadow_dy, int shadow_colorOffset, float shadow_radius, int bg_valueOffset, int bg_focus_valueOffset, short bg_scale_type, short bg_visible, short bg_clip_padding, short bg_w, short bg_h, short bg_pd_l, short bg_pd_r, short bg_pd_t, short bg_pd_b, int default_textOffset, short bg_gravity, short bg_mg_l, short bg_mg_r, short bg_mg_t, short bg_mg_b) {
        builder.startObject(52);
        addDefaultText(builder, default_textOffset);
        addBgFocusValue(builder, bg_focus_valueOffset);
        addBgValue(builder, bg_valueOffset);
        addShadowRadius(builder, shadow_radius);
        addShadowColor(builder, shadow_colorOffset);
        addMarqSpeed(builder, marq_speed);
        addFocusFontColor(builder, focus_font_colorOffset);
        addFontColor(builder, font_colorOffset);
        addSkewX(builder, skew_x);
        addText(builder, textOffset);
        addDefaultValue(builder, default_valueOffset);
        addFocusValue(builder, focus_valueOffset);
        addValue(builder, valueOffset);
        addBgMgB(builder, bg_mg_b);
        addBgMgT(builder, bg_mg_t);
        addBgMgR(builder, bg_mg_r);
        addBgMgL(builder, bg_mg_l);
        addBgGravity(builder, bg_gravity);
        addBgPdB(builder, bg_pd_b);
        addBgPdT(builder, bg_pd_t);
        addBgPdR(builder, bg_pd_r);
        addBgPdL(builder, bg_pd_l);
        addBgH(builder, bg_h);
        addBgW(builder, bg_w);
        addBgClipPadding(builder, bg_clip_padding);
        addBgVisible(builder, bg_visible);
        addBgScaleType(builder, bg_scale_type);
        addShadowDy(builder, shadow_dy);
        addShadowDx(builder, shadow_dx);
        addMarqTextSpace(builder, marq_text_space);
        addMarqDelay(builder, marq_delay);
        addEllipsize(builder, ellipsize);
        addLineSpace(builder, line_space);
        addTitleType(builder, title_type);
        addLines(builder, lines);
        addFontSize(builder, font_size);
        addFont(builder, font);
        addGravity(builder, gravity);
        addScaleType(builder, scale_type);
        addClipType(builder, clip_type);
        addClipPadding(builder, clip_padding);
        addVisible(builder, visible);
        addMgB(builder, mg_b);
        addMgT(builder, mg_t);
        addMgR(builder, mg_r);
        addMgL(builder, mg_l);
        addH(builder, h);
        addW(builder, w);
        addPdB(builder, pd_b);
        addPdT(builder, pd_t);
        addPdR(builder, pd_r);
        addPdL(builder, pd_l);
        return endFlatStyle(builder);
    }

    public static void startFlatStyle(FlatBufferBuilder builder) {
        builder.startObject(52);
    }

    public static void addValue(FlatBufferBuilder builder, int valueOffset) {
        builder.addOffset(0, valueOffset, 0);
    }

    public static void addPdL(FlatBufferBuilder builder, short pdL) {
        builder.addShort(1, pdL, 0);
    }

    public static void addPdR(FlatBufferBuilder builder, short pdR) {
        builder.addShort(2, pdR, 0);
    }

    public static void addPdT(FlatBufferBuilder builder, short pdT) {
        builder.addShort(3, pdT, 0);
    }

    public static void addPdB(FlatBufferBuilder builder, short pdB) {
        builder.addShort(4, pdB, 0);
    }

    public static void addW(FlatBufferBuilder builder, short w) {
        builder.addShort(5, w, 0);
    }

    public static void addH(FlatBufferBuilder builder, short h) {
        builder.addShort(6, h, 0);
    }

    public static void addMgL(FlatBufferBuilder builder, short mgL) {
        builder.addShort(7, mgL, 0);
    }

    public static void addMgR(FlatBufferBuilder builder, short mgR) {
        builder.addShort(8, mgR, 0);
    }

    public static void addMgT(FlatBufferBuilder builder, short mgT) {
        builder.addShort(9, mgT, 0);
    }

    public static void addMgB(FlatBufferBuilder builder, short mgB) {
        builder.addShort(10, mgB, 0);
    }

    public static void addVisible(FlatBufferBuilder builder, short visible) {
        builder.addShort(11, visible, 1);
    }

    public static void addClipPadding(FlatBufferBuilder builder, short clipPadding) {
        builder.addShort(12, clipPadding, 1);
    }

    public static void addClipType(FlatBufferBuilder builder, short clipType) {
        builder.addShort(13, clipType, 0);
    }

    public static void addScaleType(FlatBufferBuilder builder, short scaleType) {
        builder.addShort(14, scaleType, 0);
    }

    public static void addGravity(FlatBufferBuilder builder, short gravity) {
        builder.addShort(15, gravity, 5);
    }

    public static void addFocusValue(FlatBufferBuilder builder, int focusValueOffset) {
        builder.addOffset(16, focusValueOffset, 0);
    }

    public static void addDefaultValue(FlatBufferBuilder builder, int defaultValueOffset) {
        builder.addOffset(17, defaultValueOffset, 0);
    }

    public static void addText(FlatBufferBuilder builder, int textOffset) {
        builder.addOffset(18, textOffset, 0);
    }

    public static void addFont(FlatBufferBuilder builder, short font) {
        builder.addShort(19, font, 0);
    }

    public static void addSkewX(FlatBufferBuilder builder, float skewX) {
        builder.addFloat(20, skewX, 0.25d);
    }

    public static void addFontSize(FlatBufferBuilder builder, short fontSize) {
        builder.addShort(21, fontSize, 0);
    }

    public static void addFontColor(FlatBufferBuilder builder, int fontColorOffset) {
        builder.addOffset(22, fontColorOffset, 0);
    }

    public static void addFocusFontColor(FlatBufferBuilder builder, int focusFontColorOffset) {
        builder.addOffset(23, focusFontColorOffset, 0);
    }

    public static void addLines(FlatBufferBuilder builder, short lines) {
        builder.addShort(24, lines, 1);
    }

    public static void addTitleType(FlatBufferBuilder builder, short titleType) {
        builder.addShort(25, titleType, 0);
    }

    public static void addLineSpace(FlatBufferBuilder builder, short lineSpace) {
        builder.addShort(26, lineSpace, 0);
    }

    public static void addEllipsize(FlatBufferBuilder builder, short ellipsize) {
        builder.addShort(27, ellipsize, 0);
    }

    public static void addMarqSpeed(FlatBufferBuilder builder, float marqSpeed) {
        builder.addFloat(28, marqSpeed, 1.0d);
    }

    public static void addMarqDelay(FlatBufferBuilder builder, short marqDelay) {
        builder.addShort(29, marqDelay, 500);
    }

    public static void addMarqTextSpace(FlatBufferBuilder builder, short marqTextSpace) {
        builder.addShort(30, marqTextSpace, 0);
    }

    public static void addShadowDx(FlatBufferBuilder builder, short shadowDx) {
        builder.addShort(31, shadowDx, 0);
    }

    public static void addShadowDy(FlatBufferBuilder builder, short shadowDy) {
        builder.addShort(32, shadowDy, 0);
    }

    public static void addShadowColor(FlatBufferBuilder builder, int shadowColorOffset) {
        builder.addOffset(33, shadowColorOffset, 0);
    }

    public static void addShadowRadius(FlatBufferBuilder builder, float shadowRadius) {
        builder.addFloat(34, shadowRadius, 0.0d);
    }

    public static void addBgValue(FlatBufferBuilder builder, int bgValueOffset) {
        builder.addOffset(35, bgValueOffset, 0);
    }

    public static void addBgFocusValue(FlatBufferBuilder builder, int bgFocusValueOffset) {
        builder.addOffset(36, bgFocusValueOffset, 0);
    }

    public static void addBgScaleType(FlatBufferBuilder builder, short bgScaleType) {
        builder.addShort(37, bgScaleType, 0);
    }

    public static void addBgVisible(FlatBufferBuilder builder, short bgVisible) {
        builder.addShort(38, bgVisible, 1);
    }

    public static void addBgClipPadding(FlatBufferBuilder builder, short bgClipPadding) {
        builder.addShort(39, bgClipPadding, 1);
    }

    public static void addBgW(FlatBufferBuilder builder, short bgW) {
        builder.addShort(40, bgW, 0);
    }

    public static void addBgH(FlatBufferBuilder builder, short bgH) {
        builder.addShort(41, bgH, 0);
    }

    public static void addBgPdL(FlatBufferBuilder builder, short bgPdL) {
        builder.addShort(42, bgPdL, 0);
    }

    public static void addBgPdR(FlatBufferBuilder builder, short bgPdR) {
        builder.addShort(43, bgPdR, 0);
    }

    public static void addBgPdT(FlatBufferBuilder builder, short bgPdT) {
        builder.addShort(44, bgPdT, 0);
    }

    public static void addBgPdB(FlatBufferBuilder builder, short bgPdB) {
        builder.addShort(45, bgPdB, 0);
    }

    public static void addDefaultText(FlatBufferBuilder builder, int defaultTextOffset) {
        builder.addOffset(46, defaultTextOffset, 0);
    }

    public static void addBgGravity(FlatBufferBuilder builder, short bgGravity) {
        builder.addShort(47, bgGravity, 5);
    }

    public static void addBgMgL(FlatBufferBuilder builder, short bgMgL) {
        builder.addShort(48, bgMgL, 0);
    }

    public static void addBgMgR(FlatBufferBuilder builder, short bgMgR) {
        builder.addShort(49, bgMgR, 0);
    }

    public static void addBgMgT(FlatBufferBuilder builder, short bgMgT) {
        builder.addShort(50, bgMgT, 0);
    }

    public static void addBgMgB(FlatBufferBuilder builder, short bgMgB) {
        builder.addShort(51, bgMgB, 0);
    }

    public static int endFlatStyle(FlatBufferBuilder builder) {
        return builder.endObject();
    }
}
