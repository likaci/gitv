package com.gala.tv.voice.service;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import com.gala.tv.voice.core.Log;
import com.gala.tv.voice.core.VoiceUtils;
import java.util.HashMap;
import java.util.Map;

class VoiceViewHelper {
    private static int a = 0;
    private static int b = 0;

    private VoiceViewHelper() {
    }

    public static Map<String, View> scanView(DialogInterface dialogInterface) {
        Map hashMap = new HashMap();
        if (dialogInterface instanceof Dialog) {
            Dialog dialog = (Dialog) dialogInterface;
            if (dialog.isShowing()) {
                hashMap.putAll(a(dialog.getWindow().getDecorView()));
            }
        }
        return null;
    }

    public static View searchView(DialogInterface dialogInterface, String str) {
        Log.d("VoiceViewHelper", "searchView(" + dialogInterface + ", " + str + ")");
        View view = null;
        if (dialogInterface instanceof Dialog) {
            Dialog dialog = (Dialog) dialogInterface;
            if (dialog.isShowing()) {
                view = a(dialog.getWindow().getDecorView(), str);
            }
        }
        Log.d("VoiceViewHelper", "searchView(" + str + ") return " + view);
        return view;
    }

    public static boolean performView(DialogInterface dialogInterface, View view) {
        Log.d("VoiceViewHelper", "performView(" + dialogInterface + ", " + view + ") " + VoiceUtils.dumpViewState(view));
        boolean z = false;
        if ((dialogInterface instanceof Dialog) && ((Dialog) dialogInterface).isShowing()) {
            z = performView(view);
        }
        Log.d("VoiceViewHelper", "performView() return " + z);
        return z;
    }

    public static Map<String, View> scanView(Activity activity) {
        Map<String, View> hashMap = new HashMap();
        if (!(activity == null || activity.isFinishing())) {
            hashMap.putAll(a(activity.getWindow().getDecorView()));
        }
        return hashMap;
    }

    public static View searchView(Activity activity, String str) {
        Log.d("VoiceViewHelper", "searchView(" + activity + ", " + str + ")");
        View view = null;
        if (!(activity == null || activity.isFinishing())) {
            view = a(activity.getWindow().getDecorView(), str);
        }
        Log.d("VoiceViewHelper", "searchView(" + str + ") return " + view);
        return view;
    }

    public static boolean performView(Activity activity, View view) {
        Log.d("VoiceViewHelper", "performView(" + activity + ", " + view + ") " + VoiceUtils.dumpViewState(view));
        boolean z = false;
        if (!(activity == null || activity.isFinishing())) {
            z = performView(view);
        }
        Log.d("VoiceViewHelper", "performView() return " + z);
        return z;
    }

    public static boolean performView(final View view) {
        Log.d("VoiceViewHelper", "performView(" + VoiceUtils.dumpViewState(view) + ")");
        boolean z = false;
        if (b(view)) {
            view.post(new Runnable() {
                public final void run() {
                    if (view.isFocusable()) {
                        view.requestFocus();
                    }
                    view.performClick();
                }
            });
            z = true;
        } else if (isAbsOperational(view)) {
            z = a(view);
        }
        Log.d("VoiceViewHelper", "performView() return " + z);
        return z;
    }

