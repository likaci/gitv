package com.gala.afinal.core;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.AbstractList;
import java.util.Comparator;
import java.util.List;
import java.util.RandomAccess;

public class Arrays {
    static final /* synthetic */ boolean $assertionsDisabled = (!Arrays.class.desiredAssertionStatus());

    static class ArrayList<E> extends AbstractList<E> implements Serializable, List<E>, RandomAccess {
        private static final long serialVersionUID = -2764017481108945198L;
        private final E[] f57a;

        ArrayList(E[] storage) {
            if (storage == null) {
                throw new NullPointerException();
            }
            this.f57a = storage;
        }

        public boolean contains(Object object) {
            if (object != null) {
                for (Object equals : this.f57a) {
                    if (object.equals(equals)) {
                        return true;
                    }
                }
            } else {
                for (Object equals2 : this.f57a) {
                    if (equals2 == null) {
                        return true;
                    }
                }
            }
            return false;
        }

        public E get(int location) {
            try {
                return this.f57a[location];
            } catch (ArrayIndexOutOfBoundsException e) {
                throw e;
            }
        }

        public int indexOf(Object object) {
            int i = 0;
            if (object != null) {
                while (i < this.f57a.length) {
                    if (object.equals(this.f57a[i])) {
                        return i;
                    }
                    i++;
                }
            } else {
                while (i < this.f57a.length) {
                    if (this.f57a[i] == null) {
                        return i;
                    }
                    i++;
                }
            }
            return -1;
        }

        public int lastIndexOf(Object object) {
            int length;
            if (object != null) {
                for (length = this.f57a.length - 1; length >= 0; length--) {
                    if (object.equals(this.f57a[length])) {
                        return length;
                    }
                }
            } else {
                for (length = this.f57a.length - 1; length >= 0; length--) {
                    if (this.f57a[length] == null) {
                        return length;
                    }
                }
            }
            return -1;
        }

        public E set(int location, E object) {
            E e = this.f57a[location];
            this.f57a[location] = object;
            return e;
        }

        public int size() {
            return this.f57a.length;
        }

        public Object[] toArray() {
            return (Object[]) this.f57a.clone();
        }

        public <T> T[] toArray(T[] contents) {
            int size = size();
            if (size > contents.length) {
                contents = (Object[]) Array.newInstance(contents.getClass().getComponentType(), size);
            }
            System.arraycopy(this.f57a, 0, contents, 0, size);
            if (size < contents.length) {
                contents[size] = null;
            }
            return contents;
        }
    }

    private Arrays() {
    }

    public static <T> List<T> asList(T... array) {
        return new ArrayList(array);
    }

    public static int binarySearch(byte[] array, byte value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(byte[] array, int startIndex, int endIndex, byte value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            byte b = array[i];
            if (b < value) {
                startIndex = i + 1;
            } else if (b <= value) {
                return i;
            } else {
                endIndex = i - 1;
            }
        }
        return startIndex ^ -1;
    }

    public static int binarySearch(char[] array, char value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(char[] array, int startIndex, int endIndex, char value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            char c = array[i];
            if (c < value) {
                startIndex = i + 1;
            } else if (c <= value) {
                return i;
            } else {
                endIndex = i - 1;
            }
        }
        return startIndex ^ -1;
    }

    public static int binarySearch(double[] array, double value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(double[] array, int startIndex, int endIndex, double value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            double d = array[i];
            if (d < value) {
                startIndex = i + 1;
            } else if (d > value) {
                endIndex = i - 1;
            } else if (d != 0.0d && d == value) {
                return i;
            } else {
                long doubleToLongBits = Double.doubleToLongBits(d);
                long doubleToLongBits2 = Double.doubleToLongBits(value);
                if (doubleToLongBits < doubleToLongBits2) {
                    startIndex = i + 1;
                } else if (doubleToLongBits <= doubleToLongBits2) {
                    return i;
                } else {
                    endIndex = i - 1;
                }
            }
        }
        return startIndex ^ -1;
    }

