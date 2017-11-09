package com.gala.albumprovider.base;

public interface IFavouritesAlbumSet {
    void loadDataNewAsync(String str, int i, int i2, IAlbumCallback iAlbumCallback);

    void loadNoLoginDataNewAsync(String str, int i, int i2, IAlbumCallback iAlbumCallback);
}
