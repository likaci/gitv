package com.gala.video.cloudui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.util.TypedValue;
import com.alibaba.fastjson.JSON;
import com.gala.sdk.player.constants.PlayerIntentConfig;
import com.gala.video.cloudui.view.model.QCloudImageJsonModel;
import com.gala.video.cloudui.view.model.QCloudTextJsonModel;
import com.gala.video.cloudui.view.model.QCloudViewInfoModel;
import com.gala.video.cloudui.view.model.QCloudViewJsonModel;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.http.util.EncodingUtils;

public class CloudUtils {
    private static Typeface a = null;
    private static String f802a = PlayerIntentConfig.URI_AUTH;
    private static final HashMap<String, Integer> f803a = new HashMap(60);
    static final LinkedHashMap<String, QCloudViewJsonModel> f804a = new LinkedHashMap(18);
    private static final ConcurrentHashMap<String, Integer> f805a = new ConcurrentHashMap(20);
    private static final HashMap<String, Drawable> b = new HashMap(30);

    public static Typeface getTypeface() {
        return a;
    }

    public static void setTypeface(Typeface mTypeface) {
        a = mTypeface;
    }

    static QCloudViewInfoModel a(String str, CloudView cloudView) {
        return new QCloudViewInfoModel(cloudView, (QCloudViewJsonModel) a(cloudView.getContext(), str, f804a, QCloudViewJsonModel.class));
    }

    static QCloudViewInfoModel a(CloudView cloudView, String str) {
        return new QCloudViewInfoModel(cloudView, (QCloudViewJsonModel) b(cloudView.getContext(), str, QCloudViewJsonModel.class));
    }

    static <T> T a(Context context, String str, Map<String, T> map, Class<T> cls) {
        if (map.containsKey(str)) {
            return map.get(str);
        }
        T a = a(context, str, cls);
        if (map.size() > 17) {
            map.clear();
        }
        map.put(str, a);
        return a;
    }