    public static int binarySearch(float[] array, float value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(float[] array, int startIndex, int endIndex, float value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            float f = array[i];
            if (f < value) {
                startIndex = i + 1;
            } else if (f > value) {
                endIndex = i - 1;
            } else if (f != 0.0f && f == value) {
                return i;
            } else {
                int floatToIntBits = Float.floatToIntBits(f);
                int floatToIntBits2 = Float.floatToIntBits(value);
                if (floatToIntBits < floatToIntBits2) {
                    startIndex = i + 1;
                } else if (floatToIntBits <= floatToIntBits2) {
                    return i;
                } else {
                    endIndex = i - 1;
                }
            }
        }
        return startIndex ^ -1;
    }

    public static int binarySearch(int[] array, int value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(int[] array, int startIndex, int endIndex, int value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            int i2 = array[i];
            if (i2 < value) {
                startIndex = i + 1;
            } else if (i2 <= value) {
                return i;
            } else {
                endIndex = i - 1;
            }
        }
        return startIndex ^ -1;
    }

    public static int binarySearch(long[] array, long value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(long[] array, int startIndex, int endIndex, long value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            long j = array[i];
            if (j < value) {
                startIndex = i + 1;
            } else if (j <= value) {
                return i;
            } else {
                endIndex = i - 1;
            }
        }
        return startIndex ^ -1;
    }

    public static int binarySearch(Object[] array, Object value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(Object[] array, int startIndex, int endIndex, Object value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            int compareTo = ((Comparable) array[i]).compareTo(value);
            if (compareTo < 0) {
                startIndex = i + 1;
            } else if (compareTo <= 0) {
                return i;
            } else {
                endIndex = i - 1;
            }
        }
        return startIndex ^ -1;
    }

    public static <T> int binarySearch(T[] array, T value, Comparator<? super T> comparator) {
        return binarySearch(array, 0, array.length, value, comparator);
    }

