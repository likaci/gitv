package com.gala.video.app.epg.ui.albumlist.factory;

import android.os.Bundle;
import android.text.TextUtils;
import com.gala.albumprovider.logic.set.SetTool;
import com.gala.albumprovider.logic.source.SourceTool;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.albumprovider.model.Tag;
import com.gala.video.app.epg.ui.albumlist.constant.IAlbumConfig;
import com.gala.video.app.epg.ui.albumlist.data.factory.DataInfoProvider;
import com.gala.video.app.epg.ui.albumlist.fragment.AlbumBaseFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.left.ChannelLeftFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.AlbumBaseRightFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.cardview.ChannelNormalCardFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.cardview.ChannelSearchResultCardFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.gridview.ChannelGridViewFragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.recommend.ChannelRecommend1Fragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.recommend.ChannelRecommend2Fragment;
import com.gala.video.app.epg.ui.albumlist.fragment.right.recommend.ChannelRecommend3Fragment;
import com.gala.video.app.epg.ui.albumlist.model.AlbumInfoModel;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumInfoFactory;
import com.gala.video.lib.framework.core.utils.StringUtils;

public class AlbumFragmentFactory {
    public static AlbumBaseFragment[] create(String pageType) {
        AlbumBaseFragment[] baseFragments = new AlbumBaseFragment[2];
        if (IAlbumConfig.CHANNEL_API_PAGE.equals(pageType)) {
            return initChannelOpenApi(baseFragments);
        }
        return initChannel(baseFragments);
    }

    public static AlbumBaseRightFragment createChannelRightFragment(AlbumInfoModel infoModel) {
        String dataTagType = infoModel.getDataTagType();
        if (SourceTool.REC_CHANNEL_TAG.equals(dataTagType)) {
            if (DataInfoProvider.isRecommend1Type(infoModel.getChannelId())) {
                return new ChannelRecommend1Fragment();
            }
            if (DataInfoProvider.isRecommend3Type(infoModel.getChannelId())) {
                return new ChannelRecommend3Fragment();
            }
            return new ChannelRecommend2Fragment();
        } else if (SourceTool.CARD_TAG.equals(dataTagType)) {
            return new ChannelNormalCardFragment();
        } else {
            return createGridViewFragment(infoModel);
        }
    }

    public static AlbumBaseRightFragment createChannelRightFragment(AlbumInfoModel infoModel, ChannelLeftFragment lf) {
        AlbumBaseRightFragment fragment;
        String firstLabelLocationTagId = infoModel.getFirstLabelLocationTagId();
        String[] firstMultiLocationTagId = infoModel.getFirstMultiLocationTagId();
        int channelId = infoModel.getChannelId();
        if (!StringUtils.isEmpty(firstMultiLocationTagId) || !TextUtils.isEmpty(firstLabelLocationTagId) || infoModel.isJumpNextByRecTag()) {
            prepareTagCacheLayoutKind(lf, infoModel);
            fragment = createGridViewFragment(infoModel);
        } else if (AlbumInfoFactory.isNewVipChannel(infoModel.getChannelId())) {
            fragment = new ChannelRecommend1Fragment();
        } else if (AlbumInfoFactory.isLiveChannel(infoModel.getChannelId(), infoModel.getPageType())) {
            fragment = new ChannelNormalCardFragment();
        } else if (!infoModel.isHasRecommendTag()) {
            prepareTagCacheLayoutKind(lf, infoModel);
            fragment = createGridViewFragment(infoModel);
        } else if (DataInfoProvider.isRecommend1Type(channelId)) {
            fragment = new ChannelRecommend1Fragment();
        } else if (DataInfoProvider.isRecommend3Type(channelId)) {
            fragment = new ChannelRecommend3Fragment();
        } else if (DataInfoProvider.isRecommend2Type(channelId)) {
            fragment = new ChannelRecommend2Fragment();
        } else {
            prepareTagCacheLayoutKind(lf, infoModel);
            fragment = createGridViewFragment(infoModel);
        }
        Bundle bundle = new Bundle();
        bundle.putBoolean(IAlbumConfig.INTENT_SHOW_CACHE_WITHOUT_DATA, true);
        if (fragment != null) {
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    private static void prepareTagCacheLayoutKind(ChannelLeftFragment lf, AlbumInfoModel infoModel) {
        int channelId = infoModel.getChannelId();
        if (channelId != -1 || !IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD.equals(infoModel.getPageType())) {
            QLayoutKind layoutKind = SetTool.setLayoutKind(String.valueOf(channelId));
            if (lf.getDataApi().getNewTag() != null) {
                lf.getDataApi().getNewTag().setLayout(layoutKind);
            } else {
                lf.resetDataApi(new Tag("", "", "", layoutKind));
            }
        }
    }

    private static AlbumBaseRightFragment createGridViewFragment(AlbumInfoModel infoModel) {
        if (IAlbumConfig.UNIQUE_CHANNEL_SEARCH_RESULT_CARD.equals(infoModel.getPageType())) {
            return new ChannelSearchResultCardFragment();
        }
        return new ChannelGridViewFragment();
    }

    private static AlbumBaseFragment[] initChannelOpenApi(AlbumBaseFragment[] baseFragments) {
        baseFragments[1] = new ChannelGridViewFragment();
        return baseFragments;
    }

    private static AlbumBaseFragment[] initChannel(AlbumBaseFragment[] baseFragments) {
        baseFragments[0] = new ChannelLeftFragment();
        return baseFragments;
    }
}
