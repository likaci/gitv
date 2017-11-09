package com.alibaba.fastjson.parser.deserializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.DefaultJSONParser.ResolveTask;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.JSONToken;
import com.alibaba.fastjson.parser.ParseContext;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class MapDeserializer implements ObjectDeserializer {
    public static MapDeserializer instance = new MapDeserializer();

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        if (type == JSONObject.class && parser.getFieldTypeResolver() == null) {
            return parser.parseObject();
        }
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        }
        Map<Object, Object> map = createMap(type);
        ParseContext context = parser.getContext();
        try {
            parser.setContext(context, map, fieldName);
            T deserialze = deserialze(parser, type, fieldName, map);
            return deserialze;
        } finally {
            parser.setContext(context);
        }
    }

    protected Object deserialze(DefaultJSONParser parser, Type type, Object fieldName, Map map) {
        if (!(type instanceof ParameterizedType)) {
            return parser.parseObject(map, fieldName);
        }
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Class keyType = parameterizedType.getActualTypeArguments()[0];
        Type valueType = parameterizedType.getActualTypeArguments()[1];
        if (String.class == keyType) {
            return parseMap(parser, map, valueType, fieldName);
        }
        return parseMap(parser, map, keyType, valueType, fieldName);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static java.util.Map parseMap(com.alibaba.fastjson.parser.DefaultJSONParser r14, java.util.Map<java.lang.String, java.lang.Object> r15, java.lang.reflect.Type r16, java.lang.Object r17) {
        /*
        r7 = r14.lexer;
        r11 = r7.token();
        r12 = 12;
        if (r11 == r12) goto L_0x0028;
    L_0x000a:
        r11 = new com.alibaba.fastjson.JSONException;
        r12 = new java.lang.StringBuilder;
        r12.<init>();
        r13 = "syntax error, expect {, actual ";
        r12 = r12.append(r13);
        r13 = r7.token();
        r12 = r12.append(r13);
        r12 = r12.toString();
        r11.<init>(r12);
        throw r11;
    L_0x0028:
        r3 = r14.getContext();
        r5 = 0;
    L_0x002d:
        r7.skipWhitespace();	 Catch:{ all -> 0x0082 }
        r1 = r7.getCurrent();	 Catch:{ all -> 0x0082 }
        r11 = com.alibaba.fastjson.parser.Feature.AllowArbitraryCommas;	 Catch:{ all -> 0x0082 }
        r11 = r7.isEnabled(r11);	 Catch:{ all -> 0x0082 }
        if (r11 == 0) goto L_0x004b;
    L_0x003c:
        r11 = 44;
        if (r1 != r11) goto L_0x004b;
    L_0x0040:
        r7.next();	 Catch:{ all -> 0x0082 }
        r7.skipWhitespace();	 Catch:{ all -> 0x0082 }
        r1 = r7.getCurrent();	 Catch:{ all -> 0x0082 }
        goto L_0x003c;
    L_0x004b:
        r11 = 34;
        if (r1 != r11) goto L_0x0087;
    L_0x004f:
        r11 = r14.getSymbolTable();	 Catch:{ all -> 0x0082 }
        r12 = 34;
        r6 = r7.scanSymbol(r11, r12);	 Catch:{ all -> 0x0082 }
        r7.skipWhitespace();	 Catch:{ all -> 0x0082 }
        r1 = r7.getCurrent();	 Catch:{ all -> 0x0082 }
        r11 = 58;
        if (r1 == r11) goto L_0x012f;
    L_0x0064:
        r11 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0082 }
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0082 }
        r12.<init>();	 Catch:{ all -> 0x0082 }
        r13 = "expect ':' at ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x0082 }
        r13 = r7.pos();	 Catch:{ all -> 0x0082 }
        r12 = r12.append(r13);	 Catch:{ all -> 0x0082 }
        r12 = r12.toString();	 Catch:{ all -> 0x0082 }
        r11.<init>(r12);	 Catch:{ all -> 0x0082 }
        throw r11;	 Catch:{ all -> 0x0082 }
    L_0x0082:
        r11 = move-exception;
        r14.setContext(r3);
        throw r11;
    L_0x0087:
        r11 = 125; // 0x7d float:1.75E-43 double:6.2E-322;
        if (r1 != r11) goto L_0x009a;
    L_0x008b:
        r7.next();	 Catch:{ all -> 0x0082 }
        r7.resetStringPosition();	 Catch:{ all -> 0x0082 }
        r11 = 16;
        r7.nextToken(r11);	 Catch:{ all -> 0x0082 }
        r14.setContext(r3);
    L_0x0099:
        return r15;
    L_0x009a:
        r11 = 39;
        if (r1 != r11) goto L_0x00e2;
    L_0x009e:
        r11 = com.alibaba.fastjson.parser.Feature.AllowSingleQuotes;	 Catch:{ all -> 0x0082 }
        r11 = r7.isEnabled(r11);	 Catch:{ all -> 0x0082 }
        if (r11 != 0) goto L_0x00af;
    L_0x00a6:
        r11 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0082 }
        r12 = "syntax error";
        r11.<init>(r12);	 Catch:{ all -> 0x0082 }
        throw r11;	 Catch:{ all -> 0x0082 }
    L_0x00af:
        r11 = r14.getSymbolTable();	 Catch:{ all -> 0x0082 }
        r12 = 39;
        r6 = r7.scanSymbol(r11, r12);	 Catch:{ all -> 0x0082 }
        r7.skipWhitespace();	 Catch:{ all -> 0x0082 }
        r1 = r7.getCurrent();	 Catch:{ all -> 0x0082 }
        r11 = 58;
        if (r1 == r11) goto L_0x012f;
    L_0x00c4:
        r11 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0082 }
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0082 }
        r12.<init>();	 Catch:{ all -> 0x0082 }
        r13 = "expect ':' at ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x0082 }
        r13 = r7.pos();	 Catch:{ all -> 0x0082 }
        r12 = r12.append(r13);	 Catch:{ all -> 0x0082 }
        r12 = r12.toString();	 Catch:{ all -> 0x0082 }
        r11.<init>(r12);	 Catch:{ all -> 0x0082 }
        throw r11;	 Catch:{ all -> 0x0082 }
    L_0x00e2:
        r11 = com.alibaba.fastjson.parser.Feature.AllowUnQuotedFieldNames;	 Catch:{ all -> 0x0082 }
        r11 = r7.isEnabled(r11);	 Catch:{ all -> 0x0082 }
        if (r11 != 0) goto L_0x00f3;
    L_0x00ea:
        r11 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0082 }
        r12 = "syntax error";
        r11.<init>(r12);	 Catch:{ all -> 0x0082 }
        throw r11;	 Catch:{ all -> 0x0082 }
    L_0x00f3:
        r11 = r14.getSymbolTable();	 Catch:{ all -> 0x0082 }
        r6 = r7.scanSymbolUnQuoted(r11);	 Catch:{ all -> 0x0082 }
        r7.skipWhitespace();	 Catch:{ all -> 0x0082 }
        r1 = r7.getCurrent();	 Catch:{ all -> 0x0082 }
        r11 = 58;
        if (r1 == r11) goto L_0x012f;
    L_0x0106:
        r11 = new com.alibaba.fastjson.JSONException;	 Catch:{ all -> 0x0082 }
        r12 = new java.lang.StringBuilder;	 Catch:{ all -> 0x0082 }
        r12.<init>();	 Catch:{ all -> 0x0082 }
        r13 = "expect ':' at ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x0082 }
        r13 = r7.pos();	 Catch:{ all -> 0x0082 }
        r12 = r12.append(r13);	 Catch:{ all -> 0x0082 }
        r13 = ", actual ";
        r12 = r12.append(r13);	 Catch:{ all -> 0x0082 }
        r12 = r12.append(r1);	 Catch:{ all -> 0x0082 }
        r12 = r12.toString();	 Catch:{ all -> 0x0082 }
        r11.<init>(r12);	 Catch:{ all -> 0x0082 }
        throw r11;	 Catch:{ all -> 0x0082 }
    L_0x012f:
        r7.next();	 Catch:{ all -> 0x0082 }
        r7.skipWhitespace();	 Catch:{ all -> 0x0082 }
        r1 = r7.getCurrent();	 Catch:{ all -> 0x0082 }
        r7.resetStringPosition();	 Catch:{ all -> 0x0082 }
        r11 = com.alibaba.fastjson.JSON.DEFAULT_TYPE_KEY;	 Catch:{ all -> 0x0082 }
        if (r6 != r11) goto L_0x01a7;
    L_0x0140:
        r11 = com.alibaba.fastjson.parser.Feature.DisableSpecialKeyDetect;	 Catch:{ all -> 0x0082 }
        r11 = r7.isEnabled(r11);	 Catch:{ all -> 0x0082 }
        if (r11 != 0) goto L_0x01a7;
    L_0x0148:
        r11 = r14.getSymbolTable();	 Catch:{ all -> 0x0082 }
        r12 = 34;
        r9 = r7.scanSymbol(r11, r12);	 Catch:{ all -> 0x0082 }
        r11 = r14.getConfig();	 Catch:{ all -> 0x0082 }
        r11 = r11.getDefaultClassLoader();	 Catch:{ all -> 0x0082 }
        r2 = com.alibaba.fastjson.util.TypeUtils.loadClass(r9, r11);	 Catch:{ all -> 0x0082 }
        r11 = java.util.Map.class;
        r11 = r11.isAssignableFrom(r2);	 Catch:{ all -> 0x0082 }
        if (r11 == 0) goto L_0x017d;
    L_0x0166:
        r11 = 16;
        r7.nextToken(r11);	 Catch:{ all -> 0x0082 }
        r11 = r7.token();	 Catch:{ all -> 0x0082 }
        r12 = 13;
        if (r11 != r12) goto L_0x01eb;
    L_0x0173:
        r11 = 16;
        r7.nextToken(r11);	 Catch:{ all -> 0x0082 }
        r14.setContext(r3);
        goto L_0x0099;
    L_0x017d:
        r11 = r14.getConfig();	 Catch:{ all -> 0x0082 }
        r4 = r11.getDeserializer(r2);	 Catch:{ all -> 0x0082 }
        r11 = 16;
        r7.nextToken(r11);	 Catch:{ all -> 0x0082 }
        r11 = 2;
        r14.setResolveStatus(r11);	 Catch:{ all -> 0x0082 }
        if (r3 == 0) goto L_0x0199;
    L_0x0190:
        r0 = r17;
        r11 = r0 instanceof java.lang.Integer;	 Catch:{ all -> 0x0082 }
        if (r11 != 0) goto L_0x0199;
    L_0x0196:
        r14.popContext();	 Catch:{ all -> 0x0082 }
    L_0x0199:
        r0 = r17;
        r11 = r4.deserialze(r14, r2, r0);	 Catch:{ all -> 0x0082 }
        r11 = (java.util.Map) r11;	 Catch:{ all -> 0x0082 }
        r14.setContext(r3);
        r15 = r11;
        goto L_0x0099;
    L_0x01a7:
        r7.nextToken();	 Catch:{ all -> 0x0082 }
        if (r5 == 0) goto L_0x01af;
    L_0x01ac:
        r14.setContext(r3);	 Catch:{ all -> 0x0082 }
    L_0x01af:
        r11 = r7.token();	 Catch:{ all -> 0x0082 }
        r12 = 8;
        if (r11 != r12) goto L_0x01d8;
    L_0x01b7:
        r10 = 0;
        r7.nextToken();	 Catch:{ all -> 0x0082 }
    L_0x01bb:
        r15.put(r6, r10);	 Catch:{ all -> 0x0082 }
        r14.checkMapResolve(r15, r6);	 Catch:{ all -> 0x0082 }
        r14.setContext(r3, r10, r6);	 Catch:{ all -> 0x0082 }
        r14.setContext(r3);	 Catch:{ all -> 0x0082 }
        r8 = r7.token();	 Catch:{ all -> 0x0082 }
        r11 = 20;
        if (r8 == r11) goto L_0x01d3;
    L_0x01cf:
        r11 = 15;
        if (r8 != r11) goto L_0x01df;
    L_0x01d3:
        r14.setContext(r3);
        goto L_0x0099;
    L_0x01d8:
        r0 = r16;
        r10 = r14.parseObject(r0, r6);	 Catch:{ all -> 0x0082 }
        goto L_0x01bb;
    L_0x01df:
        r11 = 13;
        if (r8 != r11) goto L_0x01eb;
    L_0x01e3:
        r7.nextToken();	 Catch:{ all -> 0x0082 }
        r14.setContext(r3);
        goto L_0x0099;
    L_0x01eb:
        r5 = r5 + 1;
        goto L_0x002d;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.fastjson.parser.deserializer.MapDeserializer.parseMap(com.alibaba.fastjson.parser.DefaultJSONParser, java.util.Map, java.lang.reflect.Type, java.lang.Object):java.util.Map");
    }

    public static Object parseMap(DefaultJSONParser parser, Map<Object, Object> map, Type keyType, Type valueType, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == 12 || lexer.token() == 16) {
            ObjectDeserializer keyDeserializer = parser.getConfig().getDeserializer(keyType);
            ObjectDeserializer valueDeserializer = parser.getConfig().getDeserializer(valueType);
            lexer.nextToken(keyDeserializer.getFastMatchToken());
            ParseContext context = parser.getContext();
            while (lexer.token() != 13) {
                try {
                    if (lexer.token() == 4 && lexer.isRef() && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                        Map<Object, Object> object = null;
                        lexer.nextTokenWithColon(4);
                        if (lexer.token() == 4) {
                            String ref = lexer.stringVal();
                            if ("..".equals(ref)) {
                                object = context.parent.object;
                            } else if ("$".equals(ref)) {
                                ParseContext rootContext = context;
                                while (rootContext.parent != null) {
                                    rootContext = rootContext.parent;
                                }
                                object = rootContext.object;
                            } else {
                                parser.addResolveTask(new ResolveTask(context, ref));
                                parser.setResolveStatus(1);
                            }
                            lexer.nextToken(13);
                            if (lexer.token() != 13) {
                                throw new JSONException("illegal ref");
                            }
                            lexer.nextToken(16);
                            parser.setContext(context);
                            return object;
                        }
                        throw new JSONException("illegal ref, " + JSONToken.name(lexer.token()));
                    }
                    if (map.size() == 0 && lexer.token() == 4 && JSON.DEFAULT_TYPE_KEY.equals(lexer.stringVal()) && !lexer.isEnabled(Feature.DisableSpecialKeyDetect)) {
                        lexer.nextTokenWithColon(4);
                        lexer.nextToken(16);
                        if (lexer.token() == 13) {
                            lexer.nextToken();
                            return map;
                        }
                        lexer.nextToken(keyDeserializer.getFastMatchToken());
                    }
                    Object key = keyDeserializer.deserialze(parser, keyType, null);
                    if (lexer.token() != 17) {
                        throw new JSONException("syntax error, expect :, actual " + lexer.token());
                    }
                    lexer.nextToken(valueDeserializer.getFastMatchToken());
                    Object value = valueDeserializer.deserialze(parser, valueType, key);
                    parser.checkMapResolve(map, key);
                    map.put(key, value);
                    if (lexer.token() == 16) {
                        lexer.nextToken(keyDeserializer.getFastMatchToken());
                    }
                } finally {
                    parser.setContext(context);
                }
            }
            lexer.nextToken(16);
            parser.setContext(context);
            return map;
        }
        throw new JSONException("syntax error, expect {, actual " + lexer.tokenName());
    }

    protected Map<Object, Object> createMap(Type type) {
        if (type == Properties.class) {
            return new Properties();
        }
        if (type == Hashtable.class) {
            return new Hashtable();
        }
        if (type == IdentityHashMap.class) {
            return new IdentityHashMap();
        }
        if (type == SortedMap.class || type == TreeMap.class) {
            return new TreeMap();
        }
        if (type == ConcurrentMap.class || type == ConcurrentHashMap.class) {
            return new ConcurrentHashMap();
        }
        if (type == Map.class || type == HashMap.class) {
            return new HashMap();
        }
        if (type == LinkedHashMap.class) {
            return new LinkedHashMap();
        }
        if (type instanceof ParameterizedType) {
            return createMap(((ParameterizedType) type).getRawType());
        }
        Class<?> clazz = (Class) type;
        if (clazz.isInterface()) {
            throw new JSONException("unsupport type " + type);
        }
        try {
            return (Map) clazz.newInstance();
        } catch (Exception e) {
            throw new JSONException("unsupport type " + type, e);
        }
    }

    public int getFastMatchToken() {
        return 12;
    }
}
