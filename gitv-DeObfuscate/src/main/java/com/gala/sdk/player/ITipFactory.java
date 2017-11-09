package com.gala.sdk.player;

import android.graphics.drawable.Drawable;

public interface ITipFactory {
    ITip createTip(int i, int i2, CharSequence charSequence, Drawable drawable, Runnable runnable, int i3, int i4);

    ITip createTip(int i, int i2, CharSequence charSequence, Drawable drawable, Runnable runnable, boolean z, int i3, int i4);

    ITip createTip(int i, CharSequence charSequence, Drawable drawable, Runnable runnable, int i2, int i3);

    ITip createTip(CharSequence charSequence, Drawable drawable, Runnable runnable, int i, int i2);
}
