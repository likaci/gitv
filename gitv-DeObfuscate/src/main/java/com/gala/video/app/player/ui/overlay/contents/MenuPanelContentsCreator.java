package com.gala.video.app.player.ui.overlay.contents;

import android.content.Context;
import com.gala.sdk.player.data.IVideo;
import com.gala.video.app.player.data.ContentsCreatorParams;
import com.gala.video.app.player.data.DetailConstants;
import com.gala.video.app.player.ui.config.style.IPlayerMenuPanelUIStyle;
import com.gala.video.app.player.ui.overlay.panels.MenupanelDataHelper;
import com.gala.video.app.player.utils.DataHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.List;

public class MenuPanelContentsCreator implements IContentsCreator {
    private static final String TAG = "MenuPanelContentsCreator";

    public void createMajorContents(Context context, ContentsCreatorParams params, List<ContentHolder> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createMajorContents, list=" + list);
        }
        if (params != null) {
            list.add(createBitstreamContent(context, params.getmMenuPanelUIStyle()));
            ContentHolder associateHolder = createAssociativeContent(context, params);
            if (associateHolder != null) {
                list.add(associateHolder);
            }
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "<< createMajorContents, list=" + list);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "createMajorContents, params is null.");
        }
    }

    public void createDynamicContents(Context context, ContentsCreatorParams params, List<ContentHolder> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createDynamicContents, list=" + list);
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< createDynamicContents, list=" + list);
        }
    }

    public void createRestContents(Context context, ContentsCreatorParams params, List<ContentHolder> list) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, ">> createRestContents, list=" + list);
        }
        MenupanelDataHelper helper = new MenupanelDataHelper();
        IVideo video = params.getVideo();
        IPlayerMenuPanelUIStyle uiStyle = params.getmMenuPanelUIStyle();
        if (helper.needSkipHeaderTailer(video)) {
            list.add(createSkipContent(context, uiStyle));
        }
        if (helper.needScreenRatio(video)) {
            list.add(createScreenRatioContent(context, uiStyle));
        }
        if (helper.need2DTo3D(video)) {
            list.add(create3DContent(context, uiStyle));
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "<< createRestContents, list=" + list);
        }
    }

    private ContentHolder createBitstreamContent(Context context, IPlayerMenuPanelUIStyle uiStyle) {
        return new ContentHolder(DetailConstants.CONTENT_TAG_BITSTREAM, 8, new BitStreamContent(context, uiStyle, DetailConstants.CONTENT_TITLE_BITSTREAM));
    }

    public ContentHolder createAssociativeContent(Context context, ContentsCreatorParams params) {
        IVideo video = params.getVideo();
        MenupanelDataHelper helper = new MenupanelDataHelper();
        if (helper.needEpisode(video)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createContents, create episode content.");
            }
            return new ContentHolder(DetailConstants.CONTENT_TAG_EPISODE, 1, new EpisodeListContent(context, params.getEpisodeUIStyle(), DetailConstants.CONTENT_TITLE_EPISODE_FOR_PLAYER, false));
        } else if (helper.needPrograms(video)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createContents, create program content.");
            }
            return new ContentHolder(DetailConstants.CONTENT_TAG_PROGRAM, 2, new GalleryListContent(context, params.getGalleryUIStyle(), DetailConstants.CONTENT_TITLE_PROGRAMS, true, false, false));
        } else if (helper.needBodan(video)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createContents, create bodan content.");
            }
            Context context2 = context;
            return new ContentHolder(DetailConstants.CONTENT_TAG_BODAN, 7, new GalleryListContent(context2, params.getGalleryUIStyle(), DetailConstants.CONTENT_TITLE_PROGRAMS, true, false, DataHelper.isBodanOrDailyNews(video)));
        } else if (helper.needTrailers(video)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createContents, create trailers content.");
            }
            return new ContentHolder(DetailConstants.CONTENT_TAG_BODAN, 7, new GalleryListContent(context, params.getGalleryUIStyle(), DetailConstants.CONTENT_TITLE_TRAILERS, true, false, true));
        } else if (helper.needRelated(video)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createContents, create related content.");
            }
            return new ContentHolder(DetailConstants.CONTENT_TAG_BODAN, 7, new GalleryListContent(context, params.getGalleryUIStyle(), DetailConstants.CONTENT_TITLE_RELATED, true, false, true));
        } else if (helper.needRecommend(video)) {
            if (LogUtils.mIsDebug) {
                LogUtils.m1568d(TAG, "createContents, create recommend content.");
            }
            return new ContentHolder(DetailConstants.CONTENT_TAG_RECOMMEND, 3, new GalleryListContent(context, params.getGalleryUIStyle(), DetailConstants.CONTENT_TITLE_RECOMMEND_MENUPANEL, true, false, true));
        } else if (!LogUtils.mIsDebug) {
            return null;
        } else {
            LogUtils.m1568d(TAG, "createContents, NO associative content created!!!");
            return null;
        }
    }

    private ContentHolder createSkipContent(Context context, IPlayerMenuPanelUIStyle uiStyle) {
        return new ContentHolder(DetailConstants.CONTENT_TAG_SKIPHEADER, 9, new SkipHeadTailContent(context, uiStyle, DetailConstants.CONTENT_TITLE_SKIPHEADERTAILER));
    }

    private ContentHolder createScreenRatioContent(Context context, IPlayerMenuPanelUIStyle uiStyle) {
        return new ContentHolder(DetailConstants.CONTENT_TAG_SCREENRATIO, 10, new ScreenRatioContent(context, uiStyle, DetailConstants.CONTENT_TITLE_SCREENRATIO));
    }

    private ContentHolder create3DContent(Context context, IPlayerMenuPanelUIStyle uiStyle) {
        return new ContentHolder(DetailConstants.CONTENT_TAG_2DTO3D, 11, new Support2Dto3DContent(context, uiStyle, DetailConstants.CONTENT_TITLE_2DTO3D));
    }
}