    public static <T> int binarySearch(T[] array, int startIndex, int endIndex, T value, Comparator<? super T> comparator) {
        if (comparator == null) {
            return binarySearch((Object[]) array, startIndex, endIndex, (Object) value);
        }
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            int compare = comparator.compare(array[i], value);
            if (compare < 0) {
                startIndex = i + 1;
            } else if (compare <= 0) {
                return i;
            } else {
                endIndex = i - 1;
            }
        }
        return startIndex ^ -1;
    }

    public static int binarySearch(short[] array, short value) {
        return binarySearch(array, 0, array.length, value);
    }

    public static int binarySearch(short[] array, int startIndex, int endIndex, short value) {
        checkBinarySearchBounds(startIndex, endIndex, array.length);
        endIndex--;
        while (startIndex <= endIndex) {
            int i = (startIndex + endIndex) >>> 1;
            short s = array[i];
            if (s < value) {
                startIndex = i + 1;
            } else if (s <= value) {
                return i;
            } else {
                endIndex = i - 1;
            }
        }
        return startIndex ^ -1;
    }

    private static void checkBinarySearchBounds(int startIndex, int endIndex, int length) {
        if (startIndex > endIndex) {
            throw new IllegalArgumentException();
        } else if (startIndex < 0 || endIndex > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    public static void fill(byte[] array, byte value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value;
        }
    }

    public static void fill(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value;
        }
    }

    public static void fill(boolean[] array, boolean value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value;
        }
    }

    public static void fill(Object[] array, Object value) {
        for (int i = 0; i < array.length; i++) {
            array[i] = value;
        }
    }

    public static int hashCode(boolean[] array) {
        int i = 0;
        if (array != null) {
            i = 1;
            int i2 = 0;
            while (i2 < array.length) {
                i2++;
                i = (i * 31) + (array[i2] ? 1231 : 1237);
            }
        }
        return i;
    }

    public static int hashCode(int[] array) {
        int i = 0;
        if (array != null) {
            i = 1;
            int i2 = 0;
            while (i2 < array.length) {
                int i3 = array[i2] + (i * 31);
                i2++;
                i = i3;
            }
        }
        return i;
    }

    public static int hashCode(short[] array) {
        int i = 0;
        if (array != null) {
            i = 1;
            int i2 = 0;
            while (i2 < array.length) {
                int i3 = array[i2] + (i * 31);
                i2++;
                i = i3;
            }
        }
        return i;
    }

    public static int hashCode(char[] array) {
        int i = 0;
        if (array != null) {
            i = 1;
            int i2 = 0;
            while (i2 < array.length) {
                int i3 = array[i2] + (i * 31);
                i2++;
                i = i3;
            }
        }
        return i;
    }

    public static int hashCode(byte[] array) {
        int i = 0;
        if (array != null) {
            i = 1;
            int i2 = 0;
            while (i2 < array.length) {
                int i3 = array[i2] + (i * 31);
                i2++;
                i = i3;
            }
        }
        return i;
    }

    public static int hashCode(long[] array) {
        int i = 0;
        if (array != null) {
            int length = array.length;
            i = 1;
            int i2 = 0;
            while (i2 < length) {
                long j = array[i2];
                i2++;
                i = ((int) (j ^ (j >>> 32))) + (i * 31);
            }
        }
        return i;
    }

    public static int hashCode(float[] array) {
        int i = 0;
        if (array != null) {
            i = 1;
            int i2 = 0;
            while (i2 < array.length) {
                int floatToIntBits = Float.floatToIntBits(array[i2]) + (i * 31);
                i2++;
                i = floatToIntBits;
            }
        }
        return i;
    }

    public static int hashCode(double[] array) {
        int i = 0;
        if (array != null) {
            int length = array.length;
            i = 1;
            int i2 = 0;
            while (i2 < length) {
                long doubleToLongBits = Double.doubleToLongBits(array[i2]);
                i2++;
                i = ((int) (doubleToLongBits ^ (doubleToLongBits >>> 32))) + (i * 31);
            }
        }
        return i;
    }

    public static int hashCode(Object[] array) {
        if (array == null) {
            return 0;
        }
        int i = 1;
        for (Object obj : array) {
            int i2;
            if (obj == null) {
                i2 = 0;
            } else {
                i2 = obj.hashCode();
            }
            i = (i * 31) + i2;
        }
        return i;
    }

    public static int deepHashCode(Object[] array) {
        int i = 0;
        if (array != null) {
            i = 1;
            int i2 = 0;
            while (i2 < array.length) {
                int deepHashCodeElement = deepHashCodeElement(array[i2]) + (i * 31);
                i2++;
                i = deepHashCodeElement;
            }
        }
        return i;
    }

    private static int deepHashCodeElement(Object element) {
        if (element == null) {
            return 0;
        }
        Class componentType = element.getClass().getComponentType();
        if (componentType == null) {
            return element.hashCode();
        }
        if (!componentType.isPrimitive()) {
            return deepHashCode((Object[]) element);
        }
        if (componentType.equals(Integer.TYPE)) {
            return hashCode((int[]) element);
        }
        if (componentType.equals(Character.TYPE)) {
            return hashCode((char[]) element);
        }
        if (componentType.equals(Boolean.TYPE)) {
            return hashCode((boolean[]) element);
        }
        if (componentType.equals(Byte.TYPE)) {
            return hashCode((byte[]) element);
        }
        if (componentType.equals(Long.TYPE)) {
            return hashCode((long[]) element);
        }
        if (componentType.equals(Float.TYPE)) {
            return hashCode((float[]) element);
        }
        if (componentType.equals(Double.TYPE)) {
            return hashCode((double[]) element);
        }
        return hashCode((short[]) element);
    }

    public static boolean equals(byte[] array1, byte[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(short[] array1, short[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(char[] array1, char[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(int[] array1, int[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(long[] array1, long[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(float[] array1, float[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (Float.floatToIntBits(array1[i]) != Float.floatToIntBits(array2[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(double[] array1, double[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (Double.doubleToLongBits(array1[i]) != Double.doubleToLongBits(array2[i])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(boolean[] array1, boolean[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (array1[i] != array2[i]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Object[] array1, Object[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            Object obj = array1[i];
            Object obj2 = array2[i];
            if (obj == null) {
                if (obj2 != null) {
                    return false;
                }
            } else if (!obj.equals(obj2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean deepEquals(Object[] array1, Object[] array2) {
        if (array1 == array2) {
            return true;
        }
        if (array1 == null || array2 == null || array1.length != array2.length) {
            return false;
        }
        for (int i = 0; i < array1.length; i++) {
            if (!deepEqualsElements(array1[i], array2[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean deepEqualsElements(Object e1, Object e2) {
        if (e1 == e2) {
            return true;
        }
        if (e1 == null || e2 == null) {
            return false;
        }
        Class componentType = e1.getClass().getComponentType();
        if (componentType != e2.getClass().getComponentType()) {
            return false;
        }
        if (componentType == null) {
            return e1.equals(e2);
        }
        if (!componentType.isPrimitive()) {
            return deepEquals((Object[]) e1, (Object[]) e2);
        }
        if (componentType.equals(Integer.TYPE)) {
            return equals((int[]) e1, (int[]) e2);
        }
        if (componentType.equals(Character.TYPE)) {
            return equals((char[]) e1, (char[]) e2);
        }
        if (componentType.equals(Boolean.TYPE)) {
            return equals((boolean[]) e1, (boolean[]) e2);
        }
        if (componentType.equals(Byte.TYPE)) {
            return equals((byte[]) e1, (byte[]) e2);
        }
        if (componentType.equals(Long.TYPE)) {
            return equals((long[]) e1, (long[]) e2);
        }
        if (componentType.equals(Float.TYPE)) {
            return equals((float[]) e1, (float[]) e2);
        }
        if (componentType.equals(Double.TYPE)) {
            return equals((double[]) e1, (double[]) e2);
        }
        return equals((short[]) e1, (short[]) e2);
    }

    public static String toString(boolean[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 7);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(byte[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 6);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(char[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 3);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(double[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 7);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(float[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 7);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(int[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 6);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(long[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 6);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(short[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 6);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String toString(Object[] array) {
        if (array == null) {
            return "null";
        }
        if (array.length == 0) {
            return "[]";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 7);
        stringBuilder.append('[');
        stringBuilder.append(array[0]);
        for (int i = 1; i < array.length; i++) {
            stringBuilder.append(", ");
            stringBuilder.append(array[i]);
        }
        stringBuilder.append(']');
        return stringBuilder.toString();
    }

    public static String deepToString(Object[] array) {
        if (array == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder(array.length * 9);
        deepToStringImpl(array, new Object[]{array}, stringBuilder);
        return stringBuilder.toString();
    }

    private static void deepToStringImpl(Object[] array, Object[] origArrays, StringBuilder sb) {
        if (array == null) {
            sb.append("null");
            return;
        }
        sb.append('[');
        for (int i = 0; i < array.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            Object obj = array[i];
            if (obj == null) {
                sb.append("null");
            } else {
                Class cls = obj.getClass();
                if (cls.isArray()) {
                    cls = cls.getComponentType();
                    if (cls.isPrimitive()) {
                        if (Boolean.TYPE.equals(cls)) {
                            sb.append(toString((boolean[]) obj));
                        } else if (Byte.TYPE.equals(cls)) {
                            sb.append(toString((byte[]) obj));
                        } else if (Character.TYPE.equals(cls)) {
                            sb.append(toString((char[]) obj));
                        } else if (Double.TYPE.equals(cls)) {
                            sb.append(toString((double[]) obj));
                        } else if (Float.TYPE.equals(cls)) {
                            sb.append(toString((float[]) obj));
                        } else if (Integer.TYPE.equals(cls)) {
                            sb.append(toString((int[]) obj));
                        } else if (Long.TYPE.equals(cls)) {
                            sb.append(toString((long[]) obj));
                        } else if (Short.TYPE.equals(cls)) {
                            sb.append(toString((short[]) obj));
                        } else {
                            throw new AssertionError();
                        }
                    } else if (!$assertionsDisabled && !(obj instanceof Object[])) {
                        throw new AssertionError();
                    } else if (deepToStringImplContains(origArrays, obj)) {
                        sb.append("[...]");
                    } else {
                        Object[] objArr = (Object[]) obj;
                        Object obj2 = new Object[(origArrays.length + 1)];
                        System.arraycopy(origArrays, 0, obj2, 0, origArrays.length);
                        obj2[origArrays.length] = objArr;
                        deepToStringImpl(objArr, obj2, sb);
                    }
                } else {
                    sb.append(array[i]);
                }
            }
        }
        sb.append(']');
    }

    private static boolean deepToStringImplContains(Object[] origArrays, Object array) {
        if (origArrays == null || origArrays.length == 0) {
            return false;
        }
        for (Object obj : origArrays) {
            if (obj == array) {
                return true;
            }
        }
        return false;
    }

    public static boolean[] copyOf(boolean[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static byte[] copyOf(byte[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static char[] copyOf(char[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static double[] copyOf(double[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static float[] copyOf(float[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static int[] copyOf(int[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static long[] copyOf(long[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static short[] copyOf(short[] original, int newLength) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength);
        }
        throw new NegativeArraySizeException();
    }

    public static <T> T[] copyOf(T[] original, int newLength) {
        if (original == null) {
            throw new NullPointerException();
        } else if (newLength >= 0) {
            return copyOfRange((Object[]) original, 0, newLength);
        } else {
            throw new NegativeArraySizeException();
        }
    }

    public static <T, U> T[] copyOf(U[] original, int newLength, Class<? extends T[]> newType) {
        if (newLength >= 0) {
            return copyOfRange(original, 0, newLength, newType);
        }
        throw new NegativeArraySizeException();
    }

    public static boolean[] copyOfRange(boolean[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new boolean[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static byte[] copyOfRange(byte[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new byte[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static char[] copyOfRange(char[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new char[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static double[] copyOfRange(double[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new double[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static float[] copyOfRange(float[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new float[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static int[] copyOfRange(int[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new int[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static long[] copyOfRange(long[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new long[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static short[] copyOfRange(short[] original, int start, int end) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        length = Math.min(i, length - start);
        Object obj = new short[i];
        System.arraycopy(original, start, obj, 0, length);
        return obj;
    }

    public static <T> T[] copyOfRange(T[] original, int start, int end) {
        int length = original.length;
        if (start > end) {
            throw new IllegalArgumentException();
        } else if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        } else {
            int i = end - start;
            int min = Math.min(i, length - start);
            Object[] objArr = (Object[]) Array.newInstance(original.getClass().getComponentType(), i);
            System.arraycopy(original, start, objArr, 0, min);
            return objArr;
        }
    }

    public static <T, U> T[] copyOfRange(U[] original, int start, int end, Class<? extends T[]> newType) {
        if (start > end) {
            throw new IllegalArgumentException();
        }
        int length = original.length;
        if (start < 0 || start > length) {
            throw new ArrayIndexOutOfBoundsException();
        }
        int i = end - start;
        int min = Math.min(i, length - start);
        Object[] objArr = (Object[]) Array.newInstance(newType.getComponentType(), i);
        System.arraycopy(original, start, objArr, 0, min);
        return objArr;
    }
}
