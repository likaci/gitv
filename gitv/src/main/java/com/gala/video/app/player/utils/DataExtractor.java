package com.gala.video.app.player.utils;

import com.gala.sdk.player.data.IVideo;
import com.gala.tvapi.type.ContentType;
import com.gala.tvapi.type.PayMarkType;
import com.gala.video.app.player.albumdetail.data.AlbumInfo;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

public class DataExtractor {
    private static final String TAG = "Player/Ui/DataExtractor";

    private DataExtractor() {
    }

    public static ArrayList<Integer> getTrailerIndicesList(List<IVideo> episodes) {
        ArrayList<Integer> trailers = new ArrayList();
        if (episodes != null && !episodes.isEmpty()) {
            for (IVideo episodeItem : episodes) {
                if (episodeItem == null) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getTrailersList: episodeItem is null");
                    }
                } else if (episodeItem.getAlbum().getContentType() != ContentType.FEATURE_FILM) {
                    if (episodeItem.getAlbum().order > 0) {
                        trailers.add(Integer.valueOf(episodeItem.getAlbum().order - 1));
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getTrailersList: invalid video play order:" + episodeItem);
                    }
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "getTrailerIndicesList, result list=" + trailers);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "getTrailersList: episodes is null or empty");
        }
        return trailers;
    }

    public static ArrayList<Integer> getTrailerIndicesAlbumList(List<AlbumInfo> episodes) {
        ArrayList<Integer> trailers = new ArrayList();
        if (episodes != null && !episodes.isEmpty()) {
            for (AlbumInfo episodeItem : episodes) {
                if (episodeItem == null) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getTrailersList: episodeItem is null");
                    }
                } else if (episodeItem.getAlbum().getContentType() != ContentType.FEATURE_FILM) {
                    if (episodeItem.getAlbum().order > 0) {
                        trailers.add(Integer.valueOf(episodeItem.getAlbum().order - 1));
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getTrailersList: invalid video play order:" + episodeItem);
                    }
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "getTrailerIndicesList, result list=" + trailers);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "getTrailersList: episodes is null or empty");
        }
        return trailers;
    }

    public static HashMap<Integer, PayMarkType> getVipIndicesList(List<IVideo> episodes) {
        HashMap<Integer, PayMarkType> vips = new HashMap();
        if (!ListUtils.isEmpty((List) episodes)) {
            for (IVideo episodeItem : episodes) {
                if (episodeItem == null) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getVipIndicesList: episodeItem is null");
                    }
                } else if (episodeItem.getAlbum().getPayMarkType() == PayMarkType.VIP_MARK || episodeItem.getAlbum().getPayMarkType() == PayMarkType.COUPONS_ON_DEMAND_MARK || episodeItem.getAlbum().getPayMarkType() == PayMarkType.PAY_ON_DEMAND_MARK) {
                    if (episodeItem.getAlbum().order > 0) {
                        vips.put(Integer.valueOf(episodeItem.getAlbum().order - 1), episodeItem.getAlbum().getPayMarkType());
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getVipIndicesList: invalid video play order:" + episodeItem);
                    }
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "getVipIndicesList, result list=" + vips);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "getVipIndicesList: episodes is null or empty");
        }
        return vips;
    }

    public static HashMap<Integer, PayMarkType> getVipIndicesListAlbum(List<AlbumInfo> episodes) {
        HashMap<Integer, PayMarkType> vips = new HashMap();
        if (!ListUtils.isEmpty((List) episodes)) {
            for (AlbumInfo episodeItem : episodes) {
                if (episodeItem == null) {
                    if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getVipIndicesList: episodeItem is null");
                    }
                } else if (episodeItem.getAlbum().getPayMarkType() == PayMarkType.VIP_MARK || episodeItem.getAlbum().getPayMarkType() == PayMarkType.COUPONS_ON_DEMAND_MARK || episodeItem.getAlbum().getPayMarkType() == PayMarkType.PAY_ON_DEMAND_MARK) {
                    if (episodeItem.getAlbum().order > 0) {
                        vips.put(Integer.valueOf(episodeItem.getAlbum().order - 1), episodeItem.getAlbum().getPayMarkType());
                    } else if (LogUtils.mIsDebug) {
                        LogUtils.e(TAG, "getVipIndicesList: invalid video play order:" + episodeItem);
                    }
                }
            }
            if (LogUtils.mIsDebug) {
                LogUtils.d(TAG, "getVipIndicesList, result list=" + vips);
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e(TAG, "getVipIndicesList: episodes is null or empty");
        }
        return vips;
    }

    public static HashMap<Integer, String> getOneWordList(List<IVideo> episodes) {
        if (episodes == null || episodes.isEmpty()) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "getOneWordList episodes is null or empty");
            }
            return null;
        }
        HashMap<Integer, String> oneWordList = new HashMap();
        Iterator it = new ArrayList(episodes).iterator();
        while (it.hasNext()) {
            IVideo episodeItem = (IVideo) it.next();
            if (episodeItem == null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(TAG, "getOneWordList, episodeItem is null");
                }
            } else if (episodeItem.getAlbum().order > 0) {
                oneWordList.put(Integer.valueOf(episodeItem.getAlbum().order - 1), episodeItem.getAlbum().focus);
            } else if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "getOneWordList: invalid video play order:" + episodeItem);
            }
        }
        return oneWordList;
    }

    public static HashMap<Integer, String> getOneWordListAlbum(List<AlbumInfo> episodes) {
        if (episodes == null || episodes.isEmpty()) {
            if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "getOneWordList episodes is null or empty");
            }
            return null;
        }
        HashMap<Integer, String> oneWordList = new HashMap();
        Iterator it = new ArrayList(episodes).iterator();
        while (it.hasNext()) {
            AlbumInfo episodeItem = (AlbumInfo) it.next();
            if (episodeItem == null) {
                if (LogUtils.mIsDebug) {
                    LogUtils.e(TAG, "getOneWordList, episodeItem is null");
                }
            } else if (episodeItem.getAlbum().order > 0) {
                oneWordList.put(Integer.valueOf(episodeItem.getAlbum().order - 1), episodeItem.getAlbum().focus);
            } else if (LogUtils.mIsDebug) {
                LogUtils.e(TAG, "getOneWordList: invalid video play order:" + episodeItem);
            }
        }
        return oneWordList;
    }

    public static List<Integer> getEpisodeNoPlayList(List<IVideo> episodes, int episodeCount) {
        LogUtils.d(TAG, "getEpisodeNoPlayList episodeCount=" + episodeCount);
        if (ListUtils.isEmpty((List) episodes) || episodeCount < 0) {
            LogUtils.d(TAG, "getEpisodeNoPlayList return");
            return null;
        }
        ArrayList<Integer> actualEpisodes = new ArrayList();
        if (episodeCount >= 10000) {
            episodeCount = 10000;
        }
        for (int i = 1; i <= episodeCount; i++) {
            actualEpisodes.add(Integer.valueOf(i));
        }
        LogUtils.d(TAG, "getEpisodeNoPlayList actualEpisodes.size = " + actualEpisodes.size());
        ArrayList<Integer> realEpisodes = new ArrayList();
        for (IVideo episode : new ArrayList(episodes)) {
            if (episode.getAlbum().order > 0) {
                realEpisodes.add(Integer.valueOf(episode.getAlbum().order));
            }
        }
        LogUtils.d(TAG, "getEpisodeNoPlayList realEpisodes.size = " + realEpisodes.size());
        TreeSet<Integer> acturalSet = new TreeSet(actualEpisodes);
        TreeSet<Integer> realSet = new TreeSet(realEpisodes);
        LogUtils.d(TAG, "getEpisodeNoPlayList before CollectionUtils.symDifference");
        TreeSet<Integer> treeSet = symDifference(acturalSet, realSet);
        LogUtils.d(TAG, "getEpisodeNoPlayList after CollectionUtils.symDifference treeSet.size" + treeSet.size());
        return new ArrayList(treeSet);
    }

    public static List<Integer> getEpisodeNoPlayListAlbum(List<AlbumInfo> episodes, int episodeCount) {
        LogUtils.d(TAG, "getEpisodeNoPlayList episodeCount=" + episodeCount);
        if (ListUtils.isEmpty((List) episodes) || episodeCount < 0) {
            LogUtils.d(TAG, "getEpisodeNoPlayList return");
            return null;
        }
        ArrayList<Integer> actualEpisodes = new ArrayList();
        if (episodeCount >= 10000) {
            episodeCount = 10000;
        }
        for (int i = 1; i <= episodeCount; i++) {
            actualEpisodes.add(Integer.valueOf(i));
        }
        LogUtils.d(TAG, "getEpisodeNoPlayList actualEpisodes.size = " + actualEpisodes.size());
        ArrayList<Integer> realEpisodes = new ArrayList();
        for (AlbumInfo episode : new ArrayList(episodes)) {
            if (episode.getAlbum().order > 0) {
                realEpisodes.add(Integer.valueOf(episode.getAlbum().order));
            }
        }
        LogUtils.d(TAG, "getEpisodeNoPlayList realEpisodes.size = " + realEpisodes.size());
        TreeSet<Integer> acturalSet = new TreeSet(actualEpisodes);
        TreeSet<Integer> realSet = new TreeSet(realEpisodes);
        LogUtils.d(TAG, "getEpisodeNoPlayList before CollectionUtils.symDifference");
        TreeSet<Integer> treeSet = symDifference(acturalSet, realSet);
        LogUtils.d(TAG, "getEpisodeNoPlayList after CollectionUtils.symDifference treeSet.size" + treeSet.size());
        return new ArrayList(treeSet);
    }

    private static <T> TreeSet<T> symDifference(TreeSet<T> TreeSetA, TreeSet<T> TreeSetB) {
        TreeSet<T> tmpA = new TreeSet(TreeSetA);
        tmpA.addAll(TreeSetB);
        TreeSet<T> tmpB = new TreeSet(TreeSetA);
        tmpB.retainAll(TreeSetB);
        TreeSet<T> tmp = new TreeSet(tmpA);
        tmp.removeAll(tmpB);
        return tmp;
    }
}
