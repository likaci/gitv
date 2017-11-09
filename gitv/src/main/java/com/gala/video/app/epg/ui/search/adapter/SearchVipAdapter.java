package com.gala.video.app.epg.ui.search.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.gala.imageprovider.ImageProviderApi;
import com.gala.imageprovider.base.IImageCallback;
import com.gala.imageprovider.base.ImageRequest;
import com.gala.tvapi.type.LivePlayingType;
import com.gala.tvapi.vrs.model.ChannelLabel;
import com.gala.video.app.epg.R;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.share.ifmanager.CreateInterfaceTools;
import com.gala.video.lib.share.ifmanager.GetInterfaceTools;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.ICornerProvider;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.livecorner.ILiveCornerFactory.LiveCornerListener;
import java.util.ArrayList;
import java.util.List;

public class SearchVipAdapter extends BaseAdapter {
    private final int SEARCHVIP_NUM = 4;
    private ImageView image;
    private List<ChannelLabel> mAlbumDataList;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private LayoutInflater mInflater;
    private ILiveCornerFactory mLiveCornerFactory;
    private final LiveCornerListener mLiveCornerListener = new LiveCornerListener() {
        public void showBefore() {
            SearchVipAdapter.this.topRightImg.setImageResource(R.drawable.share_corner_notice);
        }

        public void showPlaying() {
            SearchVipAdapter.this.topRightImg.setImageResource(R.drawable.share_corner_living);
        }

        public void showEnd() {
            SearchVipAdapter.this.topRightImg.setImageResource(R.drawable.share_corner_end_living);
        }
    };
    private ImageView rankImg;
    private TextView title;
    private ImageView topLeftImg;
    private ImageView topRightImg;
    private ArrayList<String> vipHotWords = new ArrayList();

    public SearchVipAdapter(Context context, List<ChannelLabel> list) {
        this.mAlbumDataList = list;
        this.mInflater = LayoutInflater.from(context);
        for (int i = 0; i < 4; i++) {
            if (i < ListUtils.getCount((List) list)) {
                this.vipHotWords.add(((ChannelLabel) this.mAlbumDataList.get(i)).albumName);
            }
        }
    }

    @SuppressLint({"ViewHolder", "InflateParams"})
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView != null) {
            view = convertView;
        } else {
            view = this.mInflater.inflate(R.layout.epg_search_vip, null);
        }
        this.image = (ImageView) view.findViewById(R.id.epg_searchVip_img);
        this.topLeftImg = (ImageView) view.findViewById(R.id.epg_searchVip_topLeftImg);
        this.topRightImg = (ImageView) view.findViewById(R.id.epg_searchVip_topRightImg);
        this.title = (TextView) view.findViewById(R.id.epg_searchVip_titleText);
        this.rankImg = (ImageView) view.findViewById(R.id.epg_searchVip_rank);
        if (ListUtils.getCount(this.mAlbumDataList) > position) {
            if (position == 3) {
                this.image.setImageResource(R.drawable.epg_search_vip_entrance);
                this.title.setVisibility(8);
            } else {
                this.image.setImageResource(R.drawable.share_default_image);
                ImageProviderApi.getImageProvider().loadImage(new ImageRequest(getImgUrl(position), this.image), new IImageCallback() {
                    public void onSuccess(ImageRequest imageRequest, final Bitmap bmp) {
                        final ImageView view = (ImageView) imageRequest.getCookie();
                        if (view != null && bmp != null) {
                            SearchVipAdapter.this.mHandler.post(new Runnable() {
                                public void run() {
                                    view.setImageBitmap(bmp);
                                }
                            });
                        }
                    }

                    public void onFailure(ImageRequest arg0, Exception e) {
                    }
                });
                setTopCorner(position);
                switch (position) {
                    case 0:
                        this.rankImg.setBackgroundResource(R.drawable.share_corner_rank_1);
                        break;
                    case 1:
                        this.rankImg.setBackgroundResource(R.drawable.share_corner_rank_2);
                        break;
                    case 2:
                        this.rankImg.setBackgroundResource(R.drawable.share_corner_rank_3);
                        break;
                }
                if (position < ListUtils.getCount(this.vipHotWords)) {
                    this.title.setText((CharSequence) this.vipHotWords.get(position));
                }
            }
        }
        return view;
    }

    public int getCount() {
        int i = ListUtils.getCount(this.mAlbumDataList);
        return i < 4 ? i : 4;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(List<ChannelLabel> list) {
        this.mAlbumDataList = list;
        super.notifyDataSetChanged();
    }

    private String getImgUrl(int position) {
        if (this.mAlbumDataList.get(position) == null) {
            return null;
        }
        ChannelLabel mChannelLabel = (ChannelLabel) this.mAlbumDataList.get(position);
        if (mChannelLabel.imageUrl != null) {
            return mChannelLabel.imageUrl.replaceAll(".jpg", "_260_360.jpg");
        }
        return null;
    }

    public void onDetachedFromWindows() {
        clearLiveCorner();
    }

    private void clearLiveCorner() {
        if (this.mLiveCornerFactory != null) {
            this.mLiveCornerFactory.end();
        }
    }

    private void setTopCorner(int position) {
        ChannelLabel channelLabel = (ChannelLabel) this.mAlbumDataList.get(position);
        ICornerProvider provider = GetInterfaceTools.getCornerProvider();
        if (provider.getCornerInfo(channelLabel, 7)) {
            this.topLeftImg.setImageResource(R.drawable.share_corner_yongquan);
        } else if (provider.getCornerInfo(channelLabel, 1)) {
            this.topLeftImg.setImageResource(R.drawable.share_corner_fufeidianbo);
        } else if (provider.getCornerInfo(channelLabel, 0)) {
            this.topLeftImg.setImageResource(R.drawable.share_corner_vip);
        }
        clearLiveCorner();
        if (!LivePlayingType.DEFAULT.equals(channelLabel.getLivePlayingType())) {
            if (this.mLiveCornerFactory == null) {
                this.mLiveCornerFactory = CreateInterfaceTools.createLiveCornerFactory();
            }
            this.mLiveCornerFactory.start(channelLabel, this.mLiveCornerListener);
        } else if (provider.getCornerInfo(channelLabel, 2)) {
            this.topRightImg.setImageResource(R.drawable.share_corner_dubo);
        } else if (provider.getCornerInfo(channelLabel, 6)) {
            this.topRightImg.setImageResource(R.drawable.share_corner_zhuanti);
        }
    }

    public ArrayList<String> getVipHotWord() {
        return this.vipHotWords;
    }
}
