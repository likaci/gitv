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
    private static int f855a = 0;
    private static int f856b = 0;

    private VoiceViewHelper() {
    }

    public static Map<String, View> scanView(DialogInterface dialogInterface) {
        Map hashMap = new HashMap();
        if (dialogInterface instanceof Dialog) {
            Dialog dialog = (Dialog) dialogInterface;
            if (dialog.isShowing()) {
                hashMap.putAll(m559a(dialog.getWindow().getDecorView()));
            }
        }
        return null;
    }

    public static View searchView(DialogInterface dialogInterface, String str) {
        Log.m525d("VoiceViewHelper", "searchView(" + dialogInterface + ", " + str + ")");
        View view = null;
        if (dialogInterface instanceof Dialog) {
            Dialog dialog = (Dialog) dialogInterface;
            if (dialog.isShowing()) {
                view = m558a(dialog.getWindow().getDecorView(), str);
            }
        }
        Log.m525d("VoiceViewHelper", "searchView(" + str + ") return " + view);
        return view;
    }

    public static boolean performView(DialogInterface dialogInterface, View view) {
        Log.m525d("VoiceViewHelper", "performView(" + dialogInterface + ", " + view + ") " + VoiceUtils.dumpViewState(view));
        boolean z = false;
        if ((dialogInterface instanceof Dialog) && ((Dialog) dialogInterface).isShowing()) {
            z = performView(view);
        }
        Log.m525d("VoiceViewHelper", "performView() return " + z);
        return z;
    }

    public static Map<String, View> scanView(Activity activity) {
        Map<String, View> hashMap = new HashMap();
        if (!(activity == null || activity.isFinishing())) {
            hashMap.putAll(m559a(activity.getWindow().getDecorView()));
        }
        return hashMap;
    }

    public static View searchView(Activity activity, String str) {
        Log.m525d("VoiceViewHelper", "searchView(" + activity + ", " + str + ")");
        View view = null;
        if (!(activity == null || activity.isFinishing())) {
            view = m558a(activity.getWindow().getDecorView(), str);
        }
        Log.m525d("VoiceViewHelper", "searchView(" + str + ") return " + view);
        return view;
    }

    public static boolean performView(Activity activity, View view) {
        Log.m525d("VoiceViewHelper", "performView(" + activity + ", " + view + ") " + VoiceUtils.dumpViewState(view));
        boolean z = false;
        if (!(activity == null || activity.isFinishing())) {
            z = performView(view);
        }
        Log.m525d("VoiceViewHelper", "performView() return " + z);
        return z;
    }

    public static boolean performView(final View view) {
        Log.m525d("VoiceViewHelper", "performView(" + VoiceUtils.dumpViewState(view) + ")");
        boolean z = false;
        if (m563b(view)) {
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
            z = m559a(view);
        }
        Log.m525d("VoiceViewHelper", "performView() return " + z);
        return z;
    }

    private static boolean m561a(final View view) {
        boolean z;
        if (view != null && (view.getParent() instanceof AbsListView)) {
            Log.m525d("VoiceViewHelper", "performAbsListView() try AbsListView!");
            final AbsListView absListView = (AbsListView) view.getParent();
            int childCount = absListView.getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (absListView.getChildAt(i) == view) {
                    Log.m525d("VoiceViewHelper", "performAbsListView() try AbsListView! find position " + i);
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
        Log.m525d("VoiceViewHelper", "performAbsListView() return " + z);
        return z;
    }

    private static View m558a(View view, String str) {
        View view2;
        Log.m525d("VoiceViewHelper", "collectView() begin. " + VoiceUtils.dumpViewState(view));
        if (view != null && view.isEnabled() && view.isShown()) {
            if (view instanceof ViewGroup) {
                view2 = (ViewGroup) view;
                int childCount = view2.getChildCount();
                int i = 0;
                while (i < childCount) {
                    View a = m558a(view2.getChildAt(i), str);
                    if (a != null) {
                        if (m563b(a)) {
                            view2 = a;
                        } else if (isAbsOperational(a)) {
                            view2 = a;
                        } else {
                            Log.m525d("VoiceViewHelper", "collectView() find(" + VoiceUtils.dumpViewState(view2) + ") switch to group(" + VoiceUtils.dumpViewState(view2) + ")");
                        }
                        if (view2 == null && m558a(view, str)) {
                            view2 = view;
                        }
                    } else {
                        i++;
                    }
                }
                view2 = null;
                view2 = view;
            } else if (m563b(view)) {
                Log.m525d("VoiceViewHelper", "matchText(" + view + ", " + str + ")");
                boolean contain = VoiceUtils.contain(m559a(view), str);
                Log.m525d("VoiceViewHelper", "matchText() return " + contain);
                if (contain || m558a(view, str)) {
                    view2 = view;
                }
            }
            Log.m525d("VoiceViewHelper", "collectView() find view=" + VoiceUtils.dumpViewState(view));
            return view2;
        }
        view2 = null;
        Log.m525d("VoiceViewHelper", "collectView() find view=" + VoiceUtils.dumpViewState(view));
        return view2;
    }

    private static Map<String, View> m560a(View view) {
        Map<String, View> hashMap = new HashMap();
        if (view != null && view.isEnabled() && view.isShown()) {
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    hashMap.putAll(m559a(viewGroup.getChildAt(i)));
                }
            } else if (m563b(view)) {
                CharSequence a = m559a(view);
                if (!TextUtils.isEmpty(a)) {
                    hashMap.put(a, view);
                }
                a = m563b(view);
                if (!TextUtils.isEmpty(a)) {
                    hashMap.put(a, view);
                }
            }
        }
        return hashMap;
    }

    private static String m559a(View view) {
        String str = null;
        Log.m525d("VoiceViewHelper", "getText(" + view + ")");
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            if (textView.getText() != null) {
                str = textView.getText().toString().trim();
            }
        }
        Log.m525d("VoiceViewHelper", "getText() text=" + str);
        return str;
    }

    private static boolean m562a(View view, String str) {
        Log.m525d("VoiceViewHelper", "matchContentDescription(" + view + ", " + str + ")");
        boolean contain = VoiceUtils.contain(m563b(view), str);
        Log.m525d("VoiceViewHelper", "matchContentDescription() return " + contain);
        return contain;
    }

    private static String m563b(View view) {
        String str = null;
        Log.m525d("VoiceViewHelper", "getContentDescription(" + view + ")");
        if (!(view == null || TextUtils.isEmpty(view.getContentDescription()))) {
            str = view.getContentDescription().toString().trim();
        }
        Log.m525d("VoiceViewHelper", "getContentDescription() description=" + str);
        return str;
    }

    private static boolean m564b(View view) {
        boolean z;
        Log.m525d("VoiceViewHelper", "isOperational(" + VoiceUtils.dumpViewState(view) + ")");
        if (view != null && view.isEnabled() && view.isShown() && view.isClickable() && view.isFocusable()) {
            int i;
            if (view instanceof TextView) {
                Log.m525d("VoiceViewHelper", "isUserVisable---textView.getText() = " + ((TextView) view).getText());
            }
            Log.m525d("VoiceViewHelper", " isUserVisable---view.getContentDescription() =" + view.getContentDescription());
            if (f855a == 0 || f856b == 0) {
                Point point = new Point();
                Activity currentActivity = VoiceManager.instance().getCurrentActivity();
                if (currentActivity != null) {
                    Log.m525d("VoiceViewHelper", "initScreenSize---activity!=null");
                    currentActivity.getWindowManager().getDefaultDisplay().getSize(point);
                    f855a = point.x;
                    f856b = point.y;
                }
            }
            if (view.getLocalVisibleRect(new Rect(0, 0, f855a, f856b))) {
                Log.m525d("VoiceViewHelper", "isUserVisable-- return true");
                i = 1;
            } else {
                Log.m525d("VoiceViewHelper", "isUserVisable--- return false");
                i = 0;
            }
            if (i != 0) {
                z = true;
                Log.m525d("VoiceViewHelper", "isOpearational() return " + z);
                return z;
            }
        }
        z = false;
        Log.m525d("VoiceViewHelper", "isOpearational() return " + z);
        return z;
    }

    public static boolean isAbsOperational(View view) {
        boolean z = false;
        Log.m525d("VoiceViewHelper", "isAbsOperational(" + VoiceUtils.dumpViewState(view) + ")");
        if (view != null && view.isEnabled() && view.isShown() && (view.getParent() instanceof AbsListView)) {
            AbsListView absListView = (AbsListView) view.getParent();
            boolean z2 = absListView.isEnabled() && absListView.isShown();
            z = z2;
        }
        Log.m525d("VoiceViewHelper", "isAbsOperational() find AbsListView! view=" + view + ", parent=" + (view != null ? view.getParent() : null));
        Log.m525d("VoiceViewHelper", "isAbsOperational() return " + z);
        return z;
    }
}
