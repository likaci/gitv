package com.google.flatbuffers;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;

public class FlatBufferBuilder {
    static final /* synthetic */ boolean $assertionsDisabled = (!FlatBufferBuilder.class.desiredAssertionStatus());
    static final Charset utf8charset = Charset.forName("UTF-8");
    ByteBuffer bb;
    ByteBuffer dst;
    CharsetEncoder encoder;
    boolean finished;
    boolean force_defaults;
    int minalign;
    boolean nested;
    int num_vtables;
    int object_start;
    int space;
    int vector_num_elems;
    int[] vtable;
    int vtable_in_use;
    int[] vtables;

    public FlatBufferBuilder(int initial_size) {
        this.minalign = 1;
        this.vtable = null;
        this.vtable_in_use = 0;
        this.nested = false;
        this.finished = false;
        this.vtables = new int[16];
        this.num_vtables = 0;
        this.vector_num_elems = 0;
        this.force_defaults = false;
        this.encoder = utf8charset.newEncoder();
        if (initial_size <= 0) {
            initial_size = 1;
        }
        this.space = initial_size;
        this.bb = newByteBuffer(initial_size);
    }

    public FlatBufferBuilder() {
        this(1024);
    }

    public FlatBufferBuilder(ByteBuffer existing_bb) {
        this.minalign = 1;
        this.vtable = null;
        this.vtable_in_use = 0;
        this.nested = false;
        this.finished = false;
        this.vtables = new int[16];
        this.num_vtables = 0;
        this.vector_num_elems = 0;
        this.force_defaults = false;
        this.encoder = utf8charset.newEncoder();
        init(existing_bb);
    }

    public FlatBufferBuilder init(ByteBuffer existing_bb) {
        this.bb = existing_bb;
        this.bb.clear();
        this.bb.order(ByteOrder.LITTLE_ENDIAN);
        this.minalign = 1;
        this.space = this.bb.capacity();
        this.vtable_in_use = 0;
        this.nested = false;
        this.finished = false;
        this.object_start = 0;
        this.num_vtables = 0;
        this.vector_num_elems = 0;
        return this;
    }

    public void clear() {
        this.space = this.bb.capacity();
        this.bb.clear();
        this.minalign = 1;
        while (this.vtable_in_use > 0) {
            int[] iArr = this.vtable;
            int i = this.vtable_in_use - 1;
            this.vtable_in_use = i;
            iArr[i] = 0;
        }
        this.vtable_in_use = 0;
        this.nested = false;
        this.finished = false;
        this.object_start = 0;
        this.num_vtables = 0;
        this.vector_num_elems = 0;
    }

    static ByteBuffer newByteBuffer(int capacity) {
        ByteBuffer newbb = ByteBuffer.allocate(capacity);
        newbb.order(ByteOrder.LITTLE_ENDIAN);
        return newbb;
    }

    static ByteBuffer growByteBuffer(ByteBuffer bb) {
        int old_buf_size = bb.capacity();
        if ((-1073741824 & old_buf_size) != 0) {
            throw new AssertionError("FlatBuffers: cannot grow buffer beyond 2 gigabytes.");
        }
        int new_buf_size = old_buf_size << 1;
        bb.position(0);
        ByteBuffer nbb = newByteBuffer(new_buf_size);
        nbb.position(new_buf_size - old_buf_size);
        nbb.put(bb);
        return nbb;
    }

    public int offset() {
        return this.bb.capacity() - this.space;
    }

    public void pad(int byte_size) {
        for (int i = 0; i < byte_size; i++) {
            ByteBuffer byteBuffer = this.bb;
            int i2 = this.space - 1;
            this.space = i2;
            byteBuffer.put(i2, (byte) 0);
        }
    }

    public void prep(int size, int additional_bytes) {
        if (size > this.minalign) {
            this.minalign = size;
        }
        int align_size = ((((this.bb.capacity() - this.space) + additional_bytes) ^ -1) + 1) & (size - 1);
        while (this.space < (align_size + size) + additional_bytes) {
            int old_buf_size = this.bb.capacity();
            this.bb = growByteBuffer(this.bb);
            this.space += this.bb.capacity() - old_buf_size;
        }
        pad(align_size);
    }

    public void putBoolean(boolean x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 1;
        this.space = i;
        byteBuffer.put(i, (byte) (x ? 1 : 0));
    }

    public void putByte(byte x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 1;
        this.space = i;
        byteBuffer.put(i, x);
    }

    public void putShort(short x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 2;
        this.space = i;
        byteBuffer.putShort(i, x);
    }

    public void putInt(int x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 4;
        this.space = i;
        byteBuffer.putInt(i, x);
    }