    static <T> T a(Context context, String str, Class<T> cls) {
        try {
            return JSON.parseObject(getStreamFromAssets(context, str), (Class) cls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("please check json file. fatal . path = " + str);
        }
    }

    static <T> T b(Context context, String str, Class<T> cls) {
        try {
            return JSON.parseObject(str, (Class) cls);
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("please check json file. fatal . jsonStream = " + str);
        }
    }

    public static String getStreamFromAssets(Context context, String jsonPath) {
        String str = "";
        try {
            InputStream open = a(context).getAssets().open(jsonPath);
            byte[] bArr = new byte[open.available()];
            open.read(bArr);
            str = EncodingUtils.getString(bArr, "UTF-8");
        } catch (Exception e) {
        }
        return str;
    }

    public static CuteView[] getCloudViews(List<QCloudTextJsonModel> textJsons, List<QCloudImageJsonModel> imageJsons, QCloudViewInfoModel infoModel, CloudView cloudView) {
        int i = 0;
        Context context = cloudView.getContext();
        int listCount = getListCount(textJsons);
        int listCount2 = getListCount(imageJsons);
        CuteView[] cuteViewArr = new CuteView[(listCount + listCount2)];
        for (int i2 = 0; i2 < listCount; i2++) {
            QCloudTextJsonModel qCloudTextJsonModel = (QCloudTextJsonModel) textJsons.get(i2);
            CuteTextView cuteTextView = new CuteTextView();
            cuteTextView.order = qCloudTextJsonModel.order;
            cuteTextView.id = qCloudTextJsonModel.id;
            cuteTextView.setInfoModel(infoModel);
            cuteTextView.cloudView = cloudView;
            cuteTextView.context = cloudView.getContext();
            cuteTextView.text = qCloudTextJsonModel.text;
            cuteTextView.size = getDimenFromResidStr(context, qCloudTextJsonModel.size);
            cuteTextView.focusColor = getColorFromResidStr(context, qCloudTextJsonModel.focusColor);
            cuteTextView.normalColor = getColorFromResidStr(context, qCloudTextJsonModel.normalColor);
            cuteTextView.alphaPercentage = qCloudTextJsonModel.alphaPercentage;
            cuteTextView.antiAlias = qCloudTextJsonModel.antiAlias;
            cuteTextView.ellipsize = TruncateAt.valueOf(qCloudTextJsonModel.ellipsize);
            cuteTextView.marqueeDelay = qCloudTextJsonModel.marqueeDelay;
            cuteTextView.marqueeSpeed = qCloudTextJsonModel.marqueeSpeed;
            cuteTextView.marqueeTextSpace = getDimenFromResidStr(context, qCloudTextJsonModel.marqueeTextSpace);
            cuteTextView.gravity = Gravity4CuteText.valueOf(qCloudTextJsonModel.gravity);
            cuteTextView.lines = qCloudTextJsonModel.lines;
            cuteTextView.lineSpace = getDimenFromResidStr(context, qCloudTextJsonModel.lineSpace);
            cuteTextView.scaleX = qCloudTextJsonModel.scaleX;
            cuteTextView.shadowLayerColor = getColorFromResidStr(context, qCloudTextJsonModel.shadowLayerColor);
            cuteTextView.shadowLayerDx = qCloudTextJsonModel.shadowLayerDx;
            cuteTextView.shadowLayerDy = qCloudTextJsonModel.shadowLayerDy;
            cuteTextView.shadowLayerRadius = qCloudTextJsonModel.shadowLayerRadius;
            cuteTextView.width = getDimenFromResidStr(context, qCloudTextJsonModel.width);
            cuteTextView.height = getDimenFromResidStr(context, qCloudTextJsonModel.height);
            cuteTextView.paddingLeft = getDimenFromResidStr(context, qCloudTextJsonModel.paddingLeft);
            cuteTextView.paddingRight = getDimenFromResidStr(context, qCloudTextJsonModel.paddingRight);
            cuteTextView.marginLeft = getDimenFromResidStr(context, qCloudTextJsonModel.marginLeft);
            cuteTextView.marginTop = getDimenFromResidStr(context, qCloudTextJsonModel.marginTop);
            cuteTextView.marginRight = getDimenFromResidStr(context, qCloudTextJsonModel.marginRight);
            cuteTextView.marginBottom = getDimenFromResidStr(context, qCloudTextJsonModel.marginBottom);
            cuteTextView.bgDrawable = getDrawableFromResidStr(context, qCloudTextJsonModel.bgDrawable);
            cuteTextView.bgWidth = getDimenFromResidStr(context, qCloudTextJsonModel.bgWidth);
            cuteTextView.bgHeight = getDimenFromResidStr(context, qCloudTextJsonModel.bgHeight);
            cuteTextView.bgScaleType = ScaleType4CuteTextBg.valueOf(qCloudTextJsonModel.bgScaleType);
            cuteTextView.bgPaddingLeft = getDimenFromResidStr(context, qCloudTextJsonModel.bgPaddingLeft);
            cuteTextView.bgPaddingRight = getDimenFromResidStr(context, qCloudTextJsonModel.bgPaddingRight);
            cuteTextView.bgPaddingTop = getDimenFromResidStr(context, qCloudTextJsonModel.bgPaddingTop);
            cuteTextView.bgPaddingBottom = getDimenFromResidStr(context, qCloudTextJsonModel.bgPaddingBottom);
            cuteTextView.bgClipCanvas = qCloudTextJsonModel.bgClipCanvas;
            cuteTextView.visible = a(qCloudTextJsonModel.visible);
            cuteTextView.bgVisible = a(qCloudTextJsonModel.bgVisible);
            cuteTextView.font = qCloudTextJsonModel.font;
            cuteTextView.skewX = qCloudTextJsonModel.skewX;
            cuteViewArr[i2] = cuteTextView;
        }
        while (i < listCount2) {
            QCloudImageJsonModel qCloudImageJsonModel = (QCloudImageJsonModel) imageJsons.get(i);
            CuteImageView cuteImageView = new CuteImageView();
            cuteImageView.order = qCloudImageJsonModel.order;
            cuteImageView.id = qCloudImageJsonModel.id;
            cuteImageView.setInfoModel(infoModel);
            cuteImageView.cloudView = cloudView;
            cuteImageView.context = cloudView.getContext();
            cuteImageView.width = getDimenFromResidStr(context, qCloudImageJsonModel.width);
            cuteImageView.height = getDimenFromResidStr(context, qCloudImageJsonModel.height);
            cuteImageView.gravity = Gravity4CuteImage.valueOf(qCloudImageJsonModel.gravity);
            cuteImageView.scaleType = ScaleType4CuteImage.valueOf(qCloudImageJsonModel.scaleType);
            cuteImageView.marginLeft = getDimenFromResidStr(context, qCloudImageJsonModel.marginLeft);
            cuteImageView.marginTop = getDimenFromResidStr(context, qCloudImageJsonModel.marginTop);
            cuteImageView.marginRight = getDimenFromResidStr(context, qCloudImageJsonModel.marginRight);
            cuteImageView.marginBottom = getDimenFromResidStr(context, qCloudImageJsonModel.marginBottom);
            cuteImageView.paddingLeft = getDimenFromResidStr(context, qCloudImageJsonModel.paddingLeft);
            cuteImageView.paddingTop = getDimenFromResidStr(context, qCloudImageJsonModel.paddingTop);
            cuteImageView.paddingRight = getDimenFromResidStr(context, qCloudImageJsonModel.paddingRight);
            cuteImageView.paddingBottom = getDimenFromResidStr(context, qCloudImageJsonModel.paddingBottom);
            cuteImageView.clipCanvas = qCloudImageJsonModel.clipCanvas;
            cuteImageView.visible = a(qCloudImageJsonModel.visible);
            cuteImageView.a(getResIdFromResidStr(context, qCloudImageJsonModel.drawable));
            cuteViewArr[listCount + i] = cuteImageView;
            i++;
        }
        a(cuteViewArr);
        return cuteViewArr;
    }

    static void a(CuteView[] cuteViewArr) {
        for (int i = 0; i < cuteViewArr.length - 1; i++) {
            for (int i2 = i + 1; i2 < cuteViewArr.length; i2++) {
                if (cuteViewArr[i].order > cuteViewArr[i2].order) {
                    CuteView cuteView = cuteViewArr[i];
                    cuteViewArr[i] = cuteViewArr[i2];
                    cuteViewArr[i2] = cuteView;
                }
            }
        }
    }

    static int a(String str) {
        if ("INVISIBLE".equalsIgnoreCase(str)) {
            return 4;
        }
        if ("GONE".equalsIgnoreCase(str)) {
            return 8;
        }
        return 0;
    }

    public static Drawable getCurStateDrawable(Drawable drawable, int[] currentState) {
        if (drawable == null) {
            return null;
        }
        Drawable stateDrawable;
        try {
            stateDrawable = getStateDrawable(drawable, ((Integer) StateListDrawable.class.getMethod("getStateDrawableIndex", new Class[]{int[].class}).invoke((StateListDrawable) drawable, new Object[]{currentState})).intValue());
        } catch (Exception e) {
            stateDrawable = null;
        }
        return stateDrawable;
    }

    public static Drawable getStateDrawable(Drawable drawable, int index) {
        if (drawable == null) {
            return null;
        }
        try {
            return (Drawable) StateListDrawable.class.getMethod("getStateDrawable", new Class[]{Integer.TYPE}).invoke((StateListDrawable) drawable, new Object[]{Integer.valueOf(index)});
        } catch (Exception e) {
            return null;
        }
    }

    public static int calcNinePatchBorder(Drawable d) {
        if (d == null) {
            return 0;
        }
        Rect rect = new Rect();
        d.getPadding(rect);
        int i = rect.top;
        if (i < rect.bottom) {
            i = rect.bottom;
        }
        if (i < rect.left) {
            i = rect.left;
        }
        if (i < rect.right) {
            return rect.right;
        }
        return i;
    }

    public static boolean isMapEmpty(Map<?, ?> map) {
        return map == null || map.isEmpty();
    }

    public static boolean isListEmpty(List<?> list) {
        return list == null || list.isEmpty();
    }

    public static boolean isListLegal(List<?> list, int position) {
        return list != null && list.size() > 0 && position >= 0 && position < list.size();
    }

    public static int getListCount(List<?> list) {
        return list == null ? 0 : list.size();
    }

    public static int getColorFromResidStr(Context context, String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return 0;
        }
        Integer num = (Integer) f805a.get(jsonStr);
        if (num != null) {
            return num.intValue();
        }
        Resources a = a(context);
        int color = a.getColor(a.getIdentifier(jsonStr, "color", f802a));
        f805a.put(jsonStr, Integer.valueOf(color));
        return color;
    }

