package com.alibaba.fastjson.parser;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessable;
import com.alibaba.fastjson.parser.deserializer.ExtraProcessor;
import com.alibaba.fastjson.parser.deserializer.ExtraTypeProvider;
import com.alibaba.fastjson.parser.deserializer.FieldDeserializer;
import com.alibaba.fastjson.parser.deserializer.FieldTypeResolver;
import com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.alibaba.fastjson.parser.deserializer.ResolveFieldDeserializer;
import com.alibaba.fastjson.serializer.IntegerCodec;
import com.alibaba.fastjson.serializer.LongCodec;
import com.alibaba.fastjson.serializer.StringCodec;
import com.alibaba.fastjson.util.TypeUtils;
import java.io.Closeable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.xbill.DNS.WKSRecord.Service;

public class DefaultJSONParser implements Closeable {
    public static final int NONE = 0;
    public static final int NeedToResolve = 1;
    public static final int TypeNameRedirect = 2;
    private static final Set<Class<?>> primitiveClasses = new HashSet();
    protected ParserConfig config;
    protected ParseContext context;
    private ParseContext[] contextArray;
    private int contextArrayIndex;
    private DateFormat dateFormat;
    private String dateFormatPattern;
    private List<ExtraProcessor> extraProcessors;
    private List<ExtraTypeProvider> extraTypeProviders;
    protected FieldTypeResolver fieldTypeResolver;
    public final Object input;
    public final JSONLexer lexer;
    public int resolveStatus;
    private List<ResolveTask> resolveTaskList;
    public final SymbolTable symbolTable;

    public static class ResolveTask {
        public final ParseContext context;
        public FieldDeserializer fieldDeserializer;
        public ParseContext ownerContext;
        public final String referenceValue;

        public ResolveTask(ParseContext context, String referenceValue) {
            this.context = context;
            this.referenceValue = referenceValue;
        }
    }

    static {
        primitiveClasses.add(Boolean.TYPE);
        primitiveClasses.add(Byte.TYPE);
        primitiveClasses.add(Short.TYPE);
        primitiveClasses.add(Integer.TYPE);
        primitiveClasses.add(Long.TYPE);
        primitiveClasses.add(Float.TYPE);
        primitiveClasses.add(Double.TYPE);
        primitiveClasses.add(Boolean.class);
        primitiveClasses.add(Byte.class);
        primitiveClasses.add(Short.class);
        primitiveClasses.add(Integer.class);
        primitiveClasses.add(Long.class);
        primitiveClasses.add(Float.class);
        primitiveClasses.add(Double.class);
        primitiveClasses.add(BigInteger.class);
        primitiveClasses.add(BigDecimal.class);
        primitiveClasses.add(String.class);
    }

    public String getDateFomartPattern() {
        return this.dateFormatPattern;
    }

    public DateFormat getDateFormat() {
        if (this.dateFormat == null) {
            this.dateFormat = new SimpleDateFormat(this.dateFormatPattern, this.lexer.getLocale());
            this.dateFormat.setTimeZone(this.lexer.getTimeZone());
        }
        return this.dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormatPattern = dateFormat;
        this.dateFormat = null;
    }

    public void setDateFomrat(DateFormat dateFormat) {
        this.dateFormat = dateFormat;
    }

    public DefaultJSONParser(String input) {
        this(input, ParserConfig.getGlobalInstance(), JSON.DEFAULT_PARSER_FEATURE);
    }

    public DefaultJSONParser(String input, ParserConfig config) {
        this((Object) input, new JSONScanner(input, JSON.DEFAULT_PARSER_FEATURE), config);
    }

    public DefaultJSONParser(String input, ParserConfig config, int features) {
        this((Object) input, new JSONScanner(input, features), config);
    }

    public DefaultJSONParser(char[] input, int length, ParserConfig config, int features) {
        this((Object) input, new JSONScanner(input, length, features), config);
    }

    public DefaultJSONParser(JSONLexer lexer) {
        this(lexer, ParserConfig.getGlobalInstance());
    }

    public DefaultJSONParser(JSONLexer lexer, ParserConfig config) {
        this(null, lexer, config);
    }

    public DefaultJSONParser(Object input, JSONLexer lexer, ParserConfig config) {
        this.dateFormatPattern = JSON.DEFFAULT_DATE_FORMAT;
        this.contextArrayIndex = 0;
        this.resolveStatus = 0;
        this.extraTypeProviders = null;
        this.extraProcessors = null;
        this.fieldTypeResolver = null;
        this.lexer = lexer;
        this.input = input;
        this.config = config;
        this.symbolTable = config.symbolTable;
        int ch = lexer.getCurrent();
        if (ch == Service.NTP) {
            lexer.next();
            ((JSONLexerBase) lexer).token = 12;
        } else if (ch == 91) {
            lexer.next();
            ((JSONLexerBase) lexer).token = 14;
        } else {
            lexer.nextToken();
        }
    }

    public SymbolTable getSymbolTable() {
        return this.symbolTable;
    }

