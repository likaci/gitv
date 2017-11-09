package com.gala.video.app.player.ui.config;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import com.gala.video.app.player.R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.dynamic.IDynamicResult;
import java.io.File;
import java.util.List;
import java.util.TreeMap;

public class LoadingViewUiConfig4Normal implements ILoadingViewUiConfig {
    private static final String TAG = "Player/Config4LoadingView";
    private Context mContext;

    public LoadingViewUiConfig4Normal(Context context) {
        this.mContext = context;
    }

    private TreeMap<String, String> getLoadingFrameAnimPath() {
        Exception e;
        TreeMap<String, String> treeMap = null;
        IDynamicResult dataModel = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (dataModel != null) {
            List playNewUrlList = dataModel.getPlayNewUrl();
        } else {
            List<String> playNewUrlList2 = null;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getLoadingFrameAnimPath: playNewUrlList=" + playNewUrlList);
        }
        if (!ListUtils.isEmpty(playNewUrlList)) {
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "getLoadingFrameAnimPath: play new url not NULL");
            }
            List<String> pathList = dataModel.getPlayNewUrl();
            if (!ListUtils.isEmpty((List) pathList)) {
                CharSequence imagePath = (String) pathList.get(0);
                try {
                    if (!StringUtils.isEmpty(imagePath) && imagePath.contains("-")) {
                        TreeMap<String, String> treeMap2 = new TreeMap();
                        try {
                            for (String path : pathList) {
                                String[] name = getPictureName(path);
                                treeMap2.put(name[name.length - 2], path);
                            }
                            if (LogUtils.mIsDebug) {
                                LogUtils.d(TAG, "getLoadingFrameAnimPath listsize=" + pathList.size());
                            }
                            treeMap = treeMap2;
                        } catch (Exception e2) {
                            e = e2;
                            treeMap = treeMap2;
                            if (LogUtils.mIsDebug) {
                                LogUtils.e(TAG, "getLoadingFrameAnimPath: exception happened", e);
                            }
                            return treeMap;
                        }
                    }
                } catch (Exception e3) {
                    e = e3;
                    if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getLoadingFrameAnimPath: exception happened", e);
                    }
                    return treeMap;
                }
            } else if (!LogUtils.mIsDebug) {
                return null;
            } else {
                LogUtils.d(TAG, "getLoadingFrameAnimPath: pathList is null");
                return null;
            }
        }
        return treeMap;
    }

    private String[] getPictureName(String imagePath) {
        String[] pathArray = imagePath.split("/");
        return pathArray[pathArray.length - 1].split("_")[0].split("-");
    }

    private Bitmap decodeFile(String cachePath) {
        Bitmap loadingImage = null;
        if (!StringUtils.isEmpty((CharSequence) cachePath)) {
            File cacheFile = new File(cachePath);
            if (cacheFile.exists()) {
                loadingImage = BitmapFactory.decodeFile(cacheFile.getAbsolutePath());
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "decodeFile loadingImage=" + loadingImage);
        }
        return loadingImage;
    }

    public void checkLoadingFrameAnim() {
        TreeMap<String, String> treeMap = getLoadingFrameAnimPath();
        LoadingViewAnimManager.getInstance(this.mContext).setAnimPath(treeMap);
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getLoadingFrameAnim() treeMap=" + treeMap);
        }
    }

    private BitmapDrawable getBimapDrawable(Bitmap bitmap) {
        if (bitmap != null) {
            return new BitmapDrawable(this.mContext.getResources(), bitmap);
        }
        return null;
    }

    private String getLoadingLogoPath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getPlayerLogo()))) {
            List loadinglist = model.getPlayerLogo();
            if (!ListUtils.isEmpty(loadinglist)) {
                path = (String) loadinglist.get(0);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getLoadingLogoPath path=" + path);
        }
        return path;
    }

    public Bitmap getLoadingLogo() {
        return decodeFile(getLoadingLogoPath());
    }

    private String getLoadingBgPath() {
        String path = null;
        IDynamicResult model = GetInterfaceTools.getIDynamicQDataProvider().getDynamicQDataModel();
        if (!(model == null || ListUtils.isEmpty(model.getPlayBackgroundImagePath()))) {
            List loadinglist = model.getPlayBackgroundImagePath();
            if (!ListUtils.isEmpty(loadinglist)) {
                path = (String) loadinglist.get(0);
            }
        }
        if (LogUtils.mIsDebug) {
            LogUtils.d(TAG, "getLoadingBgPath path=" + path);
        }
        return path;
    }

    public void checkLoadingBackground() {
        LoadingViewAnimManager.getInstance(this.mContext).setBackgroundPath(getLoadingBgPath());
    }

    public BitmapDrawable getLoadingBg() {
        return null;
    }

    public int getAnimWidth() {
        return 0;
    }

    public int getAnimHeight() {
        return 0;
    }

    public int getLogoWidth() {
        return getPixelFromDimensIdSafe(R.dimen.loading_logo_width);
    }

    public int getLogoHeight() {
        return getPixelFromDimensIdSafe(R.dimen.loading_logo_height);
    }

    public int getLogoMarginLeft() {
        return getPixelFromDimensIdSafe(R.dimen.loading_logo_marginleft);
    }

    public int getSpeedTxtSize() {
        return getPixelFromDimensIdSafe(R.dimen.loading_speed_txt_size);
    }

    public int getSpeedTxtMargiTop() {
        return getPixelFromDimensIdSafe(R.dimen.loading_speed_txt_margintop);
    }

    public int getTxtNameMarginLeftAndRight() {
        return getPixelFromDimensIdSafe(R.dimen.loading_txt_name_margin_left_and_right);
    }

    public int getTitleImageViewWidth() {
        return 0;
    }

    public int getTitleImageViewHeight() {
        return 0;
    }

    public int getTxtNameSize() {
        return getPixelFromDimensIdSafe(R.dimen.video_play_text_size);
    }

    public int getDeriveWidth() {
        return getPixelFromDimensIdSafe(R.dimen.loading_derive_width);
    }

    public int getDeriveHeight() {
        return getPixelFromDimensIdSafe(R.dimen.loading_derive_height);
    }

    public int getDeriveMarginTop() {
        return getPixelFromDimensIdSafe(R.dimen.loading_derive_margin_top);
    }

    private int getPixelFromDimensIdSafe(int dimenId) {
        if (dimenId != 0) {
            return this.mContext.getResources().getDimensionPixelSize(dimenId);
        }
        return 0;
    }

    public int getAnimMarginTop() {
        return getPixelFromDimensIdSafe(R.dimen.loading_anim_margintop);
    }

    public int getHelpTipSizeFull() {
        return getPixelFromDimensIdSafe(R.dimen.dimen_30dp);
    }

    public int getHelpTipMarginTopFull() {
        return getPixelFromDimensIdSafe(R.dimen.dimen_40dp);
    }

    public int getHelpTipTxtSizeFull() {
        return getPixelFromDimensIdSafe(R.dimen.dimen_20dp);
    }
}