    public static int getDimenFromResidStr(Context context, String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return 0;
        }
        Integer num = (Integer) f803a.get(jsonStr);
        if (num != null) {
            return num.intValue();
        }
        Resources a = a(context);
        int applyDimension = (int) TypedValue.applyDimension(0, (float) a.getDimensionPixelSize(a.getIdentifier(jsonStr, "dimen", f802a)), a.getDisplayMetrics());
        f803a.put(jsonStr, Integer.valueOf(applyDimension));
        return applyDimension;
    }

    public static int getResIdFromResidStr(Context context, String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return 0;
        }
        return a(context).getIdentifier(jsonStr, "drawable", f802a);
    }

    public static Drawable getDrawableFromResidStr(Context context, String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return null;
        }
        Drawable drawable = (Drawable) b.get(jsonStr);
        if (drawable != null) {
            return drawable;
        }
        Resources a = a(context);
        drawable = a.getDrawable(a.getIdentifier(jsonStr, "drawable", f802a));
        b.put(jsonStr, drawable);
        return drawable;
    }

    public static int getLayoutFromResidStr(Context context, String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return 0;
        }
        return a(context).getIdentifier(jsonStr, "layout", f802a);
    }

    static Context a(Context context) {
        if (context instanceof Activity) {
            return context.getApplicationContext();
        }
        return context;
    }

    static Resources m209a(Context context) {
        return a(context).getResources();
    }

    public static String getPackageName() {
        return f802a;
    }

    public static void setPackageName(String packageName) {
        f802a = packageName;
    }

    public static void clearColorCache() {
        f805a.clear();
    }

    public static void clearDimenCache() {
        f803a.clear();
    }

    public static void clearDrawableCache() {
        b.clear();
    }

    public static void clearJsonCache() {
        f804a.clear();
    }

    public static void clearAllCache() {
        f803a.clear();
        b.clear();
        f805a.clear();
        f804a.clear();
    }
}