    private static boolean m75a(final View view) {
        boolean z;
        if (view != null && (view.getParent() instanceof AbsListView)) {
            Log.d("VoiceViewHelper", "performAbsListView() try AbsListView!");
            final AbsListView absListView = (AbsListView) view.getParent();
            int childCount = absListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (absListView.getChildAt(i) == view) {
                    Log.d("VoiceViewHelper", "performAbsListView() try AbsListView! find position " + i);
                    view.post(new Runnable() {
                        public final void run() {
                            absListView.setSelection(i);
                            absListView.performItemClick(view, i, absListView.getItemIdAtPosition(i));
                        }
                    });
                    z = true;
                    break;
                }
            }
        }
        z = false;
        Log.d("VoiceViewHelper", "performAbsListView() return " + z);
        return z;
    }

    private static View a(View view, String str) {
        View view2;
        Log.d("VoiceViewHelper", "collectView() begin. " + VoiceUtils.dumpViewState(view));
        if (view != null && view.isEnabled() && view.isShown()) {
            if (view instanceof ViewGroup) {
                view2 = (ViewGroup) view;
                int childCount = view2.getChildCount();
                int i = 0;
                while (i < childCount) {
                    View a = a(view2.getChildAt(i), str);
                    if (a != null) {
                        if (b(a)) {
                            view2 = a;
                        } else if (isAbsOperational(a)) {
                            view2 = a;
                        } else {
                            Log.d("VoiceViewHelper", "collectView() find(" + VoiceUtils.dumpViewState(view2) + ") switch to group(" + VoiceUtils.dumpViewState(view2) + ")");
                        }
                        if (view2 == null && a(view, str)) {
                            view2 = view;
                        }
                    } else {
                        i++;
                    }
                }
                view2 = null;
                view2 = view;
            } else if (b(view)) {
                Log.d("VoiceViewHelper", "matchText(" + view + ", " + str + ")");
                boolean contain = VoiceUtils.contain(a(view), str);
                Log.d("VoiceViewHelper", "matchText() return " + contain);
                if (contain || a(view, str)) {
                    view2 = view;
                }
            }
            Log.d("VoiceViewHelper", "collectView() find view=" + VoiceUtils.dumpViewState(view));
            return view2;
        }
        view2 = null;
        Log.d("VoiceViewHelper", "collectView() find view=" + VoiceUtils.dumpViewState(view));
        return view2;
    }

    private static Map<String, View> m74a(View view) {
        Map<String, View> hashMap = new HashMap();
        if (view != null && view.isEnabled() && view.isShown()) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    hashMap.putAll(a(viewGroup.getChildAt(i)));
                }
            } else if (b(view)) {
                CharSequence a = a(view);
                if (!TextUtils.isEmpty(a)) {
                    hashMap.put(a, view);
                }
                a = b(view);
                if (!TextUtils.isEmpty(a)) {
                    hashMap.put(a, view);
                }
            }
        }
        return hashMap;
    }

    private static String a(View view) {
        String str = null;
        Log.d("VoiceViewHelper", "getText(" + view + ")");
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (textView.getText() != null) {
                str = textView.getText().toString().trim();
            }
        }
        Log.d("VoiceViewHelper", "getText() text=" + str);
        return str;
    }

    private static boolean m76a(View view, String str) {
        Log.d("VoiceViewHelper", "matchContentDescription(" + view + ", " + str + ")");
        boolean contain = VoiceUtils.contain(b(view), str);
        Log.d("VoiceViewHelper", "matchContentDescription() return " + contain);
        return contain;
    }

    private static String b(View view) {
        String str = null;
        Log.d("VoiceViewHelper", "getContentDescription(" + view + ")");
        if (!(view == null || TextUtils.isEmpty(view.getContentDescription()))) {
            str = view.getContentDescription().toString().trim();
        }
        Log.d("VoiceViewHelper", "getContentDescription() description=" + str);
        return str;
    }

    private static boolean m77b(View view) {
        boolean z;
        Log.d("VoiceViewHelper", "isOperational(" + VoiceUtils.dumpViewState(view) + ")");
        if (view != null && view.isEnabled() && view.isShown() && view.isClickable() && view.isFocusable()) {
            int i;
            if (view instanceof TextView) {
                Log.d("VoiceViewHelper", "isUserVisable---textView.getText() = " + ((TextView) view).getText());
            }
            Log.d("VoiceViewHelper", " isUserVisable---view.getContentDescription() =" + view.getContentDescription());
            if (a == 0 || b == 0) {
                Point point = new Point();
                Activity currentActivity = VoiceManager.instance().getCurrentActivity();
                if (currentActivity != null) {
                    Log.d("VoiceViewHelper", "initScreenSize---activity!=null");
                    currentActivity.getWindowManager().getDefaultDisplay().getSize(point);
                    a = point.x;
                    b = point.y;
                }
            }
            if (view.getLocalVisibleRect(new Rect(0, 0, a, b))) {
                Log.d("VoiceViewHelper", "isUserVisable-- return true");
                i = 1;
            } else {
                Log.d("VoiceViewHelper", "isUserVisable--- return false");
                i = 0;
            }
            if (i != 0) {
                z = true;
                Log.d("VoiceViewHelper", "isOpearational() return " + z);
                return z;
            }
        }
        z = false;
        Log.d("VoiceViewHelper", "isOpearational() return " + z);
        return z;
    }

    public static boolean isAbsOperational(View view) {
        boolean z = false;
        Log.d("VoiceViewHelper", "isAbsOperational(" + VoiceUtils.dumpViewState(view) + ")");
        if (view != null && view.isEnabled() && view.isShown() && (view.getParent() instanceof AbsListView)) {
            AbsListView absListView = (AbsListView) view.getParent();
            boolean z2 = absListView.isEnabled() && absListView.isShown();
            z = z2;
        }
        Log.d("VoiceViewHelper", "isAbsOperational() find AbsListView! view=" + view + ", parent=" + (view != null ? view.getParent() : null));
        Log.d("VoiceViewHelper", "isAbsOperational() return " + z);
        return z;
    }
}
