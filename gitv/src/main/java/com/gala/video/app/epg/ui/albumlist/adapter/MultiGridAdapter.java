package com.gala.video.app.epg.ui.albumlist.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import com.gala.sdk.player.TipType;
import com.gala.video.albumlist4.widget.RecyclerView.ViewHolder;
import com.gala.video.app.epg.R;
import com.gala.video.app.epg.ui.albumlist.data.factory.DataInfoProvider;
import com.gala.video.app.epg.ui.albumlist.widget.CardView;
import com.gala.video.lib.framework.core.utils.ListUtils;
import com.gala.video.lib.framework.core.utils.LogUtils;
import com.gala.video.lib.share.common.configs.ViewConstant.AlbumViewType;
import com.gala.video.lib.share.common.widget.AlbumView;
import com.gala.video.lib.share.ifmanager.bussnessIF.epg.album.IData;
import com.gala.video.lib.share.utils.ResourceUtil;
import java.util.ArrayList;
import java.util.List;

public class MultiGridAdapter extends BaseGridAdapter<IData> {
    private static final int VIEW_TYPE_ALBUM = 2;
    private static final int VIEW_TYPE_CARD = 1;
    private List<IData> mAlbumDataList;
    private List<IData> mCardDataList;

    public MultiGridAdapter(Context context) {
        super(context);
        this.mCardDataList = new ArrayList();
        this.mAlbumDataList = new ArrayList();
        this.TAG = "EPG/album4/MultiGridAdapter";
    }

    public int getCardDataListSize() {
        int cardCount = this.mCardDataList.size();
        return (cardCount % 2 == 0 || this.mAlbumDataList.size() == 0) ? cardCount : cardCount + 1;
    }

    public int getCardDataList() {
        return this.mCardDataList.size();
    }

    public IData getIData(int position) {
        int cardsize = getCardDataListSize();
        if (position < cardsize) {
            if (position < this.mCardDataList.size()) {
                return (IData) this.mCardDataList.get(position);
            }
            return null;
        } else if (position - cardsize < this.mAlbumDataList.size()) {
            return (IData) this.mAlbumDataList.get(position - cardsize);
        } else {
            return null;
        }
    }

    private void setAlbumViewContent(IData info, AlbumView albumView) {
        if (info != null) {
            String imageUrlByPos = getImageUrlByPos(info);
            albumView.setTag(TAG_KEY_INFO_DATA, info);
            albumView.releaseData();
            setTitleText(albumView);
            if (TextUtils.isEmpty(imageUrlByPos)) {
                setDescAndCorner(albumView);
            } else {
                loadBitmap(albumView, imageUrlByPos);
            }
        }
    }

    private void setCardViewContent(IData info, CardView cardView) {
        if (info != null) {
            String imageUrlByPos = getImageUrlByPos(info);
            cardView.setTag(TAG_KEY_INFO_DATA, info);
            cardView.setViewType(DataInfoProvider.getCardType(info.getAlbum()));
            cardView.setTextData(info);
            if (TextUtils.isEmpty(imageUrlByPos)) {
                cardView.setImageData(info);
            } else {
                loadBitmap(cardView, imageUrlByPos);
            }
        }
    }

    private String getImageUrlByPos(IData info) {
        if (info == null) {
            return null;
        }
        return info.getImageUrl(2);
    }

    protected void setDescAndCorner(AlbumView albumView) {
        if (albumView != null) {
            IData info = (IData) albumView.getTag(TAG_KEY_INFO_DATA);
            if (info != null) {
                albumView.setFilmScore(info.getText(9));
                albumView.setDescLine1Left(info.getText(10));
                albumView.setDescLine1Right(info.getText(11));
                albumView.setCorner(info);
            }
        }
    }

