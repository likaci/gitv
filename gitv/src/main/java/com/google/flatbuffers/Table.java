package com.google.flatbuffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;
import java.util.Comparator;

public class Table {
    private static final ThreadLocal<CharBuffer> CHAR_BUFFER = new ThreadLocal();
    public static final ThreadLocal<Charset> UTF8_CHARSET = new ThreadLocal<Charset>() {
        protected Charset initialValue() {
            return Charset.forName("UTF-8");
        }
    };
    private static final ThreadLocal<CharsetDecoder> UTF8_DECODER = new ThreadLocal<CharsetDecoder>() {
        protected CharsetDecoder initialValue() {
            return Charset.forName("UTF-8").newDecoder();
        }
    };
    protected ByteBuffer bb;
    protected int bb_pos;

    public ByteBuffer getByteBuffer() {
        return this.bb;
    }

    protected int __offset(int vtable_offset) {
        int vtable = this.bb_pos - this.bb.getInt(this.bb_pos);
        return vtable_offset < this.bb.getShort(vtable) ? this.bb.getShort(vtable + vtable_offset) : 0;
    }

    protected static int __offset(int vtable_offset, int offset, ByteBuffer bb) {
        int vtable = bb.array().length - offset;
        return bb.getShort((vtable + vtable_offset) - bb.getInt(vtable)) + vtable;
    }

    protected int __indirect(int offset) {
        return this.bb.getInt(offset) + offset;
    }

    protected static int __indirect(int offset, ByteBuffer bb) {
        return bb.getInt(offset) + offset;
    }

    protected String __string(int offset) {
        CharsetDecoder decoder = (CharsetDecoder) UTF8_DECODER.get();
        decoder.reset();
        offset += this.bb.getInt(offset);
        ByteBuffer src = this.bb.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        int length = src.getInt(offset);
        src.position(offset + 4);
        src.limit((offset + 4) + length);
        int required = (int) (((float) length) * decoder.maxCharsPerByte());
        CharBuffer dst = (CharBuffer) CHAR_BUFFER.get();
        if (dst == null || dst.capacity() < required) {
            dst = CharBuffer.allocate(required);
            CHAR_BUFFER.set(dst);
        }
        dst.clear();
        try {
            CoderResult cr = decoder.decode(src, dst, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            return dst.flip().toString();
        } catch (CharacterCodingException x) {
            throw new Error(x);
        }
    }

    protected int __vector_len(int offset) {
        offset += this.bb_pos;
        return this.bb.getInt(offset + this.bb.getInt(offset));
    }

    protected int __vector(int offset) {
        offset += this.bb_pos;
        return (this.bb.getInt(offset) + offset) + 4;
    }

    protected ByteBuffer __vector_as_bytebuffer(int vector_offset, int elem_size) {
        int o = __offset(vector_offset);
        if (o == 0) {
            return null;
        }
        ByteBuffer bb = this.bb.duplicate().order(ByteOrder.LITTLE_ENDIAN);
        int vectorstart = __vector(o);
        bb.position(vectorstart);
        bb.limit((__vector_len(o) * elem_size) + vectorstart);
        return bb;
    }

    protected Table __union(Table t, int offset) {
        offset += this.bb_pos;
        t.bb_pos = this.bb.getInt(offset) + offset;
        t.bb = this.bb;
        return t;
    }

    protected static boolean __has_identifier(ByteBuffer bb, String ident) {
        if (ident.length() != 4) {
            throw new AssertionError("FlatBuffers: file identifier must be length 4");
        }
        for (int i = 0; i < 4; i++) {
            if (ident.charAt(i) != ((char) bb.get((bb.position() + 4) + i))) {
                return false;
            }
        }
        return true;
    }

    protected void sortTables(int[] offsets, final ByteBuffer bb) {
        int i;
        Integer[] off = new Integer[offsets.length];
        for (i = 0; i < offsets.length; i++) {
            off[i] = Integer.valueOf(offsets[i]);
        }
        Arrays.sort(off, new Comparator<Integer>() {
            public int compare(Integer o1, Integer o2) {
                return Table.this.keysCompare(o1, o2, bb);
            }
        });
        for (i = 0; i < offsets.length; i++) {
            offsets[i] = off[i].intValue();
        }
    }

    protected int keysCompare(Integer o1, Integer o2, ByteBuffer bb) {
        return 0;
    }

    protected static int compareStrings(int offset_1, int offset_2, ByteBuffer bb) {
        offset_1 += bb.getInt(offset_1);
        offset_2 += bb.getInt(offset_2);
        int len_1 = bb.getInt(offset_1);
        int len_2 = bb.getInt(offset_2);
        int startPos_1 = offset_1 + 4;
        int startPos_2 = offset_2 + 4;
        int len = Math.min(len_1, len_2);
        byte[] bbArray = bb.array();
        for (int i = 0; i < len; i++) {
            if (bbArray[i + startPos_1] != bbArray[i + startPos_2]) {
                return bbArray[i + startPos_1] - bbArray[i + startPos_2];
            }
        }
        return len_1 - len_2;
    }

    protected static int compareStrings(int offset_1, byte[] key, ByteBuffer bb) {
        offset_1 += bb.getInt(offset_1);
        int len_1 = bb.getInt(offset_1);
        int len_2 = key.length;
        int startPos_1 = offset_1 + 4;
        int len = Math.min(len_1, len_2);
        byte[] bbArray = bb.array();
        for (int i = 0; i < len; i++) {
            if (bbArray[i + startPos_1] != key[i]) {
                return bbArray[i + startPos_1] - key[i];
            }
        }
        return len_1 - len_2;
    }
}