    public void putLong(long x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 8;
        this.space = i;
        byteBuffer.putLong(i, x);
    }

    public void putFloat(float x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 4;
        this.space = i;
        byteBuffer.putFloat(i, x);
    }

    public void putDouble(double x) {
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - 8;
        this.space = i;
        byteBuffer.putDouble(i, x);
    }

    public void addBoolean(boolean x) {
        prep(1, 0);
        putBoolean(x);
    }

    public void addByte(byte x) {
        prep(1, 0);
        putByte(x);
    }

    public void addShort(short x) {
        prep(2, 0);
        putShort(x);
    }

    public void addInt(int x) {
        prep(4, 0);
        putInt(x);
    }

    public void addLong(long x) {
        prep(8, 0);
        putLong(x);
    }

    public void addFloat(float x) {
        prep(4, 0);
        putFloat(x);
    }

    public void addDouble(double x) {
        prep(8, 0);
        putDouble(x);
    }

    public void addOffset(int off) {
        prep(4, 0);
        if ($assertionsDisabled || off <= offset()) {
            putInt((offset() - off) + 4);
            return;
        }
        throw new AssertionError();
    }

    public void startVector(int elem_size, int num_elems, int alignment) {
        notNested();
        this.vector_num_elems = num_elems;
        prep(4, elem_size * num_elems);
        prep(alignment, elem_size * num_elems);
        this.nested = true;
    }

    public int endVector() {
        if (this.nested) {
            this.nested = false;
            putInt(this.vector_num_elems);
            return offset();
        }
        throw new AssertionError("FlatBuffers: endVector called without startVector");
    }

    public ByteBuffer createUnintializedVector(int elem_size, int num_elems, int alignment) {
        int length = elem_size * num_elems;
        startVector(elem_size, num_elems, alignment);
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - length;
        this.space = i;
        byteBuffer.position(i);
        ByteBuffer copy = this.bb.slice().order(ByteOrder.LITTLE_ENDIAN);
        copy.limit(length);
        return copy;
    }

    public int createVectorOfTables(int[] offsets) {
        notNested();
        startVector(4, offsets.length, 4);
        for (int i = offsets.length - 1; i >= 0; i--) {
            addOffset(offsets[i]);
        }
        return endVector();
    }

    public <T extends Table> int createSortedVectorOfTables(T obj, int[] offsets) {
        obj.sortTables(offsets, this.bb);
        return createVectorOfTables(offsets);
    }

    public int createString(CharSequence s) {
        CharBuffer src;
        int estimatedDstCapacity = (int) (((float) s.length()) * this.encoder.maxBytesPerChar());
        if (this.dst == null || this.dst.capacity() < estimatedDstCapacity) {
            this.dst = ByteBuffer.allocate(Math.max(128, estimatedDstCapacity));
        }
        this.dst.clear();
        if (s instanceof CharBuffer) {
            src = (CharBuffer) s;
        } else {
            src = CharBuffer.wrap(s);
        }
        CoderResult result = this.encoder.encode(src, this.dst, true);
        if (result.isError()) {
            try {
                result.throwException();
            } catch (CharacterCodingException x) {
                throw new Error(x);
            }
        }
        this.dst.flip();
        return createString(this.dst);
    }

    public int createString(ByteBuffer s) {
        int length = s.remaining();
        addByte((byte) 0);
        startVector(1, length, 1);
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - length;
        this.space = i;
        byteBuffer.position(i);
        this.bb.put(s);
        return endVector();
    }

    public int createByteVector(byte[] arr) {
        int length = arr.length;
        startVector(1, length, 1);
        ByteBuffer byteBuffer = this.bb;
        int i = this.space - length;
        this.space = i;
        byteBuffer.position(i);
        this.bb.put(arr);
        return endVector();
    }

    public void finished() {
        if (!this.finished) {
            throw new AssertionError("FlatBuffers: you can only access the serialized buffer after it has been finished by FlatBufferBuilder.finish().");
        }
    }

    public void notNested() {
        if (this.nested) {
            throw new AssertionError("FlatBuffers: object serialization must not be nested.");
        }
    }

    public void Nested(int obj) {
        if (obj != offset()) {
            throw new AssertionError("FlatBuffers: struct must be serialized inline.");
        }
    }

    public void startObject(int numfields) {
        notNested();
        if (this.vtable == null || this.vtable.length < numfields) {
            this.vtable = new int[numfields];
        }
        this.vtable_in_use = numfields;
        Arrays.fill(this.vtable, 0, this.vtable_in_use, 0);
        this.nested = true;
        this.object_start = offset();
    }

