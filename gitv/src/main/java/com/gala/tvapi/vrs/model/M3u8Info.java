package com.gala.tvapi.vrs.model;

import com.gala.sdk.player.IMediaProfile;
import com.gala.sdk.plugin.PluginType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class M3u8Info extends Model {
    public static String DOLBY_VISION = IMediaProfile.FEATURE_DOLBY_VISION;
    public static String HDR = "hdr";
    private static final long serialVersionUID = 1;
    public int ad = 0;
    public int adDuration = 0;
    public BossInfo bossInfo;
    public int cid = 0;
    public String clientIp = "";
    public Ctl ctl;
    public List<Vid> dolby_vision;
    public List<Vid> drmt = null;
    public String ds = "";
    public String duration = "";
    public List<Vid> hdr;
    public String head = "";
    public String m3u = "";
    public String m3utx = "";
    public String playInfo = null;
    public String prv = "";
    public String tail = "";
    public int ugc = 0;
    public int vd = 0;
    public String vid = "";
    public List<Vid> vidl = null;
    private List<Vid> vids = new ArrayList();

    class ComparatorVid implements Comparator {
        private /* synthetic */ M3u8Info this$0;

        ComparatorVid() {
        }

        public int compare(Object lhs, Object rhs) {
            return ((Vid) rhs).vd - ((Vid) lhs).vd;
        }
    }

    public Map<String, VipBidInfo> getVipBids(String name) {
        if (!(name == null || name.equals("") || this.ctl == null || this.ctl.vip == null)) {
            List<String> list = this.ctl.vip.types;
            if (list != null && list.size() > 0) {
                for (String str : list) {
                    if (DOLBY_VISION.equals(name)) {
                        if (IMediaProfile.FEATURE_DOLBY_VISION.equals(str)) {
                            return this.ctl.dolby_vision;
                        }
                    } else if (HDR.equals(name) && "hdr".equals(str)) {
                        return this.ctl.hdr;
                    }
                }
            }
        }
        return null;
    }

    public VipBidInfo getVipBidInfo(Map<String, VipBidInfo> bids, String vd) {
        if (bids.containsKey(vd)) {
            return (VipBidInfo) bids.get(vd);
        }
        if (bids.containsKey(PluginType.DEFAULT_TYPE)) {
            return (VipBidInfo) bids.get(PluginType.DEFAULT_TYPE);
        }
        return new VipBidInfo();
    }

    public List<Integer> getVipVids() {
        List<Integer> arrayList = new ArrayList();
        if (this.ctl == null) {
            return arrayList;
        }
        VipBids vipBids = this.ctl.vip;
        if (vipBids == null || vipBids.bids == null || vipBids.bids.size() == 0) {
            return arrayList;
        }
        return vipBids.bids;
    }

    public String getM3utx() {
        return this.m3utx;
    }

    public int getHighestVidFlag() {
        sortVidList();
        if (this.vids.size() > 0) {
            return ((Vid) this.vids.get(0)).vd;
        }
        return 0;
    }

    public int getHighestVidFlag(int level) {
        sortVidList();
        if (this.vids.size() > 0) {
            for (Vid vid : this.vids) {
                if (vid.vd <= level) {
                    return vid.vd;
                }
            }
        }
        return 0;
    }

    public int getLowestVidFlag() {
        sortVidList();
        if (this.vids.size() > 0) {
            return ((Vid) this.vids.get(this.vids.size() - 1)).vd;
        }
        return 0;
    }

    public int getLowestVidFlag(int level) {
        sortVidList();
        if (this.vids.size() > 0) {
            for (int size = this.vids.size() - 1; size >= 0; size--) {
                if (((Vid) this.vids.get(size)).vd >= level) {
                    return ((Vid) this.vids.get(size)).vd;
                }
            }
        }
        return 0;
    }

    public String getVid(int level) {
        if (this.vidl != null && this.vidl.size() > 0) {
            for (Vid vid : this.vidl) {
                if (vid.vd == level) {
                    return vid.vid;
                }
            }
        }
        return null;
    }

    public void sortVidList() {
        if ((this.vids == null || this.vids.size() == 0) && this.vidl != null && this.vidl.size() > 0) {
            for (Vid vid : this.vidl) {
                if (vid.vd <= 16 && vid.vd >= 2) {
                    if (vid.vd == 16) {
                        this.vids.add(vid);
                    } else if (vid.vd == 15) {
                        this.vids.add(vid);
                    } else if (vid.vd == 14) {
                        this.vids.add(vid);
                    } else if (vid.vd == 13) {
                        this.vids.add(vid);
                    } else if (vid.vd == 12) {
                        this.vids.add(vid);
                    } else if (vid.vd == 11) {
                        this.vids.add(vid);
                    } else if (vid.vd == 10) {
                        this.vids.add(vid);
                    } else if (vid.vd == 6) {
                        this.vids.add(vid);
                    } else if (vid.vd == 5) {
                        this.vids.add(vid);
                    } else if (vid.vd == 4) {
                        this.vids.add(vid);
                    } else if (vid.vd == 3) {
                        this.vids.add(vid);
                    } else if (vid.vd == 2) {
                        this.vids.add(vid);
                    }
                }
            }
            Collections.sort(this.vids, new ComparatorVid());
        }
    }

    public String getM3u8Url(int vd) {
        if (this.vidl != null && this.vidl.size() > 0) {
            for (Vid vid : this.vidl) {
                if (vid.vd == vd) {
                    return vid.m3utx;
                }
            }
        }
        return this.m3utx;
    }
}
