package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONLexerBase;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.util.FieldInfo;
import com.alibaba.fastjson.util.JavaBeanInfo;
import com.alibaba.fastjson.util.TypeUtils;
import com.gala.video.lib.share.common.model.multiscreen.MultiScreenParams;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;

public class JavaBeanDeserializer implements ObjectDeserializer {
    public final JavaBeanInfo beanInfo;
    protected final Class<?> clazz;
    private final FieldDeserializer[] fieldDeserializers;
    protected final FieldDeserializer[] sortedFieldDeserializers;

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz) {
        this(config, clazz, clazz);
    }

    public JavaBeanDeserializer(ParserConfig config, Class<?> clazz, Type type) {
        int i;
        this.clazz = clazz;
        this.beanInfo = JavaBeanInfo.build(clazz, type);
        this.sortedFieldDeserializers = new FieldDeserializer[this.beanInfo.sortedFields.length];
        int size = this.beanInfo.sortedFields.length;
        for (i = 0; i < size; i++) {
            this.sortedFieldDeserializers[i] = config.createFieldDeserializer(config, this.beanInfo, this.beanInfo.sortedFields[i]);
        }
        this.fieldDeserializers = new FieldDeserializer[this.beanInfo.fields.length];
        size = this.beanInfo.fields.length;
        for (i = 0; i < size; i++) {
            this.fieldDeserializers[i] = getFieldDeserializer(this.beanInfo.fields[i].name);
        }
    }

    public FieldDeserializer getFieldDeserializer(String key) {
        if (key == null) {
            return null;
        }
        int low = 0;
        int high = this.sortedFieldDeserializers.length - 1;
        while (low <= high) {
            int mid = (low + high) >>> 1;
            int cmp = this.sortedFieldDeserializers[mid].fieldInfo.name.compareTo(key);
            if (cmp < 0) {
                low = mid + 1;
            } else if (cmp <= 0) {
                return this.sortedFieldDeserializers[mid];
            } else {
                high = mid - 1;
            }
        }
        return null;
    }

    public Object createInstance(DefaultJSONParser parser, Type type) {
        if ((type instanceof Class) && this.clazz.isInterface()) {
            Class<?> clazz = (Class) type;
            return Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new JSONObject());
        } else if (this.beanInfo.defaultConstructor == null) {
            return null;
        } else {
            try {
                Object object;
                Constructor<?> constructor = this.beanInfo.defaultConstructor;
                if (this.beanInfo.defaultConstructorParameterSize == 0) {
                    object = constructor.newInstance(new Object[0]);
                } else {
                    object = constructor.newInstance(new Object[]{parser.getContext().object});
                }
                if (parser != null && parser.lexer.isEnabled(Feature.InitStringFieldAsEmpty)) {
                    for (FieldInfo fieldInfo : this.beanInfo.fields) {
                        if (fieldInfo.fieldClass == String.class) {
                            try {
                                fieldInfo.set(object, "");
                            } catch (Exception e) {
                                throw new JSONException("create instance error, class " + this.clazz.getName(), e);
                            }
                        }
                    }
                }
                return object;
            } catch (Exception e2) {
                throw new JSONException("create instance error, class " + this.clazz.getName(), e2);
            }
        }
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        return deserialze(parser, type, fieldName, null);
    }

    public <T> T deserialzeArrayMapping(DefaultJSONParser parser, Type type, Object fieldName, Object object) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() != 14) {
            throw new JSONException(MultiScreenParams.DLNA_PHONE_CONTROLL_ERROR);
        }
        object = createInstance(parser, type);
        int i = 0;
        int size = this.sortedFieldDeserializers.length;
        while (i < size) {
            char seperator = i == size + -1 ? ']' : ',';
            FieldDeserializer fieldDeser = this.sortedFieldDeserializers[i];
            Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
            if (fieldClass == Integer.TYPE) {
                fieldDeser.setValue(object, lexer.scanInt(seperator));
            } else if (fieldClass == String.class) {
                fieldDeser.setValue(object, lexer.scanString(seperator));
            } else if (fieldClass == Long.TYPE) {
                fieldDeser.setValue(object, lexer.scanLong(seperator));
            } else if (fieldClass.isEnum()) {
                Object value;
                char ch = lexer.getCurrent();
                if (ch == '\"' || ch == 'n') {
                    value = lexer.scanEnum(fieldClass, parser.getSymbolTable(), seperator);
                } else if (ch < '0' || ch > '9') {
                    value = scanEnum(lexer, seperator);
                } else {
                    value = ((EnumDeserializer) ((DefaultFieldDeserializer) fieldDeser).getFieldValueDeserilizer(parser.getConfig())).valueOf(lexer.scanInt(seperator));
                }
                fieldDeser.setValue(object, value);
            } else if (fieldClass == Boolean.TYPE) {
                fieldDeser.setValue(object, lexer.scanBoolean(seperator));
            } else if (fieldClass == Float.TYPE) {
                fieldDeser.setValue(object, Float.valueOf(lexer.scanFloat(seperator)));
            } else if (fieldClass == Double.TYPE) {
                fieldDeser.setValue(object, Double.valueOf(lexer.scanDouble(seperator)));
            } else if (fieldClass == Date.class && lexer.getCurrent() == '1') {
                fieldDeser.setValue(object, new Date(lexer.scanLong(seperator)));
            } else {
                lexer.nextToken(14);
                fieldDeser.setValue(object, parser.parseObject(fieldDeser.fieldInfo.fieldType));
                check(lexer, seperator == ']' ? 15 : 16);
            }
            i++;
        }
        lexer.nextToken(16);
        return object;
    }

    protected void check(JSONLexer lexer, int token) {
        if (lexer.token() != token) {
            throw new JSONException("syntax error");
        }
    }

    protected Enum<?> scanEnum(JSONLexer lexer, char seperator) {
        throw new JSONException("illegal enum. " + lexer.info());
    }

    protected <T> T deserialze(com.alibaba.fastjson.parser.DefaultJSONParser r43, java.lang.reflect.Type r44, java.lang.Object r45, java.lang.Object r46) {
        /* JADX: method processing error */
/*
Error: jadx.core.utils.exceptions.JadxRuntimeException: Exception block dominator not found, method:com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object):T. bs: [B:15:0x0040, B:285:0x059c, B:300:0x05ee, B:308:0x0624]
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.searchTryCatchDominators(ProcessTryCatchRegions.java:86)
	at jadx.core.dex.visitors.regions.ProcessTryCatchRegions.process(ProcessTryCatchRegions.java:45)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.postProcessRegions(RegionMakerVisitor.java:63)
	at jadx.core.dex.visitors.regions.RegionMakerVisitor.visit(RegionMakerVisitor.java:58)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:31)
	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:17)
	at jadx.core.ProcessClass.process(ProcessClass.java:37)
	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:59)
	at jadx.core.ProcessClass.process(ProcessClass.java:42)
	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:306)
	at jadx.api.JavaClass.decompile(JavaClass.java:62)
*/
        /*
        r42 = this;
        r4 = com.alibaba.fastjson.JSON.class;
        r0 = r44;
        if (r0 == r4) goto L_0x000c;
    L_0x0006:
        r4 = com.alibaba.fastjson.JSONObject.class;
        r0 = r44;
        if (r0 != r4) goto L_0x0011;
    L_0x000c:
        r12 = r43.parse();
    L_0x0010:
        return r12;
    L_0x0011:
        r0 = r43;
        r0 = r0.lexer;
        r28 = r0;
        r28 = (com.alibaba.fastjson.parser.JSONLexerBase) r28;
        r38 = r28.token();
        r4 = 8;
        r0 = r38;
        if (r0 != r4) goto L_0x002c;
    L_0x0023:
        r4 = 16;
        r0 = r28;
        r0.nextToken(r4);
        r12 = 0;
        goto L_0x0010;
    L_0x002c:
        r15 = r43.getContext();
        if (r46 == 0) goto L_0x0036;
    L_0x0032:
        if (r15 == 0) goto L_0x0036;
    L_0x0034:
        r15 = r15.parent;
    L_0x0036:
        r13 = 0;
        r9 = 0;
        r4 = 13;
        r0 = r38;
        if (r0 != r4) goto L_0x0059;
    L_0x003e:
        r4 = 16;
        r0 = r28;	 Catch:{ all -> 0x010c }
        r0.nextToken(r4);	 Catch:{ all -> 0x010c }
        if (r46 != 0) goto L_0x004b;	 Catch:{ all -> 0x010c }
    L_0x0047:
        r46 = r42.createInstance(r43, r44);	 Catch:{ all -> 0x010c }
    L_0x004b:
        if (r13 == 0) goto L_0x0051;
    L_0x004d:
        r0 = r46;
        r13.object = r0;
    L_0x0051:
        r0 = r43;
        r0.setContext(r15);
        r12 = r46;
        goto L_0x0010;
    L_0x0059:
        r4 = 14;
        r0 = r38;
        if (r0 != r4) goto L_0x008d;
    L_0x005f:
        r0 = r42;	 Catch:{ all -> 0x010c }
        r4 = r0.beanInfo;	 Catch:{ all -> 0x010c }
        r4 = r4.parserFeatures;	 Catch:{ all -> 0x010c }
        r5 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean;	 Catch:{ all -> 0x010c }
        r5 = r5.mask;	 Catch:{ all -> 0x010c }
        r4 = r4 & r5;	 Catch:{ all -> 0x010c }
        if (r4 != 0) goto L_0x0076;	 Catch:{ all -> 0x010c }
    L_0x006c:
        r4 = com.alibaba.fastjson.parser.Feature.SupportArrayToBean;	 Catch:{ all -> 0x010c }
        r0 = r28;	 Catch:{ all -> 0x010c }
        r4 = r0.isEnabled(r4);	 Catch:{ all -> 0x010c }
        if (r4 == 0) goto L_0x008a;	 Catch:{ all -> 0x010c }
    L_0x0076:
        r27 = 1;	 Catch:{ all -> 0x010c }
    L_0x0078:
        if (r27 == 0) goto L_0x008d;	 Catch:{ all -> 0x010c }
    L_0x007a:
        r12 = r42.deserialzeArrayMapping(r43, r44, r45, r46);	 Catch:{ all -> 0x010c }
        if (r13 == 0) goto L_0x0084;
    L_0x0080:
        r0 = r46;
        r13.object = r0;
    L_0x0084:
        r0 = r43;
        r0.setContext(r15);
        goto L_0x0010;
    L_0x008a:
        r27 = 0;
        goto L_0x0078;
    L_0x008d:
        r4 = 12;
        r0 = r38;
        if (r0 == r4) goto L_0x0119;
    L_0x0093:
        r4 = 16;
        r0 = r38;
        if (r0 == r4) goto L_0x0119;
    L_0x0099:
        r4 = r28.isBlankInput();	 Catch:{ all -> 0x010c }
        if (r4 == 0) goto L_0x00ad;
    L_0x009f:
        r12 = 0;
        if (r13 == 0) goto L_0x00a6;
    L_0x00a2:
        r0 = r46;
        r13.object = r0;
    L_0x00a6:
        r0 = r43;
        r0.setContext(r15);
        goto L_0x0010;
    L_0x00ad:
        r4 = 4;
        r0 = r38;
        if (r0 != r4) goto L_0x00cd;
    L_0x00b2:
        r37 = r28.stringVal();	 Catch:{ all -> 0x010c }
        r4 = r37.length();	 Catch:{ all -> 0x010c }
        if (r4 != 0) goto L_0x00cd;	 Catch:{ all -> 0x010c }
    L_0x00bc:
        r28.nextToken();	 Catch:{ all -> 0x010c }
        r12 = 0;
        if (r13 == 0) goto L_0x00c6;
    L_0x00c2:
        r0 = r46;
        r13.object = r0;
    L_0x00c6:
        r0 = r43;
        r0.setContext(r15);
        goto L_0x0010;
    L_0x00cd:
        r4 = new java.lang.StringBuffer;	 Catch:{ all -> 0x010c }
        r4.<init>();	 Catch:{ all -> 0x010c }
        r5 = "syntax error, expect {, actual ";	 Catch:{ all -> 0x010c }
        r4 = r4.append(r5);	 Catch:{ all -> 0x010c }
        r5 = r28.tokenName();	 Catch:{ all -> 0x010c }
        r4 = r4.append(r5);	 Catch:{ all -> 0x010c }
        r5 = ", pos ";	 Catch:{ all -> 0x010c }
        r4 = r4.append(r5);	 Catch:{ all -> 0x010c }
        r5 = r28.pos();	 Catch:{ all -> 0x010c }
        r10 = r4.append(r5);	 Catch:{ all -> 0x010c }
        r0 = r45;	 Catch:{ all -> 0x010c }
        r4 = r0 instanceof java.lang.String;	 Catch:{ all -> 0x010c }
        if (r4 == 0) goto L_0x0102;	 Catch:{ all -> 0x010c }
    L_0x00f6:
        r4 = ", fieldName ";	 Catch:{ all -> 0x010c }
        r4 = r10.append(r4);	 Catch:{ all -> 0x010c }
        r0 = r45;	 Catch:{ all -> 0x010c }
        r4.append(r0);	 Catch:{ all -> 0x010c }
    L_0x0102:
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x010c }
        r5 = r10.toString();	 Catch:{ all -> 0x010c }
        r4.<init>(r5);	 Catch:{ all -> 0x010c }
        throw r4;	 Catch:{ all -> 0x010c }
    L_0x010c:
        r4 = move-exception;
    L_0x010d:
        if (r13 == 0) goto L_0x0113;
    L_0x010f:
        r0 = r46;
        r13.object = r0;
    L_0x0113:
        r0 = r43;
        r0.setContext(r15);
        throw r4;
    L_0x0119:
        r0 = r43;	 Catch:{ all -> 0x010c }
        r4 = r0.resolveStatus;	 Catch:{ all -> 0x010c }
        r5 = 2;	 Catch:{ all -> 0x010c }
        if (r4 != r5) goto L_0x0125;	 Catch:{ all -> 0x010c }
    L_0x0120:
        r4 = 0;	 Catch:{ all -> 0x010c }
        r0 = r43;	 Catch:{ all -> 0x010c }
        r0.resolveStatus = r4;	 Catch:{ all -> 0x010c }
    L_0x0125:
        r21 = 0;
        r25 = r9;
    L_0x0129:
        r6 = 0;
        r20 = 0;
        r22 = 0;
        r19 = 0;
        r0 = r42;	 Catch:{ all -> 0x034c }
        r4 = r0.sortedFieldDeserializers;	 Catch:{ all -> 0x034c }
        r4 = r4.length;	 Catch:{ all -> 0x034c }
        r0 = r21;	 Catch:{ all -> 0x034c }
        if (r0 >= r4) goto L_0x014b;	 Catch:{ all -> 0x034c }
    L_0x0139:
        r0 = r42;	 Catch:{ all -> 0x034c }
        r4 = r0.sortedFieldDeserializers;	 Catch:{ all -> 0x034c }
        r20 = r4[r21];	 Catch:{ all -> 0x034c }
        r0 = r20;	 Catch:{ all -> 0x034c }
        r0 = r0.fieldInfo;	 Catch:{ all -> 0x034c }
        r22 = r0;	 Catch:{ all -> 0x034c }
        r0 = r22;	 Catch:{ all -> 0x034c }
        r0 = r0.fieldClass;	 Catch:{ all -> 0x034c }
        r19 = r0;	 Catch:{ all -> 0x034c }
    L_0x014b:
        r30 = 0;	 Catch:{ all -> 0x034c }
        r41 = 0;	 Catch:{ all -> 0x034c }
        r24 = 0;	 Catch:{ all -> 0x034c }
        if (r20 == 0) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x0153:
        r0 = r22;	 Catch:{ all -> 0x034c }
        r0 = r0.name_chars;	 Catch:{ all -> 0x034c }
        r31 = r0;	 Catch:{ all -> 0x034c }
        r4 = java.lang.Integer.TYPE;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 == r4) goto L_0x0165;	 Catch:{ all -> 0x034c }
    L_0x015f:
        r4 = java.lang.Integer.class;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x01ce;	 Catch:{ all -> 0x034c }
    L_0x0165:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r4 = r0.scanFieldInt(r1);	 Catch:{ all -> 0x034c }
        r24 = java.lang.Integer.valueOf(r4);	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        if (r4 <= 0) goto L_0x01bf;	 Catch:{ all -> 0x034c }
    L_0x0177:
        r30 = 1;	 Catch:{ all -> 0x034c }
        r41 = 1;	 Catch:{ all -> 0x034c }
    L_0x017b:
        if (r30 != 0) goto L_0x0490;	 Catch:{ all -> 0x034c }
    L_0x017d:
        r0 = r43;	 Catch:{ all -> 0x034c }
        r4 = r0.symbolTable;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r6 = r0.scanSymbol(r4);	 Catch:{ all -> 0x034c }
        if (r6 != 0) goto L_0x030d;	 Catch:{ all -> 0x034c }
    L_0x0189:
        r38 = r28.token();	 Catch:{ all -> 0x034c }
        r4 = 13;	 Catch:{ all -> 0x034c }
        r0 = r38;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x02f9;	 Catch:{ all -> 0x034c }
    L_0x0193:
        r4 = 16;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r0.nextToken(r4);	 Catch:{ all -> 0x034c }
        r9 = r25;
    L_0x019c:
        if (r46 != 0) goto L_0x05a8;
    L_0x019e:
        if (r9 != 0) goto L_0x056a;
    L_0x01a0:
        r46 = r42.createInstance(r43, r44);	 Catch:{ all -> 0x010c }
        if (r13 != 0) goto L_0x01b0;	 Catch:{ all -> 0x010c }
    L_0x01a6:
        r0 = r43;	 Catch:{ all -> 0x010c }
        r1 = r46;	 Catch:{ all -> 0x010c }
        r2 = r45;	 Catch:{ all -> 0x010c }
        r13 = r0.setContext(r15, r1, r2);	 Catch:{ all -> 0x010c }
    L_0x01b0:
        if (r13 == 0) goto L_0x01b6;
    L_0x01b2:
        r0 = r46;
        r13.object = r0;
    L_0x01b6:
        r0 = r43;
        r0.setContext(r15);
        r12 = r46;
        goto L_0x0010;
    L_0x01bf:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        r5 = -2;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x01c6:
        r9 = r25;	 Catch:{ all -> 0x034c }
    L_0x01c8:
        r21 = r21 + 1;	 Catch:{ all -> 0x034c }
        r25 = r9;	 Catch:{ all -> 0x034c }
        goto L_0x0129;	 Catch:{ all -> 0x034c }
    L_0x01ce:
        r4 = java.lang.Long.TYPE;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 == r4) goto L_0x01da;	 Catch:{ all -> 0x034c }
    L_0x01d4:
        r4 = java.lang.Long.class;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x01fb;	 Catch:{ all -> 0x034c }
    L_0x01da:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r4 = r0.scanFieldLong(r1);	 Catch:{ all -> 0x034c }
        r24 = java.lang.Long.valueOf(r4);	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        if (r4 <= 0) goto L_0x01f1;	 Catch:{ all -> 0x034c }
    L_0x01ec:
        r30 = 1;	 Catch:{ all -> 0x034c }
        r41 = 1;	 Catch:{ all -> 0x034c }
        goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x01f1:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        r5 = -2;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x01f8:
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x01c8;	 Catch:{ all -> 0x034c }
    L_0x01fb:
        r4 = java.lang.String.class;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x021f;	 Catch:{ all -> 0x034c }
    L_0x0201:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r24 = r0.scanFieldString(r1);	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        if (r4 <= 0) goto L_0x0215;	 Catch:{ all -> 0x034c }
    L_0x020f:
        r30 = 1;	 Catch:{ all -> 0x034c }
        r41 = 1;	 Catch:{ all -> 0x034c }
        goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x0215:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        r5 = -2;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x021c:
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x01c8;	 Catch:{ all -> 0x034c }
    L_0x021f:
        r4 = java.lang.Boolean.TYPE;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 == r4) goto L_0x022b;	 Catch:{ all -> 0x034c }
    L_0x0225:
        r4 = java.lang.Boolean.class;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x024e;	 Catch:{ all -> 0x034c }
    L_0x022b:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r4 = r0.scanFieldBoolean(r1);	 Catch:{ all -> 0x034c }
        r24 = java.lang.Boolean.valueOf(r4);	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        if (r4 <= 0) goto L_0x0243;	 Catch:{ all -> 0x034c }
    L_0x023d:
        r30 = 1;	 Catch:{ all -> 0x034c }
        r41 = 1;	 Catch:{ all -> 0x034c }
        goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x0243:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        r5 = -2;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x024a:
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x01c8;	 Catch:{ all -> 0x034c }
    L_0x024e:
        r4 = java.lang.Float.TYPE;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 == r4) goto L_0x025a;	 Catch:{ all -> 0x034c }
    L_0x0254:
        r4 = java.lang.Float.class;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x027d;	 Catch:{ all -> 0x034c }
    L_0x025a:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r4 = r0.scanFieldFloat(r1);	 Catch:{ all -> 0x034c }
        r24 = java.lang.Float.valueOf(r4);	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        if (r4 <= 0) goto L_0x0272;	 Catch:{ all -> 0x034c }
    L_0x026c:
        r30 = 1;	 Catch:{ all -> 0x034c }
        r41 = 1;	 Catch:{ all -> 0x034c }
        goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x0272:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        r5 = -2;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x0279:
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x01c8;	 Catch:{ all -> 0x034c }
    L_0x027d:
        r4 = java.lang.Double.TYPE;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 == r4) goto L_0x0289;	 Catch:{ all -> 0x034c }
    L_0x0283:
        r4 = java.lang.Double.class;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x02ac;	 Catch:{ all -> 0x034c }
    L_0x0289:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r4 = r0.scanFieldDouble(r1);	 Catch:{ all -> 0x034c }
        r24 = java.lang.Double.valueOf(r4);	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        if (r4 <= 0) goto L_0x02a1;	 Catch:{ all -> 0x034c }
    L_0x029b:
        r30 = 1;	 Catch:{ all -> 0x034c }
        r41 = 1;	 Catch:{ all -> 0x034c }
        goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x02a1:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        r5 = -2;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x02a8:
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x01c8;	 Catch:{ all -> 0x034c }
    L_0x02ac:
        r4 = r19.isEnum();	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x02eb;	 Catch:{ all -> 0x034c }
    L_0x02b2:
        r4 = r43.getConfig();	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        r4 = r4.getDeserializer(r0);	 Catch:{ all -> 0x034c }
        r4 = r4 instanceof com.alibaba.fastjson.parser.deserializer.EnumDeserializer;	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x02eb;	 Catch:{ all -> 0x034c }
    L_0x02c0:
        r0 = r43;	 Catch:{ all -> 0x034c }
        r4 = r0.symbolTable;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r18 = r0.scanFieldSymbol(r1, r4);	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        if (r4 <= 0) goto L_0x02e0;	 Catch:{ all -> 0x034c }
    L_0x02d2:
        r30 = 1;	 Catch:{ all -> 0x034c }
        r41 = 1;	 Catch:{ all -> 0x034c }
        r0 = r19;	 Catch:{ all -> 0x034c }
        r1 = r18;	 Catch:{ all -> 0x034c }
        r24 = java.lang.Enum.valueOf(r0, r1);	 Catch:{ all -> 0x034c }
        goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x02e0:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x034c }
        r5 = -2;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x02e7:
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x01c8;	 Catch:{ all -> 0x034c }
    L_0x02eb:
        r0 = r28;	 Catch:{ all -> 0x034c }
        r1 = r31;	 Catch:{ all -> 0x034c }
        r4 = r0.matchField(r1);	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x064d;	 Catch:{ all -> 0x034c }
    L_0x02f5:
        r30 = 1;	 Catch:{ all -> 0x034c }
        goto L_0x017b;	 Catch:{ all -> 0x034c }
    L_0x02f9:
        r4 = 16;	 Catch:{ all -> 0x034c }
        r0 = r38;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x030d;	 Catch:{ all -> 0x034c }
    L_0x02ff:
        r4 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r4 = r0.isEnabled(r4);	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x030d;	 Catch:{ all -> 0x034c }
    L_0x0309:
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x01c8;	 Catch:{ all -> 0x034c }
    L_0x030d:
        r4 = "$ref";	 Catch:{ all -> 0x034c }
        if (r4 != r6) goto L_0x040d;	 Catch:{ all -> 0x034c }
    L_0x0312:
        r4 = 4;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r0.nextTokenWithColon(r4);	 Catch:{ all -> 0x034c }
        r38 = r28.token();	 Catch:{ all -> 0x034c }
        r4 = 4;	 Catch:{ all -> 0x034c }
        r0 = r38;	 Catch:{ all -> 0x034c }
        if (r0 != r4) goto L_0x03d0;	 Catch:{ all -> 0x034c }
    L_0x0321:
        r34 = r28.stringVal();	 Catch:{ all -> 0x034c }
        r4 = "@";	 Catch:{ all -> 0x034c }
        r0 = r34;	 Catch:{ all -> 0x034c }
        r4 = r4.equals(r0);	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x0351;	 Catch:{ all -> 0x034c }
    L_0x0330:
        r0 = r15.object;	 Catch:{ all -> 0x034c }
        r46 = r0;	 Catch:{ all -> 0x034c }
    L_0x0334:
        r4 = 13;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r0.nextToken(r4);	 Catch:{ all -> 0x034c }
        r4 = r28.token();	 Catch:{ all -> 0x034c }
        r5 = 13;	 Catch:{ all -> 0x034c }
        if (r4 == r5) goto L_0x03ee;	 Catch:{ all -> 0x034c }
    L_0x0343:
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x034c }
        r5 = "illegal ref";	 Catch:{ all -> 0x034c }
        r4.<init>(r5);	 Catch:{ all -> 0x034c }
        throw r4;	 Catch:{ all -> 0x034c }
    L_0x034c:
        r4 = move-exception;	 Catch:{ all -> 0x034c }
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x010d;	 Catch:{ all -> 0x034c }
    L_0x0351:
        r4 = "..";	 Catch:{ all -> 0x034c }
        r0 = r34;	 Catch:{ all -> 0x034c }
        r4 = r4.equals(r0);	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x0381;	 Catch:{ all -> 0x034c }
    L_0x035c:
        r0 = r15.parent;	 Catch:{ all -> 0x034c }
        r33 = r0;	 Catch:{ all -> 0x034c }
        r0 = r33;	 Catch:{ all -> 0x034c }
        r4 = r0.object;	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x036d;	 Catch:{ all -> 0x034c }
    L_0x0366:
        r0 = r33;	 Catch:{ all -> 0x034c }
        r0 = r0.object;	 Catch:{ all -> 0x034c }
        r46 = r0;	 Catch:{ all -> 0x034c }
        goto L_0x0334;	 Catch:{ all -> 0x034c }
    L_0x036d:
        r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x034c }
        r0 = r33;	 Catch:{ all -> 0x034c }
        r1 = r34;	 Catch:{ all -> 0x034c }
        r4.<init>(r0, r1);	 Catch:{ all -> 0x034c }
        r0 = r43;	 Catch:{ all -> 0x034c }
        r0.addResolveTask(r4);	 Catch:{ all -> 0x034c }
        r4 = 1;	 Catch:{ all -> 0x034c }
        r0 = r43;	 Catch:{ all -> 0x034c }
        r0.resolveStatus = r4;	 Catch:{ all -> 0x034c }
        goto L_0x0334;	 Catch:{ all -> 0x034c }
    L_0x0381:
        r4 = "$";	 Catch:{ all -> 0x034c }
        r0 = r34;	 Catch:{ all -> 0x034c }
        r4 = r4.equals(r0);	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x03bd;	 Catch:{ all -> 0x034c }
    L_0x038c:
        r35 = r15;	 Catch:{ all -> 0x034c }
    L_0x038e:
        r0 = r35;	 Catch:{ all -> 0x034c }
        r4 = r0.parent;	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x039b;	 Catch:{ all -> 0x034c }
    L_0x0394:
        r0 = r35;	 Catch:{ all -> 0x034c }
        r0 = r0.parent;	 Catch:{ all -> 0x034c }
        r35 = r0;	 Catch:{ all -> 0x034c }
        goto L_0x038e;	 Catch:{ all -> 0x034c }
    L_0x039b:
        r0 = r35;	 Catch:{ all -> 0x034c }
        r4 = r0.object;	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x03a8;	 Catch:{ all -> 0x034c }
    L_0x03a1:
        r0 = r35;	 Catch:{ all -> 0x034c }
        r0 = r0.object;	 Catch:{ all -> 0x034c }
        r46 = r0;	 Catch:{ all -> 0x034c }
        goto L_0x0334;	 Catch:{ all -> 0x034c }
    L_0x03a8:
        r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x034c }
        r0 = r35;	 Catch:{ all -> 0x034c }
        r1 = r34;	 Catch:{ all -> 0x034c }
        r4.<init>(r0, r1);	 Catch:{ all -> 0x034c }
        r0 = r43;	 Catch:{ all -> 0x034c }
        r0.addResolveTask(r4);	 Catch:{ all -> 0x034c }
        r4 = 1;	 Catch:{ all -> 0x034c }
        r0 = r43;	 Catch:{ all -> 0x034c }
        r0.resolveStatus = r4;	 Catch:{ all -> 0x034c }
        goto L_0x0334;	 Catch:{ all -> 0x034c }
    L_0x03bd:
        r4 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x034c }
        r0 = r34;	 Catch:{ all -> 0x034c }
        r4.<init>(r15, r0);	 Catch:{ all -> 0x034c }
        r0 = r43;	 Catch:{ all -> 0x034c }
        r0.addResolveTask(r4);	 Catch:{ all -> 0x034c }
        r4 = 1;	 Catch:{ all -> 0x034c }
        r0 = r43;	 Catch:{ all -> 0x034c }
        r0.resolveStatus = r4;	 Catch:{ all -> 0x034c }
        goto L_0x0334;	 Catch:{ all -> 0x034c }
    L_0x03d0:
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x034c }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x034c }
        r5.<init>();	 Catch:{ all -> 0x034c }
        r7 = "illegal ref, ";	 Catch:{ all -> 0x034c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x034c }
        r7 = com.alibaba.fastjson.parser.JSONToken.name(r38);	 Catch:{ all -> 0x034c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x034c }
        r5 = r5.toString();	 Catch:{ all -> 0x034c }
        r4.<init>(r5);	 Catch:{ all -> 0x034c }
        throw r4;	 Catch:{ all -> 0x034c }
    L_0x03ee:
        r4 = 16;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r0.nextToken(r4);	 Catch:{ all -> 0x034c }
        r0 = r43;	 Catch:{ all -> 0x034c }
        r1 = r46;	 Catch:{ all -> 0x034c }
        r2 = r45;	 Catch:{ all -> 0x034c }
        r0.setContext(r15, r1, r2);	 Catch:{ all -> 0x034c }
        if (r13 == 0) goto L_0x0404;
    L_0x0400:
        r0 = r46;
        r13.object = r0;
    L_0x0404:
        r0 = r43;
        r0.setContext(r15);
        r12 = r46;
        goto L_0x0010;
    L_0x040d:
        r4 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x034c }
        if (r4 != r6) goto L_0x0490;	 Catch:{ all -> 0x034c }
    L_0x0411:
        r4 = 4;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r0.nextTokenWithColon(r4);	 Catch:{ all -> 0x034c }
        r4 = r28.token();	 Catch:{ all -> 0x034c }
        r5 = 4;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x0487;	 Catch:{ all -> 0x034c }
    L_0x041e:
        r39 = r28.stringVal();	 Catch:{ all -> 0x034c }
        r4 = 16;	 Catch:{ all -> 0x034c }
        r0 = r28;	 Catch:{ all -> 0x034c }
        r0.nextToken(r4);	 Catch:{ all -> 0x034c }
        r0 = r42;	 Catch:{ all -> 0x034c }
        r4 = r0.beanInfo;	 Catch:{ all -> 0x034c }
        r4 = r4.typeName;	 Catch:{ all -> 0x034c }
        r0 = r39;	 Catch:{ all -> 0x034c }
        r4 = r0.equals(r4);	 Catch:{ all -> 0x034c }
        if (r4 == 0) goto L_0x0446;	 Catch:{ all -> 0x034c }
    L_0x0437:
        r4 = r28.token();	 Catch:{ all -> 0x034c }
        r5 = 13;	 Catch:{ all -> 0x034c }
        if (r4 != r5) goto L_0x064d;	 Catch:{ all -> 0x034c }
    L_0x043f:
        r28.nextToken();	 Catch:{ all -> 0x034c }
        r9 = r25;	 Catch:{ all -> 0x034c }
        goto L_0x019c;	 Catch:{ all -> 0x034c }
    L_0x0446:
        r14 = r43.getConfig();	 Catch:{ all -> 0x034c }
        r0 = r42;	 Catch:{ all -> 0x034c }
        r4 = r0.beanInfo;	 Catch:{ all -> 0x034c }
        r0 = r42;	 Catch:{ all -> 0x034c }
        r1 = r39;	 Catch:{ all -> 0x034c }
        r16 = r0.getSeeAlso(r14, r4, r1);	 Catch:{ all -> 0x034c }
        r40 = 0;	 Catch:{ all -> 0x034c }
        if (r16 != 0) goto L_0x046e;	 Catch:{ all -> 0x034c }
    L_0x045a:
        r4 = r14.getDefaultClassLoader();	 Catch:{ all -> 0x034c }
        r0 = r39;	 Catch:{ all -> 0x034c }
        r40 = com.alibaba.fastjson.util.TypeUtils.loadClass(r0, r4);	 Catch:{ all -> 0x034c }
        r4 = r43.getConfig();	 Catch:{ all -> 0x034c }
        r0 = r40;	 Catch:{ all -> 0x034c }
        r16 = r4.getDeserializer(r0);	 Catch:{ all -> 0x034c }
    L_0x046e:
        r0 = r16;	 Catch:{ all -> 0x034c }
        r1 = r43;	 Catch:{ all -> 0x034c }
        r2 = r40;	 Catch:{ all -> 0x034c }
        r3 = r45;	 Catch:{ all -> 0x034c }
        r12 = r0.deserialze(r1, r2, r3);	 Catch:{ all -> 0x034c }
        if (r13 == 0) goto L_0x0480;
    L_0x047c:
        r0 = r46;
        r13.object = r0;
    L_0x0480:
        r0 = r43;
        r0.setContext(r15);
        goto L_0x0010;
    L_0x0487:
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x034c }
        r5 = "syntax error";	 Catch:{ all -> 0x034c }
        r4.<init>(r5);	 Catch:{ all -> 0x034c }
        throw r4;	 Catch:{ all -> 0x034c }
    L_0x0490:
        if (r46 != 0) goto L_0x0649;	 Catch:{ all -> 0x034c }
    L_0x0492:
        if (r25 != 0) goto L_0x0649;	 Catch:{ all -> 0x034c }
    L_0x0494:
        r46 = r42.createInstance(r43, r44);	 Catch:{ all -> 0x034c }
        if (r46 != 0) goto L_0x0645;	 Catch:{ all -> 0x034c }
    L_0x049a:
        r9 = new java.util.HashMap;	 Catch:{ all -> 0x034c }
        r0 = r42;	 Catch:{ all -> 0x034c }
        r4 = r0.fieldDeserializers;	 Catch:{ all -> 0x034c }
        r4 = r4.length;	 Catch:{ all -> 0x034c }
        r9.<init>(r4);	 Catch:{ all -> 0x034c }
    L_0x04a4:
        r0 = r43;	 Catch:{ all -> 0x010c }
        r1 = r46;	 Catch:{ all -> 0x010c }
        r2 = r45;	 Catch:{ all -> 0x010c }
        r13 = r0.setContext(r15, r1, r2);	 Catch:{ all -> 0x010c }
    L_0x04ae:
        if (r30 == 0) goto L_0x051e;	 Catch:{ all -> 0x010c }
    L_0x04b0:
        if (r41 != 0) goto L_0x04d6;	 Catch:{ all -> 0x010c }
    L_0x04b2:
        r0 = r20;	 Catch:{ all -> 0x010c }
        r1 = r43;	 Catch:{ all -> 0x010c }
        r2 = r46;	 Catch:{ all -> 0x010c }
        r3 = r44;	 Catch:{ all -> 0x010c }
        r0.parseField(r1, r2, r3, r9);	 Catch:{ all -> 0x010c }
    L_0x04bd:
        r4 = r28.token();	 Catch:{ all -> 0x010c }
        r5 = 16;	 Catch:{ all -> 0x010c }
        if (r4 == r5) goto L_0x01c8;	 Catch:{ all -> 0x010c }
    L_0x04c5:
        r4 = r28.token();	 Catch:{ all -> 0x010c }
        r5 = 13;	 Catch:{ all -> 0x010c }
        if (r4 != r5) goto L_0x0539;	 Catch:{ all -> 0x010c }
    L_0x04cd:
        r4 = 16;	 Catch:{ all -> 0x010c }
        r0 = r28;	 Catch:{ all -> 0x010c }
        r0.nextToken(r4);	 Catch:{ all -> 0x010c }
        goto L_0x019c;	 Catch:{ all -> 0x010c }
    L_0x04d6:
        if (r46 != 0) goto L_0x04ea;	 Catch:{ all -> 0x010c }
    L_0x04d8:
        r0 = r22;	 Catch:{ all -> 0x010c }
        r4 = r0.name;	 Catch:{ all -> 0x010c }
        r0 = r24;	 Catch:{ all -> 0x010c }
        r9.put(r4, r0);	 Catch:{ all -> 0x010c }
    L_0x04e1:
        r0 = r28;	 Catch:{ all -> 0x010c }
        r4 = r0.matchStat;	 Catch:{ all -> 0x010c }
        r5 = 4;	 Catch:{ all -> 0x010c }
        if (r4 != r5) goto L_0x04bd;	 Catch:{ all -> 0x010c }
    L_0x04e8:
        goto L_0x019c;	 Catch:{ all -> 0x010c }
    L_0x04ea:
        if (r24 != 0) goto L_0x0514;	 Catch:{ all -> 0x010c }
    L_0x04ec:
        r4 = java.lang.Integer.TYPE;	 Catch:{ all -> 0x010c }
        r0 = r19;	 Catch:{ all -> 0x010c }
        if (r0 == r4) goto L_0x04e1;	 Catch:{ all -> 0x010c }
    L_0x04f2:
        r4 = java.lang.Long.TYPE;	 Catch:{ all -> 0x010c }
        r0 = r19;	 Catch:{ all -> 0x010c }
        if (r0 == r4) goto L_0x04e1;	 Catch:{ all -> 0x010c }
    L_0x04f8:
        r4 = java.lang.Float.TYPE;	 Catch:{ all -> 0x010c }
        r0 = r19;	 Catch:{ all -> 0x010c }
        if (r0 == r4) goto L_0x04e1;	 Catch:{ all -> 0x010c }
    L_0x04fe:
        r4 = java.lang.Double.TYPE;	 Catch:{ all -> 0x010c }
        r0 = r19;	 Catch:{ all -> 0x010c }
        if (r0 == r4) goto L_0x04e1;	 Catch:{ all -> 0x010c }
    L_0x0504:
        r4 = java.lang.Boolean.TYPE;	 Catch:{ all -> 0x010c }
        r0 = r19;	 Catch:{ all -> 0x010c }
        if (r0 == r4) goto L_0x04e1;	 Catch:{ all -> 0x010c }
    L_0x050a:
        r0 = r20;	 Catch:{ all -> 0x010c }
        r1 = r46;	 Catch:{ all -> 0x010c }
        r2 = r24;	 Catch:{ all -> 0x010c }
        r0.setValue(r1, r2);	 Catch:{ all -> 0x010c }
        goto L_0x04e1;	 Catch:{ all -> 0x010c }
    L_0x0514:
        r0 = r20;	 Catch:{ all -> 0x010c }
        r1 = r46;	 Catch:{ all -> 0x010c }
        r2 = r24;	 Catch:{ all -> 0x010c }
        r0.setValue(r1, r2);	 Catch:{ all -> 0x010c }
        goto L_0x04e1;	 Catch:{ all -> 0x010c }
    L_0x051e:
        r4 = r42;	 Catch:{ all -> 0x010c }
        r5 = r43;	 Catch:{ all -> 0x010c }
        r7 = r46;	 Catch:{ all -> 0x010c }
        r8 = r44;	 Catch:{ all -> 0x010c }
        r29 = r4.parseField(r5, r6, r7, r8, r9);	 Catch:{ all -> 0x010c }
        if (r29 != 0) goto L_0x04bd;	 Catch:{ all -> 0x010c }
    L_0x052c:
        r4 = r28.token();	 Catch:{ all -> 0x010c }
        r5 = 13;	 Catch:{ all -> 0x010c }
        if (r4 != r5) goto L_0x01c8;	 Catch:{ all -> 0x010c }
    L_0x0534:
        r28.nextToken();	 Catch:{ all -> 0x010c }
        goto L_0x019c;	 Catch:{ all -> 0x010c }
    L_0x0539:
        r4 = r28.token();	 Catch:{ all -> 0x010c }
        r5 = 18;	 Catch:{ all -> 0x010c }
        if (r4 == r5) goto L_0x0548;	 Catch:{ all -> 0x010c }
    L_0x0541:
        r4 = r28.token();	 Catch:{ all -> 0x010c }
        r5 = 1;	 Catch:{ all -> 0x010c }
        if (r4 != r5) goto L_0x01c8;	 Catch:{ all -> 0x010c }
    L_0x0548:
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x010c }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x010c }
        r5.<init>();	 Catch:{ all -> 0x010c }
        r7 = "syntax error, unexpect token ";	 Catch:{ all -> 0x010c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x010c }
        r7 = r28.token();	 Catch:{ all -> 0x010c }
        r7 = com.alibaba.fastjson.parser.JSONToken.name(r7);	 Catch:{ all -> 0x010c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x010c }
        r5 = r5.toString();	 Catch:{ all -> 0x010c }
        r4.<init>(r5);	 Catch:{ all -> 0x010c }
        throw r4;	 Catch:{ all -> 0x010c }
    L_0x056a:
        r0 = r42;	 Catch:{ all -> 0x010c }
        r4 = r0.beanInfo;	 Catch:{ all -> 0x010c }
        r0 = r4.fields;	 Catch:{ all -> 0x010c }
        r23 = r0;	 Catch:{ all -> 0x010c }
        r0 = r23;	 Catch:{ all -> 0x010c }
        r0 = r0.length;	 Catch:{ all -> 0x010c }
        r36 = r0;	 Catch:{ all -> 0x010c }
        r0 = r36;	 Catch:{ all -> 0x010c }
        r0 = new java.lang.Object[r0];	 Catch:{ all -> 0x010c }
        r32 = r0;	 Catch:{ all -> 0x010c }
        r26 = 0;	 Catch:{ all -> 0x010c }
    L_0x057f:
        r0 = r26;	 Catch:{ all -> 0x010c }
        r1 = r36;	 Catch:{ all -> 0x010c }
        if (r0 >= r1) goto L_0x0594;	 Catch:{ all -> 0x010c }
    L_0x0585:
        r22 = r23[r26];	 Catch:{ all -> 0x010c }
        r0 = r22;	 Catch:{ all -> 0x010c }
        r4 = r0.name;	 Catch:{ all -> 0x010c }
        r4 = r9.get(r4);	 Catch:{ all -> 0x010c }
        r32[r26] = r4;	 Catch:{ all -> 0x010c }
        r26 = r26 + 1;	 Catch:{ all -> 0x010c }
        goto L_0x057f;	 Catch:{ all -> 0x010c }
    L_0x0594:
        r0 = r42;	 Catch:{ all -> 0x010c }
        r4 = r0.beanInfo;	 Catch:{ all -> 0x010c }
        r4 = r4.creatorConstructor;	 Catch:{ all -> 0x010c }
        if (r4 == 0) goto L_0x05e6;
    L_0x059c:
        r0 = r42;	 Catch:{ Exception -> 0x05bf }
        r4 = r0.beanInfo;	 Catch:{ Exception -> 0x05bf }
        r4 = r4.creatorConstructor;	 Catch:{ Exception -> 0x05bf }
        r0 = r32;	 Catch:{ Exception -> 0x05bf }
        r46 = r4.newInstance(r0);	 Catch:{ Exception -> 0x05bf }
    L_0x05a8:
        r0 = r42;	 Catch:{ all -> 0x010c }
        r4 = r0.beanInfo;	 Catch:{ all -> 0x010c }
        r11 = r4.buildMethod;	 Catch:{ all -> 0x010c }
        if (r11 != 0) goto L_0x0623;
    L_0x05b0:
        if (r13 == 0) goto L_0x05b6;
    L_0x05b2:
        r0 = r46;
        r13.object = r0;
    L_0x05b6:
        r0 = r43;
        r0.setContext(r15);
        r12 = r46;
        goto L_0x0010;
    L_0x05bf:
        r17 = move-exception;
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x010c }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x010c }
        r5.<init>();	 Catch:{ all -> 0x010c }
        r7 = "create instance error, ";	 Catch:{ all -> 0x010c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x010c }
        r0 = r42;	 Catch:{ all -> 0x010c }
        r7 = r0.beanInfo;	 Catch:{ all -> 0x010c }
        r7 = r7.creatorConstructor;	 Catch:{ all -> 0x010c }
        r7 = r7.toGenericString();	 Catch:{ all -> 0x010c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x010c }
        r5 = r5.toString();	 Catch:{ all -> 0x010c }
        r0 = r17;	 Catch:{ all -> 0x010c }
        r4.<init>(r5, r0);	 Catch:{ all -> 0x010c }
        throw r4;	 Catch:{ all -> 0x010c }
    L_0x05e6:
        r0 = r42;	 Catch:{ all -> 0x010c }
        r4 = r0.beanInfo;	 Catch:{ all -> 0x010c }
        r4 = r4.factoryMethod;	 Catch:{ all -> 0x010c }
        if (r4 == 0) goto L_0x05a8;
    L_0x05ee:
        r0 = r42;	 Catch:{ Exception -> 0x05fc }
        r4 = r0.beanInfo;	 Catch:{ Exception -> 0x05fc }
        r4 = r4.factoryMethod;	 Catch:{ Exception -> 0x05fc }
        r5 = 0;	 Catch:{ Exception -> 0x05fc }
        r0 = r32;	 Catch:{ Exception -> 0x05fc }
        r46 = r4.invoke(r5, r0);	 Catch:{ Exception -> 0x05fc }
        goto L_0x05a8;
    L_0x05fc:
        r17 = move-exception;
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x010c }
        r5 = new java.lang.StringBuilder;	 Catch:{ all -> 0x010c }
        r5.<init>();	 Catch:{ all -> 0x010c }
        r7 = "create factory method error, ";	 Catch:{ all -> 0x010c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x010c }
        r0 = r42;	 Catch:{ all -> 0x010c }
        r7 = r0.beanInfo;	 Catch:{ all -> 0x010c }
        r7 = r7.factoryMethod;	 Catch:{ all -> 0x010c }
        r7 = r7.toString();	 Catch:{ all -> 0x010c }
        r5 = r5.append(r7);	 Catch:{ all -> 0x010c }
        r5 = r5.toString();	 Catch:{ all -> 0x010c }
        r0 = r17;	 Catch:{ all -> 0x010c }
        r4.<init>(r5, r0);	 Catch:{ all -> 0x010c }
        throw r4;	 Catch:{ all -> 0x010c }
    L_0x0623:
        r4 = 0;
        r4 = new java.lang.Object[r4];	 Catch:{ Exception -> 0x0639 }
        r0 = r46;	 Catch:{ Exception -> 0x0639 }
        r12 = r11.invoke(r0, r4);	 Catch:{ Exception -> 0x0639 }
        if (r13 == 0) goto L_0x0632;
    L_0x062e:
        r0 = r46;
        r13.object = r0;
    L_0x0632:
        r0 = r43;
        r0.setContext(r15);
        goto L_0x0010;
    L_0x0639:
        r17 = move-exception;
        r4 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x010c }
        r5 = "build object error";	 Catch:{ all -> 0x010c }
        r0 = r17;	 Catch:{ all -> 0x010c }
        r4.<init>(r5, r0);	 Catch:{ all -> 0x010c }
        throw r4;	 Catch:{ all -> 0x010c }
    L_0x0645:
        r9 = r25;
        goto L_0x04a4;
    L_0x0649:
        r9 = r25;
        goto L_0x04ae;
    L_0x064d:
        r9 = r25;
        goto L_0x01c8;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer.deserialze(com.alibaba.fastjson.parser.DefaultJSONParser, java.lang.reflect.Type, java.lang.Object, java.lang.Object):T");
    }

    public boolean parseField(DefaultJSONParser parser, String key, Object object, Type objectType, Map<String, Object> fieldValues) {
        JSONLexer lexer = parser.lexer;
        FieldDeserializer fieldDeserializer = smartMatch(key);
        if (fieldDeserializer != null) {
            lexer.nextTokenWithColon(fieldDeserializer.getFastMatchToken());
            fieldDeserializer.parseField(parser, object, objectType, fieldValues);
            return true;
        } else if (lexer.isEnabled(Feature.IgnoreNotMatch)) {
            parser.parseExtra(object, key);
            return false;
        } else {
            throw new JSONException("setter not found, class " + this.clazz.getName() + ", property " + key);
        }
    }

    public FieldDeserializer smartMatch(String key) {
        int i = 0;
        if (key == null) {
            return null;
        }
        FieldDeserializer fieldDeserializer = getFieldDeserializer(key);
        FieldDeserializer fieldDeser;
        if (fieldDeserializer == null) {
            boolean startsWithIs = key.startsWith("is");
            FieldDeserializer[] fieldDeserializerArr = this.sortedFieldDeserializers;
            int length = fieldDeserializerArr.length;
            int i2 = 0;
            while (i2 < length) {
                fieldDeser = fieldDeserializerArr[i2];
                FieldInfo fieldInfo = fieldDeser.fieldInfo;
                Class<?> fieldClass = fieldInfo.fieldClass;
                String fieldName = fieldInfo.name;
                if (!fieldName.equalsIgnoreCase(key)) {
                    if (startsWithIs && ((fieldClass == Boolean.TYPE || fieldClass == Boolean.class) && fieldName.equalsIgnoreCase(key.substring(2)))) {
                        fieldDeserializer = fieldDeser;
                        break;
                    }
                    i2++;
                } else {
                    fieldDeserializer = fieldDeser;
                    break;
                }
            }
        }
        if (fieldDeserializer != null || key.indexOf(95) == -1) {
            return fieldDeserializer;
        }
        String key2 = key.replaceAll("_", "");
        fieldDeserializer = getFieldDeserializer(key2);
        if (fieldDeserializer != null) {
            return fieldDeserializer;
        }
        FieldDeserializer[] fieldDeserializerArr2 = this.sortedFieldDeserializers;
        int length2 = fieldDeserializerArr2.length;
        while (i < length2) {
            fieldDeser = fieldDeserializerArr2[i];
            if (fieldDeser.fieldInfo.name.equalsIgnoreCase(key2)) {
                return fieldDeser;
            }
            i++;
        }
        return fieldDeserializer;
    }

    public int getFastMatchToken() {
        return 12;
    }

    public Object createInstance(Map<String, Object> map, ParserConfig config) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Object object = null;
        if (this.beanInfo.creatorConstructor == null && this.beanInfo.buildMethod == null) {
            object = createInstance(null, (Type) this.clazz);
            for (Entry<String, Object> entry : map.entrySet()) {
                String key = (String) entry.getKey();
                Object value = entry.getValue();
                FieldDeserializer fieldDeser = getFieldDeserializer(key);
                if (fieldDeser != null) {
                    Method method = fieldDeser.fieldInfo.method;
                    if (method != null) {
                        value = TypeUtils.cast(value, method.getGenericParameterTypes()[0], config);
                        method.invoke(object, new Object[]{value});
                    } else {
                        fieldDeser.fieldInfo.field.set(object, TypeUtils.cast(value, fieldDeser.fieldInfo.fieldType, config));
                    }
                }
            }
        } else {
            FieldInfo[] fieldInfoList = this.beanInfo.fields;
            int size = fieldInfoList.length;
            Object[] params = new Object[size];
            for (int i = 0; i < size; i++) {
                params[i] = map.get(fieldInfoList[i].name);
            }
            if (this.beanInfo.creatorConstructor != null) {
                try {
                    object = this.beanInfo.creatorConstructor.newInstance(params);
                } catch (Exception e) {
                    throw new JSONException("create instance error, " + this.beanInfo.creatorConstructor.toGenericString(), e);
                }
            } else if (this.beanInfo.factoryMethod != null) {
                try {
                    object = this.beanInfo.factoryMethod.invoke(null, params);
                } catch (Exception e2) {
                    throw new JSONException("create factory method error, " + this.beanInfo.factoryMethod.toString(), e2);
                }
            }
        }
        return object;
    }

    public Type getFieldType(int ordinal) {
        return this.sortedFieldDeserializers[ordinal].fieldInfo.fieldType;
    }

    protected Object parseRest(DefaultJSONParser parser, Type type, Object fieldName, Object instance) {
        return deserialze(parser, type, fieldName, instance);
    }

    protected JavaBeanDeserializer getSeeAlso(ParserConfig config, JavaBeanInfo beanInfo, String typeName) {
        if (beanInfo.jsonType == null) {
            return null;
        }
        for (Type seeAlsoClass : beanInfo.jsonType.seeAlso()) {
            ObjectDeserializer seeAlsoDeser = config.getDeserializer(seeAlsoClass);
            if (seeAlsoDeser instanceof JavaBeanDeserializer) {
                JavaBeanDeserializer seeAlsoJavaBeanDeser = (JavaBeanDeserializer) seeAlsoDeser;
                JavaBeanInfo subBeanInfo = seeAlsoJavaBeanDeser.beanInfo;
                if (subBeanInfo.typeName.equals(typeName)) {
                    return seeAlsoJavaBeanDeser;
                }
                JavaBeanDeserializer subSeeAlso = getSeeAlso(config, subBeanInfo, typeName);
                if (subSeeAlso != null) {
                    return subSeeAlso;
                }
            }
        }
        return null;
    }

    protected static void parseArray(Collection collection, ObjectDeserializer deser, DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexerBase lexer = parser.lexer;
        int token = lexer.token();
        if (token == 8) {
            lexer.nextToken(16);
            token = lexer.token();
        }
        if (token != 14) {
            parser.throwException(token);
        }
        if (lexer.getCurrent() == '[') {
            lexer.next();
            lexer.setToken(14);
        } else {
            lexer.nextToken(14);
        }
        int index = 0;
        while (true) {
            collection.add(deser.deserialze(parser, type, Integer.valueOf(index)));
            index++;
            if (lexer.token() != 16) {
                break;
            } else if (lexer.getCurrent() == '[') {
                lexer.next();
                lexer.setToken(14);
            } else {
                lexer.nextToken(14);
            }
        }
        token = lexer.token();
        if (token != 15) {
            parser.throwException(token);
        }
        if (lexer.getCurrent() == ',') {
            lexer.next();
            lexer.setToken(16);
            return;
        }
        lexer.nextToken(16);
    }
}
