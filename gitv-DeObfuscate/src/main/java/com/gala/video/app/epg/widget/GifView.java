package com.gala.video.app.epg.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import com.gala.video.lib.framework.core.utils.StringUtils;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class GifView extends ImageView {
    private static final int DEFAULT_MOVIEW_DURATION = 1000;
    private static final int SIZE = 16384;
    private int mCurrentAnimationTime = 0;
    protected Movie mMovie;
    private long mMovieStart;
    private float mPaddingLeft;
    private float mPaddingTop;
    private volatile boolean mPaused = false;
    private float mScaleH;
    private float mScaleW;
    private boolean mVisible = true;

    public GifView(Context context) {
        super(context);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GifView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setLayerType(1, null);
    }

    public void setImageResource(int resId) {
        Log.d("GifView", "GifView setImageResource resId = " + resId);
        InputStream inputStream = null;
        try {
            inputStream = getResources().openRawResource(resId);
            this.mMovie = Movie.decodeStream(inputStream);
        } catch (Exception e) {
        } finally {
            closeStream(inputStream);
        }
        if (this.mMovie == null) {
            setLayerType(2, null);
            super.setImageResource(resId);
            return;
        }
        setLayerType(1, null);
        setImageBitmap(null);
        requestLayout();
    }

    private void closeStream(InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
            }
        }
    }

    public void setImageResource(String path) throws FileNotFoundException {
        Log.d("GifView", "GifView setImageResource path = " + path);
        if (StringUtils.isEmpty((CharSequence) path)) {
            throw new FileNotFoundException("gif path must not be null !");
        }
        setImageResource(new File(path));
    }

    public void setImageResource(File file) throws FileNotFoundException {
        Exception e;
        Bitmap bitmap;
        String str;
        StringBuilder append;
        Throwable th;
        boolean z = true;
        Log.d("GifView", "GifView setImageResource file = " + file);
        if (file == null || !file.exists()) {
            throw new FileNotFoundException("Gif not found the gif file !");
        }
        BufferedInputStream is = null;
        try {
            BufferedInputStream is2 = new BufferedInputStream(new FileInputStream(file), 16384);
            try {
                is2.mark(16384);
                this.mMovie = Movie.decodeStream(is2);
                closeStream(is2);
                is = is2;
            } catch (Exception e2) {
                e = e2;
                is = is2;
                try {
                    e.printStackTrace();
                    closeStream(is);
                    if (this.mMovie == null) {
                        Log.d("GifView", "GifView setImageResource  >>>>>  movie != null ");
                        setLayerType(1, null);
                        setImageBitmap(null);
                        requestLayout();
                    }
                    Log.d("GifView", "GifView setImageResource  >>>>>  movie == null ");
                    setLayerType(2, null);
                    bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                    str = "GifView";
                    append = new StringBuilder().append("GifView setImageResource  >>>>> decodeFile  bitmap == null  >>> ");
                    if (bitmap != null) {
                        z = false;
                    }
                    Log.d(str, append.append(z).toString());
                    if (bitmap != null) {
                        Log.d("GifView", "GifView  BitmapFactory.decodeFile == null  >>> do not setImageBitmap !  >>>> file.length = " + file.length() + " 字节");
                        return;
                    } else {
                        setImageBitmap(bitmap);
                        return;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    closeStream(is);
                    throw th;
                }
            } catch (Throwable th3) {
                th = th3;
                is = is2;
                closeStream(is);
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            closeStream(is);
            if (this.mMovie == null) {
                Log.d("GifView", "GifView setImageResource  >>>>>  movie == null ");
                setLayerType(2, null);
                bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                str = "GifView";
                append = new StringBuilder().append("GifView setImageResource  >>>>> decodeFile  bitmap == null  >>> ");
                if (bitmap != null) {
                    z = false;
                }
                Log.d(str, append.append(z).toString());
                if (bitmap != null) {
                    setImageBitmap(bitmap);
                    return;
                } else {
                    Log.d("GifView", "GifView  BitmapFactory.decodeFile == null  >>> do not setImageBitmap !  >>>> file.length = " + file.length() + " 字节");
                    return;
                }
            }
            Log.d("GifView", "GifView setImageResource  >>>>>  movie != null ");
            setLayerType(1, null);
            setImageBitmap(null);
            requestLayout();
        }
        if (this.mMovie == null) {
            Log.d("GifView", "GifView setImageResource  >>>>>  movie == null ");
            setLayerType(2, null);
            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            str = "GifView";
            append = new StringBuilder().append("GifView setImageResource  >>>>> decodeFile  bitmap == null  >>> ");
            if (bitmap != null) {
                z = false;
            }
            Log.d(str, append.append(z).toString());
            if (bitmap != null) {
                setImageBitmap(bitmap);
                return;
            } else {
                Log.d("GifView", "GifView  BitmapFactory.decodeFile == null  >>> do not setImageBitmap !  >>>> file.length = " + file.length() + " 字节");
                return;
            }
        }
        Log.d("GifView", "GifView setImageResource  >>>>>  movie != null ");
        setLayerType(1, null);
        setImageBitmap(null);
        requestLayout();
    }

    @Deprecated
    public void setImageBitmap(Bitmap bm) {
        if (bm != null) {
            this.mMovie = null;
        }
        super.setImageBitmap(bm);
    }

    @Deprecated
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    @Deprecated
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
    }

    public Movie getMovie() {
        return this.mMovie;
    }

    public void setMovieTime(int time) {
        this.mCurrentAnimationTime = time;
        invalidate();
    }

    public void setPaused(boolean paused) {
        this.mPaused = paused;
        if (!paused) {
            this.mMovieStart = SystemClock.uptimeMillis() - ((long) this.mCurrentAnimationTime);
        }
        invalidate();
    }

    public boolean isPaused() {
        return this.mPaused;
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mMovie != null) {
            calScale();
            if (this.mPaused) {
                drawMovieFrame(canvas);
                return;
            }
            updateAnimationTime();
            drawMovieFrame(canvas);
            invalidateView();
        }
    }

    private void calScale() {
        if (this.mScaleW != 0.0f && this.mScaleH != 0.0f) {
            return;
        }
        if (getScaleType() == ScaleType.FIT_XY) {
            float movieWidth = (float) this.mMovie.width();
            float movieHeight = (float) this.mMovie.height();
            this.mPaddingLeft = (float) getPaddingLeft();
            this.mPaddingTop = (float) getPaddingTop();
            this.mScaleW = ((((float) getWidth()) - this.mPaddingLeft) - ((float) getPaddingRight())) / movieWidth;
            this.mScaleH = ((((float) getHeight()) - this.mPaddingTop) - ((float) getPaddingBottom())) / movieHeight;
            return;
        }
        this.mScaleH = 1.0f;
        this.mScaleW = 1.0f;
    }

    @SuppressLint({"NewApi"})
    private void invalidateView() {
        if (!this.mVisible) {
            return;
        }
        if (VERSION.SDK_INT >= 16) {
            postInvalidateOnAnimation();
        } else {
            invalidate();
        }
    }

    private void updateAnimationTime() {
        long now = SystemClock.uptimeMillis();
        if (this.mMovieStart == 0) {
            this.mMovieStart = now;
        }
        int dur = this.mMovie.duration();
        if (dur == 0) {
            dur = 1000;
        }
        this.mCurrentAnimationTime = (int) ((now - this.mMovieStart) % ((long) dur));
    }

    private void drawMovieFrame(Canvas canvas) {
        this.mMovie.setTime(this.mCurrentAnimationTime);
        canvas.save(1);
        canvas.translate(this.mPaddingLeft, this.mPaddingTop);
        canvas.scale(this.mScaleW, this.mScaleH);
        this.mMovie.draw(canvas, 0.0f, 0.0f);
        canvas.restore();
    }

    @SuppressLint({"NewApi"})
    public void onScreenStateChanged(int screenState) {
        boolean z = true;
        super.onScreenStateChanged(screenState);
        if (screenState != 1) {
            z = false;
        }
        this.mVisible = z;
        invalidateView();
    }

    @SuppressLint({"NewApi"})
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        this.mVisible = visibility == 0;
        invalidateView();
    }

    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        this.mVisible = visibility == 0;
        invalidateView();
    }
}
