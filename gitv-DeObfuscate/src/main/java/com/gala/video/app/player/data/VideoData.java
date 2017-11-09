package com.gala.video.app.player.data;

import android.content.Context;
import com.gala.albumprovider.model.QLayoutKind;
import com.gala.sdk.player.SourceType;
import com.gala.sdk.player.data.IVideo;
import com.gala.sdk.player.data.IVideo.VideoKind;
import com.gala.tvapi.tv2.model.Album;
import com.gala.tvapi.type.ResourceType;
import com.gala.video.app.epg.ui.albumlist.utils.AlbumEnterFactory;
import com.gala.video.app.player.ui.GalleryCornerHelper;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils;
import com.gala.video.lib.framework.core.utils.PicSizeUtils.PhotoSize;
import com.gala.video.lib.framework.core.utils.StringUtils;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IAlbumInfoHelper.AlbumKind;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.project.Project;

public class VideoData implements IData<IVideo> {
    private static final String TAG = "Player/detail/VideoData";
    private boolean mIsDetail;
    private boolean mIsPlaying;
    private boolean mIsShowDuboCorner;
    private QLayoutKind mLayout;
    private IVideo mVideo;

    public VideoData(IVideo video, QLayoutKind layout) {
        this.mVideo = video;
        this.mLayout = layout;
    }

    public void click(Context context, Object infoModel) {
    }

    public String getField(int type) {
        switch (type) {
            case 1:
                return this.mVideo.getAlbumId();
            case 2:
                return String.valueOf(this.mVideo.getChannelId());
            case 3:
                return this.mVideo.getTvId();
            case 4:
                return this.mVideo.getEventId();
            case 5:
                return this.mVideo.getTvName();
            default:
                return null;
        }
    }

    public String getImageUrl(int type) {
        return getAlbumImageUrl(this, this.mLayout);
    }