    private void setAlbumDisplayData(String url, final Bitmap netBitmap, Object cookie) {
        if (cookie != null) {
            final AlbumView albumView = (AlbumView) cookie;
            IData info = (IData) albumView.getTag(TAG_KEY_INFO_DATA);
            if (info != null) {
                String rightUrl = info.getImageUrl(2);
                if (url == null || url.equals(rightUrl)) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            MultiGridAdapter.this.setDescAndCorner(albumView);
                            if (netBitmap != null) {
                                albumView.setImageBitmap(netBitmap);
                                albumView.setTag(BaseGridAdapter.TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(false));
                            }
                        }
                    });
                } else if (LogUtils.mIsDebug) {
                    LogUtils.e("--return---current.url=" + url + "---right.url=" + rightUrl);
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.e("info is null");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e("cookie is null");
        }
    }

    private void setCardDisplayData(String url, final Bitmap netBitmap, Object cookie) {
        if (cookie != null) {
            final CardView cardView = (CardView) cookie;
            final IData info = (IData) cardView.getTag(TAG_KEY_INFO_DATA);
            if (info != null) {
                String rightUrl = info.getImageUrl(2);
                if (url == null || url.equals(rightUrl)) {
                    this.mHandler.post(new Runnable() {
                        public void run() {
                            cardView.setImageData(info);
                            if (netBitmap != null) {
                                cardView.setImageBitmap(netBitmap);
                                cardView.setTag(BaseGridAdapter.TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(false));
                            }
                        }
                    });
                } else if (LogUtils.mIsDebug) {
                    LogUtils.e("--return---current.url=" + url + "---right.url=" + rightUrl);
                }
            } else if (LogUtils.mIsDebug) {
                LogUtils.e("info is null");
            }
        } else if (LogUtils.mIsDebug) {
            LogUtils.e("cookie is null");
        }
    }

    public void updateCardData(List<IData> datas) {
        if (!ListUtils.isEmpty((List) datas)) {
            int size = datas.size();
            int oldDataSize = ListUtils.getCount(this.mCardDataList);
            this.mCardDataList.clear();
            this.mCardDataList.addAll(datas);
            if (oldDataSize <= 0 || size < oldDataSize) {
                notifyDataSetChanged();
            } else {
                notifyDataSetAdd();
            }
        }
    }

    public void updateAlbumData(List<IData> datas) {
        if (!ListUtils.isEmpty((List) datas)) {
            int size = datas.size();
            int oldDataSize = ListUtils.getCount(this.mAlbumDataList);
            this.mAlbumDataList.clear();
            this.mAlbumDataList.addAll(datas);
            if (oldDataSize <= 0 || size < oldDataSize) {
                notifyDataSetChanged();
            } else {
                notifyDataSetAdd();
            }
        }
    }

    protected int getItemType(int position) {
        if (position < getCardDataListSize()) {
            return 1;
        }
        return 2;
    }

    protected int getItemNumRows(int position) {
        if (position < getCardDataListSize()) {
            return 2;
        }
        return 4;
    }

    protected void requestBitmapSucc(String url, Bitmap netBitmap, Object cookie) {
        if (cookie instanceof CardView) {
            setCardDisplayData(url, netBitmap, cookie);
        } else {
            setAlbumDisplayData(url, netBitmap, cookie);
        }
    }

    protected void requestBitmapFailed(String url, Object cookie, Exception exception) {
        if (cookie instanceof CardView) {
            setCardDisplayData(url, null, cookie);
        } else {
            setAlbumDisplayData(url, null, cookie);
        }
    }

    private boolean isShowingCardDefault(View convertView) {
        return ((Boolean) ((CardView) convertView).getTag(TAG_KEY_SHOW_DEFAULT)).booleanValue();
    }

    private boolean isShowingAlbumDefault(View convertView) {
        return ((Boolean) ((AlbumView) convertView).getTag(TAG_KEY_SHOW_DEFAULT)).booleanValue();
    }

    protected boolean isShowingDefault(View convertView) {
        if (convertView == null || convertView.getTag(TAG_KEY_SHOW_DEFAULT) == null) {
            return true;
        }
        if (convertView instanceof CardView) {
            return isShowingCardDefault(convertView);
        }
        if (convertView instanceof AlbumView) {
            return isShowingAlbumDefault(convertView);
        }
        return true;
    }

    public void resetList() {
        this.mCardDataList.clear();
        this.mAlbumDataList.clear();
        super.resetList();
    }

    protected int getDataCount() {
        return getCardDataListSize() + this.mAlbumDataList.size();
    }

    protected void showDefaultBitmap(View convertView) {
        if (convertView != null) {
            if (convertView instanceof CardView) {
                CardView albumView = (CardView) convertView;
                albumView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
                albumView.setImageDrawable(getDefaultDrawable());
            } else if (convertView instanceof AlbumView) {
                AlbumView albumView2 = (AlbumView) convertView;
                albumView2.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
                albumView2.setImageDrawable(getDefaultDrawable());
            }
        }
    }

    public void releaseData(View convertView) {
        if (convertView != null) {
            if (convertView instanceof CardView) {
                ((CardView) convertView).releaseData();
            } else if (convertView instanceof AlbumView) {
                ((AlbumView) convertView).releaseData();
            }
        }
    }

    protected View onCreateItemViewHolder(int viewType) {
        if (viewType == 1) {
            View cardView = new CardView(this.mContext.getApplicationContext());
            cardView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
            cardView.setImageDrawable(getDefaultDrawable());
            return cardView;
        }
        View albumView = new AlbumView(this.mContext.getApplicationContext(), AlbumViewType.HORIZONTAL);
        albumView.setTag(TAG_KEY_SHOW_DEFAULT, Boolean.valueOf(true));
        albumView.setImageDrawable(getDefaultDrawable());
        return albumView;
    }

    protected void onBindItemViewHolder(ViewHolder holder, int position, LayoutParams params) {
        IData data = getIData(position);
        int cardCount = this.mCardDataList.size();
        if (holder.getItemViewType() == 1) {
            CardView cardView = holder.itemView;
            if (ListUtils.isEmpty(this.mCardDataList)) {
                cardView.setFocusable(false);
                return;
            }
            cardView.setFocusable(true);
            if (position < cardCount) {
                setCardViewContent(data, cardView);
            } else {
                cardView.setVisibility(4);
                cardView.setFocusable(false);
            }
        } else {
            AlbumView albumView = holder.itemView;
            if (ListUtils.isEmpty(this.mAlbumDataList)) {
                albumView.setFocusable(false);
                return;
            } else {
                albumView.setFocusable(true);
                setAlbumViewContent(data, albumView);
            }
        }
        if (cardCount % 2 == 0 || position != cardCount) {
            holder.itemView.setFocusable(true);
            holder.itemView.setVisibility(0);
            return;
        }
        holder.itemView.setFocusable(false);
        holder.itemView.setVisibility(4);
    }

    public void initLayoutParamsParams(ViewHolder holder, LayoutParams params) {
        if (holder.getItemViewType() == 1) {
            params.width = ResourceUtil.getDimen(R.dimen.dimen_496dp);
            params.height = ResourceUtil.getDimen(R.dimen.dimen_260dp);
            return;
        }
        params.width = ResourceUtil.getDimen(R.dimen.dimen_246dp);
        params.height = ResourceUtil.getPx(TipType.CONCRETE_TYPE_HISTORY);
    }
}