    public void addBoolean(int o, boolean x, boolean d) {
        if (this.force_defaults || x != d) {
            addBoolean(x);
            slot(o);
        }
    }

    public void addByte(int o, byte x, int d) {
        if (this.force_defaults || x != d) {
            addByte(x);
            slot(o);
        }
    }

    public void addShort(int o, short x, int d) {
        if (this.force_defaults || x != d) {
            addShort(x);
            slot(o);
        }
    }

    public void addInt(int o, int x, int d) {
        if (this.force_defaults || x != d) {
            addInt(x);
            slot(o);
        }
    }

    public void addLong(int o, long x, long d) {
        if (this.force_defaults || x != d) {
            addLong(x);
            slot(o);
        }
    }

    public void addFloat(int o, float x, double d) {
        if (this.force_defaults || ((double) x) != d) {
            addFloat(x);
            slot(o);
        }
    }

    public void addDouble(int o, double x, double d) {
        if (this.force_defaults || x != d) {
            addDouble(x);
            slot(o);
        }
    }

    public void addOffset(int o, int x, int d) {
        if (this.force_defaults || x != d) {
            addOffset(x);
            slot(o);
        }
    }

    public void addStruct(int voffset, int x, int d) {
        if (x != d) {
            Nested(x);
            slot(voffset);
        }
    }

    public void slot(int voffset) {
        this.vtable[voffset] = offset();
    }

    public int endObject() {
        if (this.vtable == null || !this.nested) {
            throw new AssertionError("FlatBuffers: endObject called without startObject");
        }
        int i;
        addInt(0);
        int vtableloc = offset();
        for (i = this.vtable_in_use - 1; i >= 0; i--) {
            int i2;
            if (this.vtable[i] != 0) {
                i2 = vtableloc - this.vtable[i];
            } else {
                i2 = 0;
            }
            addShort((short) i2);
        }
        addShort((short) (vtableloc - this.object_start));
        addShort((short) ((this.vtable_in_use + 2) * 2));
        int existing_vtable = 0;
        loop1:
        for (i = 0; i < this.num_vtables; i++) {
            int vt1 = this.bb.capacity() - this.vtables[i];
            int vt2 = this.space;
            short len = this.bb.getShort(vt1);
            if (len == this.bb.getShort(vt2)) {
                short j = (short) 2;
                while (j < len) {
                    if (this.bb.getShort(vt1 + j) == this.bb.getShort(vt2 + j)) {
                        j += 2;
                    }
                }
                existing_vtable = this.vtables[i];
                break loop1;
            }
        }
        if (existing_vtable != 0) {
            this.space = this.bb.capacity() - vtableloc;
            this.bb.putInt(this.space, existing_vtable - vtableloc);
        } else {
            if (this.num_vtables == this.vtables.length) {
                this.vtables = Arrays.copyOf(this.vtables, this.num_vtables * 2);
            }
            int[] iArr = this.vtables;
            int i3 = this.num_vtables;
            this.num_vtables = i3 + 1;
            iArr[i3] = offset();
            this.bb.putInt(this.bb.capacity() - vtableloc, offset() - vtableloc);
        }
        this.nested = false;
        return vtableloc;
    }

    public void required(int table, int field) {
        int table_start = this.bb.capacity() - table;
        if (!(this.bb.getShort((table_start - this.bb.getInt(table_start)) + field) != (short) 0)) {
            throw new AssertionError("FlatBuffers: field " + field + " must be set");
        }
    }

    public void finish(int root_table) {
        prep(this.minalign, 4);
        addOffset(root_table);
        this.bb.position(this.space);
        this.finished = true;
    }

    public void finish(int root_table, String file_identifier) {
        prep(this.minalign, 8);
        if (file_identifier.length() != 4) {
            throw new AssertionError("FlatBuffers: file identifier must be length 4");
        }
        for (int i = 3; i >= 0; i--) {
            addByte((byte) file_identifier.charAt(i));
        }
        finish(root_table);
    }

    public FlatBufferBuilder forceDefaults(boolean forceDefaults) {
        this.force_defaults = forceDefaults;
        return this;
    }

    public ByteBuffer dataBuffer() {
        finished();
        return this.bb;
    }

    @Deprecated
    private int dataStart() {
        finished();
        return this.space;
    }

    public byte[] sizedByteArray(int start, int length) {
        finished();
        byte[] array = new byte[length];
        this.bb.position(start);
        this.bb.get(array);
        return array;
    }

    public byte[] sizedByteArray() {
        return sizedByteArray(this.space, this.bb.capacity() - this.space);
    }
}