    public String getText(int type) {
        String text = null;
        switch (type) {
            case 3:
                CharSequence albumName = this.mVideo.getAlbum().getAlbumSubName();
                String tvName = this.mVideo.getAlbum().getAlbumSubTvName();
                CharSequence shortName = this.mVideo.getAlbum().shortName;
                boolean isOtherType = !GalleryCornerHelper.isVerticalType(this.mVideo.getAlbum().chnId);
                String titleText;
                if (this.mLayout != QLayoutKind.LANDSCAPE) {
                    if (this.mVideo.getKind() != VideoKind.VIDEO_EPISODE) {
                        if (StringUtils.isEmpty(albumName)) {
                            titleText = tvName;
                        } else {
                            titleText = albumName;
                        }
                        if (titleText == null) {
                            text = "";
                        } else {
                            text = titleText;
                        }
                        break;
                    }
                    CharSequence text2 = albumName;
                    break;
                } else if (this.mVideo.getKind() != VideoKind.ALBUM_EPISODE && this.mVideo.getKind() != VideoKind.ALBUM_SOURCE) {
                    if (this.mVideo.getKind() != VideoKind.VIDEO_EPISODE) {
                        if (this.mVideo.getKind() != VideoKind.VIDEO_SOURCE) {
                            CharSequence titleText2;
                            if (!isOtherType) {
                                if (StringUtils.isEmpty(shortName)) {
                                    titleText2 = albumName;
                                } else {
                                    titleText = tvName;
                                }
                                if (titleText == null) {
                                    text = "";
                                } else {
                                    text = titleText;
                                }
                                break;
                            }
                            if (StringUtils.isEmpty(shortName)) {
                                titleText2 = albumName;
                            } else {
                                titleText = shortName;
                            }
                            if (titleText == null) {
                                text = "";
                            } else {
                                text = titleText;
                            }
                            break;
                        }
                        text = tvName;
                        break;
                    }
                    text = tvName;
                    break;
                } else {
                    if (StringUtils.isEmpty(albumName)) {
                        titleText = tvName;
                    } else {
                        titleText = albumName;
                    }
                    if (titleText == null) {
                        text = "";
                    } else {
                        text = titleText;
                    }
                    break;
                }
                break;
            case 4:
                text = GetInterfaceTools.getCornerProvider().getDescRB(this.mVideo.getAlbum(), this.mLayout);
                break;
            case 5:
                if (this.mVideo.getSourceType() != SourceType.DAILY_NEWS) {
                    if (!this.mVideo.isSourceType()) {
                        if (this.mVideo.getKind() == VideoKind.VIDEO_EPISODE || this.mVideo.getKind() == VideoKind.VIDEO_SINGLE || this.mVideo.getKind() == VideoKind.VIDEO_SOURCE) {
                            text = getRealTime(this.mVideo);
                            break;
                        }
                    }
                    text = StringUtils.isEmpty(this.mVideo.getIssueTime()) ? this.mVideo.getAlbum().getInitIssueTime() : formatDate(this.mVideo.getIssueTime());
                    break;
                }
                if (Project.getInstance().getBuild().isLitchi()) {
                    text = this.mVideo.getAlbum().getInitIssueTimeFormat();
                } else {
                    text = GalleryCornerHelper.getOnlineTime(this.mVideo.getAlbum().getInitIssueTime());
                }
                if (LogUtils.mIsDebug) {
                    LogUtils.m1568d(TAG, "getText text " + text);
                    break;
                }
                break;
            case 7:
                text = GetInterfaceTools.getCornerProvider().getLength(this.mVideo.getAlbum());
                break;
        }
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "getText(" + type + ") return " + text);
        }
        return text;
    }

    private String getRealTime(IVideo video) {
        String issueTime = video.getAlbum().getInitIssueTime();
        if (issueTime.length() == 10) {
            return issueTime;
        }
        return GalleryCornerHelper.getOnlineTime(issueTime);
    }

    private String formatDate(String date) {
        if (LogUtils.mIsDebug) {
            LogUtils.m1568d(TAG, "formatDate(" + date + ")");
        }
        if (StringUtils.isEmpty((CharSequence) date)) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        try {
            String year = date.substring(0, 4);
            String month = date.substring(4, 6);
            sb.append(year).append("-").append(month).append("-").append(date.substring(6, 8));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public boolean getCornerStatus(int type) {
        if ((type == 3 || type == 2) && !getDuboCornerStatus()) {
            return false;
        }
        return GetInterfaceTools.getCornerProvider().getCornerInfo(this.mVideo.getAlbum(), type);
    }

    public ResourceType getResourceType() {
        return null;
    }

    public void setIndexOfCurPage(int indexOfCurPage) {
    }

    public Album getAlbum() {
        return this.mVideo.getAlbum();
    }

    public void setShowingCard(boolean showingCard) {
    }

    public boolean isShowingCard() {
        return false;
    }

    public IVideo getData() {
        return this.mVideo;
    }

    public boolean isPlaying() {
        return this.mIsPlaying;
    }

    public void setPlaying(boolean playing) {
        this.mIsPlaying = playing;
    }

    public void setDuboCornerStatus(boolean show) {
        this.mIsShowDuboCorner = show;
    }

    private boolean getDuboCornerStatus() {
        return this.mIsShowDuboCorner;
    }

    public void setIsDetail(boolean isDetail) {
        this.mIsDetail = isDetail;
    }

    public boolean getIsDetail() {
        return this.mIsDetail;
    }

    public String getAlbumImageUrl(IData info, QLayoutKind layout) {
        IVideo video = (IVideo) info.getData();
        String url;
        switch (layout) {
            case LANDSCAPE:
                url = video.getSourceType() == SourceType.BO_DAN ? StringUtils.isEmpty(video.getAlbum().pic) ? video.getAlbum().tvPic : video.getAlbum().pic : StringUtils.isEmpty(video.getAlbum().tvPic) ? video.getAlbum().pic : video.getAlbum().tvPic;
                LogUtils.m1568d(TAG, "getAlbumImageUrl1 LANDSCAPE url " + url);
                if (this.mIsDetail) {
                    return PicSizeUtils.getUrlWithSize(PhotoSize._480_270, url);
                }
                return PicSizeUtils.getUrlWithSize(PhotoSize._320_180, url);
            default:
                CharSequence url2 = video.getExtraImageUrl();
                if (StringUtils.isEmpty(url2)) {
                    url = StringUtils.isEmpty(video.getAlbum().tvPic) ? video.getAlbum().pic : video.getAlbum().tvPic;
                    if (GetInterfaceTools.getAlbumInfoHelper().getAlbumType(video.getAlbum()) == AlbumKind.SIGLE_VIDEO || !GetInterfaceTools.getAlbumInfoHelper().isSingleType(video.getAlbum())) {
                        LogUtils.m1568d(TAG, "getAlbumImageUrl2 url " + url);
                        return PicSizeUtils.getUrlWithSize(PhotoSize._260_360, url);
                    }
                    LogUtils.m1568d(TAG, "getAlbumImageUrl3 url " + url);
                    return PicSizeUtils.getUrlWithSize(PhotoSize._195_260, url);
                }
                LogUtils.m1568d(TAG, "getAlbumImageUrl1 url " + url2);
                return url2;
        }
    }

    public String toString() {
        return "VideoData[" + hashCode() + ", tvid=" + this.mVideo.getTvId() + AlbumEnterFactory.SIGN_STR;
    }
}
