package pl.droidsonroids.gif;

import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.view.TextureView.SurfaceTextureListener;
import pl.droidsonroids.gif.GifTextureView.PlaceholderDrawListener;

class PlaceholderDrawingSurfaceTextureListener implements SurfaceTextureListener {
    private final PlaceholderDrawListener mDrawer;

    PlaceholderDrawingSurfaceTextureListener(PlaceholderDrawListener drawer) {
        this.mDrawer = drawer;
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        Surface surface = new Surface(surfaceTexture);
        Canvas lockCanvas = surface.lockCanvas(null);
        this.mDrawer.onDrawPlaceholder(lockCanvas);
        surface.unlockCanvasAndPost(lockCanvas);
        surface.release();
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        return false;
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
    }
}
