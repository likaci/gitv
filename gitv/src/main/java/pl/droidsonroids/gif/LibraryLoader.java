package pl.droidsonroids.gif;

import android.annotation.SuppressLint;
import android.content.Context;
import com.gala.imageprovider.private.G;

public class LibraryLoader {
    static final String BASE_LIBRARY_NAME = "gala_gif";
    private static final String LOG_TAG = "GifDrawable/LibraryLoader";
    static final String SURFACE_LIBRARY_NAME = "pl_droidsonroids_gif_surface";
    @SuppressLint({"StaticFieldLeak"})
    private static Context sAppContext;

    private LibraryLoader() {
    }

    public static void initialize(Context context) {
        if (context != null) {
            G.a(LOG_TAG, ">>>>> LibraryLoader.initialize(context) success");
        }
        sAppContext = context;
    }

    private static Context getContext() {
        if (sAppContext == null) {
            try {
                sAppContext = (Context) Class.forName("android.app.ActivityThread").getDeclaredMethod("currentApplication", new Class[0]).invoke(null, new Object[0]);
            } catch (Throwable e) {
                throw new IllegalStateException("LibraryLoader not initialized. Call LibraryLoader.initialize() before using library classes.", e);
            }
        }
        return sAppContext;
    }

    static void loadLibrary(Context context) {
        try {
            System.loadLibrary(BASE_LIBRARY_NAME);
        } catch (UnsatisfiedLinkError e) {
            if (sAppContext == null) {
                G.a(LOG_TAG, ">>>>> LibraryLoader.sAppContext is null, initial error");
                getContext();
            }
            ReLinker.loadLibrary(sAppContext);
        }
    }
}