    public String getInput() {
        if (this.input instanceof char[]) {
            return new String((char[]) this.input);
        }
        return this.input.toString();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final java.lang.Object parseObject(java.util.Map r37, java.lang.Object r38) {
        /*
        r36 = this;
        r0 = r36;
        r0 = r0.lexer;
        r17 = r0;
        r33 = r17.token();
        r34 = 8;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x0018;
    L_0x0012:
        r17.nextToken();
        r37 = 0;
    L_0x0017:
        return r37;
    L_0x0018:
        r33 = r17.token();
        r34 = 13;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x0028;
    L_0x0024:
        r17.nextToken();
        goto L_0x0017;
    L_0x0028:
        r33 = r17.token();
        r34 = 12;
        r0 = r33;
        r1 = r34;
        if (r0 == r1) goto L_0x006d;
    L_0x0034:
        r33 = r17.token();
        r34 = 16;
        r0 = r33;
        r1 = r34;
        if (r0 == r1) goto L_0x006d;
    L_0x0040:
        r33 = new com.alibaba.fastjson.JSONException;
        r34 = new java.lang.StringBuilder;
        r34.<init>();
        r35 = "syntax error, expect {, actual ";
        r34 = r34.append(r35);
        r35 = r17.tokenName();
        r34 = r34.append(r35);
        r35 = ", ";
        r34 = r34.append(r35);
        r35 = r17.info();
        r34 = r34.append(r35);
        r34 = r34.toString();
        r33.<init>(r34);
        throw r33;
    L_0x006d:
        r0 = r36;
        r5 = r0.context;
        r27 = 0;
    L_0x0073:
        r17.skipWhitespace();	 Catch:{ all -> 0x00ea }
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        r33 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        if (r33 == 0) goto L_0x0097;
    L_0x0086:
        r33 = 44;
        r0 = r33;
        if (r3 != r0) goto L_0x0097;
    L_0x008c:
        r17.next();	 Catch:{ all -> 0x00ea }
        r17.skipWhitespace();	 Catch:{ all -> 0x00ea }
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        goto L_0x0086;
    L_0x0097:
        r14 = 0;
        r33 = 34;
        r0 = r33;
        if (r3 != r0) goto L_0x00f1;
    L_0x009e:
        r0 = r36;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r34 = 34;
        r0 = r17;
        r1 = r33;
        r2 = r34;
        r16 = r0.scanSymbol(r1, r2);	 Catch:{ all -> 0x00ea }
        r17.skipWhitespace();	 Catch:{ all -> 0x00ea }
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        r33 = 58;
        r0 = r33;
        if (r3 == r0) goto L_0x020b;
    L_0x00bd:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "expect ':' at ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.pos();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = ", name ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r0 = r34;
        r1 = r16;
        r34 = r0.append(r1);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x00ea:
        r33 = move-exception;
        r0 = r36;
        r0.setContext(r5);
        throw r33;
    L_0x00f1:
        r33 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r0 = r33;
        if (r3 != r0) goto L_0x0107;
    L_0x00f7:
        r17.next();	 Catch:{ all -> 0x00ea }
        r17.resetStringPosition();	 Catch:{ all -> 0x00ea }
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        goto L_0x0017;
    L_0x0107:
        r33 = 39;
        r0 = r33;
        if (r3 != r0) goto L_0x015f;
    L_0x010d:
        r33 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        if (r33 != 0) goto L_0x0122;
    L_0x0119:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = "syntax error";
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x0122:
        r0 = r36;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r34 = 39;
        r0 = r17;
        r1 = r33;
        r2 = r34;
        r16 = r0.scanSymbol(r1, r2);	 Catch:{ all -> 0x00ea }
        r17.skipWhitespace();	 Catch:{ all -> 0x00ea }
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        r33 = 58;
        r0 = r33;
        if (r3 == r0) goto L_0x020b;
    L_0x0141:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "expect ':' at ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.pos();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x015f:
        r33 = 26;
        r0 = r33;
        if (r3 != r0) goto L_0x016e;
    L_0x0165:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = "syntax error";
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x016e:
        r33 = 44;
        r0 = r33;
        if (r3 != r0) goto L_0x017d;
    L_0x0174:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = "syntax error";
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x017d:
        r33 = 48;
        r0 = r33;
        if (r3 < r0) goto L_0x0189;
    L_0x0183:
        r33 = 57;
        r0 = r33;
        if (r3 <= r0) goto L_0x018f;
    L_0x0189:
        r33 = 45;
        r0 = r33;
        if (r3 != r0) goto L_0x01f7;
    L_0x018f:
        r17.resetStringPosition();	 Catch:{ all -> 0x00ea }
        r17.scanNumber();	 Catch:{ all -> 0x00ea }
        r33 = r17.token();	 Catch:{ NumberFormatException -> 0x01d8 }
        r34 = 2;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x01cd;
    L_0x01a1:
        r16 = r17.integerValue();	 Catch:{ NumberFormatException -> 0x01d8 }
    L_0x01a5:
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        r33 = 58;
        r0 = r33;
        if (r3 == r0) goto L_0x020b;
    L_0x01af:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "parse number key error";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.info();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x01cd:
        r33 = 1;
        r0 = r17;
        r1 = r33;
        r16 = r0.decimalValue(r1);	 Catch:{ NumberFormatException -> 0x01d8 }
        goto L_0x01a5;
    L_0x01d8:
        r9 = move-exception;
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "parse number key error";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.info();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x01f7:
        r33 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0 = r33;
        if (r3 == r0) goto L_0x0203;
    L_0x01fd:
        r33 = 91;
        r0 = r33;
        if (r3 != r0) goto L_0x0261;
    L_0x0203:
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r16 = r36.parse();	 Catch:{ all -> 0x00ea }
        r14 = 1;
    L_0x020b:
        if (r14 != 0) goto L_0x0213;
    L_0x020d:
        r17.next();	 Catch:{ all -> 0x00ea }
        r17.skipWhitespace();	 Catch:{ all -> 0x00ea }
    L_0x0213:
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        r17.resetStringPosition();	 Catch:{ all -> 0x00ea }
        r33 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x00ea }
        r0 = r16;
        r1 = r33;
        if (r0 != r1) goto L_0x038b;
    L_0x0222:
        r33 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        if (r33 != 0) goto L_0x038b;
    L_0x022e:
        r0 = r36;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r34 = 34;
        r0 = r17;
        r1 = r33;
        r2 = r34;
        r31 = r0.scanSymbol(r1, r2);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0 = r0.config;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r33 = r33.getDefaultClassLoader();	 Catch:{ all -> 0x00ea }
        r0 = r31;
        r1 = r33;
        r4 = com.alibaba.fastjson.util.TypeUtils.loadClass(r0, r1);	 Catch:{ all -> 0x00ea }
        if (r4 != 0) goto L_0x02bc;
    L_0x0254:
        r33 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x00ea }
        r0 = r37;
        r1 = r33;
        r2 = r31;
        r0.put(r1, r2);	 Catch:{ all -> 0x00ea }
        goto L_0x0073;
    L_0x0261:
        r33 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        if (r33 != 0) goto L_0x0276;
    L_0x026d:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = "syntax error";
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x0276:
        r0 = r36;
        r0 = r0.symbolTable;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r0 = r17;
        r1 = r33;
        r16 = r0.scanSymbolUnQuoted(r1);	 Catch:{ all -> 0x00ea }
        r17.skipWhitespace();	 Catch:{ all -> 0x00ea }
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        r33 = 58;
        r0 = r33;
        if (r3 == r0) goto L_0x020b;
    L_0x0291:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "expect ':' at ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.pos();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = ", actual ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r0 = r34;
        r34 = r0.append(r3);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x02bc:
        r33 = 16;
        r0 = r17;
        r1 = r33;
        r0.nextToken(r1);	 Catch:{ all -> 0x00ea }
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 13;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x0330;
    L_0x02d1:
        r33 = 16;
        r0 = r17;
        r1 = r33;
        r0.nextToken(r1);	 Catch:{ all -> 0x00ea }
        r13 = 0;
        r0 = r36;
        r0 = r0.config;	 Catch:{ Exception -> 0x0322 }
        r33 = r0;
        r0 = r33;
        r8 = r0.getDeserializer(r4);	 Catch:{ Exception -> 0x0322 }
        r0 = r8 instanceof com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer;	 Catch:{ Exception -> 0x0322 }
        r33 = r0;
        if (r33 == 0) goto L_0x02f5;
    L_0x02ed:
        r8 = (com.alibaba.fastjson.parser.deserializer.JavaBeanDeserializer) r8;	 Catch:{ Exception -> 0x0322 }
        r0 = r36;
        r13 = r8.createInstance(r0, r4);	 Catch:{ Exception -> 0x0322 }
    L_0x02f5:
        if (r13 != 0) goto L_0x0302;
    L_0x02f7:
        r33 = java.lang.Cloneable.class;
        r0 = r33;
        if (r4 != r0) goto L_0x030b;
    L_0x02fd:
        r13 = new java.util.HashMap;	 Catch:{ Exception -> 0x0322 }
        r13.<init>();	 Catch:{ Exception -> 0x0322 }
    L_0x0302:
        r0 = r36;
        r0.setContext(r5);
        r37 = r13;
        goto L_0x0017;
    L_0x030b:
        r33 = "java.util.Collections$EmptyMap";
        r0 = r33;
        r1 = r31;
        r33 = r0.equals(r1);	 Catch:{ Exception -> 0x0322 }
        if (r33 == 0) goto L_0x031d;
    L_0x0318:
        r13 = java.util.Collections.emptyMap();	 Catch:{ Exception -> 0x0322 }
        goto L_0x0302;
    L_0x031d:
        r13 = r4.newInstance();	 Catch:{ Exception -> 0x0322 }
        goto L_0x0302;
    L_0x0322:
        r9 = move-exception;
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = "create instance error";
        r0 = r33;
        r1 = r34;
        r0.<init>(r1, r9);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x0330:
        r33 = 2;
        r0 = r36;
        r1 = r33;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0 = r0.context;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x034c;
    L_0x0341:
        r0 = r38;
        r0 = r0 instanceof java.lang.Integer;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 != 0) goto L_0x034c;
    L_0x0349:
        r36.popContext();	 Catch:{ all -> 0x00ea }
    L_0x034c:
        r33 = r37.size();	 Catch:{ all -> 0x00ea }
        if (r33 <= 0) goto L_0x0370;
    L_0x0352:
        r0 = r36;
        r0 = r0.config;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r0 = r37;
        r1 = r33;
        r19 = com.alibaba.fastjson.util.TypeUtils.cast(r0, r4, r1);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r19;
        r0.parseObject(r1);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        r37 = r19;
        goto L_0x0017;
    L_0x0370:
        r0 = r36;
        r0 = r0.config;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r0 = r33;
        r8 = r0.getDeserializer(r4);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r38;
        r37 = r8.deserialze(r0, r4, r1);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        goto L_0x0017;
    L_0x038b:
        r33 = "$ref";
        r0 = r16;
        r1 = r33;
        if (r0 != r1) goto L_0x04f4;
    L_0x0394:
        r33 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        if (r33 != 0) goto L_0x04f4;
    L_0x03a0:
        r33 = 4;
        r0 = r17;
        r1 = r33;
        r0.nextToken(r1);	 Catch:{ all -> 0x00ea }
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 4;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x04d2;
    L_0x03b5:
        r23 = r17.stringVal();	 Catch:{ all -> 0x00ea }
        r33 = 13;
        r0 = r17;
        r1 = r33;
        r0.nextToken(r1);	 Catch:{ all -> 0x00ea }
        r24 = 0;
        r33 = "@";
        r0 = r33;
        r1 = r23;
        r33 = r0.equals(r1);	 Catch:{ all -> 0x00ea }
        if (r33 == 0) goto L_0x0423;
    L_0x03d1:
        r0 = r36;
        r0 = r0.context;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x04be;
    L_0x03d9:
        r0 = r36;
        r0 = r0.context;	 Catch:{ all -> 0x00ea }
        r29 = r0;
        r0 = r29;
        r0 = r0.object;	 Catch:{ all -> 0x00ea }
        r30 = r0;
        r0 = r30;
        r0 = r0 instanceof java.lang.Object[];	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 != 0) goto L_0x03f5;
    L_0x03ed:
        r0 = r30;
        r0 = r0 instanceof java.util.Collection;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x040e;
    L_0x03f5:
        r24 = r30;
    L_0x03f7:
        r37 = r24;
    L_0x03f9:
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 13;
        r0 = r33;
        r1 = r34;
        if (r0 == r1) goto L_0x04c2;
    L_0x0405:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = "syntax error";
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x040e:
        r0 = r29;
        r0 = r0.parent;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x03f7;
    L_0x0416:
        r0 = r29;
        r0 = r0.parent;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r0 = r33;
        r0 = r0.object;	 Catch:{ all -> 0x00ea }
        r24 = r0;
        goto L_0x03f7;
    L_0x0423:
        r33 = "..";
        r0 = r33;
        r1 = r23;
        r33 = r0.equals(r1);	 Catch:{ all -> 0x00ea }
        if (r33 == 0) goto L_0x0459;
    L_0x0430:
        r0 = r5.object;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x043d;
    L_0x0436:
        r0 = r5.object;	 Catch:{ all -> 0x00ea }
        r24 = r0;
        r37 = r24;
        goto L_0x03f9;
    L_0x043d:
        r33 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00ea }
        r0 = r33;
        r1 = r23;
        r0.<init>(r5, r1);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r33;
        r0.addResolveTask(r1);	 Catch:{ all -> 0x00ea }
        r33 = 1;
        r0 = r36;
        r1 = r33;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00ea }
        r37 = r24;
        goto L_0x03f9;
    L_0x0459:
        r33 = "$";
        r0 = r33;
        r1 = r23;
        r33 = r0.equals(r1);	 Catch:{ all -> 0x00ea }
        if (r33 == 0) goto L_0x04a5;
    L_0x0466:
        r26 = r5;
    L_0x0468:
        r0 = r26;
        r0 = r0.parent;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x0477;
    L_0x0470:
        r0 = r26;
        r0 = r0.parent;	 Catch:{ all -> 0x00ea }
        r26 = r0;
        goto L_0x0468;
    L_0x0477:
        r0 = r26;
        r0 = r0.object;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x0489;
    L_0x047f:
        r0 = r26;
        r0 = r0.object;	 Catch:{ all -> 0x00ea }
        r24 = r0;
    L_0x0485:
        r37 = r24;
        goto L_0x03f9;
    L_0x0489:
        r33 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00ea }
        r0 = r33;
        r1 = r26;
        r2 = r23;
        r0.<init>(r1, r2);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r33;
        r0.addResolveTask(r1);	 Catch:{ all -> 0x00ea }
        r33 = 1;
        r0 = r36;
        r1 = r33;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00ea }
        goto L_0x0485;
    L_0x04a5:
        r33 = new com.alibaba.fastjson.parser.DefaultJSONParser$ResolveTask;	 Catch:{ all -> 0x00ea }
        r0 = r33;
        r1 = r23;
        r0.<init>(r5, r1);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r33;
        r0.addResolveTask(r1);	 Catch:{ all -> 0x00ea }
        r33 = 1;
        r0 = r36;
        r1 = r33;
        r0.setResolveStatus(r1);	 Catch:{ all -> 0x00ea }
    L_0x04be:
        r37 = r24;
        goto L_0x03f9;
    L_0x04c2:
        r33 = 16;
        r0 = r17;
        r1 = r33;
        r0.nextToken(r1);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        goto L_0x0017;
    L_0x04d2:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "illegal ref, ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.token();	 Catch:{ all -> 0x00ea }
        r35 = com.alibaba.fastjson.parser.JSONToken.name(r35);	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x04f4:
        if (r27 != 0) goto L_0x04ff;
    L_0x04f6:
        r6 = r36.setContext(r37, r38);	 Catch:{ all -> 0x00ea }
        if (r5 != 0) goto L_0x04fd;
    L_0x04fc:
        r5 = r6;
    L_0x04fd:
        r27 = 1;
    L_0x04ff:
        r33 = r37.getClass();	 Catch:{ all -> 0x00ea }
        r34 = com.alibaba.fastjson.JSONObject.class;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x0510;
    L_0x050b:
        if (r16 != 0) goto L_0x055e;
    L_0x050d:
        r16 = "null";
    L_0x0510:
        r33 = 34;
        r0 = r33;
        if (r3 != r0) goto L_0x0563;
    L_0x0516:
        r17.scanString();	 Catch:{ all -> 0x00ea }
        r28 = r17.stringVal();	 Catch:{ all -> 0x00ea }
        r32 = r28;
        r33 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        if (r33 == 0) goto L_0x0543;
    L_0x052b:
        r15 = new com.alibaba.fastjson.parser.JSONScanner;	 Catch:{ all -> 0x00ea }
        r0 = r28;
        r15.<init>(r0);	 Catch:{ all -> 0x00ea }
        r33 = r15.scanISO8601DateIfMatch();	 Catch:{ all -> 0x00ea }
        if (r33 == 0) goto L_0x0540;
    L_0x0538:
        r33 = r15.getCalendar();	 Catch:{ all -> 0x00ea }
        r32 = r33.getTime();	 Catch:{ all -> 0x00ea }
    L_0x0540:
        r15.close();	 Catch:{ all -> 0x00ea }
    L_0x0543:
        r0 = r37;
        r1 = r16;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ all -> 0x00ea }
    L_0x054c:
        r17.skipWhitespace();	 Catch:{ all -> 0x00ea }
        r3 = r17.getCurrent();	 Catch:{ all -> 0x00ea }
        r33 = 44;
        r0 = r33;
        if (r3 != r0) goto L_0x0784;
    L_0x0559:
        r17.next();	 Catch:{ all -> 0x00ea }
        goto L_0x0073;
    L_0x055e:
        r16 = r16.toString();	 Catch:{ all -> 0x00ea }
        goto L_0x0510;
    L_0x0563:
        r33 = 48;
        r0 = r33;
        if (r3 < r0) goto L_0x056f;
    L_0x0569:
        r33 = 57;
        r0 = r33;
        if (r3 <= r0) goto L_0x0575;
    L_0x056f:
        r33 = 45;
        r0 = r33;
        if (r3 != r0) goto L_0x05a5;
    L_0x0575:
        r17.scanNumber();	 Catch:{ all -> 0x00ea }
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 2;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x0592;
    L_0x0584:
        r32 = r17.integerValue();	 Catch:{ all -> 0x00ea }
    L_0x0588:
        r0 = r37;
        r1 = r16;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ all -> 0x00ea }
        goto L_0x054c;
    L_0x0592:
        r33 = com.alibaba.fastjson.parser.Feature.UseBigDecimal;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r32 = r0.decimalValue(r1);	 Catch:{ all -> 0x00ea }
        goto L_0x0588;
    L_0x05a5:
        r33 = 91;
        r0 = r33;
        if (r3 != r0) goto L_0x0603;
    L_0x05ab:
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r18 = new com.alibaba.fastjson.JSONArray;	 Catch:{ all -> 0x00ea }
        r18.<init>();	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r18;
        r2 = r16;
        r0.parseArray(r1, r2);	 Catch:{ all -> 0x00ea }
        r33 = com.alibaba.fastjson.parser.Feature.UseObjectArray;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        if (r33 == 0) goto L_0x05eb;
    L_0x05c8:
        r32 = r18.toArray();	 Catch:{ all -> 0x00ea }
    L_0x05cc:
        r0 = r37;
        r1 = r16;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ all -> 0x00ea }
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 13;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x05ee;
    L_0x05e1:
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        goto L_0x0017;
    L_0x05eb:
        r32 = r18;
        goto L_0x05cc;
    L_0x05ee:
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 16;
        r0 = r33;
        r1 = r34;
        if (r0 == r1) goto L_0x0073;
    L_0x05fa:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = "syntax error";
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x0603:
        r33 = 123; // 0x7b float:1.72E-43 double:6.1E-322;
        r0 = r33;
        if (r3 != r0) goto L_0x0715;
    L_0x0609:
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        if (r38 == 0) goto L_0x06d2;
    L_0x060e:
        r33 = r38.getClass();	 Catch:{ all -> 0x00ea }
        r34 = java.lang.Integer.class;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x06d2;
    L_0x061a:
        r22 = 1;
    L_0x061c:
        r12 = new com.alibaba.fastjson.JSONObject;	 Catch:{ all -> 0x00ea }
        r33 = com.alibaba.fastjson.parser.Feature.OrderedField;	 Catch:{ all -> 0x00ea }
        r0 = r17;
        r1 = r33;
        r33 = r0.isEnabled(r1);	 Catch:{ all -> 0x00ea }
        r0 = r33;
        r12.<init>(r0);	 Catch:{ all -> 0x00ea }
        r7 = 0;
        if (r22 != 0) goto L_0x0638;
    L_0x0630:
        r0 = r36;
        r1 = r16;
        r7 = r0.setContext(r5, r12, r1);	 Catch:{ all -> 0x00ea }
    L_0x0638:
        r20 = 0;
        r21 = 0;
        r0 = r36;
        r0 = r0.fieldTypeResolver;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        if (r33 == 0) goto L_0x0672;
    L_0x0644:
        if (r16 == 0) goto L_0x06d6;
    L_0x0646:
        r25 = r16.toString();	 Catch:{ all -> 0x00ea }
    L_0x064a:
        r0 = r36;
        r0 = r0.fieldTypeResolver;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r0 = r33;
        r1 = r37;
        r2 = r25;
        r11 = r0.resolve(r1, r2);	 Catch:{ all -> 0x00ea }
        if (r11 == 0) goto L_0x0672;
    L_0x065c:
        r0 = r36;
        r0 = r0.config;	 Catch:{ all -> 0x00ea }
        r33 = r0;
        r0 = r33;
        r10 = r0.getDeserializer(r11);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r16;
        r20 = r10.deserialze(r0, r11, r1);	 Catch:{ all -> 0x00ea }
        r21 = 1;
    L_0x0672:
        if (r21 != 0) goto L_0x067c;
    L_0x0674:
        r0 = r36;
        r1 = r16;
        r20 = r0.parseObject(r12, r1);	 Catch:{ all -> 0x00ea }
    L_0x067c:
        if (r7 == 0) goto L_0x0686;
    L_0x067e:
        r0 = r20;
        if (r12 == r0) goto L_0x0686;
    L_0x0682:
        r0 = r37;
        r7.object = r0;	 Catch:{ all -> 0x00ea }
    L_0x0686:
        r33 = r16.toString();	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r37;
        r2 = r33;
        r0.checkMapResolve(r1, r2);	 Catch:{ all -> 0x00ea }
        r33 = r37.getClass();	 Catch:{ all -> 0x00ea }
        r34 = com.alibaba.fastjson.JSONObject.class;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x06da;
    L_0x069f:
        r33 = r16.toString();	 Catch:{ all -> 0x00ea }
        r0 = r37;
        r1 = r33;
        r2 = r20;
        r0.put(r1, r2);	 Catch:{ all -> 0x00ea }
    L_0x06ac:
        if (r22 == 0) goto L_0x06b7;
    L_0x06ae:
        r0 = r36;
        r1 = r20;
        r2 = r16;
        r0.setContext(r1, r2);	 Catch:{ all -> 0x00ea }
    L_0x06b7:
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 13;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x06e4;
    L_0x06c3:
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        goto L_0x0017;
    L_0x06d2:
        r22 = 0;
        goto L_0x061c;
    L_0x06d6:
        r25 = 0;
        goto L_0x064a;
    L_0x06da:
        r0 = r37;
        r1 = r16;
        r2 = r20;
        r0.put(r1, r2);	 Catch:{ all -> 0x00ea }
        goto L_0x06ac;
    L_0x06e4:
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 16;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x06f7;
    L_0x06f0:
        if (r22 == 0) goto L_0x0073;
    L_0x06f2:
        r36.popContext();	 Catch:{ all -> 0x00ea }
        goto L_0x0073;
    L_0x06f7:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "syntax error, ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.tokenName();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x0715:
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r32 = r36.parse();	 Catch:{ all -> 0x00ea }
        r33 = r37.getClass();	 Catch:{ all -> 0x00ea }
        r34 = com.alibaba.fastjson.JSONObject.class;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x072c;
    L_0x0728:
        r16 = r16.toString();	 Catch:{ all -> 0x00ea }
    L_0x072c:
        r0 = r37;
        r1 = r16;
        r2 = r32;
        r0.put(r1, r2);	 Catch:{ all -> 0x00ea }
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 13;
        r0 = r33;
        r1 = r34;
        if (r0 != r1) goto L_0x074b;
    L_0x0741:
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        goto L_0x0017;
    L_0x074b:
        r33 = r17.token();	 Catch:{ all -> 0x00ea }
        r34 = 16;
        r0 = r33;
        r1 = r34;
        if (r0 == r1) goto L_0x0073;
    L_0x0757:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "syntax error, position at ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.pos();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = ", name ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r0 = r34;
        r1 = r16;
        r34 = r0.append(r1);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
    L_0x0784:
        r33 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        r0 = r33;
        if (r3 != r0) goto L_0x07a3;
    L_0x078a:
        r17.next();	 Catch:{ all -> 0x00ea }
        r17.resetStringPosition();	 Catch:{ all -> 0x00ea }
        r17.nextToken();	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r1 = r32;
        r2 = r16;
        r0.setContext(r1, r2);	 Catch:{ all -> 0x00ea }
        r0 = r36;
        r0.setContext(r5);
        goto L_0x0017;
    L_0x07a3:
        r33 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x00ea }
        r34 = new java.lang.StringBuilder;	 Catch:{ all -> 0x00ea }
        r34.<init>();	 Catch:{ all -> 0x00ea }
        r35 = "syntax error, position at ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = r17.pos();	 Catch:{ all -> 0x00ea }
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r35 = ", name ";
        r34 = r34.append(r35);	 Catch:{ all -> 0x00ea }
        r0 = r34;
        r1 = r16;
        r34 = r0.append(r1);	 Catch:{ all -> 0x00ea }
        r34 = r34.toString();	 Catch:{ all -> 0x00ea }
        r33.<init>(r34);	 Catch:{ all -> 0x00ea }
        throw r33;	 Catch:{ all -> 0x00ea }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseObject(java.util.Map, java.lang.Object):java.lang.Object");
    }

    public ParserConfig getConfig() {
        return this.config;
    }

    public void setConfig(ParserConfig config) {
        this.config = config;
    }

    public <T> T parseObject(Class<T> clazz) {
        return parseObject((Type) clazz, null);
    }

    public <T> T parseObject(Type type) {
        return parseObject(type, null);
    }

    public <T> T parseObject(Type type, Object fieldName) {
        int token = this.lexer.token();
        if (token == 8) {
            this.lexer.nextToken();
            return null;
        }
        if (token == 4) {
            if (type == byte[].class) {
                T bytes = this.lexer.bytesValue();
                this.lexer.nextToken();
                return bytes;
            } else if (type == char[].class) {
                String strVal = this.lexer.stringVal();
                this.lexer.nextToken();
                return strVal.toCharArray();
            }
        }
        try {
            return this.config.getDeserializer(type).deserialze(this, type, fieldName);
        } catch (JSONException e) {
            throw e;
        } catch (Throwable e2) {
            JSONException jSONException = new JSONException(e2.getMessage(), e2);
        }
    }

    public <T> List<T> parseArray(Class<T> clazz) {
        Collection array = new ArrayList();
        parseArray((Class) clazz, array);
        return array;
    }

    public void parseArray(Class<?> clazz, Collection array) {
        parseArray((Type) clazz, array);
    }

    public void parseArray(Type type, Collection array) {
        parseArray(type, array, null);
    }

    public void parseArray(Type type, Collection array, Object fieldName) {
        if (this.lexer.token() == 21 || this.lexer.token() == 22) {
            this.lexer.nextToken();
        }
        if (this.lexer.token() != 14) {
            throw new JSONException("exepct '[', but " + JSONToken.name(this.lexer.token()) + ", " + this.lexer.info());
        }
        ObjectDeserializer deserializer;
        if (Integer.TYPE == type) {
            deserializer = IntegerCodec.instance;
            this.lexer.nextToken(2);
        } else if (String.class == type) {
            deserializer = StringCodec.instance;
            this.lexer.nextToken(4);
        } else {
            deserializer = this.config.getDeserializer(type);
            this.lexer.nextToken(deserializer.getFastMatchToken());
        }
        ParseContext context = this.context;
        setContext(array, fieldName);
        int i = 0;
        while (true) {
            if (this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                while (this.lexer.token() == 16) {
                    this.lexer.nextToken();
                }
            }
            try {
                if (this.lexer.token() == 15) {
                    break;
                }
                if (Integer.TYPE == type) {
                    array.add(IntegerCodec.instance.deserialze(this, null, null));
                } else if (String.class == type) {
                    String value;
                    if (this.lexer.token() == 4) {
                        value = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        Object obj = parse();
                        if (obj == null) {
                            value = null;
                        } else {
                            value = obj.toString();
                        }
                    }
                    array.add(value);
                } else {
                    Object obj2;
                    if (this.lexer.token() == 8) {
                        this.lexer.nextToken();
                        obj2 = null;
                    } else {
                        obj2 = deserializer.deserialze(this, type, Integer.valueOf(i));
                    }
                    array.add(obj2);
                    checkListResolve(array);
                }
                if (this.lexer.token() == 16) {
                    this.lexer.nextToken(deserializer.getFastMatchToken());
                }
                i++;
            } finally {
                setContext(context);
            }
        }
        this.lexer.nextToken(16);
    }

    public Object[] parseArray(Type[] types) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken(16);
            return null;
        } else if (this.lexer.token() != 14) {
            throw new JSONException("syntax error : " + this.lexer.tokenName());
        } else {
            Object[] list = new Object[types.length];
            if (types.length == 0) {
                this.lexer.nextToken(15);
                if (this.lexer.token() != 15) {
                    throw new JSONException("syntax error");
                }
                this.lexer.nextToken(16);
                return new Object[0];
            }
            this.lexer.nextToken(2);
            int i = 0;
            while (i < types.length) {
                Object value;
                if (this.lexer.token() == 8) {
                    value = null;
                    this.lexer.nextToken(16);
                } else {
                    Type type = types[i];
                    if (type == Integer.TYPE || type == Integer.class) {
                        if (this.lexer.token() == 2) {
                            value = Integer.valueOf(this.lexer.intValue());
                            this.lexer.nextToken(16);
                        } else {
                            value = TypeUtils.cast(parse(), type, this.config);
                        }
                    } else if (type != String.class) {
                        boolean isArray = false;
                        Type componentType = null;
                        if (i == types.length - 1 && (type instanceof Class)) {
                            Class<?> clazz = (Class) type;
                            isArray = clazz.isArray();
                            componentType = clazz.getComponentType();
                        }
                        if (!isArray || this.lexer.token() == 14) {
                            value = this.config.getDeserializer(type).deserialze(this, type, null);
                        } else {
                            Object varList = new ArrayList();
                            ObjectDeserializer derializer = this.config.getDeserializer(componentType);
                            int fastMatch = derializer.getFastMatchToken();
                            if (this.lexer.token() != 15) {
                                while (true) {
                                    varList.add(derializer.deserialze(this, type, null));
                                    if (this.lexer.token() != 16) {
                                        break;
                                    }
                                    this.lexer.nextToken(fastMatch);
                                }
                                if (this.lexer.token() != 15) {
                                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                                }
                            }
                            value = TypeUtils.cast(varList, type, this.config);
                        }
                    } else if (this.lexer.token() == 4) {
                        value = this.lexer.stringVal();
                        this.lexer.nextToken(16);
                    } else {
                        value = TypeUtils.cast(parse(), type, this.config);
                    }
                }
                list[i] = value;
                if (this.lexer.token() == 15) {
                    break;
                } else if (this.lexer.token() != 16) {
                    throw new JSONException("syntax error :" + JSONToken.name(this.lexer.token()));
                } else {
                    if (i == types.length - 1) {
                        this.lexer.nextToken(15);
                    } else {
                        this.lexer.nextToken(2);
                    }
                    i++;
                }
            }
            if (this.lexer.token() != 15) {
                throw new JSONException("syntax error");
            }
            this.lexer.nextToken(16);
            return list;
        }
    }

    public void parseObject(Object object) {
        Type clazz = object.getClass();
        JavaBeanDeserializer beanDeser = null;
        ObjectDeserializer deserizer = this.config.getDeserializer(clazz);
        if (deserizer instanceof JavaBeanDeserializer) {
            beanDeser = (JavaBeanDeserializer) deserizer;
        }
        if (this.lexer.token() == 12 || this.lexer.token() == 16) {
            while (true) {
                String key = this.lexer.scanSymbol(this.symbolTable);
                if (key == null) {
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    } else if (this.lexer.token() == 16 && this.lexer.isEnabled(Feature.AllowArbitraryCommas)) {
                    }
                }
                FieldDeserializer fieldDeser = null;
                if (beanDeser != null) {
                    fieldDeser = beanDeser.getFieldDeserializer(key);
                }
                if (fieldDeser != null) {
                    Object fieldValue;
                    Class<?> fieldClass = fieldDeser.fieldInfo.fieldClass;
                    Type fieldType = fieldDeser.fieldInfo.fieldType;
                    if (fieldClass == Integer.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        fieldValue = IntegerCodec.instance.deserialze(this, fieldType, null);
                    } else if (fieldClass == String.class) {
                        this.lexer.nextTokenWithColon(4);
                        fieldValue = StringCodec.deserialze(this);
                    } else if (fieldClass == Long.TYPE) {
                        this.lexer.nextTokenWithColon(2);
                        fieldValue = LongCodec.instance.deserialze(this, fieldType, null);
                    } else {
                        ObjectDeserializer fieldValueDeserializer = this.config.getDeserializer(fieldClass, fieldType);
                        this.lexer.nextTokenWithColon(fieldValueDeserializer.getFastMatchToken());
                        fieldValue = fieldValueDeserializer.deserialze(this, fieldType, null);
                    }
                    fieldDeser.setValue(object, fieldValue);
                    if (this.lexer.token() != 16 && this.lexer.token() == 13) {
                        this.lexer.nextToken(16);
                        return;
                    }
                } else if (this.lexer.isEnabled(Feature.IgnoreNotMatch)) {
                    this.lexer.nextTokenWithColon();
                    parse();
                    if (this.lexer.token() == 13) {
                        this.lexer.nextToken();
                        return;
                    }
                } else {
                    throw new JSONException("setter not found, class " + clazz.getName() + ", property " + key);
                }
            }
        }
        throw new JSONException("syntax error, expect {, actual " + this.lexer.tokenName());
    }

    public Object parseArrayWithType(Type collectionType) {
        if (this.lexer.token() == 8) {
            this.lexer.nextToken();
            return null;
        }
        Type[] actualTypes = ((ParameterizedType) collectionType).getActualTypeArguments();
        if (actualTypes.length != 1) {
            throw new JSONException("not support type " + collectionType);
        }
        Type actualTypeArgument = actualTypes[0];
        Collection array;
        if (actualTypeArgument instanceof Class) {
            array = new ArrayList();
            parseArray((Class) actualTypeArgument, array);
            return array;
        } else if (actualTypeArgument instanceof WildcardType) {
            WildcardType wildcardType = (WildcardType) actualTypeArgument;
            Type upperBoundType = wildcardType.getUpperBounds()[0];
            if (!Object.class.equals(upperBoundType)) {
                array = new ArrayList();
                parseArray((Class) upperBoundType, array);
                return array;
            } else if (wildcardType.getLowerBounds().length == 0) {
                return parse();
            } else {
                throw new JSONException("not support type : " + collectionType);
            }
        } else {
            if (actualTypeArgument instanceof TypeVariable) {
                TypeVariable<?> typeVariable = (TypeVariable) actualTypeArgument;
                Type[] bounds = typeVariable.getBounds();
                if (bounds.length != 1) {
                    throw new JSONException("not support : " + typeVariable);
                }
                Type boundType = bounds[0];
                if (boundType instanceof Class) {
                    array = new ArrayList();
                    parseArray((Class) boundType, array);
                    return array;
                }
            }
            if (actualTypeArgument instanceof ParameterizedType) {
                Type parameterizedType = (ParameterizedType) actualTypeArgument;
                array = new ArrayList();
                parseArray(parameterizedType, array);
                return array;
            }
            throw new JSONException("TODO : " + collectionType);
        }
    }

    public void acceptType(String typeName) {
        JSONLexer lexer = this.lexer;
        lexer.nextTokenWithColon();
        if (lexer.token() != 4) {
            throw new JSONException("type not match error");
        } else if (typeName.equals(lexer.stringVal())) {
            lexer.nextToken();
            if (lexer.token() == 16) {
                lexer.nextToken();
            }
        } else {
            throw new JSONException("type not match error");
        }
    }

    public int getResolveStatus() {
        return this.resolveStatus;
    }

    public void setResolveStatus(int resolveStatus) {
        this.resolveStatus = resolveStatus;
    }

    public Object getObject(String path) {
        for (int i = 0; i < this.contextArrayIndex; i++) {
            if (path.equals(this.contextArray[i].toString())) {
                return this.contextArray[i].object;
            }
        }
        return null;
    }

    public void checkListResolve(Collection array) {
        if (this.resolveStatus != 1) {
            return;
        }
        if (array instanceof List) {
            int index = array.size() - 1;
            List list = (List) array;
            ResolveTask task = getLastResolveTask();
            task.fieldDeserializer = new ResolveFieldDeserializer(this, list, index);
            task.ownerContext = this.context;
            setResolveStatus(0);
            return;
        }
        task = getLastResolveTask();
        task.fieldDeserializer = new ResolveFieldDeserializer(array);
        task.ownerContext = this.context;
        setResolveStatus(0);
    }

    public void checkMapResolve(Map object, Object fieldName) {
        if (this.resolveStatus == 1) {
            ResolveFieldDeserializer fieldResolver = new ResolveFieldDeserializer(object, fieldName);
            ResolveTask task = getLastResolveTask();
            task.fieldDeserializer = fieldResolver;
            task.ownerContext = this.context;
            setResolveStatus(0);
        }
    }

    public Object parseObject(Map object) {
        return parseObject(object, null);
    }

    public JSONObject parseObject() {
        return (JSONObject) parseObject(new JSONObject(this.lexer.isEnabled(Feature.OrderedField)));
    }

    public final void parseArray(Collection array) {
        parseArray(array, null);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void parseArray(java.util.Collection r13, java.lang.Object r14) {
        /*
        r12 = this;
        r11 = 4;
        r10 = 16;
        r4 = r12.lexer;
        r8 = r4.token();
        r9 = 21;
        if (r8 == r9) goto L_0x0015;
    L_0x000d:
        r8 = r4.token();
        r9 = 22;
        if (r8 != r9) goto L_0x0018;
    L_0x0015:
        r4.nextToken();
    L_0x0018:
        r8 = r4.token();
        r9 = 14;
        if (r8 == r9) goto L_0x0051;
    L_0x0020:
        r8 = new com.alibaba.fastjson.JSONException;
        r9 = new java.lang.StringBuilder;
        r9.<init>();
        r10 = "syntax error, expect [, actual ";
        r9 = r9.append(r10);
        r10 = r4.token();
        r10 = com.alibaba.fastjson.parser.JSONToken.name(r10);
        r9 = r9.append(r10);
        r10 = ", pos ";
        r9 = r9.append(r10);
        r10 = r4.pos();
        r9 = r9.append(r10);
        r9 = r9.toString();
        r8.<init>(r9);
        throw r8;
    L_0x0051:
        r4.nextToken(r11);
        r0 = r12.context;
        r12.setContext(r13, r14);
        r1 = 0;
    L_0x005a:
        r8 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x006c }
        r8 = r4.isEnabled(r8);	 Catch:{ all -> 0x006c }
        if (r8 == 0) goto L_0x0071;
    L_0x0062:
        r8 = r4.token();	 Catch:{ all -> 0x006c }
        if (r8 != r10) goto L_0x0071;
    L_0x0068:
        r4.nextToken();	 Catch:{ all -> 0x006c }
        goto L_0x0062;
    L_0x006c:
        r8 = move-exception;
        r12.setContext(r0);
        throw r8;
    L_0x0071:
        r8 = r4.token();	 Catch:{ all -> 0x006c }
        switch(r8) {
            case 2: goto L_0x008f;
            case 3: goto L_0x0099;
            case 4: goto L_0x00b2;
            case 5: goto L_0x0078;
            case 6: goto L_0x00de;
            case 7: goto L_0x00e6;
            case 8: goto L_0x0120;
            case 9: goto L_0x0078;
            case 10: goto L_0x0078;
            case 11: goto L_0x0078;
            case 12: goto L_0x00ee;
            case 13: goto L_0x0078;
            case 14: goto L_0x0103;
            case 15: goto L_0x012e;
            case 16: goto L_0x0078;
            case 17: goto L_0x0078;
            case 18: goto L_0x0078;
            case 19: goto L_0x0078;
            case 20: goto L_0x0137;
            case 21: goto L_0x0078;
            case 22: goto L_0x0078;
            case 23: goto L_0x0127;
            default: goto L_0x0078;
        };	 Catch:{ all -> 0x006c }
    L_0x0078:
        r7 = r12.parse();	 Catch:{ all -> 0x006c }
    L_0x007c:
        r13.add(r7);	 Catch:{ all -> 0x006c }
        r12.checkListResolve(r13);	 Catch:{ all -> 0x006c }
        r8 = r4.token();	 Catch:{ all -> 0x006c }
        if (r8 != r10) goto L_0x008c;
    L_0x0088:
        r8 = 4;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
    L_0x008c:
        r1 = r1 + 1;
        goto L_0x005a;
    L_0x008f:
        r7 = r4.integerValue();	 Catch:{ all -> 0x006c }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x0099:
        r8 = com.alibaba.fastjson.parser.Feature.UseBigDecimal;	 Catch:{ all -> 0x006c }
        r8 = r4.isEnabled(r8);	 Catch:{ all -> 0x006c }
        if (r8 == 0) goto L_0x00ac;
    L_0x00a1:
        r8 = 1;
        r7 = r4.decimalValue(r8);	 Catch:{ all -> 0x006c }
    L_0x00a6:
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x00ac:
        r8 = 0;
        r7 = r4.decimalValue(r8);	 Catch:{ all -> 0x006c }
        goto L_0x00a6;
    L_0x00b2:
        r6 = r4.stringVal();	 Catch:{ all -> 0x006c }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        r8 = com.alibaba.fastjson.parser.Feature.AllowISO8601DateFormat;	 Catch:{ all -> 0x006c }
        r8 = r4.isEnabled(r8);	 Catch:{ all -> 0x006c }
        if (r8 == 0) goto L_0x00dc;
    L_0x00c3:
        r2 = new com.alibaba.fastjson.parser.JSONScanner;	 Catch:{ all -> 0x006c }
        r2.<init>(r6);	 Catch:{ all -> 0x006c }
        r8 = r2.scanISO8601DateIfMatch();	 Catch:{ all -> 0x006c }
        if (r8 == 0) goto L_0x00da;
    L_0x00ce:
        r8 = r2.getCalendar();	 Catch:{ all -> 0x006c }
        r7 = r8.getTime();	 Catch:{ all -> 0x006c }
    L_0x00d6:
        r2.close();	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x00da:
        r7 = r6;
        goto L_0x00d6;
    L_0x00dc:
        r7 = r6;
        goto L_0x007c;
    L_0x00de:
        r7 = java.lang.Boolean.TRUE;	 Catch:{ all -> 0x006c }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x00e6:
        r7 = java.lang.Boolean.FALSE;	 Catch:{ all -> 0x006c }
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x00ee:
        r5 = new com.alibaba.fastjson.JSONObject;	 Catch:{ all -> 0x006c }
        r8 = com.alibaba.fastjson.parser.Feature.OrderedField;	 Catch:{ all -> 0x006c }
        r8 = r4.isEnabled(r8);	 Catch:{ all -> 0x006c }
        r5.<init>(r8);	 Catch:{ all -> 0x006c }
        r8 = java.lang.Integer.valueOf(r1);	 Catch:{ all -> 0x006c }
        r7 = r12.parseObject(r5, r8);	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x0103:
        r3 = new com.alibaba.fastjson.JSONArray;	 Catch:{ all -> 0x006c }
        r3.<init>();	 Catch:{ all -> 0x006c }
        r8 = java.lang.Integer.valueOf(r1);	 Catch:{ all -> 0x006c }
        r12.parseArray(r3, r8);	 Catch:{ all -> 0x006c }
        r8 = com.alibaba.fastjson.parser.Feature.UseObjectArray;	 Catch:{ all -> 0x006c }
        r8 = r4.isEnabled(r8);	 Catch:{ all -> 0x006c }
        if (r8 == 0) goto L_0x011d;
    L_0x0117:
        r7 = r3.toArray();	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x011d:
        r7 = r3;
        goto L_0x007c;
    L_0x0120:
        r7 = 0;
        r8 = 4;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x0127:
        r7 = 0;
        r8 = 4;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        goto L_0x007c;
    L_0x012e:
        r8 = 16;
        r4.nextToken(r8);	 Catch:{ all -> 0x006c }
        r12.setContext(r0);
        return;
    L_0x0137:
        r8 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x006c }
        r9 = "unclosed jsonArray";
        r8.<init>(r9);	 Catch:{ all -> 0x006c }
        throw r8;	 Catch:{ all -> 0x006c }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.DefaultJSONParser.parseArray(java.util.Collection, java.lang.Object):void");
    }

    public ParseContext getContext() {
        return this.context;
    }

    public List<ResolveTask> getResolveTaskList() {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        return this.resolveTaskList;
    }

    public void addResolveTask(ResolveTask task) {
        if (this.resolveTaskList == null) {
            this.resolveTaskList = new ArrayList(2);
        }
        this.resolveTaskList.add(task);
    }

    public ResolveTask getLastResolveTask() {
        return (ResolveTask) this.resolveTaskList.get(this.resolveTaskList.size() - 1);
    }

    public List<ExtraProcessor> getExtraProcessors() {
        if (this.extraProcessors == null) {
            this.extraProcessors = new ArrayList(2);
        }
        return this.extraProcessors;
    }

    public List<ExtraTypeProvider> getExtraTypeProviders() {
        if (this.extraTypeProviders == null) {
            this.extraTypeProviders = new ArrayList(2);
        }
        return this.extraTypeProviders;
    }

    public FieldTypeResolver getFieldTypeResolver() {
        return this.fieldTypeResolver;
    }

    public void setFieldTypeResolver(FieldTypeResolver fieldTypeResolver) {
        this.fieldTypeResolver = fieldTypeResolver;
    }

    public void setContext(ParseContext context) {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = context;
        }
    }

    public void popContext() {
        if (!this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            this.context = this.context.parent;
            this.contextArray[this.contextArrayIndex - 1] = null;
            this.contextArrayIndex--;
        }
    }

    public ParseContext setContext(Object object, Object fieldName) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        return setContext(this.context, object, fieldName);
    }

    public ParseContext setContext(ParseContext parent, Object object, Object fieldName) {
        if (this.lexer.isEnabled(Feature.DisableCircularReferenceDetect)) {
            return null;
        }
        this.context = new ParseContext(parent, object, fieldName);
        addContext(this.context);
        return this.context;
    }

    private void addContext(ParseContext context) {
        int i = this.contextArrayIndex;
        this.contextArrayIndex = i + 1;
        if (this.contextArray == null) {
            this.contextArray = new ParseContext[8];
        } else if (i >= this.contextArray.length) {
            ParseContext[] newArray = new ParseContext[((this.contextArray.length * 3) / 2)];
            System.arraycopy(this.contextArray, 0, newArray, 0, this.contextArray.length);
            this.contextArray = newArray;
        }
        this.contextArray[i] = context;
    }

    public Object parse() {
        return parse(null);
    }

    public Object parseKey() {
        if (this.lexer.token() != 18) {
            return parse(null);
        }
        String value = this.lexer.stringVal();
        this.lexer.nextToken(16);
        return value;
    }

    public Object parse(Object fieldName) {
        HashSet<Object> set = null;
        JSONLexer lexer = this.lexer;
        switch (lexer.token()) {
            case 2:
                Number intValue = lexer.integerValue();
                lexer.nextToken();
                return intValue;
            case 3:
                Number value = lexer.decimalValue(lexer.isEnabled(Feature.UseBigDecimal));
                lexer.nextToken();
                return value;
            case 4:
                String stringLiteral = lexer.stringVal();
                lexer.nextToken(16);
                if (lexer.isEnabled(Feature.AllowISO8601DateFormat)) {
                    JSONScanner iso8601Lexer = new JSONScanner(stringLiteral);
                    try {
                        if (iso8601Lexer.scanISO8601DateIfMatch()) {
                            set = iso8601Lexer.getCalendar().getTime();
                            return set;
                        }
                        iso8601Lexer.close();
                    } finally {
                        iso8601Lexer.close();
                    }
                }
                return stringLiteral;
            case 6:
                lexer.nextToken();
                return Boolean.TRUE;
            case 7:
                lexer.nextToken();
                return Boolean.FALSE;
            case 8:
                lexer.nextToken();
                return null;
            case 9:
                lexer.nextToken(18);
                if (lexer.token() != 18) {
                    throw new JSONException("syntax error");
                }
                lexer.nextToken(10);
                accept(10);
                long time = lexer.integerValue().longValue();
                accept(2);
                accept(11);
                return new Date(time);
            case 12:
                return parseObject(new JSONObject(lexer.isEnabled(Feature.OrderedField)), fieldName);
            case 14:
                Collection array = new JSONArray();
                parseArray(array, fieldName);
                return lexer.isEnabled(Feature.UseObjectArray) ? array.toArray() : array;
            case 20:
                if (lexer.isBlankInput()) {
                    return null;
                }
                throw new JSONException("unterminated json string, " + lexer.info());
            case 21:
                lexer.nextToken();
                Collection set2 = new HashSet();
                parseArray(set2, fieldName);
                return set2;
            case 22:
                lexer.nextToken();
                Collection treeSet = new TreeSet();
                parseArray(treeSet, fieldName);
                return treeSet;
            case 23:
                lexer.nextToken();
                return null;
            default:
                throw new JSONException("syntax error, " + lexer.info());
        }
    }

    public void config(Feature feature, boolean state) {
        this.lexer.config(feature, state);
    }

    public boolean isEnabled(Feature feature) {
        return this.lexer.isEnabled(feature);
    }

    public JSONLexer getLexer() {
        return this.lexer;
    }

    public final void accept(int token) {
        JSONLexer lexer = this.lexer;
        if (lexer.token() == token) {
            lexer.nextToken();
            return;
        }
        throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual " + JSONToken.name(lexer.token()));
    }

    public final void accept(int token, int nextExpectToken) {
        JSONLexer lexer = this.lexer;
        if (lexer.token() == token) {
            lexer.nextToken(nextExpectToken);
        } else {
            throwException(token);
        }
    }

    public void throwException(int token) {
        throw new JSONException("syntax error, expect " + JSONToken.name(token) + ", actual " + JSONToken.name(this.lexer.token()));
    }

    public void close() {
        JSONLexer lexer = this.lexer;
        try {
            if (!lexer.isEnabled(Feature.AutoCloseSource) || lexer.token() == 20) {
                lexer.close();
                return;
            }
            throw new JSONException("not close json text, token : " + JSONToken.name(lexer.token()));
        } catch (Throwable th) {
            lexer.close();
        }
    }

    public void handleResovleTask(Object value) {
        if (this.resolveTaskList != null) {
            int size = this.resolveTaskList.size();
            for (int i = 0; i < size; i++) {
                Object refValue;
                ResolveTask task = (ResolveTask) this.resolveTaskList.get(i);
                String ref = task.referenceValue;
                Object object = null;
                if (task.ownerContext != null) {
                    object = task.ownerContext.object;
                }
                if (ref.startsWith("$")) {
                    refValue = getObject(ref);
                } else {
                    refValue = task.context.object;
                }
                FieldDeserializer fieldDeser = task.fieldDeserializer;
                if (fieldDeser != null) {
                    fieldDeser.setValue(object, refValue);
                }
            }
        }
    }

    public void parseExtra(Object object, String key) {
        Object value;
        this.lexer.nextTokenWithColon();
        Type type = null;
        if (this.extraTypeProviders != null) {
            for (ExtraTypeProvider extraProvider : this.extraTypeProviders) {
                type = extraProvider.getExtraType(object, key);
            }
        }
        if (type == null) {
            value = parse();
        } else {
            value = parseObject(type);
        }
        if (object instanceof ExtraProcessable) {
            ((ExtraProcessable) object).processExtra(key, value);
        } else if (this.extraProcessors != null) {
            for (ExtraProcessor process : this.extraProcessors) {
                process.processExtra(object, key, value);
            }
        }
    }
}
