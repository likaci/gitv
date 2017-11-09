package com.alibaba.fastjson.serializer;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.parser.DefaultJSONParser;
import com.alibaba.fastjson.parser.JSONLexer;
import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
import com.gala.video.app.epg.ui.search.ad.Keys;
import com.gala.video.lib.share.common.configs.WebConstants;
import com.gala.video.webview.utils.WebSDKConstants;
import com.mcto.ads.internal.net.TrackingConstants;
import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.lang.reflect.Type;
import org.xbill.DNS.WKSRecord.Service;

public class AwtCodec implements ObjectSerializer, ObjectDeserializer {
    public static final AwtCodec instance = new AwtCodec();

    public static boolean support(Class<?> clazz) {
        return clazz == Point.class || clazz == Rectangle.class || clazz == Font.class || clazz == Color.class;
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {
        SerializeWriter out = serializer.out;
        if (object == null) {
            out.writeNull();
            return;
        }
        if (object instanceof Point) {
            Point font = (Point) object;
            out.writeFieldValue(writeClassName(out, Point.class, '{'), WebConstants.PARAM_KEY_X, font.getX());
            out.writeFieldValue(',', WebConstants.PARAM_KEY_Y, font.getY());
        } else if (object instanceof Font) {
            Font font2 = (Font) object;
            out.writeFieldValue(writeClassName(out, Font.class, '{'), WebSDKConstants.PARAM_KEY_PL_NAME, font2.getName());
            out.writeFieldValue(',', "style", font2.getStyle());
            out.writeFieldValue(',', "size", font2.getSize());
        } else if (object instanceof Rectangle) {
            Rectangle rectangle = (Rectangle) object;
            out.writeFieldValue(writeClassName(out, Rectangle.class, '{'), WebConstants.PARAM_KEY_X, rectangle.getX());
            out.writeFieldValue(',', WebConstants.PARAM_KEY_Y, rectangle.getY());
            out.writeFieldValue(',', "width", rectangle.getWidth());
            out.writeFieldValue(',', "height", rectangle.getHeight());
        } else if (object instanceof Color) {
            Color color = (Color) object;
            out.writeFieldValue(writeClassName(out, Color.class, '{'), "r", color.getRed());
            out.writeFieldValue(',', Keys.G, color.getGreen());
            out.writeFieldValue(',', TrackingConstants.TRACKING_KEY_TIMESTAMP, color.getBlue());
            if (color.getAlpha() > 0) {
                out.writeFieldValue(',', "alpha", color.getAlpha());
            }
        } else {
            throw new JSONException("not support awt class : " + object.getClass().getName());
        }
        out.write((int) Service.LOCUS_MAP);
    }

    protected char writeClassName(SerializeWriter out, Class<?> clazz, char sep) {
        if (!out.isEnabled(SerializerFeature.WriteClassName)) {
            return sep;
        }
        out.write((int) Service.NTP);
        out.writeFieldName(JSON.DEFAULT_TYPE_KEY);
        out.writeString(clazz.getName());
        return ',';
    }

    public <T> T deserialze(DefaultJSONParser parser, Type type, Object fieldName) {
        JSONLexer lexer = parser.lexer;
        if (lexer.token() == 8) {
            lexer.nextToken(16);
            return null;
        } else if (lexer.token() == 12 || lexer.token() == 16) {
            lexer.nextToken();
            if (type == Point.class) {
                return parsePoint(parser);
            }
            if (type == Rectangle.class) {
                return parseRectangle(parser);
            }
            if (type == Color.class) {
                return parseColor(parser);
            }
            if (type == Font.class) {
                return parseFont(parser);
            }
            throw new JSONException("not support awt class : " + type);
        } else {
            throw new JSONException("syntax error");
        }
    }

    protected Font parseFont(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        int size = 0;
        int style = 0;
        String name = null;
        while (lexer.token() != 13) {
            if (lexer.token() == 4) {
                String key = lexer.stringVal();
                lexer.nextTokenWithColon(2);
                if (key.equalsIgnoreCase(WebSDKConstants.PARAM_KEY_PL_NAME)) {
                    if (lexer.token() == 4) {
                        name = lexer.stringVal();
                        lexer.nextToken();
                    } else {
                        throw new JSONException("syntax error");
                    }
                } else if (key.equalsIgnoreCase("style")) {
                    if (lexer.token() == 2) {
                        style = lexer.intValue();
                        lexer.nextToken();
                    } else {
                        throw new JSONException("syntax error");
                    }
                } else if (!key.equalsIgnoreCase("size")) {
                    throw new JSONException("syntax error, " + key);
                } else if (lexer.token() == 2) {
                    size = lexer.intValue();
                    lexer.nextToken();
                } else {
                    throw new JSONException("syntax error");
                }
                if (lexer.token() == 16) {
                    lexer.nextToken(4);
                }
            } else {
                throw new JSONException("syntax error");
            }
        }
        lexer.nextToken();
        return new Font(name, style, size);
    }

    protected Color parseColor(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        int r = 0;
        int g = 0;
        int b = 0;
        int alpha = 0;
        while (lexer.token() != 13) {
            if (lexer.token() == 4) {
                String key = lexer.stringVal();
                lexer.nextTokenWithColon(2);
                if (lexer.token() == 2) {
                    int val = lexer.intValue();
                    lexer.nextToken();
                    if (key.equalsIgnoreCase("r")) {
                        r = val;
                    } else if (key.equalsIgnoreCase(Keys.G)) {
                        g = val;
                    } else if (key.equalsIgnoreCase(TrackingConstants.TRACKING_KEY_TIMESTAMP)) {
                        b = val;
                    } else if (key.equalsIgnoreCase("alpha")) {
                        alpha = val;
                    } else {
                        throw new JSONException("syntax error, " + key);
                    }
                    if (lexer.token() == 16) {
                        lexer.nextToken(4);
                    }
                } else {
                    throw new JSONException("syntax error");
                }
            }
            throw new JSONException("syntax error");
        }
        lexer.nextToken();
        return new Color(r, g, b, alpha);
    }

    protected Rectangle parseRectangle(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        int x = 0;
        int y = 0;
        int width = 0;
        int height = 0;
        while (lexer.token() != 13) {
            if (lexer.token() == 4) {
                String key = lexer.stringVal();
                lexer.nextTokenWithColon(2);
                if (lexer.token() == 2) {
                    int val = lexer.intValue();
                    lexer.nextToken();
                    if (key.equalsIgnoreCase(WebConstants.PARAM_KEY_X)) {
                        x = val;
                    } else if (key.equalsIgnoreCase(WebConstants.PARAM_KEY_Y)) {
                        y = val;
                    } else if (key.equalsIgnoreCase("width")) {
                        width = val;
                    } else if (key.equalsIgnoreCase("height")) {
                        height = val;
                    } else {
                        throw new JSONException("syntax error, " + key);
                    }
                    if (lexer.token() == 16) {
                        lexer.nextToken(4);
                    }
                } else {
                    throw new JSONException("syntax error");
                }
            }
            throw new JSONException("syntax error");
        }
        lexer.nextToken();
        return new Rectangle(x, y, width, height);
    }

    protected Point parsePoint(DefaultJSONParser parser) {
        JSONLexer lexer = parser.lexer;
        int x = 0;
        int y = 0;
        while (lexer.token() != 13) {
            if (lexer.token() == 4) {
                String key = lexer.stringVal();
                if (JSON.DEFAULT_TYPE_KEY.equals(key)) {
                    parser.acceptType("java.awt.Point");
                } else {
                    lexer.nextTokenWithColon(2);
                    if (lexer.token() == 2) {
                        int val = lexer.intValue();
                        lexer.nextToken();
                        if (key.equalsIgnoreCase(WebConstants.PARAM_KEY_X)) {
                            x = val;
                        } else if (key.equalsIgnoreCase(WebConstants.PARAM_KEY_Y)) {
                            y = val;
                        } else {
                            throw new JSONException("syntax error, " + key);
                        }
                        if (lexer.token() == 16) {
                            lexer.nextToken(4);
                        }
                    } else {
                        throw new JSONException("syntax error : " + lexer.tokenName());
                    }
                }
            }
            throw new JSONException("syntax error");
        }
        lexer.nextToken();
        return new Point(x, y);
    }

    public int getFastMatchToken() {
        return 12;
    }
}
